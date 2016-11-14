package org.talend.components.azurestorage.tazurestoragecontainerlist;

import java.util.Collections;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.AzureStorageProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;

public class TAzureStorageContainerListProperties extends AzureStorageProperties {

    public TAzureStorageContainerListProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        Schema s = SchemaBuilder.record("Main").fields().name("ContainerName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "50")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault().endRecord();
        schema.schema.setValue(s);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(new PropertyPathConnector(Connector.MAIN_NAME, "schema")); //$NON-NLS-1$
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void setupLayout() {
        Form main = new Form(this, Form.MAIN);
        main.addRow(connection.getForm(Form.REFERENCE));
        main.addRow(schema.getForm(Form.REFERENCE));
        main.addRow(dieOnError);
    }
}
