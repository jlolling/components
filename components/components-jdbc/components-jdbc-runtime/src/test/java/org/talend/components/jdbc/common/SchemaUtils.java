package org.talend.components.jdbc.common;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

class SchemaUtils {

    static Schema wrap(Schema schema) {
        return SchemaBuilder.builder().nullable().type(schema);
    }

    static Schema wrap(Schema schema, boolean nullable) {
        if (nullable) {
            return wrap(schema);
        }

        return schema;
    }

}
