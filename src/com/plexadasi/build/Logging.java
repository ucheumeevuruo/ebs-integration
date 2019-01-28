/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.build;

import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.PlexFilter;
import com.plexadasi.build.PlexHandler;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging<T> {
    private static Logger LOGGER;
    private static final String ENV = "intg_property";
    private static final Formatter DEFAULT_FORMAT;
    private static final String DEFAULT_FILENAME = "ebs_logger_";
    private static final String DATE_FORMAT = "dd-MM-yyyy-h";
    private Formatter format;
    private String fileName;
    private Boolean append;
    private static final String EXTENSION = ".log";

    private Logging(String fileName) {
        this(fileName, DEFAULT_FORMAT, true);
    }

    private Logging(String fileName, Formatter format) {
        this(fileName, format, true);
    }

    private Logging(String fileName, Formatter format, Boolean append) {
        this.format = format;
        this.fileName = fileName;
        this.append = append;
    }

    public static Logger logger() {
        Logging logger = new Logging("ebs_logger_");
        return logger.init();
    }

    public static Logger logger(String fileName) {
        Logging logger = new Logging(fileName);
        return logger.init();
    }

    public static Logger logger(String fileName, Formatter format) {
        Logging logger = new Logging(fileName, format);
        return logger.init();
    }

    public static Logger logger(String fileName, Formatter format, Boolean append) {
        Logging logger = new Logging(fileName, format, append);
        return logger.init();
    }

    public static <T> Logger logger(Class<T> type) {
        Logging logger = new Logging(type.getSimpleName());
        return logger.init();
    }

    public static <T> Logger logger(Class<T> type, Formatter format) {
        Logging<T> logger = new Logging<T>(type.getSimpleName(), format);
        return logger.init();
    }

    private <T> Logger init() {
        try {
            if (LOGGER == null) {
                LOGGER = Logger.getLogger(Logging.class.getName());
                this.getLogger();
            }
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return LOGGER;
    }

    private <T> void getLogger() throws SecurityException, IOException {
        LOGGER.addHandler(new PlexHandler());
        FileHandler fileHandler = new FileHandler(Logging.getLogFile(this.fileName), this.append);
        fileHandler.setFormatter(this.format);
        fileHandler.setFilter(new PlexFilter());
        LOGGER.addHandler(fileHandler);
    }

    private static <T> String getLogFile(String fileName) {
        SimpleDateFormat app = new SimpleDateFormat("dd-MM-yyyy-h");
        String dateApp = app.format(new Date());
        String path = HelperAP.getLogPath() + fileName + dateApp + ".log";
        return path;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; ++i) {
            Logging.logger(Logging.class).log(Level.INFO, "test Today Today{0}", i);
        }
    }

    static {
        DEFAULT_FORMAT = new SimpleFormatter();
    }
}

