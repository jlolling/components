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

package org.talend.components.elasticsearch;

import java.net.MalformedURLException;
import java.net.URL;

import org.talend.components.api.component.runtime.DependenciesReader;
import org.talend.components.api.component.runtime.JarRuntimeInfo;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.common.datastore.DatastoreDefinition;
import org.talend.components.elasticsearch.input.ElasticSearchInputDefinition;
import org.talend.components.elasticsearch.output.ElasticSearchOutputDefinition;
import org.talend.daikon.definition.I18nDefinition;
import org.talend.daikon.runtime.RuntimeInfo;

public class ElasticSearchDatastoreDefinition extends I18nDefinition
        implements DatastoreDefinition<ElasticSearchDatastoreProperties> {

    public static final String RUNTIME_2_4 = "org.talend.components.elasticsearch.runtime_2_4.ElasticSearchDatastoreRuntime";

    public static final String NAME = ElasticSearchComponentFamilyDefinition.NAME + "Datastore";

    public ElasticSearchDatastoreDefinition() {
        super(NAME);
    }

    @Override
    public Class<ElasticSearchDatastoreProperties> getPropertiesClass() {
        return ElasticSearchDatastoreProperties.class;
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ElasticSearchDatastoreProperties properties) {
        try {
            switch (properties.version.getValue()) {
            case V_2_4:
            default:
                return new JarRuntimeInfo(new URL("mvn:org.talend.components/elasticsearch-runtime"),
                        DependenciesReader.computeDependenciesFilePath("org.talend.components", "elasticsearch-runtime"),
                        RUNTIME_2_4);
            }
        } catch (MalformedURLException e) {
            throw new ComponentException(e);
        }
    }

    @Override
    public String getImagePath() {
        return NAME + "_icon32.png";
    }

    @Override
    public DatasetProperties createDatasetProperties(ElasticSearchDatastoreProperties storeProp) {
        ElasticSearchDatasetProperties setProp = new ElasticSearchDatasetProperties(ElasticSearchDatasetDefinition.NAME);
        setProp.init();
        setProp.setDatastoreProperties(storeProp);
        return setProp;
    }

    @Override
    public String getInputCompDefinitionName() {
        return ElasticSearchInputDefinition.NAME;
    }

    @Override
    public String getOutputCompDefinitionName() {
        return ElasticSearchOutputDefinition.NAME;
    }

}
