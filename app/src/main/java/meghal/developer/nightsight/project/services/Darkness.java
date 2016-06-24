package meghal.developer.nightsight.project.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.Subscribe;

import meghal.developer.nightsight.project.Events.BrightnessEvent;
import meghal.developer.nightsight.project.Events.ColorEvent;
import meghal.developer.nightsight.project.R;
import meghal.developer.nightsight.project.helper.SharedPrefs;
import meghal.developer.nightsight.project.ui.brightness.view.ChangeBrightness;

/**
 * Created by Meghal on 6/21/2016.
 */
public class Darkness extends Service {

    private static final int NOTIFICATION_ID = 10001;
    private static final String TAG = "Darkness";
    private NotificationManager mNotifyManager;
    private android.support.v7.app.NotificationCompat.Builder mBuilder;
    private static WindowManager wm;
    private static WindowManager.LayoutParams wmParams;
    private static LinearLayout myView;
    private SharedPreferences sharedPreferences;
    private int BRIGHTNESS = 0;
    private int color = R.color.black;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = this.getSharedPreferences(SharedPrefs.PREF_NAME, Activity.MODE_PRIVATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        BRIGHTNESS = sharedPreferences.getInt(SharedPrefs.KEY_BRIGHTNESS, 0);
        color = sharedPreferences.getInt(SharedPrefs.KEY_COLOR_ID, 0);
        if (sharedPreferences.getBoolean(SharedPrefs.KEY_SHOW_NOTIFICATION, true)) {
            startForeground(NOTIFICATION_ID, showNotification());
        }
        Log.i(TAG, "Service Created");
        createView();
        Log.i(TAG, "Service Started");

        return START_STICKY;
    }


    private Notification showNotification() {

        Intent resultIntent = new Intent(this, ChangeBrightness.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("NightView ")
                .setContentText("NightView is Running ! Tap to open the app")
                .setSmallIcon(R.mipmap.ic_launcher).setOngoing(true).setContentIntent(resultPendingIntent);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }
                }
        ).start();

        return mBuilder.build();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(myView);

        if (mNotifyManager != null) {
            mNotifyManager.cancel(NOTIFICATION_ID);
        }
    }


    private void createView() {

        Log.i(TAG, "View created");
        //  myView = new LinearLayout(new ContextThemeWrapper(getApplicationContext(), R.style.Theme_Transparent), null, 0);
        myView = new LinearLayout(getApplicationContext());
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);


        /**
         *
         * Window type: phone.  These are non-application windows providing
         * user interaction with the phone (in particular incoming calls).
         * These windows are normally placed above all applications, but behind
         * the status bar.
         */
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;

     /*   if (color != 0) {
            myView.setBackgroundColor(color));

        } else {
     */
//        myView.setBackgroundColor(ContextCompat.getColor(this,R.color.black));
        myView.setBackgroundColor(ContextCompat.
                getColor(this, sharedPreferences.getInt(SharedPrefs.KEY_COLOR_ID, R.color.black)));

        // }
        wmParams.alpha = (1.0f - ((BRIGHTNESS + 55) / (float) 255));
        myView.setClickable(false);
        wm.addView(myView, wmParams);
    }

    @Subscribe
    public void onBrightnesschangeEvent(BrightnessEvent brightnessEvent) {
        //  Log.i(TAG,wmParams.toString());

        Log.i(TAG, "Event called");
        BRIGHTNESS = brightnessEvent.getBrightness();
        //    BRIGHTNESS=sharedPreferences.getInt(SharedPrefs.KEY_BRIGHTNESS, 0);
        wmParams.alpha = (1.0f - ((BRIGHTNESS + 55) / (float) 255));
        wm.updateViewLayout(myView, wmParams);
        //  this.createView();
    }

    @Subscribe
    public void colorChangeEvent(ColorEvent colorEvent) {

        //     color=colorEvent.getColor();
        myView.setBackgroundColor(colorEvent.getColor());
        Log.i(TAG, "SELECTED color is :" + colorEvent.getColor());
        wm.updateViewLayout(myView, wmParams);

    }


}
