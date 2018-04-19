package fr.tnducrocq.carmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.carmanager.adapter.CarAdapter;
import fr.tnducrocq.carmanager.car.CarActivity;
import fr.tnducrocq.carmanager.model.Provider;
import fr.tnducrocq.carmanager.model.SchedulerProvider;
import fr.tnducrocq.carmanager.model.beans.Car;
import fr.tnducrocq.carmanager.model.beans.User;
import fr.tnducrocq.carmanager.utils.UserUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MENU_LOGOUT = 6;

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.list_main_cars)
    RecyclerView mList;

    private CarAdapter mAdapter;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        User user = UserUtils.getCurrentUser(this);
        Provider.cars(this, user.getTeamId())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(cars -> {
                    for (Car car : cars) {
                        Log.d(TAG, car.toString());
                    }
                    mAdapter = new CarAdapter(cars);
                    mAdapter.setOnCarInteractionListener((car, position) -> {
                        Intent intent = new Intent(MainActivity.this, CarActivity.class);
                        intent.putExtra("car", car);
                        startActivity(intent);
                    });
                    mList.setAdapter(mAdapter);
                }, throwable -> Log.e(TAG, "Provider.cars()", throwable));

        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getMail()).withIcon(user.getPhotoUrl()).withIdentifier(100);
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withOnAccountHeaderListener((view, profile1, current) -> {
                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.btn_main_logout).withIcon(R.drawable.ic_exit_to_app_blue_24dp).withSelectedIcon(R.drawable.ic_exit_to_app_blue_24dp).withIdentifier(MENU_LOGOUT).withSetSelected(true)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem != null) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .build();
        result.updateBadge(4, new StringHolder(10 + ""));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
    }
}
