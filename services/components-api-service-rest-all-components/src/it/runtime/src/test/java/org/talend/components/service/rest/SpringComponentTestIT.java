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
package org.talend.components.service.rest;

import static org.hamcrest.Matchers.*;

import java.util.Collection;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.talend.components.api.RuntimableDefinition;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.daikon.definition.service.DefinitionRegistryService;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.runtime.RuntimeInfo;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringComponentTestIT extends AbstractComponentTest {

    @Inject
    DefinitionRegistryService definitionRegistry;

    @Inject
    private ComponentService componentService;

    @Override
    public ComponentService getComponentService() {
        return componentService;
    }

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void checkRuntimes() {
        Collection<RuntimableDefinition> runtimableDefinitions = definitionRegistry
                .getDefinitionsMapByType(RuntimableDefinition.class).values();
        for (RuntimableDefinition runtimeDef : runtimableDefinitions) {
            Properties properties = definitionRegistry.createProperties(runtimeDef, "root");
            RuntimeInfo runtimeInfo = runtimeDef.getRuntimeInfo(properties, null);
            checkRuntimeInfo(runtimeDef, runtimeInfo);
        }
    }

    public void checkRuntimeInfo(RuntimableDefinition runtimeDef, RuntimeInfo runtimeInfo) {
        if (runtimeInfo != null) {
            try (SandboxedInstance sandboxI = RuntimeUtil.createRuntimeClass(runtimeInfo,
                    runtimeDef.getClass().getClassLoader())) {
                errorCollector.checkThat(sandboxI.getInstance(), notNullValue());
            }
        } // else no runtime info, this is OK.
    }

}
