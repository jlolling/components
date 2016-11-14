
package org.talend.components.azurestorage;

import org.talend.components.api.AbstractComponentFamilyDefinition;
import org.talend.components.api.ComponentInstaller;
import org.talend.components.api.Constants;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionDefinition;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerdelete.TAzureStorageContainerDeleteDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistDefinition;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListDefinition;
import org.talend.components.azurestorage.tazurestoragedelete.TAzureStorageDeleteDefinition;
import org.talend.components.azurestorage.tazurestorageget.TAzureStorageGetDefinition;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListDefinition;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutDefinition;

import aQute.bnd.annotation.component.Component;

/**
 * Install all of the definitions provided for the Azure Storage family of components.
 */
@Component(name = Constants.COMPONENT_INSTALLER_PREFIX + AzureStorageFamilyDefinition.NAME, provide = ComponentInstaller.class)
public class AzureStorageFamilyDefinition extends AbstractComponentFamilyDefinition implements ComponentInstaller {

    /**
     * FIXME - Remove "DEV" suffix when OK.
     */
    public static final String NAME = "Azure Storage DEV"; //$NON-NLS-1$

    public AzureStorageFamilyDefinition() {
        super(NAME, new TAzureStorageConnectionDefinition(), new TAzureStorageContainerExistDefinition(),
                new TAzureStorageContainerCreateDefinition(), new TAzureStorageContainerDeleteDefinition(),
                new TAzureStorageContainerListDefinition(), new TAzureStorageListDefinition(),
                new TAzureStorageDeleteDefinition(), new TAzureStorageGetDefinition(), new TAzureStoragePutDefinition());
    }

    @Override
    public void install(ComponentFrameworkContext ctx) {
        ctx.registerComponentFamilyDefinition(this);
    }
}