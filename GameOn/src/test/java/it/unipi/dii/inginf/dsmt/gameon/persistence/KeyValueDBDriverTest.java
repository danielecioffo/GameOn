package it.unipi.dii.inginf.dsmt.gameon.persistence;

import it.unipi.dii.inginf.dsmt.gameon.config.ConfigurationParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class KeyValueDBDriverTest {

    @Test
    void getInstance() throws ExecutionException, InterruptedException {

        // Test the getInstance in multi-threading world
        Callable<KeyValueDBDriver> callable = () -> KeyValueDBDriver.getInstance();

        ExecutorService executor = Executors.newCachedThreadPool();
        final int NUM_THREAD = 5;

        List<Future<KeyValueDBDriver>> futureList = new ArrayList<>();
        for (int i = 0; i < NUM_THREAD; i++) {
            futureList.add(executor.submit(callable));
        }

        List<KeyValueDBDriver> keyValueDBDrivers = new ArrayList<>();
        for (int i=0; i<NUM_THREAD; i++)
        {
            keyValueDBDrivers.add(futureList.get(i).get());
        }

        keyValueDBDrivers.stream().parallel()
                .forEach(keyValueDBDriver -> assertSame(keyValueDBDriver, KeyValueDBDriver.getInstance()));

        executor.shutdown();
    }
}