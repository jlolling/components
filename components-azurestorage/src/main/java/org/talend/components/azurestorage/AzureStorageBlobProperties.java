package org.talend.components.azurestorage;

import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.helpers.RemoteBlobsTable;

public class AzureStorageBlobProperties extends AzureStorageProperties {

    public RemoteBlobsTable remoteBlobs = new RemoteBlobsTable("remoteBlobs"); //$NON-NLS-1$

    public AzureStorageBlobProperties(String name) {
        super(name);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(new PropertyPathConnector(Connector.MAIN_NAME, "schema")); //$NON-NLS-1$
        } else {
            return Collections.emptySet();
        }
    }
}
