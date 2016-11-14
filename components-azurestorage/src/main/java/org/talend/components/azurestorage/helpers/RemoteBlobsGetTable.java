package org.talend.components.azurestorage.helpers;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class RemoteBlobsGetTable extends RemoteBlobsTable {
    
    public Property<List<Boolean>> create = newProperty(LIST_BOOLEAN_TYPE, "create"); //$NON-NLS-1$

    public RemoteBlobsGetTable(String name) {
        super(name);
    }
    
    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(prefix);
        mainForm.addColumn(include);
        mainForm.addColumn(create);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
    }
}
