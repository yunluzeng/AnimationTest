package lucy.animationtest;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LucyTestActivity extends Activity {
    private TextView text1,text2;
    private TextView scaleText;
    private RelativeLayout contentLayout;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucy_test);
        text1 = (TextView) findViewById(R.id.title1);
        text2 = (TextView) findViewById(R.id.title2);
        scaleText = (TextView) findViewById(R.id.scale_text);
        contentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        listview = (ListView) findViewById(R.id.list);
        String[] strings = {"img","title","info","time"};//Map的key集合数组
        int[] ids = {R.id.img,R.id.title,R.id.info,R.id.time};//对应布局文件的id
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                getData(), R.layout.tem, strings, ids);
        listview.setAdapter(simpleAdapter);//绑定适配器


        layoutAnimation();
        titleAnimation();
        scaleAnimation();

    }
    // 初始化一个List
    private List<HashMap<String, Object>> getData() {
        // 新建一个集合类，用于存放多条数据
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        for (int i = 1; i <= 40; i++) {
            map = new HashMap<String, Object>();
            map.put("title", "人物" + i);
            map.put("time", "9月20日");
            map.put("info", "LayoutAnimationController");
            map.put("img", R.mipmap.ic_launcher);
            list.add(map);
        }
        return list;
    }

    private void layoutAnimation(){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,300,0,0);
        translateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        translateAnimation.setRepeatMode(ValueAnimator.REVERSE);
        translateAnimation.setDuration(3000);

        LayoutAnimationController controller = new LayoutAnimationController(translateAnimation);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);//设置控件显示的顺序；
        contentLayout.setLayoutAnimation(controller);
    }

    private void titleAnimation(){
        AnimatorSet animatorSet1 = new AnimatorSet();
        AnimatorSet animatorSet2 = new AnimatorSet();

        ValueAnimator alphaAnimator1 = ObjectAnimator.ofFloat(text1,"alpha",0,1);
        alphaAnimator1.setDuration(2000);
        alphaAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator1.setRepeatMode(ValueAnimator.REVERSE);

        ValueAnimator alphaAnimator2 = ValueAnimator.ofFloat(0,1);
        alphaAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float) valueAnimator.getAnimatedValue();
                text2.setAlpha(alpha);
            }
        });
        ValueAnimator yAnimator1 = ObjectAnimator.ofFloat(text1,"y",0,100);
        yAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                Log.d("test","update ----> text1 getY = " + text1.getY() + ",text2 getY = "+ text2.getY());
            }
        });
        yAnimator1.setDuration(2000);
        yAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        yAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        yAnimator1.setInterpolator(new AccelerateInterpolator());

        ValueAnimator yAnimator2 = ObjectAnimator.ofFloat(text2,"y",500,300);
        yAnimator2.setDuration(2000);
        yAnimator2.setInterpolator(new AccelerateInterpolator());


        animatorSet1.playTogether(alphaAnimator1,yAnimator1);
        animatorSet2.playTogether(alphaAnimator2,yAnimator2);
        animatorSet1.start();
        animatorSet2.start();
    }

    private void scaleAnimation(){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.4f, 1.4f, 0.4f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimation.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimation.setFillAfter(false);//动画执行完后是否停留在执行完的状态
        scaleAnimation.setStartOffset(3000);//执行前的等待时间
        scaleAnimation.setDuration(3000);

        int color1 = Color.rgb((new Random()).nextInt(255), (new Random()).nextInt(255),
                (new Random()).nextInt(255));
        int color2 = Color.rgb((new Random()).nextInt(255), (new Random()).nextInt(255),
                (new Random()).nextInt(255));
        ValueAnimator valueAnimator = ObjectAnimator.ofArgb(scaleText,"backgroundColor",color1,color2);
        valueAnimator.setDuration(3000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.start();


        RotateAnimation rotateAnimation = new RotateAnimation(0,90,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setRepeatMode(ValueAnimator.REVERSE);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);

        scaleText.setAnimation(rotateAnimation);
        rotateAnimation.start();

//        scaleText.setAnimation(scaleAnimation);
//        scaleAnimation.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_anima,R.anim.activity_exit);
    }
}
