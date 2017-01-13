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
package org.talend.components.splunk.runtime;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.Sink;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.splunk.TSplunkEventCollectorProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.i18n.GlobalI18N;
import org.talend.daikon.i18n.I18nMessages;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;

public class TSplunkEventCollectorSink implements Sink {

    /**
     * 
     */
    private static final long serialVersionUID = -2587927325500427743L;

    private transient static final Logger LOGGER = LoggerFactory.getLogger(TSplunkEventCollectorSink.class);

    private String serverUrl;

    private String token;

    private int eventsBatchSize;

    private Schema schema;

    private I18nMessages messageFormatter;

    @Override
    public ValidationResult initialize(RuntimeContainer container, ComponentProperties properties) {
        TSplunkEventCollectorProperties props = (TSplunkEventCollectorProperties) properties;
        this.serverUrl = props.fullUrl.getStringValue();
        if (this.serverUrl.isEmpty()) {
            return new ValidationResult().setStatus(Result.ERROR).setMessage("Server url should not be empty.");
        }
        this.token = props.token.getStringValue();
        if (this.token.isEmpty()) {
            return new ValidationResult().setStatus(Result.ERROR).setMessage("Token should not be empty.");
        }
        this.eventsBatchSize = props.getBatchSize();
        if (this.eventsBatchSize <= 0) {
            return new ValidationResult().setStatus(Result.ERROR)
                    .setMessage("Events batch size cannot be less or equal to zero.");
        }
        this.schema = props.getSchema();
        return ValidationResult.OK;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            LOGGER.debug("Server URL is empty.");
            return new ValidationResult().setStatus(Result.ERROR).setMessage(getMessage("message.serverUrlCannotBeEmpty"));
        }
        if (token == null || token.trim().isEmpty()) {
            LOGGER.debug("Splunk Authorization Token is empty.");
            return new ValidationResult().setStatus(Result.ERROR).setMessage(getMessage("message.tokenCannotBeEmpty"));
        }
        return ValidationResult.OK;
    }

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer adaptor) throws IOException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {
        return null;
    }

    @Override
    public WriteOperation<?> createWriteOperation() {
        return new TSplunkEventCollectorWriteOperation(this);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getToken() {
        return token;
    }

    public int getEventsBatchSize() {
        return eventsBatchSize;
    }

    private String getMessage(String key, Object... arguments) {
        if (messageFormatter == null) {
            messageFormatter = GlobalI18N.getI18nMessageProvider().getI18nMessages(this.getClass());
        }
        return messageFormatter.getMessage(key, arguments);
    }

    public Schema getSchema() {
        return schema;
    }

}
