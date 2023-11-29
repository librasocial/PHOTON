package com.ssf.homevisit.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.HouseHoldLevelMappingViewModel;
import com.ssf.homevisit.views.HouseholdRow;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HouseholdsAdapter extends RecyclerView.Adapter<HouseholdRow> {

    Context context;
    ArrayList<HouseHoldProperties> houseHoldPropertiesArrayList;

    Fragment currentFragment;
    int surveyType;

    public HouseholdsAdapter(Context mContext, ArrayList<HouseHoldProperties> nearbyHouseholdList, Fragment fragment, int surveyType) {
        context = mContext;
        houseHoldPropertiesArrayList = nearbyHouseholdList;
        currentFragment = fragment;
        this.surveyType = surveyType;
    }

    @NotNull
    @Override
    public HouseholdRow onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_household_row, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new HouseholdRow(view, currentFragment, context, surveyType);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HouseholdRow holder, int position) {
        holder.setHouseHoldProperties(houseHoldPropertiesArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return houseHoldPropertiesArrayList.size();
    }
}
