package com.datastax.astra.cli.db.dsbulk;

import jakarta.inject.Inject;
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
    
    /**
     * Service layer for dsbulk. 
     */
    @Inject 
    DsBulkService dsbulkService;
    
    /** {@inheritDoc} */
    @Override
    public void execute()  {
        dsbulkService.unload(this);
    }

}
