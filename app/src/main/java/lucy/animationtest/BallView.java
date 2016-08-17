package lucy.animationtest;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class  BallView extends View implements ValueAnimator.AnimatorUpdateListener{
	private static final int RED = 0xffFF8080;
	private static final int BLUE = 0xff8080FF;
	private static final int CYAN = 0xff80ffff;
	private static final int GREEN = 0xff80ff80;

	private ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();


	public BallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BallView(Context context) {
		this(context, null);
	}

	public BallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//自定义View的背景切换动画
		ValueAnimator colorAnim = ObjectAnimator.ofInt(this,
				"backgroundColor", RED, BLUE,CYAN,GREEN);
		colorAnim.setDuration(3000L);
		colorAnim.setEvaluator(new ArgbEvaluator());
		colorAnim.setRepeatCount(ValueAnimator.INFINITE);
		colorAnim.setRepeatMode(ValueAnimator.REVERSE);
		colorAnim.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//在画布上绘制图形
		for(ShapeHolder holder:balls){
			canvas.save();
			canvas.translate(holder.getX(), holder.getY());
			holder.getShape().draw(canvas);
			canvas.restore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN
				&& event.getAction() != MotionEvent.ACTION_MOVE) {
			return false;
		}
		
		
		ShapeHolder newBall = addBall(event.getX(), event.getY());

		float startY = newBall.getY();
		float endY = getHeight() - 50f;

		float h = getHeight();
		float eventy = event.getY();

		int duration = (int) (500 * ((h - eventy) / h));
		
		//设置加速掉落的效果
		ValueAnimator bounceAnim = ObjectAnimator.ofFloat(newBall, "y",
				startY, endY);
		bounceAnim.setDuration(duration);
		bounceAnim.setInterpolator(new AccelerateInterpolator());
		
		//当掉落到底部的时候，球体压扁，高度降低
		ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(newBall, "x",
				newBall.getX(), newBall.getX() - 25);
		squashAnim1.setDuration(duration /4);
		squashAnim1.setRepeatCount(1);
		squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
		squashAnim1.setInterpolator(new DecelerateInterpolator());

		ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(newBall,"width", 
				newBall.getWidth(), newBall.getWidth() + 50);
		squashAnim2.setDuration(duration /4);
		squashAnim2.setRepeatCount(1);
		squashAnim2.setRepeatMode(ValueAnimator.REVERSE);
		squashAnim2.setInterpolator(new DecelerateInterpolator());

		ValueAnimator strechAnim1 = ObjectAnimator.ofFloat(newBall, "y",
				endY, endY + 25);
		strechAnim1.setDuration(duration /4);
		strechAnim1.setRepeatCount(1);
		strechAnim1.setRepeatMode(ValueAnimator.REVERSE);
		strechAnim1.setInterpolator(new DecelerateInterpolator());

		ValueAnimator strechAnim2 = ObjectAnimator.ofFloat(newBall,"height",
				newBall.getHeight(), newBall.getHeight() - 25);
		strechAnim2.setDuration(duration /4);
		strechAnim2.setRepeatCount(1);
		strechAnim2.setRepeatMode(ValueAnimator.REVERSE);
		strechAnim2.setInterpolator(new DecelerateInterpolator());

		ValueAnimator bounceBack = ObjectAnimator.ofFloat(newBall, "y",
				endY, startY+25);
		bounceBack.setDuration(duration);
		bounceAnim.setInterpolator(new DecelerateInterpolator());

		AnimatorSet set = new AnimatorSet();
		set.play(bounceAnim).before(squashAnim1);
		
		set.play(squashAnim1).with(squashAnim2);
		set.play(squashAnim1).with(strechAnim1);
		set.play(squashAnim1).with(strechAnim2);
		set.play(bounceBack).after(strechAnim2);
		
		//逐渐消失
		ValueAnimator fadeAnimator = ObjectAnimator.ofFloat(newBall,
				"alpha", 1F, 0F);
		fadeAnimator.setDuration(600L);

		fadeAnimator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				balls.remove(((ObjectAnimator) animation).getTarget());
			}

		});

		AnimatorSet animationSet = new AnimatorSet();
		animationSet.play(set).before(fadeAnimator);
		
		animationSet.start();
		return true;
	}

	/**
	 * 生成一个球
	 * @param x
	 * @param y
	 * @return
	 */
	public ShapeHolder addBall(float x, float y) {
		// 构造一个圆形的图案
		OvalShape oval = new OvalShape();
		oval.resize(50f, 50f);

		ShapeDrawable draw = new ShapeDrawable(oval);
		ShapeHolder holder = new ShapeHolder(draw);
		holder.setX(x - 25);
		holder.setY(y - 25);

		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);

		int color = 0xff000000 | red << 16 | green << 8 | blue;

		int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
				/ 4;
		Paint paint = draw.getPaint();
		RadialGradient gradient = new RadialGradient(12.5f, 12.5f, 50f,
				color, darkColor, Shader.TileMode.CLAMP);
		paint.setShader(gradient);

		holder.setGradient(gradient);
		holder.setPaint(paint);

		balls.add(holder);

		return holder;
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		// TODO Auto-generated method stub
		invalidate();
	}
}
