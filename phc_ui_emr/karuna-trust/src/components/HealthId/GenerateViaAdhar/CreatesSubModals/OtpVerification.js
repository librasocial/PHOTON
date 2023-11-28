import React, { useState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import OTPInput, { ResendOTP } from "otp-input-react";
import "./OtpInput.css";
import {
  loadMobileVerification,
  loadOTPVerification,
  loadPreVerified,
  loadResendOtp,
} from "../../../../redux/AdminAction";
import * as ahbaserviceHeader from "../ahbaserviceHeader";
import { useDispatch, useSelector } from "react-redux";
import { ToastContainer } from "react-toastify";
import { JSEncrypt } from "jsencrypt";

let countdownInterval;
function OtpVerification(props) {
  let dispatch = useDispatch();
  var encrypt = new JSEncrypt();
  const { OtpData } = useSelector((state) => state.abhaData);
  const { publicKey } = useSelector((state) => state.abhaData);

  const [otp, setOtp] = useState("");
  const [timeoutCountdown, setTimeoutCountdown] = useState(0);
  let transId = sessionStorage.getItem("txnId");

  const minutes = Math.floor(timeoutCountdown / 60);
  const seconds = timeoutCountdown % 60;

  function padTo2Digits(num) {
    return num.toString().padStart(2, "0");
  }
  const result = `${padTo2Digits(minutes)}:${padTo2Digits(seconds)}`;

  useEffect(() => {
    if (props.otpType == "Aadhar") {
      otpTimer();
    }
  }, [props.otpType]);

  function otpTimer() {
    setTimeout(() => {
      let countDown = 120;
      setTimeoutCountdown(countDown);
      countdownInterval = setInterval(() => {
        if (countDown > 0) {
          setTimeoutCountdown(--countDown);
        }
      }, 1000);
    });
  }

  const resendOtp = () => {
    otpTimer();

    var resendPost = {
      txnId: transId,
    };
    let abhaAuthtoken = sessionStorage.getItem("abhaToken");
    const myHeaders2 = new Headers();
    let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
    myHeaders2.append("Authorization", abhaJwttoken);
    myHeaders2.append("Content-Type", "application/json");
    myHeaders2.append("Accept", "application/json");

    let postResendData = {
      headers: myHeaders2,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(resendPost),
    };
    dispatch(loadResendOtp(postResendData));
  };

  const formStatus = () => {
    if (props.otpType == "Aadhar") {
      props.setFormStatus("adhar-verified");
    } else if (props.otpType == "Mobile") {
      props.setFormStatus("mobile-verified");
    }
  };

  const preStatus = () => {
    props.setFormStatus("abha-created");
  };

  const verifyOtp = () => {
    encrypt.setPublicKey(publicKey);
    var encrypted = encrypt.encrypt(otp);

    var verifySendData = {
      otp: encrypted,
      txnId: transId,
    };
    let abhaAuthtoken = sessionStorage.getItem("abhaToken");
    const myHeaders2 = new Headers();
    let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
    myHeaders2.append("Authorization", abhaJwttoken);
    myHeaders2.append("Content-Type", "application/json");
    myHeaders2.append("Accept", "application/json");

    let postResendData = {
      headers: myHeaders2,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(verifySendData),
    };

    if (props.otpType == "Aadhar") {
      dispatch(loadOTPVerification(postResendData, formStatus));
    } else if (props.otpType == "Mobile") {
      dispatch(loadMobileVerification(postResendData, formStatus));
    }
  };

  return (
    <div>
      <ToastContainer />
      <div className="abha-otp-head">
        {props.otpType == "Aadhar" && (
          <h4>
            We just sent an OTP on the Mobile Number {OtpData.mobileNumber}{" "}
            linked with your Aadhaar Number
          </h4>
        )}
        {props.otpType == "Mobile" && (
          <h4>We just sent an OTP on the Mobile Number</h4>
        )}
      </div>
      <div>
        <Form className="adhar-otp-div">
          <div className="col-container adhar-form">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Enter OTP * </Form.Label>
              <OTPInput
                value={otp}
                onChange={setOtp}
                autoFocus
                OTPLength={6}
                otpType="number"
                disabled={false}
                // secure
              />
              {props.otpType == "Aadhar" && (
                <div className="otp-resend">
                  <p>
                    Resend OTP in{" "}
                    <span className="counter-time">{result}s</span>
                  </p>
                  <Button
                    type="button"
                    className="resend-btn btn btn-link"
                    style={{ textDecoration: "underline" }}
                    disabled={timeoutCountdown != 0}
                    onClick={resendOtp}
                  >
                    Resend OTP
                  </Button>
                </div>
              )}
            </Form.Group>
          </div>

          <div className="modal-btn-div">
            <div className="">
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={verifyOtp}
              >
                Next
              </Button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}

export default OtpVerification;
