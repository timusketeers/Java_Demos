package com.power.core.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * 
 * @author li.zhang
 *
 */
public class JmxClient
{
    public static void main(String[] args) throws Exception
    {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        
        // Get domains from MBeanServer
        System.out.println("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains)
        {
            System.out.println("\tDomain = " + domain);
        }
        waitForEnterPressed();
        
        Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
        for (ObjectName name : names)
        {
            System.out.println("\tObjectName = " + name);
        }
        
        waitForEnterPressed();
        
        ObjectName mbeanName = new ObjectName("org.power:type=MyCar");

        ModelMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, ModelMBean.class, true);
        System.out.println("-----------------" + mbeanProxy.getAttribute("color"));
    }
    
    private static void waitForEnterPressed()
    {
        try
        {
            System.out.println("\nPress <Enter> to continue...");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
