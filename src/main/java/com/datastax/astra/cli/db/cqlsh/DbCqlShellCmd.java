package com.datastax.astra.cli.db.cqlsh;

import com.datastax.astra.cli.core.AbstractConnectedCmd;

import jakarta.inject.Inject;
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
@Command(name = "cqlsh", 
         description = "Start Cqlsh",
         synopsisHeading = "%nUsage: ",
         mixinStandardHelpOptions = true)
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
    
    @Inject
    CqlShellService cqlshService;
    
    /** {@inheritDoc}  */
    public void execute() {
        CqlShellOptions options = new CqlShellOptions(
                false, cqlShOptionDebug, cqlshOptionEncoding,
                cqlshOptionExecute,cqlshOptionFile,cqlshOptionKeyspace);
        cqlshService.run(options, db);
    }
    
}
