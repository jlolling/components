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
package org.talend.components.jdbc.common;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.Assert;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.common.avro.JDBCAvroRegistry;
import org.talend.components.jdbc.runtime.JDBCSink;
import org.talend.components.jdbc.runtime.JDBCSource;
import org.talend.components.jdbc.runtime.JdbcRuntimeUtils;
import org.talend.components.jdbc.runtime.setting.AllSetting;
import org.talend.components.jdbc.runtime.writer.JDBCOutputWriter;
import org.talend.components.jdbc.runtime.writer.JDBCRowWriter;
import org.talend.components.jdbc.tjdbcconnection.TJDBCConnectionDefinition;
import org.talend.components.jdbc.tjdbcconnection.TJDBCConnectionProperties;
import org.talend.components.jdbc.tjdbcinput.TJDBCInputDefinition;
import org.talend.components.jdbc.tjdbcinput.TJDBCInputProperties;
import org.talend.components.jdbc.tjdbcoutput.TJDBCOutputDefinition;
import org.talend.components.jdbc.tjdbcoutput.TJDBCOutputProperties;
import org.talend.components.jdbc.tjdbcoutput.TJDBCOutputProperties.DataAction;
import org.talend.components.jdbc.tjdbcrow.TJDBCRowDefinition;
import org.talend.components.jdbc.tjdbcrow.TJDBCRowProperties;
import org.talend.daikon.avro.converter.IndexedRecordConverter;

public class DBTestUtils {

    public static void shutdownDBIfNecessary() {
        // TODO need to shutdown the db or drop the db?

        /*
         * if ("org.apache.derby.jdbc.EmbeddedDriver".equals(driverClass)) {
         * boolean gotSQLExc = false;
         * try {
         * DriverManager.getConnection("jdbc:derby:memory:;drop=true");
         * } catch (SQLException se) {
         * if (se.getSQLState().equals("XJ015")) {
         * gotSQLExc = true;
         * }
         * }
         * if (!gotSQLExc) {
         * System.out.println("Database did not shut down normally");
         * } else {
         * System.out.println("Database shut down normally");
         * }
         * }
         */
    }

    public static void releaseResource(AllSetting allSetting) throws ClassNotFoundException, SQLException {
        try (Connection conn = JdbcRuntimeUtils.createConnection(allSetting)) {
            dropTestTable(conn);
        } finally {
            shutdownDBIfNecessary();
        }
    }

