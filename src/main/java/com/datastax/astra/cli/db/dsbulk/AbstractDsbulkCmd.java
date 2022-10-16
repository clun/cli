package com.datastax.astra.cli.db.dsbulk;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * This command allows to load data with DsBulk.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractDsbulkCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
    @Parameters(
           arity = "1",
           paramLabel = "DB",
           description = "Database name or identifier")
    public String db;
    
    /**
     * Target Keyspace
     */
    @Option(names = { "-k", "--keyspace" }, 
            paramLabel = "KEYSPACE", 
            description = "Keyspace used for loading or unloading data.")
    protected String keyspace;
    
    /**
     * Target Table
     */
    @Option(names = { "-t", "--table" }, 
            paramLabel = "TABLE", 
            description = "Table used for loading or unloading data. "
                    + "Table names should not be quoted and are case-sensitive.")
    protected String table;
    
    /**
     * Optional filter
     */
    @Option(names = { "--schema.query" },
            paramLabel = "QUERY", 
            description = "Optional to unloac or count")
    protected String query;

    /**
     * Provide parameters in dedicated externalize file
     */
    @Option(names = { "-encoding" },
            paramLabel = "ENCODING", 
            description = "The file name format to use when writing. "
                    + "This setting is ignored when reading and for non-file URLs. ")
    protected String encoding = "UTF-8";
    
    /**
     * Optional filter
     */
    @Option(names = { "-maxConcurrentQueries" },
            paramLabel = "maxConcurrentQueries", 
            description = "The maximum number of concurrent queries that should be carried in parallel.")
    protected String maxConcurrentQueries = "AUTO";
      
    /**
     * Provide parameters in dedicated externalize file
     */
    @Option(names = { "-logDir" },
            paramLabel = "log directory", 
            description = "Optional filter")
    protected String logDir = "./logs";
    
    /**
     * Provide parameters in dedicated externalize file
     */
    @Option(names = { "--dsbulk-config" },
            paramLabel = "DSBULK_CONF_FILE", 
            description = "Not all options offered by the loader DSBulk "
                    + "are exposed in this CLI")
    protected String dsBulkConfig;
    
}
