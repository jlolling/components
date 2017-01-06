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
import org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import org.talend.components.api.component.runtime.RuntimableRuntime;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.elasticsearch.output.ElasticSearchOutputProperties;
import org.talend.daikon.properties.ValidationResult;

public class ElasticSearchOutputRuntime extends PTransform<PCollection<IndexedRecord>, PDone>
        implements RuntimableRuntime<ElasticSearchOutputProperties> {

    /**
     * The component instance that this runtime is configured for.
     */
    private ElasticSearchOutputProperties properties;

    @Override
    public ValidationResult initialize(RuntimeContainer container, ElasticSearchOutputProperties properties) {
        this.properties = properties;
        return ValidationResult.OK;
    }

    @Override
    public PDone expand(PCollection<IndexedRecord> in) {
        ElasticsearchIO.Write esWrite = ElasticsearchIO.write()
                .withConnectionConfiguration(ElasticSearchInputRuntime.createConnectionConf(properties.getDatasetProperties()));
        return in.apply("formatForElasticSearch", ParDo.of(new FormatESRequest())).apply("writeToElasticSearch", esWrite);
    }

    public static class FormatESRequest extends DoFn<IndexedRecord, String> {

        @DoFn.ProcessElement
        public void processElement(ProcessContext c) {
            IndexedRecord in = c.element();
            c.output(in.toString());
        }
    }
}
