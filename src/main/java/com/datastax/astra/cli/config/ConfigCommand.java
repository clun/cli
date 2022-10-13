package com.datastax.astra.cli.config;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.CliVersionProvider;

import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "config", 
         description = "Manage local configurations", 
         mixinStandardHelpOptions = true, 
         versionProvider = CliVersionProvider.class,
         subcommands = { 
           ConfigCreateCmd.class, ConfigDeleteCmd.class, 
           ConfigListCmd.class, ConfigGetCmd.class })
public class ConfigCommand extends AbstractCmd {

    /** {@inheritDoc} */
    @Override
    protected void execute() {
        new ConfigListCmd().execute();
    }

}
