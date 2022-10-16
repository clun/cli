package com.datastax.astra.cli.streaming.pulsarshell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.datastax.astra.cli.core.exception.CannotStartProcessException;
import com.datastax.astra.cli.core.exception.ConfigurationException;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.core.out.LoggerShell;
import com.datastax.astra.cli.streaming.AstraStreamingDao;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;
import com.datastax.astra.cli.utils.AstraCliUtils;
import com.datastax.astra.cli.utils.FileUtils;
import com.datastax.astra.cli.utils.PulsarShellSettings;
import com.datastax.astra.sdk.streaming.domain.Tenant;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Operations related to Pulsar shell
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Singleton
public class PulsarShellService {
    
    @Inject
    PulsarShellSettings settings;
    
    @Inject
    AstraStreamingDao dao;
    
    /** Local installation for CqlSh. */
    File pulsarLocalFolder;
    
    /** Configuration folder. */
    String pulsarConfigurationFolder;
    
    /**
     * Initialization.
     */
    @PostConstruct
    public void init() {
        pulsarConfigurationFolder = AstraCliUtils.ASTRA_HOME + 
                File.separator + "lunastreaming-shell-" + settings.version() +
                File.separator + "conf";
        
        pulsarLocalFolder = new File(AstraCliUtils.ASTRA_HOME 
                + File.separator + "lunastreaming-shell-" + settings.version());
    }
    
    /**
     * Create Pulsar conf for a tenant if needed.
     * 
     * @param tenant
     *      current tenant
     */
    public void createPulsarConf(Tenant tenant) {
        
        FileWriter fileWriter = null;
        PrintWriter pw        = null;
        try {
            fileWriter = new FileWriter(getPulsarConfFile(tenant).getAbsolutePath());
            pw = new PrintWriter(fileWriter);
            pw.printf("webServiceUrl=https://pulsar-%s-%s.api.streaming.datastax.com\n", tenant.getCloudProvider(), tenant.getCloudRegion());
            pw.printf("brokerServiceUrl=pulsar+ssl://pulsar-%s-%s.streaming.datastax.com:6651\n", tenant.getCloudProvider(), tenant.getCloudRegion());
            pw.printf("authPlugin=org.apache.pulsar.client.impl.auth.AuthenticationToken\n");
            pw.printf("authParams=token:%s\n", tenant.getPulsarToken());
            pw.printf("tlsAllowInsecureConnection=%b\n", false);
            pw.printf("tlsEnableHostnameVerification=%b\n", true);
            pw.printf("useKeyStoreTls=%b\n", false);
            pw.printf("tlsTrustStoreType=%s\n", "JKS");
            pw.printf("tlsTrustStorePath=\n");
            pw.printf("tlsTrustStorePassword=\n");
            //pw.flush();
            LoggerShell.info("Pulsar client.conf has been generated.");
        } catch (IOException e1) {
            throw new IllegalStateException("Cannot generate configuration file.");
        } finally {
            try {
                if (pw != null)        pw.close();
                if (fileWriter!= null) fileWriter .close();
            } catch (IOException e) {}
        }
    }
    
    /**
     * Provide path of the pulsar conf for a tenant.
     *
     * @param tenant
     *      current tenant.
     * @return
     *      configuration file
     */
    private File getPulsarConfFile(Tenant tenant) {
        return new File(pulsarConfigurationFolder + 
                File.separator + "client" 
                + "-" + tenant.getCloudProvider() 
                + "-" + tenant.getCloudRegion()
                + "-" + tenant.getTenantName()
                + ".conf");
    }
    
    /**
     * Start Pulsar shell as a Process.
     * 
     * @param options
     *      options from the comman dline
     * @param tenantName
     *      current tenant name
     * @throws TenantNotFoundException
     *      error if tenant is not found 
     * @throws CannotStartProcessException
     *      cannot start the process 
     * @throws FileSystemException
     *      cannot access configuration file
     */
    public void run(PulsarShellOptions options, String tenantName) 
    throws TenantNotFoundException, CannotStartProcessException, FileSystemException {
        
        // Retrieve tenant information from devops Apis or exception
        Tenant tenant = dao.getTenant(tenantName);
        
        // Download and install pulsar-shell tarball when needed
        if (!isInstalled()) {
            install();
        }
        
        // Generating configuration file if needed (~/.astra/lunastreaming-shell-2.10.1.1/conf/...)
        createPulsarConf(tenant);
        
        try {
            System.out.println("Pulsar-shell is starting please wait for connection establishment...");
            Process cqlShProc = startProcess(options, tenant, getPulsarConfFile(tenant));
            cqlShProc.waitFor();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            LoggerShell.error("Cannot start Pulsar Shel :" + e.getMessage());
            throw new CannotStartProcessException("pulsar-shell", e);
        }
    }
    
