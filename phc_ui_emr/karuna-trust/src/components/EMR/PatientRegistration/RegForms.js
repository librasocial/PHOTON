import React, { useState, useRef, useCallback, useEffect } from "react";
import PageLoader from "../../PageLoader";
import {
  Row,
  Col,
  Button,
  Accordion,
  Image,
  Modal,
  Container,
  Form,
  Alert,
} from "react-bootstrap";
import "../../../css/RegForms.css";
import { Camera, FACING_MODES, IMAGE_TYPES } from "react-html5-camera-photo";
import "react-html5-camera-photo/build/css/index.css";
import moment from "moment";
import "react-datepicker/dist/react-datepicker.css";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import axios from "axios";
import * as constant from "../../ConstUrl/constant";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import Webcam from "react-webcam";
import CancelModal from "./CancelModal";
import Loader from "./Loader";
import * as Tostify from "../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import SaveButton from "../../EMR_Buttons/SaveButton";
import { useDispatch, useSelector } from "react-redux";
import { loadRegFormItems } from "../../../redux/formUtilityAction";
import {
  loadRegistration,
  loadUpdateRegistration,
} from "../../../redux/actions";
import {
  loadAllDistrict,
  loadAllState,
  loadAllTaluk,
  loadAllVillage,
} from "../../../redux/phcAction";
import PermanentAddress from "./PermanentAddress";
import ABDMPopUps from "../ABDMpopUps/ABDMPopUps";
import ABdMService from "./ABDMService/AbdmM2steps";
import {
  loadAbdmFetchModes,
  loadAbdmModeVerification,
} from "../../../redux/WSAction";
import SockJS from "sockjs-client";
import { over } from "stompjs";

