package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.db.exception.InvalidDatabaseStateException;
import com.datastax.astra.sdk.databases.domain.DatabaseStatusType;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Create a DB with the CLI (initializing connection)
 *
 * astra db create NAME -r eu-east1 -ks ks1
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = AbstractCmd.CREATE, 
         description = "Create a database",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
public class DbCreateCmd extends AbstractConnectedCmd implements DatabaseConstants {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or identifier")
    public String db;
    
    /** 
     * Database or keyspace are created when needed
     **/
    @Option(names = { "--if-not-exist", "--if-not-exists" }, 
            description = "will create a new DB only if none with same name")
    protected boolean ifNotExist = false;

    /**
     * Cloud provider region to provision
     */
    @Option(names = { "-r", "--region" }, 
            paramLabel = "REGION", 
            arity = "1", 
            description = "Cloud provider region to provision")
    protected String databaseRegion = DEFAULT_REGION;
    
    /**
     * Default keyspace created with the Db
     */
    @Option(names = { "-k", "--keyspace" }, paramLabel = "KEYSPACE", 
            arity = "1", 
            description = "Default keyspace created with the Db")
    protected String defaultKeyspace;
    
    /** 
     * Will wait until the database become ACTIVE.
     */
    @Option(names = { "--wait" }, 
            description = "Will wait until the database become ACTIVE")
    protected boolean wait = false;
    
    /** 
     * Provide a limit to the wait period in seconds, default is 180s. 
     */
    @Option(names = { "--timeout" }, 
            description = "Provide a limit to the wait period in seconds, default is 180s.")
    protected int timeout = 180;
    
    /**
     * Working with databases.
     */
    @Inject 
    DatabaseService dbService;
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        dbService.createDb(db, databaseRegion, defaultKeyspace, ifNotExist);
        if (wait) {
            switch(dbService.waitForDbStatus(db, DatabaseStatusType.ACTIVE, timeout)) {
                case NOT_FOUND:
                    throw new DatabaseNotFoundException(db);
                case UNAVAILABLE:
                    throw new InvalidDatabaseStateException(db, DatabaseStatusType.ACTIVE,  DatabaseStatusType.PENDING);
                default:
                    LoggerShell.success("Database \'" + db +  "' has been created.");
                break;  
            }
        }
    }
    
}
