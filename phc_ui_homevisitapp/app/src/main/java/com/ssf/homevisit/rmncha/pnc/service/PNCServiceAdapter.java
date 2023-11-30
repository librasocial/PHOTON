package com.ssf.homevisit.rmncha.pnc.service;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getBabyAgeInWeeks;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
import com.ssf.homevisit.rmncha.base.RMNCHAUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PNCServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int itemsCount = 3;
    private final Context context;
    private PNCServiceItemListener listener;
    private List<RMNCHAPNCInfantResponse.Infant> babiesList;
    private final RMNCHAPNCWomenResponse.Content selectedWomen;
    private boolean isItemNotSelected = true;
    private int babiesCount = 0;


    public PNCServiceAdapter(Context mContext, List<RMNCHAPNCInfantResponse.Infant> babiesList,
                             RMNCHAPNCWomenResponse.Content selectedWomen,
                             PNCServiceItemListener listener) {
        this.context = mContext;
        this.listener = listener;
        this.selectedWomen = selectedWomen;
        this.babiesList = babiesList;
        if (babiesList != null) {
            babiesCount = babiesList.size();
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rmncha_hh_member_row, parent, false);
            return new WomenViewHolder(view);
        } else if (viewType == (babiesCount + 1)
                || viewType == (babiesCount + 2)) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rmncha_pnc_service_textview, parent, false);
            return new TextViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rmncha_baby_card, parent, false);
            return new BabyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (position == 0) {
                WomenViewHolder womenViewHolder = (WomenViewHolder) holder;
                setMotherView(womenViewHolder, selectedWomen);
            } else if (position == (babiesCount + 1)) {
                TextViewHolder tvHolder = (TextViewHolder) holder;
                tvHolder.titleTV.setText(context.getResources().getString(R.string.text_pnc_view_delivery_outcomes));
                tvHolder.titleTV.setOnClickListener(view -> listener.onDeliveryOutcomesClicked());
            } else if (position == (babiesCount + 2)) {
                TextViewHolder tvHolder = (TextViewHolder) holder;
                tvHolder.titleTV.setText(context.getResources().getString(R.string.text_pnc_view_infant_details));
                tvHolder.titleTV.setOnClickListener(view -> listener.onInfantDetailsClicked());
            } else {
                BabyViewHolder babyViewHolder = (BabyViewHolder) holder;
                setBabyView(babyViewHolder, position - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemsCount + babiesCount;
    }

    static class WomenViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout motherRootView;
        private final TextView motherName;
        private final TextView motherPhone;
        private final TextView motherAgeAndGender;
        private final TextView motherDOB;
        private final TextView motherResidentStatus;
        private final TextView motherHealthID;
        private final TextView motherRCHId;
        private final ImageView motherImage;

        public WomenViewHolder(@NonNull View view) {
            super(view);
            motherRootView = view.findViewById(R.id.root_view);
            motherName = view.findViewById(R.id.member_name);
            motherPhone = view.findViewById(R.id.member_phone);
            motherAgeAndGender = view.findViewById(R.id.member_age_and_gender);
            motherDOB = view.findViewById(R.id.member_dob);
            motherResidentStatus = view.findViewById(R.id.member_resident_status);
            motherHealthID = view.findViewById(R.id.health_id_tv);
            motherRCHId = view.findViewById(R.id.rch_id_tv);
            motherImage = view.findViewById(R.id.member_image);
        }
    }

    static class BabyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout babyRootLayout;
        private final TextView babyName;
        private final TextView babyGender;
        private final TextView babyAge;
        private final TextView babyWeight;
        private final TextView babyDOB;
        private final TextView babyChildId;

        public BabyViewHolder(@NonNull View itemView) {
            super(itemView);
            babyRootLayout = itemView.findViewById(R.id.baby_root_view);
            babyName = itemView.findViewById(R.id.baby_name);
            babyGender = itemView.findViewById(R.id.baby_gender);
            babyAge = itemView.findViewById(R.id.baby_age);
            babyWeight = itemView.findViewById(R.id.baby_weight);
            babyDOB = itemView.findViewById(R.id.baby_dob);
            babyChildId = itemView.findViewById(R.id.baby_child_id);
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTV;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.text_view);
        }
    }

    public void setMotherView(WomenViewHolder womenViewHolder, RMNCHAPNCWomenResponse.Content memberData) {
        if (womenViewHolder != null && memberData != null) {
            RMNCHAPNCWomenResponse.Content.Source.SourceProperties sourceProperties = memberData.getSource().getProperties();


            String name = setNonNullValue(sourceProperties.getFirstName());
            String phone = "Ph : " + setNonNullValue(sourceProperties.getContact());
            String ageAndGender = setNonNullValue(sourceProperties.getAge().split("\\.")[0] + "") + "years - " + setNonNullValue(sourceProperties.getGender());
            String dob = "DOB : " + setNonNullValue(sourceProperties.getDateOfBirth());
            String residentStatus = "Status : " + getResidentStatus(sourceProperties.getResidingInVillage());
            String healthID = "Health ID number : " + setNonNullValue(sourceProperties.getHealthID());
            List<String> urlsList = sourceProperties.getImageUrls();
            String url = "";
            if (urlsList != null && urlsList.size() > 0) {
                url = urlsList.get(0);
            }
            loadMemberImage(url, womenViewHolder.motherImage);
            womenViewHolder.motherRCHId.setVisibility(View.GONE);
            womenViewHolder.motherName.setText(name);
            womenViewHolder.motherPhone.setText(phone);
            womenViewHolder.motherAgeAndGender.setText(ageAndGender);
            womenViewHolder.motherDOB.setText(dob);
            womenViewHolder.motherResidentStatus.setText(residentStatus);
            womenViewHolder.motherHealthID.setText(healthID);
        }
        womenViewHolder.motherRootView.setOnClickListener(view -> {
            if (isItemNotSelected) {
                isItemNotSelected = false;
                view.setBackgroundColor(context.getResources().getColor(R.color.rmncha_bg_selected_green));
                listener.onMotherItemClicked();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setBabyView(BabyViewHolder babyViewHolder, int position) {
        RMNCHAPNCInfantResponse.Infant memberData = babiesList.get(position);
        if (babyViewHolder != null && memberData != null) {
            String gender = setNonNullValue(memberData.getGender());
            Double age = RMNCHAUtils.getBabyAgeFromDOB(memberData.getDateOfBirth());
            String weight = setNonNullValue(memberData.getBirthWeight() + "");
            String dob = "DOB : " + setNonNullValue(memberData.getDateOfBirth());
            String childID = "Child ID : " + setNonNullValue(memberData.getChildId() + "");
            babyViewHolder.babyName.setText("Baby " + (position + 1));
            babyViewHolder.babyGender.setText(gender);
            babyViewHolder.babyAge.setText(getBabyAgeInWeeks(age));
            babyViewHolder.babyWeight.setText(weight);
            babyViewHolder.babyDOB.setText(dob);
            babyViewHolder.babyChildId.setText(childID);

        }
        babyViewHolder.babyRootLayout.setOnClickListener(view -> {
            if (isItemNotSelected) {
                isItemNotSelected = false;
                view.setBackgroundColor(context.getResources().getColor(R.color.rmncha_bg_selected_green));
                listener.onBabyItemClicked(position+1);
            }
        });
    }

    public interface PNCServiceItemListener {
        void onMotherItemClicked();

        void onBabyItemClicked(int position);

        void onDeliveryOutcomesClicked();

        void onInfantDetailsClicked();
    }

}
