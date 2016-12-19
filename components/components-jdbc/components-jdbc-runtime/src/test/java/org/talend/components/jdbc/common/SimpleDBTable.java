package org.talend.components.jdbc.common;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.Schema.Field;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;

public class SimpleDBTable {

    public static void createTestTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("create table TEST (ID int, NAME varchar(8))");
        }
    }

    public static Schema createTestSchema() {
        FieldAssembler<Schema> builder = SchemaBuilder.builder().record("TEST").fields();

        Schema schema = AvroUtils._int();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("ID").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "ID").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("NAME").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "NAME").type(schema).noDefault();

        return builder.endRecord();
    }

    public static void loadTestData(Connection conn) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("insert into TEST values(?,?)")) {
            statement.setInt(1, 1);
            statement.setString(2, "wangwei");

            statement.executeUpdate();

            statement.setInt(1, 2);
            statement.setString(2, "gaoyan");

            statement.executeUpdate();

            statement.setInt(1, 3);
            statement.setString(2, "dabao");

            statement.executeUpdate();
        }

        if (!conn.getAutoCommit()) {
            conn.commit();
        }
    }

    public static Schema createDesignSchemaWithResultSetOnly() {
        FieldAssembler<Schema> builder = SchemaBuilder.builder().record("TEST").fields();

        Schema schema = AvroUtils._string();// TODO : fix it as should be object type
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("RESULTSET").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "RESULTSET").type(schema)
                .noDefault();

        return builder.endRecord();
    }

    public static Schema createDesignSchemaWithAllColumnsAndResultSet() {
        FieldAssembler<Schema> builder = SchemaBuilder.builder().record("TEST").fields();

        Schema schema = AvroUtils._int();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("ID").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "ID").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("NAME").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "NAME").type(schema).noDefault();

        schema = AvroUtils._string();// TODO : fix it as should be object type
        schema = SchemaUtils.wrap(schema);
        builder = builder.name("RESULTSET").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "RESULTSET").type(schema)
                .noDefault();

        return builder.endRecord();
    }

    public static void testMetadata(List<Field> columns) {
        Schema.Field field = columns.get(0);

        assertEquals("ID", field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME));
        assertEquals(Schema.Type.INT, AvroUtils.unwrapIfNullable(field.schema()).getType());
        assertEquals(java.sql.Types.INTEGER, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_TYPE));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH));
        assertEquals(10, field.getObjectProp(SchemaConstants.TALEND_COLUMN_PRECISION));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_SCALE));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_PATTERN));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DEFAULT));

        field = columns.get(1);

        assertEquals("NAME", field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME));
        assertEquals(Schema.Type.STRING, AvroUtils.unwrapIfNullable(field.schema()).getType());
        assertEquals(java.sql.Types.VARCHAR, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_TYPE));
        assertEquals(8, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_PRECISION));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_SCALE));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_PATTERN));
        assertEquals(null, field.getObjectProp(SchemaConstants.TALEND_COLUMN_DEFAULT));
    }
}
