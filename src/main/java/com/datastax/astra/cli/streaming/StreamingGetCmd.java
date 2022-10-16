package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Display information relative to a db.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.GET, 
        description = "Show details of a tenant",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class StreamingGetCmd extends AbstractConnectedCmd {

    /** Enum for db get. */
    public static enum StreamingGetKeys { 
        /** tenant status */
        status, 
        /** cloud provider*/
        cloud, 
        /** pulsar token */
        pulsar_token,
        /** cloud region */
        region};
    
    /**
      * Database name or identifier
      */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    public String tenant;
    
    /** Authentication token used if not provided in config. */
    @Option(names = { "-k", "--key" }, 
            paramLabel = "KEY", description = ""
            + "Valid keys: "
            + "'status', 'cloud', 'pulsar_token', 'region'")
    StreamingGetKeys key;
    
    @Inject
    AstraStreamingService streamService;
    
    /** {@inheritDoc} */
    public void execute()
    throws TenantNotFoundException {
        streamService.showTenant(tenant, key);
    }

}
