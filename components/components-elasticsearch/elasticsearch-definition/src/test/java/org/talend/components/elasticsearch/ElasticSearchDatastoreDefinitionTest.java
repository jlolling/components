// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.components.elasticsearch;

import org.junit.Ignore;
import org.junit.Test;

import org.talend.daikon.runtime.RuntimeInfo;

import static org.junit.Assert.assertEquals;

public class ElasticSearchDatastoreDefinitionTest {

    private final ElasticSearchDatastoreDefinition datastoreDefinition = new ElasticSearchDatastoreDefinition();

    /**
    * Check {@link ElasticSearchDatastoreDefinition#getRuntimeInfo(ElasticSearchDatastoreProperties) returns RuntimeInfo,
    * which runtime class name is "org.talend.components.elasticsearch.runtime_2_4.ElasticSearchDatastoreRuntime"
    */
    @Test
    @Ignore("This can't work unless the runtime jar is already installed in maven!")
    public void testRuntimeInfo() {
        RuntimeInfo runtimeInfo = datastoreDefinition.getRuntimeInfo(null);
        assertEquals("org.talend.components.elasticsearch.runtime_2_4.ElasticSearchDatastoreRuntime", runtimeInfo.getRuntimeClassName());
    }
}
