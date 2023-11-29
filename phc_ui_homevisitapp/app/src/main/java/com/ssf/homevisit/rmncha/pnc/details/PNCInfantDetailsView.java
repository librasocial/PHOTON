package com.ssf.homevisit.rmncha.pnc.details;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.ssf.homevisit.R;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.rmncha.pnc.service.PNCServiceActivity;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class PNCInfantDetailsView extends Fragment {
    private static View view;
    private RMNCHAPNCDeliveryOutcomesResponse deliveryOutcomesResponse;
    private int totalInfantsCount = 0;
    private String motherName;
    private List<RMNCHAPNCInfantRequest.Infant> infantList;
    private String pncRegistrationId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_rmncha_parent_infant_details, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.view_pager_new);
        viewPager.setEnabled(false);

        if (getActivity() instanceof PNCServiceActivity) {
            String lmpDate = ((PNCServiceActivity) getActivity()).getLMPDate();
            String eddDate = ((PNCServiceActivity) getActivity()).getEDDDate();
            String lastANCVisitDate = ((PNCServiceActivity) getActivity()).getLastANCVisitDate();
            List<RMNCHAPNCInfantResponse.Infant> infantListViewOnly = ((PNCServiceActivity) getActivity()).getInfantDetails();
            PNCInfantFragmentPagerAdapter infantFragmentPagerAdapter = new PNCInfantFragmentPagerAdapter(
                    getChildFragmentManager(), lmpDate, eddDate, lastANCVisitDate, infantListViewOnly);
            viewPager.setAdapter(infantFragmentPagerAdapter);
        }
        if (getActivity() instanceof PNCDetailsActivity) {
            deliveryOutcomesResponse = ((PNCDetailsActivity) getActivity()).getInfantDetails();
            pncRegistrationId = ((PNCDetailsActivity) getActivity()).getPNCRegistrationId();
            this.totalInfantsCount = deliveryOutcomesResponse.getDeliveryDetails().getLiveBirthCount();
            infantList = new ArrayList<>();
            for (int i = 0; i < totalInfantsCount; i++) {
                infantList.add(null);
            }
            PNCInfantFragmentPagerAdapter infantFragmentPagerAdapter = new PNCInfantFragmentPagerAdapter(
                    getChildFragmentManager(), pncRegistrationId, deliveryOutcomesResponse,
                    new PNCInfantFragment.PNCInfantFragmentClickListener() {
                        @Override
                        public void onSubmitClicked(RMNCHAPNCInfantRequest.Infant infant) {

                            boolean isValid = true;

                            int position = viewPager.getCurrentItem();
                            if (position < infantList.size()) {
                                infantList.set(position, infant);
                            }

                            for (RMNCHAPNCInfantRequest.Infant i : infantList) {
                                if (i == null) {
                                    isValid = false;
                                    break;
                                }
                            }
                            RMNCHAPNCInfantRequest request = new RMNCHAPNCInfantRequest();
                            request.setInfants(infantList);

                            if (isValid && totalInfantsCount == infantList.size()) {
                                if (getActivity() instanceof PNCDetailsActivity) {
                                    ((PNCDetailsActivity) getActivity()).onPNCSubmitClicked(request);
                                }
                            } else {
                                showMyToast(view.getContext(), "Enter details of all babies");
                            }
                        }
                        @Override
                        public void onNextClicked(RMNCHAPNCInfantRequest.Infant infant) {
                            int position = viewPager.getCurrentItem();
                            if (position < infantList.size()) {
                                infantList.set(position, infant);
                                viewPager.setCurrentItem(+1, true);
                            }
                        }

                        @Override
                        public void onPreviousClicked(RMNCHAPNCInfantRequest.Infant infant) {
                            int position = viewPager.getCurrentItem();
                            if (position < infantList.size()) {
                                infantList.set(position, infant);
                                viewPager.setCurrentItem(position - 1, true);
                            }
                        }
                    });
            viewPager.setAdapter(infantFragmentPagerAdapter);
        }
    }

}
