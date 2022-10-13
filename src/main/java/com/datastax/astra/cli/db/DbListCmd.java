package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Show Databases for an organization 
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "db-list", 
         description = "Display the list of Databases in an organization")
public class DbListCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        OperationsDb.listDb();
    }

}
