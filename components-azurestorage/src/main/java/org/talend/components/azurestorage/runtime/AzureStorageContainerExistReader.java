package org.talend.components.azurestorage.runtime;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistProperties;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureStorageContainerExistReader extends AzureStorageReader<Boolean> {

    private transient Boolean result;

    protected TAzureStorageContainerExistProperties properties;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageContainerExistReader.class);

    protected AzureStorageContainerExistReader(RuntimeContainer container, BoundedSource source,
            TAzureStorageContainerExistProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        try {
            String mycontainer = properties.container.getValue();
            CloudBlobClient clientService = ((AzureStorageSource) getCurrentSource()).getServiceClient(runtime);
            CloudBlobContainer container = clientService.getContainerReference(mycontainer);
            result = container.exists();
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
        resultMap.put(TAzureStorageContainerExistDefinition.RETURN_CONTAINER, properties.container.getValue());
        resultMap.put(TAzureStorageContainerExistDefinition.RETURN_CONTAINER_EXIST, new Boolean(result));
        
        return resultMap;
    }
}
