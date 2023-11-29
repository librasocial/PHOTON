package com.ssf.homevisit.rmncha.base;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaSaveDraftBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaSavedSuccessBinding;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
import com.ssf.homevisit.models.RMNCHAServiceStatus;

import java.util.List;

public abstract class RMNCHABaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void showProgressBar();

    protected abstract void hideProgressBar();

    protected void showExitWithoutSavingAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaSavedSuccessBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_saved_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile_error));
        binding.message.setText("Would you like to exit without saving?");
        binding.buttonExit.setText("Exit");
        binding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        binding.buttonExit.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
    }

    protected void showSaveSuccessAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaSavedSuccessBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_saved_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.message.setText("Form has been saved successfully!");
        binding.buttonExit.setText("Click here to Continue");
        binding.closeDialogue.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        binding.buttonExit.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
    }

    private void showDraftSavedAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaSavedSuccessBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_saved_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.message.setText("Draft has been saved successfully!");
        binding.buttonExit.setText("Exit");
        binding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        binding.buttonExit.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
    }

    void showSaveAsDraftBeforeExitingAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaSaveDraftBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_save_draft, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.buttonClear.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        binding.buttonSave.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

}
