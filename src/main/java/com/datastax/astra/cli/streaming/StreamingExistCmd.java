package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a tenant.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = OperationsStreaming.CMD_EXIST, description = "Show existence of a tenant")
public class StreamingExistCmd extends AbstractConnectedCmd {

    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    public String tenant;
    
    /** {@inheritDoc} */
    public void execute() {
        OperationsStreaming.showTenantExistence(tenant);
    }

}
