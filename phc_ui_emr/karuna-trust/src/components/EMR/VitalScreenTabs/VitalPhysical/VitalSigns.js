import React, { useEffect, useState } from "react";
import {
  Col,
  Row,
  Form,
  Accordion,
  Button,
  Tab,
  Nav,
  Modal,
} from "react-bootstrap";
import styled from "styled-components";
import moment from "moment";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import "../VitalScreenTabs.css";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";

// var UHId;
function VitalSigns(props) {
  const UHId = props.vitalsPatientData?.Patient?.UHId;
  const PatName = props.vitalsPatientData?.Patient?.name;
  const PatHeaId = props.vitalsPatientData?.Patient?.healthId;
  const PatGen = props.vitalsPatientData?.Patient?.gender;
  const PatDob = props.vitalsPatientData?.Patient?.dob;
  const PatMob = props.vitalsPatientData?.Patient?.phone;
  const encounterId = props.vitalsPatientData?.encounterId;
  console.log(props.vitalsPatientData, "hiii");
  const patientId = props.vitalsPatientData?.Patient?.patientId;

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // set state for status to rerender
  const [status, setStatus] = useState(false);

  // set state for checkbox toggle
  const [checkValue1, setCheckbox] = useState(false);
  const [checkValue2, setValue] = useState(false);

  function displayTime() {
    var time4 = moment().format("ddd, DD MMM YYYY");
    var time5 = moment().format("hh:mm A");
    $("#clock2").html(time4);
    $("#clock3").html(time5);
    setTimeout(displayTime, 1000);
  }
  $(document).ready(function () {
    displayTime();
  });

  // const date = new Date().toLocaleDateString();
  let typeofuser = sessionStorage.getItem("typeofuser");

  // set state for positon check box
  const [select, setSelect] = useState("Sitting");

  const handleSelectChange = (event) => {
    const value = event.target.value;
    setSelect(value);
  };

  // set state for edit onclik loader
  // const [isLoading, setLoading] = useState(false);

  // function onloading() {
  //     setLoading(true);
  //     setTimeout(() => {
  //         setLoading(false);
  //     }, 2500)
  // }

  // set state for form input fields
  const [height, setHeight22] = useState(null);
  const [feet, setFeet] = useState("");
  const [inch, setInch] = useState("");
  const [weight, setWeight22] = useState(null);
  const [massIndex, setMassIndex] = useState("");
  const [circum, setCircum] = useState("");
  const [temper, setTemper] = useState("");
  const [pulse, setPulse] = useState("");
  const [sysBp, setSysBP] = useState("");
  const [diastoBP, setDiastoBP] = useState("");
  const [artPressure, setArtPressure] = useState("");
  const [respRate, setRespRate] = useState("");
  const [spo, setSpo] = useState("");
  const [comment, setComment] = useState("");

  const [vitalRecords, setVitalRecords] = useState([]);
  const [vitalDataId, setVitalDataId] = useState("");
  const sortedActivities = vitalRecords
    .slice()
    .sort((a, b) => (a.date > b.date ? 1 : -1));
  var latestVitalData = vitalRecords[vitalRecords.length - 1];

  const setHeight = (event) => {
    setHeight22(event);
    var initialFeetvalue = (event * 0.3937) / 12;
    var converttofeetvalue = Math.floor(initialFeetvalue);

    setFeet(converttofeetvalue);
    var converttoinchvalue = Math.round(
      (initialFeetvalue - converttofeetvalue) * 12
    );
    setInch(converttoinchvalue);
  };

  const setWeight = (event) => {
    setWeight22(event);
    const bmi = (event * 10000) / (height * height);
    const BMI = parseFloat(bmi).toFixed(2);

    setMassIndex(BMI);
  };
  const setDiastoBP2 = (e) => {
    setDiastoBP(e);
    const DBP = Number(e);
    let Map = (1 / 3) * (sysBp - e) + DBP;
    const MAP = parseFloat(Map).toFixed(2);
    setArtPressure(MAP);
  };

  const [vitalSignDataShow, setVitalSignDataShow] = useState(false);
  const vitalsignClose = () => setVitalSignDataShow(false);
  const vitalsignShow = () => setVitalSignDataShow(true);

  let patVitaldata = [
    {
      signName: "Height (in cm)",
      signValue: height,
    },
    {
      signName: "Feet",
      signValue: feet,
    },
    {
      signName: "Inch",
      signValue: inch,
    },
    {
      signName: "Weight (in Kgs)",
      signValue: weight,
    },
    {
      signName: "Body Mass Index",
      signValue: massIndex,
    },
    {
      signName: "Head Circumference",
      signValue: circum,
    },
    {
      signName: "Axellery temperature(f)",
      signValue: temper,
    },
    {
      signName: "Periperal pulse rate (bpm)",
      signValue: pulse,
    },
    {
      signName: "Systolic BP (mmHg)",
      signValue: sysBp,
    },
    {
      signName: "Diastolic BP(mmHg)",
      signValue: diastoBP,
    },
    {
      signName: "Mean Atrial Pressure(mmHg)",
      signValue: artPressure,
    },
    {
      signName: "Position",
      signValue: select,
    },
    {
      signName: "Respiratory rate (br/min)",
      signValue: respRate,
    },
    {
      signName: "SPO2 %",
      signValue: spo,
    },
    {
      signName: "Oedema",
      signValue: checkValue1,
    },
    {
      signName: "Pallor",
      signValue: checkValue2,
    },
    {
      signName: "Other Comments",
      signValue: comment,
    },
  ];

  // submit form data to the api
  const date = new Date();
  function submitData(e) {
    e.preventDefault();
    const vitalFormData = {
      citizenId: "string",
      patientId: patientId,
      UHId: UHId,
      encounterId: encounterId,
      encounterDate: date,
      medicalSigns: patVitaldata,
      audit: {
        createdBy: typeofuser,
        dateCreated: date,
        modifiedBy: "string",
        dateModified: "string",
      },
    };
    console.log(vitalFormData, "text");
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(vitalFormData),
    };

    fetch(`${constant.ApiUrl}/vitalsigns`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res) {
          setStatus(true);
          setHeight22("");
          setFeet("");
          setInch("");
          setWeight22("");
          setMassIndex("");
          setCircum("");
          setTemper("");
          setPulse("");
          setSysBP("");
          setDiastoBP("");
          setArtPressure("");
          setRespRate("");
          setSpo("");
          setComment("");
          setCheckbox(false);
          setValue(false);
          setSelect("Sitting");
          alert("Data is submitted Successfully");
        } else {
          alert("Something Went wrong, please Try again");
        }
      });
    setVitalShow(false);
  }

  // fetcing vitals data
  useEffect(() => {
    if (encounterId != undefined) {
      fetch(
        `${constant.ApiUrl}/vitalsigns/filter?encounterId=` +
          encounterId +
          "&page=1",
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res.data.length != 0) {
            setVitalRecords(res["data"]);
            setStatus(false);
          }
        })
        .catch((err) => {
          reject(err);
        });
    }
  }, [encounterId, status]);
  // fetcing vitals data

  // function for fetch recently save data for update
  function editVitals(e) {
    setVitalShow(true);
    setVitalDataId(e);
    let vitalId = e;

    if (vitalId != "") {
      fetch(
        `${constant.ApiUrl}/vitalsigns/${vitalId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setHeight22(res["medicalSigns"][0]["signValue"]);
          setFeet(res["medicalSigns"][1]["signValue"]);
          setInch(res["medicalSigns"][2]["signValue"]);
          setWeight22(res["medicalSigns"][3]["signValue"]);
          setMassIndex(res["medicalSigns"][4]["signValue"]);
          setCircum(res["medicalSigns"][5]["signValue"]);
          setTemper(res["medicalSigns"][6]["signValue"]);
          setPulse(res["medicalSigns"][7]["signValue"]);
          setSysBP(res["medicalSigns"][8]["signValue"]);
          setDiastoBP(res["medicalSigns"][9]["signValue"]);
          setArtPressure(res["medicalSigns"][10]["signValue"]);
          setSelect(res["medicalSigns"][11]["signValue"]);
          setRespRate(res["medicalSigns"][12]["signValue"]);
          setSpo(res["medicalSigns"][13]["signValue"]);
          let booleanValue1 = JSON.parse(res["medicalSigns"][14]["signValue"]);
          setCheckbox(booleanValue1);
          let booleanValue2 = JSON.parse(res["medicalSigns"][15]["signValue"]);
          setValue(booleanValue2);
          setComment(res["medicalSigns"][16]["signValue"]);
          setStatus(false);
        });
    }
  }
  // function for fetch recently save data for update

  // Update form data to the api
  function updateData(e) {
    e.preventDefault();
    const updateFormData = {
      medicalSigns: patVitaldata,
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateFormData),
    };
    if (vitalDataId != undefined) {
      fetch(`${constant.ApiUrl}/vitalsigns/${vitalDataId}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          if (res) {
            setStatus(true);
            setHeight22("");
            setFeet("");
            setInch("");
            setWeight22("");
            setMassIndex("");
            setCircum("");
            setTemper("");
            setPulse("");
            setSysBP("");
            setDiastoBP("");
            setArtPressure("");
            setRespRate("");
            setSpo("");
            setComment("");
            setCheckbox(false);
            setValue(false);
            setSelect("Sitting");
            alert("Data is Updated Successfully");
          } else {
            alert("Something Went wrong, please Try again");
          }
        });
    }
    setVitalDataId("");
    setVitalShow(false);
  }
  // Update form data to the api

  // modal box vitaModalForm
  const [vitalShow, setVitalShow] = useState(false);
  const handleVitalClose = () => {
    setVitalDataId("");
    setVitalShow(false);
    setStatus(true);
    setHeight22("");
    setFeet("");
    setInch("");
    setWeight22("");
    setMassIndex("");
    setCircum("");
    setTemper("");
    setPulse("");
    setSysBP("");
    setDiastoBP("");
    setArtPressure("");
    setRespRate("");
    setSpo("");
    setComment("");
    setCheckbox(false);
    setValue(false);
    setSelect("Sitting");
  };
  const vitaModalForm = () => setVitalShow(true);

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  return (
    <React.Fragment>
      {/* Vital sign foem modal */}
      <Modal
        show={vitalShow}
        onHide={handleVitalClose}
        className="vital-modal-div"
      >
        <div className="vital-modal-div">
          {/* <h5 className="vital-header">Vital Signs</h5> */}
          <div>
            <div className="form-col">
              <form className="vital-form">
                <Row>
                  <Col md={11}>
                    <div className="form-text">
                      <p className="form-note">
                        You are capturing vitals for the above patient on &nbsp;
                        <span id="clock2"></span>
                        <i className="bi bi-calendar-week vital-clock2 "></i>
                        <span
                          className="acc-time vital-clock2 "
                          id="clock3"
                        ></span>
                        <i className="bi bi-clock vital-clock2 "></i>
                      </p>
                    </div>
                  </Col>
                </Row>
                <div className="form-fields">
                  <Row>
                    <Col md={4}>
                      <Row>
                        <Col md={6}>
                          <div className="col-container">
                            <Form.Group
                              className="mb-3_mname"
                              controlId="exampleForm.MName"
                            >
                              <Form.Label className="require">
                                Height (in cm){" "}
                              </Form.Label>
                              <Form.Control
                                type="number"
                                placeholder="0"
                                maxLength={3}
                                value={height || ""}
                                controlid="fomrheight"
                                onChange={(event) =>
                                  setHeight(event.target.value)
                                }
                                required
                              />
                            </Form.Group>
                          </div>
                        </Col>
                        <Col md={6}>
                          <Row>
                            <Col>
                              <div className="col-container">
                                <Form.Group
                                  className="mb-3_mname"
                                  controlId="exampleForm.MName"
                                >
                                  <Form.Label className="require">
                                    Feet{" "}
                                  </Form.Label>
                                  <Form.Control
                                    type="number"
                                    placeholder="0"
                                    value={feet || ""}
                                    controlid="formfeet"
                                    maxLength={3}
                                    onChange={(event) =>
                                      setFeet(event.target.value)
                                    }
                                    disabled
                                  />
                                </Form.Group>
                              </div>
                            </Col>
                            <Col>
                              <div className="col-container">
                                <Form.Group
                                  className="mb-3_mname"
                                  controlId="exampleForm.MName"
                                >
                                  <Form.Label className="require">
                                    Inch{" "}
                                  </Form.Label>
                                  <Form.Control
                                    controlid="frominch"
                                    type="number"
                                    maxLength={3}
                                    placeholder="0"
                                    value={inch || ""}
                                    onChange={(event) =>
                                      setInch(event.target.value)
                                    }
                                    disabled
                                  />
                                </Form.Group>
                              </div>
                            </Col>
                          </Row>
                        </Col>
                      </Row>
                    </Col>
                    <Col md={2}></Col>
                    <Col md={4}>
                      <Row>
                        <Col md={6}>
                          <div className="col-container">
                            <Form.Group
                              className="mb-3_mname"
                              controlId="exampleForm.MName"
                            >
                              <Form.Label className="require">
                                Weight (in Kgs){" "}
                              </Form.Label>
                              <Form.Control
                                type="number"
                                controlid="fromwieght"
                                placeholder="0"
                                maxLength={3}
                                value={weight || ""}
                                onChange={(event) =>
                                  setWeight(event.target.value)
                                }
                              />
                            </Form.Group>
                          </div>
                        </Col>
                        <Col md={6}>
                          <div className="col-container">
                            <Form.Group
                              className="mb-3_mname"
                              controlId="exampleForm.MName"
                            >
                              <Form.Label className="require">
                                Body Mass Index{" "}
                              </Form.Label>
                              <Form.Control
                                type="number"
                                controlid="fromBMI"
                                placeholder="0"
                                maxLength={3}
                                value={massIndex || ""}
                                onChange={(event) =>
                                  setMassIndex(event.target.value)
                                }
                                disabled
                              />
                            </Form.Group>
                          </div>
                        </Col>
                      </Row>
                    </Col>
                    <Col md={2}></Col>
                  </Row>
                  <br />
                  <Row>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Head Circumference (in cm)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            controlid="fromheadcirmference"
                            placeholder="0"
                            maxLength={2}
                            value={circum || ""}
                            onChange={(event) => setCircum(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Axellery temperature(f)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            controlid="fromaxellery"
                            placeholder="0"
                            maxLength={3}
                            value={temper || ""}
                            onChange={(event) => setTemper(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Periperal pulse rate (bpm)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={pulse || ""}
                            maxLength={3}
                            onChange={(event) => setPulse(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <br />
                  <Row>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Systolic BP (mmHg)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={sysBp || ""}
                            maxLength={3}
                            onChange={(event) => setSysBP(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Diastolic BP(mmHg)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={diastoBP || ""}
                            maxLength={3}
                            onChange={(event) =>
                              setDiastoBP2(event.target.value)
                            }
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Mean Atrial Pressure(mmHg)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={artPressure || ""}
                            maxLength={3}
                            onChange={(event) =>
                              setArtPressure(event.target.value)
                            }
                            disabled
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">Position</Form.Label>
                          <div className="radio-div">
                            <Wrapper>
                              <Item>
                                <div>
                                  <img
                                    src="../img/sittingimage.png"
                                    className="pa-img"
                                    alt="patient-image"
                                  />
                                </div>
                                <RadioButton
                                  type="radio"
                                  name="radio"
                                  value="Sitting"
                                  checked={select === "Sitting"}
                                  onChange={(event) =>
                                    handleSelectChange(event)
                                  }
                                />
                                <RadioButtonLabel />
                              </Item>
                              <Item>
                                <div>
                                  <i className="fa fa-bed patient-possition"></i>
                                </div>
                                <RadioButton
                                  type="radio"
                                  name="radio"
                                  value="Sleeping"
                                  checked={select === "Sleeping"}
                                  onChange={(event) =>
                                    handleSelectChange(event)
                                  }
                                />
                                <RadioButtonLabel />
                              </Item>
                            </Wrapper>
                          </div>
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <br />
                  <Row>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Respiratory rate (br/min)
                          </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={respRate || ""}
                            onChange={(event) =>
                              setRespRate(event.target.value)
                            }
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">SPO2 % </Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="0"
                            value={spo || ""}
                            onChange={(event) => setSpo(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <Row>
                        <Col>
                          <div className="col-container vital-check">
                            <Form.Group
                              className="mb-3_fname"
                              controlId="exampleForm.FName"
                            >
                              <Form.Label className="require">
                                Oedema{" "}
                              </Form.Label>
                              <br />
                              <div
                                className={`checkbox ${
                                  checkValue1 && "checkbox--on"
                                }`}
                                onClick={() => setCheckbox(!checkValue1)}
                              >
                                <div className="checkbox__ball"></div>
                              </div>
                            </Form.Group>
                          </div>
                        </Col>
                        <Col>
                          <div className="col-container vital-check">
                            <Form.Group
                              className="mb-3_fname"
                              controlId="exampleForm.FName"
                            >
                              <Form.Label className="require">
                                Pallor{" "}
                              </Form.Label>
                              <br />
                              <div
                                className={`checkbox ${
                                  checkValue2 && "checkbox--on"
                                }`}
                                onClick={() => setValue(!checkValue2)}
                              >
                                <div className="checkbox__ball"></div>
                              </div>
                            </Form.Group>
                          </div>
                        </Col>
                      </Row>
                    </Col>
                    <Col md={3}></Col>
                  </Row>
                  <br />
                  <Row>
                    <Col md={11}>
                      <div className="col-container text-area-div">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Other comments (if any){" "}
                          </Form.Label>
                          <Form.Control
                            as="textarea"
                            placeholder=""
                            value={comment || ""}
                            onChange={(event) => setComment(event.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                </div>
                <div className="vitalsubmit align-me2">
                  <Button
                    className="vitalBtnPC btn2"
                    onClick={handleVitalClose}
                  >
                    Cancel
                  </Button>
                  {vitalDataId ? (
                    <Button className="vitalBtnN btn3" onClick={updateData}>
                      Update
                    </Button>
                  ) : (
                    <Button className="vitalBtnN btn3" onClick={submitData}>
                      Submit
                    </Button>
                  )}
                </div>
              </form>
            </div>
          </div>
        </div>
      </Modal>
      {/* Vital sign foem modal */}
      <ViewModalPopups
        chiefClose={vitalsignClose}
        cheifShow={vitalSignDataShow}
        PatName={PatName}
        vitalsignUHID={UHId}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        vilatEncounterId={encounterId}
        dateto={dateto}
        datefrom={datefrom}
        modType="vital"
      />

      <div className="form-col">
        <form className="cheif-complaint-form">
          <Row>
            <Col md={9}>
              <h5 className="vital-heads">Vitals captured for this visit</h5>
            </Col>
            <Col md={3} className="float-end">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  onClick={vitalsignShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Vital Signs
                </Button>
              </div>
            </Col>
          </Row>
          <div className="accordion-div">
            {sortedActivities?.map((item, i) => (
              <React.Fragment key={i}>
                <Accordion defaultActiveKey={0} alwaysOpen>
                  <Accordion.Item eventKey={i}>
                    {/* <Accordion.Header className="vital-acc-head"><i className="bi bi-caret-down-fill down-mark"></i>25 Nov 2022, <span className="acc-time">4:15 PM</span><Icon icon="bytesize:edit" id="edit-icon" /></Accordion.Header> */}
                    <Accordion.Header className="vital-acc-head">
                      <i className="bi bi-caret-down-fill down-mark"></i>
                      <span>
                        {moment(item.audit.dateCreated).format("DD MMM YYYY")}
                        ,&nbsp;
                      </span>{" "}
                      <span className="acc-time">
                        {moment(item.audit.dateCreated).format("hh:mm A")}
                      </span>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      {item === latestVitalData ? (
                        <span
                          className="edit-medication"
                          onClick={(e) => editVitals(item._id)}
                        >
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      ) : (
                        ""
                      )}
                    </Accordion.Header>
                    <Accordion.Body>
                      <Row>
                        {item.medicalSigns.map((item2, j) => (
                          <Col md={6} key={j}>
                            <div className="accordion-body-section">
                              <p>
                                {item2.signName} : <b>{item2.signValue}</b>
                              </p>
                            </div>
                          </Col>
                        ))}
                      </Row>
                    </Accordion.Body>
                  </Accordion.Item>
                </Accordion>
              </React.Fragment>
            ))}
          </div>
          <div className="" align="right">
            <Button
              varient="light"
              className="vital-btn"
              onClick={vitaModalForm}
            >
              Capture Vitals
            </Button>
          </div>
        </form>
      </div>
    </React.Fragment>
  );
}

export default VitalSigns;

const Wrapper = styled.div`
  display: inline-flex;
`;

const Item = styled.div`
  display: flex;
  align-items: center;
  height: 35px;
  position: relative;
  border: 2px dotted #ccc;
  box-sizing: border-box;
  border-radius: 4px;
  margin-right: 20px;
  width: 60px;
  padding: 0px 5px;
`;

const RadioButtonLabel = styled.label`
  position: absolute;
  top: 10%;
  right: 4px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: white;
  border: 2px dotted #ccc;
`;
const RadioButton = styled.input`
  content: "\f00c";
  opacity: 0;
  z-index: 1;
  position: absolute;
  right: -4px;
  cursor: pointer;
  width: 25px;
  height: 25px;
  margin-right: 10px;
  & ~ ${RadioButtonLabel} {
    background: #ccc;
    &::after {
      content: "\f00c";
      font-family: "FontAwesome";
      display: block;
      color: white;
      width: 12px;
      height: 12px;
      margin: -2px 3px;
    }
  }
  &:hover ~ ${RadioButtonLabel} {
    background: #ccc;
    &::after {
      content: "\f00c";
      font-family: "FontAwesome";
      display: block;
      color: white;
      width: 12px;
      height: 12px;
      margin: -2px 3px;
    }
  }
  &:checked + ${Item} {
    background: #07baa6;
    border: 2px dotted #07baa6;
  }
  &:checked + ${RadioButtonLabel} {
    background: #07baa6;
    border: 2px dotted #07baa6;
    &::after {
      content: "\f00c";
      font-family: "FontAwesome";
      display: block;
      color: white;
      width: 12px;
      height: 12px;
      margin: -2px 3px;
    }
  }
`;
