import Keycloak, {KeycloakConfig, KeycloakInstance} from "keycloak-js";


const keycloakConfig: KeycloakConfig = {
    url: 'http://localhost:8081',
    realm: 'LSADF',
    clientId: 'lsadf-admin-ui',
}

const keycloak: KeycloakInstance = new Keycloak(keycloakConfig);

export async function initKeycloak() {
    try {
        console.log("Initializing Keycloak...");
        keycloak.init({
            onLoad: 'login-required',
            pkceMethod: 'S256',
            redirectUri: window.location.origin + '/',
            checkLoginIframe: false,
        }).then((authenticated) => {
            if (!authenticated) {
                console.log("Unable to authenticate user. Logging out...");
            } else {
                console.log("Authenticated");
                console.log({keycloak});
                setInterval(() => {
                    keycloak
                        .updateToken(30)
                        .then((refreshed) => {
                            if (refreshed) {
                                console.log("Token refreshed");
                            }
                        })
                        .catch(() => {
                            console.error("Failed to refresh token. Logging out...");
                            keycloak.logout();
                        });
                }, 30000);
            }
        });
    } catch (err) {
        console.log("Keycloak init error", err);
    }
}

export default keycloak;