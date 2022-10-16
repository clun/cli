package com.datastax.astra.cli.test.streaming;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.datastax.astra.cli.streaming.pulsarshell.PulsarShellService;
import com.datastax.astra.cli.test.AbstractCmdTest;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

/**
 * Pulsar Shell.S
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@MicronautTest
public class PulsarShellTest extends AbstractCmdTest {
    
    /** Use to disable usage of CqlSh, DsBulkd and other during test for CI/CD. */
    static final String FLAG_TOOLS = "disable_tools";
    
    /** random. */
    static final String RANDOM_TENANT = "cli-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    
    /** flag coding for tool disabling. */
    public static boolean disableTools = false;
    
    @Inject
    public PulsarShellService pulsarService;
    
    @BeforeAll
    public static void should_create_when_needed() {
        readEnvVariable(FLAG_TOOLS).ifPresent(flag -> disableTools = Boolean.valueOf(flag));
        assertSuccessCli("streaming create " + RANDOM_TENANT);
    }
    
    @Test
    @Order(1)
    public void should_install_pulsarshell()  throws Exception {
        if (!disableTools) {
            pulsarService.install();
            Assertions.assertTrue(pulsarService.isInstalled());
        }
    }
    
    @Test
    @Order(2)
    public void should_pulsar_shell() {
        if (!disableTools) {
            assertSuccessCli(new String[] {
                "streaming", "pulsar-shell", RANDOM_TENANT, "-e", 
                "admin namespaces list " + RANDOM_TENANT});
        }
    }
    
    @AfterAll
    public static void should_close_tenant() {
        assertSuccessCli("streaming delete " + RANDOM_TENANT);
    }

}
