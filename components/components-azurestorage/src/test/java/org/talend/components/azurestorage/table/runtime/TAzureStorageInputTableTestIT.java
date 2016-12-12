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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.avro.generic.IndexedRecord;
import org.junit.Test;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.table.tazurestorageinputtable.TAzureStorageInputTableProperties;

public class TAzureStorageInputTableTestIT extends AzureStorageTableBaseTestIT {

    public TAzureStorageInputTableTestIT() {
        super("tablereader");
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testFirstReader() throws Throwable {
        TAzureStorageInputTableProperties properties = new TAzureStorageInputTableProperties("tests");
        properties = (TAzureStorageInputTableProperties) setupConnectionProperties(
                (AzureStorageProvideConnectionProperties) properties);
        properties.tableName.setValue("customers");
        properties.schema.schema.setValue(null);
        BoundedReader reader = createBoundedReader(properties);
        assertTrue(reader.start());
        while (reader.advance()) {
            IndexedRecord current = (IndexedRecord) reader.getCurrent();
            assertNotNull(current);
        }
        reader.close();
    }

    @SuppressWarnings({ "rawtypes" })
    @Test
    public void testFilterReader() throws Throwable {
        TAzureStorageInputTableProperties properties = new TAzureStorageInputTableProperties("tests");
        properties = (TAzureStorageInputTableProperties) setupConnectionProperties(
                (AzureStorageProvideConnectionProperties) properties);
        properties.tableName.setValue("customers");
        properties.combinedFilter.setValue("(PartitionKey eq 'Harp') and (Timestamp gt datetime'2016-11-29T17:52:38Z')");
        properties.useFilterExpression.setValue(true);
        properties.schema.schema.setValue(getDynamicSchema());
        BoundedReader reader = createBoundedReader(properties);
        assertTrue(reader.start());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testDate = sdf.parse("2016-11-29 17:52:38");
        while (reader.advance()) {
            IndexedRecord current = (IndexedRecord) reader.getCurrent();
            assertNotNull(current);
            assertEquals("Harp", current.get(0));
            assertTrue(((Date) current.get(2)).after(testDate));
        }
        reader.close();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testSystemReader() throws Throwable {
        TAzureStorageInputTableProperties properties = new TAzureStorageInputTableProperties("tests");
        properties = (TAzureStorageInputTableProperties) setupConnectionProperties(
                (AzureStorageProvideConnectionProperties) properties);
        properties.tableName.setValue("customers");
        properties.schema.schema.setValue(getSystemSchema());
        BoundedReader reader = createBoundedReader(properties);
        assertTrue(reader.start());
        while (reader.advance()) {
            IndexedRecord current = (IndexedRecord) reader.getCurrent();
            assertNotNull(current);
        }
        reader.close();
    }
}
