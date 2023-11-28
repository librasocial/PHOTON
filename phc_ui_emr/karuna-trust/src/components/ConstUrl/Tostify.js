import React from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./Tostify.css";

export const notifySuccess = (message) =>
  toast(<p style={{ fontSize: 15 }}>{message}</p>, {
    position: "top-center",
    autoClose: 2000,
    hideProgressBar: false,
    newestOnTop: false,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    type: "Success",
  });
export const notifyWarning = (message) =>
  toast(<p style={{ fontSize: 15 }}>{message}</p>, {
    position: "top-center",
    autoClose: 2000,
    hideProgressBar: false,
    newestOnTop: false,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    type: "warning",
  });

export const notifyFail = (message) =>
  toast(<p style={{ fontSize: 15 }}>{message}</p>, {
    position: "top-center",
    autoClose: 2000,
    hideProgressBar: false,
    newestOnTop: false,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    type: "Danger",
  });
