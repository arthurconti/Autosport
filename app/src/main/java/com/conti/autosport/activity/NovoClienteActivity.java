package com.conti.autosport.activity;

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
    EditText txtCel;
    Customer client;
    City cidade = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cliente);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        txtNome = findViewById(R.id.txtNome);
        txtCel = findViewById(R.id.txtCel);
        txtCel.addTextChangedListener(Mask.insert("(##)#####-####", txtCel));
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
        txtCel.setText("");
        cidade = null;

        TextView txt = findViewById(R.id.txtNomeCidade);
        txt.setText("");
    }

    private void salvar() {
        if (txtNome.getText().toString() != ""
                && txtCel.getText().toString() != ""
                && cidade != null) {
            client = new Customer();

            client.setName(txtNome.getText().toString());
            client.setMobile(txtCel.getText().toString());
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

                TextView txt = findViewById(R.id.txtNomeCidade);
                txt.setText(cidade.getNome());
            }
        }
    }

}
