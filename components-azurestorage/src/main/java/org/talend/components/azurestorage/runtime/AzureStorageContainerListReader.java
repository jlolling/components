// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
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

    private TAzureStorageContainerListProperties properties;

    private transient Iterator<CloudBlobContainer> containers;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageContainerListReader.class);

    public AzureStorageContainerListReader(RuntimeContainer container, BoundedSource source,
            TAzureStorageContainerListProperties properties) {
        super(container, source);
        this.properties = properties;
    }

    @Override
    public boolean start() throws IOException {
        Boolean startable = false;
        try {
            CloudBlobClient clientService = ((AzureStorageSource) getCurrentSource()).getServiceClient(runtime);
            containers = clientService.listContainers().iterator();
            startable = containers.hasNext();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            if (properties.dieOnError.getValue())
                throw new ComponentException(e);
            else
                startable = false;
        }
        if (startable)
            dataCount++;
        return startable;
    }

    @Override
    public boolean advance() throws IOException {
        Boolean advanceable = false;
        try {
            advanceable = containers.hasNext();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            if (properties.dieOnError.getValue())
                throw (e);
            else
                advanceable = false;
        }
        if (advanceable)
            dataCount++;
        return advanceable;
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
