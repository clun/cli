package com.datastax.astra.cli.db.dsbulk;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * Configuration Bean.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@ConfigurationProperties("dsbulk")
public record DsBulkConfig(String url, String version) {}
