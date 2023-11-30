package com.ssf.homevisit.interfaces;

import com.ssf.homevisit.models.CreateHouseholdResponse;
import com.ssf.homevisit.models.CreateResidentResponse;
import com.ssf.homevisit.models.ResidentProperties;

public interface OnCitizenCreated {
    public void onCitizenCreated(CreateResidentResponse createHouseholdResponse);
    public void onCitizenCreationFailed();
}
