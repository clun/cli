package com.datastax.astra.cli.core;

import java.util.stream.Stream;

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
        return Stream.of(ExitCode.values())
                .filter(current -> current.getExceptions().contains(ex.getClass()))
                .findFirst()
                .orElse(ExitCode.INTERNAL_ERROR)
                .getCode();
    }

}
