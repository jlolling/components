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

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.IndexedRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.exception.ComponentException;
import org.talend.daikon.avro.converter.AvroConverter;
import org.talend.daikon.avro.converter.IndexedRecordConverter;

import com.microsoft.azure.storage.table.DynamicTableEntity;

public class AzureStorageTableAdaptorFactory implements IndexedRecordConverter<DynamicTableEntity, IndexedRecord> {

    private Schema schema;

    @SuppressWarnings("rawtypes")
    protected transient AvroConverter[] fieldConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageTableAdaptorFactory.class);

    @Override
    public Class<DynamicTableEntity> getDatumClass() {
        return DynamicTableEntity.class;
    }

    @Override
    public DynamicTableEntity convertToDatum(IndexedRecord value) {
        return null;
    }

    @Override
    public AzureStorageTableIndexedRecord convertToAvro(DynamicTableEntity value) {
        return new AzureStorageTableIndexedRecord(value, getSchema());
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public void setSchema(Schema schema) {
        this.schema = schema;
        String[] names;
        names = new String[getSchema().getFields().size()];
        fieldConverter = new AvroConverter[names.length];
        for (int j = 0; j < names.length; j++) {
            Field f = getSchema().getFields().get(j);
            names[j] = f.name();
            AzureStorageAvroRegistry.DTEConverter converter = AzureStorageAvroRegistry.get().getConverter(f);
            fieldConverter[j] = converter;
        }
    }

    private class AzureStorageTableIndexedRecord implements IndexedRecord {

        private Object[] values;

        @SuppressWarnings("unchecked")
        public AzureStorageTableIndexedRecord(DynamicTableEntity record, Schema schema) {
            try {
                values = new Object[schema.getFields().size()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = fieldConverter[i].convertToAvro(record);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new ComponentException(e);
            }
        }

        @Override
        public Schema getSchema() {
            return AzureStorageTableAdaptorFactory.this.getSchema();
        }

        @Override
        public void put(int paramInt, Object paramObject) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(int idx) {
            return values[idx];
        }
    }

}
