package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.db.exception.InvalidDatabaseStateException;
import com.datastax.astra.sdk.databases.domain.DatabaseStatusType;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Delete a DB is exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = OperationsDb.CMD_RESUME, 
        description = "Resume a db if needed")
public class DbResumeCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
   @Parameters(
           arity = "1",
           paramLabel = "DB",
           description = "Database name or identifier")
   public String db;
    
    /** 
     * Will wait until the database become ACTIVE.
     */
    @Option(names = { "--wait" }, 
            description = "Make the command blocking")
    protected boolean wait = false;
    
    /** 
     * Provide a limit to the wait period in seconds, default is 180s. 
     */
    @Option(names = { "--timeout" }, 
            description = "Wait timeout in seconds (default: 180)")
    protected int timeout = 180;
    
    /** {@inheritDoc}  */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, 
           InvalidDatabaseStateException  {
        OperationsDb.resumeDb(db);
        if (wait) {
           switch(OperationsDb.waitForDbStatus(db, DatabaseStatusType.ACTIVE, timeout)) {
            case NOT_FOUND:
                throw new DatabaseNotFoundException(db);
            case UNAVAILABLE:
                throw new InvalidDatabaseStateException(db, DatabaseStatusType.ACTIVE,  DatabaseStatusType.HIBERNATED);
            default:
                LoggerShell.success("Database \'" + db +  "' has resumed");
            break;
           }
        } else {
            LoggerShell.success("Database \'" + db +  "' is resuming");
        }
    }
    
}
