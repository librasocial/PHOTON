package com.ssf.homevisit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;

import java.util.List;

public class LayoutAdapter extends BaseAdapter {
    private Context context;
    static List<AllPhcResponse.Content> allPhcResponseList;
    List<SubCenterResponse.Content>subCenterList;
    List<SubCVillResponse.Content>villageList;
    LayoutInflater inflter;
    Object object;

    public LayoutAdapter(AllPhcResponse allPhcResponse, List<AllPhcResponse.Content> content, Context context, List<AllPhcResponse.Content> allPhcResponseList) {
        this.context = context;
        this.allPhcResponseList = allPhcResponseList;
        inflter = (LayoutInflater.from(context));
        this.object = allPhcResponse;
    }

    public LayoutAdapter(SubCenterResponse subCenterResponse, Context context, List<SubCenterResponse.Content> subCenterList) {
        this.context = context;
        this.subCenterList = subCenterList;
        inflter = (LayoutInflater.from(context));
        this.object = subCenterResponse;
    }

    public LayoutAdapter(SubCVillResponse subCVillResponse, Context context, List<SubCVillResponse.Content> villageList) {
        this.context = context;
        this.villageList = villageList;
        inflter = (LayoutInflater.from(context));
        this.object = subCVillResponse;
    }

    @Override
    public int getCount() {
        if (object instanceof AllPhcResponse)
            return allPhcResponseList.size();
        else if (object instanceof SubCenterResponse)
            return subCenterList.size();
        else
            return villageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.toolbar_layout, null);
        TextView names = (TextView) view.findViewById(R.id.ussrname);

        if (object instanceof AllPhcResponse) {
            AllPhcResponse.Content content = allPhcResponseList.get(i);
            names.setText(content.getProperties().getName());
        }
        else if (object instanceof SubCenterResponse)
        {
            SubCenterResponse.Content content = subCenterList.get(i);
            names.setText(content.getSource().getProperties().getName());
        }
        else {
            SubCVillResponse.Content content = villageList.get(i);
            names.setText(content.getTarget().getVillageProperties().getName());
        }
        return view;
    }

}
