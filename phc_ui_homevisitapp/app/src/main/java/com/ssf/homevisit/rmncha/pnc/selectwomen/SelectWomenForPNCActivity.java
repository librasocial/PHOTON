package com.ssf.homevisit.rmncha.pnc.selectwomen;

import static com.ssf.homevisit.rmncha.base.RMNCHAConstants.PNC_REQUEST_CODE;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.pnc.details.PNCDetailsActivity.SELECTED_WOMEN_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaSelectWomenForPncBinding;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;
import com.ssf.homevisit.rmncha.pnc.details.PNCDetailsActivity;
import com.ssf.homevisit.rmncha.pnc.service.PNCServiceActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectWomenForPNCActivity extends RMNCHABaseActivity implements PNCWomenAdapter.PNCWomenClickListener {
    public static final String PARAM_1 = "param_1";
    public static final String PARAM_SUB_CENTER = "PARAM_SUB_CENTER";
    private ActivityRmnchaSelectWomenForPncBinding binding;
    private String houseHoldUUID;
    private String subCenterUUID;
    private SelectWomenForPNCViewModel viewModel;
    private PNCWomenAdapter mRMNCHAWomenAdapter;
    private List<RMNCHAPNCWomenResponse.Content> womenMemberList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        houseHoldUUID = getIntent().getExtras().get(PARAM_1) + "";
        subCenterUUID = getIntent().getExtras().get(PARAM_SUB_CENTER) + "";
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_select_women_for_pnc);
        viewModel = new SelectWomenForPNCViewModel(getApplication());
        binding.setViewModel(viewModel);
        binding.backButton.setOnClickListener(view -> this.onBackPressed());
        getWomenInHouseHoldList();
    }

    private void getWomenInHouseHoldList() {
        showProgressBar();
        viewModel.getPNCWomenMembersInHouseHoldLiveData(houseHoldUUID).observe(this, contents -> {
            hideProgressBar();
            if (contents != null) {
                womenMemberList = contents;
            }
            setWomenMemberAdapter();
        });
    }

    private void setWomenMemberAdapter() {
        if (womenMemberList.size() > 0) {
            mRMNCHAWomenAdapter = new PNCWomenAdapter(this, womenMemberList, this);
            binding.selectFemaleMemberGridView.setAdapter(mRMNCHAWomenAdapter);
        } else {
            showMyToast(this, "No eligible women for PNC");
        }
    }

    @Override
    public void goToPNCDetailsScreen(RMNCHAPNCWomenResponse.Content selectedWomen) {
        Intent intent = new Intent(this, PNCDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_1, houseHoldUUID);
        bundle.putString(PARAM_SUB_CENTER, subCenterUUID);
        bundle.putSerializable(SELECTED_WOMEN_KEY, selectedWomen);
        intent.putExtras(bundle);
        startActivityForResult(intent, PNC_REQUEST_CODE);
    }

    @Override
    public void goToPNCServiceScreen(RMNCHAPNCWomenResponse.Content selectedWomen) {
        Intent intent = new Intent(this, PNCServiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_1, houseHoldUUID);
        bundle.putString(PARAM_SUB_CENTER, subCenterUUID);
        bundle.putSerializable(SELECTED_WOMEN_KEY, selectedWomen);
        intent.putExtras(bundle);
        startActivityForResult(intent, PNC_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PNC_REQUEST_CODE) {
            setWomenMemberAdapter();
        }
    }

    @Override
    protected void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }

}