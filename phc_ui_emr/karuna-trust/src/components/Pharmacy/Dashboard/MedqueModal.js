import React from "react";
import { Modal, Row, Col, Button } from "react-bootstrap";
import "./MedqueModal.css";

function MedqueModal(props) {
  return (
    <Modal
      show={props.medqueModal}
      onHide={props.medqueModalClose}
      className="check-In-modal-div"
    >
      <div className="add-service-form-col">
        <div className="pharma-close">
          <button
            onClick={props.medqueModalClose}
            className="bi bi-x close-popup"
          ></button>
        </div>
        <h1 className="dia-heading pro-head">
          Sugunahalli Primary Health Care Center
        </h1>
        <Row className="pharma-details">
          <Col md={9}>
            <p className="pharma-modal-desc">
              <b>Medical Officer</b>
            </p>
            <p className="pharma-modal-desc">Dr. Archana C</p>
          </Col>
          <Col md={3} align="right">
            <p className="pharma-modal-desc date-details">Date & time</p>
            <p className="pharma-modal-desc date-details">
              15 Apr 2022, 11:45 A.M.
            </p>
          </Col>
        </Row>
        <hr style={{ margin: "0" }} />
        <div className="pat-body">
          <h3 className="pat-off">Patient Details</h3>
          <h3 className="pat-fname">
            <b>
              {/* {PatName} */}
              Mr.Seetharaman Nanjundappa
            </b>
          </h3>
        </div>
        <Row className="prescuhid-detail">
          <Col md={4}>
            <p className="pharma-modal-desc">
              UHID : SUG10011
              {/* {UHID} */}
            </p>
            <p className="pharma-modal-desc">
              Health ID : 88-1234-1234-1234
              {/* {!PatHeaId ? "" : PatHeaId.replace(/(\d{2})(\d{4})(\d{4})(\d{4})/, "$1-$2-$3-$4")} */}
            </p>
          </Col>
          <Col md={2} className="gender-details">
            <p className="pharma-modal-desc">Male</p>
          </Col>
          <Col md={3} className="age-details">
            <p className="pharma-modal-desc">
              52 Yers
              {/* {(a = new Date(), b = (moment(PatDob)), (-b.diff(a, 'years') + " Years"))} */}
            </p>
            <p className="pharma-modal-desc">
              01 Jan 1971
              {/* {moment(PatDob).format('DD MMM YYYY')} */}
            </p>
          </Col>
          <Col md={3} className="mobile-details">
            <p className="pharma-modal-desc">
              +91-98xx6xxx35
              {/* +91-{PatMob} */}
            </p>
          </Col>
        </Row>
        <hr style={{ margin: "0" }} />
        <div className="pharma-details">
          <b>RX</b>
        </div>
        <Row className="pharma-details">
          <Col md={3}>
            <p className="pharma-modal-desc">Tab Minipress XL 10mg</p>
          </Col>
          <Col md={3} align="center">
            <p className="pharma-modal-desc">1-0-1</p>
            <span className="drugs-timing">(1 morning, 1 Evening)</span>
          </Col>
          <Col md={3} align="center">
            <p className="pharma-modal-desc">15 Days</p>
          </Col>
          <Col md={3} align="right">
            <p className="pharma-modal-desc">After Food</p>
          </Col>
        </Row>
        <div className="mo-sign">
          <p>Medical officer signature / Stamp</p>
        </div>
        <hr />
        <Modal.Footer className="prescription-button">
          <Button
            variant="outline-secondary"
            className="prescription-cancel-btn"
            onClick={props.medqueModalClose}
          >
            Cancel
          </Button>
          <Button variant="light" className="prescription-save-btn">
            <i className="bi bi-printer"></i>&nbsp;&nbsp;Print
          </Button>
        </Modal.Footer>
      </div>
    </Modal>
  );
}

export default MedqueModal;
