package com.datastax.astra.cli.db.keyspace;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.OperationsDb;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.db.exception.KeyspaceAlreadyExistException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Delete a DB is exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = OperationsDb.CMD_CREATE_KEYSPACE, 
         description = "Create keyspace")
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
    
    /** {@inheritDoc}  */
    public void execute()
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException,
           InvalidArgumentException, KeyspaceAlreadyExistException  {
        OperationsDb.createKeyspace(db, keyspace, ifNotExist);
    }
    
}
