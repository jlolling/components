/**
 * 
 */
package org.talend.components.azurestorage.tazurestorageconnection;

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;

import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.components.api.properties.ComponentReferenceProperties;
import org.talend.components.api.properties.ComponentReferencePropertiesEnclosing;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class TAzureStorageConnectionProperties extends ComponentPropertiesImpl
        implements ComponentReferencePropertiesEnclosing, AzureStorageProvideConnectionProperties {

    public enum Protocol {
        HTTP,
        HTTPS
    }

    public Property<Protocol> protocol = newEnum("protocol", Protocol.class).setRequired(); //$NON-NLS-1$

    public Property<String> accountName = PropertyFactory.newString("accountName").setRequired(); //$NON-NLS-1$

    public Property<String> accountKey = PropertyFactory.newString("accountKey").setRequired(); //$NON-NLS-1$

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    public ComponentReferenceProperties referencedComponent = new ComponentReferenceProperties("referencedComponent", this); //$NON-NLS-1$

    public TAzureStorageConnectionProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        protocol.setValue(Protocol.HTTPS);
        accountName.setValue("");
        accountKey.setValue("");
        dieOnError.setValue(false);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(accountName);
        mainForm.addColumn(accountKey);
        mainForm.addRow(protocol);

        Form refForm = Form.create(this, Form.REFERENCE);
        Widget compListWidget = widget(referencedComponent).setWidgetType(Widget.COMPONENT_REFERENCE_WIDGET_TYPE);
        referencedComponent.componentType.setValue(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        refForm.addRow(compListWidget);
        refForm.addRow(mainForm);
        
        // don't add dieOnError on reference form
        mainForm.addRow(dieOnError);
    }

    @Override
    public void afterReferencedComponent() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.REFERENCE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        String refComponentIdValue = getReferencedComponentId();
        boolean useOtherConnection = refComponentIdValue != null
                && refComponentIdValue.startsWith(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        if (form.getName().equals(Form.MAIN)) {
            form.getWidget(accountName).setHidden(useOtherConnection);
            form.getWidget(accountKey).setHidden(useOtherConnection);
            form.getWidget(protocol).setHidden(useOtherConnection);
        }
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return this;
    }

    public String getReferencedComponentId() {
        return referencedComponent.componentInstanceId.getValue();
    }

    public TAzureStorageConnectionProperties getReferencedConnectionProperties() {
        TAzureStorageConnectionProperties refProps = (TAzureStorageConnectionProperties) referencedComponent.componentProperties;
        if (refProps != null) {
            return refProps;
        }
        return null;
    }

}
