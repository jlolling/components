package org.talend.components.snowflake.test;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.talend.components.api.service.ComponentService;
import org.talend.components.service.spring.SpringTestApp;
import org.talend.daikon.definition.service.DefinitionRegistryService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringTestApp.class)
@Ignore
public class SpringSnowflakeTestIT extends SnowflakeIntegration {

    @Inject
    ComponentService componentService;

    @Override
    public ComponentService getComponentService() {
        return componentService;
    }

    @Inject
    DefinitionRegistryService osgiDefinitionService;

    @Override
    public DefinitionRegistryService getDefinitionRegistry() {
        return osgiDefinitionService;
    }

}
