import React, { createContext, useContext, useState, useCallback } from "react"
import api from "@/services/api"

interface User {
  id: number
  name: string
  email: string
  role: string
}

interface AuthContextType {
  user: User | null
  token: string | null
  login: (email: string, password: string) => Promise<void>
  signup: (name: string, email: string, password: string) => Promise<void>
  logout: () => void
  isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error("useAuth must be used within AuthProvider")
  return context
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(() => {
    const stored = localStorage.getItem("user")
    return stored ? JSON.parse(stored) : null
  })
  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem("token")
  })

  const login = useCallback(async (email: string, password: string) => {
    const response = await api.post("/auth/login", { email, password })
    const { token: jwt, email: userEmail, name, role, userId } = response.data.data

    const authUser: User = { id: userId, name, email: userEmail, role }
    setUser(authUser)
    setToken(jwt)
    localStorage.setItem("user", JSON.stringify(authUser))
    localStorage.setItem("token", jwt)
  }, [])

  const signup = useCallback(async (name: string, email: string, password: string) => {
    const response = await api.post("/auth/signup", { name, email, password })
    const { token: jwt, email: userEmail, name: userName, role, userId } = response.data.data

    const authUser: User = { id: userId, name: userName, email: userEmail, role }
    setUser(authUser)
    setToken(jwt)
    localStorage.setItem("user", JSON.stringify(authUser))
    localStorage.setItem("token", jwt)
  }, [])

  const logout = useCallback(() => {
    setUser(null)
    setToken(null)
    localStorage.removeItem("user")
    localStorage.removeItem("token")
  }, [])

  return (
    <AuthContext.Provider value={{ user, token, login, signup, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  )
}
