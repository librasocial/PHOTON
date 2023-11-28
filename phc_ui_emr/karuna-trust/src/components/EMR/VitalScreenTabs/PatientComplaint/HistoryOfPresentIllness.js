import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import "../VitalScreenTabs.css";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function HistoryOfPresentIllness(props) {
  const UHID = props.vitalDatas?.Patient?.UHId;
  const EncID = props.vitalDatas?.encounterId;
  const PatID = props.vitalDatas?.Patient?.patientId;
  const PatName = props.vitalDatas?.Patient?.name;
  const PatHeaId = props.vitalDatas?.Patient?.healthId;
  const PatGen = props.vitalDatas?.Patient?.gender;
  const PatDob = props.vitalDatas?.Patient?.dob;
  const PatMob = props.vitalDatas?.Patient?.phone;
  const docName = props.vitalDatas?.Provider?.name;

  // set state for fetced data
  const [history, setHistory] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const [historytFetchData, setHistoryFetchData] = useState("");

  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const dateYesterday = date.setDate(date.getDate() - 1);
  const [status, setStatus] = useState();
  const [oldDataId, setOldDataId] = useState("");

  const isValid = history != null && history.length > 0; // For validation
  // post illness data
  function createHistory() {
    const historyData = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: "Illness",
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: history,
          onsetPeriod: "",
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(historyData),
    };
    fetch(`${constant.ApiUrl}/familymemberhistory`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess(
          "History Of Present Illness Saved for" + " " + PatName
        );
        setStatus(true);
      });
    setHistory("");
  }
  // post illness data

  // update today data
  const updateHistory = (e) => {
    let illnessId = e;
    const updatinghistorydata = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: history,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatinghistorydata),
    };

    if (illnessId != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${illnessId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "History Of Present Illness Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setHistory("");
  };
  // update today data

  // fetch illness data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Illness&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            setHistory(res["data"][0]["conditions"][0]["problem"]);
            setStatus(false);
          }
        });
    }

    fetch(
      `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Illness&UHId=${UHID}`,
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
          setHistoryFetchData(tempDataArray);
        }
        setStatus(false);
      });
  }, [EncID, status]);
  // fetch illness data

  // update old data
  const updateOldData = (e) => {
    setOldDataId(e);
    let oldId = e;

    if (oldId != "") {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setHistory(res["conditions"][0]["problem"]);
          setStatus(false);
        });
    }
  };
  // update old data

  // update illness data
  function updateIllnessHistory() {
    const historyData = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: history,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(historyData),
    };
    if (oldDataId != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldDataId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous History Of Present Illness Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setHistory("");
    setOldDataId("");
  }
  // update illness data

  // previous history modal
  const [historyShow, sethistoryShow] = useState(false);
  const vitalClose = () => sethistoryShow(false);
  const vitalShow = () => sethistoryShow(true);
  // previous history modal

  return (
    <React.Fragment>
      <ToastContainer />
      <ViewModalPopups
        chiefClose={vitalClose}
        cheifShow={historyShow}
        PatName={PatName}
        historyUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        historyEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="illness"
      />
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <div className="form-col">
                <h5 className="vital-header">History Of Present Illness</h5>
                <div className="col-container chief-textarea">
                  <Form.Group className="mb-3_fname">
                    <Form.Control
                      as="textarea"
                      className="chief-textare-input"
                      placeholder="Start Typing Here..."
                      value={history || ""}
                      onChange={(event) => setHistory(event.target.value)}
                    />
                  </Form.Group>
                  <div className="save-btn-section">
                    {oldDataId ? (
                      <SaveButton
                        butttonClick={updateIllnessHistory}
                        class_name="regBtnN"
                        button_name="Update History Of Present Illness"
                        btnDisable={!isValid}
                      />
                    ) : (
                      <>
                        {todayDataID ? (
                          <SaveButton
                            butttonClick={(e) => updateHistory(todayDataID)}
                            class_name="regBtnN"
                            button_name="Save history of present illness"
                            btnDisable={!isValid}
                          />
                        ) : (
                          <SaveButton
                            butttonClick={createHistory}
                            class_name="regBtnN"
                            button_name="Save History Of Present Illness"
                            btnDisable={!isValid}
                          />
                        )}
                      </>
                    )}
                  </div>
                </div>
              </div>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="complaint-history-body">
                <div className="history-btn-div">
                  <Button
                    varient="light"
                    className="view-prev-details"
                    onClick={vitalShow}
                  >
                    <i className="fa fa-undo prev-icon"></i>
                    Previous Illness
                  </Button>
                </div>
                <div className="history-body-section">
                  {Object.keys(historytFetchData).map((historyData, i) => (
                    <React.Fragment key={i}>
                      <h4 className="history-date">
                        {moment(
                          historytFetchData[historyData]?.audit.dateCreated
                        ).format("DD MMM, YYYY H:mm:ss")}
                        <span
                          className="edit-medication"
                          onClick={(e) =>
                            updateOldData(historytFetchData[historyData]?._id)
                          }
                        >
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      </h4>
                      <p className="doc-name">
                        Medical Officer (
                        {historytFetchData[historyData]?.doctor}){" "}
                      </p>
                      <p className="doc-desc" key={i}>
                        {historytFetchData[historyData].conditions[0].problem}{" "}
                      </p>
                    </React.Fragment>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default HistoryOfPresentIllness;
