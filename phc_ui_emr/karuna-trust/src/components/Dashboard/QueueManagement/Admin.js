import React, { useState, useCallback } from "react";
import { Badge, Col, Form, Row } from "react-bootstrap";
import moment from "moment";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import { Link, NavLink } from "react-router-dom";
import { Icon } from "@iconify/react";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Paginations from "../../Paginations";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  loadUsers,
  loadDoctorPatdata,
  updatePatDet,
  loadSinglePatDet,
  loadSinglePatient,
} from "../../../redux/actions";
import SignedHealthIDImage from "../SignedHealthIDImage";
import "../../../css/Queue.css";
import PageLoader from "../../PageLoader";

function Admin(props) {
  let dispatch = useDispatch();
  const { usersList } = useSelector((state) => state.data);
  const { doctorsList } = useSelector((state) => state.data);
  const { labTechList } = useSelector((state) => state.data);
  const { pharmacistList } = useSelector((state) => state.data);

  const [ptlist, setPtlist] = useState("");
  const [queuelist, setQueuelist] = useState("");
  const [emergenyData, setEmergenyData] = useState("");
  const [emergencyQueueData, setEmergencyQueueData] = useState("");

  useEffect(() => {
    document.title = "EMR Admin Dashboard";
    let dashboard_eme = [];
    let dashboard_list = [];
    let queue_eme = [];
    let queue_queArray = [];
    if (usersList) {
      usersList.map((items) => {
        if (items["label"] === "Emergency") {
          dashboard_eme.push(items);
        }
        if (
          items["label"] === "Emergency" &&
          items["status"] == "OnHold" &&
          items["reservationFor"] == "Doctor"
        ) {
          queue_eme.push(items);
        }
        if (items["tokenNumber"] !== null && items["label"] != "Emergency") {
          dashboard_list.push(items);

          if (
            items["status"] == "OnHold" &&
            items["reservationFor"] == "Doctor"
          ) {
            queue_queArray.push(items);
          }
        }
      });
      setPtlist(
        dashboard_list.sort((a, b) =>
          parseInt(a.tokenNumber) > parseInt(b.tokenNumber) ? 1 : -1
        )
      );
      setEmergenyData(
        dashboard_eme.sort((a, b) =>
          parseInt(a.tokenNumber) > parseInt(b.tokenNumber) ? 1 : -1
        )
      );
      setEmergencyQueueData(
        queue_eme.sort((a, b) =>
          parseInt(a.tokenNumber) > parseInt(b.tokenNumber) ? 1 : -1
        )
      );
      setQueuelist(
        queue_queArray.sort((a, b) =>
          parseInt(a.tokenNumber) > parseInt(b.tokenNumber) ? 1 : -1
        )
      );
    }
  }, [usersList]);

  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    const interval = setInterval(() => {
      if (!props.selectProvide || props.selectProvide === "all") {
        dispatch(loadUsers(setPageLoader));
      } else {
        dispatch(loadDoctorPatdata(props.selectProvide));
      }
    }, 500);
    return () => clearInterval(interval);
  }, [props.selectProvide]);

  const [status, setStatus] = useState(false);
  const [disp, setDisp] = useState(false);
  const arrayData = [...emergenyData, ...ptlist];
  const QueueArrayData = [...emergencyQueueData, ...queuelist];
  const date = new Date();
  let number1 = moment(date).format("YYYY");
  const [types, setTypes] = useState("resevation");

  const capture_vitals = (e) => {
    sessionStorage.setItem("ResPateintid", e);
  };

  // Pagination code
  const currentPage = props.currentPage;
  let NUM_OF_RECORDS;
  if (props.isdashboard) {
    NUM_OF_RECORDS = arrayData.length;
  } else {
    NUM_OF_RECORDS = QueueArrayData.length;
  }
  // let NUM_OF_RECORDS = arrayData.length;
  let LIMIT = 20;
  let array3;
  let QueueArray;
  if (props.isdashboard) {
    array3 = arrayData.slice(
      (currentPage - 1) * LIMIT,
      (currentPage - 1) * LIMIT + LIMIT
    );
  } else {
    QueueArray = QueueArrayData.slice(
      (currentPage - 1) * LIMIT,
      (currentPage - 1) * LIMIT + LIMIT
    );
  }

  const onPageChanged = useCallback(
    (event, page) => {
      event.preventDefault();
      props.setCurrentPage(page);
    },
    [props.setCurrentPage]
  );
  // Pagination code

  // for emergency

  const emergencyEncounter = (e) => {
    const pateintid = e;
    const PresentTime = new Date();
    let parsedDate = moment(PresentTime, "DD.MM.YYYY H:mm:ss");
    const AssignEmergency = {
      label: "Emergency",
      reservationTime: parsedDate.toISOString(),
    };
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(AssignEmergency),
    };
    dispatch(updatePatDet(pateintid, requestOptions1));
  };
  // for emergency

  //for more details
  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };
  //for more details

  const [showd, setShowd] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const handleCloseModal = () => setShowModal(false);

  const [showCancelModal, setCancelModal] = useState(false);
  const closeCancelVisit = () => setCancelModal(false);
  const [cancelpateintid, setCancelpateintid] = useState("");
  const closeField = () => {
    setShowd("");
    setDisp(true);
  };

  // send for consultation
  const handleShow = (e) => {
    setShowModal(true);
    const resevationofpateint = e;
    dispatch(loadSinglePatDet(resevationofpateint));
  };
  // send for consultation

  // cancel consultation
  function handleCancelShow(e) {
    setCancelpateintid(e);
    setCancelModal(true);
  }
  // cancel consultation

  return (
    <React.Fragment>
      {/* modal */}
      <ModalPopups
        isCheckInShow={showModal}
        checkInClose={handleCloseModal}
        setShowModal={setShowModal}
        isCancelVisitShow={showCancelModal}
        cancelVisitClose={closeCancelVisit}
        cancelpaId={cancelpateintid}
        isPatientDetailsShow={showd}
        patDetailsClose={closeField}
      />
      {/* modal */}
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      {props.isdashboard ? (
        <Row id="pat_que2">
          <Col md={5} className="queue">
            <p className="rec">
              Showing&nbsp;&nbsp; {(currentPage - 1) * LIMIT + 1} -{" "}
              {(currentPage - 1) * LIMIT + parseInt(array3.length)} of{" "}
              {NUM_OF_RECORDS}
            </p>
          </Col>
          <Col md={7} className="drop">
            <Row className="viewing-list">
              <Col md={7}>
                <p id="ViewQue">You are viewing visit for</p>
              </Col>
              <Col md={5} className="provider-list">
                <Form.Select
                  aria-label="Default select example"
                  className="mb3_doc"
                  id="doc_list"
                  onChange={props.selectsearch}
                >
                  <option value="all">&#10003;&nbsp; All</option>
                  {/* <option value="new">&#10003;&nbsp; New</option> */}
                  {doctorsList?.content?.map((item, i) => (
                    <React.Fragment key={i}>
                      <option value={item.targetNode?.properties?.uuid} key={i}>
                        &#10003;&nbsp; {item.targetNode?.properties?.name}
                      </option>
                    </React.Fragment>
                  ))}
                  {labTechList?.content?.map((item, i) => (
                    <React.Fragment key={i}>
                      <option value={item.targetNode?.properties?.uuid} key={i}>
                        &#10003;&nbsp; {item.targetNode?.properties?.name}
                      </option>
                    </React.Fragment>
                  ))}
                  {pharmacistList?.content?.map((item, i) => (
                    <React.Fragment key={i}>
                      <option value={item.targetNode?.properties?.uuid} key={i}>
                        &#10003;&nbsp; {item.targetNode?.properties?.name}
                      </option>
                    </React.Fragment>
                  ))}
                </Form.Select>
              </Col>
            </Row>
          </Col>
        </Row>
      ) : (
        <>
          {" "}
          <Row id="pat_que2">
            <Col md={5} className="queue">
              <p className="rec">
                Showing&nbsp;&nbsp; {(currentPage - 1) * LIMIT + 1} -{" "}
                {(currentPage - 1) * LIMIT + parseInt(QueueArray.length)} of{" "}
                {NUM_OF_RECORDS}
              </p>
            </Col>
            <Col md={7} className="drop">
              <Row className="viewing-list">
                <Col md={7}>
                  <p id="ViewQue">You are viewing queue for</p>
                </Col>
                <Col md={5} className="provider-list">
                  <Form.Select
                    aria-label="Default select example"
                    className="mb3_doc"
                    id="doc_list"
                    onChange={props.selectsearch}
                  >
                    <option value="all">&#10003;&nbsp; All Doctors</option>
                    {/* <option value="new">&#10003;&nbsp; New</option> */}
                    {doctorsList?.content?.map((item, i) => (
                      <React.Fragment key={i}>
                        <option
                          value={item.targetNode?.properties?.uuid}
                          key={i}
                        >
                          &#10003;&nbsp; {item.targetNode?.properties?.name}
                        </option>
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Col>
              </Row>
            </Col>
          </Row>
        </>
      )}
      {props.isdashboard ? (
        <>
          {array3 && array3.length == 0 ? (
            <React.Fragment>
              <div id="pat_que3" className="empty-img-div">
                <div className="empty-img">
                  <img src="../img/dashboard-emty-img.svg" />
                </div>
              </div>
            </React.Fragment>
          ) : (
            <>
              {array3 &&
                Object.keys(array3).map((item, index) => (
                  <React.Fragment key={index}>
                    <div className="box-dsg1">
                      <Row id="pat_que3">
                        <Col
                          md={1}
                          align="center"
                          className="d-flex justify-content-center"
                        >
                          <SignedHealthIDImage
                            healthID={
                              array3[item]?.Patient?.healthId
                                ? array3[item]?.Patient?.healthId
                                : ""
                            }
                          />
                        </Col>
                        <Col md={3} id="pat_det" className="pat-name-id">
                          <div>
                            <p id="pat_name" className="pateintdataforfontsize">
                              {array3[item]?.Patient?.name}
                            </p>
                            <p id="pati_id" className="pateintdataforfontsize">
                              UHID: {array3[item]?.Patient?.UHId}
                            </p>
                            <p id="pati_id" className="pateintdataforfontsize">
                              Health Id:&nbsp;
                              {!array3[item]?.Patient?.healthId ? (
                                ""
                              ) : (
                                <>
                                  {array3[item]?.Patient?.healthId.replace(
                                    /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                    "$1-$2-$3-$4"
                                  )}
                                </>
                              )}
                            </p>
                          </div>
                        </Col>
                        <Col md={1} id="pat_gen">
                          <p className="pateintdataforfontsize">
                            {array3[item]?.Patient?.gender}
                          </p>
                        </Col>
                        <Col md={2} id="pat_age">
                          <div>
                            <p className="pateintdataforfontsize">
                              {" "}
                              {number1 -
                                moment(array3[item]?.Patient?.dob).format(
                                  "YYYY"
                                )}{" "}
                              Years
                            </p>
                            <p id="pat_year" className="pateintdataforfontsize">
                              {moment(array3[item]?.Patient?.dob).format(
                                "DD MMM YYYY"
                              )}
                            </p>
                          </div>
                        </Col>
                        <Col md={3} id="mob_num">
                          <p className="pateintdataforfontsize">
                            +91-{array3[item]?.Patient?.phone}
                          </p>
                        </Col>
                        <Col md={2} className="dot-div">
                          {array3[item]?.status != "Cancelled" && (
                            <div>
                              <div
                                className="btn-group dotBtn-group"
                                align="center"
                              >
                                <button
                                  type="button"
                                  className="btn btn-primary dotBtn"
                                  data-toggle="dropdown"
                                >
                                  <span className="dot"></span>
                                  <span className="dot"></span>
                                  <span className="dot"></span>
                                </button>
                                <div className="dropdown-menu" id="show">
                                  <div className="dot-dropdown-menu">
                                    {array3[item]?.status ==
                                    "startedConsultation" ? (
                                      ""
                                    ) : (
                                      <>
                                        {array3[item]?.status == "CheckedIn" ? (
                                          ""
                                        ) : (
                                          <p
                                            className="dropdown-item"
                                            onClick={(e) =>
                                              handleCancelShow(
                                                array3[item]?._id
                                              )
                                            }
                                          >
                                            Cancel visit
                                          </p>
                                        )}
                                      </>
                                    )}
                                    {array3[item]?.label == "End" ? (
                                      ""
                                    ) : (
                                      <p className="dropdown-item">
                                        <Link
                                          to={`./createEncounter/${array3[item]._id}/${types}`}
                                        >
                                          Edit visit details
                                        </Link>
                                      </p>
                                    )}
                                    <p
                                      className="dropdown-item"
                                      data-toggle="modal"
                                      data-target="#PatientDetails-wndow"
                                      onClick={() =>
                                        showptdtls(
                                          array3[item]?.Patient?.patientId
                                        )
                                      }
                                    >
                                      More details
                                    </p>
                                  </div>
                                </div>
                              </div>
                            </div>
                          )}
                        </Col>
                      </Row>
                      <hr id="que_sta" />
                      <Row id="que_div">
                        <Col md={1} id="sta" align="center">
                          {props.isdashboard ? (
                            <>
                              {array3[item]?.label == "Emergency" ? (
                                <div align="center">
                                  <Icon icon="mdi:alarm-light" id="lightred" />
                                  <p
                                    id="pat_sta"
                                    className="pateintdataforfontsize"
                                    style={{ color: "red" }}
                                  >
                                    Emergency
                                  </p>
                                </div>
                              ) : (
                                <div align="center">
                                  <Icon
                                    icon="mdi:alarm-light-outline"
                                    id="lightoutline"
                                  />
                                  <p
                                    id="pat_sta"
                                    className="pateintdataforfontsize"
                                  >
                                    Emergency
                                  </p>
                                </div>
                              )}
                            </>
                          ) : (
                            <>
                              {array3[item]?.label == "Emergency" ? (
                                <div
                                  align="center"
                                  style={{ cursor: "pointer" }}
                                >
                                  <Icon icon="mdi:alarm-light" id="lightred" />
                                  <p
                                    id="pat_sta"
                                    className="pateintdataforfontsize"
                                    style={{ color: "red" }}
                                  >
                                    Emergency
                                  </p>
                                </div>
                              ) : (
                                <React.Fragment>
                                  {array3[item]?.status ===
                                  "startedConsultation" ? (
                                    <div>
                                      <Icon
                                        icon="mdi:alarm-light-outline"
                                        id="lightoutline"
                                      />
                                      <p
                                        id="pat_sta"
                                        className="pateintdataforfontsize"
                                      >
                                        Emergency
                                      </p>
                                    </div>
                                  ) : (
                                    <div
                                      onClick={() =>
                                        emergencyEncounter(array3[item]?._id)
                                      }
                                      style={{ cursor: "pointer" }}
                                    >
                                      <Icon
                                        icon="mdi:alarm-light-outline"
                                        id="lightoutline"
                                      />
                                      <p
                                        id="pat_sta"
                                        className="pateintdataforfontsize"
                                      >
                                        Emergency
                                      </p>
                                    </div>
                                  )}
                                </React.Fragment>
                              )}
                            </>
                          )}
                        </Col>
                        <Col md={3} id="sta">
                          {array3[item]?.status == "Cancelled" ? (
                            <Badge
                              bg=""
                              id="pro"
                              style={{
                                backgroundColor: "#b9430a",
                                color: "#fff",
                              }}
                            >
                              Cancelled Consultation
                            </Badge>
                          ) : (
                            <>
                              {array3[item]?.label == "End" ? (
                                <Badge
                                  bg=""
                                  id="pro"
                                  style={{ backgroundColor: "#0ab928" }}
                                >
                                  Consultation Completed
                                </Badge>
                              ) : (
                                <>
                                  {array3[item]?.status == "OnHold" ? (
                                    <Badge bg="" id="pro">
                                      {" "}
                                      Waiting{" "}
                                    </Badge>
                                  ) : (
                                    <React.Fragment>
                                      {array3[item]?.status == "CheckedIn" ? (
                                        <Badge
                                          bg=""
                                          style={{
                                            backgroundColor: "#071A8E",
                                            color: "#FFFFFF",
                                          }}
                                          id="pro"
                                        >
                                          {" "}
                                          Checked-in{" "}
                                        </Badge>
                                      ) : (
                                        <React.Fragment>
                                          {array3[item]?.label == "Emergency" &&
                                          array3[item]?.status ==
                                            "CheckedIn" ? (
                                            <Badge bg="" id="pro">
                                              {" "}
                                              Waiting{" "}
                                            </Badge>
                                          ) : (
                                            <React.Fragment>
                                              {array3[item]?.status ==
                                              "startedConsultation" ? (
                                                <Badge id="pro" bg="warning">
                                                  {" "}
                                                  Consultation in progress{" "}
                                                </Badge>
                                              ) : (
                                                ""
                                              )}
                                            </React.Fragment>
                                          )}
                                        </React.Fragment>
                                      )}
                                    </React.Fragment>
                                  )}
                                </>
                              )}
                            </>
                          )}
                          <p id="pat_sta">Status</p>
                        </Col>
                        <Col md={1} className="tok">
                          <div>
                            <span className="badge1">
                              {array3[item]?.tokenNumber}
                            </span>
                            <p id="pat_tok">Token no.</p>
                          </div>
                        </Col>
                        <Col md={3} id="med">
                          <div>
                            {array3[item].reservationFor == "Lab" ? (
                              <b id="med_off">Lab Technician</b>
                            ) : (
                              <>
                                {array3[item].reservationFor == "Pharmacy" ? (
                                  <b id="med_off">Pharmacist</b>
                                ) : (
                                  <b id="med_off">Medical Officer</b>
                                )}
                              </>
                            )}
                            <p id="pat_doc"> {array3[item]?.Provider?.name}</p>
                            <p id="pat_ico">
                              {array3[item].reservationFor == "Lab" ? (
                                <>
                                  <i className="fa fa-light fa-flask"></i>&nbsp;
                                  To checkup
                                </>
                              ) : (
                                <>
                                  {array3[item].reservationFor == "Pharmacy" ? (
                                    <>
                                      <i className="fa fa-plus-square"></i>
                                      &nbsp; To deliver
                                    </>
                                  ) : (
                                    <>
                                      <i className="fa fa-stethoscope"></i>
                                      &nbsp; To attend
                                    </>
                                  )}
                                </>
                              )}
                            </p>
                          </div>
                        </Col>
                        <Col md={3} id="date">
                          <div>
                            <p id="visit_time" className="lab-date">
                              {moment(array3[item]?.reservationTime).format(
                                "DD MMM YYYY,  hh:mm A"
                              )}
                            </p>
                            <p id="visit_text">Visit Date, Time</p>
                          </div>
                        </Col>
                        <Col md={1} id="date"></Col>
                      </Row>
                    </div>
                  </React.Fragment>
                ))}
            </>
          )}
        </>
      ) : (
        <>
          {QueueArray && QueueArray.length == 0 ? (
            <React.Fragment>
              <div id="pat_que3" className="empty-img-div">
                <div className="empty-img">
                  <img src="../img/dashboard-emty-img.svg" />
                </div>
              </div>
            </React.Fragment>
          ) : (
            <>
              {QueueArray &&
                Object.keys(QueueArray).map((item, index) => (
                  <React.Fragment key={index}>
                    <div className="box-dsg1">
                      <Row id="pat_que3">
                        <Col
                          md={1}
                          align="center"
                          className="d-flex justify-content-center"
                        >
                          <SignedHealthIDImage
                            healthID={QueueArray[item]?.Patient?.healthId}
                          />
                        </Col>
                        <Col md={3} id="pat_det" className="pat-name-id">
                          <div>
                            <p id="pat_name" className="pateintdataforfontsize">
                              {QueueArray[item]?.Patient?.name}
                            </p>
                            <p id="pati_id" className="pateintdataforfontsize">
                              UHID: {QueueArray[item]?.Patient?.UHId}
                            </p>
                            <p id="pati_id" className="pateintdataforfontsize">
                              Health Id:&nbsp;
                              {!QueueArray[item]?.Patient?.healthId ? (
                                ""
                              ) : (
                                <>
                                  {QueueArray[item]?.Patient?.healthId.replace(
                                    /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                    "$1-$2-$3-$4"
                                  )}
                                </>
                              )}
                            </p>
                          </div>
                        </Col>

                        <Col md={1} id="pat_gen">
                          <p className="pateintdataforfontsize">
                            {QueueArray[item]?.Patient?.gender}
                          </p>
                        </Col>
                        <Col md={2} id="pat_age">
                          <div>
                            <p className="pateintdataforfontsize">
                              {" "}
                              {number1 -
                                moment(QueueArray[item]?.Patient?.dob).format(
                                  "YYYY"
                                )}{" "}
                              Years
                            </p>
                            <p id="pat_year" className="pateintdataforfontsize">
                              {moment(QueueArray[item]?.Patient?.dob).format(
                                "DD MMM YYYY"
                              )}
                            </p>
                          </div>
                        </Col>
                        <Col md={3} id="mob_num">
                          <p className="pateintdataforfontsize">
                            +91-{QueueArray[item]?.Patient?.phone}
                          </p>
                        </Col>
                        <Col md={2} className="dot-div">
                          <div>
                            <div
                              className="btn-group dotBtn-group"
                              align="center"
                            >
                              <button
                                type="button"
                                className="btn btn-primary dotBtn"
                                data-toggle="dropdown"
                              >
                                <span className="dot"></span>
                                <span className="dot"></span>
                                <span className="dot"></span>
                              </button>
                              <div className="dropdown-menu" id="show">
                                <div className="dot-dropdown-menu">
                                  {QueueArray[item]?.status ==
                                  "startedConsultation" ? (
                                    ""
                                  ) : (
                                    <>
                                      {QueueArray[item]?.status ==
                                      "CheckedIn" ? (
                                        ""
                                      ) : (
                                        <p
                                          className="dropdown-item"
                                          onClick={(e) =>
                                            handleCancelShow(
                                              QueueArray[item]?._id
                                            )
                                          }
                                        >
                                          Cancel visit
                                        </p>
                                      )}
                                    </>
                                  )}

                                  <p className="dropdown-item">
                                    <Link
                                      to={`./createEncounter/${QueueArray[item]._id}/${types}`}
                                    >
                                      Edit visit details
                                    </Link>
                                  </p>
                                  <p
                                    className="dropdown-item"
                                    data-toggle="modal"
                                    data-target="#PatientDetails-wndow"
                                    onClick={() =>
                                      showptdtls(
                                        QueueArray[item]?.Patient?.patientId
                                      )
                                    }
                                  >
                                    More details
                                  </p>
                                </div>
                              </div>
                            </div>
                            <p
                              onClick={() => {
                                capture_vitals(QueueArray[item]?._id);
                              }}
                            >
                              <NavLink to="/vitals" className="capturevitals">
                                Capture Vitals
                              </NavLink>
                            </p>
                          </div>
                        </Col>
                      </Row>
                      <hr id="que_sta" />
                      <Row id="que_div">
                        <Col md={1} id="sta" align="center">
                          <>
                            {QueueArray[item]?.label == "Emergency" ? (
                              <div align="center" style={{ cursor: "pointer" }}>
                                <Icon icon="mdi:alarm-light" id="lightred" />

                                <p
                                  id="pat_sta"
                                  className="pateintdataforfontsize"
                                  style={{ color: "red" }}
                                >
                                  Emergency
                                </p>
                              </div>
                            ) : (
                              <React.Fragment>
                                {QueueArray[item]?.status ===
                                "startedConsultation" ? (
                                  <div align="center">
                                    <Icon
                                      icon="mdi:alarm-light-outline"
                                      id="lightoutline"
                                    />

                                    <p
                                      id="pat_sta"
                                      className="pateintdataforfontsize"
                                    >
                                      Emergency
                                    </p>
                                  </div>
                                ) : (
                                  <div
                                    align="center"
                                    onClick={() =>
                                      emergencyEncounter(QueueArray[item]?._id)
                                    }
                                    style={{ cursor: "pointer" }}
                                  >
                                    <Icon
                                      icon="mdi:alarm-light-outline"
                                      id="lightoutline"
                                    />

                                    <p
                                      id="pat_sta"
                                      className="pateintdataforfontsize"
                                    >
                                      Emergency
                                    </p>
                                  </div>
                                )}
                              </React.Fragment>
                            )}
                          </>
                        </Col>
                        <Col md={3} id="sta">
                          {QueueArray[item]?.status == "OnHold" ? (
                            <Badge bg="" id="pro">
                              {" "}
                              Waiting{" "}
                            </Badge>
                          ) : (
                            <React.Fragment>
                              {QueueArray[item]?.status == "CheckedIn" ? (
                                <Badge
                                  bg=""
                                  style={{
                                    backgroundColor: "#071A8E",
                                    color: "#FFFFFF",
                                  }}
                                  id="pro"
                                >
                                  {" "}
                                  Checked-in{" "}
                                </Badge>
                              ) : (
                                <React.Fragment>
                                  {QueueArray[item]?.label == "Emergency" &&
                                  QueueArray[item]?.status == "CheckedIn" ? (
                                    <Badge bg="" id="pro">
                                      {" "}
                                      Waiting{" "}
                                    </Badge>
                                  ) : (
                                    <React.Fragment>
                                      {QueueArray[item]?.status ==
                                      "startedConsultation" ? (
                                        <Badge id="pro" bg="warning">
                                          {" "}
                                          Consultation in progress{" "}
                                        </Badge>
                                      ) : (
                                        ""
                                      )}
                                    </React.Fragment>
                                  )}
                                </React.Fragment>
                              )}
                            </React.Fragment>
                          )}
                          <p id="pat_sta">Status</p>
                        </Col>
                        <Col md={1} className="tok">
                          <span className="badge1">
                            {(currentPage - 1) * LIMIT + (index + 1)}
                          </span>
                          <p id="pat_tok">Queue no.</p>
                        </Col>
                        <Col md={1} className="tok">
                          {/* <span className="badge1">{ptlist[item].tokenNumber}</span> */}
                          <span className="badge1">
                            {QueueArray[item]?.tokenNumber}
                          </span>
                          <p id="pat_tok">Token no.</p>
                        </Col>
                        <Col md={2} id="med">
                          {QueueArray[item].reservationFor == "Lab" ? (
                            <b id="med_off">Lab Technician</b>
                          ) : (
                            <b id="med_off">Medical Officer</b>
                          )}
                          <p id="pat_doc">
                            {" "}
                            {QueueArray[item]?.Provider?.name}
                          </p>
                          <p id="pat_ico">
                            <i className="fa fa-stethoscope"></i>&nbsp; To
                            attend
                          </p>
                        </Col>
                        <Col md={2} id="date">
                          <div className="bokkedtimedate">
                            <p id="visit_time" className="lab-date">
                              {moment(QueueArray[item]?.reservationTime).format(
                                "DD MMM YYYY,  hh:mm A"
                              )}
                            </p>
                            <p id="visit_text">Visit Date, Time</p>
                          </div>
                        </Col>
                        <Col md={2} id="date">
                          <div
                            className="capturevitals"
                            onClick={() => handleShow(QueueArray[item]?._id)}
                          >
                            <i className="bi bi-arrow-right"></i>

                            <p className="capturevitals">
                              Send for Consultation{" "}
                            </p>
                          </div>
                        </Col>
                      </Row>
                    </div>
                  </React.Fragment>
                ))}
            </>
          )}
        </>
      )}
      {props.isdashboard ? (
        <>
          {array3 && array3.length != 0 && (
            <div className="pagination-wrapper">
              <Paginations
                totalRecords={NUM_OF_RECORDS}
                pageLimit={LIMIT}
                pageNeighbours={1}
                onPageChanged={onPageChanged}
                currentPage={currentPage}
              />
            </div>
          )}
        </>
      ) : (
        <>
          {QueueArray && QueueArray.length != 0 ? (
            <div className="pagination-wrapper">
              <Paginations
                totalRecords={NUM_OF_RECORDS}
                pageLimit={LIMIT}
                pageNeighbours={1}
                onPageChanged={onPageChanged}
                currentPage={currentPage}
              />
            </div>
          ) : (
            ""
          )}
        </>
      )}
    </React.Fragment>
  );
}

export default Admin;
