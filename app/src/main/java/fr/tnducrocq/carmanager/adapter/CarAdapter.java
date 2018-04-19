package fr.tnducrocq.carmanager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.carmanager.R;
import fr.tnducrocq.carmanager.model.Provider;
import fr.tnducrocq.carmanager.model.SchedulerProvider;
import fr.tnducrocq.carmanager.model.beans.Car;
import io.reactivex.Observable;

/**
 * Created by tony on 12/04/2018.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private static final String TAG = CarAdapter.class.getSimpleName();

    private List<Car> mCarList;
    private OnCarInteractionListener mOnCarInteractionListener;

    public CarAdapter(List<Car> carList) {
        this.mCarList = carList;
    }

    public void setOnCarInteractionListener(OnCarInteractionListener onCarInteractionListener) {
        this.mOnCarInteractionListener = onCarInteractionListener;
    }

    @Override
    public int getItemCount() {
        return mCarList.size();
    }

    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_car, parent, false);
        return new CarAdapter.CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarAdapter.CarViewHolder holder, int position) {
        holder.bindData(mCarList.get(position));
        holder.mView.setOnClickListener(v -> {
            if (mOnCarInteractionListener != null) {
                mOnCarInteractionListener.onCarInteraction(mCarList.get(position), position);
            }
        });
    }

    public interface OnCarInteractionListener {

        void onCarInteraction(Car car, int position);
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_adapter_car_name)
        public TextView mName;

        @BindView(R.id.text_adapter_car_kilometers)
        public TextView mKilometers;

        @BindView(R.id.image_adapter_car_fuel)
        public ImageView mFuel;

        @BindView(R.id.image_adapter_car)
        public ImageView mCar;

        public View mView;

        public CarViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final Car car) {
            Context context = mView.getContext();
            mName.setText(car.getBrand() + " " + car.getName());
            mKilometers.setText(java.text.NumberFormat.getNumberInstance(Locale.FRANCE).format(car.getKilometers()) + " km");

            Drawable drawable;
            if (car.getFuel() < 0.05) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_0, null);
            } else if (car.getFuel() < 0.15) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_10, null);
            } else if (car.getFuel() < 0.25) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_20, null);
            } else if (car.getFuel() < 0.35) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_30, null);
            } else if (car.getFuel() < 0.45) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_40, null);
            } else if (car.getFuel() < 0.55) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_50, null);
            } else if (car.getFuel() < 0.65) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_60, null);
            } else if (car.getFuel() < 0.75) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_70, null);
            } else if (car.getFuel() < 0.85) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_80, null);
            } else if (car.getFuel() < 0.95) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_90, null);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.ic_fuel_100, null);
            }
            mFuel.setImageDrawable(drawable);

            if (car.getUrl() != null) {
                Glide.with(context).load(car.getUrl()).into(mCar);
            } else if (car.getImage() != null) {
                Observable.create(subscriber -> {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference gsReference = storage.getReference();
                    gsReference.child(car.getImage()).getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageURL = uri.toString();

                        car.setUrl(imageURL);
                        Provider.update(context, car)
                                .subscribeOn(SchedulerProvider.io())
                                .observeOn(SchedulerProvider.ui())
                                .subscribe(newCar -> {
                                    Log.d(TAG, "Provider.update(car)");
                                }, throwable -> Log.e(TAG, "Provider.update(car)", throwable));

                        subscriber.onNext(imageURL);
                        subscriber.onComplete();
                    }).addOnFailureListener(e -> {
                        subscriber.onError(e);
                        subscriber.onComplete();
                    });
                }).subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(imageURL -> {
                            Glide.with(context).load(imageURL).into(mCar);
                        }, throwable -> Log.e(TAG, "Provider.cars()", throwable));
            }

        }

    }

}
