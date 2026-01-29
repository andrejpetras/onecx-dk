package org.tkit.onecx.test.dk.rs.kc.v2652.controllers;

import java.io.File;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.jboss.resteasy.reactive.RestResponse;

import gen.org.lorislab.keycloak.admin.v2652.model.*;
import gen.org.lorislab.keycloak.admin.v2652.server.api.RealmsAdminApi;

@ApplicationScoped
public class RealmsAdminRestController implements RealmsAdminApi {

    @Override
    public RestResponse<List<RealmRepresentation>> adminRealmsGet(Boolean briefRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsPost(@Valid File body) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmAdminEventsDelete(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<AdminEventRepresentation>> adminRealmsRealmAdminEventsGet(String realm, String authClient,
            String authIpAddress, String authRealm, String authUser, String dateFrom, String dateTo, String direction,
            Integer first, Integer max, List<String> operationTypes, String resourcePath, List<String> resourceTypes) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ClientRepresentation> adminRealmsRealmClientDescriptionConverterPost(String realm, @Valid String body) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ClientPoliciesRepresentation> adminRealmsRealmClientPoliciesPoliciesGet(String realm,
            Boolean includeGlobalPolicies) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmClientPoliciesPoliciesPut(String realm,
            @Valid ClientPoliciesRepresentation clientPoliciesRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ClientProfilesRepresentation> adminRealmsRealmClientPoliciesProfilesGet(String realm,
            Boolean includeGlobalProfiles) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmClientPoliciesProfilesPut(String realm,
            @Valid ClientProfilesRepresentation clientProfilesRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<Map>> adminRealmsRealmClientSessionStatsGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ClientTypesRepresentation> adminRealmsRealmClientTypesGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmClientTypesPut(String realm,
            @Valid ClientTypesRepresentation clientTypesRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<String>> adminRealmsRealmCredentialRegistratorsGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultDefaultClientScopesClientScopeIdDelete(String realm,
            String clientScopeId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultDefaultClientScopesClientScopeIdPut(String realm, String clientScopeId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<ClientScopeRepresentation>> adminRealmsRealmDefaultDefaultClientScopesGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<GroupRepresentation>> adminRealmsRealmDefaultGroupsGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultGroupsGroupIdDelete(String realm, String groupId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultGroupsGroupIdPut(String realm, String groupId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultOptionalClientScopesClientScopeIdDelete(String realm,
            String clientScopeId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDefaultOptionalClientScopesClientScopeIdPut(String realm, String clientScopeId) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<ClientScopeRepresentation>> adminRealmsRealmDefaultOptionalClientScopesGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmDelete(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<RealmEventsConfigRepresentation> adminRealmsRealmEventsConfigGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmEventsConfigPut(String realm,
            @Valid RealmEventsConfigRepresentation realmEventsConfigRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmEventsDelete(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<EventRepresentation>> adminRealmsRealmEventsGet(String realm, String client, String dateFrom,
            String dateTo, String direction, Integer first, String ipAddress, Integer max, List<String> type, String user) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<RealmRepresentation> adminRealmsRealmGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<GroupRepresentation> adminRealmsRealmGroupByPathPathGet(String realm,
            @Pattern(regexp = ".*") String path) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<List<String>> adminRealmsRealmLocalizationGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmLocalizationLocaleDelete(String realm, String locale) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Map<String, String>> adminRealmsRealmLocalizationLocaleGet(String realm, String locale,
            Boolean useRealmDefaultLocaleFallback) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmLocalizationLocaleKeyDelete(String realm, String key, String locale) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<String> adminRealmsRealmLocalizationLocaleKeyGet(String realm, String key, String locale) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmLocalizationLocaleKeyPut(String realm, String key, String locale,
            @Valid String body) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmLocalizationLocalePost(String realm, String locale,
            @Valid Map<String, String> requestBody) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<GlobalRequestResult> adminRealmsRealmLogoutAllPost(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<RealmRepresentation> adminRealmsRealmPartialExportPost(String realm, Boolean exportClients,
            Boolean exportGroupsAndRoles) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Object> adminRealmsRealmPartialImportPost(String realm, @Valid File body) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<GlobalRequestResult> adminRealmsRealmPushRevocationPost(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmPut(String realm, @Valid RealmRepresentation realmRepresentation) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmSessionsSessionDelete(String realm, String session, Boolean isOffline) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<Void> adminRealmsRealmTestSMTPConnectionPost(String realm, @Valid Map<String, String> requestBody) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ManagementPermissionReference> adminRealmsRealmUsersManagementPermissionsGet(String realm) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }

    @Override
    public RestResponse<ManagementPermissionReference> adminRealmsRealmUsersManagementPermissionsPut(String realm,
            @Valid ManagementPermissionReference managementPermissionReference) {
        return RestResponse.status(RestResponse.Status.NOT_IMPLEMENTED);
    }
}
