package org.talend.components.snowflake.tsnowflakeoutput;

import org.apache.avro.Schema;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.ISchemaListener;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.SchemaProperties;
import org.talend.components.snowflake.SnowflakeConnectionTableProperties;
import org.talend.components.snowflake.SnowflakeTableProperties;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;

import java.util.*;

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

public class TSnowflakeOutputProperties extends SnowflakeConnectionTableProperties {

    public enum OutputAction {
        INSERT,
        UPDATE,
        UPSERT,
        DELETE
    }

    public Property<OutputAction> outputAction = newEnum("outputAction", OutputAction.class); // $NON-NLS-1$

    public Property<String> upsertKeyColumn = newString("upsertKeyColumn"); //$NON-NLS-1$

    protected transient PropertyPathConnector FLOW_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schemaFlow");

    protected transient PropertyPathConnector REJECT_CONNECTOR = new PropertyPathConnector(Connector.REJECT_NAME, "schemaReject");

    public SchemaProperties schemaFlow = new SchemaProperties("schemaFlow"); //$NON-NLS-1$

    public SchemaProperties schemaReject = new SchemaProperties("schemaReject"); //$NON-NLS-1$

    // Have to use an explicit class to get the override of afterTableName(), an anonymous
    // class cannot be public and thus cannot be called.
    public class TableSubclass extends SnowflakeTableProperties {

        public TableSubclass(String name) {
            super(name);
        }

        @Override
        public ValidationResult afterTableName() throws Exception {
            ValidationResult validationResult = super.afterTableName();
            if (table.main.schema.getValue() != null) {
                List<String> fieldNames = getFieldNames(table.main.schema);
                upsertKeyColumn.setPossibleValues(fieldNames);
            }
            return validationResult;
        }
    }

    public static final String FIELD_ERROR_CODE = "errorCode";

    public static final String FIELD_ERROR_FIELDS = "errorFields";

    public static final String FIELD_ERROR_MESSAGE = "errorMessage";


    public TSnowflakeOutputProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        outputAction.setValue(OutputAction.INSERT);

        table = new TableSubclass("table");
        table.connection = connection;
        table.setupProperties();

        table.setSchemaListener(new ISchemaListener() {

            @Override
            public void afterSchema() {
                updateOutputSchemas();
                beforeUpsertKeyColumn();
            }
        });
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(outputAction);
        mainForm.addColumn(widget(upsertKeyColumn).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
    }

    public void afterOutputAction() {
        refreshLayout(getForm(Form.MAIN));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (form.getName().equals(Form.MAIN)) {
            Form advForm = getForm(Form.ADVANCED);
            if (advForm != null) {
                boolean isUpsert = OutputAction.UPSERT.equals(outputAction.getValue());
                form.getWidget(upsertKeyColumn.getName()).setHidden(!isUpsert);
                if (isUpsert) {
                    beforeUpsertKeyColumn();
                }
            }
        }
    }

    protected List<String> getFieldNames(Property schema) {
        Schema s = (Schema) schema.getValue();
        List<String> fieldNames = new ArrayList<>();
        for (Schema.Field f : s.getFields()) {
            fieldNames.add(f.name());
        }
        return fieldNames;
    }

    public void beforeUpsertKeyColumn() {
        if (table.main.schema.getValue() != null)
            upsertKeyColumn.setPossibleValues(getFieldNames(table.main.schema));
    }

    private void updateOutputSchemas() {
        Schema inputSchema = table.main.schema.getValue();

        Schema.Field field = null;
        schemaFlow.schema.setValue(inputSchema);

        final List<Schema.Field> additionalRejectFields = new ArrayList<Schema.Field>();

        field = new Schema.Field(FIELD_ERROR_CODE, Schema.create(Schema.Type.STRING), null, (Object) null);
        field.addProp(SchemaConstants.TALEND_IS_LOCKED, "false");
        field.addProp(SchemaConstants.TALEND_FIELD_GENERATED, "true");
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255");
        additionalRejectFields.add(field);

        field = new Schema.Field(FIELD_ERROR_FIELDS, Schema.create(Schema.Type.STRING), null, (Object) null);
        field.addProp(SchemaConstants.TALEND_IS_LOCKED, "false");
        field.addProp(SchemaConstants.TALEND_FIELD_GENERATED, "true");
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255");
        additionalRejectFields.add(field);

        field = new Schema.Field(FIELD_ERROR_MESSAGE, Schema.create(Schema.Type.STRING), null, (Object) null);
        field.addProp(SchemaConstants.TALEND_IS_LOCKED, "false");
        field.addProp(SchemaConstants.TALEND_FIELD_GENERATED, "true");
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255");
        additionalRejectFields.add(field);

        Schema rejectSchema = newSchema(inputSchema, "rejectOutput", additionalRejectFields);

        schemaReject.schema.setValue(rejectSchema);
    }

    private Schema newSchema(Schema metadataSchema, String newSchemaName, List<Schema.Field> moreFields) {
        Schema newSchema = Schema.createRecord(newSchemaName, metadataSchema.getDoc(), metadataSchema.getNamespace(),
                metadataSchema.isError());

        // Deep Copy
        List<Schema.Field> copyFieldList = new ArrayList<>();
        for (Schema.Field se : metadataSchema.getFields()) {
            Schema.Field field = new Schema.Field(se.name(), se.schema(), se.doc(), se.defaultVal(), se.order());
            field.getObjectProps().putAll(se.getObjectProps());
            for (Map.Entry<String, Object> entry : se.getObjectProps().entrySet()) {
                field.addProp(entry.getKey(), entry.getValue());
            }
            copyFieldList.add(field);
        }

        copyFieldList.addAll(moreFields);

        newSchema.setFields(copyFieldList);
        for (Map.Entry<String, Object> entry : metadataSchema.getObjectProps().entrySet()) {
            newSchema.addProp(entry.getKey(), entry.getValue());
        }

        return newSchema;
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        HashSet<PropertyPathConnector> connectors = new HashSet<>();
        if (isOutputConnection) {
            connectors.add(FLOW_CONNECTOR);
            connectors.add(REJECT_CONNECTOR);
        } else {
            connectors.add(MAIN_CONNECTOR);
        }
        return connectors;
    }


}
