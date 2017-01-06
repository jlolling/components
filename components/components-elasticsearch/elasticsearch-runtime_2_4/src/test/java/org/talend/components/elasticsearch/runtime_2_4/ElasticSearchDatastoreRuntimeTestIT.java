package org.talend.components.elasticsearch.runtime_2_4;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.elasticsearch.ElasticSearchDatastoreProperties;
import org.talend.daikon.properties.ValidationResult;

import static org.junit.Assert.assertEquals;

public class ElasticSearchDatastoreRuntimeTestIT {

    ElasticSearchDatastoreRuntime runtime;

    @Before
    public void reset() {
        runtime = new ElasticSearchDatastoreRuntime();
    }

    @Test
    public void doHealthChecksTest() {
        runtime.initialize(null, createDatastoreProp());
        Iterable<ValidationResult> validationResults = runtime.doHealthChecks(null);
        assertEquals(ValidationResult.OK, validationResults.iterator().next());
    }

    public static ElasticSearchDatastoreProperties createDatastoreProp() {
        ElasticSearchDatastoreProperties datastore = new ElasticSearchDatastoreProperties("datastore");
        datastore.nodes.setValue(ElasticSearchTestConstants.HOSTS);
        return datastore;
    }
}
