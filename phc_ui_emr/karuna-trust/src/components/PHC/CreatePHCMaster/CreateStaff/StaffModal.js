import React, { useState, useEffect, useRef } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Modal,
  Accordion,
  InputGroup,
} from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import moment from "moment";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import {
  loadCreateStaffMember,
  loadStaffData,
  loadUpdateStaffMember,
} from "../../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import { loadFacilityDropdown } from "../../../../redux/formUtilityAction";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import FileUpload from "../../FileUpload/FileUpload";
import PageLoader from "../../../PageLoader";

let center_type = "Staff";
let updateType = "remove-image";
export default function StaffModal(props) {
  // const [photo, setPhoto] = useState("");
  let dispatch = useDispatch();
  const { staffDetails } = useSelector((state) => state.phcData);
  const { facilityDropDown } = useSelector((state) => state.formData);
  let phcuuid = sessionStorage.getItem("uuidofphc");

  const [valuePlaceholder, setValuePlaceholder] = useState(
    "Enter Citizen Aadhar Number"
  );
  const [aadhar, setAadhar] = useState(true);
  const [voterId, setVoterId] = useState(false);

  const [imageShow, setImageShow] = useState(false);
  const [imgText, setImgText] = useState(false);
  const [imageChange, setImageChange] = useState(false);
  const [upload, setUpload] = useState(false);

  const [uploadImage, setUploadImage] = useState("");

  const startCam = () => {
    setImageShow(true);
    setImgText(false);
  };
  const onButtonClick = () => {
    setImageShow(true);
    setImgText(true);
    setUpload(false);
  };

  const handleImageClose = () => {
    setImageShow(false);
  };

  const changeImage = () => {
    setImageShow(true);
    setImageChange(true);
  };

  useEffect(() => {
    (document.title = "EMR Super Admin Create Staff"),
      dispatch(loadFacilityDropdown());
  }, []);

  // Software Register Form
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

  const [panType, setPanType] = useState("password");
  const [panText, setPanText] = useState(
    <i className="bi bi-eye-slash-fill"></i>
  );

  const handlePanToggle = () => {
    if (panType === "password") {
      setPanType("text");
      setPanText(<i className="bi bi-eye"></i>);
    } else {
      setPanType("password");
      setPanText(<i className="bi bi-eye-slash-fill"></i>);
    }
  };

  const [staffRole, setStaffRole] = useState("");
  const [creatsStaff, setCreatsStaff] = useState({
    salutation: "",
    name: "",
    email: "",
    contact: "",
    gender: "",
    dateOfBirth: "",
    healthID: "",
    pan: "",
    citizenIdType: "Aadhaar",
    citizenIdNumber: "",
    bloodGroup: "",
    height: "",
    caste: "",
    religion: "",
    nationality: "",
    photo: "",
    status: "INACTIVE",
    posted_by: "",
    phc: phcuuid,
  });

  const {
    salutation,
    name,
    email,
    contact,
    gender,
    dateOfBirth,
    healthID,
    pan,
    citizenIdType,
    citizenIdNumber,
    bloodGroup,
    height,
    caste,
    religion,
    nationality,
    photo,
    status,
    posted_by,
    phc,
  } = creatsStaff;

  useEffect(() => {
    if (Object.keys(staffDetails).length != 0) {
      Object.keys(staffDetails).map((staff) => {
        let data = staffDetails[staff].properties;
        setStaffRole(data.type);
        setCreatsStaff({
          salutation: data.salutation,
          name: data.name,
          email: data.email,
          contact: data.contact,
          gender: data.gender,
          dateOfBirth: data.dateOfBirth,
          healthID: data.healthID,
          pan: data.pan,
          citizenIdType: data.citizenIdType,
          citizenIdNumber: data.citizenIdNumber,
          bloodGroup: data.bloodGroup,
          height: data.height,
          caste: data.caste,
          religion: data.religion,
          nationality: data.nationality,
          photo: data.photo,
          status: data.status,
          posted_by: data.posted_by,
        });
      });
    }
  }, [staffDetails]);

  useEffect(() => {
    if (uploadImage) {
      creatsStaff.photo = uploadImage;
    }
  }, [uploadImage]);

  const staffHndleChange = (e) => {
    const { name, value } = e.target;
    setCreatsStaff({ ...creatsStaff, [name]: value });
    if (e.target.name == "salutation") {
      if (e.target.value == "Mr.") {
        creatsStaff.gender = "Male";
      } else if (e.target.value == "Ms." || e.target.value == "Mrs.") {
        creatsStaff.gender = "Female";
      }
      setCreatsStaff({ ...creatsStaff, [name]: value });
    }
    if (e.target.name == "gender") {
      if (e.target.value == "Male") {
        creatsStaff.salutation = "Mr.";
      } else if (e.target.value == "Female") {
        creatsStaff.salutation = "Mrs.";
      }
      setCreatsStaff({ ...creatsStaff, [name]: value });
    }
    if (e.target.name == "dateOfBirth") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        setCreatsStaff({ ...creatsStaff, dateOfBirth: "" });
        Tostify.notifyWarning(
          "You are not Suppose to Enter Future Date in Date Of Birth...!"
        );
      } else {
        setCreatsStaff({ ...creatsStaff, [name]: value });
      }
    }
    if (e.target.name == "pan") {
      setCreatsStaff({ ...creatsStaff, [name]: value.toUpperCase() });
    }
    if (e.target.name == "citizenIdType") {
      const xx = e.target.value;
      if (xx === "Aadhaar") {
        setValuePlaceholder("Enter Citizen Aadhar Number");
        setAadhar(true);
        setVoterId(false);
      } else if (xx === "Voter ID") {
        setValuePlaceholder("Enter Citizen Voter ID");
        setAadhar(false);
        setVoterId(true);
      }
      setCreatsStaff({ ...creatsStaff, [name]: value.toUpperCase() });
    }
  };

  const [loginAccess, setLoginAccess] = useState({
    USERNAME: "",
    PASSWORD: "",
  });

  const { USERNAME, PASSWORD } = loginAccess;
  const setAccessChange = (e) => {
    const { name, value } = e.target;
    setLoginAccess({ ...loginAccess, [name]: value });
  };

  const [passwordType, setPasswordType] = useState("password");
  const [passwordText, setPasswordText] = useState(
    <i className="bi bi-eye-slash-fill"></i>
  );

  const handleToggle = () => {
    if (passwordType === "password") {
      setPasswordType("text");
      setPasswordText(<i className="bi bi-eye"></i>);
    } else {
      setPasswordType("password");
      setPasswordText(<i className="bi bi-eye-slash-fill"></i>);
    }
  };

  const modalClose = () => {
    props.handleStaffClose(false);
  };

  // POST Call
  const [pageLoader, setPageLoader] = useState(false);
  function saveStaff() {
    var postData = {
      type: staffRole,
      properties: creatsStaff,
    };

    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(postData),
    };

    if (!salutation || !name || !dateOfBirth || !gender || !staffRole) {
      Tostify.notifyWarning("Please Enter All Mandatory Fields...!");
    } else {
      dispatch(loadCreateStaffMember(postResponse, loginAccess, modalClose));
    }
  }
  // POST Call
  // PATCH Call
  function updateStaff(uuid, updateType) {
    setPageLoader(true);
    let postData;
    if (updateType) {
      postData = {
        type: staffRole,
        properties: {
          photo: "",
        },
      };
    } else {
      postData = {
        type: staffRole,
        properties: creatsStaff,
      };
    }

    var patchResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(postData),
    };

    dispatch(
      loadUpdateStaffMember(
        uuid,
        patchResponse,
        setPageLoader,
        modalClose,
        loginAccess
      )
    );
  }
  // PATCH Call

  const closeStaffModal = () => {
    setCreatsStaff({
      salutation: "",
      name: "",
      email: "",
      contact: "",
      gender: "",
      dateOfBirth: "",
      healthID: "",
      pan: "",
      citizenIdType: "",
      citizenIdNumber: "",
      bloodGroup: "",
      height: "",
      caste: "",
      religion: "",
      nationality: "",
      photo: "",
      status: "",
      posted_by: "",
      phc: phcuuid,
    });
    dispatch(loadStaffData(null));
    props.setIdForUpdate("");
    props.handleStaffClose(false);
  };

  const [panError, setPanError] = useState("");
  const [voterError, setVoterError] = useState("");
  const pancardValidation = (text) => {
    let regex = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
    if (regex.test(text)) {
      setPanError("");
    } else {
      setPanError("Enter valid PAN number");
    }
  };
  const voterValidation = (text) => {
    let regex = /^[A-Z]{3}[0-9]{7}$/;
    if (regex.test(text)) {
      setVoterError("");
    } else {
      setVoterError("Enter valid Voter Id number");
    }
  };

  useEffect(() => {
    if (pan) {
      pancardValidation(pan);
    }
    if (citizenIdType == "Voter ID") {
      if (citizenIdNumber) {
        voterValidation(citizenIdNumber);
      }
    }
  }, [pan, citizenIdType, citizenIdNumber]);

  return (
    <React.Fragment>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      {/* upload image */}
      {imageShow && (
        <FileUpload
          imageShow={imageShow}
          imgText={imgText}
          setImgText={setImgText}
          upload={upload}
          setUpload={setUpload}
          handleImageClose={handleImageClose}
          setUploadImage={setUploadImage}
          imageChange={imageChange}
          setImageChange={setImageChange}
          center_type={center_type}
        />
      )}
      {/* upload image */}
      <Modal
        show={props.staffShow}
        onHide={props.handleStaffClose}
        className="phc-staff-div"
      >
        <ToastContainer />
        <div className="phc-header-div">
          <h5 className="staff-header">
            {" "}
            {props.idForUpdate ? "Edit" : "Enter"} Staff Details{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={closeStaffModal}
            ></i>
          </h5>
        </div>
        <Form autoComplete="off">
          <Row>
            <Col lg={9} className="cred-staff">
              <Row className="super-staff-form">
                <Col lg={3}>
                  <div>
                    <p className="upload-staff">Upload Staff Photo</p>
                    <div className="manage-phc">
                      {photo != "" && photo != " " && photo != null ? (
                        <div>
                          <div className="image-div">
                            <img
                              src={photo || ""}
                              style={{ height: "150px", width: "400px" }}
                            />
                          </div>
                          <Row className="image-action-btn">
                            <Col md={6}>
                              <div
                                onClick={(e) =>
                                  updateStaff(props.idForUpdate, updateType)
                                }
                                className="updateImage"
                              >
                                Remove
                              </div>
                            </Col>
                            <Col md={6}>
                              <div
                                onClick={changeImage}
                                className="updateImage"
                              >
                                Change
                              </div>
                            </Col>
                          </Row>
                        </div>
                      ) : (
                        <div>
                          {uploadImage ? (
                            <div>
                              <img
                                src={uploadImage}
                                className="user-login-img"
                                style={{ height: "150px", width: "100%" }}
                              />
                            </div>
                          ) : (
                            <div>
                              <img
                                src="../img/super/user.png"
                                className="user-login-img"
                              />
                              <Button
                                className="regCapture"
                                onClick={startCam}
                                style={{ cursor: "pointer" }}
                              >
                                Capture
                              </Button>
                              <div>
                                <p style={{ marginTop: "6px" }}>or</p>
                                <p
                                  className="uploadphc"
                                  onClick={onButtonClick}
                                  style={{ cursor: "pointer" }}
                                >
                                  Upload from Computer
                                </p>
                              </div>
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                </Col>
                <Col lg={9}>
                  <Row className="phc-staff-div">
                    <Col lg={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Salutation <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="salutation"
                            value={salutation || ""}
                            onChange={staffHndleChange}
                          >
                            <option value="" disabled hidden>
                              Select
                            </option>
                            {facilityDropDown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Salutation" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Staff Name <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter Staff Name"
                            name="name"
                            value={name || ""}
                            onChange={staffHndleChange}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={4}>
                      <div>
                        <Form.Group>
                          <Form.Label className="age">
                            {" "}
                            Date of Birth{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Control
                            className="age"
                            type="date"
                            max={moment(new Date()).format("YYYY-MM-DD")}
                            name="dateOfBirth"
                            value={dateOfBirth || ""}
                            onChange={staffHndleChange}
                            validate={[disableFutureDt]}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={4}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Gender <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            name="gender"
                            value={gender || ""}
                            onChange={staffHndleChange}
                          >
                            <option value="" disabled hidden>
                              Select
                            </option>
                            {facilityDropDown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Gender" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Staff Designation in the PHC{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select Salutation"
                            value={staffRole || ""}
                            onChange={(e) => setStaffRole(e.target.value)}
                          >
                            <option value="" disabled hidden>
                              Select
                            </option>
                            {facilityDropDown.map((dropStaff, i) => (
                              <React.Fragment key={i}>
                                {dropStaff.groupName == "Staff Designation" && (
                                  <>
                                    {dropStaff.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.label}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={3}></Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9}>
                      <div>
                        <Form.Label className="age"> Mobile No. </Form.Label>
                        <InputGroup className="mb-3">
                          <InputGroup.Text id="basic-tel-code">
                            +91-{" "}
                          </InputGroup.Text>
                          <Form.Control
                            type="tel"
                            placeholder="Enter Mobile No."
                            className="staff-tel-number"
                            name="contact"
                            maxLength={10}
                            onKeyPress={(e) => {
                              if (!/[0-9]/.test(e.key)) {
                                e.preventDefault();
                              }
                            }}
                            value={contact || ""}
                            onChange={staffHndleChange}
                          />
                        </InputGroup>
                      </div>
                    </Col>
                    <Col lg={3}></Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9}>
                      <div>
                        <Form.Group>
                          <Form.Label className="age"> E-Mail ID </Form.Label>
                          <Form.Control
                            type="email"
                            placeholder="example@abcd.com"
                            name="email"
                            value={email || ""}
                            onChange={staffHndleChange}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={3}></Col>
                  </Row>
                  <div className="staff-reg-accordion">
                    <Accordion id="reg-accordion">
                      <Accordion.Item eventKey="0" id="custom-staff-item">
                        <Accordion.Header className="additional-staff-head">
                          <span className="additional-staff">
                            Add Additional Details{" "}
                          </span>
                        </Accordion.Header>
                        <Accordion.Body className="additional-staff-body">
                          <Row className="phc-acc-div">
                            <Col lg={6}>
                              <div>
                                <Form.Group>
                                  <Form.Label className="age">
                                    {" "}
                                    Staff Number&nbsp;(ID){" "}
                                  </Form.Label>
                                  <Form.Control
                                    type="number"
                                    placeholder="Enter Number"
                                    maxLength={10}
                                    name="posted_by"
                                    value={posted_by || ""}
                                    onChange={staffHndleChange}
                                  />
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={6}></Col>
                          </Row>
                          <Row className="phc-acc-div">
                            <Col lg={6}>
                              <div>
                                <Form.Group>
                                  <Form.Label className="age">
                                    {" "}
                                    Staff Health ID{" "}
                                  </Form.Label>
                                  <Form.Control
                                    type="tel"
                                    placeholder="Enter ID"
                                    name="healthID"
                                    maxLength={14}
                                    onKeyPress={(e) => {
                                      if (!/[0-9]/.test(e.key)) {
                                        e.preventDefault();
                                      }
                                    }}
                                    value={healthID || ""}
                                    onChange={staffHndleChange}
                                  />
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={6}>
                              <Form.Group className="staff-pan-card">
                                <Form.Label>Staff PAN Number </Form.Label>
                                <Form.Control
                                  type={panType}
                                  placeholder="Enter Staff PAN Number"
                                  id="staff_text"
                                  name="pan"
                                  maxLength={12}
                                  value={(pan && pan.toUpperCase()) || ""}
                                  onChange={staffHndleChange}
                                  required
                                />
                                <a className="button" onClick={handlePanToggle}>
                                  {panText}
                                </a>
                                <span
                                  style={{ color: "red", fontSize: "11px" }}
                                >
                                  {panError && panError}
                                </span>
                              </Form.Group>
                            </Col>
                          </Row>
                          <Row className="phc-acc-div">
                            <Col lg={6}>
                              <div>
                                <Form.Group>
                                  <Form.Label>Citizen ID Type </Form.Label>
                                  <Form.Select
                                    aria-label="Default select example"
                                    placeholder="Select Citizen ID"
                                    name="citizenIdType"
                                    value={citizenIdType || ""}
                                    onChange={staffHndleChange}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {facilityDropDown.map((dropStaff, i) => (
                                      <React.Fragment key={i}>
                                        {dropStaff.groupName ==
                                          "Citizen ID Type" && (
                                          <>
                                            {dropStaff.elements.map(
                                              (drpItem, drpIndex) => (
                                                <option
                                                  value={drpItem.title}
                                                  key={drpIndex}
                                                >
                                                  {drpItem.title}
                                                </option>
                                              )
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={6}>
                              <div>
                                <Form.Group>
                                  <Form.Label className="age">
                                    {" "}
                                    Citizen ID Number{" "}
                                  </Form.Label>
                                  {aadhar == true ? (
                                    <Form.Control
                                      className="form-control"
                                      type="tel"
                                      placeholder={valuePlaceholder}
                                      name="citizenIdNumber"
                                      value={citizenIdNumber || ""}
                                      maxLength={12}
                                      onChange={staffHndleChange}
                                      onKeyPress={(e) => {
                                        if (!/[0-9]/.test(e.key)) {
                                          e.preventDefault();
                                        }
                                      }}
                                    />
                                  ) : voterId == true ? (
                                    <Form.Control
                                      className="form-control"
                                      type="text"
                                      placeholder={valuePlaceholder}
                                      name="citizenIdNumber"
                                      value={
                                        (citizenIdNumber &&
                                          citizenIdNumber.toUpperCase()) ||
                                        ""
                                      }
                                      maxLength={10}
                                      onChange={staffHndleChange}
                                    />
                                  ) : (
                                    ""
                                  )}
                                  <span
                                    style={{ color: "red", fontSize: "11px" }}
                                  >
                                    {voterError && voterError}
                                  </span>
                                </Form.Group>
                              </div>
                            </Col>
                          </Row>
                          <Row className="phc-acc-div">
                            <Col lg={4}>
                              <div>
                                <Form.Group>
                                  <Form.Label>Blood Group</Form.Label>
                                  <Form.Select
                                    aria-label="Default select example"
                                    name="bloodGroup"
                                    value={bloodGroup || ""}
                                    onChange={staffHndleChange}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {facilityDropDown.map((dropStaff, i) => (
                                      <React.Fragment key={i}>
                                        {dropStaff.groupName ==
                                          "Blood Group" && (
                                          <>
                                            {dropStaff.elements.map(
                                              (drpItem, drpIndex) => (
                                                <option
                                                  value={drpItem.title}
                                                  key={drpIndex}
                                                >
                                                  {drpItem.title}
                                                </option>
                                              )
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={4}>
                              <div>
                                <Form.Group>
                                  <Form.Label className="age">
                                    {" "}
                                    Height{" "}
                                  </Form.Label>
                                  <Form.Control
                                    type="number"
                                    placeholder="Enter Height"
                                    name="height"
                                    value={height || ""}
                                    onChange={staffHndleChange}
                                  />
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={4}></Col>
                          </Row>
                          <Row className="phc-acc-div">
                            <Col lg={4}>
                              <div>
                                <Form.Group>
                                  <Form.Label>Caste</Form.Label>
                                  <Form.Select
                                    aria-label="Default select example"
                                    name="caste"
                                    value={caste || ""}
                                    onChange={staffHndleChange}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {facilityDropDown.map((dropStaff, i) => (
                                      <React.Fragment key={i}>
                                        {dropStaff.groupName == "Caste" && (
                                          <>
                                            {dropStaff.elements.map(
                                              (drpItem, drpIndex) => (
                                                <option
                                                  value={drpItem.title}
                                                  key={drpIndex}
                                                >
                                                  {drpItem.title}
                                                </option>
                                              )
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={4}>
                              <div>
                                <Form.Group>
                                  <Form.Label>Religion</Form.Label>
                                  <Form.Select
                                    aria-label="Default select example"
                                    name="religion"
                                    value={religion || ""}
                                    onChange={staffHndleChange}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {facilityDropDown.map((dropStaff, i) => (
                                      <React.Fragment key={i}>
                                        {dropStaff.groupName == "Religion" && (
                                          <>
                                            {dropStaff.elements.map(
                                              (drpItem, drpIndex) => (
                                                <option
                                                  value={drpItem.title}
                                                  key={drpIndex}
                                                >
                                                  {drpItem.title}
                                                </option>
                                              )
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </div>
                            </Col>
                            <Col lg={4}>
                              <div>
                                <Form.Group>
                                  <Form.Label>Nationality</Form.Label>
                                  <Form.Select
                                    aria-label="Default select example"
                                    name="nationality"
                                    value={nationality || ""}
                                    onChange={staffHndleChange}
                                  >
                                    <option value="" disabled hidden>
                                      Select
                                    </option>
                                    {facilityDropDown.map((dropStaff, i) => (
                                      <React.Fragment key={i}>
                                        {dropStaff.groupName ==
                                          "Nationality" && (
                                          <>
                                            {dropStaff.elements.map(
                                              (drpItem, drpIndex) => (
                                                <option
                                                  value={drpItem.title}
                                                  key={drpIndex}
                                                >
                                                  {drpItem.title}
                                                </option>
                                              )
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Form.Select>
                                </Form.Group>
                              </div>
                            </Col>
                          </Row>
                        </Accordion.Body>
                      </Accordion.Item>
                    </Accordion>
                    <div className="staff_Foot">
                      <div className="save-btn-section">
                        {props.idForUpdate ? (
                          <SaveButton
                            class_name="regBtnN"
                            button_name="Update"
                            butttonClick={(e) => updateStaff(props.idForUpdate)}
                          />
                        ) : (
                          <SaveButton
                            class_name="regBtnN"
                            button_name="Save"
                            butttonClick={saveStaff}
                          />
                        )}
                      </div>
                      <div className="save-btn-section">
                        <SaveButton
                          class_name="regBtnPC"
                          button_name="Cancel"
                          butttonClick={closeStaffModal}
                        />
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </Col>
            <Col lg={3} className="login-soft">
              <h5 className="log-soft-access-header">Software Login Access</h5>
              <p className="staff-pass-email">
                This username and password will be used by this Staff to login
                into PHC software application.
              </p>
              <div className="staff-log-pass">
                <div className="staff-cont">
                  <Form.Group>
                    <Form.Label>
                      User Name<span style={{ color: "red" }}>*</span>
                    </Form.Label>
                    <Form.Control
                      type="email"
                      id="USERNAME"
                      placeholder="Enter User Name"
                      name="USERNAME"
                      autoComplete="new-user"
                      value={USERNAME || ""}
                      onChange={setAccessChange}
                      required
                    />
                  </Form.Group>
                </div>
                <div className="staff-cont">
                  <Form.Group>
                    <Form.Label>
                      Password<span style={{ color: "red" }}>*</span>
                    </Form.Label>
                    <Form.Control
                      type={passwordType}
                      id="PASSWORD"
                      placeholder="Enter Password"
                      name="PASSWORD"
                      autoComplete="new-password"
                      value={PASSWORD || ""}
                      onChange={setAccessChange}
                      required
                    />
                    <a className="button" onClick={handleToggle}>
                      {passwordText}
                    </a>
                  </Form.Group>
                </div>
              </div>
            </Col>
          </Row>
        </Form>
      </Modal>
    </React.Fragment>
  );
}
