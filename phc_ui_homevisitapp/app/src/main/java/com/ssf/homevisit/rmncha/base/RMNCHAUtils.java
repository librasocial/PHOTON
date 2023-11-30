package com.ssf.homevisit.rmncha.base;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;
import com.ssf.homevisit.R;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.GetImageUrlResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RMNCHAUtils {

    public static final String RMNCHA_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String getResidentStatus(String value) {
        if (value == null || value.length() < 1)
            return "Not Available";

        switch (value) {
            case "Resides":
                return "Resident";
            case "Migrant":
                return "Migrant";
            case "Guest":
                return "Guest";
            default:
                return "Not Available";
        }
    }

    public static String setNonNullValue(String value) {
        if (value == null || value.isEmpty())
            return "Not available";
        else {
            value.length();
            return value;
        }
    }


    public static String getTimeStampFrom(String dateString, String timeString) {
        SimpleDateFormat resultFormat = new SimpleDateFormat(RMNCHA_DATE_FORMAT);
        String result = null;
        if (timeString != null && timeString.length() < 3) timeString = "00:00";
        try {
            /*Date dateTime = new SimpleDateFormat("yyyy/MM/ddThh:mm a").parse(dateString + "T" + timeString);
            result = resultFormat.format(dateTime.getTime());*/
            result = dateString + "T" + timeString + ":00.000";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCurrentDate() {
        SimpleDateFormat resultFormat = new SimpleDateFormat(RMNCHA_DATE_FORMAT);
        String result = null;
        try {
            result = resultFormat.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCurrentFinancialYear() {
        String result = null;
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            result = year + " - " + (year + 1 - 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCurrentYear() {
        String result = null;
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            result = year + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDateToView(String dateString, String currentPattern) {
        String result = null;
        try {
            SimpleDateFormat currentFormat = new SimpleDateFormat(currentPattern);
            SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = currentFormat.parse(dateString);
            result = resultFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setNonNullValue(result);
    }

    public static String getTimeToView(String dateString, String currentPattern) {
        SimpleDateFormat currentFormat = new SimpleDateFormat(currentPattern);
        Date date;
        String result = null;
        try {
            date = currentFormat.parse(dateString);
            result = date.getHours() + ":" + date.getMinutes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setNonNullValue(result);
    }

    public static String getAgeFromDOB(String str) {
        SimpleDateFormat ageFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dobDate;
        String resultAge = "Not available";
        try {
            dobDate = ageFormat.parse(str);
            int years = Calendar.getInstance().getTime().getYear() - dobDate.getYear();
            resultAge = years + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultAge;
    }


    public static Double getBabyAgeFromDOB(String dateOfBirth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date2 = new Date();
        Date parsedDate1 = null;

        try {
            parsedDate1 = dateFormat.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - parsedDate1.getTime();
        long millisecondsPerDay = 1000 * 60 * 60 * 24;
        return (double) (diff / millisecondsPerDay);
    }

    public static String getBabyAgeInWeeks(Double days) {
        double weeks = days / 7;
        double months = days / 30;
        double years = days / 365;
        if (days <= 42) {
            return String.valueOf(Math.round(days) + " Days");
        } else if (weeks <= 14) {
            return String.valueOf(Math.round(weeks) + " Weeks");
        } else if (months <= 60) {
            return String.valueOf(Math.round(months) + " Months");
        } else {
            return String.valueOf(Math.round(years) + " Years");
        }
    }

    public static int getHusbandAge(String womenDOBString, String menDOBString, int womenAgeAtMarriage) {
        SimpleDateFormat ageFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date womenDOB;
        Date menDOB;
        int husbandAge = 0;
        if (womenAgeAtMarriage > 0) {
            try {
                womenDOB = ageFormat.parse(womenDOBString);
                menDOB = ageFormat.parse(menDOBString);
                int differenceAge = womenDOB.getYear() - menDOB.getYear();
                husbandAge = womenAgeAtMarriage + differenceAge;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return husbandAge;
    }

    public static TextWatcher getTextChangeListener(List<Integer> dashIndexes, String dividerString) {
        return new TextWatcher() {
            private boolean isFormatting = false;
            private boolean deletingHyphen = false;
            private int hyphenStart = 0;
            private boolean deletingBackward = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isFormatting)
                    return;

                // Make sure user is deleting one char, without a selection
                int selStart = Selection.getSelectionStart(s);
                int selEnd = Selection.getSelectionEnd(s);

                if (s.length() > 1 // Can delete another character
                        && count - after == 1 // deleting only 1 character
                        && s.charAt(selStart - 1) == '-' // a hyphen
                        && selStart == selEnd
                ) { // no selection
                    deletingHyphen = true;
                    hyphenStart = selStart - 1;
                    // Check if the user is deleting forward or backward
                    deletingBackward = selStart == (selStart - 1) + 1;
                } else {
                    deletingHyphen = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFormatting)
                    return;

                isFormatting = true;

                // If deleting hyphen, also delete character before or after it
                if (deletingHyphen && hyphenStart > 0) {
                    if (deletingBackward) {
                        if (hyphenStart - 1 < editable.length()) {
                            editable.delete(hyphenStart - 1, hyphenStart);
                        }
                    } else if (hyphenStart < editable.length()) {
                        editable.delete(hyphenStart, hyphenStart + 1);
                    }
                }

                if (!deletingHyphen &&
                        dashIndexes.contains(editable.toString().length())
                ) {
                    editable.append(dividerString);
                }

                isFormatting = false;
            }

        };
    }

    public static ArrayList<String> getAshaWorkerNamesList(List<AshaWorkerResponse.Content> ashaWorkersList) {
        ArrayList<String> menList = new ArrayList<>();
        menList.add("Select");
        for (AshaWorkerResponse.Content member : ashaWorkersList) {
            AshaWorkerResponse.Content.TargetNode.TargetProperties targetProperties = member.getTargetNode().getProperties();
            String item = targetProperties.getName();
            menList.add(item);
        }
        return menList;
    }

    public static void hideSoftInputKeyboard(Context context, View view) {
        /** Hiding keyboard if already opened **/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void loadMemberImage(String imageURL, ImageView view) {
        ApiInterface apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
        final String[] secureImageUrl = {AppDefines.BASE_URL + imageURL};
        apiInterface.getMembershipImageUrl(imageURL, Util.getHeader()).enqueue(new Callback<GetImageUrlResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetImageUrlResponse> call, @NotNull Response<GetImageUrlResponse> response) {
                if (response.body() != null)
                    secureImageUrl[0] = response.body().getPreSignedUrl();

                Picasso.get()
                        .load(secureImageUrl[0])
                        .placeholder(R.drawable.ic_placeholder)
                        .into(view);
            }

            @Override
            public void onFailure(@NotNull Call<GetImageUrlResponse> call, @NotNull Throwable t) {

            }
        });

        Picasso.get()
                .load(secureImageUrl[0])
                .placeholder(R.drawable.ic_placeholder)
                .into(view);
    }

    public static void showMyToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void getTimeFromTimePicker(TextView textView) {
        getTimeFromTimePicker(textView, null);
    }

    public static void getTimeFromTimePicker(TextView textView, TimeChangeListener listener) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(textView.getContext(),
                (view, hourOfDay, minute) -> {

                    String formattedTime;

                    String h = hourOfDay + "";
                    String m = minute + "";

                    if (h.length() == 1)
                        h = "0" + h;
                    if (m.length() == 1)
                        m = "0" + m;

                    formattedTime = h + ":" + m;

                    /*if (hourOfDay == 0) {
                        if (minute < 10) {
                            formattedTime = (hourOfDay + 12) + ":0" + minute + " am";
                        } else {
                            formattedTime = (hourOfDay + 12) + ":" + minute + " am";
                        }
                    } else if (hourOfDay > 12) {
                        if (minute < 10) {
                            formattedTime = (hourOfDay - 12) + ":0" + minute + " pm";
                        } else {
                            formattedTime = (hourOfDay - 12) + ":" + minute + " pm";
                        }
                    } else if (
                            hourOfDay == 12) {
                        if (minute < 10) {
                            formattedTime = hourOfDay + ":0" + minute + " pm";
                        } else {
                            formattedTime = hourOfDay + ":" + minute + " pm";
                        }
                    } else {
                        if (minute < 10) {
                            formattedTime = hourOfDay + ":" + minute + " am";
                        } else {
                            formattedTime = hourOfDay + ":" + minute + " am";
                        }
                    }*/

                    textView.setText(formattedTime);
                    if (listener != null) {
                        listener.onTimeChanged(formattedTime);
                    }

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
        timePickerDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // timePickerDialog.getWindow().setLayout(600, 400);
    }

    public static void getDateFromDatePicker(TextView textView) {
        getDateFromDatePicker(textView, null);
    }

    public static void getDateFromDatePicker(TextView textView, DateChangeListener listener) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(textView.getContext(), (arg0, y, m, d) -> {
            String month = (m + 1) + "";
            String day = d + "";
            if ((m + 1) < 10)
                month = "0" + month;
            if (d < 10)
                day = "0" + day;

            String date = y + "/" + month + "/" + day;

            textView.setText(date);
            if (listener != null)
                listener.onDateChanged(y, (m + 1), d);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        datePicker.show();
        datePicker.getWindow().setLayout(600, 400);
    }

    public static String getExpectedDeliveryDate(int year, int month, int day) {

        Calendar edd = Calendar.getInstance();
        edd.set(year, (month - 1), day);
        edd.add(Calendar.DATE, 7);
        edd.add(Calendar.MONTH, 9);

        String mm = (edd.get(Calendar.MONTH) + 1) + "";
        String dd = edd.get(Calendar.DAY_OF_MONTH) + "";

        if (mm.length() < 2)
            mm = "0" + mm;
        if (dd.length() < 2)
            dd = "0" + dd;

        String eddDate = edd.get(Calendar.YEAR) + "/" + mm + "/" + dd;
        return eddDate;
    }

    public interface DateChangeListener {
        void onDateChanged(int year, int month, int day);
    }

    public interface TimeChangeListener {
        void onTimeChanged(String time);
    }

    public static void showSelectComplicationsAlertDialogue(Context context, String[] complicationList, boolean[] list, TextView textView, TextView textView2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMultiChoiceItems(complicationList, list, (dialog, which, isChecked) -> {
            list[which] = isChecked;
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            textView.setText(getSelectedValues(complicationList, list));
            if (textView2 != null)
                textView2.setText(getSelectedValues(complicationList, list));
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.getWindow().setLayout(400, 500);
    }

    public static boolean[] getDummyBoolList(int length) {
        boolean[] dummyList = new boolean[length];
        for (int i = 0; i < length; i++)
            dummyList[i] = false;
        return dummyList;
    }

    public static String getSelectedValues(String[] complicationList, boolean[] list) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (list[i]) {
                value.append(complicationList[i]);
                if (i < list.length)
                    value.append(", ");
            }
        }
        return value.substring(0, value.length() - 2);
    }


}
