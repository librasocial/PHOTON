import React, { useEffect, useState, useCallback } from "react";
import { Badge, Col, Form, Row } from "react-bootstrap";
import moment from "moment";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import { Icon } from "@iconify/react";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Paginations from "../../Paginations";
import { Link, NavLink } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  loadUsers,
  loadDoctorPatdata,
  updatePatDet,
  loadSinglePatDet,
  loadSinglePatient,
} from "../../../redux/actions";
import SignedHealthIDImage from "../SignedHealthIDImage";
import PageLoader from "../../PageLoader";

function Nurse(props) {
  let dispatch = useDispatch();
  const { usersList } = useSelector((state) => state.data);
  const { doctorsList } = useSelector((state) => state.data);
  const { labTechList } = useSelector((state) => state.data);
  const { pharmacistList } = useSelector((state) => state.data);

  const [ptlist, setPtlist] = useState("");
  const [emergenyData, setEmergenyData] = useState("");

  useEffect(() => {
    document.title = "EMR Nurse Dashbord";
    let dashboard_eme = [];
    let dashboard_list = [];
    usersList.map((items) => {
      if (
        items["label"] === "Emergency" &&
        items["status"] == "OnHold" &&
        items["reservationFor"] == "Doctor"
      ) {
        dashboard_eme.push(items);
      }
      if (items["tokenNumber"] !== null && items["label"] != "Emergency") {
        // dashboard_list.push(items)
        if (
          items["status"] == "OnHold" &&
          items["reservationFor"] == "Doctor"
        ) {
          dashboard_list.push(items);
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

  // const ptlistfornurseData = props.ptlistfornurse;
  const ptlistfornurseData = [...emergenyData, ...ptlist];
  const date = new Date();
  let number1 = moment(date).format("YYYY");
  const [types, setTypes] = useState("resevation");

  const [status, setStatus] = useState(false);
  const capture_vitals = (e) => {
    sessionStorage.setItem("ResPateintid", e);
  };

  // Pagination code
  const currentPage = props.currentPage;
  let NUM_OF_RECORDS = ptlistfornurseData.length;
  let LIMIT = 20;
  let ptlistfornurse = ptlistfornurseData.slice(
    (currentPage - 1) * LIMIT,
    (currentPage - 1) * LIMIT + LIMIT
  );

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
      <Row id="pat_que2">
        <Col md={5} className="queue">
          <p className="rec">
            Showing&nbsp;&nbsp; {(currentPage - 1) * LIMIT + 1} -{" "}
            {(currentPage - 1) * LIMIT + parseInt(ptlistfornurse.length)} of{" "}
            {NUM_OF_RECORDS}
          </p>
        </Col>
        <Col md={7} className="drop">
          <Row className="viewing-list">
            <Col md={7}>
              <p id="ViewQue">You are viewing queue for</p>
            </Col>
            <Col md={5}>
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
                {/* {labTechList?.content?.map((item, i) => (
                                    <React.Fragment key={i}>

                                        <option value={item.targetNode?.properties?.uuid} key={i}>&#10003;&nbsp;  {item.targetNode?.properties?.name}</option>
                                    </React.Fragment>
                                ))}
                                {pharmacistList?.content?.map((item, i) => (
                                    <React.Fragment key={i}>

                                        <option value={item.targetNode?.properties?.uuid} key={i}>&#10003;&nbsp;  {item.targetNode?.properties?.name}</option>
                                    </React.Fragment>
                                ))} */}
              </Form.Select>
            </Col>
          </Row>
        </Col>
      </Row>
      {ptlistfornurse.length == 0 ? (
        <React.Fragment>
          <div id="pat_que3" className="empty-img-div">
            <div className="empty-img">
              <img src="../img/dashboard-emty-img.svg" />
            </div>
          </div>
        </React.Fragment>
      ) : (
        <>
          {Object.keys(ptlistfornurse).map((item, index) => (
            <React.Fragment key={index}>
              <div className="box-dsg1">
                <Row id="pat_que3">
                  <Col
                    md={1}
                    align="center"
                    className="d-flex justify-content-center"
                  >
                    <SignedHealthIDImage
                      healthID={ptlistfornurse[item]?.Patient?.healthId}
                    />
                  </Col>
                  <Col md={3} id="pat_det" className="pat-name-id">
                    <div>
                      <p id="pat_name" className="pateintdataforfontsize">
                        {ptlistfornurse[item]?.Patient?.name}
                      </p>
                      <p id="pati_id" className="pateintdataforfontsize">
                        UHID: {ptlistfornurse[item]?.Patient?.UHId}
                      </p>
                      <p id="pati_id" className="pateintdataforfontsize">
                        Health Id:&nbsp;
                        {!ptlistfornurse[item]?.Patient?.healthId ? (
                          ""
                        ) : (
                          <>
                            {ptlistfornurse[item]?.Patient?.healthId.replace(
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
                      {ptlistfornurse[item]?.Patient?.gender}
                    </p>
                  </Col>
                  <Col md={2} id="pat_age">
                    <div>
                      <p className="pateintdataforfontsize">
                        {" "}
                        {number1 -
                          moment(ptlistfornurse[item]?.Patient?.dob).format(
                            "YYYY"
                          )}{" "}
                        Years
                      </p>
                      <p id="pat_year" className="pateintdataforfontsize">
                        {moment(ptlistfornurse[item]?.Patient?.dob).format(
                          "DD MMM YYYY"
                        )}
                      </p>
                    </div>
                  </Col>
                  <Col md={3} id="mob_num">
                    <p className="pateintdataforfontsize">
                      +91-{ptlistfornurse[item]?.Patient?.phone}
                    </p>
                  </Col>
                  <Col md={2} className="dot-div">
                    <div>
                      <div className="btn-group dotBtn-group" align="center">
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
                            {ptlistfornurse[item]?.status == "CheckedIn" ? (
                              ""
                            ) : (
                              <p
                                className="dropdown-item"
                                onClick={(e) =>
                                  handleCancelShow(ptlistfornurse[item]?._id)
                                }
                              >
                                Cancel visit
                              </p>
                            )}
                            <p
                              className="dropdown-item"
                              data-toggle="modal"
                              data-target="#PatientDetails-wndow"
                              onClick={() =>
                                showptdtls(
                                  ptlistfornurse[item]?.Patient?.patientId
                                )
                              }
                            >
                              More details
                            </p>
                          </div>
                        </div>
                      </div>
                      {ptlistfornurse[item]?.reservationFor == "Doctor" && (
                        <p
                          onClick={() => {
                            capture_vitals(ptlistfornurse[item]?._id);
                          }}
                        >
                          <NavLink to="/vitals" className="capturevitals">
                            Capture Vitals
                          </NavLink>
                        </p>
                      )}
                    </div>
                  </Col>
                </Row>
                <hr id="que_sta" />
                <Row id="que_div">
                  <Col md={1} id="sta" align="center">
                    {ptlistfornurse[item]?.label == "Emergency" ? (
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
                      <div
                        align="center"
                        onClick={() =>
                          emergencyEncounter(ptlistfornurse[item]?._id)
                        }
                        style={{ cursor: "pointer" }}
                      >
                        {/* <i className="fa fa-lightbulb-o" aria-hidden="true" ></i> */}
                        <Icon
                          icon="mdi:alarm-light-outline"
                          id="lightoutline"
                        />

                        <p id="pat_sta" className="pateintdataforfontsize">
                          Emergency
                        </p>
                      </div>
                    )}
                  </Col>
                  <Col md={3} id="sta">
                    {ptlistfornurse[item]?.status == "OnHold" ? (
                      <Badge bg="" id="pro">
                        Waiting
                      </Badge>
                    ) : (
                      <React.Fragment>
                        {ptlistfornurse[item]?.status == "CheckedIn" ? (
                          <Badge
                            bg=""
                            style={{
                              backgroundColor: "#071A8E",
                              color: "#FFFFFF",
                            }}
                            id="pro"
                          >
                            Checked-in
                          </Badge>
                        ) : (
                          <React.Fragment>
                            {ptlistfornurse[item]?.label == "Emergency" ? (
                              <Badge bg="" id="pro">
                                Waiting
                              </Badge>
                            ) : (
                              ""
                            )}
                          </React.Fragment>
                        )}{" "}
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
                    <span className="badge1">
                      {ptlistfornurse[item]?.tokenNumber}
                    </span>
                    <p id="pat_tok">Token no.</p>
                  </Col>
                  <Col md={2} id="med">
                    {ptlistfornurse[item].reservationFor == "Lab" ? (
                      <b id="med_off">Lab Technician</b>
                    ) : (
                      <>
                        {ptlistfornurse[item].reservationFor == "Doctor" ? (
                          <b id="med_off">Medical Officer</b>
                        ) : (
                          <b id="med_off">Pharmacist</b>
                        )}
                      </>
                    )}
                    <p id="pat_doc"> {ptlistfornurse[item]?.Provider?.name}</p>
                    <p id="pat_ico">
                      {ptlistfornurse[item].reservationFor == "Lab" ? (
                        <>
                          <i className="fa fa-light fa-flask"></i>&nbsp; To
                          checkup
                        </>
                      ) : (
                        <>
                          <i className="fa fa-stethoscope"></i>&nbsp; To attend
                        </>
                      )}
                    </p>
                  </Col>
                  <Col md={2} id="date">
                    <div className="bokkedtimedate">
                      <p id="visit_time" className="lab-date">
                        {moment(ptlistfornurse[item]?.reservationTime).format(
                          "DD MMM YYYY,  hh:mm A"
                        )}
                      </p>
                      <p id="visit_text">Visit Date, Time</p>
                    </div>
                  </Col>
                  <Col md={2} id="date">
                    {ptlistfornurse[item]?.reservationFor == "Doctor" && (
                      <>
                        {ptlistfornurse[item]?.status == "CheckedIn" ? (
                          <div
                            className="capturevitals"
                            style={{ opacity: 0.4 }}
                          >
                            <i className="bi bi-arrow-right"></i>

                            <p className="capturevitals">
                              Send for Consultation{" "}
                            </p>
                          </div>
                        ) : (
                          <div
                            className="capturevitals"
                            onClick={() =>
                              handleShow(ptlistfornurse[item]?._id)
                            }
                          >
                            <i className="bi bi-arrow-right"></i>

                            <p className="capturevitals">
                              Send for Consultation{" "}
                            </p>
                          </div>
                        )}
                      </>
                    )}
                  </Col>
                </Row>
                {/* 
                                        <hr id="que_sta" /> */}
              </div>
            </React.Fragment>
          ))}
          <>
            {ptlistfornurse.length != 0 && (
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
        </>
      )}
    </React.Fragment>
  );
}

export default Nurse;
