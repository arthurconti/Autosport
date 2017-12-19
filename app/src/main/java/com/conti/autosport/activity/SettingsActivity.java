package com.conti.autosport.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.util.ConnectionInterface;


public class SettingsActivity extends AppCompatActivity {

    private EditText editIP;
    private Button btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setEnabled(false);

        editIP = (EditText) findViewById(R.id.txtIP);

        editIP.setText(prefs.getString("IP", ""));
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        editIP.setFilters(filters);

    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                String ip = editIP.getText().toString().trim();
                if (ip.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "IP Vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Testando IP:" + ip);
                    new MyTask(dialog, ip).execute();
                }
                break;
            case R.id.btnSave:
                prefs.edit().putString("IP", editIP.getText().toString().trim()).commit();
                finish();
                break;
            default:
                break;
        }

    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressdialog;
        int result;
        String ip;

        public MyTask(ProgressDialog progress, String ip) {
            this.progressdialog = progress;
            this.ip = ip;
        }

        public void onPreExecute() {
            this.progressdialog.show();
        }

        public void onPostExecute(Void unused) {
            this.progressdialog.dismiss();
            switch (result) {
                case 1:
                    Toast.makeText(getApplicationContext(), "Conexão realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Conexão falhou! Teste outro IP.", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectionInterface conn = new ConnectionInterface(getApplicationContext(), ip);

            if (conn.getClientes() != null) {
                result = 1;
            } else {
                result = 2;
            }

            return null;
        }
    }
}
