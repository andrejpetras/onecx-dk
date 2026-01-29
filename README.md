# onecx-dk
OneCx Developer Keycloak


## Test

```shell
VERIFIER=xxxx3
CHAL=$(printf "$VERIFIER" | openssl dgst -binary -sha256 | openssl base64 -A | tr '+/' '-_' | tr -d '=')
```

http://localhost:9090/realms/onecx/protocol/openid-connect/auth?response_type=code&client_id=web-portal&redirect_uri=https://www.youtube.com/&scope=openid%20email&state=foo&nonce=n1&code_challenge_method=S256&code_challenge=Rjxc-5eBu6MfVmBkODoIvlufDCZ18RQOHjxKioEDKFc

```shell
curl -X POST http://localhost:9090/realms/onecx/protocol/openid-connect/token -H 'Content-Type: application/x-www-form-urlencoded' -d "grant_type=authorization_code&client_id=web-portal&code=b5c12333-4fe5-48f0-9941-457f7a4333b6&redirect_uri=https://www.youtube.com/&code_verifier=xxxx3" | jq
```