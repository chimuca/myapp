package com.messake.messake.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.Settings;

import com.messake.messake.foregin.Defaults;
import com.messake.messake.foregin.Globals;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by messake on 2015/12/26.
 */
abstract public class RemoteUtil {
    public static String getAndroidId() {
        ContentResolver cr = Globals.getContext().getContentResolver();
        return Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
    }

    /**
     * Get the SwiFTP version from the manifest.
     * @return The version as a String.
     */
    public static String getVersion() {
        String packageName = Globals.getContext().getPackageName();
        try {
            return Globals.getContext().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch ( PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    public static byte byteOfInt(int value, int which) {
        int shift = which * 8;
        return (byte)(value >> shift);
    }

    public static String ipToString(int addr, String sep) {
        //myLog.l(Log.DEBUG, "IP as int: " + addr);
        if(addr > 0) {
            StringBuffer buf = new StringBuffer();
            buf.
                    append(byteOfInt(addr, 0)).append(sep).
                    append(byteOfInt(addr, 1)).append(sep).
                    append(byteOfInt(addr, 2)).append(sep).
                    append(byteOfInt(addr, 3));

            return buf.toString();
        } else {
            return null;
        }
    }

    public static InetAddress intToInet(int value) {
        byte[] bytes = new byte[4];
        for(int i = 0; i<4; i++) {
            bytes[i] = byteOfInt(value, i);
        }
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            // This only happens if the byte array has a bad length
            return null;
        }
    }

    public static String ipToString(int addr) {
        if(addr == 0) {
            // This can only occur due to an error, we shouldn't blindly
            // convert 0 to string.

            return null;
        }
        return ipToString(addr, ".");
    }

    // This exists to avoid cluttering up other code with
    // UnsupportedEncodingExceptions.
    public static byte[] jsonToByteArray(JSONObject json) throws JSONException {
        try {
            return json.toString().getBytes(Defaults.STRING_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // This exists to avoid cluttering up other code with
    // UnsupportedEncodingExceptions.
    public static JSONObject byteArrayToJson(byte[] bytes) throws JSONException {
        try {
            return new JSONObject(new String(bytes, Defaults.STRING_ENCODING));
        } catch (UnsupportedEncodingException e) {
            // This will never happen because we use valid encodings
            return null;
        }
    }

    public static void newFileNotify(String path) {
        if(Defaults.do_mediascanner_notify) {

            new MediaScannerNotifier(Globals.getContext(), path);
        }
    }

    public static void deletedFileNotify(String path) {
        // This might not work, I couldn't find an API call for this.
        if(Defaults.do_mediascanner_notify) {

            new MediaScannerNotifier(Globals.getContext(), path);
        }
    }

    // A class to help notify the Music Player and other media services when
    // a file has been uploaded. Thanks to Dave Sparks in his post to the
    // Android Developers mailing list on 14 Feb 2009.
    private static class MediaScannerNotifier implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection connection;
        private String path;

        public MediaScannerNotifier(Context context, String path) {
            this.path = path;
            connection = new MediaScannerConnection(context, this);
            connection.connect();
        }

        public void onMediaScannerConnected() {
            connection.scanFile(path, null); // null: we don't know MIME type
        }

        public void onScanCompleted(String path, Uri uri) {
            connection.disconnect();
        }
    }

    public static String[] concatStrArrays(String[] a1, String[] a2) {
        String[] retArr = new String[a1.length + a2.length];
        System.arraycopy(a1, 0, retArr, 0, a1.length);
        System.arraycopy(a2, 0, retArr, a1.length, a2.length);
        return retArr;
    }

    public static void sleepIgnoreInterupt(long millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) {}
    }
}

