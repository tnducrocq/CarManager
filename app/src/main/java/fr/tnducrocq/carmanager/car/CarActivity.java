package fr.tnducrocq.carmanager.car;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.carmanager.R;
import fr.tnducrocq.carmanager.model.beans.Car;

public class CarActivity extends AppCompatActivity {

    private static final String TAG = CarActivity.class.getSimpleName();

    private Car mCar;

    @BindView(R.id.toolbar_car)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            mCar = getIntent().getParcelableExtra("car");
        }
        if (mCar != null) {
            mToolbar.setTitle(mCar.getBrand() + " " + mCar.getName());
            Fragment fragment = CarHomeFragment.newInstance(mCar);
            getFragmentManager().beginTransaction()
                    .replace(R.id.layout_car_container, fragment)
                    .commit();
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
