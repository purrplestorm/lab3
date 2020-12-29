package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class CrawlerTask implements Runnable{

    public UrlPool urlPool;

    public CrawlerTask(UrlPool initUrlPool){
        urlPool = initUrlPool;
    }

    public synchronized void run(){
        while(true){
            UrlDepthPair pair = urlPool.getNextNotVisitedSite();
            getSites(pair);
        }
    }

    public synchronized void getSites(UrlDepthPair urlDepthPair){
        System.out.println(urlDepthPair.getString());
        LinkedList<UrlDepthPair> notVisitedSites = new LinkedList<>();

        try{
            URL url = new URL(urlDepthPair.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = reader.readLine();

            while(line != null){
                if(line.contains(UrlDepthPair.HTTP_PREFIX)){
                    line = line.substring(line.indexOf(UrlDepthPair.HTTP_PREFIX), line.length());
                    line = line.substring(0, line.indexOf("\""));

                    notVisitedSites.add(new UrlDepthPair(line, urlDepthPair.depth + 1));
                }

                if(line.contains(UrlDepthPair.HTTPS_PREFIX)){
                    line = line.substring(line.indexOf(UrlDepthPair.HTTPS_PREFIX), line.length());
                    line = line.substring(0, line.indexOf("\""));

                    notVisitedSites.add(new UrlDepthPair(line, urlDepthPair.depth + 1));
                }

                line = reader.readLine();
            }
            reader.close();
        }
        catch(Exception ex){

            }
        urlPool.addToNotVisited(notVisitedSites);
    }
}
