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
package org.talend.components.azurestorage.wizard;

import static org.junit.Assert.assertNotNull;

import org.apache.avro.Schema;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;

public class AzureStorageComponentListPropertiesTest {

    private AzureStorageComponentListProperties azureStorageComponentListProperties;

    @Before
    public void setUp() throws Exception {
        azureStorageComponentListProperties = new AzureStorageComponentListProperties("test");
    }

    /**
     *
     * @see org.talend.components.azurestorage.wizard.AzureStorageComponentListProperties#getContainerSchema()
     */
    @Test
    public void getContainerSchema() {
        Schema containerschema = azureStorageComponentListProperties.getContainerSchema();
        assertNotNull("containerschema cannot be null", containerschema);
    }

    /**
     *
     * @see org.talend.components.azurestorage.wizard.AzureStorageComponentListProperties#setConnection(TAzureStorageConnectionProperties)
     */
    @Test
    public void testSetConnection() {
        TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties(null);
        AzureStorageComponentListProperties result = azureStorageComponentListProperties.setConnection(connection);
        assertNotNull("result cannot be null", result);
    }

}
