import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

function EmailVerification(props) {
  const [enterEmail, setEnterEmail] = useState("");
  const verifyEmail = () => {};

  return (
    <div>
      <div className="abha-profile-div">
        <h4>Email - Verification (Optional)</h4>
        <p>
          To kick start your digital health journey, link an Email Address with
          your ABHA Number.{" "}
        </p>
      </div>
      <Form className="adhar-form-div">
        <div className="col-container adhar-form">
          <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
            <Form.Label className="require">
              <b>E-mail</b>
            </Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter Your Email"
              value={enterEmail || ""}
              name="email"
              onChange={(e) => setEnterEmail(e.target.value)}
            />
          </Form.Group>
        </div>
        <div className="modal-btn-email">
          <div>
            <h4 onClick={skipMailVerification}>Skip for Now</h4>
          </div>
          <div>
            <Button
              variant="primary"
              className="genIdBtn"
              value="mobile"
              onClick={verifyEmail}
            >
              GetOTP
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
}

export default EmailVerification;
