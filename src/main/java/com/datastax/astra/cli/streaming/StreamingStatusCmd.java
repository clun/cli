package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a tenant.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = OperationsStreaming.CMD_STATUS, description = "Show status of a tenant")
public class StreamingStatusCmd extends AbstractConnectedCmd {

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
        OperationsStreaming.showTenantStatus(tenant);
    }

}
