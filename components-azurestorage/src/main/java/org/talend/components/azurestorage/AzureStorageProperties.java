package org.talend.components.azurestorage;

import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.SchemaProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public abstract class AzureStorageProperties
        // extends ComponentPropertiesImpl//PR
        extends FixedConnectorsComponentProperties implements AzureStorageProvideConnectionProperties {

    public Property<String> container = PropertyFactory.newString("container"); //$NON-NLS-1$

    public TAzureStorageConnectionProperties connection = new TAzureStorageConnectionProperties("connection"); //$NON-NLS-1$

    public SchemaProperties schema = new SchemaProperties("schema"); //$NON-NLS-1$

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schema");

    public AzureStorageProperties(String name) {
        super(name);
    }

    public TAzureStorageConnectionProperties getConnectionProperties() {
        return connection.getConnectionProperties();
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        container.setValue("");
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form main = new Form(this, Form.MAIN);
        main.addRow(connection.getForm(Form.REFERENCE));
        main.addRow(container);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        for (Form childForm : connection.getForms()) {
            connection.refreshLayout(childForm);
        }
    }
    
    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(new PropertyPathConnector(Connector.MAIN_NAME, "schema")); //$NON-NLS-1$
        } else {
            return Collections.emptySet();
        }
    }

    
}
