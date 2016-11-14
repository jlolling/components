
package org.talend.components.azurestorage;

import java.util.Set;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.DependenciesReader;
import org.talend.components.api.component.runtime.SimpleRuntimeInfo;
import org.talend.components.api.component.runtime.SourceOrSink;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.runtime.RuntimeInfo;

public abstract class AzureStorageDefinition extends AbstractComponentDefinition {

    @Override
    public RuntimeInfo getRuntimeInfo(Properties properties, ConnectorTopology connectorTopology) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        // TODO Auto-generated method stub
        return null;
    }

    public static final String RETURN_ACCOUNT_KEY = "accountKey"; //$NON-NLS-1$

    public static final Property<String> RETURN_ACCOUNT_KEY_PROP = PropertyFactory.newString(RETURN_ACCOUNT_KEY);

    public static final String RETURN_ACCOUNT_NAME = "accountName"; //$NON-NLS-1$

    public static final Property<String> RETURN_ACCOUNT_NAME_PROP = PropertyFactory.newString(RETURN_ACCOUNT_NAME);

    public static final String RETURN_CONTAINER = "container"; //$NON-NLS-1$

    public static final Property<String> RETURN_CONTAINER_PROP = PropertyFactory.newString(RETURN_CONTAINER);

    public AzureStorageDefinition(String componentName) {
        super(componentName);
        setupI18N(new Property<?>[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_KEY_PROP, RETURN_ACCOUNT_NAME_PROP,
                RETURN_CONTAINER_PROP });
    }

    /**
     * FIXME - Remove "DEV" suffix when OK.
     */
    @Override
    public String[] getFamilies() {
        return new String[] { "Cloud/Azure Storage DEV" }; //$NON-NLS-1$
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_NAME_PROP, RETURN_ACCOUNT_KEY_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return AzureStorageProperties.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends ComponentProperties>[] getNestedCompatibleComponentPropertiesClass() {
        return new Class[] { TAzureStorageConnectionProperties.class };
    }

    public static RuntimeInfo getCommonRuntimeInfo(ClassLoader classLoader, Class<? extends SourceOrSink> clazz) {
        return new SimpleRuntimeInfo(classLoader,
                DependenciesReader.computeDependenciesFilePath("org.talend.components", "components-azurestorage"),
                clazz.getCanonicalName());
    }

    @Override
    public boolean isStartable() {
        return true;
    }
}
