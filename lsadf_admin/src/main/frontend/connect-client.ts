import {ConnectClient, ConnectClientOptions} from "@vaadin/hilla-frontend";
import {LoggingMiddleware} from "Frontend/middlewares/logging-middleware";


const connectClientConfiguration: ConnectClientOptions = {
    prefix: "/connect",
    middlewares: [LoggingMiddleware]
};

const connectClient: ConnectClient = new ConnectClient(connectClientConfiguration);

export default connectClient;