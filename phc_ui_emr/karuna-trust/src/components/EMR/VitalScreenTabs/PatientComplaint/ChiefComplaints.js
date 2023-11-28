import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import "../VitalScreenTabs.css";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import SpeechRecgongition from "../../../Dashboard/QueueManagement/SpeechRecgongition";

function ChiefComplaints(props) {
  const UHID = props.vitalsPatientData?.Patient?.UHId;
  const EncID = props.vitalsPatientData?.encounterId;
  const PatID = props.vitalsPatientData?.Patient?.patientId;
  const docName = props.vitalsPatientData?.Provider?.name;
  const PatName = props.vitalsPatientData?.Patient?.name;
  const PatHeaId = props.vitalsPatientData?.Patient?.healthId;
  const PatGen = props.vitalsPatientData?.Patient?.gender;
  const PatDob = props.vitalsPatientData?.Patient?.dob;
  const PatMob = props.vitalsPatientData?.Patient?.phone;
  const date = new Date();
  const [chiefDataId, setChiefDataId] = useState("");
  const [oldComplaint, setOldComplaint] = useState("");

  // let complaint = cadiacVascularData + " " + "from" + " " + days + " " + "days";
  const [complaint, setComplaint] = useState("");

  const [status, setStatus] = useState();
  const [todayDataID, setTodayDataID] = useState("");
  const [complaintFetchData, setComplaintFetchData] = useState("");
  // const sortedActivities = complaintFetchData.slice().sort((a, b) => (a.date > b.date) ? 1 : -1)
  // var latestData = [];
  // latestData.push(complaintFetchData[complaintFetchData.length - 2]);
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const dateYesterday = date.setDate(date.getDate() - 1);

  function createComplaint() {
    const complaintdata = {
      UHId: UHID,
      encounterId: EncID,
      patientId: PatID,
      recordedBy: "string",
      doctor: docName,
      effectiveDate: date,
      effectivePeriod: "string",
      complaintText: complaint.charAt(0).toUpperCase() + complaint.slice(1),
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(complaintdata),
    };
    fetch(`${constant.ApiUrl}/MedicalConditions`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Cheif complaint Saved for" + " " + PatName);
        setStatus(true);
      });
    setComplaint("");
  }

  const isValid = complaint != null && complaint.length > 0; // For validation
  // function for fetch recently save data for update
  function updateChiefComplaint(e) {
    setChiefDataId(e);
    let chiefId = e;

    if (chiefId != "" && chiefId != undefined) {
      fetch(
        `${constant.ApiUrl}/MedicalConditions/${chiefId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setOldComplaint(res["complaintText"]);
          setStatus(true);
        });
    }
  }
  // function for fetch recently save data for update
  // update chief complaint data
  function updateComplaint(e) {
    const updatingdata = {
      doctor: docName,
      complaintText: complaint.charAt(0).toUpperCase() + complaint.slice(1),
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingdata),
    };
    if (todayDataID != undefined) {
      fetch(
        `${constant.ApiUrl}/MedicalConditions/${todayDataID}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess("Cheif complaint Updated for" + " " + PatName);
          setStatus(true);
        });
    }
    setChiefDataId("");
    setComplaint("");
  }

  function updateOldComplaint(e) {
    const updatingolddata = {
      doctor: docName,
      complaintText:
        oldComplaint.charAt(0).toUpperCase() + oldComplaint.slice(1),
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingolddata),
    };
    if (chiefDataId != undefined) {
      fetch(
        `${constant.ApiUrl}/MedicalConditions/${chiefDataId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Cheif Complaint Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setChiefDataId("");
    setComplaint("");
  }
  // update chief complaint data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/MedicalConditions/filter?page=&size=&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            setComplaint(res["data"][0]["complaintText"]);
            setStatus(false);
          }
        });
    }
    if (UHID) {
      fetch(
        `${constant.ApiUrl}/MedicalConditions/filter?page=&size=&UHId=${UHID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let tempDataArray = [];
          for (var i = 0; i < res["data"].length; i++) {
            if (
              moment(res["data"][i].audit.dateCreated).format("YYYY-MM-DD") ==
              moment(dateYesterday).format("YYYY-MM-DD")
            ) {
              tempDataArray.push(res["data"][i]);
            }
          }
          if (tempDataArray.length != 0) {
            setComplaintFetchData(tempDataArray);
          }
          setStatus(false);
        });
    }
  }, [EncID, status]);
  const [cheifShow, setcheifShow] = useState(false);
  const chiefClose = () => setcheifShow(false);
  const chiefShow = () => setcheifShow(true);

  return (
    <React.Fragment>
      <ToastContainer />
      <ViewModalPopups
        chiefClose={chiefClose}
        cheifShow={cheifShow}
        PatName={PatName}
        chiefUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        cheifEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="chief"
      />
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Chief Complaint </h5>
              <Row>
                <Col md={10}>
                  <div className="col-container chief-textarea">
                    {chiefDataId ? (
                      <Form.Group className="mb-3_fname">
                        <Form.Control
                          as="textarea"
                          className="chief-textare-input"
                          placeholder="Start Typing Here..."
                          value={
                            oldComplaint.charAt(0).toUpperCase() +
                            oldComplaint.slice(1)
                          }
                          onChange={(event) =>
                            setOldComplaint(event.target.value)
                          }
                          controlid="formcreatecomplaint"
                        />
                      </Form.Group>
                    ) : (
                      <Form.Group className="mb-3_fname">
                        <Form.Control
                          as="textarea"
                          className="chief-textare-input"
                          placeholder="Start Typing Here..."
                          value={
                            complaint.charAt(0).toUpperCase() +
                            complaint.slice(1)
                          }
                          onChange={(event) => setComplaint(event.target.value)}
                          controlid="formcreatecomplaint"
                        />
                      </Form.Group>
                    )}
                    {/* <div> */}
                    {chiefDataId ? (
                      <div className="save-btn-section">
                        <SaveButton
                          butttonClick={(e) => updateOldComplaint(chiefDataId)}
                          class_name="regBtnN"
                          button_name="Update Chief Complaint"
                        />
                      </div>
                    ) : (
                      <div className="save-btn-section">
                        {todayDataID ? (
                          <SaveButton
                            butttonClick={(e) => updateComplaint(todayDataID)}
                            class_name="regBtnN"
                            button_name="Save chief complaint"
                            btnDisable={!isValid}
                          />
                        ) : (
                          <SaveButton
                            butttonClick={createComplaint}
                            class_name="regBtnN"
                            button_name="Save Chief Complaint"
                            btnDisable={!isValid}
                          />
                        )}
                      </div>
                    )}
                    {/* </div> */}
                  </div>
                </Col>
                <Col md={2}>
                  <SpeechRecgongition setComplaint={setComplaint} />
                </Col>
              </Row>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  onClick={chiefShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Chief Complaint
                </Button>
              </div>
              <div className="history-body-section">
                {complaintFetchData &&
                  Object.keys(complaintFetchData).map((compData, i) => (
                    <React.Fragment key={i}>
                      <h4 className="history-date">
                        {moment(
                          complaintFetchData[compData]?.audit.dateCreated
                        ).format("DD MMM, YYYY hh:mm A")}
                        <span
                          className="edit-medication"
                          onClick={(e) =>
                            updateChiefComplaint(
                              complaintFetchData[compData]?._id
                            )
                          }
                        >
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      </h4>
                      <p className="doc-name">
                        Medical Officer ({complaintFetchData[compData]?.doctor}){" "}
                      </p>
                      <p className="doc-desc">
                        {complaintFetchData[compData]?.complaintText}
                      </p>
                    </React.Fragment>
                  ))}
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}
export default ChiefComplaints;
