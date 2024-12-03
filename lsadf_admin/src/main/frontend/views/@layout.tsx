import {AppLayout, DrawerToggle, Icon, ProgressBar, Scroller, SideNav, SideNavItem} from "@vaadin/react-components";
import {initKeycloak} from "Frontend/keycloak";
import {Suspense} from "react";
import {Outlet} from "react-router-dom";
import {Toaster} from "react-hot-toast";

initKeycloak().then(() => {
    console.log("Keycloak initialized");
});


export default function Layout({children}: { children: React.ReactNode }) {

    return (
        <div>
            <AppLayout primarySection="drawer">
                <h1 slot="drawer">
                    LSADF Admin
                </h1>
                <Scroller slot="drawer" className="p-s">
                    <SideNav>
                        <SideNavItem path="/users">
                            <Icon icon="vaadin:user" slot="prefix"/>
                            Users
                        </SideNavItem>
                        <SideNavItem path="/gameSaves">
                            <Icon icon="vaadin:gamepad" slot="prefix"/>
                            Game Saves
                        </SideNavItem>
                    </SideNav>
                </Scroller>

                <div slot="navbar">
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <DrawerToggle/>
                        <h2>Orders</h2>
                    </div>
                </div>

                <Suspense fallback={<ProgressBar indeterminate={true} className="m-0"/>}>
                    <Outlet/>
                </Suspense>

            </AppLayout>
            <Toaster/>
        </div>
    );
}