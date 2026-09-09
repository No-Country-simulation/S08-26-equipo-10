import { Outlet } from 'react-router'
import NavBar from './NavBar'
import SideBar from './SideBar'

function MainLayout() {
    return (
        <div className="flex min-h-screen bg-slate-950">
            <SideBar />
            <div className="flex min-w-0 flex-1 flex-col">
                <NavBar />
                <main className="flex-1">
                    <Outlet />
                </main>
            </div>

        </div>
    )
}

export default MainLayout