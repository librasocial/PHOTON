import React from "react";
import { Col, Button, Card, Row, Modal } from "react-bootstrap";
import moment from "moment";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import "./ModalPopups.css";
import { useDispatch, useSelector } from "react-redux";
import { updatePatDet } from "../../../redux/actions";
import SaveButton from "../../EMR_Buttons/SaveButton";
import PatientRow from "../../PatientRow/PatientRow";

export default function ModalPopups(props) {
  const phc = sessionStorage.getItem("phc");
  const { singlePatDet } = useSelector((state) => state.data);
  const { singlePatientDetails } = useSelector((state) => state.data);

  const patientdt = singlePatientDetails;
  const cancelpateintid = props.cancelpaId;

  let dispatch = useDispatch();
  let newDate = new Date();
  let thisyear = moment(newDate).format("YYYY");
  const check_In_Data = singlePatDet;
  const dt_of_birth = moment(singlePatDet?.Patient?.dob).format("DD-MM-YYYY");
  const pt_age = moment(singlePatDet?.Patient?.dob).format("YYYY");
  const current_age = thisyear - pt_age;
  let newyear = thisyear - props.checkInData?.Patient?.age;
  const date_of_birth = "01-01-" + newyear;

  let parsedDate = moment(new Date(), "DD.MM.YYYY H:mm:ss");
  // set state for cancel visit modal
  const cancelvisit = (e) => {
    const pateintid = e;
    const CancelPatient = {
      reservationTime: parsedDate.toISOString(),
      status: "Cancelled",
    };
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(CancelPatient),
    };
    dispatch(updatePatDet(pateintid, requestOptions1, props.cancelVisitClose));
  };
  // set state for cancel visit modal
  const startcheckin = (e) => {
    let reservationidofpateint = e;
    const checkindata = {
      status: "CheckedIn",
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(checkindata),
    };
    dispatch(
      updatePatDet(reservationidofpateint, requestOptions1, props.setShowModal)
    );
  };

  return (
    <React.Fragment>
      {/*modal-1 modal for check in consultation */}
      <Modal
        show={props.isCheckInShow}
        onHide={props.checkInClose}
        className="check-In-modal-div"
      >
        <Row>
          <Col md={6} xs={9}>
            <h1 className="patient-details-header">Patient details</h1>
          </Col>
          <Col md={6} xs={3} align="right">
            <button
              onClick={props.checkInClose}
              className="bi bi-x close-popup"
            ></button>
          </Col>
        </Row>

        <React.Fragment>
          <Row>
            <Col md={12} className="dataFlr">
              <div>
                <PatientRow
                  healthId={check_In_Data?.Patient?.healthId}
                  fullName={check_In_Data?.Patient?.name}
                  UHId={check_In_Data?.Patient?.UHId}
                  gender={check_In_Data?.Patient?.gender}
                  age={!dt_of_birth ? check_In_Data?.Patient?.age : current_age}
                  dateOfBirth={!dt_of_birth ? date_of_birth : !dt_of_birth}
                  mobile={check_In_Data?.Patient?.phone}
                />
              </div>
            </Col>
          </Row>
          <div className="check-in-text" align="center">
            Above patient details will be enabled for Medical officer to further
            reference.
            <br />
            Are you sure you want to send for consultation?
          </div>
          <div className="check-in-buttons">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={props.checkInClose}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              <SaveButton
                butttonClick={() => startcheckin(check_In_Data?._id)}
                class_name="regBtnN"
                button_name="Yes"
              />
            </div>
          </div>
        </React.Fragment>
      </Modal>
      {/*modal-1 modal for check in consultation */}

      {/*modal-2 modal for cancel visit */}
      <Modal show={props.isCancelVisitShow} onHide={props.cancelVisitClose}>
        <Row>
          <Col align="center">
            <h4 className="logout-head">Cancel Visit ?</h4>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="log-text">
              Are you sure, you want to cancel patient registration?
              <br />
              All the data will be lost.
            </p>
          </Col>
        </Row>
        <Row>
          <Col className="btn1">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={props.cancelVisitClose}
                class_name="regBtnN"
                button_name="No, continue"
              />
            </div>
          </Col>
          <Col className="btn2">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={() => cancelvisit(cancelpateintid)}
                class_name="regBtnPC"
                button_name="Yes, Cancel"
              />
            </div>
          </Col>
        </Row>
      </Modal>
      {/*modal-2 cancel button model box */}

      {/* modal-3 patient more details modal box */}
      {props.isPatientDetailsShow == true ? (
        <div>
          <div
            id="PatientDetails-wndow"
            className="modal fade modaldetails "
            tabIndex="-1"
          >
            <div className="modal-dialog pateint-details-windowiwidth">
              <div className="modal-content pb-3 pt-2 details-design">
                <Row>
                  <Col md={10}>
                    <div>
                      <h1 className="patient-details-header">
                        Patient details
                      </h1>
                    </div>
                  </Col>
                  <Col md={2}>
                    <div>
                      <button
                        onClick={props.patDetailsClose}
                        className="bi bi-x close-btn"
                      ></button>
                    </div>
                  </Col>
                </Row>
                <React.Fragment>
                  <div className="pat-mor-details">
                    <PatientRow
                      healthId={patientdt?.citizen?.healthId}
                      salutation={patientdt?.citizen?.salutation}
                      firstName={patientdt?.citizen?.firstName}
                      middleName={patientdt?.citizen?.middleName}
                      lastName={patientdt?.citizen?.lastName}
                      UHId={patientdt?.UHId}
                      gender={patientdt?.citizen?.gender}
                      age={patientdt?.citizen?.age}
                      dateOfBirth={patientdt?.citizen?.dateOfBirth}
                      mobile={patientdt?.citizen?.mobile}
                    />
                  </div>
                  <div className="pat-mor-details">
                    <Row>
                      <Col md={6}>
                        <div className="d-flex">
                          <i className="bi bi-geo-alt"></i>
                          <span>
                            <b>Present address</b>
                            <br></br>
                            {patientdt?.citizen?.address?.present?.addressLine}
                            <br></br>
                            {patientdt?.citizen?.address?.present?.area}
                            <br></br>
                            {patientdt?.citizen?.address?.present?.village}{" "}
                            <br></br>
                            {patientdt?.citizen?.address?.present?.state}
                            <br></br>
                            {patientdt?.citizen?.address?.present?.country},
                            &nbsp;&nbsp;
                            {patientdt?.citizen?.address?.present?.pinCode}
                          </span>
                        </div>
                      </Col>

                      <Col md={6}>
                        <div className="d-flex">
                          <span>
                            <br></br>
                            <p>
                              Marital Status:{" "}
                              <b>{patientdt?.citizen?.maritalStatus}</b>
                            </p>
                            <br></br>
                            <p>
                              Father / Spouse Name:{" "}
                              <b>{patientdt?.citizen?.spouseName}</b>
                            </p>
                            <br></br>
                            <p>
                              Resident Type:{" "}
                              <b>{patientdt?.citizen?.residentType}</b>
                            </p>
                            <br></br>
                          </span>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </React.Fragment>
              </div>
            </div>
          </div>
        </div>
      ) : null}
      {/* modal-3 patient more details modal box */}
    </React.Fragment>
  );
}
