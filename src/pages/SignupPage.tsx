import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { useAuth } from "@/context/AuthContext"
import { useToast } from "@/components/ui/toast"
import { Mail, Lock, User, MessageSquareHeart } from "lucide-react"

export function SignupPage() {
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [confirmPassword, setConfirmPassword] = useState("")
  const [errors, setErrors] = useState<Record<string, string>>({})
  const [isLoading, setIsLoading] = useState(false)
  const { signup } = useAuth()
  const { showToast } = useToast()
  const navigate = useNavigate()

  const validate = () => {
    const newErrors: Record<string, string> = {}
    if (!name.trim()) newErrors.name = "Name is required"
    if (!email) newErrors.email = "Email is required"
    else if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "Enter a valid email"
    if (!password) newErrors.password = "Password is required"
    else if (password.length < 6) newErrors.password = "Password must be at least 6 characters"
    if (!confirmPassword) newErrors.confirmPassword = "Please confirm your password"
    else if (password !== confirmPassword) newErrors.confirmPassword = "Passwords do not match"
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return

    setIsLoading(true)
    try {
      await signup(name, email, password)
      showToast("Account created successfully! Welcome aboard.", "success")
      navigate("/dashboard")
    } catch {
      showToast("Signup failed. Please try again.", "error")
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
            <CardTitle>Create an account</CardTitle>
            <CardDescription>Get started with FeedbackHub today</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Full Name"
                type="text"
                placeholder="John Doe"
                value={name}
                onChange={(e) => setName(e.target.value)}
                error={errors.name}
                icon={<User className="h-4 w-4" />}
              />
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
                placeholder="Create a password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={errors.password}
                icon={<Lock className="h-4 w-4" />}
              />
              <Input
                label="Confirm Password"
                type="password"
                placeholder="Confirm your password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                error={errors.confirmPassword}
                icon={<Lock className="h-4 w-4" />}
              />
              <Button type="submit" className="w-full" size="lg" isLoading={isLoading}>
                Create Account
              </Button>
            </form>
            <p className="text-center text-sm text-muted-foreground mt-6">
              Already have an account?{" "}
              <Link to="/login" className="text-primary font-medium hover:underline">
                Sign in
              </Link>
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
