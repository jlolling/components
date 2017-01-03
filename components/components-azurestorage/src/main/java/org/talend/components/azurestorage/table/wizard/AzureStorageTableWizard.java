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
package org.talend.components.azurestorage.table.wizard;

import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.azurestorage.table.AzureStorageTableProperties;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.properties.presentation.Form;

public class AzureStorageTableWizard extends ComponentWizard {

    private AzureStorageTableProperties properties;

    public AzureStorageTableWizard(ComponentWizardDefinition definition, String repositoryLocation) {
        super(definition, repositoryLocation);

        properties = new AzureStorageTableProperties("tProperties").setRepositoryLocation(repositoryLocation);
        properties.init();
        addForm(properties.getForm(Form.MAIN));
    }

    public void setupProperties(TAzureStorageConnectionProperties cPropsOther) {
        System.out.println("TW::setupProperties::Conn");
        properties.connection.copyValuesFrom(cPropsOther);
    }

    public void setupProperties(AzureStorageTableProperties cPropsOther) {
        System.out.println("TW::setupProperties::Tblprops");
        properties.copyValuesFrom(cPropsOther);
    }

}
