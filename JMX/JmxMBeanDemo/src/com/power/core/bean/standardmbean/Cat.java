package com.power.core.bean.standardmbean;

public class Cat implements CatMBean
{
    /** sleepTime **/
    private int sleepTime;
    
    public void sleep(int seconds)
    {
        System.out.println("sleep for " + seconds + "seconds");
    }

    public void eat()
    {
        System.out.println("cat is eating food....");
    }

    public void setSleepTime(int seconds)
    {
        this.sleepTime = seconds;
    }

    public int getSleepTime()
    {
        return sleepTime;
    }
}
