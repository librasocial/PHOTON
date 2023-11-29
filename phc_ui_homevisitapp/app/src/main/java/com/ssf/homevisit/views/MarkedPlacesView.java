package com.ssf.homevisit.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.ssf.homevisit.R;


public class MarkedPlacesView extends LinearLayout {
    private final TextView assetTypeText;
    private final TextView assetCountText;
    private final View pointerView;

    public MarkedPlacesView(Context context) {
        this(context, null);
    }

    public MarkedPlacesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ll = inflater.inflate(R.layout.layout_marked_place_view, this, true);

        pointerView = ll.findViewById(R.id.pointer_view);
        assetCountText = ll.findViewById(R.id.asset_count_text);
        assetTypeText = ll.findViewById(R.id.asset_type_text);

        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MarkedPlaceOptions, 0, 0);
        String assetType = a.getString(R.styleable.MarkedPlaceOptions_assetType);
        setAssetType(assetType);
        int color = a.getColor(R.styleable.MarkedPlaceOptions_color, Color.GREEN);
        setColor(color);

        int assetCount = a.getInt(R.styleable.MarkedPlaceOptions_assetCount, 0);
        setAssetCount(assetCount);

        a.recycle();
        setOrientation(LinearLayout.HORIZONTAL);

    }

    public void setColor(int color) {
        assetTypeText.setTextColor(color);
        assetCountText.setTextColor(color);

        pointerView.setBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{color}));
//        pointerView.setBackgroundT(color);
    }

    public void setAssetType(String assetType) {
        assetTypeText.setText(assetType);
    }

    @SuppressLint("SetTextI18n")
    public void setAssetCount(int count) {
        assetCountText.setText(Integer.toString(count));
    }
}

