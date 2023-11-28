import React, { useState, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Breadcrumb,
  Modal,
  Card,
  Accordion,
} from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

export default function AssignFacilityModal(props) {
  const [isActive, setIsActive] = useState(false);
  function assignProResponse() {
    setIsActive(true);
  }

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  return (
    <React.Fragment>
      <Modal
        show={props.assignFacilityWindow}
        onHide={props.facilityWindowClose}
        className="assign-process-div"
      >
        <div className="assign-header-div">
          <h5 className="assign-process-header">
            Select from the List of Staff to Make them Process Owner{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={props.facilityWindowClose}
            ></i>
          </h5>
        </div>
        <Row className="assign-staff-details">
          <Col lg={4}>
            <Card
              className="staff-assign-card"
              style={{
                backgroundColor: isActive ? "#DCF3E5" : "",
                borderColor: isActive ? "#1D9824" : "",
              }}
              onClick={assignProResponse}
            >
              <Row>
                <Col lg={3}>
                  <img src="../img/super/user.png" />
                </Col>
                <Col lg={9}>
                  <div className="staff-div">
                    <p className="assign-staff">Rangaswamy</p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff No:</span> 1234
                    </p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff Post:</span>{" "}
                      Admin
                    </p>
                  </div>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col lg={4}>
            <Card
              className="staff-assign-card"
              style={{
                backgroundColor: isActive ? "#DCF3E5" : "",
                borderColor: isActive ? "#1D9824" : "",
              }}
              onClick={assignProResponse}
            >
              <Row>
                <Col lg={3}>
                  <img src="../img/super/user.png" />
                </Col>
                <Col lg={9}>
                  <div className="staff-div">
                    <p className="assign-staff">Rangaswamy</p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff No:</span> 1234
                    </p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff Post:</span>{" "}
                      Admin
                    </p>
                  </div>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col lg={4}>
            <Card
              className="staff-assign-card"
              style={{
                backgroundColor: isActive ? "#DCF3E5" : "",
                borderColor: isActive ? "#1D9824" : "",
              }}
              onClick={assignProResponse}
            >
              <Row>
                <Col lg={3}>
                  <img src="../img/super/user.png" />
                </Col>
                <Col lg={9}>
                  <div className="staff-div">
                    <p className="assign-staff">Rangaswamy</p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff No:</span> 1234
                    </p>
                    <p className="total-staff">
                      <span style={{ color: "#707070" }}>Staff Post:</span>{" "}
                      Admin
                    </p>
                  </div>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
        <div className="assign-pro-btn">
          <div className="save-btn-section">
            <SaveButton
              butttonClick={props.facilityWindowClose}
              class_name="regBtnPC"
              button_name="Cancel"
            />
          </div>
          <div className="save-btn-section">
            <SaveButton
              butttonClick={props.facilityAssignRoles}
              class_name="regBtnN"
              button_name="Save"
            />
          </div>
        </div>
      </Modal>
    </React.Fragment>
  );
}
