package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Delete a DB is exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@picocli.CommandLine.Command(
        name = OperationsDb.CMD_DOWNLOAD_SCB, 
        description = "Download zip secure bundle")
public class DbDownloadScbCmd extends AbstractConnectedCmd {
    
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
            description = "Cloud provider region")
    protected String region;
    
    /** Authentication token used if not provided in config. */
    @Option(names = { "-f", "--output-file" },
            paramLabel = "DEST", 
            description = "Destination file")
    protected String destination;
    
    /** {@inheritDoc} */
    public void execute()
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, InvalidArgumentException {
        OperationsDb.downloadCloudSecureBundle(db, region, destination);
    }
    
}
