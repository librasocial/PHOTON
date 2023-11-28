import React, { useState, useCallback, useEffect } from "react";
import { Badge, Col, Form, Row, Button } from "react-bootstrap";
import moment from "moment";
import Paginations from "../../Paginations";
import "../../../components/Pharmacy/PharmacyTab.css";
import { useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  loadRegPharma,
  loadAllPharmaList,
  loadPharmaPatientBySearch,
  loadSinglePatient,
} from "../../../redux/actions";
import SignedHealthIDImage from "../SignedHealthIDImage";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import PageLoader from "../../PageLoader";

function PharmacyDashboard(props) {
  let history = useHistory();
  let dispatch = useDispatch();
  const { regPharmacy } = useSelector((state) => state.data);
  const { AllPharmaList } = useSelector((state) => state.data);

  let regPharmacyList = regPharmacy.map((reg) => {
    return {
      patient: {
        uhId: reg["Patient"]["UHId"],
        dob: reg["Patient"]["dob"],
        gender: reg["Patient"]["gender"],
        healthId: reg["Patient"]["healthId"],
        name: reg["Patient"]["name"],
        patientId: reg["Patient"]["patientId"],
        phone: reg["Patient"]["phone"],
        mamberId: "",
      },
      status: "",
      encounter: {
        encounterId: reg["encounterId"],
        encounterDateTime: reg["reservationTime"],
        staffId: reg["Provider"]["memberId"],
        staffName: reg["Provider"]["name"],
        prescriptionId: "",
      },
      items: [],
      orderDate: "",
      originatedBy: reg["Provider"]["name"],
      registeredDate: reg["reservationTime"],
      OrderId: "",
      pat_create_id: reg["_id"],
    };
  });

  const directRegister = regPharmacyList.filter((product) =>
    AllPharmaList.every(
      (product2) =>
        !product2.patient?.patientId.includes(product.patient?.patientId)
    )
  );

  let pharmaid = sessionStorage.getItem("userid");
  const isoDateforgenerated = moment(new Date().toISOString()).format(
    "YYYY-MM-DD"
  );

  const [searchStatus, setSearchStatus] = useState(false);
  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    document.title = "EMR Pharmacy Dashbord";
    if (searchStatus == false) {
      const interval = setInterval(() => {
        dispatch(loadRegPharma(pharmaid, isoDateforgenerated, setPageLoader));
        // dispatch(loadAllPharmaList());
      }, 1000);
      return () => clearInterval(interval);
    }
  }, [pharmaid, isoDateforgenerated, searchStatus]);

  let number1 = moment(new Date()).format("YYYY");
  const [searchName, setSearchName] = useState("");
  const [selectStDate, setSelectStDate] = useState("");
  const [selectEdDate, setSelectEdDate] = useState("");
  let allPharmalist = [];

  if (searchName || selectStDate || selectEdDate) {
    allPharmalist = [...AllPharmaList];
  } else {
    allPharmalist = [...directRegister, ...AllPharmaList];
  }

  // Pagination code
  const [currentPage2, setCurrentPage2] = useState(1);
  let NUM_OF_RECORDS2 = allPharmalist.length;
  let LIMIT1 = 20;
  const pharmadata = allPharmalist.slice(
    (currentPage2 - 1) * LIMIT1,
    (currentPage2 - 1) * LIMIT1 + LIMIT1
  );

  const onPageChanged2 = useCallback(
    (event, page) => {
      event.preventDefault();
      setCurrentPage2(page);
    },
    [setCurrentPage2]
  );
  // Pagination code

  const [searchvalue, setSearchvalue] = useState("NAME");
  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter Name to Search"
  );

  const selectsearchtype = (e) => {
    const xx = e.target.value;
    if (xx === "NAME") {
      setSearchplaceholder("Enter Name to Search");
    } else if (xx === "UHID") {
      setSearchplaceholder("Enter UHID to Search");
    }
    setSearchvalue(xx);
  };

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  const OrderDispMedicine = (patId, resId) => {
    sessionStorage.setItem("LabPateintid", patId);
    sessionStorage.setItem("LabResid", resId);
    if (sessionStorage.getItem("LabResid")) {
      history.push("/Dispence-Medicine");
    }
  };
  const OrdDispMedicine = (patId, resId) => {
    sessionStorage.setItem("LabPateintid", patId);
    sessionStorage.setItem("LabOrdId", resId);
    if (sessionStorage.getItem("LabOrdId")) {
      history.push("/Dispence-Medicine");
    }
  };
  const dispenceMedicine = (patId, resId) => {
    sessionStorage.setItem("LabPateintid", patId);
    sessionStorage.setItem("LabOrdId", resId);
    if (sessionStorage.getItem("LabOrdId")) {
      history.push("/Dispence-Medicine");
    }
  };

  // Search Functionality

  const searchPharmaPatient = () => {
    setSearchStatus(true);
    setPageLoader(true);
    dispatch(
      loadPharmaPatientBySearch(
        searchName,
        selectStDate,
        selectEdDate,
        currentPage2 - 1,
        LIMIT1,
        searchvalue,
        setPageLoader
      )
    );
  };
  const resetPharmaPatient = () => {
    setSearchName(""), setSelectStDate(""), setSelectEdDate("");
    setSearchvalue("NAME");
    setSearchplaceholder("Enter Name to Search");
    setSearchStatus(false);
    dispatch(loadRegPharma(pharmaid, isoDateforgenerated, setPageLoader));
  };
  const removeSearchValue = () => {
    setSearchStatus(false);
    setSearchName("");
    dispatch(loadRegPharma(pharmaid, isoDateforgenerated, setPageLoader));
  };

  //for more details
  const [showd, setShowd] = useState(false);

  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };
  const closeField = () => {
    setShowd(false);
  };

  return (
    <React.Fragment>
      <React.Fragment>
        {/* modal */}
        <ModalPopups
          isPatientDetailsShow={showd}
          patDetailsClose={closeField}
        />
        {/* modal */}
        {/* loader */}
        {pageLoader == true && <PageLoader />}
        {/* loader */}
        <div className="mobile-view">
          <h1 className="dash-mnag-list">Pharmacy Orders</h1>
        </div>
        <div className="searchFilter-div">
          <Row className="search-row">
            <Col md={4} className="search-select">
              <Row>
                <Col md={3}>
                  <div>
                    <select
                      className="regDropdown"
                      onChange={selectsearchtype}
                      // defaultValue="NAME"
                      value={searchvalue}
                    >
                      <option value="NAME">Name</option>
                      <option value="UHID">UHID</option>
                    </select>
                  </div>
                </Col>
                <Col md={9}>
                  <div className="form-group order-search">
                    <input
                      type="text"
                      id="pharma-search"
                      className="form-control order-search"
                      placeholder={searchplaceholder}
                      name="search"
                      value={searchName}
                      onChange={(e) => setSearchName(e.target.value)}
                    />
                    {searchName ? (
                      <span
                        className="fa fa-times form-control-ord-feedback"
                        onClick={removeSearchValue}
                      ></span>
                    ) : (
                      <span className="fa fa-search form-control-ord-feedback"></span>
                    )}
                  </div>
                </Col>
              </Row>
            </Col>
            <Col md={4}>
              <Row className="ourdate">
                <Col md={6}>
                  <div className="search-date">
                    <input
                      className="form-control"
                      type="date"
                      key="random1"
                      name="stDate"
                      value={selectStDate}
                      pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                      onChange={(e) =>
                        setSelectStDate(
                          moment(new Date(e.target.value)).format("YYYY-MM-DD")
                        )
                      }
                      placeholder={searchplaceholder}
                      max={currentdate}
                      selected={searchvalue}
                      validate={[disableFutureDt]}
                      id="pharma-input"
                    />
                  </div>
                </Col>
                <Col md={6}>
                  <div className="search-date">
                    <input
                      className="form-control"
                      type="date"
                      key="random1"
                      name="edDate"
                      value={selectEdDate}
                      pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                      onChange={(e) =>
                        setSelectEdDate(
                          moment(new Date(e.target.value)).format("YYYY-MM-DD")
                        )
                      }
                      placeholder={searchplaceholder}
                      max={currentdate}
                      selected={searchvalue}
                      validate={[disableFutureDt]}
                      id="pharma-input"
                    />
                  </div>
                </Col>
              </Row>
            </Col>
            <Col md={4}>
              <Row>
                <Col md={6} className="d-flex justify-content-center">
                  <Button
                    type="button"
                    className="orderBtn btn btn-secondary"
                    onClick={searchPharmaPatient}
                  >
                    Search
                  </Button>
                  <Button
                    type="button"
                    className="linkBtn btn btn-link"
                    onClick={resetPharmaPatient}
                  >
                    Clear
                  </Button>
                </Col>
              </Row>
            </Col>
          </Row>
          {allPharmalist.length == 0 ? (
            <React.Fragment>
              <div id="pat_que3" className="empty-img-div">
                <div className="empty-img">
                  <img src="../img/dashboard-emty-img.svg" />
                </div>
              </div>
            </React.Fragment>
          ) : (
            <>
              {pharmadata.map((pharma, index) => (
                <div className="box-dsg1" key={index}>
                  <Row id="pat_que3">
                    <Col
                      md={1}
                      align="center"
                      className="d-flex justify-content-center"
                    >
                      <SignedHealthIDImage
                        healthID={pharma?.patient?.healthId}
                      />
                    </Col>
                    <Col md={3} id="pat_det" className="pat-name-id">
                      <p id="pat_name" className="pateintdataforfontsize">
                        {pharma?.patient?.name}
                      </p>
                      <p id="pati_id" className="pateintdataforfontsize">
                        UHId: {pharma?.patient?.uhId}
                      </p>
                      <p id="pati_id" className="pateintdataforfontsize">
                        Health ID:{" "}
                        {!pharma?.patient?.healthId ? (
                          ""
                        ) : (
                          <>
                            {pharma?.patient?.healthId.replace(
                              /(\d{2})(\d{4})(\d{4})(\d{4})/,
                              "$1-$2-$3-$4"
                            )}
                          </>
                        )}
                      </p>
                    </Col>
                    <Col md={1} id="pat_gen">
                      <p className="pateintdataforfontsize">
                        {pharma?.patient?.gender}
                      </p>
                    </Col>
                    <Col md={2} id="pat_age">
                      <div>
                        <p className="pateintdataforfontsize">
                          {number1 -
                            moment(pharma?.patient?.dob).format("YYYY")}{" "}
                          Years
                        </p>
                        <p id="pat_year" className="pateintdataforfontsize">
                          {moment(pharma?.patient?.dob).format("DD MMM YYYY")}
                        </p>
                      </div>
                    </Col>
                    <Col md={3} id="mob_num">
                      {pharma?.patient?.phone && (
                        <p className="pateintdataforfontsize">
                          +91-{pharma?.patient?.phone}
                        </p>
                      )}
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
                              <p
                                className="dropdown-item"
                                data-toggle="modal"
                                data-target="#PatientDetails-wndow"
                                onClick={() =>
                                  showptdtls(pharma?.patient?.patientId)
                                }
                              >
                                More details
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <hr id="que_sta" />
                  <Row id="que_div">
                    {/* <Col md={1} id="sta"></Col> */}
                    <Col md={3} id="sta" align="center">
                      {pharma?.status == "" ||
                      pharma?.status == "REGISTERED" ? (
                        <Badge className="pharma-dash-badge reg-badge">
                          Registered for Pharma Service
                        </Badge>
                      ) : (
                        <Badge className="pharma-dash-badge ord-badge">
                          Ordered
                        </Badge>
                      )}
                      <p id="pat_sta">Status</p>
                    </Col>
                    <Col md={2} id="sta" style={{ fontSize: "12px" }}>
                      {pharma?.OrderId != "" && (
                        <>
                          <b>{pharma.id}</b>
                          <p id="pat_sta">Order Id</p>
                        </>
                      )}
                    </Col>
                    <Col md={2} id="med">
                      <p>
                        <b>
                          {pharma?.status == "" ||
                          pharma?.status == "REGISTERED"
                            ? "Ordered By"
                            : "Referred By"}
                        </b>
                        <br />
                        <b>
                          {pharma?.status == "" ||
                          pharma?.status == "REGISTERED"
                            ? "Pharmacist"
                            : "Medical Officer"}{" "}
                        </b>
                      </p>
                      <p id="pat_sta">
                        {pharma?.status == "" ||
                        pharma?.status == "REGISTERED" ? (
                          <>{pharma?.encounter?.staffName}</>
                        ) : (
                          <>{pharma?.encounter?.staffName}</>
                        )}
                      </p>
                    </Col>
                    <Col md={3} id="date">
                      <div className="bokkedtimedate">
                        <p id="visit_time" className="lab-date">
                          {pharma?.orderDate == ""
                            ? moment(pharma?.registeredDate).format(
                                "DD MMM YYYY,  hh:mm A"
                              )
                            : moment(pharma?.orderDate).format(
                                "DD MMM YYYY,  hh:mm A"
                              )}
                        </p>
                        <p id="visit_text">
                          {pharma?.OrderId == ""
                            ? "Order Date, Time"
                            : "Visit Date, Time"}
                        </p>
                      </div>
                    </Col>
                    <Col md={2} id="date">
                      {pharma?.status == "" ||
                      pharma?.status == "REGISTERED" ? (
                        <>
                          {pharma?.status == "REGISTERED" ? (
                            <p
                              onClick={(e) =>
                                OrdDispMedicine(
                                  pharma.patient?.patientId,
                                  pharma.id
                                )
                              }
                            >
                              <i className="bi bi-arrow-right"></i>
                              <span className="capturevitals">
                                Dispense Medicine
                              </span>
                            </p>
                          ) : (
                            <p
                              onClick={(e) =>
                                OrderDispMedicine(
                                  pharma.patient?.patientId,
                                  pharma.pat_create_id
                                )
                              }
                            >
                              <i className="bi bi-arrow-right"></i>
                              <span className="capturevitals">
                                Dispense Medicine
                              </span>
                            </p>
                          )}
                        </>
                      ) : (
                        <p
                          onClick={(e) =>
                            dispenceMedicine(
                              pharma.patient?.patientId,
                              pharma.id
                            )
                          }
                        >
                          <i className="bi bi-arrow-right"></i>
                          <span className="capturevitals">
                            Dispense Medicine
                          </span>
                        </p>
                      )}
                    </Col>
                  </Row>
                </div>
              ))}
            </>
          )}
        </div>
        <>
          {allPharmalist.length != 0 && !props.searchValue && (
            <div className="pagination-wrapper">
              <Paginations
                totalRecords={NUM_OF_RECORDS2}
                pageLimit={LIMIT1}
                pageNeighbours={1}
                onPageChanged={onPageChanged2}
                currentPage={currentPage2}
              />
            </div>
          )}
        </>
      </React.Fragment>
    </React.Fragment>
  );
}

export default PharmacyDashboard;
