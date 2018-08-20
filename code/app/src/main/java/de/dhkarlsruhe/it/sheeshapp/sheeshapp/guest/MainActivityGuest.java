package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.FriendsFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SettingsActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.TimeTrackerActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.TrackerSetupFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.history.HistoryFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.DelayAutoCompleteTextView;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.MyProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

public class MainActivityGuest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private TextView tvUsername, tvEmail;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View header;
    private TabLayout tabLayout;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfaHelper;
    private RapidFloatingActionContentLabelList rfaContent;
    private Guest guest;
    private ImageView imgUser;
    private ImageHelper imageHelper;
    private String actualTab;
    private Window window;
    private final static String AD_BANNER_ID = "ca-app-pub-4355529827581242/7220321532";
    private final static String AD_BANNER_ID_TEST = "ca-app-pub-3940256099942544/1033173712";
    private InterstitialAd ad;
    private FriendsFragmentGuest friendsFragment;
    private HistoryFragmentGuest historyFragment;
    private TrackerSetupFragmentGuest trackerSetupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        setContentView(R.layout.activity_main);
        friendsFragment = new FriendsFragmentGuest();
        historyFragment = new HistoryFragmentGuest();
        trackerSetupFragment = new TrackerSetupFragmentGuest();
        guest = new Guest(this);
        initStart();

        MobileAds.initialize(this, MyUtilities.AD_APP_ID);
        ad = new InterstitialAd(this);
        ad.setAdUnitId(AD_BANNER_ID_TEST);
        ad.loadAd(new AdRequest.Builder().build());
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent = new Intent(MainActivityGuest.this, TimeTrackerActivityGuest.class);
                startActivity(intent);
                ad.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void initStart() {
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
        toolbar.setTitle(getString(R.string.friends_text));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        changeFabToAddFriend();
                        toolbar.setTitle(getString(R.string.friends_text));
                        break;
                    case 1:
                        changeFabToSetup();
                        toolbar.setTitle(R.string.lets_sheesh_text);
                        break;
                    case 2:
                        changeFabToStatistics();
                        toolbar.setTitle(getString(R.string.history_text));
                        break;
                }
                super.onTabSelected(tab);
            }
        });

        imageHelper = new ImageHelper(this);

        initRfa();

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

        tvUsername.setText(guest.getName());
        tvEmail.setText(guest.getEmail());

        imgUser = header.findViewById(R.id.imgHeader);
        imageHelper.setRoundImageDefault(imgUser);
        // setImgUser();
    }

    private void initRfa() {
        rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        rfaLayout = findViewById(R.id.rfabLayoutMain);
        rfaButton = findViewById(R.id.rfabButtonMain);
    }

    public void changeFabToAddFriend() {
        actualTab = "friend";
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.add_friend_text))
                .setResId(R.mipmap.icon_plus_white)
                .setIconNormalColor(getResources().getColor(R.color.firstIconNormal))
                .setIconPressedColor(getResources().getColor(R.color.firstIconPressed))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.plan_session_text))
                .setResId(R.drawable.icon_comment_white)
                .setIconNormalColor(getResources().getColor(R.color.secondIconNormal))
                .setIconPressedColor(getResources().getColor(R.color.secondIconPressed))
                .setLabelSizeSp(14)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfaHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
        rfaButton.setVisibility(View.VISIBLE);

    }

    public void changeFabToSetup() {
        actualTab = "setup";

        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.start_tracker_text))
                .setResId(R.mipmap.icon_setup_white)
                .setIconNormalColor(getResources().getColor(R.color.firstIconNormal))
                .setIconPressedColor(getResources().getColor(R.color.firstIconPressed))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.cancel_text))
                .setResId(R.drawable.icon_cancel_white)
                .setIconNormalColor(getResources().getColor(R.color.secondIconNormal))
                .setIconPressedColor(getResources().getColor(R.color.secondIconPressed))
                .setLabelSizeSp(14)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfaHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
        rfaButton.setVisibility(View.VISIBLE);
    }

    public void changeFabToStatistics() {
        actualTab = "history";
        rfaButton.setVisibility(View.GONE);
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
            Toast.makeText(getApplicationContext(),getString(R.string.not_available_for_guest),Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if (actualTab.equals("friend")) {
            if (position == 0) {
                Toast.makeText(getApplicationContext(),getString(R.string.not_available_for_guest),Toast.LENGTH_LONG).show();

            } else if (position == 1) {
                Toast.makeText(getApplicationContext(),getString(R.string.not_available_for_guest),Toast.LENGTH_LONG).show();
            }
        } else if (actualTab.equals("setup")) {
            if (position == 0) {
                if (TrackerSetupFragmentGuest.runShisha(getApplicationContext())) {
                    if (ad.isLoaded()) {
                        ad.show();
                    } else {
                        Intent intent = new Intent(MainActivityGuest.this, TimeTrackerActivityGuest.class);
                        startActivity(intent);
                        ad.loadAd(new AdRequest.Builder().build());
                    }
                }
            } else if (position == 1) {
                Toast.makeText(this, R.string.canceled_text, Toast.LENGTH_SHORT).show();
            }
        }
        rfaHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (actualTab.equals("friend")) {
            if (position == 0) {
                Toast.makeText(getApplicationContext(),getString(R.string.not_available_for_guest),Toast.LENGTH_LONG).show();
            } else if (position == 1) {
                Toast.makeText(getApplicationContext(),getString(R.string.not_available_for_guest),Toast.LENGTH_LONG).show();
            }
            rfaHelper.toggleContent();
        } else if (actualTab.equals("setup")) {
            if (position == 0) {
                if (TrackerSetupFragmentGuest.runShisha(getApplicationContext())) {
                    if (ad.isLoaded()) {
                        ad.show();
                    } else {
                        Intent intent = new Intent(MainActivityGuest.this, TimeTrackerActivityGuest.class);
                        startActivity(intent);
                        ad.loadAd(new AdRequest.Builder().build());
                    }
                }
            } else if (position == 1) {
                Toast.makeText(this, getString(R.string.canceled_text), Toast.LENGTH_SHORT).show();
            }
            rfaHelper.toggleContent();
        }
    }


    //Tabs adapter
    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return friendsFragment;
                case 1:
                    return trackerSetupFragment;
                case 2:
                    return historyFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return null;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK) {
            MyUtilities.openLeaveDialog(this);
            return true;
        }
        return super.onKeyDown(KeyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
