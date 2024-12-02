import {Middleware} from "@vaadin/hilla-frontend";
import keycloak from "Frontend/keycloak";


export const keycloakAuthMiddleware: Middleware = async function (context, next) {
    const request: Request = context.request;

    if (keycloak.token) {
        // add headers + authorization header to request
        request.headers.set('Authorization', `Bearer ${keycloak.token}`);
        console.log("Added Authorization header to request");
    }

    return next(context);
};