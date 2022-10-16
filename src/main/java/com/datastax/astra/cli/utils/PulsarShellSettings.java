package com.datastax.astra.cli.utils;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("pulsar-shell") 
public record PulsarShellSettings(String url, String version) {}
