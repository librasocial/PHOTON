import React from "react";
import { Col, Row, Tab, Nav } from "react-bootstrap";
import "../../css/Services.css";
import LiveService from "./LiveService";
import Pharmacy from "./Pharmacy";
import UserManagement from "./UserManagement";

function Services(props) {
  const vitalPatientData = props.vitalDatas;
  return (
    <React.Fragment>
      <div className="container dash-container ">
        <div className="lab-service">
          {/* <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                            <Row>
                                <Col sm={2} className="tabs-col">
                                    <Nav variant="pills" className="flex-column">
                                        <Nav.Item>
                                            <Nav.Link eventKey="first">Diagnostic Services</Nav.Link>
                                        </Nav.Item>
                                        <Nav.Item>
                                            <Nav.Link eventKey="second">Pharmacy</Nav.Link>
                                        </Nav.Item>
                                        <Nav.Item>
                                            <Nav.Link eventKey="third">User Management</Nav.Link>
                                        </Nav.Item>
                                    </Nav>
                                </Col>
                                <Col sm={10} className="tabs-col">
                                    <Tab.Content>
                                        <Tab.Pane eventKey="first">
                                            <LiveService />
                                        </Tab.Pane>
                                        <Tab.Pane eventKey="second">
                                            <Pharmacy />
                                        </Tab.Pane>
                                        <Tab.Pane eventKey="third">
                                            <UserManagement />
                                        </Tab.Pane>
                                    </Tab.Content>
                                </Col>
                            </Row>
                        </Tab.Container> */}
        </div>
      </div>
    </React.Fragment>
  );
}

export default Services;
