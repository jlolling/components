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
package org.talend.components.azurestorage.table;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.talend.daikon.properties.ValidationResult;

public class AzureStorageTablePropertiesTest {

    private AzureStorageTableProperties properties;

    @Before
    public void setup() {
        properties = new AzureStorageTableProperties("tests");
        properties.setupProperties();
    }

    /**
     *
     * @see org.talend.components.azurestorage.table.AzureStorageTableProperties#validateNameMapping()
     */
    @Test
    public void testValidateNameMapping() {
        ValidationResult result = properties.validateNameMapping();
        assertNotNull("result cannot be null", result);
    }

}
