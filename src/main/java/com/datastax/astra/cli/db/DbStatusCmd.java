package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a db.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = OperationsDb.CMD_STATUS, 
         description = "Show details of a database")
public class DbStatusCmd extends AbstractConnectedCmd {
   
    /**
     * Database name or identifier
     */
   @Parameters(
           arity = "1",
           paramLabel = "DB",
           description = "Database name or identifier")
   public String db;
   
    /** {@inheritDoc}  */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException  {
        OperationsDb.showDbStatus(db);
    }

}
