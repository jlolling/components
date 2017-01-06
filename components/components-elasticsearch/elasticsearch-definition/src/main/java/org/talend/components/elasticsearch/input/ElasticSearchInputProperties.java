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

package org.talend.components.elasticsearch.input;

import org.talend.components.api.properties.ComponentPropertiesImpl;

import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.common.io.IOProperties;
import org.talend.components.elasticsearch.ElasticSearchDatasetDefinition;
import org.talend.components.elasticsearch.ElasticSearchDatasetProperties;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class ElasticSearchInputProperties extends ComponentPropertiesImpl implements IOProperties {

    public ReferenceProperties<ElasticSearchDatasetProperties> datasetRef = new ReferenceProperties<>("datasetRef",
        ElasticSearchDatasetDefinition.NAME);

    public Property<String> query = PropertyFactory.newString("query");

    public ElasticSearchInputProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(Widget.widget(query).setWidgetType(Widget.TEXT_AREA_WIDGET_TYPE));
    }

    @Override
    public ElasticSearchDatasetProperties getDatasetProperties() {
        return datasetRef.getReference();
    }

    @Override
    public void setDatasetProperties(DatasetProperties datasetProperties) {
        datasetRef.setReference(datasetProperties);
    }
}
