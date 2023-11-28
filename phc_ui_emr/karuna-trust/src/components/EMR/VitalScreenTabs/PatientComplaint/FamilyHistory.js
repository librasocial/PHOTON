import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import { ToastContainer } from "react-toastify";
import * as Tostify from "../../../ConstUrl/Tostify";

function FamilyHistory(props) {
  const UHID = props.vitalsFamilyData?.Patient?.UHId;
  const EncID = props.vitalsFamilyData?.encounterId;
  const PatID = props.vitalsFamilyData?.Patient?.patientId;
  const PatName = props.vitalsFamilyData?.Patient?.name;
  const PatHeaId = props.vitalsFamilyData?.Patient?.healthId;
  const PatGen = props.vitalsFamilyData?.Patient?.gender;
  const PatDob = props.vitalsFamilyData?.Patient?.dob;
  const PatMob = props.vitalsFamilyData?.Patient?.phone;
  const docName = props.vitalsFamilyData?.Provider?.name;

  const [status, setStatus] = useState();
  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const dateYesterday = date.setDate(date.getDate() - 1);

  // previous history modal
  const [familyHistoryShow, setFamilyHistoryShow] = useState(false);
  const familyClose = () => setFamilyHistoryShow(false);
  const familyShow = () => setFamilyHistoryShow(true);
  // previous history modal

  // set state for history type
  const [familyhistory, setFamilyHistory] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const [familytype, setFamilytype] = useState("Family");
  const [familyDataId, setFamilyDataId] = useState("");
  const [familyHistoryFetchData, setFamilyHistoryFetchData] = useState("");
  const [oldDataId, setOldDataId] = useState("");

  const isValid = familyhistory != null && familyhistory.length > 0; // For validation

  // post medical history data
  function createFamilyHistory() {
    const familyhistoryData = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: familytype,
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: familyhistory,
          onsetPeriod: "",
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(familyhistoryData),
    };
    fetch(`${constant.ApiUrl}/familymemberhistory`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Family History Saved for" + " " + PatName);
        setStatus(true);
      });
    setFamilyHistory("");
  }
  // post medical history data

  // update today data
  function updateFamilyHistory() {
    const updatingfamilyhistoryData = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: familyhistory,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingfamilyhistoryData),
    };
    if (todayDataID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${todayDataID}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess("Family History Updated for" + " " + PatName);
          setStatus(true);
        });
    }
    setFamilyHistory("");
    setFamilyDataId("");
  }
  // update today data

  // fetching medical history data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Family&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            setFamilyHistory(res["data"][0]["conditions"][0]["problem"]);
            setStatus(false);
          }
        });
    }

    if (UHID) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Family&&UHId=${UHID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let tempDataArray = [];
          for (var i = 0; i < res["data"].length; i++) {
            if (
              moment(res["data"][i].audit.dateCreated).format("YYYY-MM-DD") ==
              moment(dateYesterday).format("YYYY-MM-DD")
            ) {
              tempDataArray.push(res["data"][i]);
            }
          }
          if (tempDataArray.length != 0) {
            setFamilyHistoryFetchData(tempDataArray);
          }
          setStatus(false);
        });
    }
  }, [EncID, status]);
  // fetching medical history data

  // update old data
  const updateOldData = (e) => {
    setOldDataId(e);
    let oldid = e;
    if (oldid != "") {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldid}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setFamilyHistory(res["conditions"][0]["problem"]);
          setStatus(false);
        });
    }
  };
  // update old data

  // update history
  function updateOldFM() {
    const familyhistoryData = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: familyhistory,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(familyhistoryData),
    };
    if (oldDataId != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldDataId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Family History Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setFamilyHistory("");
    setOldDataId("");
  }
  // update history

  return (
    <React.Fragment>
      <ToastContainer />
      <ViewModalPopups
        chiefClose={familyClose}
        cheifShow={familyHistoryShow}
        PatName={PatName}
        familyUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        medicalEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="family"
      />
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Family History</h5>
              <div className="col-container chief-textarea">
                <Form.Group className="mb-3_fname">
                  <Form.Control
                    as="textarea"
                    className="chief-textare-input"
                    placeholder="Start Typing Here..."
                    value={familyhistory}
                    onChange={(event) => setFamilyHistory(event.target.value)}
                  />
                </Form.Group>
                {oldDataId ? (
                  <div className="save-btn-section">
                    <SaveButton
                      butttonClick={updateOldFM}
                      class_name="regBtnN"
                      button_name="Update Family History"
                      btnDisable={!isValid}
                    />
                  </div>
                ) : (
                  <div className="save-btn-section">
                    {todayDataID ? (
                      <SaveButton
                        butttonClick={(e) => updateFamilyHistory(todayDataID)}
                        class_name="regBtnN"
                        button_name="Save family history"
                        btnDisable={!isValid}
                      />
                    ) : (
                      <SaveButton
                        butttonClick={createFamilyHistory}
                        class_name="regBtnN"
                        button_name="Save Family History"
                        btnDisable={!isValid}
                      />
                    )}
                  </div>
                )}
              </div>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="complaint-history-body">
                <div className="history-btn-div">
                  <Button
                    varient="light"
                    className="view-prev-details"
                    onClick={familyShow}
                  >
                    <i className="fa fa-undo prev-icon"></i>
                    Previous Family History
                  </Button>
                </div>
                <div className="history-body-section">
                  {Object.keys(familyHistoryFetchData).map(
                    (familyhistoryData, i) => (
                      <React.Fragment key={i}>
                        <h4 className="history-date">
                          {moment(
                            familyHistoryFetchData[familyhistoryData].audit
                              .dateCreated
                          ).format("DD MMM, YYYY hh:mm A")}
                          <span
                            className="edit-medication"
                            onClick={(e) =>
                              updateOldData(
                                familyHistoryFetchData[familyhistoryData]?._id
                              )
                            }
                          >
                            <i className="bi bi-pencil chief-edit"></i>
                          </span>
                        </h4>
                        <p className="doc-name">
                          Medical Officer (
                          {familyHistoryFetchData[familyhistoryData]?.doctor}){" "}
                        </p>
                        <p className="doc-desc">
                          {
                            familyHistoryFetchData[familyhistoryData]
                              .conditions[0].problem
                          }
                        </p>
                      </React.Fragment>
                    )
                  )}
                </div>
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default FamilyHistory;
