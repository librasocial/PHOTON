import React, { useEffect, useState } from "react";
import "../../css/Dashboard.css";
import "../../mediacss/mediacss.css";
import { Col, Row, Modal, Carousel } from "react-bootstrap";
import moment from "moment";
import QueueManagement from "./QueueManagement/QueueManagement";
import Sidemenu from "../Sidemenus";
import { useDispatch, useSelector } from "react-redux";
import {
  loadDoctor,
  loadLabTech,
  loadPharma,
  loadNurse,
} from "../../redux/actions";
import SuperAdmin from "./QueueManagement/SuperAdmin";
import SaveButton from "../EMR_Buttons/SaveButton";

let isdashboard;
export default function Dashboard(props) {
  let sucess_modal = sessionStorage.getItem("successmodal");
  let typeofuser = sessionStorage.getItem("typeofuser");

  setTimeout(function () {
    sessionStorage.removeItem("successmodal");
  }, 3000);
  let visitPatName = sessionStorage.getItem("VisitPatientName");
  let visitResFor = sessionStorage.getItem("VisitResFor");
  let visitProName = sessionStorage.getItem("VisitProvider");

  const [maleCount1, setMaleCount1] = useState("");
  const [femaleCount1, setFemaleCount1] = useState("");

  let dispatch = useDispatch();
  const { usersList } = useSelector((state) => state.data);
  const { labOrderList } = useSelector((state) => state.data);
  const { doctorsList } = useSelector((state) => state.data);
  const { labTechList } = useSelector((state) => state.data);
  const { pharmacistList } = useSelector((state) => state.data);
  const { nurseList } = useSelector((state) => state.data);
  const [totalLabOrders, setTotalLabOrders] = useState("");

  useEffect(() => {
    if (usersList) {
      let result2 = usersList.filter((person) =>
        labOrderList.every(
          (person2) =>
            !person2.patient.patientId.includes(person.Patient.patientId)
        )
      );
      if (typeofuser == "Lab Technician") {
        let maleArray1 = [];
        let femaleArray1 = [];
        let totalCount = [];
        result2.map((items) => {
          totalCount.push(items);
          if (items["Patient"]["gender"].toLowerCase() == "male") {
            maleArray1.push(items);
          }
          if (items["Patient"]["gender"].toLowerCase() == "female") {
            femaleArray1.push(items);
          }
        });
        labOrderList.map((items) => {
          totalCount.push(items);
          if (items["patient"]["gender"].toLowerCase() == "male") {
            maleArray1.push(items);
          }
          if (items["patient"]["gender"].toLowerCase() == "female") {
            femaleArray1.push(items);
          }
        });
        setTotalLabOrders(totalCount);
        setMaleCount1(maleArray1.length);
        setFemaleCount1(femaleArray1.length);
      } else {
        let maleArray1 = [];
        let femaleArray1 = [];
        usersList.map((items) => {
          if (items["Patient"]["gender"].toLowerCase() == "male") {
            maleArray1.push(items);
          }
          if (items["Patient"]["gender"].toLowerCase() == "female") {
            femaleArray1.push(items);
          }
        });
        setMaleCount1(maleArray1.length);
        setFemaleCount1(femaleArray1.length);
      }
    }
  }, [usersList, labOrderList]);

  useEffect(() => {
    dispatch(loadDoctor());
    dispatch(loadLabTech());
    dispatch(loadPharma());
    dispatch(loadNurse());
  }, []);

  const [timeOfDay, setTimeOfDay] = useState("");

  useEffect(() => {
    const date = new Date();
    let hours = date.getHours();
    const timeOfDay1 = `Good ${
      (hours < 12 && "Morning") ||
      (hours < 16 && "Afternoon") ||
      (hours < 20 && "Evening") ||
      "Night"
    }`;
    setTimeOfDay(timeOfDay1);
  }, [setTimeOfDay]);

  useEffect(() => {
    var urlstr = window.location.href;
    let str = urlstr;
    if (str.match("dashboard")) {
      isdashboard = str.match("dashboard", "ig");
    } else if (str.match("")) {
      isdashboard = str.match("", "ig");
    }
  });

  const [showSuccessModal, setSuccessShowModal] = useState(true);
  const closeSuccessModal = () => {
    setSuccessShowModal(false);
    sessionStorage.removeItem("successmodal");
    sessionStorage.removeItem("VisitPatientName");
    sessionStorage.removeItem("VisitResFor");
    sessionStorage.removeItem("VisitProvider");
  };

  /* Display date and time */
  function displayTime() {
    var time2 = moment().format("ddd, DD MMM YYYY");
    var time3 = moment().format("hh:mm A");
    $("#clock2").html(time2);
    $("#clock3").html(time3);
    setTimeout(displayTime, 1000);
  }
  $(document).ready(function () {
    displayTime();
  });
  /* Display date and time */

  return (
    <React.Fragment>
      {/* successfully create visit modal */}
      {sucess_modal && (
        <Modal
          show={showSuccessModal}
          onHide={closeSuccessModal}
          className="alert-modal-div"
        >
          <Row>
            <Col xsm={1} sm={1} md={1} id="icon" align="center">
              <i className="bi bi-check2"></i>
            </Col>
            <Col xsm={11} sm={11} md={11} id="modal_text">
              <div className="">
                <Row>
                  <Col xsm={11} sm={11} md={11}>
                    <b id="modal-title">Visit created successfully</b>
                  </Col>
                  <Col xsm={1} sm={1} md={1} align="center">
                    <button
                      type="button"
                      className="btn-close modelCls"
                      data-bs-dismiss="modal"
                      aria-label="Close"
                      id="btn"
                      onClick={closeSuccessModal}
                    ></button>
                  </Col>
                </Row>
              </div>
              <div className="">
                <p className="success-text">
                  Patient {visitPatName} added to queue for{" "}
                  {visitResFor == "Doctor"
                    ? "Medical Officer"
                    : visitResFor == "Lab"
                    ? "Lab Technician"
                    : "Pharmacist"}{" "}
                  {visitProName}.
                </p>
              </div>
            </Col>
          </Row>
        </Modal>
      )}
      {/* successfully create visit modal */}
      <Row className={typeofuser === "Super Admin" ? "notify-div" : "main-div"}>
        {typeofuser != "Super Admin" ? (
          <Col lg={2} className="side-bar">
            <Sidemenu />
          </Col>
        ) : (
          ""
        )}

        <Col
          lg={typeofuser === "Super Admin" ? 12 : 10}
          md={12}
          sm={12}
          xs={12}
        >
          <Row id="dash_que">
            <Col className={typeofuser === "Super Admin" && "super-dash"}>
              <Row id="card-wish">
                <Col xs={3} lg={3}>
                  <div id="wish1">
                    <div className="card-clr dtedesn">
                      <h3 className="date-time countdown" id="clock2"></h3>
                      <h3 className="countdown" id="clock3"></h3>
                    </div>
                    <img
                      src="../img/dashboard-greeting-img.png"
                      style={{ marginBottom: "-2%" }}
                    />
                  </div>
                </Col>
                {typeofuser === "Admin" || typeofuser === "Super Admin" ? (
                  <Col md={9} sm={8} xs={8} id="gm">
                    {/* <div className="my_text">
                                        <h1 className="morn"> {timeOfDay} </h1>
                                        <h2 className="user-type">{props.typeofuser}</h2>
                                        <h2 className="names"> {props.membername} </h2>
                                    </div> */}
                  </Col>
                ) : (
                  <Col lg={8} xs={8} id="gm">
                    {/* <div className="my_text">
                                        <h1 className="morn"> {timeOfDay} </h1>
                                        <h2 className="user-type">{props.typeofuser}</h2>
                                        <h2 className="names"> {props.membername} </h2>
                                    </div> */}
                  </Col>
                )}
              </Row>
              {/* for Admin login */}
              {typeofuser === "Admin" && (
                <Row id="pat_que1">
                  <Col md={9} id="left_que">
                    <div className="create-icon">
                      <div className="appoint">
                        <Row>
                          <Col md={4}></Col>
                          <Col md={4} id="appo">
                            <div style={{ width: "100%" }}>
                              <div className="visit-div">
                                <img
                                  src="../img/icon-appointment.png"
                                  className="appo-icon"
                                  style={{ display: "inline-flex" }}
                                />
                                <b id="pat_id">
                                  {usersList ? usersList.length : "0"}
                                </b>
                              </div>
                              <div className="visit-div">
                                <p className="visit-text largeDeviceText">
                                  Visits for Today
                                </p>
                              </div>
                            </div>
                          </Col>
                          <Col md={4} id="pat_visit1" className="text-center">
                            <div>
                              <SaveButton
                                toLink="EncounterSearch"
                                button_name="Create Visit"
                                class_name="regBtnN"
                              />
                              <p>
                                <i>for already registered users</i>{" "}
                              </p>
                            </div>
                          </Col>
                        </Row>
                      </div>
                    </div>
                    <React.Fragment>
                      <div className="avalabledoctorlist smallDeviceText">
                        <div className="service-provider">
                          <h6
                            style={{ textAlign: "center", fontWeight: "600" }}
                          >
                            Available Service Providers
                          </h6>
                        </div>
                        <div className="available-list">
                          <Carousel
                            fade
                            variant="dark"
                            className="admin-carousel"
                          >
                            <Carousel.Item>
                              {doctorsList?.content?.map((item, index) => (
                                <Row id="det_doc" key={index}>
                                  <Col lg={3} id="doc_name">
                                    <center>
                                      <img
                                        src="../img/admin.png"
                                        className="rounded_doc"
                                        alt="doc"
                                      />
                                    </center>
                                    <p
                                      id="doc_img1"
                                      style={{ backgroundColor: "green" }}
                                    ></p>
                                  </Col>
                                  <Col lg={9} id="doc_det">
                                    <div>
                                      <p id="doct_name">Medical Officer</p>
                                      <p className="doctor_name" key={item}>
                                        ({item?.targetNode?.properties?.name})
                                      </p>
                                    </div>
                                  </Col>
                                </Row>
                              ))}
                            </Carousel.Item>
                            <Carousel.Item>
                              {labTechList?.content?.map((item, index) => (
                                <Row id="det_doc" key={index}>
                                  <Col lg={3} id="doc_name">
                                    <center>
                                      <img
                                        src="../img/admin.png"
                                        className="rounded_doc"
                                        alt="doc"
                                      />
                                    </center>
                                    <p
                                      id="doc_img1"
                                      style={{ backgroundColor: "green" }}
                                    ></p>
                                  </Col>
                                  <Col lg={9} id="doc_det">
                                    <div>
                                      <p id="doct_name">Lab Technician</p>
                                      <p className="doctor_name" key={item}>
                                        ( {item?.targetNode?.properties?.name} )
                                      </p>
                                    </div>
                                  </Col>
                                </Row>
                              ))}
                            </Carousel.Item>
                            <Carousel.Item>
                              {pharmacistList?.content?.map((item, index) => (
                                <Row id="det_doc" key={index}>
                                  <Col lg={3} id="doc_name">
                                    <center>
                                      <img
                                        src="../img/admin.png"
                                        className="rounded_doc"
                                        alt="doc"
                                      />
                                    </center>
                                    <p
                                      id="doc_img1"
                                      style={{ backgroundColor: "green" }}
                                    ></p>
                                  </Col>
                                  <Col lg={9} id="doc_det">
                                    <div>
                                      <p id="doct_name">Pharmacist</p>
                                      <p className="doctor_name" key={item}>
                                        ( {item?.targetNode?.properties?.name} )
                                      </p>
                                    </div>
                                  </Col>
                                </Row>
                              ))}
                            </Carousel.Item>
                            <Carousel.Item>
                              {nurseList?.content?.map((item, index) => (
                                <Row id="det_doc" key={index}>
                                  <Col lg={3} id="doc_name">
                                    <center>
                                      <img
                                        src="../img/admin.png"
                                        className="rounded_doc"
                                        alt="doc"
                                      />
                                    </center>
                                    <p
                                      id="doc_img1"
                                      style={{ backgroundColor: "green" }}
                                    ></p>
                                  </Col>
                                  <Col lg={9} id="doc_det">
                                    <div>
                                      <p id="doct_name">Pharmacist</p>
                                      <p className="doctor_name" key={item}>
                                        ( {item?.targetNode?.properties?.name} )
                                      </p>
                                    </div>
                                  </Col>
                                </Row>
                              ))}
                            </Carousel.Item>
                          </Carousel>
                        </div>
                      </div>
                      <QueueManagement isdashboard={isdashboard} />
                    </React.Fragment>
                  </Col>
                  <Col md={3} id="right_queue">
                    <div className="avalabledoctorlist">
                      <div className="service-provider">
                        <h6 style={{ textAlign: "center", fontWeight: "600" }}>
                          Available Service Providers
                        </h6>
                      </div>
                      <div className="available-list">
                        {doctorsList?.content?.map((item, index) => (
                          <Row id="det_doc" key={index}>
                            <Col lg={3} id="doc_name">
                              <center>
                                <img
                                  src="../img/admin.png"
                                  className="rounded_doc"
                                  alt="doc"
                                />
                              </center>
                              <p
                                id="doc_img1"
                                style={{ backgroundColor: "green" }}
                              ></p>
                            </Col>
                            <Col lg={9} id="doc_det">
                              <div>
                                <p id="doct_name">Medical Officer</p>
                                <p className="doctor_name" key={item}>
                                  ( {item?.targetNode?.properties?.name} )
                                </p>
                              </div>
                            </Col>
                          </Row>
                        ))}
                        {labTechList?.content?.map((item, index) => (
                          <Row id="det_doc" key={index}>
                            <Col lg={3} id="doc_name">
                              <center>
                                <img
                                  src="../img/admin.png"
                                  className="rounded_doc"
                                  alt="doc"
                                />
                              </center>
                              <p
                                id="doc_img1"
                                style={{ backgroundColor: "green" }}
                              ></p>
                            </Col>
                            <Col lg={9} id="doc_det">
                              <div>
                                <p id="doct_name">Lab Technician</p>
                                <p className="doctor_name" key={item}>
                                  ( {item?.targetNode?.properties?.name} )
                                </p>
                              </div>
                            </Col>
                          </Row>
                        ))}
                        {pharmacistList?.content?.map((item, index) => (
                          <Row id="det_doc" key={index}>
                            <Col lg={3} id="doc_name">
                              <center>
                                <img
                                  src="../img/admin.png"
                                  className="rounded_doc"
                                  alt="doc"
                                />
                              </center>
                              <p
                                id="doc_img1"
                                style={{ backgroundColor: "green" }}
                              ></p>
                            </Col>
                            <Col lg={9} id="doc_det">
                              <div>
                                <p id="doct_name">Pharmacist</p>
                                <p className="doctor_name" key={item}>
                                  ( {item?.targetNode?.properties?.name} )
                                </p>
                              </div>
                            </Col>
                          </Row>
                        ))}
                        {nurseList?.content?.map((item, index) => (
                          <Row id="det_doc" key={index}>
                            <Col lg={3} id="doc_name">
                              <center>
                                <img
                                  src="../img/admin.png"
                                  className="rounded_doc"
                                  alt="doc"
                                />
                              </center>
                              <p
                                id="doc_img1"
                                style={{ backgroundColor: "green" }}
                              ></p>
                            </Col>
                            <Col lg={9} id="doc_det">
                              <div>
                                <p id="doct_name">Pharmacist</p>
                                <p className="doctor_name" key={item}>
                                  ( {item?.targetNode?.properties?.name} )
                                </p>
                              </div>
                            </Col>
                          </Row>
                        ))}
                      </div>
                    </div>
                  </Col>
                </Row>
              )}
              {/* for Admin login */}

              {/* for Super Admin login */}
              {typeofuser === "Super Admin" && (
                <div id="pat_que1">
                  <div id="left_que">
                    <SuperAdmin />
                  </div>
                </div>
              )}
              {/* for Super Admin login */}

              {/* for nurse login */}
              {typeofuser === "Nurse" && (
                <div id="pat_que1">
                  <div id="left_que">
                    <Row>
                      <Col md={11}>
                        <React.Fragment>
                          <QueueManagement />
                        </React.Fragment>
                      </Col>
                    </Row>
                  </div>
                </div>
              )}
              {/* for nurse login */}

              {/* for Medical Officer login */}
              {typeofuser === "Medical Officer" && (
                <div id="pat_que1">
                  <div id="left_que">
                    <Row>
                      <Col md={12} lg={11}>
                        <div className="create-icon">
                          <div className="appoint">
                            <div className="medical-officer-div">
                              <Row className="medical-row largeDeviceText">
                                <Col md={3} id="appo">
                                  <div
                                    className="encounter-count day-visit text-center"
                                    style={{ width: "100%" }}
                                    align="center"
                                  >
                                    <div className="visit-div">
                                      <img
                                        src="../img/icon-appointment.png"
                                        className="appo-icon"
                                        style={{ display: "inline-flex" }}
                                      />
                                      <b id="pat_id">
                                        {usersList ? usersList.length : "0"}
                                      </b>{" "}
                                      <br />
                                    </div>
                                    <div className="visit-div">
                                      <p id="icon-text1">Visits for Today</p>
                                    </div>
                                  </div>
                                </Col>
                                <Col md={3} id="appo">
                                  <div
                                    className="encounter-count text-center"
                                    style={{ width: "100%" }}
                                  >
                                    <Row>
                                      <Col
                                        xsm={6}
                                        className="encounter-gender-count"
                                      >
                                        <div className="gen-count">
                                          <b id="pat_id">
                                            {maleCount1 ? maleCount1 : "0"}
                                          </b>{" "}
                                          <br />
                                          <p id="icon-text1">Male</p>
                                        </div>
                                      </Col>
                                      <Col
                                        xsm={6}
                                        className="encounter-gender-count"
                                      >
                                        <div className="gen-count">
                                          <b id="pat_id">
                                            {femaleCount1 ? femaleCount1 : "0"}
                                          </b>{" "}
                                          <br />
                                          <p id="icon-text1">Female</p>
                                        </div>
                                      </Col>
                                    </Row>
                                  </div>
                                </Col>
                                <Col md={3} className="text-center" id="appo">
                                  <div
                                    className="encounter-count day-visit text-center"
                                    style={{ width: "100%" }}
                                  >
                                    <b id="pat_id">0</b> <br />
                                    <p id="icon-text1">
                                      Average time spent / patient
                                    </p>
                                  </div>
                                </Col>
                                <Col md={3} className="text-center" id="appo">
                                  <div
                                    className="encounter-compare day-visit text-center"
                                    style={{ width: "100%" }}
                                  >
                                    <b id="pat_id" className="encount-yest">
                                      0%{" "}
                                      <i className="bi bi-arrow-down-short"></i>
                                    </b>{" "}
                                    <br />
                                    <p id="icon-text1">
                                      Visits compared to yesterday
                                    </p>
                                  </div>
                                </Col>
                              </Row>
                            </div>
                          </div>
                        </div>
                      </Col>
                    </Row>
                    <React.Fragment>
                      <QueueManagement />
                    </React.Fragment>
                  </div>
                </div>
              )}
              {/* for Medical Officer login */}

              {/* for laboratory login */}
              {typeofuser === "Lab Technician" && (
                <div id="pat_que1">
                  <div id="left_que">
                    <Row>
                      <Col md={11}>
                        <div className="create-icon">
                          <div className="appoint">
                            <div className="medical-officer-div">
                              <Row className="medical-row">
                                <Col md={4} id="appo">
                                  <div
                                    className="text-center encounter-count"
                                    style={{ width: "100%" }}
                                    align="center"
                                  >
                                    <div className="visit-div">
                                      <img
                                        src="../img/icon-appointment.png"
                                        style={{ display: "inline-flex" }}
                                        className="appo-icon"
                                      />
                                      <b id="pat_id">
                                        {totalLabOrders
                                          ? totalLabOrders.length
                                          : "0"}
                                      </b>{" "}
                                      <br />
                                    </div>
                                    <div className="visit-div">
                                      <p id="icon-text1 ">
                                        Lab Order for today
                                      </p>
                                    </div>
                                  </div>
                                </Col>
                                <Col md={4} id="appo">
                                  <div
                                    className="text-center encounter-count"
                                    style={{ width: "100%" }}
                                  >
                                    <Row>
                                      <Col
                                        md={6}
                                        className="encounter-gender-count"
                                      >
                                        <div className="gen-count">
                                          <b id="pat_id">
                                            {maleCount1 ? maleCount1 : "0"}
                                          </b>{" "}
                                          <br />
                                          <p id="icon-text1">Male</p>
                                        </div>
                                      </Col>
                                      <Col
                                        md={6}
                                        className="encounter-gender-count"
                                      >
                                        <div className="gen-count">
                                          <b id="pat_id">
                                            {femaleCount1 ? femaleCount1 : "0"}
                                          </b>{" "}
                                          <br />
                                          <p id="icon-text1">Female</p>
                                        </div>
                                      </Col>
                                    </Row>
                                  </div>
                                </Col>
                                <Col md={4} className="text-center" id="appo">
                                  <div
                                    className="encounter-compare  text-center "
                                    style={{ width: "100%" }}
                                  >
                                    <b id="pat_id" className="encount-yest">
                                      0%{" "}
                                      <i className="bi bi-arrow-down-short"></i>
                                    </b>{" "}
                                    <br />
                                    <p id="icon-text1 ">
                                      Lab orders compared to yesterday
                                    </p>
                                  </div>
                                </Col>
                              </Row>
                            </div>
                          </div>
                        </div>
                      </Col>
                    </Row>
                    <React.Fragment>
                      <QueueManagement />
                    </React.Fragment>
                  </div>
                </div>
              )}
              {/* for laboratory login */}

              {/* for pharcacist login */}
              {typeofuser === "Pharmacist" && (
                <div id="pat_que1">
                  <div id="left_que">
                    <Row>
                      <Col md={11}>
                        <React.Fragment>
                          <QueueManagement />
                        </React.Fragment>
                      </Col>
                    </Row>
                  </div>
                </div>
              )}
              {/* for pharcacist login */}
            </Col>
          </Row>
        </Col>
      </Row>
    </React.Fragment>
  );
}
