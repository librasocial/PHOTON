import React, { useState } from "react";
import { Col, Row, Form, OverlayTrigger, Tooltip } from "react-bootstrap";

function CardioVascular(props) {
  let phyExamItems = props.phyExamItems;

  const {
    pulseRate,
    rrrRate,
    obsRRR,
    tachyValue,
    bradycardia,
    jugularVenous,
    S1S2heard,
    noAdded,
  } = props.cardiacValues;

  return (
    <div>
      <Row className="assessment-button-section1">
        <div className="sysPsyHead">
          <img src="../img/physical/heart.png" className="psySysImage" />
          Cardiovascular System
        </div>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Pulse Rate </Form.Label>
              <Form.Control
                type="text"
                autoComplete="off"
                placeholder="Enter Here"
                value={pulseRate || ""}
                name="pulseRate"
                onChange={(e) => props.handleChange(e)}
              />
            </Form.Group>
          </div>
        </Col>
        <Col lg={2}>
          <div className="assessments-button phy-general1">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">
                Regular Rate & Rhythm (RRR)
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={rrrRate || ""}
                name="rrrRate"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Regular rate & rhythm (RRR)" && (
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
              <Form.Label className="require">Observed RRR </Form.Label>
              <Form.Control
                type="text"
                autoComplete="off"
                placeholder="Enter Here"
                value={obsRRR || ""}
                name="obsRRR"
                onChange={(e) => props.handleChange(e)}
              />
            </Form.Group>
          </div>
        </Col>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Tachycardia </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={tachyValue || ""}
                name="tachyValue"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Tachycardia" && (
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
              <Form.Label className="require">Bradycardia </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={bradycardia || ""}
                name="bradycardia"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Bradycardia" && (
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
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Jugular Venous Pulse </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={jugularVenous || ""}
                name="jugularVenous"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Jugular Venous Pulse" && (
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
              <Form.Label className="require">S1 S2 Heard </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={S1S2heard || ""}
                name="S1S2heard"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "S1 S2 Heard" && (
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
                No Added Sounds and Murmurs
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={noAdded || ""}
                name="noAdded"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "No added sounds and Murmurs" && (
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

export default CardioVascular;
