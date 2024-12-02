import {initKeycloak} from "Frontend/keycloak";


initKeycloak().then(() => {
    console.log("Keycloak initialized");
});

// keycloak
//     .init({
//         onLoad: 'login-required',
//         pkceMethod: 'S256',
//         checkLoginIframe: false,
//     })
//     .then((authenticated) => {
//         if (authenticated) {
//             console.log({keycloak});
//             // Set up token refresh logic
//             setInterval(() => {
//                 keycloak.updateToken(30).catch(() => {
//                     console.error("Failed to refresh token. Logging out...");
//                     keycloak.logout();
//                 });
//             }, 30000); // Refresh token every 30 seconds
//
//         } else {
//             console.error("Not authenticated");
//         }
//     })
//     .catch((error) => {
//         console.error("Keycloak init error", error);
//     });