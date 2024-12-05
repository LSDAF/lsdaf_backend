import {
    AppLayout,
    Button,
    DrawerToggle,
    Icon,
    ProgressBar,
    Scroller,
    SideNav,
    SideNavItem
} from "@vaadin/react-components";
import keycloak, {initKeycloak} from "Frontend/keycloak";
import {Suspense} from "react";
import {Outlet} from "react-router-dom";
import {Toaster} from "react-hot-toast";

initKeycloak().then(() => {
    console.log("Keycloak initialized");
});

const handleLogout = async function() {
    console.log("Logging out...");
    if (keycloak.authenticated) {
        await keycloak.logout();
    }
}

export default function Layout({children}: { children: React.ReactNode }) {

    const containerStyle = {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        width: '100%'
    };

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
                    <div style={containerStyle}>
                        <DrawerToggle/>
                        <h2>Orders</h2>
                        <Button theme="secondary" onClick={handleLogout}>Logout</Button>
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