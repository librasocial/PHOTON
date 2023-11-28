import React, { useState, useEffect } from "react";
import { Col, Row, Button, Form } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import { Multiselect } from "multiselect-react-dropdown";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import { loadSurgicalDropdown } from "../../../../redux/formUtilityAction";
import { useDispatch, useSelector } from "react-redux";
import PageLoader from "../../../PageLoader";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";

function SurgicalHistory(props) {
  let dispatch = useDispatch();
  const vitalSurgicalData = props.vitalsSurgicalData;
  const UHID = props.vitalsSurgicalData?.Patient?.UHId;
  const EncID = props.vitalsSurgicalData?.encounterId;
  const PatID = props.vitalsSurgicalData?.Patient?.patientId;
  const PatName = props.vitalsSurgicalData?.Patient?.name;
  const PatHeaId = props.vitalsSurgicalData?.Patient?.healthId;
  const PatGen = props.vitalsSurgicalData?.Patient?.gender;
  const PatDob = props.vitalsSurgicalData?.Patient?.dob;
  const PatMob = props.vitalsSurgicalData?.Patient?.phone;
  const docName = props.vitalsSurgicalData?.Provider?.name;

  const { surgicalDropdown } = useSelector((state) => state.formData);

  // page loader
  const [pageLoader, setPageLoader] = useState(true);
  useEffect(() => {
    dispatch(loadSurgicalDropdown(setPageLoader));
  }, []);

  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // previous history modal
  const [surgicalHistoryShow, setSurgicalHistoryShow] = useState(false);
  const surgicalClose = () => setSurgicalHistoryShow(false);
  const surgicalShow = () => setSurgicalHistoryShow(true);
  // previous history modal

  // set state for history type
  const [status, setStatus] = useState();
  const [surgicaltype, setSurgicaltype] = useState("Surgical");
  const [surgicalhistory, setSurgicalHistory] = useState("");
  const [surgicalDataId, setSurgicalDataId] = useState("");
  const [surgicalHistoryFetchData, setSurgicalHistoryFetchData] = useState("");
  const [oldDataId, setOldDataId] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const dateYesterday = date.setDate(date.getDate() - 1);

  const [objectArray, setObjectArray] = useState([]);
  useEffect(() => {
    let dropdownArray = [];
    if (surgicalDropdown) {
      for (var i = 0; i < surgicalDropdown.length; i++) {
        for (var j = 0; j < surgicalDropdown[i].elements.length; j++) {
          dropdownArray.push({
            id: surgicalDropdown[i].elements[j]._id,
            key:
              surgicalDropdown[i].elements[j].code +
              "-" +
              surgicalDropdown[i].elements[j].title,
            code: surgicalDropdown[i].elements[j].code,
            value: surgicalDropdown[i].elements[j].title,
          });
        }
      }
    }
    setObjectArray(dropdownArray);
  }, [surgicalDropdown]);

  const [items, setItems] = useState([]);
  const isValid = items != null && items.length > 0; // For validation
  let value = [];

  for (var i = 0; i < items.length; i++) {
    value.push(items[i].key);
  }

  const medicalValue = value.join("|");

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

  // save surgical history data
  function createSurgicalHistory() {
    const surgicalhistorydata = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      type: surgicaltype,
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
      body: JSON.stringify(surgicalhistorydata),
    };
    fetch(`${constant.ApiUrl}/familymemberhistory`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        Tostify.notifySuccess("Surgical History Saved for" + " " + PatName);
        setStatus(true);
        setItems([]);
      });
    setSurgicalHistory("");
  }
  // save surgical history data

  // update surgical history data
  function updateSurgicalHistory() {
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
          Tostify.notifySuccess("Surgical History Updated for" + " " + PatName);
          setStatus(true);
        });
      setSurgicalDataId("");
      setItems([]);
    }
  }
  // update surgical history data

  // fetching medical history data
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Surgical&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let problem = [];
          if (res["data"].length != 0) {
            setTodayDataID(res["data"][0]["_id"]);
            problem.push(
              ...res["data"][0]["conditions"][0]["problem"].split("|")
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
        `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=&type=Surgical&UHId=${UHID}`,
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
            setSurgicalHistoryFetchData(tempDataArray);
          }
          setStatus(false);
        });
    }
  }, [EncID, status, surgicalDropdown]);
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
          problem.push(...res["conditions"][0]["problem"].split("|"));
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

  // update surgical history
  function updateSurgical() {
    const updatingOlddata = {
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
      body: JSON.stringify(updatingOlddata),
    };
    if (oldDataId != "") {
      fetch(
        `${constant.ApiUrl}/familymemberhistory/${oldDataId}`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          Tostify.notifySuccess(
            "Previous Surgical History Updated for" + " " + PatName
          );
          setStatus(true);
        });
      setItems([]);
      setOldDataId("");
    }
  }
  // update surgical history

  return (
    <React.Fragment>
      <ToastContainer />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <ViewModalPopups
        chiefClose={surgicalClose}
        cheifShow={surgicalHistoryShow}
        PatName={PatName}
        surgicalUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        medicalEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="surgical"
      />
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <div className="form-col">
                <h5 className="vital-header">Surgical History</h5>
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
                        butttonClick={updateSurgical}
                        class_name="regBtnN"
                        button_name="Update Surgical History"
                        btnDisable={!isValid}
                      />
                    ) : (
                      <>
                        {todayDataID ? (
                          <SaveButton
                            butttonClick={(e) =>
                              updateSurgicalHistory(todayDataID)
                            }
                            class_name="regBtnN"
                            button_name="Save surgical history"
                            btnDisable={!isValid}
                          />
                        ) : (
                          <SaveButton
                            butttonClick={createSurgicalHistory}
                            class_name="regBtnN"
                            button_name="Save Surgical History"
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
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  onClick={surgicalShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Surgical History
                </Button>
              </div>
              <div className="history-body-section">
                {Object.keys(surgicalHistoryFetchData).map(
                  (surgicalData, i) => (
                    <React.Fragment key={i}>
                      <h4 className="history-date">
                        {moment(
                          surgicalHistoryFetchData[surgicalData].audit
                            .dateCreated
                        ).format("DD MMM, YYYY hh:mm A")}
                        <span
                          className="edit-medication"
                          onClick={(e) =>
                            updateOldData(
                              surgicalHistoryFetchData[surgicalData]._id
                            )
                          }
                        >
                          <i className="bi bi-pencil chief-edit"></i>
                        </span>
                      </h4>
                      <p className="doc-name">
                        Medical Officer (
                        {surgicalHistoryFetchData[surgicalData]?.doctor}){" "}
                      </p>
                      <p className="doc-desc">
                        {
                          surgicalHistoryFetchData[surgicalData].conditions[0]
                            .problem
                        }
                      </p>
                    </React.Fragment>
                  )
                )}
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default SurgicalHistory;
