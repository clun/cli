package com.datastax.astra.cli.org;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Show organization id
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = OperationsOrganization.CMD_ID, 
        description = "Show organization id.")
public class OrgIdCmd extends AbstractConnectedCmd {

    /** {@inheritDoc} */
    public void execute() {
        OperationsOrganization.getId();
    }

}
