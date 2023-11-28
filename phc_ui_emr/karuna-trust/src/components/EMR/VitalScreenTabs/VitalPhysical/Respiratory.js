import React, { useState } from "react";
import { Col, Row, Form, OverlayTrigger, Tooltip } from "react-bootstrap";

function Respiratory(props) {
  let phyExamItems = props.phyExamItems;

  const respValues = props.respValues;

  return (
    <div>
      <Row className="assessment-button-section1">
        <div className="sysPsyHead">
          <img src="../img/physical/respiratory.png" className="psySysImage" />
          Respiratory
        </div>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Respiratory Rate</Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={respValues.respiratoryRate || ""}
                name="respiratoryRate"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Respiratory Rate" && (
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
        <Col lg={2}>
          <div className="assessments-button phy-general1">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Observed Respiratory Rate{" "}
              </Form.Label>
              <Form.Control
                type="text"
                autoComplete="off"
                placeholder="Enter Here"
                value={respValues.obsRespiratoryRate || ""}
                name="obsRespiratoryRate"
                onChange={(e) => props.handleChange(e)}
              />
            </Form.Group>
          </div>
        </Col>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Tachypnea </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={respValues.Tachypnea || ""}
                name="Tachypnea"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Tachypnea" && (
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
        <Col lg={2}>
          <div className="assessments-button phy-general1">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Accessory Muscles (Sternocleidomastoid){" "}
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={respValues.AccessoryMuscles || ""}
                name="AccessoryMuscles"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName ==
                      "Accessory muscles (sternocleidomastoid)" && (
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
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Intercostal Retractions{" "}
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={respValues.InterRetractions || ""}
                name="InterRetractions"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Intercostal retractions" && (
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
      </Row>
    </div>
  );
}

export default Respiratory;
