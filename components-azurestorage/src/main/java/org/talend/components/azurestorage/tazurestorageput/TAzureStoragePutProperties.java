package org.talend.components.azurestorage.tazurestorageput;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.talend.components.azurestorage.AzureStorageBlobProperties;
import org.talend.components.azurestorage.helpers.FileMaskTable;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStoragePutProperties extends AzureStorageBlobProperties {

    public Property<String> localFolder = PropertyFactory.newString("localFolder"); //$NON-NLS-1$

    public Property<String> remoteFolder = PropertyFactory.newString("remoteFolder"); //$NON-NLS-1$

    public Property<Boolean> useFileList = PropertyFactory.newBoolean("useFileList"); //$NON-NLS-1$

    public FileMaskTable files = new FileMaskTable("files"); //$NON-NLS-1$

    public TAzureStoragePutProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(localFolder);
        mainForm.addRow(remoteFolder);
        mainForm.addRow(useFileList);
        mainForm.addRow(widget(files).setWidgetType(Widget.TABLE_WIDGET_TYPE));
        mainForm.addRow(dieOnError);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        localFolder.setValue("");
        remoteFolder.setValue("");
        useFileList.setValue(false);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        Boolean useFileLst = useFileList.getValue();
        form.getWidget(files.getName()).setHidden(!useFileLst);
    }

    public void afterUseFileList() {
        refreshLayout(getForm(Form.MAIN));
    }
}
