package com.conti.autosport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.model.Peca;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ListaPecasActivity extends AppCompatActivity {
    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pecas);

        ArrayList<Peca> listaPecas = OrcamentoManager.getInstance().getOrcamento().getListaPecas();
        if (listaPecas != null) {
            for (Peca peca : listaPecas
                    ) {
                addPeca(peca);
            }
        }
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPeca:
                addPeca(null);
                break;
            case R.id.btnAvancar:
                if (salvar()) {
                    finish();
                }
                break;
            case R.id.btnLimpar:
                limparPecas();
                break;

            default:
                break;
        }
    }

    private void limparPecas() {
        LinearLayout linear = findViewById(R.id.linearPeca);
        linear.removeAllViews();

        i = 0;
    }

    private boolean salvar() {
        String erro = null;

        LinearLayout linear = findViewById(R.id.linearPeca);
        ArrayList<Peca> listaPecas = new ArrayList<>();

        // loop para pegar todos os items na lista
        for (int j = 1; j < i + 1; j++) {
            Peca peca = new Peca();

            EditText tipo = linear.findViewWithTag("peca" + j);
            EditText valor = linear.findViewWithTag("preco" + j);

            if (tipo.getText().toString().equals(""))
                erro = getString(R.string.erro_vazio);
            else
                peca.setNome(tipo.getText().toString());

            if (valor.getText().toString().equals(""))
                erro = getString(R.string.erro_vazio);
            else {
                String preco = valor.getText().toString()
                        .replaceAll("[R$.]", "").replaceAll(",", ".");
                peca.setPreco(Double.parseDouble(preco));
            }

            listaPecas.add(peca);
        }

        if (erro == null) {
            if (i > 0) {
                OrcamentoManager.getInstance().getOrcamento().setListaPecas(listaPecas);
            } else {
                OrcamentoManager.getInstance().getOrcamento().setListaPecas(null);
            }
            return true;
        } else {
            Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }

    private void addPeca(Peca peca) {
        i++;
        LinearLayout linear = findViewById(R.id.linearPeca);

        View v = getLayoutInflater().inflate(R.layout.peca_line, null);

        TextView txtservico = v.findViewById(R.id.txtID_servico);
        txtservico.setText(i + "");

        EditText txtTipo = v.findViewById(R.id.edit_servico);
        txtTipo.setTag("peca" + i);

        final EditText txtPreco = v.findViewById(R.id.edit_valor);
        txtPreco.setTag("preco" + i);
        txtPreco.addTextChangedListener(new TextWatcher() {

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
                    txtPreco.setText(str);
                    txtPreco.setSelection(txtPreco.getText().length());
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

        // adiciona a linha na lista
        linear.addView(v);

        //Fill fields
        if (peca != null) {
            txtTipo.setText(peca.getNome());
            txtPreco.setText(String.format("R$%.2f", peca.getPreco()));
        }
    }
}
