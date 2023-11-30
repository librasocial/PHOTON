package com.ssf.homevisit.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.ssf.homevisit.BuildConfig;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.interfaces.OnMakingHead;
import com.ssf.homevisit.models.PrefetchURLResponse;
import com.ssf.homevisit.models.PutImageResponse;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.CameraUtility;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.HouseholdAlertViewModel;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPersonDetail extends LinearLayout implements DatePickerDialog.OnDateSetListener {

    private Fragment currentFragment;
    private int CAMERA_RESULT_CODE;
    private DatePickerDialog datePickerDialog;
    private ImageView profileImageView;
    private EditText firstNameText;
    private EditText middleNameText;
    private EditText lastNameText;
    private EditText contactText;
    private TextView datePickerText;
    private OnMakingHead onMakingHead;
    private TextView mobileNumber;
    private Spinner genderSpinner;
    private Spinner residingSpinner;
    private ArrayList<String> genderList;
    private ArrayList<String> residingList;
    private CheckBox isHeadCheckBox;
    private ResidentProperties residentProperties;
    private String selectedDOB;
    private Map<Integer, String> HashMap;
    private String fileName = "";
    private Uri profileImageUri;

    public AddPersonDetail(Context context) {
        this(context, null);
    }

    public AddPersonDetail(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        genderList = new ArrayList<String>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Trans");
        residingList = new ArrayList<String>();
        residingList.add("Resides");
        residingList.add("New born");
        residingList.add("Deceased");
        residingList.add("Migrated out after marriage");
        residingList.add("Migrated in after marriage");
        residingList.add("Migrated out for work");
        residingList.add("Migrated out with a head of the family");
        residingList.add("Guest");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View villagerDetail = inflater.inflate(R.layout.layout_view, this, true);
        profileImageView = villagerDetail.findViewById(R.id.profile_image);
        firstNameText = villagerDetail.findViewById(R.id.first_name_id);
        middleNameText = villagerDetail.findViewById(R.id.middle_name_id);
        lastNameText = villagerDetail.findViewById(R.id.last_name_id);
        datePickerText = villagerDetail.findViewById(R.id.simpleDatePicker);
        contactText = villagerDetail.findViewById(R.id.number);
        genderSpinner = (Spinner) villagerDetail.findViewById(R.id.gender);
        residingSpinner = (Spinner) villagerDetail.findViewById(R.id.residing);
        isHeadCheckBox = villagerDetail.findViewById(R.id.checkbox_ishead);
        isHeadCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            try {
                onMakingHead.checkHead();
            } catch (Exception e) {
                e.printStackTrace();
                UIController.getInstance().displayToastMessage(context, e.getMessage());
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, genderList);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, residingList);
        genderSpinner.setAdapter(arrayAdapter);

        residingSpinner.setAdapter(arrayAdapter1);


        residingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (pos == 2) {
                    selectedText.setTextColor(Color.RED);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        datePickerText.setOnClickListener(view -> {
            showDatePickerDialog();
        });
        setProfileImageViewOnClick();

    }

    public void setCAMERA_RESULT_CODE(int cameraResultCode) {
        CAMERA_RESULT_CODE = cameraResultCode;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.RED);
        }
    }

    public AddPersonDetail(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddPersonDetail(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setProfileImageBucketKey(String bucketKey, HouseholdAlertViewModel householdAlertViewModel) {
        if (!bucketKey.isEmpty()) {
            householdAlertViewModel.getImageUrl(new OnImageUrlFetched() {
                @Override
                public void onImageUrlFetched(String imageUrl) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.progress_animation).into(profileImageView);
                }

                @Override
                public void onUrlFetchingFail() {
                }
            }, bucketKey);
        }
    }

    public void setFirstName(String firstName) {
        firstNameText.setText(firstName);
    }

    public void setMiddleName(String middleName) {
        middleNameText.setText(middleName);
    }

    public void setlastName(String lastName) {
        lastNameText.setText(lastName);
    }

    public void setGender(String gender) {
        int selection = genderList.indexOf(gender);
        selection = selection == -1 ? 0 : selection;
        genderSpinner.setSelection(selection);
    }

    public void setContact(String contact) {
        isValidMobile(contact);
        contactText.setText(contact);
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.DatePickerDialogTheme, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void setResidingSpinner(String residing) {
        int selection = residingList.indexOf(residing);
        selection = selection == -1 ? 0 : selection;
        residingSpinner.setSelection(selection);
    }

    public void setIsHead(boolean isHead) {
        isHeadCheckBox.setChecked(isHead);
    }

    private boolean isValidMobile(String phone) {
        if (phone != null)
            if (!Pattern.matches("[a-zA-Z]+", phone)) {
                return phone.length() > 6 && phone.length() <= 13;
            }
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        month = month + 1;
        String dob = year + "-" + month + "-" + dayOfMonth;
        setDob(dob);
    }

    public ResidentProperties getResidentProperties(int index, Map<Integer, String> imagePosition) {
        if (firstNameText.getText().toString().equals("")) {
            firstNameText.requestFocus();
            throw new NullPointerException("Enter First Name and try again");
        }
        residentProperties.setFirstName(firstNameText.getText().toString());
        residentProperties.setMiddleName(middleNameText.getText().toString());
        residentProperties.setLastName(lastNameText.getText().toString());

        if (selectedDOB != null) {
            String[] dob = selectedDOB.split("/");
            String day = dob[0].length() > 1 ? dob[0] : "0" + dob[0];
            String month = dob[1].length() > 1 ? dob[1] : "0" + dob[1];
            residentProperties.setDateOfBirth(dob[2] + "-" + month + "-" + day);
            residentProperties.setAge(getAge(Integer.parseInt(dob[2]), Integer.parseInt(month), Integer.parseInt(day)));
        }
        residentProperties.setIsHead(isHeadCheckBox.isChecked());
        residentProperties.setGender(genderList.get(genderSpinner.getSelectedItemPosition()));
        residentProperties.setResidingInVillage(residingList.get(residingSpinner.getSelectedItemPosition()));
        residentProperties.setContact(contactText.getText().toString());
        List<String> imgUrls = new ArrayList<>();
        if (profileImageView.getDrawable() != null) if (imagePosition.get(index)==null) {
            if (residentProperties.getImageUrls().size() > 0) {
                imgUrls.add(residentProperties.getImageUrls().get(0));
            }
        } else {
            imgUrls.add(imagePosition.get(index));
        }
        residentProperties.setImageUrls(imgUrls);
        return residentProperties;
    }

    public void setResidentProperties(ResidentProperties residentProperties, HouseholdAlertViewModel householdAlertViewModel) {
        this.residentProperties = residentProperties;
        setFirstName(residentProperties.getFirstName());
        setMiddleName(residentProperties.getMiddleName());
        setlastName(residentProperties.getLastName());
        setContact(residentProperties.getContact());
        setIsHead(residentProperties.getIsHead());
        setDob(residentProperties.getDateOfBirth());
        setResidingSpinner(residentProperties.getResidingInVillage());
        setGender(residentProperties.getGender());
        if (residentProperties.getImageUrls() != null && residentProperties.getImageUrls().size() != 0) {
            if (residentProperties.getImageUrls().size() > 0) {
                setProfileImageBucketKey(residentProperties.getImageUrls().get(0), householdAlertViewModel);
            }
        }
    }

    private void setDob(String date_of_birth) {
        if (date_of_birth == null) return;
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dob = "";
        try {
            Date date = sourceDateFormat.parse(date_of_birth);
            dob = date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
        } catch (Exception ignored) {
        }
        selectedDOB = dob;
        datePickerText.setText(selectedDOB);
    }

    public String getFirstName() {
        return residentProperties.getFirstName();
    }

    public boolean isHead() {
        return isHeadCheckBox.isChecked();
    }

    public void removeHead(){
        isHeadCheckBox.setChecked(false);
    }

    public void setOnMakingHead(OnMakingHead onMakingHead) {
        this.onMakingHead = onMakingHead;
    }

    private void setProfileImageViewOnClick() {
        profileImageView.setOnClickListener(view -> {
            if (currentFragment == null) {
                UIController.getInstance().displayToastMessage(getContext(), "please set currentFragment");
                return;
            }
            if (ActivityCompat.checkSelfPermission(
                    AppController.getInstance().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.CAMERA}, 2);
                return;
            }
            if (ActivityCompat.checkSelfPermission(
                    AppController.getInstance().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return;
            }
            if (ActivityCompat.checkSelfPermission(
                    AppController.getInstance().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
                return;
            }
            File file = CameraUtility.createImageFile(getContext());
            if (file == null) {
                UIController.getInstance().displayToastMessage(getContext(), "Cant write on the external storage, please check permission");
                return;
            }
            fileName = file.getName();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            profileImageUri = FileProvider.getUriForFile(getContext().getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, profileImageUri);
            currentFragment.startActivityForResult(intent, CAMERA_RESULT_CODE);

        });
    }

    private String generateBucketKey() {
        return UUID.randomUUID().toString();
    }

    public void setFragment(Fragment fragment) {
        currentFragment = fragment;
    }

    public void imageCaptured(int index, Map<Integer, String> imagePosition) {
        Map<String, String> properties = new HashMap<>();
        properties.put("uri", profileImageUri.toString());
        profileImageView.setImageURI(profileImageUri);
        imagePosition.put(index, fileName);
    }

    public void postImageToS3Bucket(int index, Map<Integer, String> imagePosition) {
        if (profileImageView.getDrawable() == null) return;
        ApiInterface apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
        String bucketKey = imagePosition.get(index);
        if(bucketKey!= null){
        if (!bucketKey.isEmpty()) {
            apiInterface.getMembersPresignedURL(bucketKey, Util.getHeader()).enqueue(new Callback<PrefetchURLResponse>() {
                @Override
                public void onResponse(Call<PrefetchURLResponse> call, Response<PrefetchURLResponse> response) {
                    ApiInterface apiInterface1 = ApiClient.getClient("").create(ApiInterface.class);
                    if (response.body().getPreSignedUrl() == null) {
                        UIController.getInstance().displayToastMessage(getContext(), "Cannot get prefetchURl");
                        return;
                    }

                    try {
                        Bitmap bitmap = Bitmap.createBitmap(((BitmapDrawable) profileImageView.getDrawable()).getBitmap());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageInByte = stream.toByteArray();
                        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), imageInByte);

                        apiInterface1.putImageToS3(response.body().getPreSignedUrl(), "image/jpeg", body).enqueue(new Callback<PutImageResponse>() {
                            @Override
                            public void onResponse(Call<PutImageResponse> call, Response<PutImageResponse> response) {
                                UIController.getInstance().displayToastMessage(getContext(), "Images Uploaded, Saving data");
                            }

                            @Override
                            public void onFailure(Call<PutImageResponse> call, Throwable t) {
                                if (t instanceof EOFException) {
                                    Log.d("tag", "tasdf");
                                } else {
                                    t.printStackTrace();
                                    Log.d("test", "failed while uploading image");
                                    UIController.getInstance().displayToastMessage(getContext(), "Failed to upload image");
                                }
                            }
                        });
                    } catch (Exception t) {
                        if (t instanceof EOFException) {
                            Log.d("tag", "tasdf");
                        } else {
                            t.printStackTrace();
                            Log.d("test", "failed while uploading image");
                            UIController.getInstance().displayToastMessage(getContext(), "Failed to upload image");
                        }
                    }
                }

                @Override
                public void onFailure(Call<PrefetchURLResponse> call, Throwable t) {
                    t.printStackTrace();
                    UIController.getInstance().displayToastMessage(getContext(), "Cannot get prefetchURl");
                    ProgressDialog.dismiss();
                }
            });
        }}
    }

    private Integer getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        return ageInt;
    }

}