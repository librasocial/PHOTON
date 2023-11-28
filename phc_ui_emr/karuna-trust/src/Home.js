import React, { useEffect } from "react";
import { Col, Row } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import SaveButton from "./components/EMR_Buttons/SaveButton";
import "./css/Home.css";
function Home() {
  useEffect(() => {
    document.title = "EMR Home Screen";
  }, []);

  return (
    <div className="welcompage-div">
      <div className="homeDiv">
        <Row className="login-div-row">
          <Col
            md={7}
            align="center"
            className="d-flex justify-content-center align-items-center"
          >
            <img
              src="../img/HomeScreenImg.svg"
              alt="hosp"
              className="homeImg"
            />
          </Col>
          <Col
            md={5}
            className="d-flex justify-content-center align-items-center"
          >
            <div className="homeRight">
              <div>
                <h2 className="homeHeader">
                  Welcome to Karuna Comprehensive Primary Healthcare Services
                </h2>
              </div>
              <div>
                <p className="homText">
                  Karuna Trust provides Comprehensive Primary Health care
                  services in the PHCs. With the Government of India backing the
                  Digital India movement, there have been a lot of advancements
                  in the technology front in health care.{" "}
                </p>
                <p className="homText">
                  The Electronic Medical Record [EMR] helps the healthcare
                  service providers of Sub Centres (SCs) and PHCs with complete,
                  accurate, adequate and timely patient information for
                  providing quality care during the episode of care.{" "}
                </p>
                <p className="homText">
                  The EMR facilitates paper less operation, maintenance of
                  patient records, secure sharing of information for secondary
                  and tertiary care, and enhances productivity and ensures
                  completeness of data. In addition to this, real-time entry
                  allows for remote monitoring and support.
                </p>
              </div>
              <div align="center" className="homeBtn">
                <NavLink to="/login">
                  <SaveButton class_name="regBtnN" button_name="Continue" />
                </NavLink>
              </div>
            </div>
          </Col>
        </Row>
      </div>
    </div>
  );
}

export default Home;
