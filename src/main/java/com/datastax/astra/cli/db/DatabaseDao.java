package com.datastax.astra.cli.db;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.utils.AstraCliUtils;
import com.datastax.astra.cli.utils.FileUtils;
import com.datastax.astra.sdk.config.AstraClientConfig;
import com.datastax.astra.sdk.databases.DatabaseClient;
import com.datastax.astra.sdk.databases.DatabasesClient;
import com.datastax.astra.sdk.databases.domain.Database;
import com.datastax.astra.sdk.databases.domain.Datacenter;

import jakarta.inject.Singleton;

/**
 * Unitary operation for databases.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Singleton
public class DatabaseDao {
    
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
    public Database getDatabase(String databaseName) 
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
    public DatabaseClient getRequiredDatabaseClient(String databaseName) {
        Optional<DatabaseClient> dbClient = getDatabaseClient(databaseName);
        if (dbClient.isPresent()) {
            return dbClient.get();
        }
        throw new DatabaseNotFoundException(databaseName);
    }
    
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
    public Optional<DatabaseClient> getDatabaseClient(String db) 
    throws DatabaseNameNotUniqueException {
        DatabasesClient dbsClient = CliContext.getInstance().getApiDevopsDatabases();
        
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
        
        LoggerShell.warning("Database " + db + " has not been found");
        return Optional.empty();
    }
    
    /**
     * Download the cloud secure bundles.
     * 
     * @param databaseName
     *      database name and id
     * @throws DatabaseNameNotUniqueException 
     *      error if db name is not unique
     * @throws DatabaseNotFoundException 
     *      error is db is not found
     */
    public void downloadCloudSecureBundles(String databaseName)
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException {
        getRequiredDatabaseClient(databaseName)
            .downloadAllSecureConnectBundles(
                    AstraCliUtils.ASTRA_HOME + File.separator + AstraCliUtils.SCB_FOLDER);
        LoggerShell.info("Secure connect bundles have been downloaded.");
    }
    
    /**
     * Download SCB when needed.
     *
     * @param databaseName
     *      database name.
     * @param region
     *      specified region 
     * @param location
     *      location
     * @throws DatabaseNameNotUniqueException 
     *      error if db name is not unique
     * @throws DatabaseNotFoundException 
     *      error is db is not found
     * @throws InvalidArgumentException
     *      invalid argument to download sb. 
     */
    public void downloadCloudSecureBundle(String databaseName, String region, String location)
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, InvalidArgumentException {
        Database        db  = getDatabase(databaseName);
        Set<Datacenter> dcs = db.getInfo().getDatacenters();
        Datacenter      dc  = dcs.iterator().next();
        if (dcs.size() > 1) {
            if (null == region) {
                throw new InvalidArgumentException(""
                        + "Your database is deployed on multiple regions. "
                        + "A scb is associated to only one region. "
                        + "Add -r or --region in the command to select one.");
            }
            Optional<Datacenter> optDc = dcs.stream()
               .filter(d -> d.getName().equals(region)).findFirst();
            if (optDc.isEmpty()) {
                throw new InvalidArgumentException(""
                        + "Your database is deployed on multiple regions. "
                        + "You selectect and invalid region name. "
                        + "Please use one from %s".formatted(
                                dcs.stream().map(Datacenter::getName)
                                   .toList().toString()));
            }
            dc = optDc.get();
        }
        
        // Default location
        if (location == null) {
            location = "." + File.separator + AstraClientConfig.buildScbFileName(db.getId(), dc.getName());
        }
        FileUtils.downloadFile(dc.getSecureBundleUrl(), location);
        LoggerShell.info("Bundle downloaded in %s".formatted(location));
    }
    

}
