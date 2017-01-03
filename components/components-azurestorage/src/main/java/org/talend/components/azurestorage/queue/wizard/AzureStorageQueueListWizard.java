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

import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.properties.presentation.Form;

public class AzureStorageQueueListWizard extends ComponentWizard {

    private AzureStorageQueueListProperties properties;

    public AzureStorageQueueListWizard(ComponentWizardDefinition definition, String repositoryLocation) {
        super(definition, repositoryLocation);
        System.out.println("QLW::init");
        properties = new AzureStorageQueueListProperties("qProperties").setRepositoryLocation(repositoryLocation);
        properties.init();
        addForm(properties.getForm(Form.MAIN));
    }

    public void setupProperties(TAzureStorageConnectionProperties properties) {
        this.properties.setConnection(properties);
    }

}
