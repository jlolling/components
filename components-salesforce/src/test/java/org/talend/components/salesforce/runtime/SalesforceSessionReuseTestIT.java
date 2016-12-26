package org.talend.components.salesforce.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.talend.components.salesforce.runtime.SalesforceSourceOrSink.MAX_VALID_SECONDS;
import static org.talend.components.salesforce.runtime.SalesforceSourceOrSink.SERVICE_ENDPOINT;
import static org.talend.components.salesforce.runtime.SalesforceSourceOrSink.SESSION_FILE_PREFX;
import static org.talend.components.salesforce.runtime.SalesforceSourceOrSink.SESSION_ID;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.avro.generic.IndexedRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.salesforce.SalesforceConnectionProperties;
import org.talend.components.salesforce.test.SalesforceTestBase;
import org.talend.components.salesforce.tsalesforceinput.TSalesforceInputProperties;
import org.talend.daikon.properties.ValidationResult;

import com.sforce.soap.partner.fault.ExceptionCode;
import com.sforce.soap.partner.fault.LoginFault;

/**
 * Test Salesforce connection
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
    public void testReuseSessionWithInput() throws Throwable {
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
        invalidSessionFile(props.connection);
        testConnection(props);

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
        invalidSessionFile(props.connection);
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

    protected ValidationResult testConnection(ComponentProperties props) {
        SalesforceSourceOrSink sourceOrSink = new SalesforceSourceOrSink();
        sourceOrSink.initialize(null, props);
        ValidationResult result = sourceOrSink.validate(null);
        LOGGER.debug(result.toString());
        return result;
    }

    protected void invalidSessionFile(SalesforceConnectionProperties connectionProperties) throws Throwable {
        String sessionPath = connectionProperties.sessionDirectory.getValue() + "/" + SESSION_FILE_PREFX
                + connectionProperties.userPassword.userId.getValue();
        FileInputStream sessionInput = new FileInputStream(sessionPath);
        Properties sessionPropInput = new Properties();
        Properties sessionPropOut = new Properties();
        try {
            sessionPropInput.load(sessionInput);
            String sessionId = sessionPropInput.getProperty(SESSION_ID);
            sessionPropOut.setProperty(SESSION_ID, sessionId + "invalid");
            sessionPropOut.setProperty(SERVICE_ENDPOINT, sessionPropInput.getProperty(SERVICE_ENDPOINT));
            sessionPropOut.setProperty(MAX_VALID_SECONDS, sessionPropInput.getProperty(MAX_VALID_SECONDS));

        } finally {
            sessionInput.close();
        }

        FileOutputStream sessionOutput = new FileOutputStream(sessionPath);
        try {
            sessionPropOut.store(sessionOutput, null);
        } finally {
            sessionOutput.close();
        }

    }
}
