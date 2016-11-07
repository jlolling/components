// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.api.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.Writer;
import org.talend.components.api.exception.error.ComponentsApiErrorCode;
import org.talend.components.api.service.ComponentService;
import org.talend.daikon.definition.Definition;
import org.talend.daikon.definition.service.DefinitionRegistryService;
import org.talend.daikon.exception.TalendRuntimeException;

public abstract class AbstractComponentTest {

    // for benchmarking the apis, one suggestion is to use http://openjdk.java.net/projects/code-tools/jmh/.
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    abstract public ComponentService getComponentService();

    abstract public DefinitionRegistryService getDefinitionRegistry();

    @Test
    public void testAlli18n() {
        ComponentTestUtils.testAlli18n(getComponentService(), errorCollector);
    }

    /**
     * @deprecated use {@link #assertAlli18nAreSet()}
     */
    @Test
    @Deprecated
    public void testAllImages() {
        ComponentTestUtils.testAllImages(getComponentService());
    }

    @Test
    public void asserAllImagesAreProvided() {
        ComponentTestUtils.testAllImages(getDefinitionRegistry());
    }

    public void assertComponentIsRegistered(String componentName) {
        try {
            Definition componentDefinition = getDefinitionRegistry().getDefinitionsMapByType(Definition.class).get(componentName);
            assertNotNull(componentDefinition);
        } catch (TalendRuntimeException ce) {
            if (ce.getCode() == ComponentsApiErrorCode.WRONG_COMPONENT_NAME) {
                fail("Could not find component [], please check the registered component familly is in package org.talend.components");
            } else {
                throw ce;
            }
        }
    }

    public static Map<String, Object> getConsolidatedResults(Result result, Writer writer) {
        List<Result> results = new ArrayList<>();
        results.add(result);
        Map<String, Object> resultMap = writer.getWriteOperation().finalize(results, null);
        return resultMap;
    }

}
