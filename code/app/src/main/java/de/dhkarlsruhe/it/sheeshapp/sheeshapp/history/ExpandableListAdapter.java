package de.dhkarlsruhe.it.sheeshapp.sheeshapp.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;

/**
 * Created by Informatik on 29.11.2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }



    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_statistic, null);
        }

        TextView tvData = convertView.findViewById(R.id.stTvChildData);
        TextView tvInfo = convertView.findViewById(R.id.stTvChildInfo);
        ImageView imgIcon = convertView.findViewById(R.id.imgChildIcon);
        tvData.setText(childText);
        switch (childPosition) {
            case 0:
                imgIcon.setImageResource(R.drawable.icon_location);
                tvInfo.setText("Location");
                break;
            case 1:
                imgIcon.setImageResource(R.drawable.icon_friends);
                tvInfo.setText("Friends");
                tvData.setText("");
                String[] friends = childText.split(";");
                for(int i = 0; i < friends.length; i++) {
                    if(i==friends.length-1) {
                        tvData.append(friends[i]+".");
                    } else {
                        tvData.append(friends[i]+", ");
                    }
                }
                break;
            case 2:
                imgIcon.setImageResource(R.drawable.icon_comment);
                tvInfo.setText("Comment");
                break;
            case 3:
                imgIcon.setImageResource(R.drawable.icon_stopwatch);
                tvInfo.setText("Duration");
                break;
            case 4:
                imgIcon.setImageResource(R.drawable.icon_number);
                tvInfo.setText("Number of Shishas");
                break;
            default:
                imgIcon.setImageResource(R.drawable.error_1);
                tvInfo.setText("Error");
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header_statistic, null);
        }
        CheckedTextView tvHeader = convertView.findViewById(R.id.stTvHeader);
        tvHeader.setText(headerTitle);
        ImageView imgArrow = convertView.findViewById(R.id.imgHeaderStatistic);
        if (isExpanded) {
            convertView.setBackground(context.getResources().getDrawable(R.drawable.header_active_colors));
            imgArrow.setImageResource(R.drawable.down_arrow);
        } else {
            convertView.setBackground(context.getResources().getDrawable(R.drawable.toolbar_colors));
            imgArrow.setImageResource(R.drawable.up_arrow);
        }
        return convertView;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}



