import React, { useEffect, useState } from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import ChiefComplaints from "./ChiefComplaints";
import HistoryOfPresentIllness from "./HistoryOfPresentIllness";
import FamilyHistory from "./FamilyHistory";
import MedicalHistory from "./MedicalHistory";
import SocialHistory from "./SocialHistory";
import SurgicalHistory from "./SurgicalHistory";
import "../VitalScreenTabs.css";
import "./PatientHistory.css";
import { loadDiagnosisDropdown } from "../../../../redux/formUtilityAction";
import { useDispatch } from "react-redux";
import Allergy from "./Allergy";
import PatientReportPage from "./PatientReportPage";

function PatientComplaint(props) {
  let dispatch = useDispatch();
  const vitalPatientData = props.vitalDatas;
  const [patTabIndex, setPatTabIndex] = useState("active0");

  function activeTabSelect(index) {
    setPatTabIndex("active" + index);
  }

  const [isMobile, setIsMobile] = useState(false);

  //choose the screen size
  useEffect(() => {
    if (window.innerWidth < 720) {
      setIsMobile(true);
    } else {
      setIsMobile(false);
    }

    if (patTabIndex !== "active2" || patTabIndex !== "active3") {
      dispatch(loadDiagnosisDropdown());
    }
  }, [window.innerWidth, patTabIndex]);

  return (
    <React.Fragment>
      <div className="vital-tab">
        <div>
          <div className="patient-complaint">
            {isMobile == true && (
              <div id="content12" className="tab-content" role="tablist">
                <div
                  id="pane-1"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-1"
                >
                  <div className="card-header" role="tab" id="heading-1">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#first"
                        aria-expanded="true"
                        aria-controls="first"
                        className="vitals-accordion"
                      >
                        Chief Complaint
                      </a>
                    </h5>
                  </div>
                  <div
                    id="first"
                    className="collapse show"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-1"
                  >
                    <div className="card-body new-card-body">
                      <ChiefComplaints vitalsPatientData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                {/* <div id="pane-2" className="card tab-pane fade show active" role="tabpanel12" aria-labelledby="tab-2">
                                            <div className="card-header" role="tab" id="heading-2">
                                                <h5 className="mb-0">
                                                    <a data-toggle="collapse" href="#second" aria-expanded="false" aria-controls="second"
                                                        className='vitals-accordion'>
                                                        History Of Present Illness
                                                    </a>
                                                </h5>
                                            </div>
                                            <div id="second" className="collapse" data-parent="#content12" role="tabpanel12" aria-labelledby="heading-2">
                                                <div className="card-body new-card-body">
                                                    <HistoryOfPresentIllness vitalDatas={vitalPatientData} />
                                                </div>
                                            </div>
                                        </div> */}
                <div
                  id="pane-3"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-3"
                >
                  <div className="card-header" role="tab" id="heading-3">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#third"
                        aria-expanded="false"
                        aria-controls="third"
                        className="vitals-accordion"
                      >
                        Medical History
                      </a>
                    </h5>
                  </div>
                  <div
                    id="third"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-3"
                  >
                    <div className="card-body new-card-body">
                      <MedicalHistory vitalsMedicalData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                <div
                  id="pane-4"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-4"
                >
                  <div className="card-header" role="tab" id="heading-4">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#fourth"
                        aria-expanded="false"
                        aria-controls="fourth"
                        className="vitals-accordion"
                      >
                        Surgical History
                      </a>
                    </h5>
                  </div>
                  <div
                    id="fourth"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-4"
                  >
                    <div className="card-body new-card-body">
                      <SurgicalHistory vitalsSurgicalData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                <div
                  id="pane-5"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-5"
                >
                  <div className="card-header" role="tab" id="heading-5">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#fifth"
                        aria-expanded="false"
                        aria-controls="fifth"
                        className="vitals-accordion"
                      >
                        Family History
                      </a>
                    </h5>
                  </div>
                  <div
                    id="fifth"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-5"
                  >
                    <div className="card-body new-card-body">
                      <FamilyHistory vitalsFamilyData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                <div
                  id="pane-6"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-6"
                >
                  <div className="card-header" role="tab" id="heading-5">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#sixth"
                        aria-expanded="false"
                        aria-controls="sixth"
                        className="vitals-accordion"
                      >
                        Social History
                      </a>
                    </h5>
                  </div>
                  <div
                    id="sixth"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-6"
                  >
                    <div className="card-body new-card-body">
                      <SocialHistory vitalsSocialData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                <div
                  id="pane-7"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-7"
                >
                  <div className="card-header" role="tab" id="heading-5">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#seventh"
                        aria-expanded="false"
                        aria-controls="seventh"
                        className="vitals-accordion"
                      >
                        Allergy
                      </a>
                    </h5>
                  </div>
                  <div
                    id="seventh"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-7"
                  >
                    <div className="card-body new-card-body">
                      <Allergy vitalsSocialData={vitalPatientData} />
                    </div>
                  </div>
                </div>
                {/* <div
                  id="pane-8"
                  className="card tab-pane fade show active"
                  role="tabpanel12"
                  aria-labelledby="tab-7"
                >
                  <div className="card-header" role="tab" id="heading-6">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#seventh"
                        aria-expanded="false"
                        aria-controls="seventh"
                        className="vitals-accordion"
                      >
                        View Report
                      </a>
                    </h5>
                  </div>
                  <div
                    id="seventh"
                    className="collapse"
                    data-parent="#content12"
                    role="tabpanel12"
                    aria-labelledby="heading-7"
                  >
                    <div className="card-body new-card-body">
                      <PatientReportPage vitalsSocialData={vitalPatientData} />
                    </div>
                  </div>
                </div> */}
              </div>
            )}
            {isMobile == false && (
              <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                <Row>
                  <Col sm={2}>
                    <Nav variant="pills" className="flex-column bigDevice">
                      <Nav.Item onClick={(e) => activeTabSelect(0)}>
                        <Nav.Link eventKey="first">Chief Complaint</Nav.Link>
                      </Nav.Item>
                      {/* <Nav.Item onClick={(e) => activeTabSelect(1)}>
                                            <Nav.Link eventKey="second">History Of Present Illness</Nav.Link>
                                        </Nav.Item> */}
                      <Nav.Item onClick={(e) => activeTabSelect(2)}>
                        <Nav.Link eventKey="third">Medical History</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(3)}>
                        <Nav.Link eventKey="fourth">Surgical History</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(4)}>
                        <Nav.Link eventKey="fifth">Family History</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(5)}>
                        <Nav.Link eventKey="sixth">Social History</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(6)}>
                        <Nav.Link eventKey="seventh">Allergy</Nav.Link>
                      </Nav.Item>
                      {/* <Nav.Item onClick={(e) => activeTabSelect(7)}>
                        <Nav.Link eventKey="eight">View Report</Nav.Link>
                      </Nav.Item> */}
                    </Nav>
                  </Col>
                  <Col md={10} className="bigDevice">
                    <Tab.Content>
                      {patTabIndex == "active0" && (
                        <Tab.Pane eventKey="first">
                          <ChiefComplaints
                            vitalsPatientData={vitalPatientData}
                          />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active1" && (
                        <Tab.Pane eventKey="second">
                          <HistoryOfPresentIllness
                            vitalDatas={vitalPatientData}
                          />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active2" && (
                        <Tab.Pane eventKey="third">
                          <MedicalHistory
                            vitalsMedicalData={vitalPatientData}
                          />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active3" && (
                        <Tab.Pane eventKey="fourth">
                          <SurgicalHistory
                            vitalsSurgicalData={vitalPatientData}
                          />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active4" && (
                        <Tab.Pane eventKey="fifth">
                          <FamilyHistory vitalsFamilyData={vitalPatientData} />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active5" && (
                        <Tab.Pane eventKey="sixth">
                          <SocialHistory vitalsSocialData={vitalPatientData} />
                        </Tab.Pane>
                      )}
                      {patTabIndex == "active6" && (
                        <Tab.Pane eventKey="seventh">
                          <Allergy vitalsSocialData={vitalPatientData} />
                        </Tab.Pane>
                      )}
                      {/* {patTabIndex == "active7" && (
                        <Tab.Pane eventKey="eight">
                          <PatientReportPage
                            vitalsSocialData={vitalPatientData}
                          />
                        </Tab.Pane>
                      )} */}
                    </Tab.Content>
                  </Col>
                </Row>
              </Tab.Container>
            )}
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}

export default PatientComplaint;
