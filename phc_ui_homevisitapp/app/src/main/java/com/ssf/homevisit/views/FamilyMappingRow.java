package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ssf.homevisit.R;
import com.ssf.homevisit.models.ResidentProperties;

import java.util.ArrayList;

public class FamilyMappingRow extends FrameLayout {
    private Context context;
    private View view;
    private TextView residentNameTV;
    private Spinner relationshipSpinner;
    private ResidentProperties residentProperties;
    private ArrayList<String> relationships;

    public FamilyMappingRow(@NonNull Context context) {
        super(context);
        init(context);
    }

    public FamilyMappingRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FamilyMappingRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FamilyMappingRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_family_mapping_row, this, true);
        residentNameTV = findViewById(R.id.name_of_resident);
        relationshipSpinner = findViewById(R.id.relationship_status);
        getListOfRelationships();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_item, relationships);
        relationshipSpinner.setAdapter(arrayAdapter);
        relationshipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (residentProperties != null) {
                    residentProperties.setRelationshipWithHead(relationships.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (residentProperties != null) {
                    residentProperties.setRelationshipWithHead(null);
                }
            }
        });
    }

    public void setResidentProperties(ResidentProperties residentProperties) {
        this.residentProperties = residentProperties;
        residentNameTV.setText(residentProperties.getFirstName());
        if (residentProperties.getRelationshipWithHead() != null) {
            relationshipSpinner.setSelection(getIndexOfRelationships(residentProperties.getRelationshipWithHead()));
        }
    }

    private void getListOfRelationships() {
        relationships = new ArrayList<String>();
        relationships.add("Wife");
        relationships.add("Daughter");
        relationships.add("Son");
        relationships.add("Father");
        relationships.add("Mother");
        relationships.add("Brother");
        relationships.add("Sister");
        relationships.add("Husband");
    }

    private int getIndexOfRelationships(String relation) {
        return relationships.indexOf(relation);
    }

    public ResidentProperties getResidentProperties() {
        return residentProperties;
    }
}
