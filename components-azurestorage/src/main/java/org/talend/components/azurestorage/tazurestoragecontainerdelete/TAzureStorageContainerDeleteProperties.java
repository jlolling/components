package org.talend.components.azurestorage.tazurestoragecontainerdelete;

import org.talend.components.azurestorage.AzureStorageProperties;
import org.talend.daikon.properties.presentation.Form;

public class TAzureStorageContainerDeleteProperties extends AzureStorageProperties {

    public TAzureStorageContainerDeleteProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(dieOnError);
    }
}
