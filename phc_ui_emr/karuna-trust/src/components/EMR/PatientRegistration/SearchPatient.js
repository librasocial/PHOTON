import React, {
  useState,
  useRef,
  useMemo,
  useEffect,
  useCallback,
} from "react";

import "react-html5-camera-photo/build/css/index.css";
import "../../../css/SearchPatient.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "../../../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../../../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import {
  Button,
  Col,
  Modal,
  Row,
  Dropdown,
  Form,
  ButtonGroup,
} from "react-bootstrap";
import { Link } from "react-router-dom";
import RegForms from "../PatientRegistration/RegForms";
import ReactPaginate from "react-paginate";
import moment from "moment";
import QRCode from "qrcode";
import QrReader from "react-qr-reader";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Sidemenu from "../../Sidemenus";
import Webcam from "react-webcam";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SignedHealthIDImage from "../../Dashboard/SignedHealthIDImage";
import PatientRow from "../../PatientRow/PatientRow";
import SaveButton from "../../EMR_Buttons/SaveButton";

let PageSize = 25;
const list = [
  { key: "Name", value: "Name" },
  { key: "UHID", value: "UHID" },
  { key: "Date of birth", value: "Date of birth" },
  { key: "Mobile number", value: "Mobile number" },
];

const buttonmystyle = {
  color: "white",
  height: "25px",
  padding: "5px",
};
const inputbox = {
  height: "30px",
};

