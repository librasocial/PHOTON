import React, { useState, useEffect } from "react";
import { Col, Row, Button, Form } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import { Multiselect } from "multiselect-react-dropdown";
import { useDispatch, useSelector } from "react-redux";
import {
  loadMedHistoryItems,
  loadDiagnosisDropdown,
} from "../../../../redux/formUtilityAction";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import PageLoader from "../../../PageLoader";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function MedicalHistory(props) {
  let dispatch = useDispatch();
  const { medHistoryItems } = useSelector((state) => state.formData);
  const { diagnosysDropdown } = useSelector((state) => state.formData);

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

  const UHID = props.vitalsMedicalData?.Patient?.UHId;
  const EncID = props.vitalsMedicalData?.encounterId;
  const PatID = props.vitalsMedicalData?.Patient?.patientId;
  const PatName = props.vitalsMedicalData?.Patient?.name;
  const PatHeaId = props.vitalsMedicalData?.Patient?.healthId;
  const PatGen = props.vitalsMedicalData?.Patient?.gender;
  const PatDob = props.vitalsMedicalData?.Patient?.dob;
  const PatMob = props.vitalsMedicalData?.Patient?.phone;
  const docName = props.vitalsMedicalData?.Provider?.name;
  console.log(props.vitalsMedicalData, "check");
  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // previous history modal
  const [medicalHistoryShow, setMedicalHistoryShow] = useState(false);
  const medicalClose = () => setMedicalHistoryShow(false);
  const medicalShow = () => setMedicalHistoryShow(true);
  // previous history modal

  const [pageLoader, setPageLoader] = useState(true);

  useEffect(() => {
    dispatch(loadDiagnosisDropdown(setPageLoader));
  }, []);

  // set state for history type
  const [status, setStatus] = useState();
  const [medicalhistory, setMedicalHistory] = useState("");
  const [medicalDataId, setMedicalDataId] = useState("");
  const [medicalHistoryFetchData, setMedicalHistoryFetchData] = useState("");

  const [oldDataId, setOldDataId] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const dateYesterday = date.setDate(date.getDate() - 1);

  const [items, setItems] = useState([]);
  const isValid = items != null && items.length > 0; // For validation
  let value = [];
  for (var i = 0; i < items.length; i++) {
    value.push(items[i].key);
  }

  const medicalValue = String(value);

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

  // save medical history data
  function createMedicalHistory() {
    const medicalhistorydata = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: "Medical",
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: medicalValue,
          onsetPeriod: "",
        },
      ],
    };
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(medicalhistorydata),
    };

    fetch(`${constant.ApiUrl}/familymemberhistory`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Medical History Saved for" + " " + PatName);
        setStatus(true);
      });
    setItems([]);
  }
  // save medical history data

  // update medical history data
  function updateMedicalHistory() {
    const updatingMedicaldata = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: medicalValue,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingMedicaldata),
    };
    if (todayDataID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${todayDataID}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess("Medical History Updated for" + " " + PatName);
          setStatus(true);
        });
      setMedicalDataId("");
    }
    setItems([]);
  }
  // update medical history data

  // fetching medical history data
  useEffect(() => {
    dispatch(loadMedHistoryItems());
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Medical&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let problem = [];
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            problem.push(
              ...res["data"][0]["conditions"][0]["problem"].split(",")
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
    if (UHID) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Medical&UHId=${UHID}`,
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
            setMedicalHistoryFetchData(tempDataArray);
          }
          setStatus(false);
        });
    }
  }, [EncID, status, objectArray]);
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
          let problem = [];
          problem.push(...res["conditions"][0]["problem"].split(","));
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
        });
    }
  };
  // update old data

  // update medical data
  function updateOldMedical(e) {
    const updatingMedicaldata = {
      doctor: docName,
      conditions: [
        {
          member: "self",
          problem: medicalValue,
        },
      ],
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingMedicaldata),
    };
    if (oldDataId != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldDataId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Medical History Updated for" + " " + PatName
          );
          setStatus(true);
          setItems([]);
        });
      setMedicalHistory("");
      setOldDataId("");
    }
  }
  // updating medical data

  return (
    <React.Fragment>
      <ToastContainer />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <ViewModalPopups
        chiefClose={medicalClose}
        cheifShow={medicalHistoryShow}
        PatName={PatName}
        medicalUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        medicalEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="medical"
      />
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Medical History</h5>
              <p className="vital-text">
                Type At Least 3 Characters,{" "}
                <span className="vital-exa">E.g., Shou Fra</span>
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
              <div>
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
                <div className="save-btn-section">
                  {oldDataId ? (
                    <SaveButton
                      butttonClick={updateOldMedical}
                      class_name="regBtnN"
                      button_name="Update Medical History"
                      btnDisable={!isValid}
                    />
                  ) : (
                    <>
                      {todayDataID ? (
                        <SaveButton
                          butttonClick={(e) =>
                            updateMedicalHistory(todayDataID)
                          }
                          class_name="regBtnN"
                          button_name="Save medical history"
                          btnDisable={!isValid}
                        />
                      ) : (
                        <SaveButton
                          butttonClick={createMedicalHistory}
                          class_name="regBtnN"
                          button_name="Save Medical History"
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
                  onClick={medicalShow}
                >
                  <i className="fa fa-undo prev-icon"></i> Previous Medical
                  History
                </Button>
              </div>
              <div className="history-body-section">
                {Object.keys(medicalHistoryFetchData).map((medicalData, i) => (
                  <React.Fragment key={i}>
                    <h4 className="history-date">
                      {moment(
                        medicalHistoryFetchData[medicalData].audit.dateCreated
                      ).format("DD MMM, YYYY hh:mm A")}
                      <span
                        className="edit-medication"
                        onClick={(e) =>
                          updateOldData(
                            medicalHistoryFetchData[medicalData]._id
                          )
                        }
                      >
                        <i className="bi bi-pencil chief-edit"></i>
                      </span>
                    </h4>
                    <p className="doc-name">
                      Medical Officer (
                      {medicalHistoryFetchData[medicalData]?.doctor}){" "}
                    </p>
                    <p className="doc-desc">
                      {
                        medicalHistoryFetchData[medicalData].conditions[0]
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
    </React.Fragment>
  );
}

export default MedicalHistory;
