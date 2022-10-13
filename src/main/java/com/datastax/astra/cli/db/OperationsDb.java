package com.datastax.astra.cli.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.ExitCode;
import com.datastax.astra.cli.core.out.AstraCliConsole;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.core.out.ShellTable;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.db.exception.InvalidDatabaseStateException;
import com.datastax.astra.sdk.databases.DatabaseClient;
import com.datastax.astra.sdk.databases.DatabasesClient;
import com.datastax.astra.sdk.databases.domain.Database;
import com.datastax.astra.sdk.databases.domain.DatabaseStatusType;
import com.datastax.astra.sdk.utils.ApiLocator;

/**
 * Utility class for command `db`
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class OperationsDb {

    /** Command constants. */
    public static final String DB                    = "db";
    
    /** Command constants. */
    public static final String CMD_KEYSPACE          = "keyspace";
    
    /** Command constants. */
    public static final String CMD_INFO              = "info";
    
    /** Command constants. */
    public static final String CMD_STATUS            = "status";
    
    /** Command constants. */
    public static final String CMD_CREATE_KEYSPACE   = "create-keyspace";
    
    /** Command constants. */
    public static final String CMD_DELETE_KEYSPACE   = "delete-keyspace";
    
    /** Command constants. */
    public static final String CMD_LIST_KEYSPACES   = "list-keyspaces";
    
    /** Command constants. */
    public static final String CMD_CREATE_REGION     = "create-region";
    
    /** Command constants. */
    public static final String CMD_DELETE_REGION     = "delete-region";
    
    /** Command constants. */
    public static final String CMD_DOWNLOAD_SCB      = "download-scb";
    
    /** Command constants. */
    public static final String CMD_RESUME            = "resume";
    
    /** Default region. **/
    public static final String DEFAULT_REGION        = "us-east-1";
    
    /** Default tier. **/
    public static final String DEFAULT_TIER          = "serverless";
    
    /** Allow Snake case. */
    public static final String KEYSPACE_NAME_PATTERN = "^[_a-z0-9]+$";
    
    /** column names. */
    public static final String COLUMN_ID                = "id";
    /** column names. */
    public static final String COLUMN_NAME              = "Name";
    /** column names. */
    public static final String COLUMN_DEFAULT_REGION    = "Default Region";
    /** column names. */
    public static final String COLUMN_REGIONS           = "Regions";
    /** column names. */
    public static final String COLUMN_DEFAULT_CLOUD     = "Default Cloud Provider";
    /** column names. */
    public static final String COLUMN_STATUS            = "Status";
    /** column names. */
    public static final String COLUMN_DEFAULT_KEYSPACE  = "Default Keyspace";
    /** column names. */
    public static final String COLUMN_KEYSPACES         = "Keyspaces";
    /** column names. */
    public static final String COLUMN_CREATION_TIME     = "Creation Time";
    
    /**
     * Hide default constructor.
     */
    private OperationsDb() {}
    
    /**
     * Load the databaseClient by user input.
     * 
     * @param db
     *      database name or identifier
     * @return
     *      db id
     * @throws DatabaseNameNotUniqueException 
     *      cli does not work if multiple db with same name
     */
    public static Optional<DatabaseClient> getDatabaseClient(String db) 
    throws DatabaseNameNotUniqueException {
        DatabasesClient dbsClient = ctx().getApiDevopsDatabases();
        
        // Escape special chars
        db = db.replaceAll("\"", "");
        // Database name containing spaces cannot be an id
        if (!db.contains(" ") ) {
            DatabaseClient dbClient = dbsClient.database(db);
            if (dbClient.exist()) {
                LoggerShell.debug("Database found id=" + dbClient.getDatabaseId());
                return Optional.ofNullable(dbClient);
            }
        }
        
        // Not found, try with the name
        List<Database> dbs = dbsClient
                .databasesNonTerminatedByName(db)
                .collect(Collectors.toList());
        
        // Multiple databases with the same name
        if (dbs.size() > 1) {
            throw new DatabaseNameNotUniqueException(db);
        }
        
        // Database exists and is unique
        if (1 == dbs.size()) {
            LoggerShell.debug("Database found id=" + dbs.get(0).getId());
            return Optional.ofNullable(dbsClient.database(dbs.get(0).getId()));
        }
        return Optional.empty();
    }
    
    /**
     * Wait for a DB status.
     *
     * @param databaseName
     *      database name
     * @param status
     *      expected status
     * @param timeout
     *      timeout number of loop to wait
     * @return
     *      db is in correct status
     * @throws DatabaseNameNotUniqueException
     *      db name is present multiple times
     * @throws DatabaseNotFoundException
     *      database has not been found
     */
    public static ExitCode waitForDbStatus(String databaseName, DatabaseStatusType status, int timeout) 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        long start = System.currentTimeMillis();
        Database db = getDatabase(databaseName);
        if (db.getStatus().equals(status)) {
            return ExitCode.SUCCESS;
        }
        LoggerShell.success("Database '%s' has status '%s', waiting to be '%s'"
                   .formatted(databaseName, db.getStatus(), status));
        int retries = 0;
        do {
            try {
                db = getDatabase(databaseName);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LoggerShell.error("Interupted operation: %s".formatted(e.getMessage()));
                Thread.currentThread().interrupt();
            }
        } while (retries++ < timeout && !db.getStatus().equals(status));
        
        // Success if you did not reach the timeout (meaning status is good)
        if (retries < timeout) {
            LoggerShell.success("Database '%s' has status '%s' (took %d millis)"
                    .formatted(databaseName, db.getStatus(), System.currentTimeMillis() - start));
            return ExitCode.SUCCESS;
        } 
        LoggerShell.warning("Timeout %d s: Database '%s' status is not yet '%s' (current status '%s')"
                       .formatted(timeout, databaseName, status,  db.getStatus()));
        return ExitCode.UNAVAILABLE;
    }
     
    
    private static CliContext ctx() {
        return CliContext.getInstance();
    }
    
    /**
     * List Databases.
     */
    public static void listDb() {
        ShellTable sht = new ShellTable();
        sht.addColumn(COLUMN_NAME,    20);
        sht.addColumn(COLUMN_ID,      37);
        sht.addColumn(COLUMN_DEFAULT_REGION, 20);
        sht.addColumn(COLUMN_STATUS,  15);
        new DatabasesClient(ctx().getToken())
           .databasesNonTerminated()
           .forEach(db -> {
                Map <String, String> rf = new HashMap<>();
                rf.put(COLUMN_NAME,    db.getInfo().getName());
                rf.put(COLUMN_ID,      db.getId());
                rf.put(COLUMN_DEFAULT_REGION, db.getInfo().getRegion());
                rf.put(COLUMN_STATUS,  db.getStatus().name());
                sht.getCellValues().add(rf);
        });
        AstraCliConsole.printShellTable(sht);
    }
    
    /**
     * List keyspaces of a database.
     *
     * @param databaseName
     *      database name
     * @throws DatabaseNameNotUniqueException
     *      multiple databases with the name.
     * @throws DatabaseNotFoundException
     *      database name has not been found.
     */
    public static void listKeyspaces(String databaseName)
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        Database   db  = getDatabase(databaseName);
        ShellTable sht = new ShellTable();
        sht.addColumn(COLUMN_NAME,    20);
        db.getInfo().getKeyspaces().forEach(ks -> {
            Map <String, String> rf = new HashMap<>();
            if (db.getInfo().getKeyspace().equals(ks)) {
                rf.put(COLUMN_NAME, ks + " (default)");
            } else {
                rf.put(COLUMN_NAME, ks);
            }
            sht.getCellValues().add(rf);
        });
        AstraCliConsole.printShellTable(sht);
    }
    
    /**
     * Delete a dabatase if exist.
     * 
     * @param databaseName
     *      db name or db id
     * @throws DatabaseNameNotUniqueException 
     *      error if db name is not unique
     * @throws DatabaseNotFoundException 
     *      error is db is not found
     */
    public static void deleteDb(String databaseName) 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        getRequiredDatabaseClient(databaseName).delete();
        AstraCliConsole.outputSuccess("Deleting Database '%s' (async operation)".formatted(databaseName));
    }
  
    /**
     * Resume a dabatase if exist.
     * 
     * @param databaseName
     *      db name or db id
     * @throws DatabaseNameNotUniqueException 
     *      error if db name is not unique
     * @throws DatabaseNotFoundException 
     *      error is db is not found
     * @throws InvalidDatabaseStateException
     *      database is in invalid state
     */
    public static void resumeDb(String databaseName)
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, 
           InvalidDatabaseStateException {
        Database db = getDatabase(databaseName);
        switch(db.getStatus()) {
            case HIBERNATED:
                resumeDbRequest(db);
                LoggerShell.success("Database \'" + databaseName +  "' is resuming");
                break;
            case RESUMING:
                LoggerShell.warning("Database '" + databaseName + "'is already resuming");
                break;
            case INITIALIZING:
            case PENDING:
                LoggerShell.success("Database '" + databaseName + "'is starting");
                break;
            case ACTIVE:
                LoggerShell.success("Database '" + databaseName + "'is already active");
                break;
            default:
                throw new InvalidDatabaseStateException(databaseName, DatabaseStatusType.HIBERNATED, db.getStatus());
        }
    }
    
    /**
     * Database name.
     *
     * @param db
     *      database name
     */
    private static void resumeDbRequest(Database db) {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            StringBuilder keyspacesUrl = new StringBuilder(
                        ApiLocator.getApiRestEndpoint(
                                db.getId(), 
                                db.getInfo().getRegion()));
            keyspacesUrl.append("/v2/schemas/keyspace");
            HttpUriRequestBase req = new HttpGet(keyspacesUrl.toString());
            req.addHeader("accept", "application/json");
            req.addHeader("X-Cassandra-Token", CliContext.getInstance().getToken());
            httpClient.execute(req);
         } catch (IOException e) {
             throw new IllegalArgumentException(e);
         }
    }
    
    /**
     * Display status of a database.
     * 
     * @param databaseName
     *      database name
     * @throws DatabaseNameNotUniqueException 
     *      error if db name is not unique
     * @throws DatabaseNotFoundException 
     *      error is db is not found
     */
    public static void showDbStatus(String databaseName)
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        AstraCliConsole.outputSuccess("Database '%s' has status '%s'"
                    .formatted(databaseName, getDatabase(databaseName).getStatus()));
    }
    
    /**
     * Access unique db.
     * 
     * @param databaseName
     *      database name
     * @return
     *      unique db
     * @throws DatabaseNameNotUniqueException
     *      error when multiple dbs
     * @throws DatabaseNotFoundException
     *      error when db does not exists
     */
    public static Database getDatabase(String databaseName) 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        Optional<Database> optDb = getRequiredDatabaseClient(databaseName).find();
        if (optDb.isPresent()) {
            return optDb.get();
        }
        throw new DatabaseNotFoundException(databaseName);
    }
    
    /**
     * Access unique db.
     * 
     * @param databaseName
     *      database name
     * @return
     *      unique db
     * @throws DatabaseNameNotUniqueException
     *      error when multiple dbs
     * @throws DatabaseNotFoundException
     *      error when db does not exists
     */
    private static DatabaseClient getRequiredDatabaseClient(String databaseName) 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        Optional<DatabaseClient> dbClient = getDatabaseClient(databaseName);
        if (dbClient.isPresent()) {
            return dbClient.get();
        }
        throw new DatabaseNotFoundException(databaseName);
    }
 }
