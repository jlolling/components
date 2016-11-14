package org.talend.components.azurestorage.helpers;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class RemoteBlobsTable extends ComponentPropertiesImpl {

    public static final String ADD_QUOTES = "ADD_QUOTES"; //$NON-NLS-1$

    protected static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {
    };

    protected static final TypeLiteral<List<Boolean>> LIST_BOOLEAN_TYPE = new TypeLiteral<List<Boolean>>() {
    };

    public Property<List<String>> prefix = newProperty(LIST_STRING_TYPE, "prefix"); //$NON-NLS-1$

    public Property<List<Boolean>> include = newProperty(LIST_BOOLEAN_TYPE, "include"); //$NON-NLS-1$

    public RemoteBlobsTable(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(prefix);
        mainForm.addColumn(include);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        // TODO see why this leads to problems during job generation
        // prefix.setTaggedValue(ADD_QUOTES, true);
    }
}
