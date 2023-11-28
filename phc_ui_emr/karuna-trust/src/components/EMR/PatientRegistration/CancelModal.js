import React from "react";
import { Row, Col, Button, Modal, Container, Form } from "react-bootstrap";
import "../../../css/RegForms.css";
import SaveButton from "../../EMR_Buttons/SaveButton";

function CancelModal(props) {
  return (
    <div>
      {/* cancel button model box */}
      <Modal show={props.isShow} onHide={props.handleClose}>
        <Container>
          <Row>
            <Col>
              {props.searchHealthId || props.citizenPatientId_id ? (
                <h4 className="logout-head">Cancel Updating Registration ?</h4>
              ) : (
                <h4 className="logout-head">Cancel Registration ?</h4>
              )}
            </Col>
          </Row>
          <Row>
            <Col>
              {props.searchHealthId || props.citizenPatientId_id ? (
                <p className="log-text">
                  Are you sure, you want to cancel update patient registration?
                  <br />
                  All the data will be lost.
                </p>
              ) : (
                <p className="log-text">
                  Are you sure, you want to cancel patient registration?
                  <br />
                  All the data will be lost.
                </p>
              )}
            </Col>
          </Row>
          <Row>
            <Col className="btn1">
              <div className="save-btn-section">
                <SaveButton
                  butttonClick={props.handleClose}
                  class_name="regBtnN"
                  button_name="No, Continue"
                />
              </div>
            </Col>
            <Col className="btn2">
              <div className="save-btn-section">
                <SaveButton
                  butttonClick={props.cancelEditReg}
                  class_name="regBtnPC"
                  button_name="Yes, Cancel"
                />
              </div>
            </Col>
          </Row>
        </Container>
      </Modal>
      {/* cancel button model box */}
    </div>
  );
}

export default CancelModal;
