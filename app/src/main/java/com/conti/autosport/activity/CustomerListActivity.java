package com.conti.autosport.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.adapters.CustomerAdapter;
import com.conti.autosport.model.Customer;
import com.conti.autosport.util.ConnectionInterface;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    ArrayList<Customer> mCustomers = new ArrayList<>();
    CustomerAdapter mAdapter = null;
    ProgressDialog mProgress;
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.listView);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getResources().getString(
                R.string.carregando_clientes));
        new LoadCustomersTask(mProgress).execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mProgress.dismiss();
    }

    private int loadList() {
        ConnectionInterface conn = new ConnectionInterface(this);

        if (conn.getClientes() != null) {
            mCustomers = conn.getClientes();

            if (mCustomers.size() == 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }

    }

    private void updateUI() {
        mAdapter = new CustomerAdapter(getApplicationContext(), mCustomers);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent data = new Intent();
                data.putExtra(Customer.ID, mAdapter.getFilteredClients().get(arg2)
                        .getId());
                data.putExtra(Customer.NAME, mAdapter.getFilteredClients().get(arg2)
                        .getName());
                setResult(PreOrderFormActivity.RESULT_CODE, data);
                finish();
            }
        });

        EditText txtSearch = findViewById(R.id.editSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mAdapter.getFilter().filter(s.toString());
                mList.setAdapter(mAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class LoadCustomersTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressdialog;
        int result;

        public LoadCustomersTask(ProgressDialog progress) {
            this.progressdialog = progress;
        }

        public void onPreExecute() {
            this.progressdialog.show();
        }

        public void onPostExecute(Void unused) {
            this.progressdialog.dismiss();
            switch (result) {
                case 1:
                    finish();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.sem_clientes), Toast.LENGTH_LONG)
                            .show();
                    break;

                case 2:
                    finish();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.erro_clientes), Toast.LENGTH_LONG)
                            .show();
                    break;

                default:
                    break;
            }

            updateUI();
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = loadList();
            return null;
        }
    }

}
