package org.talend.components.salesforce.runtime;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.salesforce.SalesforceConnectionProperties;
import org.talend.components.salesforce.test.SalesforceTestBase;
import org.talend.components.salesforce.tsalesforceinput.TSalesforceInputProperties;
import org.talend.components.salesforce.tsalesforceoutput.TSalesforceOutputProperties;
import org.talend.daikon.properties.ValidationResult;

import com.sforce.soap.partner.fault.ExceptionCode;
import com.sforce.soap.partner.fault.LoginFault;

/**
 * Test Salesforce connection session
 */
public class SalesforceSessionReuseTestIT extends SalesforceTestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceInputReaderTestIT.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testBasicLogin() throws Throwable {
        File sessionFolder = new File(tempFolder.getRoot().getPath() + "/tsalesforceconnection/");
        assertEquals(0, sessionFolder.getTotalSpace());
        LOGGER.debug("session folder: " + sessionFolder.getAbsolutePath());
        SalesforceConnectionProperties props = setupProps(null, !ADD_QUOTES);
        // setup session function
        props.reuseSession.setValue(true);
        props.sessionDirectory.setValue(sessionFolder.getAbsolutePath());

        // Init session
        assertEquals(ValidationResult.Result.OK, testConnection(props).getStatus());
        assertNotEquals(0, sessionFolder.getTotalSpace());

