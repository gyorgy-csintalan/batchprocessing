package com.batch.process.csv.batch.listeners;

import com.batch.process.csv.batch.dto.PersonDTO;
import org.slf4j.Logger;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.batch.api.chunk.listener.SkipProcessListener;

@Component
public class PersonDtoSkipProcessListener implements SkipListener {
    @Autowired
    Logger logger;

    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(Object o, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(Object o, Throwable throwable) {
        logger.info("Skipping " + ((PersonDTO)o).toString() + '\n' + throwable.getMessage());
    }
}
