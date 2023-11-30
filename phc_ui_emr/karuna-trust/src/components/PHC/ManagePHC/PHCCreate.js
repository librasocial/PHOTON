import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Modal, InputGroup } from "react-bootstrap";
import { MultiSelect } from "react-multi-select-component";
import SaveButton from "../../EMR_Buttons/SaveButton";
import { ToastContainer } from "react-toastify";
import {
  loadAllDistrict,
  loadAllGrams,
  loadAllState,
  loadAllTaluk,
  loadAllVillage,
  loadCreatePHC,
  loadUpdatePHC,
  loadAllGramPanchayatPhc,
} from "../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import GoogleMapModal from "./GoogleMapModal";
import Geocode from "react-geocode";
import PageLoader from "../../PageLoader";
Geocode.setApiKey("YOUR_GOOGLE_API_KEY");

export default function PHCCreate(props) {
  let dispatch = useDispatch();
  const phc = sessionStorage.getItem("phc");
  const phcUuid = sessionStorage.getItem("uuidofphc");

  const { stateList } = useSelector((state) => state.phcData);
  const { districtList } = useSelector((state) => state.phcData);
  const { talukList } = useSelector((state) => state.phcData);
  const { villageList } = useSelector((state) => state.phcData);
  const { gramPanchayatList } = useSelector((state) => state.phcData);
  const { gramPanchayatListPhc } = useSelector((state) => state.phcData);

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
  let sortedGramPhc;
  if (gramPanchayatListPhc) {
    sortedGramPhc = gramPanchayatListPhc.sort((a, b) =>
      a?.target?.properties?.name.localeCompare(b?.target?.properties?.name)
    );
  }

  const [stateuuid, setStateuuid] = useState("");
  const [distuuid, setDistuuid] = useState("");
  const [talukuuid, setTalukuuid] = useState("");

  const [createPHCData, setCreatePHCData] = useState({
    name: phc,
    latitude: "",
    longitude: "",
    photo: "",
    type: "RURAL",
    description: "",
    facilityId: "",
    addressLine: "",
    stateName: "",
    districtName: "",
    talukName: "",
    villageName: "",
    pin: "",
    contact: "",
    email: "",
    status: "INACTIVE",
  });

  const {
    name,
    latitude,
    longitude,
    photo,
    type,
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
  } = createPHCData;

  useEffect(() => {
    if (props.editUuid) {
      let getData = props.phcDetails?.properties;

      setLatLngFieldValue(getData?.latitude + "/" + getData?.longitude);
      setCreatePHCData({
        name: getData?.name,
        latitude: getData?.latitude,
        longitude: getData?.longitude,
        photo: "",
        type: getData?.type,
        description: getData?.description,
        facilityId: getData?.facilityId,
        addressLine: getData?.addressLine,
        stateName: getData?.stateName,
        districtName: getData?.districtName,
        talukName: getData?.talukName,
        villageName: getData?.villageName,
        pin: getData?.pin,
        contact: getData?.contact,
        email: getData?.email,
        status: getData?.status,
      });
    }
  }, [props.editUuid, props.phcDetails]);

  useEffect(() => {
    if (createPHCData.stateName) {
      if (stateList) {
        stateList.map((item) => {
          if (item.target.properties.name == createPHCData.stateName) {
            setStateuuid(item.target.properties.uuid);
          }
        });
      }
    }
    if (createPHCData.districtName) {
      if (districtList) {
        districtList.map((item) => {
          if (item.target.properties.name == createPHCData.districtName) {
            setDistuuid(item.target.properties.uuid);
          }
        });
      }
    }
    if (createPHCData.talukName) {
      if (talukList) {
        talukList.map((item) => {
          if (item.target.properties.name == createPHCData.talukName) {
            setTalukuuid(item.target.properties.uuid);
          }
        });
      }
    }
  }, [createPHCData, stateList, districtList, talukList]);

  const [checked, setChecked] = useState([]);
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const [latLngFieldValue, setLatLngFieldValue] = useState("");
  const handlePHCData = (e) => {
    const { name, value } = e.target;
    if (name == "latlng") {
      setLatLngFieldValue(e.target.value);
      if (e.target.value.includes("/")) {
        let latLngArray = e.target.value.split("/");
        setCreatePHCData({
          ...createPHCData,
          latitude: parseFloat(latLngArray[0]),
          longitude: parseFloat(latLngArray[1]),
        });
      } else {
        setCreatePHCData({
          ...createPHCData,
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
      setCreatePHCData({ ...createPHCData, [name]: value });
    }
  };

  const [selectedPanchayat, setSelectedPanchayat] = useState([]);
  const [topicsOptions, setTopicsOptions] = useState([]);
  const [filteredOptions, setFilteredOptions] = useState([]);
  const [searchKey, setSearchKey] = useState("");

  const setGramPanchayat = (e, item) => {
    if (e.target.checked) {
      setSelectedPanchayat([...selectedPanchayat, item]);
    } else {
      setSelectedPanchayat(
        selectedPanchayat.filter((id) => id.uuid !== e.target.value)
      );
    }
  };

  const searchPanchayat = (e) => {
    let keyWord = e.target.value;
    setSearchKey(keyWord);
    if (keyWord) {
      const filteredData = topicsOptions.filter((items) => {
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
      panchayats = topicsOptions;
    }
    setFilterPanchayat(panchayats);
  }, [filteredOptions, topicsOptions, searchKey]);

  function savePHC(e) {
    let phcData = {
      type: "Phc",
      properties: createPHCData,
    };
    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(phcData),
    };
    dispatch(loadCreatePHC(postResponse, selectedPanchayat));
    props.handlePhcClose(false);
  }

  const [pageLoader, setPageLoader] = useState(false);
  const closeModal = () => {
    props.handlePhcClose(false);
  };

  const updatePHC = (e) => {
    setPageLoader(true);
    let phcUpdateData = {
      type: "Phc",
      properties: createPHCData,
    };
    var postResponse = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(phcUpdateData),
    };

    // dispatch(loadUpdatePHC(e, postResponse, selectedPanchayat))
    dispatch(loadUpdatePHC(e, postResponse, setPageLoader, closeModal));
  };

  const [googleMapsModel, setGoogleMapsModel] = useState(false);
  const [tooltip, setTooltip] = useState(true);

  const openMapModal = () => {
    setGoogleMapsModel(true);
    // props.handlePhcClose(false);
  };

  const closeGoogleMaps = () => {
    setGoogleMapsModel(false);
    props.setPhcShow(true);
  };

  const [address, setAddress] = useState("");
  const [latLan, setLatLan] = useState({
    lat: "",
    lng: "",
  });

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
    dispatch(loadAllGramPanchayatPhc(phcUuid));
  }, [stateuuid, distuuid, talukuuid, phcUuid]);

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
      setCreatePHCData({
        ...createPHCData,
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

    if (gramPanchayatListPhc) {
      let dropOption1 = [];
      for (var i = 0; i < gramPanchayatListPhc.length; i++) {
        dropOption1.push({
          label: gramPanchayatListPhc[i].target.properties.name,
          value: gramPanchayatListPhc[i].target.properties.name,
          uuid: gramPanchayatListPhc[i].target.properties.uuid,
        });
      }
      setSelectedPanchayat(dropOption1);
    }
  }, [latLan, gramPanchayatList, gramPanchayatListPhc]);

  useEffect(() => {
    let ele = document.getElementsByName("inputCheck");
    for (var i = 0; i < ele.length; i++) {
      for (var j = 0; j < gramPanchayatListPhc.length; j++) {
        if (ele[i].value === gramPanchayatListPhc[j].target.properties.uuid) {
          ele[i].checked = true;
        }
      }
    }
  }, [gramPanchayatListPhc]);

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
      <ToastContainer />
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <Modal
        show={props.phcShow}
        onHide={props.handlePhcClose}
        className="phc-modal-div">
        <div className="phc-header-div">
          <h5 className="vital-header">
            {" "}
            {props.editUuid ? "Edit PHC Details" : "Enter PHC Details"}{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={props.handlePhcClose}></i>
          </h5>
        </div>
        <Form>
          <Row>
            <Col lg={6} md={6} className="cred-staff">
              <Row className="phc-info-div">
                <Col lg={6} md={6}>
                  <div>
                    <Form.Group>
                      <Form.Label>
                        PHC Name <span style={{ color: "red" }}>*</span>
                      </Form.Label>
                      <Form.Control
                        type="text"
                        name="name"
                        placeholder="Enter PHC Name"
                        readOnly
                        value={phc || ""}
                        onChange={handlePHCData}
                      />
                    </Form.Group>
                  </div>
                </Col>
                <Col lg={6} md={6}>
                  <Row>
                    <Col lg={6} md={6}>
                      <div>
                        <Form.Group>
                          <Form.Label>PHC Type </Form.Label>
                          <Form.Control
                            type="text"
                            placeholder="Enter PHC Type"
                            readOnly
                            name="type"
                            value="Rural"
                            onChange={handlePHCData}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col lg={6} md={6}></Col>
                  </Row>
                </Col>
              </Row>
              <div className="phc-info-div">
                <div>
                  <Form.Group>
                    <Form.Label>Description about the PHC</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Enter Description"
                      name="description"
                      value={description || ""}
                      onChange={handlePHCData}
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
                        onChange={handlePHCData}
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
                          value={latLngFieldValue || ""}
                          onChange={handlePHCData}
                          name="latlng"
                        />
                        <InputGroup.Text
                          id="basic-lat-addon"
                          className="lat-long"
                          onClick={openMapModal}>
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
                  <div className="pro-info-circle">
                    {!tooltip ? (
                      <Row
                        className="location-modal"
                        onClick={() => setTooltip(!tooltip)}>
                        <Col lg={2} md={2}>
                          <Button
                            variant="link"
                            className="pro-close-btn-circle">
                            <i className="bi bi-info-circle-fill pro-close-circle"></i>
                          </Button>
                        </Col>
                        <Col lg={10} md={10}>
                          <p className="location-text-modal">
                            Turn on Location to capture the Lat-Long or Enter
                            Manually
                          </p>
                        </Col>
                      </Row>
                    ) : (
                      <Button variant="link">
                        <i
                          className="bi bi-info-circle-fill pro-circle"
                          onClick={() => setTooltip(!tooltip)}></i>
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
                      onChange={handlePHCData}
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
                        onChange={handlePHCData}>
                        <option value="" disabled hidden>
                          Select State
                        </option>
                        {stateList &&
                          stateList.map((states, i) => (
                            <option
                              value={states.target.properties.name}
                              select={states.target.properties.uuid}
                              key={i}>
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
                        onChange={handlePHCData}
                        disabled={!stateuuid}>
                        <option value="" disabled hidden>
                          Select District
                        </option>
                        {districtList &&
                          districtList.map((dist, i) => (
                            <option value={dist.target.properties.name} key={i}>
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
                        onChange={handlePHCData}
                        disabled={!distuuid}>
                        <option value="" disabled hidden>
                          Select Taluk
                        </option>
                        {talukList &&
                          talukList.map((taluk, i) => (
                            <option
                              value={taluk.target.properties.name}
                              key={i}>
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
                        onChange={handlePHCData}
                        disabled={!talukuuid}>
                        <option value="" disabled hidden>
                          Select Village / Ward
                        </option>
                        {villageList &&
                          villageList.map((village, i) => (
                            <option
                              value={village.target.properties.name}
                              key={i}>
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
                        name="pin"
                        maxLength={6}
                        onKeyPress={(e) => {
                          if (!/[0-9]/.test(e.key)) {
                            e.preventDefault();
                          }
                        }}
                        value={pin || ""}
                        onChange={handlePHCData}
                      />
                    </Form.Group>
                  </div>
                </Col>
              </Row>
              <Row className="phc-info-div">
                <Col lg={9} md={9}>
                  <div>
                    <Form.Group>
                      <Form.Label>PHC Contact No.</Form.Label>
                      <Form.Control
                        type="tel"
                        placeholder="Enter PHC Contact No."
                        name="contact"
                        maxLength={10}
                        onKeyPress={(e) => {
                          if (!/[0-9]/.test(e.key)) {
                            e.preventDefault();
                          }
                        }}
                        value={contact || ""}
                        onChange={handlePHCData}
                      />
                    </Form.Group>
                  </div>
                </Col>
              </Row>
              <Row className="phc-info-div">
                <Col lg={9} md={9}>
                  <div>
                    <Form.Group>
                      <Form.Label>PHC Email ID</Form.Label>
                      <Form.Control
                        type="email"
                        placeholder="Enter PHC Email ID"
                        name="email"
                        value={email || ""}
                        onChange={handlePHCData}
                      />
                    </Form.Group>
                  </div>
                </Col>
                <Col lg={3} md={3}></Col>
              </Row>
            </Col>
            <Col lg={6} md={6}>
              <Row>
                <Col lg={6} md={6} className="village-list">
                  <h5 className="vital-header">
                    Associate Gram Panchayats to PHC
                  </h5>
                  <p className="super-desc">
                    Select the Gram Panchayats to PHC for{" "}
                    <b>
                      {talukName && (
                        <>
                          {talukName.charAt(0).toUpperCase() +
                            talukName.slice(1).toLowerCase()}
                        </>
                      )}
                    </b>
                  </p>
                  <div>
                    <Form.Control
                      type="text"
                      placeholder="Search Gram Panchayat"
                      onChange={searchPanchayat}
                    />
                  </div>
                  <div className="gram-list-div">
                    {filterPanchayat &&
                      filterPanchayat.map((item, i) => (
                        <div key={i} className="gram-list-item">
                          <Form.Check
                            key={i}
                            name="inputCheck"
                            value={item?.uuid}
                            type="checkbox"
                            className={isChecked(item)}
                            label={item?.label}
                            onChange={(e) => setGramPanchayat(e, item)}
                            style={{ fontSize: "14px" }}
                          />
                        </div>
                      ))}
                  </div>
                  <br />
                  <br />
                </Col>
                <Col lg={6} md={6} className="associated-village-list">
                  <h5 className="vital-header">Associated Gram Panchayats</h5>
                  <p className="super-desc">
                    All the Gram Panchayat selected will be shown in the list
                    below
                  </p>
                  <p className="super-list">
                    {selectedPanchayat.map((gram, i) => (
                      <li key={i}>{gram.label}</li>
                    ))}
                  </p>
                </Col>
              </Row>
            </Col>
          </Row>
          <div className="regFoot align-me2 resetButton">
            <div className="save-btn-section">
              <SaveButton
                butttonClick={props.handlePhcClose}
                class_name="regBtnPC"
                button_name="Cancel"
              />
            </div>
            <div className="save-btn-section">
              {props.editUuid ? (
                <SaveButton
                  class_name="regBtnN"
                  butttonClick={(e) => updatePHC(props.editUuid)}
                  button_name="Update"
                />
              ) : (
                <SaveButton
                  class_name="regBtnN"
                  butttonClick={savePHC}
                  button_name="Save"
                />
              )}
            </div>
          </div>
        </Form>
      </Modal>
    </React.Fragment>
  );
}
