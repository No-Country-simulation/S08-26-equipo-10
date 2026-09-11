import { Outlet } from 'react-router'
import NavBar from './NavBar'
import SideBar from './SideBar'

function MainLayout() {
    return (
        <div className="flex h-screen overflow-hidden bg-slate-950">
            {/* Sidebar fijo */}
            <SideBar />

            <div className="flex min-w-0 flex-1 flex-col">
                {/* Navbar fijo */}
                <NavBar />

                {/* Única zona con scroll */}
                <main className="min-h-0 flex-1 overflow-y-auto">
                    <Outlet />
                </main>
            </div>
        </div>
    )
}

export default MainLayout