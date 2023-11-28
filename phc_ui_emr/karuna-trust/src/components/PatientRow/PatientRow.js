import React, { useState } from "react";
import {
  Button,
  Col,
  Row,
  Image,
  Dropdown,
  Form,
  ButtonGroup,
} from "react-bootstrap";
import SignedHealthIDImage from "../Dashboard/SignedHealthIDImage";
import ModalPopups from "../EMR/ModalPopups/ModalPopups";
import moment from "moment";
import { Link } from "react-router-dom";
import { loadSinglePatient } from "../../redux/actions";
import { useDispatch } from "react-redux";

function PatientRow(props) {
  let dispatch = useDispatch();
  const [types, setTypes] = useState("patient");
  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };

  // more details modal box
  const [patientdt, setPatientdt] = useState("");
  const [showd, setShowd] = useState(false);
  const handShow = () => {
    setShowd(true);
  };

  const closeField = () => {
    setShowd(false);
  };

  // registering resident data
  const [isLoading, setLoading] = useState(false);
  const Registeraspateint = (e) => {
    // setOpenregister(true);
    props.setUuidcitizenid(e);
    props.handleClose(true);
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      props.setShowModal(true);
    }, 2500);
    props.setUuuid(e);
  };
  // registering resident data

  return (
    <div>
      {/* loader */}
      {isLoading && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <h3 className="load-header">Receiving details</h3>
            </div>
            <br></br>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="../img/circle.png"
              />
            </div>
            <br />
            <div>
              <p className="">
                Please wait while we receive details for the selected user
              </p>
            </div>
          </div>
        </div>
      )}
      {/* loader */}
      {/* modal */}
      <ModalPopups
        isPatientDetailsShow={showd}
        patDetailsClose={closeField}
        paData={patientdt}
      />
      {/* modal */}
      <div className="box-dsg1">
        <Row id="pat_que3">
          <Col md={1} align="center" className="d-flex justify-content-center">
            <SignedHealthIDImage healthID={props.healthId} />
          </Col>
          <Col md={props.page == "view-modal" ? 5 : 3} align="center">
            <div className="dataName">
              {props.fullName ? (
                <div>
                  <p className="dataP">
                    <b>{props.fullName}</b>
                  </p>
                </div>
              ) : (
                <div>
                  <p className="dataP">
                    <b>
                      {props.salutation}
                      {props.salutation && "."} {props.firstName}{" "}
                      {props.middleName} {props.lastName}
                    </b>
                  </p>
                </div>
              )}
              <div>
                <p style={{ paddingTop: "1%" }}>UHID: {props.UHId}</p>
              </div>
              <div>
                <p className="dataP" style={{ paddingTop: "1%" }}>
                  Health ID:&nbsp;
                  {!props.healthId ? (
                    <em style={{ color: "red" }}>&nbsp;Not Available</em>
                  ) : (
                    <>
                      {props.healthId.replace(
                        /(\d{2})(\d{4})(\d{4})(\d{4})/,
                        "$1-$2-$3-$4"
                      )}
                    </>
                  )}
                </p>
              </div>
            </div>
          </Col>
          <Col md={1} id="pat_age">
            <div className="dataGender" align="center">
              <div>
                <p>{props.gender}</p>
              </div>
            </div>
          </Col>
          <Col md={2} id="pat_age">
            <div>
              <p className="pateintdataforfontsize"> {props.age} Years</p>
              <p id="pat_year" className="pateintdataforfontsize">
                {props.dateOfBirth ? (
                  <>
                    {moment(new Date(props.dateOfBirth)).format("DD MMM YYYY")}
                  </>
                ) : (
                  ""
                )}
              </p>
            </div>
          </Col>
          <Col md={3} id="pat_age">
            <div className="data-caste" align="center">
              <p>{!props.mobile ? "" : <>+91-{props.mobile}</>}</p>
            </div>
          </Col>
          {props.page != "view-modal" && (props._id || props.id) && (
            <Col md={2} align="center" className="dot-div visit-dropdown">
              <div className="btn-group dotBtn-group" align="center">
                <button
                  type="button"
                  className="btn btn-primary dotBtn"
                  data-toggle="dropdown"
                >
                  <span className="dot"></span>
                  <span className="dot"></span>
                  <span className="dot"></span>
                </button>
                <div className="dropdown-menu" id="show">
                  {props._id ? (
                    <div className="dot-dropdown-menu">
                      <Link
                        style={{ color: "black", textDecoration: "none" }}
                        to={`/createEncounter/${props._id}/${types}`}
                      >
                        <p className="dropdown-item">Create Visit</p>
                      </Link>
                      <p
                        className="dropdown-item"
                        data-toggle="modal"
                        data-target="#PatientDetails-wndow"
                        onClick={() => showptdtls(props._id)}
                      >
                        More details
                      </p>
                      <Link
                        style={{ color: "black", textDecoration: "none" }}
                        to={`/searchpatient/${props._id}`}
                      >
                        <p className="dropdown-item">
                          Edit registration details
                        </p>
                      </Link>
                    </div>
                  ) : (
                    <div className="dot-dropdown-menu">
                      <p
                        className="dropdown-item"
                        data-toggle="modal"
                        data-target="#PatientDetails-wndow"
                        onClick={() => showptdtls(props.id)}
                      >
                        More details
                      </p>
                    </div>
                  )}
                </div>
              </div>
            </Col>
          )}
          {props.pat_uuid && (
            <Col md={2} id="pat_age">
              <div className="regbtn" align="center">
                <div>
                  {props.regStatus == "true" ? (
                    <p style={{ color: "#eb5811", fontWeight: "700" }}>
                      Registered
                    </p>
                  ) : (
                    <p onClick={() => Registeraspateint(props.pat_uuid)}>
                      Register
                    </p>
                  )}
                </div>
              </div>
            </Col>
          )}
        </Row>
      </div>
    </div>
  );
}

export default PatientRow;
