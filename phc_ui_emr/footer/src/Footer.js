import React, { useState } from "react";
import { Col, Row } from "react-bootstrap";
import "./CSS/Footer.css";
import "./index.scss";

export default function Footer() {
  const [border, setBorder] = useState("#fafafa");
  const appStyles = {
    border: `2px solid ${border}`,
  };
  const styles = {
    width: "100px",
    fontSize: "20px",
    borderRadius: "40px",
    border: "1px solid black",
    color: "white",
    margin: "0.5em 1em",
    padding: "0.25em 1em",
  };
  return (
    <footer className="footer">
      <div>
        <Row>
          <Col md={4}></Col>
          <Col md={4} align="center">
            <p className="footer1">
              {/* (c) 2022, Copyright Sampoorna Swaraj Foundation */}
            </p>
          </Col>
          <Col md={4}>
            <Row>
              <Col md={7} align="center">
                <p className="foot-right right-div">
                  Powered by <br /> Sampoorna Swaraj Foundation
                </p>
              </Col>
              <Col md={5} className="footImg">
                <img
                  src="../../img/powered-by-ssf-logo.png"
                  className="footeImg img-responsive"
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    </footer>
  );
}
