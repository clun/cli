package com.datastax.astra.cli.db.dsbulk;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;

/**
 * Load data into AstraDB.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "count", 
        description = "Count items for a table, a query",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class DbCountCmd extends AbstractDsbulkDataCmd {
    
    /**
     * Service layer for dsbulk. 
     */
    @Inject 
    DsBulkService dsbulkService;
    
    /** {@inheritDoc} */
    @Override
    public void execute()  {
        dsbulkService.count(this);
    }


}
