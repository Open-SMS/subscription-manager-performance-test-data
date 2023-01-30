package uk.co.zodiac2000.subscriptionmanagerperformancetestdata.service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.zodiac2000.subscriptionmanager.service.SubscriberService;
import uk.co.zodiac2000.subscriptionmanager.transfer.subscriber.NewSubscriberCommandDto;
import uk.co.zodiac2000.subscriptionmanager.transfer.subscriber.OidcIdentifierCommandDto;
import uk.co.zodiac2000.subscriptionmanager.transfer.subscriber.SubscriberResponseDto;

/**
 * Service containing methods for creating subscribers.
 */
@Service
public class SubscriberCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriberCreationService.class);

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberNameService subscriberNameService;

    @Autowired
    private SubscriberOidcIdentifierCommandDtoService subscriberOidcIdentifierCommandDtoService;

    /**
     * Creates subscribers.
     * @param numberOfSubscribers the number of subscribers to create
     * @param numberOfThreads the number of threads to use
     * @throws InterruptedException
     */
    public void createSubscribers(int numberOfSubscribers, int numberOfThreads) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CompletionService completionService = new ExecutorCompletionService(executorService);
        try {
            for (int index = 0; index < numberOfSubscribers; index = index + 1) {
                final int subscriberIndex = index;
                completionService.submit(() -> {
                    String subscriberName = this.subscriberNameService.getSubscriberName(subscriberIndex);
                    Set<OidcIdentifierCommandDto> oidcIdentifierCommandDtos
                            = this.subscriberOidcIdentifierCommandDtoService
                                    .getOidcIdentifierCommandDto(subscriberIndex);
                    Optional<SubscriberResponseDto> subscriber
                            = this.subscriberService.createSubscriber(new NewSubscriberCommandDto(subscriberName));
                    this.subscriberService.setOidcIdentifiers(subscriber.get().getId(), oidcIdentifierCommandDtos);
                    LOG.debug("Created subscriber {}", subscriberIndex);
                    return true;
                });
            }
            // Wait for tasks to complete and output progress of completed tasks at 10% intervals.
            final int progressMessageIndex = numberOfSubscribers / 10;
            for (int completedTasks = 0; completedTasks < numberOfSubscribers; completedTasks++) {
                completionService.take();
                if (completedTasks % progressMessageIndex == 0) {
                    Float percentComplete = (((float) completedTasks / (float) numberOfSubscribers) * 100);
                    System.out.println("Progress: " + percentComplete.intValue() + "%");
                }
            }
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        }
    }
}
