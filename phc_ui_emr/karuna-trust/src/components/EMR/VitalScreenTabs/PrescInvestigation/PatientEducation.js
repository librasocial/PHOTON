import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button } from "react-bootstrap";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import "./InvestigationTab.css";

function PatientEducation(props) {
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
  const [education, setEdudcation] = useState("");
  const [todayDataID, setTodayDataID] = useState("");
  const [illnesstype, seaIllnesstype] = useState("Illness");
  const [educationIllDataId, setEdudcationIllDataId] = useState("");
  const [educationFetchData, setEdudcationFetchData] = useState("");

  const date = new Date();
  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const dateYesterday = date.setDate(date.getDate() - 1);
  const [status, setStatus] = useState();
  const [oldDataId, setOldDataId] = useState("");

  const isValid = education != null && education.length > 0; // For validation

  // post illness data
  // function createEducation() {
  //     const educationData = {
  //         "UHId": UHID,
  //         "encounterId": EncID,
  //         "encounterDate": date,
  //         "patientId": PatID,
  //         "type": illnesstype,
  //         "doctor": docName,
  //         "conditions": [
  //             {
  //                 "member": "self",
  //                 "problem": education,
  //                 "onsetPeriod": ""
  //             }
  //         ]
  //     }

  //     var requestOptions = {
  //         headers: serviceHeaders.myHeaders1,
  //         "method": "POST",
  //         "mode": "cors",
  //         body: JSON.stringify(educationData)
  //     };
  //     fetch(`${constant.ApiUrl}/familymembereducation`, requestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             setStatus(true);
  //         })
  //     setEdudcation('')
  // }
  // post illness data

  // update today data
  // const updateEducation = () => {
  //     const updatingeducationdata = {
  //         "doctor": docName,
  //         "conditions": [
  //             {
  //                 "member": "self",
  //                 "problem": education,
  //             }
  //         ]
  //     }

  //     var requestOptions = {
  //         headers: serviceHeaders.myHeaders1,
  //         "method": "PATCH",
  //         "mode": "cors",
  //         body: JSON.stringify(updatingeducationdata)
  //     };
  //     if (todayDataID != undefined) {
  //         fetch(`${constant.ApiUrl}/familymembereducation/${todayDataID}`, requestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             setStatus(true);
  //         })
  //     }
  //     setEdudcationIllDataId("")
  //     setEdudcation('')
  // }
  // update today data

  // fetch illness data
  // useEffect(() => {
  //     if (EncID != undefined) {
  //         fetch(`${constant.ApiUrl}/familymembereducation/filter?page=1&size=&type=Illness&encounterId=${EncID}`, serviceHeaders.getRequestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             if (res['data'].length != 0) {
  //                 setTodayDataID(res['data'][0]['_id'])
  //                 setEdudcation(res['data'][0]['conditions'][0]['problem'])
  //                 setStatus(false);
  //             }
  //         });
  //     }

  //     fetch(`${constant.ApiUrl}/familymembereducation/filter?page=1&size=&type=Illness&UHId=${UHID}`, serviceHeaders.getRequestOptions)
  //     .then((res) => res.json())
  //     .then((res) => {
  //         let tempDataArray = [];
  //         for (var i = 0; i < res['data'].length; i++) {
  //             if ((moment(res['data'][i].audit.dateCreated).format('YYYY-MM-DD')) == (moment(dateYesterday).format('YYYY-MM-DD'))) {
  //                 tempDataArray.push(res['data'][i])
  //             }
  //         }
  //         if (tempDataArray.length != 0) {
  //             setEdudcationFetchData(tempDataArray)
  //         }
  //         setStatus(false);
  //     });
  // }, [EncID, status]);
  // fetch illness data

  // update old data
  // const updateOldData = (e) => {
  //     setOldDataId(e)
  //     let oldId = e;

  //     if (oldId != "") {
  //         fetch(`${constant.ApiUrl}/familymembereducation/${oldId}`, serviceHeaders.getRequestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             setEdudcation(res['conditions'][0]['problem'])
  //             setStatus(false);
  //         })
  //     }
  // }
  // update old data

  // update illness data
  // function updateOldEducation() {
  //     const educationData = {
  //         "doctor": docName,
  //         "conditions": [
  //             {
  //                 "member": "self",
  //                 "problem": education
  //             }
  //         ]
  //     }

  //     var requestOptions = {
  //         headers: serviceHeaders.myHeaders1,
  //         "method": "PATCH",
  //         "mode": "cors",
  //         body: JSON.stringify(educationData)
  //     };
  //     if (oldDataId != undefined) {
  //         fetch(`${constant.ApiUrl}/familymembereducation/${oldDataId}`, requestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {
  //             setStatus(true);
  //         })
  //     }
  //     setEdudcation("")
  //     setOldDataId('')
  // }
  // update illness data

  // previous education modal
  const [patEdu, setPatEdu] = useState(false);
  const patEduClose = () => setPatEdu(false);
  const patEduShow = () => setPatEdu(true);
  // previous education modal

  return (
    <React.Fragment>
      {/* <ViewModalPopups chiefClose={patEduClose} cheifShow={patEduShow} PatName={PatName} patEduUHID={UHID}
                PatHeaId={PatHeaId} PatGen={PatGen} PatDob={PatDob} PatMob={PatMob} dateto={dateto} datefrom={datefrom}
                educationEncounterId={EncID}  /> */}
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Patient Education</h5>
              <div className="col-container chief-textarea">
                <Form.Group
                  className="mb-3_fname"
                  controlId="exampleForm.FName"
                >
                  <Form.Control
                    as="textarea"
                    className="chief-textare-input"
                    placeholder="Start typing here..."
                    value={education}
                    onChange={(event) => setEdudcation(event.target.value)}
                  />
                </Form.Group>
              </div>
              {/* {oldDataId ?
                                <Button variant="primary" className="onsave-btn"
                                    onClick={updateIllnessHistory} disabled={!isValid}>
                                    Update History Of Illness
                                </Button> :
                                <>
                                    {todayDataID ? 
                                        <Button variant="primary" className="onsave-btn"
                                            onClick={(e) => updateHistory(todayDataID)} disabled={!isValid}
                                            >
                                            Save patient education
                                        </Button>
                                        : 
                                        <Button variant="primary" className="onsave-btn"
                                            //onClick={createEducation} disabled={!isValid}
                                            >
                                            Save Patient Education
                                        </Button>
                                    }
                                </>
                            }  */}
              <Button variant="primary" className="onsave-btn">
                Save Patient Education
              </Button>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  // onClick={patEduShowShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Patient Education
                </Button>
              </div>
              <div className="history-body-section">
                {/* <React.Fragment>
                                    <h4 className='history-date'>{moment(historytFetchData[historyData]?.audit.dateCreated).format('DD MMM, YYYY H:mm:ss')}
                                        <span className='edit-medication' onClick={(e) => updateOldData(historytFetchData[historyData]?._id)}>
                                            <i className="bi bi-pencil chief-edit"></i>
                                        </span>
                                    </h4>
                                    <p className='doc-name'>Medical Officer ({historytFetchData[historyData]?.doctor}) </p>
                                    <p className='doc-desc' key={i}>{historytFetchData[historyData].conditions[0].problem} </p>
                                </React.Fragment> */}
              </div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default PatientEducation;
