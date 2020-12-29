package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        String url;
        Integer depth, numOfThreads;

        if(args.length == 3)
            try{
                url = args[0];
                depth = Integer.parseInt(args[1]);
                numOfThreads = Integer.parseInt(args[2]);

                UrlPool urlPool = new UrlPool(new UrlDepthPair(url, 0), depth);

                ArrayList<Thread> taskList = new ArrayList<>();
                for(int i = 0; i < numOfThreads; i++){
                    taskList.add(new Thread(new CrawlerTask(urlPool)));
                }

                for(Thread t : taskList)
                    t.start();

                while(true){
                    if(urlPool.getNumOfWaitingThreads() == numOfThreads)
                    {
                        LinkedList<UrlDepthPair> s = urlPool.visitedSites;
                        for(UrlDepthPair i : s)
                            System.out.println(i.getString());
                        System.exit(0);
                    }
                    // Раз в секунду проверяем как там наши потоки
                    Thread.sleep(1000);
                }
            }
            catch (Exception ex){
                System.out.println("Ввод: lab3_1 <URL> <depth> <number of pools>");
                return;
            }
        else
            System.out.println("Ввод: lab3_1 <URL> <depth> <number of pools>");
    }
}
