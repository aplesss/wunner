package com.production.wunner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class Misson extends AppCompatActivity {
    ProgressBar progressBarView;
    CountDownTimer countDownTimer;
    int endTime = 25;
    TextView txt_timer,txt_mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misson);
        Toolbar toolbar = findViewById(R.id.toolbar);
        txt_timer =findViewById(R.id.txt_timer);
        txt_mission=findViewById(R.id.txt_mission);
        setSupportActionBar(toolbar);
        updateStage(3);
        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        txt_mission.setText("a) Luật chơi: Các đội được phát 15 tấm hình chụp các nơi trong bảo tàng. Trong đó có 10 tấm ảnh đúng và 5 tấm ảnh sai. Đội chơi có nhiệm vụ tìm địa điểm trong hình và chụp hình nhóm với địa điểm đó. Kiểu càng bựa càng tốt. Thời gian để hoàn thành trạm này: 30 phút.\n" +
                "Lưu ý: Đây là bảo tàng nên đội chơi không được gây ồn ào, mất trật tự.\n" +
                "b) Cách tính điểm: Mỗi tấm ảnh tìm được và chụp ảnh đúng thì được 5 điểm. Tổng là 50 điểm.");
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);
        Intent intent =new  Intent(Misson.this, TimeCounterService.class);
        intent.putExtra("count_timer",(long) endTime);
        startService(intent);
    }

    private void updateStage(int numstage) {

            SharedPreferences.Editor editor =getSharedPreferences("Stage",MODE_PRIVATE).edit();
            editor.putInt("Num_Stage",numstage);
            editor.apply();
    }

    private BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateTimerView(intent);
        }
    };

    private void UpdateTimerView(Intent intent) {
        long timer =intent.getExtras().getLong("responetimer");
        setProgress((int)timer,endTime);
        txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(timer)));
        if(timer<=0)
        {
            Toast.makeText(Misson.this,"Time out. Fighting", Toast.LENGTH_SHORT).show();

        }
    }


    private void setProgress(int myProgress, long time) {
        progressBarView.setMax((int) time);
        progressBarView.setSecondaryProgress( (int) time);
        progressBarView.setProgress(myProgress/1000);
        txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(time-myProgress)));
        if(myProgress<=0)
        {
            stopService(new Intent(Misson.this, TimeCounterService.class));
            updateStage(1);
        }

    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(TimeCounterService.COUNTTIMER_BR));
        super.onResume();
    }

    @Override
    protected void onPause() {

            try {
                unregisterReceiver(receiver);
            }
            catch (Exception ex ) {

            }
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(receiver);
        }
        catch (Exception ex )
        {

        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
