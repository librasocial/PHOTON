package com.ssf.homevisit.rmncha.ec.selectwomen;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;

import java.util.List;

public class ECWomenAdapter extends BaseAdapter {

    Context context;
    ECWomenClickListener listener;
    List<RMNCHAMembersInHouseHoldResponse.Content> womenMemberList;

    public ECWomenAdapter(Context context, List<RMNCHAMembersInHouseHoldResponse.Content> womenMemberList, ECWomenClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.womenMemberList = womenMemberList;
    }

    @Override
    public int getCount() {
        if (womenMemberList != null)
            return womenMemberList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return womenMemberList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_rmncha_hh_member_row, null);
        }
        if (position < womenMemberList.size()) {
            RMNCHAMembersInHouseHoldResponse.Content memberData = womenMemberList.get(position);
            if (memberData != null) {
                setHouseHoldMemberView(view, memberData);
                view.setTag(position + "");
                view.setOnClickListener(view1 -> listener.onECWomenClicked(Integer.parseInt(view1.getTag().toString())));
            }
        }
        return view;
    }

    public static void setHouseHoldMemberView(View view, RMNCHAMembersInHouseHoldResponse.Content memberData) {
        if (view != null && memberData != null) {
            String name = setNonNullValue(memberData.getTargetNode().getProperties().getFirstName());
            String phone = "Ph : " + setNonNullValue(memberData.getTargetNode().getProperties().getContact());
            String ageAndGender = setNonNullValue(memberData.getTargetNode().getProperties().getAge() + "") + "years - " + setNonNullValue(memberData.getTargetNode().getProperties().getGender());
            String dob = "DOB : " + setNonNullValue(memberData.getTargetNode().getProperties().getDateOfBirth());
            String residentStatus = "Status : " + getResidentStatus(memberData.getTargetNode().getProperties().getResidingInVillage());
            String healthID = "Health ID number : " + setNonNullValue(memberData.getTargetNode().getProperties().getHealthID());
            List<String> urlsList = memberData.getTargetNode().getProperties().getImageUrls();
            String url = "";
            if (urlsList != null && urlsList.size() > 0) url = urlsList.get(0);
            String rchID = memberData.getTargetNode().getProperties().getRchId();
            RMNCHAServiceStatus serviceType = memberData.getTargetNode().getProperties().getRMNCHAServiceStatus();

            ((TextView) view.findViewById(R.id.member_name)).setText(name);
            ((TextView) view.findViewById(R.id.member_phone)).setText(phone);
            ((TextView) view.findViewById(R.id.member_age_and_gender)).setText(ageAndGender);
            ((TextView) view.findViewById(R.id.member_dob)).setText(dob);
            ((TextView) view.findViewById(R.id.member_resident_status)).setText(residentStatus);
            ((TextView) view.findViewById(R.id.health_id_tv)).setText(healthID);
            loadMemberImage(url, (ImageView) view.findViewById(R.id.member_image));

            TextView rchIDTextView = (TextView) view.findViewById(R.id.rch_id_tv);
            rchIDTextView.setVisibility(View.GONE);
            if (rchID != null && !rchID.isEmpty() && serviceType != null) {
                rchIDTextView.setVisibility(View.VISIBLE);
                switch (serviceType) {
                    case PC_ONGOING:
                    case PCOngoing:
                        rchIDTextView.setText("PC Service : Ongoing");
                        rchIDTextView.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_rounder_corner_blue));
                        rchIDTextView.setCompoundDrawablesWithIntrinsicBounds(view.getContext().getResources().getDrawable(R.drawable.ic_service), null, null, null);
                        rchIDTextView.setPadding(35, 0, 15, 0);
                        break;
                    case BC_ONGOING:
                    case BCOngoing:
                        rchIDTextView.setText("BC Service : Ongoing");
                        rchIDTextView.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_rounder_corner_blue));
                        rchIDTextView.setCompoundDrawablesWithIntrinsicBounds(view.getContext().getResources().getDrawable(R.drawable.ic_service), null, null, null);
                        rchIDTextView.setPadding(35, 0, 15, 0);
                        break;
                    case ECRegistered:
                    case EC_REGISTERED:
                        rchIDTextView.setText("RCH - ID : " + rchID);
                        rchIDTextView.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_rounder_corner_green));
                        rchIDTextView.setCompoundDrawablesWithIntrinsicBounds(view.getContext().getResources().getDrawable(R.drawable.ic_tick_white), null, null, null);
                        rchIDTextView.setPadding(15, 0, 15, 0);
                        break;
                    case NEW:
                    default:
                        rchIDTextView.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    public interface ECWomenClickListener {
        void onECWomenClicked(int position);
    }

}