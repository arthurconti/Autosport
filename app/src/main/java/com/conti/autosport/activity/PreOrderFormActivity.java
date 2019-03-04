package com.conti.autosport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.model.Customer;

public class PreOrderFormActivity extends AppCompatActivity implements TextWatcher {

    private static final int REQUEST_CODE = 10;
    public static final int RESULT_CODE = 20;

    private Customer mCustomer;

    private EditText editNumPlate;
    private EditText editLetPlate;
    private EditText editBrand;
    private EditText editModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_orcamento);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editLetPlate = findViewById(R.id.editLetPlate);
        editNumPlate = findViewById(R.id.editNumPlate);
        editBrand = findViewById(R.id.editBrand);
        editModel = findViewById(R.id.editModel);

        editLetPlate.addTextChangedListener(this);
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btnConcluir:
                String plate = editLetPlate.getText() + "-" + editNumPlate.getText();

                String brand = editBrand.getText().toString();

                String model = editModel.getText().toString();

                if (mCustomer != null && plate.length() > 1 && brand.length() > 1
                        && model.length() > 1) {
                    OrcamentoManager.getInstance().getOrcamento()
                            .setCliente(mCustomer);
                    OrcamentoManager.getInstance().getOrcamento().setPlaca(plate);
                    OrcamentoManager.getInstance().getOrcamento().setMarca(brand);
                    OrcamentoManager.getInstance().getOrcamento().setModelo(model);
                    finish();

                    startActivity(new Intent(getApplicationContext(),
                            SavePreOrderActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.erro_campos_vazios),
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnSelectCustomer:
                Intent intent = new Intent(getApplicationContext(),
                        CustomerListActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.btnAddCustomer:
                Intent intent2 = new Intent(getApplicationContext(),
                        NovoClienteActivity.class);
                startActivityForResult(intent2, REQUEST_CODE);
                break;
            case R.id.btnCancel:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 2) {
            editNumPlate.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                mCustomer = new Customer();
                mCustomer.setId(data.getExtras().getInt(Customer.ID));
                mCustomer.setName(data.getExtras().getString(Customer.NAME));

                TextView txt = findViewById(R.id.txtCustomerName);
                txt.setText(mCustomer.getName());
                txt.setTextColor(Color.LTGRAY);

                editLetPlate.setEnabled(true);
                editNumPlate.setEnabled(true);
                editBrand.setEnabled(true);
                editModel.setEnabled(true);
            }
        }
    }

}
