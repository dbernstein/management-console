/*
 * Copyright (c) 2009-2010 DuraSpace. All rights reserved.
 */
package org.duracloud.account.monitor.domain;

import org.duracloud.account.common.domain.AccountInfo;
import org.duracloud.account.monitor.util.HadoopUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.duracloud.account.monitor.util.HadoopUtil.STATE;

/**
 * @author Andrew Woods
 *         Date: 7/11/11
 */
public class HadoopServiceReportTest {

    private HadoopServiceReport report;

    private static final String SUBDOMAIN_PREFIX = "acct-name-";
    private static final String SERVICE_NAME_PREFIX = "service-name-";


    @Before
    public void setUp() throws Exception {
        int thresholdDays = 5;
        report = new HadoopServiceReport(thresholdDays);
    }

    @Test
    public void testAddAcctError() {
        Assert.assertFalse(report.hasErrors());

        final int id0 = 0;
        final int id1 = 1;
        final String error0 = "error-msg-0";
        final String error1 = "error-msg-1";

        AccountInfo acct0 = createAccount(id0);
        AccountInfo acct1 = createAccount(id1);
        report.addAcctError(acct0, error0);
        report.addAcctError(acct1, error1);

        Assert.assertTrue(report.hasErrors());

        Map<AccountInfo, String> errorsByAcct = report.getErrorsByAcct();
        Assert.assertNotNull(errorsByAcct);
        Assert.assertEquals(2, errorsByAcct.size());

        Collection<String> errors = errorsByAcct.values();
        Assert.assertTrue(errors.contains(error0));
        Assert.assertTrue(errors.contains(error1));

        Map<AccountInfo, Collection<HadoopServiceInfo>> servicesByAcct =
            report.getServicesByAcct();
        Assert.assertNotNull(servicesByAcct);
        Assert.assertEquals(0, servicesByAcct.size());
    }

    @Test
    public void testAddAcctServices() throws Exception {
        int id0 = 0;
        int numRunning0 = 3;
        int numComplete0 = 3;
        AccountInfo acct0 = addToReport(id0, numRunning0, numComplete0, report);

        int id1 = 1;
        int numRunning1 = 2;
        int numComplete1 = 3;
        AccountInfo acct1 = addToReport(id1, numRunning1, numComplete1, report);

        int id2 = 2;
        int numRunning2 = 0;
        int numComplete2 = 1;
        AccountInfo acct2 = addToReport(id2, numRunning2, numComplete2, report);

        int id3 = 3;
        int numRunning3 = 0;
        int numComplete3 = 0;
        AccountInfo acct3 = addToReport(id3, numRunning3, numComplete3, report);

        Map<AccountInfo, String> errorsByAcct = report.getErrorsByAcct();
        Assert.assertNotNull(errorsByAcct);
        Assert.assertEquals(0, errorsByAcct.size());

        Map<AccountInfo, Collection<HadoopServiceInfo>> servicesByAcct =
            report.getServicesByAcct();
        Assert.assertNotNull(servicesByAcct);
        Assert.assertEquals(4, servicesByAcct.size());

        Collection<HadoopServiceInfo> services0 = servicesByAcct.get(acct0);
        Collection<HadoopServiceInfo> services1 = servicesByAcct.get(acct1);
        Collection<HadoopServiceInfo> services2 = servicesByAcct.get(acct2);
        Collection<HadoopServiceInfo> services3 = servicesByAcct.get(acct3);

        Assert.assertNotNull(services0);
        Assert.assertNotNull(services1);
        Assert.assertNotNull(services2);
        Assert.assertNotNull(services3);

        Assert.assertEquals(numRunning0 + numComplete0, services0.size());
        Assert.assertEquals(numRunning1 + numComplete1, services1.size());
        Assert.assertEquals(numRunning2 + numComplete2, services2.size());
        Assert.assertEquals(numRunning3 + numComplete3, services3.size());

        verifyServices(id0, numRunning0, STATE.RUNNING, services0);
        verifyServices(id0, numComplete0, STATE.COMPLETED, services0);

        verifyServices(id1, numRunning1, STATE.RUNNING, services1);
        verifyServices(id1, numComplete1, STATE.COMPLETED, services1);

        verifyServices(id2, numRunning2, STATE.RUNNING, services2);
        verifyServices(id2, numComplete2, STATE.COMPLETED, services2);

        verifyServices(id3, numRunning3, STATE.RUNNING, services3);
        verifyServices(id3, numComplete3, STATE.COMPLETED, services3);
    }

    private void verifyServices(int id,
                                int num,
                                STATE state,
                                Collection<HadoopServiceInfo> services) {
        if (num > 0) {
            for (int i = 0; i < num; ++i) {
                String name = serviceName(id, i, state);
                verifyContains(name, services);
            }
        }
    }

    private void verifyContains(String name,
                                Collection<HadoopServiceInfo> services) {
        Iterator itr = services.iterator();
        while (itr.hasNext()) {
            HadoopServiceInfo service = (HadoopServiceInfo) itr.next();
            if (name.equals(service.getName())) {
                return;
            }
        }
        Assert.fail("Name not found in services: " + name + ", " + services);
    }

    private AccountInfo addToReport(int id,
                                    int numRunning,
                                    int numCompleted,
                                    HadoopServiceReport report) {
        AccountInfo acct = createAccount(id);
        Collection<HadoopServiceInfo> services = createAllServices(id,
                                                                   numRunning,
                                                                   numCompleted);
        report.addAcctServices(acct, services);
        return acct;
    }

    private Collection<HadoopServiceInfo> createAllServices(int id,
                                                            int numRunning,
                                                            int numCompleted) {
        Collection<HadoopServiceInfo> services =
            new HashSet<HadoopServiceInfo>();
        services.addAll(createServices(id, numRunning, STATE.RUNNING));
        services.addAll(createServices(id, numCompleted, STATE.COMPLETED));
        return services;
    }

    private Collection<HadoopServiceInfo> createServices(int id,
                                                         int num,
                                                         HadoopUtil.STATE state) {
        Date now = new Date();
        Set<HadoopServiceInfo> services = new HashSet<HadoopServiceInfo>();
        for (int i = 0; i < num; ++i) {
            services.add(new HadoopServiceInfo(serviceName(id, i, state),
                                               state.name(),
                                               now,
                                               now));
        }
        return services;
    }

    private String serviceName(int id, int i, HadoopUtil.STATE state) {
        return SERVICE_NAME_PREFIX + id + "-" + i + "-" + state.name();
    }

    private AccountInfo createAccount(int id) {
        return new AccountInfo(id,
                               SUBDOMAIN_PREFIX + id,
                               null,
                               null,
                               null,
                               -1,
                               -1,
                               null,
                               null,
                               -1,
                               null,
                               null);
    }
}
