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

package org.talend.components.simplefileio.output;

import org.junit.Before;

/**
 * Unit tests for {@link SimpleFileIoOutputProperties}.
 */
public class SimpleFileIoOutputPropertiesTest {

    /**
     * Instance to test. A new instance is created for each test.
     */
    private SimpleFileIoOutputProperties properties = null;

    @Before
    public void setup() {
        properties = new SimpleFileIoOutputProperties("test");
        properties.init();
    }

}
