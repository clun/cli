package com.datastax.astra.cli.config;

import com.datastax.astra.cli.core.AbstractCmd;

import picocli.CommandLine.Command;

/**
 * Show the list of available configurations.
 * 
 * astra config list
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "list", 
         description = "List available configurations.",
         mixinStandardHelpOptions = true)
public class ConfigListCmd extends AbstractCmd {
   
    /** {@inheritDoc} */
    @Override
    public void execute() {
        
        OperationsConfig.listConfigurations();
    }
    
}
