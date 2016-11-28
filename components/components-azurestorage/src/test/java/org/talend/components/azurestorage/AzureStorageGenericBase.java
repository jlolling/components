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

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;

public abstract class AzureStorageGenericBase extends AbstractComponentTest {

    @Inject
    ComponentService compServ;

    @Override
    public ComponentService getComponentService() {
        System.out.println("getting component service..." + compServ);
        return compServ;
    }

    @Test
    public void testAllComponentsAreRegistered() {
        // assertComponentIsRegistered(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageContainerCreateDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageContainerDeleteDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageContainerExistDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageContainerListDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageDeleteDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageGetDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageListDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStoragePutDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageInputTableDefinition.COMPONENT_NAME);
        // assertComponentIsRegistered(TAzureStorageOutputTableDefinition.COMPONENT_NAME);
    }
}
