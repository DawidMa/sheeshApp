package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.DelayAutoCompleteTextView;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.FriendAutoCompleteAdapter;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.UserSearchObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by User on 23.11.2017.
 */
public class AddFriendActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{

    private TextView tvTitle;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfaHelper;
    private DelayAutoCompleteTextView autoCompleteTextView;
    private Friend friend;
    private String emailOfFriend;
    private String nameOfFriend;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRfa();

        friend = new Friend(this);
        setTitle(getString(R.string.add_friend_text));
        init();
    }

    private void initRfa() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaLayout = findViewById(R.id.rfabLayoutAddFriend);
        rfaButton = findViewById(R.id.rfabButtonAddFriend);

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.cancel_text))
                .setResId(R.drawable.icon_cancel_white)
                .setIconNormalColor(getResources().getColor(R.color.firstIconNormal))
                .setIconPressedColor(getResources().getColor(R.color.firstIconPressed))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.add_text))
                .setResId(R.mipmap.icon_plus_white)
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
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(android.R.drawable.ic_menu_search);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.auto_et_with_icons, null);
        actionBar.setCustomView(v);
       // tvTitle = findViewById(R.id.addTvTitle);
        autoCompleteTextView = findViewById(R.id.autoAddName);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(new FriendAutoCompleteAdapter(this)); // 'this' is Activity instance
        autoCompleteTextView.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserSearchObject user = (UserSearchObject) adapterView.getItemAtPosition(position);
                autoCompleteTextView.setText(user.getName());
                emailOfFriend = user.getEmail();
                nameOfFriend = user.getName();
            }
        });

        btnDelete = findViewById(R.id.btnAddDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        switch (position) {
            case 0:
                rfaHelper.toggleContent();
                break;
            case 1:
                friend.addFriend(emailOfFriend,nameOfFriend);
                rfaHelper.toggleContent();
                finish();
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch (position) {
            case 0:
                rfaHelper.toggleContent();
                break;
            case 1:
                friend.addFriend(emailOfFriend,nameOfFriend);
                rfaHelper.toggleContent();
                finish();
                break;
        }
    }
}
