package com.datastax.astra.cli.core;

import java.util.Set;

import com.datastax.astra.cli.core.exception.ConfigurationException;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.core.exception.InvalidArgumentException;
import com.datastax.astra.cli.core.exception.InvalidTokenException;
import com.datastax.astra.cli.core.exception.TokenNotFoundException;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;
import com.datastax.astra.cli.db.exception.DatabaseNotSelectedException;
import com.datastax.astra.cli.iam.exception.RoleNotFoundException;
import com.datastax.astra.cli.iam.exception.UserAlreadyExistException;
import com.datastax.astra.cli.iam.exception.UserNotFoundException;
import com.datastax.astra.cli.streaming.exception.TenantAlreadyExistExcepion;
import com.datastax.astra.cli.streaming.exception.TenantNotFoundException;

import picocli.CommandLine.UnmatchedArgumentException;

/**
 * Normalization of exit codes.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@SuppressWarnings("unchecked")
public enum ExitCode {
    
    /** code ok. */
    SUCCESS(0),
    
    /** code. */
    PARSE_ERROR(1),
    
    /** code. */
    INVALID_ARGUMENT(2, 
      UnmatchedArgumentException.class),
    
    /** code. */
    UNAVAILABLE(11),
    
    /** code. */
    NOT_IMPLEMENTED(3),
    
    /** code. */
    
    INVALID_PARAMETER(4, 
       DatabaseNameNotUniqueException.class,
       InvalidArgumentException.class),
    
    /** code. */
    NOT_FOUND(5, 
       DatabaseNotFoundException.class,
       TenantNotFoundException.class, 
       RoleNotFoundException.class,
       UserNotFoundException.class),
    
    /** conflict. */
    CONFLICT(6),
    
    /** conflict. */
    ALREADY_EXIST(7, 
       TenantAlreadyExistExcepion.class,
       UserAlreadyExistException.class),
    
    /** code. */
    CANNOT_CONNECT(8), 
    
    /** code. */
    CONFIGURATION(9, 
       TokenNotFoundException.class,
       FileSystemException.class, 
       ConfigurationException.class,
       InvalidTokenException.class),
    
    /** code. */
    ILLEGAL_STATE(10,
      DatabaseNotSelectedException.class), 
    
    /** code. */
    INVALID_OPTION(12),
    
    /** code. */
    UNRECOGNIZED_COMMAND(14),
    
    /** Internal error. */
    INTERNAL_ERROR(100);
    
    /* Exit code. */
    private int code;
    
    /** Related Expression. */
    private Set<Class<? extends Throwable>> exceptions;
    
    /**
     * Constructor.
     *
     * @param code
     *      target code
     * @param ex
     *      exceptions
     */
    private ExitCode(int code, Class<? extends Throwable>... ex) {
        this.code       = code;
        this.exceptions = Set.of(ex);
    }

    /**
     * Getter accessor for attribute 'code'.
     *
     * @return
     *       current value of 'code'
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter accessor for attribute 'exceptions'.
     *
     * @return
     *       current value of 'exceptions'
     */
    public Set<Class<? extends Throwable>> getExceptions() {
        return exceptions;
    }

}
