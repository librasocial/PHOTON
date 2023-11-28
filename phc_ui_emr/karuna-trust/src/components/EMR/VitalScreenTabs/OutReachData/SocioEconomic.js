import React from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import "./Socioeconomic.css";

function SocioEconomic() {
  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Socio Econimic</h5>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  /*onClick={provisionalShow}*/
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Socio Economic Survey
                </Button>
              </div>
            </Col>
          </Row>
          <Row class="front">
            <Col md={2}>
              <p>Blood Groop</p>
              <p>O +ve</p>
            </Col>
            <Col md={2}>
              <p>Marital Status</p>
              <p>Married</p>
            </Col>
            <Col md={2}>
              <p>Caste</p>
              <p>OBC</p>
            </Col>
            <Col md={2}>
              <p>Economic Status</p>
              <p>APL</p>
            </Col>
            <Col md={2}>
              <p>Education</p>
              <p>Diplome</p>
            </Col>
          </Row>
          <hr />
          <Row>
            <Col md={3}>
              <p>Socio security benefit</p>
              <p>Yes</p>
            </Col>
            <Col md={3}>
              <p>Present Occupation</p>
              <p>Buisnessman</p>
            </Col>
            <Col md={3}>
              <p>Any Disabilities</p>
              <p>Yes</p>
            </Col>
            <Col md={3}>
              <p>Type of Disabilites</p>
              <p>Speech and Language Disability</p>
            </Col>
          </Row>
          <hr />
        </Form>
      </div>
    </React.Fragment>
  );
}

export default SocioEconomic;
