package org.talend.components.salesforce.runtime;

import java.util.ArrayList;
import java.util.List;

import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.salesforce.tsalesforcebulkexec.TSalesforceBulkExecProperties;

public class SalesforceBulkExecSource extends SalesforceBulkExecSourceOrSink implements BoundedSource {

    private static final long serialVersionUID = 9212050917956092986L;

    @Override
    public List<? extends BoundedSource> splitIntoBundles(long desiredBundleSizeBytes, RuntimeContainer adaptor)
            throws Exception {
        List<BoundedSource> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    @Override
    public long getEstimatedSizeBytes(RuntimeContainer adaptor) {
        return 0;
    }

    @Override
    public boolean producesSortedKeys(RuntimeContainer adaptor) {
        return false;
    }

    @Override
    public BoundedReader createReader(RuntimeContainer container) {
        if (!isPropertiesValid()) {
            return null;
        }

        return new SalesforceBulkExecReader(container, this, (TSalesforceBulkExecProperties) properties, bulkRuntime);
    }

    protected boolean needCloseAtOnceAfterLoad() {
        return false;
    }

}
