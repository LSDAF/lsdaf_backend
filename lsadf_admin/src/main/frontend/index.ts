import keycloak from "Frontend/keycloak";


keycloak
    .init({
        onLoad: 'login-required',
        silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
        redirectUri: `${window.location.origin}/`,
        pkceMethod: 'S256',
        checkLoginIframe: false,
    })
    .then((authenticated) => {
        if (authenticated) {
            // Set up token refresh logic
            setInterval(() => {
               keycloak.updateToken(30).catch(() => {
                   console.error("Failed to refresh token. Logging out...");
                   keycloak.logout();
               });
            }, 30000); // Refresh token every 30 seconds
            http://localhost:8081/realms/LSADF/protocol/openid-connect/auth?client_id=lsadf-api-admin&redirect_uri=http%3A%2F%2Flocalhost%3A8082%2F&state=98d71f53-dc8b-4660-8abe-4ed600b249e8&response_mode=fragment&response_type=code&scope=openid&nonce=7c295284-6dd9-478d-8776-428f4799bb78&code_challenge=eW7n2v4bVW8KbhmI_bs4f5xUUNVOjkBjnZdBgCfGIZU&code_challenge_method=S256

        } else {
            console.error("Not authenticated");
        }
    })
    .catch((error) => {
        console.error("Keycloak init error", error);
    });