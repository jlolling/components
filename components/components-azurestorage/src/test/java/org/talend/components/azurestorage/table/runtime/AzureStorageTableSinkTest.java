// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.azurestorage.table.tazurestorageoutputtable.TAzureStorageOutputTableProperties;
import org.talend.daikon.properties.ValidationResult;

import com.microsoft.azure.storage.StorageException;

public class AzureStorageTableSinkTest {

    AzureStorageTableSink sink;

    @Before
    public void setUp() throws Exception {
        sink = new AzureStorageTableSink();
        TAzureStorageOutputTableProperties p = new TAzureStorageOutputTableProperties("test");
        p.connection.setupProperties();
        p.setupProperties();
        sink.initialize(null, p);
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSink#createWriteOperation()}.
     */
    @Test
    public final void testCreateWriteOperation() {
        assertNotNull(sink.createWriteOperation());
    }

    /**
     * Test method for {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSink#getProperties()}.
     */
    @Test
    public final void testGetProperties() {
        assertNotNull(sink.getProperties());
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#validate(org.talend.components.api.container.RuntimeContainer)}.
     */
    @Test
    public final void testValidate() {
        assertEquals(ValidationResult.Result.ERROR, sink.validate(null).getStatus());
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getEndpointSchema(org.talend.components.api.container.RuntimeContainer, java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetEndpointSchema() throws IOException {
        sink.getEndpointSchema(null, "test");
        fail("Should have failed...");
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getSchemaNames(org.talend.components.api.container.RuntimeContainer, org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetSchemaNames() throws IOException {
        AzureStorageTableSink.getSchemaNames(null, sink.getConnectionProperties());
        fail("Should have failed...");
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getSchemaNames(org.talend.components.api.container.RuntimeContainer)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetSchemaNamesRuntimeContainer() throws IOException {
        sink.getSchemaNames(null);
        fail("Should have failed...");
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getSchema(org.talend.components.api.container.RuntimeContainer, org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties, java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetSchema() throws IOException {
        AzureStorageTableSourceOrSink.getSchema(null, sink.getConnectionProperties(), "test");
        fail("Should have failed...");
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getStorageTableClient(org.talend.components.api.container.RuntimeContainer)}.
     *
     * @throws URISyntaxException
     * @throws InvalidKeyException
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetStorageTableClient() throws IOException, InvalidKeyException, URISyntaxException {
        sink.getStorageTableClient(null);
        fail("Should have failed...");
    }

    /**
     * Test method for
     * {@link org.talend.components.azurestorage.table.runtime.AzureStorageTableSourceOrSink#getStorageTableReference(org.talend.components.api.container.RuntimeContainer, java.lang.String)}.
     * 
     * @throws StorageException
     * @throws URISyntaxException
     * @throws InvalidKeyException
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testGetStorageTableReference()
            throws IOException, InvalidKeyException, URISyntaxException, StorageException {
        sink.getStorageTableReference(null, "table");
        fail("Should have failed...");
    }

}
