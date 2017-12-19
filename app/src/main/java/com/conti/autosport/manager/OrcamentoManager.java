package com.conti.autosport.manager;

import android.content.Context;

import com.conti.autosport.model.Local;
import com.conti.autosport.model.PreOrder;
import com.conti.autosport.model.Servico;
import com.conti.autosport.util.Parte;

public class OrcamentoManager {

    // private static final String LOG_TAG = OrcamentoManager.class.getName();

    private PreOrder currOrcamento;
    private Parte parte = Parte.SUPERIOR;

    private static OrcamentoManager instance;

    private OrcamentoManager() {
        currOrcamento = new PreOrder();
    }

    public static OrcamentoManager getInstance() {
        if (instance == null) {
            instance = new OrcamentoManager();
        }
        return instance;
    }

    public void initialize(Context ctx) {
        currOrcamento = new PreOrder();
    }

    public PreOrder getOrcamento() {
        return currOrcamento;
    }

    public Parte getParte() {
        return parte;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Local getServicesFromPart(Parte part){
        switch (part){
            case FRENTE:
                return currOrcamento.getFrente();
            case DIREITA:
                return currOrcamento.getLatDireita();
            case ESQUERDA:
                return currOrcamento.getLatEsquerda();
            case SUPERIOR:
                return currOrcamento.getSuperior();
            case TRASEIRA:
                return currOrcamento.getTraseira();
            default:
                return null;
        }
    }

    // private void uploadClients(FinishedUploadingCallback listener) {
    // ConnectivityManager connManager = (ConnectivityManager) ctx
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // NetworkInfo mWifi = connManager
    // .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    // if (mWifi.isConnected()) {
    // uploader.uploadeClients(listener);
    // Log.d(LOG_TAG, "uploading clients");
    // } else {
    // uploader.printList();
    // }
    // }

}
