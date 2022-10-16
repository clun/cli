package com.datastax.astra.cli.db.dsbulk;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Load data into AstraDB.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "load", 
        description = "Load data leveraging DSBulk",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbLoadCmd extends AbstractDsbulkDataCmd {
    
    /**
     * Optional filter
     */
    @Option(names = { "-dryRun" },
            paramLabel = "dryRun", 
            description = "Enable or disable dry-run mode, a test mode that runs the "
                    + "command but does not load data. ")
    protected boolean dryRun = false;
   
    
    /** {@inheritDoc} */
    @Override
    public void execute()  {
    }
    
    

}
