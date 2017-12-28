package com.conti.autosport.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.util.ConnectionInterface;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class PreOrderEntryActivity extends AppCompatActivity {

    private Uri mSuperiorUri;
    private Uri mFrontUri;
    private Uri mRightUri;
    private Uri mBehindUri;
    private Uri mLeftUri;

    private Uri mPanelUri;

    private Button btnSuperior;
    private Button btnFrente;
    private Button btnDireita;
    private Button btnTraseira;
    private Button btnEsquerda;

    private Button btnPainel;

    private String orcamento;

    boolean gravou = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_orcamento);
        orcamento = getIntent().getStringExtra(PreOrder.ID);

        TextView txtEntrada = findViewById(R.id.txtentrada);
        txtEntrada.setText(getString(R.string.tire_fotos) + orcamento);

        btnSuperior = findViewById(R.id.btnFotoSuperior);
        btnDireita = findViewById(R.id.btnFotoDireita);
        btnEsquerda = findViewById(R.id.btnFotoEsquerda);
        btnFrente = findViewById(R.id.btnFotoFrente);
        btnPainel = findViewById(R.id.btnFotoPainel);
        btnTraseira = findViewById(R.id.btnFotoTraseira);
    }

    public void buttonClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (v.getId()) {
            case R.id.btnFotoSuperior:
                startActivityForResult(intent, 1);
                break;
            case R.id.btnFotoFrente:
                startActivityForResult(intent, 2);
                break;
            case R.id.btnFotoDireita:
                startActivityForResult(intent, 3);
                break;
            case R.id.btnFotoTraseira:
                startActivityForResult(intent, 4);
                break;
            case R.id.btnFotoEsquerda:
                startActivityForResult(intent, 5);
                break;
            case R.id.btnFotoPainel:
                startActivityForResult(intent, 6);
                break;
            case R.id.btnProcessar:
                new MyTask(PreOrderEntryActivity.this).execute();
                break;
            case R.id.btnLimpar:
                Limpar();
                break;
        }
    }

    public boolean gravarEntrada() {
        enviarFtp("Entrada_" + orcamento);
        ConnectionInterface conn = new ConnectionInterface(this);
        return conn.addEntrada(orcamento, mSuperiorUri, mFrontUri, mRightUri, mBehindUri,
                mLeftUri, mPanelUri);

    }

    public void UpdateUI() {
        if (gravou) {
            finish();
            Toast.makeText(
                    getApplicationContext(),
                    String.format(getString(R.string.sucesso_entrada),
                            orcamento), Toast.LENGTH_LONG).show();

        } else {
            finish();
            Toast.makeText(getApplicationContext(),
                    getString(R.string.erro_entrada), Toast.LENGTH_LONG).show();
        }
    }


    private void Limpar() {
        mSuperiorUri = null;
        mFrontUri = null;
        mRightUri = null;
        mBehindUri = null;
        mLeftUri = null;
        mPanelUri = null;

        btnSuperior.setEnabled(true);
        btnFrente.setEnabled(true);
        btnDireita.setEnabled(true);
        btnTraseira.setEnabled(true);
        btnEsquerda.setEnabled(true);
        btnPainel.setEnabled(true);
    }

    private void enviarFtp(String pasta) {
        FTPClient mFTP = new FTPClient();
        try {
            // Connect to FTP Server
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);
            mFTP.connect(ConnectionInterface.ip);
            mFTP.login("admin", "admin");
            mFTP.setFileType(FTP.BINARY_FILE_TYPE);
            mFTP.enterLocalPassiveMode();
            String nomePasta = pasta;
            mFTP.makeDirectory(nomePasta);
            // Prepare file to be uploaded to FTP Server
            String imageFilePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/AutoSport/copy.png";

            if (mSuperiorUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fotosuperior.jpg",
                        mSuperiorUri);
            if (mFrontUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fotofrente.jpg",
                        mFrontUri);
            if (mRightUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fotodireita.jpg",
                        mRightUri);
            if (mBehindUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fototraseira.jpg",
                        mBehindUri);
            if (mLeftUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fotoesquerda.jpg",
                        mLeftUri);

            if (mPanelUri != null)
                enviarFoto(mFTP, imageFilePath, nomePasta, "fotopainel.jpg",
                        mPanelUri);

            mFTP.disconnect();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarFoto(FTPClient mFTP, String imageFilePath, String path,
                            String filename, Uri photo) {
        try {
            Bitmap img = Media.getBitmap(getContentResolver(), photo);
            FileOutputStream out = new FileOutputStream(imageFilePath);
            img.compress(Bitmap.CompressFormat.JPEG, 50, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(imageFilePath);
        FileInputStream ifile = null;
        try {
            ifile = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        // Upload file to FTP Server
        try {
            mFTP.storeFile(path + "/" + filename, ifile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri photoUri = null;
            if (data == null) {
                Toast.makeText(this, "Image saved successfully",
                        Toast.LENGTH_LONG).show();
            } else {
                photoUri = data.getData();

                switch (requestCode) {
                    case 1:
                        mSuperiorUri = photoUri;
                        btnSuperior.setEnabled(false);
                        break;
                    case 2:
                        mFrontUri = photoUri;
                        btnFrente.setEnabled(false);
                        break;
                    case 3:
                        mRightUri = photoUri;
                        btnDireita.setEnabled(false);
                        break;
                    case 4:
                        mBehindUri = photoUri;
                        btnTraseira.setEnabled(false);
                        break;
                    case 5:
                        mLeftUri = photoUri;
                        btnEsquerda.setEnabled(false);
                        break;
                    case 6:
                        mPanelUri = photoUri;
                        btnPainel.setEnabled(false);
                        break;

                }
                Toast.makeText(this,
                        "Image saved successfully in: " + data.getData(),
                        Toast.LENGTH_LONG).show();

                if (photoUri != null) {

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

    class MyTask extends AsyncTask<Void, Void, Void> {
        Context context;
        ProgressDialog dialog;

        public MyTask(Context context) {
            this.context = context;
        }

        public void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage(getString(R.string.gravanda_entrada));
            dialog.setCancelable(false);
            dialog.show();
        }

        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dialog.dismiss();
            UpdateUI();
        }

        @Override
        protected Void doInBackground(Void... params) {
            gravou = gravarEntrada();
            return null;
        }
    }
}
