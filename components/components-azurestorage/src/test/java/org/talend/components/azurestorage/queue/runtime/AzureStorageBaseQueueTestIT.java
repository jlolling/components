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
package org.talend.components.azurestorage.queue.runtime;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageBaseTestIT;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.queue.tazurestoragequeuecreate.TAzureStorageQueueCreateProperties;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueue;

public class AzureStorageBaseQueueTestIT extends AzureStorageBaseTestIT {

    protected static final String TEST_QUEUE_NAME = "test-queue";

    protected static final String TEST_QUEUE_NAME_CREATE = "test-queue-create";

    protected CloudQueue queue;

    protected String[] messages = { "A message to you rudy", "Message in a bottle", "Alert Message" };

    public AzureStorageBaseQueueTestIT(String testName) {
        super(testName);

        try {
            TAzureStorageQueueCreateProperties properties = new TAzureStorageQueueCreateProperties("tests");
            properties = (TAzureStorageQueueCreateProperties) setupConnectionProperties(
                    (AzureStorageProvideConnectionProperties) properties);
            properties.setupProperties();
            properties.queueName.setValue(TEST_QUEUE_NAME);
            AzureStorageQueueSource source = new AzureStorageQueueSource();
            source.initialize(null, properties);
            queue = source.getCloudQueue(null, TEST_QUEUE_NAME);
        } catch (InvalidKeyException | URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BoundedReader<T> createBoundedReader(ComponentProperties props) {
        AzureStorageQueueSource source = new AzureStorageQueueSource();
        source.initialize(null, props);
        source.validate(null);
        return source.createReader(null);
    }

}
