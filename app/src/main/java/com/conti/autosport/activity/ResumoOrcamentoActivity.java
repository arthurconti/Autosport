package com.conti.autosport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.model.Peca;
import com.conti.autosport.model.Servico;
import com.conti.autosport.manager.OrcamentoManager;

import java.util.ArrayList;

public class ResumoOrcamentoActivity extends AppCompatActivity {

    private PreOrder orcamento;
    private double subtotal = 0;
    private double total = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumo_orcamento);

        orcamento = OrcamentoManager.getInstance().getOrcamento();

        if (orcamento.getSuperior() != null || orcamento.getFrente() != null
                || orcamento.getLatDireita() != null
                || orcamento.getTraseira() != null
                || orcamento.getLatEsquerda() != null || orcamento.getListaPecas() != null) {

            if (orcamento.getSuperior() != null) {
                LinearLayout superiorGeral = (LinearLayout) findViewById(R.id.SuperiorGeral);
                superiorGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout superiorContainer = (LinearLayout) findViewById(R.id.superiorContainer);
                ArrayList<Servico> servicos = orcamento.getSuperior()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    addServico(
                            superiorContainer,
                            servicos.get(i).getDescricao(),
                            "R$"
                                    + String.format("%.2f", servicos.get(i)
                                    .getPreco()));
                    subtotal += servicos.get(i).getPreco();
                    total += servicos.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                superiorContainer.addView(txtSubtotal);
            }

            if (orcamento.getFrente() != null) {
                LinearLayout frenteGeral = (LinearLayout) findViewById(R.id.frenteGeral);
                frenteGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout frenteContainer = (LinearLayout) findViewById(R.id.frenteContainer);
                ArrayList<Servico> servicos = orcamento.getFrente()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    addServico(
                            frenteContainer,
                            servicos.get(i).getDescricao(),
                            "R$"
                                    + String.format("%.2f", servicos.get(i)
                                    .getPreco()));
                    subtotal += servicos.get(i).getPreco();
                    total += servicos.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                frenteContainer.addView(txtSubtotal);
            }

            if (orcamento.getLatDireita() != null) {
                LinearLayout latDireitaGeral = (LinearLayout) findViewById(R.id.latDireitaGeral);
                latDireitaGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout latDireitaContainer = (LinearLayout) findViewById(R.id.latDireitaContainer);
                ArrayList<Servico> servicos = orcamento.getLatDireita()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    addServico(
                            latDireitaContainer,
                            servicos.get(i).getDescricao(),
                            "R$"
                                    + String.format("%.2f", servicos.get(i)
                                    .getPreco()));
                    subtotal += servicos.get(i).getPreco();
                    total += servicos.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                latDireitaContainer.addView(txtSubtotal);
            }

            if (orcamento.getTraseira() != null) {
                LinearLayout traseiraGeral = (LinearLayout) findViewById(R.id.traseiraGeral);
                traseiraGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout traseiraContainer = (LinearLayout) findViewById(R.id.traseiraContainer);
                ArrayList<Servico> servicos = orcamento.getTraseira()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    addServico(
                            traseiraContainer,
                            servicos.get(i).getDescricao(),
                            "R$"
                                    + String.format("%.2f", servicos.get(i)
                                    .getPreco()));
                    subtotal += servicos.get(i).getPreco();
                    total += servicos.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                traseiraContainer.addView(txtSubtotal);
            }

            if (orcamento.getLatEsquerda() != null) {
                LinearLayout latEsquerdaGeral = (LinearLayout) findViewById(R.id.latEsquerdaGeral);
                latEsquerdaGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout latEsquerdaContainer = (LinearLayout) findViewById(R.id.latEsquerdaContainer);
                ArrayList<Servico> servicos = orcamento.getLatEsquerda()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    addServico(
                            latEsquerdaContainer,
                            servicos.get(i).getDescricao(),
                            "R$"
                                    + String.format("%.2f", servicos.get(i)
                                    .getPreco()));
                    subtotal += servicos.get(i).getPreco();
                    total += servicos.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                latEsquerdaContainer.addView(txtSubtotal);
            }

            if (orcamento.getListaPecas() != null) {
                LinearLayout pecasGeral = (LinearLayout) findViewById(R.id.PecasGeral);
                pecasGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout pecasContainer = (LinearLayout) findViewById(R.id.pecasContainer);
                ArrayList<Peca> pecas = orcamento.getListaPecas();
                for (int i = 0; i < pecas.size(); i++) {
                    addServico(pecasContainer, pecas.get(i).getNome(), "R$"
                            + String.format("%.2f", pecas.get(i).getPreco()));
                    subtotal += pecas.get(i).getPreco();
                    total += pecas.get(i).getPreco();
                }
                TextView txtSubtotal = new TextView(getApplicationContext());
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 20, 0);
                txtSubtotal.setLayoutParams(params);
                txtSubtotal.setGravity(Gravity.RIGHT);
                txtSubtotal.setText("Subtotal:  R$"
                        + String.format("%.2f", subtotal));

                pecasContainer.addView(txtSubtotal);
            }

            TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
            txtTotal.setText("Total: R$" + String.format("%.2f", total));

        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.erro_orcamento), Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }

    public void buttonClick(View v) {
        finish();
        switch (v.getId()) {
            case R.id.btnAvancar:
                Intent i = new Intent(getApplicationContext(), PreOrderFormActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    public void addServico(LinearLayout container, String descricao,
                           String valor) {
        LinearLayout line = (LinearLayout) View.inflate(this,
                R.layout.template_servico, null);
        TextView txtservico = (TextView) line.findViewById(R.id.txtDescricao);
        txtservico.setText(descricao);
        TextView txtvalor = (TextView) line.findViewById(R.id.txtValor);
        txtvalor.setText(valor);

        container.addView(line);
    }
}
