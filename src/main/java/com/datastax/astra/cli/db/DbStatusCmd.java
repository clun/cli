package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a db.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "status", 
         description = "Show details of a database",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
public class DbStatusCmd extends AbstractConnectedCmd {
   
    /**
     * Database name or identifier
     */
   @Parameters(
           arity = "1",
           paramLabel = "DB",
           description = "Database name or identifier")
   public String db;
   
   /**
    * Working with databases.
    */
   @Inject 
   DatabaseService dbService;
   
    /** {@inheritDoc}  */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException  {
        dbService.showDbStatus(db);
    }

}
