package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Delete a DB is exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.DELETE, 
        description = "Delete a database",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbDeleteCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or id")
    public String db;
    
    /**
     * Working with databases.
     */
    @Inject 
    DatabaseService dbService;
    
    /** {@inheritDoc} */
    public void execute() {
        dbService.deleteDb(db);
    }
    
}
