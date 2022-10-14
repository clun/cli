package com.datastax.astra.cli.db.dsbulk;

import java.util.List;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.CannotStartProcessException;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.OperationsDb;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * This command allows to load data with DsBulk.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "dsbulk", description = "Load data leveraging DSBulk")
public class DbDSBulkCmd extends AbstractConnectedCmd {

    /**
     * List of parameters to provide for dsbulk.
     */
    @Parameters(description = "Use DSBulk arguments")
    private List<String> dsbulkArguments;
    
    /** {@inheritDoc} */
    @Override
    public void execute() 
    throws InvalidArgumentException, DatabaseNameNotUniqueException, 
           DatabaseNotFoundException, CannotStartProcessException, FileSystemException {
        if (dsbulkArguments.size() < 3) {
            throw new InvalidArgumentException("Please use format astra db dsbulk <DB_NAME> [dsbulk options]");
        }
        OperationsDb.runDsBulk(dsbulkArguments);
    }
    
}
