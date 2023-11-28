import React from "react";
import { Button, Form } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { ToastContainer } from "react-toastify";
import {
  loadPngData,
  loadProfileCard,
  loadProfileData,
  loadQRData,
  loadSvgData,
} from "../../../../redux/AdminAction";

function SuccessPage(props) {
  let dispatch = useDispatch();
  const { adharDetails } = useSelector((state) => state.abhaData);

  const formStatus = () => {
    // props.setFormStatus("abha-profile")
    props.setAadharStatus(true);
  };

  const generateAbhaCard = () => {
    let abhaAuthtoken = sessionStorage.getItem("abhaToken");
    let abha_X_token = sessionStorage.getItem("abha-X-Token");
    let myHeaders3 = new Headers();
    let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
    let abhaJwt_X_token = "Bearer " + "" + abha_X_token;
    myHeaders3.append("Authorization", abhaJwttoken);
    myHeaders3.append("Content-Type", "application/json");
    myHeaders3.append("Accept", "application/json");
    myHeaders3.append("X-Token", abhaJwt_X_token);

    const getAbhaHeader = {
      headers: myHeaders3,
      method: "GET",
      mode: "cors",
    };
    // dispatch(loadQRData(getAbhaHeader))
    // dispatch(loadProfileCard(getAbhaHeader))
    // dispatch(loadSvgData(getAbhaHeader))
    dispatch(loadPngData(getAbhaHeader, formStatus));
    dispatch(loadProfileCard(getAbhaHeader));
  };

  return (
    <div>
      <ToastContainer />
      <div className="abha-profile-div">
        <p style={{ paddingBottom: "0.5rem" }}>
          Aadhaar Authentication Successful &nbsp;&nbsp;{" "}
          <img src="../../img/healthId/rightCheck.png" alt="check" />
        </p>

        <h4>ABHA Number has now been created!</h4>

        <h6>
          Your ABHA Number is <b>{adharDetails.healthIdNumber}</b>
        </h6>
      </div>
      <div>
        <Form className="adhar-otp-div">
          <div className="modal-btn-div">
            <div className="">
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={generateAbhaCard}
              >
                Go to your ABHA Card
              </Button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}

export default SuccessPage;
