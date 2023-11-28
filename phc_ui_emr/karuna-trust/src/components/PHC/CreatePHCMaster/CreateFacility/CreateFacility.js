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
import { loadFacilityDropdown } from "../../../../redux/formUtilityAction";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import CancelFacilityModal from "../../Facility/CancelFacilityModal";
import {
  loadAllFacPhc,
  loadAllProcessList,
  loadAllStaffData,
  loadCreateFacilityPHC,
  loadUpdateFac,
} from "../../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import Select from "react-select";
import PageLoader from "../../../PageLoader";
import { useHistory } from "react-router-dom";
import moment from "moment";
import FacilityHead from "./FacilityHead";

let center_type = "Phc";
export default function CreateFacility(props) {
  let dispatch = useDispatch();

  const { staffData } = useSelector((state) => state.phcData);
  const { allProcessList } = useSelector((state) => state.phcData);
  const { facPhcData } = useSelector((state) => state.phcData);

  const phcuuid = sessionStorage.getItem("uuidofphc");

  const [facilityDataId, setFacilityDataId] = useState("");
  const [facilityGetData, setFacilityGetData] = useState([]);
  const [topicsOptions, setTopicsOptions] = useState([]);
  const [isFound1, setIsFound1] = useState(false);

  const [facilityArray, setFacilityArray] = useState([
    {
      facilityId: "",
      name: "",
      code: "",
      description: "",
      head: "",
      status: false,
    },
  ]);
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

    if (facPhcData) {
      let fetchFac = [];
      for (var i = 0; i < facPhcData.length; i++) {
        setLastModDate(facPhcData[0].source?.properties?.dateModified);
        if (facPhcData[i].target?.properties?.status == "ACTIVE") {
          fetchFac.push({
            facId: facPhcData[i].target?.properties?.uuid,
            name: facPhcData[i].target?.properties?.name,
            code: facPhcData[i].target?.properties?.code,
            description: facPhcData[i].target?.properties?.description,
            head: "",
            status: true,
          });
        } else {
          fetchFac.push({
            facId: facPhcData[i].target?.properties?.uuid,
            name: facPhcData[i].target?.properties?.name,
            code: facPhcData[i].target?.properties?.code,
            description: facPhcData[i].target?.properties?.description,
            head: "",
            status: false,
          });
        }
      }
      setFetchFacData(fetchFac);
    }
  }, [allProcessList, facPhcData]);

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
    let list = [];
    if (editFacId) {
      list = [...editData];
    } else {
      list = [...inputList];
    }
    if (type == "toggle") {
      if (editFacId) {
        list[i].status = !editData[i].status;
      } else {
        list[i].status = !inputList[i].status;
      }
      if (editFacId) {
        setEditData(list);
      } else {
        setInputList(list);
      }
    } else if (name == "head") {
      list[i].headUuid = e.target.value;
      {
        staffData.filter((facHead) => {
          if (facHead?.targetNode?.properties.uuid == e.target.value) {
            list[i].head = facHead?.targetNode?.labels[0];
          }
        });
      }
      if (editFacId) {
        setEditData(list);
      } else {
        setInputList(list);
      }
    } else {
      list[i][name] = value;
      if (editFacId) {
        setEditData(list);
      } else {
        setInputList(list);
      }
    }
  };

  const [phcEnable1, setPhcEnable1] = useState(false);
  const handleCheckBox = () => {
    setPhcEnable1(!phcEnable1);
  };

  const removevitalstorage = () => {
    window.history.back();
  };

  useEffect(() => {
    (document.title = "EMR Super Admin Create Facility"),
      dispatch(loadFacilityDropdown());
    dispatch(loadAllProcessList());
    dispatch(loadAllStaffData(phcuuid));
    dispatch(loadAllFacPhc(phcuuid, setPageLoader));
  }, [phcuuid, setPageLoader]);

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
            phc: phcuuid,
          });
        } else if (items.status == false) {
          postData.push({
            name: items.name,
            code: items.code,
            description: items.description,
            status: "INACTIVE",
            phc: phcuuid,
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
              loadCreateFacilityPHC(
                postResponse,
                sourceData,
                facHeadUuid,
                setPageLoader,
                status
              )
            );
          } else {
            dispatch(
              loadCreateFacilityPHC(
                postResponse,
                sourceData,
                facHeadUuid,
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
      loadUpdateFac(facId, postResponse, setPageLoader, phcuuid, center_type)
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
    editData.map((items) => {
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
        phcuuid,
        center_type,
        setEditFacId
      )
    );
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
      <CancelFacilityModal
        cancelFacilityWindow={cancelFacilityWindow}
        facilityDetailClose={facilityDetailClose}
        removevitalstorage={removevitalstorage}
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
                  onClick={removevitalstorage}
                >
                  PHC Configuration
                </Breadcrumb.Item>
                <Breadcrumb.Item active className="phc-breadcrumb">
                  Create Facility Master
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div>
              <div className="form-col">
                <Form className="super-admin-form">
                  <Row className="super-complaint">
                    <Col lg={6}>
                      <h3 className="super-config-details">
                        Create Facility Master
                      </h3>
                      <p className="total-phc">
                        <i className="bi bi-info-square-fill add-phc-icon"></i>
                        <b>Enter/Add</b> details about the facility available in
                        the PHC
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

                        {/* {facPhcData && */}
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
                        {/* } */}
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
                    {lastModDate && (
                      <p className="total-modify">
                        Last Modified :{" "}
                        {moment(lastModDate).format("DD-MM-YYYY")}
                      </p>
                    )}
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
