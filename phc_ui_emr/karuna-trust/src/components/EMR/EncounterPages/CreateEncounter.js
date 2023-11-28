import React, { useState, useEffect, useRef } from "react";
import "../../../css/CreateEncounter.css";
import {
  Col,
  Row,
  Button,
  Form,
  Modal,
  Alert,
  InputGroup,
} from "react-bootstrap";
import moment from "moment";
import { useParams, useHistory } from "react-router-dom";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import * as OptionData from "../../ConstUrl/OptionsData";
import Sidemenu from "../../Sidemenus";
import { useDispatch, useSelector } from "react-redux";
import { loadCreateVisit, loadTokenNumber } from "../../../redux/actions";
import SignedHealthIDImage from "../../Dashboard/SignedHealthIDImage";
import SaveButton from "../../EMR_Buttons/SaveButton";
import PatientRow from "../../PatientRow/PatientRow";
// import Button from "react-bootstrap/Button";
// import Modal from "react-bootstrap/Modal";
import { loadcreatevisit } from "../../../redux/formUtilityAction";

// import { loadesanjeevani } from "../../../redux/actions";
// import * as serviceHeaders from '../../ConstUrl/serviceHeaders'
import { format } from "date-fns";

function CreateEncounter(props) {
  const form = useRef(null);
  let dispatch = useDispatch();
  let history = useHistory();

  const { tokenList } = useSelector((state) => state.data);

  const [assignType, setAssignType] = useState("");
  const [AddressLine1, setAddressLine1] = useState("");
  const [cityDisplay, setcityDisplay] = useState("");
  const [Country, setCountry] = useState("");
  const [district, setdistrict] = useState("");
  const [pinCode, setpinCode] = useState("");
  const [patientState, setpatientState] = useState("");
  const [mobile, setmobile] = useState("");
  const [referenceIdSSO, setreferenceIdSSO] = useState("");

  const [status, setStatus] = useState(false);
  const [resUpdateData, setResUpdateData] = useState("");
  const [contact, setEnContact] = useState("");
  const [abhaAddresses, setAbhaAddress] = useState("");
  const [names, setEnFName] = useState("");
  const [ides, setEnhealthId] = useState("");

  const [genders, setEnGender] = useState("");
  const [dBirth, setEnDbirth] = useState("");
  const [ages, setEnAge] = useState("");
  const [salutation, setEnSalutation] = useState("");
  const [Mname, setEnMname] = useState("");
  const [Lname, setEnLname] = useState("");
  const [enMemberId, setEnMemberId] = useState("");
  const [uniqid, setPaUniqId] = useState("");
  const [paStatus, setPaStatus] = useState("");
  const [email, setemail] = useState("");
  const [Password, setPassword] = useState("");
  let fullname;
  if (names && Mname && Lname) {
    fullname = salutation + "." + " " + names + " " + Mname + " " + Lname;
  } else if (names && Mname) {
    fullname = salutation + "." + " " + names + " " + Mname;
  } else if (names && Lname) {
    fullname = salutation + "." + " " + names + " " + Lname;
  } else if (names) {
    fullname = salutation + "." + " " + names;
  }
  let parsedDate = moment(new Date(), "DD.MM.YYYY H:mm:ss");
  let reservationTime = parsedDate.toISOString();
  const [srcval, setSrcval] = useState("");
  const [labs, SetLabs] = useState(false);
  const [encounter_patchid, setEncounter_patchid] = useState([]);

  const [users, setUser] = useState([]);
  const [option, setSelectOption] = useState("");
  const [ashaworkerid, setAshaworkerid] = useState("");
  const [ashaworkername, setAshaworkername] = useState("");

  const [showModal, setshowModal] = useState(false);
  const closeSampleModal = () => {
    setshowModal(false);
  };
  const handleOption1 = (e) => {
    setAshaworkername(e.target.value);
    let idx = e.target.selectedIndex;
    let dataset = e.target.options[idx].dataset;
    setAshaworkerid(dataset.isd);
  };
  const handleOption = async (e) => {
    setSelectOption(e.target.value);
  };

  const [type, setType] = React.useState("");
  const [doc, setDoc] = useState("");
  const [awlist, setAwlist] = useState([]);
  const [Tokennumber, setTokennumber] = useState([]);

  // encounter data
  const [UhidData, setUHIDData] = useState("");
  const [labels, setLabel] = useState("");

  const { createvisitdata } = useSelector((state) => state.formData);

  const date = new Date();
  let number1 = moment(date).format("YYYY");
  const { id } = useParams();
  const { types } = useParams();
  let encounterPatient_id = id;
  console.log("for id ", id);
  useEffect(() => {
    document.title = "EMR Create Visit";
    encounterDatas(encounterPatient_id);
    setStatus(false);
    dispatch(loadcreatevisit());
  }, [encounterPatient_id, status]);

  function encounterDatas() {
    if (types === "resevation") {
      fetch(
        `${constant.ApiUrl}/reservations/${encounterPatient_id}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          console.log("Hiiii ", res);
          if (res.status === 404) {
            alert("data is not fetching");
          } else {
            setResUpdateData(res);
            setUHIDData(res["Patient"]["UHId"]);
            setAbhaAddress(res["Patient"]["abhaAddress"]);
            setPaUniqId(res["Patient"]["patientId"]);
            setEnContact(res["Patient"]["healthId"]);
            setEnContact(res["Patient"]["phone"]);
            setEnFName(res["Patient"]["name"]);
            setEnhealthId(res["Patient"]["healthId"]);
            setEnGender(res["Patient"]["gender"]);
            setEnDbirth(res["Patient"]["dob"]);
            setEnAge(res["Patient"]["age"]);
            setPaStatus(res["status"]);
            setEnSalutation(res["Patient"]["salutation"]);
            setEnMname(res["Patient"]["middleName"]);
            setEnLname(res["Patient"]["lastName"]);
            setEnMemberId(res["Patient"]["memberId"]);
            setType(res["reservationFor"]);
            setSelectOption(res["visitType"]);
            setAssignType(res["Provider"]["memberId"]);

            setTokennumber(res["tokenNumber"]);
            setDoc(res["Provider"]["name"]);
            setLabel(res["label"]);
            SetLabs(true);

            var encounter_idv = res["encounterId"];
            setEncounter_patchid(res["encounterId"]);
            fetch(
              `${constant.ApiUrl}/encounters/${encounter_idv}`,
              serviceHeaders.getRequestOptions
            )
              .then((values) => values.json())
              .then((values) => {
                setAshaworkername(values["referredByAshaWorker"]["staffName"]);
                setAshaworkerid(values["referredByAshaWorker"]["staffId"]);
                setRefferingdoctor(values["referredByDoctor"]["staffName"]);
                setAttendname(values["attendantName"]);
                setAttendnamecontact(values["attendantPhone"]);
              });
          }
        });
    } else if (types === "patient") {
      fetch(
        `${constant.ApiUrl}/patients/${encounterPatient_id}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          console.log("final", res);
          if (res.status === 404) {
            alert("data is not fetching");
          } else {
            setUHIDData(res["UHId"]);
            setPaUniqId(res["_id"]);
            setEnContact(res["citizen"]["mobile"]);
            setEnFName(res["citizen"]["firstName"]);
            setEnMname(res["citizen"]["middleName"]);
            setEnLname(res["citizen"]["lastName"]);
            setEnhealthId(res["citizen"]["healthId"]);
            setAbhaAddress(res["citizen"]["abhaAddress"]);
            setEnGender(res["citizen"]["gender"]);
            setEnDbirth(res["citizen"]["dateOfBirth"]);
            setEnAge(res["citizen"]["age"]);
            setPaStatus(res["status"]);
            setEnSalutation(res["citizen"]["salutation"]);
            setEnMname(res["citizen"]["middleName"]);
            setEnMemberId(res["citizen"]["memberId"]);
            setAddressLine1(res.citizen.address.present.addressLine);
            setcityDisplay(res.citizen.address.present.village);
            setCountry(res.citizen.address.present.country);
            setdistrict(res.citizen.address.present.district);
            setpinCode(res.citizen.address.present.pinCode);
            setpatientState(res.citizen.address.present.state);
            setmobile(res.citizen.mobile);
          }
        });
    }
  }
  //---------->eSanjeevani<-------------
  const eSanjeevaniPortal = () => {
    const inputDateString = dBirth;
    const inputDate = new Date(inputDateString);
    const formattedDate = format(inputDate, "yyyy-MM-dd");
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const dob = dBirth;
    const dateObject = new Date(dob);
    const year = dateObject.getFullYear();
    const age = currentYear - year;
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
      genderDisplay: genders,
      isBlock: false,
      lstPatientAddress: [
        {
          addressLine1: AddressLine1,
          addressType: "Physical",
          addressUse: "Work",
          blockDisplay: "",
          cityDisplay: cityDisplay,
          countryDisplay: Country,
          districtDisplay: district,
          postalCode: pinCode,
          stateDisplay: patientState,
        },
      ],
      lstPatientContactDetail: [
        {
          contactPointStatus: true,
          contactPointType: "Phone",
          contactPointUse: "Work",
          contactPointValue: mobile,
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
        console.log(result.message);
        setshowModal(true);

        // alert(result.message);

        // ssoESanjeevani();
      })
      .catch((error) => console.log("error", error));
  };
  //---------->SSO-eSanjeevani<-------------
  const ssoESanjeevani = () => {
    var raw = JSON.stringify({
      userName: email,
      password: Password,
    });
    console.log(email);
    console.log(Password);
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
        console.log(result.model.referenceId);
        setreferenceIdSSO(result.model.referenceId);

        window.open(
          `https://uat.esanjeevani.in/#/external-provider-signin/${result.model.referenceId}`,
          "_blank"
        );
        setshowModal(false);
      })
      .catch((error) => console.log("error", error));
  };
  useEffect(() => {
    console.log("rehre ", referenceIdSSO);
  });

  const clickChange = async (event) => {
    event.preventDefault();
    SetLabs(true);
    let rt;
    const rdbtn = event.target.value;
    if (rdbtn == "Doctor") {
      setType(event.target.value);
      rt = "MedicalOfficer";
      setSrcval(rt);
      setType(event.target.value);
    } else if (rdbtn == "Lab") {
      rt = "JuniorLabTechnician";
      setSrcval(rt);
      setType(event.target.value);
    } else if (rdbtn == "Pharmacy") {
      rt = "JuniorPharmacist";
      setSrcval(rt);
      setType(event.target.value);
    } else if (rdbtn == "Optometry") {
      rt = "JuniorPharmacist";
      setSrcval(rt);
      setType(event.target.value);
    }

    setType(event.target.value);

    let phcuuid = sessionStorage.getItem("uuidofphc");
    const res = await fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=${rt}&srcNodeId=${phcuuid}&srcType=Phc`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        if (res["content"]) {
          setUser(res["content"]);
        }
      });
    // const data = await res.content;
    // let tmpArray = []
    // for (let i = 0; i < res['content'].length; i++) {
    //     tmpArray.push(res['content'][i])
    // }
    // setUser(tmpArray);
  };

  useEffect(() => {
    fetch(
      `${constant.ApiUrl}/member-svc/members/filter?type=AshaWorker`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        setAwlist(res.content);
        setStatus(false);
      });
  }, [status]);

  useEffect(() => {
    if (users.length == 1) {
      var ele = document.getElementsByName("radio-button");
      for (var i = 0; i < ele.length; i++) {
        if (ele[i].type == "radio") {
          ele[i].checked = true;
          if ((ele[i].checked = true)) {
            setAssignType(ele[i].id);
            setDoc(ele[i].value);
            setLabel(ele[i].selected);
          }
        }
      }
    }
  });

  const clickAssign = (e) => {
    let Uuid = e;
    for (var i = 0; i < users.length; i++) {
      if (users[i].targetNode.properties.uuid == Uuid) {
        setAssignType(users[i].targetNode.properties.uuid);
        setDoc(users[i].targetNode.properties.name);
        setLabel(users[i].targetNode.labels[0]);
      }
    }
  };

  const [assignload, setAssignload] = useState(false);
  useEffect(() => {
    if (assignType) {
      setAssignload(true);
      dispatch(loadTokenNumber(assignType, setAssignload));
    }
  }, [assignType]);

  const [refferingdoctor, setRefferingdoctor] = useState("");
  const [attendname, setAttendname] = useState("");
  const [attendnamecontact, setAttendnamecontact] = useState("");

  // const isValid = doc != null && doc.length > 0; // For validation

  function createencv(e) {
    e.preventDefault();

    let temptokenaray = [];
    let tokenNumber = 0;
    if (tokenList && tokenList.length != 0) {
      tokenList.map((items) => {
        if (
          items["status"] === "OnHold" ||
          items["status"] === "startedConsultation" ||
          items["status"] === "CheckedIn"
        ) {
          if (items["tokenNumber"] !== null) {
            temptokenaray.push(items["tokenNumber"]);
          }
        }
      });
    }
    if (temptokenaray.length == 0) {
      tokenNumber = 0;
    } else {
      tokenNumber = Math.max(...temptokenaray);
    }

    const isoDateforgenerated = moment(new Date().toISOString()).format(
      "YYYY-MM-DD"
    );
    fetch(
      `${constant.ApiUrl}/reservations/filter?&date=` +
        isoDateforgenerated +
        "&status=[CheckedIn,Confirmed,OnHold,startedConsultation]",
      serviceHeaders.getRequestOptions
    )
      .then((res1) => res1.json())
      .then((res1) => {
        let arrayPatientId = [];
        let isFound;
        for (let i = 0; i < res1["data"].length; i++) {
          if (res1["data"][i]["status"] != "Cancelled") {
            arrayPatientId.push(res1["data"][i]);
          }

          isFound = arrayPatientId.some((element) => {
            if (element["Patient"]["patientId"] === uniqid) {
              return true;
            }
            return false;
          });
        }
        if (!isFound) {
          const encounterdata = {
            citizenId: "string",
            patientId: uniqid,
            purpose: type,
            visitType: option,
            assignedTo: {
              staffId: assignType,
              staffName: doc,
              staffType: srcval,
            },
            referredByAshaWorker: {
              staffId: ashaworkerid,
              staffName: ashaworkername,
              staffType: "asha-worker",
            },
            referredByDoctor: {
              staffId: "string",
              staffName: refferingdoctor,
              staffType: "string",
            },
            attendantName: attendname,
            attendantPhone: attendnamecontact,
            reservationFlag: true,
          };

          if (doc == "" || assignType == "") {
            alert("please assign the doctor to patient");
          } else if (!uniqid) {
            alert("Something went wrong...! Please check later");
          } else {
            var requestOptions = {
              headers: serviceHeaders.myHeaders1,
              method: "POST",
              mode: "cors",
              body: JSON.stringify(encounterdata),
            };
            fetch(
              `${constant.ApiUrl}/encounters?fromSaga=false`,
              requestOptions
            )
              .then((resencounterdata) => resencounterdata.json())
              .then((resencounterdata) => {
                // if (resencounterdata["_id"]) {
                try {
                  setStatus(true);
                  var encounterdata_id = resencounterdata["_id"];
                  const encvdata = {
                    Patient: {
                      UHId: UhidData,
                      abhaAddress: abhaAddresses,
                      memberId: enMemberId,
                      patientId: uniqid,
                      healthId: ides,
                      name: fullname,
                      gender: genders,
                      dob: dBirth,
                      phone: contact,
                    },
                    Provider: {
                      memberId: assignType,
                      name: doc,
                    },
                    encounterId: encounterdata_id,
                    reservationFor: type,
                    reservationTime: reservationTime,
                    visitType: option,
                    status: "OnHold",
                    label: labels,
                    tokenNumber: tokenNumber + 1,
                  };
                  var requestOptions1 = {
                    headers: serviceHeaders.myHeaders1,
                    method: "POST",
                    mode: "cors",
                    body: JSON.stringify(encvdata),
                  };

                  dispatch(loadCreateVisit(requestOptions1));
                  sessionStorage.setItem("successmodal", "open");
                  history.push("/dashboard");
                } catch (e) {
                  console.error(e);
                }
                // } else {
                //   alert(resencounterdata);
                // }
              });
          }
        } else {
          alert("Visit is already created for this patient");
          window.history.back();
        }
      });
  }

  function Editencounterdata() {
    const edited_data = {
      Patient: {
        UHId: UhidData,
        abhaAddress: abhaAddresses,
        memberId: enMemberId,
        patientId: uniqid,
        healthId: ides,
        name: names,
        gender: genders,
        dob: dBirth,
        phone: contact,
      },
      Provider: {
        memberId: assignType,
        name: doc,
      },
      visitType: option,
      reservationFor: type,
      reservationTime: reservationTime,
      // "encounterId": EncounterIds,
      status: paStatus,
      tokenNumber: Tokennumber,
      label: labels,
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(edited_data),
    };

    fetch(
      `${constant.ApiUrl}/reservations/${encounterPatient_id}`,
      requestOptions
    )
      .then((res) => res.json())

      .then((res) => {
        setStatus(true);
      });
    const encounter_edited_data = {
      assignedTo: {
        staffName: doc,
      },
      referredByAshaWorker: {
        staffId: ashaworkerid,
        staffName: ashaworkername,
        staffType: "string",
      },
      referredByDoctor: {
        staffId: "string",
        staffName: refferingdoctor,
        staffType: "string",
      },
      attendantName: attendname,
      attendantPhone: attendnamecontact,
      reservationFlag: true,
    };
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(encounter_edited_data),
    };

    fetch(
      `${constant.ApiUrl}/encounters/${encounter_patchid}?fromSaga=true`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
      });
    window.history.back();
  }

  // cancel button
  const backPage = () => {
    window.history.back();
  };
  const onClickChange = () => {};
  // show={showModal} onHide={closeSampleModal}
  return (
    <React.Fragment>
      {/* modal */}
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

      <Row className="main-div ">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          <React.Fragment>
            <div className="div">
              <div style={{ minHeight: "85vh" }}>
                <div className="regHeader">
                  <h1 className="Encounter-Header">Create Visit</h1>
                  <hr />
                </div>
                <Row className="mt-4">
                  <Col md={1}></Col>
                  <Col md={9} className="dataFlr">
                    <div className="dataEncounter">
                      <PatientRow
                        healthId={ides}
                        fullName={fullname}
                        UHId={UhidData}
                        gender={genders}
                        age={number1 - moment(dBirth).format("YYYY")}
                        dateOfBirth={dBirth}
                        mobile={contact}
                      />
                    </div>
                    <Form ref={form}>
                      <div>
                        <Row>
                          <Col md={4}>
                            <div className="formContainer">
                              <Form.Group controlId="exampleForm.Facility">
                                <p className="require visit-type">
                                  Choose Visit Type
                                </p>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={option}
                                  onChange={handleOption}
                                >
                                  <option value="" disabled hidden>
                                    Select a Value
                                  </option>

                                  {createvisitdata.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName ==
                                        "Choose a Visit type" && (
                                        <>
                                          {formItem.elements.map(
                                            (drpItem, drpIndex) => (
                                              <option
                                                value={drpItem.title}
                                                key={drpIndex}
                                              >
                                                {drpItem.title}
                                              </option>
                                            )
                                          )}
                                        </>
                                      )}
                                    </React.Fragment>
                                  ))}
                                </Form.Select>
                              </Form.Group>
                            </div>
                          </Col>
                          {option === "Out Patient" ? (
                            <Col md={8}>
                              <div className="Fradio">
                                <p className="require">Visit Purpose</p>
                                <div className="RadioForm">
                                  <div className="radio">
                                    <input
                                      type="radio"
                                      value="Doctor"
                                      checked={type === "Doctor"}
                                      onChange={clickChange}
                                    />{" "}
                                    Doctor
                                  </div>
                                  <div className="radio">
                                    <input
                                      type="radio"
                                      value="Lab"
                                      checked={type === "Lab"}
                                      onChange={clickChange}
                                    />{" "}
                                    Lab
                                  </div>
                                  <div className="radio">
                                    <input
                                      type="radio"
                                      value="Pharmacy"
                                      checked={type === "Pharmacy"}
                                      onChange={clickChange}
                                      // onChange={onClickChange}
                                    />{" "}
                                    Pharmacy
                                  </div>
                                  {/* <div className='radio'>
                                                            <input
                                                                type="radio"
                                                                value="Optometry"
                                                                checked={type === 'Optometry'}
                                                                onChange={clickChange}
                                                            /> Optometry
                                                        </div> */}
                                </div>
                              </div>
                            </Col>
                          ) : option === "In Patient" ? (
                            <Col md={8}>
                              <div className="Fradio">
                                <p className="require visit-type">
                                  Choose Visit Type
                                </p>
                                <div className="RadioForm">
                                  <div className="radio">
                                    <input
                                      type="radio"
                                      value="Doctor"
                                      checked={type === "Doctor"}
                                      onChange={clickChange}
                                    />{" "}
                                    Doctor
                                  </div>
                                </div>
                              </div>
                            </Col>
                          ) : option === "eSanjeevani" ? (
                            <Col md={8}>
                              <div className="Fradio">
                                <p className="require visit-type">
                                  Click On Submit
                                </p>
                                <div className="RadioForm">
                                  <SaveButton
                                    butttonClick={eSanjeevaniPortal}
                                    class_name="regBtnN"
                                    button_name={"Submit"}
                                  />
                                </div>
                              </div>
                            </Col>
                          ) : (
                            ""
                          )}
                        </Row>
                        <div>
                          {type && type == "Doctor" ? (
                            <div className="Aradio">
                              <p className="require mt-2">
                                <b>Assign to</b>
                              </p>
                              <div className="RadioForm">
                                <div className="radioAssign">
                                  <div className="radioInput">
                                    {users && users.length != 0 ? (
                                      <Row>
                                        {Object.keys(users).map(
                                          (item, index) => (
                                            <Col md={3} key={index}>
                                              <>
                                                {users.length == 1 ? (
                                                  <div>
                                                    <input
                                                      type="radio"
                                                      checked
                                                      name="radio-button"
                                                      id={
                                                        users[item].targetNode
                                                          .properties.uuid
                                                      }
                                                      selected={
                                                        users[item].targetNode
                                                          .labels[0]
                                                      }
                                                      value={
                                                        users[item].targetNode
                                                          .properties.name
                                                      }
                                                      onChange={() =>
                                                        clickAssign(
                                                          users[item].targetNode
                                                            .properties.uuid,
                                                          users[item].targetNode
                                                            .properties.name
                                                        )
                                                      }
                                                    />

                                                    <span className="radio-label">
                                                      {users[item].targetNode
                                                        .labels[0] ==
                                                        "MedicalOfficer" &&
                                                        "Medical Officer"}
                                                      <br></br>
                                                      <p className="radio-label">
                                                        &nbsp; &nbsp; (
                                                        {
                                                          users[item].targetNode
                                                            .properties.name
                                                        }
                                                        )
                                                      </p>
                                                    </span>
                                                  </div>
                                                ) : (
                                                  <div>
                                                    <input
                                                      type="radio"
                                                      value={assignType}
                                                      checked={
                                                        assignType ===
                                                        users[item].targetNode
                                                          .properties.uuid
                                                      }
                                                      onChange={(e) =>
                                                        clickAssign(
                                                          users[item].targetNode
                                                            .properties.uuid
                                                        )
                                                      }
                                                    />

                                                    <span className="radio-label">
                                                      {users[item].targetNode
                                                        .labels[0] ==
                                                        "MedicalOfficer" &&
                                                        "Medical Officer"}
                                                      <br></br>
                                                      <p className="radio-label">
                                                        &nbsp; &nbsp; (
                                                        {
                                                          users[item].targetNode
                                                            .properties.name
                                                        }
                                                        )
                                                      </p>
                                                    </span>
                                                  </div>
                                                )}
                                              </>
                                            </Col>
                                          )
                                        )}
                                      </Row>
                                    ) : (
                                      <Row>
                                        <input
                                          type="radio"
                                          value={assignType}
                                          checked
                                          onChange={(e) =>
                                            clickAssign(assignType)
                                          }
                                        />

                                        <span className="radio-label">
                                          {labels}
                                          <br></br>
                                          <p className="radio-label">
                                            &nbsp; &nbsp; ({doc})
                                          </p>
                                        </span>
                                      </Row>
                                    )}
                                  </div>
                                </div>
                              </div>
                            </div>
                          ) : type === "Lab" ? (
                            <div className="Aradio">
                              <p className="require mt-2">
                                <b>Assign to</b>
                              </p>
                              <div className="RadioForm">
                                <div className="radioAssign">
                                  <div className="radioInput">
                                    {users && users.length != 0 ? (
                                      <Row>
                                        {Object.keys(users).map(
                                          (item, index) => (
                                            <Col md={3} key={index}>
                                              <>
                                                {users.length == 1 ? (
                                                  <div>
                                                    <input
                                                      type="radio"
                                                      checked
                                                      name="radio-button"
                                                      id={
                                                        users[item].targetNode
                                                          .properties.uuid
                                                      }
                                                      selected={
                                                        users[item].targetNode
                                                          .labels[0]
                                                      }
                                                      value={
                                                        users[item].targetNode
                                                          .properties.name
                                                      }
                                                      onChange={() =>
                                                        clickAssign(
                                                          users[item].targetNode
                                                            .properties.uuid,
                                                          users[item].targetNode
                                                            .properties.name
                                                        )
                                                      }
                                                    />

                                                    <span className="radio-label">
                                                      {users[item].targetNode
                                                        .labels[0] ==
                                                        "JuniorLabTechnician" &&
                                                        "Junior Lab Technician"}
                                                      <br></br>
                                                      <p className="radio-label">
                                                        &nbsp; &nbsp; (
                                                        {
                                                          users[item].targetNode
                                                            .properties.name
                                                        }
                                                        )
                                                      </p>
                                                    </span>
                                                  </div>
                                                ) : (
                                                  <div>
                                                    <input
                                                      type="radio"
                                                      value={assignType}
                                                      checked={
                                                        assignType ===
                                                        users[item].targetNode
                                                          .properties.uuid
                                                      }
                                                      onChange={(e) =>
                                                        clickAssign(
                                                          users[item].targetNode
                                                            .properties.uuid
                                                        )
                                                      }
                                                    />

                                                    <span className="radio-label">
                                                      {users[item].targetNode
                                                        .labels[0] ==
                                                        "JuniorLabTechnician" &&
                                                        "Junior Lab Technician"}
                                                      <br></br>
                                                      <p className="radio-label">
                                                        &nbsp; &nbsp; (
                                                        {
                                                          users[item].targetNode
                                                            .properties.name
                                                        }
                                                        )
                                                      </p>
                                                    </span>
                                                  </div>
                                                )}
                                              </>
                                            </Col>
                                          )
                                        )}
                                      </Row>
                                    ) : (
                                      <>
                                        <input
                                          type="radio"
                                          value={assignType}
                                          checked
                                          onChange={(e) =>
                                            clickAssign(assignType)
                                          }
                                        />

                                        <span className="radio-label">
                                          {labels}
                                          <br></br>
                                          <p className="radio-label">
                                            &nbsp; &nbsp; ({doc})
                                          </p>
                                        </span>
                                      </>
                                    )}
                                  </div>
                                </div>
                              </div>
                            </div>
                          ) : (
                            type === "Pharmacy" && (
                              <div className="Aradio">
                                <p className="require mt-2">
                                  <b>Assign to</b>
                                </p>
                                <div className="RadioForm">
                                  <div className="radioAssign">
                                    <div className="radioInput">
                                      {users && users.length != 0 ? (
                                        <Row>
                                          {Object.keys(users).map(
                                            (item, index) => (
                                              <Col md={3} key={index}>
                                                <>
                                                  {users.length == 1 ? (
                                                    <div>
                                                      <input
                                                        type="radio"
                                                        checked
                                                        name="radio-button"
                                                        id={
                                                          users[item].targetNode
                                                            .properties.uuid
                                                        }
                                                        selected={
                                                          users[item].targetNode
                                                            .labels[0]
                                                        }
                                                        value={
                                                          users[item].targetNode
                                                            .properties.name
                                                        }
                                                        onChange={() =>
                                                          clickAssign(
                                                            users[item]
                                                              .targetNode
                                                              .properties.uuid,
                                                            users[item]
                                                              .targetNode
                                                              .properties.name
                                                          )
                                                        }
                                                      />

                                                      <span className="radio-label">
                                                        {users[item].targetNode
                                                          .labels[0] ==
                                                          "JuniorPharmacist" &&
                                                          "Pharmacist"}
                                                        <br></br>
                                                        <p className="radio-label">
                                                          &nbsp; &nbsp; (
                                                          {
                                                            users[item]
                                                              .targetNode
                                                              .properties.name
                                                          }
                                                          )
                                                        </p>
                                                      </span>
                                                    </div>
                                                  ) : (
                                                    <div>
                                                      <input
                                                        type="radio"
                                                        value={assignType}
                                                        checked={
                                                          assignType ===
                                                          users[item].targetNode
                                                            .properties.uuid
                                                        }
                                                        onChange={(e) =>
                                                          clickAssign(
                                                            users[item]
                                                              .targetNode
                                                              .properties.uuid
                                                          )
                                                        }
                                                      />

                                                      <span className="radio-label">
                                                        {users[item].targetNode
                                                          .labels[0] ==
                                                          "JuniorPharmacist" &&
                                                          "Pharmacist"}
                                                        <br></br>
                                                        <p className="radio-label">
                                                          &nbsp; &nbsp; (
                                                          {
                                                            users[item]
                                                              .targetNode
                                                              .properties.name
                                                          }
                                                          )
                                                        </p>
                                                      </span>
                                                    </div>
                                                  )}
                                                </>
                                              </Col>
                                            )
                                          )}
                                        </Row>
                                      ) : (
                                        <>
                                          <input
                                            type="radio"
                                            value={assignType}
                                            checked
                                            onChange={(e) =>
                                              clickAssign(assignType)
                                            }
                                          />

                                          <span className="radio-label">
                                            {labels}
                                            <br></br>
                                            <p className="radio-label">
                                              &nbsp; &nbsp; ({doc})
                                            </p>
                                          </span>
                                        </>
                                      )}
                                    </div>
                                  </div>
                                </div>
                              </div>
                            )
                          )}
                        </div>
                        <Row>
                          <Col md={9}>
                            {!labs ? (
                              ""
                            ) : (
                              <div>
                                <Row style={{ width: "100%" }}>
                                  <Col md={6}>
                                    <div style={{ width: "90%" }}>
                                      <Form.Group
                                        className="mb-3_health"
                                        controlId="exampleForm.Health"
                                      >
                                        <Form.Label className="require">
                                          Referred By
                                        </Form.Label>
                                        <Form.Select
                                          aria-label="Default select select example"
                                          data-val="true"
                                          value={ashaworkername}
                                          onChange={handleOption1}
                                        >
                                          <option value="" disabled hidden>
                                            Select Referred By
                                          </option>
                                          {awlist.map((item, index) => (
                                            <option
                                              data-isd={item.properties.uuid}
                                              value={item.properties.name}
                                              key={index}
                                            >
                                              {item.properties.name}
                                            </option>
                                          ))}
                                        </Form.Select>
                                      </Form.Group>
                                    </div>
                                  </Col>
                                  {/* <Col md={6}>
                                                                    <div style={{ width: "90%" }}>
                                                                        <Form.Group className="mb-3_health" controlId="exampleForm.Health">
                                                                            <Form.Label className="require">Referring doctor</Form.Label>
                                                                            <Form.Control type="text" placeholder="Enter refering doctor name"
                                                                                value={refferingdoctor} onChange={(e) => setRefferingdoctor(e.target.value)} />
                                                                        </Form.Group>
                                                                    </div>
                                                                </Col> */}
                                </Row>
                                <div className="mt-3">
                                  <Form.Label className="require">
                                    <b>Patient Attendant Details</b>
                                  </Form.Label>
                                </div>
                                <Row style={{ width: "100%" }}>
                                  <Col md={6}>
                                    <div style={{ width: "90%" }}>
                                      <Form.Group
                                        className="mb-3_health"
                                        controlId="exampleForm.Health"
                                      >
                                        <Form.Label className="require">
                                          Attendant Name
                                        </Form.Label>
                                        <Form.Control
                                          type="text"
                                          placeholder="Enter attendent name"
                                          value={attendname}
                                          onChange={(e) =>
                                            setAttendname(e.target.value)
                                          }
                                        />
                                      </Form.Group>
                                    </div>
                                  </Col>
                                  <Col md={6}>
                                    <div style={{ width: "90%" }}>
                                      <Form.Group
                                        className="mb-3_health"
                                        controlId="exampleForm.Health"
                                      >
                                        <Form.Label className="require">
                                          Attendant Contact Number
                                        </Form.Label>
                                        <Form.Control
                                          type="phone"
                                          maxLength={10}
                                          placeholder="Enter attendent contact number"
                                          value={attendnamecontact}
                                          onChange={(e) =>
                                            setAttendnamecontact(e.target.value)
                                          }
                                        />
                                      </Form.Group>
                                    </div>
                                  </Col>
                                </Row>
                                <Row align="center" className="rBtn-Row">
                                  <Col md={4}></Col>
                                  <Col md={6} className="align-me2">
                                    <div className="save-btn-section">
                                      <SaveButton
                                        butttonClick={backPage}
                                        class_name="regBtnPC"
                                        button_name="Cancel"
                                      />
                                    </div>
                                    {!resUpdateData ? (
                                      <SaveButton
                                        butttonClick={createencv}
                                        class_name="regBtnN"
                                        button_name="Assign Now"
                                        btnDisable={assignload == true}
                                      />
                                    ) : (
                                      <SaveButton
                                        butttonClick={Editencounterdata}
                                        class_name="regBtnN"
                                        button_name="Save"
                                      />
                                    )}
                                  </Col>
                                </Row>
                              </div>
                            )}
                          </Col>
                          <Col md={2}></Col>
                        </Row>
                      </div>
                    </Form>
                  </Col>
                  <Col md={2}></Col>
                </Row>
              </div>
            </div>
          </React.Fragment>
        </Col>
      </Row>
    </React.Fragment>
  );
}

export default CreateEncounter;
