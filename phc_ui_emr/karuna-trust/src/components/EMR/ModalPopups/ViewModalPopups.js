import React, { useState, useEffect } from "react";
import { Col, Row, Modal } from "react-bootstrap";
import OneWeek from "../PrescribedDays/OneWeek";
import OneMonth from "../PrescribedDays/OneMonth";
import All from "../PrescribedDays/All";
import FortNight from "../PrescribedDays/Fortnight";
import ThreeMonths from "../PrescribedDays/ThreeMonths";
import SixMonths from "../PrescribedDays/SixMonths";
import OneYear from "../PrescribedDays/OneYear";
import moment from "moment";
import { loadPrevDataMO } from "../../../redux/actions";
import { useDispatch, useSelector } from "react-redux";
import PageLoader from "../../PageLoader";
import PatientRow from "../../PatientRow/PatientRow";

export default function ViewModalPopups(props) {
  //page change for Investigation
  let dispatch = useDispatch();
  const { prevDataArray } = useSelector((state) => state.data);
  const [pageChange, setPageChange] = useState(false);
  var a = new Date();
  var b = moment("YYYY-DD-MM");

  let patUHID;
  let prevHeader = "";
  let modalType = "";
  if (props.modType == "chief") {
    patUHID = props.chiefUHID;
    prevHeader = "Chief Complaint";
    modalType = "Chief";
  } else if (props.modType == "illness") {
    patUHID = props.historyUHID;
    prevHeader = "History Of Illness";
    modalType = "Illness";
  } else if (props.modType == "vital") {
    patUHID = props.vitalsignUHID;
    prevHeader = "Vital Signs";
    modalType = "vitals";
  } else if (props.modType == "physical") {
    patUHID = props.physicalUHID;
    prevHeader = "Physical Examination";
    modalType = "physical";
  } else if (props.modType == "provisional") {
    patUHID = props.provisionalUHID;
    prevHeader = "Provisional Diagnosis";
    modalType = "provisional";
  } else if (props.modType == "medical") {
    patUHID = props.medicalUHID;
    prevHeader = "Medical History";
    modalType = "medical";
  } else if (props.modType == "surgical") {
    patUHID = props.surgicalUHID;
    prevHeader = "Surgical History";
    modalType = "surgical";
  } else if (props.modType == "family") {
    patUHID = props.familyUHID;
    prevHeader = "Family History";
    modalType = "family";
  } else if (props.modType == "social") {
    patUHID = props.socialUHID;
    prevHeader = "Social History";
    modalType = "social";
  } else if (props.modType == "prescription") {
    patUHID = props.prescriptionUHID;
    prevHeader = "Prescription Data";
    modalType = "prescription";
  } else if (props.modType == "investigation") {
    patUHID = props.investigationUHID;
    prevHeader = "Investigaton Data";
    modalType = "investigation";
  } else if (props.modType == "allergy") {
    patUHID = props.allergyUHId;
    prevHeader = "Allergy Data";
    modalType = "allergy";
  }

  const { datefrom, dateto } = props;
  const datefrom15 = moment(Date.now() - 15 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const datefrom30 = moment(Date.now() - 30 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const datefrom90 = moment(Date.now() - 90 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const datefrom180 = moment(Date.now() - 180 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const datefrom365 = moment(Date.now() - 365 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );
  const datefromall = moment(Date.now() - 3650 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  // fetching previous datas for perticular patient
  // const [pageLoader, setPageLoader] = useState(true)
  useEffect(() => {
    if (patUHID) {
      dispatch(loadPrevDataMO(patUHID, modalType));
    }
  }, [modalType, patUHID]);

  const [sortedDataOneWeek, setSortedDataOneWeek] = useState();
  const [sortedDataFortNight, setSortedDataFortNight] = useState();
  const [sortedDataOneMonth, setSortedDataOneMonth] = useState();
  const [sortedDataThreeMonth, setSortedDataThreeMonth] = useState();
  const [sortedDataSixMonth, setSortedDataSixMonth] = useState();
  const [sortedDataOneYear, setSortedDataOneYear] = useState();
  const [sortedDataAll, setSortedDataAll] = useState();

  useEffect(() => {
    let forOneWeek = [];
    let forFortNight = [];
    let forOneMonth = [];
    let forThreeMonth = [];
    let forSixMonth = [];
    let forOneYear = [];
    let forAll = [];
    if (prevDataArray.length != 0) {
      prevDataArray.map((data) => {
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom, dateto)
        ) {
          forOneWeek.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom15, dateto)
        ) {
          forFortNight.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom30, dateto)
        ) {
          forOneMonth.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom90, dateto)
        ) {
          forThreeMonth.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom180, dateto)
        ) {
          forSixMonth.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefrom365, dateto)
        ) {
          forOneYear.push(data);
        }
        if (
          moment(
            moment(data.audit.dateCreated).format("DD MMM YYYY")
          ).isBetween(datefromall, dateto)
        ) {
          forAll.push(data);
        }
      });
    }

    setSortedDataOneWeek(
      forOneWeek.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataFortNight(
      forFortNight.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataOneMonth(
      forOneMonth.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataThreeMonth(
      forThreeMonth.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataSixMonth(
      forSixMonth.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataOneYear(
      forOneYear.slice().sort((a, b) => (a.date > b.date ? 1 : -1))
    );
    setSortedDataAll(forAll.slice().sort((a, b) => (a.date > b.date ? 1 : -1)));
  }, [prevDataArray]);
  // fetching previous datas for perticular patient

  let newDate = new Date();
  let thisyear = moment(newDate).format("YYYY");

  return (
    <React.Fragment>
      <Modal
        show={props.cheifShow}
        onHide={props.chiefClose}
        className="check-In-modal-div history-data"
      >
        <Row onClick={props.chiefClose}>
          <Col md={12} align="right">
            <button className="bi bi-x close-popup"></button>
          </Col>
        </Row>
        <Row>
          <Col md={1}></Col>
          <Col md={10}>
            <PatientRow
              healthId={props.PatHeaId}
              fullName={props.PatName}
              UHId={patUHID}
              gender={props.PatGen}
              age={thisyear - moment(new Date(props.PatDob)).format("YYYY")}
              dateOfBirth={moment(props.PatDob).format("DD MMM YYYY")}
              mobile={props.PatMob}
              page="view-modal"
            />
          </Col>
          <Col md={1}></Col>
        </Row>
        <div className="modal-desc">
          {pageChange == false && (
            <h3 className="presc">Previous&nbsp;{prevHeader}</h3>
          )}
          <React.Fragment>
            <div className="pat-tabs">
              <div className="tabbable-line">
                <div>
                  {pageChange == false && (
                    <ul className="nav nav-tabs nav-tabs-header presc-tab-head">
                      <li className="active">
                        <a
                          href="#tab_presc_1"
                          data-toggle="tab"
                          className="active"
                        >
                          1W
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_2" data-toggle="tab">
                          15D
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_3" data-toggle="tab">
                          1M
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_4" data-toggle="tab">
                          3M
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_5" data-toggle="tab">
                          6M
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_6" data-toggle="tab">
                          1Y
                        </a>
                      </li>
                      <li>
                        <a href="#tab_presc_7" data-toggle="tab">
                          All
                        </a>
                      </li>
                    </ul>
                  )}
                  <div className="tab-content bigDevice">
                    <div className="tab-pane active" id="tab_presc_1">
                      <OneWeek
                        modalType={modalType}
                        prevDataArray={sortedDataOneWeek}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_2">
                      <FortNight
                        modalType={modalType}
                        prevDataArray={sortedDataFortNight}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_3">
                      <OneMonth
                        modalType={modalType}
                        prevDataArray={sortedDataOneMonth}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_4">
                      <ThreeMonths
                        modalType={modalType}
                        prevDataArray={sortedDataThreeMonth}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_5">
                      <SixMonths
                        modalType={modalType}
                        prevDataArray={sortedDataSixMonth}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_6">
                      <OneYear
                        modalType={modalType}
                        prevDataArray={sortedDataOneYear}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                    <div className="tab-pane" id="tab_presc_7">
                      <All
                        modalType={modalType}
                        prevDataArray={sortedDataAll}
                        pageChange={pageChange}
                        setPageChange={setPageChange}
                      />
                    </div>
                  </div>
                </div>

                {pageChange == false && (
                  <div id="contentpre" className="tab-content" role="tablist">
                    <div
                      id="pane-1W"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-1W"
                    >
                      <div className="card-header" role="tab" id="heading-1W">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_11"
                            aria-expanded="true"
                            aria-controls="tab_presc_11"
                            className="vitals-accordion"
                          >
                            1W
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_11"
                        className="collapse show"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-1W"
                      >
                        <div className="card-body new-card-body">
                          <OneWeek
                            modalType={modalType}
                            prevDataArray={sortedDataOneWeek}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_21"
                            aria-expanded="true"
                            aria-controls="tab_presc_21"
                            className="vitals-accordion"
                          >
                            15D
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_21"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <FortNight
                            modalType={modalType}
                            prevDataArray={sortedDataFortNight}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_31"
                            aria-expanded="true"
                            aria-controls="tab_presc_31"
                            className="vitals-accordion"
                          >
                            1M
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_31"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <OneMonth
                            modalType={modalType}
                            prevDataArray={sortedDataOneMonth}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_41"
                            aria-expanded="true"
                            aria-controls="tab_presc_41"
                            className="vitals-accordion"
                          >
                            3M
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_41"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <ThreeMonths
                            modalType={modalType}
                            prevDataArray={sortedDataThreeMonth}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_51"
                            aria-expanded="true"
                            aria-controls="tab_presc_51"
                            className="vitals-accordion"
                          >
                            6M
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_51"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <SixMonths
                            modalType={modalType}
                            prevDataArray={sortedDataSixMonth}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_61"
                            aria-expanded="true"
                            aria-controls="tab_presc_61"
                            className="vitals-accordion"
                          >
                            1Y
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_61"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <OneYear
                            modalType={modalType}
                            prevDataArray={sortedDataOneYear}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                    <div
                      id="pane-15D"
                      className="card tab-pane fade show active"
                      role="tabpanelDays"
                      aria-labelledby="tab-15D"
                    >
                      <div className="card-header" role="tab" id="heading-15D">
                        <h5 className="mb-0">
                          <a
                            data-toggle="collapse"
                            href="#tab_presc_71"
                            aria-expanded="true"
                            aria-controls="tab_presc_71"
                            className="vitals-accordion"
                          >
                            All
                          </a>
                        </h5>
                      </div>
                      <div
                        id="tab_presc_71"
                        className="collapse"
                        data-parent="#contentpre"
                        role="tabpanelDays"
                        aria-labelledby="heading-15D"
                      >
                        <div className="card-body">
                          <All
                            modalType={modalType}
                            prevDataArray={sortedDataAll}
                            pageChange={pageChange}
                            setPageChange={setPageChange}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </React.Fragment>
        </div>
      </Modal>
    </React.Fragment>
  );
}
