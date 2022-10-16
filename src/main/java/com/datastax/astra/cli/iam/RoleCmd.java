package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.utils.AstraCliUtils;

import picocli.CommandLine;
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
public class RoleCmd extends AbstractCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        CommandLine.usage(RoleCmd.class, System.out, AstraCliUtils.COLOR_SCHEME);
    }

}
