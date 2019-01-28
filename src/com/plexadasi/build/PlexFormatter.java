/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.build;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PlexFormatter
extends Formatter {
    @Override
    public String format(LogRecord record) {
        return new Date(record.getMillis()) + "::" + record.getMessage() + System.getProperty("line.separator");
    }
}

