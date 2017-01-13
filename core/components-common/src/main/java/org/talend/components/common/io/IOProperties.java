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
package org.talend.components.common.io;

import org.talend.components.common.dataset.DatasetProperties;
import org.talend.daikon.properties.Properties;

public interface IOProperties<DatasetPropT extends DatasetProperties> extends Properties {

    public DatasetPropT getDatasetProperties();

    public void setDatasetProperties(DatasetPropT datasetProperties);

}
