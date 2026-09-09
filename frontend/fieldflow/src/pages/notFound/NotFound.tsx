import { ArrowLeft, Home } from 'lucide-react'
import { Link } from 'react-router'

function NotFound() {
    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-950 px-6">
            <div className="flex max-w-lg flex-col items-center text-center">
                {/* Error code */}
                <div className="mb-6 flex h-24 w-24 items-center justify-center rounded-2xl border border-blue-500/20 bg-blue-500/10">
                    <span className="text-3xl font-bold text-blue-400">
                        404
                    </span>
                </div>

                {/* Title */}
                <h1 className="text-3xl font-bold tracking-tight text-white">
                    Página no encontrada
                </h1>

                <p className="mt-3 max-w-md text-sm leading-6 text-slate-400">
                    La página que estás buscando no existe o puede que
                    haya sido movida. Verifica la dirección e inténtalo
                    nuevamente.
                </p>

                {/* Actions */}
                <div className="mt-8 flex items-center gap-3">
                    <button
                        type="button"
                        onClick={() => window.history.back()}
                        className="inline-flex items-center gap-2 rounded-md border border-slate-700 bg-slate-900 px-4 py-2 text-sm font-medium text-slate-300 transition hover:bg-slate-800 hover:text-white"
                    >
                        <ArrowLeft className="h-4 w-4" />
                        Volver
                    </button>

                    <Link
                        to="/fieldflow/login"
                        className="inline-flex items-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
                    >
                        <Home className="h-4 w-4" />
                        Ir al inicio
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default NotFound