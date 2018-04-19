package fr.tnducrocq.carmanager.car;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.tnducrocq.carmanager.R;
import fr.tnducrocq.carmanager.model.beans.Car;
import fr.tnducrocq.carmanager.ui.view.CircularSeekBar;

/**
 * Created by tony on 19/04/2018.
 */

public class CarHomeFragment extends Fragment {

    private static final String TAG = CarHomeFragment.class.getSimpleName();
    private static final String ARG_CAR = "car";

    @NonNull
    private Car mCar;

    protected Unbinder unbinder;

    @BindView(R.id.image_car_picture)
    ImageView mPicture;

    @BindView(R.id.text_car_maintenance)
    TextView mTextMaintenance;

    @BindView(R.id.text_car_kilometers)
    TextView mTextKilometers;

    @BindView(R.id.deco_car_fuel)
    DecoView mDecoView;

    @BindView(R.id.fab_car_menu)
    FloatingActionMenu mFab;

    @BindView(R.id.fab_car_fuel)
    FloatingActionButton mFuelChange;

    public static CarHomeFragment newInstance(Car car) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CAR, car);
        CarHomeFragment fragment = new CarHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCar = getArguments().getParcelable(ARG_CAR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (mCar.getUrl() != null) {
            Glide.with(this).load(mCar.getUrl()).into(mPicture);
        }

        mTextMaintenance.setText(java.text.NumberFormat.getNumberInstance(Locale.FRANCE).format(mCar.getRevision()) + " km");
        mTextKilometers.setText(java.text.NumberFormat.getNumberInstance(Locale.FRANCE).format(mCar.getKilometers()) + " km");

        mFuelChange.setOnClickListener(v -> {


            CustomPopupSeekView customPopupSeekView = new CustomPopupSeekView(getActivity());
            int actual = (int) (mCar.getFuel() * 100);
            customPopupSeekView.mSeek.setProgress(actual);
            customPopupSeekView.mText.setText(Integer.toString(actual) + " %");
            customPopupSeekView.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    customPopupSeekView.mText.setText(Integer.toString(progress) + " %");
                    Log.d(TAG, "progress: " + progress);
                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });

            new MaterialDialog.Builder(getActivity())
                    .customView(customPopupSeekView, false)
                    .positiveText("OK")
                    .negativeText("FERMER")
                    .onPositive((dialog, which) -> {
                        int progress = customPopupSeekView.mSeek.getProgress();
                        mCar.setFuel(((float) progress) / 100f);
                        initFuel();
                    })
                    .show();
        });

        initFuel();
        return rootView;
    }

    private void initFuel() {
        mDecoView.executeReset();
        mDecoView.deleteAll();

        SeriesItem backSerie = new SeriesItem.Builder(getActivity().getColor(R.color.colorSerieBack))
                .setRange(0.0f, 1.0f, 1.0f)
                .setInitialVisibility(true)
                .build();
        int backIndex = mDecoView.addSeries(backSerie);

        int color = R.color.colorSerieRed;
        if (mCar.getFuel() > 0.55f) {
            color = R.color.colorSerieGreen;
        } else if (mCar.getFuel() > 0.25f) {
            color = R.color.colorSerieOrange;
        }

        final SeriesItem seriesItem = new SeriesItem.Builder(getActivity().getColor(color))
                .setRange(0.0f, 1.0f, 0)
                .setInterpolator(new LinearInterpolator())
                .build();
        int serieIndex = mDecoView.addSeries(seriesItem);


        final DecoEvent.ExecuteEventListener eventListener = new DecoEvent.ExecuteEventListener() {
            @Override
            public void onEventStart(DecoEvent event) {
            }

            @Override
            public void onEventEnd(DecoEvent event) {
                Log.d(TAG, "percentComplete: " + 1.0f + " percentFilled: " + event.getEndPosition());
            }
        };

        mDecoView.addEvent(new DecoEvent.Builder(1.0f)
                .setIndex(backIndex)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(mCar.getFuel())
                .setIndex(serieIndex)
                .setListener(eventListener)
                .setDuration((int) (mCar.getFuel() * 1000))
                .setDelay(500)
                .build());
    }
}
