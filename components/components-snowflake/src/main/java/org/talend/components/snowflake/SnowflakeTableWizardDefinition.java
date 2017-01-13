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
package org.talend.components.snowflake;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.wizard.AbstractComponentWizardDefintion;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.WizardImageType;

public class SnowflakeTableWizardDefinition extends AbstractComponentWizardDefintion {

    public static final String COMPONENT_WIZARD_NAME = "snowflake.table"; //$NON-NLS-1$

    @Override
    public String getName() {
        return COMPONENT_WIZARD_NAME;
    }

    @Override
    public ComponentWizard createWizard(String location) {
        return new SnowflakeTableWizard(this, location);
    }

    @Override
    public boolean supportsProperties(Class<? extends ComponentProperties> propertiesClass) {
        return propertiesClass.isAssignableFrom(SnowflakeConnectionProperties.class);
    }

    @Override
    public ComponentWizard createWizard(ComponentProperties properties, String location) {
        SnowflakeTableWizard wizard = (SnowflakeTableWizard) createWizard(location);
        wizard.setupProperties((SnowflakeConnectionProperties) properties);
        return wizard;
    }

    @Override
    public String getPngImagePath(WizardImageType imageType) {
        switch (imageType) {
            case TREE_ICON_16X16:
                return "connectionWizardIcon.png"; //$NON-NLS-1$
            case WIZARD_BANNER_75X66:
                return "snowflakeWizardBanner.png"; //$NON-NLS-1$ //TODO: check this file

            default:
                // will return null
        }
        return null;
    }
}
