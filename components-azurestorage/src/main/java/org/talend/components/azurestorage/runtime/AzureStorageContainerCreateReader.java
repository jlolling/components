package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.azurestorage.AzureStorageDefinition;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties;

import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureStorageContainerCreateReader extends AzureStorageReader<Boolean> {

    private TAzureStorageContainerCreateProperties properties;

    private Boolean result;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageContainerCreateReader.class);

    public AzureStorageContainerCreateReader(RuntimeContainer container, BoundedSource source,
            TAzureStorageContainerCreateProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        try {
            String mycontainer = properties.container.getValue();
            String access = properties.accessControl.getStringValue();
            CloudBlobContainer container = ((AzureStorageSource) getCurrentSource()).getStorageContainerReference(runtime,
                    mycontainer);
            result = container.createIfNotExists();
            // Manage accessControl
            if (access.equals("Public") && result) {
                // Create a permissions object.
                BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
                // Include public access in the permissions object.
                containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                // Set the permissions on the container.
                container.uploadPermissions(containerPermissions);
            }
            if (!result) {
                LOGGER.warn("Container " + mycontainer + " already exists !");
            }
            dataCount++;
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new ComponentException(e);
        }
    }

    @Override
    public boolean advance() throws IOException {
        return false;
    }

    @Override
    public Boolean getCurrent() throws NoSuchElementException {
        return result;
    }

    @Override
    public Map<String, Object> getReturnValues() {
        Map<String, Object> resultMap = super.getReturnValues();
        resultMap.put(AzureStorageDefinition.RETURN_CONTAINER, properties.container.getValue());

        return resultMap;
    }
}
