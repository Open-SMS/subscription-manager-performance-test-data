package uk.co.zodiac2000.subscriptionmanagerperformancetestdata.service;

import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import org.springframework.stereotype.Service;
import uk.co.zodiac2000.subscriptionmanager.transfer.subscriber.OidcIdentifierClaimCommandDto;
import uk.co.zodiac2000.subscriptionmanager.transfer.subscriber.OidcIdentifierCommandDto;

/**
 * Class containing functions that construct a set of OidcIdentifierCommandDto suitable for creating test data.
 * The functions are deterministic depending on the {@code index} parameter. For a given revision of this code
 * the same data will always be returned for the same index.
 */
@Service
public class SubscriberOidcIdentifierCommandDtoService {

    /**
     * Defines a function the produces OIDC identifiers that might be expected to identify an individual's
     * Google account.
     */
    private final static IntFunction<Set<OidcIdentifierCommandDto>> GOOGLE_SUB = (index) -> {
        return Set.of(
                new OidcIdentifierCommandDto("https://accounts.google.com", List.of(
                        new OidcIdentifierClaimCommandDto("sub", String.format("%08X", index))
                ))
        );
    };

    /**
     * Defines a function the produces OIDC identifiers that might be expected to identify an individual's
     * Facebook account.
     */
    private final static IntFunction<Set<OidcIdentifierCommandDto>> FACEBOOK_SUB = (index) -> {
        return Set.of(
                new OidcIdentifierCommandDto("https://www.facebook.com", List.of(
                        new OidcIdentifierClaimCommandDto("sub", String.format("%012o", index))
                ))
        );
    };

    /**
     * Defines a function the produces OIDC identifiers that might be expected to identify an organization's
     * claims received via Open Athens Keystone.
     */
    private final static IntFunction<Set<OidcIdentifierCommandDto>> OPEN_ATHENS_EPSA = (index) -> {
        String entityId = "https://idp." + String.format("%08X", index) + "example.com/shib";
        String scope = String.format("%08X", index) + "university.ac.uk";
        return Set.of(
                new OidcIdentifierCommandDto("http://connect.openathens.net", List.of(
                        new OidcIdentifierClaimCommandDto("realmName", entityId),
                        new OidcIdentifierClaimCommandDto("derivedEduPersonScope", scope)
                ))
        );
    };

    /**
     * Defines a function the produces OIDC identifiers that might be expected to identify an organization's
     * account on a private OIDC IdP.
     */
    private final static IntFunction<Set<OidcIdentifierCommandDto>> PRIVATE_OIDC_GROUPS_ONE = (index) -> {
        String[] groups = {"staff", "admin", "member", "contractor"};
        String group = groups[index % 4];
        return Set.of(
                new OidcIdentifierCommandDto("http://auth.example.com", List.of(
                        new OidcIdentifierClaimCommandDto("groups", group)
                ))
        );
    };

    /**
     * Defines a list of 10 functions which are called to produce a set of OidcIdentifierCommandDto objects. The
     * number of occurrences of a function in the list determines approximately how many times each function is called
     * when generating test data. For example, because GOOGLE_SUB appears three times, approximately 30% of the
     * time this function is called.
     */
    private final static List<IntFunction<Set<OidcIdentifierCommandDto>>> OIDC_IDENTIFIER_FUNCTIONS = List.of(
        FACEBOOK_SUB, GOOGLE_SUB, FACEBOOK_SUB,
        GOOGLE_SUB, FACEBOOK_SUB, GOOGLE_SUB,
        OPEN_ATHENS_EPSA, PRIVATE_OIDC_GROUPS_ONE,
        PRIVATE_OIDC_GROUPS_ONE, OPEN_ATHENS_EPSA
    );

    /**
     * Returns a set of OidcIdentifierCommandDto objects the content of which is determined by {@code index}.
     * @param index the test data index
     * @return a set of OidcIdentifierCommandDto objects
     */
    public Set<OidcIdentifierCommandDto> getOidcIdentifierCommandDto(int index) {
        return OIDC_IDENTIFIER_FUNCTIONS.get(index % 10).apply(index);
    }
}
