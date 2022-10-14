package com.datastax.astra.cli.org;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.AbstractConnectedCmd;

import picocli.CommandLine.Command;

/**
 * Display information relative to a db.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(
        name = AbstractCmd.ORG, 
        description = "Manage organization",
        subcommands = {
                OrgIdCmd.class, OrgNameCmd.class, 
                OrgListRegionsClassicCmd.class, 
                OrgListRegionsServerlessCmd.class })
public class OrgCmd extends AbstractConnectedCmd {

    /** {@inheritDoc} */
    public void execute() {
        OperationsOrganization.showOrg();
    }

}
