package org.tkit.onecx.test.dk.domain.model;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Realm {

    private String name;

    private final Map<String, Role> roles = new HashMap<>();

    private final Map<String, Client> clients = new HashMap<>();

    private final Map<String, User> users = new HashMap<>();

    private final Map<String, AuthorizationCode> codes = new HashMap<>();

    public void addRole(Role role) {
        roles.put(role.getName(), role);
    }

    public Role getRoles(String name) {
        return roles.get(name);
    }

    public Set<String> filterRole(Set<String> data) {
        if (data.isEmpty()) {
            return Collections.emptySet();
        }
        return data.stream()
                .filter(roles::containsKey)
                .filter(role -> roles.get(role).isEnabled())
                .collect(Collectors.toSet());
    }

    public void saveAuthCode(AuthorizationCode code) {
        codes.put(code.getCode(), code);
    }

    public Optional<AuthorizationCode> getAuthCode(String code) {
        AuthorizationCode ac = codes.get(code);
        if (ac == null)
            return Optional.empty();
        if (ac.getExpiresAt().isBefore(Instant.now())) {
            codes.remove(code);
            return Optional.empty();
        }
        return Optional.of(ac);
    }

    public void consumeAuthCode(String code) {
        codes.remove(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasUser(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean hasClient(String clientId) {
        return clients.containsKey(clientId);
    }

    public Client getClient(String id) {
        return clients.get(id);
    }

    public void addClient(Client client) {
        clients.put(client.getClientId(), client);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
}
