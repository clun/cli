package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a tenant.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "exist", 
         description = "Show existence of a tenant",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
public class StreamingExistCmd extends AbstractConnectedCmd {

    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    String tenant;
    
    @Inject
    AstraStreamingService streamService;
    
    /** {@inheritDoc} */
    public void execute() {
        streamService.showTenantExistence(tenant);
    }

}
