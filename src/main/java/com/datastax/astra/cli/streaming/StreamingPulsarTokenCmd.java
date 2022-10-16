package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a tenant.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "pulsar-token", 
        description = "Show status of a tenant",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class StreamingPulsarTokenCmd extends AbstractConnectedCmd {

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
   public void execute()
   throws TenantNotFoundException {
       streamService.showTenantPulsarToken(tenant);
   }

}
