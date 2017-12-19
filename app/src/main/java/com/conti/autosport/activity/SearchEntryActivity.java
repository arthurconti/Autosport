package com.conti.autosport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.util.ConnectionInterface;

public class SearchEntryActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        final ConnectionInterface conn = new ConnectionInterface(this);
        final EditText txtOrcamento = (EditText) findViewById(R.id.txtOrcamento);

        findViewById(R.id.btnBuscar).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!conn.getBuscaEntrada((Integer.parseInt(txtOrcamento
                        .getText().toString())))) {
                    startActivity(new Intent(getApplicationContext(),
                            PreOrderEntryActivity.class).putExtra(PreOrder.ID,
                            txtOrcamento.getText().toString()));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.existe_entrada),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
