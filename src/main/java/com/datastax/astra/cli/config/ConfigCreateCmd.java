package com.datastax.astra.cli.config;

import com.datastax.astra.cli.core.AbstractCmd;
import com.datastax.astra.cli.core.ExitCode;
import com.datastax.astra.cli.core.exception.InvalidTokenException;
import com.datastax.astra.cli.core.exception.TokenNotFoundException;
import com.datastax.astra.cli.core.out.AstraCliConsole;
import com.datastax.astra.sdk.organizations.OrganizationsClient;
import com.datastax.astra.sdk.organizations.domain.Organization;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Create a new section in configuration.
 * 
 * "astra config create"
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "create", description = "Create a configuration")
public class ConfigCreateCmd extends AbstractCmd {
    
    /** Section in configuration file to as as default. */
    @Parameters(arity = "1",
            paramLabel = "section",
            description = "Section in configuration file to as as default.")
    protected String sectionName;
   
    /** Authentication token used if not provided in config. */
    @Option(names = { "-t", "--token" }, 
            paramLabel  = "token", 
            description = "Key to use authenticate each call.")
    protected String token;
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        if (token == null) {
            AstraCliConsole.outputError(ExitCode.INVALID_PARAMETER, "Please Provide a token with option -t, --token");
            throw new TokenNotFoundException();
        }
        if (!token.startsWith("AstraCS:")) {
            AstraCliConsole.outputError(ExitCode.INVALID_PARAMETER, "Your token should start with 'AstraCS:'");
            throw new InvalidTokenException();
        }
        OrganizationsClient apiOrg  = new OrganizationsClient(token);
        Organization o = apiOrg.organization();
        if (sectionName == null) {
            sectionName = o.getName();
        }
        ctx().getConfiguration().createSectionWithToken(sectionName, token);
        ctx().getConfiguration().save();
        AstraCliConsole.outputSuccess("Configuration Saved.");
    }
    
}
