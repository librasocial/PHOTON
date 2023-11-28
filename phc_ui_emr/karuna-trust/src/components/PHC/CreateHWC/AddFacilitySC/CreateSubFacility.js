import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Breadcrumb } from "react-bootstrap";
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
import momemt from "moment";
import { ToastContainer } from "react-toastify";
import CancelSubFacilityModal from "./CancelSubFacilityModal";
import { useDispatch, useSelector } from "react-redux";
import { loadFacilityDropdown } from "../../../../redux/formUtilityAction";
import {
  loadAllFacSC,
  loadAllProcessList,
  loadAllStaffData,
  loadCreateFacilitySC,
  loadUpdateFac,
} from "../../../../redux/phcAction";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import PageLoader from "../../../PageLoader";
import { useHistory } from "react-router-dom";
import FacilityHead from "../../CreatePHCMaster/CreateFacility/FacilityHead";

let suCenterId;
let center_type = "Sub-Center";
export default function CreateSubFacility(props) {
  let dispatch = useDispatch();
  const { staffData } = useSelector((state) => state.phcData);
  const { allProcessList } = useSelector((state) => state.phcData);
  const { sunCenterDetails } = useSelector((state) => state.phcData);
  const { facSCData } = useSelector((state) => state.phcData);

  const phcuuid = sessionStorage.getItem("uuidofphc");

  const [facilityDataId, setFacilityDataId] = useState("");
  const [facilityGetData, setFacilityGetData] = useState([]);
  const [isFound1, setIsFound1] = useState(false);

  // const [facilityArray, setFacilityArray] = useState([{ facilityId: "", facilityName: "", facilityCode: "", facilityDesc: "", facilityHead: "", phcEnable: false, facilityAction: "" }])
  const [inputList, setInputList] = useState([
    {
      facilityId: "",
      name: "",
      code: "",
      description: "",
      head: "",
      headUuid: "",
      status: false,
    },
  ]);

  const [fetchFacData, setFetchFacData] = useState([]);
  const [lastModDate, setLastModDate] = useState([]);

  useEffect(() => {
    if (allProcessList) {
      let list = [];
      for (var i = 0; i < allProcessList.length; i++) {
        list.push({
          facilityId: allProcessList[i]?.targetNode?.properties?.uuid,
          name: allProcessList[i]?.targetNode?.properties?.name,
          code: "",
          description: "",
          head: "",
          status: false,
        });
      }
      setInputList(list);
    }

    if (facSCData) {
      let fetchFac = [];
      for (var i = 0; i < facSCData.length; i++) {
        setLastModDate(facSCData[0].source?.properties?.dateModified);
        if (facSCData[i].target?.properties?.status == "ACTIVE") {
          fetchFac.push({
            facId: facSCData[i].target?.properties?.uuid,
            name: facSCData[i].target?.properties?.name,
            code: facSCData[i].target?.properties?.code,
            description: facSCData[i].target?.properties?.description,
            head: "",
            status: true,
          });
        } else {
          fetchFac.push({
            facId: facSCData[i].target?.properties?.uuid,
            name: facSCData[i].target?.properties?.name,
            code: facSCData[i].target?.properties?.code,
            description: facSCData[i].target?.properties?.description,
            head: "",
            status: false,
          });
        }
      }
      setFetchFacData(fetchFac);
    }

    if (sunCenterDetails?.properties?.uuid) {
      suCenterId = sunCenterDetails?.properties?.uuid;
    }
  }, [allProcessList, facSCData, sunCenterDetails]);

  useEffect(() => {
    if (fetchFacData) {
      removeDublicateData(fetchFacData, inputList);
    }
  }, [fetchFacData]);

  const removeDublicateData = () => {
    let empltyList = [...inputList];
    let list = empltyList.filter((person) =>
      fetchFacData.every((person2) => !person2.name.includes(person.name))
    );
    setInputList(list);
  };

  const [pageLoader, setPageLoader] = useState(true);
  let type = "toggle";
  const handleInputChange = (e, i, type) => {
    const { name, value } = e.target;
    const list = [...inputList];
    if (type == "toggle") {
      list[i].status = !inputList[i].status;
      setInputList(list);
    } else if (name == "head") {
      list[i].headUuid = e.target.value;
      {
        staffData.filter((facHead) => {
          if (facHead?.targetNode?.properties.uuid == e.target.value) {
            list[i].head = facHead?.targetNode?.labels[0];
          }
        });
      }
      setInputList(list);
    } else {
      list[i][name] = value;
      setInputList(list);
    }
  };

  const [phcEnable1, setPhcEnable1] = useState(false);
  const handleCheckBox = () => {
    setPhcEnable1(!phcEnable1);
  };

  useEffect(() => {
    (document.title = "EMR Super Admin Create Facility"),
      dispatch(loadFacilityDropdown());
    dispatch(loadAllProcessList());
    dispatch(loadAllStaffData(phcuuid));
    if (suCenterId) {
      dispatch(loadAllFacSC(suCenterId, setPageLoader));
    }
  }, [phcuuid, suCenterId]);

  const removevitalstorege = () => {
    window.history.back();
  };

  function saveFacility() {
    setPageLoader(true);
    let postData = [];

    inputList.map((items) => {
      if (items.name && items.code && items.description && items.head) {
        if (items.status == true) {
          postData.push({
            name: items.name,
            code: items.code,
            description: items.description,
            status: "ACTIVE",
            subCenter: suCenterId,
          });
        } else if (items.status == false) {
          postData.push({
            name: items.name,
            code: items.code,
            description: items.description,
            status: "INACTIVE",
            subCenter: suCenterId,
          });
        }
      }
    });

    for (var i = 0; i < postData.length; i++) {
      for (var j = 0; j < inputList.length; j++) {
        if (postData[i].name == inputList[j].name) {
          var postFacility = {
            type: "Facility",
            properties: postData[i],
          };

          let facHeadUuid = inputList[j].headUuid;
          let sourceData = {
            type: inputList[j].head,
            properties: {
              uuid: inputList[j].headUuid,
            },
          };
          var postResponse = {
            headers: serviceHeaders.myHeaders1,
            method: "POST",
            mode: "cors",
            body: JSON.stringify(postFacility),
          };

          if (i == postData.length - 1) {
            let status = "sucess";
            dispatch(
              loadCreateFacilitySC(
                postResponse,
                sourceData,
                facHeadUuid,
                suCenterId,
                setPageLoader,
                status
              )
            );
          } else {
            dispatch(
              loadCreateFacilitySC(
                postResponse,
                sourceData,
                facHeadUuid,
                suCenterId,
                setPageLoader
              )
            );
          }
        }
      }
    }
  }

  const updateFacStatus = (e, i, facId) => {
    const list = [...fetchFacData];

    let status;
    if (fetchFacData[i].status == true) {
      status = "INACTIVE";
    } else if (fetchFacData[i].status == false) {
      status = "ACTIVE";
    }

    list[i].status = !fetchFacData[i].status;
    setFetchFacData(list);
    setPageLoader(true);

    var updateFac = {
      type: "Facility",
      properties: {
        status: status,
      },
    };

    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateFac),
    };

    dispatch(
      loadUpdateFac(facId, postResponse, setPageLoader, suCenterId, center_type)
    );
  };

  const [editFacId, setEditFacId] = useState("");
  const [editData, setEditData] = useState("");
  function editFacility(e, i, facId) {
    setEditFacId(facId);
    let editFacData = fetchFacData.filter((item) => {
      if (item.facId == facId) {
        return [
          {
            facilityId: item.facId,
            name: item.name,
            code: item.caode,
            description: item.description,
            head: "",
            headUuid: "",
            status: item.status,
          },
        ];
      }
    });
    setEditData(editFacData);
  }

  let displayInput;
  if (editFacId) {
    displayInput = editData;
  } else {
    displayInput = inputList;
  }

  const cancelUpdate = () => {
    setEditFacId("");
    removeDublicateData(fetchFacData, inputList);
  };

  const updateFacility = (editFacId) => {
    let postData;
    inputList.map((items) => {
      if (items.status == true) {
        postData = {
          name: items.name,
          code: items.code,
          description: items.description,
          status: "ACTIVE",
        };
      } else if (items.status == false) {
        postData = {
          name: items.name,
          code: items.code,
          description: items.description,
          status: "INACTIVE",
        };
      }
    });
    setPageLoader(true);

    var updateFac = {
      type: "Facility",
      properties: postData,
    };

    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateFac),
    };

    dispatch(
      loadUpdateFac(
        editFacId,
        postResponse,
        setPageLoader,
        suCenterId,
        center_type,
        setEditFacId
      )
    );
    // setPageLoader(true);
  };

  const [cancelFacilityWindow, setCancelFacilityWindow] = useState(false);
  const facilityDetailClose = () => {
    setCancelFacilityWindow(false);
  };

  const resetFacility = () => setCancelFacilityWindow(true);
  let history = useHistory();
  const removeCurrentPage = () => {
    history.go(-2);
  };

  return (
    <React.Fragment>
      <ToastContainer />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <CancelSubFacilityModal
        cancelFacilityWindow={cancelFacilityWindow}
        facilityDetailClose={facilityDetailClose}
        removevitalstorege={removevitalstorege}
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
                  PHC Configuration
                </Breadcrumb.Item>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removevitalstorege}
                >
                  Select SC/HWCS to Create Facility Masters
                </Breadcrumb.Item>
                <Breadcrumb.Item active className="phc-breadcrumb">
                  Create Facility Masters for{" "}
                  {sunCenterDetails?.properties?.name}
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div>
              <div className="form-col">
                <Form className="super-admin-form">
                  <Row className="super-complaint">
                    <Col lg={6}>
                      <h3 className="super-config-details">
                        Create Facility Master for{" "}
                        {sunCenterDetails?.properties?.name}
                      </h3>
                      <p className="total-phc">
                        <i className="bi bi-info-square-fill add-phc-icon"></i>
                        <b>Enter/Add</b> details about the facility present in
                        the SC/HWC
                      </p>
                    </Col>
                    <Col lg={6}></Col>
                  </Row>
                  <Paper
                    sx={{ width: "100%", overflow: "hidden" }}
                    className="stock-available-table"
                  >
                    <TableContainer sx={{ maxHeight: 440 }}>
                      <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                          <TableRow>
                            <TableCell align="left" style={{ width: "15%" }}>
                              Facility Name
                            </TableCell>
                            <TableCell align="left" style={{ width: "10%" }}>
                              Facility Code
                            </TableCell>
                            <TableCell align="left" style={{ width: "30%" }}>
                              Description About the Facility
                            </TableCell>
                            <TableCell align="left" style={{ width: "20%" }}>
                              Facility Head
                            </TableCell>
                            <TableCell align="center" style={{ width: "25%" }}>
                              Enable/Disable Facility
                            </TableCell>
                            <TableCell align="center" style={{ width: "15%" }}>
                              Edit
                            </TableCell>
                          </TableRow>
                        </TableHead>
                        {!editFacId && (
                          <TableBody>
                            {fetchFacData.map((facItem, i) => (
                              <TableRow key={i}>
                                <TableCell
                                  align="left"
                                  style={{ width: "15%" }}
                                >
                                  {facItem.name}
                                </TableCell>
                                <TableCell
                                  align="left"
                                  style={{ width: "10%" }}
                                >
                                  {facItem.code}
                                </TableCell>
                                <TableCell
                                  align="left"
                                  style={{ width: "30%" }}
                                >
                                  {facItem.description}
                                </TableCell>
                                <FacilityHead
                                  facId={facItem.facId}
                                  setPageLoader={setPageLoader}
                                />
                                <TableCell
                                  align="center"
                                  style={{ width: "25%" }}
                                >
                                  <div className="phc-address phc-general-table">
                                    <Form.Group className="facility-details">
                                      <div
                                        className={`checkbox ${
                                          facItem?.status && "checkbox--on"
                                        }`}
                                        onClick={(e) =>
                                          updateFacStatus(e, i, facItem?.facId)
                                        }
                                      >
                                        <div className="checkbox__ball">
                                          <span className="status-facility-text">
                                            {facItem?.status == false
                                              ? "Disabled"
                                              : "Enabled"}
                                          </span>
                                        </div>
                                      </div>
                                    </Form.Group>
                                  </div>
                                </TableCell>
                                <TableCell
                                  align="center"
                                  style={{ width: "15%" }}
                                >
                                  <Button variant="link">
                                    <i
                                      className="fa fa-pencil pen-facility"
                                      onClick={(e) =>
                                        editFacility(e, i, facItem?.facId)
                                      }
                                    ></i>
                                  </Button>
                                </TableCell>
                              </TableRow>
                            ))}
                          </TableBody>
                        )}
                        <TableBody>
                          {displayInput.map((x, i) => {
                            return (
                              <TableRow key={i}>
                                <TableCell
                                  align="left"
                                  style={{ width: "15%" }}
                                >
                                  {x.name}
                                </TableCell>
                                <TableCell
                                  align="left"
                                  style={{ width: "10%" }}
                                >
                                  <Form.Control
                                    type="text"
                                    className="presription-input"
                                    placeholder="Enter"
                                    name="code"
                                    value={x.code}
                                    onChange={(e) => handleInputChange(e, i)}
                                  />
                                </TableCell>
                                <TableCell
                                  align="left"
                                  style={{ width: "30%" }}
                                >
                                  <Form.Control
                                    type="text"
                                    className="presription-input"
                                    placeholder="Enter"
                                    name="description"
                                    value={x.description}
                                    onChange={(e) => handleInputChange(e, i)}
                                  />
                                </TableCell>
                                {!editFacId ? (
                                  <TableCell
                                    align="left"
                                    style={{ width: "20%" }}
                                  >
                                    <Form.Select
                                      aria-label="Default select example"
                                      placeholder="Select State"
                                      name="head"
                                      value={x.headUuid || ""}
                                      onChange={(e) => handleInputChange(e, i)}
                                      className="table-drop-down"
                                    >
                                      <option value="" disabled hidden>
                                        Select
                                      </option>
                                      {staffData &&
                                        staffData.map((facHead, i) => (
                                          <React.Fragment key={i}>
                                            <option
                                              value={
                                                facHead?.targetNode?.properties
                                                  .uuid
                                              }
                                            >
                                              {facHead?.targetNode?.properties
                                                .name +
                                                "(" +
                                                facHead?.targetNode?.labels[0] +
                                                ")"}
                                            </option>
                                          </React.Fragment>
                                        ))}
                                    </Form.Select>
                                  </TableCell>
                                ) : (
                                  <FacilityHead
                                    facId={x.facId}
                                    setPageLoader={setPageLoader}
                                  />
                                )}
                                <TableCell
                                  align="center"
                                  style={{ width: "25%" }}
                                >
                                  <div className="phc-address phc-general-table">
                                    <Form.Group className="facility-details">
                                      <div
                                        className={`checkbox ${
                                          x.status && "checkbox--on"
                                        }`}
                                        name="status"
                                        onClick={(e) =>
                                          handleInputChange(e, i, type)
                                        }
                                      >
                                        <div className="checkbox__ball">
                                          <span className="status-facility-text">
                                            {x.status == false
                                              ? "Disabled"
                                              : "Enabled"}
                                          </span>
                                        </div>
                                      </div>
                                    </Form.Group>
                                  </div>
                                </TableCell>
                                <TableCell
                                  align="center"
                                  style={{ width: "15%" }}
                                >
                                  <Button
                                    variant="link"
                                    style={{
                                      opacity: "0.6",
                                      cursor: "context-menu",
                                    }}
                                  >
                                    <i className="fa fa-pencil pen-facility"></i>
                                  </Button>
                                </TableCell>
                              </TableRow>
                            );
                          })}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  </Paper>
                  <div className="total-modify-facility">
                    <p className="total-modify">
                      Last Modified : {momemt(lastModDate).format("DD-MM-YYYY")}
                    </p>
                  </div>
                  <div className="profession-assign-btn">
                    <div className="save-btn-section">
                      {editFacId ? (
                        <SaveButton
                          butttonClick={cancelUpdate}
                          class_name="regBtnPC"
                          button_name="Cancel"
                        />
                      ) : (
                        <SaveButton
                          butttonClick={resetFacility}
                          class_name="regBtnPC"
                          button_name="Cancel"
                        />
                      )}
                    </div>
                    <div className="save-btn-section">
                      {editFacId ? (
                        <SaveButton
                          butttonClick={(e) => updateFacility(editFacId)}
                          class_name="regBtnN"
                          button_name="Update"
                        />
                      ) : (
                        <SaveButton
                          butttonClick={saveFacility}
                          class_name="regBtnN"
                          button_name="Save"
                        />
                      )}
                    </div>
                  </div>
                </Form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
