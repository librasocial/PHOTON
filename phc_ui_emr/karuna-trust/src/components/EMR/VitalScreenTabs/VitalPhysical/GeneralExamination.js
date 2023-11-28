import React, { useState } from "react";
import { useEffect } from "react";
import { Col, Row, Form } from "react-bootstrap";
import GeneralRadioButton from "./GeneralRadioButton";

function GeneralExamination(props) {
  let phyExamItems = props.phyExamItems;

  let observationName1 = [
    { name: "Conscious" },
    { name: "Cooperative" },
    { name: "Comfortable" },
    { name: "Toxic" },
    { name: "Dyspneic" },
  ];

  let observationName2 = [{ name: "Build" }, { name: "Nourishment" }];

  let observationName3 = [
    { name: "Pallor" },
    { name: "Icterus" },
    { name: "Cyanosis" },
    { name: "Clubbing" },
    { name: "Koilonychia" },
  ];

  let observationName4 = [{ name: "Lymphadenopathy" }, { name: "Pedal Edema" }];

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  return (
    <div>
      <Row className="assessment-button-section1">
        {observationName1.map((name, index) => (
          <Col md={2} lg={2} key={index}>
            <div className="new-assessment-buttons phy-general">
              <span className="require">{name.name}</span>
              <br />
              <GeneralRadioButton
                phyExamItems={phyExamItems}
                setGenPallor={props.setGenPallor}
                isChecked={isChecked}
                assessName={name.name}
                selectValue={
                  name.name == "Conscious"
                    ? props.consciousVal
                    : name.name == "Cooperative"
                    ? props.corporateVal
                    : name.name == "Comfortable"
                    ? props.comfortableVal
                    : name.name == "Toxic"
                    ? props.toxicVal
                    : name.name == "Dyspneic" && props.dyspneicVal
                }
              />
            </div>
          </Col>
        ))}
      </Row>
      <Row className="assessment-button-section1">
        {observationName2.map((name, index) => (
          <Col md={4} lg={4} key={index}>
            <div className="new-assessment-buttons phy-general">
              <span className="require">{name.name}</span>
              <br />
              <GeneralRadioButton
                phyExamItems={phyExamItems}
                setGenPallor={props.setGenPallor}
                isChecked={isChecked}
                assessName={name.name}
                selectValue={
                  name.name == "Build"
                    ? props.buildVal
                    : name.name == "Nourishment" && props.nourishmentVal
                }
              />
            </div>
          </Col>
        ))}
      </Row>
      <Row className="assessment-button-section1">
        <h3 className="acc-sub-title">PICKLE</h3>
        {observationName3.map((name, index) => (
          <Col md={2} lg={2} key={index}>
            <div className="new-assessment-buttons phy-general">
              <span className="require">{name.name}</span>
              <br />
              <GeneralRadioButton
                phyExamItems={phyExamItems}
                setGenPallor={props.setGenPallor}
                isChecked={isChecked}
                assessName={name.name}
                selectValue={
                  name.name == "Pallor"
                    ? props.pallorVal
                    : name.name == "Icterus"
                    ? props.icterusVal
                    : name.name == "Cyanosis"
                    ? props.cyanosisVal
                    : name.name == "Clubbing"
                    ? props.clubbingVal
                    : name.name == "Koilonychia" && props.koilonychiaVal
                }
              />
            </div>
          </Col>
        ))}
      </Row>
      <Row className="assessment-button-section1">
        {observationName4.map((name, index) => (
          <Col md={2} lg={2} key={index}>
            <div className="new-assessment-buttons phy-general">
              <span className="require">{name.name}</span>
              <br />
              <GeneralRadioButton
                phyExamItems={phyExamItems}
                setGenPallor={props.setGenPallor}
                isChecked={isChecked}
                assessName={name.name}
                selectValue={
                  name.name == "Lymphadenopathy"
                    ? props.lymphadenopathyVal
                    : name.name == "Pedal Edema" && props.pedalEdemaVal
                }
              />
            </div>
          </Col>
        ))}
      </Row>
    </div>
  );
}

export default GeneralExamination;
