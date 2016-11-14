package org.talend.components.azurestorage.tazurestoragecontainerexist;

import org.talend.components.azurestorage.AzureStorageProperties;
import org.talend.daikon.properties.presentation.Form;

public class TAzureStorageContainerExistProperties extends AzureStorageProperties {

    public TAzureStorageContainerExistProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(dieOnError);
    }
}
