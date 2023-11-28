import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import "../VitalScreenTabs.css";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import { Multiselect } from "multiselect-react-dropdown";
import { loadDiagnosisDropdown } from "../../../../redux/formUtilityAction";
import { useDispatch, useSelector } from "react-redux";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import PageLoader from "../../../PageLoader";

function ProvisionalDiagnosis(props) {
  const UHID = props.ProvDatas?.Patient?.UHId;
  const EncID = props.ProvDatas?.encounterId;
  const PatID = props.ProvDatas?.Patient?.patientId;
  const PatName = props.ProvDatas?.Patient?.name;
  const PatHeaId = props.ProvDatas?.Patient?.healthId;
  const PatGen = props.ProvDatas?.Patient?.gender;
  const PatDob = props.ProvDatas?.Patient?.dob;
  const PatMob = props.ProvDatas?.Patient?.phone;
  const docName = props.ProvDatas?.Provider?.name;

  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  let dispatch = useDispatch();
  const { diagnosysDropdown } = useSelector((state) => state.formData);

  // previous history modal
  const [provisionalDiagnosisShow, setProvisionalDiagnosis] = useState(false);
  const provisionalClose = () => setProvisionalDiagnosis(false);
  const provisionalShow = () => setProvisionalDiagnosis(true);
  // previous history modal

  // set state for history type
  const [status, setStatus] = useState();
  const [provDiagnosis, setProvDiagnosis] = useState("");
  const [provDiaDataId, setProvDiaDataId] = useState("");
  const [provHistoryFetchData, setProvHistoryFetchData] = useState("");

  const [oldDataId, setOldDataId] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const dateYesterday = date.setDate(date.getDate() - 1);

  // for loader
  const [pageLoader, setPageLoader] = useState(true);

  useEffect(() => {
    dispatch(loadDiagnosisDropdown(setPageLoader));
  }, []);

  const [objectArray, setObjectArray] = useState([]);
  useEffect(() => {
    let dropdownArray = [];
    if (diagnosysDropdown) {
      for (var i = 0; i < diagnosysDropdown.length; i++) {
        for (var j = 0; j < diagnosysDropdown[i].elements.length; j++) {
          dropdownArray.push({
            id: diagnosysDropdown[i].elements[j]._id,
            key:
              diagnosysDropdown[i].elements[j].code +
              "-" +
              diagnosysDropdown[i].elements[j].title,
            code: diagnosysDropdown[i].elements[j].code,
            value: diagnosysDropdown[i].elements[j].title,
          });
        }
      }
    }
    setObjectArray(dropdownArray);
  }, [diagnosysDropdown]);

  const [items, setItems] = useState([]);
  const isValid = items != null && items.length > 0; // For validation

  let value = [];

  for (var i = 0; i < items.length; i++) {
    value.push(items[i].key);
  }

  const handleSelect = (selectedList) => {
    setItems(selectedList);
  };

  const handleRemove = (selectedList) => {
    setItems(selectedList);
  };
  const deleteItem = (e) => {
    setItems((current) =>
      current.filter((items) => {
        return items.id !== e;
      })
    );
  };

  // save provisional diagnosis data
  function createProvisionalDiagnosis() {
    const provisionaldata = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: "Clinical Diagnosis",
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Provisional diagnosis",
          observations: [
            {
              observationName: "Provisional diagnosis",
              observationValues: value,
            },
          ],
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(provisionaldata),
    };

    fetch(`${constant.ApiUrl}/Observations`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess(
          "Provisional Diagnosis Saved for" + " " + PatName
        );
        setStatus(true);
      });
    setProvDiagnosis("");
  }
  // save provisional diagnosis data

  // update provisional diagnosis data
  function updateProvisionalDiagnosis() {
    const updatingProvisionaldata = {
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Provisional diagnosis",
          observations: [
            {
              observationName: "Provisional diagnosis",
              observationValues: value,
            },
          ],
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingProvisionaldata),
    };

    if (todayDataID != undefined) {
      fetch(`${constant.ApiUrl}/Observations/${todayDataID}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Provisional Diagnosis Updated for" + " " + PatName
          );
          setStatus(true);
        });
      setProvDiaDataId("");
      setProvDiagnosis("");
    }
  }
  // update provisional diagnosis data

  // fetching provisional diagnosis data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/Observations/filter?page=1&size=&type=Clinical Diagnosis&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let problem = [];
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            problem.push(
              ...res["data"][0]["assessments"][0]["observations"][0][
                "observationValues"
              ]
            );
            setStatus(false);
            let itemsGet = [];
            for (var i = 0; i < objectArray.length; i++) {
              for (var j = 0; j < problem.length; j++) {
                if (problem[j].includes(objectArray[i].key)) {
                  itemsGet.push(objectArray[i]);
                }
              }
            }
            setItems(itemsGet);
          }
        });
    }

    fetch(
      `${constant.ApiUrl}/Observations/filter?page=1&size=&type=Clinical Diagnosis&UHId=${UHID}`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        let tempDataArray = [];

        if (res.length != 0) {
          for (var i = 0; i < res["data"].length; i++) {
            if (
              moment(res["data"][i].audit.dateCreated).format("YYYY-MM-DD") ==
              moment(dateYesterday).format("YYYY-MM-DD")
            ) {
              tempDataArray.push(res["data"][i]);
            }
          }
        }
        if (tempDataArray.length != 0) {
          setProvHistoryFetchData(tempDataArray);
        }
        setStatus(false);
      });
  }, [EncID, UHID, status, objectArray]);
  // fetching provisional diagnosis data

  // update old data
  function updateOldData(e) {
    setOldDataId(e);
    let oldid = e;
    if (oldid != "") {
      fetch(
        `${constant.ApiUrl}/Observations/${oldid}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          // if (res.length != 0) {
          //     setProvDiagnosis(res['assessments'][0]['observations'][0]['observationValues'])
          //     setStatus(false);
          // }
          let problem = [];
          if (res) {
            setTodayDataID(res["_id"]);
            problem.push(
              ...res["assessments"][0]["observations"][0]["observationValues"]
            );
            setStatus(false);
            let itemsGet = [];
            for (var i = 0; i < objectArray.length; i++) {
              for (var j = 0; j < problem.length; j++) {
                if (problem[j].includes(objectArray[i].key)) {
                  itemsGet.push(objectArray[i]);
                }
              }
            }
            setItems(itemsGet);
          }
        });
    }
  }
  // update old data

  // update provisional diagnosis data
  function updateOldProvisional(e) {
    const updatingProvisionalData = {
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Provisional diagnosis",
          observations: [
            {
              observationName: "Provisional diagnosis",
              observationValues: value,
            },
          ],
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingProvisionalData),
    };
    if (oldDataId != undefined) {
      fetch(`${constant.ApiUrl}/Observations/${oldDataId}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Provisional Diagnosis Updated for" + " " + PatName
          );
          setStatus(true);
          setItems([]);
        });
      setProvDiagnosis("");
      setOldDataId("");
    }
  }
  // updating provisional diagnosis data

  return (
    <React.Fragment>
      <ToastContainer />
      <ViewModalPopups
        chiefClose={provisionalClose}
        cheifShow={provisionalDiagnosisShow}
        PatName={PatName}
        provisionalUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        provisionalId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="provisional"
      />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <div className="form-col">
                <h5 className="vital-header">Provisional Diagnosis</h5>
                <div className="col-container chief-textarea">
                  <p className="vital-text">
                    Type At Least 3 Characters,{" "}
                    <span className="vital-exa">E.g., shou fra</span>
                  </p>
                  <Multiselect
                    options={objectArray} // Options to display in the dropdown
                    selectedValues={items} // Preselected value to persist in dropdown
                    onSelect={handleSelect} // Function will trigger on select event
                    onRemove={handleRemove} // Function will trigger on remove event
                    showCheckbox={true}
                    placeholder="Search and Add"
                    displayValue="key" // Property name to display in the dropdown options
                  />
                </div>
                <br />
                {items &&
                  items.map((items, i) => (
                    <div key={i} className="item_display">
                      <Row>
                        <Col md={9}>{items.key}</Col>
                        <Col md={2}>
                          <span onClick={(e) => deleteItem(items.id)}>
                            <i
                              className="bi bi-trash del-icon"
                              style={{ cursor: "pointer" }}
                            ></i>
                          </span>
                        </Col>
                      </Row>
                      {/* <p>{items.key}&nbsp;&nbsp;<span onClick={(e) => deleteItem(items.id)}><i className="bi bi-trash del-icon" styles={{ cursor: "pointer" }}></i></span></p> */}
                    </div>
                  ))}
                <br />
                <div>
                  <br />
                  <br />
                  {oldDataId ? (
                    <Button
                      variant="primary"
                      className="onsave-btn"
                      onClick={updateOldProvisional}
                      disabled={!isValid}
                    >
                      Update Provisional Diagnosis
                    </Button>
                  ) : (
                    <>
                      {todayDataID ? (
                        <Button
                          variant="primary"
                          className="onsave-btn"
                          disabled={!isValid}
                          onClick={(e) =>
                            updateProvisionalDiagnosis(todayDataID)
                          }
                        >
                          Save provisional diagnosis
                        </Button>
                      ) : (
                        <Button
                          variant="primary"
                          className="onsave-btn"
                          disabled={!isValid}
                          onClick={createProvisionalDiagnosis}
                        >
                          Save Provisional Diagnosis
                        </Button>
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
                  onClick={provisionalShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Provisional Diagnosis
                </Button>
              </div>
              <div className="history-body-section">
                {provHistoryFetchData &&
                  Object.keys(provHistoryFetchData).map((provData, i) => (
                    <React.Fragment key={i}>
                      <h4 className="history-date">
                        {moment(
                          provHistoryFetchData[provData].audit.dateCreated
                        ).format("DD MMM, YYYY hh:mm A")}
                        <span
                          className="edit-medication"
                          onClick={(e) =>
                            updateOldData(provHistoryFetchData[provData]._id)
                          }
                        >
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      </h4>
                      <p className="doc-name">
                        Medical Officer (
                        {provHistoryFetchData[provData]?.doctor}){" "}
                      </p>
                      {provHistoryFetchData[
                        provData
                      ].assessments[0].observations[0].observationValues?.map(
                        (value, i) => (
                          <p className="doc-desc" key={i}>
                            {value}
                          </p>
                        )
                      )}
                    </React.Fragment>
                  ))}
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default ProvisionalDiagnosis;
