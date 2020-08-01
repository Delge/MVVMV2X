package model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SaveExceptionUtil implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "EMCrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static SaveExceptionUtil INSTANCE = new SaveExceptionUtil();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private String versionName;
    private String versionCode;

    private SaveExceptionUtil() {
    }


    public static SaveExceptionUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SaveExceptionUtil();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void removeHandler() {
        Thread.setDefaultUncaughtExceptionHandler(null);

    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // Log.e(TAG, "error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(mContext);
        String fileName = saveCrashInfo2File(ex);
        return true;
    }


    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            //    Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                //   Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                // Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    private String saveCrashInfo2File(Throwable ex) {

        // NIMClient.getService(AuthService.class).logout();

        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        Log.d(this.getClass().getSimpleName(), result);
        sb.append(result);
        saveException(sb.toString());
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
//            String userName = "nobody";
//            if (Hub.getDefault() != null && HubUser.current() != null) {
//                userName = HubUser.current().getName();
//            }
//            String fileName = userName + "-" + time + "-" + timestamp;
            String fileName = time + "-" + timestamp;
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = getCrashLogFileNameByUserAndName(fileName);
                prepareFilePath(path);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }


    public static final String Crash_File_Directory_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/MokerBug/Crash/";

    public static String getCrashLogFileNameByUserAndName(String fileName) {
        return Crash_File_Directory_Path + fileName + ".txt";
    }

    public static boolean prepareFilePath(String fileFullPathName) {

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            // no sdcard
            Log.e(TAG, "sd_card_is_not_available");
            return false;
        }
        Log.d(TAG, "fileFullPathName=" + fileFullPathName);

        File file = new File(fileFullPathName);
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            // can not create path directory
            Log.e(TAG, "can not create path directory, filePath=" + fileFullPathName);
            return false;
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void saveException(String ex) {
//        try {
//            PTException.save(ex,
//                    versionName,
//                    versionCode,
//                    new PTException.SaveCallback() {
//                        @Override
//                        public void saveCallback(boolean r) {
//                            Log.e(TAG, "PTException.save r=" + r);
//                        }
//                    });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
