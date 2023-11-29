package com.ssf.homevisit.rmncha.anc.selectwomen;

import static com.ssf.homevisit.rmncha.base.RMNCHAConstants.ANC_REQUEST_CODE;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaSelectWomenForAncBinding;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;
import com.ssf.homevisit.rmncha.anc.pwregistration.ANCPWRegistrationActivity;
import com.ssf.homevisit.rmncha.anc.pwtracking.ANCPWTrackingActivity;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectWomenForANCActivity extends RMNCHABaseActivity implements ANCWomenAdapter.ANCWomenClickListener {
    public static final String PARAM_1 = "param_1";
    public static final String PARAM_SUB_CENTER = "PARAM_SUB_CENTER";
    public static final String PARAM_ANC_SELECTED_WOMEN = "PARAM_ANC_SELECTED_WOMEN";
    private ActivityRmnchaSelectWomenForAncBinding binding;
    private String houseHoldUUID;
    private String subCenterUUID;
    private SelectWomenForANCViewModel viewModel;
    private ANCWomenAdapter mRMNCHAECWomenAdapter;
    private List<RMNCHAANCPWsResponse.Content> womenMemberList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        houseHoldUUID = getIntent().getExtras().get(PARAM_1) + "";
        subCenterUUID = getIntent().getExtras().get(PARAM_SUB_CENTER) + "";
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_select_women_for_anc);
        viewModel = new SelectWomenForANCViewModel(getApplication());
        binding.setViewModel(viewModel);
        binding.backButton.setOnClickListener(view -> this.onBackPressed());
        getWomenInHouseHoldList();
    }

    private void getWomenInHouseHoldList() {
        showProgressBar();
        viewModel.getANCPWsInHouseHoldLiveData(houseHoldUUID).observe(this, contents -> {
            hideProgressBar();
            if (contents != null) {
                womenMemberList = contents;
                setWomenMemberAdapter();
            } else {
                showMyToast(this, "Error fetching data  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void setWomenMemberAdapter() {
        if (womenMemberList.size() > 0) {
            mRMNCHAECWomenAdapter = new ANCWomenAdapter(this, womenMemberList, this);
            binding.selectFemaleMemberGridView.setAdapter(mRMNCHAECWomenAdapter);
        } else {
            showMyToast(this, "No eligible women for ANC");
        }
    }

    @Override
    public void onANCWomenClicked(int position) {
        RMNCHAANCPWsResponse.Content selectedWomen = womenMemberList.get(position);
        RMNCHAServiceStatus serviceType = selectedWomen.getSource().getProperties().getRmnchaServiceStatus();
        Intent intent;
        if (serviceType == RMNCHAServiceStatus.ANC_ONGOING || serviceType == RMNCHAServiceStatus.ANCOngoing || serviceType == RMNCHAServiceStatus.ANC_REGISTERED) {
            intent = new Intent(this, ANCPWTrackingActivity.class);
        } else {
            intent = new Intent(this, ANCPWRegistrationActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_1, houseHoldUUID);
        bundle.putString(PARAM_SUB_CENTER, subCenterUUID);
        bundle.putSerializable(PARAM_ANC_SELECTED_WOMEN, selectedWomen);
        intent.putExtras(bundle);
        startActivityForResult(intent, ANC_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ANC_REQUEST_CODE) {
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