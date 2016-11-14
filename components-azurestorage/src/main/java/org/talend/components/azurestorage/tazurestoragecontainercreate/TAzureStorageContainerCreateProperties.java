package org.talend.components.azurestorage.tazurestoragecontainercreate;

import org.talend.components.azurestorage.AzureStorageProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageContainerCreateProperties extends AzureStorageProperties {

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
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.REFERENCE));
        mainForm.addRow(container);
        mainForm.addRow(accessControl);
        mainForm.addRow(dieOnError);
    }   
    
    @Override
    public void setupProperties() {
        super.setupProperties();
        accessControl.setValue(AccessControl.Private);
    }
}
