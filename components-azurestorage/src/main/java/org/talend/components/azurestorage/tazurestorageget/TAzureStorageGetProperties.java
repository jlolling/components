package org.talend.components.azurestorage.tazurestorageget;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.talend.components.azurestorage.AzureStorageBlobProperties;
import org.talend.components.azurestorage.helpers.RemoteBlobsGetTable;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageGetProperties extends AzureStorageBlobProperties {

    public Property<String> localFolder = PropertyFactory.newString("localFolder"); //$NON-NLS-1$

    public RemoteBlobsGetTable remoteBlobsGet = new RemoteBlobsGetTable("remoteBlobsGet"); //$NON-NLS-1$

    public TAzureStorageGetProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(localFolder);
        mainForm.addRow(widget(remoteBlobsGet).setWidgetType(Widget.TABLE_WIDGET_TYPE));
        mainForm.addRow(dieOnError);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        localFolder.setValue("");
    }
}
