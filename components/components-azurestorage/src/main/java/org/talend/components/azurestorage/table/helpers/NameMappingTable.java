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
package org.talend.components.azurestorage.table.helpers;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class NameMappingTable extends ComponentPropertiesImpl {

    private static final long serialVersionUID = 1978667508342861384L;

    protected static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {
    };

    public Property<List<String>> name = newProperty(LIST_STRING_TYPE, "name"); //$NON-NLS-1$

    public Property<List<String>> placeHolder = newProperty(LIST_STRING_TYPE, "placeHolder"); //$NON-NLS-1$

    public NameMappingTable(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(name);
        mainForm.addColumn(placeHolder);
    }

    public int size() {
        if (name.getValue() != null && !name.getValue().isEmpty())
            return name.getValue().size();
        else
            return 0;
    }
}
