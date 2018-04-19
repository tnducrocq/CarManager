package fr.tnducrocq.carmanager.car;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tnducrocq.carmanager.R;
import fr.tnducrocq.carmanager.ui.view.CircularSeekBar;

/**
 * Created by tony on 19/04/2018.
 */
public class CustomPopupSeekView extends RelativeLayout {

    CircularSeekBar.OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;

    CircularSeekBar mSeek;
    TextView mText;

    public CustomPopupSeekView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomPopupSeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomPopupSeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnCircularSeekBarChangeListener(CircularSeekBar.OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener) {
        this.mOnCircularSeekBarChangeListener = mOnCircularSeekBarChangeListener;
        if (mSeek != null) {
            mSeek.setOnSeekBarChangeListener(mOnCircularSeekBarChangeListener);
        }
    }

    protected void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.layout_popup_seek, this);
        mSeek = findViewById(R.id.seek_layoutseek_station);
        mSeek.setOnSeekBarChangeListener(mOnCircularSeekBarChangeListener);

        mText = findViewById(R.id.text_layoutseek_station);
    }


}
