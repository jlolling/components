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
package org.talend.components.azurestorage.blob.tazurestoragecontainercreate;

import org.talend.components.azurestorage.blob.AzureStorageContainerProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageContainerCreateProperties extends AzureStorageContainerProperties {

    public enum AccessControl {
        Private,
        Public
    }

    public Property<AccessControl> accessControl = PropertyFactory.newEnum("accessControl", AccessControl.class); //$NON-NLS-1$

    public TAzureStorageContainerCreateProperties(String name) {
        super(name);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.REFERENCE));
        mainForm.addRow(container);
        mainForm.addRow(accessControl);
        mainForm.addRow(dieOnError);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        container.setRequired(true);
        accessControl.setValue(AccessControl.Private);
    }
}
