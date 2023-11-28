import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Modal } from "react-bootstrap";
import moment from "moment";
import "../PrescInvestigation/InvestigationTab.css";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import { useDispatch, useSelector } from "react-redux";
import { loadAllergyDropdown } from "../../../../redux/formUtilityAction";

function Allergy(props) {
  let dispatch = useDispatch();
  const { allergyDropdown } = useSelector((state) => state.formData);

  const UHID = props.vitalDatas?.Patient?.UHId;
  const EncID = props.vitalDatas?.encounterId;
  const PatID = props.vitalDatas?.Patient?.patientId;
  const PatName = props.vitalDatas?.Patient?.name;
  const PatHeaId = props.vitalDatas?.Patient?.healthId;
  const PatGen = props.vitalDatas?.Patient?.gender;
  const PatDob = props.vitalDatas?.Patient?.dob;
  const PatMob = props.vitalDatas?.Patient?.phone;
  const docName = props.vitalDatas?.Provider?.name;
  const PresentTime = new Date();

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // set status for rerender
  const [status, setStatus] = useState(false);
  const [modalStatus, setModalStatus] = useState(false);
  // set status for rerender

  var a = new Date().toDateString();
  var b = moment("YYYY-MM-DD");
  let newDate = new Date().toDateString();
  let date = moment(newDate).format("YYYY-MM-DD");

  // set allergyid for update
  const [allergyDataId, setAllergyDataId] = useState("");
  // function for submit button tpo submit the selected data
  const [Allergydetailswindow, setAllergydetailswindow] = useState(false);
  const AllergydetailClose = () => {
    setAllergydetailswindow(false);
    setAllergyDataId("");
    setAllergyCategory("");
    setAllergen("");
    setReactionType("");
    setReaction("");
    setAllConfirmation("");
    setOnsetDate("");
    setAllergyStatus("");
    setSeverity("");
    setInfoSource("");
    setReacSite("");
    setReliving("");
    setClosureDate("");
  };
  const Add_allergy_details_modal = () => setAllergydetailswindow(true);

  // set allergy id for fetching data
  const [allegyId, setAllegyId] = useState("");
  // set allergy id for fetching data
  const [allergyFetchData, setAllergyFetchData] = useState([]);
  const sortedActivities = allergyFetchData
    .slice()
    .sort((a, b) => (a.date > b.date ? 1 : -1));
  // set state for allergy form
  const [allergyCategory, setAllergyCategory] = useState("");
  const [allergen, setAllergen] = useState("");
  const [reacType, setReactionType] = useState("");
  const [reaction, setReaction] = useState("");
  const [allConfirmation, setAllConfirmation] = useState("");
  const [approximately, setApproximately] = useState(false);
  const [onsetDate, setOnsetDate] = useState("");
  const [dateSince, setDateSince] = useState("");
  const [allergyStatus, setAllergyStatus] = useState("");
  const [severity, setSeverity] = useState("");
  const [infoSource, setInfoSource] = useState("");
  const [day, setDay] = useState("");
  const [month, setMonth] = useState("");
  const [year, setYear] = useState("");
  const [reacSite, setReacSite] = useState("");
  const [reliving, setReliving] = useState("");
  const [remarks, setRemarks] = useState("");
  const [closureDate, setClosureDate] = useState("");
  // set state for allergy form

  //var sinDateIso;
  var onsetDateIso;
  var closureDateIso;
  if (onsetDate != "" && onsetDate != "Invalid date") {
    onsetDateIso = moment(onsetDate).format();
  }
  if (closureDate != "" && closureDate != "Invalid date") {
    closureDateIso = moment(closureDate).format();
  }

  let AllergyData;
  if (closureDate) {
    AllergyData = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      recordedBy: docName,
      doctor: docName,
      category: allergyCategory,
      allergen: allergen,
      reactionType: reacType,
      reaction: reaction,
      confirmationType: allConfirmation,
      approximately: approximately,
      dateSince: dateSince,
      day: day,
      month: month,
      year: year,
      onsetDate: onsetDateIso || "",
      status: allergyStatus,
      severity: severity,
      infoSource: infoSource,
      reactionSite: reacSite,
      relievingFactor: reliving,
      remarks: remarks,
      closureDate: closureDateIso || "",
    };
  } else {
    AllergyData = {
      UHId: UHID,
      encounterId: EncID,
      encounterDate: date,
      patientId: PatID,
      recordedBy: docName,
      doctor: docName,
      category: allergyCategory,
      allergen: allergen,
      reactionType: reacType,
      reaction: reaction,
      confirmationType: allConfirmation,
      approximately: approximately,
      dateSince: dateSince,
      day: day,
      month: month,
      year: year,
      onsetDate: onsetDateIso || "",
      status: allergyStatus,
      severity: severity,
      infoSource: infoSource,
      reactionSite: reacSite,
      relievingFactor: reliving,
      remarks: remarks,
    };
  }
  // // Post Allergy data
  function saveAllergy(event) {
    event.preventDefault();
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(AllergyData),
    };

    fetch(`${constant.ApiUrl}/allergies`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.errors) {
          Tostify.notifyFail("Data not submitted");
        } else {
          try {
            Tostify.notifySuccess("data submitted succesfully");
            setAllegyId(res["_id"]);
            setStatus(true);
            setAllergydetailswindow(false);
            setAllergyCategory("");
            setAllergen("");
            setReactionType("");
            setReaction("");
            setAllConfirmation("");
            setApproximately("");
            setDay("");
            setMonth("");
            setYear("");
            setOnsetDate("");
            setDateSince("");
            setAllergyStatus("");
            setSeverity("");
            setInfoSource("");
            setReacSite("");
            setReliving("");
            setClosureDate("");
            setRemarks("");
            setSuccessShowModal(true);
            setModalStatus(false);
          } catch (err) {
            Tostify.notifyFail("data not submited");
          }
        }
      });
  }
  // Post Allergy data

  // Update allergy data
  function updateAllergy(event) {
    event.preventDefault();

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(AllergyData),
    };

    fetch(`${constant.ApiUrl}/allergies/${allergyDataId}`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        setAllergyDataId("");
        setAllergydetailswindow(false);
        setAllergyCategory("");
        setAllergen("");
        setReactionType("");
        setReaction("");
        setAllConfirmation("");
        setApproximately("");
        setDay("");
        setMonth("");
        setYear("");
        setOnsetDate("");
        setDateSince("");
        setAllergyStatus("");
        setSeverity("");
        setInfoSource("");
        setReacSite("");
        setReliving("");
        setClosureDate("");
        setRemarks("");
        setSuccessShowModal(true);
        setModalStatus(false);
      });
  }
  // Update allergy data

  // fetch allergy data
  useEffect(() => {
    dispatch(loadAllergyDropdown());
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/allergies/filter?page=1&size=&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res.data.length != 0) {
            setAllergyFetchData(res.data);
            setStatus(false);
          }
        });
    }
  }, [EncID, status]);
  // fetch allergy data

  // updarte allergy data
  useEffect(() => {
    updateAllergyData(allergyDataId);
  }, [allergyDataId]);

  // function for update recently save data
  function updateAllergyData(e) {
    setAllergyDataId(e);
    if (allergyDataId != "") {
      setAllergydetailswindow(true);
      fetch(
        `${constant.ApiUrl}/allergies/${allergyDataId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setAllergyCategory(res["category"]);
          setAllergen(res["allergen"]);
          setReactionType(res["reactionType"]);
          setReaction(res["reaction"]);
          setAllConfirmation(res["confirmationType"]);
          const onsetdate = moment(res["onsetDate"]).format("YYYY-MM-DD");
          setOnsetDate(onsetdate);
          setDateSince(res["dateSince"]);
          setAllergyStatus(res["status"]);
          setSeverity(res["severity"]);
          setInfoSource(res["infoSource"]);
          setReacSite(res["reactionSite"]);
          setReliving(res["relievingFactor"]);
          setRemarks(res["remarks"]);
          if (res["approximately"] == true) {
            setApproximately(true);
          } else {
            setApproximately(false);
          }
          setDay(res["day"]);
          setMonth(res["month"]);
          setYear(res["year"]);
          setStatus(false);
        });
    }
  }
  // update allergy data

  // previous history modal
  const [allergyDataShow, setAllergyDataShow] = useState(false);
  const allergyDataClose = () => setAllergyDataShow(false);
  const allergyShow = () => setAllergyDataShow(true);
  // previous history modal

  // Not able to select Future Date for Date Of Birth
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const disableFutureDt = (current) => {
    return current.isBefore(currentdate);
  };
  // Not able to select Future Date for Date Of Birth

  const handleOnChangeOnset = (event) => {
    if (
      !moment(event.target.max).isSameOrAfter(
        moment(event.target.value).format("YYYY-MM-DD")
      )
    ) {
      setOnsetDate("");
      Tostify.notifyWarning(
        "You are not Suppose to Enter Future Date in Onset...!"
      );
    } else {
      setOnsetDate(event.target.value);
    }
  };

  const handleOnChangeClosure = (event) => {
    if (
      !moment(event.target.max).isSameOrAfter(
        moment(event.target.value).format("YYYY-MM-DD")
      )
    ) {
      setClosureDate("");
      Tostify.notifyWarning(
        "You are not Suppose to Enter Future Date in Closure Date...!"
      );
    } else {
      setClosureDate(event.target.value);
    }
  };

  {
    /* Add Allergy Modal */
  }
  const [showSuccessModal, setSuccessShowModal] = useState(false);
  const closeSuccessModal = () => {
    setSuccessShowModal(false);
  };
  {
    /* Add Allergy Modal */
  }

  return (
    <React.Fragment>
      {/* Modal for allergy form */}
      <Modal
        show={Allergydetailswindow}
        onHide={AllergydetailClose}
        className="vital-modal-div"
      >
        <ToastContainer />
        <div className="vital-modal-div">
          <h5 className="vital-header">
            Allergy Details
            <i
              className="fa fa-close close-btn-style"
              onClick={AllergydetailClose}
            ></i>
          </h5>
          <p className="form-note pt-3">
            Add Allergy Related Information Of The Patient
          </p>
        </div>
        <Form>
          <Row className="allergy-information-div">
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Category</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    value={allergyCategory}
                    onChange={(event) => setAllergyCategory(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Category
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Category" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Allergen</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Allergen Here"
                    value={allergen}
                    onChange={(event) => setAllergen(event.target.value)}
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Reaction Type</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    placeholder="Choose a workshop"
                    value={reacType}
                    onChange={(event) => setReactionType(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Reaction Type
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Reaction Type" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Reaction</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Reaction Here"
                    value={reaction}
                    onChange={(event) => setReaction(event.target.value)}
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <Row className="allergy-information-div mt-3">
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Confirmation</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    value={allConfirmation}
                    onChange={(event) => setAllConfirmation(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Confirmation
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Confirmation" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div className="allergy-check">
                <input
                  name="stat"
                  type="checkbox"
                  value={approximately}
                  checked={approximately}
                  onChange={() => {
                    setApproximately(!approximately);
                  }}
                />
                &nbsp; Approximately
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Since </Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    disabled={!approximately}
                    value={dateSince}
                    onChange={(event) => setDateSince(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Since" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <Row>
                <Col md={3}>
                  <div>
                    <Form.Group>
                      <Form.Label> Day </Form.Label>
                      <Form.Control
                        type="number"
                        placeholder="Enter"
                        disabled={!approximately}
                        value={day}
                        onChange={(event) => setDay(event.target.value)}
                      />
                    </Form.Group>
                  </div>
                </Col>
                <Col md={1}></Col>
                <Col md={3}>
                  <div>
                    <Form.Group>
                      <Form.Label> Month </Form.Label>
                      <Form.Control
                        type="number"
                        placeholder="Enter"
                        disabled={!approximately}
                        value={month}
                        onChange={(event) => setMonth(event.target.value)}
                      />
                    </Form.Group>
                  </div>
                </Col>
                <Col md={1}></Col>
                <Col md={3}>
                  <div>
                    <Form.Group>
                      <Form.Label> Year </Form.Label>
                      <Form.Control
                        type="number"
                        placeholder="Enter"
                        disabled={!approximately}
                        value={year}
                        onChange={(event) => setYear(event.target.value)}
                      />
                    </Form.Group>
                  </div>
                </Col>
                <Col md={1}></Col>
              </Row>
            </Col>
          </Row>
          <Row className="allergy-information-div mt-3">
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Status</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    value={allergyStatus}
                    onChange={(event) => setAllergyStatus(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Status
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Status" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Severity</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    value={severity}
                    onChange={(event) => setSeverity(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Severity
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Severity" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Date Of Onset (Approx.)</Form.Label>
                  <Form.Control
                    type="date"
                    value={onsetDate}
                    max={moment(new Date()).format("YYYY-MM-DD")}
                    onChange={(event) => handleOnChangeOnset(event)}
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <Row className="allergy-information-div mt-3">
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Source Of Information</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    value={infoSource}
                    onChange={(event) => setInfoSource(event.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Source Of Information
                    </option>
                    {allergyDropdown.map((formItem, i) => (
                      <React.Fragment key={i}>
                        {formItem.groupName == "Source of Information" && (
                          <>
                            {formItem.elements.map((drpItem, drpIndex) => (
                              <option value={drpItem.title} key={drpIndex}>
                                {drpItem.title}
                              </option>
                            ))}
                          </>
                        )}
                      </React.Fragment>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Site Of Reaction</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Site of Reaction Here"
                    value={reacSite}
                    onChange={(event) => setReacSite(event.target.value)}
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Reliving Factor</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Reliving Factor Here"
                    value={reliving}
                    onChange={(event) => setReliving(event.target.value)}
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3}>
              <div>
                <Form.Group>
                  <Form.Label>Date Of Closure (Approx.)</Form.Label>
                  <Form.Control
                    type="date"
                    value={closureDate}
                    max={moment(new Date()).format("YYYY-MM-DD")}
                    onChange={(event) => handleOnChangeClosure(event)}
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
          <div className="allergy-information-div mt-3 mb-2">
            <Form.Group>
              <Form.Label>Remarks</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Remarks Here"
                value={remarks}
                onChange={(event) => setRemarks(event.target.value)}
              />
            </Form.Group>
          </div>
          <div className="vitalsubmit align-me2">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={AllergydetailClose}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              {allergyDataId ? (
                <SaveButton
                  butttonClick={updateAllergy}
                  class_name="regBtnN"
                  button_name="Update"
                />
              ) : (
                <SaveButton
                  butttonClick={saveAllergy}
                  class_name="regBtnN"
                  button_name="Save"
                />
              )}
            </div>
          </div>
        </Form>
      </Modal>
      {/* Modal for allergy form */}
      <div className="form-col">
        <div className="cheif-complaint-form">
          <Row>
            <Col md={6}>
              <h5 className="vital-header">Allergy</h5>
            </Col>
            <Col md={6}>
              <Button
                varient="light"
                className="float-right view-prev-details"
                onClick={allergyShow}
              >
                <i className="fa fa-undo prev-icon"></i> Previous Allergy
              </Button>
            </Col>
          </Row>
          <div>
            {sortedActivities ? (
              <>
                {sortedActivities.map((item, i) => (
                  <div className="allergy-data-div" key={i}>
                    <Row className="allergy-row">
                      <Col md={2}>
                        <p>Allergen</p>
                        <br />
                        <p>
                          <b>{item.allergen}</b>
                        </p>
                      </Col>
                      <Col md={2}>
                        <p>Category</p>
                        <br />
                        <p>
                          <b>{item.category}</b>
                        </p>
                      </Col>
                      <Col md={2}>
                        <p>Reaction Type</p>
                        <br />
                        <p>
                          <b>{item.reactionType}</b>
                        </p>
                      </Col>
                      <Col md={3}>
                        {item.dateSince && (
                          <>
                            <p>Since</p>
                            <br />
                            <p>
                              <b>{item.dateSince}</b>
                            </p>
                          </>
                        )}
                      </Col>
                      <Col md={1}>
                        {item.day && (
                          <>
                            <p>Day</p>
                            <br />
                            <p>
                              <b>{item.day}</b>
                            </p>
                          </>
                        )}
                      </Col>
                      <Col md={1}>
                        {item.month && (
                          <>
                            <p>Month</p>
                            <br />
                            <p>
                              <b>{item.month}</b>
                            </p>
                          </>
                        )}
                      </Col>
                      <Col md={1}>
                        {item.year && (
                          <>
                            <p>Year</p>
                            <br />
                            <p>
                              <b>{item.year}</b>
                            </p>
                          </>
                        )}
                      </Col>
                    </Row>
                    <hr />
                    <Row className="allergy-row">
                      <Col md={6}>
                        <p>Reaction</p>
                        <br />
                        <p>
                          <b>{item.reaction}</b>
                        </p>
                      </Col>
                      <Col md={3}>
                        <p>Confirmation</p>
                        <br />
                        <p>
                          <b>{item.confirmationType}</b>
                        </p>
                      </Col>
                      <Col md={3}>
                        <p>Date Of Onset (Approx.)</p>
                        <br />
                        <p>
                          <b>{moment(item.onsetDate).format("DD-MMM-YYYY")}</b>
                        </p>
                      </Col>
                    </Row>
                    <hr />
                    <Row className="allergy-row">
                      <Col md={3}>
                        <p>Status</p>
                        <br />
                        <p>
                          <b>{item.status}</b>
                        </p>
                      </Col>
                      <Col md={3}>
                        <p>Severity</p>
                        <br />
                        <p>
                          <b>{item.severity}</b>
                        </p>
                      </Col>
                      <Col md={6}>
                        <p>Site Of Reaction</p>
                        <br />
                        <p>
                          <b>{item.reactionSite}</b>
                        </p>
                      </Col>
                    </Row>
                    <hr />
                    <Row className="allergy-row">
                      <Col md={3}>
                        <p>Source Of Information</p>
                        <br />
                        <p>
                          <b>{item.infoSource}</b>
                        </p>
                      </Col>
                      <Col md={6}>
                        {item.relievingFactor && (
                          <>
                            <p>Relieving Factor</p>
                            <br />
                            <p>
                              <b>{item.relievingFactor}</b>
                            </p>
                          </>
                        )}
                      </Col>
                      <Col md={3}>
                        {item.closureDate && (
                          <>
                            <p>Date Of Closure (Approx.)</p>
                            <br />
                            <p>
                              <b>
                                {moment(item.closureDate).format("DD-MMM-YYYY")}
                              </b>
                            </p>
                          </>
                        )}
                      </Col>
                    </Row>
                    <hr />
                    {item.remarks && (
                      <div className="allergy-row1">
                        <p>Remarks</p>
                        <br />
                        <p>
                          <b>{item.remarks}</b>
                        </p>
                      </div>
                    )}
                    <hr />
                    <div className="allergy-btn">
                      <div className="delete-icon-div">
                        {/* <div className='delete-medication' align="center" >
                          <i className="bi bi-trash delete-icon"></i>
                          <p>Delete</p>
                        </div> */}
                        <div
                          className="edit-medication"
                          align="center"
                          onClick={(e) => updateAllergyData(item._id)}
                        >
                          <i className="bi bi-pencil edit-icon"></i>
                          <p>Edit</p>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </>
            ) : (
              ""
            )}
            <div className="d-flex add-allergy-btn">
              <Button
                onClick={Add_allergy_details_modal}
                className="allergy-details-tab"
              >
                Add Allergy Details
              </Button>
            </div>
          </div>
        </div>
      </div>
      <ViewModalPopups
        chiefClose={allergyDataClose}
        cheifShow={allergyDataShow}
        PatName={PatName}
        allergyUHId={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        allergyEncounterId={EncID}
        dateto={dateto}
        datefrom={datefrom}
        modType="allergy"
      />
    </React.Fragment>
  );
}

export default Allergy;
