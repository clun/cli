package com.datastax.astra.cli.db.cqlsh;

import com.datastax.astra.cli.core.AbstractConnectedCmd;
import com.datastax.astra.cli.core.exception.CannotStartProcessException;
import com.datastax.astra.cli.core.exception.FileSystemException;
import com.datastax.astra.cli.db.OperationsDb;
import com.datastax.astra.cli.db.exception.DatabaseNameNotUniqueException;
import com.datastax.astra.cli.db.exception.DatabaseNotFoundException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Start CqlSh for a DB.
 * 
 * https://cassandra.apache.org/doc/latest/cassandra/tools/cqlsh.html
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "cqlsh", description = "Start Cqlsh")
public class DbCqlShellCmd extends AbstractConnectedCmd {

   /**
    * Database name or identifier
    */
   @Parameters(
           arity = "1",
           paramLabel = "DB",
           description = "Database name or identifier")
   public String db;
    
    // -- Cqlsh --
    
    /** Cqlsh Options. */
    @Option(names = { "--version" }, 
            description = "Display information of cqlsh.")
    protected boolean cqlShOptionVersion = false;
    
    /** Cqlsh Options. */
    @Option(names= {"--debug"}, 
            description= "Show additional debugging information.")
    protected boolean cqlShOptionDebug = false;
    
    /** Cqlsh Options. */
    @Option(names = {"--encoding" }, 
            arity = "1",
            paramLabel = "ENCODING",
            description = "Output encoding. Default encoding: utf8.")
    protected String cqlshOptionEncoding;
    
    /** Cqlsh Options. */
    @Option(names = {"-e", "--execute" }, 
            arity = "1",
            paramLabel = "STATEMENT",
            description = "Execute the statement and quit.")
    protected String cqlshOptionExecute;
    
    /** Cqlsh Options. */
    @Option(names = {"-f", "--file" },
            arity = "1",
            paramLabel = "FILE",
            description = "Execute commands from a CQL file, then exit.")
    protected String cqlshOptionFile;
    
    /** Cqlsh Options. */
    @Option(names = {"-k", "--keyspace" }, 
            arity ="1",
            paramLabel = "KEYSPACE",  
            description = "Authenticate to the given keyspace.")
    protected String cqlshOptionKeyspace;
    
    /** {@inheritDoc}  */
    public void execute() 
    throws DatabaseNameNotUniqueException, DatabaseNotFoundException, 
           CannotStartProcessException, FileSystemException {
        CqlShellOption options = new CqlShellOption(
                cqlShOptionVersion, cqlShOptionDebug, cqlshOptionEncoding,
                cqlshOptionExecute,cqlshOptionFile,cqlshOptionKeyspace);
        OperationsDb.startCqlShell(options, db);
    }
    
}
