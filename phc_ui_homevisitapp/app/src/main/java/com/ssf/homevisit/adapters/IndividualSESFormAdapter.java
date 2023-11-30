package com.ssf.homevisit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.models.CitizenProperties;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.views.HouseholdRow;
import com.ssf.homevisit.views.IndividualSESFormRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IndividualSESFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ResidentProperties> citizenPropertiesArrayList;

    Fragment currentFragment;
    private SurveyFilterResponse surveyQuestions;
    private RecyclerView recyclerView;

    public IndividualSESFormAdapter(Context mContext, ArrayList<ResidentProperties> citizenPropertiesArrayList, Fragment fragment, SurveyFilterResponse surveyQuestions, RecyclerView recyclerView) {
        context = mContext;
        this.citizenPropertiesArrayList = citizenPropertiesArrayList;
        currentFragment = fragment;
        this.surveyQuestions = surveyQuestions;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return citizenPropertiesArrayList.size();
    }
}
