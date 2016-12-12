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
package org.talend.components.azurestorage.table.runtime;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.avro.Schema;
import org.talend.components.api.component.runtime.SourceOrSink;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.azurestorage.blob.runtime.AzureStorageSourceOrSink;
import org.talend.components.azurestorage.table.AzureStorageTableProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.ValidationResult;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;

public class AzureStorageTableSourceOrSink extends AzureStorageSourceOrSink implements SourceOrSink {

    public static final String PARTITION_KEY = "PartitionKey";

    public static final String ROW_KEY = "RowKey";

    public static final String TIMESTAMP = "Timestamp";

    private static final long serialVersionUID = 5588144102302302129L;

    private final Pattern tableCheckNamePattern = Pattern.compile("^[A-Za-z][A-Za-z0-9]{2,62}$");

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        ValidationResult vr = super.validate(container);
        if (vr == ValidationResult.OK) {
            AzureStorageTableProperties p = (AzureStorageTableProperties) properties;
            String tn = p.tableName.getValue();
            if (tn.isEmpty()) {
                vr = new ValidationResult();
                vr.setStatus(ValidationResult.Result.ERROR);
                vr.setMessage("TableName cannot be empty.");
                return vr;
            }
            if (!tableCheckNamePattern.matcher(tn).matches()) {
                vr = new ValidationResult();
                vr.setStatus(ValidationResult.Result.ERROR);
                vr.setMessage("TableName doesn't follow the naming convention.");
                return vr;
            }
        } else
            return vr;
        return ValidationResult.OK;
    }

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer container) throws IOException {
        return null;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {
        return null;
    }

    public CloudTableClient getStorageTableClient(RuntimeContainer runtime) throws InvalidKeyException, URISyntaxException {
        return getStorageAccount(runtime).createCloudTableClient();
    }

    public CloudTable getStorageTableReference(RuntimeContainer runtime, String tableName)
            throws InvalidKeyException, URISyntaxException, StorageException {
        return getStorageTableClient(runtime).getTableReference(tableName);
    }
}
