package com.ssf.homevisit.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.TextView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;

public class ProgressDialog {
    private final Context context;
    private static ProgressDialog alert;
    private Dialog currentDialog;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static ProgressDialog getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (ProgressDialog.class) {
                if (alert == null) {
                    alert = new ProgressDialog(mcontext);
                }
            }
        }
        return alert;
    }

    public ProgressDialog(Context context) {
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    public void display() {
        if((currentDialog != null)&& currentDialog.isShowing()){
            currentDialog.dismiss();
        } currentDialog = null;
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
//        AlertLayoutMappingBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.progress_bar_layout, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_bar_layout);
        dialog.show();
        currentDialog = dialog;
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if (!dialog.isShowing())
                    dialog.show();
            }

            @Override
            public void onFinish() {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }.start();
    }
    @SuppressLint("SetTextI18n")
    public void display1() {
        if((currentDialog != null)&& currentDialog.isShowing()){
            currentDialog.dismiss();
        }
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
//        AlertLayoutMappingBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.progress_bar_layout, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_bar_layout);
        dialog.show();
        currentDialog = dialog;
       new CountDownTimer(6200,1000){

           @Override
           public void onTick(long millisUntilFinished) {
         if (!dialog.isShowing())
             dialog.show();
           }

           @Override
           public void onFinish() {
               if (dialog.isShowing())
                 dialog.dismiss();
           }
       }.start();
    }

    public void setStatus(String status) {
        ((TextView) currentDialog.findViewById(R.id.status)).setText(status);
    }

    public static void dismiss() {
        if (alert != null) {
            alert.currentDialog.dismiss();
        }
    }

    public void setCancellable(boolean b) {
        currentDialog.setCancelable(true);
    }
}
