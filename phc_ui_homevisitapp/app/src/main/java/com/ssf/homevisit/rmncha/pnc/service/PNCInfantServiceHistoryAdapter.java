package com.ssf.homevisit.rmncha.pnc.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAPNCInfantVisitLogResponse;

import java.util.List;

public class PNCInfantServiceHistoryAdapter extends RecyclerView.Adapter<PNCInfantServiceHistoryAdapter.PNCInfantServiceRow> {

    private Context context;
    private List<RMNCHAPNCInfantVisitLogResponse> historyList;

    public PNCInfantServiceHistoryAdapter(Context mContext, List<RMNCHAPNCInfantVisitLogResponse> historyList) {
        this.context = mContext;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public PNCInfantServiceRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_rmncha_pnc_child_service_row, parent, false);
        return new PNCInfantServiceHistoryAdapter.PNCInfantServiceRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PNCInfantServiceRow holder, int position) {
        try {
            RMNCHAPNCInfantVisitLogResponse history = historyList.get(position);
            holder.period.setText(history.getPncPeriod());
            holder.date.setText(history.getPncDate());
            holder.danger.setText(history.getSignOfInfantDanger());
            holder.weight.setText(history.getWeight());
            holder.death.setText(history.isHasInfantDeath() + "");
            holder.deathDate.setText("-");
            holder.ashaName.setText(history.getAudit().getCreatedBy());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (historyList != null) {
            return historyList.size();
        } else {
            return 0;
        }
    }

    class PNCInfantServiceRow extends RecyclerView.ViewHolder {

        private final TextView period;
        private final TextView date;
        private final TextView danger;
        private final TextView weight;
        private final TextView death;
        private final TextView deathDate;
        private final TextView ashaName;

        public PNCInfantServiceRow(@NonNull View itemView) {
            super(itemView);
            period = itemView.findViewById(R.id.pnc_child_service_period);
            date = itemView.findViewById(R.id.pnc_child_service_date);
            danger = itemView.findViewById(R.id.pnc_child_service_danger_signs);
            weight = itemView.findViewById(R.id.pnc_child_service_weight);
            death = itemView.findViewById(R.id.pnc_child_death);
            deathDate = itemView.findViewById(R.id.pnc_child_death_date);
            ashaName = itemView.findViewById(R.id.pnc_child_service_asha_name);
        }
    }

}
