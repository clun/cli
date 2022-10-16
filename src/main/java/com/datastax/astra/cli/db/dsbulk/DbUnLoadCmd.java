package com.datastax.astra.cli.db.dsbulk;

import picocli.CommandLine.Command;

/**
 * Load data into AstraDB.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "unload", 
        description = "Unload data leveraging DSBulk",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbUnLoadCmd extends AbstractDsbulkDataCmd {
    
    /** {@inheritDoc} */
    @Override
    public void execute()  {
    }

}
