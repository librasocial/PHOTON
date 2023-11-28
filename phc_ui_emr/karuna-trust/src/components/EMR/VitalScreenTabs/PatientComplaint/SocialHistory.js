import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../../EMR_Buttons/SaveButton";

function SocialHistory(props) {
  const UHID = props.vitalsSocialData?.Patient?.UHId;
  const EncID = props.vitalsSocialData?.encounterId;
  const PatID = props.vitalsSocialData?.Patient?.patientId;
  const PatName = props.vitalsSocialData?.Patient?.name;
  const PatHeaId = props.vitalsSocialData?.Patient?.healthId;
  const PatGen = props.vitalsSocialData?.Patient?.gender;
  const PatDob = props.vitalsSocialData?.Patient?.dob;
  const PatMob = props.vitalsSocialData?.Patient?.phone;
  const docName = props.vitalsSocialData?.Provider?.name;

  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const dateYesterday = date.setDate(date.getDate() - 1);
  const [todayDataID, setTodayDataID] = useState("");

  // previous history modal
  const [socialHistoryShow, setSocialHistoryShow] = useState(false);
  const socialClose = () => setSocialHistoryShow(false);
  const socialShow = () => setSocialHistoryShow(true);
  // previous history modal

  // set state for history type
  const [status, setStatus] = useState();
  const [socialtype, setSocialtype] = useState("Social");
  const [socialhistory, setSocialHistory] = useState("");
  const [socialDataId, setSocialDataId] = useState("");
  const [socialHistoryFetchData, setSocialHistoryFetchData] = useState("");
  const [oldDataId, setOldDataId] = useState("");

  // set state for history type
  const isValid = socialhistory != null && socialhistory.length > 0; // For validation
  // save medical history data
  function createSocialHistory() {
    const socialhistorydata = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: socialtype,
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: socialhistory,
          onsetPeriod: "",
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(socialhistorydata),
    };
    fetch(`${constant.ApiUrl}/familymemberhistory`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Social History Saved for" + " " + PatName);
        setStatus(true);
      });
    setSocialHistory("");
  }
  // save medical history data

  // update today data
  function updateSocialHistory() {
    const updatingSocialdata = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: socialhistory,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingSocialdata),
    };
    if (todayDataID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${todayDataID}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess("Social History Updated for" + " " + PatName);
          setStatus(true);
        });
      setSocialHistory("");
      setSocialDataId("");
    }
  }
  // update today data

  // fetching social history data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Social&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            setSocialHistory(res["data"][0]["conditions"][0]["problem"]);
            setStatus(false);
          }
        });
    }
    if (UHID) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Social&UHId=${UHID}`,
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
            setSocialHistoryFetchData(tempDataArray);
          }
          setStatus(false);
        });
    }
  }, [EncID, status]);
  // fetching social history data

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
          setSocialHistory(res["conditions"][0]["problem"]);
          setStatus(false);
        });
    }
  };
  // update old data

  // update social history
  function updateOldMS() {
    const familyhistoryData = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: socialhistory,
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
            "Previous Social History Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setSocialHistory("");
    setOldDataId("");
  }
  // update social history

  return (
    <React.Fragment>
      <ToastContainer />
      <Row>
        <ViewModalPopups
          chiefClose={socialClose}
          cheifShow={socialHistoryShow}
          PatName={PatName}
          socialUHID={UHID}
          PatHeaId={PatHeaId}
          PatGen={PatGen}
          PatDob={PatDob}
          PatMob={PatMob}
          medicalEncounterId={EncID}
          dateto={dateto}
          datefrom={datefrom}
          modType="social"
        />
        <div className="form-col">
          <Form className="patient-history-form med-history">
            <Row>
              <Col md={8}>
                <h5 className="vital-header">Social History</h5>
                <div className="col-container chief-textarea">
                  <Form.Group className="mb-3_fname">
                    <Form.Control
                      as="textarea"
                      className="chief-textare-input"
                      placeholder="Start Typing Here..."
                      value={socialhistory}
                      onChange={(event) => setSocialHistory(event.target.value)}
                    />
                  </Form.Group>
                  <div className="save-btn-section">
                    {oldDataId ? (
                      <SaveButton
                        butttonClick={updateOldMS}
                        class_name="regBtnN"
                        button_name="Update Social History"
                        btnDisable={!isValid}
                      />
                    ) : (
                      <>
                        {todayDataID ? (
                          <SaveButton
                            butttonClick={(e) =>
                              updateSocialHistory(todayDataID)
                            }
                            class_name="regBtnN"
                            button_name="Save social history"
                            btnDisable={!isValid}
                          />
                        ) : (
                          <SaveButton
                            butttonClick={createSocialHistory}
                            class_name="regBtnN"
                            button_name="Save Social History"
                            btnDisable={!isValid}
                          />
                        )}
                      </>
                    )}
                  </div>
                </div>
              </Col>
              <Col md={4} className="view-details-history">
                <div className="history-btn-div">
                  <Button
                    varient="light"
                    className="view-prev-details"
                    onClick={socialShow}
                  >
                    <i className="fa fa-undo prev-icon"></i>
                    Previous Social History
                  </Button>
                </div>
                <div className="history-body-section">
                  {Object.keys(socialHistoryFetchData).map((socialData, i) => (
                    <React.Fragment key={i}>
                      <h4 className="history-date">
                        {moment(
                          socialHistoryFetchData[socialData].audit.dateCreated
                        ).format("DD MMM, YYYY hh:mm A")}
                        <span
                          className="edit-medication"
                          onClick={(e) =>
                            updateOldData(
                              socialHistoryFetchData[socialData]?._id
                            )
                          }
                        >
                          {" "}
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      </h4>
                      <p className="doc-name">
                        Medical Officer (
                        {socialHistoryFetchData[socialData]?.doctor}){" "}
                      </p>
                      <p className="doc-desc">
                        {
                          socialHistoryFetchData[socialData].conditions[0]
                            .problem
                        }
                      </p>
                    </React.Fragment>
                  ))}
                </div>
              </Col>
            </Row>
          </Form>
        </div>
      </Row>
    </React.Fragment>
  );
}

export default SocialHistory;
