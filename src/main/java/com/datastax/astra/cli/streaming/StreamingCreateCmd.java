package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.streaming.exception.TenantAlreadyExistExcepion;
import com.datastax.astra.sdk.streaming.domain.CreateTenant;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
/**
 * Will create a tenant when needed.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.CREATE, 
        description = "Create a tenant in streaming with cli",
        synopsisHeading = "%nUsage: ",
        mixinStandardHelpOptions = true)
public class StreamingCreateCmd extends AbstractConnectedCmd implements AstraStreamingConstants {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    String tenant;
    
    // Create Tenant Options
    @Option(names = { "-c", "--cloud" }, description = "Cloud Provider to create a tenant")
    String cloudProvider = DEFAULT_CLOUD_PROVIDER;
    
    /** option. */
    @Option(names = { "-r", "--region" }, description = "Cloud Region for the tenant")
    String cloudRegion = DEFAULT_CLOUD_REGION;
    
    /** option. */
    @Option(names = { "-p", "--plan" }, description = "Plan for the tenant")    
    String plan = DEFAULT_CLOUD_TENANT;
    
    /** option. */
    @Option(names = { "-e", "--email" }, description = "User Email")    
    String email = DEFAULT_EMAIL;
    
    @Inject
    AstraStreamingService streamService;
    
    /** {@inheritDoc} */
    @Override
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, InvalidArgumentException, TenantAlreadyExistExcepion {
        CreateTenant ct = new CreateTenant();
        ct.setCloudProvider(cloudProvider);
        ct.setCloudRegion(cloudRegion);
        ct.setPlan(plan);
        ct.setUserEmail(email);
        ct.setTenantName(tenant);
        streamService.createStreamingTenant(ct);
    }
    

}
