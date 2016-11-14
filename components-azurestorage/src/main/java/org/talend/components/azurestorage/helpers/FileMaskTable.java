package org.talend.components.azurestorage.helpers;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class FileMaskTable extends ComponentPropertiesImpl {

    protected static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {
    };

    public Property<List<String>> fileMask = newProperty(LIST_STRING_TYPE, "fileMask"); //$NON-NLS-1$

    public Property<List<String>> newName = newProperty(LIST_STRING_TYPE, "newName"); //$NON-NLS-1$

    public FileMaskTable(String name) {
        super(name);
    }
    
    public int size(){
        return fileMask.getValue().size();
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(fileMask);
        mainForm.addColumn(newName);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
    }
}
