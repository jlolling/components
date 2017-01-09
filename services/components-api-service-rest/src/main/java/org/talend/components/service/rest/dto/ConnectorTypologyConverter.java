//==============================================================================
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
//==============================================================================
package org.talend.components.service.rest.dto;

import java.beans.PropertyEditorSupport;

/**
 *
 */
public class ConnectorTypologyConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        final ConnectorTypology typology = ConnectorTypology.valueOf(text.toUpperCase());
        setValue(typology);
    }


}
