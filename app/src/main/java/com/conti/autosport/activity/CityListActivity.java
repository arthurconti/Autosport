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
import com.conti.autosport.adapters.CityAdapter;
import com.conti.autosport.model.City;
import com.conti.autosport.model.Customer;
import com.conti.autosport.util.ConnectionInterface;

import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity implements OnItemClickListener,
        TextWatcher {

    private ArrayList<City> mCities = new ArrayList<>();
    private CityAdapter mCityAdapter = null;
    private ListView mList;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.listView);

        EditText editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(getString(R.string.aguarde));
        mProgressDialog.setMessage(getString(R.string.processando));

        new LoadCitiesTask(mProgressDialog).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent data = new Intent();
        data.putExtra(Customer.ID, mCityAdapter.getFilteredCities().get(arg2).getID());
        data.putExtra(Customer.NAME, mCityAdapter.getFilteredCities().get(arg2).getNome());
        setResult(20, data);
        finish();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mCityAdapter != null) {
            mCityAdapter.getFilter().filter(s.toString());
            mList.setAdapter(mCityAdapter);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class LoadCitiesTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressdialog;
        int result;

        public LoadCitiesTask(ProgressDialog progress) {
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
                            getString(R.string.sem_cidades), Toast.LENGTH_LONG)
                            .show();
                    break;
                case 2:
                    finish();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.erro_cidades), Toast.LENGTH_LONG)
                            .show();
                    break;
                case 3:
                    reorderCities(new ArrayList<>(mCities));
                    mCityAdapter = new CityAdapter(getApplicationContext(), mCities);
                    mList.setAdapter(mCityAdapter);
                    mList.setOnItemClickListener(CityListActivity.this);
                    break;

                default:
                    break;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectionInterface conn = new ConnectionInterface(getApplicationContext());

            if (conn.getClientes() != null) {
                mCities = conn.getCidades();

                if (mCities.size() == 0) {
                    result = 1;
                } else {
                    result = 3;
                }
            } else {
                result = 2;
            }

            return null;
        }

        private void reorderCities(List<City> cities) {
            if (cities != null) {
                for (City city : cities
                        ) {
                    if (city.getNome().trim().equalsIgnoreCase("itapetininga")) {
                        int index = cities.indexOf(city);
                        mCities.remove(index);
                        mCities.add(0, city);
                        break;
                    }
                }
            }
        }
    }
}
