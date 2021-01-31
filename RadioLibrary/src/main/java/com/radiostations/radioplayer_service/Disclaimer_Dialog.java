package com.radiostations.radioplayer_service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.radiostations.R;

public class Disclaimer_Dialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("Disclaimer")
                .setMessage(getString(R.string.app_disclaimer))
                .setPositiveButton("OK", (dialogInterface, i) -> {

                });

        return builder.create();
    }
}