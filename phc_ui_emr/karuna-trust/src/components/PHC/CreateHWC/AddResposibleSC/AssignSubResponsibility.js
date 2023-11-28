import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Breadcrumb,
  Card,
  Carousel,
} from "react-bootstrap";
import * as Tostify from "../../../ConstUrl/Tostify";
import { loadSubCenterDetails } from "../../../../redux/phcAction";
import { useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import ResponsibilityModal from "../../AssignResposibility/ResponsibilityModal";
import AssignResponsibility from "../../AssignResposibility/AssignResponsibility";

let center_type = "Sub-Center";
export default function AssignSubResponsibility(props) {
  const { sunCenterDetails } = useSelector((state) => state.phcData);
  // let dispatch = useDispatch();
  // useEffect(() => {
  //     document.title = "EMR Super Admin Assign Responsibilities",
  //         dispatch(loadSubCenterDetails())
  // }, [])

  // const { sunCenterDetails } = useSelector(state => state.phcData);

  // const [staffActShow, setStaffActShow] = useState(false);

  // const staffActClose = () => {
  //     setStaffActShow(false);
  // }

  // const assignActivities = () => setStaffActShow(true);

  // const removevitalstorege = () => {
  //     window.history.back();
  // }

  // let history = useHistory();
  // const removeCurrentPage = () => {
  //     history.push("/config-phc");
  // }

  return (
    <React.Fragment>
      <AssignResponsibility
        center_type={center_type}
        sunCenterDetails={sunCenterDetails}
      />
      {/* <ResponsibilityModal staffActShow={staffActShow} staffActClose={staffActClose} />
            <div className="div vital-div">
                <div className='regHeader'>
                    <h1 className="Encounter-Header">
                        PHC Configuration
                    </h1>
                    <hr style={{ margin: "0px" }} />
                </div>
                <div className="config-div">
                    <div className="super-tab">
                        <div className="super-breadcrumb">
                            <Breadcrumb>
                                <Breadcrumb.Item className="pur-order-breadcrumb" onClick={removeCurrentPage}>PHC Configuration</Breadcrumb.Item>
                                <Breadcrumb.Item className="pur-order-breadcrumb" onClick={removevitalstorege}>Select SC/HWC to Assign Responsibilities</Breadcrumb.Item>
                                <Breadcrumb.Item active className="phc-breadcrumb">
                                    Assign Responsibilities
                                </Breadcrumb.Item>
                            </Breadcrumb>
                        </div>
                        <div className="form-col">
                            <Form className="super-admin-form">
                                <div className="super-complaint">
                                    <h3 className="super-config-details">
                                        Assign Responsibilities in {sunCenterDetails?.properties?.name}
                                    </h3>
                                    <h3 className="super-owner-details">
                                        Process Owner's List
                                    </h3>
                                    <div className='search-staff-add'>
                                        <Carousel className='staff-carousel'>
                                            <Carousel.Item>
                                                <Row className='pro-carousel'>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Registration </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Nursing </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Medical Consultation </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Medical Consultation </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                </Row>
                                            </Carousel.Item>
                                            <Carousel.Item>
                                                <Row className='pro-carousel'>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Nursing </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Medical Consultation </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Lab Technician </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                    <Col md={3}>
                                                        <Card className='staff-assign-card'>
                                                            <Row>
                                                                <Col lg={3}>
                                                                    <img src="../img/super/member.png" />
                                                                </Col>
                                                                <Col lg={9}>
                                                                    <div className='staff-div'>
                                                                        <p className='assign-staff'>
                                                                            Rangaswamy
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                        </p>
                                                                        <p className='total-staff'>
                                                                            <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                        </p>
                                                                    </div>
                                                                </Col>
                                                            </Row>
                                                            <hr />
                                                            <div className='staff-footer' align="center">
                                                                <p className='total-staff'> <b> Process Owner - Pharmacy </b></p>
                                                            </div>
                                                        </Card>
                                                    </Col>
                                                </Row>
                                            </Carousel.Item>
                                        </Carousel>
                                    </div>
                                    <div>
                                        <p className="resp-subcenter">
                                            <i className="bi bi-info-square-fill add-phc-icon"></i>
                                            <b>Select activities</b> to <b>assign</b> responsibilities to the Staff
                                        </p>
                                        <div className="form-group resp-staff-search">
                                            <input type="text" className="form-control staff-search" placeholder="Search Staff By Name / Contact Number" />
                                            <span className="fa fa-search form-control-pro-feedback d-flex"></span>
                                        </div>
                                    </div>
                                    <Row className='search-staff-add'>
                                        <Col lg={11}>
                                            <Row>
                                                <Col lg={4}>
                                                    <Card className='staff-activity-card'>
                                                        <Row>
                                                            <Col lg={3}>
                                                                <img src="../img/super/member.png" />
                                                            </Col>
                                                            <Col lg={9}>
                                                                <div className='staff-div'>
                                                                    <p className='assign-staff'>
                                                                        Rangaswamy
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                    </p>
                                                                </div>
                                                            </Col>
                                                        </Row>
                                                        <hr />
                                                        <div className='staff-footer' align="center">
                                                            <Button variant='link' to="/AssignResponsibility" className='selectActLink' onClick={assignActivities}>Select Activities</Button>
                                                        </div>
                                                    </Card>
                                                </Col>
                                                <Col lg={4}>
                                                    <Card className='staff-activity-card'>
                                                        <Row>
                                                            <Col lg={3}>
                                                                <img src="../img/super/member.png" />
                                                            </Col>
                                                            <Col lg={9}>
                                                                <div className='staff-div'>
                                                                    <p className='assign-staff'>
                                                                        Rangaswamy
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                    </p>
                                                                </div>
                                                            </Col>
                                                        </Row>
                                                        <hr />
                                                        <div className='staff-footer' align="center">
                                                            <Button variant='link' to="/AssignResponsibility" className='selectActLink' onClick={assignActivities}>Select Activities</Button>
                                                        </div>
                                                    </Card>
                                                </Col>
                                                <Col lg={4}>
                                                    <Card className='staff-activity-card'>
                                                        <Row>
                                                            <Col lg={3}>
                                                                <img src="../img/super/member.png" />
                                                            </Col>
                                                            <Col lg={9}>
                                                                <div className='staff-div'>
                                                                    <p className='assign-staff'>
                                                                        Rangaswamy
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff No:</span> 1234
                                                                    </p>
                                                                    <p className='total-staff'>
                                                                        <span style={{ color: "#707070" }}>Staff Role:</span> Admin
                                                                    </p>
                                                                </div>
                                                            </Col>
                                                        </Row>
                                                        <hr />
                                                        <div className='staff-footer' align="center">
                                                            <Button variant='link' to="/AssignResponsibility" className='selectActLink' onClick={assignActivities}>Select Activities</Button>
                                                        </div>
                                                    </Card>
                                                </Col>
                                            </Row>
                                        </Col>
                                        <Col lg={1}></Col>
                                    </Row>
                                </div>
                            </Form>
                        </div>
                    </div>
                </div>
            </div> */}
    </React.Fragment>
  );
}
