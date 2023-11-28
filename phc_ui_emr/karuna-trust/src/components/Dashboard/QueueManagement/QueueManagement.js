import React, { useEffect, useState } from "react";
import { Badge, Row, Col, Form } from "react-bootstrap";
import moment from "moment";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import LabTechnician from "./LabTechnician";
import Admin from "./Admin";
import Nurse from "./Nurse";
import MedicalOfficer from "./MedicalOfficer";
import PharmacyDashboard from "./PharmacyDashboard";
import { useDispatch, useSelector } from "react-redux";
import { loadLabOrdersByDate } from "../../../redux/actions";

let pagenumber;
function QueueManagement(props) {
  let typeofuser = sessionStorage.getItem("typeofuser");
  let dispatch = useDispatch();
  const { labOrderListByDate } = useSelector((state) => state.data);

  const [users, setUsers] = useState([]);
  const [checkusers, setcheckUsers] = useState([]);
  const [selected, setSelected] = useState({});
  const [status, setStatus] = useState(false);
  let todayDate = moment(new Date()).format("YYYY-MM-DD");

  const [pageCount, setpageCount] = useState(1);
  const [checkpageCount, setcheckpageCount] = useState(1);

  // /pagination
  const [currentPage, setCurrentPage] = useState(1);
  pagenumber = currentPage;

  const [selectProvide, setSelectProvide] = useState("");

  const selectsearch = (e) => {
    setSelectProvide(e.target.value);
  };

  {
    /* For lab */
  }
  const inputbox = {
    height: "30px",
  };

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  const [d_o_b, setD_o_b] = useState(false);
  const [datebet, setDatebet] = useState(false);
  const [username, setUsername] = useState(true);
  const [contactnumber, setContactnumber] = useState(false);
  const [abahanumber, setAbahanumber] = useState(false);
  const [uhidn, setUhidn] = useState(false);

  const selectsearchtype = (e) => {
    const xx = e.target.value;
    if (xx === "Name") {
      setSearchplaceholder("Enter name to search");
      setD_o_b(false);
      setUsername(true);
      setUhidn(false);
      setContactnumber(false);
      setDatebet(false);
    } else if (xx === "DateOfBirth") {
      setSearchplaceholder("Enter Date of Birth to search");
      setD_o_b(true);
      setUsername(false);
      setUhidn(false);
      setContactnumber(false);
      setDatebet(false);
    } else if (xx === "MobileNumber") {
      setSearchplaceholder("Enter contact number to search");
      setD_o_b(false);
      setUsername(false);
      setUhidn(false);
      setContactnumber(true);
      setDatebet(false);
    } else if (xx === "UHID") {
      setSearchplaceholder("Enter UHID Number to search");
      setD_o_b(false);
      setUsername(false);
      setUhidn(true);
      setContactnumber(false);
      setDatebet(false);
    } else if (xx === "DateBetween") {
      //setSearchplaceholder("Enter UHID Number to search")
      setD_o_b(false);
      setUsername(false);
      setUhidn(false);
      setContactnumber(false);
      setDatebet(true);
    }
  };
  {
    /* For lab */
  }

  const [searchAllData, setSearchAllData] = useState([]);
  useEffect(() => {
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/filter?page=0&size=10000000`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        setSearchAllData(res["content"]);
        setStatus(false);
      });
  }, [status]);

  const [searchValue, setSearchValue] = useState("");
  const [searchStatus, setSearchStatus] = useState(false);
  const [filteredResults, setFilteredResults] = useState([]);

  useEffect(() => {
    if (searchValue == "") {
      setSearchStatus(false);
    }
  }, [searchValue]);

  const searchItems = (e) => {
    setSearchStatus(true);
    setSearchValue(e.target.value);
    const searchKeyWord = e.target.value;
    if (searchKeyWord !== "") {
      if (username == true) {
        const filteredData = searchAllData.filter((item) => {
          return item?.patient?.name
            .toLowerCase()
            .includes(searchKeyWord.toLowerCase());
        });
        setFilteredResults(filteredData);
      } else if (contactnumber == true) {
        const filteredData = searchAllData.filter((item) => {
          if (item?.patient?.phone) {
            return item?.patient?.phone.toLowerCase().includes(searchKeyWord);
          }
        });
        setFilteredResults(filteredData);
      } else if (d_o_b == true) {
        const filteredData = searchAllData.filter((item) => {
          return item?.patient?.dob
            .toLowerCase()
            .includes(searchKeyWord.toLowerCase());
        });
        setFilteredResults(filteredData);
      } else if (uhidn == true) {
        const filteredData = searchAllData.filter((item) => {
          return item?.patient?.uhid
            ?.toString()
            .startsWith(searchKeyWord.toString());
        });
        setFilteredResults(filteredData);
      } else if (datebet == true) {
        const filteredData = filteredResults.filter((item) => {
          return item?.patient?.name
            ?.toString()
            .includes(searchKeyWord.toString());
        });
        setFilteredResults(filteredData);
      }
    } else {
      setFilteredResults(filteredResults);
    }
  };

  const [fromDate, setFromDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const searchByFromDate = (e) => {
    setFromDate(e.target.value);
  };
  const searchByEndDate = (e) => {
    setEndDate(e.target.value);
  };

  useEffect(() => {
    if (fromDate && endDate) {
      setSearchValue(fromDate);
      dispatch(loadLabOrdersByDate(fromDate, endDate));
      //     fetch(`${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${fromDate}&endDate=${endDate}&page=0&size=1000000`, serviceHeaders.getRequestOptions)
      //         .then((res) => res.json())
      //         .then((res) => {
      //             setFilteredResults(res['content']);
      //             setStatus(false);
      //         });
    }
  }, [fromDate, endDate]);

  useEffect(() => {
    if (labOrderListByDate) {
      setFilteredResults(labOrderListByDate);
    }
  }, [labOrderListByDate]);

  return (
    <div>
      {typeofuser === "Lab Technician" ? (
        <div className="search-bar">
          <Row>
            <Col md={1}></Col>
            <Col md={10}>
              <Row className="srchDiv">
                <Col md={9}>
                  <h6 className="lab-date-fil">Search by</h6>
                  <Row>
                    <Col md={2} className="search-dropdown">
                      <div>
                        <select
                          className="regDropdown form-select"
                          defaultValue="Name"
                          onChange={selectsearchtype}
                        >
                          <option value="DateOfBirth">Date Of Birth</option>
                          <option value="MobileNumber">Mobile Number</option>
                          <option value="Name">Name</option>
                          <option value="UHID">UHID</option>
                          <option value="DateBetween">Date Between</option>
                        </select>
                      </div>
                    </Col>
                    <Col md={10}>
                      {d_o_b ? (
                        <div className="search-dropdown">
                          <div className="searchForm">
                            <input
                              className="form-control"
                              type="date"
                              key="random1"
                              pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                              style={inputbox}
                              id="search-input"
                              placeholder={searchplaceholder}
                              max={todayDate}
                              onChange={searchItems}
                            />
                          </div>
                        </div>
                      ) : username ? (
                        <div className="search-dropdown">
                          <input
                            className="form-control"
                            type="search"
                            key="random1"
                            style={inputbox}
                            id="search-input"
                            placeholder={searchplaceholder}
                            onChange={searchItems}
                          />
                        </div>
                      ) : contactnumber ? (
                        <div className="search-dropdown">
                          <div className="xbtn">
                            <input
                              key="random1"
                              className="form-control"
                              type="number"
                              style={inputbox}
                              id="search-input"
                              placeholder={searchplaceholder}
                              maxLength={10}
                              onChange={searchItems}
                            />
                          </div>
                        </div>
                      ) : abahanumber ? (
                        <div className="search-dropdown">
                          <div className="xbtn">
                            <input
                              key="random1"
                              className="form-control"
                              aria-describedby="search-addon"
                              type="number"
                              style={inputbox}
                              placeholder={searchplaceholder}
                              maxLength={10}
                              id="search-input"
                              selected={searchvalue}
                              onChange={(e) =>
                                searchByname(
                                  e.target.value,
                                  0,
                                  genderData,
                                  villageData
                                )
                              }
                            />
                          </div>
                        </div>
                      ) : uhidn ? (
                        <div className="search-dropdown">
                          <div className="xbtn">
                            <input
                              className="form-control"
                              type="search"
                              key="random1"
                              aria-describedby="search-addon"
                              style={inputbox}
                              id="search-input"
                              placeholder={searchplaceholder}
                              onChange={searchItems}
                            />
                          </div>
                        </div>
                      ) : datebet ? (
                        <Row>
                          <Col md={6} className="datebet search-dropdown">
                            <input
                              className="form-control"
                              type="date"
                              key="random1"
                              pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                              style={inputbox}
                              id="search-input"
                              placeholder={searchplaceholder}
                              max={todayDate}
                              onChange={searchByFromDate}
                            />
                          </Col>
                          <Col md={6} className="datebet search-dropdown">
                            <input
                              className="form-control"
                              type="date"
                              key="random1"
                              pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                              style={inputbox}
                              id="search-input"
                              placeholder={searchplaceholder}
                              max={todayDate}
                              onChange={searchByEndDate}
                            />
                          </Col>
                        </Row>
                      ) : (
                        ""
                      )}
                    </Col>
                  </Row>
                </Col>
                <Col md={3} className="search-dropdown">
                  {datebet ? (
                    <div className="datebet search-dropdown">
                      <h6 className="lab-date-fil">Filter by</h6>
                      <input
                        className="form-control"
                        type="search"
                        key="random1"
                        style={inputbox}
                        id="search-input"
                        onChange={searchItems}
                      />
                    </div>
                  ) : (
                    ""
                  )}
                </Col>
              </Row>
            </Col>
          </Row>
        </div>
      ) : (
        <>
          {typeofuser != "Pharmacist" && (
            <div className="mobile-view">
              <h1 className="dash-mnag-list">List of patients in visit </h1>
            </div>
          )}
        </>
      )}

      <React.Fragment>
        <>
          {typeofuser === "Medical Officer" && (
            <MedicalOfficer pageCount={pageCount} />
          )}
        </>

        <>
          {typeofuser === "Nurse" && (
            <Nurse
              pageCount={pageCount}
              selectProvide={selectProvide}
              selectsearch={selectsearch}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
            />
          )}
        </>

        <>
          {typeofuser === "Admin" && (
            <Admin
              isdashboard={props.isdashboard}
              selectProvide={selectProvide}
              pageCount={pageCount}
              selectsearch={selectsearch}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
            />
          )}
        </>

        <>
          {typeofuser === "Lab Technician" && (
            <LabTechnician
              searchStatus={searchStatus}
              pageCount={pageCount}
              filteredResults={filteredResults}
              searchValue={searchValue}
            />
          )}
        </>
        <>
          {/* {(typeofuser === "Pharmasist") && */}
          {typeofuser === "Pharmacist" && (
            <PharmacyDashboard pageCount={pageCount} />
          )}
        </>
      </React.Fragment>
    </div>
  );
}

export default QueueManagement;
