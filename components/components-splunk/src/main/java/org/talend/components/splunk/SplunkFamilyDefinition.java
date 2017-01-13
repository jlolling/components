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
package org.talend.components.splunk;

import org.talend.components.api.AbstractComponentFamilyDefinition;
import org.talend.components.api.ComponentInstaller;
import org.talend.components.api.Constants;

import aQute.bnd.annotation.component.Component;

/**
 * Install all of the definitions provided by the Splunk family of components.
 */
@Component(name = Constants.COMPONENT_INSTALLER_PREFIX + SplunkFamilyDefinition.NAME, provide = ComponentInstaller.class)
public class SplunkFamilyDefinition extends AbstractComponentFamilyDefinition implements ComponentInstaller {

    public static final String NAME = "Splunk";

    public SplunkFamilyDefinition() {
        super(NAME, new TSplunkEventCollectorDefinition());
    }

    @Override
    public void install(ComponentFrameworkContext ctx) {
        ctx.registerComponentFamilyDefinition(this);
    }
}
