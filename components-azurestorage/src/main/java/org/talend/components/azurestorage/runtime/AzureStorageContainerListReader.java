package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListProperties;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureStorageContainerListReader extends AzureStorageReader<String> {

    private String result;

    private transient Iterator<CloudBlobContainer> containers;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageContainerListReader.class);

    public AzureStorageContainerListReader(RuntimeContainer container, BoundedSource source,
            TAzureStorageContainerListProperties properties) {
        super(container, source);
    }

    @Override
    public boolean start() throws IOException {
        try {
            CloudBlobClient clientService = ((AzureStorageSource) getCurrentSource()).getServiceClient(runtime);
            containers = clientService.listContainers().iterator();
            if (containers.hasNext()) {
                dataCount++;
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
        if (containers.hasNext()) {
            dataCount++;
            return true;
        } else
            return false;
    }

    @Override
    public String getCurrent() throws NoSuchElementException {
        result = containers.next().getName();
        return result;
    }

    @Override
    public Map<String, Object> getReturnValues() {
        Map<String, Object> resultMap = super.getReturnValues();
        resultMap.put(TAzureStorageContainerListDefinition.RETURN_TOTAL_RECORD_COUNT, dataCount);

        return resultMap;
    }
}
