/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package org.duracloud.account.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all controllers.
 *
 * @contributor "Daniel Bernstein (dbernstein@duraspace.org)"
 */

public abstract class AbstractController {

    protected Logger log = LoggerFactory.getLogger(getClass());

	protected static final String PREFIX = "/app";


	public static final String NEW_MAPPING = "/new";

    public void init() {
    	log.info("initializing " + this.toString());
    }

    public void destroy() {
    	log.info("destroying " + this.toString());
    }
}