package com.datastax.astra.cli.org;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Show organization name
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = OperationsOrganization.CMD_NAME, 
        description = "Show organization name.")
public class OrgNameCmd extends AbstractConnectedCmd {

    /** {@inheritDoc} */
    public void execute() {
        OperationsOrganization.getName();
    }

}
