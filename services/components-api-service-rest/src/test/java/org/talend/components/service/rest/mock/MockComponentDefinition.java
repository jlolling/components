package org.talend.components.service.rest.mock;

import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.ComponentImageType;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.fullexample.FullExampleProperties;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 *
 */
public class MockComponentDefinition extends AbstractComponentDefinition {

    private String name;

    public MockComponentDefinition(String name) {
        super("mock "+ name);
        this.name = name;
    }

    @Override
    public String[] getFamilies() {
        return new String[]{"mock"};
    }

    @Override
    public Property[] getReturnProperties() {
        return new Property[0];
    }

    @Override
    public String getPngImagePath(ComponentImageType imageType) {
        return "mock_"+ name +"_icon32.png";
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return FullExampleProperties.class;
    }

    @Override
    public RuntimeInfo getRuntimeInfo(Properties properties, ConnectorTopology compType) {
        return null;
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return Collections.emptySet();
    }

}
