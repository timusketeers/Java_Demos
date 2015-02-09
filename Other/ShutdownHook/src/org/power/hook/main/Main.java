package org.power.hook.main;

/**
 * 
 * ��cmd������������java -classpath shutdownHook.jar org.power.hook.main.Main
 * 
 * �����������ǰ�contrl+c�˳���ʱ�򣬿���̨���ӡ"execute shutdown hook."
 * 
 * @author li.zhang
 *
 */
public class Main
{

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                System.out.println("execute shutdown hook.");
            }
        });
        
        while (true);
    }

}
