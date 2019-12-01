package com.production.wunner;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class TimeCounterService extends Service {
    public final static String TAG="TimeCounterService";
    public static final String COUNTTIMER_BR ="COUNTTIMERDOWN";
    Intent result =new Intent(COUNTTIMER_BR);
    long Timer= 0;
    CountDownTimer countDownTimer=null;
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();

        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            SharedPreferences.Editor editor =getSharedPreferences("TimerCount",MODE_PRIVATE).edit();
            Timer = intent.getLongExtra("count_timer", 0);
            editor.putLong("MaxTime",Timer);
            editor.apply();
        }
        catch (Exception ex)
        {
            SharedPreferences preferences =getSharedPreferences("TimeCount",MODE_PRIVATE);
            Timer=preferences.getLong("MaxTime",0);
        }
        Log.d("CHECK",new String(String.valueOf(Timer)));
        if(Timer!=0)
            countDownTimer =new CountDownTimer(Timer*60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    result.putExtra("responetimer", millisUntilFinished);
                    sendBroadcast(result);
                }

                @Override
                public void onFinish() {
                    result.putExtra("responetimer",0);
                    SharedPreferences.Editor editor =getSharedPreferences("TimerCount",MODE_PRIVATE).edit();
                    editor.remove("MaxTime");
                    editor.apply();
                    sendBroadcast(result);
                }
            }.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
