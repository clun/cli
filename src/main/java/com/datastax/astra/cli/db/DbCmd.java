package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "db", 
         description = "Manage databases", 
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true,
         subcommands = { DbStatusCmd.class, DbCreateCmd.class, 
                         DbDeleteCmd.class, DbDotEnvCmd.class,
                         DbListCmd.class,   DbResumeCmd.class,
                         DbDownloadScbCmd.class, DbGetCmd.class })
public class DbCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        new DbListCmd().execute();
    }

}
