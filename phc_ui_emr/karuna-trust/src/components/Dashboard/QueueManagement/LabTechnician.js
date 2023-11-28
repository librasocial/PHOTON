import React, { useEffect, useState, useCallback } from "react";
import { Badge, Col, Form, Row, Tabs, Tab } from "react-bootstrap";
import moment from "moment";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Paginations from "../../Paginations";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import { NavLink } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  loadUsers,
  loadLabOrders,
  loadSinglePatient,
} from "../../../redux/actions";
import SignedHealthIDImage from "../SignedHealthIDImage";
import PageLoader from "../../PageLoader";

function LabTechnician(props) {
  let dispatch = useDispatch();
  let doctorid = sessionStorage.getItem("userid");
  const { usersList } = useSelector((state) => state.data);

  const { labOrderList } = useSelector((state) => state.data);

  const [labPtlist, setLabPtlist] = useState([]);
  const [labEmergenyData, setLabEmergenyData] = useState([]);
  useEffect(() => {
    document.title = "EMR Lab Technician Dashbord";
    let tmpArray = [];
    let emergency = [];
    usersList.map((items) => {
      if (items["label"] === "Emergency") {
        emergency.push({
          Patient: {
            UHId: items["Patient"]["UHId"],
            dob: items["Patient"]["dob"],
            gender: items["Patient"]["gender"],
            healthId: items["Patient"]["healthId"],
            name: items["Patient"]["name"],
            patientId: items["Patient"]["patientId"],
            phone: items["Patient"]["phone"],
          },
          medicalTests: [],
          Provider: {
            memberId: items["Provider"]["memberId"],
            memberName: items["Provider"]["name"],
          },
          isStat: "",
          encounterId: items["encounterId"],
          orderDate: items["reservationTime"],
          // reservationFor: items,
          originatedBy: "",
          assignedToLab: "",
          status: items["reservationFor"],
          patres_Id: items["Patient"]["patientId"],
          pat_create_id: items["_id"],
        });
      }

      if (items["tokenNumber"] !== null && items["label"] != "Emergency") {
        // tmpArray.push(items)
        tmpArray.push({
          Patient: {
            UHId: items["Patient"]["UHId"],
            dob: items["Patient"]["dob"],
            gender: items["Patient"]["gender"],
            healthId: items["Patient"]["healthId"],
            name: items["Patient"]["name"],
            patientId: items["Patient"]["patientId"],
            phone: items["Patient"]["phone"],
          },
          medicalTests: [],
          Provider: {
            memberId: items["Provider"]["memberId"],
            memberName: items["Provider"]["name"],
          },
          isStat: "",
          encounterId: items["encounterId"],
          orderDate: items["reservationTime"],
          // reservationFor: items,
          originatedBy: "",
          assignedToLab: "",
          status: items["reservationFor"],
          patres_Id: items["Patient"]["patientId"],
          pat_create_id: items["_id"],
        });
      }
    });
    setLabPtlist(
      tmpArray.sort((a, b) =>
        parseInt(a.orderDate) > parseInt(b.orderDate) ? 1 : -1
      )
    );
    setLabEmergenyData(
      emergency.sort((a, b) =>
        parseInt(a.orderDate) > parseInt(b.orderDate) ? 1 : -1
      )
    );

    let labOrderArray = [];
    labOrderList.map((items) => {
      labOrderArray.push({
        Patient: {
          UHId: items["patient"]["uhid"],
          dob: items["patient"]["dob"],
          gender: items["patient"]["gender"],
          healthId: items["patient"]["healthId"],
          name: items["patient"]["name"],
          patientId: items["patient"]["patientId"],
          phone: items["patient"]["phone"],
        },
        medicalTests: items["medicalTests"],
        Provider: {
          memberId: items["encounter"]["staffId"],
          memberName: items["encounter"]["staffName"],
        },
        isStat: items["isStat"],
        encounterId: items["encounter"]["encounterId"],
        orderDate: items["orderDate"],
        originatedBy: items["originatedBy"],
        assignedToLab: items["assignedToLab"],
        status: items["status"],
        orderPat_Id: items["patient"]["patientId"],
        Laborder_Id: items["id"],
        pat_create_id: items["patient"]["citizenId"],
      });
    });
    setLabOrders(labOrderArray);
  }, [usersList, labOrderList]);

  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    if (props.searchStatus == false) {
      const interval = setInterval(() => {
        dispatch(loadUsers(doctorid, setPageLoader));
      }, 5000);
      return () => clearInterval(interval);
    }
  }, [doctorid, props.searchStatus]);

  // fetching all lab oeder data
  let filteredResult = props.filteredResults;

  // Pagination code
  const [currentPage, setCurrentPage] = useState(1);

  let NUM_OF_RECORDS = filteredResult.length;
  let LIMIT = 20;
  const filteredResults = filteredResult.slice(
    (currentPage - 1) * LIMIT,
    (currentPage - 1) * LIMIT + LIMIT
  );

  const onPageChanged = useCallback(
    (event, page) => {
      event.preventDefault();
      setCurrentPage(page);
    },
    [setCurrentPage]
  );
  // Pagination code

  // let typeofuser = sessionStorage.getItem('typeofuser');
  const [labOrders, setLabOrders] = useState([]);
  const date = new Date();
  let number1 = moment(date).format("YYYY");
  const [status, setStatus] = useState(false);
  const lab_service = (patId, resId) => {
    sessionStorage.setItem("LabPateintid", patId);
    sessionStorage.setItem("LabResid", resId);
  };

  const lab_ByOrderId = (patId, resId) => {
    sessionStorage.setItem("LabPateintid", patId);
    sessionStorage.setItem("LabResid", resId);
  };

  const collect_sample = (e) => {
    sessionStorage.setItem("LabOrderId", e);
    sessionStorage.setItem("cancelStatus", "true");
  };
  const lab_ordered = (e) => {
    sessionStorage.setItem("LabOrderId", e);
  };

  let result2 = labPtlist.filter((person) =>
    labOrders.every(
      (person2) => !person2.Patient.patientId.includes(person.Patient.patientId)
    )
  );

  const allOrderdata = [...result2, ...labOrders];

  // Pagination code
  const [currentPage1, setCurrentPage1] = useState(1);
  let NUM_OF_RECORDS1 = allOrderdata.length;

  let LIMIT1 = 20;
  const alldata = allOrderdata.slice(
    (currentPage1 - 1) * LIMIT1,
    (currentPage1 - 1) * LIMIT1 + LIMIT1
  );

  const onPageChanged1 = useCallback(
    (event, page) => {
      event.preventDefault();
      setCurrentPage1(page);
    },
    [setCurrentPage1]
  );
  // Pagination code

  //for more details
  const [showd, setShowd] = useState(false);

  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };
  //for more details

  const closeField = () => {
    setShowd(false);
  };

  return (
    <React.Fragment>
      {/* modal */}
      <ModalPopups isPatientDetailsShow={showd} patDetailsClose={closeField} />
      {/* modal */}
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="mobile-view">
        <h1 className="dash-mnag-list">List of Lab orders for today </h1>
        <p className="rec">
          Showing&nbsp;&nbsp;{" "}
          {props.searchValue != 0 ? (
            <>
              {(currentPage - 1) * LIMIT + 1} -{" "}
              {(currentPage - 1) * LIMIT + parseInt(filteredResult.length)} of{" "}
              {NUM_OF_RECORDS}
            </>
          ) : (
            <>
              {(currentPage - 1) * LIMIT + 1} -{" "}
              {(currentPage - 1) * LIMIT + parseInt(alldata.length)} of{" "}
              {NUM_OF_RECORDS1}
            </>
          )}
        </p>
      </div>
      {!props.searchValue && alldata.length == 0 ? (
        <React.Fragment>
          <div id="pat_que3" className="empty-img-div">
            <div className="empty-img">
              <img src="../img/dashboard-emty-img.svg" />
            </div>
          </div>
        </React.Fragment>
      ) : (
        <>
          {props.searchValue ? (
            <>
              {filteredResults.length == 0 ? (
                <p align="center">No result found</p>
              ) : (
                <>
                  {filteredResults.map((item, index) => (
                    <Row key={index}>
                      <Col md={11}>
                        <div className="box-lob">
                          <Row>
                            <Col md={10}>
                              <Row id="pat_que3">
                                <Col
                                  md={1}
                                  align="center"
                                  className="d-flex justify-content-center"
                                >
                                  <SignedHealthIDImage
                                    healthID={item?.patient?.healthId}
                                  />
                                </Col>
                                <Col md={4} id="pat_det">
                                  <p
                                    id="pat_name"
                                    className="pateintdataforfontsize lab_det"
                                  >
                                    {item?.patient?.name}
                                  </p>
                                  <p
                                    id="pati_id"
                                    className="pateintdataforfontsize"
                                  >
                                    UHID: {item?.patient?.uhid}
                                  </p>
                                  <p
                                    id="pati_id"
                                    className="pateintdataforfontsize"
                                  >
                                    Health ID:&nbsp;
                                    {!item?.patient?.healthId ? (
                                      ""
                                    ) : (
                                      <>
                                        {item?.patient?.healthId.replace(
                                          /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                          "$1-$2-$3-$4"
                                        )}
                                      </>
                                    )}
                                  </p>
                                </Col>
                                <Col md={1} id="pat_gen">
                                  <p className="pateintdataforfontsize">
                                    {item?.patient?.gender}
                                  </p>
                                </Col>
                                <Col md={2} id="pat_age">
                                  <div>
                                    <p className="pateintdataforfontsize">
                                      {number1 -
                                        moment(item?.patient?.dob).format(
                                          "YYYY"
                                        )}{" "}
                                      Years
                                    </p>
                                    <p
                                      id="pat_year"
                                      className="pateintdataforfontsize"
                                    >
                                      {moment(item?.patient?.dob).format(
                                        "DD MMM YYYY"
                                      )}
                                    </p>
                                  </div>
                                </Col>
                                <Col md={3} id="mob_num">
                                  <p className="pateintdataforfontsize">
                                    +91-{item?.patient?.phone}
                                  </p>
                                </Col>
                              </Row>
                              <hr id="que_sta" />
                              <Row id="que_div">
                                <Col md={1} id="sta" align="center">
                                  {item?.isStat == true && (
                                    <Badge bg="primary" id="eme_sta">
                                      STAT
                                    </Badge>
                                  )}
                                </Col>
                                <Col md={4} id="sta">
                                  {item.status == "RESULTS_ENTERED" ? (
                                    <Badge bg="primary" id="lab_status">
                                      {" "}
                                      Report Ready{" "}
                                    </Badge>
                                  ) : (
                                    <>
                                      {item.status == "SAMPLE_COLLECTED" ? (
                                        <Badge bg="primary" id="lab_status">
                                          {" "}
                                          Sample Collected{" "}
                                        </Badge>
                                      ) : (
                                        <>
                                          {item.originatedBy == "Internal" ? (
                                            <Badge bg="primary" id="lab_status">
                                              {" "}
                                              Lab Service Ordered{" "}
                                            </Badge>
                                          ) : (
                                            <Badge bg="primary" id="lab_status">
                                              {" "}
                                              Registered for Lab Service{" "}
                                            </Badge>
                                          )}
                                        </>
                                      )}
                                    </>
                                  )}
                                  <p id="pat_sta">Status</p>
                                </Col>
                                <Col md={1} className="tok">
                                  <Badge bg="primary" id="lab_token">
                                    {item?.assignedToLab}
                                  </Badge>
                                  <p id="pat_sta">Lab No.</p>
                                </Col>
                                <Col md={2} id="med" className="lab-queue">
                                  <p>
                                    <b id="med_off">
                                      {item.originatedBy == "Internal"
                                        ? "Referred by"
                                        : "Ordered by"}{" "}
                                    </b>
                                  </p>
                                  <p>
                                    {item?.originatedBy == "Internal" ? (
                                      <b id="med_off">Medical Officer</b>
                                    ) : (
                                      <b id="med_off">Lab Technician</b>
                                    )}
                                  </p>
                                  <p id="pat_doc">
                                    {item?.encounter?.staffName}
                                  </p>
                                </Col>
                                <Col md={3} id="date">
                                  <div className="bokkedtimedate">
                                    <p id="visit_time" className="lab-date">
                                      {moment(item?.audit?.dateCreated).format(
                                        "DD MMM YYYY, hh:mm A"
                                      )}
                                    </p>
                                    <p id="visit_text">
                                      {item?.originatedBy == "Internal"
                                        ? "Visit Date, Time"
                                        : "Ordered Date, Time"}
                                    </p>
                                  </div>
                                </Col>
                              </Row>
                              {/* </div> */}
                            </Col>

                            <Col md={2}>
                              <div className="box-dsg2 lab-data-row-2">
                                <div className="lab-btn" align="center">
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
                                      <p
                                        className="dropdown-item"
                                        data-toggle="modal"
                                        data-target="#PatientDetails-wndow"
                                        onClick={() =>
                                          showptdtls(item?.patient?.patientId)
                                        }
                                      >
                                        More details
                                      </p>
                                    </div>
                                  </div>
                                </div>
                                <div className="dot-div lab-order">
                                  <div className="my-navlink">
                                    <>
                                      {item.status == "ORDER_ACCEPTED" ? (
                                        <>
                                          <p
                                            className="lab-order-link"
                                            style={{ opacity: "0.5" }}
                                          >
                                            {" "}
                                            Order lab services{" "}
                                          </p>
                                          <p
                                            className="lab-order-link"
                                            onClick={() =>
                                              collect_sample(item.id)
                                            }
                                          >
                                            <NavLink to="/collect-sample">
                                              {" "}
                                              <b>Collect sample </b>
                                            </NavLink>
                                          </p>
                                          <p
                                            className="lab-order-link"
                                            style={{ opacity: "0.5" }}
                                          >
                                            {" "}
                                            Enter result{" "}
                                          </p>
                                          <p
                                            className="lab-order-link"
                                            style={{ opacity: "0.5" }}
                                          >
                                            {" "}
                                            View report{" "}
                                          </p>
                                        </>
                                      ) : (
                                        <>
                                          {item.status == "SAMPLE_COLLECTED" ? (
                                            <>
                                              <p
                                                className="lab-order-link"
                                                style={{ opacity: "0.5" }}
                                              >
                                                {" "}
                                                Order lab services{" "}
                                              </p>
                                              <p
                                                className="lab-order-link"
                                                style={{ opacity: "0.5" }}
                                              >
                                                {" "}
                                                Collect sample{" "}
                                              </p>
                                              <p
                                                className="lab-order-link"
                                                onClick={() =>
                                                  lab_ordered(item.id)
                                                }
                                              >
                                                <NavLink to="/enter-result">
                                                  {" "}
                                                  <b>Enter result</b>
                                                </NavLink>
                                              </p>
                                              <p
                                                className="lab-order-link"
                                                style={{ opacity: "0.5" }}
                                              >
                                                {" "}
                                                View report{" "}
                                              </p>
                                            </>
                                          ) : (
                                            <>
                                              {item.status ==
                                              "RESULTS_ENTERED" ? (
                                                <>
                                                  <p
                                                    className="lab-order-link"
                                                    style={{ opacity: "0.5" }}
                                                  >
                                                    {" "}
                                                    Order lab services{" "}
                                                  </p>
                                                  <p
                                                    className="lab-order-link"
                                                    style={{ opacity: "0.5" }}
                                                  >
                                                    {" "}
                                                    Collect sample{" "}
                                                  </p>
                                                  <p
                                                    className="lab-order-link"
                                                    style={{ opacity: "0.5" }}
                                                  >
                                                    {" "}
                                                    Enter result{" "}
                                                  </p>
                                                  <p
                                                    className="lab-order-link"
                                                    onClick={() =>
                                                      lab_ordered(item.id)
                                                    }
                                                  >
                                                    <NavLink to="/view-report">
                                                      <b>View report</b>
                                                    </NavLink>
                                                  </p>
                                                </>
                                              ) : (
                                                <>
                                                  {item.status ==
                                                  "DELIVERED" ? (
                                                    <>
                                                      <p
                                                        className="lab-order-link"
                                                        style={{
                                                          opacity: "0.5",
                                                        }}
                                                      >
                                                        {" "}
                                                        Order lab services{" "}
                                                      </p>
                                                      <p
                                                        className="lab-order-link"
                                                        style={{
                                                          opacity: "0.5",
                                                        }}
                                                      >
                                                        {" "}
                                                        Collect sample{" "}
                                                      </p>
                                                      <p
                                                        className="lab-order-link"
                                                        style={{
                                                          opacity: "0.5",
                                                        }}
                                                      >
                                                        {" "}
                                                        Enter result{" "}
                                                      </p>
                                                      <p
                                                        className="lab-order-link"
                                                        style={{
                                                          opacity: "0.5",
                                                        }}
                                                      >
                                                        {" "}
                                                        View report{" "}
                                                      </p>
                                                    </>
                                                  ) : (
                                                    <></>
                                                  )}
                                                </>
                                              )}
                                            </>
                                          )}
                                        </>
                                      )}
                                    </>
                                  </div>
                                </div>
                              </div>
                            </Col>
                          </Row>
                        </div>
                      </Col>
                    </Row>
                  ))}
                  <div className="pagination-wrapper">
                    <Paginations
                      totalRecords={NUM_OF_RECORDS}
                      pageLimit={LIMIT}
                      pageNeighbours={1}
                      onPageChanged={onPageChanged}
                      currentPage={currentPage}
                    />
                  </div>
                </>
              )}
            </>
          ) : (
            <>
              {Object.keys(alldata).map((item, index) => (
                <Row key={index}>
                  <Col md={11}>
                    <div className="box-lob">
                      <Row>
                        <Col md={10}>
                          <Row id="pat_que3">
                            <Col
                              md={1}
                              align="center"
                              className="d-flex justify-content-center"
                            >
                              <SignedHealthIDImage
                                healthID={alldata[item]?.Patient?.healthId}
                              />
                            </Col>
                            <Col md={4} id="pat_det">
                              <p
                                id="pat_name"
                                className="pateintdataforfontsize lab_det"
                              >
                                {alldata[item]?.Patient?.name}
                              </p>
                              <p
                                id="pati_id"
                                className="pateintdataforfontsize"
                              >
                                UHID: {alldata[item]?.Patient?.UHId}
                              </p>
                              <p
                                id="pati_id"
                                className="pateintdataforfontsize"
                              >
                                Health ID:&nbsp;
                                {!alldata[item]?.Patient?.healthId ? (
                                  ""
                                ) : (
                                  <>
                                    {alldata[item]?.Patient?.healthId.replace(
                                      /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                      "$1-$2-$3-$4"
                                    )}
                                  </>
                                )}
                              </p>
                            </Col>
                            <Col md={1} id="pat_gen">
                              <p className="pateintdataforfontsize">
                                {alldata[item]?.Patient?.gender}
                              </p>
                            </Col>
                            <Col md={2} id="pat_age">
                              <div>
                                <p className="pateintdataforfontsize">
                                  {number1 -
                                    moment(alldata[item]?.Patient?.dob).format(
                                      "YYYY"
                                    )}{" "}
                                  Years
                                </p>
                                <p
                                  id="pat_year"
                                  className="pateintdataforfontsize"
                                >
                                  {moment(alldata[item]?.Patient?.dob).format(
                                    "DD MMM YYYY"
                                  )}
                                </p>
                              </div>
                            </Col>
                            <Col md={3} id="mob_num">
                              <p className="pateintdataforfontsize">
                                +91-{alldata[item]?.Patient?.phone}
                              </p>
                            </Col>
                          </Row>
                          <hr id="que_sta" />
                          <Row id="que_div">
                            <Col md={1} id="sta" align="center">
                              {alldata[item].isStat == true && (
                                <Badge bg="primary" id="eme_sta">
                                  STAT
                                </Badge>
                              )}
                            </Col>
                            <Col md={4} id="sta">
                              {/* {alldata[item].status == "Lab" ?
                                                            <Badge bg="primary" id="lab_status"> Registered for Lab Service </Badge> : ""} */}
                              {alldata[item].status == "RESULTS_ENTERED" ? (
                                <Badge bg="primary" id="lab_status">
                                  {" "}
                                  Report Ready{" "}
                                </Badge>
                              ) : (
                                <>
                                  {alldata[item].status ==
                                  "SAMPLE_COLLECTED" ? (
                                    <Badge bg="primary" id="lab_status">
                                      {" "}
                                      Sample Collected{" "}
                                    </Badge>
                                  ) : (
                                    <>
                                      {alldata[item].originatedBy ==
                                      "Internal" ? (
                                        <Badge bg="primary" id="lab_status">
                                          {" "}
                                          Lab Service Ordered{" "}
                                        </Badge>
                                      ) : (
                                        <Badge bg="primary" id="lab_status">
                                          {" "}
                                          Registered for Lab Service{" "}
                                        </Badge>
                                      )}
                                    </>
                                  )}
                                </>
                              )}
                              <p id="pat_sta">Status</p>
                            </Col>
                            <Col md={1} className="tok">
                              {alldata[item]?.assignedToLab == "" ? (
                                <Badge bg="primary" id="dot">
                                  <p></p>
                                </Badge>
                              ) : (
                                <Badge bg="primary" id="lab_token">
                                  {alldata[item]?.assignedToLab}
                                </Badge>
                              )}
                              <p id="pat_sta">Lab No.</p>
                            </Col>
                            <Col md={2} id="med" className="lab-queue">
                              <p>
                                <b id="med_off">
                                  {" "}
                                  {alldata[item].status == "Lab" ||
                                  alldata[item].originatedBy == "External"
                                    ? "Ordered by"
                                    : "Referred by"}{" "}
                                </b>
                              </p>
                              <p>
                                {alldata[item].status == "Lab" ||
                                alldata[item].originatedBy == "External" ? (
                                  <b id="med_off">Lab Technician</b>
                                ) : (
                                  <b id="med_off">Medical Officer</b>
                                )}
                              </p>
                              <p id="pat_doc">
                                {alldata[item]?.Provider?.memberName}
                              </p>
                            </Col>
                            <Col md={3} id="date">
                              <div className="bokkedtimedate">
                                <p id="visit_time" className="lab-date">
                                  {moment(alldata[item]?.orderDate).format(
                                    "DD MMM YYYY, hh:mm A"
                                  )}
                                </p>
                                <p id="visit_text">
                                  {alldata[item].status == "Lab" ||
                                  alldata[item].originatedBy == "External"
                                    ? "Ordered Date, Time"
                                    : "Visit Date, Time"}
                                </p>
                              </div>
                            </Col>
                          </Row>
                          {/* </div> */}
                        </Col>

                        <Col md={2}>
                          <div className="box-dsg2 lab-data-row-2">
                            <div className="lab-btn" align="center">
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
                                  <p
                                    className="dropdown-item"
                                    data-toggle="modal"
                                    data-target="#PatientDetails-wndow"
                                    onClick={() =>
                                      showptdtls(
                                        alldata[item]?.Patient?.patientId
                                      )
                                    }
                                  >
                                    More details
                                  </p>
                                </div>
                              </div>
                            </div>
                            <div className="dot-div lab-order">
                              <div className="my-navlink">
                                {alldata[item].status == "Lab" ||
                                alldata[item].medicalTests.length == 0 ? (
                                  <>
                                    {alldata[item].status == "Lab" ? (
                                      <p
                                        className="lab-order-link"
                                        onClick={() =>
                                          lab_service(
                                            alldata[item]?.patres_Id,
                                            alldata[item]?.pat_create_id
                                          )
                                        }
                                      >
                                        <NavLink to="/order-lab">
                                          <b>Order lab services</b>{" "}
                                        </NavLink>
                                      </p>
                                    ) : (
                                      <p
                                        className="lab-order-link"
                                        onClick={() =>
                                          lab_ByOrderId(
                                            alldata[item]?.orderPat_Id,
                                            alldata[item]?.pat_create_id
                                          )
                                        }
                                      >
                                        <NavLink to="/order-lab">
                                          <b>Order lab services</b>
                                        </NavLink>
                                      </p>
                                    )}
                                    <p
                                      className="lab-order-link"
                                      style={{ opacity: "0.5" }}
                                    >
                                      {" "}
                                      Collect sample{" "}
                                    </p>
                                    <p
                                      className="lab-order-link"
                                      style={{ opacity: "0.5" }}
                                    >
                                      {" "}
                                      Enter result{" "}
                                    </p>
                                    <p
                                      className="lab-order-link"
                                      style={{ opacity: "0.5" }}
                                    >
                                      {" "}
                                      View report{" "}
                                    </p>
                                  </>
                                ) : (
                                  <>
                                    {alldata[item].status ==
                                    "ORDER_ACCEPTED" ? (
                                      <>
                                        <p
                                          className="lab-order-link"
                                          style={{ opacity: "0.5" }}
                                        >
                                          {" "}
                                          Order lab services{" "}
                                        </p>
                                        <p
                                          className="lab-order-link"
                                          onClick={() =>
                                            collect_sample(
                                              alldata[item]?.Laborder_Id
                                            )
                                          }
                                        >
                                          <NavLink to="/collect-sample">
                                            <b>Collect sample</b>
                                          </NavLink>
                                        </p>
                                        <p
                                          className="lab-order-link"
                                          style={{ opacity: "0.5" }}
                                        >
                                          {" "}
                                          Enter result{" "}
                                        </p>
                                        <p
                                          className="lab-order-link"
                                          style={{ opacity: "0.5" }}
                                        >
                                          {" "}
                                          View report{" "}
                                        </p>
                                      </>
                                    ) : (
                                      <>
                                        {alldata[item].status ==
                                        "SAMPLE_COLLECTED" ? (
                                          <>
                                            <p
                                              className="lab-order-link"
                                              style={{ opacity: "0.5" }}
                                            >
                                              {" "}
                                              Order lab services{" "}
                                            </p>
                                            <p
                                              className="lab-order-link"
                                              style={{ opacity: "0.5" }}
                                            >
                                              {" "}
                                              Collect sample{" "}
                                            </p>
                                            <p
                                              className="lab-order-link"
                                              onClick={() =>
                                                lab_ordered(
                                                  alldata[item]?.Laborder_Id
                                                )
                                              }
                                            >
                                              <NavLink to="/enter-result">
                                                <b>Enter result</b>
                                              </NavLink>
                                            </p>
                                            <p
                                              className="lab-order-link"
                                              style={{ opacity: "0.5" }}
                                            >
                                              {" "}
                                              View report{" "}
                                            </p>
                                          </>
                                        ) : (
                                          <>
                                            {alldata[item].status ==
                                            "RESULTS_ENTERED" ? (
                                              <>
                                                <p
                                                  className="lab-order-link"
                                                  style={{ opacity: "0.5" }}
                                                >
                                                  {" "}
                                                  Order lab services{" "}
                                                </p>
                                                <p
                                                  className="lab-order-link"
                                                  style={{ opacity: "0.5" }}
                                                >
                                                  {" "}
                                                  Collect sample{" "}
                                                </p>
                                                <p
                                                  className="lab-order-link"
                                                  style={{ opacity: "0.5" }}
                                                >
                                                  {" "}
                                                  Enter result{" "}
                                                </p>
                                                <p
                                                  className="lab-order-link"
                                                  onClick={() =>
                                                    lab_ordered(
                                                      alldata[item]?.Laborder_Id
                                                    )
                                                  }
                                                >
                                                  <NavLink to="/view-report">
                                                    <b>View report</b>
                                                  </NavLink>
                                                </p>
                                              </>
                                            ) : (
                                              <>
                                                {alldata[item].status ==
                                                "DELIVERED" ? (
                                                  <>
                                                    <p
                                                      className="lab-order-link"
                                                      style={{ opacity: "0.5" }}
                                                    >
                                                      {" "}
                                                      Order lab services{" "}
                                                    </p>
                                                    <p
                                                      className="lab-order-link"
                                                      style={{ opacity: "0.5" }}
                                                    >
                                                      {" "}
                                                      Collect sample{" "}
                                                    </p>
                                                    <p
                                                      className="lab-order-link"
                                                      style={{ opacity: "0.5" }}
                                                    >
                                                      {" "}
                                                      Enter result{" "}
                                                    </p>
                                                    <p
                                                      className="lab-order-link"
                                                      style={{ opacity: "0.5" }}
                                                    >
                                                      {" "}
                                                      View report{" "}
                                                    </p>
                                                  </>
                                                ) : (
                                                  <></>
                                                )}
                                              </>
                                            )}
                                          </>
                                        )}
                                      </>
                                    )}
                                  </>
                                )}
                              </div>
                            </div>
                          </div>
                        </Col>
                      </Row>
                    </div>
                  </Col>
                </Row>
              ))}
            </>
          )}
        </>
      )}
      <>
        {allOrderdata.length != 0 && !props.searchValue && (
          <div className="pagination-wrapper">
            <Paginations
              totalRecords={NUM_OF_RECORDS1}
              pageLimit={LIMIT1}
              pageNeighbours={1}
              onPageChanged={onPageChanged1}
              currentPage={currentPage1}
            />
          </div>
        )}
      </>
    </React.Fragment>
  );
}

export default LabTechnician;
