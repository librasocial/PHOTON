import React, { useState, useEffect } from "react";
import { Col, Row, Button } from "react-bootstrap";
import moment from "moment";
import PatientComplaint from "./VitalScreenTabs/PatientComplaint/PatientComplaint";
import SummaryData from "./VitalScreenTabs/SummaryData/SummaryData";
import VitalPhysical from "./VitalScreenTabs/VitalPhysical/VitalPhysical";
import PrescInvestigation from "./VitalScreenTabs/PrescInvestigation/PrescInvestigation";
import ProvisionalDiagnosisData from "./VitalScreenTabs/ProvisionalDiagnosisData/ProvisionalDiagnosisData";
import OutReachData from "./VitalScreenTabs/OutReachData/OutReachData";
import DiagnosisDischarge from "./VitalScreenTabs/DiagnosisDischarge/DiagnosisDischarge";
import ModalPopups from "./ModalPopups/ModalPopups";
import * as constant from "../ConstUrl/constant";
import * as serviceHeaders from "../ConstUrl/serviceHeaders";
import { useDispatch, useSelector } from "react-redux";
import SignedHealthIDImage from "../Dashboard/SignedHealthIDImage";
import { loadSinglePatient, loadSinglePatDet } from "../../redux/actions";
import "../../css/CreateEncounter.css";
import "../../css/Vitals.css";
import ViewReport from "../LabModule/LabScreens/ViewReport";
import PatientRow from "../PatientRow/PatientRow";
import PageLoader from "../PageLoader";
import { loadDiagnosisDropdown } from "../../redux/formUtilityAction";
import { loadAsssignActivityList } from "../../redux/phcAction";

