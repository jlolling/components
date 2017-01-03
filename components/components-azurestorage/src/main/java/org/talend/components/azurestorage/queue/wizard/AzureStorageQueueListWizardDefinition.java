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
package org.talend.components.azurestorage.queue.wizard;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.wizard.AbstractComponentWizardDefintion;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.WizardImageType;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;

public class AzureStorageQueueListWizardDefinition extends AbstractComponentWizardDefintion {

    public static final String COMPONENT_WIZARD_NAME = "azurestorage.queue";

    @Override
    public ComponentWizard createWizard(String location) {
        return new AzureStorageQueueListWizard(this, location);
    }

    @Override
    public ComponentWizard createWizard(ComponentProperties properties, String location) {
        System.out.println("QLWD::createWizard");

        AzureStorageQueueListWizard wizard = (AzureStorageQueueListWizard) createWizard(location);
        wizard.setupProperties((TAzureStorageConnectionProperties) properties);
        return wizard;
    }

    @Override
    public String getPngImagePath(WizardImageType imageType) {
        System.out.println("QLWD::getPngImagePath");

        return "queueWizardIcon.png"; //$NON-NLS-1$
    }

    @Override
    public boolean supportsProperties(Class<? extends ComponentProperties> propertiesClass) {
        System.out.println("QLWD::supportsProperties");
        return propertiesClass.isAssignableFrom(TAzureStorageConnectionProperties.class);
    }

    @Override
    public String getName() {
        return COMPONENT_WIZARD_NAME;
    }

    @Override
    public boolean isTopLevel() {
        System.out.println("QLWD::isTopLevel");
        return false;
    }

}
