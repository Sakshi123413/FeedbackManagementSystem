import React, { createContext, useContext, useState, useCallback } from "react"

interface User {
  id: string
  name: string
  email: string
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

  const login = useCallback(async (email: string, _password: string) => {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1200))

    const mockUser: User = {
      id: "1",
      name: email.split("@")[0],
      email,
    }
    const mockToken = "jwt_" + Math.random().toString(36).substring(7)

    setUser(mockUser)
    setToken(mockToken)
    localStorage.setItem("user", JSON.stringify(mockUser))
    localStorage.setItem("token", mockToken)
  }, [])

  const signup = useCallback(async (name: string, email: string, _password: string) => {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1200))

    const mockUser: User = {
      id: Math.random().toString(36).substring(7),
      name,
      email,
    }
    const mockToken = "jwt_" + Math.random().toString(36).substring(7)

    setUser(mockUser)
    setToken(mockToken)
    localStorage.setItem("user", JSON.stringify(mockUser))
    localStorage.setItem("token", mockToken)
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