var UHId;
function RecordVitals(props) {
  const [vitaldetails, setVitaldetails] = useState("");
  const [detailsPatient, setDetailsPatient] = useState("");
  const [vitalData, setVitalData] = useState("");
  let respateintid = sessionStorage.getItem("ResPateintid");
  let patientId = sessionStorage.getItem("LabPateintid");

  // logedin userType
  let dispatch = useDispatch();
  let typeofuser = sessionStorage.getItem("typeofuser");
  let poUser = sessionStorage.getItem("poUser");

  let userUuid = sessionStorage.getItem("userid");
  const { activityList } = useSelector((state) => state.phcData);

  useEffect(() => {
    if (userUuid) {
      dispatch(loadAsssignActivityList(userUuid));
    }
  }, [userUuid]);

  let newDate = new Date();
  let thisyear = moment(newDate).format("YYYY");

  const [roleActivity, setRoleActivity] = useState([]);

  useEffect(() => {
    if (activityList) {
      const sorted = activityList.slice().sort(function (a, b) {
        if (a?.targetNode?.id > b?.targetNode?.id) return 1;
        if (a?.targetNode?.id < b?.targetNode?.id) return -1;
        return 0;
      });
      let activities = [];

      sorted.map((item) => {
        if (item.targetNode.properties.name) {
          activities.push(item.targetNode.properties.name);
        }
      });
      setRoleActivity(activities);
    }
  }, [activityList]);

  useEffect(() => {
    if (respateintid != undefined && respateintid != "") {
      (document.title = "EMR Vitals Screen"),
        fetch(
          `${constant.ApiUrl}/reservations/` + respateintid,
          serviceHeaders.getRequestOptions
        )
          .then((res) => res.json())
          .then((res) => {
            let dtArray = [];
            console.log(res, "testing data");
            for (let i = 0; i < 1; i++) {
              dtArray.push(res);
            }
            setVitalData(res);
            setVitaldetails(dtArray);
            setStatus(false);
          });
    }
  }, [status]);

  // back button
  const removevitalstorege = () => {
    window.history.back();
    sessionStorage.removeItem("LabPateintid");
    sessionStorage.removeItem("ResPateintid");
  };

  const backToVitals = () => {
    window.history.back();
    sessionStorage.removeItem("poUser");
    sessionStorage.removeItem("LabOrderId");
  };

  // set state for cancel visit modal
  const [showCancelModal, setCancelModal] = useState(false);
  const closeCancelVisit = () => setCancelModal(false);
  const [cancelpateintid, setCancelpateintid] = useState("");

  // cancel visit
  const [patientdt, setPatientdt] = useState("");
  const [popup, setPopup] = useState(false);

  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };

  const [showModal, setShowModal] = useState(false);
  const handleCloseModal = () => setShowModal(false);
  const [status, setStatus] = useState(false);
  const [showd, setShowd] = useState(false);
  const [doctordetails, setDoctordetails] = useState([]);

  const handClose = () => {
    setShowd(false);
  };
  const handShow = () => {
    setShowd(true);
  };

  const closeField = () => {
    setShowd("");
  };

  // checkin for consultation
  const [checkInData, setCheckInData] = useState("");

  const handleShow = (e) => {
    setShowModal(true);
    setPopup(true);
    const resevationofpateint = e;
    dispatch(loadSinglePatDet(resevationofpateint));
  };
  // checkin for consultation

  // End consultation
  const EndConsultaion = (e) => {
    const pateintid = e;
    fetch(
      `${constant.ApiUrl}/reservations/` + pateintid,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        const PresentTime = new Date();
        let parsedDate = moment(PresentTime, "DD.MM.YYYY H:mm:ss");
        let AssignEmergency = {
          Patient: {
            patientId: res["Patient"]["patientId"],
            healthId: res["Patient"]["healthId"],
            name: res["Patient"]["name"],
            gender: res["Patient"]["gender"],
            dob: res["Patient"]["dob"],
            phone: res["Patient"]["phone"],
          },
          Provider: {
            memberId: res["Provider"]["memberId"],
            name: res["Provider"]["name"],
          },
          _id: res["_id"],
          reservationFor: res["reservationFor"],
          reservationTime: parsedDate.toISOString(),
          status: res["status"],
          tokenNumber: res["tokenNumber"],
          label: "End",
        };
        setStatus(false);

        var requestOptions1 = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          body: JSON.stringify(AssignEmergency),
        };
        fetch(`${constant.ApiUrl}/reservations/` + pateintid, requestOptions1)
          .then((res1) => res1.json())
          .then((res1) => {
            window.location.reload(true);
            setStatus(true);
          });
        sessionStorage.removeItem("vitalon");
        window.history.back();
      });
  };
  //

  useEffect(() => {
    if (patientId != undefined && patientId != "") {
      fetch(
        `${constant.ApiUrl}/patients/` + patientId,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let dtArray = [];
          for (let i = 0; i < 1; i++) {
            dtArray.push(res);
          }
          setDetailsPatient(dtArray);
          setStatus(false);
        });
    }
  }, [status]);

  const [tabIndex, setTabIndex] = useState("");
  const [activeTabIndex, setActiveTabIndex] = useState();

  useEffect(() => {
    if (roleActivity) {
      // for (var i = 0; i <= roleActivity.length; i++){
      //     // if (roleActivity[i].includes("Patient Complaint") && (roleActivity[i] == "Patient Complaint")) {
      //     if (roleActivity[i].includes("Patient Complaint")) {
      //         setTabIndex("active0")
      //         setActiveTabIndex(0)
      //     } else if (!(roleActivity[i].includes("Patient Complaint")) && (roleActivity[i].includes("Vitals and Physical Examination"))) {
      //         setTabIndex("active2")
      //         setActiveTabIndex(2)
      //     } else if (!(roleActivity[i].includes("Patient Complaint")) && !(roleActivity[i].includes("Vitals and Physical Examination"))
      //         && (roleActivity[i].includes("Provisional Diagnosis")))
      // }
      roleActivity.map((roles) => {
        if (
          roles.includes("Patient Complaint") &&
          roles == "Patient Complaint"
        ) {
          setTabIndex("active0");
          setActiveTabIndex(0);
        }
        // else if (!(roles.includes("Patient Complaint")) && (roles.includes("Vitals and Physical Examination"))) {
        //     setTabIndex("active2")
        //     setActiveTabIndex(2)
        // } else if (!(roles.includes("Patient Complaint")) && !(roles.includes("Vitals and Physical Examination"))
        //     && (roles.includes("Provisional Diagnosis"))) {
        //     setTabIndex("active3")
        //     setActiveTabIndex(3)
        // } else if (!(roles.includes("Patient Complaint")) && !(roles.includes("Vitals and Physical Examination"))
        //     && !(roles.includes("Provisional Diagnosis")) && (roles.includes("Outreach Data"))) {
        //     setTabIndex("active4")
        //     setActiveTabIndex(4)
        // } else if (!(roles.includes("Patient Complaint")) && !(roles.includes("Vitals and Physical Examination"))
        //     && !(roles.includes("Provisional Diagnosis")) && !(roles.includes("Outreach Data")) &&
        //     (roles.includes("Prescription and Investigation"))) {
        //     setTabIndex("active5")
        //     setActiveTabIndex(5)
        // } else {
        //     setTabIndex("active6")
        //     setActiveTabIndex(6)
        // }
      });
    }
  }, [roleActivity]);

  function activeTabSelect(index) {
    setTabIndex("active" + index);
    setActiveTabIndex(index);
  }

  const [class_name0, setClass_name0] = useState("");
  const [class_name1, setClass_name1] = useState("");
  const [class_name2, setClass_name2] = useState("");
  const [class_name3, setClass_name3] = useState("");
  const [class_name4, setClass_name4] = useState("");
  const [class_name5, setClass_name5] = useState("");
  const [class_name6, setClass_name6] = useState("");

  useEffect(() => {
    if (activeTabIndex == 0) {
      setClass_name0("active");
    } else {
      setClass_name0("");
    }
    if (activeTabIndex == 1) {
      setClass_name1("active");
    } else {
      setClass_name1("");
    }
    if (activeTabIndex == 2) {
      setClass_name2("active");
    } else {
      setClass_name2("");
    }
    if (activeTabIndex == 3) {
      setClass_name3("active");
    } else {
      setClass_name3("");
    }
    if (activeTabIndex == 4) {
      setClass_name4("active");
    } else {
      setClass_name4("");
    }
    if (activeTabIndex == 5) {
      setClass_name5("active");
    } else {
      setClass_name5("");
    }
    if (activeTabIndex == 6) {
      setClass_name6("active");
    } else {
      setClass_name6("");
    }
  }, [activeTabIndex]);

  // for loader
  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    if (vitaldetails.length == 0 && detailsPatient.length == 0) {
      setPageLoader(true);
    } else {
      setPageLoader(false);
    }

    if (activeTabIndex !== 0 || activeTabIndex !== 2) {
      dispatch(loadDiagnosisDropdown());
    }
  }, [vitaldetails, detailsPatient, activeTabIndex]);
  // for loader

  if (window.performance) {
    if (performance.navigation.type == 1) {
      sessionStorage.removeItem("poUser");
      sessionStorage.removeItem("prevReport");
    }
  }

  const [isMobile, setIsMobile] = useState(false);

  //choose the screen size
  // const handleResize = () => {

  //     if (window.innerWidth < 720) {
  //         setIsMobile(true)
  //     } else {
  //         setIsMobile(false)
  //     }
  // }

  // create an event listener
  useEffect(() => {
    if (window.innerWidth < 720) {
      setIsMobile(true);
    } else {
      setIsMobile(false);
    }
    // window.addEventListener("resize", handleResize)
  }, [window.innerWidth]);

  return (
    <React.Fragment>
      {showModal == true && (
        <ModalPopups
          isCheckInShow={showModal}
          checkInClose={handleCloseModal}
          paData={patientdt}
          checkInData={checkInData}
          isCancelVisitShow={showCancelModal}
          cancelVisitClose={closeCancelVisit}
          cancelpaId={cancelpateintid}
          isPatientDetailsShow={showd}
          patDetailsClose={closeField}
          setShowModal={setShowModal}
        />
      )}
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="div vital-div">
        <div style={{ minHeight: "85vh" }}>
          <div className="regHeader">
            <h1 className="Encounter-Header">Patient Details</h1>
            <hr />
          </div>
          <Row className="">
            <Col md={2} className="back-btn-div">
              <div>
                <Button
                  variant="outline-secondary"
                  className="btn-back"
                  onClick={
                    poUser == "medical-Officer"
                      ? backToVitals
                      : removevitalstorege
                  }
                >
                  {" "}
                  <i className="bi bi-chevron-left"></i>
                  {poUser == "medical-Officer" ? "Back" : "Back to list"}{" "}
                </Button>
              </div>
            </Col>

            {vitaldetails &&
              Object.keys(vitaldetails).map((i, index) => (
                <Col md={8} className="dataFlr vital-data-section" key={index}>
                  <div className="dataVital">
                    <PatientRow
                      healthId={vitaldetails[i]?.Patient?.healthId}
                      fullName={vitaldetails[i]?.Patient?.name}
                      UHId={vitaldetails[i]?.Patient?.UHId}
                      gender={vitaldetails[i]?.Patient?.gender}
                      age={
                        thisyear -
                        moment(new Date(vitaldetails[i]?.Patient?.dob)).format(
                          "YYYY"
                        )
                      }
                      dateOfBirth={vitaldetails[i]?.Patient?.dob}
                      mobile={vitaldetails[i]?.Patient?.phone}
                      id={vitaldetails[i]?.Patient?.patientId}
                    />
                  </div>
                </Col>
              ))}
            {detailsPatient &&
              Object.keys(detailsPatient).map((i, index) => (
                <Col md={8} className="dataFlr vital-data-section" key={index}>
                  <div className="dataVital">
                    <PatientRow
                      healthId={detailsPatient[i]?.citizen?.healthId}
                      salutation={detailsPatient[i]?.citizen?.salutation}
                      firstName={detailsPatient[i]?.citizen?.firstName}
                      middleName={detailsPatient[i]?.citizen?.middleName}
                      lastName={detailsPatient[i]?.citizen?.lastName}
                      UHId={detailsPatient[i]?.UHId}
                      gender={detailsPatient[i]?.citizen?.gender}
                      age={
                        thisyear -
                        moment(
                          new Date(detailsPatient[i]?.citizen?.dateOfBirth)
                        ).format("YYYY")
                      }
                      dateOfBirth={detailsPatient[i]?.citizen?.dateOfBirth}
                      mobile={detailsPatient[i]?.citizen?.mobile}
                      id={detailsPatient[i]?._id}
                    />
                  </div>
                </Col>
              ))}

            {poUser != "medical-Officer" && (
              <Col md={2} className="end-consultation">
                {typeofuser === "Medical Officer" ? (
                  <div align="center" className="pa-checkin end-consultation">
                    <p onClick={(e) => EndConsultaion(vitaldetails[0]?._id)}>
                      End <br />
                      consultation
                    </p>
                  </div>
                ) : (
                  <>
                    {vitaldetails[0]?.status == "CheckedIn" ? (
                      ""
                    ) : (
                      <div
                        align="center"
                        className="pa-checkin send-consultation"
                        onClick={() => handleShow(vitaldetails[0]?._id)}
                      >
                        <p>
                          Send For <br />
                          consultation
                        </p>
                      </div>
                    )}
                  </>
                )}
              </Col>
            )}
          </Row>

          {poUser === "medical-Officer" ? (
            <>
              <ViewReport />
            </>
          ) : (
            <div className="pat-tabs main_acc">
              {typeofuser === "Medical Officer" ? (
                <React.Fragment>
                  {isMobile == true && (
                    <div id="content0" className="tab-content" role="tablist">
                      {roleActivity &&
                        roleActivity.map((activeTabs, i) => (
                          <React.Fragment key={i}>
                            {activeTabs == "Patient Complaint" && (
                              <div
                                id="pane-A"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-A"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-A"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_11"
                                      aria-expanded="true"
                                      aria-controls="tab_default_11"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/icon-patient-complaint.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      PATIENT COMPLAINT
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_11"
                                  className="collapse show"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-A"
                                >
                                  <div className="card-body new-card-body">
                                    <PatientComplaint vitalDatas={vitalData} />
                                  </div>
                                </div>
                              </div>
                            )}
                            {activeTabs ==
                              "Vitals and Physical Examination" && (
                              <div
                                id="pane-B"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-B"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-B"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_32"
                                      aria-expanded="false"
                                      aria-controls="tab_default_32"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/icon-physical-examination.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      VITALS & PHYSICAL EXAMINATION
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_32"
                                  className="collapse"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-B"
                                >
                                  <div className="card-body">
                                    <VitalPhysical vitalDatas={vitalData} />
                                  </div>
                                </div>
                              </div>
                            )}
                            {activeTabs == "Provisional Diagnosis" && (
                              <div
                                id="pane-C"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-C"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-C"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_43"
                                      aria-expanded="false"
                                      aria-controls="tab_default_43"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/icon-patient-history.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      PROVISIONAL DIAGNOSIS
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_43"
                                  className="collapse"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-C"
                                >
                                  <div className="card-body">
                                    <ProvisionalDiagnosisData
                                      vitalDatas={vitalData}
                                    />
                                  </div>
                                </div>
                              </div>
                            )}
                            {activeTabs == "Outreach Data" && (
                              <div
                                id="pane-D"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-D"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-D"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_54"
                                      aria-expanded="false"
                                      aria-controls="tab_default_54"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/survey.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      OUTREACH DATA
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_54"
                                  className="collapse"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-D"
                                >
                                  <div className="card-body">
                                    <OutReachData vitalDatas={vitalData} />
                                  </div>
                                </div>
                              </div>
                            )}
                            {activeTabs == "Prescription and Investigation" && (
                              <div
                                id="pane-E"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-E"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-E"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_65"
                                      aria-expanded="false"
                                      aria-controls="tab_default_65"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/icon-physical-examination.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      PRESCRIPTION AND INVESTIGATION
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_65"
                                  className="collapse"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-E"
                                >
                                  <div className="card-body">
                                    <PrescInvestigation
                                      vitalDatas={vitalData}
                                    />
                                  </div>
                                </div>
                              </div>
                            )}
                            {activeTabs == "Diagnosis and Discharge" && (
                              <div
                                id="pane-F"
                                className="card tab-pane fade show active"
                                role="tabpanel"
                                aria-labelledby="tab-F"
                              >
                                <div
                                  className="card-header"
                                  role="tab"
                                  id="heading-F"
                                >
                                  <h5 className="mb-0">
                                    <a
                                      data-toggle="collapse"
                                      href="#tab_default_76"
                                      aria-expanded="false"
                                      aria-controls="tab_default_76"
                                      className="vitals-accordion"
                                    >
                                      <img
                                        src="../img/default/icon-discharge.svg"
                                        alt="img"
                                        className="vitals-accordion-image"
                                      />
                                      DIAGNOSIS AND DISCHARGE
                                    </a>
                                  </h5>
                                </div>
                                <div
                                  id="tab_default_76"
                                  className="collapse"
                                  data-parent="#content0"
                                  role="tabpanel"
                                  aria-labelledby="heading-F"
                                >
                                  <div className="card-body">
                                    <DiagnosisDischarge
                                      vitalDatas={vitalData}
                                    />
                                  </div>
                                </div>
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                    </div>
                  )}
                  {isMobile == false && (
                    <div className="tabbable-panel">
                      <div className="tabbable-line">
                        <div>
                          <ul className="nav nav-tabs nav-tabs-header tabs_for_small_device">
                            {roleActivity &&
                              roleActivity.map((activeTabs, i) => (
                                <React.Fragment key={i}>
                                  {activeTabs == "Patient Complaint" && (
                                    <li onClick={(e) => activeTabSelect(0)}>
                                      <a
                                        href="#tab_default_1"
                                        data-toggle="tab"
                                        className={class_name0}
                                      >
                                        {/* <center><img src="../img/default/summary.svg" alt="img" /></center>
                                                        SUMMARY */}
                                        <center>
                                          <img
                                            src="../img/default/icon-patient-complaint.svg"
                                            alt="img"
                                          />
                                        </center>
                                        PATIENT COMPLAINT
                                      </a>
                                    </li>
                                  )}

                                  {/* <li onClick={(e) => activeTabSelect(1)}>
                                                    <a href="#tab_default_2" data-toggle="tab" >
                                                        <center><img src="../img/default/icon-patient-complaint.svg" alt="img" /></center>
                                                        PATIENT COMPLAINT
                                                    </a>
                                                </li> */}
                                  {activeTabs ==
                                    "Vitals and Physical Examination" && (
                                    <li onClick={(e) => activeTabSelect(2)}>
                                      <a
                                        href="#tab_default_3"
                                        data-toggle="tab"
                                        className={class_name2}
                                      >
                                        <center>
                                          <img
                                            src="../img/default/icon-physical-examination.svg"
                                            alt="img"
                                          />
                                        </center>
                                        VITALS & PHYSICAL EXAMINATION
                                      </a>
                                    </li>
                                  )}
                                  {activeTabs == "Provisional Diagnosis" && (
                                    <li onClick={(e) => activeTabSelect(3)}>
                                      <a
                                        href="#tab_default_4"
                                        data-toggle="tab"
                                        className={class_name3}
                                      >
                                        <center>
                                          <img
                                            src="../img/default/icon-patient-history.svg"
                                            alt="img"
                                          />
                                        </center>
                                        PROVISIONAL DIAGNOSIS
                                      </a>
                                    </li>
                                  )}
                                  {activeTabs == "Outreach Data" && (
                                    <li onClick={(e) => activeTabSelect(4)}>
                                      <a
                                        href="#tab_default_5"
                                        data-toggle="tab"
                                        className={class_name4}
                                      >
                                        <center>
                                          <img
                                            src="../img/default/survey.svg"
                                            alt="img"
                                          />
                                        </center>
                                        OUTREACH DATA
                                      </a>
                                    </li>
                                  )}
                                  {activeTabs ==
                                    "Prescription and Investigation" && (
                                    <li onClick={(e) => activeTabSelect(5)}>
                                      <a
                                        href="#tab_default_6"
                                        data-toggle="tab"
                                        className={class_name5}
                                      >
                                        <center>
                                          <img
                                            src="../img/default/icon-prescription.svg"
                                            alt="img"
                                          />
                                        </center>
                                        PRESCRIPTION AND INVESTIGATION
                                      </a>
                                    </li>
                                  )}
                                  {activeTabs == "Diagnosis and Discharge" && (
                                    <li onClick={(e) => activeTabSelect(6)}>
                                      <a
                                        href="#tab_default_7"
                                        data-toggle="tab"
                                        className={class_name6}
                                      >
                                        <center>
                                          <img
                                            src="../img/default/icon-discharge.svg"
                                            alt="img"
                                          />
                                        </center>
                                        DIAGNOSIS AND DISCHARGE
                                      </a>
                                    </li>
                                  )}
                                </React.Fragment>
                              ))}
                          </ul>
                          <div className="tab-content bigDevice">
                            {tabIndex == "active0" && (
                              <div className="active" id="tab_default_1">
                                {/* <SummaryData vitalDatas={vitalData} /> */}
                                <PatientComplaint vitalDatas={vitalData} />
                              </div>
                            )}
                            {/* {tabIndex == "active1" &&
                                                    <div className="tab-pane" id="tab_default_2">
                                                        <PatientComplaint vitalDatas={vitalData} />
                                                    </div>
                                                } */}
                            {tabIndex == "active2" && (
                              <div className="" id="tab_default_3">
                                <VitalPhysical vitalDatas={vitalData} />
                              </div>
                            )}
                            {tabIndex == "active3" && (
                              <div className="" id="tab_default_4">
                                <ProvisionalDiagnosisData
                                  vitalDatas={vitalData}
                                />
                              </div>
                            )}
                            {tabIndex == "active4" && (
                              <div className="" id="tab_default_5">
                                <OutReachData vitalDatas={vitalData} />
                              </div>
                            )}
                            {tabIndex == "active5" && (
                              <div className="" id="tab_default_6">
                                <PrescInvestigation vitalDatas={vitalData} />
                              </div>
                            )}
                            {tabIndex == "active6" && (
                              <div className="" id="tab_default_7">
                                <DiagnosisDischarge vitalDatas={vitalData} />
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                  )}
                </React.Fragment>
              ) : (
                <React.Fragment>
                  <div className="tabbable-line">
                    <ul className="nav nav-tabs nav-tabs-header">
                      <li className="active">
                        <a
                          href="#tab_default_1"
                          data-toggle="tab"
                          className="active"
                        >
                          <center>
                            <img
                              src="../img/default/icon-physical-examination.svg"
                              alt="img"
                            />
                          </center>
                          VITAL SIGNS{" "}
                        </a>
                      </li>
                    </ul>
                    <div className="tab-content">
                      <div className="tab-pane active" id="tab_default_1">
                        <VitalPhysical vitalDatas={vitalData} />
                      </div>
                    </div>
                  </div>
                </React.Fragment>
              )}
            </div>
          )}
        </div>
      </div>
    </React.Fragment>
  );
}
export default RecordVitals;
