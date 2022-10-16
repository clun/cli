package com.datastax.astra.cli.streaming;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;
import com.datastax.astra.sdk.streaming.StreamingClient;
import com.datastax.astra.sdk.streaming.TenantClient;
import com.datastax.astra.sdk.streaming.domain.Tenant;

import jakarta.inject.Singleton;

/**
 * Accessing streaming client from API.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Singleton
public class AstraStreamingDao {

    /** limit resource usage by caching tenant clients. */
    private static Map<String, TenantClient> cacheTenantClient = new HashMap<>();
    
    /**
     * Syntax Sugar to work with Streaming Devops Apis.
     * 
     * @return
     *      streaming tenant.
     */
    public StreamingClient streamingClient() {
        return  CliContext.getInstance().getApiDevopsStreaming();
    }
    
    /**
     * Syntax sugar and caching for tenant clients.
     * 
     * @param tenantName
     *      current tenant name.
     * @return
     *      tenant client or error
     */
    public TenantClient tenantClient(String tenantName) {
        if (!cacheTenantClient.containsKey(tenantName)) {
            cacheTenantClient.put(tenantName, streamingClient().tenant(tenantName));
        }
        return cacheTenantClient.get(tenantName);
    }
    
    /**
     * Get tenant informations.
     *
     * @param tenantName
     *      tenant name
     * @return
     *      tenant when exist
     * @throws TenantNotFoundException
     *      tenant has not been foind 
     */
    public Tenant getTenant(String tenantName) 
    throws TenantNotFoundException {
        Optional<Tenant> optTenant = tenantClient(tenantName).find();
        if (!optTenant.isPresent()) {
            throw new TenantNotFoundException(tenantName);
        }
        return optTenant.get();
    }
    
}
