package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Collections;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.SourceOrSink;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.ValidationResult;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureStorageSourceOrSink implements SourceOrSink {

    public final static String KEY_CONNECTION_PROPERTIES = "connection";

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageSourceOrSink.class);

    private static final long serialVersionUID = 1589394346101991075L;

    protected AzureStorageProvideConnectionProperties properties;

    private Schema schema;

    @Override
    public ValidationResult initialize(RuntimeContainer container, ComponentProperties properties) {
        LOGGER.debug("initialize");
        this.properties = (AzureStorageProvideConnectionProperties) properties;
        return ValidationResult.OK;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        LOGGER.debug("validate");
        TAzureStorageConnectionProperties conn = validateConnection(container);
        // checks connection's account and key
        if (StringUtils.isEmpty(conn.accountName.getStringValue()) || StringUtils.isEmpty(conn.accountKey.getStringValue())) {
            ValidationResult vr = new ValidationResult();
            vr.setMessage("The account name or key cannot be empty."); //$NON-NLS-1$
            vr.setStatus(ValidationResult.Result.ERROR);
            return vr;
        }
        return ValidationResult.OK;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {
        LOGGER.debug("getEndpointSchema");
        return null;
    }

    public Schema getSchema() {
        LOGGER.debug("getSchema");
        return schema;
    }

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer container) throws IOException {
        LOGGER.debug("getSchemaNames");
        return Collections.emptyList();
    }

    protected TAzureStorageConnectionProperties validateConnection(RuntimeContainer container) {
        LOGGER.debug("connect");
        TAzureStorageConnectionProperties connProps = getConnectionProperties();
        String refComponentId = connProps.getReferencedComponentId();
        TAzureStorageConnectionProperties sharedConn = null;
        // Using another component's connection
        if (refComponentId != null) {
            // In a runtime container
            if (container != null) {
                sharedConn = (TAzureStorageConnectionProperties) container.getComponentData(refComponentId,
                        KEY_CONNECTION_PROPERTIES);
                if (sharedConn != null) {
                    return sharedConn;
                }
            }
            // Design time
            connProps = connProps.getReferencedConnectionProperties();
        }
        if (container != null) {
            container.setComponentData(container.getCurrentComponentId(), KEY_CONNECTION_PROPERTIES, connProps);
        }
        return connProps;
    }

    public TAzureStorageConnectionProperties getConnectionProperties() {
        LOGGER.debug("getConnectionProperties");
        return properties.getConnectionProperties();
    }

    /**
     * @return
     */
    public CloudBlobClient getServiceClient(RuntimeContainer container) throws InvalidKeyException, URISyntaxException {
        LOGGER.debug("getServiceClient");
        TAzureStorageConnectionProperties conn = validateConnection(container);
        String account = conn.accountName.getValue();
        String key = conn.accountKey.getValue();
        String protocol = conn.protocol.getValue().toString().toLowerCase();
        String storageConnectionString = "DefaultEndpointsProtocol=" + protocol + ";" + "AccountName=" + account + ";"
                + "AccountKey=" + key;
        CloudStorageAccount cloudAccount = CloudStorageAccount.parse(storageConnectionString);
        return cloudAccount.createCloudBlobClient();
    }

    /**
     * @return
     */
    public CloudBlobContainer getStorageContainerReference(RuntimeContainer container, String storageContainer)
            throws InvalidKeyException, URISyntaxException, StorageException {
        LOGGER.debug("getStorageContainerReference");
        return getServiceClient(container).getContainerReference(storageContainer);
    }
}
