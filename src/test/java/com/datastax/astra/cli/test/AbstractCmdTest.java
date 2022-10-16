package com.datastax.astra.cli.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.datastax.astra.cli.AstraCli;
import com.datastax.astra.cli.config.AstraConfiguration;
import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.ExitCode;
import com.datastax.astra.sdk.config.AstraClientConfig;
import com.datastax.stargate.sdk.utils.Utils;

/**
 * Parent class for tests
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractCmdTest {
    
    @BeforeAll
    public static void init() {
        //runCli("--version");
    }
    
    /**
     * Syntaxic sugar to read environment variables.
     *
     * @param key
     *      environment variable
     * @return
     *      if the value is there
     */
    public static Optional<String> readEnvVariable(String key) {
        if (Utils.hasLength(System.getProperty(key))) {
            return Optional.ofNullable(System.getProperty(key));
        } else if (Utils.hasLength(System.getenv(key))) {
            return Optional.ofNullable(System.getenv(key));
        }
        return Optional.empty();
    }
    
    /**
     * Astra Client to get a token
     */
    protected String getToken() {
        Optional<String> t2 = readEnvVariable(AstraClientConfig.ASTRA_DB_APPLICATION_TOKEN);
        /** Env var is always first. */
        if (t2.isPresent()) { 
            return t2.get();
        }
        Optional<String> t1 = new AstraConfiguration().getSectionKey(
                AstraConfiguration.ASTRARC_DEFAULT, 
                AstraClientConfig.ASTRA_DB_APPLICATION_TOKEN);
        if (t1.isPresent()) { 
            return t1.get();
        }
        throw new IllegalStateException("Cannot find token");
    }
    
    /**
     * Help tests.
     * 
     * @return
     *      utils
     */
    protected CliContext ctx() {
        return CliContext.getInstance();
    }
   
    protected AstraConfiguration astraRc() {
        return ctx().getConfiguration();
    }
    
    protected static void assertSuccessCli(String cmd) {
        assertExitCodeCli(ExitCode.SUCCESS, cmd);
    }
    
    protected void assertSuccessCli(String... cmd) {
        assertExitCodeCli(ExitCode.SUCCESS, cmd);
    }
    
    protected static void assertExitCodeCli(ExitCode code, String cmd) {
        Assertions.assertEquals(code.getCode(), runCli(cmd));
    }
    
    protected static void assertExitCodeCli(ExitCode code, String[] cmd) {
        Assertions.assertEquals(code.getCode(), runCli(cmd));
    }
    
    protected static int runCli(String cmd) {
        return runCli(cmd.split(" "));
    }
    
    protected static int runCli(String[] cmd) {
        return AstraCli.runCli(cmd);
    }

}
