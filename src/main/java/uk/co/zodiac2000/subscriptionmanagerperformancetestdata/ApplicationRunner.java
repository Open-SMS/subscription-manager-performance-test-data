package uk.co.zodiac2000.subscriptionmanagerperformancetestdata;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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

        CommandLine commandLine = readOptions(args);

        this.subscriberCreationService.createSubscribers(Integer.parseInt(commandLine.getOptionValue("n")),
                Integer.parseInt(commandLine.getOptionValue("t", "4")));
        LOG.info("Complete");
    }

    private CommandLine readOptions(String... args) {
        Options options = new Options();
        options.addOption(
                Option.builder("n")
                        .required()
                        .numberOfArgs(1)
                        .desc("The number of subscribers to create.")
                        .build());
        options.addOption(
                Option.builder("t")
                        .optionalArg(true)
                        .numberOfArgs(1)
                        .desc("The number of subscribers to threads to use (default 4).")
                        .build());
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException|RuntimeException ex) {
            System.err.println("Command failed. Reason: " + ex.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("gnu", options);
            System.exit(1);
        }
        return commandLine;
    }
}
