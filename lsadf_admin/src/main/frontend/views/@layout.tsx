import {
    AppLayout,
    DrawerToggle,
    HorizontalLayout,
    Icon, ProgressBar,
    Scroller,
    SideNav,
    SideNavItem
} from "@vaadin/react-components";
import {Suspense} from "react";
import {Outlet} from "react-router-dom";

export default function Layout({ children }: { children: React.ReactNode }) {
    return (
        <AppLayout primarySection="drawer">
            <h1 slot="drawer">
                LSADF Admin
            </h1>
            <Scroller slot="drawer" className="p-s">
                <SideNav>
                    <SideNavItem path="/users">
                        <Icon icon="vaadin:user" slot="prefix" />
                        Users
                    </SideNavItem>
                    <SideNavItem path="/gameSaves">
                        <Icon icon="vaadin:gamepad" slot="prefix" />
                        Game Saves
                    </SideNavItem>
                </SideNav>
            </Scroller>

            <div slot="navbar">
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <DrawerToggle />
                    <h2>Orders</h2>
                </div>

                {/*<HorizontalLayout className="h-m justify-center gap-s">*/}
                {/*    <a*/}
                {/*        href="/all"*/}
                {/*        className="flex items-center px-m text-secondary font-medium"*/}
                {/*        style={{ textDecoration: 'none' }}*/}
                {/*    >*/}
                {/*        All*/}
                {/*    </a>*/}
                {/*    <a*/}
                {/*        href="/open"*/}
                {/*        className="flex items-center px-m text-secondary font-medium"*/}
                {/*        style={{ textDecoration: 'none' }}*/}
                {/*    >*/}
                {/*        Open*/}
                {/*    </a>*/}
                {/*</HorizontalLayout>*/}
            </div>

            <Suspense fallback={<ProgressBar indeterminate={true} className="m-0" />}>
                <Outlet />
            </Suspense>

        </AppLayout>
    );
}