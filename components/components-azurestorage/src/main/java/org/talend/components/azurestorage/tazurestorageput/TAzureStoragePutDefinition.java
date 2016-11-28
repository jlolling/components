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
package org.talend.components.azurestorage.tazurestorageput;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageBlobDefinition;
import org.talend.daikon.properties.property.Property;

public class TAzureStoragePutDefinition extends AzureStorageBlobDefinition {

    public static final String COMPONENT_NAME = "tAzureStoragePut"; //$NON-NLS-1$

    public TAzureStoragePutDefinition() {
        super(COMPONENT_NAME);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_NAME_PROP, RETURN_ACCOUNT_KEY_PROP,
                RETURN_CONTAINER_PROP, RETURN_LOCAL_FOLDER_PROP, RETURN_REMOTE_FOLDER_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStoragePutProperties.class;
    }

    @Override
    public Class getPropertiesClass() {
        return TAzureStoragePutProperties.class;
    }

}
