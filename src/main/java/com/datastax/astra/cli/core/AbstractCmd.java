package com.datastax.astra.cli.core;

import com.datastax.astra.cli.config.AstraConfiguration;
import com.datastax.astra.cli.core.out.OutputFormat;

import picocli.CommandLine.Option;

/**
 * Options, parameters and treatments that you want to apply on all commands.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractCmd implements Runnable, CliConstants {
    
    /** 
     * Each command can have a verbose mode. 
     **/
    @Option(names = { "-v","--verbose" }, description = "Verbose mode with log in console")
    protected boolean verbose = false;
    
    /** 
     * Each command can have a verbose mode. 
     **/
    @Option(names = { "--no-color" }, description = "Remove all colors in output")
    protected boolean noColor = false;
    
    /**
     * No log but provide output as a JSON
     */
    @Option(names = { "-o", "--output" }, 
            paramLabel = "FORMAT",
            description = "Output format, valid values are: human,json,csv")
    protected OutputFormat output = OutputFormat.human;
    
    /**
     * File on disk to reuse configuration.
     */
    @Option(names = { "-cf, --config-file" }, 
            paramLabel = "CONFIF_FILE",
            description= "Configuration file (default = ~/.astrarc)")
    protected String configFilename = AstraConfiguration.getDefaultConfigurationFileName();
    
    /** {@inheritDoc} */
    public void run() {
        ctx().init(new CoreOptions(verbose, noColor, output, configFilename));
        execute();
    }
    
    /**
     * Function to be implemented by terminal class.
     */
    protected abstract void execute();
    
    /**
     * Get current context.
     * 
     * @return
     *      current context
     */
    protected CliContext ctx() {
        return CliContext.getInstance();
    }
    
}
