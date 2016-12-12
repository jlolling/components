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
package org.talend.components.azurestorage.blob.runtime;

import java.io.IOException;

import org.apache.avro.generic.IndexedRecord;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.WriterWithFeedback;
import org.talend.components.api.container.RuntimeContainer;

public class AzureStoragePutWriter implements WriterWithFeedback<Result, IndexedRecord, IndexedRecord> {

    RuntimeContainer runtime;

    AzureStorageWriteOperation writeOperation;

    public AzureStoragePutWriter(AzureStorageWriteOperation write, RuntimeContainer adaptor) {
        this.runtime = adaptor;
        this.writeOperation = write;
    }

    @Override
    public void open(String uId) throws IOException {
        writeOperation.getPutSink().getPutProperties();
        // TODO Auto-generated method stub

    }

    @Override
    public void write(Object object) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Result close() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WriteOperation<Result> getWriteOperation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<IndexedRecord> getSuccessfulWrites() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<IndexedRecord> getRejectedWrites() {
        // TODO Auto-generated method stub
        return null;
    }
}
