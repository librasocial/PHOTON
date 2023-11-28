import * as types from "./WSActionType";
import * as constant from "../components/ConstUrl/constant";
import axios from "axios";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";
import * as ahbaserviceHeader from "../components/HealthId/GenerateViaAdhar/ahbaserviceHeader";
import * as Tostify from "../components/ConstUrl/Tostify";
import SockJS from "sockjs-client";
import { over } from "stompjs";

const getAbdmFetchModes = (modes) => ({
  type: types.Get_Fetch_MOdes,
  payload: modes,
});

export const loadAbdmFetchModes = (requestOptions, connectToWebSocket) => {
  return function (dispatch) {
    fetch(
      `https://07343ene9c.execute-api.ap-south-1.amazonaws.com/abdm-svc/v0.5/users/auth/fetch-modes`,
      requestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        console.log(resp.id, "testing");
        // alert("gfhghhghg")
        connectToWebSocket(resp.id, resp.type);
      })
      .catch((err) => {});
  };
};

export const loadAbdmModeVerification = (
  requestOptions,
  connectToWebSocket
) => {
  return function (dispatch) {
    fetch(
      `https://07343ene9c.execute-api.ap-south-1.amazonaws.com/abdm-svc/v0.5/users/auth/init`,
      requestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        // console.log(resp, "testing")
        // alert("gfhghhghg")
        connectToWebSocket(resp.id, resp.type);
      })
      .catch((err) => {});
  };
};

export const loadAbdmData = (
  requestOptions,
  connectToWebSocket,
  closeModal
) => {
  return function (dispatch) {
    fetch(
      `https://07343ene9c.execute-api.ap-south-1.amazonaws.com/abdm-svc/v0.5/users/auth/confirm`,
      requestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        // console.log(resp, "testing")
        // alert("gfhghhghg")
        connectToWebSocket(resp.id, resp.type);
        closeModal();
      })
      .catch((err) => {});
  };
};
