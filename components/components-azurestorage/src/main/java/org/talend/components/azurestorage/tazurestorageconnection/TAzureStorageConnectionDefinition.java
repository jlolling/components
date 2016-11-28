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
package org.talend.components.azurestorage.tazurestorageconnection;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageDefinition;
import org.talend.components.azurestorage.runtime.AzureStorageSourceOrSink;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 * @author undx
 *
 */
public class TAzureStorageConnectionDefinition extends AzureStorageDefinition {

    public static final String COMPONENT_NAME = "tAzureStorageConnection"; //$NON-NLS-1$

    public TAzureStorageConnectionDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return TAzureStorageConnectionProperties.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getPropertiesClass() {
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
    public RuntimeInfo getRuntimeInfo(ComponentProperties properties, ConnectorTopology connectorTopology) {
        if (connectorTopology == ConnectorTopology.NONE) {
            return getCommonRuntimeInfo(this.getClass().getClassLoader(), AzureStorageSourceOrSink.class);
        } else {
            return null;
        }
    }
}
