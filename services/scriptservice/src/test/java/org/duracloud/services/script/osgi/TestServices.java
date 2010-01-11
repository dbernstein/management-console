package org.duracloud.services.script.osgi;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.duracloud.services.ComputeService;
import org.duracloud.services.script.ScriptService;
import org.duracloud.services.script.osgi.ScriptServiceTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Bill Branan
 *         Date: Dec 18, 2009
 */
public class TestServices extends AbstractDuracloudOSGiTestBasePax {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int MAX_TRIES = 10;

    private ScriptService scriptService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        String scriptServiceId = getScriptService().getServiceId();
        File workDir = new File(getScriptService().getWorkDir());
        FileUtils.deleteDirectory(workDir);
    }

    @Test
    public void testScriptService() throws Exception {
        log.debug("testing ScriptService");

        ScriptServiceTester tester =
            new ScriptServiceTester(getScriptService());
        tester.testScriptService();
    }

    protected Object getService(String serviceInterface) throws Exception {
        return getService(serviceInterface, null);
    }

    private Object getService(String serviceInterface, String filter)
        throws Exception {
        ServiceReference[] refs = getBundleContext().getServiceReferences(
            serviceInterface,
            filter);

        int count = 0;
        while ((refs == null || refs.length == 0) && count < MAX_TRIES) {
            count++;
            log.debug("Trying to find service: '" + serviceInterface + "'");
            Thread.sleep(1000);
            refs = getBundleContext().getServiceReferences(serviceInterface,
                                                           filter);
        }
        Assert.assertNotNull("service not found: " + serviceInterface, refs[0]);
        log.debug(getPropsText(refs[0]));
        return getBundleContext().getService(refs[0]);
    }

    private String getPropsText(ServiceReference ref) {
        StringBuilder sb = new StringBuilder("properties:");
        for (String key : ref.getPropertyKeys()) {
            sb.append("\tprop: [" + key);
            sb.append(":" + ref.getProperty(key) + "]\n");
        }
        return sb.toString();
    }

    public ScriptService getScriptService() throws Exception {
        if (scriptService == null) {
            scriptService =
                (ScriptService) getService(ComputeService.class.getName(),
                                           "(duraService=script)");
        }
        Assert.assertNotNull(scriptService);
        return scriptService;
    }

}