import React from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import Summary from "./Summary";
import "../VitalScreenTabs.css";

function SummaryData(props) {
  const vitalPatientData = props.vitalDatas;

  return (
    <React.Fragment>
      <div className="vital-tab">
        <div>
          <div className="patient-complaint">
            <Tab.Container id="left-tabs-example" defaultActiveKey="first">
              <Row>
                <Col sm={2}>
                  <Nav variant="pills" className="flex-column">
                    <Nav.Item>
                      <Nav.Link eventKey="first">Summary</Nav.Link>
                    </Nav.Item>
                  </Nav>
                </Col>
                <Col sm={10}>
                  <Tab.Content>
                    <Tab.Pane eventKey="first">
                      <Summary vitalsPatientData={vitalPatientData} />
                    </Tab.Pane>
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

export default SummaryData;