        // Set wrong pwd to test reuse session from session folder
        props.userPassword.password.setValue("WRONG_PASSWORD");
        assertEquals(ValidationResult.Result.OK, testConnection(props).getStatus());

    }

    @Test
    public void testInputReuseSession() throws Throwable {
        File sessionFolder = new File(tempFolder.getRoot().getPath() + "/tsalesforceinput/");
        assertEquals(0, sessionFolder.getTotalSpace());
        LOGGER.debug("session folder: " + sessionFolder.getAbsolutePath());

        TSalesforceInputProperties props = (TSalesforceInputProperties) new TSalesforceInputProperties("foo").init(); //$NON-NLS-1$
        props.module.moduleName.setValue(EXISTING_MODULE_NAME);
        props.module.main.schema.setValue(getMakeRowSchema(false));
        props.connection = setupProps(null, !ADD_QUOTES);

        // setup session function
        props.connection.reuseSession.setValue(true);
        props.connection.sessionDirectory.setValue(sessionFolder.getAbsolutePath());

        // Init session
        assertEquals(ValidationResult.Result.OK, testConnection(props).getStatus());
        assertNotEquals(0, sessionFolder.getTotalSpace());

        // Invalid session, test whether it can be renew the session
        invalidSession(props.connection, null);

        List<IndexedRecord> records = readRows(props);
        assertNotNull(records);
        LOGGER.debug("current records number in module " + EXISTING_MODULE_NAME + ": " + records.size());
        assertNotEquals(0, records.size());

        // Set wrong pwd to test reuse session from session folder
        props.connection.userPassword.password.setValue("WRONG_PASSWORD");
        testConnection(props);

        records = readRows(props);
        assertNotNull(records);
        LOGGER.debug("current records number in module " + EXISTING_MODULE_NAME + ": " + records.size());
        assertNotEquals(0, records.size());

        // Test reuse session fails with wrong pwd
        invalidSession(props.connection, null);
        try {
            readRows(props);
        } catch (IOException e) {
            Throwable caused = e.getCause();
            // Should login fails with wrong pwd
            assertTrue(caused instanceof LoginFault);
            assertEquals(ExceptionCode.INVALID_LOGIN, ((LoginFault) caused).getExceptionCode());
            LOGGER.debug("except login fails: " + e.getMessage());
        }

        // Disable reuse session function
        props.connection.reuseSession.setValue(false);
        LOGGER.debug("except login fails:");
        assertEquals(ValidationResult.Result.ERROR, testConnection(props).getStatus());

    }

    @Test
    public void testBulkSessionRenew() throws Throwable {

        TSalesforceInputProperties props = (TSalesforceInputProperties) new TSalesforceInputProperties("foo").init(); //$NON-NLS-1$
        props.module.moduleName.setValue(EXISTING_MODULE_NAME);
        props.module.main.schema.setValue(getMakeRowSchema(false));
        props.connection = setupProps(null, !ADD_QUOTES);

        // setup session function
        props.connection.bulkConnection.setValue(true);
        props.queryMode.setValue(TSalesforceInputProperties.QueryMode.Bulk);

        // Init session
        assertEquals(ValidationResult.Result.OK, testConnection(props).getStatus());

        BoundedReader reader = createBoundedReader(props);
        assertTrue(reader instanceof SalesforceBulkQueryInputReader);
        boolean hasRecord = reader.start();
        // Invalid the session by session id
        String sessionIdBeforeRenew = ((SalesforceBulkQueryInputReader) reader).bulkRuntime.getBulkConnection().getConfig()
                .getSessionId();
        invalidSession(props.connection, sessionIdBeforeRenew);
        // Test renew session for bulk connections
        ((SalesforceBulkQueryInputReader) reader).executeSalesforceBulkQuery();
        // Check the renew session
        String sessionIdAfterRenew = ((SalesforceBulkQueryInputReader) reader).bulkRuntime.getBulkConnection().getConfig()
                .getSessionId();
        assertNotEquals(sessionIdBeforeRenew, sessionIdAfterRenew);

    }

    @Test
    public void testOutputReuseSession() throws Throwable {
        File sessionFolder = new File(tempFolder.getRoot().getPath() + "/tsalesforceoutput/");
        assertEquals(0, sessionFolder.getTotalSpace());
        LOGGER.debug("session folder: " + sessionFolder.getAbsolutePath());

        TSalesforceOutputProperties props = (TSalesforceOutputProperties) new TSalesforceOutputProperties("foo").init(); //$NON-NLS-1$
        props.module.moduleName.setValue(EXISTING_MODULE_NAME);
        props.module.main.schema.setValue(getMakeRowSchema(false));
        props.connection = setupProps(null, !ADD_QUOTES);

        // setup session function
        props.connection.reuseSession.setValue(true);
        props.connection.sessionDirectory.setValue(sessionFolder.getAbsolutePath());

        // Init session
        assertEquals(ValidationResult.Result.OK, testConnection(props).getStatus());
        assertNotEquals(0, sessionFolder.getTotalSpace());

        // Invalid session, test whether it can be renew the session
        invalidSession(props.connection, null);
        // length=260
        String invalidName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        List<IndexedRecord> records = new ArrayList<>();
        IndexedRecord r1 = new GenericData.Record(props.module.main.schema.getValue());
        r1.put(0, invalidName);
        records.add(r1);
        try {
            doWriteRows(props, records);
        } catch (IOException e) {
            // means exception caused by "Name" too long
            assertTrue(e.getMessage().contains(invalidName));
            LOGGER.debug("expect exception: " + e.getMessage());
        }
        // Set wrong pwd to test reuse session from session folder
        props.connection.userPassword.password.setValue("WRONG_PASSWORD");
        try {
            doWriteRows(props, records);
        } catch (IOException e) {
            // means exception caused by "Name" too long
            assertTrue(e.getMessage().contains(invalidName));
            LOGGER.debug("expect exception: " + e.getMessage());
        }

        // Test reuse session fails with wrong pwd
        invalidSession(props.connection, null);
        try {
            doWriteRows(props, records);
        } catch (IOException e) {
            Throwable caused = e.getCause();
            // Should login fails with wrong pwd
            assertThat(caused, instanceOf(LoginFault.class));
            assertEquals(ExceptionCode.INVALID_LOGIN, ((LoginFault) caused).getExceptionCode());
            LOGGER.debug("except login fails: " + e.getMessage());
        }

        // Disable reuse session function
        props.connection.reuseSession.setValue(false);
        LOGGER.debug("except login fails:");
        assertEquals(ValidationResult.Result.ERROR, testConnection(props).getStatus());

    }

    protected ValidationResult testConnection(ComponentProperties props) {
        SalesforceSourceOrSink sourceOrSink = new SalesforceSourceOrSink();
        sourceOrSink.initialize(null, props);
        ValidationResult result = sourceOrSink.validate(null);
        return result;
    }

    protected void invalidSession(SalesforceConnectionProperties props, String sessionId) throws Throwable {
        SalesforceSourceOrSink sourceOrSink = new SalesforceSourceOrSink();
        sourceOrSink.initialize(null, props);
        SalesforceSourceOrSink.ConnectionHolder connectionHolder = sourceOrSink.connect(null);
        assertNotNull(connectionHolder.connection);
        if (sessionId != null) {
            connectionHolder.connection.invalidateSessions(new String[] { sessionId });
        }
        connectionHolder.connection.logout();
        LOGGER.debug("session \"" + connectionHolder.connection.getConfig().getSessionId() + "\" invalided!");
    }

}