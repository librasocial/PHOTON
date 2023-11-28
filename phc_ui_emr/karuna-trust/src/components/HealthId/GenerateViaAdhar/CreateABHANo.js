import React, { useEffect, useState } from "react";
import { Col, Modal, Row } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import "../../../css/CreateABHANo.css";
import AdharProfile from "./CreatesSubModals/AdharProfile";
import CreateByAdhar from "./CreatesSubModals/CreateByAdhar";
import SelectRadio from "./CreatesSubModals/SelectRadio";
import { loadGetPublicKey } from "../../../redux/AdminAction";

function CreateABHANo(props) {
  let dispatch = useDispatch();
  // const { accessToken } = useSelector(state => state.abhaData);
  const [selectType, setSelectType] = useState("");
  const [selectStatus, setSelectStatus] = useState(false);
  const [aadharStatus, setAadharStatus] = useState(false);

  const [adharModal, setAdharModal] = useState("");
  const [mobileModal, setMobileModal] = useState("");

  useEffect(() => {
    if (props.timeoutCountdown == 0) {
      alert("Access Token expired. Please Try again..!");
      window.location.reload();
    }
  }, [props.timeoutCountdown]);

  useEffect(() => {
    dispatch(loadGetPublicKey());
  }, []);

  return (
    <div>
      <Modal
        show={props.AbhaModal}
        onHide={props.AbhaModalClose}
        className="check-In-modal-div"
      >
        <Row>
          <Col md={6} xs={9}>
            <h3 className="patient-details-header">
              <b>{aadharStatus ? "Your ABHA CArd" : "Create ABHA Number"}</b>
            </h3>
          </Col>
          <Col md={6} xs={3} align="right">
            <button
              onClick={props.AbhaModalClose}
              className="bi bi-x close-popup"
            ></button>
          </Col>
        </Row>
        <hr />

        {/* {selectStatus ?
                    <CreateByAdhar setSelectStatus={setSelectStatus} AbhaModalClose={props.AbhaModalClose}
                        setAadharStatus={setAadharStatus} setSelectType={setSelectType} />
                    :
                    <>{aadharStatus ?
                        <AdharProfile AbhaModalClose={props.AbhaModalClose} />
                        :
                        <SelectRadio
                            setSelectStatus={setSelectStatus}
                            setSelectType={setSelectType} selectType={selectType} setAdharModal={setAdharModal}
                            setMobileModal={setMobileModal} />
                    }
                    </>
                } */}
        {aadharStatus ? (
          <AdharProfile AbhaModalClose={props.AbhaModalClose} />
        ) : (
          <CreateByAdhar
            setSelectStatus={setSelectStatus}
            AbhaModalClose={props.AbhaModalClose}
            setAadharStatus={setAadharStatus}
            setSelectType={setSelectType}
          />
        )}
      </Modal>
    </div>
  );
}

export default CreateABHANo;
