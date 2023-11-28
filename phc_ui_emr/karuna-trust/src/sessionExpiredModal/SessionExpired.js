import React from "react";
import { Modal, Container } from "react-bootstrap";
import SaveButton from "../components/EMR_Buttons/SaveButton";

function Sessionexpired(props) {
  const handleClick = () => {
    window.location.href = "/";
  };
  return (
    <Modal show={props.show} className="session-modal">
      <Container>
        <div className="logout-body">
          {/* <h4 className="logout-head">Logout</h4> */}
          <div align="center" className="logout-img">
            <img src="../../img/logout-svg-walkout.png" className="rounded" />
          </div>
          <p className="log-text" align="center">
            Your session got expired...!
          </p>
        </div>
        <div align="center">
          <SaveButton
            class_name="regBtnN"
            button_name="Ok"
            butttonClick={handleClick}
          />
        </div>
      </Container>
    </Modal>
  );
}

export default Sessionexpired;
