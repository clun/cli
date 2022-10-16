package com.datastax.astra.cli.streaming;

/**
 * Extermalization of constants
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface AstraStreamingConstants {
    
    /** Command constants. */
    String STREAMING = "streaming";
    
    /** default cloud.*/
    String DEFAULT_CLOUD_PROVIDER = "aws";
    
    /** default region. */
    String DEFAULT_CLOUD_REGION = "useast2";
    
    /** default plan. */
    String DEFAULT_CLOUD_TENANT = "free";
    
    /** default email. */
    String DEFAULT_EMAIL = "astra-cli@datastax.com";
    
    /** columns. */
    String COLUMN_NAME   = "name";
    
    /** columns. */
    String COLUMN_CLOUD  = "cloud";
    
    /** columns. */
    String COLUMN_REGION = "region";
    
    /** columns. */
    String COLUMN_STATUS = "Status";
    
}
