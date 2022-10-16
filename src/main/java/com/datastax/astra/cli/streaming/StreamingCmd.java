package com.datastax.astra.cli.streaming;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.streaming.pulsarshell.PulsarShellCmd;
import com.datastax.astra.cli.utils.AstraCliUtils;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

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
                         StreamingGetCmd.class,    StreamingPulsarTokenCmd.class, 
                         StreamingStatusCmd.class, PulsarShellCmd.class})
public class StreamingCmd extends AbstractCmd {
   
    @Spec 
    CommandSpec spec;
    
    /** {@inheritDoc} */
    public void execute() {
        //CommandLine.usage(StreamingCmd.class, System.out, AstraCliUtils.COLOR_SCHEME);
        spec.commandLine().usage(System.out, AstraCliUtils.COLOR_SCHEME);
    }

}
