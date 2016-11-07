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
package org.talend.components.api.service.internal.osgi;

import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.daikon.definition.service.DefinitionRegistryService;

import aQute.bnd.annotation.component.Component;

@Component(provide = { DefinitionRegistry.class, DefinitionRegistryService.class })
public class DefinitionRegistryOsgi extends DefinitionRegistry {
    // OSGI service definition
}
