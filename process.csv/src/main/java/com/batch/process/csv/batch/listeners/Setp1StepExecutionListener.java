package com.batch.process.csv.batch.listeners;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Setp1StepExecutionListener implements StepExecutionListener {
    @Autowired
    Logger logger;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("STEP STARTED AT: " + stepExecution.getStartTime().toString());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("STEP ENDED AT: " + stepExecution.getStartTime().toString());
        return stepExecution.getExitStatus();
    }
}
