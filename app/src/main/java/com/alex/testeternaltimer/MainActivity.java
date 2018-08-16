package com.alex.testeternaltimer;


import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    Intent mServiceIntent;
    Context ctx;
    private boolean canStart;

    public Context getCtx() {
        return ctx;
    }

    final int RequestExternalStoragePersmissionID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        canStart = true;
        setContentView(R.layout.activity_main);
        TimerService mTimerService = new TimerService(getCtx());
        mServiceIntent = new Intent(getCtx(), mTimerService.getClass());
        if (!isMyServiceRunning(mTimerService.getClass())) {
            startService(mServiceIntent);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestExternalStoragePersmissionID);
            canStart = false;
        }
        if (canStart) {
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File[] files = dir.listFiles();

            for (File file : files) {
                if (file.getName().startsWith("test")) {

                        Toast.makeText(ctx, unpackZip(file.getPath(), file.getName()), Toast.LENGTH_LONG).show();
                        return;
                }
            }
        }
    }

    private String unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "Success!";
    }

    /**
     * checking service state
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
    }
}


