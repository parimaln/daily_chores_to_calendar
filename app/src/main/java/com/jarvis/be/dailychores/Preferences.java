package com.jarvis.be.dailychores;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.psdev.licensesdialog.LicensesDialogFragment;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;


public class Preferences extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Button btn = (Button) findViewById(R.id.open_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Notices notices = new Notices();
                notices.addNotice(new Notice("Material Dialogs", "https://github.com/afollestad/material-dialogs", "Copyright (c) 2015 Aidan Michael Follestad", new MITLicense()));
                notices.addNotice(new Notice("FloatingActionButton", "https://github.com/futuresimple/android-floating-action-button", "Copyright (C) 2014 Jerzy Chalupski", new ApacheSoftwareLicense20()));

                final LicensesDialogFragment fragment = LicensesDialogFragment.newInstance(notices, false, true);
                fragment.show(getSupportFragmentManager(), null);

            }
        });
    }


}
