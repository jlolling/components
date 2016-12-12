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
package org.talend.components.azurestorage.table;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.ISchemaListener;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.table.helpers.NameMappingTable;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.SchemaProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public abstract class AzureStorageTableProperties extends FixedConnectorsComponentProperties
        implements AzureStorageProvideConnectionProperties {

    public static final String TABLE_PARTITION_KEY = "PartitionKey";

    public static final String TABLE_ROW_KEY = "RowKey";

    public static final String TABLE_TIMESTAMP = "Timestamp";

    public TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties("connection");

    public Property<String> tableName = PropertyFactory.newString("tableName").setRequired();

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    public NameMappingTable nameMapping = new NameMappingTable("nameMapping");

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schema");

    public ISchemaListener schemaListener;

    public SchemaProperties schema = new SchemaProperties("schema") {

        @SuppressWarnings("unused")
        public void afterSchema() {
            if (schemaListener != null) {
                schemaListener.afterSchema();
            }
        }

    };

    public AzureStorageTableProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form main = new Form(this, Form.MAIN);
        main.addRow(connection.getForm(Form.REFERENCE));
        main.addRow(tableName);
        main.addRow(schema.getForm(Form.REFERENCE));

        Form advancedForm = new Form(this, Form.ADVANCED);
        advancedForm.addRow(widget(nameMapping).setWidgetType(Widget.TABLE_WIDGET_TYPE));
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        dieOnError.setValue(true);
        tableName.setValue("");
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        for (Form childForm : connection.getForms()) {
            connection.refreshLayout(childForm);
        }
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return connection.getConnectionProperties();
    }

    public void setSchemaListener(ISchemaListener schemaListener) {
        this.schemaListener = schemaListener;
    }
}
