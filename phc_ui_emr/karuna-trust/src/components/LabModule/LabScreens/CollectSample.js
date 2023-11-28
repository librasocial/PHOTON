import React, { useState, useEffect, useRef } from "react";
import { Form, Row, Col, Button, Modal, Container } from "react-bootstrap";
import "./LabScreens.css";
import "../../EMR/VitalScreenTabs/VitalScreenTabs.css";
import LabScreen from "./LabScreen";
import * as constant from "../../ConstUrl/constant";
import moment from "moment";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../EMR_Buttons/SaveButton";
import { useHistory } from "react-router-dom";

export default function CollectSample() {
  let history = useHistory();
  const [sampleData, setSampleData] = useState([]);
  const sampleObj = [
    ...new Map(
      sampleData.map((item) => [JSON.stringify(item.sample.labTestName), item])
    ).values(),
  ];

  const [containerTypes, setContainerTypes] = useState("");
  const [conatinerCount, setConatinerCount] = useState("");

  const setContainerType = (e) => {
    let containerType = e;
    setContainerTypes(e);
    if (sampleData.length != 0) {
      for (var i = 0; i < sampleData.length; i++) {
        sampleData[i].sample.containerType = containerType;
      }
    }
  };

  const setConatinerCounts = (e) => {
    let containerCount = e;
    setConatinerCount(e);
    if (sampleData.length != 0) {
      for (var i = 0; i < sampleData.length; i++) {
        sampleData[i].sample.containerCount = containerCount;
      }
    }
  };

  // add container modal
  const [status, setStatus] = useState(false);
  const [medicalShow, setMedicalShow] = useState(false);

  const medicalOpen = () => setMedicalShow(true);
  // add container modal

  // fetching patient data by order id
  const [orderedDetails, setOrderedDetails] = useState([]);
  const [testData, setTestData] = useState([]);

  const [testDataByID, setTestDataByID] = useState([]);

  const [getTestName, setGetTestName] = useState("");
  const [collectSamp, setCollectSamp] = useState("");
  const [filteredSelectData, setFilteredSelectData] = useState([]);
  const [filteredCollectData, setFilteredCollectData] = useState([]);
  const obj1 = [
    ...new Map(
      filteredSelectData.map((item) => [JSON.stringify(item.labTestId), item])
    ).values(),
  ];
  const obj2 = [
    ...new Map(
      filteredCollectData.map((item) => [JSON.stringify(item.labTestId), item])
    ).values(),
  ];

  let result2 = obj1.filter((person) =>
    obj2.every((person2) => !person2.labTestId.includes(person.labTestId))
  );

  const obj = [...obj2, ...result2];

  let labOrderId = sessionStorage.getItem("LabOrderId");
  let cancelStatus = sessionStorage.getItem("cancelStatus");
  useEffect(() => {
    document.title = "EMR Collect Sample";
    if (labOrderId != undefined) {
      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let dtArray = [];
          let testArray = [];
          let testIdArray = [];

          for (let i = 0; i < res["content"][0]["medicalTests"].length; i++) {
            dtArray.push(res["content"]);
            testArray.push(res["content"][0]["medicalTests"][i]);
            testIdArray.push(res["content"][0]["medicalTests"][i]["labTestId"]);
          }

          setOrderedDetails(res["content"]);
          setTestData(testArray);
          setStatus(false);

          fetch(
            `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?labTestIds=${testIdArray}`,
            serviceHeaders.getRequestOptions
          )
            .then((res1) => res1.json())
            .then((res1) => {
              setStatus(false);
              setTestDataByID(res1["data"]);
              let testDataByIdArray = [];
              for (var i = 0; i < res1["data"].length; i++) {
                testDataByIdArray.push(res1["data"][i]);
              }

              fetch(
                `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/samples/filter?size=100`,
                serviceHeaders.getRequestOptions
              )
                .then((res) => res.json())
                .then((res) => {
                  setStatus(false);

                  let testNameArray = [];
                  let testCollectArray = [];

                  for (var i = 0; i < res["content"].length; i++) {
                    testNameArray.push(
                      res["content"][i]["sample"]["labTestName"]
                    );
                    testCollectArray.push(res["content"][i]);
                  }
                  setGetTestName(testNameArray);
                  setCollectSamp(testCollectArray);

                  let collectFilterdata = [];
                  let nonCollectFilterdata = [];

                  for (var i = 0; i < testArray.length; i++) {
                    for (var j = 0; j < testDataByIdArray.length; j++) {
                      if (
                        testDataByIdArray[j].name.includes(
                          testArray[i].labTestName
                        )
                      ) {
                        if (testCollectArray.length != 0) {
                          for (var k = 0; k < testCollectArray.length; k++) {
                            if (
                              testCollectArray[k].sample?.labTestName.includes(
                                testArray[i].labTestName
                              ) &&
                              testCollectArray[k].sample?.labTestName ==
                                testArray[i].labTestName
                            ) {
                              collectFilterdata.push({
                                id: testCollectArray[k].id,
                                labTestId: testArray[i].labTestId,
                                labTestName: testArray[i].labTestName,
                                resultType: testArray[i].resultType,
                                sampleType: testArray[i].sampleType,
                                sampleSnomedCode: testArray[i].sampleSnomedCode,
                                outSourced: testDataByIdArray[j].outSourced,
                                isSampleCollected:
                                  testCollectArray[k].sample?.isSampleCollected,
                              });
                            } else if (
                              testDataByIdArray[j].name.includes(
                                testArray[i].labTestName
                              ) &&
                              testDataByIdArray[j].name ==
                                testArray[i].labTestName
                            ) {
                              nonCollectFilterdata.push({
                                id: "",
                                labTestId: testArray[i].labTestId,
                                labTestName: testArray[i].labTestName,
                                resultType: testArray[i].resultType,
                                sampleType: testArray[i].sampleType,
                                sampleSnomedCode: testArray[i].sampleSnomedCode,
                                outSourced: testDataByIdArray[j].outSourced,
                                isSampleCollected: false,
                              });
                            }
                          }
                        } else {
                          nonCollectFilterdata.push({
                            labTestId: testArray[i].labTestId,
                            labTestName: testArray[i].labTestName,
                            resultType: testArray[i].resultType,
                            sampleType: testArray[i].sampleType,
                            sampleSnomedCode: testArray[i].sampleSnomedCode,
                            outSourced: testDataByIdArray[j].outSourced,
                            isSampleCollected: false,
                          });
                        }
                      }
                    }
                  }
                  // let filteredData = []
                  if (testCollectArray.length != 0) {
                    // filteredData = [...nonCollectFilterdata, ...collectFilterdata];
                    setFilteredCollectData([
                      ...filteredCollectData,
                      ...collectFilterdata,
                    ]);
                  }
                  // filteredData = [...nonCollectFilterdata];
                  setFilteredSelectData([
                    ...filteredSelectData,
                    ...nonCollectFilterdata,
                  ]);

                  // setFilteredSelectData(filteredData)
                });
            });
        });
    }
  }, [status, labOrderId]);
  // fetching patient data by order id

  const [checkedItems, setCheckedItems] = useState([]);
  const checkItemAll = [
    ...new Map(
      checkedItems.map((item) => [JSON.stringify(item), item])
    ).values(),
  ];

  const [testNameData, setTestNameData] = useState("");
  const [outSourceDataValue, setOutSourceDataValue] = useState("");
  const [testCodeData, setTestCodeData] = useState("");
  const [sampleTypeData, setSampleTypeData] = useState("");

  const checKBoxHandler = (event) => {
    // setSampleData([])
    let arraytemp = [];

    if (event.target.checked) {
      setCheckedItems([...checkedItems, event.target.value]);
      arraytemp.push([...checkedItems, event.target.value]);
    } else {
      setCheckedItems(
        checkedItems.filter((value) => value !== event.target.value)
      );
      arraytemp.push(
        checkedItems.filter((value) => value !== event.target.value)
      );
    }

    let sampleDataArray = [];

    var ele = document.getElementsByName("sample-test");
    for (var i = 0; i < ele.length; i++) {
      for (var l = 0; l < obj1.length; l++) {
        for (var j = 0; j < arraytemp.length; j++) {
          if (arraytemp[j].includes(obj1[l].labTestName)) {
            sampleDataArray.push({
              orderId: labOrderId,
              sample: {
                labTestId: obj1[l].labTestId,
                labTestName: obj1[l].labTestName,
                resultType: obj1[l].resultType,
                containerType: containerTypes,
                containerCount: conatinerCount,
                sampleType: obj1[l].sampleType,
                sampleSnomedCode: obj[1].sampleSnomedCode,
                serviceDate: isoDate,
                isOutsourced: obj1[l].outSourced,
                isSampleCollected: true,
              },
            });
          }
        }
      }
    }
    setSampleData(sampleDataArray);
  };

  const selectAllTests = (event) => {
    var ele = document.getElementsByName("sample-test");
    // let arraytemp = [];
    let arraytemp1 = [];

    for (var i = 0; i < ele.length; i++) {
      for (var k = 0; k < result2.length; k++) {
        if (ele[i].value.includes(result2[k].labTestName)) {
          if (ele[i].type == "checkbox") {
            ele[i].checked = true;
            // setCheckedItems(ele[i].value);
            // arraytemp.push(ele[i].value);
            arraytemp1.push(ele[i].value);
          }
        }
      }
    }

    for (var l = 0; l < obj1.length; l++) {
      for (var j = 0; j < arraytemp1.length; j++) {
        if (
          arraytemp1[j].includes(obj1[l].labTestName) &&
          arraytemp1[j] == obj1[l].labTestName
        ) {
          sampleData.push({
            orderId: labOrderId,
            sample: {
              labTestId: obj1[l].labTestId,
              labTestName: obj1[l].labTestName,
              resultType: obj1[l].resultType,
              containerType: containerTypes,
              containerCount: conatinerCount,
              sampleType: obj1[l].sampleType,
              sampleSnomedCode: obj[1].sampleSnomedCode,
              serviceDate: isoDate,
              isOutsourced: obj1[l].outSourced,
              isSampleCollected: true,
            },
          });
        }
      }
    }
    setCheckedItems([...checkedItems, ...arraytemp1]);
  };

  const setOutSourceData = (e, i) => {
    let sampleId = e.target.id;
    let outSourceVale;
    for (var l = 0; l < sampleObj.length; l++) {
      if (sampleObj[l].sample.testCode.includes(sampleId)) {
        sampleObj[i].sample.isOutsourced = e.target.value;
        outSourceVale = e.target.value;
      }
    }
    setOutSourceDataValue(outSourceVale);
  };

  const DeselectAllTests = () => {
    var ele = document.getElementsByName("sample-test");
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = false;
      }
    }
    setCheckedItems([]);
    setSampleData([]);
  };

  // const [selectDataItem, setSelectDataItem] = useState([]);
  let selectDataItem = [];
  var ele = document.getElementsByName("sample-test");

  useEffect(() => {
    for (var i = 0; i < ele.length; i++) {
      for (var j = 0; j < getTestName.length; j++) {
        if (
          getTestName[j].includes(obj[i].labTestName) &&
          getTestName[j] == obj[i].labTestName
        ) {
          ele[i].checked = true;
          selectDataItem.push(ele[i].value);
        }
      }
    }
  });

  // save sample collection for the test
  var isoDate = new Date().toISOString();
  const collectSample = () => {
    if (!containerTypes || !conatinerCount) {
      Tostify.notifyWarning("please select container type and count");
    } else {
      for (var i = 0; i < sampleObj.length; i++) {
        var requestOptions1 = {
          headers: serviceHeaders.myHeaders1,
          method: "POST",
          mode: "cors",
          body: JSON.stringify(sampleObj[i]),
        };

        fetch(
          `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/samples`,
          requestOptions1
        )
          .then((res) => res.json())
          .then((res) => {
            if (res.error) {
              Tostify.notifyFail("Sample is not collected, Please try again");
            } else {
              for (var i = 0; i < sampleObj[i].length; i++) {
                if ((i = sampleObj[i] - 1)) {
                  Tostify.notifySuccess("Sample is collected");
                  // history.push("/Dashboard");
                }
              }
              setStatus(true);
              setContainerTypes("");
              setConatinerCount("");
              location.reload();
              setSampleData([]);
            }
          });
      }
    }
  };

  const resetSample = (event) => {
    setConatinerCount("");
    setContainerType("");
    setEditSampleId("");
    setCheckedItems([]);
    var ele = document.getElementsByName("sample-test");
    for (var i = 0; i < ele.length; i++) {
      if (ele[i].type == "checkbox") {
        ele[i].checked = false;
      }
    }
  };

  const updateOrderStatus = () => {
    const updateLobOrderData = {
      type: "LabOrders",
      properties: {
        medicalTests: testData,
        status: "SAMPLE_COLLECTED",
      },
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateLobOrderData),
    };
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        setShowSampleModal(true);
      });
  };

  // ordera lab again
  const orderLab = () => {
    sessionStorage.removeItem("vitalon");
    sessionStorage.removeItem("LabPateintid");
    sessionStorage.removeItem("LabOrderId");
    sessionStorage.removeItem("cancelStatus");
    window.history.back();
  };
  // ordera lab again

  {
    /* Add Collect Sample Modal */
  }
  const [showSampleModal, setShowSampleModal] = useState(false);
  const closeSampleModal = () => {
    setShowSampleModal(false);
    history.push("/Dashboard");
  };
  {
    /* Add Collect Sample Modal */
  }

  {
    /* Cancel All Order Modal */
  }
  const [orderIdCancel, setOrderIdCancel] = useState("");
  const [cancelName, setCancelName] = useState("");
  const [show1, setShow1] = useState(false);
  const handleClose1 = () => setShow1(false);
  const handleShow1 = (e, n) => {
    setShow1(true);
    setOrderIdCancel(e);
    setCancelName(n);
  };
  {
    /* Cancel All Order Modal */
  }

  // edit collected sample
  const [editTestName, setEditTestName] = useState("");
  const [editResuType, setEditResuType] = useState("");
  const [editOutsorce, setEditOutsorce] = useState("");
  const [editCollect, setEditCollect] = useState("");
  const [editSampleType, setEditSampleType] = useState("");
  const [editTestCode, setEditTestCode] = useState("");
  const [editSampleId, setEditSampleId] = useState("");
  const [editID, setEditID] = useState("");
  const editSample = (e) => {
    let sampleID = e;
    setEditSampleId(e);
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/samples/${sampleID}`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        setContainerTypes(res["content"][0]["sample"]["containerType"]);
        setConatinerCount(res["content"][0]["sample"]["containerCount"]);
        setEditTestName(res["content"][0]["sample"]["labTestName"]);
        setEditResuType(res["content"][0]["sample"]["resultType"]);
        setEditOutsorce(res["content"][0]["sample"]["isOutsourced"]);
        setEditCollect(res["content"][0]["sample"]["isSampleCollected"]);
        setEditSampleType(res["content"][0]["sample"]["sampleType"]);
        setEditTestCode(res["content"][0]["sample"]["testCode"]);
        setEditID(res["content"][0]["id"]);
      });
  };
  // edit collected sample

  // update sample
  const updateSample = () => {
    const updateData = {
      type: "Sample",
      properties: {
        labTestId: editTestCode,
        labTestName: editTestName,
        resultType: editResuType,
        containerType: containerTypes,
        containerCount: conatinerCount,
        sampleType: editSampleType,
        serviceDate: isoDate,
        isOutsourced: editOutsorce,
        isSampleCollected: true,
      },
    };
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateData),
    };

    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/samples/${editSampleId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.error) {
          Tostify.notifyFail("Sample not collect not Updated..!");
        } else {
          Tostify.notifySuccess(
            "Sample is not updated for" + res.content[0].sample?.labTestName
          );
          setStatus(true);
          setEditSampleId("");
          setContainerTypes("");
          setConatinerCount("");
        }
      });
  };
  // update sample

  // delete samples
  const deleteSample = (e) => {
    let labtestId = e;
    let updatingArray = [];

    for (var i = 0; i < testData.length; i++) {
      if (!labtestId.includes(testData[i].labTestId)) {
        updatingArray.push(testData[i]);
      }
    }

    const updatingOreders = {
      type: "LabOrders",
      properties: {
        medicalTests: updatingArray,
        status: "ORDER_ACCEPTED",
      },
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingOreders),
    };
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        setStatus(true);
        setOrderIdCancel("");
        setCancelName("");
        setShow1(false);
        location.reload();
      });
  };
  // delete samples

  return (
    <React.Fragment>
      <ToastContainer />
      {/* Modal For Cancel Particular Order */}
      <Modal show={show1} onHide={handleClose1}>
        <Container>
          <Row>
            <Col>
              <h2 className="cancel-head">Cancel test</h2>
            </Col>
          </Row>
          <Row>
            <Col>
              <p className="cancel-test">
                <b>Test {cancelName}</b>, suggested for this patient will be
                cancelled and removed from the records. Order once cancelled
                cannot be reverted and will be lost.
              </p>
              <p>Are you sure you want to cancel order ?</p>
            </Col>
          </Row>
          <Row>
            <Col className="btn1">
              <Button
                variant="outline-secondary"
                className="regBtnPC"
                onClick={handleClose1}
              >
                No, Retain
              </Button>
            </Col>
            <Col className="btn2">
              <Button
                variant="outline-secondary orderContinue"
                onClick={(e) => deleteSample(orderIdCancel)}
              >
                Yes, Cancel
              </Button>
            </Col>
          </Row>
        </Container>
      </Modal>
      {/* Modal For Cancel Particular Order */}

      {/* modal */}
      <Modal show={showSampleModal} onHide={closeSampleModal}>
        <Row>
          <Col xsm={2} sm={2} md={2} className="success-icon" align="center">
            <i className="bi bi-check2 alergy-check2"></i>
          </Col>
          <Col xsm={10} sm={10} md={10} className="allergy-div-box">
            <Row className="test-div">
              <Col xsm={11} sm={11} md={11}>
                <p className="success-text">
                  <b>Sample Collected Successfully.</b>
                </p>
              </Col>
              <Col xsm={1} sm={1} md={1} align="center">
                <button
                  type="button"
                  className="btn-close modelCls"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  id="btn"
                  onClick={closeSampleModal}
                ></button>
              </Col>
            </Row>
          </Col>
        </Row>
      </Modal>
      {/* modal */}
      <div className="div vital-div">
        <div style={{ minHeight: "85vh" }}>
          <LabScreen
            orderedDetails={orderedDetails}
            cancelStatus={cancelStatus}
            labOrderId={labOrderId}
          />
          <div className="service-tab">
            {orderedDetails &&
              orderedDetails.map((orders, i) => (
                <div className="form-col" key={i}>
                  <Form className="cheif-complaint-form">
                    <div className="lab-heading">
                      <h1 className="lab-head">Collect Sample</h1>
                      <h6 className="invest-text">
                        Collect Sample for the lab services of the patient
                      </h6>
                    </div>
                    <Row>
                      <Col md={2}>
                        <p className="lab-statement">
                          Lab No: <b>{orders.assignedToLab}</b>
                        </p>
                      </Col>
                      <Col md={4}>
                        <p className="lab-statement">
                          Order date & time:&nbsp;
                          <b>
                            {moment(new Date(orders.orderDate)).format(
                              "Do MMM YYYY"
                            )}
                            ,{" "}
                            {moment(new Date(orders.orderDate)).format(
                              "hh:mm A"
                            )}
                          </b>
                        </p>
                      </Col>
                      <Col md={4}>
                        <p className="lab-statement">
                          Consultant:&nbsp;
                          <b>Medical Officer ({orders.encounter?.staffName})</b>
                        </p>
                      </Col>
                      <hr className="lab-line" />
                    </Row>
                    <div className="lab-row">
                      <Row>
                        <Col md={1}>
                          {checkItemAll.length == result2.length ? (
                            <p
                              className="sample-link"
                              onClick={DeselectAllTests}
                            >
                              Deselect All
                            </p>
                          ) : (
                            <p className="sample-link" onClick={selectAllTests}>
                              Select All
                            </p>
                          )}
                        </Col>
                        <Col md={3}>
                          <b>Test name</b>
                        </Col>
                        {/* <Col md={2}>
                                            <b>Service date</b>
                                        </Col> */}
                        <Col md={2} align="center">
                          <b>Sample</b>
                        </Col>
                        <Col md={2} align="center">
                          <b>
                            Sample Collection <br /> Status
                          </b>
                        </Col>
                        <Col md={2} align="center">
                          <b>Outsourced</b>
                        </Col>
                        <Col md={2} align="center">
                          <b>Action</b>
                        </Col>
                      </Row>
                      {editSampleId ? (
                        <Row className="lab-table">
                          <Col md={1}>
                            <p className="sample-checkbox">
                              <input
                                type="checkbox"
                                id={editID}
                                value={editTestName}
                                name="sample-test"
                                onChange={checKBoxHandler}
                              />
                            </p>
                          </Col>
                          <Col md={3}>
                            <p>{editTestName}</p>
                          </Col>
                          <Col md={2} align="center">
                            {editSampleType}
                          </Col>
                          <Col
                            md={2}
                            align="center"
                            className="sample-collect-checkbox"
                          >
                            {editCollect == true ? <p>Yes</p> : <p>No</p>}
                          </Col>
                          <Col
                            md={2}
                            align="center"
                            className="sample-collect-checkbox"
                          >
                            {editOutsorce == true ? (
                              <Form.Group className="mb-3_timesname">
                                {testDataByID.map((r, i) => (
                                  <React.Fragment key={i}>
                                    {r.name == editTestName && (
                                      <Form.Select
                                        aria-label="Default select example"
                                        id={editID}
                                        style={{ width: "80%" }}
                                        value={outSourceDataValue}
                                        onChange={(e) => setOutSourceData(e, i)}
                                      >
                                        <option disabled hidden>
                                          Select Outsource..
                                        </option>
                                        {r.outsourcedOrg
                                          .split(",")
                                          .map((outSource, index) => (
                                            <option key={index}>
                                              {outSource}
                                            </option>
                                          ))}
                                      </Form.Select>
                                    )}
                                  </React.Fragment>
                                ))}
                              </Form.Group>
                            ) : (
                              <p>No</p>
                            )}
                          </Col>
                          <Col md={2} align="center"></Col>
                        </Row>
                      ) : (
                        <>
                          {obj.map((items, i) => (
                            <Row className="lab-table" key={i}>
                              <Col md={1}>
                                <p className="sample-checkbox">
                                  <input
                                    type="checkbox"
                                    id={items.id}
                                    value={items.labTestName}
                                    checked={checkedItems[items.labTestName]}
                                    name="sample-test"
                                    onChange={checKBoxHandler}
                                  />
                                </p>
                              </Col>
                              <Col md={3}>
                                {items.isSampleCollected == true ? (
                                  <p style={{ backgroundColor: "Yellow" }}>
                                    {items.labTestName}
                                  </p>
                                ) : (
                                  <p>{items.labTestName}</p>
                                )}
                              </Col>
                              <Col md={2} align="center">
                                {items.sampleType}
                              </Col>
                              <Col
                                md={2}
                                align="center"
                                className="sample-collect-checkbox"
                              >
                                {items.isSampleCollected == true ? (
                                  <p>Yes</p>
                                ) : (
                                  <p>No</p>
                                )}
                              </Col>
                              <Col
                                md={2}
                                align="center"
                                className="sample-collect-checkbox"
                              >
                                {items.outSourced == true ? (
                                  <>
                                    {items.isSampleCollected == true ? (
                                      <p>Yes</p>
                                    ) : (
                                      <Form.Group className="mb-3_timesname">
                                        {testDataByID.map((r, i) => (
                                          <React.Fragment key={i}>
                                            {r.name == items?.labTestName && (
                                              <Form.Select
                                                aria-label="Default select example"
                                                id={items.id}
                                                style={{ width: "80%" }}
                                                value={outSourceDataValue}
                                                onChange={(e) =>
                                                  setOutSourceData(e, i)
                                                }
                                              >
                                                <option disabled hidden>
                                                  Select Outsource..
                                                </option>
                                                {r.outsourcedOrg
                                                  .split(",")
                                                  .map((outSource, index) => (
                                                    <option key={index}>
                                                      {outSource}
                                                    </option>
                                                  ))}
                                              </Form.Select>
                                            )}
                                          </React.Fragment>
                                        ))}
                                      </Form.Group>
                                    )}
                                  </>
                                ) : (
                                  <p>No</p>
                                )}
                              </Col>
                              <Col md={2} align="center">
                                {items.isSampleCollected == true ? (
                                  <b
                                    style={{ cursor: "pointer" }}
                                    onClick={(e) => editSample(items.id)}
                                  >
                                    <i className="bi bi-pencil chief-edit"></i>
                                  </b>
                                ) : (
                                  <b
                                    style={{ cursor: "pointer" }}
                                    onClick={(e) =>
                                      handleShow1(
                                        items.labTestId,
                                        items.labTestName
                                      )
                                    }
                                  >
                                    X
                                  </b>
                                )}
                              </Col>
                            </Row>
                          ))}
                        </>
                      )}

                      <div className="save-lab-data">
                        <Row>
                          {obj.length != 0 && (
                            <>
                              <Col md={3} className="lab-sample">
                                <div className="lab-fields">
                                  <Form.Label
                                    className="lab-container"
                                    style={{ fontSize: "17px !important" }}
                                  >
                                    Container
                                  </Form.Label>
                                  <Form.Group
                                    className="mb-3_timesname"
                                    controlId="exampleForm.TName"
                                  >
                                    <Form.Select
                                      aria-label="Default select example"
                                      value={containerTypes || ""}
                                      onChange={(event) => {
                                        setContainerType(event.target.value);
                                      }}
                                    >
                                      <option disabled hidden>
                                        Select Container Type
                                      </option>
                                      <option>Citrate</option>
                                      <option>EDTA</option>
                                      <option>Fluoride_F</option>
                                      <option>Fluoride_PP</option>
                                      <option>Plain</option>
                                      <option>Urine container</option>
                                      <option>Syringe 2ml</option>
                                      <option>Syringe 5ml</option>
                                      <option>Syringe 10ml</option>
                                      <option>Others</option>
                                    </Form.Select>
                                  </Form.Group>
                                </div>
                              </Col>
                              <Col md={2} className="lab-sample">
                                <div className="lab-fields">
                                  <Form.Label className="lab-container">
                                    No. of containers
                                  </Form.Label>
                                  <Form.Group className="mb-3_drugname">
                                    <Form.Control
                                      placeholder="00"
                                      type="number"
                                      maxLength={2}
                                      value={conatinerCount || ""}
                                      onChange={(event) =>
                                        setConatinerCounts(event.target.value)
                                      }
                                    />
                                  </Form.Group>
                                </div>
                              </Col>
                            </>
                          )}
                          <Col md={7} className="align-me1">
                            <div className="save-btn-section">
                              {obj.length != 0 ? (
                                <SaveButton
                                  butttonClick={resetSample}
                                  class_name="regBtnPC"
                                  button_name="Cancel"
                                />
                              ) : (
                                <SaveButton
                                  butttonClick={orderLab}
                                  class_name="regBtnPC"
                                  button_name="Order Lab"
                                />
                              )}
                            </div>
                            <div className="save-btn-section">
                              {editSampleId ? (
                                <SaveButton
                                  butttonClick={updateSample}
                                  class_name="regBtnN"
                                  button_name="Update"
                                />
                              ) : (
                                <>
                                  {obj.length !== 0 && result2.length != 0 ? (
                                    <SaveButton
                                      butttonClick={collectSample}
                                      class_name="regBtnN"
                                      button_name="Save"
                                    />
                                  ) : (
                                    <SaveButton
                                      butttonClick={updateOrderStatus}
                                      class_name="regBtnN"
                                      button_name="save"
                                    />
                                  )}
                                </>
                              )}
                            </div>
                          </Col>
                        </Row>
                      </div>
                    </div>
                  </Form>
                </div>
              ))}
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
