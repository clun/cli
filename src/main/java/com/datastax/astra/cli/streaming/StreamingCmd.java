package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.utils.AstraCliUtils;

import picocli.CommandLine;
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
public class StreamingCmd extends AbstractCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        CommandLine.usage(StreamingCmd.class, System.out, AstraCliUtils.COLOR_SCHEME);
    }

}
