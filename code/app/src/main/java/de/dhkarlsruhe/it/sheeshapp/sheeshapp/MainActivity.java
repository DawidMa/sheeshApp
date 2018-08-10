package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.history.HistoryFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.MyProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

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
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfaHelper;
    private RapidFloatingActionContentLabelList rfaContent;
    private UserSessionObject session;
    private ImageView imgUser;
    private LinearLayout linearLayout;
    private ImageHelper imageHelper;
    private String actualTab;

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

        tvUsername.setText(session.getName());
        tvEmail.setText(session.getEmail());

        imgUser = header.findViewById(R.id.imgHeader);
        decideProfileImage(imgUser);
       // setImgUser();
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntent(v);
            }
        });

    }

    private void initRfa() {
            rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        rfaLayout = findViewById(R.id.rfabLayoutMain);
            rfaButton = findViewById(R.id.rfabButtonMain);

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
        startActivity(intent, options.toBundle());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            SharedPreferences pref = getSharedPreferences(SharedPrefConstants.HISTORY,MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(SharedPrefConstants.H_OFFLINE_JSON);
            editor.apply();
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if (actualTab.equals("friend")) {
            if (position==0) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
            } else if (position==1) {
                Toast.makeText(this, R.string.starting_session_text,Toast.LENGTH_SHORT).show();
            }
        } else if(actualTab.equals("setup")) {
            if (position==0) {
                if(TrackerSetupFragment.runShisha(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, TimeTrackerActivity.class);
                    startActivity(intent);
                }
            } else if (position==1) {
                Toast.makeText(this, R.string.canceled_text,Toast.LENGTH_SHORT).show();
            }
        }
        rfaHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (actualTab.equals("friend")) {
            if (position==0) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
            } else if (position==1) {
                Toast.makeText(this,getString(R.string.starting_session_text),Toast.LENGTH_SHORT).show();
            }
            rfaHelper.toggleContent();
        } else if(actualTab.equals("setup")) {
            if (position==0) {
                if(TrackerSetupFragment.runShisha(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, TimeTrackerActivity.class);
                    startActivity(intent);
                }
            } else if (position==1) {
                Toast.makeText(this,getString(R.string.canceled_text),Toast.LENGTH_SHORT).show();
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
            imageHelper.setChanged(false,session.getUser_id()+"");
        }
    }

    //Dialog when pressed back
    @Override
    public Dialog onCreateDialog(int id) {
        switch(id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.leave_question);
                builder.setCancelable(true);
                builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
                builder.setNegativeButton(getString(R.string.no_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),getString(R.string.sheeeesh_text),Toast.LENGTH_SHORT).show();
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

    private void decideProfileImage(ImageView imgFriends) {
        long userid = session.getUser_id();
        String imageId = session.getLast_changed_icon_id();
        if (session.isHas_icon()) {
            //prepareProgressDialog();
            if (imageHelper.getIconId(userid).equals(imageId)) {
                Bitmap bitmap = imageHelper.loadImageFromStorage(userid+"");
                if (bitmap!=null) {
                    imageHelper.setRoundImageWithBitmap(imgFriends,bitmap);
                } else {
                    imageHelper.loadFileFromServer(userid,imgFriends,imageId);
                }
            } else {
                imageHelper.loadFileFromServer(userid,imgFriends,imageId);
                imageHelper.setNewIconId(userid,imageId);
               // session.setLast_changed_icon_id(imageHelper.getIconId(userid));
            }
        } else {
            imageHelper.setRoundImageDefault(imgFriends);
        }
    }

}
