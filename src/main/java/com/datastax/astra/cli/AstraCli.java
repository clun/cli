package com.datastax.astra.cli;

import java.util.Arrays;

import org.fusesource.jansi.AnsiConsole;

import com.datastax.astra.cli.config.ConfigCommand;
import com.datastax.astra.cli.config.SetupCmd;
import com.datastax.astra.cli.core.CliContext;
import com.datastax.astra.cli.core.CliExecutionExceptionHandler;
import com.datastax.astra.cli.core.CliExitCodeExceptionMapper;
import com.datastax.astra.cli.core.CliParameterExceptionHandler;
import com.datastax.astra.cli.core.CliVersionProvider;
import com.datastax.astra.cli.db.DbCmd;
import com.datastax.astra.cli.iam.RoleCmd;
import com.datastax.astra.cli.iam.UserCmd;
import com.datastax.astra.cli.org.OrgCmd;
import com.datastax.astra.cli.streaming.StreamingCmd;

import io.micronaut.configuration.picocli.MicronautFactory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.HelpCommand;

/**
 * Main class and entry point for the astra cli. `astra`
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Command(name = "astra", 
         description = "Command Line Interface for DataStax Astra",
         header = {
         "@|green,bold     _____            __                 |@",
         "@|green,bold    /  _  \\   _______/  |_____________   |@",
         "@|green,bold   /  /_\\  \\ /  ___/\\   __\\_  __ \\__  \\  |@",
         "@|green,bold  /    |    \\\\___ \\  |  |  |  | \\// __ \\_|@",
         "@|green,bold  \\____|__  /____  > |__|  |__|  (____  /|@",
         "@|green,bold          \\/     \\/                   \\/ |@",
         "@|green,bold   |@ "},
         mixinStandardHelpOptions = true,
         versionProvider = CliVersionProvider.class)
public class AstraCli {
    
    /**
     * Main class for the CLI.
     *
     * @param args
     *      current arguments
     * @throws Exception
     *      exception during the cli execution
     */
    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();
        int exitCode = runCli(args);
        AnsiConsole.systemUninstall();
        System.exit(exitCode);
    }
    
    /**
     * Run the Cli with Micronaut.
     *
     * @param args
     *      current arguments.
     * @return
     *      exit code
     */
    public static int runCli(String[] args) {
        // Autocloseable
        try (ApplicationContext context = 
                ApplicationContext.builder(
                        AstraCli.class, Environment.CLI).start()) {
            
            // Save Arguments in context
            CliContext.getInstance()
                      .setArguments(Arrays.asList(args));
            
            // Color Scheme
            ColorScheme colorScheme = new ColorScheme.Builder()
                    .commands    (Style.bold, Style.fg_yellow)
                    .options     (Style.fg_cyan)
                    .parameters  (Style.fg_yellow)
                    .optionParams(Style.italic)
                    .errors      (Style.fg_red, Style.bold)
                    .stackTraces (Style.italic)
                    .build();
            
            // Main execution
            return new CommandLine(new AstraCli(), new MicronautFactory(context))
                    // permissive
                    .setCaseInsensitiveEnumValuesAllowed(true)
                    // Form help
                    .setUsageHelpAutoWidth(true)
                    // Custom exit codes for parameters
                    .setParameterExceptionHandler(new CliParameterExceptionHandler())
                    // Custom exit codes for execution error
                    .setExecutionExceptionHandler(new CliExecutionExceptionHandler())
                    // Mapping exception to error code
                    .setExitCodeExceptionMapper(new CliExitCodeExceptionMapper())
                    // Color scheme
                    .setColorScheme(colorScheme)
                    // SubCommands
                    .addSubcommand(new HelpCommand())
                    .addSubcommand(new SetupCmd())
                    .addSubcommand(new ConfigCommand())
                    .addSubcommand(new DbCmd())
                    .addSubcommand(new StreamingCmd())
                    .addSubcommand(new OrgCmd())
                    .addSubcommand(new RoleCmd())
                    .addSubcommand(new UserCmd())
                    .execute(args);
        }
    }
}
