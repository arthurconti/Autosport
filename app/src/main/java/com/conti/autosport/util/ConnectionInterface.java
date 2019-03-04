package com.conti.autosport.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.conti.autosport.model.City;
import com.conti.autosport.model.Customer;
import com.conti.autosport.model.PreOrder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInterface {

    public static String ip = null;
    public static String urlPhp = null;

    private Context ctx;

    public ConnectionInterface(Context ctx) {
        this.ctx = ctx;
        ip = PreferenceManager.getDefaultSharedPreferences(ctx).getString("IP", "192.168.1.111");
    }

    public ConnectionInterface(Context ctx, String txt) {
        this.ctx = ctx;
        ip = txt;
    }

    public static JSONArray getJSON(String response) {
        JSONArray jArray = null;
        // Log.e("", response);
        // transformando em uma array de objetos JSON
        try {
            jArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jArray;
    }

    public ArrayList<Customer> getClientes() {
        ArrayList<Customer> clientes = new ArrayList<>();
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "clientes"));

        String response = getResponse(nameValuePairs);
        if (response != null) {
            JSONArray json = getJSON(response);

            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject json_data;
                    try {
                        Customer cliente = new Customer();
                        json_data = json.getJSONObject(i);

                        cliente.setId(json_data.getInt("ID"));
                        cliente.setName(json_data.getString("Nome"));

                        clientes.add(cliente);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return clientes;
        } else
            return null;
    }

    public ArrayList<City> getCidades() {
        ArrayList<City> cidades = new ArrayList<>();
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "cidades"));

        String response = getResponse(nameValuePairs);
        JSONArray json = getJSON(response);

        for (int i = 0; i < json.length(); i++) {
            JSONObject json_data;
            try {
                City cidade = new City();
                json_data = json.getJSONObject(i);

                cidade.setID(json_data.getInt("id"));
                cidade.setNome(json_data.getString("nome"));

                cidades.add(cidade);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cidades;
    }

    public Customer addCliente(Customer client) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addcliente"));
        nameValuePairs.add(new BasicNameValuePair("nome", client.getName()));
        nameValuePairs.add(new BasicNameValuePair("email", client.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("telefone", client
                .getPhone()));
        nameValuePairs.add(new BasicNameValuePair("celular", client
                .getMobile()));
        // nameValuePairs.add(new BasicNameValuePair("cidade","8781"));
        nameValuePairs.add(new BasicNameValuePair("cidade", String
                .valueOf(client.getCity())));
        nameValuePairs.add(new BasicNameValuePair("endereco", client
                .getAddress()));

        String response = getResponse(nameValuePairs);
        if (response != null) {
            JSONArray json = getJSON(response);
            Customer cliente = new Customer();
            if (json != null) {
                JSONObject json_data;
                try {
                    json_data = json.getJSONObject(0);

                    cliente.setId(json_data.getInt("ID"));
                    cliente.setName(json_data.getString("Nome"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return cliente;
        } else {
            return null;
        }
    }

    public String getNomeCliente(String id) {
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "getNomeCliente"));
        nameValuePairs.add(new BasicNameValuePair("id", id));

        String response = getResponse(nameValuePairs);

        if (!response.equals("")) {
            return response;
        } else {
            return null;
        }
    }

    public boolean addEntrada(String orcamento, Uri superior, Uri frente,
                              Uri direita, Uri traseira, Uri esquerda, Uri painel) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addEntrada"));
        nameValuePairs.add(new BasicNameValuePair("orcamento", orcamento));
        if (superior != null)
            nameValuePairs.add(new BasicNameValuePair("ftSuperior",
                    "fotosuperior.jpg"));
        if (frente != null)
            nameValuePairs.add(new BasicNameValuePair("ftFrente",
                    "fotofrente.jpg"));
        if (direita != null)
            nameValuePairs.add(new BasicNameValuePair("ftLatDireita",
                    "fotodireita.jpg"));
        if (traseira != null)
            nameValuePairs.add(new BasicNameValuePair("ftTraseira",
                    "fototraseira.jpg"));
        if (esquerda != null)
            nameValuePairs.add(new BasicNameValuePair("ftLatEsquerda",
                    "fotoesquerda.jpg"));
        if (painel != null)
            nameValuePairs.add(new BasicNameValuePair("ftPainel",
                    "fotopainel.jpg"));

        String response = getResponse(nameValuePairs);

        return response.equals("SIM");
    }

    public boolean salvarOrcamento(PreOrder orcament) {
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "concluirOrcamento"));
        nameValuePairs
                .add(new BasicNameValuePair("placa", orcament.getPlaca()));
        nameValuePairs.add(new BasicNameValuePair("cliente", String
                .valueOf(orcament.getCliente().getId())));
        nameValuePairs
                .add(new BasicNameValuePair("marca", orcament.getMarca()));
        nameValuePairs.add(new BasicNameValuePair("modelo", orcament
                .getModelo()));

        for (int i = 0; i < 5; i++) {
            Bundle servico = new Bundle();

            for (int j = 0; j < orcament.getSuperior().getServices().size(); j++) {
                servico.putString("descricao", orcament.getSuperior()
                        .getServices().get(j).getDescricao());

            }

            Bundle local = new Bundle();
            local.putString("nomeLocal", "Superior");
            local.putString("imagemLocal", "superior.jpg");

        }
        nameValuePairs
                .add(new BasicNameValuePair("locais", orcament.getPlaca()));

        String response = getResponse(nameValuePairs);

        return response.equals("SIM");
    }

    public String getResponse(List<NameValuePair> nameValuePairs2) {
        if (ip.isEmpty()) {
            return null;
        } else {
            urlPhp = "http://" + ip
                    + ":1234/autosport/webservice.php";
            // Create a new HttpClient and Post Header
            ConnectivityManager connManager = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            String resposta = null;
            if (mWifi.isConnected()) {
                HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
// The default value is zero, that means the timeout is not used.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost(urlPhp);
                Log.d("Arthur", urlPhp);
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    List<NameValuePair> valores = nameValuePairs2;
                    httppost.setEntity(new UrlEncodedFormEntity(valores, "UTF-8"));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    if (response.getStatusLine().getStatusCode() == 200)
                        resposta = EntityUtils.toString(response.getEntity());

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ctx, "Sem conexão Wifi", Toast.LENGTH_LONG).show();
            }
            return resposta;
        }
    }

    public int addOrcamento(Customer cliente, String placa, String marca,
                            String modelo, double desconto) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addOrcamento"));
        nameValuePairs.add(new BasicNameValuePair("placa", placa));
        nameValuePairs.add(new BasicNameValuePair("cliente", String
                .valueOf(cliente.getId())));
        nameValuePairs.add(new BasicNameValuePair("marca", marca));
        nameValuePairs.add(new BasicNameValuePair("modelo", modelo));
        nameValuePairs.add(new BasicNameValuePair("desconto", String
                .valueOf(desconto)));

        String response = getResponse(nameValuePairs);
        int idorcamento = Integer.parseInt(response);

        return idorcamento;

    }

    public int addSuperior(int size, int idorcamento) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServico"));
        nameValuePairs.add(new BasicNameValuePair("tipo", "Superior"));
        nameValuePairs.add(new BasicNameValuePair("imagem", "superior.jpg"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("servicos", String
                .valueOf(size)));

        String response = getResponse(nameValuePairs);

        int resposta = Integer.parseInt(response);

        if (resposta != 0) {
            return resposta;
        } else {
            return 0;
        }
    }

    public int addLatDireita(int size, int idorcamento) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServico"));
        nameValuePairs.add(new BasicNameValuePair("tipo", "Lateral Direita"));
        nameValuePairs.add(new BasicNameValuePair("imagem", "latdireita.jpg"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("servicos", String
                .valueOf(size)));

        String response = getResponse(nameValuePairs);

        int resposta = Integer.parseInt(response);

        if (resposta != 0) {
            return resposta;
        } else {
            return 0;
        }
    }

    public int addFrente(int size, int idorcamento) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServico"));
        nameValuePairs.add(new BasicNameValuePair("tipo", "Frente"));
        nameValuePairs.add(new BasicNameValuePair("imagem", "frente.jpg"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("servicos", String
                .valueOf(size)));

        String response = getResponse(nameValuePairs);

        int resposta = Integer.parseInt(response);

        if (resposta != 0) {
            return resposta;
        } else {
            return 0;
        }
    }

    public int addTraseira(int size, int idorcamento) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServico"));
        nameValuePairs.add(new BasicNameValuePair("tipo", "Traseira"));
        nameValuePairs.add(new BasicNameValuePair("imagem", "traseira.jpg"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("servicos", String
                .valueOf(size)));

        String response = getResponse(nameValuePairs);

        int resposta = Integer.parseInt(response);

        if (resposta != 0) {
            return resposta;
        } else {
            return 0;
        }
    }

    public int addLatEsquerda(int size, int idorcamento) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServico"));
        nameValuePairs.add(new BasicNameValuePair("tipo", "Lateral Esquerda"));
        nameValuePairs.add(new BasicNameValuePair("imagem", "latesquerda.jpg"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        // nameValuePairs.add(new BasicNameValuePair("servicos",
        // String.valueOf(size)));

        String response = getResponse(nameValuePairs);

        int resposta = Integer.parseInt(response);

        if (resposta != 0) {
            return resposta;
        } else {
            return 0;
        }
    }

    public boolean addServico(int idorcamento, int local, String descricao,
                              String foto, double preco) {
        // TODO Auto-generated method stub
        boolean resposta = false;
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addServicoOrcamento"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("local", String
                .valueOf(local)));
        nameValuePairs.add(new BasicNameValuePair("descricao", descricao));
        nameValuePairs.add(new BasicNameValuePair("foto", foto));
        nameValuePairs.add(new BasicNameValuePair("preco", String
                .valueOf(preco)));

        String response = getResponse(nameValuePairs);

        if (response.equals("SIM")) {
            resposta = true;
        }

        return resposta;
    }

    public boolean addPeca(int idorcamento, String descricao, double preco) {
        // TODO Auto-generated method stub
        boolean resposta = false;
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "addPecaOrcamento"));
        nameValuePairs.add(new BasicNameValuePair("idorcamento", String
                .valueOf(idorcamento)));
        nameValuePairs.add(new BasicNameValuePair("descricao", descricao));
        nameValuePairs.add(new BasicNameValuePair("preco", String
                .valueOf(preco)));

        String response = getResponse(nameValuePairs);

        if (response.equals("SIM")) {
            resposta = true;
        }

        return resposta;
    }

    public boolean getOrcamento(int id) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "getOrcamento"));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));

        String response = getResponse(nameValuePairs);

        return response.equals("SIM");
    }

    public boolean getBuscaEntrada(int id) {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("op", "buscaEntrada"));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));

        String response = getResponse(nameValuePairs);

        return response.equals("SIM");
    }

    // fazer pegar os tipos
    // public ArrayList<TipoServico> getTipos() {
    // // TODO Auto-generated method stub
    //
    // ArrayList<TipoServico> tipos = new ArrayList<TipoServico>();
    // String response = getResponse("tipos");
    // JSONArray json = getJSON(response);
    //
    // for (int i = 0; i < json.length(); i++) {
    // JSONObject json_data;
    // try {
    // TipoServico tipo = new TipoServico();
    // json_data = json.getJSONObject(i);
    //
    // tipo.setID(json_data.getInt("ID"));
    // tipo.setName(json_data.getString("Servico"));
    //
    // tipos.add(tipo);
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }
    // return tipos;
    // }

}
