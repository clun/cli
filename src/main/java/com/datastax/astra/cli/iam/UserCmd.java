package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

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
public class UserCmd extends AbstractConnectedCmd {
   
    /** {@inheritDoc} */
    public void execute() {
        new UserListCmd().execute();
    }

}
