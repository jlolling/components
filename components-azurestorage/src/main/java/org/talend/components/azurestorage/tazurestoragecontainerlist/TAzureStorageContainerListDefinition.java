package org.talend.components.azurestorage.tazurestoragecontainerlist;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageContainerDefinition;
import org.talend.daikon.properties.property.Property;

/**
 * TODO - Create a palette icon for this new component !!!
 */
public class TAzureStorageContainerListDefinition extends AzureStorageContainerDefinition {

    public static final String COMPONENT_NAME = "tAzureStorageContainerList"; //$NON-NLS-1$

    public TAzureStorageContainerListDefinition() {
        super(COMPONENT_NAME);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_TOTAL_RECORD_COUNT_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageContainerListProperties.class;
    }

    @Override
    public boolean isSchemaAutoPropagate() {
        return true;
    }
}
