package uk.co.zodiac2000.subscriptionmanagerperformancetestdata.service;

import java.util.Optional;
import java.util.Set;
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
     * @throws InterruptedException 
     */
    public void createSubscribers(int numberOfSubscribers) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            for (int index = 0; index < numberOfSubscribers; index = index + 1) {
                final int functionIndex = index;
                executorService.submit(() -> {
                    String subscriberName = this.subscriberNameService.getSubscriberName(functionIndex);
                    Set<OidcIdentifierCommandDto> oidcIdentifierCommandDtos
                            = this.subscriberOidcIdentifierCommandDtoService.getOidcIdentifierCommandDto(functionIndex);
                    Optional<SubscriberResponseDto> subscriber
                            = this.subscriberService.createSubscriber(new NewSubscriberCommandDto(subscriberName));
                    this.subscriberService.setOidcIdentifiers(subscriber.get().getId(), oidcIdentifierCommandDtos);
                    LOG.debug("Created subscriber {}", functionIndex);
                });
            }
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        }
    }
}
