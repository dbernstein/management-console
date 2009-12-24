package org.duracloud.rackspacestorage;

import com.mosso.client.cloudfiles.FilesCDNContainer;
import com.mosso.client.cloudfiles.FilesClient;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.apache.log4j.Logger;
import org.duracloud.common.model.Credential;
import org.duracloud.common.web.RestHttpHelper;
import org.duracloud.common.web.RestHttpHelper.HttpResponse;
import org.duracloud.storage.domain.StorageProviderType;
import org.duracloud.storage.domain.test.db.UnitTestDatabaseUtil;
import org.duracloud.storage.error.StorageException;
import org.duracloud.storage.provider.StorageProvider;
import org.duracloud.storage.provider.StorageProvider.AccessType;
import static org.duracloud.storage.util.StorageProviderUtil.compareChecksum;
import static org.duracloud.storage.util.StorageProviderUtil.contains;
import static org.duracloud.storage.util.StorageProviderUtil.count;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.List;

/**
 * Tests the Rackspace Storage Provider. This test is run via the command line
 * in order to allow passing in credentials.
 *
 * @author Bill Branan
 */
public class RackspaceStorageProviderTest {

    protected static final Logger log =
            Logger.getLogger(RackspaceStorageProviderTest.class);

    RackspaceStorageProvider rackspaceProvider;

    FilesClient filesClient;

    private static String SPACE_ID = null;
    private static final String CONTENT_ID = "duracloud-test-content";
    private static final String SPACE_META_NAME = "custom-space-metadata";
    private static final String SPACE_META_VALUE = "Testing Space";
    private static final String CONTENT_META_NAME = "custom-content-metadata";
    private static final String CONTENT_META_VALUE = "Testing Content";
    private static final String CONTENT_MIME_NAME = StorageProvider.METADATA_CONTENT_MIMETYPE;
    private static final String CONTENT_MIME_VALUE = "text/plain";
    private static final String CONTENT_DATA = "Test Content";

    @Before
    public void setUp() throws Exception {
        Credential rackspaceCredential = getCredential();
        Assert.assertNotNull(rackspaceCredential);

        String username = rackspaceCredential.getUsername();
        String password = rackspaceCredential.getPassword();
        Assert.assertNotNull(username);
        Assert.assertNotNull(password);

        rackspaceProvider = new RackspaceStorageProvider(username, password);
        filesClient = new FilesClient(username, password);
        assertTrue(filesClient.login());

        String random = String.valueOf(new Random().nextInt(99999));
        SPACE_ID = "duracloud-test-bucket." + random;
    }

    @After
    public void tearDown() {
        rackspaceProvider = null;
        filesClient = null;
    }

    private Credential getCredential() throws Exception {
        UnitTestDatabaseUtil dbUtil = new UnitTestDatabaseUtil();
        return dbUtil.findCredentialForProvider(StorageProviderType.RACKSPACE);
    }

