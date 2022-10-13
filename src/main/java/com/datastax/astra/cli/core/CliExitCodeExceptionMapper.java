package com.datastax.astra.cli.core;

import java.util.stream.Stream;

import picocli.CommandLine.IExitCodeExceptionMapper;

/**
 * As the name stated changing the returning code.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class CliExitCodeExceptionMapper implements IExitCodeExceptionMapper {

    /** {@inheritDoc} */
    @Override
    public int getExitCode(final Throwable ex) {
        return Stream.of(ExitCode.values())
              .filter(current -> current.getExceptions().contains(ex.getClass()))
              .findFirst()
              .orElse(ExitCode.INTERNAL_ERROR)
              .getCode();
    }

}
