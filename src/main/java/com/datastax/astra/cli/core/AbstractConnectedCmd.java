package com.datastax.astra.cli.core;

import com.datastax.astra.cli.config.AstraConfiguration;

import picocli.CommandLine.Option;

/**
 * Base command for cli. The cli have to deal with configuration file and initialize connection
 * each tiem where shell already have the context initialized.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractConnectedCmd extends AbstractCmd {
    
    /** Authentication token used if not provided in config. */
    @Option(names = { "--token" }, 
            paramLabel = "AUTH_TOKEN",
            description = "Key to use authenticate each call.")
    protected String token;

    /** Section. */
    @Option(names = { "-conf","--config" }, 
            paramLabel = "CONFIG_SECTION",
            description= "Section in configuration file (default = ~/.astrarc)")
    protected String configSectionName = AstraConfiguration.ASTRARC_DEFAULT;
    
    /** {@inheritDoc} */
    @Override
    public void run() {
        ctx().init(new CoreOptions(verbose, noColor, output, configFilename));
        ctx().initToken(new TokenOptions(token, configSectionName));
        execute();
    }
   
}
