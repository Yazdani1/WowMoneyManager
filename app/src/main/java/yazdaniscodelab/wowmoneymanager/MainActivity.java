package yazdaniscodelab.wowmoneymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private BottomNavigationView navigationItemView;
    private FrameLayout frameLayout;


    //Fragment..

    private DashBoardFragment dashBoardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;


    //Firebase

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Track Your Expense");
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();

        navigationItemView=findViewById(R.id.bottomNavigationView);
        frameLayout=findViewById(R.id.main_frame);

        DrawerLayout drwer = (DrawerLayout) findViewById(R.id.drawer_layout_id);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drwer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drwer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        dashBoardFragment=new DashBoardFragment();
        incomeFragment=new IncomeFragment();
        expenseFragment=new ExpenseFragment();

        setFragment(dashBoardFragment);

        navigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.dashboard:
                        navigationItemView.setItemBackgroundResource(R.color.dashboard);
                        setFragment(dashBoardFragment);

                        return true;

                    case R.id.add:
                        navigationItemView.setItemBackgroundResource(R.color.income);

                        setFragment(incomeFragment);
                        return true;


                    case R.id.remove:
                        navigationItemView.setItemBackgroundResource(R.color.expense);

                        setFragment(expenseFragment);
                        return true;

                    default:
                        return false;


                }

            }


        });


    }

    private void setFragment(android.support.v4.app.Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();


    }
    @Override
    public void onBackPressed() {

        DrawerLayout drwer = (DrawerLayout) findViewById(R.id.drawer_layout_id);

        if (drwer.isDrawerOpen(GravityCompat.END)) {
            drwer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }

    }


    public void displaySelectedScreen(int itemId) {

        //creating fragment object
        android.support.v4.app.Fragment fragment = null;

        //initializing the fragment object which is selected


        switch (itemId) {
            case R.id.dashboard:
                fragment=new DashBoardFragment();
                break;

            case R.id.add:
                fragment=new IncomeFragment();
                break;


            case R.id.remove:
                fragment=new ExpenseFragment();
                break;

            case R.id.logout:
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Log Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;

        }




        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_id);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

}
