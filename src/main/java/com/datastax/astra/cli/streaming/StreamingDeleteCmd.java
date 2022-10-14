package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Delete a tenant if exists
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = AbstractCmd.DELETE, description = "Delete an existing tenant")
public class StreamingDeleteCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    public String tenant;
    
    /** {@inheritDoc} */
    public void execute()
    throws TenantNotFoundException {
        OperationsStreaming.deleteTenant(tenant);
    }
    
}
