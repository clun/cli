package com.datastax.astra.cli.test;

import org.junit.jupiter.api.Test;

import picocli.CommandLine.Help.Ansi;

/**
 * Test Main behaviour.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class MainCmdTest extends AbstractCmdTest {

    @Test
    public void should_show_banner() {
        String str = Ansi.AUTO.string("@|bold,green,underline Hello, colored world!|@");
        System.out.println(str);
        assertSuccessCli("help config");
    }
    
    @Test
    public void should_show_version() {
        assertSuccessCli("--version");
    }
}
