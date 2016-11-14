package org.talend.components.azurestorage.helpers;

public class RemoteBlobGet extends RemoteBlob {

    /**
     * {@link #create} : Create parent directories
     */
    public Boolean create;

    public RemoteBlobGet(String prefix, Boolean include, Boolean create) {
        super(prefix, include);
        this.create = create;
    }

    public RemoteBlobGet(String prefix, Boolean include) {
        super(prefix, include);
        this.create = false;
    }
}
