package org.talend.components.azurestorage.tazurestoragecontainerdelete;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageContainerDefinition;

public class TAzureStorageContainerDeleteDefinition extends AzureStorageContainerDefinition {

    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageContainerDelete"; //$NON-NLS-1$

    public TAzureStorageContainerDeleteDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageContainerDeleteProperties.class;
    }

}
