package fr.tnducrocq.carmanager;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.carmanager.model.beans.Car;

public class CarActivity extends AppCompatActivity {

    private Car mCar;

    @BindView(R.id.image_car_picture)
    ImageView mPicture;

    @BindView(R.id.car_toolbar_layout)
    public CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.car_toolbar)
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);

        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            mCar = getIntent().getParcelableExtra("car");
        }
        if (mCar != null) {
            initView();
        }
    }

    public void initView() {
        mCollapsingToolbarLayout.setTitle(mCar.getBrand() + " " + mCar.getName());
        if (mCar.getUrl() != null) {
            Glide.with(this).load(mCar.getUrl()).into(mPicture);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_car, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
