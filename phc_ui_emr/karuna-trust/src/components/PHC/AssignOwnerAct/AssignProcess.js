import React, { useState, useEffect } from "react";
import { Form, Breadcrumb } from "react-bootstrap";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import SaveButton from "../../EMR_Buttons/SaveButton";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import AssignModal from "./AssignModal";
import AssignActivity from "./AssignActivity";
import CancelFacilityModal from "../Facility/CancelFacilityModal";
import {
  getParticularOwner,
  loadAllProcessList,
  loadProcessOwner,
  loadAsssignActivityList,
} from "../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import PageLoader from "../../PageLoader";

export default function AssignProcess(props) {
  let dispatch = useDispatch();
  const { allProcessList } = useSelector((state) => state.phcData);
  const { assignedOwner } = useSelector((state) => state.phcData);

  const removevitalstorage1 = () => {
    window.history.back();
  };

  const [listProcess, setListProcess] = useState([]);
  const [pageLoader, setPageLoader] = useState(false);

  useEffect(() => {
    document.title = "EMR Super Admin Assign Process Owner & Assign Activities";
    if (allProcessList) {
      let processId = [];
      let processList = allProcessList.map((items) => {
        processId.push(items.targetNode?.properties?.uuid);
        return items.targetNode;
      });
      setListProcess(processList);
      // dispatch(loadProcessOwner(processId))
    }
  }, [allProcessList]);

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [assignRolesValue, setAssignRolesValue] = useState("");
  console.log(assignRolesValue, "assignRolesValue");

  const setAssignRoles = (e, name) => {
    if (e.target.checked) {
      setPageLoader(true);
      setAssignRolesValue(name);
    } else {
      setAssignRolesValue("");
    }
  };

  useEffect(() => {
    document.title = "EMR Super Admin Assign Process Owner & Assign Activities";
    if (assignRolesValue) {
      dispatch(
        getParticularOwner(
          assignRolesValue.uuid,
          props.center_type,
          setPageLoader
        )
      );
    } else {
      dispatch(getParticularOwner(null));
    }
  }, [assignRolesValue]);

  const [assignFacilityWindow, setAssignFacilityWindow] = useState(false);
  const facilityWindowClose = () => {
    setAssignFacilityWindow(false);
  };
  const assignRolesRespons = () => setAssignFacilityWindow(true);

  useEffect(() => {
    dispatch(loadAllProcessList());
  }, []);

  const [assignEnable, setAssignEnable] = useState(false);

  const [assignRegisterActivity, setAssignRegisterActivity] = useState(false);
  const activityCloseRegister = () => {
    setAssignRegisterActivity(false);
  };
  const [processUuid, setProcessUuid] = useState();
  const [employeeUuid, setEmployeeUuid] = useState();
  const activityRegister = (proc, emp) => {
    setProcessUuid(proc);
    setEmployeeUuid(emp);
    setAssignRegisterActivity(true);
    dispatch(loadAsssignActivityList(emp?.properties?.uuid));
  };

  function saveAssignProcess() {
    Tostify.notifySuccess("Are you sure, You want to Assign this Process ?");
  }
  // Assign Process Owner

  //Reset Assign Process Owner
  const [cancelProcessWindow, setCancelProcessWindow] = useState(false);

  const facilityProcessClose = () => {
    setCancelProcessWindow(false);
  };

  const resetProcess = () => setCancelProcessWindow(true);
  let history = useHistory();
  const removeCurrentPage = () => {
    history.go(-2);
  };
  //Reset Assign Process Owner

  return (
    <React.Fragment>
      {/* page loader */}
      {pageLoader && <PageLoader />}
      {/* page loader */}
      <ToastContainer />
      <CancelFacilityModal
        cancelProcessWindow={cancelProcessWindow}
        facilityProcessClose={facilityProcessClose}
        removevitalstorage1={removevitalstorage1}
      />
      <div className="div vital-div">
        <div className="regHeader">
          <h1 className="Encounter-Header">PHC Configuration</h1>
          <hr style={{ margin: "0px" }} />
        </div>
        <div className="config-div">
          <div className="super-tab">
            <div className="super-breadcrumb">
              {props.center_type == "Phc" && (
                <Breadcrumb>
                  <Breadcrumb.Item
                    className="pur-order-breadcrumb"
                    onClick={removeCurrentPage}
                  >
                    Dashboard
                  </Breadcrumb.Item>
                  <Breadcrumb.Item
                    className="pur-order-breadcrumb"
                    onClick={removevitalstorage1}
                  >
                    PHC Configuration
                  </Breadcrumb.Item>
                  <Breadcrumb.Item active className="phc-breadcrumb">
                    Assign Process Owner & Assign Activities
                  </Breadcrumb.Item>
                </Breadcrumb>
              )}
              {props.center_type == "Sub-Center" && (
                <Breadcrumb>
                  <Breadcrumb.Item
                    className="pur-order-breadcrumb"
                    onClick={removeCurrentPage}
                  >
                    PHC Configuration
                  </Breadcrumb.Item>
                  <Breadcrumb.Item
                    className="pur-order-breadcrumb"
                    onClick={removevitalstorage1}
                  >
                    Select SC/HWC to Assign Process Owner & Assign Activities
                  </Breadcrumb.Item>
                  <Breadcrumb.Item active className="phc-breadcrumb">
                    Assign Process Owner & Assign Activities
                  </Breadcrumb.Item>
                </Breadcrumb>
              )}
            </div>
            {assignFacilityWindow && (
              <AssignModal
                assignFacilityWindow={assignFacilityWindow}
                facilityWindowClose={facilityWindowClose}
                assignRolesValue={assignRolesValue}
                center_type={props.center_type}
                sunCenterDetails={props.sunCenterDetails}
              />
            )}
            {assignRegisterActivity && (
              <AssignActivity
                assignRegisterActivity={assignRegisterActivity}
                activityCloseRegister={activityCloseRegister}
                center_type={props.center_type}
                sunCenterDetails={props.sunCenterDetails}
                processUuid={processUuid}
                employeeUuid={employeeUuid}
                setAssignRolesValue={setAssignRolesValue}
              />
            )}
            <div className="form-col">
              <Form className="super-admin-form">
                <div className="assign-complaint">
                  <h3 className="super-config-details">
                    Assign Process Owner and Assign Activities to the Process
                  </h3>
                  <p className="total-asign-process">
                    <i className="bi bi-info-square-fill add-phc-icon"></i>
                    <b>Select</b> a process, <b>assign</b> a process owner and{" "}
                    <b>assign activities</b> to that process
                  </p>
                  <img
                    src="../img/super/arrowdown.png"
                    className="assign-process-image"
                  />
                </div>
                <div className="responsibility-buttons">
                  {listProcess.map((Process, i) => (
                    <React.Fragment key={i}>
                      <input
                        className="checkbox-tools"
                        type="checkbox"
                        name="Process"
                        id={"role" + i}
                        value={assignRolesValue ? assignRolesValue.name : ""}
                        checked={
                          assignRolesValue &&
                          Process.properties?.name == assignRolesValue.name
                        }
                        onChange={(e) => setAssignRoles(e, Process.properties)}
                      />
                      <label
                        className="for-checkbox-tools"
                        htmlFor={"role" + i}
                      >
                        <span
                          className={isChecked(
                            assignRolesValue && assignRolesValue.name
                          )}
                        >
                          {Process.properties?.name}
                        </span>
                      </label>
                    </React.Fragment>
                  ))}
                </div>
                <Paper>
                  <TableContainer sx={{ maxHeight: 440 }}>
                    <Table
                      stickyHeader
                      aria-label="sticky table"
                      className="assign-roles-table"
                    >
                      <TableHead>
                        <TableRow>
                          <TableCell align="center" style={{ width: "10%" }}>
                            Process Name
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            Assign Process Owner
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            Activities under the Process
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            Enable/Disable Process
                          </TableCell>
                        </TableRow>
                      </TableHead>

                      <>
                        {assignRolesValue && (
                          <TableBody>
                            <TableRow>
                              <TableCell
                                align="center"
                                style={{ width: "10%" }}
                              >
                                {assignRolesValue.name}
                              </TableCell>
                              <TableCell
                                align="center"
                                style={{ width: "15%" }}
                              >
                                <div
                                  className="role-details"
                                  onClick={assignRolesRespons}
                                >
                                  <img
                                    src="../img/super/assign.png"
                                    className="role-image"
                                  />
                                  Assign Owner
                                </div>
                              </TableCell>
                              <TableCell
                                align="center"
                                style={{ width: "15%" }}
                              >
                                <p className="activity-text">
                                  Select Activities
                                </p>
                              </TableCell>
                              <TableCell
                                align="center"
                                style={{ width: "15%" }}
                              >
                                <div className="phc-address phc-general-table">
                                  <Form.Group
                                    className="facility-details"
                                    controlId="exampleForm.MName"
                                  >
                                    <div
                                      className={`checkbox ${
                                        assignEnable && "checkbox--on"
                                      }`}
                                      onClick={() =>
                                        setAssignEnable(!assignEnable)
                                      }
                                    >
                                      <div className="checkbox__ball">
                                        <span className="status-assign-text">
                                          {assignEnable == false
                                            ? "Disabled"
                                            : "Enabled"}
                                        </span>
                                      </div>
                                    </div>
                                  </Form.Group>
                                </div>
                              </TableCell>
                            </TableRow>
                          </TableBody>
                        )}
                        {assignedOwner &&
                          assignedOwner.map((assignList, i) => (
                            <TableBody key={i}>
                              <TableRow>
                                <TableCell
                                  align="center"
                                  style={{ width: "10%" }}
                                >
                                  {assignList?.sourceNode?.properties?.name}
                                </TableCell>
                                <TableCell
                                  align="center"
                                  style={{ width: "15%" }}
                                >
                                  <div className="role-details">
                                    {assignList?.targetNode?.properties
                                      ?.photo ? (
                                      <img
                                        src={
                                          assignList?.targetNode?.properties
                                            ?.photo
                                        }
                                        className="person-image"
                                      />
                                    ) : (
                                      <img
                                        src="../img/super/member.png"
                                        className="person-image"
                                      />
                                    )}
                                    {assignList?.targetNode?.properties?.name}
                                  </div>
                                </TableCell>
                                <TableCell
                                  align="center"
                                  style={{ width: "15%" }}
                                >
                                  <p
                                    className="activity-text"
                                    onClick={(e) =>
                                      activityRegister(
                                        assignList?.sourceNode?.properties,
                                        assignList?.targetNode
                                      )
                                    }
                                    style={{ cursor: "pointer" }}
                                  >
                                    Select Activities
                                  </p>
                                </TableCell>
                                <TableCell
                                  align="center"
                                  style={{ width: "15%" }}
                                >
                                  <div className="phc-address phc-general-table">
                                    <Form.Group
                                      className="facility-details"
                                      controlId="exampleForm.MName"
                                    >
                                      <div
                                        className={`checkbox ${
                                          assignEnable && "checkbox--on"
                                        }`}
                                        onClick={() =>
                                          setAssignEnable(!assignEnable)
                                        }
                                      >
                                        <div className="checkbox__ball">
                                          <span className="status-assign-text">
                                            {assignEnable == false
                                              ? "Disabled"
                                              : "Enabled"}
                                          </span>
                                        </div>
                                      </div>
                                    </Form.Group>
                                  </div>
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          ))}
                      </>
                    </Table>
                  </TableContainer>
                </Paper>
                <div className="profession-assign-btn">
                  <div className="save-btn-section">
                    <SaveButton
                      butttonClick={resetProcess}
                      class_name="regBtnPC"
                      button_name="Cancel"
                    />
                  </div>
                  <div className="save-btn-section">
                    <SaveButton
                      butttonClick={saveAssignProcess}
                      class_name="regBtnN"
                      button_name="Save"
                    />
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
