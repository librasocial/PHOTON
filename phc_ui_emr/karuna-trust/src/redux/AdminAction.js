import * as types from "./AdminActionType";
import * as constant from "../components/ConstUrl/constant";
import axios from "axios";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";
import * as ahbaserviceHeader from "../components/HealthId/GenerateViaAdhar/ahbaserviceHeader";
import * as Tostify from "../components/ConstUrl/Tostify";

const getAccessToken = (accessToken) => ({
  type: types.Abha_Token,
  payload: accessToken,
});
const generateAdarOtp = (OtpData) => ({
  type: types.Adhar_OTP,
  payload: OtpData,
});
const getOTPAgain = () => ({
  type: types.Adhar_OTP_Again,
});
const getAdarDetails = (adharDetails) => ({
  type: types.Adhar_Pre_Verified,
  payload: adharDetails,
});
const adharverifiedData = () => ({
  type: types.Adhar_Verification,
  // payload: adharDetails,
});
const getProfileData = (profileData) => ({
  type: types.Get_Profile_Data,
  payload: profileData,
});
const getQrData = (qrData) => ({
  type: types.Get_QR_Data,
  payload: qrData,
});
const getCardData = (cardData) => ({
  type: types.Get_Card_Data,
  payload: cardData,
});
const getSvgData = (svgData) => ({
  type: types.Get_SVG_Data,
  payload: svgData,
});
const getPngData = (pngData) => ({
  type: types.Get_Png_Data,
  payload: pngData,
});
const getPublicKey = (publicKey) => ({
  type: types.Encript_Key,
  payload: publicKey,
});

export const loadAccessToken = (setAbhaModal) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/healthIds/auth`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        sessionStorage.setItem("abhaToken", resp["accessToken"]);
        dispatch(getAccessToken(resp));
        setAbhaModal(true);
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

export const loadAdharOtp = (postRequest, formStatus) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v2/registration/aadhaar/generateOtp`,
      postRequest
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 401) {
          Tostify.notifyWarning("Missing access token, please try again...!");
          // window.location.reload()
        } else if (resp.code == "HIS-400") {
          Tostify.notifyWarning(
            "Seems Aadhar number not linked with mobile...!"
          );
        } else {
          let transId = resp["txnId"];
          sessionStorage.setItem("txnId", transId);
          dispatch(generateAdarOtp(resp));
          formStatus();
        }
      })
      .catch((error) => {
        Tostify.notifyWarning("Something went wrong, please try again...!");
        window.location.reload();
      });
  };
};

export const loadResendOtp = (postResendData) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v1/registration/aadhaar/resendAadhaarOtp`,
      postResendData
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOTPAgain(resp));
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

export const loadPreVerified = (postVerifyData, formStatus, setAbhaAddress) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v1/registration/aadhaar/createHealthIdWithPreVerified`,
      postVerifyData
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.code) {
          Tostify.notifyFail(resp.details[0].message);
        } else {
          dispatch(getAdarDetails(resp));
          sessionStorage.setItem("abha-X-Token", resp.token);
          if (resp.new == false) {
            Tostify.notifySuccess(resp.alreadyExists);
            formStatus();
          } else {
            Tostify.notifySuccess("ABHA Number created successfully...!");
            formStatus();
          }
        }
      })
      .catch((error) => {
        Tostify.notifyFail("Abha Address already created...!");
        setAbhaAddress(true);
      });
  };
};

