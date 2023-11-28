import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { loadVerifyMobile } from "../../../../redux/AdminAction";
import * as ahbaserviceHeader from "../ahbaserviceHeader";

function MobileLinked(props) {
  let dispatch = useDispatch();
  const [inputValue, handleInput] = useState("");
  let transId = sessionStorage.getItem("txnId");

  // const [abhaStatus, setAbhaStatus] = useState('')

  // useEffect(() => {
  //     if (abhaStatus) {
  //         if (abhaStatus == "true") {
  //             props.setFormStatus("abha-created")
  //         } else if (abhaStatus == "false") {
  //             props.setFormStatus("mobile-verified")
  //         }
  //     }
  // }, [abhaStatus])
  const formStatus = () => {
    props.setFormStatus("mobile-verified");
  };
  const verifyStatus = () => {
    props.setFormStatus("mobile-link");
  };

  const mobileLinked = () => {
    var postData = {
      mobile: parseInt(inputValue),
      txnId: transId,
    };
    let abhaAuthtoken = sessionStorage.getItem("abhaToken");
    const myHeaders2 = new Headers();
    let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
    myHeaders2.append("Authorization", abhaJwttoken);
    myHeaders2.append("Content-Type", "application/json");
    myHeaders2.append("Accept", "application/json");

    var postVerifyData = {
      headers: myHeaders2,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(postData),
    };

    dispatch(loadVerifyMobile(postVerifyData, formStatus, verifyStatus));
  };
  return (
    <div>
      <div className="abha-profile-div">
        <p>It is recommended that you use the Number linked with Aadhaar. </p>
      </div>
      <Form className="adhar-form-div">
        <div className="col-container adhar-form">
          <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
            <Form.Label className="require">
              <b>Mobile Number *</b>
            </Form.Label>
            <Form.Control
              type="tel"
              maxLength={10}
              placeholder="Enter Mobile Number"
              value={inputValue || ""}
              name="mobile"
              onKeyPress={(event) => {
                if (
                  /^[2-9]{1}[0-9]{3}\s{1}[0-9]{4}\s{1}[0-9]{4}$/.test(event.key)
                ) {
                  event.preventDefault();
                }
              }}
              onChange={(e) => handleInput(e.target.value)}
            />
          </Form.Group>
        </div>
        <div className="abha-profile-div">
          <h6>
            <b>This number will be used to authenticate your ABHA number</b>
          </h6>
        </div>
        <div className="modal-btn-div">
          <div className="">
            <Button
              variant="primary"
              className="genIdBtn"
              value="mobile"
              onClick={mobileLinked}
            >
              Next
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
}

export default MobileLinked;
