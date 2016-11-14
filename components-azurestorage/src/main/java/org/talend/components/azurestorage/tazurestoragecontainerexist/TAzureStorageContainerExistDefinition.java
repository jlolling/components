package org.talend.components.azurestorage.tazurestoragecontainerexist;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageContainerDefinition;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageContainerExistDefinition extends AzureStorageContainerDefinition {
    /**
     * FIXME - Remove "DEV" prefix when OK.
     */
    public static final String COMPONENT_NAME = "DEVtAzureStorageContainerExist"; //$NON-NLS-1$

    public static final String RETURN_CONTAINER_EXIST = "containerExist"; //$NON-NLS-1$

    public static final Property<String> RETURN_CONTAINER_EXIST_PROP = PropertyFactory.newString(RETURN_CONTAINER_EXIST);

    public TAzureStorageContainerExistDefinition() {
        super(COMPONENT_NAME);
        setupI18N(new Property<?>[] { RETURN_CONTAINER_EXIST_PROP });
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_CONTAINER_PROP, RETURN_CONTAINER_EXIST_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageContainerExistProperties.class;
    }
}
