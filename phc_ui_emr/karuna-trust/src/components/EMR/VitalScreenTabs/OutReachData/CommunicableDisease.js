import React from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";

function CommunicableDisease() {
  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <Row>
            <Col md={8}>
              <h5 className="vital-header">Communicable Disease </h5>
              <ul className="nav nav-tabs nav-tabs-header presc-tab-head">
                <li className="active">
                  <a href="" data-toggle="tab">
                    1W
                  </a>
                </li>
                <li>
                  <a href="" data-toggle="tab" className="active">
                    15D
                  </a>
                </li>
                <li>
                  <a href="" data-toggle="tab">
                    1M
                  </a>
                </li>
                <li>
                  <a href="" data-toggle="tab">
                    3M
                  </a>
                </li>
                <li>
                  <a href="" data-toggle="tab">
                    6M
                  </a>
                </li>
              </ul>
            </Col>
            <Col md={4} className="view-details-history">
              <div className="history-btn-div">
                <Button
                  varient="light"
                  className="view-prev-details"
                  /*onClick={provisionalShow}*/
                >
                  <i className="fa fa-undo prev-icon"></i>
                  Previous Communicable Disease
                </Button>
              </div>
            </Col>
          </Row>
          <div classname="app-container">
            <table>
              <thead>
                <tr>
                  <th>Result Data</th>
                  <th>Disease Type</th>
                  <th>Lab Technician Name</th>
                  <th>Result / Suspected Type</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>20 Dec 2020</td>
                  <td>Malaria</td>
                  <td>Koushik M R</td>
                  <td>Positive</td>
                </tr>
                <tr>
                  <td>20 Dec 2020</td>
                  <td>ACF (Tuberculosis)</td>
                  <td>Koushik M R</td>
                  <td>Positive</td>
                </tr>
                <tr>
                  <td>19 Dec 2020</td>
                  <td>Urine Sample</td>
                  <td>-</td>
                  <td>0000</td>
                </tr>
                <tr>
                  <td>12 Dec 2020</td>
                  <td>Leprocy</td>
                  <td>-</td>
                  <td>Positive</td>
                </tr>
                <tr>
                  <td>05 Dec 2020</td>
                  <td>COVID</td>
                  <td>-</td>
                  <td>Positive</td>
                </tr>
              </tbody>
            </table>
          </div>
        </Form>
      </div>
    </React.Fragment>
  );
}

export default CommunicableDisease;
