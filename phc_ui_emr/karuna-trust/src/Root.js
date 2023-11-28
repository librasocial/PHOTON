import React from "react";
//import ReactDOM from "react-dom";
//import Login from "login/Login";
import Mainheader from "mainheader/Mainheader";
import Dashboard from "./Dashboard";
import GeneratedIds from "./GeneratedIds";
import Sidemenu from "./Sidemenu";
import { Col, Container, Row } from "react-bootstrap";
import Footer from "footer/Footer";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import "./index.scss";
import "./css/Login.css";

function Root() {
  return (
    <React.Fragment>
      <Mainheader />
      <Router>
        <React.Fragment>
          <Container fluid>
            <Row>
              <Col md={2}>
                <Sidemenu />
              </Col>
              <Col md={10}>
                <Routes>
                  <Route
                    path="/dashboard"
                    exact={true}
                    element={<Dashboard />}
                  ></Route>
                  <Route
                    path="/generatedids"
                    exact={true}
                    element={<GeneratedIds />}
                  ></Route>
                </Routes>
              </Col>
            </Row>
          </Container>
        </React.Fragment>
      </Router>
      <Footer />
    </React.Fragment>
  );
}
export default Root;
