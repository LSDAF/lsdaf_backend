import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

export default function ErrorView() {
    return (
        <div>
            <h1>Error</h1>
            <p>Page not found</p>
        </div>
    );
}


export const config: ViewConfig = {
    route: "*",
    menu: {
        exclude: true,
    },
}