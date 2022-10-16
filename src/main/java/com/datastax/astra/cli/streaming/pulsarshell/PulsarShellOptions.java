package com.datastax.astra.cli.streaming.pulsarshell;

/**
 * Options for Pulsar Shell CLI
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public record PulsarShellOptions(
        String execute, 
        boolean failOnError, 
        String fileName, 
        boolean noProgress) {}
   
