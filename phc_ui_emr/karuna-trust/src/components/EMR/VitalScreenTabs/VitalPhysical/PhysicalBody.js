import React, { useState, useRef, useEffect } from "react";
import { Col, Row, Form } from "react-bootstrap";

export default function PhysicalBody(props) {
  let header = props.header;
  let leftsubheader = props.leftsubheader;
  let rightsubheader = props.rightsubheader;

  let rangeofmotion = props.rangeofmotion;
  let strength = props.strength;
  let muscleAccessory = props.muscleAccessory;

  let rangeLeftMotion = props.rangeLeftMotion;
  let setRangeLeftMotion = props.setRangeLeftMotion;

  let rangeRightMotion = props.rangeRightMotion;
  let setRangeRightMotion = props.setRangeRightMotion;

  let strengthLeft = props.strengthLeft;
  let setStrengthLeft = props.setStrengthLeft;

  let strengthRight = props.strengthLeft;
  let setStrengthRight = props.setStrengthRight;

  let muscleLeftWasting = props.muscleLeftWasting;
  let setMuscleLeftWasting = props.setMuscleLeftWasting;

  let muscleRightWasting = props.muscleRightWasting;
  let setMuscleRightWasting = props.setMuscleRightWasting;

  let muscleLeftSensation = props.muscleLeftSensation;
  let setMuscleLeftSensation = props.setMuscleLeftSensation;

  let muscleRightSensation = props.muscleRightSensation;
  let setMuscleRightSensation = props.setMuscleRightSensation;

  let muscleLeftReflexes = props.muscleLeftReflexes;
  let setMuscleLeftReflexes = props.setMuscleLeftReflexes;

  let muscleRightReflexes = props.muscleRightReflexes;
  let setMuscleRightReflexes = props.setMuscleRightReflexes;

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  return (
    <React.Fragment>
      <div className="assessment-radio-section">
        <div className="assessment-header">
          <h6>{header}</h6>
        </div>
        <Row>
          <Col md={6}>
            <div className="assessment-header">
              <h6>{leftsubheader}</h6>
            </div>
            <div className="assessment-buttons">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Range of Motion</Form.Label>
                <Form.Select
                  aria-label="Default select example"
                  value={rangeLeftMotion || ""}
                  name="rangeLeftMotion"
                  onChange={(e) => setRangeLeftMotion(e.target.value)}
                >
                  <option value="" hidden>
                    Select Range of Motion
                  </option>
                  {rangeofmotion.map((option, i) => (
                    <option value={option.name} key={i}>
                      {option.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </div>
            <div className="assessment-buttons">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Strength</Form.Label>
                <Form.Select
                  aria-label="Default select example"
                  value={strengthLeft || ""}
                  name="strengthLeft"
                  onChange={(e) => setStrengthLeft(e.target.value)}
                >
                  <option value="" hidden>
                    Select Strength
                  </option>
                  {strength.map((option, i) => (
                    <option value={option.name} key={i}>
                      {option.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </div>
            <div className="assessment-buttons">
              <span className="require">Wasting</span>
              <br />
              <div className="assessment-buttons">
                {muscleAccessory.map((Muscle, i) => (
                  <React.Fragment key={i}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="muscle"
                      id={
                        (header = "Shoulder"
                          ? "shr" + i
                          : (header = "Elbow"
                              ? "elr" + i
                              : (header = "Wrist"
                                  ? "wrr" + i
                                  : (header = "Hand"
                                      ? "hndr" + i
                                      : (header = "Hip"
                                          ? "hipr" + i
                                          : (header = "Knee"
                                              ? "knr" + i
                                              : (header = "Ankle"
                                                  ? "anr" + i
                                                  : (header =
                                                      "Foot" &&
                                                      "ftr" + i))))))))
                      }
                      checked={Muscle.name == muscleLeftWasting}
                      value={Muscle.name}
                      onChange={(e) => setMuscleLeftWasting(e, Muscle.name)}
                    />
                    <label className="for-checkbox-tools" htmlFor={Muscle._id}>
                      <span className={isChecked(setMuscleLeftWasting)}>
                        {Muscle.name}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </div>
            </div>
            <div className="assessment-buttons-controls pro-control">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Sensation</Form.Label>
                <div
                  className={`checkbox ${
                    muscleLeftSensation && "checkbox--on"
                  }`}
                  onClick={() => setMuscleLeftSensation(!muscleLeftSensation)}
                >
                  <div className="checkbox__ball">
                    <span className="status-sensation-text">
                      {muscleLeftSensation == false ? "No" : "Yes"}
                    </span>
                  </div>
                </div>
              </Form.Group>
            </div>
            <div className="assessment-buttons-controls pro-control">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Reflexes</Form.Label>
                <div
                  className={`checkbox ${muscleLeftReflexes && "checkbox--on"}`}
                  onClick={() => setMuscleLeftReflexes(!muscleLeftReflexes)}
                >
                  <div className="checkbox__ball">
                    <span className="status-sensation-text">
                      {muscleLeftReflexes == false ? "No" : "Yes"}
                    </span>
                  </div>
                </div>
              </Form.Group>
            </div>
          </Col>
          <Col md={6}>
            <div className="assessment-header">
              <h6>{rightsubheader}</h6>
            </div>
            <div className="assessment-buttons">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Range of Motion</Form.Label>
                <Form.Select
                  aria-label="Default select example"
                  value={rangeRightMotion || ""}
                  name="rangeRightMotion"
                  onChange={(e) => setRangeRightMotion(e.target.value)}
                >
                  <option value="" hidden>
                    Select Range of Motion
                  </option>
                  {rangeofmotion.map((option, i) => (
                    <option value={option.name} key={i}>
                      {option.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </div>
            <div className="assessment-buttons">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Strength</Form.Label>
                <Form.Select
                  aria-label="Default select example"
                  value={strengthRight || ""}
                  name="strengthRight"
                  onChange={(e) => setStrengthRight(e.target.value)}
                >
                  <option value="" hidden>
                    Select Strength
                  </option>
                  {strength.map((option, i) => (
                    <option value={option.name} key={i}>
                      {option.name}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </div>
            <div className="assessment-buttons">
              <span className="require">Wasting</span>
              <br />
              <div className="assessment-buttons">
                {muscleAccessory.map((Muscle, i) => (
                  <React.Fragment key={i}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="muscle"
                      id={
                        (header = "Shoulder"
                          ? "shl" + i
                          : (header = "Elbow"
                              ? "ell" + i
                              : (header = "Wrist"
                                  ? "wrl" + i
                                  : (header = "Hand"
                                      ? "hndl" + i
                                      : (header = "Hip"
                                          ? "hipl" + i
                                          : (header = "Knee"
                                              ? "knl" + i
                                              : (header = "Ankle"
                                                  ? "anl" + i
                                                  : (header =
                                                      "Foot" &&
                                                      "ftl" + i))))))))
                      }
                      checked={Muscle.name == muscleRightWasting}
                      value={Muscle.name}
                      onChange={(e) => setMuscleRightWasting(e, Muscle.name)}
                    />
                    <label className="for-checkbox-tools" htmlFor={Muscle._id}>
                      <span className={isChecked(muscleRightWasting)}>
                        {Muscle.name}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </div>
            </div>
            <div className="assessment-buttons-controls pro-control">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Sensation</Form.Label>
                <div
                  className={`checkbox ${
                    muscleRightSensation && "checkbox--on"
                  }`}
                  onClick={() => setMuscleRightSensation(!muscleRightSensation)}
                >
                  <div className="checkbox__ball">
                    <span className="status-sensation-text">
                      {muscleRightSensation == false ? "No" : "Yes"}
                    </span>
                  </div>
                </div>
              </Form.Group>
            </div>
            <div className="assessment-buttons-controls pro-control">
              <Form.Group className="mb-3_mname" controlId="exampleForm.MName">
                <Form.Label>Reflexes</Form.Label>
                <div
                  className={`checkbox ${
                    muscleRightReflexes && "checkbox--on"
                  }`}
                  onClick={() => setMuscleRightReflexes(!muscleRightReflexes)}
                >
                  <div className="checkbox__ball">
                    <span className="status-sensation-text">
                      {muscleRightReflexes == false ? "No" : "Yes"}
                    </span>
                  </div>
                </div>
              </Form.Group>
            </div>
          </Col>
        </Row>
      </div>
    </React.Fragment>
  );
}
