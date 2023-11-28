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
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import { useDispatch, useSelector } from "react-redux";
import { loadRefernceDropdown } from "../../../redux/formUtilityAction";
import SaveButton from "../../EMR_Buttons/SaveButton";

const ReferanceRange = (props) => {
  const form = useRef(null);
  let dispatch = useDispatch();
  const { refrenceDropdown } = useSelector((state) => state.formData);
  // const [refGetData, setRefGetData] = useState([])
  const [inputList, setInputList] = useState([
    {
      gender: "",
      period: "",
      fromAge: "",
      toAge: "",
      referenceIndicator: "",
      minValue: "",
      maxValue: "",
      description: "",
    },
  ]);
  const [reference, setReference] = useState([
    {
      gender: "",
      period: "",
      fromAge: "",
      toAge: "",
      referenceIndicator: "",
      minValue: "",
      maxValue: "",
      description: "",
    },
  ]);

  const [updatingId, setUpdatingId] = useState("");
  // handle click event of the Add button

  const handleAddClick = () => {
    setInputList([
      ...inputList,
      {
        gender: "",
        period: "",
        fromAge: "",
        toAge: "",
        referenceIndicator: "",
        minValue: "",
        maxValue: "",
        description: "",
      },
    ]);
  };

  var handleRemoveClick;
  if (updatingId != undefined && updatingId != "") {
    handleRemoveClick = (index) => {
      const list = [...reference];
      list.splice(index, 1);
      setReference(list);
    };
  } else {
    handleRemoveClick = (index) => {
      const list = [...inputList];
      list.splice(index, 1);
      setInputList(list);
    };
  }

  var handleInputChange;
  if (updatingId != undefined && updatingId != "") {
    handleInputChange = (e, index) => {
      const { name, value } = e.target;
      const list = [...reference];
      list[index][name] = value;
      setReference(list);
    };
  } else {
    handleInputChange = (e, index) => {
      const { name, value } = e.target;
      const list = [...inputList];
      list[index][name] = value;
      setInputList(list);
    };
  }

  const formRemoveClick = (index) => {
    const list = [...inputList];
    list.splice(index);
    setInputList(list);
  };

  // cancel button

  {
    /* cbc modal */
  }

  {
    /* Add CBC Modal */
  }
  const [showSuccessModal, setSuccessShowModal] = useState(false);
  const [moadlTag, setMoadlTag] = useState(false);
  const closeSuccessModal = () => {
    setSuccessShowModal(false);
    setMoadlTag(false);
  };
  const openSucessModal = () => {
    setSuccessShowModal(true);
  };
  {
    /* Add CBC Modal */
  }

  // setting value to pass
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
  const [outsourcedOrg, setOutsourcedOrg] = useState("");
  const [resultFormat, setResultFormat] = useState("");
  const [resulttype, setResultType] = useState("Single");
  const [checkValue1, setCheckbox] = useState(false);
  const [checkValue2, setValue] = useState(false);

  // setting value to pass

  let referenceArray = [];
  if (reference.length != 0) {
    referenceArray = [...reference, ...inputList];
  } else {
    referenceArray = [...inputList];
  }

  // fetching lab test data for edit
  let labTestId = props.labId;
  useEffect(() => {
    dispatch(loadRefernceDropdown());
    if (labTestId != undefined && labTestId != "") {
      fetch(
        `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setTestCode(res["code"]);
          setTestGroup(res["group"]);
          setTestName(res["name"]);
          setOutsourcedOrg(res["outsourcedOrg"]);
          setResultFormat(res["resultFormat"]);
          setResultType(res["resultType"]);
          setSampleType(res["sampleType"]);
          setSampleSnomedCode(res["sampleSnomedCode"]);
          setTestMethod(res["testMethod"]);
          setTestType(res["testType"]);
          setTestValue(res["testValue"]);
          setTurnAroundTime(res["turnAroundTime"]);
          setTurnAroundUnit(res["turnAroundUnit"]);
          setUnitOfMeasurement(res["unitOfMeasurement"]);
          setCheckbox(res["status"]);
          let refArray = [];
          if (res.length != 0 && res["referenceRanges"]) {
            for (var i = 0; i < res["referenceRanges"].length; i++) {
              refArray.push(res["referenceRanges"][i]);
            }
          }
          setReference(refArray);
          setStatus(false);
        });
    }
  }, [labTestId, status]);
  // fetching lab test data for edit

  // Update Lab tests
  // const [closeModal, setCloseModal] = useState("");
  function SaveReferance() {
    for (var i = 0; i < inputList.length; i++) {
      let validate;
      if (inputList[i].referenceIndicator == "BETWEEN") {
        validate =
          !inputList[i].gender ||
          !inputList[i].period ||
          !inputList[i].fromAge ||
          !inputList[i].toAge ||
          !inputList[i].referenceIndicator ||
          !inputList[i].minValue ||
          !inputList[i].maxValue;
      } else {
        validate =
          !inputList[i].gender ||
          !inputList[i].period ||
          !inputList[i].fromAge ||
          !inputList[i].toAge ||
          !inputList[i].referenceIndicator ||
          !inputList[i].minValue;
      }

      if (validate) {
        alert("Please fill required fields");
      } else {
        const sendReferenceData = {
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
          resultFormat: resultFormat,
          testValue: testValue,
          outSourced: checkValue2,
          outsourcedOrg: outsourcedOrg,
          referenceRanges: referenceArray,
          status: checkValue1,
        };

        var requestOptions1 = {
          headers: serviceHeaders.myHeaders1,
          method: "PATCH",
          mode: "cors",
          // "Access-Control-Allow-Origin": '*',
          body: JSON.stringify(sendReferenceData),
        };

        fetch(
          `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
          requestOptions1
        )
          .then((res) => res.json())
          .then((res) => {
            setStatus(true);

            try {
              setSuccessShowModal(true);
            } catch (error) {}
          });
        form.current.reset();
        if (inputList.length > 1) {
          formRemoveClick(true);
        }
        setInputList([
          {
            name: "",
            strength: "",
            route: "",
            frequency: "",
            duration: "",
            instruction: "",
          },
        ]);
      }
    }
  }
  // save refrence

  // update reference
  function updateReferance() {
    const sendReferenceData = {
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
      resultFormat: resultFormat,
      testValue: testValue,
      outSourced: checkValue2,
      outsourcedOrg: outsourcedOrg,
      referenceRanges: reference,
      status: checkValue1,
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      // "Access-Control-Allow-Origin": '*',
      body: JSON.stringify(sendReferenceData),
    };

    fetch(
      `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        try {
          setMoadlTag(true);
          // setCloseModal(props.closeReferenceModal);
          setSuccessShowModal(true);
        } catch (error) {}
      });
    form.current.reset();
    setInputList([
      {
        name: "",
        strength: "",
        route: "",
        frequency: "",
        duration: "",
        instruction: "",
      },
    ]);
    if (inputList.length > 1) {
      formRemoveClick(true);
    }
    setUpdatingId("");
  }
  // update reference

  // Update Lab tests
  const cancelFields = () => {
    if (updatingId != undefined && updatingId != "") {
      setUpdatingId("");
      form.current.reset();
      if (inputList.length > 1) {
        // handleRemoveClick(true);
        formRemoveClick(true);
      }
      setInputList([
        {
          name: "",
          strength: "",
          route: "",
          frequency: "",
          duration: "",
          instruction: "",
        },
      ]);
    } else {
      form.current.reset();
      if (inputList.length > 1) {
        // handleRemoveClick(true);
        formRemoveClick(true);
      }
      setInputList([
        {
          name: "",
          strength: "",
          route: "",
          frequency: "",
          duration: "",
          instruction: "",
        },
      ]);
    }
  };

  // Get Lab test by test ID

  const updateButton = (e) => {
    setUpdatingId(e);

    if (labTestId != undefined && labTestId != "") {
      fetch(
        `${constant.ApiUrl}/lab-tests-svc/labtestservices/${labTestId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let refArray = [];
          if (res["referenceRanges"].length != 0) {
            for (var i = 0; i < res["referenceRanges"].length; i++) {
              refArray.push(res["referenceRanges"][i]);
            }
          }
          setReference(refArray);
          setStatus(false);
        });
    }
  };
  // Get Lab test by test ID
  const clearId = () => {
    labTestId = "";
    setReference([]);
  };

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  return (
    <div>
      {/* cbc modal */}
      <Modal
        show={showSuccessModal}
        onHide={closeSuccessModal}
        className="alergy-molal"
      >
        <Row>
          <Col xsm={2} sm={2} md={2} className="success-icon" align="center">
            <i className="bi bi-check2 alergy-check2"></i>
          </Col>
          <Col xsm={10} sm={10} md={10} className="allergy-div-box">
            <Row className="test-div">
              <Col xsm={11} sm={11} md={11}>
                {moadlTag ? (
                  <p className="success-text">
                    Reference range Updated successfully.
                  </p>
                ) : (
                  <p className="success-text">
                    Reference range saved successfully.
                  </p>
                )}
              </Col>
              <Col xsm={1} sm={1} md={1} align="center">
                <button
                  type="button"
                  className="btn-close modelCls"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  id="btn"
                  onClick={closeSuccessModal}
                ></button>
              </Col>
            </Row>
          </Col>
        </Row>
      </Modal>
      <Modal
        show={props.referanceModal}
        onHide={props.closeReferenceModal}
        animation={false}
        className="referance-modal-div"
      >
        <Row className="reference-head">
          <Col xsm={11} sm={11} md={11}>
            <b id="modal-title" className="blood-admin">
              Reference Range
            </b>
          </Col>
          <Col
            xsm={1}
            sm={1}
            md={1}
            align="center"
            onClick={props.closeReferenceModal}
          >
            <button
              type="button"
              className="btn-close modelCls"
              data-bs-dismiss="modal"
              aria-label="Close"
              id="btn"
              onClick={clearId}
            ></button>
          </Col>
        </Row>
        <Row>
          <Col md={2}></Col>
          <Col md={8}>
            <div className="blood-count">
              <h2 className="cbc-head">{testName}</h2>
              <Row className="cbc-row">
                <Col md={6}>Code: {testcode}</Col>
                <Col md={6}>UOM: {unitOfMeasurement}</Col>
                <Col md={6}>Group: {testgroup}</Col>
                <Col md={6}>Result type: {resulttype}</Col>
              </Row>
            </div>
          </Col>
          <Col md={2}></Col>
        </Row>
        <Form ref={form}>
          <Table className="cbc-table">
            <thead>
              <tr>
                <th className="cbc-table-head">Gender</th>
                <th className="cbc-table-head">Period</th>
                <th className="cbc-table-head">From Age</th>
                <th className="cbc-table-head">To Age</th>
                <th className="cbc-table-head">
                  Reference <br /> indication
                </th>
                <th className="cbc-table-head">
                  Minimum <br /> value
                </th>
                <th className="cbc-table-head">
                  Maxium <br /> value
                </th>
                <th className="cbc-table-head">Description</th>
                <th className="cbc-table-head" colSpan={2}>
                  Action
                </th>
              </tr>
            </thead>
            <tbody>
              {updatingId ? null : (
                <>
                  {reference &&
                    reference.map((ref, i) => (
                      <React.Fragment key={i}>
                        <tr key={i}>
                          <td className="cbc-table-body">{ref.gender}</td>
                          <td className="cbc-table-body">{ref.period}</td>
                          <td className="cbc-table-body">{ref.fromAge}</td>
                          <td className="cbc-table-body">{ref.toAge}</td>
                          <td className="cbc-table-body">
                            {ref.referenceIndicator}
                          </td>
                          <td className="cbc-table-body">{ref.minValue}</td>
                          <td className="cbc-table-body">{ref.maxValue}</td>
                          <td className="cbc-table-body">{ref.description}</td>
                          <td className="cbc-table-body">
                            <div className="myact">
                              <Button
                                variant="link"
                                className="btnact"
                                onClick={(e) => updateButton(labTestId)}
                              >
                                <i className="bi bi-pencil"></i>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      </React.Fragment>
                    ))}
                </>
              )}

              {!updatingId ? (
                <>
                  {inputList.map((x, i) => (
                    // return (
                    <tr key={i}>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.gender || ""}
                            className="pre-drop"
                            name="gender"
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option value="" hidden>
                              Select
                            </option>
                            {refrenceDropdown.map((formItem, i) => (
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
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.period || ""}
                            className="pre-drop"
                            name="period"
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option value="" hidden>
                              Select
                            </option>
                            {refrenceDropdown.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Period" && (
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
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Age"
                            value={x.fromAge || ""}
                            name="fromAge"
                            //onChange={(event) => setFromage(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                            type="number"
                            maxLength={3}
                            onKeyPress={(event) => {
                              if (!/[0-9]/.test(event.key)) {
                                event.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Age"
                            value={x.toAge || ""}
                            name="toAge"
                            //onChange={(event) => setToage(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                            type="number"
                            maxLength={3}
                            onKeyPress={(event) => {
                              if (!/[0-9]/.test(event.key)) {
                                event.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.referenceIndicator || ""}
                            className="pre-drop"
                            name="referenceIndicator"
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option hidden>Select</option>
                            <option value="EQUALS">=</option>
                            <option value="GREATERTHAN"> {">"} </option>
                            <option value="LESSERTHAN"> {"<"} </option>
                            <option value="GREATERTHANEQUALTO"> {">="} </option>
                            <option value="LESSERTHANEQUALTO"> {"<="} </option>
                            <option value="BETWEEN"> Between </option>
                          </Form.Select>
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Value"
                            value={x.minValue || ""}
                            name="minValue"
                            onChange={(e) => handleInputChange(e, i)}
                            type="number"
                            maxLength={3}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        {!x.referenceIndicator ||
                        x.referenceIndicator == "BETWEEN" ? (
                          <Form.Group className="mb-3_drugname">
                            <Form.Control
                              className="presription-input"
                              placeholder="Enter Value"
                              value={x.maxValue || ""}
                              name="maxValue"
                              onChange={(e) => handleInputChange(e, i)}
                              type="number"
                              maxLength={3}
                            />
                          </Form.Group>
                        ) : (
                          <Form.Group className="mb-3_drugname">
                            <Form.Control
                              className="presription-input"
                              placeholder="Enter Value"
                              value={x.maxValue || ""}
                              name="maxValue"
                              disabled
                              onChange={(e) => handleInputChange(e, i)}
                              type="number"
                              maxLength={3}
                            />
                          </Form.Group>
                        )}
                      </td>
                      <td className="cbc-table-body ref-textarea">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            as="textarea"
                            className="presription-input"
                            placeholder="Enter Description"
                            value={x.description || ""}
                            name="description"
                            onChange={(e) => handleInputChange(e, i)}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        {inputList.length - 1 === i && (
                          <a
                            className="btnact btn btn-link add-lab-button"
                            onClick={handleAddClick}
                          >
                            <i
                              className="fa fa-floppy-o"
                              aria-hidden="true"
                            ></i>
                          </a>
                        )}
                        {inputList.length != 1 && (
                          <a
                            className="btnact btn btn-link add-lab-button"
                            onClick={() => handleRemoveClick(i)}
                          >
                            <i className="bi bi-trash"></i>
                          </a>
                        )}
                      </td>
                    </tr>
                    // )
                  ))}
                </>
              ) : (
                <>
                  {reference.map((x, i) => (
                    // return (
                    <tr key={i}>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.gender || ""}
                            className="pre-drop"
                            name="gender"
                            //onChange={(event) => setGenders(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option disabled hidden>
                              Select
                            </option>
                            <option>Male</option>
                            <option>Female</option>
                            <option>Both</option>
                            <option>Others</option>
                          </Form.Select>
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.period || ""}
                            className="pre-drop"
                            name="period"
                            //onChange={(event) => setDays(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option disabled hidden>
                              Select
                            </option>
                            <option>Day</option>
                            <option>Month</option>
                            <option>Year</option>
                          </Form.Select>
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Age"
                            value={x.fromAge || ""}
                            name="fromAge"
                            //onChange={(event) => setFromage(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                            type="phone"
                            maxLength={3}
                            onKeyPress={(event) => {
                              if (!/[0-9]/.test(event.key)) {
                                event.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Age"
                            value={x.toAge || ""}
                            name="toAge"
                            //onChange={(event) => setToage(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                            type="phone"
                            maxLength={3}
                            onKeyPress={(event) => {
                              if (!/[0-9]/.test(event.key)) {
                                event.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group
                          className="mb-3_dosename"
                          controlId="exampleForm.DName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select a value..."
                            value={x.referenceIndicator || ""}
                            className="pre-drop"
                            name="referenceIndicator"
                            //onChange={(event) => setCompare(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                          >
                            <option disabled hidden>
                              Select
                            </option>
                            <option value="EQUALS">=</option>
                            <option value="GREATERTHAN"> {">"} </option>
                            <option value="LESSERTHAN"> {"<"} </option>
                            <option value="GREATERTHANEQUALTO"> {">="} </option>
                            <option value="LESSERTHANEQUALTO"> {"<="} </option>
                            <option value="BETWEEN"> Between </option>
                          </Form.Select>
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            className="presription-input"
                            placeholder="Enter Value"
                            value={x.minValue || ""}
                            name="minValue"
                            //onChange={(event) => setMinvalue(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                            type="number"
                            maxLength={3}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        {x.referenceIndicator == "BETWEEN" ? (
                          <Form.Group className="mb-3_drugname">
                            <Form.Control
                              className="presription-input"
                              placeholder="Enter Value"
                              value={x.maxValue || ""}
                              name="maxValue"
                              //onChange={(event) => setMaxvalue(event.target.value)}
                              onChange={(e) => handleInputChange(e, i)}
                              type="number"
                              maxLength={3}
                            />
                          </Form.Group>
                        ) : (
                          <Form.Group className="mb-3_drugname">
                            <Form.Control
                              className="presription-input"
                              placeholder="Enter Value"
                              value={x.maxValue || ""}
                              name="maxValue"
                              disabled
                              onChange={(e) => handleInputChange(e, i)}
                              type="number"
                              maxLength={3}
                              onKeyPress={(event) => {
                                if (!/[0-9]/.test(event.key)) {
                                  event.preventDefault();
                                }
                              }}
                            />
                          </Form.Group>
                        )}
                      </td>
                      <td className="cbc-table-body ref-textarea">
                        <Form.Group className="mb-3_drugname">
                          <Form.Control
                            as="textarea"
                            className="presription-input"
                            placeholder="Enter Description"
                            value={x.description || ""}
                            name="description"
                            //onChange={(event) => setDescription(event.target.value)}
                            onChange={(e) => handleInputChange(e, i)}
                          />
                        </Form.Group>
                      </td>
                      <td className="cbc-table-body">
                        {reference.length - 1 === i && (
                          <a
                            className="btnact btn btn-link add-lab-button"
                            onClick={handleAddClick}
                          >
                            <i
                              className="fa fa-floppy-o"
                              aria-hidden="true"
                            ></i>
                          </a>
                        )}
                        {reference.length != 1 && (
                          <a
                            className="btnact btn btn-link add-lab-button"
                            onClick={() => handleRemoveClick(i)}
                          >
                            <i className="bi bi-trash"></i>
                          </a>
                        )}
                      </td>
                    </tr>
                    // )
                  ))}
                </>
              )}
            </tbody>
          </Table>
        </Form>
        <div className="float-right pre-btn-section pre-cbc-section">
          <div className="save-btn-section">
            <SaveButton
              butttonClick={cancelFields}
              class_name="regBtnPC"
              button_name="Cancel"
            />
          </div>
          <div className="save-btn-section">
            {updatingId ? (
              <SaveButton
                butttonClick={updateReferance}
                class_name="regBtnN"
                button_name="Update"
              />
            ) : (
              <SaveButton
                butttonClick={SaveReferance}
                class_name="regBtnN"
                button_name="Save"
              />
            )}
          </div>
        </div>
      </Modal>
      {/* cbc modal */}
    </div>
  );
};

export default ReferanceRange;
