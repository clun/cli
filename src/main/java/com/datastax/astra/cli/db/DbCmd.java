package com.datastax.astra.cli.db;

import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "db", 
         description = "Manage databases", 
         subcommands = { DbListCmd.class })
public class DbCmd {

}
