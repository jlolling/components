package org.talend.components.azurestorage.tazurestoragelist;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.azurestorage.AzureStorageBlobProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;

public class TAzureStorageListProperties extends AzureStorageBlobProperties {

    public TAzureStorageListProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(widget(remoteBlobs).setWidgetType(Widget.TABLE_WIDGET_TYPE));
        mainForm.addRow(schema.getForm(Form.REFERENCE));
        mainForm.addRow(dieOnError);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        Schema s = SchemaBuilder.record("Main").fields().name("BlobName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "300")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault().endRecord();
        schema.schema.setValue(s);
    }

}
