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

/**
 * Class TAzureStorageConnectionProperties.
 */
public class TAzureStorageConnectionProperties extends ComponentPropertiesImpl
        implements ComponentReferencePropertiesEnclosing, AzureStorageProvideConnectionProperties {

    public enum Protocol {
        HTTP,
        HTTPS
    }

    /** accountKey - The Azure Storage Account Key. */
    public Property<String> accountKey = PropertyFactory.newString("accountKey").setRequired(); //$NON-NLS-1$

    /** accountName - The Azure Storage Account Name. */
    public Property<String> accountName = PropertyFactory.newString("accountName").setRequired(); //$NON-NLS-1$

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    public Property<Protocol> protocol = newEnum("protocol", Protocol.class).setRequired(); //$NON-NLS-1$

    public ComponentReferenceProperties referencedComponent = new ComponentReferenceProperties("referencedComponent", this); //$NON-NLS-1$

    /**
     * Instantiates a new TAzureStorageConnectionProperties(String name).
     *
     * @param name {@link String} name
     */
    public TAzureStorageConnectionProperties(String name) {
        super(name);
    }

    @Override
    public void afterReferencedComponent() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.REFERENCE));
    }

    @Override
    public TAzureStorageConnectionProperties getConnectionProperties() {
        return this;
    }

    /**
     * getReferencedComponentId.
     *
     * @return {@link String} string
     */
    public String getReferencedComponentId() {
        return referencedComponent.componentInstanceId.getValue();
    }

    /**
     * getReferencedConnectionProperties.
     *
     * @return {@link TAzureStorageConnectionProperties} t azure storage connection properties
     */
    public TAzureStorageConnectionProperties getReferencedConnectionProperties() {
        TAzureStorageConnectionProperties refProps = (TAzureStorageConnectionProperties) referencedComponent.componentProperties;
        if (refProps != null) {
            return refProps;
        }
        return null;
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
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        protocol.setValue(Protocol.HTTPS);
        accountName.setValue("");
        accountKey.setValue("");
    }
}
