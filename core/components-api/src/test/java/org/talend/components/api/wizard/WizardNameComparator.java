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
package org.talend.components.api.wizard;

import java.util.Comparator;

public class WizardNameComparator implements Comparator<ComponentWizard> {

    @Override
    public int compare(ComponentWizard o1, ComponentWizard o2) {
        return o1.getDefinition().getName().compareTo(o2.getDefinition().getName());
    }
}
