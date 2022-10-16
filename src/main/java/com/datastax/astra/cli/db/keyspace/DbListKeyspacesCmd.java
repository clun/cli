package com.datastax.astra.cli.db.keyspace;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.db.DatabaseService;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Show Keyspaces for an Database.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "list-keyspaces", 
        description = "Display the list of Keyspaces in an database",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbListKeyspacesCmd extends AbstractConnectedCmd {
   
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or id")
    public String db;
   
    @Inject
    DatabaseService dbService;
    
    /** {@inheritDoc}  */
    public void execute() {
        dbService.listKeyspaces(db);
    }

}
