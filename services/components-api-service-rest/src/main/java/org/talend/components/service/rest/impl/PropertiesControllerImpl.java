/*
 * Copyright (C) 2006-2015 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package org.talend.components.service.rest.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.common.datastore.DatastoreDefinition;
import org.talend.components.common.datastore.DatastoreProperties;
import org.talend.components.service.rest.FormDataContainer;
import org.talend.components.service.rest.PropertiesController;
import org.talend.components.service.rest.PropertiesValidationResponse;
import org.talend.components.service.rest.serialization.JsonSerializationHelper;
import org.talend.daikon.annotation.ServiceImplementation;
import org.talend.daikon.definition.service.DefinitionRegistryService;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@ServiceImplementation
public class PropertiesControllerImpl implements PropertiesController {

    private static final Logger log = getLogger(PropertiesControllerImpl.class);

    @Autowired
    private JsonSerializationHelper jsonSerializationHelper;

    @Autowired
    private DefinitionRegistryService definitionServiceDelegate;

    @Override
    public String getProperties(@PathVariable("name") String definitionName) {
        Validate.notNull(definitionName, "Data store name cannot be null.");
        final DatastoreDefinition datastoreDefinition = getDataStoreDefinition(definitionName);

        String result;
        if (datastoreDefinition == null) {
            log.debug("Did not found data store definition for {}", definitionName);
            result = null;
        } else {
            log.debug("Found data store definition {} for {}", datastoreDefinition, definitionName);
            result = jsonSerializationHelper.toJson(datastoreDefinition.createProperties());
        }
        return result;
    }

    @Override
    public ResponseEntity<PropertiesValidationResponse> validateProperties(@PathVariable("definitionName") String definitionName,
                                                                           @RequestBody FormDataContainer formData) {
        Validate.notNull(definitionName, "Data store name cannot be null.");
        final DatastoreDefinition datastoreDefinition = getDataStoreDefinition(definitionName);
        Properties properties = getPropertiesFromJson(datastoreDefinition, formData.getFormData());
        ValidationResult validationResult = properties.getValidationResult();
        // TODO: I really would prefer return 200 status code any time it process correctly and that the payload determine the result of the analysis.
        // Here we use 400 return code for perfectly acceptable validation request but with result with unaccepted properties.
        ResponseEntity<PropertiesValidationResponse> response;
        switch (validationResult.getStatus()) {
        case ERROR:
        case WARNING:
            response = new ResponseEntity<>(new PropertiesValidationResponse(validationResult), BAD_REQUEST);
            break;
        case OK:
        default:
            response = new ResponseEntity<>(NO_CONTENT);
        }
        return response;
    }

    @Override
    public void validateProperty(@PathVariable("definitionName") String definitionName,
                                 @PathVariable("propName") String propName) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void beforeRenderProperty(@PathVariable("definitionName") String definitionName,
                                     @PathVariable("propName") String propName) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void afterProperty(@PathVariable("definitionName") String definitionName, @PathVariable("propName") String propName) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void beforeActivateProperty(@PathVariable("definitionName") String definitionName,
                                       @PathVariable("propName") String propName) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public String getDatasetProperties(@PathVariable("definitionName") String definitionName,
                                       @RequestBody FormDataContainer formData) {
        DatastoreDefinition datastoreDefinition = getDataStoreDefinition(definitionName);
        DatastoreProperties properties = getPropertiesFromJson(datastoreDefinition, formData.getFormData());
        DatasetProperties datasetProperties = datastoreDefinition.createDatasetProperties(properties);
        return jsonSerializationHelper.toJson(datasetProperties);
    }

    private DatastoreProperties getPropertiesFromJson(DatastoreDefinition datastoreDefinition, String formDataJson) {
        DatastoreProperties properties = (DatastoreProperties) datastoreDefinition.createProperties();
        return jsonSerializationHelper.toProperties(new ByteArrayInputStream(formDataJson.getBytes(StandardCharsets.UTF_8)),
                properties);
    }

    /** Throws exception if not found **/
    private DatastoreDefinition getDataStoreDefinition(String definitionName) {
        DatastoreDefinition datastoreDefinition = definitionServiceDelegate.getDefinitionsMapByType(DatastoreDefinition.class)
                .get(definitionName);
        Validate.notNull(datastoreDefinition, "Could not find data store definition of name %s", definitionName);
        return datastoreDefinition;
    }
}
