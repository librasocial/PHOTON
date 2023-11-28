import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Breadcrumb,
  Card,
  Modal,
  Carousel,
} from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import {
  loadAllActivityList,
  loadAllProcessList,
  loadAssignActPhc,
  loadAsssignActivityList,
} from "../../../redux/phcAction";
import SaveButton from "../../EMR_Buttons/SaveButton";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

export default function ResponsibilityModal(props) {
  let dispatch = useDispatch();
  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const { staffDetails } = useSelector((state) => state.phcData);
  const { allActivityList } = useSelector((state) => state.phcData);
  const { allProcessList } = useSelector((state) => state.phcData);

  const { activityList } = useSelector((state) => state.phcData);

  const [activityForProcess, setActivityForProcess] = useState([]);

  useEffect(() => {
    if (allActivityList) {
      let actProcList = [];
      allProcessList.map((process) => {
        let activityList = [];
        allActivityList.map((item) => {
          if (
            process.targetNode?.properties?.name ==
            item.sourceNode?.properties?.name
          ) {
            activityList.push(item);
          }
        });
        actProcList.push({
          procUuid: process.targetNode?.properties?.uuid,
          processName: process.targetNode?.properties?.name,
          activities: activityList,
        });
      });
      setActivityForProcess(actProcList);
    }
  }, [allActivityList, allProcessList]);

  useEffect(() => {
    dispatch(loadAllProcessList());
    dispatch(loadAllActivityList());
  }, []);

  const [assignActivityValue, setAssignActivityValue] = useState("");

  useEffect(() => {
    let getActivities = [];
    var ele = document.getElementsByName("Activity-list");
    for (var j = 0; j < activityList.length; j++) {
      for (var k = 0; k < ele.length; k++) {
        if (activityList[j].targetNode?.properties?.uuid == ele[k].value) {
          ele[k].checked = true;
          getActivities.push(activityList[j].targetNode?.properties?.uuid);
        }
      }
    }
    setAssignActivityValue(getActivities);
  }, [activityList, allActivityList]);

  const setAssignActivity = (e, actUuid, procName) => {
    if (e.target.checked) {
      setAssignActivityValue([
        ...assignActivityValue,
        { uuid: actUuid, name: procName },
      ]);
    } else {
      setAssignActivityValue(
        assignActivityValue.filter((id) => id.uuid !== actUuid)
      );
    }
  };

  const closingModal = () => {
    props.staffActClose(false);
    setAssignActivityValue([]);
    dispatch(loadAsssignActivityList(null));
  };

  const saveActivity = (name) => {
    let selectActivity = [];
    assignActivityValue.map((item) => {
      if (item.name == name) {
        selectActivity.push(item.uuid);
      }
    });
    let orgType;
    let orgUuid;
    if (props.center_type == "Phc") {
      orgType = "Phc";
      orgUuid = props.phcuuid;
    } else if (props.center_type == "Sub-Center") {
      orgType = "SubCenter";
      orgUuid = props.phcuuid;
    }

    for (var i = 0; i < selectActivity.length; i++) {
      var postActivity = {
        relationship: {
          type: "HASACCESS",
          properties: {
            orgType: orgType,
            orgUuid: orgUuid,
          },
        },
        source: {
          type: staffDetails[0].properties.type,
          properties: {
            uuid: staffDetails[0].properties.uuid,
          },
        },
        target: {
          type: "Activity",
          properties: {
            uuid: selectActivity[i],
          },
        },
      };

      var postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(postActivity),
      };
      dispatch(loadAssignActPhc(postResponse, closingModal));
    }
  };

  return (
    <React.Fragment>
      <Modal
        show={props.staffActShow}
        onHide={props.staffActClose}
        className="staff-act-div"
      >
        <div className="phc-header-div">
          <h5 className="staff-header">
            Select activities to assign responsibilities to{" "}
            {staffDetails[0] && (
              <>
                {" "}
                {staffDetails[0].properties.salutation}{" "}
                {staffDetails[0].properties.name}{" "}
              </>
            )}
            <i
              className="fa fa-close close-btn-style"
              onClick={closingModal}
            ></i>
          </h5>
        </div>
        <Form>
          {activityForProcess &&
            activityForProcess.map((process, i) => (
              <div className="resp-details-div" key={i}>
                <h5 className="staff-header"> {process.processName} </h5>
                <div className="box-dsg1">
                  <div className="staffResp_details">
                    <h5 className="log-access-header">
                      {process.targetNode?.properties?.name} Activities
                    </h5>
                    <p className="staff-task-body">
                      Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
                      sed diam nonumy eirmod tempor invidunt ut labore e dolore
                      magna aliquyam erat, sed diam voluptua. At vero eos et
                      accusam et justo duo
                    </p>
                    <Row className="responsibility-buttons">
                      {process.activities.map((activity, index) => (
                        <Col md={3} key={index}>
                          <input
                            className="checkbox-tools"
                            type="checkbox"
                            name="Activity-list"
                            id={"role" + index + i}
                            value={activity.targetNode?.properties?.uuid}
                            onChange={(e) =>
                              setAssignActivity(
                                e,
                                activity.targetNode?.properties?.uuid,
                                process.targetNode?.properties?.name
                              )
                            }
                          />
                          <label
                            className="for-checkbox-tools"
                            htmlFor={"role" + index + i}
                          >
                            <span className={isChecked(assignActivityValue)}>
                              {activity.targetNode?.properties?.name}
                            </span>
                          </label>
                        </Col>
                      ))}
                    </Row>
                  </div>
                  <hr />
                  <div className="staffResp_buttons">
                    <SaveButton
                      class_name="regBtnN"
                      button_name="Save"
                      butttonClick={(e) =>
                        saveActivity(process.targetNode?.properties?.name)
                      }
                    />
                  </div>
                </div>
              </div>
            ))}
          {/* {allProcessList && allProcessList.map((process, i) => (
                        <div className='resp-details-div' key={i}>
                            <h5 className="staff-header"> {process.targetNode?.properties?.name} </h5>
                            <div className='box-dsg1'>
                                <div className="staffResp_details">
                                    <h5 className='log-access-header'>{process.targetNode?.properties?.name} Activities</h5>
                                    <p className='staff-task-body'>
                                        Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore e dolore magna aliquyam erat,
                                        sed diam voluptua. At vero eos et accusam et justo duo
                                    </p>
                                    <Row className="responsibility-buttons">
                                        {allActivityList && allActivityList.map((activity, index) => (
                                            <Col md={3} key={index}>
                                                {process.targetNode?.properties?.name === activity.sourceNode?.properties?.name &&
                                                    <div>
                                                        <input className="checkbox-tools" type="checkbox" name="Activity-list" id={"role" + index}
                                                            value={activity.targetNode?.properties?.uuid}
                                                            onChange={(e) => setAssignActivity(e, activity.targetNode?.properties?.uuid, process.targetNode?.properties?.name)}
                                                        />
                                                        <label className="for-checkbox-tools" htmlFor={"role" + index}>
                                                            <span className={isChecked(assignActivityValue)} >{activity.targetNode?.properties?.name}</span>
                                                        </label>
                                                    </div>
                                                }
                                            </Col>
                                        ))}
                                    </Row>
                                </div>
                                <hr />
                                <div className="staffResp_buttons">
                                    <SaveButton class_name="regBtnN" button_name="Save" butttonClick={(e) => saveActivity(process.targetNode?.properties?.name)} />
                                </div>
                            </div>
                        </div>
                    ))} */}
        </Form>
      </Modal>
    </React.Fragment>
  );
}
