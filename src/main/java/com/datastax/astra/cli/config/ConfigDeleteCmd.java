package com.datastax.astra.cli.config;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.out.AstraCliConsole;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Delete a block in the command.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "delete", description = "Delete a configuration")
public class ConfigDeleteCmd extends AbstractCmd {
    
    /**
     * Section in configuration file to as as default.
     */
    @Parameters(arity = "1",
                paramLabel = "sectionName",
                description = "Section in configuration file to as as default.")
    protected String sectionName;
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        OperationsConfig.assertSectionExist(sectionName);
        ctx().getConfiguration().deleteSection(sectionName);
        ctx().getConfiguration().save();
        AstraCliConsole.outputSuccess("Section '" + sectionName + "' has been deleted.");
    }
}
