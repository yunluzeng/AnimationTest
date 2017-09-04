package lucy.animationtest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;

/**
 * Created by user on 2016/10/17.
 */
public class CoverAnimView extends View {

    private int mRows;
    private final int mColumns = 6;

    private Paint mPaint;
    private Rect[][] mRects;
    private AnimatorColor[][] mAnimatorColors;

    private static final long DELAY = 120;
    private static final long DURATION = 640;
    private long mTotalDuration;

    private int mItemWidth;

    private ValueAnimator mShowValueAnimator;

    public CoverAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRects != null && mAnimatorColors != null) {
            for (int row = 0; row < mRows; row++) {
                for (int col = 0; col < mColumns; col++) {
                    mPaint.setColor((mAnimatorColors[row][col].alpha << 24) + mAnimatorColors[row][col].color);
                    canvas.drawRect(mRects[row][col], mPaint);
                }
            }
        }
    }

    public void show(final OnAnimatorListener animatorListener) {
        mShowValueAnimator = ValueAnimator.ofInt(0, (int) mTotalDuration);
        mShowValueAnimator.setDuration(mTotalDuration);
        mShowValueAnimator.setInterpolator(new PathInterpolator(0.42F, 0.0F, 0.58F, 1.0F));
        mShowValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                for (AnimatorColor[] animatorColors : mAnimatorColors) {
                    for (AnimatorColor animatorColor : animatorColors) {
                        if (value <= animatorColor.animatorStartTime) {
                            animatorColor.alpha = 0xFF;
//                            animatorColor.alpha = 0;
                        } else if (value >= animatorColor.animatorEndTime) {
                            animatorColor.alpha = 0;
//                            animatorColor.alpha = 0xFF;
                        } else {
                            float temp = (animatorColor.animatorEndTime - value) / (animatorColor.animatorDuration * 1f);
                            animatorColor.alpha = (int) (0xFF * temp);
//                            animatorColor.alpha = (int) (0xFF * temp);
                        }
                    }
                }
                invalidate();
            }
        });
        mShowValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mShowValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mShowValueAnimator != null && mShowValueAnimator.isRunning()) {
            mShowValueAnimator.cancel();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemWidth = w / mColumns;
        mRows = h / mItemWidth;
        int extra = h - mRows * mItemWidth;
        if (extra > 0) {
            mRows++;
        }
        mRects = new Rect[mRows][mColumns];
        mAnimatorColors = new AnimatorColor[mRows][mColumns];
        for (int row = 0; row < mRows; row++) {
            int top = row * mItemWidth;
            int bottom = top + mItemWidth;
            if (bottom > h) {
                bottom = h;
            }
            for (int col = 0; col < mColumns; col++) {
                int left = col * mItemWidth;
                int right = left + mItemWidth;
                mRects[row][col] = new Rect(left, top, right, bottom);

                AnimatorColor animatorColor = new AnimatorColor();
                animatorColor.color = 0x1A1E24;

                int delayLevel;
                if (row <= 2) {
                    if (col <= 2) {
                        delayLevel = getDelayLevel(row, col, 2, 2);
                    } else {
                        delayLevel = getDelayLevel(row, col, 2, 3);
                    }
                } else {
                    if (col >= 3) {
                        delayLevel = getDelayLevel(row, col, 3, 3);
                    } else {
                        delayLevel = getDelayLevel(row, col, 3, 2);
                    }
                }
                animatorColor.animatorStartTime = delayLevel * DELAY;
                animatorColor.animatorDuration = DURATION;
                animatorColor.animatorEndTime = animatorColor.animatorStartTime + animatorColor.animatorDuration;
                if (animatorColor.animatorEndTime > mTotalDuration) {
                    mTotalDuration = animatorColor.animatorEndTime;
                }
                mAnimatorColors[row][col] = animatorColor;
            }
        }
    }

    private int getDelayLevel(int row, int col, int rootRow, int rootCol) {
        if (row == rootRow && col == rootCol) {
            return 0;
        } else {
            return Math.abs(rootRow - row) + Math.abs(rootCol - col);
        }
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public interface OnAnimatorListener {
        void onAnimationEnd();
    }

    private class AnimatorColor {
        public int color;
        public int alpha;
        public long animatorStartTime;
        public long animatorEndTime;
        public long animatorDuration;
    }
}
