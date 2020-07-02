package com.rklie.explore.quartz.tutorial01;

import org.apache.log4j.BasicConfigurator;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class MainApplication {

  private static Logger log = LoggerFactory.getLogger(MainApplication.class);

  public static void main(String[] args) {
    BasicConfigurator.configure();
    
    try {
      // Grab the Scheduler instance from the Factory
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

      // define the job and tie it to our HelloJob class
      JobDetail job = newJob(HelloJob.class)
          .withIdentity("myJob", "group1")
          .build();

      // Trigger the job to run now, and then every 40 seconds
      Trigger trigger = newTrigger()
          .withIdentity("myTrigger", "group1")
          .startNow()
          .withSchedule(simpleSchedule()
              .withIntervalInSeconds(10)
              .repeatForever())
          .build();

      // Tell quartz to schedule the job using our trigger
      scheduler.scheduleJob(job, trigger);

      // Need to be started explicitly.
      scheduler.start();

      // Application will keep running until terminated or the scheduler shutdown.
      // scheduler.shutdown();
    } catch (SchedulerException se) {
      log.error("uncaught exception", se);
    }
  }

  public static class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      log.info("Hello Quartz! jobExecutionContext={}", context);
    }
  }
}
