import React, { useEffect, useState } from "react";
import { Form, Button, Row, Col } from "react-bootstrap";
import "../VitalScreenTabs.css";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import moment from "moment";
import { Multiselect } from "multiselect-react-dropdown";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import { loadDiagnosisDropdown } from "../../../../redux/formUtilityAction";
import { useDispatch, useSelector } from "react-redux";
import PageLoader from "../../../PageLoader";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function FinalDiagnosis(props) {
  const UHID = props.vitalDatas?.Patient?.UHId;
  const EncID = props.vitalDatas?.encounterId;
  const PatID = props.vitalDatas?.Patient?.patientId;
  const docName = props.vitalDatas?.Provider?.name;

  const date = new Date();

  // set state for history type
  const [status, setStatus] = useState();
  const [finalDiagnosis, setFinalDiagnosis] = useState("");
  const [finalDiagnoDataId, setFinalDiagnoDataId] = useState("");
  const [finalDiagnosisFetchData, setFinalDiagnosisFetchData] = useState("");

  const [oldDataId, setOldDataId] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const dateYesterday = date.setDate(date.getDate() - 1);

  let dispatch = useDispatch();
  const { diagnosysDropdown } = useSelector((state) => state.formData);

  // for loader
  const [pageLoader, setPageLoader] = useState(true);

  useEffect(() => {
    dispatch(loadDiagnosisDropdown(setPageLoader));
  }, []);

  const [objectArray, setObjectArray] = useState([]);
  useEffect(() => {
    if (diagnosysDropdown.length != 0) {
      let dropdownArray = [];
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
      setObjectArray(dropdownArray);
    }
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

  // post final diagnosis data
  function createFinalDiagnosis() {
    const finalDiagnosisdata = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: "Final Diagnosis",
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Final Diagnosis",
          observations: [
            {
              observationName: "Final Diagnosis",
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
      body: JSON.stringify(finalDiagnosisdata),
    };
    fetch(`${constant.ApiUrl}/Observations`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Final Diagnosis Saved for" + " " + PatName);
        setStatus(true);
      });
    setFinalDiagnosis("");
  }
  // post final diagnosis data

  // update final diagnosis data
  function updateFinalDiagnosis() {
    const updatingFinalDiagnosis = {
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Final Diagnosis",
          observations: [
            {
              observationName: "Final Diagnosis",
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
      body: JSON.stringify(updatingFinalDiagnosis),
    };

    if (todayDataID != undefined) {
      fetch(`${constant.ApiUrl}/Observations/${todayDataID}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess("Final Diagnosis Updated for" + " " + PatName);
          setStatus(true);
        });
      setFinalDiagnoDataId("");
      // setFinalDiagnosis('');
    }
  }
  // update final diagnosis data

  // fetching final diagnosis data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/Observations/filter?page=1&size=5&type=Final Diagnosis&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let problem = [];
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            // setItems(res['data'][0]['conditions'][0]['problem'].split(","));
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
      `${constant.ApiUrl}/Observations/filter?page=1&size=&type=Final Diagnosis&UHId=${UHID}`,
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
          setFinalDiagnosisFetchData(tempDataArray);
        }
        setStatus(false);
      });
  }, [EncID, status, objectArray]);
  // fetching final diagnosis data

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
          if (res.length != 0) {
            setFinalDiagnosis(
              res["assessments"][0]["observations"][0]["observationValues"]
            );
            setStatus(false);
          }
        });
    }
  }
  // update old data

  // update final dianosis data
  function updateOldDiagnosis(e) {
    const updatingOldData = {
      doctor: docName,
      assessments: [
        {
          assessmentTitle: "Final Diagnosis",
          observations: [
            {
              observationName: "Final Diagnosis",
              observationValues: [finalDiagnosis],
            },
          ],
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingOldData),
    };
    if (oldDataId != "") {
      fetch(`${constant.ApiUrl}/Observations/${oldDataId}`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Final Diagnosis Updated for" + " " + PatName
          );
          setStatus(true);
        });
    }
    setFinalDiagnosis("");
    setOldDataId("");
  }
  // update final dianosis data

  return (
    <React.Fragment>
      <ToastContainer />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="form-col">
        <Form className="patient-history-form fin-history">
          <h5 className="vital-header">Final Diagnosis</h5>
          <div className="final-diagnosis-div">
            <div className="final-div">
              <p className="vital-text">
                Type at least 3 characters,{" "}
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
          </div>
          <br />
          {items &&
            items.map((items, i) => (
              <div className="item_display_final" key={i}>
                <div className="item_display">
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
              </div>
            ))}
          <br />
          <div>
            <br />
            <br />
            <div className="save-btn-section">
              {oldDataId ? (
                <SaveButton
                  butttonClick={updateOldDiagnosis}
                  class_name="regBtnN"
                  button_name="Update Final Diagnosis"
                  btnDisable={!isValid}
                />
              ) : (
                <>
                  {todayDataID ? (
                    <SaveButton
                      butttonClick={(e) => updateFinalDiagnosis(todayDataID)}
                      class_name="regBtnN"
                      button_name="Save final diagnosis"
                      btnDisable={!isValid}
                    />
                  ) : (
                    <SaveButton
                      butttonClick={createFinalDiagnosis}
                      class_name="regBtnN"
                      button_name="Save Final Diagnosis"
                      btnDisable={!isValid}
                    />
                  )}
                </>
              )}
            </div>
          </div>
          <div className="history-body-section">
            {finalDiagnosisFetchData &&
              Object.keys(finalDiagnosisFetchData).map(
                (finalDiagnosisData, i) => (
                  <React.Fragment key={i}>
                    <h4 className="history-date">
                      {moment(
                        finalDiagnosisFetchData[finalDiagnosisData].audit
                          .dateCreated
                      ).format("DD MMM, YYYY hh:mm A")}
                      <span
                        className="edit-medication"
                        onClick={(e) =>
                          updateOldData(
                            finalDiagnosisFetchData[finalDiagnosisData]._id
                          )
                        }
                      >
                        <i className="bi bi-pencil chief-edit"></i>
                      </span>
                    </h4>
                    <p className="doc-name">
                      Medical Officer (
                      {finalDiagnosisFetchData[finalDiagnosisData]?.doctor}){" "}
                    </p>
                    <p className="doc-desc">
                      {
                        finalDiagnosisFetchData[finalDiagnosisData]
                          .assessments[0].observations[0].observationValues
                      }
                    </p>
                  </React.Fragment>
                )
              )}
          </div>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default FinalDiagnosis;
