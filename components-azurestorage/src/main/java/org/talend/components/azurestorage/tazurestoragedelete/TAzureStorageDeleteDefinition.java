package org.talend.components.azurestorage.tazurestoragedelete;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageBlobDefinition;
import org.talend.daikon.properties.property.Property;

public class TAzureStorageDeleteDefinition extends AzureStorageBlobDefinition {

    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageDelete"; //$NON-NLS-1$

    public TAzureStorageDeleteDefinition() {
        super(COMPONENT_NAME);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_NAME_PROP, RETURN_ACCOUNT_KEY_PROP,
                RETURN_CONTAINER_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageDeleteProperties.class;
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.OUTGOING);
    }

}
