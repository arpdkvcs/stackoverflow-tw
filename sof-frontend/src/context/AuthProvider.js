import {createContext, useState} from "react";

const AuthContext = createContext({});

//make this state reachable in everything this wraps
export function AuthProvider({children}) {
  const [auth, setAuth] = useState({});
  return (
    <AuthContext.Provider value={{auth, setAuth}}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthContext;
