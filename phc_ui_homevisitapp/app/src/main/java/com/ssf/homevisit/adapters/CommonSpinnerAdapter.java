package com.ssf.homevisit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;

import java.util.List;

public class CommonSpinnerAdapter extends BaseAdapter {
    private Context context;
    List<SubcentersFromPHCResponse.Content> subCenterFromPhcList;
    List<AllPhcResponse.Content> allPhcResponseList;
    List<SubCenterResponse.Content> subCenterList;
    List<SubCVillResponse.Content> villageList;
    LayoutInflater inflter;
    Object object;

    public CommonSpinnerAdapter(AllPhcResponse allPhcResponse, List<AllPhcResponse.Content> content, Context context, List<AllPhcResponse.Content> allPhcResponseList) {
        this.context = context;
        this.allPhcResponseList = allPhcResponseList;
        inflter = (LayoutInflater.from(context));
        this.object = allPhcResponse;
    }

    public CommonSpinnerAdapter(SubCenterResponse subCenterResponse, Context context, List<SubCenterResponse.Content> subCenterList) {
        this.context = context;
        this.subCenterList = subCenterList;
        inflter = (LayoutInflater.from(context));
        this.object = subCenterResponse;
    }

    public CommonSpinnerAdapter(SubCVillResponse subCVillResponse, Context context, List<SubCVillResponse.Content> villageList) {
        this.context = context;
        this.villageList = villageList;
        for (int i = 0; i < villageList.size(); i++) {
            if (villageList.get(i) == null) {
                villageList.remove(i);
            } else if (villageList.get(i).getTarget().getVillageProperties().getUuid() == null || villageList.get(i).getTarget().getVillageProperties().getUuid().equals("")) {
                villageList.remove(i);
                Log.d("removing", villageList.get(i).getTarget().getVillageProperties().getName());
            } else {
                Log.d("hhhh", villageList.get(i).getTarget().getVillageProperties().getName()+villageList.get(i).getTarget().getVillageProperties().getUuid());
            }
        }
        inflter = (LayoutInflater.from(context));
        this.object = subCVillResponse;
    }

    public CommonSpinnerAdapter(SubcentersFromPHCResponse subcentersFromPHCResponse, Context context, List<SubcentersFromPHCResponse.Content> currentSubcenters) {
        this.context = context;
        this.subCenterFromPhcList = currentSubcenters;
        for (int i = 0; i < currentSubcenters.size(); i++) {
            if (currentSubcenters.get(i) == null) {
                currentSubcenters.remove(i);
            }
        }
        inflter = (LayoutInflater.from(context));
        this.object = subcentersFromPHCResponse;
    }

    @Override
    public int getCount() {
        if (object instanceof AllPhcResponse)
            return allPhcResponseList.size();
        else if (object instanceof SubCenterResponse)
            return subCenterList.size();
        else if (object instanceof SubcentersFromPHCResponse)
            return subCenterFromPhcList.size();
        else
            return villageList.size();
    }

    @Override
    public Object getItem(int i) {
        if (object instanceof AllPhcResponse)
            return allPhcResponseList.get(i);
        else if (object instanceof SubCenterResponse)
            return subCenterList.get(i);
        else if (object instanceof SubcentersFromPHCResponse)
            return subCenterFromPhcList.get(i);
        else
            return villageList.get(i);
    }

    public String getUuid(int i) {
        if (object instanceof AllPhcResponse)
            return allPhcResponseList.get(i).getProperties().getUuid();
        else if (object instanceof SubCenterResponse)
            return subCenterList.get(i).getTarget().getProperties().getUuid();
        else if (object instanceof SubcentersFromPHCResponse)
            return subCenterFromPhcList.get(i).getTarget().getProperties().getUuid();
        else
            return villageList.get(i).getTarget().getVillageProperties().getUuid();
    }

    public String getName(int i) {
        if (object instanceof AllPhcResponse)
            return allPhcResponseList.get(i).getProperties().getName();
        else if (object instanceof SubCenterResponse)
            return subCenterList.get(i).getTarget().getProperties().getName();
        else if (object instanceof SubcentersFromPHCResponse)
            return subCenterFromPhcList.get(i).getTarget().getProperties().getName();
        else
            return villageList.get(i).getTarget().getVillageProperties().getName();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (object instanceof AllPhcResponse) {
            view = inflter.inflate(R.layout.spinner_text_layout, null);
            TextView names = (TextView) view.findViewById(R.id.textView);

            AllPhcResponse.Content content = allPhcResponseList.get(i);
            names.setText(content.getProperties().getPhc());
        } else if (object instanceof SubCenterResponse) {
            view = inflter.inflate(R.layout.spinner_text_layout, null);
            TextView names = (TextView) view.findViewById(R.id.textView);

            SubCenterResponse.Content content = subCenterList.get(i);
            names.setText(content.getSource().getProperties().getName());
        } else if (object instanceof SubcentersFromPHCResponse) {
            view = inflter.inflate(R.layout.spinner_text_layout, null);
            TextView names = (TextView) view.findViewById(R.id.textView);

            SubcentersFromPHCResponse.Content content = subCenterFromPhcList.get(i);
            names.setText(content.getTarget().getProperties().getName());
        } else {
            view = inflter.inflate(R.layout.spinner_text_layout, null);
            TextView names = (TextView) view.findViewById(R.id.textView);

            SubCVillResponse.Content content = villageList.get(i);
            names.setText(content.getTarget().getVillageProperties().getName());
        }
        return view;
    }
}
