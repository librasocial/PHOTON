import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, Modal, Table } from "react-bootstrap";
import "./LabScreens.css";
import "../../EMR/VitalScreenTabs/VitalScreenTabs.css";
import LabScreen from "./LabScreen";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import moment from "moment";

export default function ViewReport() {
  {
    /* Print Test Reports */
  }
  const [orderedDetails, setOrderedDetails] = useState([]);

  const phc = sessionStorage.getItem("phc");
  let labOrderId = sessionStorage.getItem("LabOrderId");
  const [status, setStatus] = useState(false);
  const [testEnteredData, setTestEnteredData] = useState([]);

  const date = new Date();
  let number1 = moment(date).format("YYYY");

  const [descshow, setdescShow] = useState(false);
  const descClose = () => setdescShow(false);
  const descShow = () => setdescShow(true);

  const [patientData, setPatientData] = useState([]);
  // const [testDataByID, setTestDataByID] = useState([]);
  const [singleDataByID, setSingleDataByID] = useState([]);
  const [subTestDataById, setSubTestDataById] = useState([]);
  // const testDataByID = [...singleDataByID, ...subTestDataById]

  let PatientYear =
    new Date().getFullYear() -
    new Date(patientData[0]?.patient?.dob).getFullYear();
  let PatientMonth =
    new Date().getMonth() - new Date(patientData[0]?.patient?.dob).getMonth();
  let PatientDays =
    new Date().getDate() - new Date(patientData[0]?.patient?.dob).getDate();

  let typeofuser = sessionStorage.getItem("typeofuser");

  useEffect(() => {
    document.title = "EMR Test Reports";
    if (labOrderId != undefined) {
      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let dtArray = [];
          setPatientData(res["content"]);
          let testArray = [];

          for (let i = 0; i < res["content"][0]["medicalTests"].length; i++) {
            dtArray.push(res["content"][0]);
            testArray.push(res["content"][0]["medicalTests"][i]);
          }

          setOrderedDetails(res["content"]);
          setStatus(false);

          // fetching test enstered result
          fetch(
            `${constant.ApiUrl}/lab-orders-svc/laborders/${labOrderId}/results/filter?page=0&size=1000000`,
            serviceHeaders.getRequestOptions
          )
            .then((res) => res.json())
            .then((res) => {
              setStatus(false);
              let testIdArray = [];
              let tempArrayData = [];
              let resultData = [];
              if (res["content"].length != 0) {
                for (var i = 0; i < res["content"].length; i++) {
                  tempArrayData.push(res["content"][i]);
                  testIdArray.push(res["content"][i]["sample"]["labTestId"]);
                }
                for (var l = 0; l < tempArrayData.length; l++) {
                  resultData.push(tempArrayData[l].results[l]);
                }

                // fetching testData
                fetch(
                  `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?labTestIds=${testIdArray}`,
                  serviceHeaders.getRequestOptions
                )
                  .then((res1) => res1.json())
                  .then((res1) => {
                    let testDataByIdArray = [];
                    let singleDataArray = [];

                    for (var i = 0; i < res1["data"].length; i++) {
                      if (res1["data"][i].resultType == "Profile") {
                        for (
                          var l = 0;
                          l < res1["data"][i].subTests.length;
                          l++
                        ) {
                          testDataByIdArray.push(
                            res1["data"][i].subTests[l].testId
                          );
                        }
                      } else {
                        singleDataArray.push(res1["data"][i]);
                      }
                    }
                    setSingleDataByID(singleDataArray);
                    if (testDataByIdArray.length != 0) {
                      fetch(
                        `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?labTestIds=${testDataByIdArray}`,
                        serviceHeaders.getRequestOptions
                      )
                        .then((resSub) => resSub.json())
                        .then((resSub) => {
                          setSubTestDataById(resSub["data"]);
                        });
                    }

                    // for (var l = 0; l < tempArrayData.length; l++){
                    //   for (var m = 0; m < testDataByIdArray.length; m++){

                    //   }
                    // }
                  });
                // fetching testData
              }

              setTestEnteredData([...testEnteredData, ...tempArrayData]);
            });
          // fetching test enstered result
        });
    }
  }, [status, labOrderId]);
  {
    /* Print Test Reports */
  }

  let preReportStatus = sessionStorage.getItem("prevReport");

  return (
    <React.Fragment>
      <div
        className={
          (typeofuser === "Lab Technician" && "div vital-div") ||
          (typeofuser === "Medical Officer" && "div")
        }
      >
        {/* <LabModal chiefClose={medicalClose1} cheifShow={medicineShow} /> */}
        <div
          className={
            preReportStatus == "previous-Report" ? "" : "report-page-main-div"
          }
        >
          {typeofuser === "Lab Technician" && (
            <LabScreen orderedDetails={orderedDetails} />
          )}
          <div
            className={
              preReportStatus == "previous-Report"
                ? "service-tab-for-prev-report"
                : "service-tab"
            }
          >
            <div className="form-col">
              <Form className="cheif-complaint-form">
                <div className="lab-heading">
                  <Row>
                    <Col md={6}>
                      <h1 className="lab-head">
                        {typeofuser === "Medical Officer" &&
                          "Lab Investigation Report"}
                        {typeofuser === "Lab Technician" &&
                          "Lab Service Report"}
                      </h1>
                      <h6 className="invest-text">
                        {typeofuser === "Lab Technician" &&
                          "You are viewing lab services report for the above patient"}
                      </h6>
                    </Col>
                    <Col md={6} className="text-right">
                      <h1 className="lab-head-id">
                        {typeofuser === "Medical Officer" && (
                          <>Order Id: {orderedDetails[0]?.id}</>
                        )}
                      </h1>
                    </Col>
                  </Row>
                </div>
                <div
                  className={
                    typeofuser === "Medical Officer" ? "" : "view-report"
                  }
                >
                  {typeofuser === "Lab Technician" && (
                    <>
                      <Row>
                        <Col md={6}>
                          <p className="lab-statement pat-det">
                            <b>Patient Details</b> <br />
                            <b>{orderedDetails[0]?.patient?.name}</b>
                          </p>
                        </Col>
                        <Col md={6}>
                          <p className="lab-statement pat-det cons-det">
                            {orderedDetails[0]?.originatedBy == "External"
                              ? "Refered By:"
                              : "Consultant:"}
                            &nbsp;
                            <b>{orderedDetails[0]?.encounter?.staffName}</b>
                          </p>
                        </Col>
                      </Row>
                      <Row>
                        <Col md={4}>
                          <p className="lab-statement pat-det">
                            UHID:&nbsp;
                            {orderedDetails[0]?.patient?.uhid}
                          </p>
                          <p className="lab-statement pat-det">
                            Health ID:&nbsp;
                            {!orderedDetails[0]?.patient?.healthId ? (
                              ""
                            ) : (
                              <>
                                {orderedDetails[0]?.patient?.healthId.replace(
                                  /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                  "$1-$2-$3-$4"
                                )}
                              </>
                            )}
                          </p>
                        </Col>
                        <Col md={3}>
                          <p className="lab-statement pat-det">
                            Age / Gender:&nbsp;
                            <b>
                              {number1 -
                                moment(orderedDetails[0]?.patient?.dob).format(
                                  "YYYY"
                                )}{" "}
                              Years / {orderedDetails[0]?.patient?.gender}
                            </b>
                          </p>
                          <p className="lab-statement pat-det">
                            Mobile Number: +91-
                            {orderedDetails[0]?.patient?.phone}
                          </p>
                        </Col>
                        <Col md={5} className="lab-statement pat-det det-right">
                          <p>
                            Sample collection date & time:&nbsp;{" "}
                            {moment(
                              new Date(orderedDetails[0]?.audit?.dateCreated)
                            ).format("Do MMM YYYY")}
                            ,{" "}
                            {moment(
                              new Date(orderedDetails[0]?.audit?.dateCreated)
                            ).format("hh:mm A")}
                          </p>
                          <p>
                            Rep. date & time:&nbsp;{" "}
                            {moment(
                              new Date(testEnteredData[0]?.sample?.serviceDate)
                            ).format("Do MMM YYYY")}
                            ,{" "}
                            {moment(
                              new Date(testEnteredData[0]?.sample?.serviceDate)
                            ).format("hh:mm A")}
                          </p>
                        </Col>
                        <hr className="lab-line" />
                      </Row>
                    </>
                  )}
                  <div>
                    {/* <Col md={1}></Col>
                    <Col md={11}> */}
                    <Table responsive className="detail-table">
                      <thead>
                        <tr>
                          <th className="print-details">Test Name</th>
                          <th className="print-details head">Observed value</th>
                          <th className="print-details">Units</th>
                          <th className="print-details head">Methodology</th>
                          <th className="print-details head-value">
                            Normal range
                          </th>
                          <th className="print-details">Description</th>
                        </tr>
                      </thead>
                      <tbody>
                        {testEnteredData &&
                          testEnteredData.map((item, index) => (
                            <React.Fragment key={index}>
                              {item?.sample?.resultType == "Profile" ? (
                                <>
                                  <tr>
                                    <td style={{ fontWeight: "bold" }}>
                                      {item?.sample?.labTestName}
                                    </td>
                                  </tr>
                                  {item.results.map((result, i) => (
                                    <React.Fragment key={i}>
                                      <tr>
                                        <td>{result.key}</td>
                                        <td className="print-details-body">
                                          {subTestDataById.map((r, i) => (
                                            <React.Fragment key={i}>
                                              <>
                                                {r.name == result?.key && (
                                                  <>
                                                    {PatientYear > 0 ? (
                                                      <>
                                                        {r.referenceRanges ? (
                                                          <>
                                                            {r.referenceRanges.map(
                                                              (ref, index) => (
                                                                <React.Fragment
                                                                  key={index}
                                                                >
                                                                  {ref.period ==
                                                                    "Year" && (
                                                                    <>
                                                                      {PatientYear >=
                                                                        ref.fromAge &&
                                                                        PatientYear <=
                                                                          ref.toAge && (
                                                                          <>
                                                                            {ref.gender ===
                                                                            "Both" ? (
                                                                              <>
                                                                                {ref.referenceIndicator ===
                                                                                  "BETWEEN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) >
                                                                                      Number(
                                                                                        ref.maxValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "EQUALS" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) ==
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHAN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHAN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                              </>
                                                                            ) : (
                                                                              <>
                                                                                {ref.gender ===
                                                                                  patientData[0]
                                                                                    .patient
                                                                                    ?.gender && (
                                                                                  <>
                                                                                    {ref.referenceIndicator ===
                                                                                      "BETWEEN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) >
                                                                                          Number(
                                                                                            ref.maxValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "EQUALS" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
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
                                                                </React.Fragment>
                                                              )
                                                            )}
                                                          </>
                                                        ) : (
                                                          <span>
                                                            {result.value}
                                                          </span>
                                                        )}
                                                      </>
                                                    ) : (
                                                      <>
                                                        {PatientMonth > 0 &&
                                                        PatientYear < 1 ? (
                                                          <>
                                                            {r.referenceRanges ? (
                                                              <>
                                                                {r.referenceRanges.map(
                                                                  (
                                                                    ref,
                                                                    index
                                                                  ) => (
                                                                    <React.Fragment
                                                                      key={
                                                                        index
                                                                      }
                                                                    >
                                                                      {ref.period ==
                                                                        "Month" && (
                                                                        <>
                                                                          {PatientMonth >=
                                                                            ref.fromAge &&
                                                                            PatientMonth <=
                                                                              ref.toAge && (
                                                                              <>
                                                                                {ref.gender ==
                                                                                "Both" ? (
                                                                                  <>
                                                                                    {ref.referenceIndicator ===
                                                                                      "BETWEEN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) >
                                                                                          Number(
                                                                                            ref.maxValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "EQUALS" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                  </>
                                                                                ) : (
                                                                                  <>
                                                                                    {ref.gender ===
                                                                                      patientData[0]
                                                                                        .patient
                                                                                        ?.gender && (
                                                                                      <>
                                                                                        {ref.referenceIndicator ===
                                                                                          "BETWEEN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) >
                                                                                              Number(
                                                                                                ref.maxValue
                                                                                              ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "EQUALS" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHAN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHAN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
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
                                                                    </React.Fragment>
                                                                  )
                                                                )}
                                                              </>
                                                            ) : (
                                                              <span>
                                                                {result.value}
                                                              </span>
                                                            )}
                                                          </>
                                                        ) : (
                                                          <>
                                                            {PatientMonth <
                                                              1 && (
                                                              <>
                                                                {r.referenceRanges ? (
                                                                  <>
                                                                    {r.referenceRanges.map(
                                                                      (
                                                                        ref,
                                                                        index
                                                                      ) => (
                                                                        <React.Fragment
                                                                          key={
                                                                            index
                                                                          }
                                                                        >
                                                                          {ref.period ==
                                                                            "Day" && (
                                                                            <>
                                                                              {PatientDays >=
                                                                                ref.fromAge &&
                                                                                PatientDays <=
                                                                                  ref.toAge && (
                                                                                  <>
                                                                                    {ref.gender ==
                                                                                    "Both" ? (
                                                                                      <>
                                                                                        {ref.referenceIndicator ===
                                                                                          "BETWEEN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) >
                                                                                              Number(
                                                                                                ref.maxValue
                                                                                              ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "EQUALS" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHAN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHAN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                      </>
                                                                                    ) : (
                                                                                      <>
                                                                                        {ref.gender ==
                                                                                          patientData[0]
                                                                                            .patient
                                                                                            ?.gender && (
                                                                                          <>
                                                                                            {ref.referenceIndicator ===
                                                                                              "BETWEEN" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) <
                                                                                                  Number(
                                                                                                    ref.minValue
                                                                                                  ) ||
                                                                                                Number(
                                                                                                  result.value
                                                                                                ) >
                                                                                                  Number(
                                                                                                    ref.maxValue
                                                                                                  ) ? (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                            {ref.referenceIndicator ===
                                                                                              "EQUALS" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) ==
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
                                                                                                  </span>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                            {ref.referenceIndicator ===
                                                                                              "GREATERTHAN" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) <=
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                            {ref.referenceIndicator ===
                                                                                              "LESSERTHAN" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
                                                                                                  </span>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                            {ref.referenceIndicator ===
                                                                                              "GREATERTHANEQUALTO" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                )}
                                                                                              </>
                                                                                            )}
                                                                                            {ref.referenceIndicator ===
                                                                                              "LESSERTHANEQUALTO" && (
                                                                                              <>
                                                                                                {Number(
                                                                                                  result.value
                                                                                                ) <=
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                  <span>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </span>
                                                                                                ) : (
                                                                                                  <span
                                                                                                    style={{
                                                                                                      color:
                                                                                                        "Red",
                                                                                                    }}
                                                                                                  >
                                                                                                    <b>
                                                                                                      {
                                                                                                        result.value
                                                                                                      }
                                                                                                    </b>
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
                                                                        </React.Fragment>
                                                                      )
                                                                    )}
                                                                  </>
                                                                ) : (
                                                                  <span>
                                                                    {
                                                                      result.value
                                                                    }
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
                                            </React.Fragment>
                                          ))}
                                        </td>
                                        <td>{result.unitOfMeasure}</td>
                                        <td className="print-details-body">
                                          {result.testMethod}
                                        </td>
                                        <td className="print-details-body-value">
                                          {result.normalRange
                                            .split(" ")
                                            .map((items, i) => (
                                              <span
                                                style={{
                                                  display: "inline-flex",
                                                }}
                                                key={i}
                                              >
                                                {items == "EQUALS" ? (
                                                  <span> {"="}&nbsp; </span>
                                                ) : (
                                                  <>
                                                    {items == "GREATERTHAN" ? (
                                                      <span> {">"}&nbsp; </span>
                                                    ) : (
                                                      <>
                                                        {items ==
                                                        "LESSERTHAN" ? (
                                                          <span>
                                                            {" "}
                                                            {"<"}&nbsp;{" "}
                                                          </span>
                                                        ) : (
                                                          <>
                                                            {items ==
                                                            "GREATERTHANEQUALTO" ? (
                                                              <span>
                                                                {" "}
                                                                {
                                                                  ">="
                                                                }&nbsp;{" "}
                                                              </span>
                                                            ) : (
                                                              <>
                                                                {items ==
                                                                "LESSERTHANEQUALTO" ? (
                                                                  <span>
                                                                    {" "}
                                                                    {
                                                                      "<="
                                                                    }&nbsp;{" "}
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
                                                                        {items}
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
                                        </td>
                                        <td style={{ width: "250px" }}>
                                          {result.description}
                                        </td>
                                      </tr>
                                    </React.Fragment>
                                  ))}
                                </>
                              ) : (
                                <>
                                  {item.results.map((result, i) => (
                                    <React.Fragment key={i}>
                                      <tr>
                                        <td>{result.key}</td>
                                        <td className="print-details-body">
                                          {singleDataByID.map((r, i) => (
                                            <React.Fragment key={i}>
                                              {r.name ==
                                                item?.sample?.labTestName && (
                                                <>
                                                  {PatientYear > 0 ? (
                                                    <>
                                                      {r.referenceRanges ? (
                                                        <>
                                                          {r.referenceRanges.map(
                                                            (ref, index) => (
                                                              <React.Fragment
                                                                key={index}
                                                              >
                                                                {ref.period ==
                                                                  "Year" && (
                                                                  <>
                                                                    {PatientYear >=
                                                                      ref.fromAge &&
                                                                      PatientYear <=
                                                                        ref.toAge && (
                                                                        <>
                                                                          {ref.gender ===
                                                                          "Both" ? (
                                                                            <>
                                                                              {ref.referenceIndicator ===
                                                                                "BETWEEN" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) >
                                                                                    Number(
                                                                                      ref.maxValue
                                                                                    ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "EQUALS" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) ==
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "GREATERTHAN" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <=
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "LESSERTHAN" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "GREATERTHANEQUALTO" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "LESSERTHANEQUALTO" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <=
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                            </>
                                                                          ) : (
                                                                            <>
                                                                              {ref.gender ===
                                                                                patientData[0]
                                                                                  .patient
                                                                                  ?.gender && (
                                                                                <>
                                                                                  {ref.referenceIndicator ===
                                                                                    "BETWEEN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) >
                                                                                        Number(
                                                                                          ref.maxValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "EQUALS" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
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
                                                              </React.Fragment>
                                                            )
                                                          )}
                                                        </>
                                                      ) : (
                                                        <span>
                                                          {result.value}
                                                        </span>
                                                      )}
                                                    </>
                                                  ) : (
                                                    <>
                                                      {PatientMonth > 0 &&
                                                      PatientYear < 1 ? (
                                                        <>
                                                          {r.referenceRanges ? (
                                                            <>
                                                              {r.referenceRanges.map(
                                                                (
                                                                  ref,
                                                                  index
                                                                ) => (
                                                                  <React.Fragment
                                                                    key={index}
                                                                  >
                                                                    {ref.period ==
                                                                      "Month" && (
                                                                      <>
                                                                        {PatientMonth >=
                                                                          ref.fromAge &&
                                                                          PatientMonth <=
                                                                            ref.toAge && (
                                                                            <>
                                                                              {ref.gender ==
                                                                              "Both" ? (
                                                                                <>
                                                                                  {ref.referenceIndicator ===
                                                                                    "BETWEEN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) >
                                                                                        Number(
                                                                                          ref.maxValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "EQUALS" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                </>
                                                                              ) : (
                                                                                <>
                                                                                  {ref.gender ===
                                                                                    patientData[0]
                                                                                      .patient
                                                                                      ?.gender && (
                                                                                    <>
                                                                                      {ref.referenceIndicator ===
                                                                                        "BETWEEN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) >
                                                                                            Number(
                                                                                              ref.maxValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "EQUALS" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
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
                                                                  </React.Fragment>
                                                                )
                                                              )}
                                                            </>
                                                          ) : (
                                                            <span>
                                                              {result.value}
                                                            </span>
                                                          )}
                                                        </>
                                                      ) : (
                                                        <>
                                                          {PatientMonth < 1 && (
                                                            <>
                                                              {r.referenceRanges ? (
                                                                <>
                                                                  {r.referenceRanges.map(
                                                                    (
                                                                      ref,
                                                                      index
                                                                    ) => (
                                                                      <React.Fragment
                                                                        key={
                                                                          index
                                                                        }
                                                                      >
                                                                        {ref.period ==
                                                                          "Day" && (
                                                                          <>
                                                                            {PatientDays >=
                                                                              ref.fromAge &&
                                                                              PatientDays <=
                                                                                ref.toAge && (
                                                                                <>
                                                                                  {ref.gender ==
                                                                                  "Both" ? (
                                                                                    <>
                                                                                      {ref.referenceIndicator ===
                                                                                        "BETWEEN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) >
                                                                                            Number(
                                                                                              ref.maxValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "EQUALS" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                    </>
                                                                                  ) : (
                                                                                    <>
                                                                                      {ref.gender ==
                                                                                        patientData[0]
                                                                                          .patient
                                                                                          ?.gender && (
                                                                                        <>
                                                                                          {ref.referenceIndicator ===
                                                                                            "BETWEEN" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) >
                                                                                                Number(
                                                                                                  ref.maxValue
                                                                                                ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "EQUALS" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) ==
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "GREATERTHAN" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "LESSERTHAN" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "GREATERTHANEQUALTO" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "LESSERTHANEQUALTO" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <=
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
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
                                                                      </React.Fragment>
                                                                    )
                                                                  )}
                                                                </>
                                                              ) : (
                                                                <span>
                                                                  {result.value}
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
                                              {/* <>{result.value}</> */}
                                            </React.Fragment>
                                          ))}
                                        </td>
                                        <td>{result.unitOfMeasure}</td>
                                        <td className="print-details-body">
                                          {result.testMethod}
                                        </td>
                                        <td className="print-details-body-value">
                                          {result.normalRange
                                            .split(" ")
                                            .map((items, i) => (
                                              <span
                                                style={{
                                                  display: "inline-flex",
                                                }}
                                                key={i}
                                              >
                                                {items == "EQUALS" ? (
                                                  <span> {"="}&nbsp; </span>
                                                ) : (
                                                  <>
                                                    {items == "GREATERTHAN" ? (
                                                      <span> {">"}&nbsp; </span>
                                                    ) : (
                                                      <>
                                                        {items ==
                                                        "LESSERTHAN" ? (
                                                          <span>
                                                            {" "}
                                                            {"<"}&nbsp;{" "}
                                                          </span>
                                                        ) : (
                                                          <>
                                                            {items ==
                                                            "GREATERTHANEQUALTO" ? (
                                                              <span>
                                                                {" "}
                                                                {
                                                                  ">="
                                                                }&nbsp;{" "}
                                                              </span>
                                                            ) : (
                                                              <>
                                                                {items ==
                                                                "LESSERTHANEQUALTO" ? (
                                                                  <span>
                                                                    {" "}
                                                                    {
                                                                      "<="
                                                                    }&nbsp;{" "}
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
                                                                        {items}
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
                                        </td>
                                        <td style={{ width: "250px" }}>
                                          {result.description}
                                        </td>
                                      </tr>
                                    </React.Fragment>
                                  ))}
                                </>
                              )}
                            </React.Fragment>
                          ))}
                      </tbody>
                    </Table>
                    <div className="print-btn-pat-details">
                      <Button
                        variant="light"
                        className={
                          (typeofuser === "Lab Technician" &&
                            "lab-save-btn view-reports") ||
                          (typeofuser === "Medical Officer" &&
                            "mo-report-btn view-reports")
                        }
                        onClick={descShow}
                      >
                        <i className="bi bi-printer"></i>&nbsp;Print
                      </Button>
                    </div>
                    {/* </Col> */}
                  </div>
                </div>
              </Form>
            </div>
            <Modal show={descshow} onHide={descClose} className="lab-print-div">
              <Row>
                <Col md={6}>
                  <h1 className="presc-details-header lab-header">{phc}</h1>
                  {/* <p className="address-details">
                    Address comes here <br />
                    Address comes here if more than one line
                  </p> */}
                </Col>
                <Col md={6} align="right">
                  <button
                    onClick={descClose}
                    className="bi bi-x close-popup"
                  ></button>
                </Col>
                <hr className="phc-hr" />
              </Row>
              <div className="lab-body">
                <Row>
                  <Col md={6}>
                    <p className="lab-statement pat-det">
                      <b>Patient Details</b> <br />
                      <b>{orderedDetails[0]?.patient?.name}</b>
                    </p>
                  </Col>
                  <Col md={6}>
                    <p className="lab-statement pat-det cons-det">
                      {orderedDetails[0]?.originatedBy == "External"
                        ? "Refered By:"
                        : "Consultant:"}
                      &nbsp;
                      <b>{orderedDetails[0]?.encounter?.staffName}</b>
                    </p>
                  </Col>
                </Row>
                <Row>
                  <Col md={4}>
                    <p className="lab-statement pat-det">
                      UHID:&nbsp;
                      {orderedDetails[0]?.patient?.uhid}
                    </p>
                    <p className="lab-statement pat-det">
                      Health ID:&nbsp;
                      {!orderedDetails[0]?.patient?.healthId ? (
                        ""
                      ) : (
                        <>
                          {orderedDetails[0]?.patient?.healthId.replace(
                            /(\d{2})(\d{4})(\d{4})(\d{4})/,
                            "$1-$2-$3-$4"
                          )}
                        </>
                      )}
                    </p>
                  </Col>
                  <Col md={3}>
                    <p className="lab-statement pat-det">
                      Age / Gender:&nbsp;
                      <b>
                        {number1 -
                          moment(orderedDetails[0]?.patient?.dob).format(
                            "YYYY"
                          )}{" "}
                        Years / {orderedDetails[0]?.patient?.gender}
                      </b>
                    </p>
                    <p className="lab-statement pat-det">
                      Mobile Number: +91-{orderedDetails[0]?.patient?.phone}
                    </p>
                  </Col>
                  <Col md={5} className="lab-statement pat-det det-right">
                    <p>
                      Sample collection date & time:&nbsp;{" "}
                      {moment(
                        new Date(orderedDetails[0]?.audit?.dateCreated)
                      ).format("Do MMM YYYY")}
                      ,{" "}
                      {moment(
                        new Date(orderedDetails[0]?.audit?.dateCreated)
                      ).format("hh:mm A")}
                    </p>
                    <p>
                      Rep. date & time:&nbsp;{" "}
                      {moment(
                        new Date(testEnteredData[0]?.sample?.serviceDate)
                      ).format("Do MMM YYYY")}
                      ,{" "}
                      {moment(
                        new Date(testEnteredData[0]?.sample?.serviceDate)
                      ).format("hh:mm A")}
                    </p>
                  </Col>
                  <hr className="lab-line" />
                </Row>
                <div>
                  <h1 className="lab-invest">
                    Laboratory Investigation Report
                  </h1>
                  <Table responsive className="detail-table">
                    <thead>
                      <tr>
                        <th className="print-details">Test Name</th>
                        <th className="print-details head">Observed value</th>
                        <th className="print-details">Units</th>
                        <th className="print-details head">Methodology</th>
                        <th className="print-details head-value">
                          Reference range
                        </th>
                        <th className="print-details">Description</th>
                      </tr>
                    </thead>
                    <tbody>
                      {testEnteredData &&
                        testEnteredData.map((item, index) => (
                          <React.Fragment key={index}>
                            {item?.sample?.resultType == "Profile" ? (
                              <>
                                <tr>
                                  <td style={{ fontWeight: "bold" }}>
                                    {item?.sample?.labTestName}
                                  </td>
                                </tr>
                                {item.results.map((result, i) => (
                                  <React.Fragment key={i}>
                                    <tr>
                                      <td>{result.key}</td>
                                      <td className="print-details-body">
                                        {subTestDataById.map((r, i) => (
                                          <React.Fragment key={i}>
                                            <>
                                              {r.name == result?.key && (
                                                <>
                                                  {PatientYear > 0 ? (
                                                    <>
                                                      {r.referenceRanges ? (
                                                        <>
                                                          {r.referenceRanges.map(
                                                            (ref, index) => (
                                                              <React.Fragment
                                                                key={index}
                                                              >
                                                                {ref.period ==
                                                                  "Year" && (
                                                                  <>
                                                                    {PatientYear >=
                                                                      ref.fromAge &&
                                                                      PatientYear <=
                                                                        ref.toAge && (
                                                                        <>
                                                                          {ref.gender ===
                                                                          "Both" ? (
                                                                            <>
                                                                              {ref.referenceIndicator ===
                                                                                "BETWEEN" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) >
                                                                                    Number(
                                                                                      ref.maxValue
                                                                                    ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "EQUALS" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) ==
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) ==
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "GREATERTHAN" && (
                                                                                <>
                                                                                  {ref.minValue &&
                                                                                  Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "LESSERTHAN" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "GREATERTHANEQUALTO" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) <
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                              {ref.referenceIndicator ===
                                                                                "LESSERTHANEQUALTO" && (
                                                                                <>
                                                                                  {Number(
                                                                                    result.value
                                                                                  ) <=
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ||
                                                                                  Number(
                                                                                    result.value
                                                                                  ) <=
                                                                                    Number(
                                                                                      ref.minValue
                                                                                    ) ? (
                                                                                    <span>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </span>
                                                                                  ) : (
                                                                                    <span
                                                                                      style={{
                                                                                        color:
                                                                                          "Red",
                                                                                      }}
                                                                                    >
                                                                                      <b>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </b>
                                                                                    </span>
                                                                                  )}
                                                                                </>
                                                                              )}
                                                                            </>
                                                                          ) : (
                                                                            <>
                                                                              {ref.gender ===
                                                                                patientData[0]
                                                                                  .patient
                                                                                  ?.gender && (
                                                                                <>
                                                                                  {ref.referenceIndicator ===
                                                                                    "BETWEEN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) >
                                                                                        Number(
                                                                                          ref.maxValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "EQUALS" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHAN" && (
                                                                                    <>
                                                                                      {ref.minValue &&
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
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
                                                              </React.Fragment>
                                                            )
                                                          )}
                                                        </>
                                                      ) : (
                                                        <span>
                                                          {result.value}
                                                        </span>
                                                      )}
                                                    </>
                                                  ) : (
                                                    <>
                                                      {PatientMonth > 0 &&
                                                      PatientYear < 1 ? (
                                                        <>
                                                          {r.referenceRanges ? (
                                                            <>
                                                              {r.referenceRanges.map(
                                                                (
                                                                  ref,
                                                                  index
                                                                ) => (
                                                                  <React.Fragment
                                                                    key={index}
                                                                  >
                                                                    {ref.period ==
                                                                      "Month" && (
                                                                      <>
                                                                        {PatientMonth >=
                                                                          ref.fromAge &&
                                                                          PatientMonth <=
                                                                            ref.toAge && (
                                                                            <>
                                                                              {ref.gender ==
                                                                              "Both" ? (
                                                                                <>
                                                                                  {ref.referenceIndicator ===
                                                                                    "BETWEEN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) >
                                                                                        Number(
                                                                                          ref.maxValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "EQUALS" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) ==
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHAN" && (
                                                                                    <>
                                                                                      {ref.minValue &&
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHAN" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "GREATERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                  {ref.referenceIndicator ===
                                                                                    "LESSERTHANEQUALTO" && (
                                                                                    <>
                                                                                      {Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ||
                                                                                      Number(
                                                                                        result.value
                                                                                      ) <=
                                                                                        Number(
                                                                                          ref.minValue
                                                                                        ) ? (
                                                                                        <span>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </span>
                                                                                      ) : (
                                                                                        <span
                                                                                          style={{
                                                                                            color:
                                                                                              "Red",
                                                                                          }}
                                                                                        >
                                                                                          <b>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </b>
                                                                                        </span>
                                                                                      )}
                                                                                    </>
                                                                                  )}
                                                                                </>
                                                                              ) : (
                                                                                <>
                                                                                  {ref.gender ===
                                                                                    patientData[0]
                                                                                      .patient
                                                                                      ?.gender && (
                                                                                    <>
                                                                                      {ref.referenceIndicator ===
                                                                                        "BETWEEN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) >
                                                                                            Number(
                                                                                              ref.maxValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "EQUALS" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHAN" && (
                                                                                        <>
                                                                                          {ref.minValue &&
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
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
                                                                  </React.Fragment>
                                                                )
                                                              )}
                                                            </>
                                                          ) : (
                                                            <span>
                                                              {result.value}
                                                            </span>
                                                          )}
                                                        </>
                                                      ) : (
                                                        <>
                                                          {PatientMonth < 1 && (
                                                            <>
                                                              {r.referenceRanges ? (
                                                                <>
                                                                  {r.referenceRanges.map(
                                                                    (
                                                                      ref,
                                                                      index
                                                                    ) => (
                                                                      <React.Fragment
                                                                        key={
                                                                          index
                                                                        }
                                                                      >
                                                                        {ref.period ==
                                                                          "Day" && (
                                                                          <>
                                                                            {PatientDays >=
                                                                              ref.fromAge &&
                                                                              PatientDays <=
                                                                                ref.toAge && (
                                                                                <>
                                                                                  {ref.gender ==
                                                                                  "Both" ? (
                                                                                    <>
                                                                                      {ref.referenceIndicator ===
                                                                                        "BETWEEN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) >
                                                                                            Number(
                                                                                              ref.maxValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "EQUALS" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) ==
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHAN" && (
                                                                                        <>
                                                                                          {ref.minValue &&
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHAN" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "GREATERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                      {ref.referenceIndicator ===
                                                                                        "LESSERTHANEQUALTO" && (
                                                                                        <>
                                                                                          {Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ||
                                                                                          Number(
                                                                                            result.value
                                                                                          ) <=
                                                                                            Number(
                                                                                              ref.minValue
                                                                                            ) ? (
                                                                                            <span>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </span>
                                                                                          ) : (
                                                                                            <span
                                                                                              style={{
                                                                                                color:
                                                                                                  "Red",
                                                                                              }}
                                                                                            >
                                                                                              <b>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </b>
                                                                                            </span>
                                                                                          )}
                                                                                        </>
                                                                                      )}
                                                                                    </>
                                                                                  ) : (
                                                                                    <>
                                                                                      {ref.gender ==
                                                                                        patientData[0]
                                                                                          .patient
                                                                                          ?.gender && (
                                                                                        <>
                                                                                          {ref.referenceIndicator ===
                                                                                            "BETWEEN" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) >
                                                                                                Number(
                                                                                                  ref.maxValue
                                                                                                ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "EQUALS" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) ==
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) ==
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "GREATERTHAN" && (
                                                                                            <>
                                                                                              {ref.minValue &&
                                                                                              Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "LESSERTHAN" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "GREATERTHANEQUALTO" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) <
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              )}
                                                                                            </>
                                                                                          )}
                                                                                          {ref.referenceIndicator ===
                                                                                            "LESSERTHANEQUALTO" && (
                                                                                            <>
                                                                                              {Number(
                                                                                                result.value
                                                                                              ) <=
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ||
                                                                                              Number(
                                                                                                result.value
                                                                                              ) <=
                                                                                                Number(
                                                                                                  ref.minValue
                                                                                                ) ? (
                                                                                                <span>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </span>
                                                                                              ) : (
                                                                                                <span
                                                                                                  style={{
                                                                                                    color:
                                                                                                      "Red",
                                                                                                  }}
                                                                                                >
                                                                                                  <b>
                                                                                                    {
                                                                                                      result.value
                                                                                                    }
                                                                                                  </b>
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
                                                                      </React.Fragment>
                                                                    )
                                                                  )}
                                                                </>
                                                              ) : (
                                                                <span>
                                                                  {result.value}
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
                                          </React.Fragment>
                                        ))}
                                      </td>
                                      <td>{result.unitOfMeasure}</td>
                                      <td className="print-details-body">
                                        {result.testMethod}
                                      </td>
                                      <td className="print-details-body-value">
                                        {result.normalRange
                                          .split(" ")
                                          .map((items, i) => (
                                            <span
                                              style={{ display: "inline-flex" }}
                                              key={i}
                                            >
                                              {items == "EQUALS" ? (
                                                <span> {"="}&nbsp; </span>
                                              ) : (
                                                <>
                                                  {items == "GREATERTHAN" ? (
                                                    <span> {">"}&nbsp; </span>
                                                  ) : (
                                                    <>
                                                      {items == "LESSERTHAN" ? (
                                                        <span>
                                                          {" "}
                                                          {"<"}&nbsp;{" "}
                                                        </span>
                                                      ) : (
                                                        <>
                                                          {items ==
                                                          "GREATERTHANEQUALTO" ? (
                                                            <span>
                                                              {" "}
                                                              {">="}&nbsp;{" "}
                                                            </span>
                                                          ) : (
                                                            <>
                                                              {items ==
                                                              "LESSERTHANEQUALTO" ? (
                                                                <span>
                                                                  {" "}
                                                                  {
                                                                    "<="
                                                                  }&nbsp;{" "}
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
                                                                      {items}
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
                                      </td>
                                      <td style={{ width: "250px" }}>
                                        {result.description}
                                      </td>
                                    </tr>
                                  </React.Fragment>
                                ))}
                              </>
                            ) : (
                              <>
                                {item.results.map((result, i) => (
                                  <React.Fragment key={i}>
                                    <tr>
                                      <td>{result.key}</td>
                                      <td className="print-details-body">
                                        {singleDataByID.map((r, i) => (
                                          <React.Fragment key={i}>
                                            {r.name ==
                                              item?.sample?.labTestName && (
                                              <>
                                                {PatientYear > 0 ? (
                                                  <>
                                                    {r.referenceRanges ? (
                                                      <>
                                                        {r.referenceRanges.map(
                                                          (ref, index) => (
                                                            <React.Fragment
                                                              key={index}
                                                            >
                                                              {ref.period ==
                                                                "Year" && (
                                                                <>
                                                                  {PatientYear >=
                                                                    ref.fromAge &&
                                                                    PatientYear <=
                                                                      ref.toAge && (
                                                                      <>
                                                                        {ref.gender ===
                                                                        "Both" ? (
                                                                          <>
                                                                            {ref.referenceIndicator ===
                                                                              "BETWEEN" && (
                                                                              <>
                                                                                {Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ||
                                                                                Number(
                                                                                  result.value
                                                                                ) >
                                                                                  Number(
                                                                                    ref.maxValue
                                                                                  ) ? (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                ) : (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                            {ref.referenceIndicator ===
                                                                              "EQUALS" && (
                                                                              <>
                                                                                {Number(
                                                                                  result.value
                                                                                ) ==
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ||
                                                                                Number(
                                                                                  result.value
                                                                                ) ==
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                ) : (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                            {ref.referenceIndicator ===
                                                                              "GREATERTHAN" && (
                                                                              <>
                                                                                {ref.minValue &&
                                                                                Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                ) : (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                            {ref.referenceIndicator ===
                                                                              "LESSERTHAN" && (
                                                                              <>
                                                                                {Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ||
                                                                                Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                ) : (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                            {ref.referenceIndicator ===
                                                                              "GREATERTHANEQUALTO" && (
                                                                              <>
                                                                                {Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ||
                                                                                Number(
                                                                                  result.value
                                                                                ) <
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                ) : (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                            {ref.referenceIndicator ===
                                                                              "LESSERTHANEQUALTO" && (
                                                                              <>
                                                                                {Number(
                                                                                  result.value
                                                                                ) <=
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ||
                                                                                Number(
                                                                                  result.value
                                                                                ) <=
                                                                                  Number(
                                                                                    ref.minValue
                                                                                  ) ? (
                                                                                  <span>
                                                                                    {
                                                                                      result.value
                                                                                    }
                                                                                  </span>
                                                                                ) : (
                                                                                  <span
                                                                                    style={{
                                                                                      color:
                                                                                        "Red",
                                                                                    }}
                                                                                  >
                                                                                    <b>
                                                                                      {
                                                                                        result.value
                                                                                      }
                                                                                    </b>
                                                                                  </span>
                                                                                )}
                                                                              </>
                                                                            )}
                                                                          </>
                                                                        ) : (
                                                                          <>
                                                                            {ref.gender ===
                                                                              patientData[0]
                                                                                .patient
                                                                                ?.gender && (
                                                                              <>
                                                                                {ref.referenceIndicator ===
                                                                                  "BETWEEN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) >
                                                                                      Number(
                                                                                        ref.maxValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "EQUALS" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHAN" && (
                                                                                  <>
                                                                                    {ref.minValue &&
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHAN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
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
                                                            </React.Fragment>
                                                          )
                                                        )}
                                                      </>
                                                    ) : (
                                                      <span>
                                                        {result.value}
                                                      </span>
                                                    )}
                                                  </>
                                                ) : (
                                                  <>
                                                    {PatientMonth > 0 &&
                                                    PatientYear < 1 ? (
                                                      <>
                                                        {r.referenceRanges ? (
                                                          <>
                                                            {r.referenceRanges.map(
                                                              (ref, index) => (
                                                                <React.Fragment
                                                                  key={index}
                                                                >
                                                                  {ref.period ==
                                                                    "Month" && (
                                                                    <>
                                                                      {PatientMonth >=
                                                                        ref.fromAge &&
                                                                        PatientMonth <=
                                                                          ref.toAge && (
                                                                          <>
                                                                            {ref.gender ==
                                                                            "Both" ? (
                                                                              <>
                                                                                {ref.referenceIndicator ===
                                                                                  "BETWEEN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) >
                                                                                      Number(
                                                                                        ref.maxValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "EQUALS" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) ==
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHAN" && (
                                                                                  <>
                                                                                    {ref.minValue &&
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHAN" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "GREATERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                                {ref.referenceIndicator ===
                                                                                  "LESSERTHANEQUALTO" && (
                                                                                  <>
                                                                                    {Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ||
                                                                                    Number(
                                                                                      result.value
                                                                                    ) <=
                                                                                      Number(
                                                                                        ref.minValue
                                                                                      ) ? (
                                                                                      <span>
                                                                                        {
                                                                                          result.value
                                                                                        }
                                                                                      </span>
                                                                                    ) : (
                                                                                      <span
                                                                                        style={{
                                                                                          color:
                                                                                            "Red",
                                                                                        }}
                                                                                      >
                                                                                        <b>
                                                                                          {
                                                                                            result.value
                                                                                          }
                                                                                        </b>
                                                                                      </span>
                                                                                    )}
                                                                                  </>
                                                                                )}
                                                                              </>
                                                                            ) : (
                                                                              <>
                                                                                {ref.gender ===
                                                                                  patientData[0]
                                                                                    .patient
                                                                                    ?.gender && (
                                                                                  <>
                                                                                    {ref.referenceIndicator ===
                                                                                      "BETWEEN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) >
                                                                                          Number(
                                                                                            ref.maxValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "EQUALS" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHAN" && (
                                                                                      <>
                                                                                        {ref.minValue &&
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
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
                                                                </React.Fragment>
                                                              )
                                                            )}
                                                          </>
                                                        ) : (
                                                          <span>
                                                            {result.value}
                                                          </span>
                                                        )}
                                                      </>
                                                    ) : (
                                                      <>
                                                        {PatientMonth < 1 && (
                                                          <>
                                                            {r.referenceRanges ? (
                                                              <>
                                                                {r.referenceRanges.map(
                                                                  (
                                                                    ref,
                                                                    index
                                                                  ) => (
                                                                    <React.Fragment
                                                                      key={
                                                                        index
                                                                      }
                                                                    >
                                                                      {ref.period ==
                                                                        "Day" && (
                                                                        <>
                                                                          {PatientDays >=
                                                                            ref.fromAge &&
                                                                            PatientDays <=
                                                                              ref.toAge && (
                                                                              <>
                                                                                {ref.gender ==
                                                                                "Both" ? (
                                                                                  <>
                                                                                    {ref.referenceIndicator ===
                                                                                      "BETWEEN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) >
                                                                                          Number(
                                                                                            ref.maxValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "EQUALS" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) ==
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHAN" && (
                                                                                      <>
                                                                                        {ref.minValue &&
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHAN" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "GREATERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                    {ref.referenceIndicator ===
                                                                                      "LESSERTHANEQUALTO" && (
                                                                                      <>
                                                                                        {Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ||
                                                                                        Number(
                                                                                          result.value
                                                                                        ) <=
                                                                                          Number(
                                                                                            ref.minValue
                                                                                          ) ? (
                                                                                          <span>
                                                                                            {
                                                                                              result.value
                                                                                            }
                                                                                          </span>
                                                                                        ) : (
                                                                                          <span
                                                                                            style={{
                                                                                              color:
                                                                                                "Red",
                                                                                            }}
                                                                                          >
                                                                                            <b>
                                                                                              {
                                                                                                result.value
                                                                                              }
                                                                                            </b>
                                                                                          </span>
                                                                                        )}
                                                                                      </>
                                                                                    )}
                                                                                  </>
                                                                                ) : (
                                                                                  <>
                                                                                    {ref.gender ==
                                                                                      patientData[0]
                                                                                        .patient
                                                                                        ?.gender && (
                                                                                      <>
                                                                                        {ref.referenceIndicator ===
                                                                                          "BETWEEN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) >
                                                                                              Number(
                                                                                                ref.maxValue
                                                                                              ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "EQUALS" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) ==
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) ==
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHAN" && (
                                                                                          <>
                                                                                            {ref.minValue &&
                                                                                            Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHAN" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "GREATERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) <
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            )}
                                                                                          </>
                                                                                        )}
                                                                                        {ref.referenceIndicator ===
                                                                                          "LESSERTHANEQUALTO" && (
                                                                                          <>
                                                                                            {Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ||
                                                                                            Number(
                                                                                              result.value
                                                                                            ) <=
                                                                                              Number(
                                                                                                ref.minValue
                                                                                              ) ? (
                                                                                              <span>
                                                                                                {
                                                                                                  result.value
                                                                                                }
                                                                                              </span>
                                                                                            ) : (
                                                                                              <span
                                                                                                style={{
                                                                                                  color:
                                                                                                    "Red",
                                                                                                }}
                                                                                              >
                                                                                                <b>
                                                                                                  {
                                                                                                    result.value
                                                                                                  }
                                                                                                </b>
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
                                                                    </React.Fragment>
                                                                  )
                                                                )}
                                                              </>
                                                            ) : (
                                                              <span>
                                                                {result.value}
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
                                            {/* <>{result.value}</> */}
                                          </React.Fragment>
                                        ))}
                                      </td>
                                      <td>{result.unitOfMeasure}</td>
                                      <td className="print-details-body">
                                        {result.testMethod}
                                      </td>
                                      <td className="print-details-body-value">
                                        {result.normalRange
                                          .split(" ")
                                          .map((items, i) => (
                                            <span
                                              style={{ display: "inline-flex" }}
                                              key={i}
                                            >
                                              {items == "EQUALS" ? (
                                                <span> {"="}&nbsp; </span>
                                              ) : (
                                                <>
                                                  {items == "GREATERTHAN" ? (
                                                    <span> {">"}&nbsp; </span>
                                                  ) : (
                                                    <>
                                                      {items == "LESSERTHAN" ? (
                                                        <span>
                                                          {" "}
                                                          {"<"}&nbsp;{" "}
                                                        </span>
                                                      ) : (
                                                        <>
                                                          {items ==
                                                          "GREATERTHANEQUALTO" ? (
                                                            <span>
                                                              {" "}
                                                              {">="}&nbsp;{" "}
                                                            </span>
                                                          ) : (
                                                            <>
                                                              {items ==
                                                              "LESSERTHANEQUALTO" ? (
                                                                <span>
                                                                  {" "}
                                                                  {
                                                                    "<="
                                                                  }&nbsp;{" "}
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
                                                                      {items}
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
                                      </td>
                                      <td style={{ width: "250px" }}>
                                        {result.description}
                                      </td>
                                    </tr>
                                  </React.Fragment>
                                ))}
                              </>
                            )}
                          </React.Fragment>
                        ))}
                    </tbody>
                  </Table>
                  <div className="mo-sign">
                    <p>Medical Officer Signature / Stamp</p>
                  </div>
                  <hr />
                </div>
              </div>
              <Modal.Footer className="prescription-button">
                <Button
                  variant="outline-secondary"
                  className="prescription-cancel-btn"
                  onClick={descClose}
                >
                  Cancel
                </Button>
                <Button variant="light" className="prescription-save-btn">
                  Print
                </Button>
              </Modal.Footer>
            </Modal>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