    /**
     * Check if lunastreaming-shell has been installed.
     *
     * @return
     *      if the folder exist
     */
    public boolean isInstalled() {
       return pulsarLocalFolder.exists() && pulsarLocalFolder.isDirectory();
    }
    
    /**
     * Download targz and unzip.
     *
     * @param settings
     *      settings coming from properties
     * @throws FileSystemException
     *      file system exception 
     */
    public void install() 
    throws FileSystemException {
        if (!isInstalled()) {
            LoggerShell.success("pulsar-shell first launch, downloading (~ 60MB), please wait...");
            String destination = AstraCliUtils.ASTRA_HOME + File.separator + "lunastreaming-shell-%s-bin.tar.gz".formatted(settings.version());
            FileUtils.downloadFile(settings.url(), destination);
            File pulsarShelltarball = new File (destination);
            if (pulsarShelltarball.exists()) {
                LoggerShell.info("File Downloaded. Extracting archive, please wait it can take a minute...");
                try {
                    FileUtils.extactTargzInAstraCliHome(pulsarShelltarball);
                    if (isInstalled()) {
                        // Change file permission
                        File pulsarShellFile = new File(AstraCliUtils.ASTRA_HOME + File.separator  
                                + "lunastreaming-shell-" + settings.version() + File.separator 
                                + "bin" + File.separator  
                                + "pulsar-shell");
                        if (!pulsarShellFile.setExecutable(true, false)) {
                            throw new FileSystemException("Cannot set pulsar-shell file as executable");
                        }
                        if (!pulsarShellFile.setReadable(true, false)) {
                            throw new FileSystemException("Cannot set pulsar-shell file as readable");
                        }
                        if (!pulsarShellFile.setWritable(true, false)) {
                            throw new FileSystemException("Cannot set pulsar-shell file as writable");
                        }
                        LoggerShell.success("pulsar-shell has been installed");
                        if (!pulsarShelltarball.delete()) {
                            LoggerShell.warning("Pulsar-shell Tar archived was not deleted");
                        }
                    }
                } catch (IOException e) {
                    LoggerShell.error("Cannot extract tar archive:" + e.getMessage());
                    throw new FileSystemException("Cannot extract tar archive:" + e.getMessage(), e);
                }
            }
        } else {
            LoggerShell.info("pulsar-shell is already installed");
        }
    }

    /**
     * Start Pulsar Shell as a sub process in the CLI.
     * 
     * @param options
     *      command to start pulsar-shell
     * @param tenant
     *      current tenant
     * @param configFile
     *      configuration file associated with the current tenant
     * @return
     *      unix process for pulsar-shell
     * @throws IOException
     *      errors occured
     * @throws ConfigurationException
     *      starting pulsar shell 
     */
    public Process startProcess(PulsarShellOptions options, Tenant tenant, File configFile) 
    throws IOException, ConfigurationException {
        
        if (!configFile.exists()) {
            LoggerShell.error("Client.conf '" + configFile.getAbsolutePath() + "' has not been found.");
            throw new ConfigurationException(configFile.getAbsolutePath());
        }
        
        List<String> pulsarShCommand = new ArrayList<>();
        pulsarShCommand.add(new StringBuilder()
                .append(pulsarLocalFolder)
                .append(File.separator + "bin")
                .append(File.separator + "pulsar-shell")
                .toString());
        
        // Enforcing usage of generated file
        pulsarShCommand.add("--config");
        pulsarShCommand.add(configFile.getAbsolutePath());
        
        if (options.noProgress()) {
            pulsarShCommand.add("--no-progress");
        }
        if (options.failOnError()) {
            pulsarShCommand.add("--fail-on-error");
        }
        if (options.execute() != null) {
            pulsarShCommand.add("--execute-command");
            pulsarShCommand.add(options.execute());
        }
        if (options.fileName() != null) {
            pulsarShCommand.add("--filename");
            pulsarShCommand.add(options.fileName());
        }
        
        LoggerShell.info("RUNNING: " + String.join(" ", pulsarShCommand));
        ProcessBuilder pb = new ProcessBuilder(pulsarShCommand.toArray(new String[0]));
        pb.inheritIO();
        return pb.start();
    }
    
}
