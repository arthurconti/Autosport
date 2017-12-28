package com.conti.autosport.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.model.Local;
import com.conti.autosport.model.Servico;
import com.conti.autosport.util.CustomTouchImageView;
import com.conti.autosport.util.Parte;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ServicoActivity extends AppCompatActivity implements OnTouchListener {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    Bitmap bmp;
    CustomTouchImageView img;
    int i = 0;
    ArrayList<Uri> imagens = new ArrayList<>();

    Uri fileUri = null;
    ImageView photoImage = null;

    Local local = null;

    private Parte mStep;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servico);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView lblServico = findViewById(R.id.lblServico);
        img = findViewById(R.id.imgLocal);

        mStep = OrcamentoManager.getInstance().getParte();
        switch (mStep) {
            case SUPERIOR:
                lblServico.setText(getResources()
                        .getString(R.string.parte_superior));
                img.setImageResource(R.drawable.superior);
                break;
            case FRENTE:
                lblServico.setText(getResources().getString(R.string.parte_frente));
                img.setImageResource(R.drawable.frente);
                break;
            case TRASEIRA:
                lblServico.setText(getResources()
                        .getString(R.string.parte_traseira));
                img.setImageResource(R.drawable.traseira);
                break;
            case ESQUERDA:
                lblServico.setText(getResources()
                        .getString(R.string.parte_esquerda));
                img.setImageResource(R.drawable.esquerda);
                break;
            case DIREITA:
                lblServico
                        .setText(getResources().getString(R.string.parte_direita));
                img.setImageResource(R.drawable.direita);
                break;
            default:
                break;
        }

        img.setOnTouchListener(this);

        Local local = OrcamentoManager.getInstance().getServicesFromPart(mStep);
        if (local != null) {
            img.points = local.getPoints();
            img.invalidate();
            for (Servico service : local.getServices()
                    ) {
                addServico(service);
            }
        }
    }

    public void buttonClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnAvancar:
                finalizar();
                break;

            case R.id.btnLimpar:
                limparPontos();
                break;

            default:
                break;
        }

    }

    private void finalizar() {
        if (salvar()) {
            OrcamentoManager.getInstance().getOrcamento()
                    .setLocal(local, mStep);
            finish();
        }
    }

    // metodo que salva todas as informacoes da tela no orcamento
    private boolean salvar() {
        String erro = null;
        local = new Local();

        LinearLayout linear = findViewById(R.id.linearServ);
        ArrayList<Servico> servicos = new ArrayList<>();

        // loop para pegar todos os items na lista
        for (int j = 1; j < i + 1; j++) {
            Servico serv = new Servico();

            EditText tipo = linear.findViewWithTag("tipo" + j);
            EditText valor = linear.findViewWithTag("preco" + j);

            if (tipo.getText().toString().equals(""))
                erro = getResources().getString(R.string.erro_vazio);
            else
                serv.setDescricao(tipo.getText().toString());

            if (valor.getText().toString().equals(""))
                erro = getResources().getString(R.string.erro_vazio);
            else {
                String preco = valor.getText().toString()
                        .replaceAll("[R$.]", "").replaceAll(",", ".");
                serv.setPreco(Double.parseDouble(preco));
            }

            if (imagens.size() > j - 1) {
                if (imagens.get(j - 1) != null)
                    serv.setFoto(imagens.get(j - 1));
            }
            servicos.add(serv);
        }

        if (erro == null) {
            if (i > 0) {
                local.setServices(servicos);
                local.setPoints(img.points);
                img.buildDrawingCache(true);
                Bitmap bitmap = img.getDrawingCache(true).copy(Config.RGB_565,
                        false);
                img.destroyDrawingCache();
                local.setMap(bitmap);
            } else {
                local = null;
            }
            return true;
        } else {
            Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }

    private void addServico(Servico service) {
        if (i == 0) {
            findViewById(R.id.lblListaServicos).setVisibility(View.VISIBLE);
            findViewById(R.id.scrollServicos).setVisibility(View.VISIBLE);
        }

        i++;

        LinearLayout linear = findViewById(R.id.linearServ);

        View v = getLayoutInflater().inflate(R.layout.servico_line, null);

        TextView txtservico = v.findViewById(R.id.txtID_servico);
        txtservico.setText(i + "");
        txtservico.setTextColor(getResources().getColor(android.R.color.black));

        EditText txtTipo = v.findViewById(R.id.edit_servico);
        txtTipo.setTag("tipo" + i);

        final EditText txtPreco = v.findViewById(R.id.edit_valor);
        txtPreco.setTag("preco" + i);
        txtPreco.setSelectAllOnFocus(true);
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

        Button btnFoto = v.findViewById(R.id.btn_foto);
        btnFoto.setTag("foto" + i);
        btnFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("tag", i);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQ);
            }
        });

        // adiciona a linha na lista
        linear.addView(v);

        //Fill fields
        if (service != null) {
            txtTipo.setText(service.getDescricao());
            txtPreco.setText(String.format("R$%.2f", service.getPreco()));
        }

    }

    private void limparPontos() {
        img.eraseImage();
        img.invalidate();

        imagens.clear();

        findViewById(R.id.lblListaServicos).setVisibility(View.INVISIBLE);
        findViewById(R.id.scrollServicos).setVisibility(View.INVISIBLE);

        LinearLayout linear = findViewById(R.id.linearServ);
        linear.removeAllViews();

        i = 0;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Toast.makeText(this, "Image saved successfully",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this,
                            "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();

                    if (photoUri != null) {
                        imagens.add(data.getIntExtra("tag", 0), photoUri);
                    }
                }
                // showPhoto(photoUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {


            addServico(null);
        }

        return false;
    }

}
