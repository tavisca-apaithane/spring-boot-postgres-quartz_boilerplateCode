package com.example.quartzdemo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
@SpringBootApplication
public class QuartzDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzDemoApplication.class, args);

	}

	@Bean
	public CommandLineRunner demo() {
		return args -> {
			try {
				Properties properties = new Properties();

				properties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
				properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
				properties.put("org.quartz.jobStore.dataSource", "quartzDataSource");

				properties.put("org.quartz.dataSource.quartzDataSource.driver", "org.postgresql.Driver");
				properties.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:postgresql://localhost:5432/quartz");
				properties.put("org.quartz.dataSource.quartzDataSource.user", "quartz");
				properties.put("org.quartz.dataSource.quartzDataSource.password", "quartz123");
				properties.put("org.quartz.threadPool.threadCount", "5");

				Scheduler scheduler = new StdSchedulerFactory(properties).getScheduler();
				scheduler.start();
				System.out.println("scheduler started...");
				JobDetail job2 = newJob(printJob.class).withIdentity("job4").build();
				SimpleTrigger trigger1 = newTrigger().withIdentity("trigger4").startNow()
						.withSchedule(simpleSchedule().withIntervalInSeconds(5)
								.repeatForever()).build();
				scheduler.scheduleJob(job2, trigger1);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}

