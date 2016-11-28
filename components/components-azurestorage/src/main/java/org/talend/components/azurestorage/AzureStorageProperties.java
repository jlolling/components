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
package org.talend.components.azurestorage;

import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public abstract class AzureStorageProperties extends FixedConnectorsComponentProperties
        implements AzureStorageProvideConnectionProperties {

    /** container - the AzureStorage remote container name. */
    public Property<String> container = PropertyFactory.newString("container"); //$NON-NLS-1$

    public TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties("connection"); //$NON-NLS-1$

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    public AzureStorageProperties(String name) {
        super(name);
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return connection.getConnectionProperties();
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        container.setValue("");
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form main = new Form(this, Form.MAIN);
        main.addRow(connection.getForm(Form.REFERENCE));
        main.addRow(container);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        for (Form childForm : connection.getForms()) {
            connection.refreshLayout(childForm);
        }
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(new PropertyPathConnector(Connector.MAIN_NAME, "schema")); //$NON-NLS-1$
        } else {
            return Collections.emptySet();
        }
    }

}
