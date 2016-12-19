package org.talend.components.jdbc.common;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;

public class ComplexDBTableWithAllDataType {

    public static void createTestTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute(
                    "CREATE TABLE TEST (C1 INT, C2 SMALLINT, C3 BIGINT, C4 REAL,C5 DOUBLE, C6 FLOAT, C7 DECIMAL(10,2), C8 NUMERIC(10,2), C9 BOOLEAN, C10 CHAR(64), C11 DATE, C12 TIME, C13 TIMESTAMP, C14 VARCHAR(64), C15 LONG VARCHAR)");
        }
    }

    public static Schema createTestSchema(boolean nullableForAnyColumn) {
        FieldAssembler<Schema> builder = SchemaBuilder.builder().record("TEST").fields();

        Schema schema = AvroUtils._int();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C1").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C1")
                .prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true").type(schema).noDefault();

        schema = AvroUtils._short();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C2").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C2").type(schema).noDefault();

        schema = AvroUtils._long();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C3").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C3").type(schema).noDefault();

        schema = AvroUtils._float();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C4").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C4").type(schema).noDefault();

        schema = AvroUtils._double();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C5").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C5").type(schema).noDefault();

        schema = AvroUtils._float();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C6").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C6").type(schema).noDefault();

        schema = AvroUtils._decimal();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C7").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C7").type(schema).noDefault();

        schema = AvroUtils._decimal();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C8").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C8").type(schema).noDefault();

        schema = AvroUtils._boolean();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C9").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C9").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C10").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C10").type(schema).noDefault();

        schema = AvroUtils._date();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C11").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C11").type(schema).noDefault();

        schema = AvroUtils._date();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C12").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C12").type(schema).noDefault();

        schema = AvroUtils._date();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C13").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C13").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C14").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C14").type(schema).noDefault();

        schema = AvroUtils._string();
        schema = SchemaUtils.wrap(schema, nullableForAnyColumn);
        builder = builder.name("C15").prop(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, "C15").type(schema).noDefault();

        return builder.endRecord();
    }

    public static void loadTestData(Connection conn) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("insert into TEST values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            statement.setInt(1, 1);
            statement.setShort(2, (short) 2);
            statement.setLong(3, 3l);
            statement.setFloat(4, 4f);
            statement.setDouble(5, 5d);
            statement.setFloat(6, 6f);
            statement.setBigDecimal(7, new BigDecimal("7.01"));
            statement.setBigDecimal(8, new BigDecimal("8.01"));
            statement.setBoolean(9, true);
            statement.setString(10, "the first char value");
            long currentTimeMillis = System.currentTimeMillis();
            statement.setTimestamp(11, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(12, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(13, new java.sql.Timestamp(currentTimeMillis));
            statement.setString(14, "wangwei");
            statement.setString(15, "a long one : 1");
            statement.executeUpdate();

            statement.setInt(1, 1);
            statement.setShort(2, (short) 2);
            statement.setLong(3, 3l);
            statement.setFloat(4, 4f);
            statement.setDouble(5, 5d);
            statement.setFloat(6, 6f);
            statement.setBigDecimal(7, new BigDecimal("7.01"));
            statement.setBigDecimal(8, new BigDecimal("8.01"));
            statement.setBoolean(9, true);
            statement.setString(10, "the second char value");
            statement.setTimestamp(11, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(12, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(13, new java.sql.Timestamp(currentTimeMillis));
            statement.setString(14, "gaoyan");
            statement.setString(15, "a long one : 2");
            statement.executeUpdate();

            statement.setInt(1, 1);
            statement.setShort(2, (short) 2);
            statement.setLong(3, 3l);
            statement.setFloat(4, 4f);
            statement.setDouble(5, 5d);
            statement.setFloat(6, 6f);
            statement.setBigDecimal(7, new BigDecimal("7.01"));
            statement.setBigDecimal(8, new BigDecimal("8.01"));
            statement.setBoolean(9, true);
            statement.setString(10, "the third char value");
            statement.setTimestamp(11, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(12, new java.sql.Timestamp(currentTimeMillis));
            statement.setTimestamp(13, new java.sql.Timestamp(currentTimeMillis));
            statement.setString(14, "dabao");
            statement.setString(15, "a long one : 3");
            statement.executeUpdate();

            // used by testing the null value
            statement.setInt(1, 1);
            statement.setNull(2, java.sql.Types.SMALLINT);
            statement.setNull(3, java.sql.Types.BIGINT);
            statement.setNull(4, java.sql.Types.FLOAT);
            statement.setNull(5, java.sql.Types.DOUBLE);
            statement.setNull(6, java.sql.Types.FLOAT);
            statement.setNull(7, java.sql.Types.DECIMAL);
            statement.setNull(8, java.sql.Types.DECIMAL);
            statement.setNull(9, java.sql.Types.BOOLEAN);
            statement.setNull(10, java.sql.Types.CHAR);
            statement.setNull(11, java.sql.Types.DATE);
            statement.setNull(12, java.sql.Types.TIME);
            statement.setNull(13, java.sql.Types.TIMESTAMP);
            statement.setNull(14, java.sql.Types.VARCHAR);
            statement.setNull(15, java.sql.Types.LONGVARCHAR);
            statement.executeUpdate();

            statement.setNull(1, java.sql.Types.INTEGER);
            statement.setNull(2, java.sql.Types.SMALLINT);
            statement.setNull(3, java.sql.Types.BIGINT);
            statement.setNull(4, java.sql.Types.FLOAT);
            statement.setNull(5, java.sql.Types.DOUBLE);
            statement.setNull(6, java.sql.Types.FLOAT);
            statement.setNull(7, java.sql.Types.DECIMAL);
            statement.setNull(8, java.sql.Types.DECIMAL);
            statement.setNull(9, java.sql.Types.BOOLEAN);
            statement.setNull(10, java.sql.Types.CHAR);
            statement.setNull(11, java.sql.Types.DATE);
            statement.setNull(12, java.sql.Types.TIME);
            statement.setNull(13, java.sql.Types.TIMESTAMP);
            statement.setString(14, "good luck");
            statement.setNull(15, java.sql.Types.LONGVARCHAR);
            statement.executeUpdate();
        }

        if (!conn.getAutoCommit()) {
            conn.commit();
        }
    }

    public static List<IndexedRecord> prepareIndexRecords(boolean nullableForAnyColumn) {
        List<IndexedRecord> result = new ArrayList<IndexedRecord>();

        Schema schema = createTestSchema(nullableForAnyColumn);

        IndexedRecord r = new GenericData.Record(schema);
        r.put(0, 1);
        r.put(1, (short) 2);
        r.put(2, 3l);
        r.put(3, 4f);
        r.put(4, 5d);
        r.put(5, 6f);
        r.put(6, new BigDecimal("7.01"));
        r.put(7, new BigDecimal("8.01"));
        r.put(8, true);
        r.put(9, "content : 1");
        r.put(10, new java.util.Date());
        r.put(11, new java.util.Date());
        r.put(12, new java.util.Date());
        r.put(13, "wangwei");
        r.put(14, "long content : 1");
        result.add(r);

        r = new GenericData.Record(schema);
        r.put(0, 1);
        r.put(1, (short) 2);
        r.put(2, 3l);
        r.put(3, 4f);
        r.put(4, 5d);
        r.put(5, 6f);
        r.put(6, new BigDecimal("7.01"));
        r.put(7, new BigDecimal("8.01"));
        r.put(8, true);
        r.put(9, "content : 2");
        r.put(10, new java.util.Date());
        r.put(11, new java.util.Date());
        r.put(12, new java.util.Date());
        r.put(13, "gaoyan");
        r.put(14, "long content : 2");
        result.add(r);

        r = new GenericData.Record(schema);
        r.put(0, 1);
        r.put(1, (short) 2);
        r.put(2, 3l);
        r.put(3, 4f);
        r.put(4, 5d);
        r.put(5, 6f);
        r.put(6, new BigDecimal("7.01"));
        r.put(7, new BigDecimal("8.01"));
        r.put(8, true);
        r.put(9, "content : 3");
        r.put(10, new java.util.Date());
        r.put(11, new java.util.Date());
        r.put(12, new java.util.Date());
        r.put(13, "dabao");
        r.put(14, "long content : 3");
        result.add(r);

        // used by testing the null value
        r = new GenericData.Record(schema);
        r.put(0, 1);
        r.put(1, null);
        r.put(2, null);
        r.put(3, null);
        r.put(4, null);
        r.put(5, null);
        r.put(6, null);
        r.put(7, null);
        r.put(8, null);
        r.put(9, null);
        r.put(10, null);
        r.put(11, null);
        r.put(12, null);
        r.put(13, null);
        r.put(14, null);
        result.add(r);

        r = new GenericData.Record(schema);
        r.put(0, null);
        r.put(1, null);
        r.put(2, null);
        r.put(3, null);
        r.put(4, null);
        r.put(5, null);
        r.put(6, null);
        r.put(7, null);
        r.put(8, null);
        r.put(9, null);
        r.put(10, null);
        r.put(11, null);
        r.put(12, null);
        r.put(13, "good luck");
        r.put(14, null);
        result.add(r);

        return result;
    }

}
