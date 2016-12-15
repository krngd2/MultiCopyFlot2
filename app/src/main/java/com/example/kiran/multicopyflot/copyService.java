package com.example.kiran.multicopyflot;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by Sai Kiran on 8/7/2016.
 */
public class copyService extends Service {
    public String serviceclipText;
    Context context;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        final Intent mIntent = new Intent(this, copyService.class);
        final Bundle extras = mIntent.getExtras();
        //copy text from clipboard to adapter
        ClipboardManager.OnPrimaryClipChangedListener
                mPrimaryChangeListener =
                new ClipboardManager.OnPrimaryClipChangedListener() {
                    public void onPrimaryClipChanged() {
                        ClipboardManager clipBoard = (ClipboardManager)
                                getSystemService(CLIPBOARD_SERVICE);

                        serviceclipText = clipBoard.getPrimaryClip().getItemAt(0).getText().toString();

                        Intent intent = new Intent(copyService.this, MainActivity.class);

                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);


                        intent.putExtra("text",serviceclipText);
                      /*  startActivity(intent);*/
                         startActivity(intent);
                        Toast.makeText(getApplicationContext(),
                                "Text is copied to MultiCopy flot", Toast.LENGTH_SHORT).show();

                    }
                };
        clipboardManager.addPrimaryClipChangedListener(mPrimaryChangeListener);
        Toast.makeText(getApplicationContext(),
                "Background Service Started ", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),
                "Background Service Stopped ", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
