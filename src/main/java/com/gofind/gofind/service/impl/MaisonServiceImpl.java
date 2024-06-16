package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.locations.Maison;
import com.gofind.gofind.repository.locations.MaisonRepository;
import com.gofind.gofind.service.locations.MaisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.locations.Maison}.
 */
@Service
@Transactional
public class MaisonServiceImpl implements MaisonService {

    private final Logger log = LoggerFactory.getLogger(MaisonServiceImpl.class);

    private final MaisonRepository maisonRepository;

    public MaisonServiceImpl(MaisonRepository maisonRepository) {
        this.maisonRepository = maisonRepository;
    }

    @Override
    public Mono<Maison> save(Maison maison) {
        log.debug("Request to save Maison : {}", maison);
        return maisonRepository.save(maison);
    }

    @Override
    public Mono<Maison> update(Maison maison) {
        log.debug("Request to update Maison : {}", maison);
        return maisonRepository.save(maison);
    }

    @Override
    public Mono<Maison> partialUpdate(Maison maison) {
        log.debug("Request to partially update Maison : {}", maison);

        return maisonRepository
            .findById(maison.getId())
            .map(existingMaison -> {
                if (maison.getAdresse() != null) {
                    existingMaison.setAdresse(maison.getAdresse());
                }
                if (maison.getDescription() != null) {
                    existingMaison.setDescription(maison.getDescription());
                }
                if (maison.getImage() != null) {
                    existingMaison.setImage(maison.getImage());
                }

                return existingMaison;
            })
            .flatMap(maisonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Maison> findAll(Pageable pageable, String search, String searchType) {
        log.debug("Request to get all Maisons");
        return maisonRepository.findAllBy(pageable, search, searchType);
    }

    public Mono<Long> countAll() {
        return maisonRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Maison> findOne(Long id) {
        log.debug("Request to get Maison : {}", id);
        return maisonRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Maison : {}", id);
        return maisonRepository.deleteById(id);
    }
}
