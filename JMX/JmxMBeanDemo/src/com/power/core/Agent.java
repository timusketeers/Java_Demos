package com.power.core;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import com.power.core.bean.modelmbean.Car;
import com.power.core.bean.standardmbean.Cat;
import com.sun.jdmk.comm.HtmlAdaptorServer;

public class Agent
{
    /** 要被托管的类. **/
    private static final String MANAGED_CLASS_NAME = "com.power.core.bean.Car";
    
    private MBeanServer server;
    
    public Agent()
    {
        server = MBeanServerFactory.createMBeanServer();
    }
    
    public MBeanServer getMBeanServer()
    {
        return server;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Agent agent = new Agent();
        MBeanServer server = agent.getMBeanServer();
        Car car = new Car();
        
        String name = "org.power.modelMBean:type=MyCar";
        System.out.println(name);
        ObjectName oname =  agent.createObjectName(name);
        ModelMBean mbean = agent.createMBean(); 
        try
        {
            //1.注册一个modelMBean
            mbean.setManagedResource(car, "ObjectReference");//设置受托管的资源..
            server.registerMBean(mbean, oname);
            
            //2.注册一个标准mbean.
            String catMBname = "org.power.stardardMBean:type=MyCat";
            ObjectName onameForCat = agent.createObjectName(catMBname);
            Cat cat = new Cat();
            server.registerMBean(cat, onameForCat);
            
            HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
            ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=18080");
            adaptor.setPort(18080);
            server.registerMBean(adaptor, adapterName);
            
            adaptor.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            Attribute attribute = new Attribute("color", "green");
            server.setAttribute(oname, attribute);
            
            //调用属性..
            String attr = (String)server.getAttribute(oname, "color");
            System.out.println(attr);
            
            //调用方法..
            Object[] param = new Object[1];
            param[0] = 100;
            String[] signature = new String[1];
            signature[0] = "int";
                
            server.invoke(oname, "drive", param, signature);
            
        }
        catch (InstanceNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAttributeValueException e)
        {
            e.printStackTrace();
        }
        catch (AttributeNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ReflectionException e)
        {
            e.printStackTrace();
        }
        catch (MBeanException e)
        {
            e.printStackTrace();
        }
        
        while(true);
        
    }

    private ObjectName createObjectName(String name)
    {
        ObjectName oname = null;
        try
        {
            oname = new ObjectName(name);
        }
        catch (MalformedObjectNameException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        
        return oname;
    }
    
    private ModelMBean createMBean()
    {
        //设置受jmx托管的对象的属性.即就是这个对象的哪些属性是受托管的.
        ModelMBeanAttributeInfo[] attributes = new ModelMBeanAttributeInfo[1];
        attributes[0] = new ModelMBeanAttributeInfo("color", "java.lang.String", null, true, true, false, null);
        
        ModelMBeanConstructorInfo[] constructors = null;
        
        //设置受jmx托管的对象的方法.即就是这个对象的哪些方法是受托管的.
        ModelMBeanOperationInfo[] operations = new ModelMBeanOperationInfo[1];
        MBeanParameterInfo[] signature = new MBeanParameterInfo[1];
        signature[0] = new MBeanParameterInfo("speed", "int", null, null);
        operations[0] = new ModelMBeanOperationInfo("drive", null, signature, "void", ModelMBeanOperationInfo.INFO, null);
        
        
        ModelMBeanNotificationInfo[] notifications = null;
        Descriptor descriptor = null;
        ModelMBeanInfo mbeanInfo = new ModelMBeanInfoSupport(Agent.MANAGED_CLASS_NAME, null,
                attributes, constructors, operations, notifications, descriptor);
        
        ModelMBean modelMBean = null;
        try
        {
            modelMBean = new RequiredModelMBean(mbeanInfo);
        }
        catch (RuntimeOperationsException e)
        {
            e.printStackTrace();
        }
        catch (MBeanException e)
        {
            e.printStackTrace();
        }
        
        return modelMBean;
    }
}
