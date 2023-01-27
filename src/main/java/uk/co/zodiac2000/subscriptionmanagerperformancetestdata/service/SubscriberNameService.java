package uk.co.zodiac2000.subscriptionmanagerperformancetestdata.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.zodiac2000.subscriptionmanagerperformancetestdata.repository.CityNamesRepository;

/**
 * Service which produces deterministic subscriber names.
 */
@Service
public class SubscriberNameService {

    @Autowired
    private CityNamesRepository cityNamesRepository;


    private final List<String> cityNameModifiers = List.of(
            "University of {}",
            "{} College",
            "{} Library",
            "Technical Insitute of {}",
            "{} Business Park"
    );

    /**
     * Index should be less than 100,001.
     * @param index
     * @return 
     */
    public String getSubscriberName(Integer index) {
        if (index > 100000) {
            throw new IllegalArgumentException("Index must be less than 100001");
        }
        int cityIndex = index % this.cityNamesRepository.size();
        int modifierIndex = index / this.cityNamesRepository.size();
        return this.cityNameModifiers.get(modifierIndex).replace("{}", this.cityNamesRepository.getCityName(cityIndex));
    }
}
