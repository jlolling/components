package org.talend.components.elasticsearch;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.components.elasticsearch.input.ElasticSearchInputDefinition;
import org.talend.components.elasticsearch.output.ElasticSearchOutputDefinition;

public abstract class ElasticSearchComponentTestITBase extends AbstractComponentTest {

    @Inject
    ComponentService componentService;

    @Override
    public ComponentService getComponentService() {
        return componentService;
    }

    @Test
    public void assertComponentsAreRegistered() {
        assertThat(getComponentService().getComponentDefinition(ElasticSearchInputDefinition.NAME), notNullValue());
        assertThat(getComponentService().getComponentDefinition(ElasticSearchOutputDefinition.NAME), notNullValue());
    }
}
