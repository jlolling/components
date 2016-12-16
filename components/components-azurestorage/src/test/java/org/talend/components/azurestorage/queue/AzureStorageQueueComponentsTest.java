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
package org.talend.components.azurestorage.queue;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.talend.components.azurestorage.queue.runtime.AzureStorageQueueSourceOrSink;
import org.talend.components.azurestorage.queue.tazurestoragequeuecreate.TAzureStorageQueueCreateProperties;
import org.talend.components.azurestorage.queue.tazurestoragequeueinput.TAzureStorageQueueInputProperties;
import org.talend.components.azurestorage.queue.tazurestoragequeuelist.TAzureStorageQueueListProperties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;

public class AzureStorageQueueComponentsTest {

    @Test
    public void testQueueNameValidation() {
        ValidationResult vrEmpty = new ValidationResult().setStatus(Result.ERROR).setMessage("Queue name cannot be empty.");
        ValidationResult vrSize = new ValidationResult().setStatus(Result.ERROR)
                .setMessage("Queue name doesn't follow AzureStorage specification length : 3..63 characters long.");
        ValidationResult vrDash = new ValidationResult().setStatus(Result.ERROR)
                .setMessage("Queue name doesn't follow AzureStorage specification : You can't have 2 following dashes.");
        ValidationResult vrName = new ValidationResult().setStatus(Result.ERROR)
                .setMessage("Queue name doesn't follow AzureStorage specification.");
        //
        TAzureStorageQueueCreateProperties properties = new TAzureStorageQueueCreateProperties("test");
        properties.connection.accountName.setValue("dummy");
        properties.connection.accountKey.setValue("dummy");
        properties.setupProperties();
        AzureStorageQueueSourceOrSink sos = new AzureStorageQueueSourceOrSink();
        // empty queue name
        sos.initialize(null, properties);
        assertEquals(vrEmpty.message, sos.validate(null).getMessage());
        // invalid queue size
        properties.queueName.setValue("in");
        sos.initialize(null, properties);
        assertEquals(vrSize.getMessage(), sos.validate(null).getMessage());
        properties.queueName.setValue("a-too-long-queue-name-a-too-long-queue-name-a-too-long-queue-name");
        sos.initialize(null, properties);
        assertEquals(vrSize.getMessage(), sos.validate(null).getMessage());
        // invalid queue name dashes
        properties.queueName.setValue("in--in");
        sos.initialize(null, properties);
        assertEquals(vrDash.getMessage(), sos.validate(null).getMessage());
        // invalid queue name
        properties.queueName.setValue("a-wrongQueueName");
        sos.initialize(null, properties);
        assertEquals(vrName.getMessage(), sos.validate(null).getMessage());
        // a good queue name
        properties.queueName.setValue("a-good-queue-name");
        sos.initialize(null, properties);
        assertEquals(ValidationResult.OK.getStatus(), sos.validate(null).getStatus());
    }

    @Test
    public void testQueueInputProperties() {
        TAzureStorageQueueInputProperties properties = new TAzureStorageQueueInputProperties("test");
        properties.connection.accountName.setValue("dummy");
        properties.connection.accountKey.setValue("dummy");
        properties.setupProperties();
        properties.queueName.setValue("queueok");
        AzureStorageQueueSourceOrSink sos = new AzureStorageQueueSourceOrSink();
        // number of messages
        properties.numberOfMessages.setValue(-1);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.Result.ERROR, sos.validate(null).getStatus());
        properties.numberOfMessages.setValue(0);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.Result.ERROR, sos.validate(null).getStatus());
        properties.numberOfMessages.setValue(1001);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.Result.ERROR, sos.validate(null).getStatus());
        properties.numberOfMessages.setValue(1);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.OK.getStatus(), sos.validate(null).getStatus());
        properties.numberOfMessages.setValue(32);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.OK.getStatus(), sos.validate(null).getStatus());
        // visibility timeout
        properties.visibilityTimeoutInSeconds.setValue(-1);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.Result.ERROR, sos.validate(null).getStatus());
        properties.visibilityTimeoutInSeconds.setValue(1);
        sos.initialize(null, properties);
        assertEquals(ValidationResult.OK.getStatus(), sos.validate(null).getStatus());
    }

    @Test
    public void testQueueListProperties() {
        TAzureStorageQueueListProperties properties = new TAzureStorageQueueListProperties("test");
        properties.connection.accountName.setValue("dummy");
        properties.connection.accountKey.setValue("dummy");
        properties.setupProperties();
        AzureStorageQueueSourceOrSink sos = new AzureStorageQueueSourceOrSink();
        sos.initialize(null, properties);
        assertEquals(ValidationResult.OK.getStatus(), sos.validate(null).getStatus());
    }
}
