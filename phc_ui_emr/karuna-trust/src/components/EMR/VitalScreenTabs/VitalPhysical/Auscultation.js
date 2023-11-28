import React, { useState } from "react";
import { Col, Row, Form, OverlayTrigger, Tooltip } from "react-bootstrap";

function Auscultation(props) {
  let phyExamItems = props.phyExamItems;

  const { lungClr, rhonchi, wheezes, crepita, airEntry, breathMov, breathSou } =
    props.asculValues;

  return (
    <div>
      <Row className="assessment-button-section1">
        <div className="sysPsyHead">
          <img src="../img/physical/stethoscope.png" className="psySysImage" />
          Auscultation
        </div>
        <Col lg={2}>
          <div className="assessments-button phy-general1">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Lungs Clear on Auscultation{" "}
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={lungClr || ""}
                name="lungClr"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Lungs clear on auscultation" && (
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
              <Form.Label className="require">Rhonchi </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={rhonchi || ""}
                name="rhonchi"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Rhonchi" && (
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
              <Form.Label className="require">Wheezes </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={wheezes || ""}
                name="wheezes"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Wheezes" && (
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
              <Form.Label className="require">Crepitations </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={crepita || ""}
                name="crepita"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Crepitations" && (
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
              <Form.Label className="require">Airway Entry </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={airEntry || ""}
                name="airEntry"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Airway Entry" && (
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
      <Row className="assessment-button-section1">
        <Col lg={2}>
          <div className="assessments-button phy-general1">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Breath Movements Rt/Lt
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={breathMov || ""}
                name="breathMov"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Breath movements Rt/Lt" && (
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
              <Form.Label className="require">Breath Sounds Rt/Lt</Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={breathSou || ""}
                name="breathSou"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Breath Sounds Rt/Lt" && (
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

export default Auscultation;
