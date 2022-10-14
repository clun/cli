package com.datastax.astra.cli.iam;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Display roles.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.LIST, 
        description = "Display the list of Roles in an organization")
public class RoleListCmd extends AbstractConnectedCmd {
    
    /** {@inheritDoc} */
    public void execute() {
        OperationIam.listRoles();
    }
    
}
