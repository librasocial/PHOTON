import React, { useState, useEffect } from "react";
import "./LabScreens.css";
import "../../EMR/VitalScreenTabs/VitalScreenTabs.css";
import { Col, Row, Image, Form, Button, Modal } from "react-bootstrap";
import moment from "moment";
import LabScreen from "./LabScreen";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import SaveButton from "../../EMR_Buttons/SaveButton";
import LabTechnician from "../../Dashboard/QueueManagement/LabTechnician";
import { useHistory } from "react-router-dom";

export default function OrderLab(props) {
  let history = useHistory();
  const phc = sessionStorage.getItem("phc");
  const PresentTime = new Date();
  const EffectiveDate = PresentTime.toISOString();
  const [status, setStatus] = useState(false);

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // set _id of submit data toet data for print page
  const [investDataId, setInvestDataId] = useState("");
  // set sate for fetch data
  const [investigationData, setInvestigationData] = useState([]);
  // set sate for fetch data
  const [investigationRhythm, setInvestIgationRhythm] = useState([]);

  const [checked, setChecked] = useState([]);
  var a = new Date().toDateString();
  var b = moment("YYYY-MM-DD");
  let newDate = new Date().toDateString();
  let date = moment(newDate).format("YYYY-MM-DD");

  // investigation Modal
  const [investShow, setInvestShow] = useState(false);
  const investClose = () => {
    setInvestShow(false);
  };
  const investOpen = () => setInvestShow(true);

  // Generate string of checked items
  const checkedItems = checked.length
    ? checked.reduce((total, item) => {
        return total + ", " + item;
      })
    : "";

  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  // const [selectedValue, setSelectedValue] = useState([])

  // fetching all lab test data
  const [Investigations, setInvestigation] = useState([]);

  const obj = [
    ...new Map(
      Investigations.map((item) => [JSON.stringify(item.name), item])
    ).values(),
  ];

  const setInvestigationValue = (e) => {
    if (e.target.checked) {
      setInvestIgationRhythm([...investigationRhythm, e.target.value]);
    } else {
      setInvestIgationRhythm(
        investigationRhythm.filter((id) => id !== e.target.value)
      );
    }
  };
  const [statValue, setStatvalue] = useState(false);

  let selectedValue = [];
  if (investigationRhythm.length != 0) {
    for (var i = 0; i < investigationRhythm.length; i++) {
      // if (Investigations.length != 0) {
      for (var j = 0; j < obj.length; j++) {
        if (
          investigationRhythm[i].includes(obj[j].name) &&
          investigationRhythm[i] == obj[j].name
        ) {
          // ele[i].checked = true;
          selectedValue.push({
            labTestId: obj[j].id,
            labTestName: obj[j].name,
            resultType: obj[j].resultType,
            sampleType: obj[j].sampleType,
            sampleSnomedCode: obj[j].sampleSnomedCode,
          });
        }
      }
      // }
    }
  }

  const removeInvestigation = () => {
    var ele = document.getElementsByName("investigation");
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = false;
        investigationRhythm.length = 0;
      }
    }
  };

  // fetch all data for generating token no
  let respateintid = sessionStorage.getItem("LabPateintid");
  let labResId = sessionStorage.getItem("LabResid");
  const [labTokenNo, setLabTokenNo] = useState();
  const [orderLabId, setOrderLabId] = useState("");
  const [patientOrder, setpatientOrder] = useState("");
  let labOrderId = sessionStorage.getItem("labByOrderId");
  const [orderedDetails, setOrderedDetails] = useState([]);

  useEffect(() => {
    document.title = "EMR Ordering Lab Test";
    let date = moment(new Date()).format("YYYY-MM-DD");
    let yesterday = moment()
      .subtract(7, "days")
      .startOf("day")
      .format("YYYY-MM-DD")
      .toString();
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${yesterday}&endDate=${date}&page=0&size=200`, //size=1000000,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        let tempLabaray = [];
        for (let i = 0; i < resp["content"].length; i++) {
          if (resp["content"][i]["assignedToLab"] !== null) {
            tempLabaray.push(resp["content"][i]["assignedToLab"]);
          }
        }

        fetch(
          `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?page=&size=200`, //size=1000000,
          serviceHeaders.getRequestOptions
        )
          .then((res) => res.json())
          .then((res) => {
            let testOrderArray = [];
            for (var i = 0; i < res["data"].length; i++) {
              if (res["data"][i]["status"] == "Active") {
                testOrderArray.push(res["data"][i]);
              }
            }
            setStatus(false);
            setInvestigation([...Investigations, ...testOrderArray]);

            fetch(
              `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${date}&endDate=${date}&page=0&size=200`, //size=1000000,
              serviceHeaders.getRequestOptions
            )
              .then((res) => res.json())
              .then((res) => {
                let labOrderArray = [];
                setStatus(false);

                if (res.length != 0) {
                  for (let i = 0; i < res["content"].length; i++) {
                    if (res["content"][i]["assignedToLab"] !== null) {
                      tempLabaray.push(res["content"][i]["assignedToLab"]);
                      labOrderArray.push(res["content"][i]);

                      if (
                        res["content"][i].patient.patientId.includes(
                          respateintid
                        )
                      ) {
                        setpatientOrder(res["content"][i]);

                        setOrderLabId(res["content"][i]["id"]);
                        const lab_order_id = res["content"][i]["id"];

                        fetch(
                          `${constant.ApiUrl}/lab-orders-svc/laborders/${lab_order_id}`,
                          serviceHeaders.getRequestOptions
                        )
                          .then((res) => res.json())
                          .then((res) => {
                            if (res["content"][0]["isStat"] == true) {
                              setStatvalue(true);
                            } else {
                              setStatvalue(false);
                            }

                            let testArray = [];
                            for (
                              let i = 0;
                              i < res["content"][0]["medicalTests"].length;
                              i++
                            ) {
                              testArray.push(
                                res["content"][0]["medicalTests"][i]
                              );
                            }
                            // setSelectedTestArray(testArray)

                            let rythemArray = [];
                            const result = [
                              ...new Map(
                                testOrderArray.map((item) => [
                                  JSON.stringify(item.name),
                                  item,
                                ])
                              ).values(),
                            ];

                            var ele =
                              document.getElementsByName("investigation");
                            for (var k = 0; k < ele.length; k++) {
                              for (var j = 0; j < testArray.length; j++) {
                                if (
                                  testArray[j].labTestName.includes(
                                    result[k].name
                                  ) &&
                                  testArray[j].labTestName == result[k].name
                                ) {
                                  ele[k].checked = true;
                                  rythemArray.push(testArray[j].labTestName);
                                }
                              }
                            }
                            setInvestIgationRhythm(rythemArray);
                          });
                      }
                    }
                  }
                }
              });
          });
        // }
        if (tempLabaray.length == 0) {
          setLabTokenNo(0);
        } else {
          setLabTokenNo(Math.max(...tempLabaray));
        }
      });
  }, [status]);
  // fetch all data for generating token no

  // submit investigation data
  const [buttonClick, setButtonClick] = useState(false);
  function createInvestigation() {
    setButtonClick(true);
    fetch(
      `${constant.ApiUrl}/reservations/` + labResId,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        let pa_gender =
          res.Patient.gender.charAt(0).toUpperCase() +
          res.Patient.gender.slice(1);
        let pa_dob = moment(new Date(res.Patient.dob)).format("YYYY-MM-DD");

        const isoDateforgenerated = moment().toISOString(new Date());
        const labOrderData = {
          patient: {
            patientId: res.Patient.patientId,
            citizenId: labResId,
            healthId: res.Patient.healthId,
            name: res.Patient.name,
            gender: pa_gender,
            dob: pa_dob,
            phone: res.Patient.phone,
            uhid: res.Patient.UHId,
          },
          assignedToLab: labTokenNo + 1,
          isStat: statValue,
          status: "ORDER_ACCEPTED",
          originatedBy: "External",
          encounter: {
            encounterId: res.encounterId,
            encounterDateTime: res.reservationTime,
            staffId: res.Provider.memberId,
            staffName: res.Provider.name,
          },
          medicalTests: selectedValue,
          orderDate: isoDateforgenerated,
        };
        setStatus(false);

        var requestOptions1 = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(labOrderData),
        };
        fetch(`${constant.ApiUrl}/lab-orders-svc/laborders`, requestOptions1)
          .then((res) => res.json())
          .then((res) => {
            setStatus(true);
            setShowLabModal(true);
            return setButtonClick(false);
          });
      });
    history.push("/Dashboard");
  }
  // submit investigation data

  // Update investigation data
  const [modalStatus, setModalStatus] = useState(false);
  function updateInvest() {
    const updatelabOrderData = {
      type: "LabOrders",
      properties: {
        medicalTests: selectedValue,
        status: "ORDER_ACCEPTED",
        isStat: statValue,
      },
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatelabOrderData),
    };
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${orderLabId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        setShowLabModal(true);
        setModalStatus(true);
      });
    history.push("/Dashboard");
  }
  // Update investigation data

  // previous history modal
  const [investigationDataShow, setInvestigationDataShow] = useState(false);
  const investigationClose = () => setInvestigationDataShow(false);
  const investigationShow = () => setInvestigationDataShow(true);
  // previous history modal

  {
    /* Add Order Lab Modal */
  }
  const [showLabModal, setShowLabModal] = useState(false);
  const closeLabModal = () => {
    setShowLabModal(false);
  };
  {
    /* Add Order Lab Modal */
  }

  return (
    <React.Fragment>
      <div className="div vital-div">
        {/* Modal-2 Create Lab Order */}
        <Modal show={showLabModal} onHide={closeLabModal}>
          <Row>
            <Col xsm={2} sm={2} md={2} className="success-icon" align="center">
              <i className="bi bi-check2 alergy-check2"></i>
            </Col>
            <Col xsm={10} sm={10} md={10} className="allergy-div-box">
              <Row className="test-div">
                <Col xsm={11} sm={11} md={11}>
                  {modalStatus == true ? (
                    <p className="success-text">
                      <b>Lab Services Updated Successfully</b>
                    </p>
                  ) : (
                    <p className="success-text">
                      <b>Lab Services Ordered Successfully</b>
                    </p>
                  )}
                </Col>
                <Col xsm={1} sm={1} md={1} align="center">
                  <button
                    type="button"
                    className="btn-close modelCls"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                    id="btn"
                    onClick={closeLabModal}
                  ></button>
                </Col>
              </Row>
            </Col>
          </Row>
        </Modal>
        {/* Modal-2 Create Lab Order */}

        <div style={{ minHeight: "85vh" }}>
          <LabScreen orderedDetails={orderedDetails} />
          <div className="service-tab">
            <div className="form-col">
              <Form className="cheif-complaint-form">
                <div>
                  <h5 className="vital-header lab-head">Lab Services</h5>
                  <h6 className="invest-text">
                    Select required lab services for the patient
                  </h6>
                </div>
                <div className="col-container chief-textarea">
                  <Row>
                    <Col md={1}>
                      <Image
                        src="../img/tube.png"
                        className="thumbnail invest lab-img"
                        alt="jp"
                      />
                    </Col>
                    <Col md={11}>
                      <Row style={{ width: "100%", display: "inlineFlex" }}>
                        {obj.map((investigation, i) => (
                          <Col md={3} className="dis-list" key={i}>
                            <div className="assessment-buttons my">
                              <React.Fragment>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="investigation"
                                  id={investigation.id}
                                  value={investigation.name}
                                  onClick={setInvestigationValue}
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={investigation.id}
                                >
                                  <span
                                    className={isChecked(investigation)}
                                    value={investigation.id}
                                  >
                                    {investigation.name}
                                  </span>
                                </label>
                              </React.Fragment>
                            </div>
                          </Col>
                        ))}
                      </Row>
                    </Col>
                  </Row>
                  <hr />
                  <Row className="printing">
                    <Col md={4}>
                      <div>
                        {/* <Button variant="light" className="print-btn"
                                                    onClick={investOpen}
                                                ><i className="bi bi-printer"></i>&nbsp;Print</Button> */}
                      </div>
                    </Col>
                    <Col md={4}>
                      <input
                        type="checkbox"
                        id="stat"
                        name="stat"
                        value={statValue}
                        checked={statValue}
                        onChange={() => setStatvalue(!statValue)}
                      />
                      &nbsp; STAT order
                      <p>Select in case of immediate report required</p>
                    </Col>
                    <Col md={4} align="right" className="regFoot align-me2">
                      <div className="save-btn-section">
                        <SaveButton
                          butttonClick={removeInvestigation}
                          class_name="regBtnPC"
                          button_name="Cancel"
                        />
                      </div>
                      <div className="save-btn-section">
                        {orderLabId ? (
                          <SaveButton
                            butttonClick={updateInvest}
                            class_name="regBtnN"
                            button_name="Update"
                          />
                        ) : (
                          <SaveButton
                            butttonClick={createInvestigation}
                            class_name="regBtnN"
                            button_name="Save"
                            btnDisable={buttonClick == true}
                          />
                        )}
                      </div>
                    </Col>
                  </Row>
                </div>
              </Form>
            </div>

            {/*modal-1 modal for investigation print */}
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
                      <p>
                        {/* {docName} */}
                        Dr. Prakash
                      </p>
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
                      <h3 className="pat-fname">
                        {/* {PatName} */} Mr.Seetharaman Nanjundappa
                      </h3>
                    </Col>
                  </Row>
                  <Row className="uhid-detail">
                    <Col md={4}>
                      <p>
                        UHID : SUG10011
                        {/* {UHID} */}
                      </p>
                      <p>
                        Health ID : 88-1234-1234-1234
                        {/* {!PatHeaId ? "" : PatHeaId.replace(/(\d{2})(\d{4})(\d{4})(\d{4})/, "$1-$2-$3-$4")} */}
                      </p>
                    </Col>
                    <Col md={2} className="gender-details">
                      <p>
                        Male
                        {/* {PatGen} */}
                      </p>
                    </Col>
                    <Col md={1}></Col>
                    <Col md={2} className="age-details">
                      <p>
                        52 Years
                        {/* {(a = new Date(), b = (moment(PatDob)), (-b.diff(a, 'years') + " Years"))} */}
                      </p>
                      <p>
                        {/* {moment(PatDob).format('DD MMM YYYY')} */}
                        01 Jan 1971
                      </p>
                    </Col>
                    <Col md={1}></Col>
                    <Col md={2} className="mobile-details">
                      {/* +91-{PatMob} */}
                      +91-98xx6xx35
                    </Col>
                  </Row>
                  <hr />
                  <Row className="inveastigation-details">
                    <Col>
                      <h3 className="recommended-details">
                        Recommended investigations
                      </h3>
                      <Row>
                        <Col md={12}>
                          {investigationData.map((items, i) => (
                            <p style={{ marginTop: "1%" }} key={i}>
                              {items}
                            </p>
                          ))}
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                  <Row className="mo-sign">
                    <Col>
                      <p>Medical Officer Signature / Stamp</p>
                    </Col>
                  </Row>
                  <hr />
                </Col>
                <Modal.Footer className="investigation-button">
                  <Button
                    variant="outline-secondary"
                    className="investigation-cancel-btn"
                    onClick={investClose}
                  >
                    Cancel
                  </Button>
                  <Button variant="light" className="investigation-save-btn">
                    Print
                  </Button>
                </Modal.Footer>
              </Row>
            </Modal>
            {/*modal-1 modal for investigation print */}
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
