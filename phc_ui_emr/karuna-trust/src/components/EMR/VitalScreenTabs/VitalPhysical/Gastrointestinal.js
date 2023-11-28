import React, { useState } from "react";
import { Col, Row, Form } from "react-bootstrap";

function Gastrointestinal(props) {
  let phyExamItems = props.phyExamItems;

  const {
    inspection,
    palpation,
    tender,
    tenderness,
    hepatomegaly,
    splenomegaly,
    hernia,
    bowelSounds,
    murphySign,
    mcBurneyPoint,
  } = props.gastroValues;

  return (
    <div>
      <Row className="gastro-assessment-button">
        <div className="sysPsyHead">
          <img src="../img/physical/intestine.png" className="psySysImage" />
          Gastrointestinal System
        </div>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Inspection </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={inspection || ""}
                name="inspection"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Inspection" && (
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
              <Form.Label className="require">Palpation </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={palpation || ""}
                name="palpation"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Palpation" && (
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
              <Form.Label className="require">Tender </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={tender || ""}
                name="tender"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Tender" && (
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
          {/* {tender == "Yes" && */}
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Tenderness in </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={tenderness || ""}
                name="tenderness"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "If Yes, Tenderness in" && (
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
          {/* }  */}
        </Col>
        <Col lg={2}>
          <div className="assessments-button phy-general">
            <Form.Group className="mb-3_fname" controlId="exampleForm.FName">
              <Form.Label className="require">Hepatomegaly</Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={hepatomegaly || ""}
                name="hepatomegaly"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Hepatomegaly" && (
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
              <Form.Label className="require">Splenomegaly </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={splenomegaly || ""}
                name="splenomegaly"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Splenomegaly" && (
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
              <Form.Label className="require">Hernia </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={hernia || ""}
                name="hernia"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Hernia" && (
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
              <Form.Label className="require">Bowel Sounds </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={bowelSounds || ""}
                name="bowelSounds"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Bowel sounds" && (
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
              <Form.Label className="require">Murphy's Sign </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={murphySign || ""}
                name="murphySign"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "Murphy’s sign" && (
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
              <Form.Label className="require">McBurney's Point </Form.Label>
              <Form.Select
                aria-label="Default select example"
                value={mcBurneyPoint || ""}
                name="mcBurneyPoint"
                onChange={(e) => props.handleChange(e)}
              >
                <option value="" hidden>
                  Select{" "}
                </option>
                {phyExamItems.map((formItem, i) => (
                  <React.Fragment key={i}>
                    {formItem.groupName == "McBurney’s point" && (
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

export default Gastrointestinal;
