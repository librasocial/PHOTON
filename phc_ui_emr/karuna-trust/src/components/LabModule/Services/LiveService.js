import { Button, Row, Col, Form } from "react-bootstrap";
import AddLabTest from "../ModalPopUpForms/AddLabTest";
import React, { useState, useEffect } from "react";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from "@mui/material";
import "../../../css/Services.css";
import ReferanceRange from "../ModalPopUpForms/ReferanceRange";
import Sidemenu from "../../Sidemenus";
import SaveButton from "../../EMR_Buttons/SaveButton";

let TestDataDisplay = [];
let searchValue;
function LiveService() {
  const [labId, setLabId] = useState("");
  const [addlabService, setAddlabService] = useState(false);
  const addlabServiceClose = () => {
    setAddlabService(false);
    setLabId("");
  };
  const addlabServiceShow = () => {
    setAddlabService(true);
  };
  const [status, setStatus] = useState("");
  const [labTestData1, setLabTestData] = useState([]);
  const labTestData = labTestData1
    .slice()
    .sort((a, b) => (a.date > b.date ? 1 : -1));

  // fetching all lab test data
  useEffect(() => {
    document.title = "EMR Lab Master";
    fetch(
      `${constant.ApiUrl}/lab-tests-svc/labtestservices/filter?page=&size=10000000`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        const obj = [
          ...new Map(
            res["data"].map((item) => [
              JSON.stringify(item.name, item.group),
              item,
            ])
          ).values(),
        ];

        setLabTestData(obj);
        setStatus(false);
      });
  }, [status, useState]);
  // fetching all lab test data

  // material table
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const addReferance = (e) => {
    setReferanceModal(true);
    setLabId(e);
  };

  const editTestdata = (e) => {
    addlabServiceShow(true);
    setLabId(e);
    // sessionStorage.removeItem("editLabTest", e)
  };

  const [referanceModal, setReferanceModal] = useState(false);
  const closeReferenceModal = () => {
    setReferanceModal(false);
    setLabId("");
  };
  // material table

  // code for searching Tests
  const [filteredResult, setFilteredResults] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const [searchType, setSearchType] = useState("Test Name");

  if (searchValue != "" || filteredResult.length != 0) {
    TestDataDisplay = filteredResult;
  } else {
    TestDataDisplay = labTestData;
  }

  const searchItems = (e) => {
    if (e.target.value == "") {
      setSearchValue(e.target.value);
    } else {
      setSearchValue(e.target.value);
    }
    const searchKeyWord = e.target.value;
    if (searchKeyWord !== "") {
      if (searchType == "Test Name") {
        const filteredData = labTestData.filter((item) => {
          return item.name.toLowerCase().includes(searchKeyWord.toLowerCase());
        });
        setFilteredResults(filteredData);
      } else if (searchType == "Test Code") {
        const filteredData = labTestData.filter((item) => {
          return item.code.toLowerCase().includes(searchKeyWord.toLowerCase());
        });
        setFilteredResults(filteredData);
      }
    } else {
      setFilteredResults(labTestData);
    }
  };
  // code for searching Tests

  return (
    <Row className="main-div ">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <div className="regHeader">
          <h1 className="register-Header">Lab Master</h1>
        </div>
        <hr style={{ margin: "0px" }} />
        <div className="lab-service-tab">
          {/* {labId ? */}
          <AddLabTest
            addlabService={addlabService}
            addlabServiceClose={addlabServiceClose}
            labId={labId}
          />
          <ReferanceRange
            referanceModal={referanceModal}
            closeReferenceModal={closeReferenceModal}
            labId={labId}
          />

          <Row>
            <Col md={3}>
              <h3 className="dia-heading">Diagnostic Services</h3>
            </Col>
            <Col md={2}>
              <div>
                <Form.Group>
                  <Form.Select
                    aria-label="Default select example"
                    placeholder="Select category"
                    className="regDropdown"
                    value={searchType}
                    name="testType"
                    onChange={(e) => setSearchType(e.target.value)}
                  >
                    <option value="Test Name">Test Name</option>
                    <option value="Test Code">Test Code</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>
            <Col md={4}>
              <div className="form-group ward-search">
                <input
                  type="text"
                  className="form-control "
                  placeholder="Search Test Here"
                  id="search-input"
                  onChange={searchItems}
                />
                {/* <span className="fa fa-search form-control-feedback"></span> */}
              </div>
            </Col>
            {labTestData && (
              <Col md={3} className="text-right">
                <SaveButton
                  butttonClick={addlabServiceShow}
                  class_name="regBtnPC"
                  button_name="Add Lab Services"
                />
                {/* <Button variant="outline-secondary" className="add-lab-servive-btn" onClick={addlabServiceShow}>
                  Add Lab Services
                </Button> */}
              </Col>
            )}
          </Row>
          {!labTestData ? (
            <div className="lab-service-data-field">
              <div className="service-visible-screen">
                <img src="./img/info-card1.svg" alt="empty data" />
                <div className="service-btn">
                  <Button
                    variant="outline-secondary"
                    className="add-lab-servive-btn"
                    onClick={addlabServiceShow}
                  >
                    Configure diagnostic services
                  </Button>
                </div>
              </div>
            </div>
          ) : (
            <div className="lab-service-data-field">
              <Paper sx={{ width: "100%", overflow: "hidden" }}>
                <TableContainer sx={{ maxHeight: 440 }}>
                  <Table stickyHeader aria-label="sticky table">
                    <TableHead className="lab-test-table">
                      <TableRow>
                        <TableCell align="center" style={{ width: "15%" }}>
                          Test Code
                        </TableCell>
                        <TableCell align="center" style={{ width: "20%" }}>
                          Test Name
                        </TableCell>
                        <TableCell align="center" style={{ width: "15%" }}>
                          Test Group
                        </TableCell>
                        <TableCell align="center" style={{ width: "15%" }}>
                          Unit Of Measurement
                        </TableCell>
                        <TableCell align="center" style={{ width: "15%" }}>
                          Result Type
                        </TableCell>
                        <TableCell align="center" style={{ width: "10%" }}>
                          Status
                        </TableCell>
                        <TableCell align="center" style={{ width: "10%" }}>
                          Action
                        </TableCell>
                      </TableRow>
                    </TableHead>
                    {TestDataDisplay.length != 0 ? (
                      <TableBody>
                        <>
                          {TestDataDisplay.slice(
                            page * rowsPerPage,
                            page * rowsPerPage + rowsPerPage
                          ).map((row, i) => {
                            return (
                              <TableRow
                                hover
                                role="checkbox"
                                tabIndex={-1}
                                key={i}
                              >
                                <TableCell align="center">{row.code}</TableCell>
                                <TableCell align="center">{row.name}</TableCell>
                                <TableCell align="center">
                                  {row.group}
                                </TableCell>
                                <TableCell align="center">
                                  {row.unitOfMeasurement}
                                </TableCell>
                                <TableCell align="center">
                                  {row.resultType}
                                </TableCell>
                                <TableCell align="center">
                                  <span
                                    style={{
                                      color:
                                        row.status.toLowerCase() == "active"
                                          ? "Green"
                                          : "Red",
                                    }}
                                  >
                                    {row.status}
                                  </span>
                                </TableCell>
                                <TableCell align="center">
                                  <div className="btn-group dotBtn-group1">
                                    <button
                                      type="button"
                                      className="btn btn-primary dotBtn"
                                      data-toggle="dropdown"
                                    >
                                      <span className="dot"></span>
                                      <span className="dot"></span>
                                      <span className="dot"></span>
                                    </button>
                                    <div className="dropdown-menu" id="show">
                                      <div className="dot-dropdown-menu">
                                        {row.resultFormat == "Numerical" && (
                                          <p
                                            className="dropdown-item"
                                            data-toggle="modal"
                                            data-target="#PatientDetails-wndow"
                                            onClick={(e) =>
                                              addReferance(row.id)
                                            }
                                          >
                                            Edit Reference Range
                                          </p>
                                        )}
                                        <p
                                          className="dropdown-item"
                                          data-toggle="modal"
                                          data-target="#PatientDetails-wndow"
                                          onClick={(e) => editTestdata(row.id)}
                                        >
                                          Edit Test
                                        </p>
                                      </div>
                                    </div>
                                  </div>
                                </TableCell>
                              </TableRow>
                            );
                          })}
                        </>
                      </TableBody>
                    ) : (
                      <TableBody>
                        <TableRow>
                          <TableCell>No data Found</TableCell>
                        </TableRow>
                      </TableBody>
                    )}
                  </Table>
                </TableContainer>
                <TablePagination
                  className="pagination-body"
                  rowsPerPageOptions={[5, 10, 25, 50]}
                  component="div"
                  count={TestDataDisplay.length}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  onPageChange={handleChangePage}
                  onRowsPerPageChange={handleChangeRowsPerPage}
                />
              </Paper>
            </div>
          )}
        </div>
      </Col>
    </Row>
  );
}

export default LiveService;
