package com.datastax.astra.cli.db.cqlsh;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * Load config from micronaut
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@ConfigurationProperties("cqlsh")
public record CqlshSettings(String url, String tarball) {}

