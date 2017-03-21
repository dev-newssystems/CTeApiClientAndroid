package ns.eti.br.appconsumorestcte;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;

public class Main extends AppCompatActivity {

    EditText idToken;
    EditText numeroCTe;
    EditText dataHora;
    Button btnConsumir;
    Button btnRetornos;
    public String statusJson = "";
    public String motivoJson = "";
    public String chCTeJson = "";
    public String cStatJson = "";
    public String xMotivoJson = "";
    public String nRecJson = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_main);
        //CAPTURADAS AS INFORMAÇÕES DA TELA PRINCIPAL DO APP
        idToken = (EditText) findViewById(R.id.txtToken);
        numeroCTe = (EditText) findViewById(R.id.txtNumeroCTe);
        dataHora = (EditText) findViewById(R.id.txtDataHoraEdit);
        btnConsumir = (Button) findViewById(R.id.btnProcessar);
        btnRetornos = (Button) findViewById(R.id.btnRetorno);
        //BOTÃO RETORNO INICIA O APLICATIVO DESABILITADO, E SÓ SERÁ HABILITADO DEPOIS DO RETORNO DA EMISSAO DA CTE
        btnRetornos.setEnabled(false);
        //EVENTO CLIQUE DO BOTÃO PROCESSAR, QUANDO CLICAR NESTE BOTÃO O JSON SERÁ ENVIADO ENVIADO PARA NOSSO SERVIDOR
        btnConsumir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DEFINIDO O JSON PASSADO NA VARIAVEL descricaoJson PARA SER ENVIADA PARA O SERVIDOR, O CODUF, NUMEROCTE E DATAHORA DE EMISSÃO SÃO PASSADOS POR PARAMETRO
                //String descricaoJson = "{\"X-AUTH-TOKEN\": \""+ idToken.getText().toString() +"\",\"CTe\": { \"infCte\": { \"versao\": \"2.00\", \"ide\": { \"cUF\": \"43 \", \"cCT\": \" \", \"CFOP\": 5353, \"natOp\": \"PREST. DE SERV. DE TRANSP. E ESTAB. COMERCIAL\", \"forPag\": 0, \"mod\": 57, \"serie\": 0, \"nCT\":" + numeroCTe.getText().toString() + "\"dhEmi\": \"" + dataHora.getText().toString() + "\", \"tpImp\": 1, \"tpEmis\": 1, \"cDV\": 4, \"tpAmb\": 2, \"tpCTe\": 0, \"procEmi\": 0, \"verProc\": \"2.0.1.0\", \"cMunEnv\": 4303509, \"xMunEnv\": \"CAMAQUA\", \"UFEnv\": \"RS\", \"modal\": \"01\", \"tpServ\": 0, \"cMunIni\": 4314902, \"xMunIni\": \"PORTO ALEGRE\", \"UFIni\": \"RS\", \"cMunFim\": 4314902, \"xMunFim\": \"PORTO ALEGRE\", \"UFFim\": \"RS\", \"retira\": 1, \"toma03\": { \"toma\": 0 } }, \"compl\": { \"Entrega\": { \"semData\": { \"tpPer\": 0 }, \"semHora\": { \"tpHor\": 0 } }, \"xObs\": \"Observacao para o CTe\", \"ObsCont\": { \"xCampo\": \"OBS:\", \"xTexto\": \"Rota 03\" }, \"ObsFisco\": { \"xCampo\": \"OBS. FISCO\", \"xTexto\": \"FISCAL TRIBUTARIO\" } }, \"emit\": { \"CNPJ\": 12936129000195, \"IE\": 0170121003, \"xNome\": \"EMITENTE TRANSPS LTDA EPP\", \"xFant\": \"TRANSPORTES LTDA\", \"enderEmit\": { \"xLgr\": \"AV CONEGO SILVEIRA\", \"nro\": 135, \"xCpl\": \"SALA 03\", \"xBairro\": \"CENTRO\", \"cMun\": 4303509, \"xMun\": \"CAMAQUA\", \"CEP\": 96180000, \"UF\": \"RS\", \"fone\": 5136710000 } }, \"rem\": { \"CNPJ\": \"07364617000135\", \"IE\": \"0170108708\", \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\", \"fone\": 5136710000, \"enderReme\": { \"xLgr\": \"RUA FERNANDO MARQUEZ\", \"nro\": 145, \"xCpl\": \"PREDIO\", \"xBairro\": \"CENTRO\", \"cMun\": 4314902, \"xMun\": \"PORTO ALEGRE\", \"CEP\": 96900300, \"UF\": \"RS\", \"cPais\": 1058, \"xPais\": \"BRASIL\" }, \"email\": \"suporte@newssystems.eti.br\" }, \"exped\": { \"CNPJ\": \"07364617000135\", \"IE\": \"0170121003\", \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\", \"fone\": 5136710000, \"enderExped\": { \"xLgr\": \"RUA JOSE LUIZ\", \"nro\": 1450, \"xCpl\": \"SALA\", \"xBairro\": \"CENTRO\", \"cMun\": 4314902, \"xMun\": \"PORTO ALEGRE\", \"CEP\": 96900300, \"UF\": \"RS\", \"cPais\": 1058, \"xPais\": \"BRASIL\" }, \"email\": \"suporte@newssystems.eti.br\" }, \"receb\": { \"CNPJ\": \"07364617000135\", \"IE\": \"0170108708\", \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\", \"fone\": 5136710000, \"enderReceb\": { \"xLgr\": \"RUA BENTO\", \"nro\": 2140, \"xCpl\": \"SALA01\", \"xBairro\": \"CENTRO\", \"cMun\": 4314902, \"xMun\": \"PORTO ALEGRE\", \"CEP\": 96900300, \"UF\": \"RS\", \"cPais\": 1058, \"xPais\": \"BRASIL\" }, \"email\": \"suporte@newssystems.eti.br\" }, \"dest\": { \"CNPJ\": \"07364617000135\", \"IE\": \"0170108708\", \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\", \"fone\": 516920000, \"enderDest\": { \"xLgr\": \"AV LOMANTO\", \"nro\": 140, \"xCpl\": \"PREDIO\", \"xBairro\": \"SUICA\", \"cMun\": 4314902, \"xMun\": \"PORTO ALEGRE\", \"CEP\": 90900030, \"UF\": \"RS\", \"cPais\": 1058, \"xPais\": \"BRASIL\" } }, \"vPrest\": { \"vTPrest\": 6385.65, \"vRec\": 6385.65, \"Comp\": { \"xNome\": \"FRETE PESO\", \"vComp\": \"100.00\" } }, \"imp\": { \"ICMS\": { \"ICMSSN\": { \"indSN\": 1 } }, \"vTotTrib\": \"100.00\" }, \"infCTeNorm\": { \"infCarga\": { \"vCarga\": \"92335.50\", \"proPred\": \"DIESEL S-10\", \"xOutCat\": \"DIESEL S-10\", \"infQ\": { \"cUnid\": \"01\", \"tpMed\": \"PESO BRUTO\", \"qCarga\": \"0.6420\" } }, \"infDoc\": { \"infNFe\": { \"chave\": \"43140207364617000135550000000060221000060222\", \"dPrev\": \"2014-04-04\" } }, \"seg\": { \"respSeg\": 4, \"xSeg\": \"SUGURADORA\", \"nApol\": 456454, \"vCarga\": \"100.00\" }, \"peri\": { \"nONU\": 4567, \"qTotProd\": \"50.00\" }, \"cobr\": { \"fat\": { \"nFat\": 123645, \"vOrig\": 6385.65, \"vLiq\": 6385.65 }, \"dup\": [ { \"nDup\": 1515, \"dVenc\": \"2013-12-05\", \"vDup\": 3192.83 }, { \"nDup\": 1515, \"dVenc\": \"2013-12-15\", \"vDup\": 3192.82 } ] } }, \"autXML\": [ { \"CNPJ\": \"07364617000135\" }, { \"CPF\": \"00269925074\" } ] } }, \"infModal\": { \"versaoModal\": \"2.00\", \"rodo\": { \"RNTRC\": \"00052984\", \"dPrev\": \"2014-03-25\", \"lota\": 0, \"CIOT\": 123456789012, \"veic\": [ { \"RENAVAM\": 503050970, \"placa\": \"ESU2296\", \"tara\": 10000, \"capKG\": 130000, \"capM3\": 0, \"tpProp\": \"P\", \"tpVeic\": 0, \"tpRod\": \"03\", \"tpCar\": \"00\", \"UF\": \"SP\", \"prop\": { \"CPF\": \"00269925074\", \"RNTRC\": 12047259, \"xNome\": \"THIAGO DUMMER\", \"IE\": 577007617112, \"UF\": \"SP\", \"tpProp\": 0 } }, { \"RENAVAM\": 457291303, \"placa\": \"FDB5878\", \"tara\": 14500, \"capKG\": 37200, \"capM3\": 0, \"tpProp\": \"T\", \"tpVeic\": 1, \"tpRod\": \"06\", \"tpCar\": \"01\", \"UF\": \"SP\", \"prop\": { \"CPF\": \"00269925074\", \"RNTRC\": 12047259, \"xNome\": \"THIAGO DUMMER\", \"IE\": 577007617112, \"UF\": \"SP\", \"tpProp\": 0 } } ], \"moto\": { \"xNome\": \"MAX FERRAZ\", \"CPF\": \"01229452044\" } } }}";
                String descricaoJson = "{     \"X-AUTH-TOKEN\": \""+ idToken.getText().toString() +"\",     \"CTe\": {     \"infCte\": {       \"versao\": \"2.00\",       \"ide\": {         \"cUF\": 43,         \"cCT\": \"00000945\",         \"CFOP\": 5353,         \"natOp\": \"PREST. DE SERV. DE TRANSP. E ESTAB. COMERCIAL\",         \"forPag\": 0,         \"mod\": 57,         \"serie\": 0,         \"nCT\":" + numeroCTe.getText().toString() + "\"dhEmi\": \""+ dataHora.getText().toString() +"\",         \"tpImp\": 1,         \"tpEmis\": 1,         \"cDV\": 4,         \"tpAmb\": 2,         \"tpCTe\": 0,         \"procEmi\": 0,         \"verProc\": \"2.0.1.0\",         \"cMunEnv\": 4303509,         \"xMunEnv\": \"CAMAQUA\",         \"UFEnv\": \"RS\",         \"modal\": \"01\",         \"tpServ\": 0,         \"cMunIni\": 4314902,         \"xMunIni\": \"PORTO ALEGRE\",         \"UFIni\": \"RS\",         \"cMunFim\": 4314902,         \"xMunFim\": \"PORTO ALEGRE\",         \"UFFim\": \"RS\",         \"retira\": 1,         \"toma03\": {           \"toma\": 0         }       },       \"compl\": {         \"Entrega\": {           \"semData\": {             \"tpPer\": 0           },           \"semHora\": {             \"tpHor\": 0           }         },         \"xObs\": \"Observacao para o CTe\",         \"ObsCont\": {           \"xCampo\": \"OBS:\",           \"xTexto\": \"Rota 03\"         },         \"ObsFisco\": {           \"xCampo\": \"OBS. FISCO\",           \"xTexto\": \"FISCAL TRIBUTARIO\"         }       },       \"emit\": {         \"CNPJ\": \"12936129000195\",         \"IE\": \"0170121003\",         \"xNome\": \"EMITENTE TRANSPS LTDA EPP\",         \"xFant\": \"TRANSPORTES LTDA\",         \"enderEmit\": {           \"xLgr\": \"AV CONEGO SILVEIRA\",           \"nro\": 135,           \"xCpl\": \"SALA 03\",           \"xBairro\": \"CENTRO\",           \"cMun\": 4303509,           \"xMun\": \"CAMAQUA\",           \"CEP\": 96180000,           \"UF\": \"RS\",           \"fone\": 5136710000         }       },       \"rem\": {         \"CNPJ\": \"07364617000135\",         \"IE\": \"0170108708\",         \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\",         \"fone\": 5136710000,         \"enderReme\": {           \"xLgr\": \"RUA FERNANDO MARQUEZ\",           \"nro\": 145,           \"xCpl\": \"PREDIO\",           \"xBairro\": \"CENTRO\",           \"cMun\": 4314902,           \"xMun\": \"PORTO ALEGRE\",           \"CEP\": 96900300,           \"UF\": \"RS\",           \"cPais\": 1058,           \"xPais\": \"BRASIL\"         },         \"email\": \"suporte@newssystems.eti.br\"       },       \"exped\": {         \"CNPJ\": \"07364617000135\",         \"IE\": \"0170108708\",         \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\",         \"fone\": 5136710000,         \"enderExped\": {           \"xLgr\": \"RUA JOSE LUIZ\",           \"nro\": 1450,           \"xCpl\": \"SALA\",           \"xBairro\": \"CENTRO\",           \"cMun\": 4314902,           \"xMun\": \"PORTO ALEGRE\",           \"CEP\": 96900300,           \"UF\": \"RS\",           \"cPais\": 1058,           \"xPais\": \"BRASIL\"         },         \"email\": \"suporte@newssystems.eti.br\"       },       \"receb\": {         \"CNPJ\": \"07364617000135\",         \"IE\": \"0170108708\",         \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\",         \"fone\": 5136710000,         \"enderReceb\": {           \"xLgr\": \"RUA BENTO\",           \"nro\": 2140,           \"xCpl\": \"SALA01\",           \"xBairro\": \"CENTRO\",           \"cMun\": 4314902,           \"xMun\": \"PORTO ALEGRE\",           \"CEP\": 96900300,           \"UF\": \"RS\",           \"cPais\": 1058,           \"xPais\": \"BRASIL\"         },         \"email\": \"suporte@newssystems.eti.br\"       },       \"dest\": {         \"CNPJ\": \"07364617000135\",         \"IE\": \"0170108708\",         \"xNome\": \"CT-E EMITIDO EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL\",         \"fone\": 516920000,         \"enderDest\": {           \"xLgr\": \"AV LOMANTO\",           \"nro\": 140,           \"xCpl\": \"PREDIO\",           \"xBairro\": \"SUICA\",           \"cMun\": 4314902,           \"xMun\": \"PORTO ALEGRE\",           \"CEP\": 90900030,           \"UF\": \"RS\",           \"cPais\": 1058,           \"xPais\": \"BRASIL\"         }       },       \"vPrest\": {         \"vTPrest\": 6385.65,         \"vRec\": 6385.65,         \"Comp\": {           \"xNome\": \"FRETE PESO\",           \"vComp\": \"100.00\"         }       },       \"imp\": {         \"ICMS\": {           \"ICMSSN\": {             \"indSN\": 1           }         },         \"vTotTrib\": \"100.00\"       },       \"infCTeNorm\": {         \"infCarga\": {           \"vCarga\": \"92335.50\",           \"proPred\": \"DIESEL S-10\",           \"xOutCat\": \"DIESEL S-10\",           \"infQ\": {             \"cUnid\": \"01\",             \"tpMed\": \"PESO BRUTO\",             \"qCarga\": \"0.6420\"           }         },         \"infDoc\": {           \"infNFe\": {             \"chave\": \"43140207364617000135550000000060221000060222\",             \"dPrev\": \"2014-04-04\"           }         },         \"seg\": {           \"respSeg\": 4,           \"xSeg\": \"SUGURADORA\",           \"nApol\": 456454,           \"vCarga\": \"100.00\"         },         \"peri\": {           \"nONU\": 4567,           \"qTotProd\": \"50.00\"         },         \"cobr\": {           \"fat\": {             \"nFat\": 123645,             \"vOrig\": 6385.65,             \"vLiq\": 6385.65           },           \"dup\": [             {               \"nDup\": 1515,               \"dVenc\": \"2013-12-05\",               \"vDup\": 3192.83             },             {               \"nDup\": 1515,               \"dVenc\": \"2013-12-15\",               \"vDup\": 3192.82             }           ]         }       },       \"autXML\": [         {           \"CNPJ\": \"07364617000135\"         },         {           \"CPF\": \"00269925074\"         }       ]     }   },   \"infModal\": {     \"versaoModal\": \"2.00\",     \"rodo\": {       \"RNTRC\": \"00052984\",       \"dPrev\": \"2014-03-25\",       \"lota\": 0,       \"CIOT\": 123456789012,       \"veic\": [         {           \"RENAVAM\": 111222333,           \"placa\": \"ESU2296\",           \"tara\": 10000,           \"capKG\": 130000,           \"capM3\": 0,           \"tpProp\": \"P\",           \"tpVeic\": 0,           \"tpRod\": \"03\",           \"tpCar\": \"00\",           \"UF\": \"SP\",           \"prop\": {             \"CPF\": \"00269925074\",             \"RNTRC\": 11112222,             \"xNome\": \"THIAGO DUMMER\",             \"IE\": 111222333444,             \"UF\": \"SP\",             \"tpProp\": 0           }         },         {           \"RENAVAM\": 111222333,           \"placa\": \"IQU1122\",           \"tara\": 14500,           \"capKG\": 37200,           \"capM3\": 0,           \"tpProp\": \"T\",           \"tpVeic\": 1,           \"tpRod\": \"06\",           \"tpCar\": \"01\",           \"UF\": \"SP\",           \"prop\": {             \"CPF\": \"00269925074\",             \"RNTRC\": 11223344,             \"xNome\": \"THIAGO DUMMER\",             \"IE\": 111222333444,             \"UF\": \"SP\",             \"tpProp\": 0           }         }       ],       \"moto\": {         \"xNome\": \"GLAIDSON MENDES\",         \"CPF\": \"22255566644\"       }     }   } }";
                try{
                    String jsonData = descricaoJson.toString();
                    new EnviarDadosCTe().execute(jsonData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //EVENTO CLIQUE DO BOTAO RETORNO, QUANDO CLICAR NESTE BOTAO IRA ABRIR UMA NOVA PAGINA E CARREGAR AS INFORMACOES DO STATUS DE PROCESSAMENTO DO CTE (SE FOI AUTORIZADO OU REJEITADO)
        btnRetornos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetorno = new Intent(Main.this, Retorno.class);
                String statusParam = "";
                String motivoParam = "";
                String chCTeParam = "";
                String cStatParam = "";
                String xMotivoParam = "";
                String nRecParam = "";
                String idTokenParam = "";
                //PASSANDO OS PARAMETROS PARA ABRIR NA ACTIVITY Retorno.class
                statusParam = statusJson.toString();
                motivoParam = motivoJson.toString();
                chCTeParam = chCTeJson.toString();
                cStatParam = cStatJson.toString();
                xMotivoParam = xMotivoJson.toString();
                nRecParam = nRecJson.toString();
                idTokenParam = idToken.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("statusParam",statusParam);
                bundle.putString("motivoParam",motivoParam);
                bundle.putString("chCTeParam",chCTeParam);
                bundle.putString("cStatParam",cStatParam);
                bundle.putString("xMotivoParam",xMotivoParam);
                bundle.putString("nRecParam",nRecParam);
                bundle.putString("idTokenParam",idTokenParam);
                intentRetorno.putExtras(bundle);
                startActivity(intentRetorno);
            }
        });
    }

    //CLASS QUE REALIZA A CONEXAO COM O SERVIDOR VIA ASYNCTASK
    private class EnviarDadosCTe extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonDataAsk = params[0];
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL("https://cte.ns.eti.br/cte/issue");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //envio direto json
                try {
                    OutputStream dos = httpURLConnection.getOutputStream();
                    dos.write(jsonDataAsk.getBytes());
                    dos.flush();
                } catch (UnknownServiceException e) {
                    e.printStackTrace();
                }
                //retorno da requisicao
                try (BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())))) {
                    String output = "";
                    String character;
                    System.out.println("Output from Server .... \n");
                    while ((character = br.readLine()) != null) {
                        output += character;
                    }
                    return output;
                }
            } catch (Exception e) {
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
        protected void onPostExecute(String responseJson) {
            JSONObject json = null;
            try {
                json = new JSONObject(responseJson);
                statusJson = json.getString("status");
                motivoJson = json.getString("motivo");
                chCTeJson = json.getString("chCTe");
                JSONObject subJson = json.getJSONObject("retEnviCte");
                cStatJson = subJson.getString("cStat");
                xMotivoJson = subJson.getString("xMotivo");
                nRecJson = subJson.getString("nRec");
                btnRetornos.setEnabled(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
