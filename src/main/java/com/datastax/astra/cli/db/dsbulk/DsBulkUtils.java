package com.datastax.astra.cli.db.dsbulk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.db.exception.SecureBundleNotFoundException;
import com.datastax.astra.cli.utils.AstraCliUtils;
import com.datastax.astra.cli.utils.FileUtils;
import com.datastax.astra.sdk.config.AstraClientConfig;
import com.datastax.astra.sdk.databases.domain.Database;

/**
 * Utilities to work with DSBulk.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class DsBulkUtils {

    /** Version Number. */
    public static final String DSBULK_VERSION = AstraCliUtils.readProperty("dsbulk.version");
    
    /** Archive name. */
    public static final String DSBULK_TARBALL = "dsbulk-" + DSBULK_VERSION + ".tar.gz";
    
    /** URL. */
    public static final String DSBULK_DOWNLOAD = AstraCliUtils.readProperty("dsbulk.url") + DSBULK_TARBALL;
    
    /** Folder name of dslbulk once untar. */
    public static final String DSBULK_FOLDER = "dsbulk-" + DSBULK_VERSION;
   
    /**
     * Hide default construtor
     */
    private DsBulkUtils() {}
    
    /**
     * Check if cqlshel has been installed.
     *
     * @return
     *      if the folder exist
     */
    public static boolean isDsBulkInstalled() {
       File dsbulkFolder = new File(AstraCliUtils.ASTRA_HOME + File.separator + DSBULK_FOLDER);
       return dsbulkFolder.exists() && dsbulkFolder.isDirectory();
    }
    
    /**
     * Download targz and unzip.
     *
     * @throws FileSystemException
     *      cannot untar archive on disk;
     */
    public static void installDsBulk() throws FileSystemException {
        if (!isDsBulkInstalled()) {
            LoggerShell.success("dsbulk first launch, downloading (~25MB), please wait...");
            String destination = AstraCliUtils.ASTRA_HOME + File.separator + DSBULK_TARBALL;
            FileUtils.downloadFile(DSBULK_DOWNLOAD, destination);
            File dsbulkTarball = new File (destination);
            if (dsbulkTarball.exists()) {
                LoggerShell.info("File Downloaded. Extracting archive, please wait...");
                try {
                    FileUtils.extactTargzInAstraCliHome(dsbulkTarball);
                    if (isDsBulkInstalled()) {
                        // Change file permission
                        File dsBulkFile = new File(AstraCliUtils.ASTRA_HOME + File.separator  
                                + DSBULK_FOLDER + File.separator 
                                + "bin" + File.separator  
                                + "dsbulk");
                        if (!dsBulkFile.setExecutable(true, false)) {
                            throw new FileSystemException("Cannot set dsbulk file as executable");
                        }
                        if (!dsBulkFile.setReadable(true, false)) {
                            throw new FileSystemException("Cannot set dsbulk file as readable");
                        }
                        if (!dsBulkFile.setWritable(true, false)) {
                            throw new FileSystemException("Cannot set dsbulk file as writable");
                        }
                        LoggerShell.success("DSBulk has been installed");
                        if (!dsbulkTarball.delete()) {
                            LoggerShell.warning("DSBulk Tar archived was not deleted");
                        }
                    }
                } catch (IOException e) {
                    LoggerShell.error("Cannot extract tar archive:" + e.getMessage());
                        throw new FileSystemException("Cannot extract tar archive:" + e.getMessage(), e);
                }
            }
        } else {
            LoggerShell.info("DSBulk is already installed");
        }
    }

    /**
     * Install CqlShell if needed and start the program.
     * 
     * @param dsbulkParams
     *      parameters for dsbulk
     * @param db
     *      database retrieved
     * @return
     *      unix process for cqlsh
     * @throws IOException
     *      errors occured
     * @throws SecureBundleNotFoundException
     *      cannot access secure bundle
     */
    public static Process runDsBulk(Database db, List<String> dsbulkParams) 
    throws IOException, SecureBundleNotFoundException {
        List<String> commandDsbulk = new ArrayList<>();
        commandDsbulk.add(new StringBuilder()
                .append(AstraCliUtils.ASTRA_HOME + File.separator + DSBULK_FOLDER)
                .append(File.separator + "bin")
                .append(File.separator + "dsbulk")
                .toString());
        commandDsbulk.addAll(dsbulkParams);
        commandDsbulk.add("-u");
        commandDsbulk.add("token");
        commandDsbulk.add("-p");
        commandDsbulk.add(CliContext.getInstance().getToken());
        commandDsbulk.add("-b");
        File scb = new File(new StringBuilder()
                .append(AstraCliUtils.ASTRA_HOME + File.separator + AstraCliUtils.SCB_FOLDER + File.separator)
                .append(AstraClientConfig.buildScbFileName(db.getId(), db.getInfo().getRegion()))
                .toString());
        if (!scb.exists()) {
            LoggerShell.error("Cloud Secure Bundle '" + scb.getAbsolutePath() + "' has not been found.");
            throw new SecureBundleNotFoundException(scb.getAbsolutePath());
        }
        commandDsbulk.add(scb.getAbsolutePath());
        // Reducing log level
        commandDsbulk.add("--log.verbosity");
        commandDsbulk.add("normal");
        LoggerShell.info("RUNNING: " + String.join(" ", commandDsbulk));
        ProcessBuilder pb =  new ProcessBuilder(commandDsbulk.toArray(new String[0]));
        pb.inheritIO();
        return pb.start();
    }
    
    
}
