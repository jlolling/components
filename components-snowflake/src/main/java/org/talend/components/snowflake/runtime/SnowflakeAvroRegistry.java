package org.talend.components.snowflake.runtime;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.common.avro.JDBCAvroRegistry;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SnowflakeAvroRegistry extends JDBCAvroRegistry {

    private static final SnowflakeAvroRegistry sInstance = new SnowflakeAvroRegistry();

    public static SnowflakeAvroRegistry get() {
        return sInstance;
    }

    @Override
    protected Field sqlType2Avro(int size, int scale, int dbtype, boolean nullable, String name, String dbColumnName,
                                 Object defaultValue) {
        Field field = null;
        Schema schema = null;

        switch (dbtype) {
            case java.sql.Types.VARCHAR:
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.CHAR:
            case java.sql.Types.DATE:
            case java.sql.Types.TIME:
                schema = AvroUtils._string();
                field = wrap(nullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, size);
                break;
            case java.sql.Types.INTEGER:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.BIGINT:
            case java.sql.Types.NUMERIC:
            case java.sql.Types.TINYINT:
            case java.sql.Types.SMALLINT:
                schema = AvroUtils._decimal();
                field = wrap(nullable, schema, name);
                field.addProp(SchemaConstants.TALEND_COLUMN_PRECISION, size);
                field.addProp(SchemaConstants.TALEND_COLUMN_SCALE, scale);
                break;
            case java.sql.Types.DOUBLE:
            case java.sql.Types.FLOAT:
            case java.sql.Types.REAL:
                schema = AvroUtils._double();
                field = wrap(nullable, schema, name);
                break;
            case java.sql.Types.TIMESTAMP:
                schema = AvroUtils._date();
                field = wrap(nullable, schema, name);
                break;
            case java.sql.Types.BOOLEAN:
                schema = AvroUtils._boolean();
                field = wrap(nullable, schema, name);
                break;
            default:
                schema = AvroUtils._string();
                field = wrap(nullable, schema, name);
                break;
        }

        field.addProp(SchemaConstants.TALEND_COLUMN_DB_TYPE, dbtype);
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_COLUMN_NAME, dbColumnName);

        if (defaultValue != null) {
            field.addProp(SchemaConstants.TALEND_COLUMN_DEFAULT, defaultValue);
        }

        return field;
    }

}
