package com.ssf.homevisit.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import com.ssf.homevisit.R;

import java.util.ArrayList;
import java.util.List;

public class FemaleECCustomCardLayout extends FrameLayout {
    private TextView ecName,ecAge,ecPhoneno,ecDOB,ecStatus,ecHealthIdNumber;
    private ImageView ecPhoto;
    private String ecName1,ecAge1,ecPhone,ecDOB1,ecStatus1,ecHID;

    public FemaleECCustomCardLayout(Context context) {
        super(context);
        initView();
    }

    public FemaleECCustomCardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FemaleECCustomCardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_ses_quetion, this);
        ecName = findViewById(R.id.ecName);
        ecAge = findViewById(R.id.ecAge);
        ecPhoneno = findViewById(R.id.ecPhoneNo);
        ecDOB = findViewById(R.id.ecDOB);
        ecStatus = findViewById(R.id.ecStatus);
        ecPhoto = findViewById(R.id.ecPhoto);
    }

    public void setECName(String ecName1) {
        ecName.setText(ecName1);
    }
    public void setECAGE(String ecAGE1,String Gender) {
        ecAge.setText(ecAGE1+" "+Gender);
    }
    public void setECPhone(String ecPHno1) {
        ecPhoneno.setText("Ph : "+ecPHno1);
    }
    public void setECDOB(String ecDOB1) {
        ecDOB.setText("DOB : "+ecDOB1);
    }
    public void setECStatus(String ecStatus1) {
        ecStatus.setText("Staus : "+ecStatus1);
    }
    public void setHiid(String ecHiid1) {
        ecHealthIdNumber.setText(ecHiid1);
    }

//    public void setPhoto(ImageView photo1){
//        ecPhoto.setImageResource(photo1);
//    }


}
