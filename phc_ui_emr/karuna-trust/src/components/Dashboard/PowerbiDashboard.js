import React, { useState, useEffect } from "react";
import { Row, Col, Button, Form } from "react-bootstrap";
import Sidemenu from "../Sidemenus";

function PowerbiDashboard() {
  return (
    <div>
      <Row className="main-div">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          <React.Fragment>
            <div>
              <div className="regHeader">
                <h1 className="register-Header">Dashboard</h1>
                <hr />
              </div>
              <div className="queue-div">
                <iframe
                  title="Household_Report"
                  width={1000}
                  height={500}
                  frameborder="0"
                  allowFullScreen="true"
                  src="https://app.powerbi.com/reportEmbed?reportId=d325bd97-35f9-4c5b-b33a-023b9ff7d68e&autoAuth=true&ctid=1942072d-23c5-40e7-a005-51fa4fa08042"
                ></iframe>
              </div>
            </div>
          </React.Fragment>
        </Col>
      </Row>
    </div>
  );
}

export default PowerbiDashboard;
