import React, { useState, useRef, useEffect, useCallback } from "react";
import "react-html5-camera-photo/build/css/index.css";
import Camera, { FACING_MODES, IMAGE_TYPES } from "react-html5-camera-photo";
import CameraAltIcon from "@material-ui/icons/CameraAlt";
import "../../../css/SearchPatient.css";
import "../../../css/RegisterNewPatient.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "../../../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../../../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import {
  Button,
  Col,
  Row,
  Image,
  Dropdown,
  Form,
  ButtonGroup,
} from "react-bootstrap";
import { useHistory } from "react-router-dom";
import ReactPaginate from "react-paginate";
import ModalPopups from "../ModalPopups/ModalPopups";
import moment from "moment";
import { Link } from "react-router-dom";
import QRCode from "qrcode";
import QrReader from "react-qr-reader";
import Sidemenus from "../../../components/Sidemenus";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Sidemenu from "../../../components/Sidemenus";
import Webcam from "react-webcam";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import { loadSinglePatient } from "../../../redux/actions";
import { useDispatch, useSelector } from "react-redux";
import SignedHealthIDImage from "../../Dashboard/SignedHealthIDImage";
import SaveButton from "../../EMR_Buttons/SaveButton";
import PatientRow from "../../PatientRow/PatientRow";
import { loadSerchedPatient } from "../../../redux/encounterAction";
import SpeechRecgongition from "../../Dashboard/QueueManagement/SpeechRecgongition";

const inputbox = {
  height: "30px",
};

const genderArray = [
  { gender: "Male" },
  { gender: "Female" },
  { gender: "Other" },
];

