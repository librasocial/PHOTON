package com.ssf.homevisit.alerts;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ssf.homevisit.databinding.VillagelevelmappingBinding;
import com.ssf.homevisit.viewmodel.CommonAlertViewModel;

public class VillageLevelSurveyMappingAlert {

    private final Context context;
    CommonAlertViewModel commonAlertViewModel;

    private static VillageLevelSurveyMappingAlert alert;

    public static VillageLevelSurveyMappingAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (VillageLevelMappingAlert.class) {
                if (alert == null) {
                    alert = new VillageLevelSurveyMappingAlert(mcontext);
                }
            }
        }
        return alert;
    }



    public VillageLevelSurveyMappingAlert(Context context) {
        this.context = context;
        commonAlertViewModel = new CommonAlertViewModel(((Activity) context).getApplication());
    }


    private void populateSpinner(String assetType) {
        String[] arraySpinner = getTypeOfQuestionsList(assetType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }

    public String[] getTypeOfQuestionsList(String quesSelec){
        switch (quesSelec){
            case "Water Quality":
            case "Air Quality":
            case "Drainage System":
                return new String[]{
                        "Good",
                        "Poor",
                        "Very Poor"
                };
            case "Water Disposal":
                return new String[]{
                        "Open ground / fields",
                        "Beside the household",
                        "Dumped in river/water bodies",
                        "Outside the village",
                        "Community level compost arrangement(NADEP/Vermicompost, etc)",
                        "Community level waste collection",
                        "Segregated waste collect",
                };case "Source of pollution":
                return new String[]{
                        "Industry",
                        "Slaughterhouse",
                        "Households"
                };

                case "Is any open defecation Places in your village?":
            case "Is the village connected to the above by a pukka road":

            case "Panchayat Bhavan":

            case "Library/ Reading Room*":

            case "Public Playground":


            case "Park":

            case "Public Distribution System (PDS)":

            case "Forest":

            case "Anganwadi":

            case "Health SC/PHC":

            case "Primary School":

            case "High School":

            case "College":

            case "Primary Agriculture Credit Society":

            case "Co-operation Marketing Society":

            case "Dairy/Poultry/Fisheries Cooperation Society":

            case "Banks(Rural/National)":

            case "Does the village have electricity?":

            case "Any Other":
                return new String[]{
                        "Yes",
                        "No"
                };

                case "Water source for village":
                return new String[]{
                        "Piped",
                        "Well",
                        "Tank",
                        "Tube Well",
                        "Hand Pump",
                        "Canal",
                        "River",
                        "Lake",
                        "Watershed"
                };

                case "Piped Water":
            case "Well":

            case "Tank":
            case "Tube Well":
            case "Canal":
            case "Hand Pump":
            case "River":
            case "Lake":
            case "Others":
                return new String[]{
                        "Coliform Bacteria",
                        "Nitrate",
                        "Arsenic",
                        "Lead",
                        "None"
                };

            case "Available mode of Transport":
                return new String[]{
                        "Rail",
                        "Government Bus",
                        "Private Bus",
                        "Taxi/Temo/Auto",
                        "Boat",
                        "Others",
                        "None"
                };


        }
        return new String[]{};
    }
}