function RegForms(props) {
  let citizenuuid = props.uuidofcitizen;

  const form = useRef();
  let dispatch = useDispatch();
  const { regFormItems } = useSelector((state) => state.formData);

  const { stateList } = useSelector((state) => state.phcData);
  const { districtListForPresent } = useSelector((state) => state.phcData);
  const { districtListForPermanent } = useSelector((state) => state.phcData);
  const { talukListForPresent } = useSelector((state) => state.phcData);
  const { talukListForPermanent } = useSelector((state) => state.phcData);

  // Store data for Ws Abdm
  // const { modes } = useSelector((state) => state.wsAbhaData);
  // console.log(modes,  "modesmodesmodes")
  // Store data for Ws Abdm

  const { villageListForPresent } = useSelector((state) => state.phcData);
  const { villageListForPermanent } = useSelector((state) => state.phcData);

  let sortedState;
  if (stateList) {
    sortedState = stateList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedDistrictPresent;
  if (districtListForPresent) {
    sortedDistrictPresent = districtListForPresent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedDistrictPermanent;
  if (districtListForPermanent) {
    sortedDistrictPermanent = districtListForPermanent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedTalukPresent;
  if (talukListForPresent) {
    sortedTalukPresent = talukListForPresent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedTalukPermanent;
  if (talukListForPermanent) {
    sortedTalukPermanent = talukListForPermanent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedVillagePresent;
  if (villageListForPresent) {
    sortedVillagePresent = villageListForPresent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedVillagePermanent;
  if (villageListForPermanent) {
    sortedVillagePermanent = villageListForPermanent.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }

  const [Lgdcode, setLgdcode] = useState([]);
  const [status, setStatus] = useState(false);

  var currentdate = moment(new Date()).format("YYYY-MM-DD");
  const d = new Date();
  d.setFullYear(d.getFullYear() - 120);
  var minYear = moment(d).format("YYYY-MM-DD");

  const [stateuuid, setStateuuid] = useState("");
  const [permanentState, setPermanentState] = useState("");
  const [distuuid, setDistuuid] = useState("");
  const [permanentDist, setPermanentDist] = useState("");
  const [talukuuid, setTalukuuid] = useState("");
  const [permanentTaluk, setPermanentTaluk] = useState("");

  const [patientId, setPatinentId] = useState("");
  const [isShowVideo, setIsShowVideo] = useState(false);

  const [imagedata, setImagedata] = useState("");

  const startCam = () => {
    setIsShowVideo(true);
  };
  // const [AbhaAddress, setAbhaAddress] = useState("");

  // const[abhaAddress,setAbhaAddress]=useState("");

  const [pageLoader, setPageLoader] = useState(false);

  const [isLoading1, setLoading1] = useState(false);
  const [isLoading2, setLoading2] = useState(false);
  const [isNewLoading, setNewLoading] = useState(false);
  const [photo, setPhoto] = useState("");
  const [identifier1, setIdentifier1] = useState("");
  const [identifier2, setIdentifier2] = useState("");
  const [copyAddData, setCopyAddData] = useState(false);
  const [uhid, setUHId] = useState("");

  const [presentAddress, setPresentAddress] = useState({
    addressLine: "",
    area: "",
    taluk: "",
    village: "",
    state: "",
    district: "",
    country: "",
    pinCode: "",
  });
  const [permanentAddress, setPermanentAddress] = useState({
    addressLine: "",
    area: "",
    taluk: "",
    village: "",
    state: "",
    district: "",
    country: "",
    pinCode: "",
  });
  const [moreInfo, setMoreInfo] = useState({
    birthPlace: "",
    birthIdentifiers: [identifier1, identifier2],
    annualIncome: "",
    education: "",
    primaryLanguage: "",
    speakEnglish: "",
    phone: "",
    emailId: "",
  });

  useEffect(() => {
    presentAddress.country = "India";
    // presentAddress.state = "Karnataka";
    if (
      copyAddData == true &&
      (presentAddress.addressLine ||
        presentAddress.country ||
        presentAddress.state)
    ) {
      // regFormData.address.present = presentAddress;
      regFormData.address = {
        present: presentAddress,
        permanentSameAsPresent: copyAddData,
      };
    } else if (
      copyAddData == true &&
      (presentAddress.addressLine ||
        presentAddress.country ||
        presentAddress.state ||
        permanentAddress.addressLine ||
        permanentAddress.country ||
        permanentAddress.state)
    ) {
      regFormData.address = {
        present: presentAddress,
        permanentSameAsPresent: copyAddData,
        permanent: permanentAddress,
      };
    }
    // if (permanentAddress.addressLine || permanentAddress.country || permanentAddress.state) {
    //     regFormData.address.permanent = permanentAddress;
    // }
    if (identifier1 || identifier2) {
      moreInfo.birthIdentifiers = [identifier1, identifier2];
    }
    regFormData.citizenMoreInfo = moreInfo;
  }, [
    presentAddress,
    permanentAddress,
    regFormData,
    moreInfo,
    identifier1,
    identifier2,
    copyAddData,
  ]);

  useEffect(() => {
    if (presentAddress.state) {
      stateList.map((item) => {
        if (
          item.target.properties.name.toLowerCase() ==
          presentAddress.state.toLowerCase()
        ) {
          setStateuuid(item.target.properties.uuid);
        }
      });
    }
    if (presentAddress.district) {
      districtListForPresent.map((item) => {
        if (
          item.target.properties.name.toLowerCase() ==
          presentAddress.district.toLowerCase()
        ) {
          setDistuuid(item.target.properties.uuid);
        }
      });
    }
    if (presentAddress.taluk) {
      talukListForPresent.map((item) => {
        if (item.target.properties.name == presentAddress.taluk) {
          setTalukuuid(item.target.properties.uuid);
        }
      });
    }
  }, [presentAddress, stateList, districtListForPresent, talukListForPresent]);

  useEffect(() => {
    if (permanentAddress.state) {
      stateList.map((item) => {
        if (item.target.properties.name == permanentAddress.state) {
          setPermanentState(item.target.properties.uuid);
        }
      });
    }
    if (permanentAddress.district) {
      districtListForPermanent.map((item) => {
        if (item.target.properties.name == permanentAddress.district) {
          setPermanentDist(item.target.properties.uuid);
        }
      });
    }
    if (permanentAddress.taluk) {
      talukListForPermanent.map((item) => {
        if (item.target.properties.name == permanentAddress.taluk) {
          setPermanentTaluk(item.target.properties.uuid);
        }
      });
    }
  }, [
    permanentAddress,
    stateList,
    districtListForPermanent,
    talukListForPermanent,
  ]);

  const [regFormData, setRegFormData] = useState({
    memberId: "",
    abhaAddress: "",
    healthId: "",
    salutation: "",
    firstName: "",
    middleName: "",
    lastName: "",
    photoUrl: "abc",
    mobile: "",
    residentType: "",
    gender: "",
    dateOfBirth: "",
    age: "",
    maritalStatus: "",
    religion: "",
    caste: "",
    // occupation: "", fatherName: "", spouseName: "", address: { present: "", permanentSameAsPresent: copyAddData, permanent: permanentAddress },
    occupation: "",
    fatherName: "",
    spouseName: "",
    address: "",
    citizenMoreInfo: moreInfo,
  });

  const {
    memberId,
    abhaAddress,
    healthId,
    salutation,
    firstName,
    middleName,
    lastName,
    photoUrl,
    mobile,
    residentType,
    gender,
    dateOfBirth,
    age,
    maritalStatus,
    religion,
    caste,
    occupation,
    fatherName,
    spouseName,
    address,
    present,
    permanentSameAsPresent,
    permanent,
    citizenMoreInfo,
    birthPlace,
    birthIdentifiers,
    annualIncome,
    education,
    primaryLanguage,
    speakEnglish,
    phone,
    emailId,
    addressLine,
    area,
    village,
    state,
    taluk,
    district,
    country,
    pinCode,
  } = regFormData;

  // useEffect(() => {
  if (copyAddData == true) {
    regFormData.address = {
      present: presentAddress,
      permanentSameAsPresent: copyAddData,
    };
  } else {
    regFormData.address = {
      present: presentAddress,
      permanentSameAsPresent: copyAddData,
      permanent: permanentAddress,
    };
  }
  // }, [presentAddress, permanentAddress])

  const handleRegForm = (e) => {
    const { name, value } = e.target;
    setRegFormData({ ...regFormData, [name]: value });
    if (e.target.name == "salutation") {
      if (e.target.value == " Mr") {
        regFormData.gender = "Male";
      } else if (e.target.value == "Ms" || e.target.value == "Mrs") {
        regFormData.gender = "Female";
      }
      setRegFormData({ ...regFormData, [name]: value });
    }
    if (e.target.name == "gender") {
      if (e.target.value == "Male") {
        regFormData.salutation = " Mr";
      } else if (e.target.value == "Female") {
        regFormData.salutation = "Mrs";
      }
      setRegFormData({ ...regFormData, [name]: value });
    }
    if (e.target.name == "age") {
      if (e.target.value != "") {
        let age1 = Math.floor(e.target.value);
        let newyear = thisyear - age1;
        const my_date = newyear + "-" + thismonth + "-" + thisdate;
        regFormData.dateOfBirth = my_date;
        setRegFormData({ ...regFormData, [name]: value });
      } else {
        regFormData.dateOfBirth = "";
      }
    }
    if (e.target.name == "dateOfBirth") {
      if (
        !moment(e.target.max).isSameOrAfter(
          moment(e.target.value).format("YYYY-MM-DD")
        )
      ) {
        // setRegFormData({ ...regFormData, [name]: "" });
        setRegFormData({ ...regFormData, age: "", dateOfBirth: "" });
        Tostify.notifyWarning(
          "You are not Suppose to Enter Future Date in Date Of Birth...!"
        );
      } else {
        regFormData.dateOfBirth = e.target.value;
        let today = new Date(),
          dob = new Date(moment(e.target.value).format("YYYY-MM-DD")),
          diff = today.getTime() - dob.getTime(),
          years = Math.floor(diff / 31556736000),
          days_diff = Math.floor((diff % 31556736000) / 86400000),
          months = Math.floor(days_diff / 30.4167);
        regFormData.age = `${years}.${months}`;
        setRegFormData({ ...regFormData, [name]: value });
      }
    }
  };

  const handlePresentAddress = (e) => {
    const { name, value } = e.target;
    if (e.target.name == "village") {
      let idx = e.target.selectedIndex;
      let dataset = e.target.options[idx].dataset.isd;
      setLgdcode(dataset);
    }
    if (name == "state") {
      stateList.map((item) => {
        if (item.target.properties.name == value) {
          setStateuuid(item.target.properties.uuid);
        }
      });
    }
    if (name == "district") {
      districtListForPresent.map((item) => {
        if (item.target.properties.name == value) {
          setDistuuid(item.target.properties.uuid);
        }
      });
    }
    if (name == "taluk") {
      talukListForPresent.map((item) => {
        if (item.target.properties.name == value) {
          setTalukuuid(item.target.properties.uuid);
        }
      });
    }
    setPresentAddress({ ...presentAddress, [name]: value });
  };

  const handlePermanentAddress = (e) => {
    const { name, value } = e.target;
    if (name == "state") {
      stateList.map((item) => {
        if (item.target.properties.name == value) {
          setPermanentState(item.target.properties.uuid);
        }
      });
    }
    if (name == "district") {
      districtListForPermanent.map((item) => {
        if (item.target.properties.name == value) {
          setPermanentDist(item.target.properties.uuid);
        }
      });
    }
    if (name == "taluk") {
      talukListForPermanent.map((item) => {
        if (item.target.properties.name == value) {
          setPermanentTaluk(item.target.properties.uuid);
        }
      });
    }
    setPermanentAddress({ ...permanentAddress, [name]: value });
  };
  const handleMoreInfo = (e) => {
    const { name, value } = e.target;
    setMoreInfo({ ...moreInfo, [name]: value });
  };
  let prsentType = "Present";
  useEffect(() => {
    dispatch(loadAllState());
    if (stateuuid) {
      dispatch(loadAllDistrict(stateuuid, prsentType));
    }
    if (distuuid) {
      dispatch(loadAllTaluk(distuuid, prsentType));
    }
    if (talukuuid) {
      dispatch(loadAllVillage(talukuuid, prsentType));
    }
  }, [stateuuid, distuuid, talukuuid]);

  let permanentType = "Permanent";
  useEffect(() => {
    dispatch(loadAllState());
    if (permanentState) {
      dispatch(loadAllDistrict(permanentState, permanentType));
    }
    if (permanentDist) {
      dispatch(loadAllTaluk(permanentDist, permanentType));
    }
    if (permanentTaluk) {
      dispatch(loadAllVillage(permanentTaluk, permanentType));
    }
  }, [permanentState, permanentDist, permanentTaluk]);

  // set State for health Id when get data by entering healthId
  const [searchHealthId, setSearchHealthId] = useState("");
  const [searchDataID, setSearchDataID] = useState("");
  // set State for health Id when get data by entering healthId

  // get data for perticular id for update
  useEffect(() => {
    document.title = "EMR Patient Registration";
    dispatch(loadRegFormItems());
    if (citizenuuid != "") {
      fetchcitizendata(citizenuuid);
    }
  }, [citizenuuid, status]);

  const fetchcitizendata = () => {
    if (citizenuuid != "") {
      fetch(
        `${constant.ApiUrl}/member-svc/members/${citizenuuid}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          regFormData.memberId = res["content"][0]["properties"]["uuid"];
          regFormData.healthId = res["content"][0]["properties"]["healthID"];
          let healthId = res["content"][0]["properties"]["healthID"];
          // regFormData.firstName = res["content"][0]["properties"]["firstName"];
          // regFormData.middleName =
          //   res["content"][0]["properties"]["middleName"];
          // regFormData.lastName = res["content"][0]["properties"]["lastName"];
          // regFormData.mobile = res["content"][0]["properties"]["contact"];
          // regFormData.gender = res["content"][0]["properties"]["gender"];
          // regFormData.age = res["content"][0]["properties"]["age"];
          // regFormData.dateOfBirth =
          //   res["content"][0]["properties"]["dateOfBirth"];
          // regFormData.residentType = "Resident";
          let newDate = new Date();
          regFormData.maritalStatus =
            res["content"][0]["properties"]["maritalStatus"];
          regFormData.religion = res["content"][0]["properties"]["religion"];
          regFormData.caste = res["content"][0]["properties"]["caste"];
          regFormData.occupation =
            res["content"][0]["properties"]["occupation"];
          if (res["content"][0]["citizenMoreInfo"]) {
            setMoreInfo(res["content"][0]["citizenMoreInfo"]);
            setIdentifier1(
              res["content"][0]["citizenMoreInfo"]["birthIdentifiers"][0]
            );
            setIdentifier2(
              res["content"][0]["citizenMoreInfo"]["birthIdentifiers"][1]
            );
          }

          if (healthId) {
            fetch(
              `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`,
              serviceHeaders.getRequestOptions
            )
              .then((res) => res.json())
              .then((res) => {
                setImgSrc(res.preFetchURL);
              });
          } else {
            setImgSrc(null);
          }
        });
    }
  };
  // get data for perticular id for update

  // get patient data for perticular id for update
  const [pid, setpId] = useState("");
  const { id } = useParams();
  let citizenPatientId_id = id;

  useEffect(() => {
    fetchpatientdata();
  }, [citizenPatientId_id, status]);
  const fetchpatientdata = () => {
    if (citizenPatientId_id != undefined) {
      fetch(
        `${constant.ApiUrl}/patients/` + citizenPatientId_id,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          let healthId;
          // setStatus(false)
          if (res["citizen"]["healthId"] != null) {
            regFormData.healthId = res["citizen"]["healthId"];
            healthId = res["citizen"]["healthId"];
          }
          setPresentAddress(res["citizen"]["address"]["present"]);
          setUHId(res["UHId"]);
          setpId(res["_id"]);
          regFormData.memberId = res["citizen"]["memberId"];
          regFormData.abhaAddress = res["citizen"]["abhaAddress"];
          regFormData.salutation = res["citizen"]["salutation"];
          regFormData.firstName = res["citizen"]["firstName"];
          regFormData.middleName = res["citizen"]["middleName"];
          regFormData.lastName = res["citizen"]["lastName"];
          regFormData.fatherName = res["citizen"]["fatherName"];
          regFormData.mobile = res["citizen"]["mobile"];
          regFormData.gender = res["citizen"]["gender"];
          regFormData.residentType = res["citizen"]["residentType"];
          regFormData.age = res["citizen"]["age"];
          let dob = moment(res["citizen"]["dateOfBirth"]).format("YYYY-MM-DD");
          regFormData.dateOfBirth = dob;
          regFormData.maritalStatus = res["citizen"]["maritalStatus"];
          regFormData.religion = res["citizen"]["religion"];
          regFormData.caste = res["citizen"]["caste"];
          regFormData.occupation = res["citizen"]["occupation"];
          if (res["citizenMoreInfo"]) {
            setMoreInfo(res["citizenMoreInfo"]);
            setIdentifier1(res["citizenMoreInfo"]["birthIdentifiers"][0]);
            setIdentifier2(res["citizenMoreInfo"]["birthIdentifiers"][1]);
          }
          if (
            !res["citizen"]["address"]["permanentSameAsPresent"] ||
            res["citizen"]["address"]["permanentSameAsPresent"] == false
          ) {
            setCopyAddData(false);
            setPermanentAddress(res["citizen"]["address"]["permanent"]);
          } else {
            setCopyAddData(res["citizen"]["address"]["permanentSameAsPresent"]);
          }
          if (healthId) {
            fetch(
              `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`,
              serviceHeaders.getRequestOptions
            )
              .then((res) => res.json())
              .then((res) => {
                setImgSrc(res.preFetchURL);
              });
          } else {
            setImgSrc(null);
          }
        });
    }
  };
  // get patient data for perticular id for update
  let newDate = new Date();
  let thisyear = moment(newDate).format("YYYY");
  let thismonth = moment(newDate).format("MM");
  let thisdate = moment(newDate).format("DD");

  const searchbyid = (e) => {
    const searchHealthId = e;
    if (
      searchHealthId != "" &&
      searchHealthId != undefined &&
      searchHealthId.length == 14
    ) {
      fetch(
        `${constant.ApiUrl}/patients/search?value=${searchHealthId}&fieldName=HealthID&page=1`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res["data"].length != 0) {
            if (searchHealthId == res["data"][0]?.citizen?.healthId) {
              setSearchHealthId(res["data"][0]?.citizen?.healthId);
              let healthId = res["data"][0]?.citizen?.healthId;
              let dob = moment(res["data"][0].citizen?.dateOfBirth).format(
                "YYYY-MM-DD"
              );
              setRegFormData({
                memberId: res["data"][0].citizen?.memberId,
                abhaAddress: res["data"][0].citizen?.abhaAddress,
                healthId: res["data"][0]?.citizen?.healthId,
                salutation: res["data"][0].citizen?.salutation,
                firstName: res["data"][0].citizen?.firstName,
                middleName: res["data"][0].citizen?.middleName,
                lastName: res["data"][0].citizen?.lastName,
                photoUrl: "abc",
                mobile: res["data"][0].citizen?.mobile,
                residentType: res["data"][0].citizen?.residentType,
                gender: res["data"][0].citizen?.gender,
                dateOfBirth: dob,
                age: res["data"][0].citizen?.age,
                maritalStatus: res["data"][0].citizen?.maritalStatus,
                religion: res["data"][0].citizen?.religion,
                caste: res["data"][0].citizen?.caste,
                occupation: res["data"][0].citizen?.occupation,
                fatherName: res["data"][0].citizen?.fatherName,
                address: {
                  present: "",
                  permanentSameAsPresent: copyAddData,
                  permanent: "",
                },
                citizenMoreInfo: moreInfo,
              });
              setSearchDataID(res["data"][0]._id);

              regFormData.maritalStatus = res["data"][0].citizen?.maritalStatus;
              regFormData.religion = res["data"][0].citizen?.religion;
              regFormData.caste = res["data"][0].citizen?.caste;
              regFormData.occupation = res["data"][0].citizen?.occupation;
              setPresentAddress(
                res["data"][0]["citizen"]["address"]["present"]
              );
              if (res["data"][0]["citizenMoreInfo"]) {
                setMoreInfo(res["data"][0]["citizenMoreInfo"]);
                setIdentifier1(
                  res["data"][0]["citizenMoreInfo"]["birthIdentifiers"][0]
                );
                setIdentifier2(
                  res["data"][0]["citizenMoreInfo"]["birthIdentifiers"][1]
                );
              }

              if (
                !res["data"][0]["citizen"]["address"][
                  "permanentSameAsPresent"
                ] ||
                res["data"][0]["citizen"]["address"][
                  "permanentSameAsPresent"
                ] == false
              ) {
                setCopyAddData(false);
                setPermanentAddress(res["data"][0].citizen?.address?.permanent);
              } else {
                setCopyAddData(
                  res["data"][0]["citizen"]["address"]["permanentSameAsPresent"]
                );
              }

              if (healthId) {
                fetch(
                  `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`,
                  serviceHeaders.getRequestOptions
                )
                  .then((res) => res.json())
                  .then((res) => {
                    setImgSrc(res.preFetchURL);
                  });
              } else {
                setImgSrc(null);
              }
              setStatus(false);
            }
          }
        });
    }
  };

  const [user, setUser] = useState([]);
  const [MapVillageNames, setMapVillagenames] = useState([]);
  const sortedList = MapVillageNames.sort((a, b) =>
    a.properties.name.localeCompare(b.properties.name)
  );

  function copyData() {
    setCopyAddData(true);
  }

  useEffect(() => {
    if (copyAddData == true) {
      setPermanentAddress(presentAddress);
    } else {
      setPermanentAddress("");
    }
  }, [copyAddData, presentAddress]);

  useEffect(() => {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=State&rel=CONTAINEDINPLACE&targetType=Country`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        setUser(res);
        setStatus(false);
      });
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
          setMapVillagenames(resultArr);
        });
        setStatus(false);
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

  // cade for capturing photo
  const videoElement = useRef(null);
  const [imagefile, setImagefile] = useState("");
  const [imgSrc, setImgSrc] = useState(null);

  // const capture = React.useCallback(
  //     async () => {
  //         const imageSrc = videoElement.current.getScreenshot();
  //         function dataURLtoFile(dataurl, filename) {
  //             var arr = dataurl.split(","),
  //                 mime = arr[0].match(/:(.*?);/)[1],
  //                 bstr = atob(arr[1]),
  //                 n = bstr.length,
  //                 u8arr = new Uint8Array(n);
  //             while (n--) {
  //                 u8arr[n] = bstr.charCodeAt(n);
  //             }
  //             return new File([u8arr], filename, { type: mime });
  //         }

  //         //     //Usage example:
  //         var file = dataURLtoFile(imageSrc, "hello.jpeg");
  //         var FRdata = {
  //             "option": "searchFace",
  //             "region": "app-south-1",
  //             "collection_name": "dev",
  //             "bucket_name": "ssf-dev-assets",
  //             "file": imageSrc
  //         }
  //         var requestOptions = {
  //             headers: serviceHeaders.myHeaders1,
  //             "method": "POST",
  //             "mode": "cors",
  //             body: JSON.stringify(FRdata)
  //         };
  //         fetch(`${constant.ApiUrl}/FacialRecognition`, requestOptions)
  //             .then((res) => res.json())
  //             .then((res) => {
  //                 if (res.message == "File failed to upload") {
  //                     Tostify.notifyFail("Please capture the face only..!")
  //                 } else {
  //                     let faceID;
  //                     if (res.message == "No Health ID Found") {
  //                         setImgSrc(imageSrc);
  //                         Tostify.notifyWarning("No Health ID Found")

  //                     } else {
  //                         faceID = res.message
  //                     }

  //                     if (faceID) {
  //                         fetch(`${constant.ApiUrl}/member-svc/members/filter?type=Citizen&key=faceId&value=${faceID}`, serviceHeaders.getRequestOptions)
  //                             .then((res) => res.json())
  //                             .then((res) => {
  //                                 setcitizenPatientId(res['content'][0]['uuid'])
  //                                 setHealthID(res['content'][0]['healthID'])
  //                                 let healthId = res['content'][0]['healthID']
  //                                 setFName(res['content'][0]['firstName'])
  //                                 setMName(res['content'][0]['middleName'])
  //                                 setLName(res['content'][0]['lastName'])
  //                                 setDateOfBirth(res['content'][0]['dateOfBirth'])
  //                                 setAge(res['content'][0]['age'])
  //                                 setGender(res['content'][0]['gender'])
  //                                 setMnumber(res['content'][0]['contact'])
  //                                 if (res['content'][0]['residingInVillage'] == "Resides") {
  //                                     setResidentType("Resident")
  //                                 }

  //                                 fetch(`${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthId}`, serviceHeaders.getRequestOptions)
  //                                     .then((res) => res.json())
  //                                     .then((res) => {
  //                                         setImgSrc(res.preFetchURL)
  //                                     })
  //                             })
  //                     }
  //                 }
  //             })

  //         setImagefile(file);
  //         setIsShowVideo(true);
  //         //     // setHealthID("")
  //     },
  //     [videoElement, setImgSrc]
  // );
  // cade for capturing photo

  // onSumbit
  function fetchData(e) {
    e.preventDefault();
    const formData1 = {
      lgdCode: Lgdcode,
      citizen: regFormData,
    };
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(formData1),
    };

    if (
      !salutation ||
      !abhaAddress ||
      !firstName ||
      !gender ||
      !presentAddress.state ||
      !presentAddress.area ||
      !residentType ||
      !presentAddress.country ||
      !presentAddress.village ||
      !mobile ||
      !presentAddress.addressLine ||
      !dateOfBirth ||
      !presentAddress.taluk
    ) {
      alert("Please Enter All Mandatory Fields");
    } else if (!age || age == "0") {
      alert("Please Enter Age");
    } else {
      dispatch(
        loadRegistration(
          healthId,
          requestOptions,
          setLoading1,
          setLoading2,
          setNewLoading,
          setPatinentId
        )
      );
    }
  }

  // update patient data
  function updatePatientData() {
    const updateFormData = {
      citizen: regFormData,
    };
    var requestOptions1 = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateFormData),
    };

    if (citizenPatientId_id != undefined) {
      dispatch(
        loadUpdateRegistration(
          citizenPatientId_id,
          requestOptions1,
          setLoading1,
          setLoading2,
          setNewLoading
        )
      );
    } else if (searchDataID != undefined || searchDataID != "") {
      dispatch(
        loadUpdateRegistration(
          searchDataID,
          requestOptions1,
          setLoading1,
          setLoading2,
          setNewLoading
        )
      );
    }
  }
  // update patient data

  // end onSubmit

  const [types, setTypes] = useState("patient");
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

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

  // Not able to select Future Date for Date Of Birth
  const disableFutureDt = (current) => {
    return current.isBefore(today);
  };
  // Not able to select Future Date for Date Of Birth
  const cancelEditReg = () => {
    window.history.back();
  };

  //   Websockket connection
  const [authe, setAuthe] = useState([]);
  const [AbdmModal, setAbdmModal] = useState(false);
  const [modaltype, setModaltype] = useState("");
  const [transactionId, settransactionId] = useState("");
  const LoaderPage = () => {
    // setPageLoader(false);
  };
  // const timeOut = setTimeout(LoaderPage, 30000);
  const getCallBackResponce = (uuid) => {
    var requestOptions = {
      method: "GET",
      headers: serviceHeaders.myHeaders1,

      redirect: "follow",
    };

    fetch(
      `https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/callback/response/${uuid}`,
      requestOptions
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`);
        }
        return response.json();
      })
      .then((payload) => {
        console.log(payload);
        console.log("hii");
        const arr = [];
        arr.push(payload);
        if (arr[0].data.auth.modes) {
          console.log(arr[0].data.auth.modes);
          setAuthe((prevAuthe) => [...prevAuthe, ...arr[0].data.auth.modes]);
        }

        if (arr[0].data.auth.transactionId) {
          settransactionId(arr[0].data.auth.transactionId);
        }

        if (arr[0].data.auth.patient) {
          const fullName = arr[0].data.auth.patient.name;
          // regFormData.firstName = arr[0].data.auth.patient.name;
          const nameSplit = fullName.split(" ");
          if (nameSplit.length == 3) {
            regFormData.firstName = nameSplit[0];
            regFormData.middleName = nameSplit[1];
            regFormData.lastName = nameSplit[2];
          } else if (nameSplit.length == 2) {
            regFormData.firstName = nameSplit[0];
            regFormData.lastName = nameSplit[1];
          }

          regFormData.gender = arr[0].data.auth.patient.gender;
          if (arr[0].data.auth.patient.gender == "M") {
            regFormData.gender = "Male";
          } else if (arr[0].data.auth.patient.gender == "F") {
            regFormData.gender = "Female";
          } else {
            regFormData.gender = "Transgender";
          }
          console.log(regFormData.gender);

          regFormData.mobile = arr[0].data.auth.patient.identifiers[0].value;
          console.log(regFormData.mobile);

          const currentDate = new Date();
          const currentYear = currentDate.getFullYear();

          regFormData.age = currentYear - arr[0].data.auth.patient.yearOfBirth;

          let year = arr[0].data.auth.patient.yearOfBirth;
          let month = arr[0].data.auth.patient.monthOfBirth;
          let day = arr[0].data.auth.patient.dayOfBirth;
          month.toString();
          if (month <= 9) {
            month = "0" + month;
          }
          regFormData.dateOfBirth =
            year.toString() + "-" + month + "-" + day.toString();
          console.log(regFormData.dateOfBirth);

          regFormData.abhaAddress = arr[0].data.auth.patient.id;
          // console.log(arr[0].data.auth.patient.id);
          console.log(regFormData.abhaAddress);

          const newPresentAddress = {
            addressLine: arr[0].data.auth.patient.address.line,
            area: "",
            taluk: "",
            village: "",
            state: arr[0].data.auth.patient.address.state,
            district: arr[0].data.auth.patient.address.district,
            country: "",
            pinCode: arr[0].data.auth.patient.address.pincode,
          };
          setPresentAddress(newPresentAddress);
        }

        setPageLoader(false);
        // clearTimeout(timeOut);
      })
      .catch((error) => {
        setTimeout(() => {
          getCallBackResponce(uuid);
        }, 1500);
      });
  };
  const connectToWebSocket = (uuid, fetchType) => {
    console.log(uuid);
    if (transactionId) {
      setPageLoader(true);
    }
    setTimeout(() => {
      getCallBackResponce(uuid);
    }, 1000);
  };
  //   Websockket connection

  const sendRequest = () => {
    console.log("Hello", serviceHeaders.myHeaders1);
    if (healthId) {
      setPageLoader(true);
      // timeOut;

      const data = {
        query: {
          id: healthId,
          purpose: "LINK",
          requester: {
            type: "HIP",
            id: "SSF_demo_123",
          },
        },
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(data),
      };

      dispatch(loadAbdmFetchModes(requestOptions, connectToWebSocket));
    } else {
      alert("please enter Health Id");
    }
  };

  // setTimeout(() => {
  //   setPageLoader(false);
  //   alert("Try Again");
  // }, 30000);

  // setTimeout(() => {
  //   setIsLoading(false);
  // }, 1500);

  const openABDMModes = (mode) => {
    setPageLoader(true);

    setModaltype(mode);
    setAbdmModal(true);

    const data = {
      requestId: "2cecfb1e-4d3d-4027-ab23-790132d1b1de",
      timestamp: "2023-08-02T07:47:45.659Z",
      query: {
        id: healthId,
        purpose: "KYC_AND_LINK",
        authMode: mode,
        requester: {
          type: "HIP",
          id: "SSF_demo_123",
        },
      },
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(data),
    };

    dispatch(loadAbdmModeVerification(requestOptions, connectToWebSocket));
  };

  // const handleHealthId = (e) => {
  //   setHealthId(e.target.value);
  // };
  // code for retake photo
  const Retake = () => {
    setImgSrc("");
    setImagefile("");
    setHealthID("");
  };
  // code for retake photo

  // prevent input number change while scrolling
  $("input[type=number]").on("wheel", function (e) {
    return false;
  });
  // prevent input number change while scrolling

  const AbdmModalClose = () => {
    setAbdmModal(false);
    setModaltype("");
  };

  // const closeModal = () => {
  //   handleClose(true);
  // };

  return (
    <div>
      <ToastContainer />

      {/* ABDM Modal */}
      {AbdmModal && (
        <ABDMPopUps
          AbdmModal={AbdmModal}
          modaltype={modaltype}
          AbdmModalClose={AbdmModalClose}
          transactionId={transactionId}
          connectToWebSocket={connectToWebSocket}
        />
      )}
      {/* ABDM Modal */}

      {/* cancel button model box */}
      <CancelModal
        searchHealthId={searchHealthId}
        citizenPatientId_id={citizenPatientId_id}
        isShow={show}
        handleClose={handleClose}
        cancelEditReg={cancelEditReg}
      />
      {/* cancel button model box */}

      <div>
        <div className="regForm">
          <Loader
            isLoading1={isLoading1}
            isLoading2={isLoading2}
            isNewLoading={isNewLoading}
            citizenPatientId_id={citizenPatientId_id}
            citizenuuid={citizenuuid}
            searchDataID={searchDataID}
            searchHealthId={searchHealthId}
            patientId={patientId}
            pid={pid}
            setNewLoading={setNewLoading}
            types={types}
          />

          <div>
            <div className="Note">
              <p className="note">Note: </p>
              <p className="note2">
                {" "}
                Fields marked with <span style={{ color: "red" }}>*</span> are
                mandatory
              </p>
            </div>

            <div className="reg-form-sec">
              <Form
                ref={form}
                className="formAccordian register-form-accorion"
                style={{ width: "100% !important" }}
              >
                <div
                  className="body accodian-style"
                  style={{ margin: "2% 0%" }}
                >
                  <Accordion
                    defaultActiveKey={["0"]}
                    alwaysOpen
                    id="reg-accordion"
                  >
                    <Accordion.Item eventKey="0" id="custom-acco-item">
                      <Accordion.Header className="acc-head">
                        <em className="AcHr" />
                        <span className="icn">Patient Details </span>
                        <em className="AcHrR" />
                      </Accordion.Header>
                      <Accordion.Body>
                        <Row>
                          <Col md={2} align="center">
                            <div className="photo-div">
                              {imgSrc != null ? (
                                <>
                                  <img
                                    onError={() => {
                                      setImgSrc(null);
                                    }}
                                    src={imgSrc || ""}
                                    style={{
                                      height: "150px",
                                      width: "400px",
                                    }}
                                  />
                                </>
                              ) : (
                                <div>
                                  {!isShowVideo && (
                                    <label className="photo">
                                      Photo{" "}
                                      <span style={{ color: "red" }}>*</span>
                                    </label>
                                  )}
                                  <div className="camView">
                                    {isShowVideo && (
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
                                  {!isShowVideo && (
                                    <Button
                                      className="regCapture mt-2"
                                      onClick={startCam}
                                      disabled
                                      value={photo ?? ""}
                                    >
                                      Capture
                                    </Button>
                                  )}

                                  <input
                                    style={{ display: "none" }}
                                    ref={inputFile}
                                    onChange={handleFileUpload}
                                    type="file"
                                    disabled
                                  />
                                  {!isShowVideo && (
                                    <p
                                      style={{
                                        marginBottom: "20px",
                                        marginTop: "20px",
                                      }}
                                      disabled
                                    >
                                      <span
                                        className="upload"
                                        onClick={onButtonClick}
                                      >
                                        Upload from file/folder
                                      </span>
                                    </p>
                                  )}
                                </div>
                              )}
                            </div>
                            <div>
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
                          </Col>

                          <Col md={10}>
                            <Row>
                              {citizenPatientId_id ? (
                                <Col md={4}>
                                  <div className="col-container">
                                    <Form.Group
                                      className="mb-3_health"
                                      controlId="exampleForm.Health"
                                    >
                                      <Form.Label className="require">
                                        UHID
                                      </Form.Label>
                                      <Form.Control
                                        type="text"
                                        pattern="/^[0-9]{10}$/"
                                        value={uhid || ""}
                                        placeholder="ex: SUG10100"
                                        autoComplete="off"
                                        disabled
                                      />
                                    </Form.Group>
                                  </div>
                                </Col>
                              ) : (
                                ""
                              )}
                              <Col md={3}>
                                <div className="col-container">
                                  <Form.Group
                                    className="mb-3_health"
                                    controlId="exampleForm.Health"
                                  >
                                    <Form.Label className="require">
                                      Health ID
                                    </Form.Label>
                                    {!citizenuuid ? (
                                      <Form.Control
                                        type="text"
                                        pattern="/^[0-9]{10}$/"
                                        value={healthId || ""}
                                        name="healthId"
                                        onChange={(event) => {
                                          handleRegForm(event);
                                          searchbyid(event.target.value);
                                        }}
                                        placeholder="Enter Health Care ID"
                                        autoComplete="off"
                                        onKeyPress={(event) => {
                                          if (!/[0-9]/.test(event.key)) {
                                            event.preventDefault();
                                          }
                                        }}
                                        maxLength={14}
                                      />
                                    ) : (
                                      <Form.Control
                                        type="text"
                                        pattern="/^[0-9]{10}$/"
                                        value={healthId ?? ""}
                                        name="healthId"
                                        placeholder="Enter Health Care ID"
                                        disabled
                                      />
                                    )}
                                  </Form.Group>
                                </div>
                              </Col>
                              <Col md={2} className="verfy-btn">
                                <SaveButton
                                  class_name="regBtnN"
                                  butttonClick={sendRequest}
                                  // button_name="Verify"
                                  button_name={
                                    "Verify"
                                    // pageLoader ? <PageLoader /> :
                                  }
                                  // disabled={isLoading}
                                />
                              </Col>
                              {pageLoader ? (
                                <PageLoader />
                              ) : (
                                authe.length != 0 &&
                                authe.map((modes, i) => (
                                  <Col md={2} className="verfy-btn" key={i}>
                                    <SaveButton
                                      class_name="regBtnN"
                                      butttonClick={(e) => openABDMModes(modes)}
                                      // button_name={modes}
                                      button_name={
                                        modes === "MOBILE_OTP"
                                          ? "MOBILE OTP"
                                          : modes === "AADHAAR_OTP"
                                          ? "AADHAAR OTP"
                                          : "DEMOGRAPHIC"
                                      }
                                    />
                                  </Col>
                                ))
                              )}
                            </Row>
                            <br />
                            <Row>
                              <Col md={3}>
                                <div className="col-container">
                                  <Form.Group
                                    className="mb-3_health"
                                    controlId="exampleForm.Health Address"
                                  >
                                    <Form.Label className="require">
                                      Abha Address
                                    </Form.Label>
                                    <Form.Control
                                      type="text"
                                      name="abhaAddress"
                                      placeholder="Enter Abha Address"
                                      value={abhaAddress || ""}
                                      // onChange={(e) =>
                                      //   setAbhaAddress(e.target.value)
                                      // }
                                      onChange={handleRegForm}
                                      autoComplete="off"
                                    />
                                  </Form.Group>
                                </div>
                              </Col>
                            </Row>
                            <br />
                            <Row className="name">
                              <Col md={2}>
                                <div className="col-container">
                                  <Form.Group controlId="exampleForm.Salutation">
                                    <Form.Label className="require">
                                      Salutation{" "}
                                      <span style={{ color: "red" }}>*</span>
                                    </Form.Label>
                                    <Form.Select
                                      aria-label="Default select example"
                                      placeholder="Select A Value..."
                                      value={salutation || ""}
                                      name="salutation"
                                      onChange={handleRegForm}
                                    >
                                      <option value="" disabled hidden>
                                        Select...{" "}
                                      </option>
                                      {regFormItems.map((formItem, i) => (
                                        <React.Fragment key={i}>
                                          {formItem.groupName ==
                                            "Salutation" && (
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
                              <Col md={10}>
                                <div className="col-container">
                                  <Row>
                                    <Col md={3}>
                                      <Form.Group
                                        className="mb-3_fname"
                                        controlId="exampleForm.FName"
                                      >
                                        <Form.Label className="require">
                                          First Name{" "}
                                          <span style={{ color: "red" }}>
                                            *
                                          </span>
                                        </Form.Label>
                                        <Form.Control
                                          type="text"
                                          placeholder="First Name"
                                          value={firstName || ""}
                                          name="firstName"
                                          autoComplete="off"
                                          onChange={handleRegForm}
                                        />
                                      </Form.Group>
                                    </Col>
                                    <Col md={3}>
                                      <div className="col-container">
                                        <Form.Group
                                          className="mb-3_mname"
                                          controlId="exampleForm.MName"
                                        >
                                          <Form.Label>Middle Name</Form.Label>
                                          <Form.Control
                                            type="text"
                                            autoComplete="off"
                                            placeholder="Middle Name"
                                            value={middleName || ""}
                                            name="middleName"
                                            onChange={handleRegForm}
                                          />
                                        </Form.Group>
                                      </div>
                                    </Col>
                                    <Col md={3}>
                                      <div className="col-container">
                                        <Form.Group
                                          className="mb-3_lname"
                                          controlId="exampleForm.LName"
                                        >
                                          <Form.Label>Last Name </Form.Label>
                                          <Form.Control
                                            type="text"
                                            placeholder="Last Name"
                                            name="lastName"
                                            value={lastName || ""}
                                            autoComplete="off"
                                            onChange={handleRegForm}
                                          />
                                        </Form.Group>
                                      </div>
                                    </Col>
                                    <Col md={3}>
                                      <div className="col-container">
                                        <Form.Group
                                          className="mb-3_lname"
                                          controlId="exampleForm.LName"
                                        >
                                          <Form.Label className="require">
                                            Mobile Number{" "}
                                            <span style={{ color: "red" }}>
                                              *
                                            </span>
                                          </Form.Label>
                                          <Form.Control
                                            type="phone"
                                            placeholder="Mobile Number"
                                            maxLength={10}
                                            value={mobile || ""}
                                            autoComplete="off"
                                            name="mobile"
                                            onKeyPress={(event) => {
                                              if (!/[0-9]/.test(event.key)) {
                                                event.preventDefault();
                                              }
                                            }}
                                            onChange={handleRegForm}
                                          />
                                        </Form.Group>
                                      </div>
                                    </Col>
                                  </Row>
                                </div>
                              </Col>
                            </Row>
                          </Col>
                        </Row>
                      </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="1">
                      <Accordion.Header className="FormAccordian acc-head">
                        <em className="AcHr" />
                        <span className="icn">Demographic</span>
                        <em className="AcHrR" />
                      </Accordion.Header>
                      <Accordion.Body>
                        <Row>
                          <Col md={5} className="gender-row-div">
                            <Row>
                              <Col md={6}>
                                {!citizenuuid ? (
                                  <div className="col-container">
                                    <Form.Group controlId="exampleForm.Salutation">
                                      <Form.Label className="require">
                                        Resident Type{" "}
                                        <span style={{ color: "red" }}>*</span>
                                      </Form.Label>
                                      <Form.Select
                                        aria-label="Default select example"
                                        placeholder="Select a value..."
                                        value={residentType || ""}
                                        name="residentType"
                                        onChange={handleRegForm}
                                      >
                                        <option value="" disabled hidden>
                                          Select Resident Type
                                        </option>
                                        {regFormItems.map((formItem, i) => (
                                          <React.Fragment key={i}>
                                            {formItem.groupName ==
                                              "Resident Type" && (
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
                                ) : (
                                  <div className="col-container">
                                    <Form.Group controlId="exampleForm.Salutation">
                                      <Form.Label className="require">
                                        Resident Type{" "}
                                        <span style={{ color: "red" }}>*</span>
                                      </Form.Label>
                                      <Form.Select
                                        aria-label="Default select example"
                                        placeholder="Select a value..."
                                        value={residentType || ""}
                                        name="residentType"
                                        disabled
                                      >
                                        <option value={residentType || ""}>
                                          Resident
                                        </option>
                                      </Form.Select>
                                    </Form.Group>
                                  </div>
                                )}
                              </Col>
                              <Col md={6}>
                                <div className="col-container gender-input">
                                  <Form.Group
                                    controlId="exampleForm.Salutation"
                                    style={{ width: "72%" }}
                                  >
                                    <Form.Label className="require">
                                      Gender{" "}
                                      <span style={{ color: "red" }}>*</span>
                                    </Form.Label>
                                    <Form.Select
                                      aria-label="Default select example"
                                      value={gender || ""}
                                      name="gender"
                                      onChange={handleRegForm}
                                    >
                                      <option value="" disabled hidden>
                                        Select a Gender...
                                      </option>
                                      {regFormItems.map((formItem, i) => (
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
                                </div>
                              </Col>
                            </Row>
                          </Col>
                          <Col md={6} className="dob-row">
                            <Row>
                              <Col md={5}>
                                <div className="col-container responge-age">
                                  <Form.Label className="require">
                                    {" "}
                                    Age (in years){" "}
                                    <span style={{ color: "red" }}>*</span>
                                  </Form.Label>
                                  <Form.Control
                                    className="age"
                                    type="number"
                                    placeholder="Year"
                                    value={age || ""}
                                    name="age"
                                    onChange={(e) => {
                                      handleRegForm(e);
                                    }}
                                  />
                                </div>
                              </Col>
                              <Col md={1} align="center" className="divider">
                                <div className="col-container responge-divider">
                                  <div className="line" align="center"></div>
                                  <div className="line1" align="center"></div>

                                  <div className="text">Or</div>
                                  <div className="line" align="center"></div>
                                  <div className="line1" align="center"></div>
                                </div>
                              </Col>
                              <Col md={5}>
                                <div className="col-container responge-age">
                                  <Form.Label className="age">
                                    {" "}
                                    Date of Birth{" "}
                                    <span style={{ color: "red" }}>*</span>
                                  </Form.Label>
                                  <Form.Control
                                    className="age"
                                    type="date"
                                    max={moment(new Date()).format(
                                      "YYYY-MM-DD"
                                    )}
                                    value={dateOfBirth || ""}
                                    name="dateOfBirth"
                                    validate={[disableFutureDt]}
                                    onChange={(e) => {
                                      handleRegForm(e);
                                    }}
                                  />
                                </div>
                              </Col>
                            </Row>
                          </Col>
                        </Row>
                        <Row>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group controlId="exampleForm.Facility">
                                <Form.Label className="require">
                                  Marital Status
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={maritalStatus || ""}
                                  name="maritalStatus"
                                  onChange={handleRegForm}
                                >
                                  <option value="" disabled hidden>
                                    Select Status
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName ==
                                        "Marital Status" && (
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
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_fname"
                                controlId="exampleForm.FName"
                              >
                                <Form.Label className="require">
                                  Father / Spouse Name
                                </Form.Label>
                                <Form.Control
                                  type="text"
                                  placeholder="Enter Name Here"
                                  value={fatherName || ""}
                                  name="fatherName"
                                  onChange={handleRegForm}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group controlId="exampleForm.Facility">
                                <Form.Label className="require">
                                  Religion
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={religion ?? ""}
                                  name="religion"
                                  onChange={handleRegForm}
                                >
                                  <option value="" disabled hidden>
                                    Select Religion
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Religion" && (
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
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_health"
                                controlId="exampleForm.Health"
                              >
                                <Form.Label className="require">
                                  Caste
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={caste || ""}
                                  name="caste"
                                  onChange={handleRegForm}
                                >
                                  <option value="" disabled hidden>
                                    Select Caste
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Caste" && (
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
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_fname"
                                controlId="exampleForm.FName"
                              >
                                <Form.Label className="require">
                                  Occupation{" "}
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={occupation || ""}
                                  name="occupation"
                                  onChange={handleRegForm}
                                >
                                  <option value="" disabled hidden>
                                    Select Occupation
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Occupation" && (
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
                        <br />
                      </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="2">
                      <Accordion.Header className="acc-head">
                        <em className="AcHr" />
                        <span className="icn">Address</span>
                        <em className="AcHrR" />
                      </Accordion.Header>
                      <Accordion.Body>
                        <Row>
                          <Col md={5}>
                            <PermanentAddress
                              permanentAddress={presentAddress}
                              type="present"
                              handlePermanentAddress={handlePresentAddress}
                              user={user}
                              stateList={stateList}
                              districtListForPermanent={districtListForPresent}
                              talukListForPermanent={talukListForPresent}
                              villageListForPermanent={villageListForPresent}
                            />
                          </Col>
                          <Col md={2} className="align-me">
                            <button
                              type="button"
                              className="copy-address-btn"
                              onClick={copyData}
                            >
                              Copy<i className="bi bi-chevron-double-right"></i>
                            </button>
                          </Col>
                          <Col md={5}>
                            <PermanentAddress
                              permanentAddress={permanentAddress}
                              type="permanent"
                              handlePermanentAddress={handlePermanentAddress}
                              user={user}
                              stateList={stateList}
                              districtListForPermanent={
                                districtListForPermanent
                              }
                              talukListForPermanent={talukListForPermanent}
                              villageListForPermanent={villageListForPermanent}
                            />
                          </Col>
                        </Row>
                      </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="3">
                      <Accordion.Header className="acc-head">
                        <em className="AcHr" />
                        <span className="icn">Other Details</span>
                        <em className="AcHrR" />
                      </Accordion.Header>
                      <Accordion.Body>
                        <Row>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_mname"
                                controlId="exampleForm.MName"
                              >
                                <Form.Label>Birth Place</Form.Label>
                                <Form.Control
                                  type="tel"
                                  placeholder="Enter Birth Place"
                                  value={moreInfo.birthPlace || ""}
                                  name="birthPlace"
                                  onChange={handleMoreInfo}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_lname"
                                controlId="exampleForm.LName"
                              >
                                <Form.Label>Birth Identifier1</Form.Label>

                                <Form.Control
                                  type="text"
                                  placeholder="Enter Birth Identifier"
                                  autoComplete="off"
                                  value={identifier1 || ""}
                                  onChange={(e) =>
                                    setIdentifier1(e.target.value)
                                  }
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_lname"
                                controlId="exampleForm.LName"
                              >
                                <Form.Label>Birth Identifier2</Form.Label>
                                <Form.Control
                                  type="text"
                                  placeholder="Enter Birth Identifier"
                                  autoComplete="off"
                                  value={identifier2 || ""}
                                  onChange={(e) =>
                                    setIdentifier2(e.target.value)
                                  }
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={3}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_mname"
                                controlId="exampleForm.MName"
                              >
                                <Form.Label>
                                  Annual Income (in rupees)
                                </Form.Label>
                                <Form.Control
                                  type="number"
                                  placeholder="Enter Income Amount Here"
                                  value={moreInfo.annualIncome || ""}
                                  name="annualIncome"
                                  onChange={handleMoreInfo}
                                  onKeyPress={(event) => {
                                    if (!/[0-9]/.test(event.key)) {
                                      event.preventDefault();
                                    }
                                  }}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={2}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_lname"
                                controlId="exampleForm.LName"
                              >
                                <Form.Label>Education</Form.Label>

                                <Form.Select
                                  aria-label="Default select select example"
                                  value={moreInfo.education || ""}
                                  name="education"
                                  onChange={handleMoreInfo}
                                >
                                  <option value="" disabled hidden>
                                    Select Education
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName == "Education" && (
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
                        <br></br>
                        <Row>
                          <Col md={3}>
                            <div className="col-container">
                              <Form.Group controlId="exampleForm.Facility">
                                <Form.Label>
                                  Patient Primary Language{" "}
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={moreInfo.primaryLanguage || ""}
                                  name="primaryLanguage"
                                  onChange={handleMoreInfo}
                                >
                                  <option value="" disabled hidden>
                                    Select Language
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName ==
                                        "Patient Primary Language" && (
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
                            <div className="col-container">
                              <Form.Group controlId="exampleForm.Facility">
                                <Form.Label>
                                  Can Patient Speak English ?{" "}
                                </Form.Label>
                                <Form.Select
                                  aria-label="Default select select example"
                                  value={moreInfo.speakEnglish || ""}
                                  name="speakEnglish"
                                  onChange={handleMoreInfo}
                                >
                                  <option value="" disabled hidden>
                                    Select Speaking Status
                                  </option>
                                  {regFormItems.map((formItem, i) => (
                                    <React.Fragment key={i}>
                                      {formItem.groupName ==
                                        "Can a Patient Speak English" && (
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
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_mname"
                                controlId="exampleForm.MName"
                              >
                                <Form.Label>Landline Number</Form.Label>
                                <Form.Control
                                  type="tel"
                                  placeholder="Landline Number"
                                  value={moreInfo.phone || ""}
                                  name="phone"
                                  onChange={handleMoreInfo}
                                  onKeyPress={(event) => {
                                    if (!/[0-9]/.test(event.key)) {
                                      event.preventDefault();
                                    }
                                  }}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                          <Col md={3}>
                            <div className="col-container">
                              <Form.Group
                                className="mb-3_lname"
                                controlId="exampleForm.LName"
                              >
                                <Form.Label>Email Id</Form.Label>
                                <Form.Control
                                  type="email"
                                  placeholder="Enter Email-Id"
                                  autoComplete="off"
                                  value={moreInfo.emailId || ""}
                                  name="emailId"
                                  onChange={handleMoreInfo}
                                />
                              </Form.Group>
                            </div>
                          </Col>
                        </Row>
                      </Accordion.Body>
                    </Accordion.Item>
                  </Accordion>
                </div>
                <br />
                <hr />
                <br />
                <div className="regFoot align-me2">
                  <div className="save-btn-section">
                    {!citizenuuid && !citizenPatientId_id ? (
                      <React.Fragment>
                        {searchHealthId ? (
                          <SaveButton
                            class_name="regBtnN"
                            butttonClick={updatePatientData}
                            button_name="Update"
                          />
                        ) : (
                          <SaveButton
                            class_name="regBtnN"
                            butttonClick={fetchData}
                            button_name="Save Photon"
                          />
                        )}
                      </React.Fragment>
                    ) : (
                      <React.Fragment>
                        {citizenPatientId_id ? (
                          <SaveButton
                            class_name="regBtnN"
                            butttonClick={updatePatientData}
                            button_name="Update"
                          />
                        ) : (
                          <SaveButton
                            class_name="regBtnN"
                            butttonClick={fetchData}
                            button_name="Save Photon"
                          />
                        )}
                      </React.Fragment>
                    )}
                  </div>
                  <div className="save-btn-section">
                    <SaveButton
                      butttonClick={handleShow}
                      class_name="regBtnPC"
                      button_name="Cancel"
                    />
                  </div>
                  {/* <SaveButton
                    // butttonClick={}
                    class_name="regBtnN"
                    button_name="eSanjeevani"
                  /> */}
                </div>
              </Form>
              <br />
              <br />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RegForms;
