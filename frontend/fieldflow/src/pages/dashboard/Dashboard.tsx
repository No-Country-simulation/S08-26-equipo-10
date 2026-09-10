import { Button } from "@/components/common/Button"
import { TriangleAlert } from "lucide-react";
function Dashboard() {
    return (
        <div>
            <h1>Dashboard</h1>
            <Button variant="primary" onClick={() => console.log("Primary button clicked")}>
                Primary Button
            </Button>
            <Button variant="secondary" onClick={() => console.log("Secondary button clicked")}>
                Secondary Button
            </Button>
            <Button variant="danger" icon={TriangleAlert} onClick={() => console.log("Danger button clicked")}>
                Danger Button
            </Button>
            <Button variant="ghost" onClick={() => console.log("Ghost button clicked")}>
                Ghost Button
            </Button>
        </div>
    )
}

export default Dashboard
