import { useState } from "react";
import { Mail, Lock, Eye, EyeOff, ArrowRight } from "lucide-react";
import fieldflowLogo from "@/assets/logo.svg";

function Login() {
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log({
            email,
            password,
        });

        // Aquí posteriormente conectaremos con el backend
    };

    return (
        <main className="relative min-h-screen overflow-hidden bg-slate-950">
            {/* Background */}
            <div
                className="absolute inset-0 bg-cover bg-center"
                style={{
                    backgroundImage: "url('/src/assets/images/fondo.png')",
                }}
            />

            {/* Overlay */}
            <div className="absolute inset-0 bg-slate-950/35" />

            {/* Contenido */}
            <div className="relative z-10 flex min-h-screen items-center justify-between px-8 py-10 lg:px-16 xl:px-24">

                {/* Lado izquierdo */}
                <section className="hidden max-w-xl text-white lg:block">
                    {/* Logo */}
                    <div className="mb-12 flex items-center gap-4">
                        <div className="flex h-14 w-14 items-center justify-center">
                            <img
                                src={fieldflowLogo}
                                alt="FieldFlow"
                                className="h-16 w-16"
                            />
                        </div>

                        <span className="text-5xl font-bold tracking-tight">
                            FieldFlow
                        </span>
                    </div>

                    {/* Texto */}
                    <h1 className="max-w-lg text-4xl font-bold leading-tight xl:text-5xl">
                        Tecnología que
                        <br />
                        mantiene tu operación
                        <br />
                        en movimiento
                    </h1>

                    <p className="mt-6 max-w-md text-xl leading-relaxed text-slate-200">
                        Gestiona, asigna y da seguimiento
                        <br />
                        a tus servicios técnicos desde
                        <br />
                        un solo lugar.
                    </p>
                </section>

                {/* Login */}
                <section className="w-full max-w-xl">
                    <div className="rounded-2xl border border-slate-700/70 bg-slate-950/90 p-8 shadow-2xl backdrop-blur-md sm:p-10 lg:p-12">

                        {/* Header */}
                        <div className="mb-10">
                            <h2 className="text-4xl font-bold text-white">
                                Iniciar sesión
                            </h2>

                            <p className="mt-3 text-lg leading-relaxed text-slate-300">
                                Bienvenido de nuevo, ingresa a tu cuenta
                                <br className="hidden sm:block" />
                                de FieldFlow.
                            </p>
                        </div>

                        <form onSubmit={handleSubmit} className="space-y-7">

                            {/* Email */}
                            <div className="relative">
                                <Mail
                                    size={25}
                                    strokeWidth={1.8}
                                    className="absolute left-5 top-1/2 -translate-y-1/2 text-slate-200"
                                />

                                <input
                                    type="email"
                                    placeholder="Correo electrónico"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                    className="
                    h-17
                    w-full
                    rounded-xl
                    border
                    border-slate-600
                    bg-slate-900/80
                    pl-18
                    pr-5
                    text-lg
                    text-white
                    outline-none
                    transition
                    placeholder:text-slate-400
                    focus:border-blue-500
                    focus:ring-2
                    focus:ring-blue-500/20
                  "
                                />
                            </div>

                            {/* Password */}
                            <div className="relative">
                                <Lock
                                    size={25}
                                    strokeWidth={1.8}
                                    className="absolute left-5 top-1/2 -translate-y-1/2 text-slate-200"
                                />

                                <input
                                    type={showPassword ? "text" : "password"}
                                    placeholder="Contraseña"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                    className="
                    h-17
                    w-full
                    rounded-xl
                    border
                    border-slate-600
                    bg-slate-900/80
                    pl-18
                    pr-14
                    text-lg
                    text-white
                    outline-none
                    transition
                    placeholder:text-slate-400
                    focus:border-blue-500
                    focus:ring-2
                    focus:ring-blue-500/20
                  "
                                />

                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="
                    absolute
                    right-5
                    top-1/2
                    -translate-y-1/2
                    text-slate-400
                    transition
                    hover:text-white
                  "
                                    aria-label={
                                        showPassword
                                            ? "Ocultar contraseña"
                                            : "Mostrar contraseña"
                                    }
                                >
                                    {showPassword ? (
                                        <EyeOff size={25} />
                                    ) : (
                                        <Eye size={25} />
                                    )}
                                </button>
                            </div>

                            {/* Submit */}
                            <button
                                type="submit"
                                className="
                  group
                  flex
                  h-17
                  w-full
                  items-center
                  justify-center
                  gap-4
                  rounded-xl
                  bg-blue-500
                  text-lg
                  font-medium
                  text-white
                  shadow-lg
                  shadow-blue-500/20
                  transition
                  hover:bg-blue-600
                  hover:shadow-blue-500/30
                  active:scale-[0.99]
                "
                            >
                                <span>Iniciar sesión</span>

                                <ArrowRight
                                    size={28}
                                    className="transition-transform group-hover:translate-x-1"
                                />
                            </button>
                        </form>
                    </div>
                </section>
            </div>
        </main>
    );
}

export default Login;