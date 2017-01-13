// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.common;

import java.util.ArrayList;
import java.util.List;

import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;

public class ValuesTrimProperties extends ComponentPropertiesImpl {

    public Property<Boolean> trimAll = newBoolean("trimAll");

    public TrimFieldsTable trimTable = new TrimFieldsTable("trimTable");

    private List<String> fieldNames = new ArrayList<>();

    public ValuesTrimProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(trimAll);
        mainForm.addRow(widget(trimTable).setWidgetType(Widget.TABLE_WIDGET_TYPE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        if (Form.MAIN.equals(form.getName())) {
            form.getWidget(trimTable.getName()).setHidden(trimAll.getValue());
        }
    }

    public void afterTrimAll() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void beforeTrimTable() {
        if (fieldNames != null && fieldNames.size() > 0) {
            trimTable.columnName.setValue(fieldNames);
        }
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
}
