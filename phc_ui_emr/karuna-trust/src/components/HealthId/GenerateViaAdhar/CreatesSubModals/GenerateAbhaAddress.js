import React, { useState } from "react";
import { Button, Form, InputGroup } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { loadPreVerified } from "../../../../redux/AdminAction";
import * as ahbaserviceHeader from "../ahbaserviceHeader";
import { ToastContainer } from "react-toastify";

function GenerateAbhaAddress(props) {
  let dispatch = useDispatch();
  const [enterEmail, setEnterEmail] = useState("");
  let transId = sessionStorage.getItem("txnId");

  const formStatus = () => {
    props.setFormStatus("abha-created");
  };

  const [abhaAddress, setAbhaAddress] = useState(false);
  const createAbhaAddress = () => {
    var postData;
    if (abhaAddress == true) {
      postData = {
        txnId: transId,
      };
    } else {
      postData = {
        healthId: enterEmail,
        txnId: transId,
      };
    }
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
      body: JSON.stringify(postData),
    };

    dispatch(loadPreVerified(postResendData, formStatus, setAbhaAddress));
  };

  return (
    <div>
      <ToastContainer />
      <div className="abha-profile-div">
        <p>Create easy to remember ABHA Address</p>
      </div>
      <Form className="adhar-form-div">
        <div className="col-container abha-adress-form">
          <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
            <Form.Label className="require">
              <b>Please Enter ABHA Address </b>
            </Form.Label>
            {abhaAddress == true ? (
              <p style={{ color: "red" }}>
                Abha Address is already exist. Please skip this page
              </p>
            ) : (
              <InputGroup className="">
                <Form.Control
                  type="email"
                  placeholder="Enter ABHA Address"
                  value={enterEmail || ""}
                  name="email"
                  onChange={(e) => setEnterEmail(e.target.value)}
                />
                <InputGroup.Text id="mypo">@ndhm</InputGroup.Text>
              </InputGroup>
            )}
          </Form.Group>
        </div>
        <div className="modal-btn-email">
          <div>
            {/* <h4 onClick={skipMailVerification}>Skip for Now</h4> */}
          </div>
          <div>
            <Button
              variant="primary"
              className="genIdBtn"
              value="mobile"
              onClick={createAbhaAddress}
            >
              {abhaAddress == true
                ? "skipThisPage"
                : "Create & Link ABHA Address"}
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
}

export default GenerateAbhaAddress;
