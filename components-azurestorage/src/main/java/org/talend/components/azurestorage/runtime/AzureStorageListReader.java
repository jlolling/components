package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.azurestorage.AzureStorageBlobDefinition;
import org.talend.components.azurestorage.AzureStorageDefinition;
import org.talend.components.azurestorage.helpers.RemoteBlob;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListProperties;

import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class AzureStorageListReader extends AzureStorageReader<String> {

    private transient TAzureStorageListProperties properties;

    private List<CloudBlob> blobs = new ArrayList<CloudBlob>();

    private int blobIndex;

    private int blobSize;

    private CloudBlob currentBlob;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageListReader.class);

    public AzureStorageListReader(RuntimeContainer container, BoundedSource source, TAzureStorageListProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        String mycontainer = properties.container.getValue();
        // build a list with remote blobs to fetch
        List<RemoteBlob> remoteBlobs = ((AzureStorageSource) getCurrentSource()).getRemoteBlobs();
        try {
            CloudBlobContainer container = ((AzureStorageSource) getCurrentSource()).getStorageContainerReference(runtime,
                    mycontainer);
            blobs = new ArrayList<CloudBlob>();
            for (RemoteBlob rmtb : remoteBlobs) {
                for (ListBlobItem blob : container.listBlobs(rmtb.prefix, rmtb.include)) {
                    if (blob instanceof CloudBlob) {
                        blobs.add((CloudBlob) blob);
                    }
                }
            }
            blobSize = blobs.size();
            if (blobSize > 0) {
                dataCount++;
                blobIndex = 0;
                return true;
            } else
                return false;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new ComponentException(e);
        }
    }

    @Override
    public boolean advance() throws IOException {
        blobIndex++;
        if (blobIndex < blobSize) {
            dataCount++;
            return true;
        }
        return false;
    }

    @Override
    public String getCurrent() throws NoSuchElementException {
        currentBlob = blobs.get(blobIndex);
        runtime.setComponentData(runtime.getCurrentComponentId(), AzureStorageBlobDefinition.RETURN_CURRENT_BLOB,
                currentBlob.getName());
        return currentBlob.getName();
    }

    @Override
    public Map<String, Object> getReturnValues() {
        Map<String, Object> resultMap = super.getReturnValues();
        resultMap.put(AzureStorageDefinition.RETURN_CONTAINER, properties.container.getValue());
        if (currentBlob != null)
            resultMap.put(AzureStorageBlobDefinition.RETURN_CURRENT_BLOB, currentBlob.getName());

        return resultMap;
    }
}
