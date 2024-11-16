import {
    AppLayout,
    DrawerToggle,
    HorizontalLayout,
    Icon,
    Scroller,
    SideNav,
    SideNavItem
} from "@vaadin/react-components";

export default function Layout({ children }: { children: React.ReactNode }) {
    return (
        <AppLayout primarySection="drawer">
            <h1 slot="drawer">
                MyApp
            </h1>
            <Scroller slot="drawer" className="p-s">
                <SideNav>
                    <SideNavItem>
                        <Icon icon="vaadin:dashboard" slot="prefix" />
                        Dashboard
                    </SideNavItem>

                    <SideNavItem path="/orders">
                        <Icon icon="vaadin:cart" slot="prefix" />
                        Orders
                    </SideNavItem>
                </SideNav>
            </Scroller>

            <div slot="navbar">
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <DrawerToggle />
                    <h2>Orders</h2>
                </div>

                <HorizontalLayout className="h-m justify-center gap-s">
                    <a
                        href="/all"
                        className="flex items-center px-m text-secondary font-medium"
                        style={{ textDecoration: 'none' }}
                    >
                        All
                    </a>
                    <a
                        href="/open"
                        className="flex items-center px-m text-secondary font-medium"
                        style={{ textDecoration: 'none' }}
                    >
                        Open
                    </a>
                </HorizontalLayout>
            </div>

            { children }

        </AppLayout>
    );
}