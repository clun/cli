package com.datastax.astra.cli.core;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

/**
 * Error Loggin manegement
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class CliExecutionExceptionHandler implements IExecutionExceptionHandler {

    /** {@inheritDoc} */
    @Override
    public int handleExecutionException(Exception ex, 
        CommandLine cmd, ParseResult parseResult)
    throws Exception {
        if (ex.getMessage() != null) {
            cmd.getErr().println(cmd.getColorScheme().errorText(ex.getMessage()));
        }
        return cmd.getExitCodeExceptionMapper() != null
                    ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                    : cmd.getCommandSpec().exitCodeOnExecutionException();
    }

}
