import React from "react";
import { Col, Row, Form, Button, Modal, Table } from "react-bootstrap";
import moment from "moment";
import SaveButton from "../../EMR_Buttons/SaveButton";

function PrescriptionModal(props) {
  var a = new Date();
  var b = moment("YYYY-DD-MM");

  return (
    <div>
      <Modal
        show={props.descshow}
        onHide={props.descClose}
        className="check-In-modal-div"
      >
        <Row>
          <Col md={6}>
            <h1 className="presc-details-header">{props.phc}</h1>
          </Col>
          <Col md={6} align="right">
            <button
              onClick={props.descClose}
              className="bi bi-x close-popup"
            ></button>
          </Col>
        </Row>
        <div className="presc-body">
          <Row className="presc-detail">
            <Col md={9}>
              <h3 className="med-off">Medical Officer</h3>
              <p>{props.docName}</p>
            </Col>
            <Col md={3} className="text-left pre-date">
              <p className="presc-date">Date & Time</p>
              <p>
                {moment(props.EffectiveDate).format("DD MMM YYYY, hh:mm A")}
              </p>
            </Col>
          </Row>
          <hr />
          <div className="pat-body">
            <h3 className="pat-off">Patient Details</h3>
            <h3 className="pat-fname">{props.PatName}</h3>
          </div>
          <Row className="prescuhid-detail">
            <Col md={4}>
              <p>UHID : {props.UHID}</p>
              <p>
                Health ID :{" "}
                {!props.PatHeaId
                  ? ""
                  : props.PatHeaId.replace(
                      /(\d{2})(\d{4})(\d{4})(\d{4})/,
                      "$1-$2-$3-$4"
                    )}
              </p>
            </Col>
            <Col md={2} className="gender-details">
              <p>Male</p>
            </Col>
            <Col md={3} className="age-details">
              <p>
                {
                  ((a = new Date()),
                  (b = moment(props.PatDob)),
                  -b.diff(a, "years") + " Years")
                }
              </p>
              <p>{moment(props.PatDob).format("DD MMM YYYY")}</p>
            </Col>
            <Col md={3} className="mobile-details">
              {props.PatMob && <p>+91-{props.PatMob}</p>}
            </Col>
          </Row>
          <hr />
          <div className="prescription-details">
            <h3 className="recommended-details">Medication advice</h3>
            <div>
              {props.prescGetData?.map((presc, i) => (
                <Table key={i}>
                  <thead>
                    <tr>
                      <th className="presc-thead drug-details">Drugs Name</th>
                      <th className="presc-thead drug-align">Dose</th>
                      <th className="presc-thead drug-align">Route</th>
                      <th className="presc-thead drug-align">Frequency</th>
                      <th className="presc-thead drug-align">No. of days</th>
                      <th className="presc-thead">Instructions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {presc.products.map((item, j) => (
                      <tr key={j}>
                        <td className="presc-tbody drug-details">
                          {item.name}
                        </td>
                        <td className="presc-tbody drug-align">
                          {item.strength}
                        </td>
                        <td className="presc-tbody drug-align">{item.route}</td>
                        <td className="presc-tbody drug-align">
                          {String(item.frequency)}
                        </td>
                        <td className="presc-tbody drug-align">
                          {item.duration} Days
                        </td>
                        <td className="presc-tbody drug-align">
                          {item.instruction}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ))}
            </div>
          </div>
          <div className="mo-sign">
            <p>Medical officer signature / Stamp</p>
          </div>
          <hr />
          <Modal.Footer className="prescription-button">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={props.descClose}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              <SaveButton class_name="regBtnN" button_name="Print" />
            </div>
          </Modal.Footer>
        </div>
      </Modal>
    </div>
  );
}

export default PrescriptionModal;
