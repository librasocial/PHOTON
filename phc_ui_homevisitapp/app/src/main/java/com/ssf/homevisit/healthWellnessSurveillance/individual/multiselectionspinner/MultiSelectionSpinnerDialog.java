package com.ssf.homevisit.healthWellnessSurveillance.individual.multiselectionspinner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.healthWellnessSurveillance.individual.multiselectionspinner.adapter.MultiSelectionRecyclerAdapter;
import com.ssf.homevisit.healthWellnessSurveillance.individual.multiselectionspinner.interfaces.OnMultiSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class MultiSelectionSpinnerDialog extends Dialog implements OnMultiSelectionListener {

    static Dialog multiSelectionSpinnerDialog;
    private List<String> chosenList = new ArrayList<>();
    static MultiSpinner multiSpinner;


    void setMultiSelectionAdapterWithImage(final Context context, List<String> imageURLList, List<String> spinnerContentList,
                                           OnMultiSpinnerSelectionListener onMultiSpinnerSelectionListener) {

        multiSelectionSpinnerDialog = new Dialog(context);
        multiSelectionSpinnerDialog.setContentView(R.layout.layout_dialog_multi_selection);
        final Window window = multiSelectionSpinnerDialog.getWindow();

        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        multiSelectionSpinnerDialog.setCancelable(true);

        RecyclerView multiSelectionItemRecycler = multiSelectionSpinnerDialog.findViewById(R.id.recyclerMultiSelection);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        multiSelectionItemRecycler.setLayoutManager(linearLayoutManager);

        MultiSelectionRecyclerAdapter multiSelectionRecyclerAdapter = new MultiSelectionRecyclerAdapter(true, imageURLList, spinnerContentList);
        multiSelectionRecyclerAdapter.setOnMultiSelectionListener(this);
        multiSelectionItemRecycler.setAdapter(multiSelectionRecyclerAdapter);


        TextView cancelDialog = multiSelectionSpinnerDialog.findViewById(R.id.tvCancelMultiSelection);
        TextView chooseSelection = multiSelectionSpinnerDialog.findViewById(R.id.tvOK);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiSelectionSpinnerDialog.dismiss();
            }
        });

        final OnMultiSpinnerSelectionListener finalOnMultiSpinnerSelectionListener = onMultiSpinnerSelectionListener;
        chooseSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalOnMultiSpinnerSelectionListener.OnMultiSpinnerItemSelected(chosenList);
                multiSelectionSpinnerDialog.dismiss();
            }
        });

    }

    void setMultiSelectionAdapterWithOutImage(final Context context, List<String> spinnerContentList,
                                              OnMultiSpinnerSelectionListener onMultiSpinnerSelectionListener) {

        multiSelectionSpinnerDialog = new Dialog(context);
        multiSelectionSpinnerDialog.setContentView(R.layout.layout_dialog_multi_selection);
        final Window window = multiSelectionSpinnerDialog.getWindow();

        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        multiSelectionSpinnerDialog.setCancelable(true);

        RecyclerView multiSelectionItemRecycler = multiSelectionSpinnerDialog.findViewById(R.id.recyclerMultiSelection);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        multiSelectionItemRecycler.setHasFixedSize(false);
        multiSelectionItemRecycler.setLayoutManager(linearLayoutManager);

        MultiSelectionRecyclerAdapter multiSelectionRecyclerAdapter = new MultiSelectionRecyclerAdapter(false, spinnerContentList);
        multiSelectionRecyclerAdapter.setOnMultiSelectionListener(this);
        multiSelectionItemRecycler.setAdapter(multiSelectionRecyclerAdapter);
        multiSelectionRecyclerAdapter.notifyDataSetChanged();
        final OnMultiSpinnerSelectionListener finalOnMultiSpinnerSelectionListener = onMultiSpinnerSelectionListener;
        multiSelectionSpinnerDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finalOnMultiSpinnerSelectionListener.OnMultiSpinnerItemSelected(chosenList);
            }
        });

    }

    public MultiSelectionSpinnerDialog(@NonNull Context context) {
        super(context);

    }


    @Override
    public void OnMultiSpinnerSelected(List<String> itemChosenList) {

        chosenList = itemChosenList;
        chosenList.remove("");
        if (multiSpinner != null) {

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < chosenList.size(); i++) {

                if (i + 1 != chosenList.size()) {

                    stringBuilder.append(chosenList.get(i));
                    stringBuilder.append(",");
                } else {

                    stringBuilder.append(chosenList.get(i));
                }
            }

            multiSpinner.setText(stringBuilder.toString());
        }

    }


    public interface OnMultiSpinnerSelectionListener {

        void OnMultiSpinnerItemSelected(List<String> chosenItems);

    }
}

