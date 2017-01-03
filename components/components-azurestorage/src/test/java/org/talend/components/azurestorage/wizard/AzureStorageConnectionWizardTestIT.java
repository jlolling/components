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
package org.talend.components.azurestorage.wizard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.avro.Schema;
import org.junit.Test;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.api.wizard.WizardImageType;
import org.talend.components.api.wizard.WizardNameComparator;
import org.talend.components.azurestorage.AzureStorageGenericBase;
import org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.PresentationItem;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.service.Repository;
import org.talend.daikon.properties.test.PropertiesTestUtils;

public class AzureStorageConnectionWizardTestIT extends AzureStorageGenericBase {

    static class RepoProps {

        Properties props;

        String name;

        String repoLocation;

        Schema schema;

        String schemaPropertyName;

        RepoProps(Properties props, String name, String repoLocation, String schemaPropertyName) {
            this.props = props;
            this.name = name;
            this.repoLocation = repoLocation;
            this.schemaPropertyName = schemaPropertyName;
            if (schemaPropertyName != null) {
                this.schema = (Schema) props.getValuedProperty(schemaPropertyName).getValue();
            }
        }

        @Override
        public String toString() {
            return "RepoProps: " + repoLocation + "/" + name + " props: " + props;
        }
    }

    class TestRepository implements Repository {

        private int locationNum;

        public String componentIdToCheck;

        public ComponentProperties properties;

        public List<RepoProps> repoProps;

        TestRepository(List<RepoProps> repoProps) {
            this.repoProps = repoProps;
        }

        @Override
        public String storeProperties(Properties properties, String name, String repositoryLocation, String schemaPropertyName) {
            RepoProps rp = new RepoProps(properties, name, repositoryLocation, schemaPropertyName);
            repoProps.add(rp);
            return repositoryLocation + ++locationNum;
        }
    }

    @Test
    public void testWizard() throws Throwable {
        final List<RepoProps> repoProps = new ArrayList<>();

        Repository repo = new TestRepository(repoProps);
        getComponentService().setRepository(repo);

        Set<ComponentWizardDefinition> wizards = getComponentService().getTopLevelComponentWizards();
        int count = 0;
        ComponentWizardDefinition wizardDef = null;
        for (ComponentWizardDefinition wizardDefinition : wizards) {
            if (wizardDefinition instanceof AzureStorageConnectionWizardDefinition) {
                wizardDef = wizardDefinition;
                count++;
            }
        }
        assertEquals(1, count);
        assertEquals("Create Salesforce Connection", wizardDef.getMenuItemName());
        ComponentWizard wiz = getComponentService()
                .getComponentWizard(AzureStorageConnectionWizardDefinition.COMPONENT_WIZARD_NAME, "nodeSalesforce");
        assertNotNull(wiz);
        assertEquals("nodeSalesforce", wiz.getRepositoryLocation());
        AzureStorageConnectionWizard swiz = (AzureStorageConnectionWizard) wiz;
        List<Form> forms = wiz.getForms();
        Form connFormWizard = forms.get(0);
        assertEquals("Wizard", connFormWizard.getName());
        assertFalse(connFormWizard.isAllowBack());
        assertFalse(connFormWizard.isAllowForward());
        assertFalse(connFormWizard.isAllowFinish());
        // Main from SalesforceModuleListProperties
        assertEquals("Main", forms.get(1).getName());
        assertEquals("Salesforce Connection Settings", connFormWizard.getTitle());
        assertEquals("Complete these fields in order to connect to your Salesforce account.", connFormWizard.getSubtitle());

        TAzureStorageConnectionProperties connProps = (TAzureStorageConnectionProperties) connFormWizard.getProperties();

        Form af = connProps.getForm(Form.ADVANCED);
        assertTrue(
                ((PresentationItem) connFormWizard.getWidget("advanced").getContent()).getFormtoShow() + " should be == to " + af,
                ((PresentationItem) connFormWizard.getWidget("advanced").getContent()).getFormtoShow() == af);

        Object image = getComponentService().getWizardPngImage(AzureStorageConnectionWizardDefinition.COMPONENT_WIZARD_NAME,
                WizardImageType.TREE_ICON_16X16);
        assertNotNull(image);
        image = getComponentService().getWizardPngImage(AzureStorageConnectionWizardDefinition.COMPONENT_WIZARD_NAME,
                WizardImageType.WIZARD_BANNER_75X66);
        assertNotNull(image);

        // Check the non-top-level wizard

        // check password i18n
        assertEquals("Name", connProps.getProperty("name").getDisplayName());
        connProps.name.setValue("connName");

        Form userPassword = (Form) connFormWizard.getWidget("userPassword").getContent();
        Property passwordSe = (Property) userPassword.getWidget("password").getContent();
        assertEquals("Password", passwordSe.getDisplayName());
        // check name i18n
        NamedThing nameProp = connFormWizard.getWidget("name").getContent(); //$NON-NLS-1$
        assertEquals("Name", nameProp.getDisplayName());
        connProps = (TAzureStorageConnectionProperties) PropertiesTestUtils.checkAndValidate(getComponentService(),
                connFormWizard, "testConnection", connProps);
        assertTrue(connFormWizard.isAllowForward());

        Form modForm = forms.get(1);
    }

    @Test
    public void testModuleWizard() throws Throwable {
        ComponentWizard wiz = getComponentService()
                .getComponentWizard(AzureStorageConnectionWizardDefinition.COMPONENT_WIZARD_NAME, "nodeSalesforce");
        List<Form> forms = wiz.getForms();
        Form connFormWizard = forms.get(0);
        TAzureStorageConnectionProperties connProps = (TAzureStorageConnectionProperties) connFormWizard.getProperties();

        ComponentWizard[] subWizards = getComponentService().getComponentWizardsForProperties(connProps, "location")
                .toArray(new ComponentWizard[3]);
        Arrays.sort(subWizards, new WizardNameComparator());
        assertEquals(3, subWizards.length);
        // Edit connection wizard - we copy the connection properties, as we present the UI, so we use the
        // connection properties object created by the new wizard
        assertFalse(connProps == subWizards[1].getForms().get(0).getProperties());
        // Add module wizard - we refer to the existing connection properties as we don't present the UI
        // for them.
    }

}
