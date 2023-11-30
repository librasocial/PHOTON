package com.ssf.homevisit.rmncha.ec.mapping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAHouseHoldSearchResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EMMappingAdapter extends RecyclerView.Adapter<EMMappingAdapter.RMNCHAHHMemberRow> {

    private Context context;
    private boolean searchTypeFlag;
    private RMNCHAHHMemberAdapterListener listener;
    private List<RMNCHAHouseHoldResponse.Content> contentList;
    private List<RMNCHAHouseHoldSearchResponse.Content> searchContentList;

    public EMMappingAdapter(Context mContext, List<RMNCHAHouseHoldResponse.Content> contentList, RMNCHAHHMemberAdapterListener listener) {
        this.context = mContext;
        this.listener = listener;
        this.searchTypeFlag = false;
        this.contentList = contentList;
    }

    public EMMappingAdapter(Context mContext, List<RMNCHAHouseHoldSearchResponse.Content> searchContentList, boolean searchTypeFlag, RMNCHAHHMemberAdapterListener listener) {
        this.context = mContext;
        this.listener = listener;
        this.searchTypeFlag = true;
        this.searchContentList = searchContentList;
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
        String number;
        String head;
        String count;
        if (searchTypeFlag) {
            RMNCHAHouseHoldSearchResponse.Content.Properties properties = searchContentList.get(position).getProperties();
            number = properties.getHouseID();
            head = properties.getHouseHeadName();
            count = properties.getTotalFamilyMembers() + "";
        } else {
            RMNCHAHouseHoldResponse.Content.Target.TargetProperties targetProperties = contentList.get(position).getTarget().getProperties();
            number = targetProperties.getHouseID();
            head = targetProperties.getHouseHeadName();
            count = targetProperties.getTotalFamilyMembers();
        }
        holder.hhNumberTV.setText(number);
        holder.hhHeadTV.setText(head);
        holder.hhCountTV.setText(count);
        holder.hhActionTV.setTag(position + "");
        holder.hhActionTV.setOnClickListener(view -> {
            listener.onECHouseholdClicked(Integer.parseInt(view.getTag().toString()), searchTypeFlag);
        });
    }

    @Override
    public int getItemCount() {
        if (searchTypeFlag && searchContentList != null) {
            return searchContentList.size();
        } else if (contentList != null) {
            return contentList.size();
        } else {
            return 0;
        }
    }

    class RMNCHAHHMemberRow extends RecyclerView.ViewHolder {

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

    public interface RMNCHAHHMemberAdapterListener {
        void onECHouseholdClicked(int position, boolean searchTypeFlag);
    }

}
