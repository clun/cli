package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.utils.AstraCliUtils;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Working with Astra.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "user", 
         description = "Manage users", 
         mixinStandardHelpOptions = true,
         subcommands = { UserGetCmd.class, UserInviteCmd.class, 
                         UserDeleteCmd.class, UserListCmd.class })
public class UserCmd extends AbstractCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        CommandLine.usage(UserCmd.class, System.out, AstraCliUtils.COLOR_SCHEME);
    }

}
