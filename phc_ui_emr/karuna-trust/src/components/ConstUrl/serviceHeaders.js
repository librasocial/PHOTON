let authtoken = sessionStorage.getItem("token");
var myHeaders = new Headers();
let jwttoken = "Bearer " + "" + authtoken;
myHeaders.append("Authorization", jwttoken);
myHeaders.append("Content-Type", "application/json");
myHeaders.append("Accept", "application/json");
export const getRequestOptions = {
  headers: myHeaders,
  method: "GET",
  mode: "cors",
};

let authtoken1 = sessionStorage.getItem("token");
export const myHeaders1 = new Headers();
let jwttoken1 = "Bearer " + "" + authtoken1;
myHeaders1.append("Authorization", jwttoken1);
myHeaders1.append("Content-Type", "application/json");
let IdToken1 = sessionStorage.getItem("x-user-id");
myHeaders1.append("x-user-id", IdToken1);
myHeaders1.append("IdToken", IdToken1);
