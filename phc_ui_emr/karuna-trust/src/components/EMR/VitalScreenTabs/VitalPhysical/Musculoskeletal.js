import React, { useState } from "react";
import { Col, Row, Form } from "react-bootstrap";

function Musculoskeletal(props) {
  let phyExamItems = props.phyExamItems;

  const { freely, assistant, wheelchair, walkingStick, gait, extraOcular } =
    props.ambulatoryValues;
  const { flexion, bending, extension, spurling, rangeOfMove } =
    props.neckValues;

  return (
    <div>
      <Row className="gastro-assessment-button">
        <div className="sysPsyHead">
          <img src="../img/physical/bone.png" className="psySysImage" />
          Musculoskeletal Examination
        </div>
        <h2 className="physubtitle">Ambulatory</h2>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Freely Ambulatory</Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={freely || ""}
                name="freely"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Freely ambulatory" && (
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
                With Assistant (Support){" "}
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={assistant || ""}
                name="assistant"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "With assistant (support)" && (
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
              <Form.Label className="require">With Wheelchair </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={wheelchair || ""}
                name="wheelchair"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "With Wheelchair" && (
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
              <Form.Label className="require">With Walking Stick </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={walkingStick || ""}
                name="walkingStick"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "With walking stick" && (
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
              <Form.Label className="require">Gait </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={gait || ""}
                name="gait"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Gait" && (
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
                Extra-ocular Movements{" "}
              </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={extraOcular || ""}
                name="extraOcular"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Extra-ocular movements" && (
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
      <Row className="gastro-assessment-button">
        <h2 className="physubtitle">Neck</h2>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Flexion </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={flexion || ""}
                name="flexion"
                onChange={(e) => props.handleChangeNeck(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Flexion" && (
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
              <Form.Label className="require">Lateral Bending </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={bending || ""}
                name="bending"
                onChange={(e) => props.handleChangeNeck(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Lateral bending" && (
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
              <Form.Label className="require">Extension </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={extension || ""}
                name="extension"
                onChange={(e) => props.handleChangeNeck(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Extension" && (
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
              <Form.Label className="require">Spurling's Sign </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={spurling || ""}
                name="spurling"
                onChange={(e) => props.handleChangeNeck(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Spurling’s sign" && (
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
              <Form.Label className="require">Range of Movement </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={rangeOfMove || ""}
                name="rangeOfMove"
                onChange={(e) => props.handleChangeNeck(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Range of movement" && (
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

export default Musculoskeletal;
