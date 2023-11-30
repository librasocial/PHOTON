package com.ssf.homevisit.interfaces;

import com.ssf.homevisit.models.CreateHouseholdResponse;

public interface OnHouseholdCreated {
    public void onHouseholdCreated(CreateHouseholdResponse createHouseholdResponse);
    public void onHouseholdCreationFailed();
}
