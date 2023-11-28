import React, { useState, useEffect } from "react";
import { Col, Row, Image, Form, Button, Modal } from "react-bootstrap";
import moment from "moment";
import "./InvestigationTab.css";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import { useDispatch, useSelector } from "react-redux";
import { loadLabOrdersByUhid } from "../../../../redux/actions";
import { Link } from "react-router-dom";
import ViewReport from "../../../LabModule/LabScreens/ViewReport";

let PatID;
function Investigation(props) {
  let dispatch = useDispatch();
  const phc = sessionStorage.getItem("phc");
  const UHID = props.vitalDatas?.Patient?.UHId;
  const EncID = props.vitalDatas?.encounterId;
  const pa_encv_date = props.vitalDatas?.reservationTime;
  PatID = props.vitalDatas?.Patient?.patientId;
  const pa_staffId = props.vitalDatas?.Provider?.memberId;
  const pa_staffName = props.vitalDatas?.Provider?.name;
  const PatName = props.vitalDatas?.Patient?.name;
  const PatHeaId = props.vitalDatas?.Patient?.healthId;
  const PatGen =
    props.vitalDatas?.Patient?.gender.charAt(0).toUpperCase() +
    props.vitalDatas?.Patient?.gender.slice(1);
  const PatDob = moment(new Date(props.vitalDatas?.Patient?.dob)).format(
    "YYYY-MM-DD"
  );
  const PatMob = props.vitalDatas?.Patient?.phone;
  const docName = props.vitalDatas?.Provider?.name;
  const PresentTime = new Date();
  const EffectiveDate = PresentTime.toISOString();

  const { labOrderListByUhid } = useSelector((state) => state.data);

  useEffect(() => {
    if (UHID) {
      dispatch(loadLabOrdersByUhid(UHID));
    }
  }, [UHID]);

  const [latestLabReport, setLatestLabReport] = useState();
  useEffect(() => {
    if (labOrderListByUhid) {
      labOrderListByUhid.map((labReport) => {
        if (labReport.encounter.encounterId == EncID) {
          setLatestLabReport(labReport);
        }
      });
    }
  }, [labOrderListByUhid, EncID]);

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // set _id of submit data toet data for print page
  const [status, setStatus] = useState(false);
  const [Investigations, setInvestigation] = useState([]);
  const obj = [
    ...new Map(
      Investigations.map((item) => [JSON.stringify(item.name), item])
    ).values(),
  ];
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

  useEffect(() => {
    if (investigationRhythm.length != 0) {
      for (var i = 0; i < investigationRhythm.length; i++) {
        // if (Investigations.length != 0) {
        for (var j = 0; j < obj.length; j++) {
          if (
            investigationRhythm[i].includes(obj[j].name) &&
            investigationRhythm[i] == obj[j].name
          ) {
            // ele[i].checked = true;
            console.log("dev", labTestId);
            selectedValue.push({
              labTestId: obj[j].id,
              labTestName: obj[j].name,
              sampleType: obj[j].sampleType,
              sampleSnomedCode: obj[j].sampleSnomedCode,
            });
          }
        }
        // }
      }
    }
  }, [investigationRhythm, selectedValue]);

  const removeInvestigation = () => {
    setStatvalue(false);
    var ele = document.getElementsByName("investigation");
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = false;
        setInvestIgationRhythm([]);
      }
    }
  };

  // fetch all data for generating token no
  const [labTokenNo, setLabTokenNo] = useState();
  const [orderLabId, setOrderLabId] = useState("");
  const [patientOrder, setpatientOrder] = useState("");

  useEffect(() => {
    // investigation data
    fetch(
      `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?page=&size=1000000`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        console.log(res.data.map((e) => e.sampleSnomedCode));
        let testOrderArray = [];
        for (var i = 0; i < res["data"].length; i++) {
          if (res["data"][i]["status"] == "Active") {
            testOrderArray.push(res["data"][i]);
          }
        }
        setStatus(false);
        setInvestigation([...Investigations, ...testOrderArray]);

        // investigation data
        let date = moment(new Date()).format("YYYY-MM-DD");
        let yesterday = moment()
          .subtract(7, "days")
          .startOf("day")
          .format("YYYY-MM-DD")
          .toString();
        fetch(
          `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${yesterday}&endDate=${date}&page=0&size=200`, //size=1000000
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
              `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${date}&endDate=${date}&page=0&size=200`, //size=1000000
              serviceHeaders.getRequestOptions
            )
              .then((res1) => res1.json())
              .then((res1) => {
                let labOrderArray = [];
                if (res1.length != 0) {
                  for (let i = 0; i < res1["content"].length; i++) {
                    if (res1["content"][i]["assignedToLab"] !== null) {
                      labOrderArray.push(res1["content"][i]);

                      if (
                        res1["content"][i].patient.patientId.includes(PatID)
                      ) {
                        setpatientOrder(res1["content"][i]);
                        setOrderLabId(res1["content"][i]["id"]);
                        const lab_order_id = res1["content"][i]["id"];
                        fetch(
                          `${constant.ApiUrl}/lab-orders-svc/laborders/${lab_order_id}`,
                          serviceHeaders.getRequestOptions
                        )
                          .then((resp) => resp.json())
                          .then((resp) => {
                            console.log("hii bro", resp);
                            let testArray = [];
                            for (
                              let i = 0;
                              i < resp["content"][0]["medicalTests"].length;
                              i++
                            ) {
                              testArray.push(
                                resp["content"][0]["medicalTests"][i]
                              );
                              if (resp["content"][0]["isStat"] == true) {
                                setStatvalue(true);
                              } else {
                                setStatvalue(false);
                              }
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
                if (tempLabaray.length == 0) {
                  setLabTokenNo(0);
                } else {
                  setLabTokenNo(Math.max(...tempLabaray));
                }
                setStatus(false);
              });
          });
      });
  }, [status, UHID]);
  // fetch all data for generating token no

  // submit investigation data
  const [buttonClick, setButtonClick] = useState(false);
  function createInvestigation() {
    if (selectedValue.length == 0) {
      Tostify.notifyWarning("Please select atleast one test");
    } else {
      setButtonClick(true);
      const isoDateforgenerated = moment().toISOString(new Date());
      const labOrderData = {
        patient: {
          patientId: PatID,
          citizenId: "",
          healthId: PatHeaId,
          name: PatName,
          gender: PatGen,
          dob: PatDob,
          phone: PatMob,
          uhid: UHID,
        },
        assignedToLab: labTokenNo + 1,
        isStat: statValue,
        status: "ORDER_ACCEPTED",
        originatedBy: "Internal",
        encounter: {
          encounterId: EncID,
          encounterDateTime: pa_encv_date,
          staffId: pa_staffId,
          staffName: pa_staffName,
        },
        medicalTests: selectedValue,
        orderDate: isoDateforgenerated,
      };

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
    }
  }
  // submit investigation data

  // Update investigation data
  const [modalStatus, setModalStatus] = useState(false);
  function updateInvest() {
    if (selectedValue.length == 0) {
      Tostify.notifyWarning("Please select atleast one test");
    } else {
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
    }
  }
  // Update investigation data

  // previous history modal
  const [investigationDataShow, setInvestigationDataShow] = useState(false);
  const investigationClose = () => {
    setInvestigationDataShow(false);
    sessionStorage.removeItem("poUser");
    sessionStorage.removeItem("prevReport");
  };
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

  const mopostatus = (id) => {
    sessionStorage.setItem("poUser", "medical-Officer");
    sessionStorage.setItem("LabOrderId", id);
  };

  return (
    <React.Fragment>
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
                    <b>Lab Services Updated Successfully...!</b>
                  </p>
                ) : (
                  <p className="success-text">
                    <b>Lab Services Ordered Successfully...!</b>
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
      <ViewModalPopups
        chiefClose={investigationClose}
        cheifShow={investigationDataShow}
        PatName={PatName}
        investigationUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        medicalEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="investigation"
      />
      <ToastContainer />

      <div className="form-col">
        <Form className="cheif-complaint-form">
          <Row>
            <Col md={9}>
              <h5 className="vital-header"> Investigaton </h5>
              <h6 className="invest-text">
                {" "}
                Select required investigation for the patient{" "}
              </h6>
              <div className="col-container chief-textarea">
                <Row>
                  <Col md={1}>
                    <Image
                      src="../img/tube.png"
                      className="thumbnail invest"
                      alt="jp"
                    />
                  </Col>
                  <Col md={11}>
                    <Row style={{ width: "100%", display: "inlineFlex" }}>
                      {obj.map((investigation, i) => (
                        <Col md={3} className="dis-list" key={i}>
                          <div className="assessment-buttons">
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
                                <span className={isChecked(investigation)}>
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
                      {/* <Button variant="light" className="print-btn" onClick={investOpen}>
                                                <i className="bi bi-printer"></i>&nbsp;Print
                                            </Button> */}
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
            </Col>
            <Col md={3} className="view-details-history">
              {
                latestLabReport && (
                  // latestLabReport.map((reports, i) => (
                  <div className="col-container chief-textarea visit-textarea">
                    <h5 className="vital-header"> Current Visit </h5>
                    <h4 className="history-date">
                      {moment(latestLabReport.audit.dateModified).format(
                        "DD MMM, YYYY"
                      )}
                    </h4>
                    <p className="doc-name">
                      Medical Officer ({latestLabReport.encounter.staffName}){" "}
                    </p>
                    <Link
                      className="approvalslink"
                      to="/Report"
                      onClick={(e) => mopostatus(latestLabReport.id)}
                    >
                      ID-{latestLabReport.id}
                    </Link>
                    <p className="doc-desc"></p>
                  </div>
                )
                // ))
              }
              <div className="visit-button">
                <Button
                  varient="light"
                  className="float-center view-prev-details"
                  onClick={investigationShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Investigation
                </Button>
              </div>
            </Col>
          </Row>
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
                {!PatMob ? "" : <p>+91-{PatMob}</p>}
              </Col>
            </Row>
            <hr />
            <Row className="inveastigation-details">
              <Col>
                <h3 className="recommended-details">
                  Recommended Investigations
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
    </React.Fragment>
  );
}

export default Investigation;
