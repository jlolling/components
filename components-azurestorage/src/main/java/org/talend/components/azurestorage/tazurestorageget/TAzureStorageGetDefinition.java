package org.talend.components.azurestorage.tazurestorageget;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageBlobDefinition;
import org.talend.daikon.properties.property.Property;

public class TAzureStorageGetDefinition extends AzureStorageBlobDefinition {

    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageGet"; //$NON-NLS-1$

    public TAzureStorageGetDefinition() {
        super(COMPONENT_NAME);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_NAME_PROP, RETURN_ACCOUNT_KEY_PROP,
                RETURN_CONTAINER_PROP, RETURN_LOCAL_FOLDER_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageGetProperties.class;
    }
}
