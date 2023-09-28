export async function fetchPublic(path, method = "GET", bodyObject = null){
  try {
    const apiUrl = process.env.REACT_APP_API_URL;
    
    const requestConfig = {
      method: `${method}`,
      headers: {
        "Content-Type": "application/json"
      }
    };

    if (bodyObject) {
      requestConfig.body = JSON.stringify(bodyObject);
    }

    const httpResponse = await fetch(`${apiUrl}/${path}`, requestConfig);
    const responseObject = await httpResponse?.json();
    return responseObject;
  } catch (e) {
    console.error(e);
    return null;
  }
  //
}
