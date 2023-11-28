import React from "react";
import { Image, Accordion, Row, Col, Button, Table } from "react-bootstrap";
import moment from "moment";
import InvestigationReport from "./InvestigationReport";
import PreviousInvestTable from "./PreviousInvestTable";

export default function ThreeMonths(props) {
  let prevDataArray = props.prevDataArray;
  let modalType = props.modalType;
  let dataLength = props.prevDataArray?.length;

  const { datefrom, dateto } = props;

  return (
    <React.Fragment>
      {/* Chief Complaint Here */}
      {modalType == "Chief" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((chiefData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(chiefData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({chiefData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <p className="doc-desc1">
                                {chiefData.complaintText}
                              </p>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Chief Complaint Here */}

      {/* History Of Illness Here */}
      {modalType == "Illness" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((historyData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(historyData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({historyData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              {historyData.conditions.map((item, index) => (
                                <p className="doc-desc1" key={index}>
                                  {item.problem}{" "}
                                </p>
                              ))}
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* History Of Illness Here */}

      {/* Vital Sign Here */}
      {modalType == "vitals" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray?.length != 0 &&
                  prevDataArray.map((vitalSignData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(vitalSignData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({vitalSignData.audit?.createdBy}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <Row>
                                {vitalSignData?.medicalSigns?.map(
                                  (item2, j) => (
                                    <Col md={6} key={j}>
                                      <div className="accordion-body-section">
                                        <p>
                                          {item2.signName} :{" "}
                                          <b>{item2.signValue}</b>
                                        </p>
                                      </div>
                                    </Col>
                                  )
                                )}
                              </Row>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Vital Sign Here */}

      {/* Physical Examination Here */}
      {modalType == "physical" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray?.map((physicalData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(physicalData.audit?.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({physicalData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body className="phymodal-acc-body">
                            <div className="accordion-body-section">
                              <Row>
                                {physicalData.assessments.map((assess, i) => (
                                  <React.Fragment key={i}>
                                    {assess.assessmentTitle == "" ? (
                                      ""
                                    ) : (
                                      <Col
                                        md={6}
                                        className="assessment-col"
                                        key={i}
                                      >
                                        {/* Cardio vascular assessment form */}
                                        <div
                                          id="cardio-form"
                                          className="assessment-col-form"
                                        >
                                          <form className="">
                                            <div className="assessment-header modal-header">
                                              {/* <img src="../img/PA-SVG/heart.svg" alt="heart" /> */}
                                              <h3>
                                                &nbsp;{assess.assessmentTitle}{" "}
                                              </h3>
                                            </div>
                                            <div className="col-container assessment-body">
                                              {assess.observations.map(
                                                (observ, i) => (
                                                  <React.Fragment key={i}>
                                                    {observ.observationValues !=
                                                      "" && (
                                                      <div
                                                        className="assessment-button-section sub-modal-header"
                                                        key={i}
                                                      >
                                                        <h3>
                                                          {
                                                            observ.observationName
                                                          }
                                                        </h3>
                                                        <div className="assessment-buttons">
                                                          <div>
                                                            {observ?.observationValues.map(
                                                              (items, i) => (
                                                                <React.Fragment
                                                                  key={i}
                                                                >
                                                                  {items && (
                                                                    <Button
                                                                      variant="outline-secondary"
                                                                      className="physical-modal-buttons"
                                                                    >
                                                                      {items}
                                                                    </Button>
                                                                  )}
                                                                </React.Fragment>
                                                              )
                                                            )}
                                                          </div>
                                                        </div>
                                                      </div>
                                                    )}
                                                  </React.Fragment>
                                                )
                                              )}
                                            </div>
                                          </form>
                                        </div>
                                      </Col>
                                    )}
                                  </React.Fragment>
                                ))}
                              </Row>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Physical Examination Here */}

      {/* Provisional Diagnosis Here */}
      {modalType == "provisional" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((provisionalData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(provisionalData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({provisionalData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <div className="accordion-body-section">
                                <p>
                                  {
                                    provisionalData.assessments[0]
                                      .observations[0].observationValues
                                  }
                                </p>
                              </div>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Provisional Diagnosis Here */}

      {/* Medical History Here */}
      {modalType == "medical" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((medicalData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(medicalData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({medicalData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <p>{medicalData.conditions[0].problem}</p>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Medical History Here */}

      {/* Surgical History Here */}
      {modalType == "surgical" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((surgicalData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(surgicalData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({surgicalData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <p>{surgicalData.conditions[0].problem}</p>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Surgical History Here */}

      {/* Family History Here */}
      {modalType == "family" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((familyData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(familyData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({familyData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <p>{familyData.conditions[0].problem}</p>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Family History Here */}

      {/* Social History Here */}
      {modalType == "social" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((socialData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(socialData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({socialData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <p>{socialData.conditions[0].problem}</p>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Social History Here */}

      {/* Prescription Here */}
      {modalType == "prescription" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((prescriptionData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(
                                prescriptionData.audit.dateCreated
                              ).format("DD MMM YYYY, hh:mm A")}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({prescriptionData.prescribedBy}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <Table className="presc-modal-table">
                                <thead>
                                  <tr>
                                    <th className="presc-thead drug-details">
                                      Drugs Name
                                    </th>
                                    <th className="presc-thead drug-align">
                                      Dose
                                    </th>
                                    <th className="presc-thead drug-align">
                                      Route
                                    </th>
                                    <th className="presc-thead drug-align">
                                      Frequency
                                    </th>
                                    <th className="presc-thead drug-align">
                                      No. of days
                                    </th>
                                    <th className="presc-thead drug-instruction">
                                      Instructions
                                    </th>
                                  </tr>
                                </thead>
                                {prescriptionData.products.map((item, j) => (
                                  <tbody key={j}>
                                    <tr>
                                      <td className="presc-tbody drug-details drug-name">
                                        {item.name}
                                      </td>
                                      <td className="presc-tbody drug-align">
                                        {item.strength}
                                      </td>
                                      <td className="presc-tbody drug-align">
                                        {item.route}
                                      </td>
                                      <td className="presc-tbody drug-align">
                                        {item.frequency}
                                      </td>
                                      <td className="presc-tbody drug-align">
                                        {item.duration} Days
                                      </td>
                                      <td className="presc-tbody drug-instruction">
                                        {item.instruction}
                                      </td>
                                    </tr>
                                  </tbody>
                                ))}
                              </Table>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Prescription Here */}

      {/* Investigation Here */}
      {modalType == "investigation" && (
        <div className="modal-accordian">
          {/* Investigation */}
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {props.pageChange == true ? (
                  <InvestigationReport setPageChange={props.setPageChange} />
                ) : (
                  <PreviousInvestTable
                    setPageChange={props.setPageChange}
                    datefrom={datefrom}
                    dateto={dateto}
                    prevDataArray={prevDataArray}
                  />
                )}
              </React.Fragment>
            )}
          </div>
          {/* Investigation */}
        </div>
      )}
      {/* Investigation Here */}

      {/* Allergy Here */}
      {modalType == "allergy" && (
        <div className="modal-accordian">
          <div className="accordion-div">
            {dataLength == 0 ? (
              <React.Fragment>
                <Row>
                  <Col md={6}>
                    <div className="pat-message">
                      <Row>
                        <Col md={8}>
                          <Image
                            src="../img/default/icon-physical-examination.svg"
                            className="rounded mx-auto d-block pat-data"
                          />
                        </Col>
                      </Row>
                      No History Available for Past 3 Months
                    </div>
                  </Col>
                  <Col md={6}></Col>
                </Row>
              </React.Fragment>
            ) : (
              <React.Fragment>
                {prevDataArray &&
                  prevDataArray.map((allergyData, i) => (
                    <React.Fragment key={i}>
                      <Accordion defaultActiveKey={0} alwaysOpen>
                        <Accordion.Item eventKey={i}>
                          <Accordion.Header className="vital-acc-head dur-head">
                            <i className="bi bi-caret-down-fill down-mark duration"></i>
                            <h4 className="history-date dur-date">
                              {moment(allergyData.audit.dateCreated).format(
                                "DD MMM YYYY, hh:mm A"
                              )}
                            </h4>
                            <p className="doc-name1">
                              Medical Officer ({allergyData.doctor}){" "}
                            </p>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="accordion-body-section">
                              <div className="allergy-data-div">
                                <Row className="allergy-row">
                                  <Col md={2}>
                                    <p>Allergen</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.allergen}</b>
                                    </p>
                                  </Col>
                                  <Col md={2}>
                                    <p>Category</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.category}</b>
                                    </p>
                                  </Col>
                                  <Col md={2}>
                                    <p>Reaction type</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.reactionType}</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p>Since</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.dateSince).format(
                                          "DD-MMM-YYYY"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                  <Col md={1}>
                                    <p>Day</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.dateSince).format(
                                          "DD"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                  <Col md={1}>
                                    <p>Month</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.dateSince).format(
                                          "MMM"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                  <Col md={1}>
                                    <p>Year</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.dateSince).format(
                                          "YYYY"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                </Row>
                                <hr />
                                <Row className="allergy-row">
                                  <Col md={6}>
                                    <p>Reaction</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.reaction}</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p>Confirmation</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.confirmationType}</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p>Date of onset (approx.)</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.onsetDate).format(
                                          "DD-MMM-YYYY"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                </Row>
                                <hr />
                                <Row className="allergy-row">
                                  <Col md={3}>
                                    <p>Status</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.status}</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p>Severity</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.severity}</b>
                                    </p>
                                  </Col>
                                  <Col md={6}>
                                    <p>Site of reaction</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.reactionSite}</b>
                                    </p>
                                  </Col>
                                </Row>
                                <hr />
                                <Row className="allergy-row">
                                  <Col md={3}>
                                    <p>Source of information</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.infoSource}</b>
                                    </p>
                                  </Col>
                                  <Col md={6}>
                                    <p>Reliving factor</p>
                                    <br />
                                    <p>
                                      <b>{allergyData.relievingFactor}</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p>Date of closure (approx.)</p>
                                    <br />
                                    <p>
                                      <b>
                                        {moment(allergyData.closureDate).format(
                                          "DD-MMM-YYYY"
                                        )}
                                      </b>
                                    </p>
                                  </Col>
                                </Row>
                              </div>
                            </div>
                          </Accordion.Body>
                        </Accordion.Item>
                      </Accordion>
                    </React.Fragment>
                  ))}
              </React.Fragment>
            )}
          </div>
        </div>
      )}
      {/* Allergy Here */}
    </React.Fragment>
  );
}
