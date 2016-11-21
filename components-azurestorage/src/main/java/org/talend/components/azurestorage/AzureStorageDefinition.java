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
package org.talend.components.azurestorage;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.runtime.DependenciesReader;
import org.talend.components.api.component.runtime.SimpleRuntimeInfo;
import org.talend.components.api.component.runtime.SourceOrSink;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 * Class AzureStorageDefinition.
 */
public abstract class AzureStorageDefinition extends AbstractComponentDefinition {

    /** Azure storage account key. */
    public static final String RETURN_ACCOUNT_KEY = "accountKey"; //$NON-NLS-1$

    /** RETURN_ACCOUNT_KEY_PROP. */
    public static final Property<String> RETURN_ACCOUNT_KEY_PROP = PropertyFactory.newString(RETURN_ACCOUNT_KEY);

    /** Azure storage account. */
    public static final String RETURN_ACCOUNT_NAME = "accountName"; //$NON-NLS-1$

    /** RETURN_ACCOUNT_NAME_PROP. */
    public static final Property<String> RETURN_ACCOUNT_NAME_PROP = PropertyFactory.newString(RETURN_ACCOUNT_NAME);

    /** Azure storage container */
    public static final String RETURN_CONTAINER = "container"; //$NON-NLS-1$

    public static final Property<String> RETURN_CONTAINER_PROP = PropertyFactory.newString(RETURN_CONTAINER);

    /**
     * Instantiates a new AzureStorageDefinition(String componentName).
     *
     * @param componentName {@link String} component name
     */
    public AzureStorageDefinition(String componentName) {
        super(componentName);
        setupI18N(new Property<?>[] { RETURN_ERROR_MESSAGE_PROP, RETURN_ACCOUNT_KEY_PROP, RETURN_ACCOUNT_NAME_PROP,
                RETURN_CONTAINER_PROP });
    }

    @Override
    public String[] getFamilies() {
        return new String[] { "Cloud/Azure Storage" }; //$NON-NLS-1$
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

    /**
     * getCommonRuntimeInfo.
     *
     * @param classLoader {@link ClassLoader} class loader
     * @param clazz {@link SourceOrSink} clazz
     * @return {@link RuntimeInfo} runtime info
     */
    public static RuntimeInfo getCommonRuntimeInfo(ClassLoader classLoader, Class<? extends SourceOrSink> clazz) {
        return new SimpleRuntimeInfo(classLoader,
                DependenciesReader.computeDependenciesFilePath("org.talend.components", "components-azurestorage"),
                clazz.getCanonicalName());
    }

    public String getMavenGroupId() {
        return "org.talend.components";
    }

    public String getMavenArtifactId() {
        return "components-azurestorage";
    }

    @Override
    public boolean isStartable() {
        return true;
    }
}
