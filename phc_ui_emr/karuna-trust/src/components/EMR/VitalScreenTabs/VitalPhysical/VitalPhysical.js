import React, { useEffect, useState } from "react";
import {
  Col,
  Row,
  Form,
  Accordion,
  Button,
  Tab,
  Nav,
  Modal,
} from "react-bootstrap";
import "../VitalScreenTabs.css";
import PhysicalExamination from "./PhysicalExamination";
import PhysicalGeneralExam from "./PhysicalGeneralExam";
import VitalSigns from "./VitalSigns";

var UHId;
function VitalPhysical(props) {
  // logedin userType
  let typeofuser = sessionStorage.getItem("typeofuser");
  const [vitTabIndex, setVitTabIndex] = useState("active0");

  function activeTabSelect(index) {
    setVitTabIndex("active" + index);
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
          {typeofuser === "Medical Officer" ? (
            <div className="patient-complaint">
              <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                {isMobile == true && (
                  <div id="content13" className="tab-content" role="tablist">
                    <div
                      id="pane-11"
                      className="card tab-pane fade show active"
                      role="tabpanel13"
                      aria-labelledby="tab-11"
                    >
                      <div className="card-header" role="tab" id="heading-11">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#firstVital"
                            aria-expanded="true"
                            aria-controls="firstVital"
                            className="vitals-accordion"
                          >
                            Vital Signs
                          </a>
                        </h5>
                      </div>
                      <div
                        id="firstVital"
                        className="collapse show"
                        data-parent="#content13"
                        role="tabpanel13"
                        aria-labelledby="heading-11"
                      >
                        <div className="card-body new-card-body">
                          <VitalSigns vitalsPatientData={props.vitalDatas} />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-12"
                      className="card tab-pane fade show active"
                      role="tabpanel13"
                      aria-labelledby="tab-12"
                    >
                      <div className="card-header" role="tab" id="heading-12">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#secondPhysical"
                            aria-expanded="false"
                            aria-controls="secondPhysical"
                            className="vitals-accordion"
                          >
                            Physical Examination
                          </a>
                        </h5>
                      </div>
                      <div
                        id="secondPhysical"
                        className="collapse"
                        data-parent="#content13"
                        role="tabpanel13"
                        aria-labelledby="heading-12"
                      >
                        <div className="card-body new-card-body">
                          <PhysicalGeneralExam
                            vitalsPatientData={props.vitalDatas}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                )}
                <Row>
                  <Col sm={2}>
                    <Nav variant="pills" className="flex-column bigDevice">
                      <Nav.Item onClick={(e) => activeTabSelect(0)}>
                        <Nav.Link eventKey="first">Vital Signs</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(1)}>
                        <Nav.Link eventKey="second">
                          Physical Examination
                        </Nav.Link>
                      </Nav.Item>
                    </Nav>
                  </Col>
                  <Col sm={10}>
                    <Tab.Content className="bigDevice">
                      {vitTabIndex == "active0" && (
                        <Tab.Pane eventKey="first">
                          <VitalSigns vitalsPatientData={props.vitalDatas} />
                        </Tab.Pane>
                      )}
                      {vitTabIndex == "active1" && (
                        <Tab.Pane eventKey="second">
                          <div className="form-col">
                            <div className="cheif-complaint-form">
                              <div>
                                <PhysicalGeneralExam
                                  vitalsPatientData={props.vitalDatas}
                                />
                                {/* <PhysicalExamination vitalsPatientData={props.vitalDatas} /> */}
                              </div>
                            </div>
                          </div>
                        </Tab.Pane>
                      )}
                    </Tab.Content>
                  </Col>
                </Row>
              </Tab.Container>
            </div>
          ) : (
            <div className="patient-complaint">
              <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                <Row>
                  <Col sm={3}>
                    <Nav variant="pills" className="flex-column">
                      <Nav.Item>
                        <Nav.Link eventKey="first">Vital Signs</Nav.Link>
                      </Nav.Item>
                    </Nav>
                  </Col>
                  <Col sm={9}>
                    <Tab.Content>
                      <Tab.Pane eventKey="first">
                        <VitalSigns vitalsPatientData={props.vitalDatas} />
                      </Tab.Pane>
                    </Tab.Content>
                  </Col>
                </Row>
              </Tab.Container>
            </div>
          )}
        </div>
      </div>
    </React.Fragment>
  );
}

export default VitalPhysical;
