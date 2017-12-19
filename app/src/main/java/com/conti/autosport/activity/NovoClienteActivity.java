package com.conti.autosport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.model.City;
import com.conti.autosport.model.Customer;
import com.conti.autosport.util.ConnectionInterface;
import com.conti.autosport.util.Mask;

public class NovoClienteActivity extends AppCompatActivity {

    EditText txtNome;
    EditText txtEmail;
    EditText txtTel;
    EditText txtCel;
    EditText txtEndereco;
    Customer client;
    City cidade = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cliente);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTel = (EditText) findViewById(R.id.txtTel);
        txtTel.addTextChangedListener(Mask.insert("(##)####-####", txtTel));
        txtCel = (EditText) findViewById(R.id.txtCel);
        txtCel.addTextChangedListener(Mask.insert("(##)#####-####", txtCel));
        txtEndereco = (EditText) findViewById(R.id.txtEndereco);
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btnLimpar:
                limparCampos();
                break;
            case R.id.btnSalvarCliente:
                salvar();
                break;
            case R.id.btnSelecionarCidade:
                Intent intent = new Intent(getApplicationContext(),
                        CityListActivity.class);
                startActivityForResult(intent, 10);
                break;

            default:
                break;
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtCel.setText("");
        txtEndereco.setText("");
        cidade = null;

        TextView txt = (TextView) findViewById(R.id.txtNomeCidade);
        txt.setText("");
    }

    private void salvar() {
        if (txtNome.getText().toString() != ""
                && txtEmail.getText().toString() != ""
                && txtCel.getText().toString() != ""
                && txtTel.getText().toString() != ""
                && txtEndereco.getText().toString() != "" && cidade != null) {
            client = new Customer();

            client.setName(txtNome.getText().toString());
            client.setEmail(txtEmail.getText().toString());
            client.setMobile(txtCel.getText().toString());
            client.setPhone(txtTel.getText().toString());
            client.setAddress(txtEndereco.getText().toString());
            client.setCity(cidade.getID());

            ConnectionInterface conn = new ConnectionInterface(this);
            // fazer salvar cliente
            Customer resposta = conn.addCliente(client);

            if (resposta != null) {
                Intent data = new Intent();
                data.putExtra("ID", resposta.getId());
                data.putExtra("Nome", resposta.getName());
                setResult(20, data);
                finish();
                Toast.makeText(getApplicationContext(),
                        getString(R.string.cliente_sucesso), Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.cliente_erro), Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.erro_campos_vazios), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == 20) {
                cidade = new City();
                cidade.setID(data.getExtras().getInt("ID"));
                cidade.setNome(data.getExtras().getString("Nome"));

                TextView txt = (TextView) findViewById(R.id.txtNomeCidade);
                txt.setText(cidade.getNome());
            }
        }
    }

}
