package org.talend.components.azurestorage.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.talend.components.azurestorage.helpers.FileMaskTable;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutProperties;
import org.talend.components.azurestorage.utils.AzureStorageUtils;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * TODO - Shouldn't this class be a Sink class implementation ???
 */
public class AzureStoragePutReader extends AzureStorageReader<Boolean> {

    private TAzureStoragePutProperties properties;

    private Boolean result;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStoragePutReader.class);

    public AzureStoragePutReader(RuntimeContainer container, BoundedSource source, TAzureStoragePutProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        String mycontainer = properties.container.getValue();
        String localFolder = properties.localFolder.getValue();
        String remoteFolder = properties.remoteFolder.getValue();
        Boolean useFileList = properties.useFileList.getValue();
        FileMaskTable files = properties.files;
        CloudBlobContainer storageContainer;
        try {
            storageContainer = ((AzureStorageSource) getCurrentSource()).getStorageContainerReference(runtime, mycontainer);
        } catch (Exception e) {
            // we cannot continue if storageContainer is invalid...
            throw new ComponentException(e);
        }
        AzureStorageUtils utils = new AzureStorageUtils();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        // process files list
        if (useFileList && files != null && files.size() > 0) {
            for (int idx = 0; idx < files.fileMask.getValue().size(); idx++) {
                String fileMask = files.fileMask.getValue().get(idx);
                String newName = files.newName.getValue().get(idx);
                Map<String, String> map = new HashMap<String, String>();
                map.put(fileMask, newName);
                list.add(map);
            }
        }
        Map<String, String> fileMap;
        if (useFileList) {
            fileMap = utils.genFileFilterList(list, localFolder, remoteFolder);
        } else {
            fileMap = utils.genAzureObjectList(new File(localFolder), remoteFolder);
        }
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            try {
                CloudBlockBlob blob = storageContainer.getBlockBlobReference(entry.getValue());
                File source = new File(entry.getKey());
                FileInputStream stream = new FileInputStream(source);
                blob.upload(stream, source.length());
                stream.close();
                dataCount++;
            } catch (Exception e) {
                if (properties.dieOnError.getValue()) {
                    throw new ComponentException(e);
                } else {
                    LOGGER.error(e.getLocalizedMessage());
                }
            }
        }
        result = true;
        return result;
    }

    @Override
    public boolean advance() throws IOException {
        return false;
    }

    /**
     * TODO - See if we shouldn't return the blob's name instead of a boolean...
     */
    @Override
    public Boolean getCurrent() throws NoSuchElementException {
        return result;
    }

    @Override
    public Map<String, Object> getReturnValues() {
        Map<String, Object> resultMap = super.getReturnValues();
        resultMap.put(AzureStorageDefinition.RETURN_CONTAINER, properties.container.getValue());
        resultMap.put(AzureStorageBlobDefinition.RETURN_LOCAL_FOLDER, properties.localFolder.getValue());
        resultMap.put(AzureStorageBlobDefinition.RETURN_REMOTE_FOLDER, properties.remoteFolder.getValue());

        return resultMap;
    }
}
