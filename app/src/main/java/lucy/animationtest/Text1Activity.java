package lucy.animationtest;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Text1Activity extends Activity {
	private Button btn;
	private TextView text;
    int formColor;
    int toColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text1);
		  formColor = Color.parseColor("#056EA5");
          toColor = Color.parseColor("#C82B24");
		 btn = (Button) findViewById(R.id.button);
		 text = (TextView) findViewById(R.id.text);
		 btn.setOnClickListener(new OnClickListener() {

              @Override
              public void onClick(View v) {

                      text.setTextColor(formColor);

                      ValueAnimator colorAnimation = ValueAnimator.ofObject(
                                      new ArgbEvaluator(), formColor, toColor);

                      colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

                              @Override
                              public void onAnimationUpdate(ValueAnimator animation) {
                                      text.setTextColor((Integer) animation
                                                      .getAnimatedValue());
//                                  Log.d("text1Activity","update color = " + (Integer) animation.getAnimatedValue());
                              }
                      });

                      colorAnimation.setDuration(5200);
                      colorAnimation.start();
              }
      });
	}

}
