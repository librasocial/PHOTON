import React, { useState, useRef, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Modal,
  InputGroup,
  Popover,
  OverlayTrigger,
  Tooltip,
} from "react-bootstrap";
import SaveButton from "../../EMR_Buttons/SaveButton";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from "@mui/material";
import { NavLink } from "react-router-dom";
import PHCCreate from "./PHCCreate";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import {
  loadGetPhcDetails,
  loadUpdatePHC,
  loadAllGramPanchayatPhc,
} from "../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import PageLoader from "../../PageLoader";
import FileUpload from "../FileUpload/FileUpload";

let center_type = "Phc";
export default function ManagePHC(props) {
  let dispatch = useDispatch();
  const phcUuid = sessionStorage.getItem("uuidofphc");

  const { phcDetails } = useSelector((state) => state.phcData);
  const { gramPanchayatListPhc } = useSelector((state) => state.phcData);
  let sortedGramPhc;
  if (gramPanchayatListPhc) {
    sortedGramPhc = gramPanchayatListPhc.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }

  let phcData;
  if (phcDetails) {
    phcData = phcDetails?.properties;
  }

  const [imgSrc, setImgSrc] = useState(null);
  const inputFile = useRef(null);
  const startCam = (editId) => {
    setImageShow(true);
    setImgText(false);
    setEditUuid(editId);
  };
  const [imageShow, setImageShow] = useState(false);
  const [imgText, setImgText] = useState(false);
  const [imageChange, setImageChange] = useState(false);

  const handleImageClose = () => {
    setImageShow(false);
  };

  const onButtonClick = (editId) => {
    setImageShow(true);
    setImgText(true);
    setUpload(false);
    setEditUuid(editId);
  };
  const changeImage = (editId) => {
    setImageShow(true);
    setImageChange(true);
    setEditUuid(editId);
  };
  const [pageLoader, setPageLoader] = useState(false);

  useEffect(() => {
    dispatch(loadGetPhcDetails(phcUuid, setPageLoader));
    dispatch(loadAllGramPanchayatPhc(phcUuid));
  }, [phcUuid, setPageLoader]);

  // File Upload
  const [upload, setUpload] = useState(false);

  const [phcDetails1, setPhcDetails1] = useState(false);

  const removeImage = (e) => {
    setPageLoader(true);
    let updateData = {
      type: "Phc",
      properties: {
        photo: "",
      },
    };
    upDatePhcData(e, updateData);
  };

  const setUpdateStatus = (e) => {
    setPageLoader(true);
    setPhcDetails1(!phcDetails1);
    let status;
    if (!phcDetails1 == true) {
      status = "ACTIVE";
    } else {
      status = "INACTIVE";
    }
    let updateData = {
      type: "Phc",
      properties: {
        status: status,
      },
    };

    upDatePhcData(e, updateData);
  };

  function upDatePhcData(e, updateData) {
    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateData),
    };

    dispatch(loadUpdatePHC(e, postResponse, setPageLoader));
  }

  useEffect(() => {
    if (phcData?.status == "ACTIVE") {
      setPhcDetails1(true);
    } else if (phcData?.status == "INACTIVE") {
      setPhcDetails1(false);
    }
  }, [phcData]);
  const [phcShow, setPhcShow] = useState(false);

  const handlePhcClose = () => {
    setPhcShow(false);
  };

  const [editUuid, setEditUuid] = useState("");
  const createPHC = () => setPhcShow(true);
  const editPHC = (editId) => {
    setEditUuid(editId);
    setPhcShow(true);
  };

  return (
    <React.Fragment>
      {/* loader */}
      {/* {pageLoader == true && <PageLoader />} */}
      {/* loader */}
      {phcShow && (
        <PHCCreate
          phcShow={phcShow}
          handlePhcClose={handlePhcClose}
          editUuid={editUuid}
          phcDetails={phcDetails}
          setPhcShow={setPhcShow}
        />
      )}
      {imageShow && (
        <FileUpload
          imageShow={imageShow}
          imgText={imgText}
          setImgText={setImgText}
          upload={upload}
          setUpload={setUpload}
          editUuid={editUuid}
          setEditUuid={setEditUuid}
          handleImageClose={handleImageClose}
          phcData={center_type == "Phc" && phcData}
          imageChange={imageChange}
          setImageChange={setImageChange}
          center_type={center_type}
        />
      )}
      <div className="form-col">
        <Form className="super-admin-form">
          <h3 className="super-config-details">Manage PHC Profile</h3>
          <Row>
            <Col lg={2}>
              <h3 className="super-config-photo">Upload PHC Photo</h3>
              <div className="manage-phc">
                {phcData?.photo != "" ? (
                  <div>
                    <div className="image-div">
                      <img
                        src={phcData?.photo || ""}
                        style={{ height: "150px", width: "400px" }}
                      />
                    </div>
                    <Row className="image-action-btn">
                      <Col md={6}>
                        <div
                          onClick={(e) => removeImage(phcData?.uuid)}
                          className="updateImage"
                        >
                          Remove
                        </div>
                      </Col>
                      <Col md={6}>
                        <div
                          onClick={(e) => changeImage(phcData?.uuid)}
                          className="updateImage"
                        >
                          Change
                        </div>
                      </Col>
                    </Row>
                  </div>
                ) : (
                  <div>
                    <img
                      src="../img/super/facility.png"
                      className="user-login-img"
                    />
                    <Button
                      className="regCapture"
                      onClick={(e) => startCam(phcData.uuid)}
                      style={{ cursor: "pointer" }}
                    >
                      Capture
                    </Button>

                    <div>
                      <p style={{ marginTop: "6px" }}>or</p>
                      <p
                        className="uploadphc"
                        onClick={(e) => onButtonClick(phcData.uuid)}
                        style={{ cursor: "pointer" }}
                      >
                        Upload from Computer
                      </p>
                    </div>
                  </div>
                )}
              </div>
            </Col>
            <Col lg={10}>
              {!phcDetails ? (
                <div align="center">
                  <img src="../img/super/ambulance.png" />
                  <div className="create-new-phc">
                    <SaveButton
                      butttonClick={createPHC}
                      class_name="regBtnN"
                      button_name="Enter PHC Details"
                    />
                  </div>
                </div>
              ) : (
                <>
                  {phcData ? (
                    <div className="phd-details-div">
                      <Row id="phc_que">
                        <Col md={6}>
                          <h3 className="super-config-details">
                            {phcData?.name}
                          </h3>
                        </Col>
                        <Col md={6} align="center">
                          {/* <p className="total-population">
                                                    Total population Covered under this PHC &nbsp; : <b> &nbsp; 15,283 </b>
                                                </p> */}
                        </Col>
                      </Row>
                      <div>
                        <p className="phc-address">
                          <b>Code &nbsp; :</b>&nbsp; {phcData.code}
                        </p>
                        <p className="phc-address">
                          <b>Description &nbsp; :</b>&nbsp;{" "}
                          {phcData.description}
                        </p>
                        <p className="phc-address">
                          <b>Address &nbsp; :</b>&nbsp; {phcData.addressLine}
                        </p>
                        <p className="phc-address">
                          <b>Contact &nbsp; :</b>&nbsp; {phcData.contact}
                        </p>
                        <p className="phc-address">
                          <b>E-Mail ID &nbsp; :</b>&nbsp; {phcData.email}
                        </p>
                        <p className="phc-address">
                          <b>PHC Type &nbsp; :</b>&nbsp; Rural PHC
                        </p>
                        <p className="phc-address">
                          <span>
                            <b>
                              Associated Gram Panchayats &nbsp; :&nbsp;&nbsp;{" "}
                            </b>
                          </span>
                          <span className="gram-list">
                            {gramPanchayatListPhc &&
                              gramPanchayatListPhc.map((panchayat, i) => (
                                <span
                                  key={i}
                                  style={{ wordWrap: "break-word" }}
                                >
                                  {panchayat?.target?.properties.name},&nbsp;
                                </span>
                              ))}
                          </span>
                        </p>
                        <div className="phc-address phc-general">
                          <b>PHC Status &nbsp; :</b>&nbsp;
                          <Form.Group
                            className="rphc-details"
                            controlId="exampleForm.MName"
                          >
                            <div
                              className={`checkbox ${
                                phcDetails1 && "checkbox--on"
                              }`}
                              onClick={(e) =>
                                setUpdateStatus(phcDetails?.properties?.uuid)
                              }
                            >
                              <div className="checkbox__ball">
                                <span className="status-phc-text">
                                  {phcDetails1 == false
                                    ? "In-Active"
                                    : "Active"}
                                </span>
                              </div>
                            </div>
                          </Form.Group>
                        </div>
                        <div className="create-new-phc">
                          <SaveButton
                            butttonClick={(e) =>
                              editPHC(phcDetails?.properties?.uuid)
                            }
                            class_name="regBtnN"
                            button_name="Edit PHC Details"
                          />
                        </div>
                      </div>
                    </div>
                  ) : (
                    ""
                  )}
                </>
              )}
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}
