package com.datastax.astra.cli.db.exception;

import com.datastax.astra.cli.core.out.LoggerShell;

/**
 * Database not found
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class SecureBundleNotFoundException extends RuntimeException {

    /** Serial Number. */
    private static final long serialVersionUID = -755957414395745147L;

    /**
     * Constructor with path
     * 
     * @param path
     *      securebundle has not been found
     */
    public SecureBundleNotFoundException(String path) {
        super("SecureConnect bundle has not been found at '" + path + "'.");
        LoggerShell.warning(getMessage());
    }

}
