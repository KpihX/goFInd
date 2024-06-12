package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.locations.Location;
import com.gofind.gofind.repository.locations.LocationRepository;
import com.gofind.gofind.service.locations.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.locations.Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Mono<Location> save(Location location) {
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Mono<Location> update(Location location) {
        log.debug("Request to update Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Mono<Location> partialUpdate(Location location) {
        log.debug("Request to partially update Location : {}", location);

        return locationRepository
            .findById(location.getId())
            .map(existingLocation -> {
                if (location.getPrix() != null) {
                    existingLocation.setPrix(location.getPrix());
                }

                return existingLocation;
            })
            .flatMap(locationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return locationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        return locationRepository.deleteById(id);
    }
}
