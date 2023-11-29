package com.ssf.homevisit.rmncha.pnc.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;

import java.util.List;

public class PNCInfantFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private RMNCHAPNCDeliveryOutcomesResponse deliveryOutcomesResponse;
    private boolean isViewOnly = false;
    private int totalInfantsCount;
    private String pncRegistrationId;
    private PNCInfantFragment.PNCInfantFragmentClickListener listener;
    private List<RMNCHAPNCInfantResponse.Infant> infantList;
    String lmpDate;
    String eddDate;
    String lastANCVisitDate;

    public PNCInfantFragmentPagerAdapter(@NonNull FragmentManager fm, String pncRegistrationId, RMNCHAPNCDeliveryOutcomesResponse deliveryOutcomesResponse, PNCInfantFragment.PNCInfantFragmentClickListener listener) {
        super(fm);
        this.isViewOnly = false;
        this.pncRegistrationId = pncRegistrationId;
        this.listener = listener;
        this.deliveryOutcomesResponse = deliveryOutcomesResponse;
        if (deliveryOutcomesResponse != null) {
            this.totalInfantsCount = deliveryOutcomesResponse.getDeliveryDetails().getLiveBirthCount();
        }
    }

    public PNCInfantFragmentPagerAdapter(FragmentManager childFragmentManager, String lmpDate, String eddDate, String lastANCVisitDate, List<RMNCHAPNCInfantResponse.Infant> infantList) {
        super(childFragmentManager);
        this.isViewOnly = true;
        this.lmpDate = lmpDate;
        this.eddDate = eddDate;
        this.lastANCVisitDate = lastANCVisitDate;
        if (infantList != null) {
            this.infantList = infantList;
            this.totalInfantsCount = infantList.size();
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PNCInfantFragment();
        Bundle args = new Bundle();
        args.putInt(PNCInfantFragment.ARG_POSITION, position);
        args.putInt(PNCInfantFragment.ARG_TOTAL, totalInfantsCount);
        args.putBoolean(PNCInfantFragment.ARG_VIEW_ONLY, isViewOnly);
        fragment.setArguments(args);
        if (isViewOnly) {
            if (infantList != null && position <= infantList.size())
                ((PNCInfantFragment) fragment).setInfantData(infantList.get(position), lmpDate, eddDate, lastANCVisitDate);
        } else {
            ((PNCInfantFragment) fragment).setPNCInfantFragmentClickListener(pncRegistrationId, deliveryOutcomesResponse, listener);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return totalInfantsCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Baby " + (position + 1);
    }


}
