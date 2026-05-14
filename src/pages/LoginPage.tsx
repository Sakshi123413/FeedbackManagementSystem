import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { useAuth } from "@/context/AuthContext"
import { useToast } from "@/components/ui/toast"
import { Mail, Lock, MessageSquareHeart } from "lucide-react"

export function LoginPage() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [errors, setErrors] = useState<{ email?: string; password?: string }>({})
  const [isLoading, setIsLoading] = useState(false)
  const { login } = useAuth()
  const { showToast } = useToast()
  const navigate = useNavigate()

  const validate = () => {
    const newErrors: { email?: string; password?: string } = {}
    if (!email) newErrors.email = "Email is required"
    else if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "Enter a valid email"
    if (!password) newErrors.password = "Password is required"
    else if (password.length < 6) newErrors.password = "Password must be at least 6 characters"
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return

    setIsLoading(true)
    try {
      await login(email, password)
      showToast("Welcome back! Login successful.", "success")
      navigate("/dashboard")
    } catch {
      showToast("Invalid credentials. Please try again.", "error")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4 gradient-hero">
      <div className="w-full max-w-md animate-fade-in">
        <div className="flex items-center justify-center gap-2 mb-8">
          <div className="h-10 w-10 rounded-xl gradient-primary flex items-center justify-center">
            <MessageSquareHeart className="h-5 w-5 text-primary-foreground" />
          </div>
          <span className="text-xl font-bold text-foreground">FeedbackHub</span>
        </div>

        <Card>
          <CardHeader className="text-center">
            <CardTitle>Welcome back</CardTitle>
            <CardDescription>Sign in to your account to continue</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Email"
                type="email"
                placeholder="you@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                error={errors.email}
                icon={<Mail className="h-4 w-4" />}
              />
              <Input
                label="Password"
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={errors.password}
                icon={<Lock className="h-4 w-4" />}
              />
              <Button type="submit" className="w-full" size="lg" isLoading={isLoading}>
                Sign In
              </Button>
            </form>
            <p className="text-center text-sm text-muted-foreground mt-6">
              Don't have an account?{" "}
              <Link to="/signup" className="text-primary font-medium hover:underline">
                Sign up
              </Link>
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
