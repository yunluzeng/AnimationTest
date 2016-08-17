package lucy.animationtest;

import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class Text2Activity extends Activity implements OnClickListener {
	private TextView[] tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text2);
		tv = new TextView[] { (TextView) findViewById(R.id.tv01), (TextView) findViewById(R.id.tv02),
				(TextView) findViewById(R.id.tv03), (TextView) findViewById(R.id.tv04),

				(TextView) findViewById(R.id.tv11), (TextView) findViewById(R.id.tv12),
				(TextView) findViewById(R.id.tv13), (TextView) findViewById(R.id.tv14) };
		initTextViews();
	}

	private void initTextViews() {
		for (TextView textView : tv) {// 颜色随机变化代码
										// 使用ValueAnimator类给每个视图加上北京颜色随时间变化的动画
			int color1 = Color.rgb((new Random()).nextInt(255), (new Random()).nextInt(255),
					(new Random()).nextInt(255));
			int color2 = Color.rgb((new Random()).nextInt(255), (new Random()).nextInt(255),
					(new Random()).nextInt(255));
			ValueAnimator animator = ObjectAnimator.ofInt(textView, "backgroundColor", color1, color2);
			animator.setDuration(3000);
			animator.setEvaluator(new ArgbEvaluator());// 设置数值计算器，保证动画变化过程中的数值正确
			animator.setRepeatCount(ValueAnimator.INFINITE); //Animation.INFINITE 表示重复多次
			animator.setRepeatMode(ValueAnimator.REVERSE);//RESTART表示从头开始，REVERSE表示从末尾倒播
			animator.start();

			textView.setOnClickListener(this);
		}
	}


	@Override
	public void onClick(final View view) {
		final ValueAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 1, 0);// 淡出效果
		animator1.setDuration(1000);
		animator1.setInterpolator(new AccelerateInterpolator()); // 开始比较慢，然后逐渐加速的插入器。
		ValueAnimator animator2 = ObjectAnimator.ofFloat(view, "x", view.getX(), (view.getX() - view.getWidth()));// 向左移动效果
		animator2.setDuration(1000);
		animator2.setInterpolator(new DecelerateInterpolator()); // 开始很快，然后逐渐减速。.

		AnimatorSet animatorSet = new AnimatorSet();// 合起来就是左淡出效果
		animatorSet.play(animator2).before(animator1);
		// animatorSet.start();

		final ValueAnimator animator3 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);// 淡入效果
		animator3.setDuration(1000);
		animator3.setInterpolator(new AccelerateInterpolator());
		ValueAnimator animator4 = ObjectAnimator.ofFloat(view, "x", view.getX() + 2 * view.getWidth(), view.getX());// 从右边向左移动
		animator4.setDuration(1000);
		animator4.setInterpolator(new DecelerateInterpolator());

		animator4.addListener(new AnimatorListenerAdapter() {// 当动画播放完，我们做什么

					@Override
					public void onAnimationEnd(Animator animation) {// 向下移动淡出，然后向上移动淡入
						super.onAnimationEnd(animation);
						final ValueAnimator animatorY = ObjectAnimator.ofFloat(view, "y", view.getY(), view.getY()
								+ view.getHeight());
						animatorY.setDuration(1000);
						final ValueAnimator alphaY = animator1.clone();

						ValueAnimator rotate = ObjectAnimator.ofFloat(view, "rotationY", 0, 90);
						rotate.setDuration(2000);
						// rotate.start();

						animatorY.addListener(new AnimatorListenerAdapter() {

							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								animatorY.reverse();
								animator3.clone().start();
							}

						});
						AnimatorSet set = new AnimatorSet();
						set.play(animatorY).with(alphaY);
						set.start();
					}

				});

		// animatorSet.play(animator3).after(animator1);//合起来就是左淡出，右淡入效果
		// animatorSet.play(animator3).with(animator4);
		// animatorSet.start();

		AnimatorSet animatorSet1 = new AnimatorSet();
		animatorSet1.play(animator3).with(animator4);

		AnimatorSet animatorSet2 = new AnimatorSet();
		animatorSet2.play(animator2).with(animator1);

		AnimatorSet set = new AnimatorSet();
		set.playSequentially(animatorSet2, animatorSet1);// playTogether（同时执行）、playSequentially（顺序执行）
		set.start();
	}
}
