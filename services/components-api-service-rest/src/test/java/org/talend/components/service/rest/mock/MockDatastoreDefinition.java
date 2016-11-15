package org.talend.components.service.rest.mock;

import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.common.datastore.DatastoreDefinition;
import org.talend.daikon.SimpleNamedThing;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 * Mock datastore definition.
 */
public class MockDatastoreDefinition extends SimpleNamedThing implements DatastoreDefinition<MockDatastoreProperties> {

    private String name;

    public MockDatastoreDefinition(String name) {
        super("mock "+ name);
        this.name = name;
    }

    @Override
    public MockDatastoreProperties createProperties() {
        return (MockDatastoreProperties) new MockDatastoreProperties("mock "+ name).init();
    }

    @Override
    public RuntimeInfo getRuntimeInfo(MockDatastoreProperties properties, Object ctx) {
        return null;
    }

    @Override
    public DatasetProperties createDatasetProperties(MockDatastoreProperties storeProp) {
        return null;
    }

    @Override
    public String getInputCompDefinitionName() {
        return null;
    }

    @Override
    public String getOutputCompDefinitionName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "mock "+ name +" datastore";
    }

    @Override
    public String getTitle() {
        return "mock "+ name;
    }

    @Override
    public String getImagePath() {
        return "/org/talend/components/mock/mock_"+ name +"_icon32.png";
    }

}
