package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.itinaries.Trajet;
import com.gofind.gofind.repository.itinaries.TrajetRepository;
import com.gofind.gofind.service.itinaries.TrajetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.itinaries.Trajet}.
 */
@Service
@Transactional
public class TrajetServiceImpl implements TrajetService {

    private final Logger log = LoggerFactory.getLogger(TrajetServiceImpl.class);

    private final TrajetRepository trajetRepository;

    public TrajetServiceImpl(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }

    @Override
    public Mono<Trajet> save(Trajet trajet) {
        log.debug("Request to save Trajet : {}", trajet);
        return trajetRepository.save(trajet);
    }

    @Override
    public Mono<Trajet> update(Trajet trajet) {
        log.debug("Request to update Trajet : {}", trajet);
        return trajetRepository.save(trajet);
    }

    @Override
    public Mono<Trajet> partialUpdate(Trajet trajet) {
        log.debug("Request to partially update Trajet : {}", trajet);

        return trajetRepository
            .findById(trajet.getId())
            .map(existingTrajet -> {
                if (trajet.getDepart() != null) {
                    existingTrajet.setDepart(trajet.getDepart());
                }
                if (trajet.getArrivee() != null) {
                    existingTrajet.setArrivee(trajet.getArrivee());
                }
                if (trajet.getDateHeureDepart() != null) {
                    existingTrajet.setDateHeureDepart(trajet.getDateHeureDepart());
                }
                if (trajet.getPlaces() != null) {
                    existingTrajet.setPlaces(trajet.getPlaces());
                }
                if (trajet.getPrix() != null) {
                    existingTrajet.setPrix(trajet.getPrix());
                }

                return existingTrajet;
            })
            .flatMap(trajetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Trajet> findAll(Pageable pageable, String search, String search2) {
        log.debug("Request to get all Trajets");
        return trajetRepository.findAllBy(pageable, search, search2);
    }

    public Mono<Long> countAll() {
        return trajetRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Trajet> findOne(Long id) {
        log.debug("Request to get Trajet : {}", id);
        return trajetRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Trajet : {}", id);
        return trajetRepository.deleteById(id);
    }
}
