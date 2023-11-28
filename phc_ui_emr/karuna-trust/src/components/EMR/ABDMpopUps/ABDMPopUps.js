import React, { useEffect, useState } from "react";
import { Button, Col, Modal, Row } from "react-bootstrap";
import OTPInput, { ResendOTP } from "otp-input-react";
import "../../HealthId/GenerateViaAdhar/CreatesSubModals/OtpInput.css";
import { useDispatch } from "react-redux";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import { loadAbdmData } from "../../../redux/WSAction";

function ABDMPopUps(props) {
  const [otp, setOtp] = useState("");
  let dispatch = useDispatch();

  const [name, setName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [gender, setGender] = useState("");

  const handleNameChange = (event) => {
    setName(event.target.value);
  };

  const handleDateOfBirthChange = (event) => {
    setDateOfBirth(event.target.value);
  };

  const handleGenderChange = (event) => {
    setGender(event.target.value);
  };

  const closeModal = () => {
    props.AbdmModalClose();
  };

  // const [isOTPVerified, setIsOTPVerified] = useState(false);
  // const [otpSent, setOtpSent] = useState(false);

  // const handleOTPVerification = () => {
  //   const isVerified = verifyOTP(otp);

  //   if (isVerified) {
  //     setIsOTPVerified(true);
  //     setOtpSent(true);
  //   } else {
  //   }
  // };
  const handleOTPVerification = () => {
    var date = new Date();

    if (otp && props.transactionId) {
      var raw = {
        requestId: "11466ab9-ea13-4f5b-905b-c43825c3968c",
        timestamp: date,
        transactionId: `${props.transactionId}`,
        credential: {
          authCode: `${otp}`,
        },
      };
    }
    if (
      props.modaltype === "DEMOGRAPHICS" &&
      props.transactionId &&
      name &&
      gender &&
      dateOfBirth
    ) {
      var raw = {
        requestId: "11466ab9-ea13-4f5b-905b-c43825c3968c",
        timestamp: date,
        transactionId: `${props.transactionId}`,
        credential: {
          demographic: {
            name: name,
            gender: gender,
            dateOfBirth: dateOfBirth,
          },
          // name: DemoGraphicName,
          // gender: DemoGraphicGender,
          // dateOfBirth: DemoGraphicDOB,
        },
      };
    }

    var requestOptions = {
      method: "POST",
      headers: serviceHeaders.myHeaders1,
      body: JSON.stringify(raw),
      redirect: "follow",
    };

    dispatch(
      loadAbdmData(requestOptions, props.connectToWebSocket, closeModal)
    );

    // await fetch(`${API_BASE_URL}/users/auth/confirm`, requestOptions)
    //   .then((response) => response.text())
    //   .then((result) => {
    //     console.log(result);
    //     const data = JSON.parse(result);

    //     connectToWebSocket(data.id);
    //   })
    //   .catch((error) => console.log("error", error));
  };

  return (
    <div>
      {props.modaltype === "AADHAAR_OTP" && props.transactionId && (
        <Modal
          show={props.AbdmModal}
          onHide={props.AbdmModalClose}
          className="check-In-modal-div"
        >
          <Row>
            <Col md={6} xs={9}>
              <h3 className="patient-details-header">
                <b>Aadhar OTP</b>
              </h3>
              <p>Enter OTP sent to your mobile number</p>
              <br />
              <OTPInput
                value={otp}
                onChange={setOtp}
                autoFocus
                OTPLength={6}
                otpType="number"
              />
              <br />
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={handleOTPVerification}
              >
                Submit OTP
              </Button>
            </Col>

            <Col md={6} xs={3} align="right">
              <button
                onClick={closeModal}
                className="bi bi-x close-popup"
              ></button>
            </Col>
          </Row>
        </Modal>
      )}
      {props.modaltype === "MOBILE_OTP" && props.transactionId && (
        <Modal
          show={props.AbdmModal}
          onHide={props.AbdmModalClose}
          className="check-In-modal-div"
        >
          <Row>
            <Col md={6} xs={9}>
              <h3 className="patient-details-header">
                <b>Mobile OTP</b>
              </h3>
              <p>Enter OTP sent to your mobile number</p>

              <br />
              <OTPInput
                value={otp}
                onChange={setOtp}
                autoFocus
                OTPLength={6}
                otpType="number"
              />
              <br />
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={handleOTPVerification}
              >
                Submit OTP
              </Button>
            </Col>

            <Col md={6} xs={3} align="right">
              <button
                onClick={closeModal}
                className="bi bi-x close-popup"
              ></button>
            </Col>
          </Row>
        </Modal>
      )}
      {props.modaltype === "DEMOGRAPHICS" && props.transactionId && (
        <Modal
          show={props.AbdmModal}
          onHide={props.AbdmModalClose}
          className="check-In-modal-div"
        >
          <Row>
            <Col md={6} xs={9}>
              <h3 className="patient-details-header">
                <b>Demographic</b>
              </h3>
              <div>
                <label>Name:</label>
                <input type="text" value={name} onChange={handleNameChange} />
              </div>
              <div>
                <label>Date of Birth:</label>
                <input
                  type="date"
                  value={dateOfBirth}
                  onChange={handleDateOfBirthChange}
                />
              </div>
              <div>
                <label>Gender:</label>
                <select value={gender} onChange={handleGenderChange}>
                  <option value="">Select Gender</option>
                  <option value="M">Male</option>
                  <option value="F">Female</option>
                  <option value="other">Other</option>
                </select>
              </div>
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={handleOTPVerification}
              >
                Submit Details
              </Button>
            </Col>
            <Col md={6} xs={3} align="right">
              <button
                onClick={closeModal}
                className="bi bi-x close-popup"
              ></button>
            </Col>
          </Row>
          <hr />
        </Modal>
      )}
    </div>
  );
}

export default ABDMPopUps;
