import React, { useEffect, useState } from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import FinalDiagnosis from "./FinalDiagnosis";
import DischargeDisposition from "./DischargeDisposition";

function DiagnosisDischarge(props) {
  const vitalPatientData = props.vitalDatas;

  const [dignTabIndex, setDignTabIndex] = useState("active0");

  function activeTabSelect(index) {
    setDignTabIndex("active" + index);
  }

  const [isMobile, setIsMobile] = useState(false);

  //choose the screen size
  useEffect(() => {
    if (window.innerWidth < 720) {
      setIsMobile(true);
    } else {
      setIsMobile(false);
    }
  }, [window.innerWidth]);

  return (
    <React.Fragment>
      <div className="vital-tab">
        <div>
          <div className="patient-complaint">
            {isMobile == true && (
              <div id="content17" className="tab-content" role="tablist">
                <div
                  id="pane-61"
                  className="card tab-pane fade show active"
                  role="tabpanel13"
                  aria-labelledby="tab-61"
                >
                  <div className="card-header" role="tab" id="heading-61">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#firstFinal"
                        aria-expanded="true"
                        aria-controls="firstFinal"
                        className="vitals-accordion"
                      >
                        Final Diagnosis
                      </a>
                    </h5>
                  </div>
                  <div
                    id="firstFinal"
                    className="collapse show"
                    data-parent="#content17"
                    role="tabpanel13"
                    aria-labelledby="heading-61"
                  >
                    <div className="card-body new-card-body">
                      <FinalDiagnosis vitalDatas={vitalPatientData} />
                    </div>
                  </div>
                </div>
                <div
                  id="pane-62"
                  className="card tab-pane fade show active"
                  role="tabpanel13"
                  aria-labelledby="tab-62"
                >
                  <div className="card-header" role="tab" id="heading-62">
                    <h5 className="mb-0">
                      <a
                        data-toggle="collapse"
                        href="#secondDischarge"
                        aria-expanded="false"
                        aria-controls="secondDischarge"
                        className="vitals-accordion"
                      >
                        Discharge Disposition
                      </a>
                    </h5>
                  </div>
                  <div
                    id="secondDischarge"
                    className="collapse"
                    data-parent="#content17"
                    role="tabpanel13"
                    aria-labelledby="heading-62"
                  >
                    <div className="card-body new-card-body">
                      <DischargeDisposition vitalDatas={vitalPatientData} />
                    </div>
                  </div>
                </div>
              </div>
            )}
            <Tab.Container id="left-tabs-example" defaultActiveKey="first">
              {isMobile == false && (
                <Row>
                  <Col sm={2}>
                    <Nav variant="pills" className="flex-column bigDevice">
                      <Nav.Item onClick={(e) => activeTabSelect(0)}>
                        <Nav.Link eventKey="first">Final Diagnosis</Nav.Link>
                      </Nav.Item>
                      <Nav.Item onClick={(e) => activeTabSelect(1)}>
                        <Nav.Link eventKey="second">
                          Discharge Disposition
                        </Nav.Link>
                      </Nav.Item>
                    </Nav>
                  </Col>
                  <Col sm={10}>
                    <Tab.Content className="bigDevice">
                      {dignTabIndex == "active0" && (
                        <Tab.Pane eventKey="first">
                          <FinalDiagnosis vitalDatas={vitalPatientData} />
                        </Tab.Pane>
                      )}
                      {dignTabIndex == "active1" && (
                        <Tab.Pane eventKey="second">
                          <DischargeDisposition vitalDatas={vitalPatientData} />
                        </Tab.Pane>
                      )}
                    </Tab.Content>
                  </Col>
                </Row>
              )}
            </Tab.Container>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}

export default DiagnosisDischarge;
