package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "streaming", 
         description = "Manage streaming tenants", 
         mixinStandardHelpOptions = true,
         subcommands = { StreamingCreateCmd.class, StreamingDeleteCmd.class,
                         StreamingExistCmd.class,  StreamingListCmd.class, 
                         StreamingPulsarTokenCmd.class, StreamingStatusCmd.class})
public class StreamingCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        new StreamingListCmd().execute();
    }

}
