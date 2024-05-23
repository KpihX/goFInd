package com.gofind.gofind.service.impl;

import com.gofind.gofind.domain.Piece;
import com.gofind.gofind.repository.PieceRepository;
import com.gofind.gofind.service.PieceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.gofind.gofind.domain.Piece}.
 */
@Service
@Transactional
public class PieceServiceImpl implements PieceService {

    private final Logger log = LoggerFactory.getLogger(PieceServiceImpl.class);

    private final PieceRepository pieceRepository;

    public PieceServiceImpl(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
    }

    @Override
    public Mono<Piece> save(Piece piece) {
        log.debug("Request to save Piece : {}", piece);
        return pieceRepository.save(piece);
    }

    @Override
    public Mono<Piece> update(Piece piece) {
        log.debug("Request to update Piece : {}", piece);
        return pieceRepository.save(piece);
    }

    @Override
    public Mono<Piece> partialUpdate(Piece piece) {
        log.debug("Request to partially update Piece : {}", piece);

        return pieceRepository
            .findById(piece.getId())
            .map(existingPiece -> {
                if (piece.getLibelle() != null) {
                    existingPiece.setLibelle(piece.getLibelle());
                }
                if (piece.getImage() != null) {
                    existingPiece.setImage(piece.getImage());
                }
                if (piece.getEtat() != null) {
                    existingPiece.setEtat(piece.getEtat());
                }

                return existingPiece;
            })
            .flatMap(pieceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Piece> findAll(Pageable pageable) {
        log.debug("Request to get all Pieces");
        return pieceRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return pieceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Piece> findOne(Long id) {
        log.debug("Request to get Piece : {}", id);
        return pieceRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Piece : {}", id);
        return pieceRepository.deleteById(id);
    }
}
