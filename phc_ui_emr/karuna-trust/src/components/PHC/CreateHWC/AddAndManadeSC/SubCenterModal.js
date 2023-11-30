import React, { useState, useEffect, useRef } from "react";
import { Col, Row, Form, Button, Modal, InputGroup } from "react-bootstrap";
import { MultiSelect } from "react-multi-select-component";
import { useDispatch, useSelector } from "react-redux";
import {
  loadCreateSubCenter,
  loadAllState,
  loadAllDistrict,
  loadAllTaluk,
  loadAllVillage,
  loadAllGrams,
  loadAllVillGrams,
  loadUpdateSubCenter,
  loadSubCenterDetails,
  loadSubcentreVillages,
} from "../../../../redux/phcAction";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import GoogleMapModal from "../../ManagePHC/GoogleMapModal";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import Geocode from "react-geocode";
import FileUpload from "../../FileUpload/FileUpload";
Geocode.setApiKey("YOUR_GOOGLE_API_KEY");

let center_type = "Sub-Center";
let updateType = "remove-image";
export default function SubCenterModal(props) {
  let dispatch = useDispatch();
  let phcuuid = sessionStorage.getItem("uuidofphc");
  const { sunCenterDetails } = useSelector((state) => state.phcData);
  const { subcentreVillages } = useSelector((state) => state.phcData);

  const { stateList } = useSelector((state) => state.phcData);
  const { districtList } = useSelector((state) => state.phcData);
  const { talukList } = useSelector((state) => state.phcData);

  const { villageList } = useSelector((state) => state.phcData);
  const { gramPanchayatList } = useSelector((state) => state.phcData);
  const { villPanchayatList } = useSelector((state) => state.phcData);

  let sortedState;
  if (stateList) {
    sortedState = stateList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedDistrict;
  if (districtList) {
    sortedDistrict = districtList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedTaluk;
  if (talukList) {
    sortedTaluk = talukList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedVillage;
  if (villageList) {
    sortedVillage = villageList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedGram;
  if (gramPanchayatList) {
    sortedGram = gramPanchayatList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }
  let sortedGramVillage;
  if (villPanchayatList) {
    sortedGramVillage = villPanchayatList.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }

  const [stateuuid, setStateuuid] = useState("");
  const [distuuid, setDistuuid] = useState("");
  const [talukuuid, setTalukuuid] = useState("");

  const [gramuuid, setGramuuid] = useState("");
  const [gramName, setGramName] = useState([]);
  const [latLngFieldValue, setLatLngFieldValue] = useState("");

  const [imageShow, setImageShow] = useState(false);
  const [imgText, setImgText] = useState(false);
  const [imageChange, setImageChange] = useState(false);
  const [upload, setUpload] = useState(false);

  const [uploadImage, setUploadImage] = useState("");

  const [gramPanchayat, setGramPanchayat] = useState([]);

  const startCam = () => {
    setImageShow(true);
    setImgText(false);
  };
  const onButtonClick = () => {
    setImageShow(true);
    setImgText(true);
    setUpload(false);
  };

  const handleImageClose = () => {
    setImageShow(false);
  };

  const changeImage = () => {
    setImageShow(true);
    setImageChange(true);
  };

  useEffect(() => {
    if (gramPanchayat.length > 1) {
      setGramPanchayat([gramPanchayat[gramPanchayat.length - 1]]);
    }
  }, [gramPanchayat]);

  useEffect(() => {
    if (gramPanchayat) {
      gramPanchayat.map((gram) => {
        setGramuuid(gram.uuid);
        setGramName(gram.label);
      });
    }
  }, [gramPanchayat]);

  const [createSubCenterData, setCreateSubCenterData] = useState({
    name: "",
    description: "",
    facilityId: "",
    latitude: "",
    longitude: "",
    photo: "",
    addressLine: "",
    stateName: "",
    districtName: "",
    talukName: "",
    villageName: "",
    pin: "",
    contact: "",
    email: "",
    status: "INACTIVE",
    phc: phcuuid,
  });

  useEffect(() => {
    if (sunCenterDetails) {
      setLatLngFieldValue(
        sunCenterDetails?.properties?.latitude +
          "/" +
          sunCenterDetails?.properties?.longitude
      );
      setCreateSubCenterData({
        name: sunCenterDetails?.properties?.name,
        description: sunCenterDetails?.properties?.description,
        facilityId: sunCenterDetails?.properties?.facilityId,
        latitude: sunCenterDetails?.properties?.latitude,
        longitude: sunCenterDetails?.properties?.longitude,
        photo: sunCenterDetails?.properties?.photo,
        addressLine: sunCenterDetails?.properties?.addressLine,
        stateName: sunCenterDetails?.properties?.stateName,
        districtName: sunCenterDetails?.properties?.districtName,
        talukName: sunCenterDetails?.properties?.talukName,
        villageName: sunCenterDetails?.properties?.villageName,
        pin: sunCenterDetails?.properties?.pin,
        contact: sunCenterDetails?.properties?.contact,
        email: sunCenterDetails?.properties?.email,
        status: sunCenterDetails?.properties?.status,
      });
    }
  }, [sunCenterDetails]);

  useEffect(() => {
    if (createSubCenterData.stateName) {
      if (stateList) {
        stateList.map((item) => {
          if (item.target.properties.name == createSubCenterData.stateName) {
            setStateuuid(item.target.properties.uuid);
          }
        });
      }
    }
    if (createSubCenterData.districtName) {
      if (districtList) {
        districtList.map((item) => {
          if (item.target.properties.name == createSubCenterData.districtName) {
            setDistuuid(item.target.properties.uuid);
          }
        });
      }
    }
    if (createSubCenterData.talukName) {
      if (talukList) {
        talukList.map((item) => {
          if (item.target.properties.name == createSubCenterData.talukName) {
            setTalukuuid(item.target.properties.uuid);
          }
        });
      }
    }
    if (uploadImage) {
      createSubCenterData.photo = uploadImage;
    }
  }, [createSubCenterData, stateList, districtList, talukList, uploadImage]);

  const [googleMapsModel, setGoogleMapsModel] = useState(false);
  const [address, setAddress] = useState("");

  const [latLan, setLatLan] = useState({
    lat: "",
    lng: "",
  });

  useEffect(() => {
    dispatch(loadAllState());
    if (stateuuid) {
      dispatch(loadAllDistrict(stateuuid));
    }
    if (distuuid) {
      dispatch(loadAllTaluk(distuuid));
    }
    if (talukuuid) {
      dispatch(loadAllVillage(talukuuid));
      dispatch(loadAllGrams(talukuuid));
    }
    if (gramuuid) {
      dispatch(loadAllVillGrams(gramuuid));
    }
    dispatch(loadSubcentreVillages(props.idForUpdate));
  }, [stateuuid, distuuid, talukuuid, gramuuid, props.idForUpdate]);

  const [tooltip, setTooltip] = useState(true);
  // Subcenter Value

  const {
    name,
    latitude,
    longitude,
    photo,
    description,
    facilityId,
    addressLine,
    stateName,
    districtName,
    talukName,
    villageName,
    pin,
    contact,
    email,
    status,
  } = createSubCenterData;

  const handleSubCenterData = (e) => {
    const { name, value } = e.target;
    if (name == "latlng") {
      setLatLngFieldValue(e.target.value);
      if (e.target.value.includes("/")) {
        let latLngArray = e.target.value.split("/");
        setCreateSubCenterData({
          ...createSubCenterData,
          latitude: parseFloat(latLngArray[0]),
          longitude: parseFloat(latLngArray[1]),
        });
      } else {
        setCreateSubCenterData({
          ...createSubCenterData,
          latitude: parseFloat(e.target.value),
        });
      }
    } else {
      if (name == "stateName") {
        stateList.map((item) => {
          if (item.target.properties.name == value) {
            setStateuuid(item.target.properties.uuid);
          }
        });
      }
      if (name == "districtName") {
        districtList.map((item) => {
          if (item.target.properties.name == value) {
            setDistuuid(item.target.properties.uuid);
          }
        });
      }
      if (name == "talukName") {
        talukList.map((item) => {
          if (item.target.properties.name == value) {
            setTalukuuid(item.target.properties.uuid);
          }
        });
      }
      setCreateSubCenterData({ ...createSubCenterData, [name]: value });
    }
  };
  // Subcenter Value

  const openMapModal = () => {
    setGoogleMapsModel(true);
    props.handleSubCenterClose(false);
  };

  const closeGoogleMaps = () => {
    setGoogleMapsModel(false);
    props.setSubCenterShow(true);
  };

  const searchAddress = (searchVal) => {
    Geocode.fromAddress(searchVal).then(
      (response) => {
        const { lat, lng } = response.results[0].geometry.location;
        setAddress(response.results[0].formatted_address);
        setLatLan({
          lat: lat,
          lng: lng,
        });
      },
      (error) => {}
    );
  };

  const [checked, setChecked] = useState([]);
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [topicsOptions, setTopicsOptions] = useState([]);
  const [panchayatOptions, setPanchayatOptions] = useState([]);
  const [selectedVillages, setSelectedVillages] = useState([]);
  const [filteredOptions, setFilteredOptions] = useState([]);
  const [searchKey, setSearchKey] = useState("");

  const setListVillage = (e, item) => {
    if (e.target.checked) {
      setSelectedVillages([...selectedVillages, item]);
    } else {
      setSelectedVillages(
        selectedVillages.filter((id) => id.uuid !== e.target.value)
      );
    }
  };

  const searchPanchayat = (e) => {
    let keyWord = e.target.value;
    setSearchKey(keyWord);
    if (keyWord) {
      const filteredData = panchayatOptions.filter((items) => {
        return items.label.toLowerCase().startsWith(keyWord.toLowerCase());
      });
      setFilteredOptions(filteredData);
    } else {
      setFilteredOptions(topicsOptions);
    }
  };

  const [filterPanchayat, setFilterPanchayat] = useState([]);

  useEffect(() => {
    let panchayats = [];
    if (searchKey) {
      panchayats = filteredOptions;
    } else {
      panchayats = panchayatOptions;
    }
    setFilterPanchayat(panchayats);
  }, [filteredOptions, panchayatOptions, searchKey]);

  const closeModal = () => {
    props.handleSubCenterClose(false);
    setUploadImage("");
  };

  // POST Call
  function saveSubCenters() {
    const subCenterData = {
      type: "SubCenter",
      properties: createSubCenterData,
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(subCenterData),
    };
    if (!name || !stateName || !districtName || !talukName || !villageName) {
      Tostify.notifyWarning("Please Enter All Mandatory Fields...!");
    } else {
      dispatch(
        loadCreateSubCenter(
          requestOptions,
          selectedVillages,
          props.handleSubCenterClose
        )
      );
    }
  }
  // POST Call

  // PATCH Call
  function updateSubCenters(e, updateType) {
    let subCenterData;
    if (updateType) {
      subCenterData = {
        type: "SubCenter",
        properties: {
          photo: "",
        },
      };
    } else {
      subCenterData = {
        type: "SubCenter",
        properties: createSubCenterData,
      };
    }

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(subCenterData),
    };
    dispatch(
      loadUpdateSubCenter(e, requestOptions, selectedVillages, closeModal)
    );
  }
  // PATCH Call

  const closeSubCenterModal = () => {
    dispatch(loadSubCenterDetails(null));
    setCreateSubCenterData({
      name: "",
      description: "",
      facilityId: "",
      latitude: "",
      longitude: "",
      photo: "",
      addressLine: "",
      stateName: "",
      districtName: "",
      talukName: "",
      villageName: "",
      pin: "",
      contact: "",
      email: "",
      status: "",
      phc: "",
    });
    props.setIdForUpdate("");
    props.handleSubCenterClose(false);
  };

  useEffect(() => {
    if (latLan.lat != "") {
      Geocode.fromLatLng(latLan.lat, latLan.lng).then(
        (response) => {
          const address = response.results[0].formatted_address;
          setAddress(response.results[0].formatted_address);
          // setLocation(address);
        },
        (error) => {
          console.error(error);
        }
      );
      setCreateSubCenterData({
        ...createSubCenterData,
        latitude: latLan.lat,
        longitude: latLan.lng,
      });
      setLatLngFieldValue(latLan.lat + "/" + latLan.lng);
    }
    if (gramPanchayatList) {
      let dropOption = [];
      for (var i = 0; i < gramPanchayatList.length; i++) {
        dropOption.push({
          label: gramPanchayatList[i].target.properties.name,
          value: gramPanchayatList[i].target.properties.name,
          uuid: gramPanchayatList[i].target.properties.uuid,
        });
      }
      setTopicsOptions(dropOption);
    }
    if (villPanchayatList) {
      let dropOption = [];
      for (var i = 0; i < villPanchayatList.length; i++) {
        dropOption.push({
          label: villPanchayatList[i].target.properties.name,
          value: villPanchayatList[i].target.properties.name,
          uuid: villPanchayatList[i].target.properties.uuid,
        });
      }
      setPanchayatOptions(dropOption);
    }
    if (subcentreVillages) {
      let optionDrop = [];
      for (var i = 0; i < subcentreVillages.length; i++) {
        optionDrop.push({
          label: subcentreVillages[i].target.properties.name,
          value: subcentreVillages[i].target.properties.name,
          uuid: subcentreVillages[i].target.properties.uuid,
        });
      }
      setSelectedVillages(optionDrop);
    }
  }, [latLan, gramPanchayatList, villPanchayatList, subcentreVillages]);

  return (
    <React.Fragment>
      {googleMapsModel && (
        <GoogleMapModal
          googleMapsModel={googleMapsModel}
          // addressFromGoogle={addressFromGoogle}
          openGoogleMaps={closeGoogleMaps}
          latLan={latLan}
          // searchLocation={searchLocation}
          searchAddress={searchAddress}
          // searchVal={searchVal}
          setLatLan={setLatLan}
          address={address}
        />
      )}
      {/* upload image */}
      {imageShow && (
        <FileUpload
          imageShow={imageShow}
          imgText={imgText}
          setImgText={setImgText}
          upload={upload}
          setUpload={setUpload}
          handleImageClose={handleImageClose}
          setUploadImage={setUploadImage}
          imageChange={imageChange}
          setImageChange={setImageChange}
          center_type={center_type}
        />
      )}
      {/* upload image */}
      <Modal
        show={props.subCenterShow}
        onHide={props.handleSubCenterClose}
        className="sub-center-div"
      >
        <ToastContainer />
        <div className="phc-header-div">
          <h5 className="vital-header">
            {props.idForUpdate ? "Edit" : "Enter"} Sub-Centers/HWC Details
            <i
              className="fa fa-close close-btn-style"
              onClick={closeSubCenterModal}
            ></i>
          </h5>
        </div>
        <Form>
          <Row>
            <Col lg={6} md={6} className="cred-staff">
              <Row className="super-center-form">
                <Col md={3}>
                  <div>
                    <p className="upload-subcenter">
                      Upload Sub-Center/HWC photo
                    </p>
                    <div className="manage-sub-center">
                      {photo ? (
                        <div>
                          <div className="image-div">
                            <img
                              src={photo || ""}
                              style={{ height: "150px", width: "400px" }}
                            />
                          </div>
                          <Row className="image-action-btn">
                            <Col md={6}>
                              <div
                                onClick={(e) =>
                                  updateSubCenters(
                                    props.idForUpdate,
                                    updateType
                                  )
                                }
                                className="updateImage"
                              >
                                Remove
                              </div>
                            </Col>
                            <Col md={6}>
                              <div
                                onClick={changeImage}
                                className="updateImage"
                              >
                                Change
                              </div>
                            </Col>
                          </Row>
                        </div>
                      ) : (
                        <div>
                          {uploadImage ? (
                            <div>
                              <img
                                src={uploadImage}
                                className="user-login-img"
                                style={{ height: "150px", width: "100%" }}
                              />
                            </div>
                          ) : (
                            <div>
                              <img
                                src="../img/super/facility.png"
                                className="user-login-img"
                              />
                              <Button
                                className="regCapture"
                                onClick={startCam}
                                style={{ cursor: "pointer" }}
                              >
                                Capture
                              </Button>
                              <div>
                                <p style={{ marginTop: "6px" }}>or</p>
                                <p
                                  className="uploadphc"
                                  onClick={onButtonClick}
                                  style={{ cursor: "pointer" }}
                                >
                                  Upload from Computer
                                </p>
                              </div>
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                </Col>
                <Col md={9} className="sub-center-add">
                  <Row className="phc-info-div">
                    <Col lg={6} md={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Sub-Center/HWC Name{" "}
                            <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter PHC Name"
                            name="name"
                            maxLength={50}
                            value={name || ""}
                            onChange={handleSubCenterData}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={6} md={6}></Col>
                  </Row>
                  <div className="phc-info-div">
                    <div>
                      <Form.Group>
                        <Form.Label>
                          Description about the Sub-Center /HWC
                        </Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Description"
                          name="description"
                          maxLength={200}
                          value={description || ""}
                          onChange={handleSubCenterData}
                        />
                      </Form.Group>
                    </div>
                  </div>
                  <Row className="phc-info-div">
                    <Col lg={6} md={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>Facility ID </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter Facility ID"
                            name="facilityId"
                            value={facilityId || ""}
                            onChange={handleSubCenterData}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9} md={9}>
                      <div>
                        <Form.Group>
                          <Form.Label>Lat-Long</Form.Label>
                          <InputGroup>
                            <Form.Control
                              type="text"
                              placeholder="Locate / Capture Lat-Long"
                              name="latlng"
                              value={latLngFieldValue || ""}
                              onChange={handleSubCenterData}
                            />
                            <InputGroup.Text
                              id="basic-lat-addon"
                              className="lat-long"
                              onClick={openMapModal}
                              style={{ cursor: "pointer" }}
                            >
                              <img
                                src="../img/super/location.png"
                                className="lat-lon-img"
                              />
                            </InputGroup.Text>
                          </InputGroup>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={3} md={3}>
                      <div className="pro-subcenter-circle">
                        {!tooltip ? (
                          <Row
                            className="location-modal"
                            onClick={() => setTooltip(!tooltip)}
                          >
                            <Col md={2}>
                              <Button
                                variant="link"
                                className="pro-close-btn-circle"
                              >
                                <i className="bi bi-info-circle-fill pro-close-circle"></i>
                              </Button>
                            </Col>
                            <Col md={10}>
                              <p className="location-text-modal">
                                Turn on Location to capture the Lat-Long or
                                Enter Manually
                              </p>
                            </Col>
                          </Row>
                        ) : (
                          <Button variant="link">
                            <i
                              className="bi bi-info-circle-fill pro-circle"
                              onClick={() => setTooltip(!tooltip)}
                            ></i>
                          </Button>
                        )}
                      </div>
                    </Col>
                  </Row>
                  <div className="phc-info-div">
                    <div>
                      <Form.Group>
                        <Form.Label>Address</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Enter Address"
                          name="addressLine"
                          value={addressLine || ""}
                          onChange={handleSubCenterData}
                        />
                      </Form.Group>
                    </div>
                  </div>
                  <Row className="phc-info-div">
                    <Col lg={4} md={4}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            State <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select State"
                            name="stateName"
                            value={stateName || ""}
                            onChange={handleSubCenterData}
                          >
                            <option value="" hidden>
                              Select State
                            </option>
                            {stateList &&
                              stateList.map((states, i) => (
                                <option
                                  value={states.target.properties.name}
                                  select={states.target.properties.uuid}
                                  key={i}
                                >
                                  {states.target.properties.name}
                                </option>
                              ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={4} md={4}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            District <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select District"
                            name="districtName"
                            value={districtName || ""}
                            onChange={handleSubCenterData}
                            disabled={!stateuuid}
                          >
                            <option value="" hidden>
                              Select District
                            </option>
                            {districtList &&
                              districtList.map((dist, i) => (
                                <option
                                  value={dist.target.properties.name}
                                  key={i}
                                >
                                  {dist.target.properties.name
                                    .charAt(0)
                                    .toUpperCase() +
                                    dist.target.properties.name
                                      .slice(1)
                                      .toLowerCase()}
                                </option>
                              ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={4} md={4}>
                      <div>
                        <Form.Group>
                          <Form.Label>
                            Taluk <span style={{ color: "red" }}>*</span>
                          </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select Taluk"
                            name="talukName"
                            value={talukName || ""}
                            onChange={handleSubCenterData}
                            disabled={!distuuid}
                          >
                            <option value="" hidden>
                              Select Taluk
                            </option>
                            {talukList &&
                              talukList.map((taluk, i) => (
                                <option
                                  value={taluk.target.properties.name}
                                  key={i}
                                >
                                  {taluk.target.properties.name
                                    .charAt(0)
                                    .toUpperCase() +
                                    taluk.target.properties.name
                                      .slice(1)
                                      .toLowerCase()}
                                </option>
                              ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={6} md={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>Village / Ward </Form.Label>
                          <Form.Select
                            aria-label="Default select example"
                            placeholder="Select State"
                            name="villageName"
                            value={villageName || ""}
                            onChange={handleSubCenterData}
                            disabled={!talukuuid}
                          >
                            <option value="" hidden>
                              Select Village / Ward
                            </option>
                            {villageList &&
                              villageList.map((village, i) => (
                                <option
                                  value={village.target.properties.name}
                                  key={i}
                                >
                                  {village.target.properties.name
                                    .charAt(0)
                                    .toUpperCase() +
                                    village.target.properties.name
                                      .slice(1)
                                      .toLowerCase()}
                                </option>
                              ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={6} md={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>Pin Code</Form.Label>
                          <Form.Control
                            type="phone"
                            placeholder="Enter Pin Code"
                            autoComplete="off"
                            value={pin || ""}
                            name="pin"
                            maxLength={6}
                            onChange={handleSubCenterData}
                            onKeyPress={(e) => {
                              if (!/[0-9]/.test(e.key)) {
                                e.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9} md={9}>
                      <div>
                        <Form.Group>
                          <Form.Label>SC/HWC Contact No.</Form.Label>
                          <Form.Control
                            type="phone"
                            placeholder="Enter Contact No."
                            autoComplete="off"
                            value={contact || ""}
                            name="contact"
                            maxLength={10}
                            onChange={handleSubCenterData}
                            onKeyPress={(e) => {
                              if (!/[0-9]/.test(e.key)) {
                                e.preventDefault();
                              }
                            }}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                  <Row className="phc-info-div">
                    <Col lg={9} md={9}>
                      <div>
                        <Form.Group>
                          <Form.Label>SC/HWC Email ID</Form.Label>
                          <Form.Control
                            type="email"
                            placeholder="example@abcd.com"
                            name="email"
                            value={email || ""}
                            onChange={handleSubCenterData}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={3} md={3}></Col>
                  </Row>
                </Col>
              </Row>
            </Col>
            <Col lg={6} md={6}>
              <Row>
                <Col lg={6} md={6} className="village-list">
                  <h5 className="vital-header">
                    Villages Associated to SC/HWC
                  </h5>
                  <p className="super-desc">
                    Select the villages to associate with this Sub-Center/HWC at
                    Gram Panchayat level.
                  </p>
                  <div>
                    <Form.Group>
                      <Form.Label className="gram-pan-label">
                        Gram Panchayat <span style={{ color: "red" }}>*</span>
                      </Form.Label>
                      <MultiSelect
                        name="gramPanchayat"
                        selected={gramPanchayat}
                        options={topicsOptions}
                        value={gramPanchayat || ""}
                        onChange={setGramPanchayat}
                        labelledBy="Search Gram Panchayat"
                        disabled={!talukuuid}
                        hasSelectAll={false}
                      />
                    </Form.Group>
                  </div>
                  <br />
                  {/* <div>
                                        <Form.Group>
                                            <MultiSelect
                                                options={panchayatOptions}
                                                value={listVillage || ""}
                                                onChange={setListVillage}
                                                labelledBy="Select Village"
                                                disabled={gramPanchayat.length == 0}
                                            />
                                        </Form.Group>
                                    </div> */}
                  <div>
                    <Form.Control
                      type="text"
                      placeholder="Search Gram Panchayat"
                      onChange={searchPanchayat}
                    />
                  </div>
                  <div className="gram-list-div">
                    {filterPanchayat.map((item, i) => (
                      <div key={i} className="gram-list-item">
                        <Form.Check
                          key={i}
                          value={item?.uuid}
                          type="checkbox"
                          className={isChecked(item)}
                          label={item?.label}
                          onChange={(e) => setListVillage(e, item)}
                          style={{ fontSize: "14px" }}
                        />
                      </div>
                    ))}
                  </div>
                  <br />
                  <br />
                </Col>
                <Col lg={6} md={6} className="associated-village-list">
                  <h5 className="vital-header">Associated Villages List</h5>
                  <p className="super-desc">
                    All the villages selected will be shown in the list below
                  </p>

                  <p>
                    <b>{gramName}</b>
                  </p>
                  <p className="super-list">
                    {selectedVillages &&
                      selectedVillages.map((panch, i) => (
                        <li key={i}>{panch.label}</li>
                      ))}
                  </p>
                </Col>
              </Row>
            </Col>
            <div className="regFoot align-me2 resetButton">
              <div className="save-btn-section">
                <SaveButton
                  butttonClick={closeSubCenterModal}
                  class_name="regBtnPC"
                  button_name="Cancel"
                />
              </div>
              <div className="save-btn-section">
                {props.idForUpdate ? (
                  <SaveButton
                    class_name="regBtnN"
                    butttonClick={(e) => updateSubCenters(props.idForUpdate)}
                    button_name="Update"
                  />
                ) : (
                  <SaveButton
                    class_name="regBtnN"
                    butttonClick={saveSubCenters}
                    button_name="Save"
                  />
                )}
              </div>
            </div>
          </Row>
        </Form>
      </Modal>
    </React.Fragment>
  );
}