    @Test
    public void testRackspaceStorageProvider() throws Exception {
        // test createSpace()
        log.debug("Test createSpace()");
        rackspaceProvider.createSpace(SPACE_ID);

        // test setSpaceMetadata()
        log.debug("Test setSpaceMetadata()");
        Map<String, String> spaceMetadata = new HashMap<String, String>();
        spaceMetadata.put(SPACE_META_NAME, SPACE_META_VALUE);
        rackspaceProvider.setSpaceMetadata(SPACE_ID, spaceMetadata);

        // test getSpaceMetadata()
        log.debug("Test getSpaceMetadata()");
        Map<String, String> sMetadata = rackspaceProvider.getSpaceMetadata(SPACE_ID);

        // TODO: Determine if there is a way to get Last-Modified from Rackspace container
        //assertTrue(sMetadata.containsKey(StorageProvider.METADATA_SPACE_CREATED));
        assertTrue(sMetadata.containsKey(StorageProvider.METADATA_SPACE_COUNT));
        assertTrue(sMetadata.containsKey(StorageProvider.METADATA_SPACE_ACCESS));
        //assertNotNull(sMetadata.get(StorageProvider.METADATA_SPACE_CREATED));
        assertNotNull(sMetadata.get(StorageProvider.METADATA_SPACE_COUNT));
        assertNotNull(sMetadata.get(StorageProvider.METADATA_SPACE_ACCESS));

        assertTrue(sMetadata.containsKey(SPACE_META_NAME));
        assertEquals(SPACE_META_VALUE, sMetadata.get(SPACE_META_NAME));

        // test getSpaces()
        log.debug("Test getSpaces()");
        Iterator<String> spaces = rackspaceProvider.getSpaces();
        assertNotNull(spaces);
        assertTrue(contains(spaces, SPACE_ID)); // This will only work when SPACE_ID fits
                                                // the Rackspace container naming conventions

        // test invalid space
        log.debug("Test getSpaceAccess() with invalid spaceId");
        try {
            rackspaceProvider.getSpaceAccess(SPACE_ID+"-invalid");
            fail("getSpaceAccess() should throw an exception with an invalid spaceId");
        } catch(StorageException expected) {
            assertNotNull(expected);
        }

        log.debug("Test getSpaceContents() with invalid spaceId");
        try {
            rackspaceProvider.getSpaceContents(SPACE_ID+"-invalid", null);
            fail("getSpaceContents() should throw an exception with an invalid spaceId");
        } catch(StorageException expected) {
            assertNotNull(expected);
        }

        // Check space access - should be closed
        // TODO: Uncomment after Rackspace SDK bug is fixed
        //        log.debug("Check space access");
        //        FilesCDNContainer cdnContainer = filesClient.getCDNContainerInfo(SPACE_ID);
        //        assertFalse(cdnContainer.isEnabled());

        // test setSpaceAccess()
        log.debug("Test setSpaceAccess(OPEN)");
        rackspaceProvider.setSpaceAccess(SPACE_ID, AccessType.OPEN);

        // test getSpaceAccess()
        log.debug("Test getSpaceAccess()");
        AccessType access = rackspaceProvider.getSpaceAccess(SPACE_ID);
        assertEquals(access, AccessType.OPEN);

        // Check space access - should be open
        log.debug("Check space access");
        FilesCDNContainer cdnContainer =
                filesClient.getCDNContainerInfo(SPACE_ID);
        assertTrue(cdnContainer.isEnabled());

        // test addContent()
        log.debug("Test addContent()");
        addContent(SPACE_ID, CONTENT_ID);

        // test getContentMetadata()
        log.debug("Test getContentMetadata()");
        Map<String, String> cMetadata =
                rackspaceProvider.getContentMetadata(SPACE_ID, CONTENT_ID);
        assertNotNull(cMetadata);
        assertEquals(CONTENT_MIME_VALUE, cMetadata.get(CONTENT_MIME_NAME));
        assertNotNull(cMetadata.get(StorageProvider.METADATA_CONTENT_MODIFIED));
        assertNotNull(cMetadata.get(StorageProvider.METADATA_CONTENT_SIZE));
        assertNotNull(cMetadata.get(StorageProvider.METADATA_CONTENT_CHECKSUM));

        // Check content access
        log.debug("Check content access");
        String spaceUrl = cdnContainer.getCdnURL();
        String contentUrl = spaceUrl + "/" + CONTENT_ID;
        RestHttpHelper restHelper = new RestHttpHelper();
        HttpResponse httpResponse = restHelper.get(contentUrl);
        assertEquals(200, httpResponse.getStatusCode());

        // test setSpaceAccess()
        log.debug("Test setSpaceAccess(CLOSED)");
        rackspaceProvider.setSpaceAccess(SPACE_ID, AccessType.CLOSED);
        // Note that content stays in the Rackspace CDN for 24 hours
        // after it is made available, even if the container has been
        // set to private access or the content has been deleted.

        // add additional content for getContents tests
        String testContent2 = "test-content-2";
        addContent(SPACE_ID, testContent2);
        String testContent3 = "test-content-3";
        addContent(SPACE_ID, testContent3);

        // test getSpaceContents()
        log.debug("Test getSpaceContents()");
        Iterator<String> spaceContents =
            rackspaceProvider.getSpaceContents(SPACE_ID, null);
        assertNotNull(spaceContents);
        assertEquals(3, count(spaceContents));
        // Ensure that space metadata is not included in contents list
        spaceContents = rackspaceProvider.getSpaceContents(SPACE_ID, null);
        String containerName = rackspaceProvider.getContainerName(SPACE_ID);
        String spaceMetaSuffix = RackspaceStorageProvider.SPACE_METADATA_SUFFIX;
        assertFalse(contains(spaceContents, containerName + spaceMetaSuffix));

        // test getSpaceContentsChunked() maxLimit
        log.debug("Test getSpaceContentsChunked() maxLimit");
        List<String> spaceContentList =
            rackspaceProvider.getSpaceContentsChunked(SPACE_ID,
                                                      null,
                                                      2,
                                                      null);
        assertNotNull(spaceContentList);
        assertEquals(2, spaceContentList.size());
        String lastItem = spaceContentList.get(spaceContentList.size() - 1);
        spaceContentList = rackspaceProvider.getSpaceContentsChunked(SPACE_ID,
                                                                     null,
                                                                     2,
                                                                     lastItem);
        assertNotNull(spaceContentList);
        assertEquals(1, spaceContentList.size());

        // test getSpaceContentsChunked() prefix
        log.debug("Test getSpaceContentsChunked() prefix");
        spaceContentList = rackspaceProvider.getSpaceContentsChunked(SPACE_ID,
                                                                     "test",
                                                                     10,
                                                                     null);
        assertEquals(2, spaceContentList.size());

        // test getContent()
        log.debug("Test getContent()");
        InputStream is = rackspaceProvider.getContent(SPACE_ID, CONTENT_ID);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String contentLine = reader.readLine();
        assertTrue(contentLine.equals(CONTENT_DATA));

        // test invalid content
        log.debug("Test getContent() with invalid content ID");
        is = rackspaceProvider.getContent(SPACE_ID, "non-existant-content");
        assertTrue(is == null);

        // test setContentMetadata()
        log.debug("Test setContentMetadata()");
        Map<String, String> contentMetadata = new HashMap<String, String>();
        contentMetadata.put(CONTENT_META_NAME, CONTENT_META_VALUE);
        rackspaceProvider.setContentMetadata(SPACE_ID,
                                             CONTENT_ID,
                                             contentMetadata);

        // test getContentMetadata()
        log.debug("Test getContentMetadata()");
        cMetadata = rackspaceProvider.getContentMetadata(SPACE_ID, CONTENT_ID);
        assertNotNull(cMetadata);
        // TODO: Determine how to handle metadata name case change (metadata-name becomes Metadata-Name)
        //assertEquals(CONTENT_META_VALUE, CONTENT_META_NAME);

        // Mime type was not included when setting content metadata
        // so it should have been reset to a default value
        assertEquals(StorageProvider.DEFAULT_MIMETYPE, cMetadata.get(CONTENT_MIME_NAME));

        // test deleteContent()
        log.debug("Test deleteContent()");
        rackspaceProvider.deleteContent(SPACE_ID, CONTENT_ID);
        spaceContents = rackspaceProvider.getSpaceContents(SPACE_ID, null);
        assertFalse(contains(spaceContents, CONTENT_ID));

        // test deleteSpace()
        log.debug("Test deleteSpace()");
        rackspaceProvider.deleteSpace(SPACE_ID);
        spaces = rackspaceProvider.getSpaces();
        assertFalse(contains(spaces, SPACE_ID));
    }

    private void addContent(String spaceId, String contentId) {
        byte[] content = CONTENT_DATA.getBytes();
        int contentSize = content.length;
        ByteArrayInputStream contentStream = new ByteArrayInputStream(content);
        String checksum = rackspaceProvider.addContent(spaceId,
                                                       contentId,
                                                       CONTENT_MIME_VALUE,
                                                       contentSize,
                                                       contentStream);
        compareChecksum(rackspaceProvider, spaceId, contentId, checksum);
    }
}
