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
package org.talend.components.azurestorage.table.wizard;

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.queue.AzureStorageQueueProperties;
import org.talend.components.azurestorage.table.AzureStorageTableProperties;
import org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.service.Repository;

public class AzureStorageTableListProperties extends ComponentPropertiesImpl implements AzureStorageProvideConnectionProperties {

    private static final long serialVersionUID = 7445889946389612503L;

    private TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties("connection");

    private String repositoryLocation;

    private List<NamedThing> tableNames;

    public Property<List<NamedThing>> selectedTableNames = newProperty(new TypeLiteral<List<NamedThing>>() {
    }, "selectedTableNames"); //$NON-NLS-1$

    public AzureStorageTableListProperties(String name) {
        super(name);
        System.out.println("logAT001");
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return connection;
    }

    public AzureStorageTableListProperties setConnection(TAzureStorageConnectionProperties connection) {
        this.connection = connection;
        return this;
    }

    public AzureStorageTableListProperties setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
        return this;
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form tableForm = Form.create(this, Form.MAIN);
        tableForm.addRow(widget(selectedTableNames).setWidgetType(Widget.NAME_SELECTION_AREA_WIDGET_TYPE));
        refreshLayout(tableForm);
    }

    public void beforeFormPresentMain() throws Exception {
        tableNames = AzureStorageTableSourceOrSink.getSchemaNames(null, connection);
        selectedTableNames.setPossibleValues(tableNames);
        getForm(Form.MAIN).setAllowBack(true);
        getForm(Form.MAIN).setAllowFinish(true);
    }

    public ValidationResult afterFormFinishMain(Repository<Properties> repo) throws Exception {
        System.out.println("TLP::afterFormFinishMain");
        String connRepLocation = repo.storeProperties(connection, connection.name.getValue(), repositoryLocation, null);

        System.out.println("connloc" + connRepLocation);

        for (NamedThing nl : selectedTableNames.getValue()) {
            String tableId = nl.getName();
            AzureStorageTableProperties tableProps = new AzureStorageTableProperties(tableId);
            tableProps.init();
            tableProps.connection = connection;
            Schema schema = getSchema();
            tableProps.name.setValue(tableId);
            tableProps.tableName.setValue(tableId);
            tableProps.schema.schema.setValue(schema);
            System.out.println("Adding to repo: " + tableProps);
            repo.storeProperties(tableProps, nl.getName(), connRepLocation, "schema.schema");
        }
        return ValidationResult.OK;
    }

    public Schema getSchema() {

        return SchemaBuilder.builder().record("Main").fields()//
                .name(AzureStorageQueueProperties.FIELD_MESSAGE_ID).prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true")
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "100").type(AvroUtils._string()).noDefault()//
                .name(AzureStorageQueueProperties.FIELD_MESSAGE_CONTENT).type(AvroUtils._string()).noDefault() //
                .name(AzureStorageQueueProperties.FIELD_INSERTION_TIME)
                .prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd hh:mm:ss").type(AvroUtils._date()).noDefault() //
                .name(AzureStorageQueueProperties.FIELD_EXPIRATION_TIME)
                .prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd hh:mm:ss").type(AvroUtils._date()).noDefault() //
                .name(AzureStorageQueueProperties.FIELD_NEXT_VISIBLE_TIME)
                .prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd hh:mm:ss").type(AvroUtils._date()).noDefault() //
                .name(AzureStorageQueueProperties.FIELD_DEQUEUE_COUNT).type(AvroUtils._int()).noDefault() //
                .name(AzureStorageQueueProperties.FIELD_POP_RECEIPT).type(AvroUtils._string()).noDefault() //
                .endRecord();
    }
}
