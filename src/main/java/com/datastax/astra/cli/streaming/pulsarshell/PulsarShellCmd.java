package com.datastax.astra.cli.streaming.pulsarshell;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.CannotStartProcessException;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * This command allows to load data with pulsar-client.
 * 
 * 
 * astra pulsar-shell
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "pulsar-shell", description = "Start pulsar admin against your tenant")
public class PulsarShellCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
   @Parameters(
           arity = "1",
           paramLabel = "TENANT",
           description = "Tenant name (unique for the region)")
   public String tenant;
    
    /** Options. */
    @Option(names = {"-e", "--execute-command" }, 
            paramLabel = "command",
            description = "Execute the statement and quit.")
    protected String execute;
   
    /** Cqlsh Options. */
    @Option(names= {"--fail-on-error"}, 
            description= "If true, the shell will be interrupted if a command throws an exception.")
    protected boolean failOnError = false;
    
    /** Cqlsh Options. */
    @Option(names = {"-f", "--filename" }, 
            paramLabel = "FILE",
            description = "Input filename with a list of commands to be executed. Each command must be separated by a newline.")
    protected String fileName;
    
    /** Cqlsh Options. */
    @Option(names=  {"-np", "--no-progress" }, 
            description= "Display raw output of the commands without the fancy progress visualization. ")
    protected boolean noProgress = false;
    
    @Inject
    protected PulsarShellService pulsarShellService;
    
    /** {@inheritDoc} */
    public void execute() 
    throws TenantNotFoundException, CannotStartProcessException, FileSystemException {
        pulsarShellService.run(
                new PulsarShellOptions(execute, failOnError, 
                        fileName, noProgress ), tenant);
    }
    
}
