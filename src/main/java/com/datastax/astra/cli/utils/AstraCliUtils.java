package com.datastax.astra.cli.utils;

import java.io.File;

import com.datastax.astra.cli.core.out.AstraCliConsole;

import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Help.ColorScheme;

/**
 * Utilities for cli. 
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class AstraCliUtils {
    
    /** Environment variable coding user home. */
    public static final String ENV_USER_HOME = "user.home";
    
    /** Path to save third-parties. */
    public static final String ASTRA_HOME = System.getProperty(ENV_USER_HOME) + File.separator + ".astra";
    
    /** Folder name where to download SCB. */
    public static final String SCB_FOLDER = "scb";
    
    /** Folder name to download archives */
    public static final String TMP_FOLDER = "tmp";
    
    /** Color scheme for the CLI. */
    public static final ColorScheme COLOR_SCHEME = new ColorScheme.Builder()
                .commands    (Style.bold, Style.fg_yellow)
                .options     (Style.fg_cyan)
                .parameters  (Style.fg_yellow)
                .optionParams(Style.italic)
                .errors      (Style.fg_red, Style.bold)
                .stackTraces (Style.italic)
                .build();
    
    /**
     * Show version.
     *
     * @return
     *      return version
     */
    public static String version() {
        String versionPackage = AstraCliConsole.class
                .getPackage()
                .getImplementationVersion();
        if (versionPackage == null) {
            versionPackage = "Development";
        }
        return versionPackage;
    }
    
}
