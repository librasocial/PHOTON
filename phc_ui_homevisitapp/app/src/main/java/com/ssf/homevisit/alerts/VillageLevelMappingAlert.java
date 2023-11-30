package com.ssf.homevisit.alerts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.raj.jadon.filepicker.mannualDi.ImageAndFilePickerInjector;
import com.ssf.homevisit.BuildConfig;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.VillagelevelmappingBinding;
import com.ssf.homevisit.fragment.SociaEcoSurveyFragment;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.CameraUtility;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.CommonAlertViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class VillageLevelMappingAlert {
    public static int CAMERA_REQUEST_CODE_IMAGE_1 = 102;
    public static int CAMERA_REQUEST_CODE_IMAGE_2 = 103;
    public static int GALLERY_REQUEST_CODE_IMAGE_1 = 104;
    public static int GALLERY_REQUEST_CODE_IMAGE_2 = 105;
    private final Context context;
    private static VillageLevelMappingAlert alert;
    CommonAlertViewModel commonAlertViewModel;
    private Dialog currentDialog;
    VillagelevelmappingBinding binding;
    private Fragment currentFragment;
    File image1, image2;
    Uri image1Uri, image2Uri;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static VillageLevelMappingAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (VillageLevelMappingAlert.class) {
                if (alert == null) {
                    alert = new VillageLevelMappingAlert(mcontext);
                }
            }
        }
        return alert;
    }

    public VillageLevelMappingAlert(Context context) {
        this.context = context;
        commonAlertViewModel = new CommonAlertViewModel(((Activity) context).getApplication());
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(Fragment fragment, double latitude, double longitude, VillageProperties village, SociaEcoSurveyFragment.OnVillageAssetAdd onVillageAssetAdd) {
        currentFragment = fragment;
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.villagelevelmapping, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        currentDialog = dialog;
        binding.textLocation.setText(Util.DDtoDMS(latitude, "latitude") + " - " + Util.DDtoDMS(longitude, "longitude"));
        binding.ivClose.setOnClickListener(view -> dialog.dismiss());
        binding.btnSave.setOnClickListener(v -> {
            ProgressDialog.getInstance(context).display();
            pushImagesToS3(village, longitude, latitude, onVillageAssetAdd);
        });
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        binding.image1.setOnClickListener(view -> {
            openCamera(CAMERA_REQUEST_CODE_IMAGE_1);
        });
        binding.image2.setOnClickListener(view -> {
            openCamera(CAMERA_REQUEST_CODE_IMAGE_2);
        });
        setOnClick(binding);
    }

    private void pushImagesToS3(VillageProperties village, double longitude, double latitude, SociaEcoSurveyFragment.OnVillageAssetAdd onVillageAssetAdd) {
        int[] ar = new int[]{0, 0};
        if (image1 != null && binding.image1.getDrawable() != null) {
            ar[1]++;
            uploadImage(village, longitude, latitude, onVillageAssetAdd, image1, ar, (BitmapDrawable) binding.image1.getDrawable());
        }
        if (image2 != null && binding.image2.getDrawable() != null) {
            ar[1]++;
            uploadImage(village, longitude, latitude, onVillageAssetAdd, image2, ar, (BitmapDrawable) binding.image2.getDrawable());
        }
        if(ar[1]==0){
            savePlaceData(village, longitude, latitude, onVillageAssetAdd);
        }
        ProgressDialog.getInstance(context).setStatus("Uploading image");
    }

    private void uploadImage(VillageProperties village, double longitude, double latitude, SociaEcoSurveyFragment.OnVillageAssetAdd onVillageAssetAdd, File file, int[] ar, BitmapDrawable bitmapDrawable) {
        ApiInterface apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
        String bucketKey = file.getName();
        apiInterface.getOrganizationPresignedURL(bucketKey, Util.getHeader()).enqueue(new Callback<PrefetchURLResponse>() {
            @Override
            public void onResponse(Call<PrefetchURLResponse> call, Response<PrefetchURLResponse> response) {
                ApiInterface apiInterface1 = ApiClient.getClient("").create(ApiInterface.class);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();

                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"),
                        imageInByte);

                if (response.body().getPreSignedUrl() == null) {
                    UIController.getInstance().displayToastMessage(context, "Cannot get prefetchURl");
                    return;
                }

                apiInterface1.putImageToS3(response.body().getPreSignedUrl(), "image/jpeg", body).enqueue(new Callback<PutImageResponse>() {
                    @Override
                    public void onResponse(Call<PutImageResponse> call, Response<PutImageResponse> response) {
                        UIController.getInstance().displayToastMessage(context, "Images Uploaded, Saving data");
                        ar[0]++;
                        ProgressDialog.getInstance(context).setStatus("Image Uploaded " + ar[0] + "/" + ar[1]);
                        if (ar[0] == ar[1])
                            savePlaceData(village, longitude, latitude, onVillageAssetAdd);
                    }

                    @Override
                    public void onFailure(Call<PutImageResponse> call, Throwable t) {
                        if (t instanceof EOFException) {
                            ar[0]++;
                            ProgressDialog.getInstance(context).setStatus("Image Uploaded " + ar[0] + "/" + ar[1]);
                            if (ar[0] == ar[1])
                                savePlaceData(village, longitude, latitude, onVillageAssetAdd);
                        } else {
                            t.printStackTrace();
                            UIController.getInstance().displayToastMessage(context, "Failed to upload image");
                            ProgressDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<PrefetchURLResponse> call, Throwable t) {
                t.printStackTrace();
                UIController.getInstance().displayToastMessage(context, "Cannot get prefetchURl");
                ProgressDialog.dismiss();
            }
        });
    }

    public static void dismiss() {
        if (alert != null)
            alert.currentDialog.dismiss();
    }

    private void savePlaceData(VillageProperties village, double longitude, double latitude, SociaEcoSurveyFragment.OnVillageAssetAdd onVillageAssetAdd) {
        ProgressDialog.getInstance(context).setStatus("Saving Place Data");
        ApiInterface apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
        CreateVillageAssetBody createVillageAssetBody = new CreateVillageAssetBody();
        createVillageAssetBody.setType("Place");

        String assetType = getSelectedAssetType(binding);
        if (assetType.equals("not available")) {
            UIController.getInstance().displayToastMessage(context, "Cant Save, Try with other asset type");
            ProgressDialog.dismiss();
            return;
        }
        PlaceProperties placeProperties = new PlaceProperties();
        placeProperties.setVillageId(/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/village.getUuid());
        placeProperties.setName(binding.editTextName.getText().toString());
        placeProperties.setLongitude(longitude);
        placeProperties.setLatitude(latitude);
        placeProperties.setAssetType(assetType);
        List<String> imageUrlList = new ArrayList<>();
        try {
            imageUrlList.add(image1.getName());
        }catch (Exception ignore){}
        try {
            imageUrlList.add(image2.getName());
        }catch (Exception ignore){}
        placeProperties.setImgUrl(imageUrlList);
        placeProperties.setRequiredSurveys(getSelectedSurveys(binding));
        Object selectedItem = binding.spinnerTypeOfAsset.getSelectedItem();
        if (selectedItem != null)
            placeProperties.setAssetSubType(selectedItem.toString());
        else
            placeProperties.setAssetSubType("");

        createVillageAssetBody.setPlaceProperties(placeProperties);
        apiInterface.createNewVillageAsset(createVillageAssetBody, Util.getIdToken(), "application/json", Util.getHeader()).enqueue(new Callback<CreatePlaceResponse>() {
            @Override
            public void onResponse(Call<CreatePlaceResponse> call, Response<CreatePlaceResponse> response) {
                currentDialog.dismiss();
                if (response.body() != null) {
                    Log.d("response", response.body().toString());
                    UIController.getInstance().displayToastMessage(context, "Village Asset Saved");
                    onVillageAssetAdd.onVillageAssetAdd();
                } else {
                    UIController.getInstance().displayToastMessage(context, "No response from server");
                }
                ProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreatePlaceResponse> call, Throwable t) {
                UIController.getInstance().displayToastMessage(context, "Could Not Save Village Asset");
                ProgressDialog.dismiss();
            }
        });
    }

    private List<SubCVillResponse.Content> getVillageListOnSelectSubC(int i, List<SubCenterResponse.Content> result, List<SubCVillResponse.Content> villageList) {
        List<SubCVillResponse.Content> villageTemp = new ArrayList<>();
        SubCenterResponse.Content content = result.get(i);
        String seletedSubC = content.getSource().getProperties().getName();
        for (SubCVillResponse.Content value : villageList) {
            if (value.getSource().getProperties().getName().equals(seletedSubC)) {
                villageTemp.add(value);
            }
        }
        villageTemp.sort(new SubCVillResponse.SubCVillComparator());
        return villageTemp;
    }

    private String getSelectedSurveys(VillagelevelmappingBinding binding3) {
        StringBuilder requiredSurveys = new StringBuilder();
        if (binding3.larvaSurvey.isChecked()) requiredSurveys.append("Larva ");
        if (binding3.waterSampleCollection.isChecked()) requiredSurveys.append("Water ");
        if (binding3.iodineTest.isChecked()) requiredSurveys.append("Iodine ");
        if (binding3.iecProgramme.isChecked()) requiredSurveys.append("IEC ");

        Log.d("requiredSurveys", requiredSurveys.toString());
        return requiredSurveys.toString();

    }

    private String getSelectedAssetType(VillagelevelmappingBinding binding3) {
        if (binding3.radioTemple.isChecked()) return "Temple";
        if (binding3.radioMosque.isChecked()) return "Mosque";
        if (binding3.radioChurch.isChecked()) return "Church";
        if (binding3.radioHotel.isChecked()) return "Hotel";
        if (binding3.radioOffice.isChecked()) return "Office";
        if (binding3.radioHospital.isChecked()) return "Hospital";
        if (binding3.radioConstruction.isChecked()) return "ConstructionArea";
        if (binding3.radioEducation.isChecked()) return "SchoolCollage";
        if (binding3.radioBusStop.isChecked()) return "BusStop";
        if (binding3.radioWaterBody.isChecked()) return "WaterBody";
        if (binding3.radioShop.isChecked()) return "Shop";
        if (binding3.radioToilet.isChecked()) return "Toilet";
        if (binding3.radioPark.isChecked()) return "Park";
        if (binding3.radioOther.isChecked()) return "OtherPlaces";

        return "not available";

    }

    private void populateSpinner(String assetType, VillagelevelmappingBinding binding) {
        String[] arraySpinner = getTypeOfAssetList(assetType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerTypeOfAsset.setAdapter(adapter);
    }

    private String[] getTypeOfAssetList(String assetType) {
        switch (assetType) {
            case "Office":
                return new String[]{
                        "Panchayat Bavan",
                        "Library/Reading Room",
                        "Common service center",
                        "Public distribution system",
                        "Primary Agriculture Credit society",
                        "Co-operation marketing society",
                        "Diary/Poultry/Fisheries co-operation society",
                        "Bank"
                };
            case "Hospital":
                return new String[]{
                        "Govt Hospital",
                        "PHC",
                        "Subcentre",
                        "Pvt Hospital",
                        "Pvt clinic"
                };
            case "SchoolCollage":
                return new String[]{
                        "Anganwadi",
                        "Nursery",
                        "Govt Primary School",
                        "Govt College",
                        "Pvt Primary school",
                        "Pvt College",
                        "Govt High School",
                        "Pvt High School"
                };
            case "WaterBody":
                return new String[]{
                        "Tap water",
                        "Well",
                        "Water Tank",
                        "Tubewell",
                        "Handpump",
                        "Canal",
                        "River",
                        "Lake",
                        "Watershed",
                        "Pond",
                        "Rainwater harvesting"
                };
            case "OtherPlaces":
                return new String[]{
                        "Petrol Bunk(examples)",
                        "Forest",
                        "Cemetery"
                };
        }
        return new String[]{};
    }

    private void setOnClick(VillagelevelmappingBinding binding) {
        setOnClickRadio(binding, binding.radioTemple);
        setOnClickRadio(binding, binding.radioMosque);
        setOnClickRadio(binding, binding.radioChurch);
        setOnClickRadio(binding, binding.radioHotel);
        setOnClickRadio(binding, binding.radioHospital);
        setOnClickRadio(binding, binding.radioOffice);
        setOnClickRadio(binding, binding.radioConstruction);
        setOnClickRadio(binding, binding.radioEducation);
        setOnClickRadio(binding, binding.radioBusStop);
        setOnClickRadio(binding, binding.radioWaterBody);
        setOnClickRadio(binding, binding.radioShop);
        setOnClickRadio(binding, binding.radioToilet);
        setOnClickRadio(binding, binding.radioPark);
        setOnClickRadio(binding, binding.radioOther);
    }

    private int CLICK_ACTION_THRESHOLD = 200;
    private float startX;
    private float startY;

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickRadio(VillagelevelmappingBinding binding, RadioButton button) {
        button.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (isAClick(startX, endX, startY, endY)) {
                        button.callOnClick();
                        populateSpinner(getSelectedAssetType(binding), binding);
                    }
                    break;
            }
            return true;
        });
    }

    public String generateBucketKey() {
        return UUID.randomUUID().toString();
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }

    private void openCamera(int requestCode) {
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
        //File file = getOutputMediaFile(requestCode == CAMERA_REQUEST_CODE_IMAGE_1 ? 1 : 2);
        File file = CameraUtility.createImageFile(context);

        if (file == null) {
            UIController.getInstance().displayToastMessage(context, "Cant write on the external storage, please check permission");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (requestCode == CAMERA_REQUEST_CODE_IMAGE_1) {
            image1 = file;
            image1Uri = FileProvider.getUriForFile(context.getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    image1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image1Uri);
        } else if (requestCode == CAMERA_REQUEST_CODE_IMAGE_2) {
            image2 = file;
            image2Uri = FileProvider.getUriForFile(context.getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    image2);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image2Uri);
        }
        currentFragment.startActivityForResult(intent, requestCode);
    }

    public void onCameraResult(int requestCode, @NonNull Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE_IMAGE_1) {
            binding.image1.setImageURI(image1Uri);
        } else if (requestCode == CAMERA_REQUEST_CODE_IMAGE_2) {
            binding.image2.setImageURI(image2Uri);
        }
    }
}