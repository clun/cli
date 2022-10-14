package com.datastax.astra.cli.streaming;

import static com.datastax.astra.cli.streaming.OperationsStreaming.DEFAULT_CLOUD_PROVIDER;
import static com.datastax.astra.cli.streaming.OperationsStreaming.DEFAULT_CLOUD_REGION;
import static com.datastax.astra.cli.streaming.OperationsStreaming.DEFAULT_CLOUD_TENANT;
import static com.datastax.astra.cli.streaming.OperationsStreaming.DEFAULT_EMAIL;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.streaming.exception.TenantAlreadyExistExcepion;
import com.datastax.astra.sdk.streaming.domain.CreateTenant;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
/**
 * Will create a tenant when needed.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = AbstractCmd.CREATE, description = "Create a tenant in streaming with cli")
public class StreamingCreateCmd extends AbstractConnectedCmd {
    
    /**
     * Database name or identifier
     */
    @Parameters(
            arity = "1",
            paramLabel = "TENANT",
            description = "Tenant name (unique for the region)")
    public String tenant;
    
    // Create Tenant Options
    @Option(names = { "-c", "--cloud" }, description = "Cloud Provider to create a tenant")
    private String cloudProvider = DEFAULT_CLOUD_PROVIDER;
    
    /** option. */
    @Option(names = { "-r", "--region" }, description = "Cloud Region for the tenant")
    private String cloudRegion = DEFAULT_CLOUD_REGION;
    
    /** option. */
    @Option(names = { "-p", "--plan" }, description = "Plan for the tenant")    
    private String plan = DEFAULT_CLOUD_TENANT;
    
    /** option. */
    @Option(names = { "-e", "--email" }, description = "User Email")    
    private String email = DEFAULT_EMAIL;
    
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
        OperationsStreaming.createStreamingTenant(ct);
    }
    

}
