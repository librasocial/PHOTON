import React, { useState } from "react";
import { FormCheck } from "react-bootstrap";
import { Marker, InfoWindow } from "react-google-maps";

const MarkerDiv = (props) => {
  const { color, name } = props;

  let houseName;
  let type = props.type;
  let houseID;
  let familyMembers;
  let villageName;
  if (props.type === "houseHold") {
    houseName = props.houseName;
    houseID = props.houseID;
    familyMembers = props.totalFamilyMembers;
  } else if (props.type === "place") {
    houseName = props.name;
    houseID = props.subType;
    familyMembers = "";
  } else if (props.type === "village") {
    villageName = props.name;
  } else if (props.type === "survey") {
    houseName = props.houseName;
    houseID = props.houseID;
  }

  let image;
  if (type === "houseHold") {
    image = "../../img/map-icon/pink_home.png";
  } else if (type === "place") {
    if (props.assetType === "Temple") {
      image = "../../img/gis-map-icon/gis-map-001.png";
    } else if (props.assetType === "Mosque") {
      image = "../../img/gis-map-icon/gis-map-002.png";
    } else if (props.assetType === "Church") {
      image = "../../img/gis-map-icon/gis-map-003.png";
    } else if (props.assetType === "Hotel") {
      image = "../../img/gis-map-icon/gis-map-004.png";
    } else if (props.assetType === "Construction") {
      image = "../../img/gis-map-icon/gis-map-005.png";
    } else if (props.assetType === "BusStop") {
      image = "../../img/gis-map-icon/gis-map-006.png";
    } else if (props.assetType === "Shop") {
      image = "../../img/gis-map-icon/gis-map-007.png";
    } else if (props.assetType === "Toilet") {
      image = "../../img/gis-map-icon/gis-map-008.png";
    } else if (props.assetType === "Park") {
      image = "../../img/gis-map-icon/gis-map-009.png";
    } else if (props.assetType === "OtherPlaces") {
      image = "../../img/gis-map-icon/gis-map-010.png";
    } else if (props.assetType === "Office") {
      image = "../../img/gis-map-icon/gis-map-011.png";
    } else if (props.assetType === "WaterBody") {
      image = "../../img/gis-map-icon/gis-map-014.png";
    } else if (props.assetType === "SchoolCollage") {
      image = "../../img/gis-map-icon/gis-map-013.png";
    } else if (props.assetType === "Hospital") {
      image = "../../img/gis-map-icon/gis-map-012.png";
    } else {
      image = "../../img/gis-map-icon/mark-icon.png";
    }
  } else if (type === "village-name") {
    image = "../../img/map-icon/map_dot.png";
  }

  const [showInfoWindow, setShowInfoWindow] = useState(false);
  const handleMouseOver = (e) => {
    setShowInfoWindow(true);
  };
  const handleMouseExit = (e) => {
    setShowInfoWindow(false);
  };
  // alert("Check")
  return (
    <div>
      {type == "village-name" ? (
        <Marker
          className="pin bounce "
          position={{ lat: props?.lat, lng: props?.lng }}
          style={{ background: "#000", cursor: "pointer" }}
          // icon={"../../img/map-icon/pink_home1.png"}
          // icon={image}
          icon="none"
          // zIndex={10}
          title={name}
        >
          {/* {props.zoom >= "14" && */}
          <InfoWindow className="">
            <div
              style={{
                fontSize:
                  props.zoom <= "12"
                    ? "8px"
                    : props.zoom == "13"
                    ? "11px"
                    : props.zoom == "14"
                    ? "15px"
                    : props.zoom > "14" && "18px",
              }}
            >
              <b>
                {/* {props.index + 1} */}
                <i className="fa fa-dot-circle-o" aria-hidden="true"></i>
                &nbsp;&nbsp;
                {name}
              </b>
            </div>
          </InfoWindow>
          {/* } */}
        </Marker>
      ) : (
        <Marker
          className="pin bounce"
          position={{ lat: props.lat, lng: props.lng }}
          // style={{ backgroundImage: `url(${image})`, cursor: 'pointer' }}
          icon={image ? image : ""}
          zIndex={500}
          onMouseOver={(e) => handleMouseOver(e)}
          onMouseOut={(e) => handleMouseExit(e)}
          title={name}
        >
          {showInfoWindow && (
            <InfoWindow>
              <div
                style={{
                  backgroundColor: "#fff",
                  padding: "10px 15px",
                  marginRight: "30px",
                }}
              >
                {type !== "village" && (
                  <>
                    <span>{type === "houseHold" ? "House Name" : "Name"}</span>
                    &nbsp;: <b>{houseName}</b> <br />
                  </>
                )}
                {type !== "village" && (
                  <>
                    <span>{type === "houseHold" ? "House Id" : "Type"}</span>{" "}
                    &nbsp;: <b>{houseID}</b>
                    <br />
                  </>
                )}
                {type === "houseHold" && (
                  <span>
                    Total Members&nbsp;: <b>{familyMembers}</b>
                  </span>
                )}
                {type === "village" && (
                  <span className="village-name">
                    <b>{villageName}</b>
                  </span>
                )}
              </div>
            </InfoWindow>
          )}
        </Marker>
      )}
    </div>
  );
};

export default MarkerDiv;
