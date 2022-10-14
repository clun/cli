package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import picocli.CommandLine.Command;

/**
 * Show Databases for an organization 
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.LIST, 
        description = "Display the list of Tenant in an organization")
public class StreamingListCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute()
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, InvalidArgumentException {
        OperationsStreaming.listTenants();
    }

}
