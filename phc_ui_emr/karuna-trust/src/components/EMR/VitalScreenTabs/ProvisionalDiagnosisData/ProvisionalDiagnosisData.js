import React, { useEffect, useState } from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import ProvisionalDiagnosis from "./ProvisionalDiagnosis";
import "../VitalScreenTabs.css";

function ProvisionalDiagnosisData(props) {
  const vitalPatientData = props.vitalDatas;

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
                <div id="content14" className="tab-content" role="tablist">
                  <div
                    id="pane-31"
                    className="card tab-pane fade show active"
                    role="tabpanel14"
                    aria-labelledby="tab-31"
                  >
                    <div className="card-header" role="tab" id="heading-31">
                      <h5 className="mb-0">
                        <a
                          data-toggle="collapse"
                          href="#firstProvisional"
                          aria-expanded="true"
                          aria-controls="firstProvisional"
                          className="vitals-accordion"
                        >
                          Provisional Diagnosis
                        </a>
                      </h5>
                    </div>
                    <div
                      id="firstProvisional"
                      className="collapse show"
                      data-parent="#content14"
                      role="tabpanel14"
                      aria-labelledby="heading-31"
                    >
                      <div className="card-body new-card-body">
                        <ProvisionalDiagnosis ProvDatas={vitalPatientData} />
                      </div>
                    </div>
                  </div>
                </div>
              )}
              {isMobile == false && (
                <Row>
                  <Col sm={2}>
                    <Nav variant="pills" className="flex-column bigDevice">
                      <Nav.Item>
                        <Nav.Link eventKey="first">
                          Provisional Diagnosis
                        </Nav.Link>
                      </Nav.Item>
                    </Nav>
                  </Col>
                  <Col sm={10}>
                    <Tab.Content className="bigDevice">
                      <Tab.Pane eventKey="first">
                        <ProvisionalDiagnosis ProvDatas={vitalPatientData} />
                      </Tab.Pane>
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

export default ProvisionalDiagnosisData;
