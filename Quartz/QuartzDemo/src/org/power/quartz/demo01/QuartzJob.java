package org.power.quartz.demo01;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob implements Job
{

    @Override
    public void execute(JobExecutionContext jobexecutioncontext)
            throws JobExecutionException
    {
        System.out.println("QuartzJob begin....");
        
        System.out.println("threadId:" + Thread.currentThread().getId());
        
        System.out.println("QuartzJob end....");
    }

}
