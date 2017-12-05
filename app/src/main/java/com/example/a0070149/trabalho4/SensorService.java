package com.example.a0070149.trabalho4;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by 0070149 on 04/12/2017.
 */

public class SensorService {

    private static final String WEBSERVICE_URL ="http://10.0.2.2:8080/ServicoREST/rest/sensores";

    public SensorService() { }

    public List<Sensor> carregarSensores() {
        List<Sensor> listaSensores = null;
        try {
            HttpURLConnection conexao = abrirConexao(new URL(WEBSERVICE_URL), "GET", false);
            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String jsonString = streamToString(conexao.getInputStream());
                Log.d("tag",jsonString);
                Type collectionType = new TypeToken<List<Sensor>>(){}.getType();
                listaSensores = (List<Sensor>) new Gson().fromJson( jsonString , collectionType);

            }
        } catch (Exception e) {
            Log.e("SensorService", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return listaSensores;
    }
    public boolean inserir(Sensor sensor){
        boolean sucesso = false;
        try {
            sucesso = enviarSensor("POST", sensor);
        } catch (Exception e) {
            Log.e("SensorService", "Exception: " + e.getMessage());
        }
        return sucesso;
    }
    public boolean excluir(Sensor sensor){
        boolean sucesso = false;
        try {
            sucesso = enviarSensor("DELETE", sensor);
        } catch (Exception e) {
            Log.e("SensorService", "Exception: " + e.getMessage());
        }
        return sucesso;
    }
    private HttpURLConnection abrirConexao(URL urlCon, String metodo, boolean doOutput) throws Exception {

        HttpURLConnection conexao = (HttpURLConnection) urlCon.openConnection();
        conexao.setReadTimeout(15000);
        conexao.setConnectTimeout(15000);
        conexao.setRequestMethod(metodo);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutput);
        if (doOutput) {
            conexao.addRequestProperty("Content-Type", "application/json");
        }
        conexao.connect();
        return conexao;
    }

    private boolean enviarSensor(String metodoHttp, Sensor sensor) throws Exception {
        boolean sucesso = false;
        boolean doOutput = !"DELETE".equals(metodoHttp);
        String url = WEBSERVICE_URL;
        if (!doOutput) {
            System.out.println("url -> "+url);
            url += "/"+ sensor.id;
        }

        HttpURLConnection conexao = abrirConexao(new URL(url), metodoHttp, doOutput);
        if (doOutput) {
            OutputStream os = conexao.getOutputStream();
            os.write(sensorToJsonBytes(sensor));
            os.flush();
            os.close();
        }
        int responseCode = conexao.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream is = conexao.getInputStream();
            String s = streamToString(is);

            is.close();
            JSONObject json = new JSONObject(s);
            sensor.id = json.getInt("id");
            sucesso = true;
        } else {
            sucesso = false;
        }
        conexao.disconnect();
        return sucesso;
    }

    private String streamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }
    private byte[] sensorToJsonBytes(Sensor sensor) {
        try {
            JSONObject jsonSensor = new JSONObject();
            if(sensor!=null) Log.e("PS", ""+sensor.id);
            jsonSensor.put("id", sensor.id);
            if(sensor!=null) Log.e("PS", ""+sensor.nome);
            jsonSensor.put("nome", sensor.nome);
            if(sensor!=null) Log.e("PS", ""+sensor.valor);
            jsonSensor.put("valor", sensor.valor);

            String json = jsonSensor.toString();
            Log.e("PSJ", "JSON"+json);
            return json.getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
