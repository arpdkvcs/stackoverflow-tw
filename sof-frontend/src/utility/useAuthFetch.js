import { useCallback } from "react";
import useAuth from "./auth/useAuth";
import useLogout from "./auth/useLogout";

function UseAuthFetch() {
  const { auth } = useAuth();
  const logout = useLogout();
  const apiUrl = process.env.REACT_APP_API_PRIV_URL;

  const fetchWitAuth = useCallback(
    async (path, method, body) => {
      try {
        const url = `${apiUrl}/${path}`;

        const reqConfig = {
          method: `${method ?? "GET"}`,
          headers: {
            "Content-Type": "application/json"
          },
          credentials: "include"
        };

        if (body) {
          reqConfig.body = JSON.stringify(body);
        }

        const httpResponse = await fetch(url, reqConfig);

        //this breaks SRP and needs refactoring.. (needs to be useCallback bc of this)
        //(if forbidden/unauthorized: logout = fetch logout & clear auth state)
        if (httpResponse?.status === 401 || httpResponse?.status === 403) {
          await logout();
          return null;
        }

        const responseObject = await httpResponse.json();

        return responseObject;
      } catch (err) {
        return null;
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [auth.accessToken]
  );

  return fetchWitAuth;
}

export default UseAuthFetch;
