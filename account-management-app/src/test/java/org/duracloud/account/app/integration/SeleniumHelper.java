/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package org.duracloud.account.app.integration;

import com.thoughtworks.selenium.Selenium;

/**
 * @contributor "Daniel Bernstein (dbernstein@duraspace.org)"
 *
 */
public class SeleniumHelper {
	
	public static boolean isTextPresent(Selenium s, String text){
		return s.getHtmlSource().contains(text);
	}
	
	public static String getAppRoot() {
        return "/ama/app";
    }
}