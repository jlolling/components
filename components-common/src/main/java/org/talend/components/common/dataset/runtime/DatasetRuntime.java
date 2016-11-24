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
package org.talend.components.common.dataset.runtime;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.talend.components.api.component.runtime.RuntimableRuntime;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.daikon.properties.Properties;

/**
 * Provides access to runtime methods on a dataset.
 *
 * The runtime objects are created by via a
 * {@link org.talend.components.common.dataset.DatasetDefinition#getRuntimeInfo(Properties, Object)}, where the
 * properties contain all of the configuration necessary to access the dataset.
 * 
 * @param <DatasetPropT> The properties that specify a dataset.
 */
public interface DatasetRuntime<DatasetPropT extends DatasetProperties> extends RuntimableRuntime<DatasetPropT> {

    /** @return a Schema for the dataset. */
    Schema getSchema();

    /**
     * Discover a sample of data from the dataset.
     *
     * There is no requirement for sort order or start position, unless specified in the dataset properties.
     *
     * @param limit the maximum number of records to return.
     * @return a list of IndexedRecords that the dataset can support.
     */
    Iterable<? extends IndexedRecord> getSample(int limit);
}
