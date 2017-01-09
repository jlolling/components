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

package ${package}.input;

import ${packageTalend}.api.properties.ComponentPropertiesImpl;

import ${packageTalend}.common.dataset.DatasetProperties;
import ${packageTalend}.common.io.IOProperties;
import ${packageTalend}.${componentNameLowerCase}.${componentNameClass}DatasetDefinition;
import ${packageTalend}.${componentNameLowerCase}.${componentNameClass}DatasetProperties;
import ${packageDaikon}.properties.ReferenceProperties;
import ${packageDaikon}.properties.presentation.Form;

public class ${componentNameClass}InputProperties extends ComponentPropertiesImpl implements IOProperties {

    public ReferenceProperties<${componentNameClass}DatasetProperties> datasetRef = new ReferenceProperties<>("datasetRef",
        ${componentNameClass}DatasetDefinition.NAME);

    public ${componentNameClass}InputProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
    }

    @Override
    public ${componentNameClass}DatasetProperties getDatasetProperties() {
        return datasetRef.getReference();
    }

    @Override
    public void setDatasetProperties(DatasetProperties datasetProperties) {
        datasetRef.setReference(datasetProperties);
    }
}
