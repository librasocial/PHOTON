import React, { useEffect, useState } from "react";
import { Col, Row } from "react-bootstrap";
import SaveButton from "../../EMR_Buttons/SaveButton";
import { useHistory } from "react-router-dom";
import "../../../css/SuperAdmin.css";

export default function SuperAdmin(props) {
  useEffect(() => {
    document.title = "EMR Super Admin Dashboard";
  });

  let history = useHistory();
  const configState = () => {
    history.push("/config-phc");
  };

  return (
    <React.Fragment>
      <Row className="super_row">
        <Col lg={4}>
          <img src="../img/super/hospital.png" className="admin-super-image" />
        </Col>
        <Col lg={8}>
          <div className="phc-desc">
            <h3 className="super-inst">Instructions</h3>
            <h3 className="super-phc">~Sugganahalli PHC</h3>
            <p className="super-desc">
              Has been setup and you are provided the role of Administrator. As
              an administrator you are eligible to complete the setup process of
              the PHC and Sub-center (if applicable).
            </p>
            <p className="super-desc">
              Listed details are mandatory requirements to complete the setting
              up PHC / Health and Wellness Center.
            </p>
            <p className="super-desc">
              <b>Address:</b> Primary Healthcare Center, Sugganahalli, Kudur
              Hobli, Magadi Talluk, Ramanagara District
            </p>
            <h3 className="super-details">Details Required to set up a PHC</h3>
            <p className="super-list">
              <li>Processes / Activities performed in this PHC</li>
              <li>Personnel Available in PHC</li>
              <li>Assignment of Roles to Personnel</li>
              <li>Assignment of Responsibilities to Role</li>
            </p>
            <p className="super-desc">
              Configure the Software for Users, and assign Roles,
              Responsibilities and Privilege
            </p>
            <p className="super-desc">
              <input type="checkbox" />
              &nbsp; I have read all the instructions. Dont show me again
            </p>
            <div align="center" className="homeBtn">
              <SaveButton
                class_name="regBtnN"
                button_name="Start PHC Configuration"
                butttonClick={configState}
              />
            </div>
          </div>
        </Col>
      </Row>
    </React.Fragment>
  );
}
