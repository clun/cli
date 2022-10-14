package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.sdk.organizations.domain.DefaultRoles;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Invite user.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = "invite", 
        description = "Invite user to org")
public class UserInviteCmd extends AbstractConnectedCmd {

    /**
     * User email
     */
    @Parameters(
            arity = "1",
            paramLabel = "EMAIL",
            description = "User email (identifier)")
    public String email;
    
    /**
     * Cloud provider region to provision
     */
    @Option(names = { "-r", "--role"}, 
            paramLabel = "ROLE", 
            description = "Role for the user (default is Database Administrator)")
    protected String role = DefaultRoles.DATABASE_ADMINISTRATOR.getName();
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        OperationIam.inviteUser(email, role);
    }

}
