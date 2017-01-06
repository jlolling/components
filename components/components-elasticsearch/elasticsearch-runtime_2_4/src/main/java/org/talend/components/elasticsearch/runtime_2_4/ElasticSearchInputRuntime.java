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

import org.apache.avro.generic.IndexedRecord;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.talend.components.adapter.beam.coders.LazyAvroCoder;
import org.talend.components.adapter.beam.transform.ConvertToIndexedRecord;
import org.talend.components.api.component.runtime.RuntimableRuntime;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.elasticsearch.ElasticSearchDatasetProperties;
import org.talend.components.elasticsearch.input.ElasticSearchInputProperties;
import org.talend.daikon.properties.ValidationResult;

public class ElasticSearchInputRuntime extends PTransform<PBegin, PCollection<IndexedRecord>>
        implements RuntimableRuntime<ElasticSearchInputProperties> {

    /**
     * The component instance that this runtime is configured for.
     */
    private ElasticSearchInputProperties properties = null;

    private static String[] resolveAddresses(String nodes) {
        String[] addresses = nodes.split(",");
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = "http://" + addresses[i];
        }
        return addresses;
    }

    protected static ElasticsearchIO.ConnectionConfiguration createConnectionConf(ElasticSearchDatasetProperties dataset) {
        ElasticsearchIO.ConnectionConfiguration connectionConfiguration = ElasticsearchIO.ConnectionConfiguration.create(
                resolveAddresses(dataset.getDatastoreProperties().nodes.getValue()), dataset.index.getValue(),
                dataset.type.getValue());
        if (dataset.getDatastoreProperties().auth.useAuth.getValue()) {
            connectionConfiguration = connectionConfiguration
                    .withUsername(dataset.getDatastoreProperties().auth.userId.getValue())
                    .withPassword(dataset.getDatastoreProperties().auth.password.getValue());
        }
        return connectionConfiguration;
    }

    @Override
    public ValidationResult initialize(RuntimeContainer container, ElasticSearchInputProperties properties) {
        this.properties = properties;
        return ValidationResult.OK;
    }

    @Override
    public PCollection<IndexedRecord> expand(PBegin in) {
        ElasticsearchIO.Read esRead = ElasticsearchIO.read()
                .withConnectionConfiguration(createConnectionConf(properties.getDatasetProperties()));
        if (properties.query.getValue() != null) {
            esRead = esRead.withQuery(properties.query.getValue());
        }
        PCollection<String> readFromElasticSearch = in.apply("ReadFromElasticSearch", esRead);
        //TODO(bchen) String data is a json format, convert json to avro is better than string to avro
        return readFromElasticSearch.apply(ConvertToIndexedRecord.<String, IndexedRecord> of());
    }

    @Override
    public Coder getDefaultOutputCoder() {
        return LazyAvroCoder.of();
    }
}
