package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.iam.exception.RoleNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Display role.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = AbstractCmd.GET, description = "Show role details")
public class RoleGetCmd extends AbstractConnectedCmd {
    
    /**
     * User email
     */
    @Parameters(
            arity = "1",
            paramLabel = "ROLE",
            description = "Role name or identifier")
    public String role;
    
    /** {@inheritDoc} */
    public void execute() throws RoleNotFoundException {
        OperationIam.showRole(role);
    }
    
}
