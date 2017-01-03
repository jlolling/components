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
package org.talend.components.azurestorage.queue.wizard;

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.queue.AzureStorageQueueProperties;
import org.talend.components.azurestorage.queue.runtime.AzureStorageQueueSourceOrSink;
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

public class AzureStorageQueueListProperties extends ComponentPropertiesImpl implements AzureStorageProvideConnectionProperties {

    private static final long serialVersionUID = 7445889946389612503L;

    private TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties("connection");

    private String repositoryLocation;

    private List<NamedThing> queueNames;

    public Property<List<NamedThing>> selectedQueueNames = newProperty(new TypeLiteral<List<NamedThing>>() {
    }, "selectedQueueNames"); //$NON-NLS-1$

    public AzureStorageQueueListProperties(String name) {
        super(name);
        System.out.println("logAQ001");
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return connection;
    }

    public AzureStorageQueueListProperties setConnection(TAzureStorageConnectionProperties connection) {
        this.connection = connection;
        return this;
    }

    public AzureStorageQueueListProperties setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
        return this;
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form queueForm = Form.create(this, Form.MAIN);
        queueForm.addRow(widget(selectedQueueNames).setWidgetType(Widget.NAME_SELECTION_AREA_WIDGET_TYPE));
        refreshLayout(queueForm);
    }

    public void beforeFormPresentMain() throws Exception {
        queueNames = AzureStorageQueueSourceOrSink.getSchemaNames(null, connection);
        selectedQueueNames.setPossibleValues(queueNames);
        getForm(Form.MAIN).setAllowBack(true);
        getForm(Form.MAIN).setAllowForward(true);
        getForm(Form.MAIN).setAllowFinish(true);
    }

    // public ValidationResult afterFormFinishMain(Repository<Properties> repo) throws Exception {
    public ValidationResult afterFormNextMain(Repository<Properties> repo) throws Exception {
        System.out.println("QLP::afterFormFinishMain");
        String connRepLocation = repo.storeProperties(connection, connection.name.getValue(), repositoryLocation, null);

        for (NamedThing nl : selectedQueueNames.getValue()) {
            String queueId = nl.getName();
            AzureStorageQueueProperties queueProps = new AzureStorageQueueProperties(queueId);
            queueProps.connection = connection;
            queueProps.init();
            Schema schema = getSchema();
            queueProps.queueName.setValue(queueId);
            queueProps.schema.schema.setValue(schema);
            repo.storeProperties(queueProps, nl.getName(), connRepLocation, "schema.schema");
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
