package org.tkit.onecx.test.dk.domain.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.inject.Singleton;

import org.tkit.onecx.test.dk.domain.model.Realm;

@Singleton
public class RealmService {

    private static final Map<String, Realm> REALMS = new ConcurrentHashMap<>();

    public void addRealm(Realm realm) {
        REALMS.put(realm.getName(), realm);
    }

    public Realm getRealm(String realm) {
        return REALMS.get(realm);
    }

}
