import React, { useState } from "react";
import { Row, Col, Modal, Form, Button } from "react-bootstrap";

const options = ["Yes", "No"];

function AbhaAddress() {
  const [checked, setChecked] = useState([]);
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [selectType, setSelectType] = useState("");
  const checkHealthdata = (e) => {
    setSelectType(e.target.value);
  };

  const createAbhaAddress = (selectType) => {};

  return (
    <div>
      <div className="abha-profile-div">
        <h6>
          Link ABHA Address with your existing ABHA Number to enable health data
          sharing
        </h6>
      </div>
      <React.Fragment>
        <h4>Do you have an existing ABHA address?</h4>
        <div className="check-box-option">
          {options.map((item, i) => (
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
            onClick={(e) => createAbhaAddress(selectType)}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
  );
}

export default AbhaAddress;
