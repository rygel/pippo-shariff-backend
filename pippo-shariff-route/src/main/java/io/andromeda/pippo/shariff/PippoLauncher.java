package io.andromeda.pippo.shariff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Pippo;

import java.io.File;
import java.util.Locale;

/**
 * Run application from here.
 */
public class PippoLauncher {
    /** The logger instance for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PippoLauncher.class);

    private PippoLauncher() {

    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("{} is closing normally.", Constants.APPLICATION_NAME);
            LOGGER.info("---------------------------------------------------------------------------------------------------");
        }, "Shutdown-thread"));


        LOGGER.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        LOGGER.info("Welcome to the webapp {} in version {}.", Constants.APPLICATION_NAME, Constants.APPLICATION_VERSION);
        Locale.setDefault(Locale.GERMANY);
        Pippo pippo = new Pippo(new PippoShareCountProxy());

        String currentPath = System.getProperty("user.dir");
        currentPath = currentPath + File.separator + "public";
        pippo.getApplication().addFileResourceRoute("/", currentPath);
        LOGGER.info("Public path set to: {}", currentPath);

        pippo.start();
    }

}
