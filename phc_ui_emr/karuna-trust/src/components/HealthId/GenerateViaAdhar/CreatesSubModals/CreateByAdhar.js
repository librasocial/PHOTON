import React, { useEffect, useState } from "react";
import EnterAdharForm from "./EnterAdharForm";
import GenerateAbhaAddress from "./GenerateAbhaAddress";
import MobileLinked from "./MobileLinked";
import OtpVerification from "./OtpVerification";
import SuccessPage from "./SuccessPage";
import { useDispatch } from "react-redux";

function CreateByAdhar(props) {
  const [formStatus, setFormStatus] = useState("enter-adhar");

  return (
    <div>
      <div className="multi-steps-div">
        <div className="progress-steps">
          <span className="steps-text-div">
            <span
              className={
                formStatus && formStatus !== "enter-adhar"
                  ? "wrapper-circle-active"
                  : "wrapper-circle"
              }
            >
              <h5>
                {formStatus && formStatus !== "enter-adhar" ? (
                  <span>&#10003;</span>
                ) : (
                  "1"
                )}
              </h5>
            </span>
            <span>
              <p className="progress-content">Consent Collection</p>
            </span>
          </span>
        </div>

        <div className="progress-steps">
          <div className="progress-line">
            <span
              className={
                formStatus && formStatus !== "enter-adhar"
                  ? "css-95m0ql-active"
                  : "css-95m0ql"
              }
            ></span>
          </div>
          <span className="steps-text-div">
            <span
              className={
                formStatus &&
                (formStatus === "mobile-verified" ||
                  formStatus === "abha-created")
                  ? "wrapper-circle-active"
                  : "wrapper-circle"
              }
            >
              <h5>
                {formStatus &&
                (formStatus === "mobile-verified" ||
                  formStatus === "abha-created") ? (
                  <span>&#10003;</span>
                ) : (
                  "2"
                )}
              </h5>
            </span>
            <span>
              <p className="progress-content">Aadhaar Authentication</p>
            </span>
          </span>
        </div>

        <div className="progress-steps">
          <div className="progress-line">
            <span
              className={
                formStatus &&
                (formStatus === "mobile-verified" ||
                  formStatus === "abha-created")
                  ? "css-95m0ql-active"
                  : "css-95m0ql"
              }
            ></span>
          </div>
          <span className="steps-text-div">
            <span
              className={
                formStatus && formStatus === "abha-created"
                  ? "wrapper-circle-active"
                  : "wrapper-circle"
              }
            >
              <h5>
                {formStatus && formStatus === "abha-created" ? (
                  <span>&#10003;</span>
                ) : (
                  "3"
                )}
              </h5>
            </span>
            <span>
              <p className="progress-content">Link ABHA Address</p>
            </span>
          </span>
        </div>

        {/* <div className="progress-steps">
                    <div
                        className='progress-line'
                    >
                        <span className='css-95m0ql'></span>
                    </div>
                    <span className='steps-text-div'>
                        <span className='wrapper-circle'>
                            <h5>4</h5>
                        </span>
                        <span>
                            <p className="progress-content">ABHA number Creation</p>
                        </span>
                    </span>
                </div> */}
      </div>

      <div>
        {formStatus === "enter-adhar" && (
          <EnterAdharForm
            setFormStatus={setFormStatus}
            setSelectType={props.setSelectType}
            setSelectStatus={props.setSelectStatus}
            AbhaModalClose={props.AbhaModalClose}
          />
        )}
        {formStatus === "adhar-otp" && (
          <OtpVerification setFormStatus={setFormStatus} otpType="Aadhar" />
        )}
        {formStatus === "adhar-verified" && (
          <MobileLinked setFormStatus={setFormStatus} />
        )}
        {formStatus === "mobile-verified" && (
          <GenerateAbhaAddress setFormStatus={setFormStatus} />
        )}
        {formStatus === "abha-created" && (
          <SuccessPage
            setFormStatus={setFormStatus}
            setAadharStatus={props.setAadharStatus}
          />
        )}
        {formStatus === "mobile-link" && (
          <OtpVerification setFormStatus={setFormStatus} otpType="Mobile" />
        )}
      </div>
    </div>
  );
}

export default CreateByAdhar;
