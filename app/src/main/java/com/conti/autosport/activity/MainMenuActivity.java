package com.conti.autosport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.crashlytics.android.Crashlytics;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main_menu);

        File file = new File(Environment.getExternalStorageDirectory(),
                getResources().getString(R.string.folder_name));
        if (!file.exists()) {
            if (!file.mkdirs()) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OrcamentoManager.getInstance().initialize(this);
    }

    public void buttonClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnEntrada:
                startActivity(new Intent(getApplicationContext(),
                        SearchEntryActivity.class));
                this.overridePendingTransition(R.anim.anim_slidein_left,
                        R.anim.anim_slideout_left);
                break;

            case R.id.btnOrcamento:
                startActivity(new Intent(getApplicationContext(),
                        InvoiceActivity.class));
                this.overridePendingTransition(R.anim.anim_slidein_left,
                        R.anim.anim_slideout_left);
//                OrcamentoManager.getInstance().setParte(Parte.SUPERIOR);
//                startActivity(new Intent(getApplicationContext(),
//                        ServicoActivity.class));
//                this.overridePendingTransition(R.anim.anim_slidein_left,
//                        R.anim.anim_slideout_left);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(Environment.getExternalStorageDirectory(),
                getResources().getString(R.string.folder_name));
        deleteDirectory(file);
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
