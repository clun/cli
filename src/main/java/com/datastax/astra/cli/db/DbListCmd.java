package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;

/**
 * Show Databases for an organization 
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "list", 
         description = "List databases",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
public class DbListCmd extends AbstractConnectedCmd {
   
    /**
     * Working with databases.
     */
    @Inject 
    DatabaseService dbService;
    
    /** {@inheritDoc} */
    public void execute() {
        dbService.listDb();
    }

}
