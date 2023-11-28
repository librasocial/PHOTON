import React from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

function Summary() {
  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Summary </h5>
              <div className="col-container chief-textarea">
                <Form.Group className="mb-3_fname">
                  <Form.Control
                    as="textarea"
                    className="chief-textare-input"
                    placeholder="Start Typing Here..."
                    // value={oldComplaint}
                    // onChange={(event) => setOldComplaint(event.target.value)}
                    controlid="formcreatecomplaint"
                  />
                </Form.Group>
                <div className="save-btn-section">
                  <SaveButton class_name="regBtnN" button_name="Summary" />
                </div>
              </div>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  // onClick={chiefShow}
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Summary
                </Button>
              </div>
              <div className="history-body-section"></div>
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}
export default Summary;
