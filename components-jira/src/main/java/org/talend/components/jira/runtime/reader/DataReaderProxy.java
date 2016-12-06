package org.talend.components.jira.runtime.reader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.IndexedRecord;
import org.joda.time.Instant;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.Source;

public class DataReaderProxy implements Reader<IndexedRecord> {
    
    private Reader<IndexedRecord> wrappedReader;
    
    private Map<String, Object> globalMap;
    
    private String componentId;
    
    public DataReaderProxy(Reader<IndexedRecord> wrappedReader, Map<String, Object> globalMap, String componentId) {
        this.wrappedReader = wrappedReader;
        this.globalMap = globalMap;
        this.componentId = componentId;
    }

    @Override
    public boolean start() throws IOException {
        return wrappedReader.start();
    }

    @Override
    public boolean advance() throws IOException {
        return wrappedReader.advance();
    }

    @Override
    public IndexedRecord getCurrent() throws NoSuchElementException {
        IndexedRecord topLevelRecord = wrappedReader.getCurrent();
        Schema topLevelSchema = topLevelRecord.getSchema();
        // check that schema is topLevelSchema. How to do it?
        IndexedRecord dataRecord = (IndexedRecord) topLevelRecord.get(0);
        IndexedRecord outOfBandRecord = (IndexedRecord) topLevelRecord.get(1);
        
        handleOutOfBandData(outOfBandRecord);
        
        return dataRecord;
    }

    private void handleOutOfBandData(IndexedRecord outOfBandRecord) {
        Schema outOfBandSchema = outOfBandRecord.getSchema();
        List<Field> fields = outOfBandSchema.getFields();
        
        for (Field field : fields) {
            String key = field.name();
            int pos = field.pos();
            Object value = outOfBandRecord.get(pos);
            globalMap.put(componentId + "_" + key, value);
        }
        
    }

    @Override
    public Instant getCurrentTimestamp() throws NoSuchElementException {
        return wrappedReader.getCurrentTimestamp();
    }

    @Override
    public void close() throws IOException {
        wrappedReader.close();
    }

    @Override
    public Source getCurrentSource() {
        return wrappedReader.getCurrentSource();
    }

    @Override
    public Map<String, Object> getReturnValues() {
        return wrappedReader.getReturnValues();
    }

}
