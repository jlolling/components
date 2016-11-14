package org.talend.components.azurestorage.tazurestoragedelete;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.talend.components.azurestorage.AzureStorageBlobProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;

public class TAzureStorageDeleteProperties extends AzureStorageBlobProperties {

    public TAzureStorageDeleteProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(widget(remoteBlobs).setWidgetType(Widget.TABLE_WIDGET_TYPE));
        mainForm.addRow(dieOnError);
    }
}
