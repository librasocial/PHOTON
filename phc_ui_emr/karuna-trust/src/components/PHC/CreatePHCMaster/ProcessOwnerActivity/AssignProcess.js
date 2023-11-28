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
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import AssignModal from "./AssignModal";
import AssignActivity from "./AssignActivity";
import CancelFacilityModal from "../CancelFacilityModal";
import { loadAllProcessList } from "../../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";

export default function AssignProcess(props) {
  let dispatch = useDispatch();
  const { allProcessList } = useSelector((state) => state.phcData);

  const removevitalstorage1 = () => {
    window.history.back();
  };

  const [listProcess, setListProcess] = useState([]);

  useEffect(() => {
    if (allProcessList) {
      let processList = allProcessList.map((items) => {
        return items.targetNode;
      });
      setListProcess(processList);
    }
  }, [allProcessList]);

  useEffect(() => {
    document.title = "EMR Super Admin Assign Process Owner & Assign Activities";
  });

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [assignRolesValue, setAssignRolesValue] = useState("");

  const setAssignRoles = (e, name) => {
    if (e.target.checked) {
      setAssignRolesValue(name);
    } else {
      setAssignRolesValue("");
    }
  };

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
  const activityRegister = () => setAssignRegisterActivity(true);

  function saveAssignProcess() {
    Tostify.notifySuccess("Are you sure, You want to Assign this Process ?");
  }
  // Assign Process Owner

  // Activities under Registration
  function assignSelectActivity() {
    Tostify.notifySuccess(
      "Are you sure, You want to Assign Selected Activities ?"
    );
  }
  // Activities under Registration

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
            </div>
            {assignFacilityWindow && (
              <AssignModal
                assignFacilityWindow={assignFacilityWindow}
                facilityWindowClose={facilityWindowClose}
                assignRolesValue={assignRolesValue}
              />
            )}
            {assignRegisterActivity && (
              <AssignActivity
                assignRegisterActivity={assignRegisterActivity}
                activityCloseRegister={activityCloseRegister}
                assignRolesValue={assignRolesValue}
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
                      {assignRolesValue && (
                        <TableBody>
                          <TableRow>
                            <TableCell align="center" style={{ width: "10%" }}>
                              {assignRolesValue.name}
                            </TableCell>
                            <TableCell align="center" style={{ width: "15%" }}>
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
                            <TableCell align="center" style={{ width: "15%" }}>
                              <p className="activity-text">Select Activities</p>
                            </TableCell>
                            <TableCell align="center" style={{ width: "15%" }}>
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

                      <TableBody>
                        <TableRow>
                          <TableCell align="center" style={{ width: "10%" }}>
                            Registration
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            <div
                              className="role-details"
                              onClick={assignRolesRespons}
                            >
                              <img
                                src="../img/super/member.png"
                                className="person-image"
                              />
                              Deepu
                            </div>
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            <p
                              className="activity-text"
                              onClick={activityRegister}
                              style={{ cursor: "pointer" }}
                            >
                              Select Activities
                            </p>
                          </TableCell>
                          <TableCell align="center" style={{ width: "15%" }}>
                            <div className="phc-address phc-general-table">
                              <Form.Group
                                className="facility-details"
                                controlId="exampleForm.MName"
                              >
                                <div
                                  className={`checkbox ${
                                    assignEnable && "checkbox--on"
                                  }`}
                                  onClick={() => setAssignEnable(!assignEnable)}
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
