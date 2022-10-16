package com.datastax.astra.cli.db.keyspace;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.db.DatabaseService;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Delete a DB is exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "create-keyspace", 
         description = "Create keyspace",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
public class DbCreateKeyspaceCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or id")
    public String db;
   
    /**
     * Default keyspace created with the Db
     */
    @Option(names = { "-k", "--keyspace" }, 
            paramLabel = "KEYSPACE", 
            arity = "1", 
            description = "Default keyspace created with the Db")
    protected String keyspace;
    
    /** 
     * Database or keyspace are created when needed
     **/
    @Option(names = { "--if-not-exist", "--if-not-exists" }, 
            description = "will create a new DB only if none with same name")
    protected boolean ifNotExist = false;
    
    @Inject
    DatabaseService dbService;
    
    /** {@inheritDoc}  */
    public void execute() {
        dbService.createKeyspace(db, keyspace, ifNotExist);
    }
    
}
