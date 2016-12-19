package org.talend.components.jdbc.common;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.Schema.Field;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;

public class SimpleDBTableWithKey {

    public static void createTestTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("create table TEST (ID int, NAME varchar(8), PRIMARY KEY(ID))");
        }
    }

    public static Schema createTestSchema() {
        FieldAssembler<Schema> builder = SchemaBuilder.builder().record("TEST").fields();

        Schema schema = AvroUtils._int();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("ID").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "ID")
                .prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("NAME").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "NAME").type(schema).noDefault();

        return builder.endRecord();
    }

    public static void loadTestData(Connection conn) throws SQLException {
        SimpleDBTable.loadTestData(conn);
    }

    public static void testMetadata(List<Field> columns) {
        SimpleDBTable.testMetadata(columns);

        Schema.Field field = columns.get(0);
        assertEquals("true", field.getObjectProp(SchemaConstants.TALEND_COLUMN_IS_KEY));
    }
}
