package techmorphs.cloudhack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LexiNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Item returnedItem = Cam.returnedItem;
    Item LN= Cam.returnedItem;
/*
    public String productName = returnedItem.name;
    public String productDesc = returnedItem.descrp;
    public String productImg = returnedItem.img;
*/
    TextView productName = (TextView) findViewById(R.id.name);
    TextView productDesc = (TextView) findViewById(R.id.des);
    ImageView productImage = (ImageView) findViewById(R.id.productimg);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lexi_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("abcd",""+returnedItem.id);

        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        productName.setText(LN.name);


        productDesc.setText(LN.descrp);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //creating fragment object
        Fragment fragment = null;
        getMenuInflater().inflate(R.menu.lexi_navigation, menu);
        fragment = new about_product_frgment();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_about:
                //fragment = new about_product_frgment();
                Intent i = new Intent(LexiNavigation.this, LexiNavigation.class);
                startActivity(i);
                Log.d("Fragment", "About Fragement loaded");
                break;
            case R.id.nav_recipes:
                fragment = new Recipes_frgment();
                break;
            case R.id.nav_user_reviews:
                fragment = new User_Reviews();
                break;
            case R.id.nav_caption:
                Intent intent = new Intent(LexiNavigation.this, Cam.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LexiNavigation.this, MainActivity.class));
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.replace(R.id.content_frame, fragment);
            //ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }


}

