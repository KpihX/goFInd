package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.objects.Objet;
import com.gofind.gofind.repository.objects.ObjetRepository;
import com.gofind.gofind.service.objects.ObjetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.objects.Objet}.
 */
@Service
@Transactional
public class ObjetServiceImpl implements ObjetService {

    private final Logger log = LoggerFactory.getLogger(ObjetServiceImpl.class);

    private final ObjetRepository objetRepository;

    public ObjetServiceImpl(ObjetRepository objetRepository) {
        this.objetRepository = objetRepository;
    }

    @Override
    public Mono<Objet> save(Objet objet) {
        log.debug("Request to save Objet : {}", objet);
        return objetRepository.save(objet);
    }

    @Override
    public Mono<Objet> update(Objet objet) {
        log.debug("Request to update Objet : {}", objet);
        return objetRepository.save(objet);
    }

    @Override
    public Mono<Objet> partialUpdate(Objet objet) {
        log.debug("Request to partially update Objet : {}", objet);

        return objetRepository
            .findById(objet.getId())
            .map(existingObjet -> {
                if (objet.getLibelle() != null) {
                    existingObjet.setLibelle(objet.getLibelle());
                }
                if (objet.getDescription() != null) {
                    existingObjet.setDescription(objet.getDescription());
                }
                if (objet.getType() != null) {
                    existingObjet.setType(objet.getType());
                }
                if (objet.getImage() != null) {
                    existingObjet.setImage(objet.getImage());
                }
                if (objet.getIdentifiant() != null) {
                    existingObjet.setIdentifiant(objet.getIdentifiant());
                }
                if (objet.getEtat() != null) {
                    existingObjet.setEtat(objet.getEtat());
                }

                return existingObjet;
            })
            .flatMap(objetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Objet> findAll(Pageable pageable, String search, String searchType) {
        log.debug("*** Request to get all Objets");
        // log.debug("*** Search: " + search);
        // log.debug("*** SearchType: " + searchType);
        return objetRepository.findAllBy(pageable, search, searchType);
    }

    public Mono<Long> countAll() {
        return objetRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Objet> findOne(Long id) {
        log.debug("Request to get Objet : {}", id);
        return objetRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Objet : {}", id);
        return objetRepository.deleteById(id);
    }
}
