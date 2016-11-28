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
package org.talend.components.azurestorage.runtime;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.azurestorage.AzureStorageBaseTestIT;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties.AccessControl;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutProperties;

public class AzureStoragePutReaderTestIT extends AzureStorageBaseTestIT {

    private String CONTAINER;

    private TAzureStoragePutProperties properties;

    private transient static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageGetReaderTestIT.class);

    public AzureStoragePutReaderTestIT() {
        super("put-" + getRandomTestUID());
        CONTAINER = getNamedThingForTest(TEST_CONTAINER_1);
        properties = new TAzureStoragePutProperties("tests");
        properties.container.setValue(CONTAINER);
        setupConnectionProperties(properties);
        properties.localFolder.setValue(FOLDER_PATH_PUT);
        properties.remoteFolder.setValue("");
    }

    @Before
    public void createTestBlobs() throws Exception {
        doContainerCreate(CONTAINER, AccessControl.Private);
    }

    @After
    public void cleanupTestBlobs() throws Exception {
        // doContainerDelete(CONTAINER);
    }

    @SuppressWarnings({ "unused", "rawtypes" })
    @Test
    public void testBlobPutFolder() throws Exception {
        properties.useFileList.setValue(false);
        BoundedReader reader = createBoundedReader(properties);
        assertTrue(reader.start());
        List<String> blobs = listAllBlobs(CONTAINER);

    }

}
