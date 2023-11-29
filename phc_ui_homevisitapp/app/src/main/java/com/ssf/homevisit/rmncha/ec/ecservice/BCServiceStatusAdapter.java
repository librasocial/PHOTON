package com.ssf.homevisit.rmncha.ec.ecservice;

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
import com.ssf.homevisit.rmncha.base.RMNCHAUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BCServiceStatusAdapter extends RecyclerView.Adapter<BCServiceStatusAdapter.RMNCHAHHVisitLogRow> {

    private Context context;
    private List<RMNCHAVisitLogsResponse.VisitLog> visitLogList;

    public BCServiceStatusAdapter(Context mContext, List<RMNCHAVisitLogsResponse.VisitLog> visitLogList) {
        this.context = mContext;
        this.visitLogList = visitLogList;
    }

    @NotNull
    @Override
    public RMNCHAHHVisitLogRow onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rmncha_bc_visit_log_row, parent, false);
        return new RMNCHAHHVisitLogRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMNCHAHHVisitLogRow holder, int position) {
        try {
            RMNCHAVisitLogsResponse.VisitLog visitLog = visitLogList.get(position);
            holder.date.setText(getDateToView(visitLog.getVisitDate(), RMNCHAUtils.RMNCHA_DATE_FORMAT));
            holder.quantity.setText(visitLog.getBcQuantity());
            holder.type.setText(visitLog.getBcOcpType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (visitLogList != null) {
            // Showing maximum 3 items as per requirements
            if (visitLogList.size() < 4)
                return visitLogList.size();
            else
                return 3;
        } else {
            return 0;
        }
    }

    class RMNCHAHHVisitLogRow extends RecyclerView.ViewHolder {

        private final TextView date;
        private final TextView quantity;
        private final TextView type;

        public RMNCHAHHVisitLogRow(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.bc_service_date);
            quantity = itemView.findViewById(R.id.bc_service_quantity);
            type = itemView.findViewById(R.id.bc_service_type);
        }
    }

}
