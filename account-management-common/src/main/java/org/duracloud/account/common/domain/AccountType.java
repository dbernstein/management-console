/*
 * Copyright (c) 2009-2012 DuraSpace. All rights reserved.
 */
package org.duracloud.account.common.domain;

/**
 * Defines the types of DuraCloud accounts that are available for subscription.
 *
 * @author: Bill Branan
 * Date: 2/9/12
 */
public enum AccountType {

    FULL("Full DuraCloud Account"),
    COMMUNITY("DuraCloud Community Account");

    private final String text;

    private AccountType(String text) {
        this.text = text;
    }

    public static AccountType fromString(String typeString) {
        for (AccountType aType : values()) {
            if (aType.text.equalsIgnoreCase(typeString) ||
                aType.name().equalsIgnoreCase(typeString)) {
                return aType;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return name();
    }

    @Override
    public String toString() {
        return text;
    }

}