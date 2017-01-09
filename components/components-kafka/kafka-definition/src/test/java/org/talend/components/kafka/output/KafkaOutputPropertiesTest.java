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
package org.talend.components.kafka.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.talend.components.api.test.ComponentTestUtils;
import org.talend.daikon.properties.PropertiesDynamicMethodHelper;
import org.talend.daikon.properties.presentation.Form;

public class KafkaOutputPropertiesTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    KafkaOutputProperties output;

    @Before
    public void reset() {
        output = new KafkaOutputProperties("output");
        output.init();
    }

    @Test
    public void testI18N() {
        ComponentTestUtils.checkAllI18N(output, errorCollector);
    }

    @Test
    public void testVisible() throws Throwable {
        Form main = output.getForm(Form.MAIN);
        assertTrue(main.getWidget(output.partitionType).isVisible());
        assertTrue(main.getWidget(output.keyColumn).isHidden());
        assertTrue(main.getWidget(output.useCompress).isVisible());
        assertTrue(main.getWidget(output.compressType).isHidden());
        assertTrue(main.getWidget(output.configurations).isVisible());

        output.partitionType.setValue(KafkaOutputProperties.PartitionType.COLUMN);
        PropertiesDynamicMethodHelper.afterProperty(output, output.partitionType.getName());
        assertTrue(main.getWidget(output.partitionType).isVisible());
        assertTrue(main.getWidget(output.keyColumn).isVisible());
        assertTrue(main.getWidget(output.useCompress).isVisible());
        assertTrue(main.getWidget(output.compressType).isHidden());
        assertTrue(main.getWidget(output.configurations).isVisible());

        output.useCompress.setValue(true);
        PropertiesDynamicMethodHelper.afterProperty(output, output.useCompress.getName());
        assertTrue(main.getWidget(output.partitionType).isVisible());
        assertTrue(main.getWidget(output.keyColumn).isVisible());
        assertTrue(main.getWidget(output.useCompress).isVisible());
        assertTrue(main.getWidget(output.compressType).isVisible());
        assertTrue(main.getWidget(output.configurations).isVisible());

        output.useCompress.setValue(false);
        PropertiesDynamicMethodHelper.afterProperty(output, output.useCompress.getName());
        assertTrue(main.getWidget(output.partitionType).isVisible());
        assertTrue(main.getWidget(output.keyColumn).isVisible());
        assertTrue(main.getWidget(output.useCompress).isVisible());
        assertTrue(main.getWidget(output.compressType).isHidden());
        assertTrue(main.getWidget(output.configurations).isVisible());
    }

    @Test
    public void testDefaultValue() {
        assertEquals(KafkaOutputProperties.PartitionType.ROUND_ROBIN, output.partitionType.getValue());
        assertFalse(output.useCompress.getValue());
    }

    @Test
    public void testTrigger() {
        Form main = output.getForm(Form.MAIN);
        assertTrue(main.getWidget(output.partitionType).isCallAfter());
        assertTrue(main.getWidget(output.useCompress).isCallAfter());
    }

}
