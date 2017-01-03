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
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.components.api.properties.ComponentReferenceProperties;
import org.talend.components.api.properties.ComponentReferencePropertiesEnclosing;
import org.talend.components.azurestorage.AzureStorageProvideConnectionProperties;
import org.talend.components.azurestorage.blob.runtime.AzureStorageSourceOrSink;
import org.talend.daikon.properties.PresentationItem;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.properties.service.Repository;

/**
 * Class TAzureStorageConnectionProperties.
 */
public class TAzureStorageConnectionProperties extends ComponentPropertiesImpl
        implements ComponentReferencePropertiesEnclosing, AzureStorageProvideConnectionProperties {

    private static final long serialVersionUID = 5588521568261191377L;

    // Only for the wizard use
    public Property<String> name = newString("name").setRequired();

    public static final String FORM_WIZARD = "Wizard";

    private String repositoryLocation;
    //

    public enum Protocol {
        HTTP,
        HTTPS
    }

    /** accountKey - The Azure Storage Account Key. */
    public Property<String> accountKey = PropertyFactory.newString("accountKey"); //$NON-NLS-1$

    /** accountName - The Azure Storage Account Name. */
    public Property<String> accountName = PropertyFactory.newString("accountName"); //$NON-NLS-1$

    public Property<Boolean> useSharedAccessSignature = PropertyFactory.newBoolean("useSharedAccessSignature");

    public Property<String> sharedAccessSignature = PropertyFactory.newString("sharedAccessSignature");//$NON-NLS-1$

    public Property<Protocol> protocol = newEnum("protocol", Protocol.class).setRequired(); //$NON-NLS-1$

    public ComponentReferenceProperties referencedComponent = new ComponentReferenceProperties("referencedComponent", this); //$NON-NLS-1$

    public PresentationItem testConnection = new PresentationItem("testConnection", "Test connection");

    public TAzureStorageConnectionProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        protocol.setValue(Protocol.HTTPS);
        accountName.setValue("");
        accountKey.setValue("");
        useSharedAccessSignature.setValue(false);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(accountName);
        mainForm.addColumn(accountKey);
        mainForm.addRow(protocol);
        mainForm.addRow(useSharedAccessSignature);
        mainForm.addRow(sharedAccessSignature);

        Form refForm = Form.create(this, Form.REFERENCE);
        Widget compListWidget = widget(referencedComponent).setWidgetType(Widget.COMPONENT_REFERENCE_WIDGET_TYPE);
        referencedComponent.componentType.setValue(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        refForm.addRow(compListWidget);
        refForm.addRow(mainForm);

        Form wizardForm = Form.create(this, FORM_WIZARD);
        wizardForm.addRow(name);
        wizardForm.addRow(accountName);
        wizardForm.addRow(accountKey);
        wizardForm.addRow(protocol);
        wizardForm.addRow(useSharedAccessSignature);
        wizardForm.addRow(sharedAccessSignature);
        wizardForm.addColumn(widget(testConnection).setLongRunning(true).setWidgetType(Widget.BUTTON_WIDGET_TYPE));

    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        String refComponentIdValue = getReferencedComponentId();
        boolean useOtherConnection = refComponentIdValue != null
                && refComponentIdValue.startsWith(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        if (form.getName().equals(Form.MAIN) || form.getName().equals(FORM_WIZARD)) {
            form.getWidget(accountName).setHidden(useOtherConnection);
            form.getWidget(accountKey).setHidden(useOtherConnection);
            form.getWidget(protocol).setHidden(useOtherConnection);
            form.getWidget(useSharedAccessSignature.getName()).setHidden(useOtherConnection);
            form.getWidget(sharedAccessSignature.getName()).setHidden(useOtherConnection);
            boolean useSAS = useSharedAccessSignature.getValue();
            if (!useOtherConnection) {
                form.getWidget(accountName).setHidden(useSAS);
                form.getWidget(accountKey).setHidden(useSAS);
                form.getWidget(protocol).setHidden(useSAS);
                form.getWidget(sharedAccessSignature.getName()).setHidden(!useSAS);
            }
        }
    }

    @Override
    public void afterReferencedComponent() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.REFERENCE));
    }

    public void afterUseSharedAccessSignature() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.REFERENCE));
        refreshLayout(getForm(FORM_WIZARD));
    }

    public ValidationResult validateTestConnection() throws Exception {
        ValidationResult vr = AzureStorageSourceOrSink.validateConnection(this);
        if (vr.getStatus() == ValidationResult.Result.OK) {
            vr.setMessage("Connection successful");
            getForm(FORM_WIZARD).setAllowForward(true);
            getForm(FORM_WIZARD).setAllowFinish(true);
        } else {
            getForm(FORM_WIZARD).setAllowForward(false);
        }
        return vr;
    }

    public ValidationResult afterFormFinishWizard(Repository<Properties> repo) throws Exception {
        System.out.println("Repo " + repo);
        ValidationResult vr = AzureStorageSourceOrSink.validateConnection(this);
        if (vr.getStatus() != ValidationResult.Result.OK) {
            return vr;
        }

        System.out.println("RepoLoc " + repositoryLocation);
        String s = repo.storeProperties(this, this.name.getValue(), repositoryLocation, null);
        System.out.println("RepoLoc stored " + s);

        return ValidationResult.OK;
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

    public TAzureStorageConnectionProperties setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
        return this;
    }
}
