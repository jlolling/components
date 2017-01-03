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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.components.api.wizard.WizardImageType;

public class AzureStorageConnectionWizardDefinitionTest {

    private AzureStorageConnectionWizardDefinition azureStorageConnectionWizardDefinition;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // TODO add setup code.

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // TODO add tear down code.

    }

    @Before
    public void setUp() throws Exception {
        // TODO add setup code.
        azureStorageConnectionWizardDefinition = new AzureStorageConnectionWizardDefinition();
    }

    @After
    public void tearDown() throws Exception {
        // TODO add tear down code.

    }

    /**
     *
     * @see org.talend.components.azurestorage.wizard.AzureStorageConnectionWizardDefinition#isTopLevel()
     */
    @Test
    public void testIsTopLevel() {
        boolean result = azureStorageConnectionWizardDefinition.isTopLevel();

        assertTrue("result should be true", result);
    }

    /**
     *
     * @see org.talend.components.azurestorage.wizard.AzureStorageConnectionWizardDefinition#getPngImagePath(WizardImageType)
     */
    @Test
    public void testGetPngImagePath() {
        WizardImageType imageType = WizardImageType.TREE_ICON_16X16;
        String pngimagepath = azureStorageConnectionWizardDefinition.getPngImagePath(imageType);

        assertTrue("pngimagepath should be not null and not empty", pngimagepath != null && !pngimagepath.equals(""));
    }

}
