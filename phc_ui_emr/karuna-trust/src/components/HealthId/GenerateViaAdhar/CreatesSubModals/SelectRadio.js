import React, { useState } from "react";
import { Row, Col, Modal, Form, Button } from "react-bootstrap";

const healthidcheckbox = ["Using Aadhaar", "Using Mobile Number"];
function SelectRadio(props) {
  let selectType = props.selectType;

  const [checked, setChecked] = useState([]);
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const checkHealthdata = (e) => {
    props.setSelectType(e.target.value);
  };

  const createPage = (type) => {
    if (type == "Using Aadhaar") {
      props.setSelectStatus(true);
      props.setAdharModal(true);
    } else if ((type = "Using Mobile Number")) {
      props.setSelectStatus(true);
      props.setMobileModal(true);
    }
  };

  return (
    <div>
      <React.Fragment>
        <div className="check-box-option">
          {healthidcheckbox.map((item, i) => (
            <div key={i}>
              <Form.Check
                key={i}
                value={item}
                type="radio"
                className={isChecked(item)}
                label={item}
                name="dd"
                onChange={checkHealthdata}
                style={{ fontSize: "14px" }}
              />
              {selectType &&
                selectType == item &&
                selectType == "Using Aadhaar" && (
                  <p className="radio-text-div">
                    You can create your ABHA number using Aadhaar
                    instantaneously. Please ensure that Aadhaar is linked to a
                    Mobile Number as an OTP authentication will follow. If you
                    do not have a Mobile Number linked, visit the nearest{" "}
                    <a href="https://facility.ndhm.gov.in">
                      ABDM participating facility
                    </a>{" "}
                    and seek assistance.
                  </p>
                )}
              {selectType &&
                selectType == item &&
                selectType == "Using Mobile Number" && (
                  <p className="radio-text-div">
                    Please ensure that Aadhaar is linked to a Mobile Number as
                    an OTP authentication will follow. If you do not have a
                    Mobile Number linked, visit the nearest{" "}
                    <a href="https://facility.ndhm.gov.in">
                      ABDM participating facility
                    </a>{" "}
                    and seek assistance.
                  </p>
                )}
            </div>
          ))}
        </div>
      </React.Fragment>
      <div className="modal-btn-div">
        <div className="">
          <Button
            variant="primary"
            className="genIdBtn"
            value="mobile"
            disabled={!selectType}
            onClick={(e) => createPage(selectType)}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
  );
}

export default SelectRadio;
