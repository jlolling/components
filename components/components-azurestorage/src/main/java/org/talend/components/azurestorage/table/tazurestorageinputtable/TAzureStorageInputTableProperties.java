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
package org.talend.components.azurestorage.table.tazurestorageinputtable;

import static org.talend.daikon.properties.presentation.Widget.widget;

import java.util.Collections;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.table.AzureStorageTableProperties;
import org.talend.components.azurestorage.table.helpers.FilterExpressionTable;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.PresentationItem;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageInputTableProperties extends AzureStorageTableProperties {

    public Property<Boolean> useFilterExpression = PropertyFactory.newBoolean("useFilterExpression");

    public FilterExpressionTable filterExpression = new FilterExpressionTable("filterExpression");

    public Property<String> combinedFilter = PropertyFactory.newString("combinedFilter");

    public TAzureStorageInputTableProperties(String name) {
        super(name);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(MAIN_CONNECTOR);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        useFilterExpression.setValue(false);
        combinedFilter.setValue(
                "(((PartitionKey eq '12345') and (RowKey gt '12345')) or (Timestamp ge datetime'2016-01-01T00:00:00Z'))");
        // default Input schema
        Schema s = SchemaBuilder.record("Main").fields()
                //
                .name("RowKey").prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true")
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault()
                //
                .name("PartitionKey").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault()
                //
                .name("Timestamp").prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd hh:mm:ss")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "20")// $NON-NLS-3$
                .type(AvroUtils._date()).noDefault()
                //
                .endRecord();
        schema.schema.setValue(s);
    }

    public final PresentationItem showNewForm = new PresentationItem("showNewForm", "Show new form");

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(useFilterExpression);
        mainForm.addRow(combinedFilter);
        // FIXME Table doesn't with List<EnumType> and EnumListProperty...
        mainForm.addRow(widget(filterExpression).setWidgetType(Widget.TABLE_WIDGET_TYPE));
        mainForm.getWidget(filterExpression.getName()).setVisible(false);
        //
        mainForm.addRow(dieOnError);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (form.getName() == Form.MAIN) {
            form.getWidget(combinedFilter.getName()).setVisible(useFilterExpression.getValue());
            // FIXME activate when FilterExpressionTable works !
            form.getWidget(filterExpression.getName()).setVisible(useFilterExpression.getValue());
        }
    }

    public void afterUseFilterExpression() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.ADVANCED));
    }
}
