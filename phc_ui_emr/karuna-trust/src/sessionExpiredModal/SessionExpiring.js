import React from "react";
import { Button, Modal, Container, Row, Col } from "react-bootstrap";
import SaveButton from "../components/EMR_Buttons/SaveButton";

function SessionExpiring(props) {
  return (
    <Modal show={props.show} className="session-modal">
      <Container>
        <div className="logout-body">
          {/* <h4 className="logout-head">Logout</h4> */}
          <div align="center" className="logout-img">
            <img src="../../img/logout-svg-walkout.png" className="rounded" />
          </div>
          <p className="log-text" align="center">
            Your session got expiring within one minute...!
          </p>
        </div>
        {/* <Row className="expire_btn">
                    <Col align="center">
                        <div>
                            <p className="session-text" >
                                Do you want to continue, Click here...!
                            </p>
                            <SaveButton class_name="regBtnN" button_name="Continue" butttonClick={closeModal} />
                        </div>
                    </Col>
                    <Col align="center">
                        <div>
                            <p className="session-text">
                                Do you want to logout, Click here...!
                            </p>
                            <SaveButton class_name="regBtnN" button_name="Logout" />
                        </div>
                    </Col>
                </Row> */}
      </Container>
    </Modal>
  );
}

export default SessionExpiring;
