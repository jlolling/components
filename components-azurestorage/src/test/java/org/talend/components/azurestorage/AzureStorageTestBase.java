package org.talend.components.azurestorage;

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.components.api.test.ComponentTestUtils;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionDefinition;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerdelete.TAzureStorageContainerDeleteDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListDefinition;
import org.talend.components.azurestorage.tazurestoragedelete.TAzureStorageDeleteDefinition;
import org.talend.components.azurestorage.tazurestorageget.TAzureStorageGetDefinition;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListDefinition;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutDefinition;

public class AzureStorageTestBase extends AbstractComponentTest {

    @Inject
    private ComponentService componentService;

    @Override
    public ComponentService getComponentService() {
        return componentService;
    }

    @Test
    public void componentHasBeenRegistered() {
        assertComponentIsRegistered(TAzureStorageConnectionDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerCreateDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerDeleteDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerExistDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageContainerListDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageDeleteDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageGetDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStorageListDefinition.COMPONENT_NAME);
        assertComponentIsRegistered(TAzureStoragePutDefinition.COMPONENT_NAME);
    }

    /**
    *
    */
    @Test
    public void testAlli18n() {
        ComponentTestUtils.testAlli18n(getComponentService(), errorCollector);
    }

}
