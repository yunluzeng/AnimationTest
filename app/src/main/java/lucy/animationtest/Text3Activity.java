package lucy.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class Text3Activity extends Activity {
	private static final int ANIMATIONEACHOFFSET = 600; // 每个动画的播放时间间隔
	private AnimationSet aniSet, aniSet2, aniSet3;
	private ImageView btn, wave1, wave2, wave3;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x222) {
				wave2.startAnimation(aniSet2);
			} else if (msg.what == 0x333) {
				wave3.startAnimation(aniSet3);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text3);
		aniSet = getNewAnimationSet();
		aniSet2 = getNewAnimationSet();
		aniSet3 = getNewAnimationSet();
		btn = (ImageView) findViewById(R.id.btn);
		wave1 = (ImageView) findViewById(R.id.wave1);
		wave2 = (ImageView) findViewById(R.id.wave2);
		wave3 = (ImageView) findViewById(R.id.wave3);
		btn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					showWaveAnimation();
					break;
				case MotionEvent.ACTION_UP:
					cancalWaveAnimation();
					break;
				case MotionEvent.ACTION_CANCEL:
					cancalWaveAnimation();
					break;
				}
				return true;
			}
		});
	}

	private AnimationSet getNewAnimationSet() {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, 2.3f, 1f, 2.3f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(ANIMATIONEACHOFFSET * 3);
		sa.setRepeatCount(-1);// 设置循环
		AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
		aniAlp.setRepeatCount(-1);// 设置循环
		as.setDuration(ANIMATIONEACHOFFSET * 3);
		as.addAnimation(sa);
		as.addAnimation(aniAlp);
		return as;
	}

	private void showWaveAnimation() {
		wave1.startAnimation(aniSet);
		handler.sendEmptyMessageDelayed(0x222, ANIMATIONEACHOFFSET);
		handler.sendEmptyMessageDelayed(0x333, ANIMATIONEACHOFFSET * 2);

	}

	private void cancalWaveAnimation() {
		wave1.clearAnimation();
		wave2.clearAnimation();
		wave3.clearAnimation();
	}

}
