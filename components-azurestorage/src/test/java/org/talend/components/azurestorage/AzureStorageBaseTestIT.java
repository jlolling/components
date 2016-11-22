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
package org.talend.components.azurestorage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.container.DefaultComponentRuntimeContainerImpl;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.service.common.ComponentServiceImpl;
import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.components.azurestorage.helpers.RemoteBlobsTable;
import org.talend.components.azurestorage.runtime.AzureStorageSource;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties;
import org.talend.components.azurestorage.tazurestoragecontainercreate.TAzureStorageContainerCreateProperties.AccessControl;
import org.talend.components.azurestorage.tazurestoragecontainerdelete.TAzureStorageContainerDeleteProperties;
import org.talend.components.azurestorage.tazurestoragecontainerexist.TAzureStorageContainerExistProperties;
import org.talend.components.azurestorage.tazurestoragecontainerlist.TAzureStorageContainerListProperties;
import org.talend.components.azurestorage.tazurestoragelist.TAzureStorageListProperties;
import org.talend.components.azurestorage.tazurestorageput.TAzureStoragePutProperties;

/**
 * Class AzureStorageBaseTestIT.
 */
public abstract class AzureStorageBaseTestIT {

    public String TEST_NAME;

    static public final String accountKey = System.getProperty("azurestorage.account.key");

    static public final String accountName = System.getProperty("azurestorage.account.name");

    static public final String sharedAccessSignature = System.getProperty("azurestorage.sharedaccesssignature");

    static public final String useSAS = System.getProperty("azurestorage.usesas");

    public static final String TEST_CONTAINER_PREFIX = "tests-azurestorage-";

    public static final String TEST_CONTAINER_1 = TEST_CONTAINER_PREFIX + "01-";

    public static final String TEST_CONTAINER_2 = TEST_CONTAINER_PREFIX + "02-";

    public static final String TEST_CONTAINER_3 = TEST_CONTAINER_PREFIX + "03-";

    public static final String[] TEST_CONTAINERS = { TEST_CONTAINER_1, TEST_CONTAINER_2, TEST_CONTAINER_3 };

    public static final String TEST_ROOT_BLOB1 = "blob1.txt";

    public static final String TEST_ROOT_BLOB2 = "blob2.txt";

    public static final String TEST_ROOT_BLOB3 = "blob3.txt";

    public static final String TEST_SUB1_BLOB1 = "sub1/sub1blob1.txt";

    public static final String TEST_SUB1_BLOB2 = "sub1/sub1blob2.txt";

    public static final String TEST_SUB1_BLOB3 = "sub1/sub1blob3.txt";

    public static final String TEST_SUB2_BLOB1 = "sub2/sub2blob1.txt";

    public static final String TEST_SUB2_BLOB2 = "sub2/sub2blob2.txt";

    public static final String TEST_SUB2_BLOB3 = "sub2/sub2blob3.txt";

    public static final String TEST_SUB3_BLOB1 = "sub3/sub3blob1.txt";

    public static final String TEST_SUB3_BLOB2 = "sub3/sub3blob2.txt";

    public static final String TEST_SUB3_BLOB3 = "sub3/sub3blob3.txt";

    private String TEST_FOLDER_GET = "azurestorage-get";

    private String TEST_FOLDER_PUT = "azurestorage-put";

    public static final String[] TEST_ROOT_BLOBS = { TEST_ROOT_BLOB1, TEST_ROOT_BLOB2, TEST_ROOT_BLOB3 };

    public static final String[] TEST_SUB1_BLOBS = { TEST_SUB1_BLOB1, TEST_SUB1_BLOB2, TEST_SUB1_BLOB3 };

    public static final String[] TEST_SUB2_BLOBS = { TEST_SUB2_BLOB1, TEST_SUB2_BLOB2, TEST_SUB2_BLOB3 };

    public static final String[] TEST_SUB3_BLOBS = { TEST_SUB3_BLOB1, TEST_SUB3_BLOB2, TEST_SUB3_BLOB3 };

    public static final String[] TEST_SUB_BLOBS = { TEST_SUB1_BLOB1, TEST_SUB1_BLOB2, TEST_SUB1_BLOB3, TEST_SUB2_BLOB1,
            TEST_SUB2_BLOB2, TEST_SUB2_BLOB3, TEST_SUB3_BLOB1, TEST_SUB3_BLOB2, TEST_SUB3_BLOB3 };

