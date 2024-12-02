import {ConnectClient, ConnectClientOptions} from "@vaadin/hilla-frontend";
import {LoggingMiddleware} from "Frontend/middlewares/logging-middleware";
import {keycloakAuthMiddleware} from "Frontend/middlewares/keycloak-auth-middleware";


const connectClientConfiguration: ConnectClientOptions = {
    prefix: "/connect",
    middlewares: [LoggingMiddleware, keycloakAuthMiddleware]
};

const connectClient: ConnectClient = new ConnectClient(connectClientConfiguration);

export default connectClient;