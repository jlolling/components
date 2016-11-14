package org.talend.components.azurestorage.helpers;

public class RemoteBlob {

    public String prefix;

    public Boolean include = false;

    public RemoteBlob(String prefix, Boolean include) {
        this.prefix = prefix;
        this.include = include;
    }
}
