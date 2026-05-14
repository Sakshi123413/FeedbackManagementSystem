import * as React from "react"
import { cn } from "@/lib/utils"

export interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string
  error?: string
}

const Textarea = React.forwardRef<HTMLTextAreaElement, TextareaProps>(
  ({ className, label, error, ...props }, ref) => {
    return (
      <div className="space-y-2">
        {label && (
          <label className="text-sm font-medium text-foreground">
            {label}
          </label>
        )}
        <textarea
          className={cn(
            "flex min-h-[100px] w-full rounded-lg border border-input bg-background px-4 py-3 text-sm transition-smooth",
            "placeholder:text-muted-foreground",
            "focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent",
            "disabled:cursor-not-allowed disabled:opacity-50 resize-none",
            error && "border-destructive focus:ring-destructive",
            className
          )}
          ref={ref}
          {...props}
        />
        {error && (
          <p className="text-xs text-destructive animate-fade-in">{error}</p>
        )}
      </div>
    )
  }
)
Textarea.displayName = "Textarea"

export { Textarea }
