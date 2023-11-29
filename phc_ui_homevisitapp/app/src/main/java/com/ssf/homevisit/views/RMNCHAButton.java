package com.ssf.homevisit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssf.homevisit.R;

public class RMNCHAButton extends FrameLayout {
    private TextView customtext;
    private ImageView customimage;
    private RelativeLayout custombgview;

    public RMNCHAButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RMNCHAButton, 0, 0);
        String text = typedArray.getString(R.styleable.RMNCHAButton_ButtonTxt);
        text = text == null ? "" : text;
        Drawable imageFile = typedArray.getDrawable(R.styleable.RMNCHAButton_ButtonImg);
        Drawable gradientBg = typedArray.getDrawable(R.styleable.RMNCHAButton_ButtonBgColor);
        try {
            setText(text);
            setImage(imageFile);
            setBackground(gradientBg);
        } finally {
            typedArray.recycle();
        }
    }

    public void init(Context context) {
        inflate(context, R.layout.layout_rmncha_button, this);
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
