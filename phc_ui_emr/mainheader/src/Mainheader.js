import React, { useState } from "react";
import {
  Button,
  Modal,
  Nav,
  Navbar,
  NavDropdown,
  Container,
  Row,
  Col,
  Image,
} from "react-bootstrap";
import { useHistory, Link } from "react-router-dom";
import { NavLink } from "react-router-dom";
import "./css/mainheader.css";

export default function Mainheader(props) {
  const history = useHistory();

  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const checkoption = (e) => {
    e.target.value = "&#10003";
  };
  function logout() {
    sessionStorage.clear();
    window.location.href = "/";
  }

  const mopostatus = () => {
    sessionStorage.setItem("poUser", "medical-Officer");
  };

  let typeofuser = sessionStorage.getItem("typeofuser");

  return (
    <React.Fragment>
      <Navbar
        collapseOnSelect={true}
        expand="lg"
        bg="white"
        className={
          sessionStorage.getItem("login") ? "navBar" : "navBar pro-navbar"
        }
        fixed="top"
      >
        <Navbar.Brand href="#home">
          <img src="../../img/SSF.png" className="KarunaLogo" />
        </Navbar.Brand>
        {sessionStorage.getItem("login") ? (
          <>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse
              className="justify-content-end align-items-center"
              id="responsive-navbar-nav"
            >
              <React.Fragment>
                <Nav className="headerNav haederRespNav">
                  <div className="admin-head">
                    <Nav.Link>
                      <p className="bigDevicelogin">
                        {props.membername}
                        <br />
                        You have logged in as{" "}
                        <span className="login-details">
                          {props.typeofuser}
                        </span>{" "}
                        <span>for</span>{" "}
                        <span className="phcname"> {props.phc}</span>
                      </p>
                      <p className="smallDevicelogin">
                        {" "}
                        <span className="login-details">
                          {props.membername}
                        </span>{" "}
                        have logged in as <br />{" "}
                        <span className="login-details">
                          {props.typeofuser}
                        </span>{" "}
                        <span>for</span>{" "}
                        <span className="phcname"> {props.phc}</span>
                      </p>
                    </Nav.Link>
                    <Nav.Link>
                      <Image
                        className="headerAdmin"
                        src="../../img/admin.png"
                      />
                    </Nav.Link>
                    {/* {typeofuser === "Medical Officer" ?
                                            <React.Fragment>
                                                <div className="admin-divider">
                                                    <div className="divBar"></div>
                                                </div>
                                                <Nav className="">
                                                    <div className="dropdown bell-icon-dropdown">
                                                        <NavDropdown id="collasible-nav-dropdown" className="drpDwn bell-notification" align="center"
                                                            title={<div className="notification-section"><i className='bi bi-bell-fill notification-bell'><span className="main-notification"></span></i></div>}
                                                        >
                                                            <Row className="notification-border">
                                                                <Col md={9}>
                                                                    <Row>
                                                                        <Col md={1} className="text-center">
                                                                            <span className="text-notify"></span>
                                                                        </Col>
                                                                        <Col md={11}>
                                                                            <p className="notification-text">
                                                                                Investigation report of Mr.Seetharaman Nanjundappa is ready.<br />
                                                                                Order Id:&nbsp; <Nav.Link as={Link} className="approvalslink" to="/Report" eventKey="/Report" onClick={mopostatus}>ID-5378678277 </Nav.Link>
                                                                            </p>
                                                                        </Col>
                                                                    </Row>
                                                                </Col>
                                                                <Col md={3} className="head-notation">
                                                                    <div className="body-notation">
                                                                        <Image className="headerAdmin notiImage" src="../../img/admin.png" />
                                                                        <p className="notification-time">10 minutes ago</p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <Row className="notification-border">
                                                                <Col md={9}>
                                                                    <Row>
                                                                        <Col md={1} className="text-center">
                                                                            <span className="text-notify"></span>
                                                                        </Col>
                                                                        <Col md={11}>
                                                                            <p className="notification-text">
                                                                                Purchase Order raised by Mr. Robert Williams. <br />
                                                                                Order Id:&nbsp; <Nav.Link as={Link} className="approvalslink" to="/poApprovals" eventKey="/poApprovals" onClick={mopostatus}>ID-5378678277</Nav.Link>
                                                                            </p>
                                                                        </Col>
                                                                    </Row>
                                                                </Col>
                                                                <Col md={3} className="head-notation">
                                                                    <div className="body-notation">
                                                                        <Image className="headerAdmin notiImage" src="../../img/admin.png" />
                                                                        <p className="notification-time">10 minutes ago</p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <div className="showmore-div">
                                                                <Nav.Link as={Link} className="showmorelink" to="/notifications" eventKey="/notifications"> Show More </Nav.Link>
                                                            </div>
                                                        </NavDropdown>
                                                    </div>
                                                    
                                                </Nav>
                                            </React.Fragment>
                                            : ""
                                        } */}
                    <div className="admin-divider">
                      <div className="divBar"></div>
                    </div>
                  </div>
                </Nav>
                <div className="smallDevicelink">
                  {typeofuser == "Admin" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/searchpatient"
                          eventKey="/searchpatient"
                        >
                          <i className="bi bi-person-plus dash-icon"></i>
                          Register New Patient
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/EncounterSearch"
                          eventKey="/EncounterSearch"
                        >
                          <i className="bi bi-person-check dash-icon"></i>Create
                          Visit{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/queue"
                          eventKey="/queue"
                        >
                          <i className="bi bi-people dash-icon"></i>Queue
                          Management{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/gis-map"
                          eventKey="/gis-map"
                        >
                          <i className="bi bi-geo-alt dash-icon"></i>Information
                          Privilege
                        </Nav.Link>
                      </Nav>
                    </React.Fragment>
                  )}
                  {typeofuser == "Asha Worker" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/generatedIds"
                          eventKey="/generatedIds"
                        >
                          <i className="bi bi-person-plus dash-icon"></i>
                          Generate and Issue ABHA Number(Health ID)
                        </Nav.Link>
                      </Nav>
                    </React.Fragment>
                  )}
                  {typeofuser == "Lab Technician" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/Labtest"
                          eventKey="/Labtest"
                        >
                          <i className="bi bi-gear dash-icon"></i>Lab Master{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                      </Nav>
                    </React.Fragment>
                  )}
                  {typeofuser == "Medical Officer" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/notifications"
                          eventKey="/notifications"
                        >
                          <i className="bi bi-bell dash-icon"></i>Notifications{" "}
                        </Nav.Link>
                        {/* <Nav.Link as={Link} className="login-sidemenu" to="/approvals" eventKey="/approvals"><i className="bi bi-person-rolodex dash-icon"></i>Approvals </Nav.Link> */}
                      </Nav>
                    </React.Fragment>
                  )}
                  {typeofuser == "Nurse" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                      </Nav>
                    </React.Fragment>
                  )}
                  {typeofuser == "Pharmacist" && (
                    <React.Fragment>
                      <Nav defaultActiveKey="/dashboard">
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/Product-Master"
                          eventKey="/Product-Master"
                        >
                          <i className="bi bi-gear dash-icon"></i>Product Master{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/dashboard"
                          eventKey="/dashboard"
                        >
                          <i className="bi bi-columns-gap dash-icon"></i>
                          Dashboard{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/Orders"
                          eventKey="/Orders"
                        >
                          <i className="bi bi-bag-check dash-icon"></i>Orders{" "}
                        </Nav.Link>
                        <Nav.Link
                          as={Link}
                          className="login-sidemenu"
                          to="/Inwards"
                          eventKey="/Inwards"
                        >
                          <i className="bi bi-bag-plus dash-icon"></i>Inwards{" "}
                        </Nav.Link>
                      </Nav>
                    </React.Fragment>
                  )}
                </div>
                <div className="logOutLink bigDeviceLink">
                  <Nav.Link onClick={handleShow}>
                    <span className="smallDevicelink smallDeviceLogout">
                      <i className="bi bi-power dash-icon"></i>
                    </span>
                    Logout
                  </Nav.Link>
                </div>
                <div className="smallDevicelink smallLogout">
                  <Nav.Link onClick={handleShow}>
                    <span style={{ color: "red", fontWeight: "800" }}>
                      <i className="bi bi-power dash-log-icon"></i>Log-Out
                    </span>
                  </Nav.Link>
                </div>
              </React.Fragment>
            </Navbar.Collapse>
          </>
        ) : (
          <div className="select-languge">
            <select onChange={checkoption}>
              <option> English </option>
              <option> ಕನ್ನಡ</option>
              <option> हिन्दी</option>
              <option>தமிழ்</option>
              <option> తెలుగు</option>
            </select>
          </div>
        )}
      </Navbar>
      <Modal show={show} onHide={handleClose} className="logout-modal">
        <Container>
          <div className="logout-body">
            <h4 className="logout-head">Logout</h4>
            <div align="center" className="logout-img">
              <img src="../../img/logout-svg-walkout.png" className="rounded" />
            </div>
            <p className="log-text" align="center">
              Are you sure, you want to logout ?
            </p>
          </div>
          <Row>
            <Col className="btn1">
              <Button
                variant="outline-secondary"
                className="logCancel"
                onClick={handleClose}
              >
                Cancel
              </Button>
            </Col>
            <Col className="btn2">
              <Button
                variant="secondary"
                className="logModule"
                onClick={logout}
              >
                Logout
              </Button>
            </Col>
          </Row>
        </Container>
      </Modal>
    </React.Fragment>
  );
}
