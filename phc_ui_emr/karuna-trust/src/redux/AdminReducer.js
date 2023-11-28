import * as types from "./AdminActionType";

const initialState = {
  accessToken: {},
  OtpData: {},
  adharDetails: {},
  profileData: {},
  qrData: {},
  cardData: {},
  svgData: {},
  pngData: "",
  publicKey: "",
  regresp: "",
  loading: true,
};

const abhaReducers = (state = initialState, action) => {
  switch (action.type) {
    case types.Abha_Token:
      return {
        ...state,
        accessToken: action.payload,
        loading: false,
      };
    case types.Adhar_OTP:
      return {
        ...state,
        OtpData: action.payload,
        loading: false,
      };
    case types.Adhar_OTP_Again:
    case types.Adhar_Verification:
    case types.Adhar_Pre_Verified:
      return {
        ...state,
        adharDetails: action.payload,
        loading: false,
      };
    case types.Get_Profile_Data:
      return {
        ...state,
        profileData: action.payload,
        loading: false,
      };
    case types.Get_QR_Data:
      return {
        ...state,
        qrData: action.payload,
        loading: false,
      };
    case types.Get_Card_Data:
      return {
        ...state,
        cardData: action.payload,
        loading: false,
      };
    case types.Get_SVG_Data:
      return {
        ...state,
        svgData: action.payload,
        loading: false,
      };
    case types.Get_Png_Data:
      return {
        ...state,
        pngData: action.payload,
        loading: false,
      };
    case types.Encript_Key:
      return {
        ...state,
        publicKey: action.payload,
        loading: false,
      };

    // reduces for register ABDM
    case types.HRP_Validate:
    case types.register_Abdm:
      return {
        ...state,
        regresp: action.payload,
        loading: false,
      };
    // reduces for register ABDM
    default:
      return state;
  }
};
export default abhaReducers;