const movie = [{ gender: "Male" }, { gender: "Female" }, { gender: "Other" }];
let limit = 25;
// var filtergenderdata = [];
let searchval;
let pagenumber;
let filtergenderdata = [];
let filtervillagedata = [];
function SearchPatient() {
  const [openregister, setOpenregister] = useState(false);
  const [uuidcitizenid, setUuidcitizenid] = useState("");
  const [uuuid, setUuuid] = useState("");

  const [text, setText] = useState("");
  const [imageUrl, setImageUrl] = useState("");
  const [scanResultFile, setScanResultFile] = useState("");
  const [scanResultWebCam, setScanResultWebCam] = useState("");

  const qrRef = useRef(null);

  const [show, setShow] = useState(false);
  const handleClose = () => {
    setIsShowVideo(false);
    setShow(false);
  };
  const handleShow = () => setShow(true);

  const [showModal, setShowModal] = useState(false);
  const handleCloseModal = () => setShowModal(false);

  const [openscannerwindow, setOpenscannerwindow] = useState(false);
  const openscanner = () => {
    setOpenscannerwindow(true);
    setSearchplaceholder("Enter ABHA Number(Health ID) to search");
    setD_o_b(false);
    setAbahanumber(true);
    setUsername(false);
    setSearchtypevaltype("UHID");
  };

  const generateQrCode = async () => {
    try {
      const response = await QRCode.toDataURL(text);
      setImageUrl(response);
    } catch (error) {}
  };
  const handleErrorFile = (error) => {};
  const handleScanFile = (result) => {
    if (result) {
      setScanResultFile(result);
    }
  };
  const onScanFile = () => {
    qrRef.current.openImageDialog();
  };
  const handleErrorWebCam = (error) => {};
  const handleScanWebCam = (result) => {
    if (result) {
      var str = result.replace(/[{}]/g, "");

      var myarray = str.split(",");

      var data = "'" + myarray[0] + "'";

      data = data.toString();

      var nameArr = data.split(":");

      var name = "'" + nameArr[1] + "'";
      name = name.toString();

      name = name.replace(/["'-]/g, "");

      // setScanResultWebCam(result);
      setScanResultWebCam(name);

      setSearchtypevaltype("UHID");
    }
  };

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const [users, setUsers] = useState([]);
  const [regUserData, setRegUserData] = useState();
  const [pageCount, setpageCount] = useState(0);
  // const [genre, setGenre] = useState([]);
  const [filteredGenre, setFilteredGenre] = useState([]);

  // useEffect(() => {
  if (regUserData && filteredGenre) {
    if (regUserData && filteredGenre) {
      for (var i = 0; i < filteredGenre.length; i++) {
        for (var j = 0; j < regUserData.length; j++) {
          if (
            regUserData[j].citizen.memberId == filteredGenre[i].properties.uuid
          ) {
            filteredGenre[i].regStatus = "true";
          }
        }
      }
    }
  }
  // }, [filteredGenre, regUserData])

  //pagination
  const [currentPage, setCurrentPage] = useState(0);

  pagenumber = currentPage;
  //end pagination

  //set redioButton
  const [radio1, setRadio1] = useState(false);
  const [radio2, setRadio2] = useState(false);

  //set genderValue
  const [genderval, setGenderval] = useState([]);

  const [genderData, setGenderData] = useState([]);
  const [villageData, setVillageData] = useState([]);
  //set checkbox
  const [checked, setChecked] = useState([]);

  const handleCheck = (event) => {
    let updatedList = [...checked];
    if (event.target.checked) {
      updatedList = [...checked, event.target.value];
    } else {
      updatedList.splice(checked.indexOf(event.target.value), 1);
    }
    setChecked(updatedList);
    setGenderval(updatedList);
    if (radio1 == true) {
      allphcusers(updatedList);
    }
    if (radio2 == true) {
      onlymyusers(updatedList);
    }
  };

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
  searchval = searchvalue;

  const [villageName, setVillageName] = useState([]);
  const sortedList = villageName.sort((a, b) =>
    a.properties.name.localeCompare(b.properties.name)
  );
  const [status, setStatus] = useState("");
  // useEffect(() => {
  //     document.title = "EMR Search Resident"
  //     searchByname(searchvalue, pagenumber, genderData, villageData);
  // }, [status, searchvalue, pagenumber, genderData, villageData]);

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  const [searchtypevaltype, setSearchtypevaltype] = useState("NAME");

  const [d_o_b, setD_o_b] = useState(false);

  const [username, setUsername] = useState(true);
  const [contactnumber, setContactnumber] = useState(false);
  const [abahanumber, setAbahanumber] = useState(false);
  const selectsearchtype = (e) => {
    const xx = e.target.value;

    if (xx === "NAME") {
      setSearchplaceholder("Enter name to search");
      setD_o_b(false);
      setUsername(true);
    } else if (xx === "DOB") {
      setSearchplaceholder("Enter Date of Birth to search");
      setD_o_b(true);
    } else if (xx === "CONTACT") {
      setSearchplaceholder("Enter contact number to search");
      setD_o_b(false);
      setContactnumber(true);
      setUsername(false);
    } else if (xx === "UHID") {
      setSearchplaceholder("Enter ABHA Number(Health ID) to search");
      setD_o_b(false);
      setAbahanumber(true);
      setUsername(false);
    }
    setSearchtypevaltype(xx);
  };

  // set searchBy Name
  const searchByname = async (
    nameval,
    searchtypevaltype,
    currentPage,
    genderData,
    villageData
  ) => {
    var str = "";
    for (var i = 0; i < genderData.length - 1; i++) {
      str = str + '"' + genderData[i] + '",';
    }
    str = str + '"' + genderData[i] + '"';

    var str1 = "";
    for (var i = 0; i < villageData.length - 1; i++) {
      str1 = str1 + '"' + villageData[i] + '",';
    }
    str1 = str1 + '"' + villageData[i] + '"';

    if (genderData.length == 0 && villageData.length == 0) {
      var filterbody = {
        type: "Citizen",
        page: currentPage,
        size: 25,
        properties: {
          // gender: str,
          // healthID: true,
        },
      };
    } else if (genderData.length == 0 && villageData.lenght != 0) {
      var filterbody = {
        type: "Citizen",
        page: currentPage,
        size: 25,
        properties: {
          villageId: str1,
        },
      };
    } else if (genderData.length != 0 && villageData.length == 0) {
      var filterbody = {
        type: "Citizen",
        page: currentPage,
        size: 25,
        properties: {
          gender: str,
          // healthID: true,
        },
      };
    } else if (genderData.length != 0 && villageData.length != 0) {
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

    let searchval = nameval;

    if (searchval.length == 0) {
      filtergenderdata = [];
      filtervillagedata = [];
      setSearchvalue("");
      setGenderData("");
      setVillageData("");
    } else {
      setSearchvalue(searchval);
    }

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(filterbody),
    };

    if (searchval != "" && searchval != undefined) {
      // const res = await
      fetch(
        `${constant.ApiUrl}/member-svc/members/search?query=${searchval}&type=${searchtypevaltype}&page=${currentPage}&size=25`,
        requestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          setFilteredGenre(res.content);
          setUsers(res.content);
          setpageCount(Math.ceil(res.totalElements / limit));
        });
      let srt;
      if (searchtypevaltype === "NAME") {
        srt = "Name";
      } else if (searchtypevaltype === "CONTACT") {
        srt = "MobileNumber";
      } else if (searchtypevaltype === "DOB") {
        srt = "DateOfBirth";
      } else if (searchtypevaltype === "UHID") {
        srt = "HealthID";
      }
      // const regResp = await

      fetch(
        `${constant.ApiUrl}/patients/search?fieldName=${srt}&value=` +
          searchval +
          `&page=` +
          (currentPage + 1),
        serviceHeaders.getRequestOptions
      )
        .then((regResp) => regResp.json())
        .then((regResp) => {
          setRegUserData(regResp.data);
        });

      // const data = await res.content;
      // const regData = await regResp.data;
      // const ele = res.meta;

      // const total = res.totalElements;
      // setFilteredGenre(data)
      // setpageCount(Math.ceil(total / limit));
      // setUsers(data);
      // setRegUserData(regData)
      // return data;
    }
  };

  useEffect(() => {
    document.title = "EMR Search Resident";
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Phc&srcNodeId=8d9392ec-97cf-4a24-a761-8479055424b0&rel=SUBORGOF&targetType=SubCenter`,
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
    setStatus(false);
  }, [status]);

  const getVillageData = (data) => {
    const resultData = [];
    data.forEach((element) => {
      resultData.push(
        new Promise((resolve, reject) => {
          fetch(
            `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${element.target.properties.uuid}&rel=SERVICEDAREA&targetType=Village`,
            serviceHeaders.getRequestOptions
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

  const handlePageClick = (data) => {
    setCurrentPage(data.selected);
    let currentPage = data.selected;

    searchByname(
      searchval,
      searchtypevaltype,
      currentPage,
      filtergenderdata,
      filtervillagedata
    );
    // setUsers(commentsFormServer);
  };

  const checkGender = (event) => {
    if (event.target.checked) {
      setGenderData([...genderData, event.target.value]);
      filtergenderdata = [...filtergenderdata, event.target.value];
      var currentPage1 = 0;
      searchByname(
        searchval,
        searchtypevaltype,
        currentPage1,
        filtergenderdata,
        filtervillagedata
      );
      // setUsers(commentsFormServer);
    } else {
      setGenderData(genderData.filter((id) => id !== event.target.value));
      filtergenderdata = genderData.filter((id) => id !== event.target.value);
      var currentPage1 = 0;
      searchByname(
        searchval,
        searchtypevaltype,
        currentPage1,
        filtergenderdata,
        filtervillagedata
      );
      // setUsers(commentsFormServer);
      // removegender();
    }
  };

  const [healthID, setHealthID] = useState("");
  const [isShowVideo, setIsShowVideo] = useState(false);
  const videoElement = useRef(null);
  const [imagefile, setImagefile] = useState("");
  const [imgSrc, setImgSrc] = useState(null);
  const startCam = () => {
    setIsShowVideo(true);
  };
  // code for retake photo
  const Retake = () => {
    setImgSrc("");
    setImagefile("");
    setHealthID("");
  };
  // code for retake photo

  const capture = React.useCallback(async () => {
    const imageSrc = videoElement.current.getScreenshot();
    function dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(","),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
    }

    //     //Usage example:
    var file = dataURLtoFile(imageSrc, "hello.jpeg");
    var FRdata = {
      option: "searchFace",
      region: "app-south-1",
      collection_name: "dev",
      bucket_name: "ssf-dev-assets",
      file: imageSrc,
    };
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(FRdata),
    };
    fetch(`${constant.ApiUrl}/FacialRecognition`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.message == "File failed to upload") {
          Tostify.notifyFail("Please capture the face only..!");
        } else {
          let faceID;
          if (res.message == "No Health ID Found") {
            setImgSrc(imageSrc);
            Tostify.notifyWarning("No Health ID Found");
          } else {
            faceID = res.message;
          }

          if (faceID) {
            fetch(
              `${constant.ApiUrl}/member-svc/members/filter?type=Citizen&key=faceId&value=${faceID}`,
              serviceHeaders.getRequestOptions
            )
              .then((res) => res.json())
              .then((res) => {
                if (res["content"].length != 0) {
                  setD_o_b(false);
                  setUsername(false);
                  setHealthID(res["content"][0]["healthID"]);
                  let healthId = res["content"][0]["healthID"];
                  setSearchvalue(healthId);
                  setAbahanumber(true);
                  setSearchplaceholder(
                    "Enter ABHA Number(Health ID) to search"
                  );
                  let searchType = "UHID";
                  setSearchtypevaltype("UHID");
                  searchByname(
                    res["content"][0]["healthID"],
                    searchType,
                    0,
                    genderData,
                    villageData
                  );

                  fetch(
                    `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`,
                    serviceHeaders.getRequestOptions
                  )
                    .then((res) => res.json())
                    .then((res) => {
                      setImgSrc(res.preFetchURL);
                    });
                } else {
                  alert("HealthId not registered for this patient...!");
                }
              });
          }
        }
      });

    setImagefile(file);
    setIsShowVideo(true);
    //     // setHealthID("")
  }, [videoElement, setImgSrc]);
  // cade for capturing photo

  const checkvillagefilter = (event) => {
    if (event.target.checked) {
      setVillageData([...villageData, event.target.value]);
      filtervillagedata = [...filtervillagedata, event.target.value];
      var currentPage1 = 0;
      searchByname(
        searchval,
        searchtypevaltype,
        currentPage1,
        filtergenderdata,
        filtervillagedata
      );
      // setUsers(commentsFormServer);
      // addvillagedata();
    } else {
      setVillageData(villageData.filter((id) => id !== event.target.value));
      filtervillagedata = villageData.filter((id) => id !== event.target.value);
      var currentPage1 = 0;
      searchByname(
        searchval,
        searchtypevaltype,
        currentPage1,
        filtergenderdata,
        filtervillagedata
      );
      // setUsers(commentsFormServer);
    }
  };

  const [image, setImage] = useState("");
  const inputFile = useRef(null);

  const handleFileUpload = (e) => {
    const { files } = e.target;
    if (files && files.length) {
      const filename = files[0].name;

      var parts = filename.split(".");
      const fileType = parts[parts.length - 1];

      setImage(files[0]);
    }
  };

  const [firstname, setFirstname] = useState("");
  const [healthid, setHealthid] = useState("");
  const [citizenid_uusid, setCitizenid_uusid] = useState("");
  const [citizenGender, setCitizenGender] = useState("");
  const [errorvalue, setErrorvalue] = useState("");

  const settingerrorvalue = () => {
    if (healthid === "") {
      setErrorvalue("Please enter ABHA Number");
    } else if (healthid.length < 14) {
      setErrorvalue("Please Enter 14 digit ABHA number");
    } else if (healthid.length === 14) {
      setErrorvalue("");
    }
  };

  useEffect(() => {
    settingerrorvalue();
  }, [healthid, status]);

  const [healthiddata, setHealthiddata] = useState({});
  const [issueHealthIdWindow, setIssueHealthIdWindow] = useState(false);

  const uploaduser = (vadlunamge) => {
    setIssueHealthIdWindow(true);
    setCitizenid_uusid(vadlunamge);
    popUpForm = setShow(true);
    fetch(
      `${constant.ApiUrl}/member-svc/members/` + vadlunamge,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        setFirstname(res.content[0]["name"]);
        setCitizenGender(res.content[0]["gender"]);
        setHealthiddata(res.content[0]);
        setStatus(false);
      });
    popUpForm.focus();
  };

  const [imageuri, setImageuri] = useState("");
  const takesnap = (dataUri) => {
    setImageuri(dataUri);
  };
  function handleTakePhoto(dataUri) {
    setImageuri(dataUri);
  }
  function handleTakePhotoAnimationDone(dataUri) {}

  function handleCameraError(error) {}

  function handleCameraStart(stream) {}

  function handleCameraStop() {}

  // code for searching villages
  const [filteredResult, setFilteredResults] = useState([]);

  const searchItems = (e) => {
    const searchKeyWord = e.target.value;
    if (searchKeyWord !== "") {
      const filteredData = villageName.filter((item) => {
        return item.properties.name
          .toLowerCase()
          .includes(searchKeyWord.toLowerCase());
      });
      setFilteredResults(filteredData);
    } else {
      setFilteredResults(villageName);
    }
  };
  // code for searching villages

  const closeModal = () => {
    handleClose(true);
  };

  return (
    <>
      <ToastContainer />
      <Row className="main-div ">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          <React.Fragment>
            <React.Fragment>
              {/* modal modal */}
              <Modal
                show={showModal}
                onHide={handleCloseModal}
                className="alert-modal-div"
              >
                <Row>
                  <Col
                    xsm={1}
                    sm={1}
                    md={1}
                    className="alert-icon-div"
                    align="center"
                  >
                    <div className="alert-icon">
                      <i className="bi bi-info-square"></i>
                    </div>
                  </Col>
                  <Col
                    xsm={11}
                    sm={11}
                    md={11}
                    className="alert-modal-text-div"
                  >
                    <div className="">
                      <Row>
                        <Col xsm={11} sm={11} md={11}>
                          <b id="modal-title">Verify details and submit</b>
                        </Col>
                        <Col
                          xsm={1}
                          sm={1}
                          md={1}
                          align="right"
                          style={{ fontSize: "12px" }}
                        >
                          <button
                            type="button"
                            className="btn-close modelCls"
                            data-bs-dismiss="modal"
                            aria-label="Close"
                            id="btn"
                            onClick={handleCloseModal}
                          ></button>
                        </Col>
                      </Row>
                    </div>
                    <div className="">
                      <p className="alert-modal-text">
                        Please verify the details of the resident and then
                        submit for registration.
                      </p>
                    </div>
                  </Col>
                </Row>
              </Modal>
              {/* modal */}
            </React.Fragment>
            <React.Fragment>
              <div className="regHeader">
                <Row>
                  <Col md={8}>
                    <h1 className="register-Header">Register New Patient</h1>
                  </Col>
                  {!uuidcitizenid ? (
                    <Col md={4} className="seachlnk">
                      <div>
                        <div className="form-group has-search buttonregister">
                          <Button
                            variant="outline-secondary"
                            className="genIdBtn"
                            onClick={handleShow}
                          >
                            Search residents
                          </Button>
                        </div>
                      </div>
                    </Col>
                  ) : (
                    ""
                  )}
                </Row>
                <hr />
              </div>
              {show && (
                <Modal
                  show={show}
                  onHide={handleClose}
                  className="search-div-modal"
                >
                  <Row>
                    <Col md={6} xs={9}>
                      <h3 className="patient-search-header">Search</h3>
                    </Col>
                    <Col md={6} xs={3} align="right">
                      <button
                        className="bi bi-x close-popup search-close-btn"
                        onClick={handleClose}
                      ></button>
                    </Col>
                  </Row>
                  <React.Fragment>
                    <Row className="data-search-input">
                      <Col md={9} className="srchinput-div">
                        <div className="srch-ele-div">
                          <Row className="srchDiv">
                            <Col md={2} className="d-flex">
                              <h6 className="srchBy">Search by</h6>
                            </Col>
                            <Col md={3} className="search-dropdown">
                              <div>
                                <select
                                  className="regDropdown"
                                  value={searchtypevaltype || ""}
                                  onChange={selectsearchtype}
                                >
                                  <option value="DOB">Date Of Birth</option>
                                  <option value="CONTACT">Mobile Number</option>
                                  <option value="NAME">Name</option>
                                  <option value="UHID">ABHA Number</option>
                                </select>
                              </div>
                            </Col>
                            <Col md={7} className="search-dropdown">
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
                                    id="search-input"
                                    selected={searchvalue}
                                    onChange={(e) =>
                                      searchByname(
                                        moment(new Date(e.target.value)).format(
                                          "YYYY-MM-DD"
                                        ),
                                        searchtypevaltype,
                                        0,
                                        genderData,
                                        villageData
                                      )
                                    }
                                  />
                                </div>
                              ) : username ? (
                                <input
                                  className="form-control"
                                  type="search"
                                  pattern="/^[A-Za-z]+$/"
                                  key="random1"
                                  // aria-describedby="search-addon"
                                  style={inputbox}
                                  id="search-input"
                                  placeholder={searchplaceholder}
                                  // min="2014-05-11"

                                  selected={searchvalue}
                                  onChange={(e) =>
                                    searchByname(
                                      e.target.value,
                                      searchtypevaltype,
                                      0,
                                      genderData,
                                      villageData
                                    )
                                  }
                                />
                              ) : contactnumber ? (
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
                                        searchtypevaltype,
                                        0,
                                        genderData,
                                        villageData
                                      )
                                    }
                                  />
                                  {/* <button onClick={resetInputField} type="reset" >&times;</button> */}
                                </div>
                              ) : abahanumber ? (
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
                                    value={searchvalue || ""}
                                    selected={searchvalue}
                                    onChange={(e) =>
                                      searchByname(
                                        e.target.value,
                                        searchtypevaltype,
                                        0,
                                        genderData,
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
                        </div>
                      </Col>
                      <Col md={1} className="resident-modal-or divider">
                        <div className="responge-divider">
                          <div className="vl"></div>
                          <div className="vlH"></div>
                          <div className="text">Or</div>
                          <div className="vl"></div>
                          <div className="vlH"></div>
                        </div>
                      </Col>
                      <Col md={2} className="qr-box" align="center">
                        <div style={{ width: "100%" }}>
                          <div className="person-Img1 d-block">
                            {isShowVideo ? (
                              <>
                                {!imgSrc ? (
                                  <Webcam
                                    style={{
                                      height: "130px",
                                      width: "400px",
                                    }}
                                    audio={false}
                                    ref={videoElement}
                                    screenshotFormat="image/jpeg"
                                  />
                                ) : (
                                  <>
                                    <img
                                      src={imgSrc}
                                      style={{
                                        height: "150px",
                                        width: "400px",
                                      }}
                                    />
                                  </>
                                )}
                              </>
                            ) : (
                              <>
                                {!openscannerwindow ? (
                                  <React.Fragment>
                                    <div className="row">
                                      <div className="col-6 imgBlock">
                                        <div className="person-img-fill">
                                          <i
                                            className="bi bi-person-fill"
                                            style={{ fontSize: "38px" }}
                                          ></i>
                                        </div>
                                      </div>
                                      <div className="col-6 imgBlock">
                                        <div className="qr-img">
                                          <i
                                            className="bi bi-qr-code-scan"
                                            style={{ fontSize: "30px" }}
                                          ></i>
                                        </div>
                                      </div>
                                    </div>
                                    <div className="row">
                                      <div className="Img-capture">
                                        <button
                                          className="btn btn-primary iCpt"
                                          onClick={startCam}
                                        >
                                          Capture
                                        </button>
                                      </div>
                                    </div>
                                  </React.Fragment>
                                ) : (
                                  <React.Fragment>
                                    <QrReader
                                      delay={300}
                                      onError={handleErrorWebCam}
                                      onScan={handleScanWebCam}
                                    />
                                  </React.Fragment>
                                )}
                              </>
                            )}
                          </div>
                          <div className="cap-button-div">
                            {isShowVideo && (
                              <>
                                {imagefile ? (
                                  <Button
                                    className="regCapture mt-2"
                                    onClick={Retake}
                                  >
                                    Retake
                                  </Button>
                                ) : (
                                  <Button
                                    className="regCapture mt-2"
                                    onClick={capture}
                                  >
                                    Capture
                                  </Button>
                                )}
                              </>
                            )}
                          </div>
                        </div>
                      </Col>
                    </Row>

                    <div className="searched-datas">
                      {!searchvalue ? (
                        <div className="img-sec">
                          <div className="no-data-img">
                            <img
                              src="../img/search-box-waiting.svg"
                              alt="no-result"
                            />
                          </div>
                          <div className="bottom-data">
                            <p className="firsttext">
                              Refine your search by capturing the patient image
                              again
                            </p>
                            <p className="ortext">or</p>
                            <p className="optiontext">
                              Enter text to search by name / Date of Birth /
                              Mobile number / Health ID
                            </p>
                          </div>
                          <div className="orbuttton">
                            <div className="hori-or-line">
                              <div className="or-line-1" align="center"></div>
                              <div className="text">Or</div>
                              <div className="or-line-1" align="center"></div>
                            </div>
                          </div>
                          <div className="SrhRegBtn">
                            <SaveButton
                              butttonClick={closeModal}
                              toLink="/searchpatient"
                              class_name="regBtnPC"
                              button_name="Register New Patient"
                            />
                            {/* <Button as={Link} className="btn btn-primary regbutton" to="/searchpatient" onClick={closeModal}>Register New Patient</Button> */}
                          </div>
                        </div>
                      ) : (
                        <React.Fragment>
                          <div className="row data-row modal-data-row">
                            <div className="col-md-3">
                              <div className=" filterHeader">
                                <h5>Filter by</h5>
                              </div>
                              <div className="genderDiv">
                                <div className="shwText">
                                  <p className="mb-1 mt-1">
                                    <b>Gender</b>
                                  </p>
                                </div>
                                <div className="mt-2 ">
                                  <div>
                                    {movie.map((item, i) => (
                                      <div key={i}>
                                        <Form.Check
                                          key={i}
                                          value={item.gender}
                                          type="checkbox"
                                          className={isChecked(item.gender)}
                                          onChange={checkGender}
                                          label={item.gender}
                                          style={{ fontSize: "14px" }}
                                        />
                                      </div>
                                    ))}
                                  </div>
                                </div>
                              </div>

                              <hr />
                              <div className="">
                                <div className="shwText">
                                  <p className="mb-1 mt-1 ">
                                    <b>Village / Ward</b>
                                  </p>
                                </div>
                                <div className="form-group ward-search">
                                  <input
                                    type="text"
                                    className="form-control war-search"
                                    placeholder="Search Village / Ward Name"
                                    onChange={searchItems}
                                  />
                                  <span className="fa fa-search form-control-feedback"></span>
                                </div>
                                <div className="village-list">
                                  {filteredResult.length != 0 ? (
                                    <>
                                      {filteredResult.map((item, i) => (
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
                                    </>
                                  ) : (
                                    <>
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
                                    </>
                                  )}
                                </div>
                              </div>
                            </div>

                            <div className="col-md-9 listDiv text-left px-2 mb-3">
                              {/* <label className="resultText"><React.Fragment>Total results:</React.Fragment> {users.length}</label> */}
                              <label className="resultText">
                                Showing{" "}
                                <b>
                                  1-
                                  {!filteredGenre ? (
                                    <>{users.length}</>
                                  ) : (
                                    <>{filteredGenre.length}</>
                                  )}
                                </b>{" "}
                                of <b>{users.length}</b>
                              </label>
                              <div className="row  mt-1">
                                {users.length ? (
                                  <div>
                                    {!filteredGenre
                                      ? "Loading..."
                                      : filteredGenre.map((item, index) => (
                                          <PatientRow
                                            key={index}
                                            healthId={item.properties?.healthID}
                                            salutation={
                                              item.properties?.salutation
                                            }
                                            firstName={
                                              item.properties?.firstName
                                            }
                                            middleName={
                                              item.properties?.middleName
                                            }
                                            lastName={item.properties?.lastName}
                                            UHId={item.UHId}
                                            gender={item.properties?.gender}
                                            age={item.properties?.age}
                                            dateOfBirth={
                                              item.properties?.dateOfBirth
                                            }
                                            mobile={item.properties?.contact}
                                            pat_uuid={item.properties.uuid}
                                            setUuidcitizenid={setUuidcitizenid}
                                            handleClose={handleClose}
                                            setShowModal={setShowModal}
                                            setUuuid={setUuuid}
                                            regStatus={
                                              item.regStatus && item.regStatus
                                            }
                                          />
                                        ))}

                                    <ReactPaginate
                                      previousLabel={"Previous"}
                                      nextLabel={"Next"}
                                      breakLabel={"...."}
                                      pageCount={pageCount}
                                      pageRangeDisplayed={3}
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
                                      breakClassName={"page-item sup"}
                                      breakLinkClassName={"page-link"}
                                      activeClassName={"active"}
                                    />
                                    <div
                                      className="patient-reg d-flex"
                                      align="center"
                                    >
                                      <div className="ptext">
                                        <p>
                                          If you are sure, patient is not
                                          register, then&nbsp;&nbsp;
                                        </p>
                                        <Button
                                          as={Link}
                                          className="btn btn-primary pbutton"
                                          to="/searchpatient"
                                          onClick={closeModal}
                                        >
                                          Register New Patient
                                        </Button>
                                      </div>
                                    </div>
                                  </div>
                                ) : (
                                  <div className="nodata-sec">
                                    <img
                                      src="../img/search-box-waiting.svg"
                                      alt="no-result"
                                    />
                                  </div>
                                )}
                              </div>
                            </div>
                          </div>
                        </React.Fragment>
                      )}
                    </div>
                  </React.Fragment>
                </Modal>
              )}

              <RegForms uuidofcitizen={uuuid && uuuid} />
            </React.Fragment>
          </React.Fragment>
        </Col>
      </Row>
    </>
  );
}

export default SearchPatient;
