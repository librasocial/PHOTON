package com.ssf.homevisit.rmncha.anc.selectwomen;

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
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;

import java.util.List;

public class ANCWomenAdapter extends BaseAdapter {

    Context context;
    ANCWomenClickListener listener;
    List<RMNCHAANCPWsResponse.Content> womenMemberList;

    public ANCWomenAdapter(Context context, List<RMNCHAANCPWsResponse.Content> womenMemberList, ANCWomenClickListener listener) {
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
            RMNCHAANCPWsResponse.Content memberData = womenMemberList.get(position);
            if (memberData != null) {
                setANCWomenView(view, memberData);
                view.setTag(position + "");
                view.setOnClickListener(view1 -> listener.onANCWomenClicked(Integer.parseInt(view1.getTag().toString())));
            }
        }
        return view;
    }

    public static void setANCWomenView(View view, RMNCHAANCPWsResponse.Content memberData) {
        if (view != null && memberData != null) {
            String name = setNonNullValue(memberData.getSource().getProperties().getFirstName());
            String phone = "Ph : " + setNonNullValue(memberData.getSource().getProperties().getContact());
            String ageAndGender = setNonNullValue(memberData.getSource().getProperties().getAge().split("\\.")[0] + "") + "years - " + setNonNullValue(memberData.getSource().getProperties().getGender());
            String dob = "DOB : " + setNonNullValue(memberData.getSource().getProperties().getDateOfBirth());
            String residentStatus = "Status : " + getResidentStatus(memberData.getSource().getProperties().getResidingInVillage());
            String healthID = "Health ID number : " + setNonNullValue(memberData.getSource().getProperties().getHealthID());
            List<String> urlsList = memberData.getSource().getProperties().getImageUrls();
            String url = "";
            if (urlsList != null && urlsList.size() > 0) url = urlsList.get(0);
            String rchID = memberData.getSource().getProperties().getRchId();
            RMNCHAServiceStatus serviceType = memberData.getSource().getProperties().getRmnchaServiceStatus();

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
                    case ANC_ONGOING:
                        rchIDTextView.setText("ANC Service : Ongoing");
                        rchIDTextView.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_rounder_corner_blue));
                        rchIDTextView.setCompoundDrawablesWithIntrinsicBounds(view.getContext().getResources().getDrawable(R.drawable.ic_service), null, null, null);
                        rchIDTextView.setPadding(35, 0, 15, 0);
                        break;
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
                    case ANC_REGISTERED:
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

    public interface ANCWomenClickListener {
        void onANCWomenClicked(int position);
    }

}
