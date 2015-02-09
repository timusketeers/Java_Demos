package com.power.core.bean.standardmbean;

public interface CatMBean
{
    public void sleep(int seconds);
    
    public void eat();

    public void setSleepTime(int seconds);

    public int getSleepTime();
}
