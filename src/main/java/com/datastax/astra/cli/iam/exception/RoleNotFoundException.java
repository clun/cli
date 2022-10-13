package com.datastax.astra.cli.iam.exception;

/**
 * Role not found
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class RoleNotFoundException extends RuntimeException {

    /** Serial Number. */
    private static final long serialVersionUID = -1269813351970244235L;
   
    /**
     * Constructor with roleName
     * 
     * @param roleName
     *      role name
     */
    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' has not been found.");
    }

}
