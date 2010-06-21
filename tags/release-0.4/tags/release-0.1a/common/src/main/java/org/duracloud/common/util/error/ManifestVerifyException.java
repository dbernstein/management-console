package org.duracloud.common.util.error;

import java.util.List;

/**
 * @author Andrew Woods
 *         Date: Oct 24, 2009
 */
public class ManifestVerifyException extends DuraCloudException {

    private ErrorType errorType;

    /**
     * Error types of specific to verifying checksum manifests.
     */
    public static enum ErrorType {
        UNEQUAL_NUM_ENTRIES("duracloud.error.manifest.unequal"),
        CHKSUM_MISMATCH("duracloud.error.manifest.chksum");

        private String key;

        ErrorType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * @param file0  Filename of first manifest file.
     * @param file1  Filename of second manifest file.
     * @param count0 Number of entries in first manifest file.
     * @param count1 Number of entries in second manifest file.
     */
    public ManifestVerifyException(String file0,
                                   String file1,
                                   int count0,
                                   int count1) {
        super();
        this.errorType = ErrorType.UNEQUAL_NUM_ENTRIES;
        setArgs(file0,
                file1,
                Integer.toString(count0),
                Integer.toString(count1));
    }

    /**
     * @param file0          Filename of first manifest file.
     * @param file1          Filename of second manifest file.
     * @param badChksumPairs Pairs of chksum that do not match {file0:file1}
     */
    public ManifestVerifyException(String file0,
                                   String file1,
                                   List<String> badChksumPairs) {
        super();
        this.errorType = ErrorType.CHKSUM_MISMATCH;

        StringBuilder sb = new StringBuilder();
        for (String pair : badChksumPairs) {
            sb.append(pair);
            sb.append("\n");
        }
        setArgs(file0, file1, sb.toString());
    }

    @Override
    public String getKey() {
        return errorType.getKey();
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }


}
