package com.conti.autosport.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.conti.autosport.R;
import com.conti.autosport.manager.OrcamentoManager;
import com.conti.autosport.model.Peca;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.model.Servico;
import com.conti.autosport.util.ConnectionInterface;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class SavePreOrderActivity extends AppCompatActivity {

    private PreOrder mPreOrder;
    private int mPreOrderID;
    private boolean mSaved = true;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orcamento);

        mPreOrder = OrcamentoManager.getInstance().getOrcamento();
        if (mPreOrder.getSuperior() != null || mPreOrder.getFrente() != null
                || mPreOrder.getLatDireita() != null
                || mPreOrder.getTraseira() != null
                || mPreOrder.getLatEsquerda() != null) {
            mProgress = new ProgressDialog(this);
            mProgress.setMessage(getResources().getString(
                    R.string.gravando_orcamento));
            new MyTask(mProgress).execute();
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.erro_orcamento),
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean savePreOrder(PreOrder preOrder) {
        boolean error = false;

        ConnectionInterface conn = new ConnectionInterface(this);

        mPreOrderID = conn.addOrcamento(preOrder.getCliente(),
                preOrder.getPlaca(), preOrder.getMarca(),
                preOrder.getModelo());

        if (mPreOrderID != 0) {

            if (preOrder.getSuperior() != null) {
                int tamanho = preOrder.getSuperior().getServices().size();
                ArrayList<Servico> lista = preOrder.getSuperior()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addServico(mPreOrderID, 1, lista.get(i)
                            .getDescricao(), "fotosuperior" + i + ".jpg", lista
                            .get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }

            if (preOrder.getFrente() != null) {
                int tamanho = preOrder.getFrente().getServices().size();
                ArrayList<Servico> lista = preOrder.getFrente().getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addServico(mPreOrderID, 2, lista.get(i)
                            .getDescricao(), "fotofrente" + i + ".jpg", lista
                            .get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }

            if (preOrder.getLatDireita() != null) {
                int tamanho = preOrder.getLatDireita().getServices().size();
                ArrayList<Servico> lista = preOrder.getLatDireita()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addServico(mPreOrderID, 3, lista.get(i)
                                    .getDescricao(), "fotolatdireita" + i + ".jpg",
                            lista.get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }

            if (preOrder.getTraseira() != null) {
                int tamanho = preOrder.getTraseira().getServices().size();
                ArrayList<Servico> lista = preOrder.getTraseira()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addServico(mPreOrderID, 4, lista.get(i)
                            .getDescricao(), "fototraseira" + i + ".jpg", lista
                            .get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }

            if (preOrder.getLatEsquerda() != null) {
                int tamanho = preOrder.getLatEsquerda().getServices().size();
                ArrayList<Servico> lista = preOrder.getLatEsquerda()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addServico(mPreOrderID, 5, lista.get(i)
                                    .getDescricao(), "fotolatesquerda" + i + ".jpg",
                            lista.get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }

            if (preOrder.getListaPecas() != null) {
                int tamanho = preOrder.getListaPecas().size();
                ArrayList<Peca> lista = preOrder.getListaPecas();
                for (int i = 0; i < tamanho; i++) {
                    if (conn.addPeca(mPreOrderID, lista.get(i).getNome(), lista
                            .get(i).getPreco())) {
                    } else {
                        error = true;
                    }
                }
            }
        }
        return error;
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
            // mFTP.changeWorkingDirectory("Mesa/autosport/");
            String nomePasta = pasta;
            mFTP.makeDirectory(nomePasta);
            // Prepare file to be uploaded to FTP Server
            String imageFilePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/AutoSport/copy.png";

            if (mPreOrder.getSuperior() != null) {
                int tamanho = mPreOrder.getSuperior().getServices().size();
                enviarBitmap(mFTP, imageFilePath, nomePasta, "superior.jpg",
                        mPreOrder.getSuperior().getMap());
                ArrayList<Servico> lista = mPreOrder.getSuperior()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (lista.get(i).getFoto() != null) {
                        String nomearquivo = "fotosuperior" + i + ".jpg";
                        enviarFoto(mFTP, imageFilePath, nomePasta, nomearquivo,
                                lista.get(i).getFoto());
                    }
                }
            }

            if (mPreOrder.getFrente() != null) {
                int tamanho = mPreOrder.getFrente().getServices().size();
                enviarBitmap(mFTP, imageFilePath, nomePasta, "frente.jpg",
                        mPreOrder.getFrente().getMap());
                ArrayList<Servico> lista = mPreOrder.getFrente().getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (lista.get(i).getFoto() != null) {
                        String nomearquivo = "fotofrente" + i + ".jpg";
                        enviarFoto(mFTP, imageFilePath, nomePasta, nomearquivo,
                                lista.get(i).getFoto());
                    }
                }
            }

            if (mPreOrder.getLatDireita() != null) {
                int tamanho = mPreOrder.getLatDireita().getServices().size();
                enviarBitmap(mFTP, imageFilePath, nomePasta, "latdireita.jpg",
                        mPreOrder.getLatDireita().getMap());
                ArrayList<Servico> lista = mPreOrder.getLatDireita()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (lista.get(i).getFoto() != null) {
                        String nomearquivo = "fotolatdireita" + i + ".jpg";
                        enviarFoto(mFTP, imageFilePath, nomePasta, nomearquivo,
                                lista.get(i).getFoto());
                    }
                }
            }

            if (mPreOrder.getTraseira() != null) {
                int tamanho = mPreOrder.getTraseira().getServices().size();
                enviarBitmap(mFTP, imageFilePath, nomePasta, "traseira.jpg",
                        mPreOrder.getTraseira().getMap());
                ArrayList<Servico> lista = mPreOrder.getTraseira()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (lista.get(i).getFoto() != null) {
                        String nomearquivo = "fototraseira" + i + ".jpg";
                        enviarFoto(mFTP, imageFilePath, nomePasta, nomearquivo,
                                lista.get(i).getFoto());
                    }
                }
            }

            if (mPreOrder.getLatEsquerda() != null) {
                int tamanho = mPreOrder.getLatEsquerda().getServices().size();
                enviarBitmap(mFTP, imageFilePath, nomePasta, "latesquerda.jpg",
                        mPreOrder.getLatEsquerda().getMap());
                ArrayList<Servico> lista = mPreOrder.getLatEsquerda()
                        .getServices();
                for (int i = 0; i < tamanho; i++) {
                    if (lista.get(i).getFoto() != null) {
                        String nomearquivo = "fotolatesquerda" + i + ".jpg";
                        enviarFoto(mFTP, imageFilePath, nomePasta, nomearquivo,
                                lista.get(i).getFoto());
                    }
                }
            }
            mFTP.disconnect();

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File file = new File(imageFilePath);
        FileInputStream ifile = null;
        try {
            ifile = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Upload file to FTP Server
        try {
            mFTP.storeFile(path + "/" + filename, ifile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void enviarBitmap(FTPClient mFTP, String imageFilePath,
                              String path, String filename, Bitmap photo) {
        try {
            Bitmap img = photo;
            FileOutputStream out = new FileOutputStream(imageFilePath);
            img.compress(Bitmap.CompressFormat.JPEG, 50, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File file = new File(imageFilePath);
        FileInputStream ifile = null;
        try {
            ifile = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Upload file to FTP Server
        try {
            mFTP.storeFile(path + "/" + filename, ifile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void UpdateUI() {
        if (!mSaved) {
            TextView txt = findViewById(R.id.txtStatus);
            enviarFtp("Orcamento_" + mPreOrderID);
            txt.setText(String.format(getString(R.string.sucesso_orcamento),
                    String.valueOf(mPreOrderID)));
            findViewById(R.id.btnFinalizar).setVisibility(View.VISIBLE);
            findViewById(R.id.btnFinalizar).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(SavePreOrderActivity.this, MainMenuActivity.class);
                            // set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });

            Toast.makeText(
                    getApplicationContext(),
                    String.format(getString(R.string.sucesso_orcamento),
                            String.valueOf(mPreOrderID)), Toast.LENGTH_LONG)
                    .show();

        } else {
            Toast.makeText(getApplicationContext(),
                    "Problemas ao salvar orcamento", Toast.LENGTH_LONG).show();
        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressdialog;

        public MyTask(ProgressDialog progress) {
            this.progressdialog = progress;
        }

        public void onPreExecute() {
            this.progressdialog.show();
        }

        public void onPostExecute(Void unused) {
            this.progressdialog.dismiss();
            UpdateUI();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            mSaved = savePreOrder(mPreOrder);
            return null;
        }
    }

}
