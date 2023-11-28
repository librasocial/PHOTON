import React, { useState, useEffect, useRef } from "react";
import { Form, Row, Col, Button, Accordion, Modal } from "react-bootstrap";
import "./LabScreens.css";
import "../../EMR/VitalScreenTabs/VitalScreenTabs.css";
import LabScreen from "./LabScreen";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import moment from "moment";
import CollapsiblePanel from "./Collapsible";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../EMR_Buttons/SaveButton";
let PatientYear;

// let resArray = [];
export default function EnterResult() {
  // fetching patient data by order id
  const form = useRef(null);
  const [orderedDetails, setOrderedDetails] = useState([]);
  PatientYear =
    new Date().getFullYear() -
    new Date(orderedDetails[0]?.patient?.dob).getFullYear();

  let PatientMonth =
    new Date().getMonth() -
    new Date(orderedDetails[0]?.patient?.dob).getMonth();
  let PatientDays =
    new Date().getDate() - new Date(orderedDetails[0]?.patient?.dob).getDate();

  // const [testData, setTestData] = useState([]);

  const [status, setStatus] = useState(false);
  let labOrderId = sessionStorage.getItem("LabOrderId");
  const [resultArray, setresultArray] = useState([
    {
      key: "",
      value: "",
      unitOfMeasure: "",
      testMethod: "",
      normalRange: "",
      description: "",
    },
  ]);
  const [singleResultArray, setsingleResultArray] = useState([
    {
      key: "",
      value: "",
      unitOfMeasure: "",
      testMethod: "",
      normalRange: "",
      description: "",
    },
  ]);

  const resetForm = () => {
    form.current.reset();
    // setResultFields([]);
    setEditResultId("");
  };

  const [medTestData, setMedTestData] = useState([]);
  const [testData, setTestData] = useState([]);
  const [testDataByID, setTestDataByID] = useState([]);

  const [filteredSelectData, setFilteredSelectData] = useState([]);

  const [getEnterData, setGetEnterData] = useState([]);

  const obj = [
    ...new Map(
      getEnterData.map((item) => [JSON.stringify(item.orderSampleId), item])
    ).values(),
  ];
  const obj2 = [
    ...new Map(
      filteredSelectData.map((item) => [
        JSON.stringify(item.orderSampleId),
        item,
      ])
    ).values(),
  ];

  const [enterdResult, setEnterdResult] = useState();
  const [sendResult, setSendResult] = useState([]);
  useEffect(() => {
    if (enterdResult != undefined) {
      let newResult = { ...enterdResult };
      let list = [];
      list = sendResult.filter(
        (item) => item.orderSampleId !== enterdResult.orderSampleId
      );
      list.push(newResult);
      setSendResult(list);
    }
  }, [enterdResult]);

  let result2 = obj2.filter((person) =>
    obj.every(
      (person2) => !person2.orderSampleId.includes(person.orderSampleId)
    )
  );

  const handleFormChange = (i, e) => {
    const { name, value } = e.target;
    let list = [...singleResultArray];
    list[i][name] = value;
    setsingleResultArray(list);

    let resultFields = [
      {
        key: "",
        value: "",
        unitOfMeasure: "",
        testMethod: "",
        normalRange: "",
        description: "",
      },
    ];
    for (var li = 0; li < resultFields.length; li++) {
      if (e.target.name == "value") {
        resultFields[li].value = e.target.value;
      }

      if (e.target.value) {
        if (e.target.id != "" && e.target.id != undefined) {
          let refRangeArray = [];
          for (var j = 0; j < obj2.length; j++) {
            if (obj2[j]?.sample?.labTestName == e.target.id) {
              refRangeArray.push(obj2[j]);
              let resultArray1 = [];
              for (var m = 0; m < refRangeArray[0].results.length; m++) {
                if (refRangeArray[0].results[m].key == e.target.id) {
                  resultArray1.push(refRangeArray[0].results[m]);
                }
              }
              let normalrange = [];
              for (var n = 0; n < resultArray1.length; n++) {
                resultFields[li].key = resultArray1[n].key;
                resultFields[li].unitOfMeasure = resultArray1[n].unitOfMeasure;
                resultFields[li].testMethod = resultArray1[n].testMethod;

                if (resultArray1[n].normalRange != undefined) {
                  normalrange.push(...resultArray1[n].normalRange);
                }

                if (normalrange.length != 0) {
                  for (var k = 0; k < normalrange.length; k++) {
                    if (
                      new Date().getFullYear() -
                        new Date(
                          orderedDetails[0]?.patient?.dob
                        ).getFullYear() >=
                      1
                    ) {
                      if (normalrange[k].period == "Year") {
                        if (normalrange[k].gender == "Both") {
                          if (
                            new Date().getFullYear() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getFullYear() >=
                              normalrange[k].fromAge &&
                            new Date().getFullYear() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getFullYear() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        } else if (
                          normalrange[k].gender ==
                          orderedDetails[0].patient?.gender
                        ) {
                          if (
                            new Date().getFullYear() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getFullYear() >=
                              normalrange[k].fromAge &&
                            new Date().getFullYear() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getFullYear() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        }
                      }
                    } else if (
                      new Date().getMonth() -
                        new Date(orderedDetails[0]?.patient?.dob).getMonth() >
                        0 &&
                      new Date().getFullYear() -
                        new Date(
                          orderedDetails[0]?.patient?.dob
                        ).getFullYear() <
                        1
                    ) {
                      if (normalrange[k].period == "Month") {
                        if (normalrange[k].gender == "Both") {
                          if (
                            new Date().getMonth() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getMonth() >=
                              normalrange[k].fromAge &&
                            new Date().getMonth() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getMonth() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        } else if (
                          normalrange[k].gender ==
                          orderedDetails[0].patient?.gender
                        ) {
                          if (
                            new Date().getMonth() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getMonth() >=
                              normalrange[k].fromAge &&
                            new Date().getMonth() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getMonth() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        }
                      }
                    } else if (
                      new Date().getMonth() -
                        new Date(orderedDetails[0]?.patient?.dob).getMonth() <
                      1
                    ) {
                      if (normalrange[k].period == "Day") {
                        if (normalrange[k].gender == "Both") {
                          if (
                            new Date().getDate() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getDate() >=
                              normalrange[k].fromAge &&
                            new Date().getDate() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getDate() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        } else if (
                          normalrange[k].gender ==
                          orderedDetails[0].patient?.gender
                        ) {
                          if (
                            new Date().getDate() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getDate() >=
                              normalrange[k].fromAge &&
                            new Date().getDate() -
                              new Date(
                                orderedDetails[0]?.patient?.dob
                              ).getDate() <=
                              normalrange[k].toAge
                          ) {
                            if (normalrange[k].maxValue) {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue +
                                " " +
                                "to" +
                                " " +
                                normalrange[k].maxValue;
                            } else {
                              resultFields[li].normalRange =
                                normalrange[k].referenceIndicator +
                                " " +
                                normalrange[k].minValue;
                            }
                            resultFields[li].description =
                              normalrange[k].description;
                          }
                        }
                      }
                    }
                  }
                } else {
                  resultFields[li].normalRange = "";
                  resultFields[li].description = "";
                }
              }
            }
          }
        }
      }
    }

    if (e.target.value) {
      let valueID = e.target.id;
      var isoDate = new Date().toISOString();
      if (valueID != "" && valueID != undefined) {
        for (var i = 0; i < obj2.length; i++) {
          for (var k = 0; k < testData.length; k++) {
            if (
              valueID.includes(obj2[i]?.sample?.labTestName) &&
              valueID == obj2[i]?.sample?.labTestName
            ) {
              // console.log(resultFields, "resultFields")
              setEnterdResult({
                orderId: obj2[i].orderId,
                orderSampleId: obj2[i].orderSampleId,
                sample: {
                  labTestId: obj2[i]?.sample?.labTestId,
                  labTestName: obj2[i]?.sample?.labTestName,
                  resultType: obj2[i]?.sample?.resultType,
                  containerType: obj2[i]?.sample?.containerType,
                  containerCount: obj2[i]?.sample?.containerCount,
                  sampleType: obj2[i]?.sample?.sampleType,
                  sampleSnomedCode: obj2[i]?.sample?.sampleSnomedCode,
                  serviceDate: isoDate,
                  isOutsourced: obj2[i]?.sample?.isOutsourced,
                  isSampleCollected: obj2[i]?.sample?.isSampleCollected,
                },
                results: resultFields,
              });
            }
          }
        }
      }
    }
  };

  const handleOnChange = (e, index) => {
    const { name, value } = e.target;
    let list = [...resultArray];
    list[index][name] = value;
    // console.log(list, "resultArray")
    setresultArray(list);
    let postList = [];
    for (var pr = 0; pr < profileTests.length; pr++) {
      let profSubtest = [];
      for (var ps = 0; ps < profileTests[pr].subTests.length; ps++) {
        if (e.target.id == profileTests[pr].name) {
          profSubtest.push(profileTests[pr].subTests[ps]);
        }
      }

      for (var rt = 0; rt < list.length; rt++) {
        for (var st = 0; st < profSubtest.length; st++) {
          if (
            e.target.id == profileTests[pr].name &&
            list[rt].key.includes(profSubtest[st].testName)
          ) {
            postList.push(list[rt]);
          }
        }
      }
    }

    let valueID = e.target.id;
    var isoDate = new Date().toISOString();
    if (valueID != "" && valueID != undefined) {
      for (var i = 0; i < obj2.length; i++) {
        for (var k = 0; k < testData.length; k++) {
          if (
            valueID.includes(obj2[i]?.sample?.labTestName) &&
            valueID == obj2[i]?.sample?.labTestName
          ) {
            setEnterdResult({
              orderId: obj2[i].orderId,
              orderSampleId: obj2[i].orderSampleId,
              sample: {
                labTestId: obj2[i]?.sample?.labTestId,
                labTestName: obj2[i]?.sample?.labTestName,
                resultType: obj2[i]?.sample?.resultType,
                containerType: obj2[i]?.sample?.containerType,
                containerCount: obj2[i]?.sample?.containerCount,
                sampleType: obj2[i]?.sample?.sampleType,
                sampleSnomedCode: obj2[i]?.sample?.sampleSnomedCode,
                serviceDate: isoDate,
                isOutsourced: obj2[i]?.sample?.isOutsourced,
                isSampleCollected: obj2[i]?.sample?.isSampleCollected,
              },
              results: postList,
            });
          }

          // }
        }
      }
    }
  };

  const [profileTests, setProfileTests] = useState([]);

  useEffect(() => {
    document.title = "EMR Lab Test Result";
    if (labOrderId != undefined) {
      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          // let dtArray = []
          let testArray = [];

          for (let i = 0; i < res["content"][0]["medicalTests"].length; i++) {
            dtArray = res["content"];
            testArray.push(res["content"][0]["medicalTests"][i]);
          }

          setOrderedDetails(res["content"]);
          let dtArray = res["content"];
          setMedTestData(testArray);
          setStatus(false);

          // get order samples by order id
          fetch(
            `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/samples/filter?page=0&size=100`,
            serviceHeaders.getRequestOptions
          )
            .then((res) => res.json())
            .then((res) => {
              setStatus(false);

              let testArray = [];
              let testIdArray = [];
              // let testNameArray = [];
              for (var i = 0; i < res["content"].length; i++) {
                testArray.push(res["content"][i]);
                testIdArray.push(res["content"][i]["sample"]["labTestId"]);
              }
              setTestData(testArray);

              fetch(
                `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?labTestIds=${testIdArray}&page=0&size=100`,
                serviceHeaders.getRequestOptions
              )
                .then((res1) => res1.json())
                .then((res1) => {
                  setStatus(false);
                  console.log(res1["data"], "testDataByID");
                  setTestDataByID(res1["data"]);
                  let testDataByIdArray = [];
                  let refrangeArray = [];

                  for (var i = 0; i < res1["data"].length; i++) {
                    testDataByIdArray.push(res1["data"][i]);
                  }

                  // for filtered data

                  let nonCollectFilterdata = [];
                  let singleresArray = [];

                  for (var i = 0; i < testArray.length; i++) {
                    let profileTestsData = [];
                    setProfileTests(profileTestsData);

                    for (var j = 0; j < testDataByIdArray.length; j++) {
                      if (testDataByIdArray[j].resultType === "Profile") {
                        profileTestsData.push(testDataByIdArray[j]);

                        let profileData = [];
                        let profileSubData = [];
                        let subTestIds = [];

                        for (var h = 0; h < profileTestsData.length; h++) {
                          profileData.push({
                            mainTestId: profileTestsData[h].id,
                            subtests: profileTestsData[h].subTests,
                          });
                          for (var p = 0; p < profileData.length; p++) {
                            for (
                              var t = 0;
                              t < profileData[p].subtests.length;
                              t++
                            ) {
                              if (
                                profileTestsData[h].id ==
                                profileData[p].mainTestId
                              ) {
                                subTestIds.push(
                                  profileData[p].subtests[t].testId
                                );
                                profileSubData.push(profileData[p].subtests[t]);
                              }
                            }
                          }
                        }

                        for (var pf = 0; pf < profileTestsData.length; pf++) {
                          if (
                            profileTestsData[pf].name ==
                            testArray[i].sample.labTestName
                          ) {
                            // profile subtest data
                            let resArray = [];
                            let str = subTestIds.map((u) => u).join(",");
                            fetch(
                              `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?labTestIds=${str}`,
                              serviceHeaders.getRequestOptions
                            )
                              .then((resSubTest1) => resSubTest1.json())
                              .then((resSubTest1) => {
                                let resSubTest = resSubTest1.data;
                                for (var a = 0; a < resSubTest.length; a++) {
                                  for (
                                    var n = 0;
                                    n < profileSubData.length;
                                    n++
                                  ) {
                                    if (
                                      resSubTest[a].name ==
                                      profileSubData[n].testName
                                    ) {
                                      if (PatientYear > 0) {
                                        for (
                                          var b = 0;
                                          b <
                                          resSubTest[a].referenceRanges.length;
                                          b++
                                        ) {
                                          if (
                                            resSubTest[a].referenceRanges[b]
                                              .period == "Year"
                                          ) {
                                            if (
                                              PatientYear >=
                                                resSubTest[a].referenceRanges[b]
                                                  .fromAge &&
                                              PatientYear <=
                                                resSubTest[a].referenceRanges[b]
                                                  .toAge
                                            ) {
                                              if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender == "Both"
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              } else if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender ===
                                                dtArray[0]?.patient?.gender
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              }
                                            }
                                          }
                                        }
                                      } else if (
                                        PatientMonth > 0 &&
                                        PatientYear < 1
                                      ) {
                                        for (
                                          var b = 0;
                                          b <
                                          resSubTest[a].referenceRanges.length;
                                          b++
                                        ) {
                                          if (
                                            resSubTest[a].referenceRanges[b]
                                              .period == "Year"
                                          ) {
                                            if (
                                              PatientYear >=
                                                resSubTest[a].referenceRanges[b]
                                                  .fromAge &&
                                              PatientYear <=
                                                resSubTest[a].referenceRanges[b]
                                                  .toAge
                                            ) {
                                              if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender == "Both"
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              } else if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender ===
                                                dtArray[0]?.patient?.gender
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              }
                                            }
                                          }
                                        }
                                      } else if (PatientMonth < 1) {
                                        for (
                                          var b = 0;
                                          b <
                                          resSubTest[a].referenceRanges.length;
                                          b++
                                        ) {
                                          if (
                                            resSubTest[a].referenceRanges[b]
                                              .period == "Year"
                                          ) {
                                            if (
                                              PatientYear >=
                                                resSubTest[a].referenceRanges[b]
                                                  .fromAge &&
                                              PatientYear <=
                                                resSubTest[a].referenceRanges[b]
                                                  .toAge
                                            ) {
                                              if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender == "Both"
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              } else if (
                                                resSubTest[a].referenceRanges[b]
                                                  .gender ===
                                                dtArray[0]?.patient?.gender
                                              ) {
                                                if (
                                                  resSubTest[a].referenceRanges[
                                                    b
                                                  ].referenceIndicator ==
                                                  "BETWEEN"
                                                ) {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue +
                                                      " " +
                                                      "to" +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .maxValue,
                                                    description: "",
                                                  });
                                                } else {
                                                  resArray.push({
                                                    key: resSubTest[a].name,
                                                    value: "",
                                                    unitOfMeasure:
                                                      resSubTest[a]
                                                        .unitOfMeasurement,
                                                    testMethod:
                                                      resSubTest[a].testMethod,
                                                    normalRange:
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .referenceIndicator +
                                                      " " +
                                                      resSubTest[a]
                                                        .referenceRanges[b]
                                                        .minValue,
                                                    description: "",
                                                  });
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              });
                            nonCollectFilterdata.push({
                              orderId: testArray[i].orderId,
                              orderSampleId: testArray[i].id,
                              sample: {
                                labTestId: profileTestsData[pf].id,
                                labTestName: profileTestsData[pf].name,
                                resultType: profileTestsData[pf].resultType,
                                containerType:
                                  testArray[i].sample?.containerType,
                                containerCount:
                                  testArray[i].sample?.containerCount,
                                sampleType: testArray[i].sample?.sampleType,
                                sampleSnomedCode:
                                  testArray[i].sample?.sampleSnomedCode,
                                serviceDate: testArray[i].sample?.serviceDate,
                                isOutsourced: testArray[i].sample?.isOutsourced,
                                isSampleCollected:
                                  testArray[i].sample?.isSampleCollected,
                                resultFormat: profileTestsData[pf].resultFormat,
                              },
                              results: resArray,
                            });

                            setresultArray(resArray);
                          }
                        }
                      } else {
                        if (
                          testDataByIdArray[j].name.includes(
                            testArray[i].sample?.labTestName
                          ) &&
                          testDataByIdArray[j].name ==
                            testArray[i].sample?.labTestName
                        ) {
                          singleresArray.push({
                            key: testDataByIdArray[j].name,
                            value: "",
                            unitOfMeasure:
                              testDataByIdArray[j].unitOfMeasurement,
                            testMethod: testDataByIdArray[j].testMethod,
                            normalRange: testDataByIdArray[j].referenceRanges,
                            description: "",
                          });
                          // if ((refrangeArray[n].gender.includes(dtArray[m].patient.gender)) || refrangeArray[n].gender == "Both") {
                          nonCollectFilterdata.push({
                            orderId: testArray[i].orderId,
                            orderSampleId: testArray[i].id,
                            sample: {
                              labTestId: testDataByIdArray[j].id,
                              labTestName: testDataByIdArray[j].name,
                              resultType: testDataByIdArray[j].resultType,
                              containerType: testArray[i].sample?.containerType,
                              containerCount:
                                testArray[i].sample?.containerCount,
                              sampleType: testArray[i].sample?.sampleType,
                              sampleSnomedCode:
                                testArray[i].sample?.sampleSnomedCode,
                              serviceDate: testArray[i].sample?.serviceDate,
                              isOutsourced: testArray[i].sample?.isOutsourced,
                              isSampleCollected:
                                testArray[i].sample?.isSampleCollected,
                              resultFormat: testDataByIdArray[j].resultFormat,
                            },
                            results: singleresArray,
                          });
                          setsingleResultArray(singleresArray);
                        }
                      }
                    }
                  }

                  let filteredData = [];
                  filteredData = [...nonCollectFilterdata];
                  setFilteredSelectData([
                    ...filteredSelectData,
                    ...nonCollectFilterdata,
                  ]);

                  // fetching entered data
                  fetch(
                    `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/results/filter?page=0&size=1000000`,
                    serviceHeaders.getRequestOptions
                  )
                    .then((res) => res.json())
                    .then((res) => {
                      setStatus(false);
                      let tempArrayData = [];
                      if (res["content"].length != 0) {
                        for (var i = 0; i < res["content"].length; i++) {
                          tempArrayData.push(res["content"][i]);
                        }
                      }

                      let getArrrayList = [];
                      if (tempArrayData.length != 0) {
                        for (var i = 0; i < filteredData.length; i++) {
                          for (var j = 0; j < tempArrayData.length; j++) {
                            if (
                              tempArrayData[j].orderSampleId.includes(
                                filteredData[i].orderSampleId
                              )
                            ) {
                              getArrrayList.push(tempArrayData[j]);
                            }
                          }
                        }
                      }
                      setGetEnterData([...getEnterData, ...getArrrayList]);
                    });
                  // fetching entered data
                });
            });
        });
      // get order samples by order id
    }
  }, [labOrderId, status]);
  // fetching patient data by order id

  const [btnStatus, setBtnStatus] = useState(false);
  const labResults = () => {
    // alert("hi")
    setBtnStatus(true);
    for (var i = 0; i < sendResult.length; i++) {
      var requestOptions1 = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(sendResult[i]),
      };

      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/results`,
        requestOptions1
      )
        .then((res) => res.json())
        .then((res) => {
          if (res.error) {
            Tostify.notifyFail("Test Value Is Not Entered");
            setBtnStatus(false);
          } else {
            setStatus(true);
            // if(i == (sendResult.length-1)){
            Tostify.notifySuccess(
              "Value successfully entered  for" +
                " " +
                res.content[0].sample?.labTestName
            );
            setBtnStatus(false);
            // }
          }
        });
      //   form.current.reset();
      //   for (var i = 0; i < resultFields.length; i++) {
      //     resultFields[i].value = ""
      //   }
    }
    setSendResult([]);
  };

  {
    /* Add Enter Result Modal */
  }
  const [showSampleModal, setShowSampleModal] = useState(false);
  const closeSampleModal = () => {
    setShowSampleModal(false);
  };
  {
    /* Add Enter Result Modal */
  }

  const updateOrderStatus = () => {
    const updateLobOrderData = {
      type: "LabOrders",
      properties: {
        medicalTests: medTestData,
        status: "RESULTS_ENTERED",
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
        if (res.error) {
          Tostify.notifyFail("Data not updated..! please try again");
        } else {
          setStatus(true);
          setShowSampleModal(true);
        }
      });
  };

  // edit particular result data
  const [editingResult, setEditingResult] = useState([
    { key: "", value: "", unitOfMeasure: "", normalRange: "", description: "" },
  ]);

  const [editingSample, setEditingSample] = useState({});
  const [editingOrderId, setEditingOrderId] = useState("");
  const [editingSampleId, setEditingSampleId] = useState("");

  const [editResultId, setEditResultId] = useState("");
  var handleFormEdit;

  if (editResultId != undefined && editResultId != "") {
    handleFormEdit = (e, i) => {
      const { name, value } = e.target;
      const list = [...editingResult];
      list[i][name] = value;
      setEditingResult(list);
    };
  }

  const editResult = (e) => {
    setEditResultId(e);
    let resultId = e;

    if (labOrderId != undefined) {
      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/results/${resultId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let editingData = [];
          let editResultdata = [];
          if (res.length != 0) {
            for (var i = 0; i < res["content"].length; i++) {
              editingData.push(res["content"][i]);
              setEditingSample(res["content"][i]["sample"]);
              setEditingOrderId(res["content"][i]["orderId"]);
              setEditingSampleId(res["content"][i]["orderSampleId"]);
              for (var j = 0; j < res["content"][i].results.length; j++) {
                editResultdata.push(res["content"][i]["results"][j]);
              }
            }
          }
          setEditingResult(editResultdata);
          setStatus(false);
        });
    }
  };

  const updateResult = () => {
    const updatingData = {
      type: "TestResults",
      properties: {
        orderId: editingOrderId,
        orderSampleId: editingSampleId,
        sample: editingSample,
        results: editingResult,
      },
    };

    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updatingData),
    };
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/results/${editResultId}`,
      requestOptions1
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.error) {
          Tostify.notifyFail("Data not updated..!");
        } else {
          Tostify.notifySuccess(
            "Data updated successfully for" +
              " " +
              res.content[0].sample?.labTestName
          );
          setStatus(true);
        }
      });
    setEditResultId("");
  };

  const cancelResulId = () => {
    setEditResultId("");
  };
  // edit particular result data

  //new code //
  const [collapse, setCollapse] = useState(true);
  const [title, setTitle] = useState("Expand All");
  const collapseAll = () => {
    setCollapse(!collapse);

    setTitle((state) => {
      return state === "Expand All" ? "Collapse All" : "Expand All";
    });
  };
  //new code //

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling
  return (
    <React.Fragment>
      <ToastContainer />
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
                  <b>Investigation Result Saved Successfully.</b>
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
          <LabScreen orderedDetails={orderedDetails} />
          <div className="service-tab">
            <div className="form-col">
              <Form className="cheif-complaint-form" ref={form}>
                <div className="lab-heading">
                  <h1 className="lab-head">Enter Result</h1>
                  <h6 className="invest-text">
                    Enter investgation result for the patient
                  </h6>
                </div>
                <Row>
                  <Col md={2}>
                    <p className="lab-statement">
                      Lab No: <b>{orderedDetails[0]?.assignedToLab}</b>
                    </p>
                  </Col>
                  <Col md={4}>
                    <p className="lab-statement">
                      Order date & time&nbsp;
                      <b>
                        {moment(new Date(orderedDetails[0]?.orderDate)).format(
                          "Do MMM YYYY"
                        )}
                        ,{" "}
                        {moment(new Date(orderedDetails[0]?.orderDate)).format(
                          "hh:mm A"
                        )}
                      </b>
                    </p>
                  </Col>
                  <Col md={4}>
                    <p className="lab-statement">
                      Consultant:&nbsp;
                      <b>
                        {orderedDetails[0]?.originatedBy == "External"
                          ? "Lab Technician"
                          : "Medical Officer"}
                        ({orderedDetails[0]?.encounter?.staffName})
                      </b>
                    </p>
                  </Col>
                  <hr className="lab-line" />
                </Row>
                <div className="lab-row">
                  <Row>
                    <Col md={2}>
                      <p className=" sample-link" onClick={collapseAll}>
                        {title}
                      </p>
                    </Col>
                    <Col md={3}>
                      <b>Test name</b>
                    </Col>
                    <Col md={3}>
                      <b>
                        Sample collection <br />
                        Date and time
                      </b>
                    </Col>
                    <Col md={2}>
                      <b>Sample</b>
                    </Col>
                    <Col md={2}></Col>
                  </Row>

                  <Row className="lab-table">
                    {editResultId ? (
                      <>
                        <React.Fragment>
                          <CollapsiblePanel
                            title={editingSample.labTestName}
                            title1={moment(
                              new Date(editingSample.serviceDate)
                            ).format("Do MMM, YYYY")}
                            title2={moment(
                              new Date(editingSample.serviceDate)
                            ).format("hh:mm A")}
                            title3={editingSample.testMethod}
                            collapse={collapse}
                          >
                            <div>
                              {editingResult.map((x, i) => (
                                <Row key={i}>
                                  <Col md={2}>
                                    <p className="lab-unit" align="center">
                                      {x.key}
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    {testDataByID.map((m, index) => (
                                      <React.Fragment key={index}>
                                        {editingSample.labTestName ==
                                          m.name && (
                                          <>
                                            {m.resultFormat == "Multi Line" && (
                                              <Form.Group className="mb-3_days">
                                                <Form.Control
                                                  className="presription-input"
                                                  id={m.labTestName}
                                                  placeholder="Enter observed value"
                                                  type="text"
                                                  value={x.value}
                                                  name="value"
                                                  onChange={(e) =>
                                                    handleFormEdit(e, i)
                                                  }
                                                />
                                              </Form.Group>
                                            )}
                                            {/* <input type="checkbox" checked={chkValue} /> */}
                                            {m.resultFormat == "Numerical" && (
                                              <Form.Group className="mb-3_days">
                                                <Form.Control
                                                  className="presription-input"
                                                  id={m.labTestName}
                                                  placeholder="Enter observed value"
                                                  type="number"
                                                  value={x.value}
                                                  name="value"
                                                  onChange={(e) =>
                                                    handleFormEdit(e, i)
                                                  }
                                                />
                                              </Form.Group>
                                            )}
                                            {m.resultFormat ==
                                              "Single Line" && (
                                              <Form.Group className="mb-3_days">
                                                <Form.Select
                                                  className="presription-input"
                                                  id={m.labTestName}
                                                  placeholder="Enter observed value"
                                                  value={x.value}
                                                  name="value"
                                                  onChange={(e) =>
                                                    handleFormEdit(e, i)
                                                  }
                                                >
                                                  <option
                                                    value=""
                                                    defaultValue
                                                    hidden
                                                  >
                                                    Select
                                                  </option>
                                                  {m.testValue
                                                    .split("|")
                                                    .map((item, ind) => (
                                                      <option key={ind}>
                                                        {item}
                                                      </option>
                                                    ))}
                                                </Form.Select>
                                              </Form.Group>
                                            )}
                                          </>
                                        )}
                                      </React.Fragment>
                                    ))}
                                  </Col>
                                  <Col md={1}>
                                    <p className="lab-unit">
                                      <Form.Control
                                        className="disable-input"
                                        placeholder="Enter observed value"
                                        type="text"
                                        value={x.unitOfMeasure || ""}
                                        name="unitOfMeasure"
                                        disabled
                                      />
                                      {/* {item?.UoM} <br /> */}
                                      <b>UoM</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    <p className="lab-unit">
                                      <Form.Control
                                        className="disable-input"
                                        placeholder="Enter observed value"
                                        type="text"
                                        value={x.testMethod || ""}
                                        name="key"
                                        disabled
                                      />
                                      {/* {item?.methodName} <br /> */}
                                      <b>Method</b>
                                    </p>
                                  </Col>
                                  <Col md={3}>
                                    {x.normalRange && (
                                      <p className="lab-unit">
                                        {x.normalRange} <br />
                                        <b>Normal Range</b>
                                      </p>
                                    )}
                                  </Col>
                                </Row>
                              ))}
                            </div>
                          </CollapsiblePanel>
                        </React.Fragment>
                      </>
                    ) : (
                      <>
                        {obj &&
                          obj.map((item, index) => (
                            <React.Fragment key={index}>
                              <CollapsiblePanel
                                title={item?.sample.labTestName}
                                title1={moment(
                                  new Date(item?.sample?.serviceDate)
                                ).format("Do MMM, YYYY")}
                                title2={moment(
                                  new Date(item?.sample?.serviceDate)
                                ).format("hh:mm A")}
                                title3={item?.sample?.sampleType}
                                collapse={collapse}
                              >
                                <div>
                                  {item.results.map((x, i) => (
                                    <Row key={i}>
                                      <Col md={2}>
                                        {testDataByID.map((n, i) => (
                                          <React.Fragment key={i}>
                                            {item?.sample?.labTestName ==
                                              n.name && (
                                              <>
                                                <Form.Control
                                                  className="disable-input"
                                                  style={{
                                                    textAlign: "center",
                                                  }}
                                                  type="text"
                                                  value={x.key}
                                                  name="unitOfMeasure"
                                                  disabled
                                                />
                                              </>
                                            )}
                                          </React.Fragment>
                                        ))}
                                      </Col>
                                      <Col md={2}>
                                        <Form.Group className="mb-3_days">
                                          {/* <input type="checkbox" checked={chkValue} /> */}
                                          <Form.Control
                                            className="presription-input"
                                            id={item?.sample?.labTestName}
                                            placeholder="Enter observed value"
                                            type="text"
                                            value={x.value}
                                            name="value"
                                            disabled
                                          />
                                        </Form.Group>
                                      </Col>
                                      <Col md={1}>
                                        <p className="lab-unit">
                                          <Form.Control
                                            className="disable-input"
                                            placeholder="Enter observed value"
                                            type="text"
                                            value={x.unitOfMeasure}
                                            name="unitOfMeasure"
                                            disabled
                                          />
                                          {/* {item?.UoM} <br /> */}
                                          <b>UoM</b>
                                        </p>
                                      </Col>
                                      <Col md={3}>
                                        <p className="lab-unit">
                                          <Form.Control
                                            className="disable-input"
                                            placeholder="Enter observed value"
                                            type="text"
                                            value={x.testMethod}
                                            name="key"
                                            disabled
                                          />
                                          {/* {item?.methodName} <br /> */}
                                          <b>Method</b>
                                        </p>
                                      </Col>
                                      <Col md={3}>
                                        {x.normalRange &&
                                          x.normalRange != "" && (
                                            <p className="lab-unit">
                                              {x.normalRange &&
                                                x.normalRange
                                                  .split(" ")
                                                  .map((items, i) => (
                                                    <span
                                                      style={{
                                                        display: "inline-flex",
                                                      }}
                                                      key={i}
                                                    >
                                                      {items == "EQUALS" ? (
                                                        <span>
                                                          {" "}
                                                          {"="}&nbsp;{" "}
                                                        </span>
                                                      ) : (
                                                        <>
                                                          {items ==
                                                          "GREATERTHAN" ? (
                                                            <span>
                                                              {" "}
                                                              {">"}&nbsp;{" "}
                                                            </span>
                                                          ) : (
                                                            <>
                                                              {items ==
                                                              "LESSERTHAN" ? (
                                                                <span>
                                                                  {" "}
                                                                  {
                                                                    "<"
                                                                  }&nbsp;{" "}
                                                                </span>
                                                              ) : (
                                                                <>
                                                                  {items ==
                                                                  "GREATERTHANEQUALTO" ? (
                                                                    <span>
                                                                      {" "}
                                                                      {">="}
                                                                      &nbsp;{" "}
                                                                    </span>
                                                                  ) : (
                                                                    <>
                                                                      {items ==
                                                                      "LESSERTHANEQUALTO" ? (
                                                                        <span>
                                                                          {" "}
                                                                          {"<="}
                                                                          &nbsp;{" "}
                                                                        </span>
                                                                      ) : (
                                                                        <>
                                                                          {items ==
                                                                          "BETWEEN" ? (
                                                                            <span>
                                                                              {" "}
                                                                              Between&nbsp;{" "}
                                                                            </span>
                                                                          ) : (
                                                                            <span>
                                                                              {
                                                                                items
                                                                              }
                                                                              &nbsp;
                                                                            </span>
                                                                          )}
                                                                        </>
                                                                      )}
                                                                    </>
                                                                  )}
                                                                </>
                                                              )}
                                                            </>
                                                          )}
                                                        </>
                                                      )}
                                                    </span>
                                                  ))}
                                              <br />
                                              <b>Normal Range</b>
                                            </p>
                                          )}
                                      </Col>
                                      <Col md={1}>
                                        <p
                                          className="lab-unit"
                                          onClick={(e) => editResult(item.id)}
                                        >
                                          Edit
                                        </p>
                                      </Col>
                                    </Row>
                                  ))}
                                </div>
                              </CollapsiblePanel>
                            </React.Fragment>
                          ))}

                        {testDataByID.map((n, i) => (
                          <React.Fragment key={i}>
                            <>
                              {result2.map((item, index) => (
                                <React.Fragment key={index}>
                                  {item?.sample?.labTestName == n.name && (
                                    <>
                                      {n.resultType == "Profile" ? (
                                        <CollapsiblePanel
                                          title={item?.sample?.labTestName}
                                          title1={moment(
                                            new Date(item?.sample?.serviceDate)
                                          ).format("Do MMM, YYYY")}
                                          title2={moment(
                                            new Date(item?.sample?.serviceDate)
                                          ).format("hh:mm A")}
                                          title3={item?.sample?.sampleType}
                                          collapse={collapse}
                                        >
                                          {profileTests &&
                                            profileTests.map(
                                              (profile, proIndex) => (
                                                <React.Fragment key={proIndex}>
                                                  <>
                                                    {profile.subTests.map(
                                                      (propSub, subIndex) => (
                                                        <React.Fragment
                                                          key={subIndex}
                                                        >
                                                          <React.Fragment>
                                                            {resultArray &&
                                                              resultArray.map(
                                                                (
                                                                  x,
                                                                  resIndex
                                                                ) => (
                                                                  <React.Fragment
                                                                    key={
                                                                      resIndex
                                                                    }
                                                                  >
                                                                    <>
                                                                      {
                                                                        item
                                                                          ?.sample
                                                                          ?.labTestName ==
                                                                          profile.name &&
                                                                          propSub.testName ==
                                                                            x.key && (
                                                                            // <>{propSub.testName == x.name &&
                                                                            <Row>
                                                                              <Col
                                                                                md={
                                                                                  2
                                                                                }
                                                                              >
                                                                                {item
                                                                                  ?.sample
                                                                                  ?.labTestName ==
                                                                                  n.name && (
                                                                                  <Form.Control
                                                                                    className="disable-input"
                                                                                    style={{
                                                                                      textAlign:
                                                                                        "center",
                                                                                    }}
                                                                                    type="text"
                                                                                    value={
                                                                                      x.key ||
                                                                                      ""
                                                                                    }
                                                                                    name="unitOfMeasure"
                                                                                    disabled
                                                                                  />
                                                                                )}
                                                                              </Col>
                                                                              <Col
                                                                                md={
                                                                                  3
                                                                                }
                                                                              >
                                                                                {item
                                                                                  ?.sample
                                                                                  .resultFormat ==
                                                                                  "Multi Line" && (
                                                                                  <Form.Group className="mb-3_days">
                                                                                    <Form.Control
                                                                                      className="presription-input"
                                                                                      id={
                                                                                        item
                                                                                          ?.sample
                                                                                          ?.labTestName
                                                                                      }
                                                                                      placeholder="Enter observed value"
                                                                                      type="text"
                                                                                      value={
                                                                                        x.value ||
                                                                                        ""
                                                                                      }
                                                                                      name="value"
                                                                                      onChange={(
                                                                                        e
                                                                                      ) =>
                                                                                        handleOnChange(
                                                                                          e,
                                                                                          resIndex
                                                                                        )
                                                                                      }
                                                                                    />
                                                                                  </Form.Group>
                                                                                )}
                                                                                {/* <input type="checkbox" checked={chkValue} /> */}
                                                                                {item
                                                                                  ?.sample
                                                                                  .resultFormat ==
                                                                                  "Numerical" && (
                                                                                  <Form.Group className="mb-3_days">
                                                                                    <Form.Control
                                                                                      className="presription-input"
                                                                                      id={
                                                                                        item
                                                                                          ?.sample
                                                                                          ?.labTestName
                                                                                      }
                                                                                      placeholder="Enter observed value"
                                                                                      type="number"
                                                                                      value={
                                                                                        x.value ||
                                                                                        ""
                                                                                      }
                                                                                      name="value"
                                                                                      onChange={(
                                                                                        e
                                                                                      ) =>
                                                                                        handleOnChange(
                                                                                          e,
                                                                                          resIndex
                                                                                        )
                                                                                      }
                                                                                    />
                                                                                  </Form.Group>
                                                                                )}
                                                                                {item
                                                                                  ?.sample
                                                                                  .resultFormat ==
                                                                                  "Single Line" && (
                                                                                  <>
                                                                                    {testDataByID.map(
                                                                                      (
                                                                                        m,
                                                                                        index
                                                                                      ) => (
                                                                                        <React.Fragment
                                                                                          key={
                                                                                            index
                                                                                          }
                                                                                        >
                                                                                          {item
                                                                                            ?.sample
                                                                                            ?.labTestName ==
                                                                                            m.name && (
                                                                                            <Form.Group className="mb-3_days">
                                                                                              <Form.Select
                                                                                                className="presription-input"
                                                                                                id={
                                                                                                  item
                                                                                                    ?.sample
                                                                                                    ?.labTestName
                                                                                                }
                                                                                                placeholder="Enter observed value"
                                                                                                value={
                                                                                                  x.value ||
                                                                                                  ""
                                                                                                }
                                                                                                name="value"
                                                                                                onChange={(
                                                                                                  e
                                                                                                ) =>
                                                                                                  handleOnChange(
                                                                                                    e,
                                                                                                    resIndex
                                                                                                  )
                                                                                                }
                                                                                              >
                                                                                                <option
                                                                                                  value=""
                                                                                                  defaultValue
                                                                                                  hidden
                                                                                                >
                                                                                                  Select
                                                                                                </option>
                                                                                                {m.testValue
                                                                                                  .split(
                                                                                                    "|"
                                                                                                  )
                                                                                                  .map(
                                                                                                    (
                                                                                                      item,
                                                                                                      ind
                                                                                                    ) => (
                                                                                                      <option
                                                                                                        key={
                                                                                                          ind
                                                                                                        }
                                                                                                      >
                                                                                                        {
                                                                                                          item
                                                                                                        }
                                                                                                      </option>
                                                                                                    )
                                                                                                  )}
                                                                                              </Form.Select>
                                                                                            </Form.Group>
                                                                                          )}
                                                                                        </React.Fragment>
                                                                                      )
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                              </Col>
                                                                              <Col
                                                                                md={
                                                                                  1
                                                                                }
                                                                              >
                                                                                <p className="lab-unit">
                                                                                  <Form.Control
                                                                                    className="disable-input"
                                                                                    placeholder="Enter observed value"
                                                                                    type="text"
                                                                                    value={
                                                                                      x.unitOfMeasure ||
                                                                                      ""
                                                                                    }
                                                                                    name="unitOfMeasure"
                                                                                    disabled
                                                                                  />
                                                                                  <b>
                                                                                    UoM
                                                                                  </b>
                                                                                </p>
                                                                              </Col>
                                                                              <Col
                                                                                md={
                                                                                  3
                                                                                }
                                                                              >
                                                                                <p className="lab-unit">
                                                                                  <Form.Control
                                                                                    className="disable-input"
                                                                                    placeholder="Enter observed value"
                                                                                    type="text"
                                                                                    value={
                                                                                      x.testMethod ||
                                                                                      ""
                                                                                    }
                                                                                    name="key"
                                                                                    disabled
                                                                                  />
                                                                                  <b>
                                                                                    Method
                                                                                  </b>
                                                                                </p>
                                                                              </Col>
                                                                              <Col
                                                                                md={
                                                                                  3
                                                                                }
                                                                              >
                                                                                {item
                                                                                  ?.sample
                                                                                  .resultFormat ==
                                                                                  "Numerical" && (
                                                                                  <p className="lab-unit">
                                                                                    {x.normalRange ||
                                                                                      ""}
                                                                                    <br />
                                                                                    <b>
                                                                                      Normal
                                                                                      Range
                                                                                    </b>
                                                                                  </p>
                                                                                )}
                                                                              </Col>
                                                                            </Row>
                                                                          )
                                                                        // }
                                                                        // </>
                                                                      }
                                                                    </>
                                                                  </React.Fragment>
                                                                )
                                                              )}
                                                          </React.Fragment>
                                                        </React.Fragment>
                                                      )
                                                    )}
                                                  </>
                                                </React.Fragment>
                                              )
                                            )}
                                        </CollapsiblePanel>
                                      ) : (
                                        <CollapsiblePanel
                                          title={item?.sample?.labTestName}
                                          title1={moment(
                                            new Date(item?.sample?.serviceDate)
                                          ).format("Do MMM, YYYY")}
                                          title2={moment(
                                            new Date(item?.sample?.serviceDate)
                                          ).format("hh:mm A")}
                                          title3={item?.sample?.sampleType}
                                          collapse={collapse}
                                        >
                                          <div>
                                            {singleResultArray.map((x, i) => (
                                              <React.Fragment key={i}>
                                                <>
                                                  {item?.sample?.labTestName ==
                                                    x.key && (
                                                    <Row key={i}>
                                                      <Col md={2}>
                                                        <React.Fragment key={i}>
                                                          <Form.Control
                                                            className="disable-input"
                                                            style={{
                                                              textAlign:
                                                                "center",
                                                            }}
                                                            type="text"
                                                            value={x.key}
                                                            name="unitOfMeasure"
                                                            disabled
                                                          />
                                                        </React.Fragment>
                                                      </Col>
                                                      <Col md={3}>
                                                        {item?.sample
                                                          .resultFormat ==
                                                          "Multi Line" && (
                                                          <Form.Group className="mb-3_days">
                                                            <Form.Control
                                                              className="presription-input"
                                                              id={
                                                                item?.sample
                                                                  ?.labTestName
                                                              }
                                                              placeholder="Enter observed value"
                                                              type="text"
                                                              value={x.value}
                                                              name="value"
                                                              onChange={(e) =>
                                                                handleFormChange(
                                                                  i,
                                                                  e
                                                                )
                                                              }
                                                            />
                                                          </Form.Group>
                                                        )}
                                                        {/* <input type="checkbox" checked={chkValue} /> */}
                                                        {item?.sample
                                                          .resultFormat ==
                                                          "Numerical" && (
                                                          <Form.Group className="mb-3_days">
                                                            <Form.Control
                                                              className="presription-input"
                                                              id={
                                                                item?.sample
                                                                  ?.labTestName
                                                              }
                                                              placeholder="Enter observed value"
                                                              type="number"
                                                              value={x.value}
                                                              name="value"
                                                              onChange={(e) =>
                                                                handleFormChange(
                                                                  i,
                                                                  e
                                                                )
                                                              }
                                                            />
                                                          </Form.Group>
                                                        )}
                                                        {item?.sample
                                                          .resultFormat ==
                                                          "Single Line" && (
                                                          <>
                                                            {testDataByID.map(
                                                              (m, index) => (
                                                                <React.Fragment
                                                                  key={index}
                                                                >
                                                                  {item?.sample
                                                                    ?.labTestName ==
                                                                    m.name && (
                                                                    <Form.Group className="mb-3_days">
                                                                      <Form.Select
                                                                        className="presription-input"
                                                                        id={
                                                                          item
                                                                            ?.sample
                                                                            ?.labTestName
                                                                        }
                                                                        placeholder="Enter observed value"
                                                                        value={
                                                                          x.value
                                                                        }
                                                                        name="value"
                                                                        onChange={(
                                                                          e
                                                                        ) =>
                                                                          handleFormChange(
                                                                            i,
                                                                            e
                                                                          )
                                                                        }
                                                                      >
                                                                        <option
                                                                          value=""
                                                                          defaultValue
                                                                          hidden
                                                                        >
                                                                          Select
                                                                        </option>
                                                                        {m.testValue
                                                                          .split(
                                                                            "|"
                                                                          )
                                                                          .map(
                                                                            (
                                                                              item,
                                                                              ind
                                                                            ) => (
                                                                              <option
                                                                                key={
                                                                                  ind
                                                                                }
                                                                              >
                                                                                {
                                                                                  item
                                                                                }
                                                                              </option>
                                                                            )
                                                                          )}
                                                                      </Form.Select>
                                                                    </Form.Group>
                                                                  )}
                                                                </React.Fragment>
                                                              )
                                                            )}
                                                          </>
                                                        )}
                                                      </Col>
                                                      <Col md={1}>
                                                        <p className="lab-unit">
                                                          <Form.Control
                                                            className="disable-input"
                                                            placeholder="Enter observed value"
                                                            type="text"
                                                            value={
                                                              x.unitOfMeasure
                                                            }
                                                            name="unitOfMeasure"
                                                            disabled
                                                          />
                                                          {/* {item?.UoM} <br /> */}
                                                          <b>UoM</b>
                                                        </p>
                                                      </Col>
                                                      <Col md={3}>
                                                        <p className="lab-unit">
                                                          <Form.Control
                                                            className="disable-input"
                                                            placeholder="Enter observed value"
                                                            type="text"
                                                            value={x.testMethod}
                                                            name="key"
                                                            disabled
                                                          />
                                                          {/* {item?.methodName} <br /> */}
                                                          <b>Method</b>
                                                        </p>
                                                      </Col>
                                                      <Col md={3}>
                                                        {item?.sample
                                                          .resultFormat ==
                                                          "Numerical" && (
                                                          <p className="lab-unit">
                                                            {x.normalRange &&
                                                              x.normalRange.map(
                                                                (r, i) => (
                                                                  <React.Fragment
                                                                    key={i}
                                                                  >
                                                                    {new Date().getFullYear() -
                                                                      new Date(
                                                                        orderedDetails[0]?.patient?.dob
                                                                      ).getFullYear() >=
                                                                    1 ? (
                                                                      <>
                                                                        {r.period ==
                                                                          "Year" && (
                                                                          <>
                                                                            {r.gender ==
                                                                            "Both" ? (
                                                                              <>
                                                                                {new Date().getFullYear() -
                                                                                  new Date(
                                                                                    orderedDetails[0]?.patient?.dob
                                                                                  ).getFullYear() >=
                                                                                  r.fromAge &&
                                                                                  new Date().getFullYear() -
                                                                                    new Date(
                                                                                      orderedDetails[0]?.patient?.dob
                                                                                    ).getFullYear() <=
                                                                                    r.toAge && (
                                                                                    <>
                                                                                      <>
                                                                                        {r.referenceIndicator ==
                                                                                        "EQUALS" ? (
                                                                                          "="
                                                                                        ) : (
                                                                                          <>
                                                                                            {r.referenceIndicator ==
                                                                                            "GREATERTHAN" ? (
                                                                                              ">"
                                                                                            ) : (
                                                                                              <>
                                                                                                {r.referenceIndicator ==
                                                                                                "LESSERTHAN" ? (
                                                                                                  "<"
                                                                                                ) : (
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "GREATERTHANEQUALTO" ? (
                                                                                                      ">="
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "LESSERTHANEQUALTO" ? (
                                                                                                          "<="
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                              "BETWEEN" &&
                                                                                                              "Between"}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                      </>
                                                                                      &nbsp;
                                                                                      <span>
                                                                                        {
                                                                                          r.minValue
                                                                                        }
                                                                                      </span>
                                                                                      &nbsp;
                                                                                      {r.maxValue && (
                                                                                        <span>
                                                                                          -
                                                                                        </span>
                                                                                      )}{" "}
                                                                                      &nbsp;
                                                                                      <span>
                                                                                        {
                                                                                          r.maxValue
                                                                                        }
                                                                                      </span>
                                                                                    </>
                                                                                  )}
                                                                              </>
                                                                            ) : (
                                                                              <>
                                                                                {r.gender ===
                                                                                  orderedDetails[0]
                                                                                    .patient
                                                                                    ?.gender && (
                                                                                  <>
                                                                                    {new Date().getFullYear() -
                                                                                      new Date(
                                                                                        orderedDetails[0]?.patient?.dob
                                                                                      ).getFullYear() >=
                                                                                      r.fromAge &&
                                                                                      new Date().getFullYear() -
                                                                                        new Date(
                                                                                          orderedDetails[0]?.patient?.dob
                                                                                        ).getFullYear() <=
                                                                                        r.toAge && (
                                                                                        <>
                                                                                          <>
                                                                                            {r.referenceIndicator ==
                                                                                            "EQUALS" ? (
                                                                                              "="
                                                                                            ) : (
                                                                                              <>
                                                                                                {r.referenceIndicator ==
                                                                                                "GREATERTHAN" ? (
                                                                                                  ">"
                                                                                                ) : (
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "LESSERTHAN" ? (
                                                                                                      "<"
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "GREATERTHANEQUALTO" ? (
                                                                                                          ">="
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                            "LESSERTHANEQUALTO" ? (
                                                                                                              "<="
                                                                                                            ) : (
                                                                                                              <>
                                                                                                                {r.referenceIndicator ==
                                                                                                                  "BETWEEN" &&
                                                                                                                  "Between"}
                                                                                                              </>
                                                                                                            )}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                          </>
                                                                                          &nbsp;
                                                                                          <span>
                                                                                            {
                                                                                              r.minValue
                                                                                            }
                                                                                          </span>{" "}
                                                                                          &nbsp;
                                                                                          {r.maxValue && (
                                                                                            <span>
                                                                                              -
                                                                                            </span>
                                                                                          )}{" "}
                                                                                          &nbsp;
                                                                                          <span>
                                                                                            {
                                                                                              r.maxValue
                                                                                            }
                                                                                          </span>
                                                                                        </>
                                                                                      )}
                                                                                  </>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                          </>
                                                                        )}
                                                                      </>
                                                                    ) : (
                                                                      <>
                                                                        {new Date().getMonth() -
                                                                          new Date(
                                                                            orderedDetails[0]?.patient?.dob
                                                                          ).getMonth() >
                                                                          0 &&
                                                                        new Date().getFullYear() -
                                                                          new Date(
                                                                            orderedDetails[0]?.patient?.dob
                                                                          ).getFullYear() <
                                                                          1 ? (
                                                                          <>
                                                                            {r.period ==
                                                                              "Month" && (
                                                                              <>
                                                                                {r.gender ==
                                                                                "Both" ? (
                                                                                  <>
                                                                                    {new Date().getMonth() -
                                                                                      new Date(
                                                                                        orderedDetails[0]?.patient?.dob
                                                                                      ).getMonth() >=
                                                                                      r.fromAge &&
                                                                                      new Date().getMonth() -
                                                                                        new Date(
                                                                                          orderedDetails[0]?.patient?.dob
                                                                                        ).getMonth() <=
                                                                                        r.toAge && (
                                                                                        <>
                                                                                          <>
                                                                                            {r.referenceIndicator ==
                                                                                            "EQUALS" ? (
                                                                                              "="
                                                                                            ) : (
                                                                                              <>
                                                                                                {r.referenceIndicator ==
                                                                                                "GREATERTHAN" ? (
                                                                                                  ">"
                                                                                                ) : (
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "LESSERTHAN" ? (
                                                                                                      "<"
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "GREATERTHANEQUALTO" ? (
                                                                                                          ">="
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                            "LESSERTHANEQUALTO" ? (
                                                                                                              "<="
                                                                                                            ) : (
                                                                                                              <>
                                                                                                                {r.referenceIndicator ==
                                                                                                                  "BETWEEN" &&
                                                                                                                  "Between"}
                                                                                                              </>
                                                                                                            )}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                          </>
                                                                                          &nbsp;
                                                                                          <span>
                                                                                            {
                                                                                              r.minValue
                                                                                            }
                                                                                          </span>
                                                                                          &nbsp;
                                                                                          {r.maxValue && (
                                                                                            <span>
                                                                                              -
                                                                                            </span>
                                                                                          )}{" "}
                                                                                          &nbsp;
                                                                                          <span>
                                                                                            {
                                                                                              r.maxValue
                                                                                            }
                                                                                          </span>
                                                                                        </>
                                                                                      )}
                                                                                  </>
                                                                                ) : (
                                                                                  <>
                                                                                    {r.gender ===
                                                                                      orderedDetails[0]
                                                                                        .patient
                                                                                        ?.gender && (
                                                                                      <>
                                                                                        {new Date().getMonth() -
                                                                                          new Date(
                                                                                            orderedDetails[0]?.patient?.dob
                                                                                          ).getMonth() >=
                                                                                          r.fromAge &&
                                                                                          new Date().getFullYear() -
                                                                                            new Date(
                                                                                              orderedDetails[0]?.patient?.dob
                                                                                            ).getFullYear() <=
                                                                                            r.toAge && (
                                                                                            <>
                                                                                              <>
                                                                                                {r.referenceIndicator ==
                                                                                                "EQUALS" ? (
                                                                                                  "="
                                                                                                ) : (
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "GREATERTHAN" ? (
                                                                                                      ">"
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "LESSERTHAN" ? (
                                                                                                          "<"
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                            "GREATERTHANEQUALTO" ? (
                                                                                                              ">="
                                                                                                            ) : (
                                                                                                              <>
                                                                                                                {r.referenceIndicator ==
                                                                                                                "LESSERTHANEQUALTO" ? (
                                                                                                                  "<="
                                                                                                                ) : (
                                                                                                                  <>
                                                                                                                    {r.referenceIndicator ==
                                                                                                                      "BETWEEN" &&
                                                                                                                      "Between"}
                                                                                                                  </>
                                                                                                                )}
                                                                                                              </>
                                                                                                            )}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                )}
                                                                                              </>
                                                                                              &nbsp;
                                                                                              <span>
                                                                                                {
                                                                                                  r.minValue
                                                                                                }
                                                                                              </span>
                                                                                              &nbsp;
                                                                                              {r.maxValue && (
                                                                                                <span>
                                                                                                  -
                                                                                                </span>
                                                                                              )}{" "}
                                                                                              &nbsp;
                                                                                              <span>
                                                                                                {
                                                                                                  r.maxValue
                                                                                                }
                                                                                              </span>
                                                                                            </>
                                                                                          )}
                                                                                      </>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                          </>
                                                                        ) : (
                                                                          <>
                                                                            {new Date().getMonth() -
                                                                              new Date(
                                                                                orderedDetails[0]?.patient?.dob
                                                                              ).getMonth() <
                                                                              1 && (
                                                                              <>
                                                                                {r.period ==
                                                                                  "Day" && (
                                                                                  <>
                                                                                    {r.gender ==
                                                                                    "Both" ? (
                                                                                      <>
                                                                                        {new Date().getDate() -
                                                                                          new Date(
                                                                                            orderedDetails[0]?.patient?.dob
                                                                                          ).getDate() >=
                                                                                          r.fromAge &&
                                                                                          new Date().getDate() -
                                                                                            new Date(
                                                                                              orderedDetails[0]?.patient?.dob
                                                                                            ).getDate() <=
                                                                                            r.toAge && (
                                                                                            <>
                                                                                              <>
                                                                                                {r.referenceIndicator ==
                                                                                                "EQUALS" ? (
                                                                                                  "="
                                                                                                ) : (
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "GREATERTHAN" ? (
                                                                                                      ">"
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "LESSERTHAN" ? (
                                                                                                          "<"
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                            "GREATERTHANEQUALTO" ? (
                                                                                                              ">="
                                                                                                            ) : (
                                                                                                              <>
                                                                                                                {r.referenceIndicator ==
                                                                                                                "LESSERTHANEQUALTO" ? (
                                                                                                                  "<="
                                                                                                                ) : (
                                                                                                                  <>
                                                                                                                    {r.referenceIndicator ==
                                                                                                                      "BETWEEN" &&
                                                                                                                      "Between"}
                                                                                                                  </>
                                                                                                                )}
                                                                                                              </>
                                                                                                            )}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                )}
                                                                                              </>
                                                                                              &nbsp;
                                                                                              <span>
                                                                                                {
                                                                                                  r.minValue
                                                                                                }
                                                                                              </span>
                                                                                              &nbsp;
                                                                                              {r.maxValue && (
                                                                                                <span>
                                                                                                  -
                                                                                                </span>
                                                                                              )}{" "}
                                                                                              &nbsp;
                                                                                              <span>
                                                                                                {
                                                                                                  r.maxValue
                                                                                                }
                                                                                              </span>
                                                                                            </>
                                                                                          )}
                                                                                      </>
                                                                                    ) : (
                                                                                      <>
                                                                                        {r.gender ===
                                                                                          orderedDetails[0]
                                                                                            .patient
                                                                                            ?.gender && (
                                                                                          <>
                                                                                            {new Date().getDate() -
                                                                                              new Date(
                                                                                                orderedDetails[0]?.patient?.dob
                                                                                              ).getDate() >=
                                                                                              r.fromAge &&
                                                                                              new Date().getDate() -
                                                                                                new Date(
                                                                                                  orderedDetails[0]?.patient?.dob
                                                                                                ).getDate() <=
                                                                                                r.toAge && (
                                                                                                <>
                                                                                                  <>
                                                                                                    {r.referenceIndicator ==
                                                                                                    "EQUALS" ? (
                                                                                                      "="
                                                                                                    ) : (
                                                                                                      <>
                                                                                                        {r.referenceIndicator ==
                                                                                                        "GREATERTHAN" ? (
                                                                                                          ">"
                                                                                                        ) : (
                                                                                                          <>
                                                                                                            {r.referenceIndicator ==
                                                                                                            "LESSERTHAN" ? (
                                                                                                              "<"
                                                                                                            ) : (
                                                                                                              <>
                                                                                                                {r.referenceIndicator ==
                                                                                                                "GREATERTHANEQUALTO" ? (
                                                                                                                  ">="
                                                                                                                ) : (
                                                                                                                  <>
                                                                                                                    {r.referenceIndicator ==
                                                                                                                    "LESSERTHANEQUALTO" ? (
                                                                                                                      "<="
                                                                                                                    ) : (
                                                                                                                      <>
                                                                                                                        {r.referenceIndicator ==
                                                                                                                          "BETWEEN" &&
                                                                                                                          "Between"}
                                                                                                                      </>
                                                                                                                    )}
                                                                                                                  </>
                                                                                                                )}
                                                                                                              </>
                                                                                                            )}
                                                                                                          </>
                                                                                                        )}
                                                                                                      </>
                                                                                                    )}
                                                                                                  </>
                                                                                                  &nbsp;
                                                                                                  <span>
                                                                                                    {
                                                                                                      r.minValue
                                                                                                    }
                                                                                                  </span>
                                                                                                  &nbsp;
                                                                                                  {r.maxValue && (
                                                                                                    <span>
                                                                                                      -
                                                                                                    </span>
                                                                                                  )}{" "}
                                                                                                  &nbsp;
                                                                                                  <span>
                                                                                                    {
                                                                                                      r.maxValue
                                                                                                    }
                                                                                                  </span>
                                                                                                </>
                                                                                              )}
                                                                                          </>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                          </>
                                                                        )}
                                                                      </>
                                                                    )}
                                                                  </React.Fragment>
                                                                )
                                                              )}{" "}
                                                            <br />
                                                            <b>Normal Range</b>
                                                          </p>
                                                        )}
                                                      </Col>
                                                    </Row>
                                                  )}
                                                </>
                                              </React.Fragment>
                                            ))}
                                          </div>
                                        </CollapsiblePanel>
                                      )}
                                    </>
                                  )}
                                </React.Fragment>
                              ))}
                            </>
                          </React.Fragment>
                        ))}
                      </>
                    )}
                  </Row>

                  <div className="save-lab-data align-me1">
                    <div className="save-btn-section">
                      <SaveButton
                        butttonClick={resetForm}
                        class_name="regBtnPC"
                        button_name="Cancel"
                      />
                    </div>
                    <div className="save-btn-section">
                      {editResultId ? (
                        <SaveButton
                          butttonClick={updateResult}
                          class_name="regBtnN"
                          button_name="Update"
                        />
                      ) : (
                        <>
                          {result2.length != 0 ? (
                            <SaveButton
                              butttonClick={labResults}
                              class_name="regBtnN"
                              button_name="Save"
                              btnDisable={btnStatus}
                            />
                          ) : (
                            <SaveButton
                              butttonClick={updateOrderStatus}
                              class_name="regBtnN"
                              button_name="Save All"
                            />
                          )}
                        </>
                      )}
                    </div>
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
