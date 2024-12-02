import Keycloak, {KeycloakConfig, KeycloakInstance} from "keycloak-js";


const keycloakConfig: KeycloakConfig = {
    url: 'http://localhost:8081',
    realm: 'LSADF',
    clientId: 'lsadf-api-admin',
}

const keycloak: KeycloakInstance = new Keycloak(keycloakConfig);

export default keycloak;