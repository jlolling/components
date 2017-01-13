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
package org.talend.components.jira.runtime.reader;

import java.util.List;

import org.talend.components.jira.datum.Entity;
import org.talend.components.jira.datum.Projects;
import org.talend.components.jira.runtime.JiraSource;

/**
 * {@link JiraReader} for rest/api/2/project Jira REST API resource
 */
public class JiraProjectsReader extends JiraNoPaginationReader {

    private static final String REST_RESOURCE = "rest/api/2/project";

    /**
     * {@inheritDoc}
     */
    public JiraProjectsReader(JiraSource source) {
        super(source, REST_RESOURCE);
    }

    /**
     * Retrieves project entities from response
     * 
     * @param response http response
     * @return {@link List} of entities retrieved from response
     */
    protected List<Entity> processResponse(String response) {
        Projects project = new Projects(response);
        List<Entity> entities = project.getEntities();
        return entities;
    }
}
