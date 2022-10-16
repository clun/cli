package com.datastax.astra.cli.db;

import java.nio.file.Paths;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Unforce update of the program.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "create-dotenv", 
        description = "Generate .env file with environment variables", 
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbDotEnvCmd extends AbstractConnectedCmd implements DatabaseConstants {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "DB",
            description = "Database name or identifier")
    public String db;
    
    /**
     * Cloud provider region to provision
     */
    @Option(names = { "-r", "--region" }, 
            arity = "1",
            paramLabel = "REGION", 
            description = "Cloud provider region to provision")
    protected String region = DEFAULT_REGION;
    
    /**
     * Default keyspace created with the Db
     */
    @Option(names = { "-k", "--keyspace" }, 
            arity = "1", 
            paramLabel = "KEYSPACE", 
            description = "Default keyspace created with the Db")
    protected String keyspace;
    
    /**
     * Default keyspace created with the Db
     */
    @Option(names = { "-d", "--directory" }, 
            paramLabel = "DIRECTORY", 
            arity = "1", 
            description = "Destination for the config file")
    protected String destination = Paths.get(".").toAbsolutePath().normalize().toString();
    
    /**
     * Working with databases.
     */
    @Inject 
    DatabaseService dbService;
    
    /** {@inheritDoc} */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, InvalidArgumentException {
        dbService.generateDotEnvFile(db, keyspace, region, destination);
    }

}
