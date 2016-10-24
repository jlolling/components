// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.salesforce.runtime;

import java.io.IOException;

import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.salesforce.tsalesforcebulkexec.TSalesforceBulkExecProperties;
import org.talend.daikon.properties.ValidationResult;

import com.sforce.async.AsyncApiException;
import com.sforce.ws.ConnectionException;

public class SalesforceBulkExecSourceOrSink extends SalesforceSourceOrSink {

    private static final long serialVersionUID = -2278136514703418658L;

    protected SalesforceBulkRuntime bulkRuntime;

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        ValidationResult vr = new ValidationResult();
        try {
            connect(container);
            doBulkAction(container);
        } catch (IOException ex) {
            return exceptionToValidationResult(ex);
        } finally {
            if (needCloseAtOnceAfterLoad() && (bulkRuntime != null)) {
                try {
                    bulkRuntime.close();
                } catch (IOException e) {
                    // close quietly
                }
            }
        }
        return vr;
    }

    protected boolean needCloseAtOnceAfterLoad() {
        return true;
    }

    private void doBulkAction(RuntimeContainer container) throws IOException {
        if (!isPropertiesValid()) {
            return;
        }

        TSalesforceBulkExecProperties props = (TSalesforceBulkExecProperties) properties;
        props.connection.bulkConnection.setValue(true);

        bulkRuntime = new SalesforceBulkRuntime(this, container);
        bulkRuntime.setConcurrencyMode(props.bulkProperties.concurrencyMode.getValue());
        bulkRuntime.setAwaitTime(props.bulkProperties.waitTimeCheckBatchState.getValue());

        try {
            // We only support CSV file for bulk output
            bulkRuntime.executeBulk(props.module.moduleName.getStringValue(), props.outputAction.getValue(),
                    props.upsertKeyColumn.getStringValue(), "csv", props.bulkFilePath.getStringValue(),
                    props.bulkProperties.bytesToCommit.getValue(), props.bulkProperties.rowsToCommit.getValue());
        } catch (AsyncApiException | ConnectionException e) {
            throw new IOException(e);
        }
    }

    protected boolean isPropertiesValid() {
        if ((properties != null) && (properties instanceof TSalesforceBulkExecProperties)) {
            return true;
        }

        return false;
    }

}
