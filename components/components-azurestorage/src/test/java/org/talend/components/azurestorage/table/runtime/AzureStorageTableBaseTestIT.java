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
package org.talend.components.azurestorage.table.runtime;

import java.util.ArrayList;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageBaseTestIT;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;

public abstract class AzureStorageTableBaseTestIT extends AzureStorageBaseTestIT {

    public String pk_test1 = "partitionKey1";

    public String pk_test2 = "partitionKey2";

    public String pk_test3 = "partitionKey3";

    public String rk_test1 = "rowKey1";

    public String rk_test2 = "rowKey2";

    public String rk_test3 = "rowKey3";

    public String rk_test4 = "rowKey4";

    public String rk_test5 = "rowKey5";

    public String tbl_test = "testTable";

    public String[] partitions = { pk_test1, pk_test2, pk_test3 };

    public String[] rows = { rk_test1, rk_test2, rk_test3 };

    public String[] rows_all = { rk_test1, rk_test2, rk_test3, rk_test4, rk_test5 };

    public AzureStorageTableBaseTestIT(String testName) {
        super(testName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BoundedReader<T> createBoundedReader(ComponentProperties props) {
        AzureStorageTableSource source = new AzureStorageTableSource();
        source.initialize(null, props);
        source.validate(null);
        return source.createReader(null);
    }

    public Schema getDynamicSchema() {
        Schema emptySchema = Schema.createRecord("dynamic", null, null, false);
        emptySchema.setFields(new ArrayList<Schema.Field>());
        emptySchema = AvroUtils.setIncludeAllFields(emptySchema, true);
        return emptySchema;
    }

    public Schema getSystemSchema() {
        return SchemaBuilder.record("Main").fields()
                //
                .name("RowKey").prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true")
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault()
                //
                .name("PartitionKey").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault()
                //
                .name("Timestamp").prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy-MM-dd hh:mm:ss")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "20")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._date()).noDefault()
                //
                .endRecord();
    }
}
