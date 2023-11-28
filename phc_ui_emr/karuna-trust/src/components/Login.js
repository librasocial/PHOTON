import Logincontainer from "logincontainer/Logincontainer";
import React, { useEffect } from "react";
import { Col, Row } from "react-bootstrap";
import "../css/Home.css";
import "../css/Login.css";
export default function Login() {
  useEffect(() => {
    document.title = "EMR Login";
  }, []);
  return (
    <React.Fragment>
      <div className="homeDiv">
        <Row className="loginDiv">
          <Col
            md={7}
            className="d-flex justify-content-center align-items-center"
          >
            <div className="login-conent-sec">
              <div className="logScnText">
                <p>
                  Health and Wellness centres, which represents the base pillar
                  of Ayushman Bharat, are envisaged to deliver an expanded range
                  of services to address the basic primary health care needs of
                  the entire population in their area, rather than focus
                  selectively on population sub-groups, thus expanding access,
                  universality and equity close to community. The emphasis on
                  health prevention and promotion is designed to bring focus on
                  keeping people healthy by engaging and empowering individuals
                  and communities to choose healthy behaviours and make changes
                  that reduce the risk of developing chronic diseases and other
                  morbidities.
                </p>
              </div>
              <div className="imagedivision">
                <Row>
                  <Col md={3}></Col>
                  <Col md={6}>
                    <img
                      src="../img/LoginGroup.svg"
                      className="loginScnImg"
                      alt="gwalk"
                    />
                  </Col>
                  <Col md={3}></Col>
                </Row>
              </div>
            </div>
          </Col>
          <Col md={4} className="d-flex justify-content-center ">
            <div className="login-container-sec">
              <Logincontainer />
            </div>
          </Col>
        </Row>
      </div>
    </React.Fragment>
  );
}
