import React, { useEffect, useState, useRef } from "react";
import {
  Col,
  Row,
  Form,
  Accordion,
  Button,
  Tab,
  Nav,
  Modal,
  Table,
} from "react-bootstrap";
import styled from "styled-components";
import "../../../css/ModalPopUp.css";
import "../Services/services.css";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import ReferanceRange from "./ReferanceRange";
import { Multiselect } from "multiselect-react-dropdown";
import { useDispatch, useSelector } from "react-redux";
import { loadLabDropdown } from "../../../redux/formUtilityAction";
import Multi from "../../Multi";
import SaveButton from "../../EMR_Buttons/SaveButton";

function AddLabTest(props) {
  const form = useRef(null);
  let dispatch = useDispatch();
  const { labDropdown } = useSelector((state) => state.formData);
  console.log("it is lab", labDropdown);

  const [status, setStatus] = useState("");
  const [testType, setTestType] = useState("");
  const [testcode, setTestCode] = useState("");
  const [testName, setTestName] = useState("");
  const [unitOfMeasurement, setUnitOfMeasurement] = useState("");
  const [testgroup, setTestGroup] = useState("");
  const [sampleType, setSampleType] = useState("");
  const [sampleSnomedCode, setSampleSnomedCode] = useState("");
  const [testMethod, setTestMethod] = useState("");
  const [turnAroundTime, setTurnAroundTime] = useState("");
  const [turnAroundUnit, setTurnAroundUnit] = useState("");
  const [testValue, setTestValue] = useState("");
  // const [outsourcedOrg, setOutsourcedOrg] = useState('');
  const [resultFormat, setResultFormat] = useState("Numerical");
  const [resulttype, setResultType] = useState("Single");

  const objectArray = [
    {
      id: 240822009,
      key: "Hospital name",
    },
    {
      id: 704425001,
      key: "Hospital name 1",
    },
    {
      id: 135883003,
      key: "Hospital name 2",
    },
    {
      id: 426000000,
      key: "Hospital name 3",
    },
  ];

  const [selected, setSelected] = useState([]);

  const [items, setItems] = useState([]);
  const isValid = items != null && items.length > 0; // For validation
  let value = [];

  for (var i = 0; i < items.length; i++) {
    value.push(items[i].key);
  }

  const outsourcedValue = String(value);

  const handleSelect = (selectedList) => {
    setItems(selectedList);
  };

  const handleRemove = (selectedList) => {
    setItems(selectedList);
  };
  const deleteSearchItem = (e) => {
    setItems((current) =>
      current.filter((items) => {
        return items.id !== e;
      })
    );
  };

  const [hide, setHide] = useState(false);

  // set state for checkbox toggle
  const [checkValue1, setCheckbox] = useState(true);
  // const [statusValue, setStatusValue] = useState('');
  let statusValue;
  if (checkValue1 === true) {
    statusValue = "Active";
  }
  if (checkValue1 === false) {
    statusValue = "InActive";
  }

  const [checkValue2, setValue] = useState(false);

  const [checked, setChecked] = useState([]);
  // Generate string of checked items
  const checkedItems = checked.length
    ? checked.reduce((total, item) => {
        return total + ", " + item;
      })
    : "";

  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  // set valuse for Profile buttons
  const [profilevalue, setProfilevalue] = useState([]);

  // fetching all lab test data
  useEffect(() => {
    dispatch(loadLabDropdown());
    fetch(
      `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?page=&size=1000000`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        console.log("allnomad", res);
        // const obj = [...new Map(res['data'].map(item => [JSON.stringify(item.name, item.group), item])).values()];
        let subTestArray = [];
        for (var i = 0; i < res["data"].length; i++) {
          if (
            res["data"][i].testType == "Sub test" &&
            res["data"][i].status == "Active"
          ) {
            subTestArray.push(res["data"][i]);
          }
        }
        setProfilevalue([...profilevalue, ...subTestArray]);
        setStatus(false);
      });
  }, [status]);
  // fetching all lab test data
  console.log("i am profilrvalur", profilevalue);
  // set valuse for check boxes
  const [cadiacVascularData, setCadiacVascularData] = useState([]);
  const [filteredResult, setFilteredResults] = useState([]);
  let filteredResults = [
    ...new Map(
      filteredResult.map((item) => [JSON.stringify(item.name), item])
    ).values(),
  ];

  const setCardiacValue = (e, testId, testName) => {
    if (e.target.checked) {
      setCadiacVascularData([
        ...cadiacVascularData,
        { testId: testId, testName: testName },
      ]);
    } else {
      setCadiacVascularData(
        cadiacVascularData.filter((id) => id.testName !== e.target.value)
      );
    }
  };

  const deleteItem = (e) => {
    setCadiacVascularData(cadiacVascularData.filter((id) => id !== e));
    var ele = document.getElementsByName("cariac");
    for (var i = 0; i < ele.length; i++) {
      for (var j = 0; j < cadiacVascularData.length; j++) {
        if (
          ele[i].type == "checkbox" &&
          cadiacVascularData[j].testName.includes(filteredResults[i].name)
        ) {
          ele[i].checked = true;
        } else {
          ele[i].checked = false;
        }
      }
    }
  };

  const [isLoading1, setLoading1] = useState(false);
  const [isLoading2, setLoading2] = useState(false);
  const [isNewLoading, setNewLoading] = useState(false);

  const [labresTestId, setLabTestId] = useState("");
  const [refLink, setRefLink] = useState(false);
  const [loaderStatus, setLoaderStatus] = useState(false);

  let postTestData;
  console.log("hii", sampleSnomedCode);
  console.log("hii", sampleType);
  if (turnAroundTime) {
    postTestData = {
      code: testcode,
      name: testName,
      testType: testType,
      unitOfMeasurement: unitOfMeasurement,
      group: testgroup,
      sampleType: sampleType,
      sampleSnomedCode: sampleSnomedCode,
      testMethod: testMethod,
      resultType: resulttype,
      turnAroundTime: turnAroundTime,
      turnAroundUnit: turnAroundUnit,
      testValue: testValue,
      subTests: cadiacVascularData,
      outSourced: checkValue2,
      outsourcedOrg: outsourcedValue,
      status: statusValue,
      resultFormat: resultFormat,
    };
  } else {
    postTestData = {
      code: testcode,
      name: testName,
      testType: testType,
      unitOfMeasurement: unitOfMeasurement,
      group: testgroup,
      sampleType: sampleType,
      sampleSnomedCode: sampleSnomedCode,
      testMethod: testMethod,
      resultType: resulttype,
      testValue: testValue,
      subTests: cadiacVascularData,
      outSourced: checkValue2,
      outsourcedOrg: outsourcedValue,
      status: statusValue,
      resultFormat: resultFormat,
    };
  }

  const handleSampleTypeChange = (e) => {
    const selectedSampleType = e.target.value;

    // Find the selected sample item in labDropdown
    const selectedSampleItem = labDropdown.find((item) => {
      return (
        item.groupName === "Sample Type" && item.title === selectedSampleType
      );
    });

    // Update the sampleSnomedCode if a matching item is found
    if (selectedSampleItem) {
      setSampleSnomedCode(selectedSampleItem.sampleSnomedCode || "");
    } else {
      setSampleSnomedCode("");
    }

    setSampleType(selectedSampleType);
  };

  function saveLabTest() {
    let isValid;
    if (turnAroundTime) {
      isValid =
        !testcode ||
        !testName ||
        !testType ||
        !unitOfMeasurement ||
        !testgroup ||
        !sampleType ||
        !resulttype ||
        !resultFormat ||
        !turnAroundTime ||
        !turnAroundUnit;
    } else {
      isValid =
        !testcode ||
        !testName ||
        !testType ||
        !unitOfMeasurement ||
        !testgroup ||
        !sampleType ||
        !resulttype ||
        !resultFormat;
    }

    if (isValid) {
      alert("Please fill All Required fields");
    } else {
      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(postTestData),
      };

      fetch(`${constant.ApiUrl}/lab-tests-svc/labtestservices`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          console.log("from labtestservices", res);
          try {
            setLoaderStatus(false);
            setTestType("");
            setTestCode("");
            setTestName("");
            setUnitOfMeasurement("");
            setTestGroup("");
            setSampleType("");
            setSampleSnomedCode("");
            setTestMethod("");
            setTurnAroundTime("");
            setTurnAroundUnit("");
            setTestValue("");
            setResultFormat("Numerical");
            setResultType("Single");
            setCheckbox(true);
            setValue(false);
            props.addlabServiceClose();
            setLoading1(true);
            setTimeout(() => {
              setLoading2(true);
              setTimeout(() => {
                setNewLoading(true);
                setTimeout(() => {
                  setLoading1(false);
                  setLoading2(false);
                }, 2500);
                setLoading2(false);
              }, 2500);
              setLoading1(false);
            }, 2500);
            setLabTestId(res.id);
            if (
              res.resultFormat == "Single Line" ||
              res.resultFormat == "Multi Line"
            ) {
              setRefLink(true);
            }
            setStatus(true);
          } catch (error) {}
        });
    }
  }

  let labTestId = props.labId;
  useEffect(() => {
    if (labTestId != undefined && labTestId != "") {
      fetch(
        `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          console.log("labtestservices", res);
          let problem = [];
          setTestCode(res["code"]);
          setTestGroup(res["group"]);
          setTestName(res["name"]);
          setValue(res["outSourced"]);
          // setOutsourcedOrg(res['outsourcedOrg'])
          problem.push(res["outsourcedOrg"].split(","));
          setCadiacVascularData(res["subTests"]);
          setResultFormat(res["resultFormat"]);
          setResultType(res["resultType"]);
          setSampleType(res["sampleType"]);
          setSampleSnomedCode(["sampleSnomedCode"]);
          setTestMethod(res["testMethod"]);
          setTestType(res["testType"]);
          setTestValue(res["testValue"]);
          setTurnAroundTime(res["turnAroundTime"]);
          setTurnAroundUnit(res["turnAroundUnit"]);
          setUnitOfMeasurement(res["unitOfMeasurement"]);
          if (res["status"] == "Active") {
            setCheckbox(true);
          } else {
            setCheckbox(false);
          }

          let itemsGet = [];
          for (var i = 0; i < objectArray.length; i++) {
            for (var j = 0; j < problem.length; j++) {
              if (problem[j].includes(objectArray[i].key)) {
                itemsGet.push(objectArray[i]);
              }
            }
          }

          setItems(itemsGet);
          setStatus(false);
        });
    }
  }, [labTestId, status]);
  // fetching lab test data for edit

  // Update Lab tests
  function updateLabTest() {
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(postTestData),
    };

    fetch(
      `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.error) {
          alert("Test is not submitted..!");
        } else {
          try {
            setLoaderStatus(true);
            setTestType("");
            setTestCode("");
            setTestName("");
            setUnitOfMeasurement("");
            setTestGroup("");
            setSampleType("");
            setSampleSnomedCode("");
            setTestMethod("");
            setTurnAroundTime("");
            setTurnAroundUnit("");
            setTestValue("");
            setResultFormat("Numerical");
            setResultType("Single");
            setCheckbox(true);
            setValue(false);
            props.addlabServiceClose();
            setLoading1(true);
            setTimeout(() => {
              setLoading2(true);
              setTimeout(() => {
                setNewLoading(true);
                setTimeout(() => {
                  setLoading1(false);
                  setLoading2(false);
                }, 2500);
                setLoading2(false);
              }, 2500);
              setLoading1(false);
            }, 2500);
            setLabTestId(res.id);
            if (
              res.resultFormat == "Single Line" ||
              res.resultFormat == "Multi Line"
            ) {
              setRefLink(true);
            }
            setStatus(false);
          } catch (error) {}
        }
      });
  }
  // Update Lab tests

  {
    /* Add CBC Modal */
  }
  const [referanceModal, setReferanceModal] = useState(false);
  const closeReferenceModal = () => {
    setTestType("");
    setTestCode("");
    setTestName("");
    setUnitOfMeasurement("");
    setTestGroup("");
    setSampleType("");
    setSampleSnomedCode("");
    setTestMethod("");
    setTurnAroundTime("");
    setTurnAroundUnit("");
    setTestValue("");
    setResultFormat("Numerical");
    setResultType("Single");
    setCheckbox(true);
    setValue(false);
    setReferanceModal(false);
  };

  const [labId, setLabId] = useState("");
  const openReferenceModal = (e) => {
    setLabId(e);
    setReferanceModal(true);
    setNewLoading(false);
  };
  {
    /* Add CBC Modal */
  }

  const [searchValue, setSearchValue] = useState("");

  const searchItems = (e) => {
    // setSearchInput(searchValue)
    setSearchValue(e.target.value);
    const searchKeyWord = e.target.value;
    if (searchKeyWord !== "" && searchKeyWord.length >= 3) {
      const filteredData = profilevalue.filter((item) => {
        return item.name.toLowerCase().startsWith(searchKeyWord.toLowerCase());
      });
      setFilteredResults(filteredData);
    } else {
      setFilteredResults([]);
    }
  };

  function onModalClose(event) {
    setTestType("");
    setTestCode("");
    setTestName("");
    setUnitOfMeasurement("");
    setTestGroup("");
    setSampleType("");
    setSampleSnomedCode("");
    setTestMethod("");
    setTurnAroundTime("");
    setTurnAroundUnit("");
    setTestValue("");
    setResultFormat("Numerical");
    setResultType("Single");
    setCheckbox(true);
    setValue(false);
    labTestId = "";
    props.addlabServiceClose(event, labTestId);
  }

  const closeLoader = () => {
    setNewLoading(false);
    setRefLink(false);
  };

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling
  return (
    <div>
      <ReferanceRange
        referanceModal={referanceModal}
        closeReferenceModal={closeReferenceModal}
        labId={labId}
      />
      {/* loader */}
      {isLoading1 && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <h3 className="load-header" style={{ fontSize: "20px" }}>
                {loaderStatus == false
                  ? "Configuring Lab Test"
                  : "Updating Lab Test"}
              </h3>
            </div>
            <br />
            <br />
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="../img/circle.png"
              />
            </div>
            <br />
            <div>
              <p className="">
                {loaderStatus == false
                  ? "Please wait while we add new lab test into our records."
                  : "Please wait while we update exisisting lab test into our records."}
              </p>
            </div>
          </div>
        </div>
      )}
      {isLoading2 && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <h3 className="load-header" style={{ fontSize: "20px" }}>
                {loaderStatus == false
                  ? "Adding Lab Test"
                  : "Updating Lab Test"}
              </h3>
              <br />
            </div>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className=""
                src="../img/circle2.png"
              />
            </div>
            <br />
            <div>
              <p className="">
                {loaderStatus == false
                  ? "Please wait while we add new lab test into our records."
                  : "Please wait while we update existing lab test into our records."}
              </p>
            </div>
          </div>
        </div>
      )}
      {isNewLoading && (
        <div className="loder-container" align="center">
          <div className="loadBox1" align="center">
            <div>
              <p className="">
                {loaderStatus == false
                  ? "Lab Test Added Successfully"
                  : "Lab Test Updated Successfully"}
              </p>
            </div>
            <div>
              <img
                style={{ width: "80px", height: "80px" }}
                className="load-img3"
                src="../img/success-icon.png"
              />
            </div>
            <div>
              <hr className="hr" />
            </div>
            <div>
              <p className="load-text3">
                {loaderStatus == false
                  ? "Please go ahead and add reference range for newly added test."
                  : "Please go ahead and add reference range for existing updated test."}
              </p>
            </div>
            <div>
              <div className="save-btn-section">
                {refLink == true ? (
                  <SaveButton
                    butttonClick={closeLoader}
                    class_name="regBtnN"
                    button_name="Ok"
                  />
                ) : (
                  <SaveButton
                    butttonClick={() => openReferenceModal(labresTestId)}
                    class_name="regBtnN"
                    button_name="Add Reference Range"
                  />
                )}
              </div>
            </div>
            <div>
              {refLink == false && (
                <a
                  className="ancher-Text"
                  style={{ cursor: "pointer" }}
                  onClick={closeLoader}
                >
                  I will do it later
                </a>
              )}
            </div>
          </div>
        </div>
      )}
      {/* loader */}
      <Modal
        show={props.addlabService}
        onHide={props.addlabServiceClose}
        className="lab-service-modal"
      >
        <div className="">
          {/* <h5 className="vital-header">Vital Signs</h5> */}
          <div>
            <div className="add-service-form-col">
              <Row>
                <Col md={6} xs={9}>
                  <h1 className="dia-heading">
                    {labTestId ? "Updating Lab Test" : "Configure Lab Test"}
                  </h1>
                </Col>
                <Col md={6} xs={3} align="right">
                  <button
                    onClick={(e) => onModalClose(e)}
                    className="bi bi-x close-popup"
                  ></button>
                </Col>
              </Row>
              <Form className="add-lab-form" ref={form}>
                <div className="form-fields">
                  <Row className="allergy-information-div">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Test type <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={testType}
                            name="testType"
                            onChange={(e) => setTestType(e.target.value)}
                          >
                            <option value="" disabled hidden>
                              Select Test type
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Test Type" && (
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
                    <Col md={3}></Col>
                    <Col md={3}></Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Test Status{" "}
                          </Form.Label>
                          <br />
                          <div
                            className={`checkbox ${
                              checkValue1 && "checkbox--on"
                            }`}
                            name="status"
                            value={checkValue1}
                            onClick={() => setCheckbox(!checkValue1)}
                          >
                            <div className="checkbox__ball">
                              <span className="status-text">
                                {checkValue1 == false ? "Inactive" : "Active"}
                              </span>
                            </div>
                          </div>
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="allergy-information-div mt-3">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Test code <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter test code"
                            value={testcode}
                            name="code"
                            onChange={(e) => setTestCode(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Test Name <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter test name"
                            value={testName}
                            name="name"
                            onChange={(e) => setTestName(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Unit of Measurement{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={unitOfMeasurement}
                            name="unitOfMeasurement"
                            onChange={(e) =>
                              setUnitOfMeasurement(e.target.value)
                            }
                          >
                            <option value="" disabled hidden>
                              Select Unit of Measurement
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName ==
                                  "Units of Measurement" && (
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
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Test Group <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={testgroup}
                            name="testgroup"
                            onChange={(e) => setTestGroup(e.target.value)}
                          >
                            <option value="" disabled hidden>
                              Select group test
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Test Group" && (
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
                  <Row className="allergy-information-div mt-3">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Sample Type <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={sampleType}
                            name="sampleType"
                            // onChange={(e) => setSampleType(e.target.value)}
                            onChange={handleSampleTypeChange}
                          >
                            <option value="" disabled hidden>
                              Select sample type
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Sample Type" && (
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
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>Test Method</Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={testMethod}
                            name="testMethod"
                            onChange={(e) => setTestMethod(e.target.value)}
                          >
                            <option value="" disabled hidden>
                              Select test method
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Test Method" && (
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
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>Turn Around Time</Form.Label>
                          <Form.Control
                            type="number"
                            placeholder="Enter value Turn Around Time"
                            value={turnAroundTime}
                            name="turnAroundTime"
                            onChange={(e) => setTurnAroundTime(e.target.value)}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>Turn Around Unit</Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={turnAroundUnit}
                            name="turnAroundUnit"
                            onChange={(e) => setTurnAroundUnit(e.target.value)}
                          >
                            <option value="" disabled hidden>
                              Select unit for turn around
                            </option>
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Turn around Unit" && (
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
                  <Row className="allergy-information-div mt-3">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>Test Value</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter test value"
                            value={testValue}
                            name="testValue"
                            onChange={(e) => setTestValue(e.target.value)}
                          />
                          <span style={{ fontSize: "11px" }}>
                            E.g: Present|Absent
                          </span>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={3}>
                      <div className="col-container">
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Label className="require">
                            Outsourced{" "}
                          </Form.Label>
                          <br />
                          <div
                            className={`checkbox ${
                              checkValue2 && "checkbox--on"
                            }`}
                            onClick={() => setValue(!checkValue2)}
                          >
                            <div className="checkbox__ball">
                              <span className="status-text">
                                {checkValue2 == false ? "No" : "Yes"}
                              </span>
                            </div>
                          </div>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col md={6} className="multi-organization">
                      {checkValue2 == true && (
                        <div>
                          <Form.Group>
                            <Form.Label>Outsource Organisation</Form.Label>
                            <Multiselect
                              options={objectArray} // Options to display in the dropdown
                              selectedValues={items} // Preselected value to persist in dropdown
                              onSelect={handleSelect} // Function will trigger on select event
                              onRemove={handleRemove} // Function will trigger on remove event
                              showCheckbox={true}
                              displayValue="key" // Property name to display in the dropdown options
                            />

                            {/* <Multi setItems={setItems} objectArray={objectArray} /> */}
                          </Form.Group>
                        </div>
                      )}
                    </Col>
                  </Row>
                  <Row className="allergy-information-div mt-3 mb-2">
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Result Format{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={resultFormat}
                            name="resultFormat"
                            onChange={(e) => setResultFormat(e.target.value)}
                          >
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Result Format" && (
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
                    <Col md={3}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Result Type <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select category"
                            value={resulttype}
                            onChange={(e) => setResultType(e.target.value)}
                          >
                            {labDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Result Type" && (
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
                  {resulttype == "Profile" ? (
                    <>
                      <Row className="allergy-information-div mt-3 mb-2">
                        <Col md={8}>
                          <div>
                            <Form.Group>
                              <Form.Label>
                                Are you selected result type as Profile, please
                                select the sub tests to include
                              </Form.Label>

                              <div className="form-group sub-lab-search">
                                <input
                                  type="text"
                                  className="form-control"
                                  placeholder="Search sub tests"
                                  onChange={searchItems}
                                />
                                <span className="fa fa-search form-control-feedback"></span>
                              </div>
                            </Form.Group>
                          </div>
                        </Col>
                      </Row>
                      <div className="allergy-information-div mt-3 mb-2">
                        <div className="assessment-buttons">
                          <div>
                            {searchValue == "" ? (
                              <h1>Search And Select the Tests</h1>
                            ) : (
                              <>
                                {filteredResults &&
                                filteredResults.length > 0 ? (
                                  filteredResults.map((profiles, i) => (
                                    <React.Fragment key={i}>
                                      <input
                                        className="checkbox-tools"
                                        type="checkbox"
                                        name="cariac"
                                        id={profiles.id}
                                        value={profiles.name}
                                        onChange={(e) =>
                                          setCardiacValue(
                                            e,
                                            profiles.id,
                                            profiles.name
                                          )
                                        }
                                      />
                                      <label
                                        className="for-checkbox-tools"
                                        htmlFor={profiles.id}
                                      >
                                        <span className={isChecked(profiles)}>
                                          {profiles.name}
                                        </span>
                                      </label>
                                    </React.Fragment>
                                  ))
                                ) : (
                                  <h1>No results found!</h1>
                                )}
                              </>
                            )}
                          </div>
                        </div>
                      </div>
                      <div>
                        {cadiacVascularData.length != 0 && (
                          <h1>Selected Sub Tests</h1>
                        )}
                        {cadiacVascularData.map((subTest, i) => (
                          <div key={i}>
                            <p>
                              {subTest.testName} &nbsp;&nbsp;&nbsp;&nbsp;
                              <span onClick={(e) => deleteItem(subTest)}>
                                X
                              </span>
                            </p>
                          </div>
                        ))}
                      </div>
                    </>
                  ) : (
                    ""
                  )}
                  <div className="vitalsubmit align-me2" align="center">
                    <div className="save-btn-section">
                      <SaveButton
                        butttonClick={(e) => onModalClose(e)}
                        class_name="regBtnPC"
                        button_name="Cancel"
                      />
                    </div>
                    <div className="save-btn-section">
                      {labTestId ? (
                        <SaveButton
                          butttonClick={updateLabTest}
                          class_name="regBtnN"
                          button_name="Update"
                        />
                      ) : (
                        <SaveButton
                          butttonClick={saveLabTest}
                          class_name="regBtnN"
                          button_name="Save"
                        />
                      )}
                    </div>
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default AddLabTest;
