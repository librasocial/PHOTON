package com.ssf.homevisit.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.ssf.homevisit.requestmanager.AppDefines;

public class UIController implements AppDefines {

    //singleton instance
    private static UIController instance;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static UIController getInstance() {
        if (instance == null) {
            synchronized (UIController.class) {
                if (instance == null) {
                    instance = new UIController();
                }
            }
        }
        return instance;
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void displayToastMessage(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
