package com.ssf.homevisit.rmncha.ec.ecservice;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateToView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAVisitLogsResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BCServiceVisitHistoryAdapter extends RecyclerView.Adapter<BCServiceVisitHistoryAdapter.RMNCHAHHVisitHistoryRow> {

    private Context context;
    private List<RMNCHAVisitLogsResponse.VisitLog> visitLogList;

    public BCServiceVisitHistoryAdapter(Context mContext, List<RMNCHAVisitLogsResponse.VisitLog> visitLogList) {
        this.context = mContext;
        this.visitLogList = visitLogList;
    }

    @NotNull
    @Override
    public RMNCHAHHVisitHistoryRow onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rmncha_ec_bc_history_row, parent, false);
        return new RMNCHAHHVisitHistoryRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMNCHAHHVisitHistoryRow holder, int position) {
        try {
            RMNCHAVisitLogsResponse.VisitLog visitLog = visitLogList.get(position);
            holder.visitNo.setText(position + 1 + "");
            holder.date.setText(getDateToView(visitLog.getVisitDate(), RMNCHA_DATE_FORMAT));
            holder.type.setText(visitLog.getBcOcpType());
            holder.pregnancy.setText(visitLog.isPregnancyTestTaken() ? "Yes" : "No");
            holder.pregnancyResult.setText(visitLog.getPregnancyTestResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (visitLogList != null)
            return visitLogList.size();
        else
            return 0;
    }

    class RMNCHAHHVisitHistoryRow extends RecyclerView.ViewHolder {

        private final TextView visitNo;
        private final TextView date;
        private final TextView type;
        private final TextView pregnancy;
        private final TextView pregnancyResult;

        public RMNCHAHHVisitHistoryRow(@NonNull View itemView) {
            super(itemView);
            visitNo = itemView.findViewById(R.id.bc_history_visit_no);
            date = itemView.findViewById(R.id.bc_history_visit_date);
            type = itemView.findViewById(R.id.bc_history_type_of_contraceptive);
            pregnancy = itemView.findViewById(R.id.bc_history_pregnancy);
            pregnancyResult = itemView.findViewById(R.id.bc_history_pregnancy_result);
        }
    }

}
