import { useAuth } from "@/context/AuthContext"
import { ThemeToggle } from "@/components/ui/theme-toggle"
import { Button } from "@/components/ui/button"
import { MessageSquareHeart, LogOut } from "lucide-react"
import { useNavigate } from "react-router-dom"
import { useToast } from "@/components/ui/toast"

export function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const { showToast } = useToast()

  const handleLogout = () => {
    logout()
    showToast("Logged out successfully.", "success")
    navigate("/login")
  }

  return (
    <header className="sticky top-0 z-40 border-b bg-card/80 glass">
      <div className="container flex h-16 items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="h-8 w-8 rounded-lg gradient-primary flex items-center justify-center">
            <MessageSquareHeart className="h-4 w-4 text-primary-foreground" />
          </div>
          <span className="text-lg font-bold text-foreground">FeedbackHub</span>
        </div>

        <div className="flex items-center gap-3">
          {user && (
            <span className="hidden sm:block text-sm text-muted-foreground">
              {user.name}
            </span>
          )}
          <ThemeToggle />
          <Button variant="ghost" size="icon" onClick={handleLogout} aria-label="Logout">
            <LogOut className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </header>
  )
}
