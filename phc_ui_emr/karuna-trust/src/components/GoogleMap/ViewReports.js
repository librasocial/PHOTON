import React, { useState, useEffect } from "react";
import { Row, Col, Button, Form } from "react-bootstrap";
import Sidemenu from "../Sidemenus";

export default function ViewReports(props) {
  useEffect(() => {
    document.title = "EMR View Report";
  }, []);

  return (
    <Row className="main-div">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <React.Fragment>
          <div>
            <div className="regHeader">
              <h1 className="register-Header">View Report</h1>
              <hr />
            </div>
            <div className="queue-div">
              <iframe
                title="Household_Report"
                width={1000}
                height={500}
                frameborder="0"
                allowFullScreen="true"
                src="https://app.powerbi.com/reportEmbed?reportId=16036664-6c97-4f7a-b445-7b11741acb97&autoAuth=true&ctid=1942072d-23c5-40e7-a005-51fa4fa08042"
              ></iframe>
            </div>
          </div>
        </React.Fragment>
      </Col>
    </Row>
  );
}
