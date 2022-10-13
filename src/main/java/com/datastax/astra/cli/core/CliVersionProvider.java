package com.datastax.astra.cli.core;

import picocli.CommandLine.IVersionProvider;

/**
 * Provide version.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class CliVersionProvider implements IVersionProvider{

    /** {@inheritDoc} */
    @Override
    public String[] getVersion() throws Exception {
        return new String[] { "0.1.alpha6"};
    }

}
