package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.MyProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences pref;
    private TextView tvUsername, tvEmail;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View header;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private MenuItem refreshFriendItem;
    private UserSessionObject session;
    private ImageView imgUser;
    private LinearLayout linearLayout;
    private ImageHelper imageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        setContentView(R.layout.activity_main);
        session = new UserSessionObject(this);
        initStart();
    }

    private void initStart() {
        pref = getSharedPreferences("com.preferences.sheeshapp", Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle("Friends");
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        changeFabToAddFriend();
                        toolbar.setTitle("Friends");
                        refreshFriendItem.setVisible(true);
                        break;
                    case 1:
                        changeFabToSetup();
                        toolbar.setTitle("Let's Sheesh");
                        refreshFriendItem.setVisible(false);
                        break;
                    case 2:
                        changeFabToStatistics();
                        toolbar.setTitle("History");
                        refreshFriendItem.setVisible(false);
                        break;
                }
                super.onTabSelected(tab);
            }
        });

        imageHelper = new ImageHelper(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        changeFabToAddFriend();
        int[] icons = {
                R.drawable.tab_friends_selector,
                R.drawable.tab_setup_selector,
                R.drawable.tab_statistic_selector
        };

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }

        tvUsername = header.findViewById(R.id.tvMaiUsername);
        tvEmail = header.findViewById(R.id.tvMaiEmail);

        tvUsername.setText(session.getName());
        tvEmail.setText(session.getEmail());

        imgUser = header.findViewById(R.id.imgHeader);
        setImgUser();
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntent(v);
            }
        });

    }

    private void setImgUser() {
        String user = session.getUser_id()+"";
        Bitmap bitmap = imageHelper.loadImageFromStorage(user);
        if (bitmap == null) {
            if (imgUser != null) {
                Glide.with(getApplicationContext()).load(R.drawable.sheeshopa).apply(RequestOptions.circleCropTransform()).into(imgUser);
            }
        } else {
            Bitmap thumbnail = imageHelper.getThumbnailOfBitmap(bitmap,200,200);
            Glide.with(getApplicationContext()).load(thumbnail).apply(RequestOptions.circleCropTransform()).into(imgUser);
            //imgUser.setImageDrawable(imageHelper.getRoundedBitmap(thumbnail));
        }
    }

    public void animateIntent(View view) {
        System.out.println("ANIMATING");
       // Bitmap bitmap = ((BitmapDrawable)imgUser.getDrawable()).getBitmap();
      //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
       // bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
        //byte[] byteArray = stream.toByteArray();
        Intent intent = new Intent(this, MyProfileActivity.class);
        String transitionName = getString(R.string.transition_string);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        imgUser,   // Starting view
                        transitionName    // The String
                );
        //intent.putExtra("image",byteArray);
        System.out.println("BEFORE STARTING");
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    public void changeFabToAddFriend() {
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.icon_plus_white);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

    }

    public void changeFabToSetup() {
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.icon_setup_white);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TrackerSetupFragment.runShisha(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, TimeTrackerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void changeFabToStatistics() {
        fab.setVisibility(View.INVISIBLE);
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
        getMenuInflater().inflate(R.menu.main, menu);
        refreshFriendItem = menu.findItem(R.id.menuRefreshFriends);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuRefreshFriends) {

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_tracker) {
            viewPager.setCurrentItem(1);

        } else if (id == R.id.nav_history) {
            viewPager.setCurrentItem(2);

        } else if (id == R.id.nav_discord) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/GqX6SzK"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Tabs adapter
    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FriendsFragment();
                case 1:
                    return new TrackerSetupFragment();
                case 2:
                    return new HistoryFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
           /* switch (position){
                //
                //Your tab titles
                //
                case 0:return "Profile";
                case 1:return "Search";
                case 2: return "Contacts";
                default:
                */return null;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imageHelper.getChanged(session.getUser_id()+"")) {
            setImgUser();
        }
    }

    //Dialog when pressed back
    @Override
    public Dialog onCreateDialog(int id) {
        switch(id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Willst du gehen?");
                builder.setCancelable(true);
                builder.setPositiveButton("Ja!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
                builder.setNegativeButton("Nein!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Weiter gehts!",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onCreateDialog(id);
    }
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if(KeyCode==KeyEvent.KEYCODE_BACK) {
            showDialog(1);
            return true;
        }
        return super.onKeyDown(KeyCode,event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
