package com.ssf.homevisit.rmncha.anc.pwtracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAANCVisitLogResponse;

import java.util.List;

public class ANCPWServiceAdapter extends RecyclerView.Adapter<ANCPWServiceAdapter.ANCPWServiceRow> {

    private Context context;
    private List<RMNCHAANCVisitLogResponse> historyList;

    public ANCPWServiceAdapter(Context mContext, List<RMNCHAANCVisitLogResponse> historyList) {
        this.context = mContext;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ANCPWServiceRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_rmncha_anc_pw_service_row, parent, false);
        return new ANCPWServiceAdapter.ANCPWServiceRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ANCPWServiceRow holder, int position) {
        try {
            RMNCHAANCVisitLogResponse history = historyList.get(position);
            holder.visitNumber.setText((position + 1) + "");
            holder.period.setText("ANC " + (position + 1));

            String ancDate = history.getVisitDate();
            String dates[] = ancDate.split("T");
            if (dates.length > 0) {
                holder.date.setText(dates[0].replace("-", "/"));
            } else {
                holder.date.setText(ancDate);
            }

            holder.weeks.setText(history.getWeeksOfPregnancy() + "");
            holder.ttDose.setText(history.getTtDose());
            holder.ttDate.setText(history.getTtDoseTakenDate());
            holder.faGiven.setText(history.getNoFATabletsGiven() + "");
            holder.ifaGiven.setText(history.getNoIFATabletsGiven() + "");
            if (history.isHasAborted()) {
                holder.abortion.setText("Yes");
            } else {
                holder.abortion.setText("No");
            }
            holder.abortionDate.setText("-");
            if (history.isHasMaternalDealth()) {
                holder.death.setText("Yes");
            } else {
                holder.death.setText("No");
            }
            holder.deathDate.setText("-");
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

    class ANCPWServiceRow extends RecyclerView.ViewHolder {

        private final TextView visitNumber;
        private final TextView period;
        private final TextView date;
        private final TextView weeks;
        private final TextView ttDose;
        private final TextView ttDate;
        private final TextView faGiven;
        private final TextView ifaGiven;
        private final TextView abortion;
        private final TextView abortionDate;
        private final TextView death;
        private final TextView deathDate;
        private final TextView ashaName;
        private final TextView covidResult;

        public ANCPWServiceRow(@NonNull View itemView) {
            super(itemView);
            visitNumber = itemView.findViewById(R.id.anc_service_visit_no);
            period = itemView.findViewById(R.id.anc_service_anc_period);
            date = itemView.findViewById(R.id.anc_service_anc_date);
            weeks = itemView.findViewById(R.id.anc_service_anc_weeks);
            ttDose = itemView.findViewById(R.id.anc_service_anc_tt_dose);
            ttDate = itemView.findViewById(R.id.anc_service_anc_tt_dose_date);
            faGiven = itemView.findViewById(R.id.anc_service_anc_fa_given);
            ifaGiven = itemView.findViewById(R.id.anc_service_anc_ifa_given);
            abortion = itemView.findViewById(R.id.anc_service_anc_abortion);
            abortionDate = itemView.findViewById(R.id.anc_service_anc_abortion_date);
            death = itemView.findViewById(R.id.anc_service_anc_maternal_death);
            deathDate = itemView.findViewById(R.id.anc_service_anc_maternal_death_date);
            ashaName = itemView.findViewById(R.id.anc_service_anc_asha_name);
            covidResult = itemView.findViewById(R.id.anc_service_anc_covid_result);
        }
    }

}
