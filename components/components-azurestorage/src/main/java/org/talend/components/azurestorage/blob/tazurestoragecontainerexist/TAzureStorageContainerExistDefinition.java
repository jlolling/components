// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.azurestorage.tazurestoragecontainerexist;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageContainerDefinition;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageContainerExistDefinition extends AzureStorageContainerDefinition {

    public static final String COMPONENT_NAME = "tAzureStorageContainerExist"; //$NON-NLS-1$

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

    @Override
    public Class getPropertiesClass() {
        return TAzureStorageContainerExistProperties.class;
    }
}
