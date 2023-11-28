import React from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

function NonCommunicableDisease() {
  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Non Communicable Disease </h5>
              {/* <div className="col-container chief-textarea">
                                <Form.Group className="mb-3_fname">
                                    <Form.Control as="textarea" className='chief-textare-input'
                                        placeholder="Start Typing Here..."
                                        // value={oldComplaint}
                                        // onChange={(event) => setOldComplaint(event.target.value)}
                                        controlid="formcreatecomplaint"
                                    />
                                </Form.Group>
                                <div>
                                    <SaveButton class_name="regBtnN" button_name="Save Non Communicable Disease" />
                                </div>
                            </div> */}
            </Col>
            <Col md={4} className="view-details-history">
              {/* <div className='history-btn-div'>
                                <Button varient="light" className="view-prev-details"
                                // onClick={chiefShow} 
                                ><i className="fa fa-undo prev-icon"></i>
                                    Previous Non Communicable Disease
                                </Button>
                            </div>
                            <div className="history-body-section">

                            </div> */}
            </Col>
          </Row>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default NonCommunicableDisease;
