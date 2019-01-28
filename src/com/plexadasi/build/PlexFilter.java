/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.build;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PlexFilter
implements Filter {
    @Override
    public boolean isLoggable(LogRecord log) {
        if (log.getLevel() == Level.CONFIG) {
            return false;
        }
        return true;
    }
}

