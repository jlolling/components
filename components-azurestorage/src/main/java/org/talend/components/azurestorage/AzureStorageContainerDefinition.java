package org.talend.components.azurestorage;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.azurestorage.runtime.AzureStorageSource;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

public class AzureStorageContainerDefinition extends AzureStorageDefinition {

    public AzureStorageContainerDefinition(String componentName) {
        super(componentName);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_CONTAINER_PROP };
    }

    @Override
    public RuntimeInfo getRuntimeInfo(Properties properties, ConnectorTopology connectorTopology) {
        RuntimeInfo runtimeinfo = getCommonRuntimeInfo(this.getClass().getClassLoader(), AzureStorageSource.class);
        return runtimeinfo;
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.OUTGOING);
    }
}