    public static final String[] TEST_ALL_BLOBS = { TEST_ROOT_BLOB1, TEST_ROOT_BLOB2, TEST_ROOT_BLOB3, TEST_SUB1_BLOB1,
            TEST_SUB1_BLOB2, TEST_SUB1_BLOB3, TEST_SUB2_BLOB1, TEST_SUB2_BLOB2, TEST_SUB2_BLOB3, TEST_SUB3_BLOB1, TEST_SUB3_BLOB2,
            TEST_SUB3_BLOB3 };

    public String FOLDER_PATH_GET = "";

    public String FOLDER_PATH_PUT = "";

    private transient static final Logger LOGGER = LoggerFactory.getLogger(AzureStorageBaseTestIT.class);

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Inject
    private ComponentService componentService;

    protected RuntimeContainer runtime;

    /**
     * Instantiates a new AzureStorageBaseTestIT().
     */
    public AzureStorageBaseTestIT(String testName) {
        TEST_NAME = testName;
        runtime = new DefaultComponentRuntimeContainerImpl();
        // sets paths
        FOLDER_PATH_GET = getClass().getResource("/").getPath() + TEST_FOLDER_GET;
        FOLDER_PATH_PUT = getClass().getResource("/").getPath() + TEST_FOLDER_PUT;
    }

    // default implementation for pure java test. Shall be overriden of Spring or OSGI tests
    public ComponentService getComponentService() {
        if (componentService == null) {
            DefinitionRegistry testComponentRegistry = new DefinitionRegistry();
            // register component
            testComponentRegistry.registerComponentFamilyDefinition(new AzureStorageFamilyDefinition());
            componentService = new ComponentServiceImpl(testComponentRegistry);
        }
        return componentService;
    }

    /**
     * initializeComponentRegistryAndService.
     */
    @Before
    public void initializeComponentRegistryAndService() {
        // reset the component service
        componentService = null;
    }

    public static String getRandomTestUID() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String getNamedThingForTest(String aThing) {
        return aThing + TEST_NAME;
    }

    public void cleanupGetFolder() throws Exception {
        FileUtils.deleteDirectory(new File(FOLDER_PATH_GET));
        FileUtils.forceMkdir(new File(FOLDER_PATH_GET));
    }

    @SuppressWarnings("rawtypes")
    @Before
    public void cleanupResidualContainersAndFolders() throws Exception {
        //
        // cleaning remote containers
        LOGGER.info("Cleaning remote containers and local folders.");
        BoundedReader reader = this.createContainerListReader();
        Object container;
        Boolean rows = reader.start();
        while (rows) {
            container = reader.getCurrent();
            assertTrue(container instanceof String);
            if (container.toString().startsWith(TEST_CONTAINER_PREFIX)) {
                doContainerDelete(container.toString());
            }
            rows = reader.advance();
        }
        reader.close();
        //
        // cleaning local test folder
        cleanupGetFolder();
    }

    /**
     * createBoundedReader.
     *
     * @param <T> the generic type
     * @param props {@link ComponentProperties} props
     * @return <code>BoundedReader</code> {@link BoundedReader} bounded reader
     */
    @SuppressWarnings("unchecked")
    public <T> BoundedReader<T> createBoundedReader(ComponentProperties props) {
        AzureStorageSource source = new AzureStorageSource();
        source.initialize(null, props);
        source.validate(null);
        return source.createReader(null);
    }

    /**
     * setupContainerProperties - return Connection properties filled in.
     *
     * @param properties {@link AzureStorageProperties} properties
     * @return <code>AzureStorageProperties</code> {@link AzureStorageProperties} azure storage properties
     */
    public AzureStorageProperties setupConnectionProperties(AzureStorageProperties properties) {
        properties.connection.setupProperties();
        properties.connection.accountName.setValue(accountName);
        properties.connection.accountKey.setValue(accountKey);
        properties.connection.sharedAccessSignature.setValue(sharedAccessSignature);
        boolean sas = Boolean.parseBoolean(useSAS);
        if (sas) {
            properties.connection.useSharedAccessSignature.setValue(sas);
        }
        return properties;
    }

