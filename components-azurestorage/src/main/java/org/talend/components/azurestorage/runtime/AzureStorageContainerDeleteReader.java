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
import org.talend.components.azurestorage.tazurestoragecontainerdelete.TAzureStorageContainerDeleteProperties;

import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureStorageContainerDeleteReader extends AzureStorageReader<Boolean> {

    private TAzureStorageContainerDeleteProperties properties;

    private Boolean result;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageContainerCreateReader.class);

    public AzureStorageContainerDeleteReader(RuntimeContainer container, BoundedSource source,
            TAzureStorageContainerDeleteProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        try {
            String mycontainer = properties.container.getValue();
            CloudBlobContainer container = ((AzureStorageSource) getCurrentSource())
                    .getStorageContainerReference(runtime, mycontainer);
            result = container.deleteIfExists();
            if (!result) {
                LOGGER.warn("Container " + mycontainer + " didn't exist or cannot be deleted !");
            } else
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
