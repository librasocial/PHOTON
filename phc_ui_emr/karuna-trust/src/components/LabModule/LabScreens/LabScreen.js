import React, { useState, useEffect } from "react";
import "../../../css/CreateEncounter.css";
import "../../../css/Vitals.css";
import { Col, Row, Button, Form, Modal, Container } from "react-bootstrap";
import moment from "moment";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import { useHistory } from "react-router-dom";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import SignedHealthIDImage from "../../Dashboard/SignedHealthIDImage";
import { loadSinglePatient } from "../../../redux/actions";
import { useDispatch } from "react-redux";
import PatientRow from "../../PatientRow/PatientRow";

var UHId;
function LabScreen(props) {
  let dispatch = useDispatch();
  let history = useHistory();
  const [vitaldetails, setVitaldetails] = useState("");
  // const [orderedDetails, setOrderedDetails] = useState([]);
  let orderedDetails = props.orderedDetails;

  const handleCloseModal = () => setShowModal(false);
  const [status, setStatus] = useState(false);

  // set state for cancel visit modal
  const [showCancelModal, setCancelModal] = useState(false);
  const closeCancelVisit = () => setCancelModal(false);

  let respateintid = sessionStorage.getItem("LabPateintid");

  let newDate = new Date();
  let thisyear = moment(newDate).format("YYYY");

  useEffect(() => {
    if (respateintid != undefined && respateintid != "") {
      fetch(
        `${constant.ApiUrl}/patients/` + respateintid,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let dtArray = [];
          for (let i = 0; i < 1; i++) {
            dtArray.push(res);
          }

          setVitaldetails(dtArray);
          setStatus(false);
        });
    }
  }, [status]);

  // back button
  const removevitalstorege = () => {
    sessionStorage.removeItem("LabPateintid");
    sessionStorage.removeItem("LabOrderId");
    sessionStorage.removeItem("cancelStatus");
    sessionStorage.removeItem("labByOrderId");
    sessionStorage.removeItem("LabResid");
    sessionStorage.removeItem("LabOrdId");
    history.push("/Dashboard");
  };

  // cancel visit
  const [patientdt, setPatientdt] = useState("");
  const [popup, setPopup] = useState(false);
  const [show, setShow] = useState("");

  const showptdtls = (e) => {
    setShow(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };

  const closeField = () => {
    setShow("");
  };

  // canceling all lab orders
  const labOrderId = props.labOrderId;
  const cancelAllTests = () => {
    const updatingOreders = {
      type: "LabOrders",
      properties: {
        medicalTests: [],
        status: "ORDER_ACCEPTED",
      },
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingOreders),
    };
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        setShow2(false);
        location.reload();
      });
  };
  // canceling all lab orders

  {
    /* Cancel All Order Modal */
  }
  const [show2, setShow2] = useState(false);
  const handleClose2 = () => setShow2(false);
  const handleShow2 = () => setShow2(true);
  {
    /* Cancel All Order Modal */
  }

  return (
    <React.Fragment>
      {/* Modal For Cancel All Order */}
      <Modal show={show2} onHide={handleClose2}>
        <Container>
          <Row>
            <Col>
              <h2 className="cancel-head">Cancel order</h2>
            </Col>
          </Row>
          <Row>
            <Col>
              <p className="cancel-test1">
                All the tests for this patient in this lab number will be
                cancelled and removed from the records. Order once cancelled
                cannot be reverted and will be lost.
              </p>
              <p>Are you sure you want to cancel order ?</p>
            </Col>
          </Row>
          <Row>
            <Col className="btn1">
              <Button
                variant="outline-secondary"
                className="regBtnPC"
                onClick={handleClose2}
              >
                No, Retain
              </Button>
            </Col>
            <Col className="btn2">
              <Button
                variant="outline-secondary orderContinue"
                onClick={cancelAllTests}
              >
                Yes, Cancel
              </Button>
            </Col>
          </Row>
        </Container>
      </Modal>
      {/* Modal For Cancel All Order */}

      <ModalPopups
        paData={patientdt}
        isPatientDetailsShow={show}
        patDetailsClose={closeField}
      />
      <div className="regHeader">
        <h1 className="Encounter-Header">Patient Details</h1>
        <hr />
      </div>
      <Row className="lab-row-div">
        <Col md={2} className="back-btn-div">
          <div>
            <Button
              variant="outline-secondary"
              className="btn-back"
              onClick={removevitalstorege}
            >
              {" "}
              <i className="bi bi-chevron-left"></i> Back to list{" "}
            </Button>
          </div>
        </Col>
        {vitaldetails &&
          Object.keys(vitaldetails).map((i, index) => (
            <Col md={8} className="dataFlr vital-data-section" key={index}>
              <div className="dataVital">
                <PatientRow
                  healthId={vitaldetails[i]?.citizen?.healthId}
                  salutation={vitaldetails[i]?.citizen?.salutation}
                  firstName={vitaldetails[i]?.citizen?.firstName}
                  middleName={vitaldetails[i]?.citizen?.middleName}
                  lastName={vitaldetails[i]?.citizen?.lastName}
                  UHId={vitaldetails[i]?.UHId}
                  gender={vitaldetails[i]?.citizen?.gender}
                  age={
                    thisyear -
                    moment(
                      new Date(vitaldetails[i]?.citizen?.dateOfBirth)
                    ).format("YYYY")
                  }
                  dateOfBirth={vitaldetails[i]?.citizen?.dateOfBirth}
                  mobile={vitaldetails[i]?.citizen?.mobile}
                  id={vitaldetails[i]?._id}
                />
              </div>
            </Col>
          ))}

        {orderedDetails &&
          orderedDetails.map((i, index) => (
            <Col md={8} className="dataFlr vital-data-section" key={index}>
              <div className="dataVital">
                <PatientRow
                  healthId={i.patient?.healthId}
                  fullName={i.patient?.name}
                  UHId={i.patient.uhid}
                  gender={i.patient?.gender}
                  age={
                    thisyear - moment(new Date(i.patient?.dob)).format("YYYY")
                  }
                  dateOfBirth={i.patient?.dob}
                  mobile={i.patient?.phone}
                  id={i.patient?.patientId}
                />
              </div>
            </Col>
          ))}
        <Col md={2} className="end-consultation">
          {props.cancelStatus && (
            <p className="pa-checkin end-consultation" onClick={handleShow2}>
              Cancel Order
            </p>
          )}
        </Col>
      </Row>
    </React.Fragment>
  );
}
export default LabScreen;
