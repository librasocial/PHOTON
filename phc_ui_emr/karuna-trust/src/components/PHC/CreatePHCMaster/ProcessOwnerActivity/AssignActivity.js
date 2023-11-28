import React, { useState, useEffect } from "react";
import { Modal, Accordion, Row, Col } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { loadAllActivityList } from "../../../../redux/phcAction";
import SaveButton from "../../../EMR_Buttons/SaveButton";

export default function AssignActivity(props) {
  let dispatch = useDispatch();
  const { allActivityList } = useSelector((state) => state.phcData);

  const [assignProcessValue, setAssignProcessValue] = useState([]);

  const setAssignProcess = (e, name) => {
    if (e.target.checked) {
      setAssignProcessValue([...assignProcessValue, name]);
    } else {
      setAssignProcessValue(assignProcessValue.filter((id) => id !== name));
    }
  };

  const assignAllActivity = () => {
    let ele = document.getElementsByName("Activity-list");
    let value = [];
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = true;
        value.push(ele[i].value);
      }
    }
    setAssignProcessValue(value);
  };
  const unassignAllActivity = () => {
    let ele = document.getElementsByName("Activity-list");
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = false;
      }
    }
    setAssignProcessValue([]);
  };

  const [selectedActivity, setSelectedActivity] = useState(false);
  useEffect(() => {
    let ele = document.getElementsByName("Activity-list");

    if (assignProcessValue.length != 0) {
      if (assignProcessValue.length === ele.length) {
        setSelectedActivity(true);
      } else {
        setSelectedActivity(false);
      }
    } else {
      setSelectedActivity(false);
    }
  }, [assignProcessValue]);

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  useEffect(() => {
    dispatch(loadAllActivityList());
  }, []);

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
            <b>'{props.assignRolesValue.name} Process'</b>
          </p>
        </div>
        <div className="assign-staff-accordion">
          <Accordion id="reg-accordion" alwaysOpen defaultActiveKey={["0"]}>
            <Accordion.Item eventKey="0" id="custom-acco-item">
              <Accordion.Header className="additional-roles-head">
                <span className="additional-staff">
                  {props.assignRolesValue.name} Process Activities List
                </span>
              </Accordion.Header>
              <Accordion.Body className="additional-roles-body">
                <div className="additional-body-staff">
                  <p className="additional-staff-para">
                    {props.assignRolesValue.name} process facilitates an User to
                    Register a Patient into EMR, Create a Visit to a Patient and
                    Manage Patients Queue.
                  </p>
                </div>
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>
        </div>
        <div className="assign-staff-details">
          <div className="responsibility-buttons">
            <Row style={{ width: "100%" }}>
              {allActivityList &&
                allActivityList.map((Response, i) => (
                  <Col md={3} key={i}>
                    {Response?.sourceNode?.properties?.name ==
                      props.assignRolesValue.name && (
                      <React.Fragment>
                        <input
                          className="checkbox-tools"
                          type="checkbox"
                          name="Activity-list"
                          id={"resp" + i}
                          value={Response?.targetNode?.properties?.name}
                          onChange={(e) =>
                            setAssignProcess(
                              e,
                              Response?.targetNode?.properties?.name
                            )
                          }
                        />
                        <label
                          className="for-checkbox-tools"
                          htmlFor={"resp" + i}
                        >
                          <span className={isChecked(assignProcessValue)}>
                            {Response?.targetNode?.properties?.name}
                          </span>
                        </label>
                      </React.Fragment>
                    )}
                  </Col>
                ))}
            </Row>
          </div>
        </div>
        <div className="assign-pro-btn">
          <div className="save-btn-section">
            <SaveButton
              butttonClick={
                selectedActivity == true
                  ? unassignAllActivity
                  : assignAllActivity
              }
              class_name="regBtnPC"
              button_name={
                selectedActivity == true
                  ? "UnAssign All Activities"
                  : "Assign All Activities"
              }
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
