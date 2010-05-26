
package org.duracloud.duradmin;

import org.duracloud.common.web.RestHttpHelper;
import org.duracloud.common.web.RestHttpHelper.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * Runtime test of Duradmin. The durastore web application must be deployed and
 * available in order for these tests to pass.
 * 
 * @author Bill Branan
 */
public class TestDuradmin extends DuradminTestBase {

    private static RestHttpHelper restHelper = RestTestHelper.getAuthorizedRestHelper();

    @Test
    public void testSpaces() throws Exception {
        String url = getBaseUrl() + "/spaces.htm";
        HttpResponse response = restHelper.get(url);
        Assert.assertEquals(200, response.getStatusCode());

        String responseText = response.getResponseBody();
        Assert.assertNotNull(responseText);
    }

}