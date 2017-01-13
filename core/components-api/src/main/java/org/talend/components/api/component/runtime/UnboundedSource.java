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

import java.io.IOException;
import java.util.List;

import org.talend.components.api.container.RuntimeContainer;

/**
 * A {@link Source} that reads an unbounded amount of input and, because of that, supports some additional operations
 * such as checkpointing, watermarks, and record ids.
 *
 * <ul>
 * <li>Checkpointing allows sources to not re-read the same data again in the case of failures.
 * <li>Watermarks allow for downstream parts of the pipeline to know up to what point in time the data is complete.
 * <li>Record ids allow for efficient deduplication of input records; many streaming sources do not guarantee that a given record
 * will only be read a single time.
 * </ul>
 *
 * @param <CheckpointMarkT> Type of checkpoint marks used by the readers of this source.
 */
public interface UnboundedSource<CheckpointMarkT extends UnboundedSource.CheckpointMark> extends Source {

    /**
     * Returns a list of {@code UnboundedSource} objects representing the instances of this source that should be used
     * when executing the workflow. Each split should return a separate partition of the input data.
     *
     * <p>
     * For example, for a source reading from a growing directory of files, each split could correspond to a prefix of file names.
     *
     * <p>
     * Some sources are not splittable, such as reading from a single TCP stream. In that case, only a single split should be
     * returned.
     *
     * <p>
     * Some data sources automatically partition their data among readers. For these types of inputs, {@code n} identical replicas
     * of the top-level source can be returned.
     *
     * <p>
     * The size of the returned list should be as close to {@code desiredNumSplits} as possible, but does not have to match
     * exactly. A low number of splits will limit the amount of parallelism in the source.
     */
    List<? extends UnboundedSource<CheckpointMarkT>> generateInitialSplits(int desiredNumSplits,
                                                                           RuntimeContainer options);

    /**
     * Create a new {@link UnboundedReader} to read from this source, resuming from the given checkpoint if present.
     */
    UnboundedReader createReader(RuntimeContainer options, CheckpointMarkT checkpointMark);

    /**
     * Returns whether this source requires explicit deduping.
     *
     * <p>
     * This is needed if the underlying data source can return the same record multiple times, such a queuing system with a
     * pull-ack model. Sources where the records read are uniquely identified by the persisted state in the CheckpointMark do not
     * need this.
     */
    boolean requiresDeduping();

    /**
     * A marker representing the progress and state of an {@link UnboundedReader}.
     *
     * <p>
     * For example, this could be offsets in a set of files being read.
     */
    interface CheckpointMark {

        /**
         * Perform any finalization that needs to happen after a bundle of data read from the source has been processed
         * and committed.
         *
         * <p>
         * For example, this could be sending acknowledgement requests to an external data source such as Pub/Sub.
         *
         * <p>
         * This may be called from any thread, potentially at the same time as calls to the {@code UnboundedReader} that created
         * it.
         */
        void finalizeCheckpoint() throws IOException;
    }

}
