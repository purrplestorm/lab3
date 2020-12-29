package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class UrlPool {
    public LinkedList<UrlDepthPair> visitedSites = new LinkedList<>();
    public LinkedList<UrlDepthPair> notVisitedSites = new LinkedList<>();
    private static Integer depth;
    private Integer numOfWaitingThreads = 0;

    public UrlPool(UrlDepthPair initUrl, Integer initDepth){
        notVisitedSites.add(initUrl);
        depth = initDepth;
    }

    public Integer getNumOfWaitingThreads(){ return numOfWaitingThreads; }

    public synchronized UrlDepthPair getNextNotVisitedSite()
    {
        while(notVisitedSites.isEmpty()){
            try{
                numOfWaitingThreads++;
                this.wait();
                numOfWaitingThreads--;
            }
            catch (InterruptedException ex){}
        }

        // Сразу удаляем пару из непосещенных сайтов и добавляем в посещенные

        UrlDepthPair pair = notVisitedSites.getFirst();
        notVisitedSites.remove(pair);
        visitedSites.add(pair);

        return pair;
    }

    public synchronized void addToNotVisited(LinkedList<UrlDepthPair> sites){
        // Здесь учитываем глубину
        try{
            if(sites.getFirst().depth > depth)
                return;
            else {
                notVisitedSites.addAll(sites);
                this.notify();
            }
        }
        catch (NoSuchElementException ex){
            return;
        }
    }
}
