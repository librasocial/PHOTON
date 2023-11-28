import React, { useState, useEffect } from "react";
import { Modal, Accordion } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

export default function AssignRegisterActivityModal(props) {
  let assignProcessResponse = [
    { name: "Register patient to EMR" },
    { name: "Create Visit" },
    { name: "Queue Management" },
  ];
  const [assignProcessValue, setAssignProcessValue] = useState("");

  const setAssignProcess = (e, name) => {
    if (e.target.checked) {
      setAssignProcessValue(name);
    } else {
      setAssignProcessValue("");
    }
  };

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  return (
    <React.Fragment>
      <Modal
        show={props.assignRegisterActivity}
        onHide={props.activityCloseRegister}
        className="assign-register-div"
      >
        <div className="assign-div-pro">
          <h5 className="assign-process-header">
            Activities under Registration Process{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={props.activityCloseRegister}
            ></i>
          </h5>
        </div>
        <div className="assign-staff-details">
          <p className="total-resp-process">
            <i className="bi bi-info-square-fill add-phc-icon"></i>
            You can <b>enable/disable</b> multiple activities to the{" "}
            <b>'Registration Process'</b>
          </p>
        </div>
        <div className="assign-staff-accordion">
          <Accordion id="reg-accordion" alwaysOpen defaultActiveKey={["0"]}>
            <Accordion.Item eventKey="0" id="custom-acco-item">
              <Accordion.Header className="additional-roles-head">
                <span className="additional-staff">
                  Add Additional Details{" "}
                </span>
              </Accordion.Header>
              <Accordion.Body className="additional-roles-body">
                <div className="additional-body-staff">
                  <p className="additional-staff-para">
                    Registration process facilitates an User to Register a
                    Patient into EMR, Create a Visit to a Patient and Manage
                    Patients Queue.
                  </p>
                </div>
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>
        </div>
        <div className="assign-staff-details">
          <div className="responsibility-buttons">
            {assignProcessResponse.map((Response, i) => (
              <React.Fragment key={i}>
                <input
                  className="checkbox-tools"
                  type="checkbox"
                  name="Response"
                  id={"resp" + i}
                  value={assignProcessValue}
                  checked={Response.name == assignProcessValue}
                  onChange={(e) => setAssignProcess(e, Response.name)}
                />
                <label className="for-checkbox-tools" htmlFor={"resp" + i}>
                  <span className={isChecked(assignProcessValue)}>
                    {Response.name}
                  </span>
                </label>
              </React.Fragment>
            ))}
          </div>
        </div>
        <div className="assign-pro-btn">
          <div className="save-btn-section">
            <SaveButton
              butttonClick={props.assignAllActivity}
              class_name="regBtnPC"
              button_name="Assign All Activities"
            />
          </div>
          <div className="save-btn-section">
            <SaveButton
              butttonClick={props.assignSelectActivity}
              class_name="regBtnN"
              button_name="Assign Selected Activities"
            />
          </div>
        </div>
      </Modal>
    </React.Fragment>
  );
}
