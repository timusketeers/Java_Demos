package org.power.hook.main;

/**
 * 
 * 在cmd命令行中运行java -classpath shutdownHook.jar org.power.hook.main.Main
 * 
 * 这个命令，当我们按contrl+c退出的时候，控制台会打印"execute shutdown hook."
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
