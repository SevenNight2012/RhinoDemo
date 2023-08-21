package com.xxc.android.rhino;

import android.util.Log;

import java.util.LinkedList;

/**
 * js console impl
 */
public class ConsoleImpl {

    private final LinkedList<LogPoint> logStack = new LinkedList<>();

    public void log(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
            sb.append(',');
        }
        sb.delete(sb.length() - 1, sb.length());
        Log.d("Console", sb.toString());
    }

    public void error(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
            sb.append(',');
        }
        sb.delete(sb.length() - 1, sb.length());
        Log.e("Console", sb.toString());
    }

    public void startMethod(Object methodName) {
        String name = methodName.toString();
        logStack.push(new LogPoint(name));
    }

    public void endMethod() {
        LogPoint first = logStack.poll();
        if (first != null) {
            long during = System.currentTimeMillis() - first.methodStart;
            Log.d("Console", first.methodName + " during: " + during);
        }
    }

    private static class LogPoint {
        String methodName;
        long methodStart;

        public LogPoint(String methodName) {
            this.methodName = methodName;
            this.methodStart = System.currentTimeMillis();
        }
    }

}
