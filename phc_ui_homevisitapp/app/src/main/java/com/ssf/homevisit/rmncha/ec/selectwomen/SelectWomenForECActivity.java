package com.ssf.homevisit.rmncha.ec.selectwomen;

import static com.ssf.homevisit.rmncha.base.RMNCHAConstants.EC_REQUEST_CODE;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaSelectWomenForEcBinding;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;
import com.ssf.homevisit.rmncha.ec.ecservice.ECServiceActivity;
import com.ssf.homevisit.rmncha.ec.selectspouse.SelectSpouseForECActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectWomenForECActivity extends RMNCHABaseActivity implements ECWomenAdapter.ECWomenClickListener {
    public static final String PARAM_1 = "param_1";
    public static final String PARAM_SUB_CENTER = "PARAM_SUB_CENTER";
    private ActivityRmnchaSelectWomenForEcBinding binding;
    private String houseHoldUUID;
    private String subCenterUUID;
    private SelectWomenForECViewModel viewModel;
    private ECWomenAdapter mECWomenAdapter;
    private List<RMNCHAMembersInHouseHoldResponse.Content> womenMemberList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        houseHoldUUID = getIntent().getExtras().get(PARAM_1) + "";
        subCenterUUID = getIntent().getExtras().get(PARAM_SUB_CENTER) + "";
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_select_women_for_ec);
        viewModel = new SelectWomenForECViewModel(getApplication());
        binding.setViewModel(viewModel);
        binding.backButton.setOnClickListener(view -> this.onBackPressed());
        setHeaderMessage();
        getWomenInHouseHoldList();
    }

    private void setHeaderMessage() {
        binding.selectFemaleHeader.setText(getResources().getString(R.string.hh_start_service_or_select_female_header_message));
    }

    private void getWomenInHouseHoldList() {
        showProgressBar();
        viewModel.getWomenMembersInHouseHoldLiveData(houseHoldUUID).observe(this, contents -> {
            hideProgressBar();
            if (contents != null) {
                womenMemberList = contents;
                setWomenMemberAdapter();
            } else {
                showMyToast(this, "Error fetching HH data  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void setWomenMemberAdapter() {
        if (womenMemberList.size() > 0) {
            mECWomenAdapter = new ECWomenAdapter(this, womenMemberList, this);
            binding.selectFemaleMemberGridView.setAdapter(mECWomenAdapter);
        } else {
            showMyToast(this, "No eligible women for EC Registration");
        }
    }

    @Override
    public void onECWomenClicked(int position) {
        RMNCHAMembersInHouseHoldResponse.Content women = womenMemberList.get(position);
        RMNCHAServiceStatus serviceType = women.getTargetNode().getProperties().getRMNCHAServiceStatus();
        String serviceID = women.getTargetNode().getProperties().getServiceId();
        Intent intent;
        Bundle bundle = new Bundle();
        if (serviceType != null) {
            switch (serviceType) {
                case BCOngoing:
                case BC_ONGOING:
                case ECRegistered:
                case EC_REGISTERED:
                case PCOngoing:
                case PC_ONGOING:
                    intent = new Intent(this, ECServiceActivity.class);
                    break;
                case NEW:
                default:
                    intent = new Intent(this, SelectSpouseForECActivity.class);
                    break;
            }
        } else {
            intent = new Intent(this, SelectSpouseForECActivity.class);
        }
        bundle.putString(PARAM_1, houseHoldUUID);
        bundle.putString(PARAM_SUB_CENTER, subCenterUUID);
        bundle.putSerializable(SelectSpouseForECActivity.SELECTED_WOMEN_KEY, women);
        intent.putExtras(bundle);
        startActivityForResult(intent, EC_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EC_REQUEST_CODE) {
            getWomenInHouseHoldList();
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