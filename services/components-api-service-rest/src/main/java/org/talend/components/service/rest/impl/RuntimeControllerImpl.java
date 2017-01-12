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

package org.talend.components.service.rest.impl;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.talend.components.common.dataset.DatasetDefinition;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.common.dataset.runtime.DatasetRuntime;
import org.talend.components.common.datastore.DatastoreDefinition;
import org.talend.components.common.datastore.DatastoreProperties;
import org.talend.components.common.datastore.runtime.DatastoreRuntime;
import org.talend.components.service.rest.RuntimesController;
import org.talend.components.service.rest.dto.PropertiesDto;
import org.talend.components.service.rest.dto.ValidationResultsDto;
import org.talend.daikon.annotation.ServiceImplementation;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.java8.Consumer;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

@ServiceImplementation
@SuppressWarnings("unchecked")
public class RuntimeControllerImpl implements RuntimesController {

    private static final Logger log = LoggerFactory.getLogger(RuntimeControllerImpl.class);

    private static final ClassLoader classLoader = RuntimeControllerImpl.class.getClassLoader();

    @Autowired
    private PropertiesHelpers propertiesHelpers;

    @Override
    public ResponseEntity<ValidationResultsDto> validateDataStoreConnection(String dataStoreDefinitionName,
                                                                            PropertiesDto propertiesContainer) {
        final DatastoreDefinition<DatastoreProperties> definition = propertiesHelpers.getDataStoreDefinition(
                dataStoreDefinitionName);
        notNull(definition, "Could not find data store definition of name %s", dataStoreDefinitionName);
        DatastoreProperties properties = propertiesHelpers.propertiesFromDto(propertiesContainer);

        try (SandboxedInstance instance = RuntimeUtil.createRuntimeClass(definition.getRuntimeInfo(properties), classLoader)) {
            DatastoreRuntime<DatastoreProperties> datastoreRuntime = (DatastoreRuntime) instance.getInstance();
            datastoreRuntime.initialize(null, properties);
            Iterable<ValidationResult> healthChecks = datastoreRuntime.doHealthChecks(null);

            ValidationResultsDto response = new ValidationResultsDto(
                    healthChecks == null ? emptyList() : newArrayList(healthChecks));
            HttpStatus httpStatus = response.getStatus() == ValidationResult.Result.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(response, httpStatus);
        }
    }

    @Override
    public String getDatasetSchema(String datasetDefinitionName, PropertiesDto connectionInfo) throws IOException {
        return useDatasetRuntime(datasetDefinitionName, connectionInfo, runtime -> runtime.getSchema().toString(false));
    }

    @Override
    public Void getDatasetData(String datasetDefinitionName, //
                               PropertiesDto connectionInfo, //
                               Integer from, //
                               Integer limit, //
                               OutputStream response) {
        return useDatasetRuntime(datasetDefinitionName, connectionInfo, new DatasetContentWriter(response, limit, true));
    }

    @Override
    public Void getDatasetDataAsBinary(String datasetDefinitionName, //
                                       PropertiesDto connectionInfo, //
                                       Integer from,  //
                                       Integer limit,  //
                                        OutputStream response) {
        return useDatasetRuntime(datasetDefinitionName, connectionInfo, new DatasetContentWriter(response, limit, false));
    }

    private <T> T useDatasetRuntime(String datasetDefinitionName, //
                                    PropertiesDto formData, //
                                    Function<DatasetRuntime<DatasetProperties<DatastoreProperties>>, T> consumer) {

        // 1) get dataset properties from supplied data
        DatasetProperties datasetProperties = propertiesHelpers.propertiesFromDto(formData);

        // 2) Retrieve data set definition to be able to create the runtime
        final DatasetDefinition<DatasetProperties<DatastoreProperties>> datasetDefinition = //
                propertiesHelpers.getDataSetDefinition(datasetDefinitionName);

        // 3) create the runtime
        try (SandboxedInstance instance = RuntimeUtil.createRuntimeClass(datasetDefinition.getRuntimeInfo(datasetProperties), classLoader)) {
            DatasetRuntime<DatasetProperties<DatastoreProperties>> datasetRuntimeInstance = (DatasetRuntime<DatasetProperties<DatastoreProperties>>) instance
                    .getInstance();

            datasetRuntimeInstance.initialize(null, datasetProperties);

            // 4) Consume the data set runtime
            return consumer.apply(datasetRuntimeInstance);
        }
    }

    private static class DatasetContentWriter implements Function<DatasetRuntime<DatasetProperties<DatastoreProperties>>, Void> {

        private final Integer limit;

        private final boolean json;

        private final OutputStream output;

        /**
         * @param limit the number of records to write
         * @param json  true to write JSon, false for binary Avro
         */
        DatasetContentWriter(OutputStream output, Integer limit, boolean json) {
            this.output = output;
            this.limit = limit;
            this.json = json;
        }

        @Override
        public Void apply(DatasetRuntime<DatasetProperties<DatastoreProperties>> dr) {
            try {
                final Encoder[] encoder = { null };

                dr.getSample(limit == null ? MAX_VALUE : limit, new Consumer<IndexedRecord>() {

                    GenericDatumWriter<IndexedRecord> writer = null;

                    @Override
                    public void accept(IndexedRecord ir) {
                        if (writer == null) {
                            writer = new GenericDatumWriter<>(ir.getSchema());
                            try {
                                if (json) {
                                    encoder[0] = EncoderFactory.get().jsonEncoder(ir.getSchema(), output);
                                } else {
                                    encoder[0] = EncoderFactory.get().binaryEncoder(output, null);
                                }
                            } catch (IOException ioe) {
                                throw new RuntimeException(ioe);
                            }

                        }
                        writeIndexedRecord(writer, encoder[0], ir);
                    }
                });
                if (encoder[0] != null)
                    encoder[0].flush();
            } catch (RuntimeException | IOException e) {
                log.error("Couldn't create Avro records JSon encoder.", e);
                throw new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION, e);
            }
            return null;
        }

        private void writeIndexedRecord(GenericDatumWriter<IndexedRecord> writer, Encoder encoder, IndexedRecord indexedRecord) {
            try {
                writer.write(indexedRecord, encoder);
            } catch (IOException e) {
                log.warn("Couldn't serialize Avro record.", e);
            }
        }
    }
}
