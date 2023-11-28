import React, { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { ToastContainer } from "react-toastify";
import { loadAdharOtp } from "../../../../redux/AdminAction";
import * as ahbaserviceHeader from "../ahbaserviceHeader";
import { JSEncrypt } from "jsencrypt";

function EnterAdharForm(props) {
  let dispatch = useDispatch();
  var encrypt = new JSEncrypt();

  const [checked, setChecked] = useState([]);
  const { publicKey } = useSelector((state) => state.abhaData);

  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [checkBoxValue, setCheckBoxValue] = useState(false);
  const checkTerms = (e) => {
    setCheckBoxValue(!checkBoxValue);
  };

  // function isValid_Aadhaar_Number(aadhaar_number) {

  //     // Regex to check valid
  //     // aadhaar_number
  //     let regex = new RegExp(/^[2-9]{1}[0-9]{3}\s[0-9]{4}\s[0-9]{4}$/);

  //     // if aadhaar_number
  //     // is empty return false
  //     if (aadhaar_number == null) {
  //         return false;
  //     }

  //     // Return true if the aadhaar_number
  //     // matched the ReGex
  //     if (regex.test(aadhaar_number) == true) {
  //         return true;
  //     }
  //     else {
  //         return false;
  //     }
  // }
  const [inputView, setInputView] = useState(false);
  const changeInputtype = () => {
    setInputView(!inputView);
  };

  const [adharNumber, setAdharNumber] = useState("");
  const handleInput = (e) => {
    setAdharNumber(e.target.value);
  };
  const [adharError, setAdharError] = useState("");
  const [checkBoxText, setCheckBoxText] = useState("");

  const formStatus = () => {
    props.setFormStatus("adhar-otp");
  };

  const getOtpByAdhar = () => {
    console.log(getOtpByAdhar, "getOtpByAdhar");
    if (!adharNumber || !checkBoxValue) {
      if (!adharNumber) {
        setAdharError("Aadhaar Number is required");
      }
      if (!checkBoxValue) {
        setCheckBoxText("Please select terms and conditions");
      }
    } else {
      encrypt.setPublicKey(publicKey);
      var encrypted = encrypt.encrypt(adharNumber);

      let abhaAuthtoken = sessionStorage.getItem("abhaToken");
      const myHeaders2 = new Headers();
      let abhaJwttoken = "Bearer " + "" + abhaAuthtoken;
      myHeaders2.append("Authorization", abhaJwttoken);
      myHeaders2.append("Content-Type", "application/json");
      myHeaders2.append("Accept", "application/json");

      let otpPost = {
        aadhaar: encrypted,
      };

      let postRequest = {
        headers: myHeaders2,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(otpPost),
      };
      dispatch(loadAdharOtp(postRequest, formStatus));
    }
  };

  const [inputViewType, setInputViewType] = useState();
  const [showIcon, setShowIcon] = useState("");
  useEffect(() => {
    if (adharNumber && adharNumber.length < 12) {
      setAdharError("Aadhaar Number is not valid");
    } else {
      setAdharError("");
    }
    if (checkBoxValue) {
      setCheckBoxText("");
    }

    if (inputView == true) {
      setShowIcon("../img/AdminIcons/eye-close.png");
      setInputViewType("tel");
    } else if (inputView == false) {
      setShowIcon("../img/AdminIcons/eye-open.png");
      setInputViewType("password");
    }
  }, [adharNumber, checkBoxValue, inputView, showIcon]);

  const gotoBackPage = () => {
    props.setSelectType("");
    props.setSelectStatus(false);
  };

  return (
    <div>
      <ToastContainer />
      <Form className="adhar-form-div">
        <div className="col-container adhar-form">
          <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
            <Form.Label className="require">Aadhaar Number * </Form.Label>
            <Form.Control
              type={inputViewType}
              maxLength={12}
              placeholder="Enter Aadhaar Number"
              value={adharNumber || ""}
              name="adharNumber"
              onKeyPress={(event) => {
                if (
                  /^[2-9]{1}[0-9]{3}\s{1}[0-9]{4}\s{1}[0-9]{4}$/.test(event.key)
                ) {
                  event.preventDefault();
                }
              }}
              onChange={handleInput}
            />
            <img
              src={showIcon}
              className="show-password-img"
              alt="showpassword"
              onClick={changeInputtype}
            />
            <span
              style={{ color: "red", fontSize: "13px" }}
              className="error-text"
            >
              {adharError}
            </span>
          </Form.Group>
        </div>
        <div className="terms-cond-div">
          <p>
            I, hereby declare that I am voluntarily sharing my Aadhaar Number
            and demographic information issued by UIDAI, with National Health
            Authority (NHA) for the sole purpose of creation of ABHA number . I
            understand that my ABHA number can be used and shared for purposes
            as may be notified by ABDM from time to time including provision of
            healthcare services. Further, I am aware that my personal
            identifiable information (Name, Address, Age, Date of Birth, Gender
            and Photograph) may be made available to the entities working in the
            National Digital Health Ecosystem (NDHE) which inter alia includes
            stakeholders and entities such as healthcare professionals (e.g.
            doctors), facilities (e.g. hospitals, laboratories) and data
            fiduciaries (e.g. health programmes), which are registered with or
            linked to the Ayushman Bharat Digital Mission (ABDM), and various
            processes there under. I authorize NHA to use my Aadhaar number for
            performing Aadhaar based authentication with UIDAI as per the
            provisions of the Aadhaar (Targeted Delivery of Financial and other
            Subsidies, Benefits and Services) Act, 2016 for the aforesaid
            purpose. I understand that UIDAI will share my e-KYC details, or
            response of “Yes” with NHA upon successful authentication. I have
            been duly informed about the option of using other IDs apart from
            Aadhaar; however, I consciously choose to use Aadhaar number for the
            purpose of availing benefits across the NDHE. I am aware that my
            personal identifiable information excluding Aadhaar number / VID
            number can be used and shared for purposes as mentioned above. I
            reserve the right to revoke the given consent at any point of time
            as per provisions of Aadhaar Act and Regulations.
          </p>
        </div>
        <div className="check-box-div">
          <Form.Check
            value={checkBoxValue}
            type="checkbox"
            className={isChecked(checkBoxValue)}
            label="I agree to the terms and conditions"
            name="dd"
            onChange={checkTerms}
            style={{ fontSize: "14px" }}
          />
          <span
            style={{ color: "red", fontSize: "13px" }}
            className="error-text"
          >
            {checkBoxText}
          </span>
        </div>
        <div className="modal-btn-div">
          <div className="">
            <Button
              variant="primary"
              className="back-btn-adhar"
              value="mobile"
              onClick={gotoBackPage}
            >
              Back
            </Button>
            <Button
              variant="primary"
              className="genIdBtn"
              value="mobile"
              onClick={getOtpByAdhar}
            >
              Next
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
}

export default EnterAdharForm;
