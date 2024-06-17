package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.users.Utilisateur;
import com.gofind.gofind.repository.users.UtilisateurRepository;
import com.gofind.gofind.service.users.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.users.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Mono<Utilisateur> save(Utilisateur utilisateur) {
        log.debug("Request to save Utilisateur : {}", utilisateur);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Mono<Utilisateur> update(Utilisateur utilisateur) {
        log.debug("Request to update Utilisateur : {}", utilisateur);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Mono<Utilisateur> partialUpdate(Utilisateur utilisateur) {
        log.debug("Request to partially update Utilisateur : {}", utilisateur);

        return utilisateurRepository
            .findById(utilisateur.getId())
            .map(existingUtilisateur -> {
                if (utilisateur.getTelephone() != null) {
                    existingUtilisateur.setTelephone(utilisateur.getTelephone());
                }

                return existingUtilisateur;
            })
            .flatMap(utilisateurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Utilisateur> findAll() {
        log.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll();
    }

    public Flux<Utilisateur> findAllWithEagerRelationships(Pageable pageable) {
        return utilisateurRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return utilisateurRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Utilisateur> findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        return utilisateurRepository.deleteById(id);
    }
}
