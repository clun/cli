package com.datastax.astra.cli.test.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.datastax.astra.cli.db.cqlsh.CqlShellService;
import com.datastax.astra.cli.test.AbstractCmdTest;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

/**
 * Working with Cqlsh.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@MicronautTest
public class DbCqlshTest extends AbstractCmdTest {
    
    /** db name. */
    public final static String DB_TEST = "astra_cli_test";
    
    /** Use to disable usage of CqlSh, DsBulkd and other during test for CI/CD. */
    public final static String FLAG_TOOLS = "disable_tools";
    
    /** flag coding for tool disabling. */
    public static boolean disableTools = false;
    
    /** Inject Service. */
    @Inject
    public CqlShellService cqlshService;
    
    @BeforeAll
    public static void should_create_when_needed() {
        readEnvVariable(FLAG_TOOLS)
            .ifPresent(flag -> disableTools = Boolean.valueOf(flag));
    }
    
    @Test
    @Order(1)
    public void should_install_Cqlsh()  throws Exception {
        if (!disableTools) {
            cqlshService.install();
            Assertions.assertTrue(cqlshService.isInstalled());
        }
    }
    
    @Test
    @Order(2)
    public void should_start_shell() {
        if (!disableTools) {
            assertSuccessCli("db", "cqlsh", DB_TEST, 
                    "-e", "SELECT cql_version FROM system.local");
        }   
    }
    
}
