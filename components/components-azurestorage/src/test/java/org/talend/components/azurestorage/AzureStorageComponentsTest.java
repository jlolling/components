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
import static org.junit.Assert.assertTrue;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.azurestorage.blob.AzureStorageContainerProperties;
import org.talend.components.azurestorage.blob.runtime.AzureStorageSource;
import org.talend.components.azurestorage.blob.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties;
import org.talend.components.azurestorage.blob.tazurestoragecontainerlist.TAzureStorageContainerListProperties;
import org.talend.components.azurestorage.blob.tazurestoragelist.TAzureStorageListProperties;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionDefinition;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.ValidationResult;

public class AzureStorageComponentsTest {// extends AzureStorageGenericBase {

    protected RuntimeContainer runtime;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void testFamily() {
        TAzureStorageConnectionDefinition aconn = new TAzureStorageConnectionDefinition();
        assertEquals(1, aconn.getFamilies().length);
        assertEquals("Cloud/Azure Storage", aconn.getFamilies()[0]);
    }

    public void checkContainer(AzureStorageContainerProperties props, String mycontainer) {
        props.container.setValue(mycontainer);
        assertEquals(mycontainer, props.container.getValue());
        assertTrue(true);
    }

    /**
     * Check the container's validation chain
     */
    public ValidationResult getContainerValidation(String container, AzureStorageContainerProperties properties) {
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
        AzureStorageContainerProperties properties = new TAzureStorageContainerCreateProperties("tests");
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
    public void testBlobListSchema() {
        Schema s = SchemaBuilder.record("Main").fields().name("BlobName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "300")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault().endRecord();
        TAzureStorageListProperties props = new TAzureStorageListProperties("tests");
        props.setupProperties();
        Schema ps = props.schema.schema.getValue();
        assertEquals(s, ps);
    }

    @Test
    public void testContainerListSchema() {
        Schema s = SchemaBuilder.record("Main").fields().name("ContainerName").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "50")// $NON-NLS-3$
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type(AvroUtils._string()).noDefault().endRecord();
        TAzureStorageContainerListProperties props = new TAzureStorageContainerListProperties("tests");
        props.setupProperties();
        Schema ps = props.schema.schema.getValue();
        assertEquals(s, ps);
    }

}
