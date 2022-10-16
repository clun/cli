package com.datastax.astra.cli.db.dsbulk;

import picocli.CommandLine.Option;

/**
 * Load/UnLoad data into AstraDB.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractDsbulkDataCmd extends AbstractDsbulkCmd {
    
    /**
     * Optional filter
     */
    @Option(names = { "-url" },
            paramLabel = "url", 
            description = "The URL or path of the resource(s) to read from or write to.")
    protected String url;
    
    /**
     * Optional filter
     */
    @Option(names = { "-delim" },
            paramLabel = "delim", 
            description = "The character(s) to use as field delimiter. Field delimiters "
                    + "containing more than one character are accepted.")
    protected String delim = ",";
    
    /**
     * Optional filter
     */
    @Option(names = { "-m", "--schema.mapping" },
            paramLabel = "mapping", 
            description = "The field-to-column mapping to use, that applies to both "
                    + "loading and unloading; ignored when counting.")
    protected String mapping;
    
    /**
     * Optional filter
     */
    @Option(names = { "-header" },
            paramLabel = "header", 
            description = "Enable or disable whether the files to read "
                    + "or write begin with a header line.")
    protected boolean header = true;
    
    /**
     * Optional filter
     */
    @Option(names = { "-skipRecords" },
            paramLabel = "skipRecords", 
            description = "The number of records to skip from each input "
                    + "file before the parser can begin to execute. Note "
                    + "that if the file contains a header line, that line "
                    + "is not counted as a valid record. This setting is "
                    + "ignored when writing.")
    protected int skipRecords = 0;
    
    /**
     * Optional filter
     */
    @Option(names = { "-maxErrors" },
            paramLabel = "maxErrors", 
            description = "The maximum number of errors to tolerate before aborting the entire operation.")
    protected int maxErrors = 100;
  
}
