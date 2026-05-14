import { Star } from "lucide-react"
import { cn } from "@/lib/utils"

interface RatingInputProps {
  value: number
  onChange: (value: number) => void
  max?: number
  label?: string
  error?: string
}

export function RatingInput({ value, onChange, max = 5, label, error }: RatingInputProps) {
  return (
    <div className="space-y-2">
      {label && (
        <label className="text-sm font-medium text-foreground">{label}</label>
      )}
      <div className="flex gap-1">
        {Array.from({ length: max }, (_, i) => i + 1).map(star => (
          <button
            key={star}
            type="button"
            onClick={() => onChange(star)}
            className="transition-smooth hover:scale-110 active:scale-95"
          >
            <Star
              className={cn(
                "h-7 w-7 transition-smooth",
                star <= value
                  ? "fill-warning text-warning"
                  : "fill-transparent text-muted-foreground hover:text-warning/60"
              )}
            />
          </button>
        ))}
      </div>
      {error && (
        <p className="text-xs text-destructive animate-fade-in">{error}</p>
      )}
    </div>
  )
}
