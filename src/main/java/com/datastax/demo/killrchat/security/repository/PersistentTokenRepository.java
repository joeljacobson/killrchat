package com.datastax.demo.killrchat.security.repository;

import com.datastax.demo.killrchat.entity.PersistentTokenEntity;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.persistence.PersistenceManager;
import info.archinnov.achilles.type.OptionsBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

import static info.archinnov.achilles.type.OptionsBuilder.withTtl;

@Repository
public class PersistentTokenRepository {

    @Inject
    Session session;

    @Inject
    CassandraRepository repository;

    public static final int TOKEN_VALIDITY_DAYS = 31;

    public static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

    public void insert(PersistentTokenEntity token) {
        final BoundStatement bs = repository.createTokenPs.bind(token.getSeries(),
                token.getTokenValue(),
                token.getTokenDate(),
                token.getIpAddress(),
                token.getUserAgent(),
                token.getLogin(),
                token.getPass(),
                token.getAuthorities(),
                TOKEN_VALIDITY_SECONDS);
        session.execute(bs);
    }

    public void deleteById(String series) {
        repository.persistentTokenMapper.delete(series);
    }

    public PersistentTokenEntity findById(String presentedSeries) {
        return repository.persistentTokenMapper.get(presentedSeries);
    }

    public void update(PersistentTokenEntity token) {
        final BoundStatement bs = repository.updateTokenPs.bind(token.getTokenValue(), token.getSeries(), TOKEN_VALIDITY_SECONDS);
        session.execute(bs);
    }
}
