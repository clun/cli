package com.datastax.astra.cli.test.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.astra.cli.db.dsbulk.DsBulkService;
import com.datastax.astra.cli.test.AbstractCmdTest;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

/**
 * Datstax Bulk Loader.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@MicronautTest
public class DsBulkTest extends AbstractCmdTest {
    
    /** Logger for my test. */
    private static Logger LOGGER = LoggerFactory.getLogger(DsBulkTest.class);
   
    public final static String DB_TEST = "astra_cli_test";
    
    /** Use to disable usage of CqlSh, DsBulkd and other during test for CI/CD. */
    public final static String FLAG_TOOLS = "disable_tools";
    
    /** flag coding for tool disabling. */
    public static boolean disableTools = false;
    
    @BeforeAll
    public static void should_create_when_needed() {
        readEnvVariable(FLAG_TOOLS).ifPresent(flag -> disableTools = Boolean.valueOf(flag));
        if (!disableTools) {
            LOGGER.info("Third party tools are enabled in test");
        }
    }
    
    @Client("https://api.github.com")
    @Inject 
    HttpClient client;
    
    public void test() {
        //Map m = client.retrieve(HttpRequest.GET("/repos/").header(
        //        "User-Agent", "remkop-picocli"), Map.class);
    }
    
    @Inject
    DsBulkService dsbulkService;
    
    @Test
    @Order(1)
    public void should_install_dsbulk()  throws Exception {
        if (disableTools) {
            LOGGER.warn("Third Party tool is disabled for this test environment");
        } else {
            dsbulkService.install();
            Assertions.assertTrue(dsbulkService.isInstalled());
        }
    }
    
    @Test
    @Order(2)
    public void should_start_shell() {
        if (disableTools) {
            LOGGER.warn("Third Party tool is disabled for this test environment");
        } else {
            assertSuccessCli("db", "dsbulk", DB_TEST, 
                    "count","-k","system","-t","local");
        }   
    }

}
