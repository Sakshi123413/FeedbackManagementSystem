import React, { createContext, useContext, useState, useCallback } from "react"
import { cn } from "@/lib/utils"
import { CheckCircle, XCircle, AlertCircle, X } from "lucide-react"

type ToastType = "success" | "error" | "warning"

interface Toast {
  id: string
  message: string
  type: ToastType
}

interface ToastContextType {
  showToast: (message: string, type: ToastType) => void
}

const ToastContext = createContext<ToastContextType | undefined>(undefined)

export function useToast() {
  const context = useContext(ToastContext)
  if (!context) throw new Error("useToast must be used within ToastProvider")
  return context
}

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = useState<Toast[]>([])

  const showToast = useCallback((message: string, type: ToastType) => {
    const id = Math.random().toString(36).substring(7)
    setToasts(prev => [...prev, { id, message, type }])
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id))
    }, 4000)
  }, [])

  const removeToast = useCallback((id: string) => {
    setToasts(prev => prev.filter(t => t.id !== id))
  }, [])

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      <div className="fixed top-4 right-4 z-50 flex flex-col gap-2 max-w-sm">
        {toasts.map(toast => (
          <ToastItem key={toast.id} toast={toast} onClose={() => removeToast(toast.id)} />
        ))}
      </div>
    </ToastContext.Provider>
  )
}

function ToastItem({ toast, onClose }: { toast: Toast; onClose: () => void }) {
  const icons = {
    success: <CheckCircle className="h-5 w-5 text-success" />,
    error: <XCircle className="h-5 w-5 text-destructive" />,
    warning: <AlertCircle className="h-5 w-5 text-warning" />,
  }

  return (
    <div
      className={cn(
        "flex items-center gap-3 rounded-xl border bg-card p-4 shadow-card animate-slide-in-right",
        "backdrop-blur-sm"
      )}
    >
      {icons[toast.type]}
      <p className="text-sm font-medium text-card-foreground flex-1">{toast.message}</p>
      <button
        onClick={onClose}
        className="text-muted-foreground hover:text-foreground transition-smooth"
      >
        <X className="h-4 w-4" />
      </button>
    </div>
  )
}
