package ${package};

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.daikon.definition.service.DefinitionRegistryService;

public class ${componentName}TestBase extends AbstractComponentTest {

    @Inject
    private ComponentService componentService;

    @Inject
    private DefinitionRegistryService definitionService;

    public ComponentService getComponentService(){
        return componentService;
    }
    
    public DefinitionRegistryService getDefinitionRegistry() {
        return definitionService;
    }
    
    @Test
    public void componentHasBeenRegistered(){
        assertComponentIsRegistered("${componentName}");
    }
}
