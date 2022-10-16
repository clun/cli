package com.datastax.astra.cli.streaming;

import java.util.HashMap;
import java.util.Map;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.ExitCode;
import com.datastax.astra.cli.core.out.AstraCliConsole;
import com.datastax.astra.cli.core.out.JsonOutput;
import com.datastax.astra.cli.core.out.ShellTable;
import com.datastax.astra.cli.streaming.StreamingGetCmd.StreamingGetKeys;
import com.datastax.astra.cli.streaming.exception.TenantAlreadyExistExcepion;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;
import com.datastax.astra.sdk.streaming.domain.CreateTenant;
import com.datastax.astra.sdk.streaming.domain.Tenant;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Streaming Tenant Services
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Singleton
public class AstraStreamingService implements AstraStreamingConstants {
    
    @Inject
    AstraStreamingDao dao;
    
    /**
     * List Tenants.
     */
    public void listTenants() {
        ShellTable sht = new ShellTable();
        sht.addColumn(COLUMN_NAME,    20);
        sht.addColumn(COLUMN_CLOUD,   10);
        sht.addColumn(COLUMN_REGION,  15);
        sht.addColumn(COLUMN_STATUS,  15);
        dao.streamingClient()
           .tenants()
           .forEach(tnt -> {
                Map <String, String> rf = new HashMap<>();
                rf.put(COLUMN_NAME,   tnt.getTenantName());
                rf.put(COLUMN_CLOUD,  tnt.getCloudProvider());
                rf.put(COLUMN_REGION, tnt.getCloudRegion());
                rf.put(COLUMN_STATUS, tnt.getStatus());
                sht.getCellValues().add(rf);
        });
        AstraCliConsole.printShellTable(sht);
    }
    
    /**
     * Show tenant details.
     *
     * @param tenantName
     *      tenant name
     * @param key
     *      display only one key
     * @throws TenantNotFoundException 
     *      error is tenant is not found
     */
    public void showTenant(String tenantName, StreamingGetKeys key)
    throws TenantNotFoundException {
        Tenant tnt = dao.getTenant(tenantName);
        if (key == null) {
            ShellTable sht = ShellTable.propertyTable(15, 40);
            sht.addPropertyRow("Name", tnt.getTenantName());
            sht.addPropertyRow("Status", tnt.getStatus());
            sht.addPropertyRow("Cloud Provider", tnt.getCloudProvider());
            sht.addPropertyRow("Cloud region", tnt.getCloudRegion());
            sht.addPropertyRow("Cluster Name", tnt.getClusterName());
            sht.addPropertyRow("Pulsar Version", tnt.getPulsarVersion());
            sht.addPropertyRow("Jvm Version", tnt.getJvmVersion());
            sht.addPropertyRow("Plan", tnt.getPlan());
            sht.addPropertyRow("WebServiceUrl", tnt.getWebServiceUrl());
            sht.addPropertyRow("BrokerServiceUrl", tnt.getBrokerServiceUrl());
            sht.addPropertyRow("WebSocketUrl", tnt.getWebsocketUrl());
            switch(CliContext.getInstance().getOutputFormat()) {
                case json:
                    AstraCliConsole.printJson(new JsonOutput(ExitCode.SUCCESS, 
                                STREAMING + " " + AbstractConnectedCmd.GET + " " + tenantName, sht));
                break;
                case csv:
                case human:
                default:
                    AstraCliConsole.printShellTable(sht);
                break;
             }
        }  else {
            switch(key) {
                case cloud:
                    AstraCliConsole.println(tnt.getCloudProvider());
                break;
                case pulsar_token:
                    AstraCliConsole.println(tnt.getPulsarToken());
                break;
                case region:
                    AstraCliConsole.println(tnt.getCloudRegion());
                break;
                case status:
                    AstraCliConsole.println(tnt.getStatus());
                break;
             
            }
        }
    }
    
    /**
     * Delete a dabatase if exist.
     * 
     * @param tenantName
     *      tenant name
     * @throws TenantNotFoundException 
     *      error if tenant name is not unique
     */
    public void deleteTenant(String tenantName) 
    throws TenantNotFoundException {
        dao.getTenant(tenantName);
        dao.tenantClient(tenantName).delete();
        AstraCliConsole.outputSuccess("Deleting Tenant '" + tenantName + "'");
    }
    
    /**
     * Display status of a tenant.
     * 
     * @param tenantName
     *      tenant name
     * @throws TenantNotFoundException 
     *      error if tenant is not found
     */
    public void showTenantStatus(String tenantName)
    throws TenantNotFoundException {
        Tenant tnt = dao.getTenant(tenantName);
        AstraCliConsole.outputSuccess("Tenant '" + tenantName + "' has status '" + tnt.getStatus() + "'");
    }
    
    /**
     * Display existence of a tenant.
     * 
     * @param tenantName
     *      tenant name
     */
    public void showTenantExistence(String tenantName) {
        if (dao.tenantClient(tenantName).exist()) {
            AstraCliConsole.outputSuccess("Tenant '" + tenantName + "' exists.");
        } else {
            AstraCliConsole.outputSuccess("Tenant '" + tenantName + "' does not exist.");
        }
    }
    
    /**
     * Display token of a tenant.
     * 
     * @param tenantName
     *      database name
     * @throws TenantNotFoundException 
     *      error if tenant is not found
     */
    public void showTenantPulsarToken(String tenantName)
    throws TenantNotFoundException {
        System.out.println(dao.getTenant(tenantName).getPulsarToken());
    }
    
    /**
     * Create a streaming tenant.
     *
     * @param ct
     *      tenant creation request
     * @throws TenantAlreadyExistExcepion
     *      already exist exception 
     */
    public void createStreamingTenant(CreateTenant ct) 
    throws TenantAlreadyExistExcepion {
        if (dao.tenantClient(ct.getTenantName()).exist()) {
            throw new TenantAlreadyExistExcepion(ct.getTenantName());
        }
        dao.streamingClient().createTenant(ct);
        AstraCliConsole.outputSuccess("Tenant '" + ct.getTenantName() + "' has being created.");
    }    

}
