package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.LayoutAlertHouseholdMappingBinding;
import com.ssf.homevisit.factories.HouseholdAlertViewModelFactory;
import com.ssf.homevisit.interfaces.OnCitizenCreated;
import com.ssf.homevisit.models.CreateResidentResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.ResidentInHouseHoldResponse;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.HouseholdAlertViewModel;
import com.ssf.homevisit.views.AddPersonDetail;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HouseholdAlert implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CODE = 101;
    public static int cameraResultStart = 10001;
    public static int cameraResultEnd = 10021;
    private static Context context;
    private static HouseholdAlert alert;
    private HouseholdAlertViewModel householdAlertViewModel;
    private DatePicker datePicker;
    private Calendar calendar;
    private ProgressBar pb;
    private TextView dateView;
    private int date, month, year;
    private String[] gender = {"Male", "Female", "other"};
    private LayoutAlertHouseholdMappingBinding binding;
    private Dialog dialog3;
    private Fragment currentFragment;
    Map<Integer, String> imagePosition = new HashMap<>();

    public static HouseholdAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (HouseholdAlert.class) {
                if (alert == null) {
                    alert = new HouseholdAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(HouseHoldProperties houseHoldProperties, Fragment fragment) {
        currentFragment = fragment;
        householdAlertViewModel = new ViewModelProvider(
                ((AppCompatActivity) context),
                new HouseholdAlertViewModelFactory(((Activity) context).getApplication(), houseHoldProperties)
        ).get(HouseholdAlertViewModel.class);
        householdAlertViewModel.setHouseholdProperties(houseHoldProperties);
        dialog3 = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog3.getLayoutInflater(), R.layout.layout_alert_household_mapping, null, false);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setCancelable(false);
        dialog3.setContentView(binding.getRoot());
        dialog3.show();
        binding.setViewModel(householdAlertViewModel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context.getApplicationContext(), R.array.gender, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ivClose.setOnClickListener(view -> {
            householdAlertViewModel.clearData();
            dialog3.dismiss();
        });

        binding.btnCancel.setOnClickListener(view -> {
            householdAlertViewModel.clearData();
            dialog3.dismiss();
        });
        binding.buttonSaveChanges.setOnClickListener(view -> {
            onSaveButtonClick();

        });
        ProgressDialog.getInstance(context).display();
        ProgressDialog.getInstance(context).setStatus("Fetching residents");
        householdAlertViewModel.getResidentsInHouseholdData().observe((AppCompatActivity) context, new Observer<ResidentInHouseHoldResponse>() {
            @Override
            public void onChanged(ResidentInHouseHoldResponse residentInHouseHoldResponse) {
                binding.layoutAdd.removeAllViews();
                if (residentInHouseHoldResponse != null) {
                    int i = 0;
                    for (ResidentInHouseHoldResponse.Content content : residentInHouseHoldResponse.getContent()) {
                        i++;
                        if (i <= 20)
                            addPersonDetailsView(content.getTarget().getProperties());
                    }
                }
            }
        });
        binding.trees.setOnClickListener(view -> {
            AddPersonDetail addPersonDetail = new AddPersonDetail(context);
            ResidentProperties residentProperties = new ResidentProperties();
            residentProperties.setHouseHold(houseHoldProperties.getUuid());
            addPersonDetail.setResidentProperties(residentProperties, householdAlertViewModel);
            addPersonDetail.setFragment(currentFragment);
            addPersonDetail.setCAMERA_RESULT_CODE(cameraResultStart + binding.layoutAdd.getChildCount());
            addPersonDetail.setOnMakingHead(() -> {
                int heads = 0;
                for (int i = 0; i < binding.layoutAdd.getChildCount(); i++) {
                    AddPersonDetail addPersonDetail1 = (AddPersonDetail) binding.layoutAdd.getChildAt(i);
                    if (addPersonDetail1.isHead()) heads++;
                }
                if (heads > 1) {
                    throw new Exception("There can not be two heads in a family");
                }
                if (heads < 1) {
                    throw new Exception("There should be at least one head");
                }
            });
            binding.layoutAdd.addView(addPersonDetail);
            addPersonDetail.requestFocus();
        });
    }

    public void addPersonDetailsView(ResidentProperties residentProperties) {
        AddPersonDetail addPersonDetail = new AddPersonDetail(context);
        addPersonDetail.setFragment(currentFragment);
        addPersonDetail.setCAMERA_RESULT_CODE(cameraResultStart + binding.layoutAdd.getChildCount());
        addPersonDetail.setOnMakingHead(() -> {
            int heads = 0;
            for (int i = 0; i < binding.layoutAdd.getChildCount(); i++) {
                AddPersonDetail addPersonDetail1 = (AddPersonDetail) binding.layoutAdd.getChildAt(i);
                if (addPersonDetail1.isHead()) heads++;
            }
            if (heads > 1) {
                throw new Exception("There can not be two heads");
            }
            if (heads < 1) {
                throw new Exception("There should be at least one head");
            }
        });
        addPersonDetail.setResidentProperties(residentProperties, householdAlertViewModel);
        binding.layoutAdd.addView(addPersonDetail);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, date);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        Toast.makeText(context, currentDateString, Toast.LENGTH_SHORT).show();

    }

    public void saveCitizen(int index, int saved) {
        ProgressDialog.getInstance(context).setStatus("saving citizen " + (index + 1) + "/" + binding.layoutAdd.getChildCount() +"\nDo not press back button");
        if (binding.layoutAdd.getChildCount() == index) {
            ProgressDialog.dismiss();
            UIController.getInstance().displayToastMessage(context, saved + " / " + index + " saved");
            dialog3.dismiss();
            return;
        }
        try {
            AddPersonDetail addPersonDetail = (AddPersonDetail) binding.layoutAdd.getChildAt(index);
            ResidentProperties residentProperties = addPersonDetail.getResidentProperties(index,imagePosition);
            addPersonDetail.postImageToS3Bucket(index,imagePosition);
            householdAlertViewModel.createCitizen(residentProperties, new OnCitizenCreated() {
                @Override
                public void onCitizenCreated(CreateResidentResponse residentProperties1) {
                    AddPersonDetail addPersonDetail = (AddPersonDetail) binding.layoutAdd.getChildAt(index);
                    if (residentProperties1 == null) {
                        UIController.getInstance().displayToastMessage(context, "cannot save " + addPersonDetail.getFirstName());
                    } else {
                        UIController.getInstance().displayToastMessage(context, addPersonDetail.getFirstName() + " saved");
                        addPersonDetail.getResidentProperties(index,imagePosition).setUuid(residentProperties1.getUuid());
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveCitizen(index + 1, saved + 1);
                        }
                    }, 2000);
                }

                @Override
                public void onCitizenCreationFailed() {
                    AddPersonDetail addPersonDetail = (AddPersonDetail) binding.layoutAdd.getChildAt(index);
                    UIController.getInstance().displayToastMessage(context, "cannot save " + addPersonDetail.getFirstName());
                    saveCitizen(index + 1, saved);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            UIController.getInstance().displayToastMessage(context, exception.getMessage());
            saveCitizen(index + 1, saved);
        }

    }

    public void onSaveButtonClick() {
        ProgressDialog.getInstance(context).display();
        ProgressDialog.getInstance(context).setStatus("saving citizens\nDo not press back button");

        try {
            checkHeads();
            saveCitizen(0, 0);
        } catch (Exception e) {
            UIController.getInstance().displayToastMessage(context, e.getMessage());
            ProgressDialog.dismiss();
        }

    }

    private void checkHeads() throws Exception {
        int heads = 0;
        for (int i = 0; i < binding.layoutAdd.getChildCount(); i++) {
            AddPersonDetail addPersonDetail = (AddPersonDetail) binding.layoutAdd.getChildAt(i);
            if (addPersonDetail.isHead()) heads++;
        }
        if (heads > 1) {
            throw new Exception("There can not be two heads in a family");
        }
        if (heads < 1) {
            throw new Exception("There should be at least one head");
        }
    }

    public void onImageCaptured(int index) {
        Map<String, String> properties = new HashMap<>();
        properties.put("index", index + "");
        ((AddPersonDetail) binding.layoutAdd.getChildAt(index)).imageCaptured(index,imagePosition);
    }
}