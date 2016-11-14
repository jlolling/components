package org.talend.components.azurestorage.tazurestoragecontainercreate;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageContainerDefinition;

public class TAzureStorageContainerCreateDefinition extends AzureStorageContainerDefinition {

    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageContainerCreate"; //$NON-NLS-1$

    public TAzureStorageContainerCreateDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageContainerCreateProperties.class;
    }
}
