package com.ssf.homevisit.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.ssf.homevisit.R;
import com.ssf.homevisit.app.AndroidApplication;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.fragment.LandingFragment;
import com.ssf.homevisit.models.Content;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.pnc.mapping.PNCMappingFragment;
import com.ssf.homevisit.rmncha.pnc.pncalert.PNCAlert;
import com.ssf.homevisit.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private MainViewModel mainViewModel;
    DatePickerDialog datePickerDialog;
    Content content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppController.getInstance().setMainActivity(this);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        content = new Content();
        checkPermissions();
        int fragmentToShow = getIntent().getIntExtra("PNC_MAPPING_FRAGMENT", 1);
        if(fragmentToShow==2){
            PNCMappingFragment fragment = new PNCMappingFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_landing, fragment);
            transaction.commit();
        };
        callDefaultFragment();
        TextView viewById = findViewById(R.id.dropdown_menu);
        TextView view = viewById;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.profile_options_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_logout) {
            logout();
        }
        return super.onContextItemSelected(item);
    }

    private void checkPermissions() {
        int fine_location = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int course_location = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }
        if (course_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void callDefaultFragment() {
        replaceFragment(new LandingFragment(), null);
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void onBackPress(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            UIController.getInstance().displayToastMessage(this, "Cant go back");
        }
    }

    private void logout() {
        mainViewModel.logOut();
        //old codes
        SharedPreferences.Editor editor = AndroidApplication.prefs.edit();
        editor.putString(AppDefines.ACCESS_TOKEN, null);
        editor.putString(AppDefines.ID_TOKEN, null);
        editor.putString(AppDefines.REFRESH_TOKEN, null);
        editor.putInt(AppDefines.TOKEN_EXPIRE, 0);
        editor.putString(AppDefines.TOKEN_TYPE, null);
        editor.putBoolean(AppDefines.IS_LOGIN, false);
        editor.apply();


        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        MainActivity.this.finish();
    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void initDatePicker(TextView datePickerText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerText.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    public String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    public String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void setActionBarVisibility(int visibility){
        findViewById(R.id.toolbar).setVisibility(visibility);
    }
}