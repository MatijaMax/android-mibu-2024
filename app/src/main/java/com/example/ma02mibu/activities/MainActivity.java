package com.example.ma02mibu.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ActivityMainBinding;
import com.example.ma02mibu.model.UserRole;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHomeBase.toolbar;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(false);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);

        mAppBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_home, R.id.nav_products, R.id.nav_services)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        createNotificationChannel("Main notif channel", "Channel for music nottifications",
                "Kanal1", NotificationManager.IMPORTANCE_DEFAULT);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Menu m = navigationView.getMenu();
        FirebaseUser u = auth.getCurrentUser();
        if(u == null){
            return true;
        }
        CloudStoreUtil.selectUserRoleFor(u.getEmail(), result -> {
            if(result == null) {
                return;
            }

            if(result.getUserRole() == UserRole.USERROLE.ADMIN){
                m.findItem(R.id.nav_admin_category_management).setVisible(true);
                m.findItem(R.id.nav_event_type_management).setVisible(true);
                m.findItem(R.id.nav_grade_reports).setVisible(true);
                m.findItem(R.id.nav_owner_request_managment).setVisible(true);
            } else if(result.getUserRole() == UserRole.USERROLE.ORGANIZER){
                m.findItem(R.id.nav_event_creation).setVisible(true);
                m.findItem(R.id.nav_explore_and_filter).setVisible(true);
                m.findItem(R.id.nav_grading_company).setVisible(true);
                m.findItem(R.id.nav_reservations_organizer_view).setVisible(true);
            } else if(result.getUserRole() == UserRole.USERROLE.OWNER){
                m.findItem(R.id.nav_employees).setVisible(true);
                m.findItem(R.id.nav_services).setVisible(true);
                m.findItem(R.id.nav_products).setVisible(true);
                m.findItem(R.id.nav_packages).setVisible(true);
                m.findItem(R.id.nav_pricelist).setVisible(true);
                m.findItem(R.id.nav_company_profile).setVisible(true);
            } else if(result.getUserRole() == UserRole.USERROLE.EMPLOYEE){
                m.findItem(R.id.nav_employee_personal).setVisible(true);
                m.findItem(R.id.nav_services).setVisible(true);
                m.findItem(R.id.nav_products).setVisible(true);
                m.findItem(R.id.nav_packages).setVisible(true);
                m.findItem(R.id.nav_pricelist).setVisible(true);
            }
            m.findItem(R.id.log_out).setOnMenuItemClickListener(item -> {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
                return true;
            });
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if (backStackEntryCount > 0) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            String tag = backStackEntry.getName();
            if ("newProductPage".equals(tag) || "editProductPage".equals(tag)) {
                fragmentManager.popBackStackImmediate("productPage", 0);
                return;
            }
            if ("ServicesDetailsPage".equals(tag) || "editServicePage".equals(tag)) {
                fragmentManager.popBackStackImmediate("servicesPage", 0);
                return;
            }
            if ("newServicePage".equals(tag)) {
                fragmentManager.popBackStackImmediate("servicesPage", 0);
                return;
            }
            if ("newPackagePage".equals(tag) || "chooseProductsPage".equals(tag)) {
                fragmentManager.popBackStackImmediate("packagesPage", 0);
            }

            if ("newEmployeePage".equals(tag)) {
                fragmentManager.popBackStackImmediate("employeesPage", 0);
                return;
            }
            if ("EmployeeDetailsPage".equals(tag)) {
                fragmentManager.popBackStackImmediate("employeesPage", 0);
                return;
            }
            if ("myEventsPage".equals(tag)) {
                fragmentManager.popBackStackImmediate("filterAllPage", 0);
                return;
            }
            if ("EmployeeWorkCalendarPage".equals(tag)) {
                fragmentManager.popBackStackImmediate("employeesPage", 0);
                return;
            }
            if ("newWorkTimePage".equals(tag)) {
                fragmentManager.popBackStackImmediate("EmployeeDetailsPage", 0);
                return;
            }
            if ("EmployeePersonalWorkCalendar".equals(tag)) {
                fragmentManager.popBackStackImmediate("EmployeePersonalPage", 0);
                return;
            }
            if ("categoryManagement".equals(tag)) {
                fragmentManager.popBackStackImmediate("management", 0);
                return;
            }
            if ("subcategoryManagement".equals(tag)) {
                fragmentManager.popBackStackImmediate("management", 0);
                return;
            }
            if ("subcategoryRequestManagement".equals(tag)) {
                fragmentManager.popBackStackImmediate("management", 0);
                return;
            }
            if ("eventTypeEdit".equals(tag)) {
                fragmentManager.popBackStackImmediate("eventTypeManagement", 0);
                return;
            }
            if ("addBudgetPage".equals(tag)) {
                fragmentManager.popBackStack("createEventPage", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return;
            }
            if ("productsManagement".equals(tag)) {
                fragmentManager.popBackStackImmediate("productsManagement", 0);
            }
        }
        super.onBackPressed();
    }

    private void createNotificationChannel(CharSequence name, String description, String channel_id, int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("newProductPage");
        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("newServicePage");
        if (fragment1 != null && fragment1.isVisible()) {
            fragment1.onActivityResult(requestCode, resultCode, data);
        }
        else if(fragment2 != null && fragment2.isVisible()){
            fragment2.onActivityResult(requestCode, resultCode, data);
        }
    }
}
