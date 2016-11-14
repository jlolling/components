package org.talend.components.azurestorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.components.api.service.common.ComponentServiceImpl;
import org.talend.components.azurestorage.AzureStorageFamilyDefinition;

@SuppressWarnings("nls")
public class AzureStorageTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private ComponentService componentService;

    @Before
    public void initializeComponentRegistryAndService() {
        // reset the component service
        componentService = null;
    }

    // default implementation for pure java test.
    public ComponentService getComponentService() {
        if (componentService == null) {
            DefinitionRegistry testComponentRegistry = new DefinitionRegistry();

            testComponentRegistry.registerComponentFamilyDefinition(new AzureStorageFamilyDefinition());
            componentService = new ComponentServiceImpl(testComponentRegistry);
        }
        return componentService;
    }

    @Test
    public void testAzureStorageRuntime() throws Exception {

    }

}
