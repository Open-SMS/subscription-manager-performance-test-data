package uk.co.zodiac2000.subscriptionmanagerperformancetestdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import uk.co.zodiac2000.subscriptionmanagerperformancetestdata.service.SubscriberCreationService;

/**
 * Implementation of CommandLineRunner which executes code which creates test data.
 */
@Component
@ComponentScan("uk.co.zodiac2000.subscriptionmanager")
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private SubscriberCreationService subscriberCreationService;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running...");
        this.subscriberCreationService.createSubscribers(100000);
        LOG.info("Complete");
    }
}
