# Subscription Manager - Test Data Creator

This application creates test data in order to perform performance tests with the
subscription manager application.

## Details

Test data is deterministic. Exactly the same test data will be created for a given
invocation of a specific version of this application.

At present test data is created involving Subscribers and their associated OIDC
identifiers. A maximum of 100,000 subscribers can be created with the current
version.

Subscriber names are based on a list of cities combined with various prefixes and
suffixes so that 100,000 unique names are created. Each subscriber is associated
with OIDC identifiers that approximately simulate the different types of claims
that different identity providers might release.

## Usage

```
java -jar path/to/subscription-manager-performance-test-data-{VERSION}-SNAPSHOT.jar \
    -n {NUMBER_OF_SUBSCRIBERS} [-t {NUMBER_OF_THREADS}]
```

* NUMBER_OF_SUBSCRIBERS - the number of subscribers to create, max 100,000
* NUMBER_OF_THREADS - the number of threads to use. Defaults to 4.
