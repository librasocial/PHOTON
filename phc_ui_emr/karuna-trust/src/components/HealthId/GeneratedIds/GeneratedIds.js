import React, { useState, useRef, useEffect } from "react";
import "../../../css/GeneratedIds.css";
import { Button, Col, Row } from "react-bootstrap";
import { Dropdown, Form, ButtonGroup } from "react-bootstrap";
import ReactPaginate from "react-paginate";
import moment from "moment";
import * as constant from "../../ConstUrl/constant";
import Sidemenu from "../../Sidemenus";
import SignedHealthIDImage from "../../Dashboard/SignedHealthIDImage";
import HealthIdWindow from "./HealthIdWindow";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import CreateABHANo from "../GenerateViaAdhar/CreateABHANo";
import { loadAccessToken } from "../../../redux/AdminAction";
import { useDispatch } from "react-redux";

const inputbox = {
  height: "30px",
};

const genderList = ["Male", "Female", "Transgender"];

let searchval;
let pagenumber;
let filtergenderdata = [];
let filterhealthid;
let filtervillagedata = [];
const healthidcheckbox = [
  "With ABHA Number (Health ID)",
  "Without ABHA Number (Health ID)",
];

let countdownInterval;
let timeout = 1000 * 60 * 10;

function GeneratedIds() {
  let dispatch = useDispatch();
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  let limit = 25;

  const [users, setUsers] = useState([]);
  const [pageCount, setpageCount] = useState(0);
  const [filteredGenre, setFilteredGenre] = useState([]);

  //pagination
  const [currentPage, setCurrentPage] = useState(0);

  pagenumber = currentPage;
  //end pagination

  //set genderValue

  const [genderData, setGenderData] = useState([]);
  const [villageData, setVillageData] = useState([]);
  //set checkbox
  const [checked, setChecked] = useState([]);

  // Generate string of checked items
  const checkedItems = checked.length
    ? checked.reduce((total, item) => {
        return total + ", " + item;
      })
    : "";

  // Return classes based on whether item is checked
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [searchvalue, setSearchvalue] = useState("");
  const [villageName, setVillageName] = useState([]);
  const sortedList = villageName.sort((a, b) =>
    a.properties.name.localeCompare(b.properties.name)
  );
  searchval = searchvalue;

  useEffect(() => {
    document.title = "EMR Generate HealthId";
    searchByname(
      searchvalue,
      pagenumber,
      genderData,
      filterhealthid,
      villageData
    );
  }, []);

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  const [searchtypevaltype, setSearchtypevaltype] = useState("NAME");
  const [d_o_b, setD_o_b] = useState(false);
  const [username, setUsername] = useState(true);
  const [abahanumber, setAbahanumber] = useState(false);
  const selectsearchtype = (e) => {
    const xx = e.target.value;
    if (xx === "NAME") {
      setSearchplaceholder("Enter name to search");
      setD_o_b(false);
      setUsername(true);
      setSearchvalue("");
    } else if (xx === "DOB") {
      setSearchplaceholder("Enter Date of Birth to search");
      setD_o_b(true);
      setSearchvalue("");
    } else if (xx === "CONTACT") {
      setSearchplaceholder("Enter contact number to search");
      setD_o_b(false);
      setContactnumber(true);
      setSearchvalue("");
    } else if (xx === "UHID") {
      setSearchplaceholder("Enter ABHA Number(Health ID) to search");
      setD_o_b(false);
      setAbahanumber(true);
      setSearchvalue("");
    }

    setSearchtypevaltype(xx);
  };

  // set searchBy Name
  const searchByname = async (
    nameval,
    currentPage,
    genderData,
    filterhealthid,
    villageData
  ) => {
    var str1 = "";
    for (var i = 0; i < villageData.length - 1; i++) {
      str1 = str1 + '"' + villageData[i] + '",';
    }
    str1 = str1 + '"' + villageData[i] + '"';

    var str = "";
    for (var i = 0; i < genderData.length - 1; i++) {
      str = str + '"' + genderData[i] + '",';
    }
    str = str + '"' + genderData[i] + '"';

    if (genderData.length == 0 && villageData.length == 0) {
      if (filterhealthid === true) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            healthID: true,
          },
        };
      } else if (filterhealthid === false) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            healthID: false,
          },
        };
      } else if (filterhealthid === undefined) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {},
        };
      }
    } else if (genderData.length == 0 && villageData.lenght != 0) {
      if (filterhealthid === true) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            healthID: true,
            villageId: str1,
          },
        };
      } else if (filterhealthid === false) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            healthID: false,
            villageId: str1,
          },
        };
      } else if (filterhealthid === undefined) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            villageId: str1,
          },
        };
      }
    } else if (genderData.length != 0 && villageData.length == 0) {
      if (filterhealthid === true) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
            healthID: true,
          },
        };
      } else if (filterhealthid === false) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
            healthID: false,
          },
        };
      } else if (filterhealthid === undefined) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
          },
        };
      }
    } else if (genderData.length != 0 && villageData.length != 0) {
      if (filterhealthid === true) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
            healthID: true,
            villageId: str1,
          },
        };
      } else if (filterhealthid === false) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
            healthID: false,
            villageId: str1,
          },
        };
      } else if (filterhealthid === undefined) {
        var filterbody = {
          type: "Citizen",
          page: currentPage,
          size: 25,
          properties: {
            gender: str,
            villageId: str1,
          },
        };
      }
    }

    let searchval = nameval;

    if (searchval.length == 0) {
    } else {
      setSearchvalue(searchval);
    }

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(filterbody),
    };

    if (searchval == "Invalid date") {
    } else {
      const res = await fetch(
        `${constant.ApiUrl}/member-svc/members/search?query=${searchval}&type=${searchtypevaltype}`,
        requestOptions
      ).then((res) => res.json());
      const data = await res.content;

      const total = res.totalElements;
      setFilteredGenre(data);
      setpageCount(Math.ceil(total / limit));
      setUsers(data);

      return data;
    }
  };

  useEffect(() => {
    let phcuuid = sessionStorage.getItem("uuid_of_phc");

    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Phc&srcNodeId=${phcuuid}&rel=SUBORGOF&targetType=SubCenter`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        let villagesData = getVillageData(
          res.content,
          serviceHeaders.getRequestOptions
        );
        let resultArr = [];
        Promise.all(villagesData).then((response) => {
          response.map((op) => {
            op.map((dt) => {
              resultArr.push(dt);
            });
          });
          setVillageName(resultArr);
        });
      });
  }, []);

  const getVillageData = (data, requestOptions) => {
    const resultData = [];
    data.forEach((element) => {
      resultData.push(
        new Promise((resolve, reject) => {
          fetch(
            `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${element.target.properties.uuid}&rel=SERVICEDAREA&targetType=Village`,
            requestOptions
          )
            .then((viilageres) => viilageres.json())
            .then((res) => {
              const villages = res.content;
              let result = villages.map((v) => v.target);
              resolve(result);
            })
            .catch((err) => {
              reject(err);
            });
        })
      );
    });
    return resultData;
  };

  const handlePageClick = async (data) => {
    setCurrentPage(data.selected);
    let currentPage = data.selected;
    const commentsFormServer = await searchByname(
      searchval,
      currentPage,
      filtergenderdata,
      filterhealthid,
      filtervillagedata
    );
    setUsers(commentsFormServer);
  };

  const checkGender = async (event) => {
    if (event.target.checked) {
      setGenderData([...genderData, event.target.value]);

      filtergenderdata = [...filtergenderdata, event.target.value];

      var currentPage1 = 0;
      const commentsFormServer = await searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
      // addgender();
    } else {
      setGenderData(genderData.filter((id) => id !== event.target.value));
      filtergenderdata = genderData.filter((id) => id !== event.target.value);
      var currentPage1 = 0;
      const commentsFormServer = await searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
      // removegender();
    }
  };

  const checkvillagefilter = async (event) => {
    if (event.target.checked) {
      setVillageData([...villageData, event.target.value]);
      filtervillagedata = [...filtervillagedata, event.target.value];
      var currentPage1 = 0;
      const commentsFormServer = await searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
      // addvillagedata();
    } else {
      setVillageData(villageData.filter((id) => id !== event.target.value));
      filtervillagedata = villageData.filter((id) => id !== event.target.value);
      var currentPage1 = 0;
      const commentsFormServer = await searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
      // removevillagedata();
    }
  };

  const checkHealthdata = (event) => {
    if (event.target.value === "Without ABHA Number (Health ID)") {
      filterhealthid = false;
      var currentPage1 = 0;
      const commentsFormServer = searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
    } else if (event.target.value === "With ABHA Number (Health ID)") {
      filterhealthid = true;
      var currentPage1 = 0;
      const commentsFormServer = searchByname(
        searchval,
        currentPage1,
        filtergenderdata,
        filterhealthid,
        filtervillagedata
      );
      setUsers(commentsFormServer);
    }
  };

  // var popUpObj;
  // function showModalPopUp(e) {

  //   popUpObj = window.open(
  //     `${constant.healthMobileUrl}/register`,
  //     "HidModalPopUp",
  //     "toolbar=no," +
  //     "scrollbars=yes," +
  //     "location=no," +
  //     "statusbar=no," +
  //     "menubar=no," +
  //     "directories=no," +
  //     "resizable=yes," +
  //     "width=1620px," +
  //     "height=800," +
  //     // "left=100," +
  //     // "top=100," +
  //     "fullscreen=yes" +
  //     "copyhistory=no"
  //   );

  //   popUpObj.focus();
  // }
  const [timeoutCountdown, setTimeoutCountdown] = useState(0);

  function onClickTimer() {
    setTimeout(() => {
      let countDown = 600;
      setTimeoutCountdown(countDown);
      countdownInterval = setInterval(() => {
        if (countDown > 0) {
          setTimeoutCountdown(--countDown);
        }
      }, 1000);
    });
  }

  const [AbhaModal, setAbhaModal] = useState(false);
  const showModalPopUp = () => {
    // if (timeoutCountdown == 0) {
    onClickTimer();
    dispatch(loadAccessToken(setAbhaModal));
    // } else {
    //   setAbhaModal(true)
    // }
  };

  const AbhaModalClose = () => {
    setAbhaModal(false);
  };

  const [contactnumber, setContactnumber] = useState("");
  const [citizenid_uusid, setCitizenid_uusid] = useState("");

  const [healthiddata, setHealthiddata] = useState({});
  const [issueHealthIdWindow, setIssueHealthIdWindow] = useState(false);

  const uploaduser = (vadlunamge) => {
    setIssueHealthIdWindow(true);
    setCitizenid_uusid(vadlunamge);
    fetch(
      `${constant.ApiUrl}/member-svc/members/${vadlunamge}`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.status === 500) {
          setIssueHealthIdWindow(false);
          alert("Unable to fetch the data");
        } else {
          setHealthiddata(res.content[0]["properties"]);
        }
      });
    // if (popUpForm) popUpForm.focus();
  };

  return (
    <Row className="main-div ">
      {/* open abha modal */}
      {AbhaModal && (
        <CreateABHANo
          AbhaModal={AbhaModal}
          AbhaModalClose={AbhaModalClose}
          timeoutCountdown={timeoutCountdown}
        />
      )}
      {/* open abha modal */}
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <>
          {issueHealthIdWindow ? (
            <HealthIdWindow
              healthiddata={healthiddata}
              setIssueHealthIdWindow={setIssueHealthIdWindow}
              citizenid_uusid={citizenid_uusid}
            />
          ) : (
            <div className="gen-div">
              <div className="regHeader">
                {!searchvalue ? (
                  <h1 className="register-Header">
                    Generate and Issue ABHA Number(Health ID)
                  </h1>
                ) : (
                  <h1 className="register-Header">
                    View / Generate ABHA Number(Health ID)
                  </h1>
                )}
                <hr />
              </div>

              <div className="main-container-div">
                <div className="row mt-2 mb-2">
                  <div className="AdBtn">
                    <div className="buttonregister ">
                      <Button
                        variant="primary"
                        className="genIdBtn"
                        value="mobile"
                        onClick={showModalPopUp}
                      >
                        Generate via Aadhar
                      </Button>
                      {/* <Button
                        variant="primary"
                        className="genIdBtn"
                        value="mobile"
                      // onClick={(e) => showModalPopUp(e.target.value)}
                      >
                        Login to ABHA Account
                      </Button> */}
                    </div>
                  </div>
                </div>
                <div className="search-bar">
                  <Row className="generate-id">
                    <Col md={2}></Col>
                    <Col md={8}>
                      <Row className="srchDiv">
                        <Col md={2}>
                          <h6 className="srchBy">Search by</h6>
                        </Col>

                        <Col md={3}>
                          <div>
                            <select
                              value={searchtypevaltype}
                              className="regDropdown"
                              onChange={selectsearchtype}
                            >
                              <option value="DOB">Date Of Birth</option>
                              <option value="CONTACT">Mobile Number</option>
                              <option value="NAME">Name</option>
                              <option value="UHID">ABHA Number</option>
                            </select>
                          </div>
                        </Col>
                        <Col md={7}>
                          {d_o_b ? (
                            <div className="searchForm">
                              <input
                                className="form-control"
                                type="date"
                                key="random1"
                                aria-describedby="search-addon"
                                style={inputbox}
                                placeholder={searchplaceholder}
                                // min="2014-05-11"
                                max={currentdate}
                                selected={searchvalue}
                                onChange={(e) =>
                                  searchByname(
                                    moment(new Date(e.target.value)).format(
                                      "YYYY-MM-DD"
                                    ),
                                    0,
                                    genderData,
                                    filterhealthid,
                                    villageData
                                  )
                                }
                              />
                            </div>
                          ) : username ? (
                            <div className="searchForm">
                              <input
                                className="form-control"
                                type="search"
                                key="random1"
                                id="search-input"
                                aria-describedby="search-addon"
                                style={inputbox}
                                placeholder={searchplaceholder}
                                selected={searchvalue}
                                onChange={(e) =>
                                  searchByname(
                                    e.target.value,
                                    0,
                                    genderData,
                                    filterhealthid,
                                    villageData
                                  )
                                }
                              />
                            </div>
                          ) : contactnumber ? (
                            <div className="searchForm">
                              <input
                                className="form-control"
                                type="search"
                                key="random1"
                                aria-describedby="search-addon"
                                style={inputbox}
                                placeholder={searchplaceholder}
                                // min="2014-05-11"

                                maxLength={10}
                                selected={searchvalue}
                                onChange={(e) =>
                                  searchByname(
                                    e.target.value,
                                    0,
                                    genderData,
                                    filterhealthid,
                                    villageData
                                  )
                                }
                              />
                            </div>
                          ) : abahanumber ? (
                            <div className="searchForm">
                              <input
                                className="seacrh-input "
                                type="search"
                                key="random1"
                                aria-describedby="search-addon"
                                style={inputbox}
                                placeholder={searchplaceholder}
                                onChange={(e) =>
                                  searchByname(
                                    e.target.value,
                                    0,
                                    genderData,
                                    filterhealthid,
                                    villageData
                                  )
                                }
                              />
                            </div>
                          ) : (
                            ""
                          )}
                        </Col>
                      </Row>
                    </Col>
                    <Col md={2} className="genID">
                      <div className="img-sec">
                        <img
                          src="../img/arrow-point-gen-via-aadhaar.png"
                          alt="no-result"
                        />
                      </div>
                      <div>
                        <p>Click to Generate ABHA Number(Health ID)</p>
                      </div>
                    </Col>
                  </Row>
                  {!searchvalue ? (
                    <div align="center">
                      <div className="search-box">
                        <div className="Reg-Text d-flex">
                          <p className="box-text">
                            To issue the generated ABHA Number(Health ID) card
                            to the resident. Search the resident and issue ABHA
                            Number(Health ID)
                            <br />{" "}
                          </p>
                        </div>
                        <div className="searchimage">
                          <img src="../img/Group57.svg" alt="no-result" />
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="row data-row">
                      <div className="col-md-3 filter-col">
                        <div className=" filterHeader">
                          <h5>Filter by</h5>
                        </div>
                        <div className="genderDiv">
                          <div className="shwText">
                            <p className="mb-1 mt-1">Gender</p>
                          </div>
                          <div className="mt-2 ">
                            <div>
                              {genderList.map((item, i) => (
                                <div key={i}>
                                  <Form.Check
                                    key={i}
                                    value={item}
                                    type="checkbox"
                                    className={isChecked(item.toLowerCase())}
                                    onChange={checkGender}
                                    label={item}
                                    style={{ fontSize: "14px" }}
                                  />
                                </div>
                              ))}
                            </div>
                          </div>
                        </div>
                        <hr></hr>
                        <div className="userRd">
                          <div className="shwText">
                            <p className="mb-1 mt-1 ">Show users</p>{" "}
                          </div>
                          <div>
                            {healthidcheckbox.map((item, i) => (
                              <div key={i}>
                                <Form.Check
                                  key={i}
                                  value={item}
                                  type="radio"
                                  className={isChecked(item)}
                                  label={item}
                                  name="dd"
                                  onChange={checkHealthdata}
                                  style={{ fontSize: "14px" }}
                                />
                              </div>
                            ))}
                          </div>
                        </div>

                        <hr />
                        <div className="">
                          <div className="shwText">
                            <p className="mb-1 mt-1 ">Village / Ward</p>
                          </div>
                          <div className="village-list">
                            {villageName.map((item, i) => (
                              <div key={i}>
                                <Form.Check
                                  key={i}
                                  value={item?.properties?.lgdCode}
                                  type="checkbox"
                                  className={isChecked(item)}
                                  label={item?.properties.name}
                                  onChange={checkvillagefilter}
                                  style={{ fontSize: "14px" }}
                                />
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>

                      <div className="col-md-9 listDiv text-left px-2 mb-3">
                        <label className="resultText">
                          <b>Total Results:</b> {users.length}
                        </label>
                        <div className="row  mt-1 data-show-div">
                          {users.length ? (
                            <div>
                              {!filteredGenre
                                ? "Loading..."
                                : filteredGenre.map((item, i) => (
                                    <div className="box-dsg1" key={i}>
                                      <Row id="pat_que3">
                                        <Col
                                          md={1}
                                          align="center"
                                          className="d-flex justify-content-center"
                                        >
                                          <SignedHealthIDImage
                                            healthID={item.properties?.healthID}
                                          />
                                        </Col>
                                        <Col md={4} align="center">
                                          <div className="dataName">
                                            <div>
                                              <p className="dataP">
                                                <b>
                                                  {item.properties?.firstName}{" "}
                                                  {item.properties?.middleName}{" "}
                                                  {item.properties?.lastName}
                                                </b>
                                              </p>
                                            </div>

                                            {item.properties?.hasOwnProperty(
                                              "healthID"
                                            ) ? (
                                              <>
                                                {item.properties?.healthID ===
                                                "" ? (
                                                  <p
                                                    className="dataP"
                                                    style={{ paddingTop: "1%" }}
                                                    key={item.id}
                                                  >
                                                    ID:{" "}
                                                    <em
                                                      style={{ color: "red" }}
                                                    >
                                                      Issue ABHA number
                                                    </em>
                                                  </p>
                                                ) : (
                                                  <>
                                                    <p
                                                      className="dataP"
                                                      style={{
                                                        paddingTop: "1%",
                                                      }}
                                                      key={item.id}
                                                    >
                                                      ID:&nbsp;
                                                      {item.properties?.healthID.replace(
                                                        /(\d{2})(\d{4})(\d{4})(\d{4})/,
                                                        "$1-$2-$3-$4"
                                                      )}
                                                    </p>
                                                  </>
                                                )}
                                              </>
                                            ) : (
                                              <p
                                                className="dataP"
                                                style={{ paddingTop: "1%" }}
                                                key={item.id}
                                              >
                                                ID:&nbsp;{" "}
                                                <em style={{ color: "red" }}>
                                                  Issue ABHA number
                                                </em>
                                              </p>
                                            )}
                                          </div>
                                        </Col>
                                        <Col md={1} align="center" id="pat_age">
                                          <div className="dataGender">
                                            <div>
                                              <p>{item.properties?.gender}</p>
                                            </div>
                                          </div>
                                        </Col>
                                        <Col md={2} align="center" id="pat_age">
                                          <div className="dataAge">
                                            <div>
                                              {item.properties?.hasOwnProperty(
                                                "age"
                                              ) ? (
                                                <p className="dataP">
                                                  {item.properties?.age} Years
                                                </p>
                                              ) : (
                                                <>
                                                  {item.properties
                                                    ?.dateOfBirth === "" ? (
                                                    ""
                                                  ) : (
                                                    <>
                                                      {item.properties?.hasOwnProperty(
                                                        "dateOfBirth"
                                                      ) ? (
                                                        <p className="dataP">
                                                          {moment(
                                                            new Date()
                                                          ).format("YYYY") -
                                                            moment(
                                                              new Date(
                                                                item.properties?.dateOfBirth
                                                              )
                                                            ).format(
                                                              "YYYY"
                                                            )}{" "}
                                                          years
                                                        </p>
                                                      ) : (
                                                        ""
                                                      )}{" "}
                                                    </>
                                                  )}
                                                </>
                                              )}
                                            </div>
                                            {item.properties?.hasOwnProperty(
                                              "dateOfBirth"
                                            ) ? (
                                              <>
                                                {item.properties
                                                  ?.dateOfBirth === "" ? (
                                                  <p
                                                    className="dataP"
                                                    style={{ paddingTop: "1%" }}
                                                    key={item.id}
                                                  >
                                                    <em
                                                      style={{ color: "red" }}
                                                    ></em>
                                                  </p>
                                                ) : (
                                                  <>
                                                    <p
                                                      className="dataP"
                                                      style={{
                                                        paddingTop: "1%",
                                                      }}
                                                      key={item.id}
                                                    >
                                                      {moment(
                                                        new Date(
                                                          item.properties?.dateOfBirth
                                                        )
                                                      ).format("DD MMM YYYY")}
                                                    </p>
                                                  </>
                                                )}
                                              </>
                                            ) : (
                                              <p
                                                className="dataP"
                                                style={{ paddingTop: "1%" }}
                                                key={item.id}
                                              >
                                                <em
                                                  style={{ color: "red" }}
                                                ></em>
                                              </p>
                                            )}
                                          </div>
                                        </Col>
                                        <Col md={3} align="center" id="pat_age">
                                          <div className="data-caste">
                                            <div>
                                              <p>
                                                {item.properties?.contact ? (
                                                  <>
                                                    +91-
                                                    {item.properties?.contact}
                                                  </>
                                                ) : (
                                                  <em style={{ color: "red" }}>
                                                    Number Not Available
                                                  </em>
                                                )}
                                              </p>
                                            </div>
                                          </div>
                                        </Col>
                                        {item.properties?.hasOwnProperty(
                                          "healthID"
                                        ) ? (
                                          <>
                                            {item.properties?.healthID ===
                                            "" ? (
                                              <Col
                                                md={1}
                                                id="pat_age"
                                                align="center"
                                                className="dropDots"
                                              >
                                                <Dropdown
                                                  as={ButtonGroup}
                                                  className="drpBtn"
                                                >
                                                  <Dropdown.Toggle
                                                    variant="success"
                                                    className="toggleBtn"
                                                  >
                                                    <span className="dot"></span>
                                                    <span className="dot"></span>
                                                    <span className="dot"></span>
                                                  </Dropdown.Toggle>
                                                  <Dropdown.Menu className="dropMenu issueDropDown">
                                                    <Dropdown.Item
                                                      onClick={() =>
                                                        uploaduser(
                                                          item.properties.uuid
                                                        )
                                                      }
                                                    >
                                                      Issue ABHA Number(Health
                                                      ID)
                                                    </Dropdown.Item>
                                                  </Dropdown.Menu>
                                                </Dropdown>
                                              </Col>
                                            ) : (
                                              <Col
                                                md={1}
                                                id="pat_age"
                                                align="center"
                                                className="dropDots"
                                              >
                                                <Dropdown
                                                  as={ButtonGroup}
                                                  className="drpBtn"
                                                >
                                                  <Dropdown.Toggle
                                                    variant="success"
                                                    className="toggleBtn"
                                                  >
                                                    <span className="dot"></span>
                                                    <span className="dot"></span>
                                                    <span className="dot"></span>
                                                  </Dropdown.Toggle>
                                                </Dropdown>
                                              </Col>
                                            )}
                                          </>
                                        ) : (
                                          <Col
                                            md={1}
                                            id="pat_age"
                                            align="center"
                                            className="dropDots"
                                          >
                                            <Dropdown
                                              as={ButtonGroup}
                                              className="drpBtn"
                                            >
                                              <Dropdown.Toggle
                                                variant="success"
                                                className="toggleBtn"
                                              >
                                                <span className="dot"></span>
                                                <span className="dot"></span>
                                                <span className="dot"></span>
                                              </Dropdown.Toggle>
                                              <Dropdown.Menu className="dropMenu">
                                                <Dropdown.Item
                                                  onClick={() =>
                                                    uploaduser(
                                                      item.properties.uuid
                                                    )
                                                  }
                                                >
                                                  Issue ABHA Number(Health ID)
                                                </Dropdown.Item>
                                              </Dropdown.Menu>
                                            </Dropdown>
                                          </Col>
                                        )}
                                      </Row>
                                    </div>
                                  ))}

                              <div className="pagination-div">
                                <ReactPaginate
                                  previousLabel={"Previous"}
                                  nextLabel={"Next"}
                                  breakLabel={"..."}
                                  pageCount={pageCount}
                                  pageRangeDisplayed={1}
                                  marginPagesDisplayed={2}
                                  onPageChange={handlePageClick}
                                  containerClassName={
                                    "pagination justify-content-center"
                                  }
                                  pageClassName={"page-item sup"}
                                  pageLinkClassName={"page-link"}
                                  previousClassName={"page-item sup"}
                                  previousLinkClassName={"page-link"}
                                  nextClassName={"page-item sup"}
                                  nextLinkClassName={"page-link"}
                                  breakLinkClassName={"page-link"}
                                  activeClassName={"active"}
                                />
                              </div>
                            </div>
                          ) : (
                            <div className="img-sec" align="center">
                              <img
                                src="../img/search-box-waiting.svg"
                                alt="no-result"
                              />
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}
        </>
      </Col>
    </Row>
  );
}

export default GeneratedIds;
