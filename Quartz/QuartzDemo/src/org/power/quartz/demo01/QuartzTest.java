package org.power.quartz.demo01;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest
{

    public static void main(String[] args)
    {
        new QuartzTest().run();
    }

    public void run()
    {
        try
        {
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();

            JobDetail job = new JobDetail("job1", QuartzJob.class);
            Trigger trigger = new SimpleTrigger("trigger01", "group1");

            scheduler.scheduleJob(job, trigger);

            // Start up the scheduler
            scheduler.start();

            System.out.println("mainThead:" + Thread.currentThread().getId());
            Thread.sleep(30 * 1000);

            // shut down the scheduler
            scheduler.shutdown(true);

        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
