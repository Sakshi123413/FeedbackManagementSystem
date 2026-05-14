import { Moon, Sun } from "lucide-react"
import { useTheme } from "@/context/ThemeContext"
import { cn } from "@/lib/utils"

export function ThemeToggle({ className }: { className?: string }) {
  const { theme, toggleTheme } = useTheme()

  return (
    <button
      onClick={toggleTheme}
      className={cn(
        "relative h-10 w-10 rounded-lg border border-input bg-background",
        "flex items-center justify-center",
        "hover:bg-accent transition-smooth",
        className
      )}
      aria-label="Toggle theme"
    >
      {theme === "light" ? (
        <Moon className="h-4 w-4 text-foreground" />
      ) : (
        <Sun className="h-4 w-4 text-foreground" />
      )}
    </button>
  )
}
