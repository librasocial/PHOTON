import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Image,
  Form,
  Button,
  Tab,
  Nav,
  Modal,
} from "react-bootstrap";
import moment from "moment";
import Prescription from "./Prescription";
import "./InvestigationTab.css";
import Investigation from "./Investigation";
// import Allergy from '../PatientComplaint/Allergy';
import PatientEducation from "./PatientEducation";

export default function PrescInvestigation(props) {
  const vitalData = props.vitalDatas;
  const [preTabIndex, setPreTabIndex] = useState("active0");

  function activeTabSelect(index) {
    setPreTabIndex("active" + index);
  }

  const [isMobile, setIsMobile] = useState(false);

  //choose the screen size
  const handleResize = () => {
    if (window.innerWidth < 720) {
      setIsMobile(true);
    } else {
      setIsMobile(false);
    }
  };

  // create an event listener
  useEffect(() => {
    window.addEventListener("resize", handleResize);
  });

  return (
    <React.Fragment>
      <div className="vital-tab">
        <div>
          <div className="patient-complaint">
            <Tab.Container id="left-tabs-example" defaultActiveKey="first">
              {isMobile == true && (
                <div id="content16" className="tab-content" role="tablist">
                  <div
                    id="pane-51"
                    className="card tab-pane fade show active"
                    role="tabpanel16"
                    aria-labelledby="tab-51"
                  >
                    <div className="card-header" role="tab" id="heading-51">
                      <h5 className="mb-0">
                        <a
                          data-toggle="collapse"
                          href="#firstPrescription"
                          aria-expanded="true"
                          aria-controls="firstPrescription"
                          className="vitals-accordion"
                        >
                          Prescription
                        </a>
                      </h5>
                    </div>
                    <div
                      id="firstPrescription"
                      className="collapse show"
                      data-parent="#content16"
                      role="tabpanel16"
                      aria-labelledby="heading-51"
                    >
                      <div className="card-body new-card-body">
                        <Prescription vitalDatas={vitalData} />
                      </div>
                    </div>
                  </div>
                  <div
                    id="pane-52"
                    className="card tab-pane fade show active"
                    role="tabpanel16"
                    aria-labelledby="tab-52"
                  >
                    <div className="card-header" role="tab" id="heading-52">
                      <h5 className="mb-0">
                        <a
                          data-toggle="collapse"
                          href="#secondInvestigation"
                          aria-expanded="false"
                          aria-controls="secondInvestigation"
                          className="vitals-accordion"
                        >
                          Investigation
                        </a>
                      </h5>
                    </div>
                    <div
                      id="secondInvestigation"
                      className="collapse"
                      data-parent="#content16"
                      role="tabpanel16"
                      aria-labelledby="heading-52"
                    >
                      <div className="card-body new-card-body">
                        <Investigation vitalDatas={vitalData} />
                      </div>
                    </div>
                  </div>
                  <div
                    id="pane-53"
                    className="card tab-pane fade show active"
                    role="tabpanel16"
                    aria-labelledby="tab-53"
                  >
                    {/* <div className="card-header" role="tab" id="heading-53">
                                            <h5 className="mb-0">
                                                <a data-toggle="collapse" href="#thirdAllergy" aria-expanded="true" aria-controls="thirdAllergy"
                                                    className='vitals-accordion'>
                                                    Allergy
                                                </a>
                                            </h5>
                                        </div>
                                        <div id="thirdAllergy" className="collapse" data-parent="#content16" role="tabpanel16" aria-labelledby="heading-53">
                                            <div className="card-body new-card-body">
                                                <Allergy vitalDatas={vitalData} />
                                            </div>
                                        </div> */}
                  </div>
                  <div
                    id="pane-54"
                    className="card tab-pane fade show active"
                    role="tabpanel16"
                    aria-labelledby="tab-54"
                  >
                    <div className="card-header" role="tab" id="heading-54">
                      <h5 className="mb-0">
                        <a
                          data-toggle="collapse"
                          href="#fourthPatient"
                          aria-expanded="false"
                          aria-controls="fourthPatient"
                          className="vitals-accordion"
                        >
                          Patient Education
                        </a>
                      </h5>
                    </div>
                    <div
                      id="fourthPatient"
                      className="collapse"
                      data-parent="#content16"
                      role="tabpanel16"
                      aria-labelledby="heading-54"
                    >
                      <div className="card-body new-card-body">
                        <PatientEducation />
                      </div>
                    </div>
                  </div>
                </div>
              )}
              <Row>
                <Col sm={2}>
                  <Nav variant="pills" className="flex-column bigDevice">
                    <Nav.Item onClick={(e) => activeTabSelect(0)}>
                      <Nav.Link eventKey="first">Prescription</Nav.Link>
                    </Nav.Item>
                    <Nav.Item onClick={(e) => activeTabSelect(1)}>
                      <Nav.Link eventKey="second">Investigation</Nav.Link>
                    </Nav.Item>
                    {/* <Nav.Item onClick={(e) => activeTabSelect(2)}>
                                            <Nav.Link eventKey="third">Allergy</Nav.Link>
                                        </Nav.Item> */}
                    {/* <Nav.Item onClick={(e) => activeTabSelect(3)}>
                      <Nav.Link eventKey="fourth">Patient Education</Nav.Link>
                    </Nav.Item> */}
                  </Nav>
                </Col>
                <Col sm={10}>
                  <Tab.Content className="bigDevice">
                    {preTabIndex == "active0" && (
                      <Tab.Pane eventKey="first">
                        <Prescription vitalDatas={vitalData} />
                      </Tab.Pane>
                    )}
                    {preTabIndex == "active1" && (
                      <Tab.Pane eventKey="second">
                        <Investigation vitalDatas={vitalData} />
                      </Tab.Pane>
                    )}
                    {/* {preTabIndex == "active2" &&
                                            <Tab.Pane eventKey="third">
                                                <Allergy vitalDatas={vitalData} />
                                            </Tab.Pane>
                                        } */}
                    {preTabIndex == "active3" && (
                      <Tab.Pane eventKey="fourth">
                        <PatientEducation />
                      </Tab.Pane>
                    )}
                  </Tab.Content>
                </Col>
              </Row>
            </Tab.Container>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
