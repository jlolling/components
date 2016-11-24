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
package org.talend.components.snowflake;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.daikon.properties.presentation.Form;

/**
 * Handles the creating a connection and creating the modules associated with the connection.
 */
public class SnowflakeConnectionWizard extends ComponentWizard {

    SnowflakeConnectionProperties cProps;

    SnowflakeTableListProperties tProps;

    SnowflakeConnectionWizard(ComponentWizardDefinition def, String repositoryLocation) {
        super(def, repositoryLocation);

        cProps = new SnowflakeConnectionProperties("connection");
        cProps.init();
        addForm(cProps.getForm(SnowflakeConnectionProperties.FORM_WIZARD));

        tProps = new SnowflakeTableListProperties("tProps").setConnection(cProps).setRepositoryLocation(getRepositoryLocation());
        tProps.init();
        addForm(tProps.getForm(Form.MAIN));
    }

    public boolean supportsProperties(ComponentProperties properties) {
        return properties instanceof SnowflakeConnectionProperties;
    }

    public void setupProperties(SnowflakeConnectionProperties cPropsOther) {
        cProps.copyValuesFrom(cPropsOther);
        tProps.setConnection(cProps);
    }

}
