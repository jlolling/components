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
package org.talend.components.azurestorage.queue.tazurestoragequeuecreate;

import org.talend.components.azurestorage.queue.AzureStorageQueueProperties;
import org.talend.daikon.properties.presentation.Form;

public class TAzureStorageQueueCreateProperties extends AzureStorageQueueProperties {

    private static final long serialVersionUID = 5812021288325869860L;

    public TAzureStorageQueueCreateProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.REFERENCE));
        mainForm.addRow(queueName);
        mainForm.addRow(dieOnError);
    }

}
