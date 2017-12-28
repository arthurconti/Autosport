package com.conti.autosport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.util.Parte;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceActivity extends AppCompatActivity {

    @BindView(R.id.txtTotal)
    TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtTotal.setText("Valor total R$ " + String.format("%.2f",
                OrcamentoManager.getInstance().getOrcamento().getTotalPrice()));
    }

    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                OrcamentoManager.getInstance().setParte(Parte.SUPERIOR);
                break;
            case R.id.button2:
                OrcamentoManager.getInstance().setParte(Parte.FRENTE);
                break;
            case R.id.button3:
                OrcamentoManager.getInstance().setParte(Parte.ESQUERDA);
                break;
            case R.id.button4:
                OrcamentoManager.getInstance().setParte(Parte.DIREITA);
                break;
            case R.id.button5:
                OrcamentoManager.getInstance().setParte(Parte.TRASEIRA);
                break;
        }

        startActivity(new Intent(getApplicationContext(),
                ServicoActivity.class));
        this.overridePendingTransition(R.anim.anim_slidein_left,
                R.anim.anim_slideout_left);
    }

    @Override
    public void onBackPressed() {
        if (OrcamentoManager.getInstance().getOrcamento().isEmpty()) {
            finish();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deseja encerrar esse orçamento?");
            builder.setMessage("Ao sair sem salvar, esse orçamento será perdido.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    @OnClick(R.id.button6)
    public void onClickParts(View view) {
        startActivity(new Intent(getApplicationContext(),
                ListaPecasActivity.class));
        this.overridePendingTransition(R.anim.anim_slidein_left,
                R.anim.anim_slideout_left);
    }

    @OnClick(R.id.btnResume)
    public void onClickResume(View view) {
        startActivity(new Intent(getApplicationContext(),
                ResumoOrcamentoActivity.class));
        this.overridePendingTransition(R.anim.anim_slidein_left,
                R.anim.anim_slideout_left);
    }
}
