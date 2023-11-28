import React from "react";
import "../../../css/Queue.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { Col, Row } from "react-bootstrap";
import QueueManagement from "../QueueManagement/QueueManagement";
import Sidemenu from "../../Sidemenus";

export default function Queue(props) {
  return (
    <Row className="main-div ">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <React.Fragment>
          <div>
            <div className="regHeader">
              <h1 className="register-Header">Queue Management</h1>
              <hr />
            </div>
            <div className="queue-div">
              <Row>
                <Col md={12} lg={11}>
                  <QueueManagement />
                </Col>
              </Row>
            </div>
          </div>
        </React.Fragment>
      </Col>
    </Row>
  );
}
