package lucy.animationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import lucy.animationtest.draggridview.SvgActivity;

public class MainActivity extends Activity {

    private TextView text1;
    private TextView text2,text3,text4,text5, svgText;
    private TextView lucyTestText;
    private TextView activitTransition;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new RuntimeException().printStackTrace();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        int realDensity = getResources().getConfiguration().densityDpi;
        Log.d("lucy","realDensity -> " + realDensity);
    }

    public void initView(){
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        svgText = (TextView) findViewById(R.id.lucy_svg);

        lucyTestText = (TextView) findViewById(R.id.lucy_test);
        text1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Text1Activity.class);
                startActivity(i);

            }
        });
        text2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Text2Activity.class);
                startActivity(i);

            }
        });
        text3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Text3Activity.class);
                startActivity(i);
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Text4Activity.class);
                startActivity(i);
            }
        });
        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GragGridActivity.class);
                startActivity(i);
            }
        });
        lucyTestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LucyTestActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_anima,R.anim.activity_exit);
            }
        });

        svgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SvgActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.lucy_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(i);
            }
        });
    }
}
