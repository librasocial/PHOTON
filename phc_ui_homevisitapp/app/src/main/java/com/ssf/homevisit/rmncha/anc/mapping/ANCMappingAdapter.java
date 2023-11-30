package com.ssf.homevisit.rmncha.anc.mapping;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAANCHouseholdsResponse;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ANCMappingAdapter extends RecyclerView.Adapter<ANCMappingAdapter.RMNCHAHHMemberRow> {

    private final Context context;
    private final boolean flagAllList;
    private final ANCMappingItemListener listener;
    private final List<RMNCHAANCHouseholdsResponse.Content> houseHoldsList;
    private final List<RMNCHAANCPWsResponse.Content> pwList;

    public ANCMappingAdapter(Context mContext, List<RMNCHAANCHouseholdsResponse.Content> houseHoldsList,
                             List<RMNCHAANCPWsResponse.Content> pwList,
                             ANCMappingItemListener listener, boolean flagAllList) {
        this.context = mContext;
        this.listener = listener;
        this.flagAllList = flagAllList;
        this.pwList = pwList;
        this.houseHoldsList = houseHoldsList;
    }

    @NotNull
    @Override
    public RMNCHAHHMemberRow onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rmncha_house_hold_row, parent, false);
        return new RMNCHAHHMemberRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMNCHAHHMemberRow holder, int position) {
        try {
            if (flagAllList) {
                RMNCHAANCHouseholdsResponse.Content.Source.SourceProperties sourceProperties = houseHoldsList.get(position).getSource().getProperties();
                holder.hhNumberTV.setText(setNonNullValue(sourceProperties.getHouseID()));
                holder.hhHeadTV.setText(setNonNullValue(sourceProperties.getHouseHeadName()));
                holder.hhCountTV.setVisibility(View.VISIBLE);
                holder.hhCountTV.setText(setNonNullValue(sourceProperties.getTotalFamilyMembers() + ""));
                setTextViewColourAndText(holder.hhActionTV, R.string.select_hh_for_anc, R.color.button_blue);
                holder.hhActionTV.setTag(position + "");
                holder.hhActionTV.setOnClickListener(view -> {
                    int pos = Integer.parseInt(view.getTag() + "");
                    listener.goToSelectWomenForANC(houseHoldsList.get(pos));
                });
            } else {
                holder.hhCountTV.setVisibility(View.GONE);
                RMNCHAANCPWsResponse.Content.Source.SourceProperties sourceProperties = pwList.get(position).getSource().getProperties();
                RMNCHAANCPWsResponse.Content.Target.TargetProperties targetProperties = pwList.get(position).getTarget().getProperties();
                holder.hhNumberTV.setText(setNonNullValue(targetProperties.getHouseID()));
                holder.hhHeadTV.setText(setNonNullValue(sourceProperties.getFirstName()));
                RMNCHAServiceStatus status = sourceProperties.getRmnchaServiceStatus();
                holder.hhActionTV.setTag(position + "");

                if (status == RMNCHAServiceStatus.ANC_ONGOING || status== RMNCHAServiceStatus.ANC_REGISTERED) {
                    setTextViewColourAndText(holder.hhActionTV, R.string.select_pw_for_anc_service, R.color.button_green);
                    holder.hhActionTV.setOnClickListener(view -> {
                        int pos = Integer.parseInt(view.getTag() + "");
                        listener.goToANCService(pwList.get(pos));
                    });
                } else {
                    setTextViewColourAndText(holder.hhActionTV, R.string.select_pw_for_anc, R.color.button_blue);
                    holder.hhActionTV.setOnClickListener(view -> {
                        int pos = Integer.parseInt(view.getTag() + "");
                        listener.goToANCRegistration(pwList.get(pos));
                    });
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (flagAllList && houseHoldsList != null) {
            return houseHoldsList.size();
        } else if (pwList != null) {
            return pwList.size();
        } else {
            return 0;
        }
    }

    private void setTextViewColourAndText(TextView hhActionTV, int textId, int colorID) {
        hhActionTV.setText(context.getResources().getString(textId));
        hhActionTV.setTextColor(context.getResources().getColor(colorID));
    }

    static class RMNCHAHHMemberRow extends RecyclerView.ViewHolder {

        private final TextView hhNumberTV;
        private final TextView hhHeadTV;
        private final TextView hhCountTV;
        private final TextView hhActionTV;

        public RMNCHAHHMemberRow(@NonNull View itemView) {
            super(itemView);
            hhNumberTV = itemView.findViewById(R.id.hh_number_tv);
            hhHeadTV = itemView.findViewById(R.id.hh_head_tv);
            hhCountTV = itemView.findViewById(R.id.hh_member_count_tv);
            hhActionTV = itemView.findViewById(R.id.hh_action_tv);
        }
    }

    public interface ANCMappingItemListener {

        void goToANCService(RMNCHAANCPWsResponse.Content content);

        void goToANCRegistration(RMNCHAANCPWsResponse.Content content);

        void goToSelectWomenForANC(RMNCHAANCHouseholdsResponse.Content content);
    }

}
