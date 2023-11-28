import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Modal } from "react-bootstrap";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function DischargeDisposition(props) {
  const UHID = props.vitalDatas?.Patient?.UHId;
  const setid = props.vitalDatas?._id;
  const EncID = props.vitalDatas?.encounterId;
  const PatID = props.vitalDatas?.Patient?.patientId;
  const PatName = props.vitalDatas?.Patient?.name;
  const PatHeaId = props.vitalDatas?.Patient?.healthId;
  const PatGen = props.vitalDatas?.Patient?.gender;
  const PatDob = props.vitalDatas?.Patient?.dob;
  const PatMob = props.vitalDatas?.Patient?.phone;
  const docName = props.vitalDatas?.Provider?.name;

  const phc = sessionStorage.getItem("phc");
  const PresentTime = new Date();
  const EffectiveDate = PresentTime.toISOString();

  var a = new Date().toDateString();
  var b = moment("YYYY-MM-DD");

  const date = new Date();
  const [type, setType] = useState("");
  const [users, setUser] = useState("");

  // fetching doctor list for refer the doctor
  const [updateStatus, setUpdateStatus] = useState(false);
  const handleshowhide1 = async (event) => {
    event.preventDefault();
    setUpdateStatus(false);
    let rt = "MedicalOfficer";
    const rdbtn = event.target.value;
    setType(rdbtn);
    let phcuuid = sessionStorage.getItem("uuidofphc");
    const res = await fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=${rt}&srcNodeId=${phcuuid}&srcType=Phc`,
      serviceHeaders.getRequestOptions
    ).then((res) => res.json());
    const data = await res.content;
    setUser(data);
  };
  // fetching doctor list for refer the doctor

  const [dispositionId, setDispositionId] = useState("");

  const [showhide, setShowhide] = useState("Sent Home");
  const handleshowhide = (event) => {
    const getuser = event.target.value;
    setShowhide(getuser);
  };

  const [docHospital, setDocHospital] = useState("");
  const [referHospital, setReferHospital] = useState("");

  const clickAssign = (name) => {
    setDocHospital(name);
  };

  //select death
  const [status, setStatus] = useState();
  const [comment, setComment] = useState("");
  const [nextVisit, setNextVisit] = useState("");
  const [deathDate, setDeathDate] = useState("");
  const [deathTime, setDeathTime] = useState("");
  const [dischargeId, setDischargeId] = useState("");
  const [dischSummary, setDischSummary] = useState("");
  const [fetchDischData, setFetchDischData] = useState([]);
  const sortedActivities = fetchDischData
    .slice()
    .sort((a, b) => (a.date > b.date ? 1 : -1));
  var latestData = fetchDischData[fetchDischData.length - 1];
  //select death

  // const timeStr = deathTime
  const convertTime = (timeStr) => {
    const [time, modifier] = timeStr.split(" ");
    let [hours, minutes] = time.split(":");
    if (hours === "12") {
      hours = "00";
    }
    if (modifier === "PM") {
      hours = parseInt(hours, 10) + 12;
    }
    return `${hours}:${minutes}`;
  };

  useEffect(() => {
    if (deathTime) {
      convertTime(deathTime);
    }
  }, [deathTime]);
  var time = convertTime(deathTime);

  // var timeAndDate = moment(deathDate + ' ' + time);
  var timeAndDate = deathDate + " " + time;
  let convertIsoDate = moment(new Date(timeAndDate)).toISOString();

  // post service for discharge disposition
  function createDischargeDisposition() {
    var DischargeDispositiondata;
    if (showhide === "Expired") {
      DischargeDispositiondata = {
        UHId: UHID,
        encounterId: EncID,
        encounterDate: date,
        patientId: PatID,
        doctor: docName,
        type: showhide,
        remarks: comment,
        deathDateTime: convertIsoDate,
      };
    } else {
      DischargeDispositiondata = {
        UHId: UHID,
        encounterId: EncID,
        encounterDate: date,
        patientId: PatID,
        doctor: docName,
        type: showhide,
        remarks: comment,
        referTo: docHospital,
        referType: type,
        referredHospital: referHospital,
        nextVisit: nextVisit,
      };
    }

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(DischargeDispositiondata),
    };
    fetch(`${constant.ApiUrl}/dischargesummary`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        try {
          Tostify.notifySuccess(
            "Discharge Disposition saved for" + " " + PatName
          );
          setDischargeId(res._id);
          setStatus(true);
        } catch (err) {
          reject(err);
        }
      });
    setShowhide("Sent Home");
    setComment("");
    setNextVisit("");
    setDeathDate("");
    setDeathTime("");
    setDischargeId("");
    setDischSummary("");
    setType("");
  }
  // post service for discharge disposition

  const cancelField = () => {
    setShowhide("Sent Home");
    setComment("");
    setNextVisit("");
    setDeathDate("");
    setDeathTime("");
    setDischargeId("");
    setDischSummary("");
    setType("");
    setDispositionId("");
  };

  // fetching discharge disposition data by encounterId
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/dischargesummary/filter?page=1&size=0&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            setDispositionId(res["data"][0]["_id"]);
            // setFetchDischData(res['data']);
            setShowhide(res["data"][0]["type"]);
            setDeathTime(
              moment(res["data"][0]["deathDateTime"]).format("HH:mm")
            );
            setDeathDate(
              moment(res["data"][0]["deathDateTime"]).format("YYYY-MM-DD")
            );
            setComment(res["data"][0]["remarks"]);
            setNextVisit(res["data"][0]["nextVisit"]);
            setDocHospital(res["data"][0]["referTo"]);
            setReferHospital(res["data"][0]["referredHospital"]);
            setStatus(false);
          }
        });
    }
  }, [EncID, status]);
  // fetching discharge disposition data by encounterId

  // fetching discharge disposition data by dischargeId
  // useEffect(() => {
  //     if (dischargeId != undefined && dischargeId != "" ) {
  //         fetch(`${constant.ApiUrl}/dischargesummary/${dischargeId}`, serviceHeaders.getRequestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             //setFetchDischData(res['data']);
  //             setStatus(false);
  //         });
  //     }
  // }, [dischargeId, status]);
  // fetching discharge disposition data by dischargeId

  // function for fetch recently save data for update

  function updateDischargeDisposition(e) {
    setDispositionId(e);
    let dispoId = e;

    if (dispoId != "" && dispoId != undefined) {
      fetch(
        `${constant.ApiUrl}/dischargesummary/${dispoId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setUpdateStatus(true);
          setComment(res["remarks"]);
          setDeathDate(moment(res["deathDateTime"]).format("YYYY-MM-DD"));
          setDeathTime(moment(res["deathDateTime"]).format("hh:mm"));
          setShowhide(res["type"]);
          setNextVisit(res["nextVisit"]);
          setReferHospital(res["referredHospital"]);
          setType(res["referType"]);
          setDocHospital(res["referTo"]);
          setStatus(true);
        });
    }
  }
  // function for fetch recently save data for update

  // update discharge diagnosis
  function updateDischarge() {
    var DischargeDispositiondata;
    if (showhide === "Expired") {
      DischargeDispositiondata = {
        UHId: UHID,
        encounterId: EncID,
        encounterDate: date,
        patientId: PatID,
        doctor: docName,
        type: showhide,
        remarks: comment,
        deathDateTime: convertIsoDate,
      };
    } else {
      // if (type == "internal") {
      //     DischargeDispositiondata = {
      //         "UHId": UHID,
      //         "encounterId": EncID,
      //         "encounterDate": date,
      //         "patientId": PatID,
      //         "doctor": docName,
      //         "type": showhide,
      //         "remarks": comment,
      //         "referTo": docHospital,
      //         "referType": type,
      //         "referredHospital": "",
      //         "nextVisit": nextVisit
      //     }
      // } else if (type == "external") {
      //     DischargeDispositiondata = {
      //         "UHId": UHID,
      //         "encounterId": EncID,
      //         "encounterDate": date,
      //         "patientId": PatID,
      //         "doctor": docName,
      //         "type": showhide,
      //         "remarks": comment,
      //         "referTo": "",
      //         "referType": type,
      //         "referredHospital": referHospital,
      //         "nextVisit": nextVisit
      //     }
      // }
      DischargeDispositiondata = {
        UHId: UHID,
        encounterId: EncID,
        encounterDate: date,
        patientId: PatID,
        doctor: docName,
        type: showhide,
        remarks: comment,
        referTo: docHospital,
        referType: type,
        referredHospital: referHospital,
        nextVisit: nextVisit,
      };
    }

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(DischargeDispositiondata),
    };
    fetch(
      `${constant.ApiUrl}/dischargesummary/${dispositionId}`,
      requestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        try {
          Tostify.notifySuccess(
            "Discharge Disposition Updated for" + " " + PatName
          );
          setDischargeId(res._id);
          setStatus(true);
        } catch (err) {
          reject(err);
        }
      });
    setDispositionId("");
    setShowhide("");
    setComment("");
    setNextVisit("");
    setDeathDate("");
    setDeathTime("");
    setDischargeId("");
    setDischSummary("");
    setType("");
    setUpdateStatus(false);
  }
  // update discharge diagnosis

  // discharge disposition Modal
  const [investShow, setInvestShow] = useState(false);
  const investClose = () => {
    setInvestShow(false);
  };
  const investOpen = () => setInvestShow(true);

  const handleOnChangeSince = (event) => {
    if (
      !moment(event.target.max).isSameOrAfter(
        moment(event.target.value).format("YYYY-MM-DD")
      )
    ) {
      setDeathDate("");
      Tostify.notifyWarning(
        "You are not Suppose to Enter Future Date in Date Of Death...!"
      );
    } else {
      setDeathDate(event.target.value);
    }
  };

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  return (
    <React.Fragment>
      <div className="form-col">
        <form className="cheif-complaint-form">
          <h5 className="vital-header">Discharge Notes</h5>
          <div className="dis-container">
            <ToastContainer />
            <Row>
              <Col md={8}>
                <Row>
                  <Col md={6}>
                    <Form.Group
                      className="mb-3_fname"
                      controlId="exampleForm.FName"
                    >
                      <Form.Label
                        className="require"
                        style={{ margin: "0 2%" }}
                      >
                        Discharge Type{" "}
                      </Form.Label>
                      <Form.Select
                        aria-label="Default select example"
                        placeholder="Select a value..."
                        id="ddlPassport"
                        value={showhide}
                        onChange={(e) => handleshowhide(e)}
                      >
                        <option value="Sent Home">Sent Home</option>
                        <option value="Cured">Cured</option>
                        <option value="Expired">Death / Expired</option>
                        <option value="Medical Advice">
                          Discharge Against Medical Advice
                        </option>
                        <option value="Refer To">Refer To</option>
                        <option value="Dropouts">Dropouts</option>
                      </Form.Select>
                    </Form.Group>
                  </Col>
                </Row>
                {showhide === "Expired" && (
                  <div style={{ margin: "2% 0" }} className="death-date">
                    {/* <Col md={6} style={{ margin: "2% 0" }} className="death-date"> */}
                    <Row>
                      <Col md={4}>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Date Of Death{" "}
                          </Form.Label>
                          <Form.Control
                            type="date"
                            value={deathDate}
                            max={moment(new Date()).format("YYYY-MM-DD")}
                            onChange={(event) => handleOnChangeSince(event)}
                          />
                        </Form.Group>
                      </Col>
                      <Col md={1}></Col>
                      <Col md={3}>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Date Of Time{" "}
                          </Form.Label>
                          <Form.Control
                            placeholder=" 00 "
                            type="time"
                            value={deathTime}
                            onChange={(event) =>
                              setDeathTime(event.target.value)
                            }
                          />
                        </Form.Group>
                      </Col>
                      <Col md={1}></Col>
                    </Row>
                    {/* </Col> */}
                  </div>
                )}
                <div style={{ margin: "3% 0px" }} className="note-input">
                  <Row>
                    <Col md={6} className="note-input">
                      <div className="text-area-div">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">Notes </Form.Label>
                          <Form.Control
                            as="textarea"
                            placeholder=""
                            style={{ height: "60px" }}
                            value={comment}
                            onChange={(event) => setComment(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                </div>

                {showhide === "Refer To" && (
                  <React.Fragment>
                    <Row style={{ margin: "0 0 2% 0" }}>
                      <Col md={3}>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label
                            className="require"
                            style={{ margin: "0 2%" }}
                          >
                            Refer Type{" "}
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            value={type}
                            placeholder="Select a value..."
                            id="ddlPassport"
                            onChange={(e) => handleshowhide1(e)}
                          >
                            <option value="" disabled hidden>
                              Select
                            </option>
                            <option value="internal">Internal</option>
                            <option value="external">External</option>
                          </Form.Select>
                        </Form.Group>
                      </Col>
                      <Col md={8} className="asignHospital">
                        {type === "internal" && (
                          <React.Fragment>
                            <div className="referHospital">
                              <p className="require mt-2">
                                <b>Assign to</b>
                              </p>
                              {/* <input type="radio" value="Doctor" checked={type === 'Doctor'} onChange={clickChange} /> Doctor */}
                              <div className="RadioForm">
                                <div className="radioAssign">
                                  <div className="radioInput">
                                    {updateStatus == true ? (
                                      <div>
                                        <input
                                          type="radio"
                                          value={docHospital}
                                          checked
                                          onChange={(event) =>
                                            clickAssign(docHospital)
                                          }
                                        />
                                        <span className="radio-label">
                                          MedicalOfficer<br></br>
                                          <p className="radio-label">
                                            ({docHospital})
                                          </p>
                                        </span>
                                      </div>
                                    ) : (
                                      <>
                                        {Object.keys(users).map(
                                          (item, index) => (
                                            <React.Fragment key={index}>
                                              <div>
                                                <input
                                                  type="radio"
                                                  value={
                                                    item.targetNode?.properties
                                                      .name
                                                  }
                                                  onChange={(event) =>
                                                    clickAssign(
                                                      users[item].targetNode
                                                        .properties.name
                                                    )
                                                  }
                                                />
                                                <span className="radio-label">
                                                  {
                                                    users[item].targetNode
                                                      ?.labels[0]
                                                  }
                                                  <br></br>
                                                  <p className="radio-label">
                                                    (
                                                    {
                                                      users[item].targetNode
                                                        ?.properties?.name
                                                    }
                                                    )
                                                  </p>
                                                </span>
                                              </div>
                                            </React.Fragment>
                                          )
                                        )}
                                      </>
                                    )}
                                  </div>
                                </div>
                              </div>
                            </div>
                          </React.Fragment>
                        )}
                        {type === "external" && (
                          <React.Fragment>
                            <div className="referHospital">
                              {/* <Col md={6}> */}
                              <Form.Group className="mb-3_drugname">
                                <Form.Control
                                  className="presription-input hosp-input"
                                  placeholder="Enter hospital name"
                                  value={referHospital}
                                  onChange={(event) =>
                                    setReferHospital(event.target.value)
                                  }
                                />
                              </Form.Group>
                              {/* </Col> */}
                            </div>
                          </React.Fragment>
                        )}
                      </Col>
                    </Row>
                  </React.Fragment>
                )}
                {(showhide == "Refer To" || showhide == "Sent Home") && (
                  <div className="asignHospital">
                    {/* <Col md={6} className="asignHospital"> */}
                    <div className="refers">
                      <p>Next visit after</p>
                      <Form.Control
                        className="presription-input next-visit-inp"
                        type="number"
                        placeholder=""
                        value={nextVisit}
                        onChange={(event) => setNextVisit(event.target.value)}
                      />{" "}
                      &nbsp;
                      <p>days</p>
                    </div>
                    {/* </Col> */}
                  </div>
                )}
              </Col>
              <Col md={4}>
                <div className="display-discharge">
                  {sortedActivities.map((dischargeData, i) => (
                    <div className="dis-data" key={i}>
                      <h4 className="history-date">
                        {moment(dischargeData.audit.dateCreated).format(
                          "DD MMM YYYY, hh:mm A"
                        )}
                        {dischargeData === latestData ? (
                          <span
                            className="edit-medication"
                            onClick={(e) =>
                              updateDischargeDisposition(dischargeData._id)
                            }
                          >
                            <i className="bi bi-pencil chief-edit"></i>
                          </span>
                        ) : (
                          ""
                        )}
                      </h4>
                      {dischargeData?.remarks ? (
                        <p>
                          <b> Remark: </b> {dischargeData?.remarks}
                        </p>
                      ) : (
                        ""
                      )}
                      {EffectiveDate ? (
                        <p>
                          <b> Date: </b>{" "}
                          {moment(EffectiveDate).format("DD MMM YYYY")}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.type ? (
                        <p>
                          <b> Discharge Type: </b> {dischargeData?.type}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referTo ? (
                        <p>
                          <b> Assign To: </b> {dischargeData?.referTo}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referralType ? (
                        <p>
                          <b> Refer Type: </b> {dischargeData?.referralType}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referredHospital ? (
                        <p>
                          <b> Refered Hospital: </b>{" "}
                          {dischargeData?.referredHospital}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.nextVisit ? (
                        <p>
                          <b> Next Visit: </b> {dischargeData?.nextVisit}
                          &nbsp;days
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.deathDateTime ? (
                        <p>
                          <b> Expired Date: </b>{" "}
                          {moment(dischargeData?.deathDateTime).format(
                            "DD-MMM-YYYY, hh:mm A"
                          )}
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  ))}
                </div>
              </Col>
            </Row>
            <Row>
              <Col md={7}>
                <div>
                  {/* <Button variant="light" className="print-btn" onClick={investOpen}>
                                        <i className="bi bi-printer"></i>&nbsp;Print
                                    </Button> */}
                </div>
              </Col>
              <Col md={5}>
                <div className="pre-btn-section">
                  <div className="save-btn-section">
                    <SaveButton
                      butttonClick={cancelField}
                      class_name="regBtnPC"
                      button_name="Cancel"
                    />
                  </div>
                  <div className="save-btn-section">
                    {dispositionId ? (
                      <SaveButton
                        class_name="regBtnN"
                        butttonClick={updateDischarge}
                        button_name="Update"
                      />
                    ) : (
                      <SaveButton
                        class_name="regBtnN"
                        butttonClick={createDischargeDisposition}
                        button_name="Save"
                      />
                    )}
                  </div>
                </div>
              </Col>
            </Row>
          </div>
        </form>
      </div>

      {/*modal-1 modal for Discharge Disposition print */}
      <Modal
        show={investShow}
        onHide={investClose}
        className="invest-print-modal"
      >
        <Row>
          <Col md={6}>
            <h1 className="invest-details-header">{phc}</h1>
          </Col>
          <Col md={6} align="right">
            <button
              onClick={investClose}
              className="bi bi-x close-popup"
            ></button>
          </Col>
        </Row>
        <Row className="invest-body">
          <Col md={12}>
            <Row className="invest-detail">
              <Col md={6}>
                <h3 className="med-off">Medical Officer</h3>
                <p>{docName}</p>
              </Col>
              <Col md={6} className="text-end">
                <p className="med-date">
                  Date: {moment(EffectiveDate).format("DD MMM YYYY")}
                </p>
              </Col>
            </Row>
            <hr />
            <Row className="pat-body">
              <Col>
                <h3 className="pat-off">Patient Details</h3>
                <h3 className="pat-fname">{PatName}</h3>
              </Col>
            </Row>
            <Row className="uhid-detail">
              <Col md={4}>
                <p>UHID : {UHID}</p>
                <p>
                  Health ID :{" "}
                  {!PatHeaId
                    ? ""
                    : PatHeaId.replace(
                        /(\d{2})(\d{4})(\d{4})(\d{4})/,
                        "$1-$2-$3-$4"
                      )}
                </p>
              </Col>
              <Col md={2} className="gender-details">
                <p>{PatGen}</p>
              </Col>
              <Col md={1}></Col>
              <Col md={2} className="age-details">
                <p>
                  {
                    ((a = new Date()),
                    (b = moment(PatDob)),
                    -b.diff(a, "years") + " Years")
                  }
                </p>
                <p>{moment(PatDob).format("DD MMM YYYY")}</p>
              </Col>
              <Col md={1}></Col>
              <Col md={2} className="mobile-details">
                +91-{PatMob}
              </Col>
            </Row>
            <hr />
            <Row className="inveastigation-details">
              <Col>
                <h3 className="recommended-details">Discharge Summary</h3>
                <div>
                  {sortedActivities.map((dischargeData, i) => (
                    <div className="dis-data" key={i}>
                      {dischargeData?.remarks ? (
                        <p>
                          <b> Remark: </b> {dischargeData?.remarks}
                        </p>
                      ) : (
                        ""
                      )}
                      {EffectiveDate ? (
                        <p>
                          <b> Date: </b>{" "}
                          {moment(EffectiveDate).format("DD MMM YYYY")}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.type ? (
                        <p>
                          <b> Refer Type: </b> {dischargeData?.type}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referTo ? (
                        <p>
                          <b> Assign To: </b> {dischargeData?.referTo}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referralType ? (
                        <p>
                          <b> Refer Type: </b> {dischargeData?.referralType}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.referredHospital ? (
                        <p>
                          <b> Refered Hospital: </b>{" "}
                          {dischargeData?.referredHospital}
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.nextVisit ? (
                        <p>
                          <b> Next Visit: </b> {dischargeData?.nextVisit}
                          &nbsp;days
                        </p>
                      ) : (
                        ""
                      )}
                      {dischargeData?.deathDateTime ? (
                        <p>
                          <b> Expired Date: </b>{" "}
                          {moment(dischargeData?.deathDateTime).format(
                            "DD-MMM-YYYY, hh:mm A"
                          )}
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  ))}
                </div>
              </Col>
            </Row>
            <Row className="mo-sign">
              <Col>
                <p>Medical Officer Signature / Stamp</p>
              </Col>
            </Row>
            <hr />
          </Col>
          <div className="investigation-button">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={investClose}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              <SaveButton
                butttonClick={investOpen}
                class_name="regBtnN"
                button_name="Print"
              />
            </div>
          </div>
        </Row>
      </Modal>
      {/*modal-1 modal for Discharge Disposition print */}
    </React.Fragment>
  );
}

export default DischargeDisposition;
