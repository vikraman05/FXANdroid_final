package comin.chaten.statens.caugfitten.errorfinder;

import android.util.Log;


public class Writer {

    private static final String DEFAULT_TAG = "PortableCloud";

    public static void debug(Object toPrint) {
        Log.d(DEFAULT_TAG, toPrint.toString());
    }

    public static void debug(String tag, Object toPrint) {
        Log.d(tag, toPrint.toString());
    }

    public static void info(Object toPrint) {
        Log.i(DEFAULT_TAG, toPrint.toString());
    }

    public static void info(String tag, Object toPrint) {
        Log.i(tag, toPrint.toString());
    }

    public static void error(Object toPrint) {
        Log.e(DEFAULT_TAG, toPrint.toString());
    }

    public static void error(String tag, Object toPrint) {
        Log.e(tag, toPrint.toString());
    }

    public static void warn(Object toPrint) {
        Log.w(DEFAULT_TAG, toPrint.toString());
    }

    public static void warn(String tag, Object toPrint) {
        Log.w(tag, toPrint.toString());
    }

}
