package com.ssf.homevisit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssf.homevisit.R;

public class LandingPageBtn extends FrameLayout {
    private TextView customtext;
    private ImageView customimage;
    private LinearLayout custombgview;

    public LandingPageBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LandingPageBtn, 0, 0);
        String text = typedArray.getString(R.styleable.LandingPageBtn_BtnTxt);
        text = text == null ? "" : text;
        Drawable imageSrc = typedArray.getDrawable(R.styleable.LandingPageBtn_BtnImg);
        Drawable backgroundColor = typedArray.getDrawable(R.styleable.LandingPageBtn_BtnBgColor);
        try {
            setText(text);
            setImage(imageSrc);
            setBackground(backgroundColor);
        } finally {
            typedArray.recycle();
        }
    }

    public void init(Context context) {
        inflate(context, R.layout.layout_landingpage_btn, this);
        customtext = findViewById(R.id.btn_text);
        customimage = findViewById(R.id.btn_img);
        custombgview = findViewById(R.id.btn_view);
    }

    public void setText(String text) {
        customtext.setText(text);
    }

    public void setImage(Drawable imageFile) {
        customimage.setImageDrawable(imageFile);
    }

    public void setBackground(Drawable gradient) {
        custombgview.setBackground(gradient);
    }
}
