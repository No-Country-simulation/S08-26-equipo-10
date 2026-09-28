interface FormFieldProps {
    label: string;
    icon: React.ElementType;
    children: React.ReactNode;
}

function FormField({ label, icon: Icon, children }: FormFieldProps) {
    return (
        <div>
            <label className="mb-2 flex items-center gap-2 text-sm font-medium text-slate-300">
                <Icon className="h-4 w-4 text-slate-500" />
                {label}
            </label>

            {children}
        </div>
    );
}

export default FormField;