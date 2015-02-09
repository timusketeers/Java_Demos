package com.power.core.bean.modelmbean;

/**
 * 要受jmx托管的类，使用ModelMbean方式
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
