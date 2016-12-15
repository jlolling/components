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
package org.talend.components.azurestorage.table.avro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.talend.components.azurestorage.table.runtime.AzureStorageTableBaseTestIT;

import com.microsoft.azure.storage.table.DynamicTableEntity;
import com.microsoft.azure.storage.table.EntityProperty;

public class AzureStorageAvroRegistryTest extends AzureStorageTableBaseTestIT {

    AzureStorageAvroRegistry registry = AzureStorageAvroRegistry.get();

    AzureStorageTableAdaptorFactory recordConv = new AzureStorageTableAdaptorFactory(null);

    public AzureStorageAvroRegistryTest() {
        super("registry-test");
    }

    @Test
    public void testDynamicTableEntityConversion() {

        DynamicTableEntity entity = new DynamicTableEntity();
        entity.setPartitionKey(pk_test1);
        entity.setRowKey(rk_test1);
        entity.getProperties().put("a_bool", new EntityProperty(true));
        entity.getProperties().put("a_int", new EntityProperty(1000));
        entity.getProperties().put("a_string", new EntityProperty(RandomStringUtils.random(10)));

        Schema s = registry.inferSchemaDynamicTableEntity(entity);
        assertEquals(6, s.getFields().size());

        recordConv.setSchema(s);
        IndexedRecord record = recordConv.convertToAvro(entity);
        assertEquals(pk_test1, record.get(0));
        assertEquals(rk_test1, record.get(1));
        assertTrue(record.get(2) instanceof Date);
        //
        assertEquals(true, record.get(s.getField("a_bool").pos()));
        assertEquals(1000, record.get(s.getField("a_int").pos()));
        assertTrue(record.get(s.getField("a_string").pos()) instanceof String);

        // TODO Test the nameMappings
    }
}
