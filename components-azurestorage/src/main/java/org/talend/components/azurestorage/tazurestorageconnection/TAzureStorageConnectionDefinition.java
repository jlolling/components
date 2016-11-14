/**
 * 
 */
package org.talend.components.azurestorage.tazurestorageconnection;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageDefinition;
import org.talend.components.azurestorage.runtime.AzureStorageSourceOrSink;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 * @author undx
 *
 */
public class TAzureStorageConnectionDefinition extends AzureStorageDefinition {

    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageConnection"; //$NON-NLS-1$

    public TAzureStorageConnectionDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageConnectionProperties.class;
    }

    /**
     * FIXME - Currently we don't return this values ! Have to create a source and sink class for this ?
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_NAME_PROP, RETURN_ACCOUNT_KEY_PROP };
    }

    @Override
    public RuntimeInfo getRuntimeInfo(Properties properties, ConnectorTopology componentType) {
        if (componentType == ConnectorTopology.NONE) {
            return getCommonRuntimeInfo(this.getClass().getClassLoader(), AzureStorageSourceOrSink.class);
        } else {
            return null;
        }
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.NONE);
    }

    @Override
    public boolean isStartable() {
        return true;
    }
}
