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
package org.talend.components.azurestorage.utils;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AzureStorageUtilsTest {

    private AzureStorageUtils azureStorageUtils;

    String remotedir = "remote-azure";

    String localdir = "azure";

    String keyparent = "parent";

    File file = new File(".");

    String folder;

    String TEST_FOLDER_PUT = "azurestorage-put";

    @Before
    public void setUp() throws Exception {
        azureStorageUtils = new AzureStorageUtils();
        folder = getClass().getResource("/").getPath() + TEST_FOLDER_PUT;
    }

    /**
     *
     * @see org.talend.components.azurestorage.utils.AzureStorageUtils#genAzureObjectList(File,String)
     */
    @Test
    public void testGenAzureObjectList() {
        file = new File(folder);
        Map<String, String> result = azureStorageUtils.genAzureObjectList(file, keyparent);
        System.out.println(result);
        assertNotNull("result cannot be null", result);
        file = new File(folder + "/blob1.txt");
        result = azureStorageUtils.genAzureObjectList(file, keyparent);
        System.out.println(result);
        assertNotNull("result cannot be null", result);

    }

    /**
     *
     * @see org.talend.components.azurestorage.utils.AzureStorageUtils#genFileFilterList(List<Map<String,String>>,String,String)
     */
    @Test
    public void genFileFilterList() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map myMap = new HashMap<String, String>();
        myMap.put("*.txt", "b");
        myMap.put("*", "d");
        myMap.put("c", "d");
        list.add(myMap);
        Map<String, String> result = azureStorageUtils.genFileFilterList(list, folder, remotedir);
        System.out.println(result);
        assertNotNull("result cannot be null", result);
    }

}
