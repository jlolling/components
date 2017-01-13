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
package org.talend.components.api;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.service.ComponentService;

/**
 * created by sgandon on 7 sept. 2015 Detailled comment
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OsgiComponentServiceTest {

    static final Logger LOG = LoggerFactory.getLogger(OsgiComponentServiceTest.class);

    @Inject
    BundleContext bc;

    @Configuration
    public Option[] config() {
        try {
            return ComponentsPaxExamOptions.getOptions();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void exampleOnHowToGetTheServiceUsingOsgiApis() {
        // inside eclipse the bundle context can be retrieved from the Activator.start method or using the FrameworkUtil
        // class.
        BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
        ServiceReference<ComponentService> compServiceRef = bundleContext.getServiceReference(ComponentService.class);
        if (compServiceRef != null) {
            ComponentService compService = bundleContext.getService(compServiceRef);
            assertNotNull(compService);
        } else {
            fail("Failed to retrieve the Component service");
        }
    }

}
