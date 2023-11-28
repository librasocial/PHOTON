import React, { useEffect, useState } from "react";
import { Row, Col } from "react-bootstrap";
import Modal from "react-bootstrap/Modal";
import {
  withScriptjs,
  withGoogleMap,
  Marker,
  GoogleMap,
} from "react-google-maps";
import { compose, withProps } from "recompose";

const GOOGLE_API_KEY = "AIzaSyBITWADW5JiYQUJBspxyPq0OLCrTCNwrSQ";

function GoogleMapModal(props) {
  const [defaultLatLng, setDefaultLatLng] = useState({
    lat: "",
    lng: "",
  });

  let address = props.address;

  const containerStyle = {
    width: "100%",
    height: "370px",
  };

  const [searchVal, setSearchVal] = useState("");
  const searchLocation = (event) => {
    setSearchVal(event.target.value);
  };

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(function (position) {
      setDefaultLatLng({
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      });
    });
  }, []);

  useEffect(() => {
    if (props.latLan.lat != "") {
      setDefaultLatLng(props.latLan);
    }
  }, [props.latLan]);

  const onMarkerDragEnd = (e) => {
    props.setLatLan({
      lat: e.latLng.lat(),
      lng: e.latLng.lng(),
    });
  };

  const inputStyle = {
    boxSizing: `border-box`,
    border: `1px solid transparent`,
    width: `260px`,
    height: `32px`,
    padding: `0 12px`,
    borderRadius: `0px`,
    boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
    fontSize: `12px`,
    outline: `none`,
    textOverflow: `ellipses`,
    position: "absolute",
    top: "10px",
    right: "60px",
  };

  window.initMap = function initMap() {};
  const MyMapComponent = compose(
    withProps({
      googleMapURL:
        "https://maps.googleapis.com/maps/api/js?key=" +
        GOOGLE_API_KEY +
        "&libraries=geometry,drawing,places&callback=initMap",
      loadingElement: <div style={{ height: "600" }} />,
      containerElement: <div style={{ height: "600", width: "100%" }} />,
      mapElement: <div style={{ height: "600" - 80, width: "100%" }} />,
    }),
    withScriptjs,
    withGoogleMap
  )((props) => (
    <GoogleMap
      defaultZoom={12}
      defaultCenter={defaultLatLng}
      center={defaultLatLng}
    >
      <>
        <Marker
          position={defaultLatLng}
          title={address}
          clickable={true}
          draggable
          onDragEnd={onMarkerDragEnd}
        />
      </>
    </GoogleMap>
  ));

  return (
    <div>
      <div>
        <Modal
          show={props.googleMapsModel}
          onHide={props.openGoogleMaps}
          className="phc-modal-div"
        >
          <div className="add-user-heading">
            <Row>
              <Col xs={9}>
                <div>
                  <input
                    type="text"
                    placeholder={"Search..."}
                    // style={inputStyle}
                    value={searchVal}
                    onChange={searchLocation}
                  />
                  <button
                    type="button"
                    className="google-address-ok"
                    onClick={(e) => props.searchAddress(searchVal)}
                    data-bs-dismiss="modal"
                  >
                    Search
                  </button>
                </div>
              </Col>
              <Col xs={3}>
                <button
                  type="button"
                  className="google-address-ok"
                  onClick={props.openGoogleMaps}
                  data-bs-dismiss="modal"
                >
                  OK
                </button>
              </Col>
            </Row>
          </div>
          <div className="">
            <MyMapComponent />
          </div>
        </Modal>
      </div>
    </div>
  );
}

export default GoogleMapModal;
