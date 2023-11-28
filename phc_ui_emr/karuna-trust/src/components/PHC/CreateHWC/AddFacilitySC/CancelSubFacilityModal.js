import React, { useState, useEffect } from "react";
import { Col, Row, Modal } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

export default function CancelSubFacilityModal(props) {
  return (
    <React.Fragment>
      <Modal
        show={props.cancelFacilityWindow || props.cancelProcessWindow}
        onHide={props.facilityDetailClose || props.facilityProcessClose}
        className="create-facility-div"
      >
        <div align="center" className="process-img">
          <img src="../img/super/troubleshoot.png" />
        </div>
        <div className="facilty-details">
          <h4 className="facilty-head">Cancel Configuration?</h4>
          <p className="facilty-body">
            All the filled-in details will be lost. Configuration once cancelled
            cannot be reverted.
          </p>
          <p className="facilty-body">
            Are you sure you want to cancel configuration?
          </p>
        </div>
        <Row>
          <Col className="btn1">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={
                  props.removevitalstorege || props.removevitalstorage1
                }
                class_name="regBtnPC"
                button_name="Cancel Configuration"
              />
            </div>
          </Col>
          <Col className="btn2">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={
                  props.facilityDetailClose || props.facilityProcessClose
                }
                class_name="regBtnN"
                button_name="Continue  Configuration"
              />
            </div>
          </Col>
        </Row>
      </Modal>
    </React.Fragment>
  );
}
