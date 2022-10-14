package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.iam.exception.UserNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Delete a user if exist
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.DELETE, 
        description = "Remove user from org")
public class UserDeleteCmd extends AbstractConnectedCmd {
    
    /**
     * User email
     */
    @Parameters(
            arity = "1",
            paramLabel = "EMAIL",
            description = "User email (identifier)")
    public String email;
    
    /** {@inheritDoc} */
    public void execute() throws UserNotFoundException {
        OperationIam.deleteUser(this, email);
    }
    
}
