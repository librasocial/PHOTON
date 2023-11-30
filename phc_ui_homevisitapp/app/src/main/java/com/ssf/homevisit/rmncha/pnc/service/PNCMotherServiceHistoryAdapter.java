package com.ssf.homevisit.rmncha.pnc.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAPNCMotherVisitLogResponse;

import java.util.List;

public class PNCMotherServiceHistoryAdapter extends RecyclerView.Adapter<PNCMotherServiceHistoryAdapter.PNCPWServiceRow> {

    private Context context;
    private List<RMNCHAPNCMotherVisitLogResponse> historyList;

    public PNCMotherServiceHistoryAdapter(Context mContext, List<RMNCHAPNCMotherVisitLogResponse> historyList) {
        this.context = mContext;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public PNCPWServiceRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_rmncha_pnc_mother_service_row, parent, false);
        return new PNCMotherServiceHistoryAdapter.PNCPWServiceRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PNCPWServiceRow holder, int position) {
        try {
            RMNCHAPNCMotherVisitLogResponse history = historyList.get(position);
            holder.period.setText(history.getPncPeriod());
            holder.date.setText(history.getPncDate());
            holder.danger.setText(history.getSignOfMotherDanger());
            holder.ppcMethod.setText(history.getPpcMethod());
            holder.motherDeath.setText(history.getSignOfMotherDanger());
            holder.ashaName.setText(history.getAudit().getCreatedBy());

            if (history.isCovidResultPositive()) {
                holder.covidResult.setText("Positive");
            } else {
                holder.covidResult.setText("Negative");
            }
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

    class PNCPWServiceRow extends RecyclerView.ViewHolder {

        private final TextView period;
        private final TextView date;
        private final TextView danger;
        private final TextView ppcMethod;
        private final TextView motherDeath;
        private final TextView ashaName;
        private final TextView covidResult;

        public PNCPWServiceRow(@NonNull View itemView) {
            super(itemView);
            period = itemView.findViewById(R.id.pnc_mother_service_period);
            date = itemView.findViewById(R.id.pnc_mother_service_date);
            danger = itemView.findViewById(R.id.pnc_mother_service_danger_signs);
            ppcMethod = itemView.findViewById(R.id.pnc_mother_service_ppc_method);
            motherDeath = itemView.findViewById(R.id.pnc_mother_service_death);
            ashaName = itemView.findViewById(R.id.pnc_mother_service_asha_name);
            covidResult = itemView.findViewById(R.id.pnc_mother_service_covid_result);
        }
    }

}
