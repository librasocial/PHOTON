import React, { useState, useCallback, useEffect } from "react";
import { Badge, Col, Form, Row, Tabs, Tab, Modal } from "react-bootstrap";
import moment from "moment";
import ModalPopups from "../../EMR/ModalPopups/ModalPopups";
import { Icon } from "@iconify/react";
import { useParams } from "react-router-dom";
import ReactPaginate from "react-paginate";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Paginations from "../../Paginations";
import SaveButton from "../../EMR_Buttons/SaveButton";
import { useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { format } from "date-fns";
import {
  loadUsers,
  updatePatDet,
  loadSinglePatient,
} from "../../../redux/actions";
import SignedHealthIDImage from "../SignedHealthIDImage";
import PageLoader from "../../PageLoader";

function MedicalOfficer(props) {
  let dispatch = useDispatch();
  let doctorid = sessionStorage.getItem("userid");
  const { usersList } = useSelector((state) => state.data);
  const [ptlist, setPtlist] = useState("");
  const [emergenyData, setEmergenyData] = useState("");

  useEffect(() => {
    document.title = "EMR Medical Officer Dashbord";
    let dashboard_eme = [];
    let dashboard_list = [];

    usersList.map((items) => {
      if (items["label"] === "Emergency") {
        dashboard_eme.push(items);
      }
      if (items["tokenNumber"] !== null && items["label"] != "Emergency") {
        dashboard_list.push(items);
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
      dispatch(loadUsers(doctorid, setPageLoader));
    }, 500);
    return () => clearInterval(interval);
  }, [doctorid]);
  // ------------->eSanjeevani<-------------

  //-------->All Patient details for eSanjeevani is coming from ForPatientDetail <---------
  const ForPatientDetail = (e) => {
    fetch(`${constant.ApiUrl}/patients/${e}`, serviceHeaders.getRequestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.status === 404) {
          alert("data is not fetching");
        } else {
          eSanjeevaniPortal({
            names: res.citizen.firstName,
            Mname: res.citizen.middleName,
            Lname: res.citizen.lastName,
            ides: res.citizen.healthId,
            abhaAddresses: res.citizen.abhaAddress,
            gender: res.citizen.gender,
            birthDate: res.citizen.dateOfBirth,
            age: res.citizen.age,
            addressLine1: res.citizen.address.present.addressLine,
            cityDisplay: res.citizen.address.present.village,
            country: res.citizen.address.present.country,
            district: res.citizen.address.present.district,
            pincode: res.citizen.address.present.pinCode,
            state: res.citizen.address.present.state,
            mobileNumber: res.citizen.mobile,
          });
        }
      });
  };

  const eSanjeevaniPortal = ({
    names,
    Mname,
    Lname,
    ides,
    abhaAddresses,
    gender,
    birthDate,
    age,
    addressLine1,
    cityDisplay,
    country,
    district,
    pincode,
    state,
    mobileNumber,
  }) => {
    const inputDateString = birthDate;
    const inputDate = new Date(inputDateString);
    const formattedDate = format(inputDate, "yyyy-MM-dd");

    var raw = JSON.stringify({
      abhaAddress: abhaAddresses,
      abhaNumber: ides,
      age: age,
      birthdate: formattedDate,
      displayName: names + " " + Mname + " " + Lname,
      firstName: names,
      middleName: Mname,
      lastName: Lname,
      genderCode: 1,
      genderDisplay: gender,
      isBlock: false,
      lstPatientAddress: [
        {
          addressLine1: addressLine1,
          addressType: "Physical",
          addressUse: "Work",
          blockDisplay: "",
          cityDisplay: cityDisplay,
          countryDisplay: country,
          districtDisplay: district,
          postalCode: pincode,
          stateDisplay: state,
        },
      ],
      lstPatientContactDetail: [
        {
          contactPointStatus: true,
          contactPointType: "Phone",
          contactPointUse: "Work",
          contactPointValue: mobileNumber,
        },
      ],
      source: "11001",
    });

    var requestOptions = {
      method: "POST",
      headers: serviceHeaders.myHeaders1,
      body: raw,
      redirect: "follow",
    };
    fetch(
      "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/esanjeevani/api/v1/Patient",
      requestOptions
    )
      .then((response) => response.json())
      .then((result) => {
        setshowModal(true);
      })
      .catch((error) => {
        console.log("error", error);
        alert(error);
      });
  };
  //---------->SSO-eSanjeevani<-------------
  const ssoESanjeevani = () => {
    var raw = JSON.stringify({
      userName: email,
      password: Password,
    });

    var requestOptions = {
      method: "POST",
      headers: serviceHeaders.myHeaders1,
      body: raw,
      redirect: "follow",
    };
    fetch(
      "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/esanjeevani/api/ThirdPartyAuth/authenticateReference",
      requestOptions
    )
      .then((response) => response.json())
      .then((result) => {
        window.open(
          `https://uat.esanjeevani.in/#/external-provider-signin/${result.model.referenceId}`,
          "_blank"
        );
        setshowModal(false);
      })
      .catch((error) => {
        console.log("error", error);
        alert(error);
      });
  };

  const [showd, setShowd] = useState(false);
  const [status, setStatus] = useState(false);
  // const arrayData = props.array3;
  const arrayData = [...emergenyData, ...ptlist];
  const date = new Date();
  let number1 = moment(date).format("YYYY");
  const [types, setTypes] = useState("resevation");
  let history = useHistory();

  // Pagination code
  const [currentPage, setCurrentPage] = useState(1);
  let NUM_OF_RECORDS = arrayData.length;
  let LIMIT = 20;
  let array3 = arrayData.slice(
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

  //start consultation
  const start_consultation = (e) => {
    let reservationidofpateint = e;
    sessionStorage.setItem("ResPateintid", e);
    sessionStorage.setItem("vitalon", "on");
    const checkindata = {
      status: "startedConsultation",
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(checkindata),
    };
    dispatch(updatePatDet(reservationidofpateint, requestOptions1));
    history.push("/vitals");
  };
  //start consultation

  // for emergrncy
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
  // for emergrncy

  //for more details
  const showptdtls = (e) => {
    setShowd(true);
    const patient_id1 = e;
    dispatch(loadSinglePatient(patient_id1));
  };
  //for more details

  const closeField = () => {
    setShowd("");
    // setDisp(true)
  };
  const [email, setemail] = useState("");
  const [Password, setPassword] = useState("");
  const [showModal, setshowModal] = useState(false);
  const closeSampleModal = () => {
    setshowModal(false);
  };
  return (
    <React.Fragment>
      {/* modal */}
      <ModalPopups isPatientDetailsShow={showd} patDetailsClose={closeField} />
      {/* modal */}
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <Modal show={showModal} onHide={closeSampleModal}>
        <Modal.Header closeButton>
          <Modal.Title>eSanjeevani Login</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form style={{ margin: "11px" }}>
            <Form.Group
              as={Row}
              className="mb-3"
              controlId="formPlaintextEmail"
            >
              <Form.Label column sm="2">
                Email
              </Form.Label>
              <Col sm="10">
                <Form.Control
                  onChange={(e) => setemail(e.target.value)}
                  placeholder="email@example.com"
                />
              </Col>
            </Form.Group>

            <Form.Group
              as={Row}
              className="mb-3"
              controlId="formPlaintextPassword"
            >
              <Form.Label column sm="2">
                Password
              </Form.Label>
              <Col sm="10">
                <Form.Control
                  type="password"
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Password"
                />
              </Col>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <SaveButton
            butttonClick={ssoESanjeevani}
            class_name="regBtnN"
            button_name={"Submit"}
          />
          {/* <Button class_name="regBtnN" onClick={closeSampleModal}>
            Submit
          </Button> */}
        </Modal.Footer>
      </Modal>
      <Row>
        <Col md={12} lg={11}>
          <div className="que-div-tab">
            <React.Fragment>
              <Row>
                <Col md={5} className="queue">
                  {/* <p className="rec">
                                        Showing&nbsp;&nbsp; {((currentPage-1) * LIMIT) + 1} - {((currentPage - 1) * LIMIT) + parseInt(array3.length)} of {NUM_OF_RECORDS}
                                    </p> */}
                </Col>
              </Row>
              {array3.length == 0 ? (
                <React.Fragment>
                  <div id="pat_que3" className="empty-img-div">
                    <div className="empty-img">
                      <img src="../img/dashboard-emty-img.svg" />
                    </div>
                  </div>
                </React.Fragment>
              ) : (
                <>
                  {Object.keys(array3).map((item, index) => (
                    <React.Fragment key={index}>
                      <div className="box-dsg1">
                        <Row id="pat_que3">
                          <Col
                            md={1}
                            align="center"
                            className="d-flex justify-content-center"
                          >
                            <SignedHealthIDImage
                              healthID={array3[item]?.Patient?.healthId}
                            />
                          </Col>
                          <Col md={3} id="pat_det" className="pat-name-id">
                            <div>
                              <p
                                id="pat_name"
                                className="pateintdataforfontsize"
                              >
                                {array3[item]?.Patient?.name}
                              </p>
                              <p
                                id="pati_id"
                                className="pateintdataforfontsize"
                              >
                                UHID: {array3[item]?.Patient?.UHId}
                              </p>
                              <p
                                id="pati_id"
                                className="pateintdataforfontsize"
                              >
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
                            <div className="inline-for-mobile-device">
                              <p className="pateintdataforfontsize">
                                {" "}
                                {number1 -
                                  moment(array3[item]?.Patient?.dob).format(
                                    "YYYY"
                                  )}{" "}
                                Years
                              </p>
                              <p
                                id="pat_year"
                                className="pateintdataforfontsize"
                              >
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
                                      <p
                                        className="dropdown-item"
                                        data-toggle="modal"
                                        data-target="#PatientDetails-wndow"
                                        onClick={() =>
                                          ForPatientDetail(
                                            array3[item]?.Patient?.patientId
                                          )
                                        }
                                      >
                                        Send To eSanjeevani
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
                            {array3[item]?.label == "Emergency" ? (
                              <div
                                align="center"
                                className="inline-for-mobile-device"
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
                                  <div
                                    align="center"
                                    className="inline-for-mobile-device"
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
                                ) : (
                                  <div
                                    className="inline-for-mobile-device"
                                    align="center"
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
                          </Col>
                          <Col
                            md={3}
                            id="sta"
                            className="inline-for-mobile-device"
                          >
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
                                        Waiting
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
                                            Checked-in
                                          </Badge>
                                        ) : (
                                          <React.Fragment>
                                            {array3[item]?.label ==
                                              "Emergency" &&
                                            array3[item]?.status ==
                                              "CheckedIn" ? (
                                              <Badge bg="" id="pro">
                                                Waiting
                                              </Badge>
                                            ) : (
                                              <React.Fragment>
                                                {array3[item]?.status ==
                                                "startedConsultation" ? (
                                                  <Badge id="pro" bg="warning">
                                                    Consultation in progress
                                                  </Badge>
                                                ) : (
                                                  ""
                                                )}
                                              </React.Fragment>
                                            )}
                                          </React.Fragment>
                                        )}{" "}
                                      </React.Fragment>
                                    )}
                                  </>
                                )}
                              </>
                            )}
                            <p>Status</p>
                          </Col>
                          <Col md={1} className="tok inline-for-mobile-device">
                            <span className="badge1">
                              {(currentPage - 1) * LIMIT + (index + 1)}
                            </span>
                            <p id="pat_tok">Queue no.</p>
                          </Col>
                          <Col md={1} className="tok inline-for-mobile-device">
                            <span className="badge1">
                              {array3[item]?.tokenNumber}
                            </span>
                            <p id="pat_tok">Token no.</p>
                          </Col>
                          <Col md={2} id="med">
                            <b id="med_off">Medical Officer</b>
                            <p id="pat_doc"> {array3[item]?.Provider?.name}</p>
                            <p id="pat_ico">
                              <i className="fa fa-stethoscope"></i>&nbsp; To
                              attend
                            </p>
                          </Col>
                          <Col
                            md={2}
                            id="date"
                            className="inline-for-mobile-device"
                          >
                            <div className="bokkedtimedate">
                              <p id="visit_time" className="lab-date">
                                {moment(array3[item]?.reservationTime).format(
                                  "DD MMM YYYY,  hh:mm A"
                                )}
                              </p>
                              <p id="visit_text">Visit Date, Time</p>
                            </div>
                          </Col>
                          <Col md={2} id="date" className="consult-button">
                            {array3[item]?.label == "End" ||
                            array3[item]?.status == "Cancelled" ? (
                              ""
                            ) : (
                              <>
                                {array3[item]?.status ===
                                "startedConsultation" ? (
                                  <p
                                    onClick={() => {
                                      start_consultation(array3[item]?._id);
                                    }}
                                  >
                                    <i className="bi bi-arrow-right"></i>
                                    <span className="capturevitals">
                                      Continue Consultation{" "}
                                    </span>
                                  </p>
                                ) : (
                                  <p
                                    onClick={() => {
                                      start_consultation(array3[item]?._id);
                                    }}
                                  >
                                    <i className="bi bi-arrow-right"></i>
                                    <span className="capturevitals">
                                      Start Consultation{" "}
                                    </span>
                                  </p>
                                )}
                              </>
                            )}
                          </Col>
                        </Row>
                      </div>
                    </React.Fragment>
                  ))}
                </>
              )}
              <>
                {array3.length != 0 && (
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
            </React.Fragment>
            {/* </Tab> */}
            {/* </Tabs> */}
          </div>
        </Col>
      </Row>
    </React.Fragment>
  );
}

export default MedicalOfficer;
