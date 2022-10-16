package com.datastax.astra.cli.streaming.pulsarshell;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * Configuration for pulsar shell
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@ConfigurationProperties("pulsar-shell") 
public record PulsarShellSettings(String url, String version) {}