export const loadOTPVerification = (postVerifyData, formStatus) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v2/registration/aadhaar/verifyOTP`,
      postVerifyData
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.code) {
          Tostify.notifyFail("You have entered wrong OTP...!");
        } else {
          // dispatch(adharverifiedData())
          formStatus();
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

export const loadMobileVerification = (postVerifyData, formStatus) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v1/registration/aadhaar/verifyMobileOTP`,
      postVerifyData
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.code) {
          Tostify.notifyFail("You have entered wrong OTP...!");
        } else {
          // dispatch(adharverifiedData())
          formStatus();
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

export const loadVerifyMobile = (postVerifyData, formStatus, verifyStatus) => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v2/registration/aadhaar/checkAndGenerateMobileOTP`,
      postVerifyData
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.code) {
          Tostify.notifyFail("You have entered wrong OTP...!");
        } else {
          if (resp.mobileLinked == true) {
            formStatus();
          } else {
            fetch(
              `${constant.abhaUrl}/api/v1/registration/aadhaar/generateMobileOTP`,
              postVerifyData
            )
              .then((resp) => resp.json())
              .then((resp) => {
                verifyStatus();
              });
          }
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

// api call for getting profile details
export const loadProfileData = (getAbhaHeader, formStatus) => {
  return function (dispatch) {
    fetch(`${constant.abhaUrl}/api/v1/account/profile`, getAbhaHeader)
      .then((proResp) => proResp.json())
      .then((proResp) => {
        if (proResp.code) {
          Tostify.notifyFail("Something went wrong...!");
        } else {
          dispatch(getProfileData(proResp));
          formStatus();
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };
};

export const loadPngData = (getAbhaHeader, formStatus) => {
  return function (dispatch) {
    fetch(`${constant.abhaUrl}/api/v1/account/getPngCard`, getAbhaHeader)
      .then((pngResp) => pngResp.blob())
      .then(async (blob) => {
        let objectURL = URL.createObjectURL(blob);
        dispatch(getPngData(objectURL));
        formStatus();
      })
      .catch((error) => {
        console.log(error);
      });
    // for png card
  };
};

export const loadSvgData = (getAbhaHeader) => {
  return function (dispatch) {
    // for svg card
    fetch(`${constant.abhaUrl}/api/v1/account/getSvgCard`, getAbhaHeader)
      .then((svgResp) => svgResp.blob())
      .then(async (blob) => {
        let objectURL = URL.createObjectURL(blob);
        dispatch(getSvgData(objectURL));
      })
      .catch((error) => {
        console.log(error);
      });
    // for svg card
  };
};

export const loadProfileCard = (getAbhaHeader) => {
  return function (dispatch) {
    // for profile card
    fetch(`${constant.abhaUrl}/api/v1/account/getCard`, getAbhaHeader)
      .then((cardResp) => cardResp.blob())
      .then(async (blob) => {
        let objectURL = URL.createObjectURL(blob);
        dispatch(getCardData(objectURL));
      })
      .catch((error) => {
        console.log(error);
      });
    // for profile card
  };
};

export const loadQRData = (getAbhaHeader) => {
  return function (dispatch) {
    // for qrcode
    fetch(`${constant.abhaUrl}/api/v1/account/qrCode`, getAbhaHeader)
      .then((qrResp) => qrResp.blob())
      .then(async (blob) => {
        let objectURL = URL.createObjectURL(blob);
        dispatch(getQrData(objectURL));
      })
      .catch((error) => {
        console.log(error);
      });
    // for qrcode
  };
};

export const loadGetPublicKey = () => {
  return function (dispatch) {
    fetch(
      `${constant.abhaUrl}/api/v2/auth/cert`,
      ahbaserviceHeader.getRequestAbha
    )
      .then((resp) => resp.text())
      .then((resp) => {
        dispatch(getPublicKey(resp));
      })
      .catch((error) => {
        console.log(error);
      });
  };
};
// api call for getting profile details

// Register for ABDM
const hrpRespose = () => ({
  type: types.HRP_Validate,
});
const getRegisterData = (regresp) => ({
  type: types.register_Abdm,
  payload: regresp,
});

export const loadRegisterAbdm = (postRequest, closeModal) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/abdm-svc/devservice/v1/bridges/addUpdateServices`,
      postRequest
    )
      .then((regResp) => regResp.json())
      .then((regResp) => {
        dispatch(getRegisterData(regResp));
        closeModal();
      })
      .catch((err) => {
        console.log(err);
      });
  };
};
// Register for ABDM
