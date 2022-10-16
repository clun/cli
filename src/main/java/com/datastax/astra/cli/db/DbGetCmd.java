package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a db.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = AbstractCmd.GET, 
        description = "Show details of a database",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbGetCmd extends AbstractConnectedCmd {

    /** Enum for db get. */
    public enum DbGetKeys { 
        /** db unique identifier */
        id, 
        /** db status */
        status, 
        /** cloud provider */
        cloud, 
        /** default keyspace */
        keyspace, 
        /** all keyspaces */
        keyspaces, 
        /** default region */
        region, 
        /** all regions */
        regions};
    
    /**
      * Database name or identifier
      */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or identifier")
    public String db;
    
    /** Authentication token used if not provided in config. */
    @Option(names = { "-k", "--key" }, 
            paramLabel = "KEY", 
            description = ""
            + "Valid keys "
            + "'id', 'status', 'cloud', 'keyspace', 'keyspaces', 'region', 'regions'")
    protected DbGetKeys key;
    
    /**
     * Working with databases.
     */
    @Inject 
    DatabaseService dbService;
    
    /** {@inheritDoc} */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {    
        dbService.showDb(db, key);
    }

}
