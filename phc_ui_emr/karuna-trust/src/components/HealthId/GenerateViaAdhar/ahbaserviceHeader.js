let abhaAuthtoken = sessionStorage.getItem("abhaToken");
export const myHeaders2 = new Headers();
let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
myHeaders2.append("Authorization", abhaJwttoken);
myHeaders2.append("Content-Type", "application/json");
myHeaders2.append("Accept", "application/json");

export const getRequestAbha = {
  headers: myHeaders2,
  method: "GET",
  mode: "cors",
};

let abha_X_token = sessionStorage.getItem("abha-X-Token");
export const myHeaders3 = new Headers();
let abhaJwt_X_token = "Bearer " + "" + abha_X_token;
myHeaders3.append("Authorization", abhaJwttoken);
myHeaders3.append("Content-Type", "application/json");
myHeaders3.append("Accept", "application/json");
myHeaders3.append("X-Token", abhaJwt_X_token);

export const getAbhaProfile = {
  headers: myHeaders3,
  method: "GET",
  mode: "cors",
};
