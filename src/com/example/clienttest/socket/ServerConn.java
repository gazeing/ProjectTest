package com.example.clienttest.socket;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;

import org.apache.http.HttpVersion;

import org.apache.http.NameValuePair;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.conn.ClientConnectionManager;

import org.apache.http.conn.params.ConnManagerPNames;

import org.apache.http.conn.params.ConnPerRouteBean;

import org.apache.http.conn.scheme.PlainSocketFactory;

import org.apache.http.conn.scheme.Scheme;

import org.apache.http.conn.scheme.SchemeRegistry;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import org.apache.http.message.BasicNameValuePair;

import org.apache.http.params.BasicHttpParams;

import org.apache.http.params.HttpConnectionParams;

import org.apache.http.params.HttpParams;

import org.apache.http.params.HttpProtocolParams;

import org.apache.http.protocol.BasicHttpContext;


import android.widget.Toast;


public class ServerConn 

{

    Integer                 miLogin = -1;

    BasicHttpContext httpContext;

    

    // Create and initialize scheme registry 

    SchemeRegistry schemeRegistry = new SchemeRegistry();


    public final static String WEB_SERVER_ADDR = "http://xxxxxxxxxx.com.au:8008/";

    

    ClientConnectionManager cm;

    HttpClient httpClient;    


    static boolean mbTurnOn = true;

    

    boolean mbOnCall = false;

    

    

    public ServerConn()

    {

        RegisterConn();

    }


        private boolean RegisterConn()

        {

                // Create and initialize HTTP parameters

            HttpParams httpParameters = new BasicHttpParams();

            httpParameters.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);

            httpParameters.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));

            httpParameters.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);

            httpParameters.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 20000);

            httpParameters.setParameter(HttpConnectionParams.SO_TIMEOUT, 20000);

        

            HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

            HttpProtocolParams.setContentCharset(httpParameters, "utf8");

            

           

            schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 8008));

    

            cm = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);

            httpClient = new DefaultHttpClient(cm, httpParameters);

            

            return true;

        }


        public void ShutDown()

        {

                if (cm != null)

                        cm.shutdown();

        }

        


    

    static int miCounterErr = 0;

    public synchronized String CallAntCorp_Post(String pUrl, List<NameValuePair> pDataSend, boolean pbForceClose)

    {

        String lsRet = "";

        

        if (!mbTurnOn || mbOnCall)

                return lsRet;

        

            try {

                mbOnCall = true;

                

                if (pbForceClose){

                        cm.closeIdleConnections(1, TimeUnit.MILLISECONDS);

                        cm.closeExpiredConnections();

                        

                        RegisterConn();

                }

                

                    HttpPost    httppost = new HttpPost(pUrl);


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                

                nameValuePairs.addAll(pDataSend.subList(0, pDataSend.size()));

                    if (HttpHelper.HasProtectForgery())

                        nameValuePairs.add(new BasicNameValuePair(HttpHelper.GetForgeryParam(), HttpHelper.GetForgeryToken()));


                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    

                        HttpResponse    response = httpClient.execute(httppost, new BasicHttpContext());


                        if (response != null) 

                                lsRet   = HttpHelper.request(response);

                        

                mbOnCall = false;

                miCounterErr = 0;

                

                nameValuePairs.clear();

                

                return lsRet;

                

            } catch (ClientProtocolException e) {

                miCounterErr++;

                if (miCounterErr > 50){

                        miCounterErr = 0;

                }

                

                httpClient.getConnectionManager().closeExpiredConnections();

                RegisterConn();

                

            } catch (IOException e) {

                miCounterErr++;

                if (miCounterErr > 50){

                        miCounterErr = 0;

                }


                if (miCounterErr > 3){

                        Toast.makeText(null, "slow internet connection", Toast.LENGTH_SHORT).show();

                        miCounterErr = 0;

                }

                

            } catch (Exception e) {

                miCounterErr++;

                if (miCounterErr > 50){

                                CrashReport.ReportToServer(e);

                        miCounterErr = 0;

                }

                

                httpClient.getConnectionManager().closeExpiredConnections();

                RegisterConn();

            }

        

                mbOnCall = false;

        return lsRet;

    }

    


        

        public String OpenGameAntCorpServer(String psBoardSpec, String psMode, String psGameId)

        {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        

        nameValuePairs.add(new BasicNameValuePair("game_id", psGameId ));

        nameValuePairs.add(new BasicNameValuePair("mode", psMode ));

        nameValuePairs.add(new BasicNameValuePair("board_specs", psBoardSpec ));


                String lsRet = CallAntCorp_Post(WEB_SERVER_ADDR + "controller/doit", nameValuePairs);

        lsRet = AntCorpInfo(lsRet);


            return lsRet;

        }


    private String CallAntCorp_Post(String string,
				List<NameValuePair> nameValuePairs) {
			// TODO Auto-generated method stub
    	
			return CallAntCorp_Post(string,nameValuePairs,true);
		}


	private String AntCorpInfo(String pStr)

    {

        int liIni, liEnd;

        

        liIni = pStr.indexOf("<QrOrb>");

        liEnd = pStr.indexOf("</QrOrb>");

        

        String lsStrCrypt = "";

        if (liIni < liEnd)

                lsStrCrypt =  pStr.substring(liIni + 7, liEnd);

        

        return lsStrCrypt;

        

    }

    

}
