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
package org.talend.components.api.service;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

import java.io.File;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.options.libraries.JUnitBundlesOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.daikon.i18n.GlobalI18N;
import org.talend.daikon.i18n.I18nMessageProvider;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OsgiServiceTestIT {

    static String localRepo = System.getProperty("maven.repo.local", "");

    @Configuration
    public Option[] config() {
        if (localRepo != null && !"".equals(localRepo) && !new File(localRepo).isAbsolute()) {
            throw new RuntimeException("maven.repo.local system properties must be absolute.");
        }
        return options(mavenBundle("org.apache.felix", "org.apache.felix.scr"), //
                linkBundle("org.slf4j-slf4j-api"), //
                linkBundle("org.slf4j-slf4j-simple").noStart(), //
                linkBundle("com.fasterxml.jackson.core-jackson-annotations"), //
                linkBundle("com.fasterxml.jackson.core-jackson-core"), //
                linkBundle("com.cedarsoftware-json-io"), //
                linkBundle("commons-codec-commons-codec"), //
                linkBundle("com.thoughtworks.paranamer-paranamer"), //
                linkBundle("org.codehaus.jackson-jackson-core-asl"), //
                linkBundle("org.codehaus.jackson-jackson-mapper-asl"), //
                linkBundle("com.google.guava-guava"), //
                linkBundle("org.apache.commons-commons-compress"), //
                linkBundle("org.apache.commons-commons-lang3"), //
                linkBundle("org.apache.avro-avro"), //
                linkBundle("org.talend.daikon-daikon-bundle"), //
                linkBundle("org.talend.components-components-api-bundle"), //
                linkBundle("org.apache.servicemix.bundles-org.apache.servicemix.bundles.hamcrest"), //
                linkBundle("org.ops4j.pax.url-pax-url-aether"), linkBundle("org.talend.components-components-api-service-osgi"),
                // this is copied from junitBundles() to remove the default pax-exam hamcrest bundle that does
                // not contains all the nice hamcrest Matchers
                new DefaultCompositeOption(new JUnitBundlesOption(), systemProperty("pax.exam.invoker").value("junit"),
                        bundle("link:classpath:META-INF/links/org.ops4j.pax.exam.invoker.junit.link")),
                cleanCaches() //
                , frameworkProperty("org.osgi.framework.system.packages.extra").value("sun.misc"), //
                when(localRepo.length() > 0).useOptions(systemProperty("org.ops4j.pax.url.mvn.localRepository").value(localRepo))

        // ,vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5010"), systemTimeout(0)//
        );
    }

    @Inject
    ComponentService compService;

    @Inject
    DefinitionRegistry defRegistry;

    @Inject
    GlobalI18N globI18n;

    @Inject
    I18nMessageProvider i18nProvider;

    @Test
    public void test() {
        assertNotNull(compService);
        assertNotNull(defRegistry);
        assertNotNull(globI18n);
        assertNotNull(i18nProvider);
    }

}
