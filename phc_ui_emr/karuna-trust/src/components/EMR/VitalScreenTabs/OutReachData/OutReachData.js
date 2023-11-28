import React, { useState } from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import CommunicableDisease from "./CommunicableDisease";
import NonCommunicableDisease from "./NonCommunicableDisease";
import RMNCHA from "./RMNCHA";
import SocioEconomic from "./SocioEconomic";
import "../VitalScreenTabs.css";

function OutReachData(props) {
  const vitalPatientData = props.vitalDatas;
  const [outTabIndex, setOutTabIndex] = useState("active0");

  function activeTabSelect(index) {
    setOutTabIndex("active" + index);
  }
  return (
    <React.Fragment>
      <div className="vital-tab">
        <div>
          <div className="patient-complaint">
            <Tab.Container id="left-tabs-example" defaultActiveKey="first">
              <Row>
                <Col sm={2}>
                  <div id="content15" className="tab-content" role="tablist">
                    <div
                      id="pane-41"
                      className="card tab-pane fade show active"
                      role="tabpanel15"
                      aria-labelledby="tab-41"
                    >
                      <div className="card-header" role="tab" id="heading-41">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#firstSocio"
                            aria-expanded="true"
                            aria-controls="firstSocio"
                            className="vitals-accordion"
                          >
                            Socio Economic
                          </a>
                        </h5>
                      </div>
                      <div
                        id="firstSocio"
                        className="collapse show"
                        data-parent="#content15"
                        role="tabpanel15"
                        aria-labelledby="heading-41"
                      >
                        <div className="card-body new-card-body">
                          <SocioEconomic vitalsPatientData={vitalPatientData} />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-42"
                      className="card tab-pane fade show active"
                      role="tabpanel15"
                      aria-labelledby="tab-42"
                    >
                      <div className="card-header" role="tab" id="heading-42">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#secondCommunicable"
                            aria-expanded="false"
                            aria-controls="secondCommunicable"
                            className="vitals-accordion"
                          >
                            Communicable Disease
                          </a>
                        </h5>
                      </div>
                      <div
                        id="secondCommunicable"
                        className="collapse"
                        data-parent="#content15"
                        role="tabpanel15"
                        aria-labelledby="heading-42"
                      >
                        <div className="card-body new-card-body">
                          <CommunicableDisease
                            vitalPatientData={vitalPatientData}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-43"
                      className="card tab-pane fade show active"
                      role="tabpanel15"
                      aria-labelledby="tab-43"
                    >
                      <div className="card-header" role="tab" id="heading-43">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#thirdNonCommunicable"
                            aria-expanded="true"
                            aria-controls="thirdNonCommunicable"
                            className="vitals-accordion"
                          >
                            Non Communicable Disease
                          </a>
                        </h5>
                      </div>
                      <div
                        id="thirdNonCommunicable"
                        className="collapse"
                        data-parent="#content15"
                        role="tabpanel15"
                        aria-labelledby="heading-43"
                      >
                        <div className="card-body new-card-body">
                          <NonCommunicableDisease
                            vitalsPatientData={vitalPatientData}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-44"
                      className="card tab-pane fade show active"
                      role="tabpanel15"
                      aria-labelledby="tab-44"
                    >
                      <div className="card-header" role="tab" id="heading-44">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#fourthRMNCHA"
                            aria-expanded="false"
                            aria-controls="fourthRMNCHA"
                            className="vitals-accordion"
                          >
                            RMNCH+A
                          </a>
                        </h5>
                      </div>
                      <div
                        id="fourthRMNCHA"
                        className="collapse"
                        data-parent="#content15"
                        role="tabpanel15"
                        aria-labelledby="heading-44"
                      >
                        <div className="card-body new-card-body">
                          <RMNCHA vitalPatientData={vitalPatientData} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <Nav variant="pills" className="flex-column bigDevice">
                    <Nav.Item onClick={(e) => activeTabSelect(0)}>
                      <Nav.Link eventKey="first">Socio Economic</Nav.Link>
                    </Nav.Item>
                    <Nav.Item onClick={(e) => activeTabSelect(1)}>
                      <Nav.Link eventKey="second">
                        Communicable Disease
                      </Nav.Link>
                    </Nav.Item>
                    <Nav.Item onClick={(e) => activeTabSelect(2)}>
                      <Nav.Link eventKey="third">
                        Non Communicable Disease
                      </Nav.Link>
                    </Nav.Item>
                    <Nav.Item onClick={(e) => activeTabSelect(3)}>
                      <Nav.Link eventKey="fourth">RMNCH+A</Nav.Link>
                    </Nav.Item>
                  </Nav>
                </Col>
                <Col md={10}>
                  <Tab.Content className="bigDevice">
                    {outTabIndex == "active0" && (
                      <Tab.Pane eventKey="first">
                        <SocioEconomic vitalsPatientData={vitalPatientData} />
                      </Tab.Pane>
                    )}
                    {outTabIndex == "active1" && (
                      <Tab.Pane eventKey="second">
                        <CommunicableDisease
                          vitalPatientData={vitalPatientData}
                        />
                      </Tab.Pane>
                    )}
                    {outTabIndex == "active2" && (
                      <Tab.Pane eventKey="third">
                        <NonCommunicableDisease
                          vitalsPatientData={vitalPatientData}
                        />
                      </Tab.Pane>
                    )}
                    {outTabIndex == "active3" && (
                      <Tab.Pane eventKey="fourth">
                        <RMNCHA vitalPatientData={vitalPatientData} />
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

export default OutReachData;