let searchval;
let pagenumber;
let filtergenderdata = [];
let filtervillagedata = [];
const healthidcheckbox = [
  { healthid: "With ABHA Number (Health ID)" },
  { healthid: "Without ABHA Number (Health ID)" },
];
function EncounterSearch() {
  const [types, setTypes] = useState("patient");
  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const [users, setUsers] = useState([]);
  const [pageCount, setPageCount] = useState(1);
  const [filteredGenre, setFilteredGenre] = useState([]);
  let dispatch = useDispatch();
  let history = useHistory();

  //pagination
  const [currentPage, setCurrentPage] = useState(1);
  const { encounterData } = useSelector((state) => state.visitData);
  const { pageCountSearch } = useSelector((state) => state.visitData);

  useEffect(() => {
    if (pageCountSearch != "") {
      // let totalPage = Math.ceil(pageCountSearch / 25)
      setPageCount(parseInt(pageCountSearch));
    }
    if (encounterData) {
      setFilteredGenre(encounterData);
      setUsers(encounterData);
    }
  }, [pageCountSearch, encounterData]);

  pagenumber = currentPage;

  //end pagination

  const [villageData, setVillageData] = useState([]);
  const [healthData, setHealthdata] = useState([]);
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
  searchval = searchvalue;

  const [villageName, setVillageName] = useState([]);
  const sortedList = villageName.sort((a, b) =>
    a.properties.name.localeCompare(b.properties.name)
  );

  const [openscannerwindow, setOpenscannerwindow] = useState(false);
  const [scanResultWebCam, setScanResultWebCam] = useState("");

  const [searchplaceholder, setSearchplaceholder] = useState(
    "Enter name to search"
  );
  const [searchtypevaltype, setSearchtypevaltype] = useState("Name");
  const [d_o_b, setD_o_b] = useState(false);

  const [status, setStatus] = useState(false);
  const [username, setUsername] = useState(true);
  const [contactnumber, setContactnumber] = useState(false);
  const [abahanumber, setAbahanumber] = useState(false);
  const [uhidn, setUhidn] = useState(false);
  const [Healthid, setHid] = useState(false);
  const selectsearchtype = (e) => {
    const xx = e.target.value;
    if (xx === "Name") {
      setSearchplaceholder("Enter name to search");
      setD_o_b(false);
      setUsername(true);
      setContactnumber(false);
      setUhidn(false);
      setHid(false);
    } else if (xx === "DateOfBirth") {
      setSearchplaceholder("Enter Date of Birth to search");
      setD_o_b(true);
      setContactnumber(false);
      setUsername(false);
      setUhidn(false);
      setHid(false);
    } else if (xx === "MobileNumber") {
      setSearchplaceholder("Enter contact number to search");
      setD_o_b(false);
      setContactnumber(true);
      setUsername(false);
      setUhidn(false);
      setHid(false);
    } else if (xx === "HealthID") {
      setSearchplaceholder("Enter Health Id to search");
      setHid(true);
      setD_o_b(false);
      setContactnumber(false);
      setUsername(false);
      setAbahanumber(false);
      setUhidn(false);
    } else if (xx === "UHID") {
      setSearchplaceholder("Enter UHID Number to search");
      setUhidn(true);
      setD_o_b(false);
      setAbahanumber(false);
      setContactnumber(false);
      setUsername(false);
      setHid(false);
    }
    setSearchtypevaltype(xx);
  };

  const openscanner = () => {
    setOpenscannerwindow(true);
    setSearchplaceholder("Enter ABHA Number(Health ID) to search");
    setD_o_b(false);
    setContactnumber(false);
    setAbahanumber(true);
    setUsername(false);
    setSearchtypevaltype("UHID");
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

  // set searchBy Name
  const searchByname = async (nameval, searchtypevaltype) => {
    let searchval = nameval;
    if (searchval.length == 0) {
      filtergenderdata = [];
      filtervillagedata = [];
      setSearchvalue("");
      setGenderData("");
      setVillageData("");
    } else {
      setSearchvalue(nameval);
    }

    if (searchval == "Invalid date") {
    } else if (searchval != "") {
      dispatch(loadSerchedPatient(searchtypevaltype, searchval, pagenumber));
    }
  };

  useEffect(() => {
    let phcuuid = sessionStorage.getItem("uuidofphc");
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

  const handlePageClick = async (data) => {
    pagenumber = data.selected + 1;
    // setCurrentPage(currentPage);
    searchByname(
      searchval,
      searchtypevaltype,
      pagenumber,
      filtergenderdata,
      filtervillagedata
    );
  };

  // const [usergender, setUsergender] = useState([]);
  const [genderData, setGenderData] = useState([]);

  const checkGender = async (event) => {
    if (event.target.checked) {
      setGenderData([...genderData, event.target.value]);
      filtergenderdata = [...filtergenderdata, event.target.value];
    } else {
      setGenderData(genderData.filter((id) => id !== event.target.value));
      filtergenderdata = genderData.filter((id) => id !== event.target.value);
    }
  };

  const [selectVillage, setSelectVillage] = useState([]);
  const checkvillagefilter = async (event) => {
    if (event.target.checked) {
      setSelectVillage([...selectVillage, event.target.value]);
    } else {
      setSelectVillage(selectVillage.filter((id) => id !== event.target.value));
    }
  };

  const applyFilters = () => {
    let updatedList = users;

    if (genderData.length != 0 && selectVillage.length != 0) {
      updatedList = updatedList.filter((user) =>
        genderData.some((data) =>
          selectVillage.some(
            (vill) =>
              [user.citizen?.gender].flat().includes(data) &&
              [user.lgdCode].flat().includes(vill)
          )
        )
      );
    } else if (genderData.length != 0 && selectVillage.length == 0) {
      updatedList = updatedList.filter((user) =>
        genderData.some((data) => [user.citizen?.gender].flat().includes(data))
      );
    } else if (genderData.length == 0 && selectVillage.length != 0) {
      updatedList = updatedList.filter((user) =>
        selectVillage.some((vill) => [user.lgdCode].flat().includes(vill))
      );
    } else if (genderData.length == 0 && selectVillage.length == 0) {
      updatedList = users;
    }

    setFilteredGenre(updatedList);
    // setPageCount(1)
  };

  useEffect(() => {
    document.title = "EMR Search Patient";
    applyFilters();
  }, [genderData, healthData, selectVillage, users]);

  const [show, setShow] = useState(false);
  const handleClose = () => {
    setIssueHealthIdWindow(false);
    setShow(false);
  };

  const Vicon = {
    justifyContent: "end",
    position: "relative",
    marginLeft: "70%",
    fontSize: "30px",
    justifyContent: "end",
    float: "right",
  };

  var popUpObj;
  var popUpForm;
  function showModalPopUp() {
    popUpObj = window.open(
      "http://healthidsbx.ndhm.gov.in/facility?requestId=123456&customCode=s3de",
      "HidModalPopUp",
      "toolbar=no," +
        "scrollbars=yes," +
        "location=no," +
        "statusbar=no," +
        "menubar=no," +
        "directories=no," +
        "resizable=no," +
        "width=600," +
        "height=800," +
        "left=100," +
        "top=100," +
        "copyhistory=no"
    );
    popUpObj.focus();
  }

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

  const onButtonClick = () => {
    inputFile.current.click();
  };

  const [firstname, setFirstname] = useState("");
  const [middlename, setMiddlename] = useState("");
  const [lastname, setLastname] = useState("");
  const [healthid, setHealthid] = useState("");
  const [citizenid_uusid, setCitizenid_uusid] = useState("");
  const [citizenGender, setCitizenGender] = useState("");
  const [errorvalue, setErrorvalue] = useState("");
  const settingerrorvalue = () => {
    if (healthid === "") {
      setErrorvalue("Please enter ABHA Number");
    } else if (healthid.length < 18) {
      setErrorvalue("Please Enter 14 digit ABHA number");
    } else if (healthid.length === 18) {
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

  const [isLoading, setLoading] = useState(false);
  const [isNewLoading, setNewLoading] = useState(false);

  function loderCancel() {
    setNewLoading(false);
    history.push("/generatedIds");
  }

  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };

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

  const [isShowVideo, setIsShowVideo] = useState(false);
  const startCam = () => {
    setIsShowVideo(true);
  };
  const videoElement = useRef(null);
  const [imagefile, setImagefile] = useState("");
  const [imgSrc, setImgSrc] = useState(null);

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
          setImgSrc(imageSrc);
          Tostify.notifyFail("Please capture the face only..!");
        } else {
          let faceID;
          if (res.message == "No Health ID Found") {
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
                console.log(res["content"]);
                if (res["content"][0] == undefined) {
                  Tostify.notifyWarning("FaceId not recongising any Data");
                } else {
                  let healthId = res["content"][0]["properties"]["healthID"];
                  setUsername(false);
                  setHid(true);
                  setSearchplaceholder("Enter Health Id to search");
                  setSearchvalue(res["content"][0]["healthID"]);
                  setSearchtypevaltype("HealthID");
                  let searchType = "HealthID";
                  searchByname(
                    res["content"][0]["properties"]["healthID"],
                    searchType
                  );

                  fetch(
                    `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`,
                    serviceHeaders.getRequestOptions
                  )
                    .then((res) => res.json())
                    .then((res) => {
                      setImgSrc(res.preFetchURL);
                    });
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

  // code for retake photo
  const Retake = () => {
    setImgSrc(null);
    setImagefile("");
    setHealthid("");
  };
  // code for retake photo

  return (
    <Row className="main-div ">
      <Col lg={2} className="side-bar">
        <Sidemenu />
      </Col>
      <Col lg={10} md={12} sm={12} xs={12}>
        <>
          <ToastContainer />
          {/* <SpeechRecgongition /> */}

          <div>
            <div>
              {isLoading && (
                <div className="loder-container" align="center">
                  <div className="loadBox" align="center">
                    <div>
                      <p className="">Registering New Patient</p>
                    </div>
                    <br></br>
                    <div>
                      <img
                        style={{ width: "80px", height: "80px" }}
                        className=""
                        src="../img/circle-loader.svg"
                      />
                    </div>
                    <br />
                    <div>
                      <p className="">
                        Please wait while we register new patient into our
                        records
                      </p>
                    </div>
                  </div>
                </div>
              )}
              {isNewLoading && (
                <div className="loder-container" align="center">
                  <div className="loadBox" align="center">
                    <div>
                      <p className="">
                        Register ABHA Number(health id) updated successfully
                      </p>
                    </div>
                    <br></br>
                    <div>
                      <img
                        style={{ width: "80px", height: "80px" }}
                        className=""
                        src="../img/success-loadericon.svg"
                      />
                    </div>
                    <br />
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="loadBtn"
                        onClick={loderCancel}
                      >
                        okay{" "}
                      </Button>
                    </div>
                  </div>
                </div>
              )}

              {issueHealthIdWindow ? (
                <div style={{ minHeight: "71vh" }}>
                  <div className="regHeader">
                    <h1 className="register-Header">
                      Issue ABHA Number(Health ID)
                    </h1>
                    <hr />
                  </div>
                  <div className="health-id-div">
                    <Row>
                      <Col md={2}></Col>
                      <Col md={8}>
                        <div>
                          <Row>
                            {" "}
                            <Col md={12}>
                              <Form className="HealthForm">
                                <Row className="healthIdRow">
                                  <Col md={1} align="center">
                                    <SignedHealthIDImage
                                      healthID={array3[item]?.Patient?.healthId}
                                    />
                                  </Col>
                                  <Col md={4} align="center">
                                    <div className="dataName">
                                      <div>
                                        <p className="dataP">
                                          {healthiddata.name}
                                        </p>
                                      </div>
                                      <div>
                                        <p
                                          className="dataP"
                                          style={{
                                            color: "red",
                                            paddingTop: "1%",
                                          }}
                                        >
                                          <em>Issue healthid</em>
                                        </p>
                                      </div>
                                    </div>
                                  </Col>
                                  <Col md={1}>
                                    <div className="dataGender" align="center">
                                      <div>
                                        <p>{healthiddata.gender}</p>
                                      </div>
                                    </div>
                                  </Col>
                                  <Col md={2}>
                                    <div className="dataAge" align="center">
                                      <p className="dataP"></p>
                                    </div>
                                  </Col>
                                  <Col md={3}>
                                    <div className="data-caste" align="center">
                                      <div>
                                        <p></p>
                                      </div>
                                    </div>
                                  </Col>
                                </Row>
                                <Row className="healthIdForm">
                                  <Col md={4}>
                                    <div className="id-gen-steps">
                                      <div className="steps">
                                        <h3>Step-1</h3>
                                      </div>
                                      <div className="step-text">
                                        <p>
                                          Enter ABHA Number(Health ID) of the
                                          resident <em>{healthiddata.name}</em>{" "}
                                          for whom you are issuing the health ID
                                        </p>
                                      </div>
                                    </div>
                                    <div className="id-gen-steps2">
                                      <div className="steps">
                                        <h3>Step-2</h3>
                                      </div>
                                      <div className="step-text">
                                        <p>
                                          Capture Photo of the resident with
                                          ABHA Number(Health ID) card in their
                                          hand.
                                        </p>
                                      </div>
                                    </div>
                                    <div className="id-gen-steps3">
                                      <div className="steps">
                                        <h3>Step-3</h3>
                                      </div>
                                      <div className="step-text">
                                        <p>
                                          Once all data is filled, Click Submit
                                        </p>
                                      </div>
                                    </div>
                                  </Col>
                                  <Col md={8}>
                                    <div className="Id-Form">
                                      <div className="text-box">
                                        <Form.Group
                                          className=""
                                          controlId="exampleForm.FName"
                                        >
                                          <Row>
                                            <Form.Label className="require">
                                              Health ID{" "}
                                              <span style={{ color: "red" }}>
                                                *{errorvalue}
                                              </span>
                                              <span></span>
                                            </Form.Label>
                                            <Col md={10}>
                                              {" "}
                                              <Form.Control
                                                className="formControl "
                                                id="health-id-form"
                                                type="number"
                                                onChange={(e) =>
                                                  settingerrorvalue(
                                                    setHealthid(e.target.value)
                                                  )
                                                }
                                                maxLength={14}
                                              />
                                            </Col>
                                          </Row>
                                        </Form.Group>
                                      </div>
                                      <div>
                                        <div id="cameraphoto">
                                          {" "}
                                          {!imageuri ? (
                                            <div className="healthImg">
                                              <Camera
                                                onTakePhoto={(dataUri) => {
                                                  handleTakePhoto(dataUri);
                                                }}
                                                onTakePhotoAnimationDone={(
                                                  dataUri
                                                ) => {
                                                  handleTakePhotoAnimationDone(
                                                    dataUri
                                                  );
                                                }}
                                                onCameraError={(error) => {
                                                  handleCameraError(error);
                                                }}
                                                idealFacingMode={
                                                  FACING_MODES.ENVIRONMENT
                                                }
                                                idealResolution={{
                                                  width: 640,
                                                  height: 480,
                                                }}
                                                imageType={IMAGE_TYPES.JPG}
                                                imageCompression={0.97}
                                                isMaxResolution={true}
                                                isImageMirror={false}
                                                isSilentMode={false}
                                                isDisplayStartCameraError={true}
                                                isFullscreen={false}
                                                sizeFactor={1}
                                                onCameraStart={(stream) => {
                                                  handleCameraStart(stream);
                                                }}
                                                onCameraStop={() => {
                                                  handleCameraStop();
                                                }}
                                              ></Camera>
                                            </div>
                                          ) : (
                                            <img src={imageuri} />
                                          )}
                                        </div>
                                        <Row className="id-btn">
                                          <Col md={6}>
                                            <div align="center">
                                              <input
                                                style={{ display: "none" }}
                                                // accept=".zip,.rar"
                                                ref={inputFile}
                                                onChange={handleFileUpload}
                                                type="file"
                                              />
                                              <p style={{ display: "none" }}>
                                                <span
                                                  id="upload-file"
                                                  onClick={onButtonClick}
                                                >
                                                  Upload from file/folder
                                                </span>
                                              </p>
                                            </div>
                                          </Col>
                                          <Col md={6}>
                                            {/* <Button className="HealthCancel isselected" id="inner-circle" variant="outline-secondary">Capture</Button> */}
                                          </Col>
                                        </Row>
                                      </div>
                                      <Row className="healthRow">
                                        <div
                                          id="healthidenterdiv"
                                          align="center"
                                        >
                                          <div className="HealthButtons">
                                            <div className="save-btn-section">
                                              <SaveButton
                                                butttonClick={handleClose}
                                                class_name="regBtnPC"
                                                button_name="Cancel"
                                              />
                                            </div>
                                            <div className="save-btn-section">
                                              <SaveButton
                                                butttonClick={submitdata}
                                                class_name="regBtnN"
                                                button_name="Submit"
                                              />
                                            </div>
                                          </div>
                                        </div>
                                      </Row>
                                    </div>
                                  </Col>
                                </Row>
                              </Form>
                            </Col>
                          </Row>
                        </div>
                      </Col>
                      <Col lg={2}></Col>
                    </Row>
                  </div>
                </div>
              ) : (
                <div style={{ minHeight: "71vh" }}>
                  <div className="regHeader">
                    {searchvalue ? (
                      <h1 className="register-Header"> Create Visit </h1>
                    ) : (
                      <h1 className="register-Header">
                        Search Patient{" "}
                        <span style={{ display: "none" }}>
                          {" "}
                          <Sidemenus name="names" />
                        </span>
                      </h1>
                    )}
                    <hr />
                  </div>
                  <div className="search-bar">
                    <Row>
                      <Col md={1}></Col>
                      <Col md={7}>
                        <Row className="srchDiv">
                          <Col md={2} className="d-flex">
                            <h6 className="srchBy">Search by</h6>
                          </Col>
                          <Col md={3}>
                            <div>
                              <select
                                className="regDropdown form-select"
                                // defaultValue="Name"
                                value={searchtypevaltype || "Name" || ""}
                                onChange={selectsearchtype}
                              >
                                <option value="DateOfBirth">
                                  Date Of Birth
                                </option>
                                <option value="MobileNumber">
                                  Mobile Number
                                </option>
                                <option value="Name">Name</option>
                                <option value="HealthID">Health ID</option>
                                <option value="UHID">UHID</option>
                              </select>
                            </div>
                          </Col>
                          <Col md={7}>
                            <div className="searchForm">
                              {d_o_b == true ? (
                                <input
                                  className="form-control"
                                  type="date"
                                  key="random1"
                                  pattern="/^(0?[1-9]|1[0-2])[\/](0?[1-9]|[1-2][0-9]|3[01])[\/]\d{4}$/"
                                  style={inputbox}
                                  id="search-input"
                                  placeholder={searchplaceholder}
                                  max={currentdate}
                                  selected={searchvalue}
                                  validate={[disableFutureDt]}
                                  onChange={(e) =>
                                    searchByname(
                                      moment(new Date(e.target.value)).format(
                                        "YYYY-MM-DD"
                                      ),
                                      searchtypevaltype
                                    )
                                  }
                                />
                              ) : username == true ? (
                                <input
                                  className="form-control"
                                  type="search"
                                  key="random1"
                                  style={inputbox}
                                  id="search-input"
                                  placeholder={searchplaceholder}
                                  selected={searchvalue}
                                  onChange={(e) =>
                                    searchByname(
                                      e.target.value,
                                      searchtypevaltype
                                    )
                                  }
                                />
                              ) : contactnumber == true ? (
                                <div className="xbtn">
                                  <input
                                    key="random1"
                                    className="form-control"
                                    aria-describedby="search-addon"
                                    type="number"
                                    style={inputbox}
                                    id="search-input"
                                    placeholder={searchplaceholder}
                                    maxLength={10}
                                    selected={searchvalue}
                                    onChange={(e) =>
                                      searchByname(
                                        e.target.value,
                                        searchtypevaltype
                                      )
                                    }
                                  />
                                </div>
                              ) : abahanumber == true ? (
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
                                        searchtypevaltype
                                      )
                                    }
                                  />
                                </div>
                              ) : uhidn == true ? (
                                <input
                                  className="form-control"
                                  type="search"
                                  key="random1"
                                  style={inputbox}
                                  id="search-input"
                                  placeholder={searchplaceholder}
                                  selected={searchvalue}
                                  onChange={(e) =>
                                    searchByname(
                                      e.target.value,
                                      searchtypevaltype
                                    )
                                  }
                                />
                              ) : Healthid == true ? (
                                <div className="input-group">
                                  {imgSrc != null ? (
                                    <>
                                      <input
                                        key="random1"
                                        className="form-control"
                                        aria-describedby="search-addon"
                                        type="number"
                                        style={inputbox}
                                        value={searchvalue || ""}
                                        onChange={(e) =>
                                          searchByname(
                                            searchvalue,
                                            searchtypevaltype
                                          )
                                        }
                                      />
                                    </>
                                  ) : (
                                    <>
                                      <input
                                        key="random1"
                                        className="form-control"
                                        aria-describedby="search-addon"
                                        type="search"
                                        onKeyPress={(event) => {
                                          if (!/[0-9]/.test(event.key)) {
                                            event.preventDefault();
                                          }
                                        }}
                                        style={inputbox}
                                        placeholder={searchplaceholder}
                                        maxLength={14}
                                        selected={searchvalue}
                                        onChange={(e) =>
                                          searchByname(
                                            e.target.value,
                                            searchtypevaltype
                                          )
                                        }
                                      />
                                      <span className="input-group-append">
                                        <button
                                          className="btn rounded-pill border-0 ml-n5"
                                          type="button"
                                        >
                                          <i className="fa fa-search"></i>
                                        </button>
                                      </span>
                                    </>
                                  )}
                                </div>
                              ) : (
                                ""
                              )}
                            </div>
                          </Col>
                        </Row>
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
                      <Col md={2} align="center">
                        {!isShowVideo ? (
                          <div className="person-Img d-block">
                            <div className="imgBlock">
                              <i
                                className="bi bi-person-fill"
                                style={{ fontSize: "38px", padding: "0px 32%" }}
                              ></i>
                            </div>
                            <div className="Img-capture">
                              {!isShowVideo && (
                                <Button
                                  className="outline-secondary"
                                  onClick={startCam}
                                >
                                  Capture
                                </Button>
                              )}
                            </div>
                          </div>
                        ) : (
                          <React.Fragment>
                            <div className="web-cam d-block">
                              {imgSrc ? (
                                <img
                                  src={imgSrc}
                                  style={{
                                    height: "150px",
                                    width: "400px",
                                  }}
                                />
                              ) : (
                                <Webcam
                                  style={{
                                    height: "148px",
                                    width: "400px",
                                  }}
                                  audio={false}
                                  ref={videoElement}
                                  screenshotFormat="image/jpeg"
                                />
                              )}
                            </div>
                            <div className="Img-capture">
                              {isShowVideo && (
                                <>
                                  {imagefile ? (
                                    <Button
                                      className="outline-secondary"
                                      onClick={Retake}
                                    >
                                      Retake
                                    </Button>
                                  ) : (
                                    <Button
                                      className="outline-secondary"
                                      style={{ cursor: "pointer" }}
                                      onClick={capture}
                                    >
                                      Capture
                                    </Button>
                                  )}
                                </>
                              )}
                            </div>
                          </React.Fragment>
                        )}
                      </Col>
                    </Row>
                    {!searchvalue ? (
                      <Row>
                        <Col md={12} align="center">
                          <div className="">
                            <div className="Reg-Text d-flex">
                              <p className="box-text">
                                As patients may be registered in other PHC, we
                                recommend you to first search if patient already
                                exists in our records. If not, then register as
                                new patient
                                <br />{" "}
                              </p>
                            </div>
                            <div className="searchimage">
                              <img
                                src="../img/Search-svg.png"
                                alt="no-result"
                              />
                            </div>
                          </div>
                        </Col>
                      </Row>
                    ) : (
                      <>
                        {users?.length ? (
                          <div className="row data-row">
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
                                    {genderArray.map((item, i) => (
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
                              <label className="resultText">
                                Showing{" "}
                                <b>
                                  1-
                                  {!filteredGenre ? (
                                    <>{users?.length}</>
                                  ) : (
                                    <>{filteredGenre.length}</>
                                  )}
                                </b>{" "}
                                of <b>{users?.length}</b>
                              </label>
                              <div className="row  mt-1">
                                <div>
                                  {filteredGenre.length == 0 ? (
                                    <div style={{ padding: "30px" }}>
                                      No data found..!
                                    </div>
                                  ) : (
                                    filteredGenre.map((item, index) => (
                                      <PatientRow
                                        key={index}
                                        healthId={item.citizen?.healthId}
                                        salutation={item.citizen?.salutation}
                                        firstName={item.citizen?.firstName}
                                        middleName={item.citizen?.middleName}
                                        lastName={item.citizen?.lastName}
                                        UHId={item.UHId}
                                        gender={item.citizen?.gender}
                                        age={item.citizen?.age}
                                        dateOfBirth={item.citizen?.dateOfBirth}
                                        mobile={item.citizen?.mobile}
                                        _id={item._id}
                                      />
                                    ))
                                  )}
                                  {filteredGenre.length != 0 && (
                                    <ReactPaginate
                                      previousLabel={"Previous"}
                                      nextLabel={"Next"}
                                      breakLabel={"...."}
                                      pageCount={
                                        pageCount && parseInt(pageCount)
                                      }
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
                                  )}
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
                                      >
                                        Register New Patient
                                      </Button>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        ) : (
                          <div className="img-sec">
                            <div>
                              <div className="no-data-img">
                                <img
                                  src="../img/search-box-waiting.svg"
                                  alt="no-result"
                                />
                              </div>
                              <div className="bottom-data">
                                <p className="firsttext">
                                  Refine your search by capturing the patient
                                  image again
                                </p>
                                <p className="ortext">or</p>
                                <p className="optiontext">
                                  Enter text to search by name / Date of Birth /
                                  Mobile number / Health ID
                                </p>
                              </div>
                              <div className="orbuttton">
                                <div className="hori-or-line">
                                  <div
                                    className="or-line-1"
                                    align="center"
                                  ></div>
                                  <div className="text">Or</div>
                                  <div
                                    className="or-line-1"
                                    align="center"
                                  ></div>
                                </div>
                              </div>
                              <div className="SrhRegBtn">
                                <SaveButton
                                  toLink="/searchpatient"
                                  class_name="regBtnPC"
                                  button_name="Register New Patient"
                                />
                              </div>
                            </div>
                          </div>
                        )}
                      </>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        </>
      </Col>
    </Row>
  );
}

export default EncounterSearch;
