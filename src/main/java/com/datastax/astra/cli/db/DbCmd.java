package com.datastax.astra.cli.db;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.db.cqlsh.DbCqlShellCmd;
import com.datastax.astra.cli.db.dsbulk.DbCountCmd;
import com.datastax.astra.cli.db.dsbulk.DbLoadCmd;
import com.datastax.astra.cli.db.dsbulk.DbUnLoadCmd;
import com.datastax.astra.cli.db.keyspace.DbCreateKeyspaceCmd;
import com.datastax.astra.cli.db.keyspace.DbListKeyspacesCmd;
import com.datastax.astra.cli.utils.AstraCliUtils;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "db", 
         description = "Manage databases", 
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true,
         subcommands = { 
                         // CRUD
                         DbStatusCmd.class, DbCreateCmd.class, 
                         DbDeleteCmd.class, DbDotEnvCmd.class,
                         DbListCmd.class,   DbResumeCmd.class,
                         DbDownloadScbCmd.class, DbGetCmd.class,
                         // Keyspaces commands
                         DbCreateKeyspaceCmd.class, DbListKeyspacesCmd.class,
                         // Dsbulk related commands
                         DbLoadCmd.class, DbUnLoadCmd.class, DbCountCmd.class,
                         DbCqlShellCmd.class 
                         })
public class DbCmd extends AbstractCmd {
   
    @Spec 
    CommandSpec spec;
    
    /** {@inheritDoc} */
    public void execute() {
        //CommandLine.usage(DbCmd.class, System.out, AstraCliUtils.COLOR_SCHEME);
        spec.commandLine().usage(System.out, AstraCliUtils.COLOR_SCHEME);
    }

}
