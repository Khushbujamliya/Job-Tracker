import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('user')
    return stored ? JSON.parse(stored) : null
  })

  function login(authResponse) {
    // authResponse looks like: { token, name, email }
    localStorage.setItem('token', authResponse.token)
    localStorage.setItem('user', JSON.stringify({ name: authResponse.name, email: authResponse.email }))
    setUser({ name: authResponse.name, email: authResponse.email })
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
