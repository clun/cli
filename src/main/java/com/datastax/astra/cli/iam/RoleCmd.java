package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "role", 
         description = "Manage roles", 
         mixinStandardHelpOptions = true,
         subcommands = { RoleGetCmd.class, RoleListCmd.class})
public class RoleCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        new RoleListCmd().execute();
    }

}
