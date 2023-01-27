package uk.co.zodiac2000.subscriptionmanagerperformancetestdata.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Repository that contains a list of city names that can be accessed by index. The repository contains 21939 city
 * names.
 */
@Component
public class CityNamesRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CityNamesRepository.class);

    private final List<String> cities = new ArrayList<>();

    /**
     * Loads city names from file source_data/cities.txt into this repository.
     * @throws IOException 
     */
    @PostConstruct
    public void initialize() throws IOException {
        LOG.info("Creating city names repository");
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("source_data/cities.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cities.add(line);
            }
        }
        LOG.info("Loaded {} city names", this.size());
    }

    /**
     * Returns the city name identified by index. Throws an exception if the index is out of range.
     * @param index the test data index
     * @return the city name
     */
    public String getCityName(int index) {
        return this.cities.get(index);
    }

    /**
     * @return the number of cities in this repository
     */
    public int size() {
        return this.cities.size();
    }
}
