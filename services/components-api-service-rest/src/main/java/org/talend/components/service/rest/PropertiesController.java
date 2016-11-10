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

package org.talend.components.service.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.talend.daikon.annotation.Service;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Service(name = "PropertiesController")
@RequestMapping(value = "properties",
                consumes = APPLICATION_JSON_UTF8_VALUE,
                produces = APPLICATION_JSON_UTF8_VALUE)
public interface PropertiesController {

    @RequestMapping(value = "{name}", method = GET)
        // P1
    void getProperties(@PathVariable("name") String definitionName);

    /** Validate **/
    // P1
    @RequestMapping(value = "{definitionName}/validate", method = POST)
    void validateProperties(@PathVariable("definitionName") String definitionName);

    /** Validate one field. */
    // P1
    @RequestMapping(value = "{definitionName}/validate/{propName}", method = POST)
    void validateProperty(@PathVariable("definitionName") String definitionName, @PathVariable("propName") String propName);

    /**  **/
    // P2
    @RequestMapping(value = "{definitionName}/beforeRender/{propName}", method = POST)
    void beforeRenderProperty(@PathVariable("definitionName") String definitionName, @PathVariable("propName") String propName);

    /** **/
    // P2
    @RequestMapping(value = "{definitionName}/after/{propName}", method = POST)
    void afterProperty(@PathVariable("definitionName") String definitionName, @PathVariable("propName") String propName);

    /** **/
    // P2
    @RequestMapping(value = "{definitionName}/beforeActivate/{propName}", method = POST)
    void beforeActivateProperty(@PathVariable("definitionName") String definitionName, @PathVariable("propName") String propName);

    // P1

    /** Get dataset properties. Should it be GET? **/
    @RequestMapping(value = "{definitionName}/dataset", method = POST)
    void getDataset();

}
