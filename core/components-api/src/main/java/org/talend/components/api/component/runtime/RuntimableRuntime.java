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
package org.talend.components.api.component.runtime;

import java.io.Serializable;

import org.talend.components.api.container.RuntimeContainer;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;

/**
 * Base interface for defining properties and methods common to Runtime interfaces, like {@link SourceOrSink} or
 * {@link ComponentDriverInitialization}.
 * 
 * A {@link RuntimableRuntime} may be passed between partititions for distributed operation and therefore must be
 * {@link Serializable}. This allows the {@link RuntimableRuntime} instance created in this "main program" to be sent
 * (in serialized form) to remote worker machines and reconstituted for each batch of elements being processed. A
 * {@link RuntimableRuntime} can have instance variable state, and non-transient instance variable state will be
 * serialized in the main program and then deserialized on remote worker machines.
 */
public interface RuntimableRuntime<P extends Properties> extends Serializable {

    /**
     * Applies the configuration from the {@link Properties} associated with this runtime class. This class also checks
     * that all required properties are properly set and coherent. No integrity check must be done like checking a
     * remote connection or checking a file exists here. This method shall be called before the job or flow is executed
     * and shall prevent execution if the ValidationResult has an ERROR status.
     * 
     * This method is expected to be called in a distributed environment on driver node before the runtime is serialized
     * and sent to worker nodes for execution.
     *
     * @param container container instance to get access or share data with the running container.
     * @param properties used to configure this runtime class.
     * @return ValidationResult status of the validation, if the status is ERROR then the flow or job shall be stopped.
     */
    ValidationResult initialize(RuntimeContainer container, P properties);

}
