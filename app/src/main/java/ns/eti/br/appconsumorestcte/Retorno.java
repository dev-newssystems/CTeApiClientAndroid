package ns.eti.br.appconsumorestcte;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Retorno extends AppCompatActivity {
    EditText Status;
    EditText Motivo;
    EditText Chave;
    EditText CStat;
    EditText XMotivo;
    EditText NRec;
    EditText Erros;
    Button btnDownload;
    Button btnVoltar;

    //variavel para definir o tipo de ambiente de envio
    final int tpAmb = 2;
    //variavel com valor P que define que iremos realizar apenas o download do PDF
    final String tpDown = "P";
    //varival do tipo byte que recebe a base64, formato de retorno do pdf
    byte[] binarioNota;
    //variavel que recebe o token informado da tela principal_main
    public String idTokenParamRet = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_retorno);
        //RECEBE ATRAVES DE UMA INTENT OS VALORES DO RETORNO PARA ABRIR NA NOVA ACTIVITY Retorno.java
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String statusParamRet = bundle.getString("statusParam");
        String motivoParamRet = bundle.getString("motivoParam");
        String chCTeParamRet = bundle.getString("chCTeParam");
        String cStatParamRet = bundle.getString("cStatParam");
        String xMotivoParamRet = bundle.getString("xMotivoParam");
        String nRecParamRet = bundle.getString("nRecParam");
        idTokenParamRet = bundle.getString("idTokenParam");

        //ATRIBIU OS VALORES RECEBIDOS DA INTENT SETANDO NOS CAMPOS DA TELA principal_retorno.xml
        Status = (EditText) findViewById(R.id.etStatus);
        Status.setText(statusParamRet);
        Motivo = (EditText) findViewById(R.id.etMotivo);
        Motivo.setText(motivoParamRet);
        Chave = (EditText) findViewById(R.id.etChave);
        Chave.setText(chCTeParamRet);
        CStat = (EditText) findViewById(R.id.etcStat);
        CStat.setText(cStatParamRet);
        XMotivo = (EditText) findViewById(R.id.etMotivo);
        XMotivo.setText(xMotivoParamRet);
        NRec = (EditText) findViewById(R.id.etnProt);
        NRec.setText(nRecParamRet);
        Erros = (EditText) findViewById(R.id.etndhRecbto);

        //GERA O NOVO JSON PARA CONSULTA STATUS DO PROCESAMENTO
        JSONObject jProduct = new JSONObject();
        try{
        jProduct.put("X-AUTH-TOKEN", idTokenParamRet);
        jProduct.put("chCTe", Chave.getText().toString());
        jProduct.put("nRec", NRec.getText().toString());
        jProduct.put("tpAmb", tpAmb);
        Log.d("JSON api ", "Json converted from retorno object: " + jProduct.toString());
        String jsonData = jProduct.toString();

        //CHAMA A CLASS QUE FAZ O ENVIO
        new StatusDadosCTe().execute(jsonData);
        }catch (JSONException e){
            e.printStackTrace();
        }

        //EVENTO CLIQUE DO BOTAO DOWNLOAD
        btnDownload = (Button) findViewById(R.id.btnDownloadPDF);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GERANDO JSON PARA REALIZAR DOWNLOAD DO PDF
                JSONObject jRetDownload = new JSONObject();
                try{
                    jRetDownload.put("X-AUTH-TOKEN", idTokenParamRet);
                    jRetDownload.put("chCTe", Chave.getText().toString());
                    jRetDownload.put("tpAmb", tpAmb);
                    jRetDownload.put("tpDown", tpDown);
                    String jsonDownload = jRetDownload.toString();
                    //CHAMA A CLASSE QUE FAZ O ENVIO DO JSON PARA NOSSO SERVIDOR
                    new DownloadDadosCTe().execute(jsonDownload);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //EVENTO CLIQUE DO BOTAO VOLTAR
        btnVoltar = (Button) findViewById(R.id.btnVoltarInicio);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retorno.this, Main.class);
                startActivity(intent);
            }
        });
    }

    private class StatusDadosCTe extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonDataAsk = params[0];
            HttpURLConnection  httpURLConnection = null;
            try {
                URL url = new URL("https://cte.ns.eti.br/cte/issueStatus");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //senad data
                try(OutputStream dos = httpURLConnection.getOutputStream()){
                    dos.write(jsonDataAsk.getBytes());
                    dos.flush();
                }
                try (BufferedReader br
                             = new BufferedReader(new InputStreamReader(
                        (httpURLConnection.getInputStream())))){
                    String output = "";
                    String character;
                    System.out.println("Output from Server .... \n");
                    while ((character = br.readLine()) != null) {
                        output += character;
                    }
                    return output;
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            } finally {
                httpURLConnection.disconnect();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String responseJsonStatus) {
            JSONObject json = null;
            try {
                //RECEBIDO O RETORNO DO STATUS AGORA É FEITA A CONVERSAO DO JSON RECEBIDO PARA AS VARIAVEIES PARA PODER UTILIZAR OS DADOS
                json = new JSONObject(responseJsonStatus);
                String status = json.getString("status");
                String motivo = json.getString("motivo");
                String chCTe = json.getString("chCTe");
                JSONObject subJson = json.getJSONObject("retProcCTe");
                String dhRecbto = subJson.getString("dhRecbto");
                String cStat = subJson.getString("cStat");
                String xMotivo = subJson.getString("xMotivo");
                String nProt = subJson.getString("nProt");
                //SETA OS RESULTADOS NA TELA DO ANDROID
                Status.setText(status.toString());
                Motivo.setText(motivo.toString());
                Chave.setText(chCTe.toString());
                CStat.setText(cStat.toString());
                XMotivo.setText(xMotivo.toString());
                NRec.setText(nProt.toString());
                Erros.setText(dhRecbto.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //CLASS QUE FAZ A REQUISIÇÃO DO DOWNLOAD PARA O SERVIDOR
    private class DownloadDadosCTe extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            String jsonDataAsk = params[0];
            HttpURLConnection  httpURLConnection = null;
            try {
                URL url = new URL("https://cte.ns.eti.br/cte/get");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //senad data
                try(OutputStream dos = httpURLConnection.getOutputStream()){
                    dos.write(jsonDataAsk.getBytes());
                    dos.flush();
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        (httpURLConnection.getInputStream())))){
                    String output = "";
                    String character;
                    System.out.println("Output from Server .... \n");
                    while ((character = br.readLine()) != null) {
                        output += character;
                    }
                    return output;
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            } finally {
                httpURLConnection.disconnect();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String responseJsonDownlaod) {
            JSONObject json = null;
            try {
                json = new JSONObject(responseJsonDownlaod);
                String status = json.getString("status");
                String motivo = json.getString("motivo");
                String pdf = json.getString("pdf");

                Log.d("JSON api", "Carrega Base64 vinda do Server: " + pdf);
                binarioNota = Base64.decode(pdf.getBytes(), Base64.DEFAULT);
                Log.d("JSON api", "Carrega Base64 convertida para gerar pdf: " + binarioNota);
                String nomeArq = "cte.pdf";
                try {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File arq = new File(path, nomeArq);
                    path.createNewFile();
                    FileOutputStream fos;
                    fos = new FileOutputStream(arq);
                    fos.write(binarioNota);
                    fos.flush();
                    fos.close();
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.fromFile(arq), "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Intent intent = Intent.createChooser(target, "Abrir o PDF");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Erro PDF : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
