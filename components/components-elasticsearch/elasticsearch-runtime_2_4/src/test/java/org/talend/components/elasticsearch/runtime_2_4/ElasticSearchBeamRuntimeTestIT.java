// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.components.elasticsearch.runtime_2_4;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.avro.generic.IndexedRecord;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.adapter.beam.coders.LazyAvroCoder;
import org.talend.components.adapter.beam.transform.ConvertToIndexedRecord;
import org.talend.components.elasticsearch.ElasticSearchDatasetProperties;
import org.talend.components.elasticsearch.ElasticSearchDatastoreProperties;
import org.talend.components.elasticsearch.input.ElasticSearchInputProperties;
import org.talend.components.elasticsearch.output.ElasticSearchOutputProperties;

import com.google.gson.JsonParser;

public class ElasticSearchBeamRuntimeTestIT implements Serializable {

    public static final String INDEX_NAME = "beam";

    ElasticSearchDatastoreProperties datastoreProperties;

    Client client;

    @Before
    public void init() throws IOException, ExecutionException, InterruptedException {
        client = ElasticSearchTestUtils.createClient(ElasticSearchTestConstants.HOSTS.split(":")[0],
                ElasticSearchTestConstants.TRANSPORT_PORT, ElasticSearchTestConstants.CLUSTER_NAME);

        datastoreProperties = new ElasticSearchDatastoreProperties("datastore");
        datastoreProperties.init();
        datastoreProperties.nodes.setValue(ElasticSearchTestConstants.HOSTS);
        RestClient restClient = ElasticSearchConnection.createClient(datastoreProperties);

        BasicHeader emptyHeader = new BasicHeader("", "");
        Map<String, String> emptyParams = new HashMap<>();

        ElasticSearchTestUtils.deleteIndex(INDEX_NAME, client);

        Response checkExistsResponse = restClient.performRequest("HEAD", "/" + INDEX_NAME, emptyParams);
        ElasticSearchResponse checkExists = new ElasticSearchResponse(checkExistsResponse);
        if (!checkExists.isOk()) {
            // create index for test, name is 'beam'
            restClient.performRequest("PUT", "/" + INDEX_NAME, emptyHeader);
        }
    }

    @Test
    public void basicTest() throws MalformedURLException {
        final String TYPE_NAME = "basictest";

        List<String> records = Arrays.asList("r1", "r2", "r3");

        List<IndexedRecord> avroRecords = new ArrayList<>();
        for (String record : records) {
            avroRecords.add(ConvertToIndexedRecord.convertToAvro(record));
        }

        ElasticSearchDatasetProperties datasetProperties = new ElasticSearchDatasetProperties("datasetProperties");
        datasetProperties.init();
        datasetProperties.setDatastoreProperties(datastoreProperties);
        datasetProperties.index.setValue(INDEX_NAME);
        datasetProperties.type.setValue(TYPE_NAME);

        ElasticSearchOutputProperties outputProperties = new ElasticSearchOutputProperties("outputProperties");
        outputProperties.init();
        outputProperties.setDatasetProperties(datasetProperties);

        Pipeline pipeline = TestPipeline.create();

        ElasticSearchOutputRuntime outputRuntime = new ElasticSearchOutputRuntime();
        outputRuntime.initialize(null, outputProperties);

        PCollection<IndexedRecord> inputRecords = (PCollection<IndexedRecord>) pipeline
                .apply(Create.of(avroRecords).withCoder(LazyAvroCoder.of()));
        inputRecords.apply(outputRuntime);

        pipeline.run();

        ElasticSearchTestUtils.upgradeIndexAndGetCurrentNumDocs(INDEX_NAME, TYPE_NAME, client);

        // input pipeline start
        ElasticSearchInputProperties inputProperties = new ElasticSearchInputProperties("inputProperties");
        inputProperties.init();
        inputProperties.setDatasetProperties(datasetProperties);

        pipeline = TestPipeline.create();

        ElasticSearchInputRuntime inputRuntime = new ElasticSearchInputRuntime();
        inputRuntime.initialize(null, inputProperties);

        PCollection<IndexedRecord> outputRecords = pipeline.apply(inputRuntime);
        PCollection<String> out = outputRecords.apply(MapElements.via(new ExtractJson()));

        PAssert.that(out).containsInAnyOrder(records);
        pipeline.run();

    }

    @Test
    public void filterTest() throws MalformedURLException {
        final String TYPE_NAME = "filtertest";

        List<String> expectedRecords = Arrays.asList("r1", "r2", "r3");
        List<String> records = Arrays.asList("r1", "r2", "r3", "q1", "q2");

        List<IndexedRecord> avroRecords = new ArrayList<>();
        for (String record : records) {
            avroRecords.add(ConvertToIndexedRecord.convertToAvro(record));
        }

        ElasticSearchDatasetProperties datasetProperties = new ElasticSearchDatasetProperties("datasetProperties");
        datasetProperties.init();
        datasetProperties.setDatastoreProperties(datastoreProperties);
        datasetProperties.index.setValue(INDEX_NAME);
        datasetProperties.type.setValue(TYPE_NAME);

        ElasticSearchOutputProperties outputProperties = new ElasticSearchOutputProperties("outputProperties");
        outputProperties.init();
        outputProperties.setDatasetProperties(datasetProperties);

        Pipeline pipeline = TestPipeline.create();

        ElasticSearchOutputRuntime outputRuntime = new ElasticSearchOutputRuntime();
        outputRuntime.initialize(null, outputProperties);

        PCollection<IndexedRecord> inputRecords = (PCollection<IndexedRecord>) pipeline
                .apply(Create.of(avroRecords).withCoder(LazyAvroCoder.of()));
        inputRecords.apply(outputRuntime);

        pipeline.run();

        ElasticSearchTestUtils.upgradeIndexAndGetCurrentNumDocs(INDEX_NAME, TYPE_NAME, client);

        // input pipeline start
        ElasticSearchInputProperties inputProperties = new ElasticSearchInputProperties("inputProperties");
        inputProperties.init();
        inputProperties.setDatasetProperties(datasetProperties);
        inputProperties.query.setValue("{\"query\":{\"regexp\":{\"field\":\"r[1-3]\"}}}");

        pipeline = TestPipeline.create();

        ElasticSearchInputRuntime inputRuntime = new ElasticSearchInputRuntime();
        inputRuntime.initialize(null, inputProperties);

        PCollection<IndexedRecord> outputRecords = pipeline.apply(inputRuntime);
        PCollection<String> out = outputRecords.apply(MapElements.via(new ExtractJson()));

        PAssert.that(out).containsInAnyOrder(expectedRecords);
        pipeline.run();

    }

    public static class ExtractJson extends SimpleFunction<IndexedRecord, String> {

        @Override
        public String apply(IndexedRecord input) {
            JsonParser parser = new JsonParser();
            String content = parser.parse(String.valueOf(input.get(0))).getAsJsonObject().get("field").getAsString();
            return content;
        }
    }
}
