import {RouterProvider} from "react-router-dom";
import {router} from "Frontend/generated/routes";
import {createRoot} from "react-dom/client";
import {createElement} from "react";

function App() {
    return <RouterProvider router={router}/>;
}

const outlet = document.getElementById('outlet')!;
let root = (outlet as any)._root ?? createRoot(outlet);
(outlet as any)._root = root;
root.render(createElement(App));

