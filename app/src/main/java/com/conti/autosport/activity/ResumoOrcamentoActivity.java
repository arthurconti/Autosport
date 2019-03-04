package com.conti.autosport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.model.Peca;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.model.Servico;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ResumoOrcamentoActivity extends AppCompatActivity {

    private PreOrder orcamento;
    private double subtotal = 0;
    private double total = 0;
    private double desconto = 0;

    private TextView mTxtDesconto;
    private View mStrike;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumo_orcamento);

        mTxtDesconto = findViewById(R.id.txtDesconto);
        mStrike = findViewById(R.id.strike);

        orcamento = OrcamentoManager.getInstance().getOrcamento();

        if (orcamento.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.erro_orcamento), Toast.LENGTH_LONG)
                    .show();
            finish();
        } else {

            if (orcamento.getSuperior() != null) {
                LinearLayout superiorGeral = findViewById(R.id.SuperiorGeral);
                superiorGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout superiorContainer = findViewById(R.id.superiorContainer);
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
                LinearLayout frenteGeral = findViewById(R.id.frenteGeral);
                frenteGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout frenteContainer = findViewById(R.id.frenteContainer);
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
                LinearLayout latDireitaGeral = findViewById(R.id.latDireitaGeral);
                latDireitaGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout latDireitaContainer = findViewById(R.id.latDireitaContainer);
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
                LinearLayout traseiraGeral = findViewById(R.id.traseiraGeral);
                traseiraGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout traseiraContainer = findViewById(R.id.traseiraContainer);
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
                LinearLayout latEsquerdaGeral = findViewById(R.id.latEsquerdaGeral);
                latEsquerdaGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout latEsquerdaContainer = findViewById(R.id.latEsquerdaContainer);
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
                LinearLayout pecasGeral = findViewById(R.id.PecasGeral);
                pecasGeral.setVisibility(View.VISIBLE);
                subtotal = 0;

                LinearLayout pecasContainer = findViewById(R.id.pecasContainer);
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

            TextView txtTotal = findViewById(R.id.txtTotal);
            txtTotal.setText(String.format("%.2f", total));
        }
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btnAvancar:
                OrcamentoManager.getInstance().getOrcamento().setDesconto(desconto);
                finish();
                Intent i = new Intent(getApplicationContext(), PreOrderFormActivity.class);
                startActivity(i);
                break;
            case R.id.btnDesconto:
                showDiscountDialog();
                break;

            default:
                break;
        }
    }

    public void addServico(LinearLayout container, String descricao,
                           String valor) {
        LinearLayout line = (LinearLayout) View.inflate(this,
                R.layout.template_servico, null);
        TextView txtservico = line.findViewById(R.id.txtDescricao);
        txtservico.setText(descricao);
        TextView txtvalor = line.findViewById(R.id.txtValor);
        txtvalor.setText(valor);

        container.addView(line);
    }

    private void showDiscountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResumoOrcamentoActivity.this);
        builder.setTitle("Dar desconto?");

// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setSelectAllOnFocus(true);
        input.addTextChangedListener(new TextWatcher() {

            private boolean isUpdating = false;
            // Pega a formatacao do sistema, se for brasil R$ se EUA US$
            private NumberFormat nf = NumberFormat.getCurrencyInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int after) {
                // Evita que o método seja executado varias vezes.
                // Se tirar ele entre em loop
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                isUpdating = true;
                String str = s.toString();
                // Verifica se já existe a máscara no texto.
                boolean hasMask = ((str.contains("R$") || str.contains("$")) && (str.contains(".") || str.contains(",")));
                // Verificamos se existe máscara
                if (hasMask) {
                    // Retiramos a máscara.
                    str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                            .replaceAll("[.]", "");
                }

                try {
                    // Transformamos o número que está escrito no EditText em
                    // monetário.
                    str = nf.format(Double.parseDouble(str) / 100);
                    input.setText(str);
                    input.setSelection(input.getText().length());
                } catch (NumberFormatException e) {
                    s = "";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Não utilizamos
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não utilizamos
            }
        });

        input.setText(String.format("R$%.2f", desconto));

        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String preco = input.getText().toString()
                        .replaceAll("[R$.]", "").replaceAll(",", ".");
                desconto = Double.parseDouble(preco);
                if (total > desconto) {
                    showDiscountViews();
                } else {
                    Toast.makeText(ResumoOrcamentoActivity.this, "Desconto maior que o valor total!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDiscountViews() {
        if (desconto > 0) {
            mTxtDesconto.setText(String.format("%.2f", total - desconto));
            mTxtDesconto.setVisibility(View.VISIBLE);
            mStrike.setVisibility(View.VISIBLE);
        } else {
            mTxtDesconto.setVisibility(View.GONE);
            mStrike.setVisibility(View.GONE);
        }
    }
}
