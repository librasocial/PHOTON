import React, { useEffect, useState } from "react";
import { Form, Modal } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { loadRegisterAbdm } from "../../../redux/AdminAction";
import SaveButton from "../../EMR_Buttons/SaveButton";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

function RegisterModal(props) {
  let displayData = props.displayData;
  let dispatch = useDispatch();
  const { sunCenterDetails } = useSelector((state) => state.phcData);
  const { phcDetails } = useSelector((state) => state.phcData);
  const { accessToken } = useSelector((state) => state.abhaData);

  const [centerData, setCenterData] = useState("");
  let facilityType = props.facilityType;

  useEffect(() => {
    if (props.centerType) {
      if (props.centerType == "Phc") {
        setCenterData(phcDetails);
      } else if (props.centerType == "Sub_Center") {
        setCenterData(sunCenterDetails);
      }
    }
  }, [props.centerType]);

  const [inputData, setInputData] = useState({
    id: "",
    name: "",
    type: "",
    active: true,
    alias: [],
  });

  const { id, name, type, active, alias } = inputData;

  const [statusValiue, setStatusValue] = useState("");
  const handleInput = (e) => {
    const { name, value } = e.target;
    if (name === "active") {
      // setStatusValue("true");
      if (value === "true") {
        setInputData({ ...inputData, [name]: true });
      } else if (value === "false") {
        setInputData({ ...inputData, [name]: false });
      }
    } else {
      setInputData({ ...inputData, [name]: value });
    }
  };

  useEffect(() => {
    if (centerData) {
      let propData = centerData?.properties;
      console.log(centerData.properties.name);

      // if (centerData?.properties?.facilityId) {
      setInputData({
        id: propData?.facilityId,
        name: propData.name,
        type: "",
        active: propData?.status === "ACTIVE" ? true : false,
        alias: [propData?.facilityId],
      });
      // }
      //  else {
      //   alert(
      //     "Subcenter/Phc dont have Facility Id. Please add facility Id, before registering to ABDM"
      //   );
      //   props.handleRegisterClose();
      // }
    }
  }, [centerData]);

  const closeModal = () => {
    props.setCenterType("");
    setCenterData("");
    setInputData({
      id: "",
      name: "",
      type: "",
      active: "",
      alias: [],
    });
    props.handleRegisterClose();
  };

  const RegisterABDM = () => {
    const body = [];
    body.push(inputData);
    let postRequest = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      // 'Access-Control-Allow-Origin': '*',

      body: JSON.stringify(body),
    };

    dispatch(loadRegisterAbdm(postRequest, closeModal));
    props.handleRegisterClose();
  };

  return (
    <div>
      {facilityType.some(
        (e) =>
          e.name === centerData?.properties?.name &&
          ((e.types[0] === "HIP" && e.types[1] === "HIU") ||
            (e.types[0] === "HIU" && e.types[1] === "HIP"))
      ) ? (
        <>
          <Modal
            show={props.registerModalShow}
            onHide={props.handleRegisterClose}
            className="abdm-staff-div"
          >
            <div className="phc-header-div">
              <h5 className="staff-header">
                Register {centerData?.properties?.name} for ABDM{" "}
                <i
                  className="fa fa-close close-btn-style"
                  onClick={closeModal}
                ></i>
              </h5>
            </div>
            <Form>
              <div className="phc-info-div">
                <Form.Group>
                  <h3>Already Registered</h3>
                </Form.Group>
              </div>

              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label className="age">Facility Name</Form.Label>
                  <Form.Control
                    type="text"
                    // placeholder={name || "Facility Name"}
                    name="name"
                    readOnly
                    value={name || ""} //name || ""
                    onChange={handleInput}
                  />
                </Form.Group>
              </div>

              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label className="age">Facility ID</Form.Label>
                  <Form.Control
                    type="text"
                    name="id"
                    readOnly
                    value={id || ""}
                    onChange={handleInput}
                  />
                </Form.Group>
              </div>

              {/* <div className="staff-reg-accordion"> */}
              <div className="staff_Foot">
                <div className="save-btn-section">
                  <SaveButton
                    class_name="regBtnPC"
                    button_name="Cancel"
                    butttonClick={closeModal}
                  />
                </div>
              </div>
              {/* </div> */}
            </Form>
          </Modal>
        </>
      ) : (
        <>
          <Modal
            show={props.registerModalShow}
            onHide={props.handleRegisterClose}
            className="abdm-staff-div"
          >
            <div className="phc-header-div">
              <h5 className="staff-header">
                Register {centerData?.properties?.name} for ABDM{" "}
                <i
                  className="fa fa-close close-btn-style"
                  onClick={closeModal}
                ></i>
              </h5>
            </div>
            <Form>
              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label className="age">Facility Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder={name || "Facility Name"}
                    name="name"
                    // readOnly
                    value={name || ""} //name || ""
                    onChange={handleInput}
                  />
                </Form.Group>
              </div>
              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label className="age">Facility ID</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder={
                      id || "Please Enter the Facility ID from Sub Center Page"
                    }
                    name="id"
                    readOnly
                    value={id || ""}
                    onChange={handleInput}
                  />
                </Form.Group>
              </div>
              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label>Facility Type</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    placeholder="Select Facility Type"
                    name="type"
                    value={type || ""}
                    onChange={handleInput}
                  >
                    <option value="" disabled hidden>
                      Select Facility Type
                    </option>
                    {facilityType.map((e) => e.id).includes(id) ? (
                      facilityType.map((e) => {
                        if (e.id.includes(id)) {
                          if (e.types[0] === "HIP" && e.types[1] === "HIU") {
                            return <option key={e.id} value=""></option>;
                          } else if (
                            e.types[0] === "HIU" &&
                            e.types[1] === "HIP"
                          ) {
                            return <option key={e.id} value=""></option>;
                          } else if (
                            e.types[0] === "HIP" ||
                            e.types[1] == "HIP"
                          ) {
                            return (
                              <option key={e.id} value="HIU">
                                HIU
                              </option>
                            );
                          } else if (
                            e.types[0] === "HIU" ||
                            e.types[1] === "HIU"
                          ) {
                            return (
                              <option key={e.id} value="HIP">
                                HIP
                              </option>
                            );
                          } else {
                            return (
                              <>
                                <option key={e.id} value="HIP">
                                  HIP
                                </option>
                                <option key={e.id} value="HIU">
                                  HIU
                                </option>
                              </>
                            );
                          }
                        }
                      })
                    ) : (
                      <>
                        <option value="HIP">HIP</option>
                        <option value="HIU">HIU</option>
                      </>
                    )}
                  </Form.Select>
                </Form.Group>
              </div>
              <div className="phc-info-div">
                <Form.Group>
                  <Form.Label>Status</Form.Label>
                  <Form.Select
                    aria-label="Default select example"
                    placeholder="Select Status"
                    name="active"
                    value={statusValiue || ""}
                    onChange={handleInput}
                  >
                    <option value="" disabled hidden>
                      {active
                        ? active
                          ? "Active"
                          : "InActive"
                        : "Select Status"}
                    </option>
                    <option value="true">Active</option>
                    <option value="false">InActive</option>
                  </Form.Select>
                </Form.Group>
              </div>

              <div className="staff-reg-accordion">
                <div className="staff_Foot">
                  <div className="save-btn-section">
                    <SaveButton
                      class_name="regBtnPC"
                      button_name="Cancel"
                      butttonClick={closeModal}
                    />
                  </div>
                  <div className="save-btn-section">
                    <SaveButton
                      btnDisable={
                        inputData.id === undefined || inputData.id === ""
                      }
                      class_name="regBtnN"
                      button_name="Register"
                      butttonClick={RegisterABDM}
                    />
                    {/* } */}
                  </div>
                </div>
              </div>
            </Form>
          </Modal>
        </>
      )}
    </div>
  );
}

export default RegisterModal;
