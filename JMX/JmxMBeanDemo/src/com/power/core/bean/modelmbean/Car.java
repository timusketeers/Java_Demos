package com.power.core.bean.modelmbean;

/**
 * Ҫ��jmx�йܵ��࣬ʹ��ModelMbean��ʽ
 * @author li.zhang
 *
 */
public class Car
{
    /** color **/
    private String color;

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }
    
    public void drive(int speed)
    {
        System.out.println("the car is driving at " + speed + " per second.");
    }
}
