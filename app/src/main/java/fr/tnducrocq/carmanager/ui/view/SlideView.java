package fr.tnducrocq.carmanager.ui.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import fr.tnducrocq.carmanager.R;

/**
 * Created by Chee Kiat on 23/02/2017.
 */

public class SlideView extends RelativeLayout {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private Drawable mSlideImage, mSlideBackground;
    private ImageView mSlideIcon;
    //private TextView mSlideTextView;
    private float storeCoordinate;
    private String slideText;

    private int orientation;

    private Integer duration, slideTextColor;

    private boolean isCanTouch = true;

    private int slideTextSize;

    private int slideSrcMarginLeft;
    private int slideSrcMarginTop;
    private int slideSrcMarginRight;
    private int slideSrcMarginBottom;

    private int slideSuccessPercent;

    private int getPercent;
    OnFinishListener onFinishListener;

    public interface OnFinishListener {

        void onFinish();
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }

    public SlideView(Context context) {
        super(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SlideData,
                0, 0);
        try {
            mSlideImage = a.getDrawable(R.styleable.SlideData_slideSrc);
            orientation = a.getInt(R.styleable.SlideData_android_orientation, HORIZONTAL);

            int margin = a.getDimensionPixelSize(R.styleable.SlideData_slideSrcMargin, -1);
            if (margin != -1) {
                slideSrcMarginLeft = margin;
                slideSrcMarginTop = margin;
                slideSrcMarginBottom = margin;
                slideSrcMarginRight = margin;
            } else {
                slideSrcMarginLeft = a.getDimensionPixelSize(R.styleable.SlideData_slideSrcMarginLeft, 0);
                slideSrcMarginTop = a.getDimensionPixelSize(R.styleable.SlideData_slideSrcMarginTop, 0);
                slideSrcMarginBottom = a.getDimensionPixelSize(R.styleable.SlideData_slideSrcMarginBottom, 0);
                slideSrcMarginRight = a.getDimensionPixelSize(R.styleable.SlideData_slideSrcMarginRight, 0);
            }

            slideSuccessPercent = a.getInteger(R.styleable.SlideData_slideSuccessPercent, 0);
            mSlideBackground = a.getDrawable(R.styleable.SlideData_slideBackground);
            duration = a.getInteger(R.styleable.SlideData_duration, 200);

            slideText = a.getString(R.styleable.SlideData_slideText);
            slideTextSize = a.getDimensionPixelSize(R.styleable.SlideData_slideTextSize, 20);
            slideTextColor = a.getColor(R.styleable.SlideData_slideTextColor, Color.BLACK);
        } finally {
            a.recycle();
        }

        if (mSlideBackground != null) {
            setBackground(mSlideBackground);
        }

        mSlideIcon = new ImageView(getContext());
        if (mSlideImage != null) {
            mSlideIcon.setImageDrawable(mSlideImage);
            mSlideIcon.setPadding(slideSrcMarginLeft, slideSrcMarginTop, slideSrcMarginRight, slideSrcMarginBottom);
        }

        /*if (slideText != null && !slideText.isEmpty()) {
            mSlideTextView = new TextView(getContext());

            mSlideTextView.setText(slideText);
            if (slideTextColor != null) {
                mSlideTextView.setTextColor(slideTextColor);
            }
            mSlideTextView.setTextSize(slideTextSize);
            addView(mSlideTextView);

            LayoutParams layoutParams =
                    (LayoutParams) mSlideTextView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mSlideTextView.setLayoutParams(layoutParams);
        }*/

        addView(mSlideIcon);

        Resources r = getResources();
        final float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());

        LayoutParams layoutParams =
                (LayoutParams) mSlideIcon.getLayoutParams();
        if (orientation == VERTICAL) {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            mSlideIcon.setY(margin);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            mSlideIcon.setX(margin);
        }

        mSlideIcon.setLayoutParams(layoutParams);

        Log.d(this.getClass().getName(), "getWidth()) : " + getSize());
        Log.d(this.getClass().getName(), "mSlideIcon()) : " + getSize(mSlideIcon));

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (getPercent == 0) {
                    if (slideSuccessPercent == 0) {
                        int size = getSize() - ((int) margin);
                        getPercent = ((size) - getSize(mSlideIcon) / 2);
                    } else {
                        int size = getSize() - (int) margin;
                        getPercent = ((size * slideSuccessPercent) / 100) - (getSize(mSlideIcon) / 2);
                    }
                }

                if (isCanTouch) {
                    int size = getSize() - ((int) margin);
                    float maxRight = (size) - getSize(mSlideIcon);

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            storeCoordinate = getRawCoordinate(event);
                            if (mSlideIcon.getTag() == null) {
                                mSlideIcon.setTag(getCoordinate(mSlideIcon));
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:

                            float sum = Math.abs(storeCoordinate - getRawCoordinate(event));

                            if (getRawCoordinate(event) < storeCoordinate) {
                                animate(mSlideIcon.animate().setDuration(0), (float) mSlideIcon.getTag()).start();
                            } else if (sum > maxRight) {
                                animate(mSlideIcon.animate().setDuration(0), maxRight).start();
                            } else {
                                animate(mSlideIcon.animate().setDuration(0), sum).start();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            isCanTouch = false;

                            if (getCoordinate(mSlideIcon) < getPercent) {
                                animate(mSlideIcon.animate().setDuration(duration), (float) mSlideIcon.getTag()).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        isCanTouch = true;
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                }).start();
                            } else {

                                animate(mSlideIcon.animate().setDuration(duration), maxRight).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (onFinishListener != null) {
                                            onFinishListener.onFinish();
                                        }
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();
                            }

                            break;
                        default:
                            return false;
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public ViewPropertyAnimator animate(ViewPropertyAnimator animator, float value) {
        if (orientation == VERTICAL) {
            return animator.y(value);
        } else {
            return animator.x(value);
        }
    }

    public float getRawCoordinate(MotionEvent event) {
        return orientation == VERTICAL ? event.getRawY() : event.getRawX();
    }

    public float getCoordinate(View view) {
        return orientation == VERTICAL ? view.getY() : view.getX();
    }

    public int getSize() {
        return orientation == VERTICAL ? getHeight() : getWidth();
    }

    public int getSize(View view) {
        return orientation == VERTICAL ? view.getHeight() : view.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void reset() {
        if (mSlideIcon != null && mSlideIcon.getTag() != null) {
            mSlideIcon.animate().setListener(null);
            animate(mSlideIcon.animate().setDuration(0), (float) mSlideIcon.getTag()).start();
            isCanTouch = true;
        }
    }
}