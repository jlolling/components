package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.azurestorage.AzureStorageDefinition;
import org.talend.components.azurestorage.helpers.RemoteBlob;
import org.talend.components.azurestorage.tazurestoragedelete.TAzureStorageDeleteProperties;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class AzureStorageDeleteReader extends AzureStorageReader<Boolean> {

    private TAzureStorageDeleteProperties properties;

    private Boolean result;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageDeleteReader.class);

    public AzureStorageDeleteReader(RuntimeContainer container, BoundedSource source, TAzureStorageDeleteProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        List<RemoteBlob> remoteBlobs = ((AzureStorageSource) getCurrentSource()).getRemoteBlobs();
        String mycontainer = properties.container.getValue();
        try {
            CloudBlobClient clientService = ((AzureStorageSource) getCurrentSource()).getServiceClient(runtime);
            CloudBlobContainer container = clientService.getContainerReference(mycontainer);
            for (RemoteBlob rmtb : remoteBlobs) {
                for (ListBlobItem blob : container.listBlobs(rmtb.prefix, rmtb.include)) {
                    if (blob instanceof CloudBlockBlob) {
                        // FIXME - problem with blobs with space in name...
                        if (((CloudBlockBlob) blob).deleteIfExists()) {
                            dataCount++;
                        } else
                            LOGGER.warn("Failed for deleting CloudBlob : " + ((CloudBlockBlob) blob).getName() + ".");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new ComponentException(e);
        }
        result = (dataCount > 0);
        return result;
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
