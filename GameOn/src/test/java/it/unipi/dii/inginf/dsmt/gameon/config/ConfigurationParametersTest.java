package it.unipi.dii.inginf.dsmt.gameon.config;

import it.unipi.dii.inginf.dsmt.gameon.persistence.KeyValueDBDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationParametersTest {

    @Test
    void getInstance() throws ExecutionException, InterruptedException {

        // Test the getInstance in multi-threading world

        Callable<ConfigurationParameters> callable = () -> ConfigurationParameters.getInstance();

        ExecutorService executor = Executors.newCachedThreadPool();
        final int NUM_THREAD = 5;

        List<Future<ConfigurationParameters>> futureList = new ArrayList<>();
        for (int i = 0; i < NUM_THREAD; i++) {
            futureList.add(executor.submit(callable));
        }

        List<ConfigurationParameters> configurationParameters = new ArrayList<>();
        for (int i = 0; i< NUM_THREAD; i++)
        {
            configurationParameters.add(futureList.get(i).get());
        }

        configurationParameters.stream().parallel()
                .forEach(configurationParameter -> assertSame(configurationParameter, ConfigurationParameters.getInstance()));

        executor.shutdown();
    }
}