    public static void dropTestTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("drop table TEST");
        }
    }

    public static void truncateTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("delete from TEST");
        }
    }

    @SuppressWarnings("rawtypes")
    public static JDBCOutputWriter createCommonJDBCOutputWriter(TJDBCOutputDefinition definition,
            TJDBCOutputProperties properties, RuntimeContainer container) {
        JDBCSink sink = new JDBCSink();
        sink.initialize(container, properties);

        WriteOperation writerOperation = sink.createWriteOperation();
        writerOperation.initialize(container);
        JDBCOutputWriter writer = (JDBCOutputWriter) writerOperation.createWriter(container);
        return writer;
    }

    public static JDBCOutputWriter createCommonJDBCOutputWriter(TJDBCOutputDefinition definition,
            TJDBCOutputProperties properties) {
        return createCommonJDBCOutputWriter(definition, properties, null);
    }

    @SuppressWarnings("rawtypes")
    public static List<IndexedRecord> fetchDataByReaderFromTable(String tablename, Schema schema, TJDBCInputDefinition definition,
            TJDBCInputProperties properties) {
        List<IndexedRecord> result = new ArrayList<IndexedRecord>();

        properties.main.schema.setValue(schema);
        properties.tableSelection.tablename.setValue(tablename);
        properties.sql.setValue("select * from " + tablename);

        JDBCSource source = new JDBCSource();
        source.initialize(null, properties);

        Reader reader = null;
        try {
            reader = source.createReader(null);

            boolean haveNext = reader.start();

            while (haveNext) {
                IndexedRecord row = (IndexedRecord) reader.getCurrent();
                result.add(copyValueFrom(row));
                haveNext = reader.advance();
            }

            reader.close();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
            }
        }

        return result;
    }

    private static IndexedRecord copyValueFrom(IndexedRecord record) {
        Schema schema = record.getSchema();
        IndexedRecord result = new GenericData.Record(schema);
        List<Schema.Field> fields = schema.getFields();
        for (int i = 0; i < fields.size(); i++) {
            result.put(i, record.get(i));
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    public static Reader createCommonJDBCInputReader(ComponentProperties properties) {
        return createCommonJDBCInputReader(properties, null);
    }

    @SuppressWarnings("rawtypes")
    public static Reader createCommonJDBCInputReader(ComponentProperties properties, RuntimeContainer container) {
        JDBCSource source = createCommonJDBCSource(properties, container);
        return source.createReader(container);
    }

    public static JDBCSource createCommonJDBCSource(ComponentProperties properties) {
        return createCommonJDBCSource(properties, null);
    }

    public static JDBCSource createCommonJDBCSource(ComponentProperties properties, RuntimeContainer container) {
        JDBCSource source = new JDBCSource();
        source.initialize(container, properties);
        return source;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static IndexedRecordConverter<Object, ? extends IndexedRecord> getIndexRecordConverter(Reader reader,
            IndexedRecordConverter<Object, ? extends IndexedRecord> converter) {
        if (converter != null) {
            return converter;
        }

        return (IndexedRecordConverter<Object, ? extends IndexedRecord>) JDBCAvroRegistry.get()
                .createIndexedRecordConverter(reader.getCurrent().getClass());
    }

    public static void assertSuccessRecord(JDBCOutputWriter writer, IndexedRecord r) {
        assertThat(writer.getRejectedWrites(), empty());
        List<IndexedRecord> successfulWrites = writer.getSuccessfulWrites();
        assertThat(successfulWrites, hasSize(1));
        assertThat(successfulWrites.get(0), is(r));
    }

    public static void assertSuccessRecord(JDBCRowWriter writer, IndexedRecord r) {
        assertThat(writer.getRejectedWrites(), empty());
        List<IndexedRecord> successfulWrites = writer.getSuccessfulWrites();
        assertThat(successfulWrites, hasSize(1));
        assertThat(successfulWrites.get(0), is(r));
    }

    public static void assertRejectRecord(JDBCOutputWriter writer) {
        assertThat(writer.getSuccessfulWrites(), empty());

        List<IndexedRecord> rejectRecords = writer.getRejectedWrites();
        assertThat(rejectRecords, hasSize(1));

        for (IndexedRecord rejectRecord : rejectRecords) {
            Assert.assertNotNull(rejectRecord.get(2));
            Assert.assertNotNull(rejectRecord.get(3));
        }
    }

    private static Random random = new Random();

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static int randomInt() {
        return random.nextInt(5) + 1;
    }

    public static DataAction randomDataAction() {
        int value = random.nextInt(5);
        switch (value) {
        case 0:
            return DataAction.INSERT;
        case 1:
            return DataAction.UPDATE;
        case 2:
            return DataAction.DELETE;
        case 3:
            return DataAction.INSERTORUPDATE;
        case 4:
            return DataAction.UPDATEORINSERT;
        default:
            return DataAction.INSERT;
        }
    }

    public static DataAction randomDataActionExceptDelete() {
        int value = random.nextInt(4);
        switch (value) {
        case 0:
            return DataAction.INSERT;
        case 1:
            return DataAction.UPDATE;
        case 2:
            return DataAction.INSERTORUPDATE;
        case 3:
            return DataAction.UPDATEORINSERT;
        default:
            return DataAction.INSERT;
        }
    }

    public static TJDBCConnectionProperties createCommonJDBCConnectionProperties(AllSetting allSetting,
            TJDBCConnectionDefinition connectionDefinition) {
        TJDBCConnectionProperties connectionProperties = (TJDBCConnectionProperties) connectionDefinition
                .createRuntimeProperties();

        // TODO now framework doesn't support to load the JDBC jar by the setting
        // properties.connection.driverJar.setValue(null);
        connectionProperties.connection.driverClass.setValue(allSetting.getDriverClass());
        connectionProperties.connection.jdbcUrl.setValue(allSetting.getJdbcUrl());
        connectionProperties.connection.userPassword.userId.setValue(allSetting.getUsername());
        connectionProperties.connection.userPassword.password.setValue(allSetting.getPassword());
        return connectionProperties;
    }

    public static TJDBCOutputProperties createCommonJDBCOutputProperties(AllSetting allSetting,
            TJDBCOutputDefinition definition) {
        TJDBCOutputProperties properties = (TJDBCOutputProperties) definition.createRuntimeProperties();

        // TODO now framework doesn't support to load the JDBC jar by the setting
        // properties.connection.driverJar.setValue(null);
        properties.connection.driverClass.setValue(allSetting.getDriverClass());
        properties.connection.jdbcUrl.setValue(allSetting.getJdbcUrl());
        properties.connection.userPassword.userId.setValue(allSetting.getUsername());
        properties.connection.userPassword.password.setValue(allSetting.getPassword());
        return properties;
    }

    public static TJDBCInputProperties createCommonJDBCInputProperties(AllSetting allSetting, TJDBCInputDefinition definition) {
        TJDBCInputProperties properties = (TJDBCInputProperties) definition.createRuntimeProperties();

        // TODO now framework doesn't support to load the JDBC jar by the setting
        // properties.connection.driverJar.setValue(null);
        properties.connection.driverClass.setValue(allSetting.getDriverClass());
        properties.connection.jdbcUrl.setValue(allSetting.getJdbcUrl());
        properties.connection.userPassword.userId.setValue(allSetting.getUsername());
        properties.connection.userPassword.password.setValue(allSetting.getPassword());
        return properties;
    }

    public static TJDBCRowProperties createCommonJDBCRowProperties(AllSetting allSetting, TJDBCRowDefinition definition) {
        TJDBCRowProperties properties = (TJDBCRowProperties) definition.createRuntimeProperties();

        // properties.connection.driverTable.drivers.setValue(Arrays.asList(driverPath));
        properties.connection.driverClass.setValue(allSetting.getDriverClass());
        properties.connection.jdbcUrl.setValue(allSetting.getJdbcUrl());
        properties.connection.userPassword.userId.setValue(allSetting.getUsername());
        properties.connection.userPassword.password.setValue(allSetting.getPassword());
        return properties;
    }

    private static java.util.Properties props = null;

    public static AllSetting createAllSetting() throws IOException {
        if (props == null) {
            try (InputStream is = DBTestUtils.class.getClassLoader().getResourceAsStream("connection.properties")) {
                props = new java.util.Properties();
                props.load(is);
            }
        }

        String driverClass = props.getProperty("driverClass");

        String jdbcUrl = props.getProperty("jdbcUrl");

        String userId = props.getProperty("userId");

        String password = props.getProperty("password");

        AllSetting allSetting = new AllSetting();

        allSetting.setDriverClass(driverClass);
        allSetting.setJdbcUrl(jdbcUrl);
        allSetting.setUsername(userId);
        allSetting.setPassword(password);

        return allSetting;
    }

    public static String getTablename() {
        return "TEST";
    }

    public static String getSQL() {
        return "select * from TEST";
    }
}