    /**
     * doContainerCreate.
     *
     * @param container {@link String} container
     * @param access {@link AccessControl} access
     * @return <code>Boolean</code> {@link Boolean} boolean
     * @throws Exception the exception
     */
    @SuppressWarnings("rawtypes")
    public Boolean doContainerCreate(String container, TAzureStorageContainerCreateProperties.AccessControl access)
            throws Exception {
        LOGGER.info("Creating container {}", container);
        TAzureStorageContainerCreateProperties properties = new TAzureStorageContainerCreateProperties("tests");
        setupConnectionProperties(properties);
        properties.container.setValue(container);
        properties.accessControl.setValue(access);
        BoundedReader reader = createBoundedReader(properties);
        return reader.start();
    }

    /**
     * doContainerDelete.
     *
     * @param container {@link String} container
     * @return <code>Boolean</code> {@link Boolean} boolean
     * @throws Exception the exception
     */
    @SuppressWarnings("rawtypes")
    public Boolean doContainerDelete(String container) throws Exception {
        LOGGER.info("Deleting container {}", container);
        TAzureStorageContainerDeleteProperties properties = new TAzureStorageContainerDeleteProperties("tests");
        setupConnectionProperties(properties);
        properties.container.setValue(container);
        BoundedReader reader = createBoundedReader(properties);
        return reader.start();
    }

    /**
     * doContainerExist - Checks if container exists
     *
     * @param container {@link String} container
     * @return {@link Boolean} true if exists.
     * @throws Exception the exception
     */
    @SuppressWarnings("rawtypes")
    public Boolean doContainerExist(String container) throws Exception {
        LOGGER.info("Does container {} exists ?", container);
        TAzureStorageContainerExistProperties properties = new TAzureStorageContainerExistProperties("tests");
        setupConnectionProperties(properties);
        properties.container.setValue(container);
        BoundedReader reader = createBoundedReader(properties);
        return reader.start();
    }

    /**
     * createContainerListReader - List containers
     *
     * @return {@link BoundedReader} bounded reader
     * @throws Exception the exception
     */
    @SuppressWarnings("rawtypes")
    public BoundedReader createContainerListReader() throws Exception {
        TAzureStorageContainerListProperties properties = new TAzureStorageContainerListProperties("tests");
        setupConnectionProperties(properties);
        return createBoundedReader(properties);
    }

    /**
     * uploadTestBlobs.
     *
     * @param container {@link String} container
     * @throws Exception the exception
     */
    public void uploadTestBlobs(String container) throws Exception {
        LOGGER.info("Uploading test blobs in container {}", container);
        assertTrue(doContainerCreate(container, AccessControl.Private));
        TAzureStoragePutProperties props = new TAzureStoragePutProperties("tests");
        props.container.setValue(container);
        setupConnectionProperties(props);
        props.localFolder.setValue(FOLDER_PATH_PUT);
        props.remoteFolder.setValue("");
        assertTrue(createBoundedReader(props).start());
    }

    public Boolean isInBlobList(String blob, List<String> blobs) {
        for (String cb : blobs) {
            LOGGER.debug("Checking blob `{}` <-> `{}`", blob, cb);
            if (cb.equals(blob)) {
                LOGGER.debug("Found `{}`.", blob);
                return true;
            }
        }
        LOGGER.debug("`{}` not present in `{}`", blob, blobs);

        return false;
    }

    @SuppressWarnings("rawtypes")
    public List<String> listAllBlobs(String container) throws Exception {
        LOGGER.info("Listing container `{}`", container);
        List<String> blobs = new ArrayList<>();
        TAzureStorageListProperties props = new TAzureStorageListProperties("tests");
        props.container.setValue(container);
        setupConnectionProperties(props);
        RemoteBlobsTable rmt = new RemoteBlobsTable("tests");
        List<String> pfx = new ArrayList<>();
        List<Boolean> inc = new ArrayList<>();
        pfx.add("");
        inc.add(true);
        rmt.prefix.setValue(pfx);
        rmt.include.setValue(inc);
        props.remoteBlobs = rmt;
        BoundedReader reader = createBoundedReader(props);
        Boolean rows = reader.start();
        Object row;
        while (rows) {
            row = reader.getCurrent();
            assertNotNull(row);
            assertTrue(row instanceof String);
            blobs.add(row.toString());
            LOGGER.debug("`{}` => {}", container, row);
            rows = reader.advance();
        }
        reader.close();

        return blobs;
    }
}
