package lucy.animationtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2017/3/20.
 */

public class TimeActivity extends Activity {
    private TextView timeTextView;
    int year,month,day;

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_PROVIDER_CHANGED) || action.equals(
                    Intent.ACTION_TIME_CHANGED) || action.equals(
                    Intent.ACTION_TIMEZONE_CHANGED) || action.equals(
                    Intent.ACTION_DATE_CHANGED)) {
                Log.d("test","action = " + action);
                updateTime();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity_layout);
        timeTextView = (TextView) findViewById(R.id.time_text);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(timeReceiver,intentFilter);

        updateTime();
    }

    public void updateTime(){
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        year = time.year;
        month = time.month + 1;
        day = time.monthDay;
        timeTextView.setText(year + "年" +month + "月"+ day + "日");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
    }
}
