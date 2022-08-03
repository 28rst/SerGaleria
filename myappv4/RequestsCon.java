package com.example.rafae.myappv4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rafae on 04/04/2018.
 */

public class RequestsCon {
    Logger log = Logger.getLogger("com.example.rafae.myAppv4.RequestCon");


    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;

        //StringBuilder object - guarda string obtida do servidor
        StringBuilder strBuilderFromServer = new StringBuilder();
        try {
            //Initializing Url
            url = new URL(requestURL);

            //httpurl connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //propriedades da conexão do url
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf8");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //output stream
            OutputStream outWriter = conn.getOutputStream();

            //passar outWriter parametros
            BufferedWriter buffWriter = new BufferedWriter(
                    new OutputStreamWriter(outWriter, "utf8"));
            buffWriter.write(getPostDataString(postDataParams));

            buffWriter.flush();
            buffWriter.close();
            outWriter.close();
            int codResposta = conn.getResponseCode();

            if (codResposta == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //strBuilderFromServer = new StringBuilder();
                String response;
                //le o que o servidor manda até que não haja nada para ler
                while ((response = br.readLine()) != null) {
                    strBuilderFromServer.append(response);
                    System.out.println("Testatar"+strBuilderFromServer.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //passar o builder para string legível

        System.out.println(strBuilderFromServer.toString());
        return strBuilderFromServer.toString();
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder strBuilderFromServer2 = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s;
            while ((s = buffReader.readLine()) != null) {
                strBuilderFromServer2.append(s + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
            log.warning("Erro: "+e);

        }
        return strBuilderFromServer2.toString();
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

        StringBuilder resultadoStrBuilder = new StringBuilder();
       // resultadoStrBuilder.append("\r\n");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                resultadoStrBuilder.append("&");

            resultadoStrBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8"));
            resultadoStrBuilder.append("=");
            resultadoStrBuilder.append(URLEncoder.encode(entry.getValue(), "utf-8"));

        }
        System.out.println(this.getClass().getName() + "sasa" + resultadoStrBuilder.toString());
        //resultadoStrBuilder.append("\r\n");
        System.out.println(this.getClass().getName() + resultadoStrBuilder.toString());
        return resultadoStrBuilder.toString();
    }

}
