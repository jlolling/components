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
package org.talend.components.azurestorage.blob.runtime;

import java.io.File;

import org.talend.components.api.component.runtime.Sink;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.azurestorage.blob.tazurestorageput.TAzureStoragePutProperties;
import org.talend.daikon.properties.ValidationResult;

public class AzureStoragePutSink extends AzureStorageSourceOrSink implements Sink {

    private static final long serialVersionUID = 15461498966661L;

    @Override
    public WriteOperation<?> createWriteOperation() {
        return new AzureStorageWriteOperation(this);
    }

    public TAzureStoragePutProperties getPutProperties() {
        return (TAzureStoragePutProperties) properties;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        super.validate(container);
        if (this.properties instanceof TAzureStoragePutProperties) {
            TAzureStoragePutProperties p = (TAzureStoragePutProperties) this.properties;
            String folder = p.localFolder.getValue();
            // checks local folder
            if (!new File(folder).exists()) {
                ValidationResult vr = new ValidationResult();
                vr.setMessage("The local folder cannot be empty."); //$NON-NLS-1$
                vr.setStatus(ValidationResult.Result.ERROR);
                return vr;
            }
            // checks file list if set.
            if (p.useFileList.getValue() && p.files.fileMask.getValue().size() == 0) {
                ValidationResult vr = new ValidationResult();
                vr.setMessage("The file list cannot be empty."); //$NON-NLS-1$
                vr.setStatus(ValidationResult.Result.ERROR);
                return vr;
            }
        }
        // everything is OK.
        return ValidationResult.OK;
    }
}
