import React, { useState, useEffect } from "react";
import { Modal, Accordion, Row, Col } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import {
  getParticularOwner,
  loadAllActivityList,
  loadAssignActPhc,
  loadAsssignActivityList,
} from "../../../redux/phcAction";
import SaveButton from "../../EMR_Buttons/SaveButton";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

let orgType;
let orgUuid;
export default function AssignActivity(props) {
  let dispatch = useDispatch();
  const { allActivityList } = useSelector((state) => state.phcData);
  const phcuuid = sessionStorage.getItem("uuidofphc");

  const { activityList } = useSelector((state) => state.phcData);

  const [assignProcessValue, setAssignProcessValue] = useState([]);
  const [activityForProcess, setActivityForProcess] = useState([]);

  useEffect(() => {
    let getActivities = [];
    var ele = document.getElementsByName("Activity-list");
    for (var k = 0; k < ele.length; k++) {
      for (var j = 0; j < activityList.length; j++) {
        if (
          activityList[j].targetNode?.properties?.uuid.includes(
            activityForProcess[k].targetNode?.properties?.uuid
          )
        ) {
          ele[k].checked = true;
          getActivities.push(activityList[j].targetNode?.properties?.uuid);
        }
      }
    }
    setAssignProcessValue(getActivities);
  }, [activityList]);

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

    if (props.center_type) {
      if (props.center_type == "Phc") {
        orgType = "Phc";
        orgUuid = phcuuid;
      } else if (props.center_type == "Sub-Center") {
        orgType = "SubCenter";
        orgUuid = props.sunCenterDetails?.properties?.uuid;
      }
    }

    if (allActivityList) {
      let activityList = [];
      allActivityList.map((item) => {
        if (item.sourceNode?.properties?.uuid == props.processUuid.uuid) {
          activityList.push(item);
        }
      });
      setActivityForProcess(activityList);
    }
  }, [
    assignProcessValue,
    phcuuid,
    props.center_type,
    props.sunCenterDetails,
    allActivityList,
    props.processUuid,
  ]);

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  useEffect(() => {
    // dispatch(loadAllActivityList(props.processUuid.uuid))
    dispatch(loadAllActivityList());
  }, []);

  const closingModal = () => {
    props.setAssignRolesValue("");
    props.activityCloseRegister(false);
    setActivityForProcess([]);
    dispatch(loadAsssignActivityList(null));
  };

  const assignSelectActivity = () => {
    for (var i = 0; i < assignProcessValue.length; i++) {
      var postDataActivity = {
        relationship: {
          type: "HASACCESS",
          properties: {
            orgType: orgType,
            orgUuid: orgUuid,
          },
        },
        source: {
          type: props.employeeUuid.labels[0],
          properties: {
            uuid: props.employeeUuid.properties.uuid,
          },
        },
        target: {
          type: "Activity",
          properties: {
            uuid: assignProcessValue[i],
          },
        },
      };
      var postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(postDataActivity),
      };
      dispatch(loadAssignActPhc(postResponse, closingModal));
    }
  };

  return (
    <React.Fragment>
      <Modal
        show={props.assignRegisterActivity}
        onHide={props.activityCloseRegister}
        className="assign-register-div"
      >
        <div className="assign-div-pro">
          <h5 className="assign-process-header">
            Activities under {props.processUuid.name} Process{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={closingModal}
            ></i>
          </h5>
        </div>
        <div className="assign-staff-details">
          <p className="total-resp-process">
            <i className="bi bi-info-square-fill add-phc-icon"></i>
            You can <b>enable/disable</b> multiple activities to the{" "}
            <b>'{props.processUuid.name} Process'</b>
          </p>
        </div>
        <div className="assign-staff-accordion">
          <Accordion id="reg-accordion" alwaysOpen defaultActiveKey={["0"]}>
            <Accordion.Item eventKey="0" id="custom-acco-item">
              <Accordion.Header className="additional-roles-head">
                <span className="additional-staff">
                  {props.processUuid.name} Process Activities List
                </span>
              </Accordion.Header>
              <Accordion.Body className="additional-roles-body">
                <div className="additional-body-staff">
                  <p className="additional-staff-para">
                    {props.processUuid.name} process facilitates an User to
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
              {activityForProcess &&
                activityForProcess.map((Response, i) => (
                  <Col md={3} key={i}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Activity-list"
                      id={"resp" + i}
                      value={Response?.targetNode?.properties?.uuid}
                      onChange={(e) =>
                        setAssignProcess(
                          e,
                          Response?.targetNode?.properties?.uuid
                        )
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={"resp" + i}>
                      <span className={isChecked(assignProcessValue)}>
                        {Response?.targetNode?.properties?.name}
                      </span>
                    </label>
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
              butttonClick={assignSelectActivity}
              class_name="regBtnN"
              button_name="Assign Selected Activities"
            />
          </div>
        </div>
      </Modal>
    </React.Fragment>
  );
}
