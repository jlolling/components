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
package org.talend.components.azurestorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.container.DefaultComponentRuntimeContainerImpl;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.service.common.ComponentServiceImpl;
import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.components.api.test.ComponentTestUtils;
import org.talend.components.azurestorage.runtime.AzureStorageSource;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionDefinition;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateDefinition;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties;
import org.talend.components.azurestorage.tazurestoragecontainerdelete.TAzureStorageContainerDeleteDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistProperties;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListProperties;
import org.talend.components.azurestorage.tazurestoragedelete.TAzureStorageDeleteDefinition;
import org.talend.components.azurestorage.tazurestorageget.TAzureStorageGetDefinition;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListDefinition;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListProperties;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutDefinition;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.presentation.Form;

@SuppressWarnings("nls")
public class AzureStorageComponentsTest extends AbstractComponentTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    protected RuntimeContainer runtime;

    private ComponentService componentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageComponentsTest.class);

    public AzureStorageComponentsTest() {
        super();
        runtime = new DefaultComponentRuntimeContainerImpl();
    }

    @Before
    public void initializeComponentRegistryAndService() {
        // reset the component service
        componentService = null;
    }

    // default implementation for pure java test.
    @Override
    public ComponentService getComponentService() {
        if (componentService == null) {
            DefinitionRegistry testComponentRegistry = new DefinitionRegistry();

            testComponentRegistry.registerComponentFamilyDefinition(new AzureStorageFamilyDefinition());
            componentService = new ComponentServiceImpl(testComponentRegistry);
        }
        return componentService;
    }

    @Test
    public void testComponentsHaveBeenRegistered() {
        assertComponentIsRegistered(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerCreateDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerDeleteDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerExistDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerListDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageDeleteDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageGetDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageListDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStoragePutDefinition.COMPONENT_NAME);
    }

    @Test
    public void testFamily() {
        ComponentDefinition cd = getComponentService().getComponentDefinition("tAzureStorageConnection");
        assertEquals(1, cd.getFamilies().length);
        assertEquals("Cloud/Azure Storage", cd.getFamilies()[0]);
    }

    public void checkContainer(AzureStorageProperties props, String mycontainer) {
        props.container.setValue(mycontainer);
        assertEquals(mycontainer, props.container.getValue());
        assertTrue(true);
    }

    /**
     * Check the container's validation chain
     */
    public ValidationResult getContainerValidation(String container, AzureStorageProperties properties) {
        properties.container.setValue(container);
        AzureStorageSource sos = new AzureStorageSource();
        sos.initialize(runtime, properties);
        return sos.validate(runtime);
    }

    @Test
    public void testContainerValidation() {
        String errAcct = "ERROR The account name or key cannot be empty.";
        // String
        String containerOK = "a-good-container-name";
        String containerEmpty = "";
        String containerUpper = "AnUpperCasedContainer";
        String containerTooLong = "a-very-long-long-long-long-long-long-long-long-long-long-long-long-long-long-long-long-long-container-name";
        String containerInvalid = "**( ^%";
        //
        AzureStorageProperties properties = new TAzureStorageContainerCreateProperties("tests");
        // checks account name and key
        assertEquals(errAcct, getContainerValidation(containerOK, properties).toString());
        //
        properties.connection.accountName.setValue("myFakeAccountName");
        assertEquals(errAcct, getContainerValidation(containerOK, properties).toString());
        //
        properties.connection.accountName.setValue("");
        properties.connection.accountKey.setValue("myFakeAccountKey");
        assertEquals(errAcct, getContainerValidation(containerOK, properties).toString());
        //
        // now checks container's name
        properties.connection.accountName.setValue("myFakeAccountName");
        properties.connection.accountKey.setValue("myFakeAccountKey");
        assertEquals(ValidationResult.OK, getContainerValidation(containerOK, properties));
        assertEquals("ERROR The container name cannot be empty.", getContainerValidation(containerEmpty, properties).toString());
        assertEquals("ERROR The container name must be in lowercase.",
                getContainerValidation(containerUpper, properties).toString());
        assertEquals("ERROR The container name length must be between 3 and 63 characters.",
                getContainerValidation(containerTooLong, properties).toString());
        assertEquals("ERROR The container name must contain only alphanumeric chars and dash(-).",
                getContainerValidation(containerInvalid, properties).toString());
    }

    @Test
    public void testConnectionProperties() {
        TAzureStorageConnectionProperties props = (TAzureStorageConnectionProperties) new TAzureStorageConnectionDefinition()
                .createProperties();
        Form f = props.getForm(Form.MAIN);
        ComponentTestUtils.checkSerialize(props, errorCollector);
        assertEquals(Form.MAIN, f.getName());

        // assertTrue(props.accountName.));
        // assertTrue(props.accountKey.isRequired());
        // assertTrue(props.protocol.isRequired());
    }

    /**
     * testContainerList. Covers all TAzureStorageContainer*
     */
    @Test
    public void testContainerExist() {
        TAzureStorageContainerExistProperties props = (TAzureStorageContainerExistProperties) new TAzureStorageContainerExistDefinition()
                .createProperties();
        Form f = props.getForm(Form.MAIN);
        ComponentTestUtils.checkSerialize(props, errorCollector);
        LOGGER.debug(f.toString());
        LOGGER.debug(props.toString());
        assertEquals(Form.MAIN, f.getName());
        assertTrue(props.container.isRequired());
    }

    @Test
    public void testBlobListSchema() {
        Schema s = SchemaBuilder.record("Main").fields().name("BlobName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "300")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault().endRecord();
        LOGGER.debug(s.toString());
        TAzureStorageListProperties props = (TAzureStorageListProperties) new TAzureStorageListDefinition().createProperties();
        Schema ps = props.schema.schema.getValue();
        LOGGER.debug(ps.toString());
        assertEquals(s, ps);
    }

    @Test
    public void testContainerListSchema() {
        Schema s = SchemaBuilder.record("Main").fields().name("ContainerName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "50")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault().endRecord();
        LOGGER.debug(s.toString());
        TAzureStorageContainerListProperties props = (TAzureStorageContainerListProperties) new TAzureStorageContainerListDefinition()
                .createProperties();
        Schema ps = props.schema.schema.getValue();
        LOGGER.debug(ps.toString());
        assertEquals(s, ps);
    }

    @Test
    public void testPutFileList() {
        TAzureStoragePutProperties props = (TAzureStoragePutProperties) new TAzureStoragePutDefinition().createProperties();
        assertTrue(props.localFolder.isRequired());
        assertFalse(props.useFileList.getValue());
        props.useFileList.setValue(true);
        props.afterUseFileList();
        Form f = props.getForm(Form.MAIN);
        assertTrue(f.getWidget(props.files.getName()).isVisible());
    }

}
