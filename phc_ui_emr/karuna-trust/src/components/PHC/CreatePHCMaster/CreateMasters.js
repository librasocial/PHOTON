import React, { useState, useEffect } from "react";
import { Col, Row, Form, Card } from "react-bootstrap";
import { useHistory } from "react-router-dom";

export default function CreateMasters(props) {
  let history = useHistory();

  const configFacility = () => {
    history.push("/CreateFacility");
  };

  const createStaff = () => {
    history.push("/CreateStaff");
  };

  const assignProcess = () => {
    history.push("/AssignProcess");
  };

  const assignResponsibility = () => {
    history.push("/AssignResponsibility");
  };

  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="super-admin-form">
          <h3 className="super-config-details">
            Create PHC Masters to Configure the PHC software
          </h3>
          <Row>
            <Col md={4}>
              <Card className="super-card" onClick={configFacility}>
                <Card.Body className="cofigure-phc">
                  <Row>
                    <Col lg={3} md={3} sm={3}>
                      <img
                        src="../img/super/create-facility.png"
                        className="super-member-image"
                      />
                    </Col>
                    <Col lg={9} md={9} sm={9}>
                      <div className="config-desc">
                        <div>
                          <h4 className="super-member-head">
                            Create Facility Master
                          </h4>
                          <p className="super-member-body">
                            Create Facility under this PHC
                          </p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
              <Card className="super-card" onClick={assignResponsibility}>
                <Card.Body className="cofigure-phc">
                  <Row>
                    <Col lg={3} md={3} sm={3}>
                      <img
                        src="../img/super/assign-respons.png"
                        className="super-member-image"
                      />
                    </Col>
                    <Col lg={9} md={9} sm={9}>
                      <div className="config-desc">
                        <div>
                          <h4 className="super-member-head">
                            Assign Responsibilities
                          </h4>
                          <p className="super-member-body">
                            Assign responsibilities to the staff in the PHC
                          </p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4}>
              <Card className="super-card" onClick={createStaff}>
                <Card.Body className="cofigure-phc">
                  <Row>
                    <Col lg={3} md={3} sm={3}>
                      <img
                        src="../img/super/create-staff.png"
                        className="super-member-image"
                      />
                    </Col>
                    <Col lg={9} md={9} sm={9}>
                      <div className="config-desc">
                        <div>
                          <h4 className="super-member-head">
                            Create Staff Members
                          </h4>
                          <p className="super-member-body">
                            Create Staff and provide access to PHC Software
                          </p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4}>
              <Card className="super-card" onClick={assignProcess}>
                <Card.Body className="cofigure-phc">
                  <Row>
                    <Col lg={3} md={3} sm={3}>
                      <img
                        src="../img/super/assign-process.png"
                        className="super-member-image"
                      />
                    </Col>
                    <Col lg={9} md={9} sm={9}>
                      <div className="config-desc">
                        <div>
                          <h4 className="super-member-head">
                            Assign Process Owner & Assign Activities
                          </h4>
                          <p className="super-member-body">
                            Assign process ownership to an staff & assign
                            activities
                          </p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}
