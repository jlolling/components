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

import org.talend.components.common.dataset.DatasetProperties;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class ElasticSearchDatasetProperties extends PropertiesImpl
        implements DatasetProperties<ElasticSearchDatastoreProperties> {

    public final ReferenceProperties<ElasticSearchDatastoreProperties> datastoreRef = new ReferenceProperties<>("datastoreRef",
            ElasticSearchDatastoreDefinition.NAME);

    public Property<String> index = PropertyFactory.newString("index").setRequired();

    public Property<String> type = PropertyFactory.newString("type").setRequired();

    // public SchemaProperties main = new SchemaProperties("main");

    public ElasticSearchDatasetProperties(String name) {
        super(name);
    }

    @Override
    public ElasticSearchDatastoreProperties getDatastoreProperties() {
        return datastoreRef.getReference();
    }

    @Override
    public void setDatastoreProperties(ElasticSearchDatastoreProperties datastoreProperties) {
        datastoreRef.setReference(datastoreProperties);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(index);
        mainForm.addRow(type);
        // mainForm.addRow(main.getForm(Form.MAIN));
    }

}
