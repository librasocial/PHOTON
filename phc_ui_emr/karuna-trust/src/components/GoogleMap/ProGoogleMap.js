import React, { useEffect, useState } from "react";
import {
  Breadcrumb,
  Row,
  Button,
  Col,
  Form,
  Accordion,
  Card,
} from "react-bootstrap";
import MarkerDiv from "./MarkerDiv";
import { compose, withProps, withState, withHandlers } from "recompose";
import {
  loadSubCenters,
  loadVillages,
  loadHouseHold,
  loadPlaces,
  loadPhcVillage,
  loadIndividual,
  getHouseHolds,
  getIndividual,
  loadDiabetis,
  loadExamination,
  loadTest,
  loadHistory,
  loadExaminationDiabeties,
  loadEMRData,
} from "../../redux/actions";
import { useSelector, useDispatch } from "react-redux";
import {
  withScriptjs,
  withGoogleMap,
  Polygon,
  GoogleMap,
  Marker,
  InfoWindow,
} from "react-google-maps";
import * as serviceHeaders from "../ConstUrl/serviceHeaders";
import { MultiSelect } from "react-multi-select-component";
import {
  loadHouseHoldDropdown,
  loadVillageAsstes,
  loadSocioDropdown,
  loadTopicsDropdown,
  loadHealthId,
  loadRMNCHA,
  loadHealthAndWellness,
  loadCommunicable,
  loadEMR,
} from "../../redux/formUtilityAction";
import "./Marker.css";
import "../../components/EMR/VitalScreenTabs/VitalScreenTabs.css";
import Autosuggest from "react-autosuggest";
import axios from "axios";
import Select from "react-select";
import mapStyles from "./mapStyles.json";
import { useRef } from "react";
import PageLoader from "../PageLoader";
import SaveButton from "../EMR_Buttons/SaveButton";
import { ncdarray, diabetiessubtopic } from "./GisConstant";

const GOOGLE_API_KEY = "YOUR_GOOGLE_API_KEY";

window.google = window.google || {};
export default function ProGoogleMap(props) {
  const refMap = useRef(null);

  const bordColor = [
    "#ff9d4e",
    "#3D78EA",
    "#7dfc56",
    "#fc7878",
    "#1D1CE5",
    "#810CA8",
    "#7e2387",
    "#872358",
    "#0500ff",
    "#Ff00cd",
  ];

  let url_link =
    "https://app.powerbi.com/reportEmbed?reportId=16036664-6c97-4f7a-b445-7b11741acb97&autoAuth=true&ctid=1942072d-23c5-40e7-a005-51fa4fa08042";

  const [phcValue, selectPhcValue] = useState("");
  const [selected, setSelected] = useState([]);
  const selectSubCenter = (item) => {
    selectPhcValue("");
    setSelected(item);
  };

  const [selected1, setSelected1] = useState([]);
  const selectVillage = (item) => {
    selectPhcValue("");
    setSelected([]);
    setSelected1(item);
  };
  const [selectTopic, setSelectTopic] = useState("");

  const setSelected2 = (selected) => {
    setSelectTopic(selected.label);
  };
  const [filters, setFilters] = useState([]);
  const [historySymptoms, setHistorySymptoms] = useState([]);
  const [selectExaminValue, setSelectExaminValue] = useState([]);
  const [selectEMR, setSelectEMR] = useState([]);
  console.log(selectEMR, "selectEMR");

  const clearFilter = () => {
    setSelectSubTopic([]);
    setFilters([]);
    setHistorySymptoms([]);
    setSelectExaminValue([]);
    setSelectEMR([]);
  };

  useEffect(() => {
    // if (selectTopic == "Non-Communicable Diseases") {
    //   dispatch(loadDiabetis());
    //   dispatch(loadExamination());
    //   dispatch(loadTest());
    // }
    if (historySymptoms.length != 0) {
      dispatch(loadHistory(selectedVillage, historySymptoms));
    }
    if (selectExaminValue.length != 0) {
      dispatch(loadExaminationDiabeties(selectedVillage, selectExaminValue));
    }

    // dispatch(loadEMRData());
    if (selectEMR.length != 0) {
      dispatch(loadEMRData(selectedVillage, selectEMR));
    }

    if (arrayTopicsValue && filters.length != 0) {
      dispatch(loadHouseHold(arrayTopicsValue, selectedVillage, filters));
    } else {
      dispatch(getHouseHolds([]));
    }
    console.log(householdList);
  }, [filters, historySymptoms, selectExaminValue, selectEMR]);

  const [pageLoader, setPageLoader] = useState(false);

  useEffect(() => {
    let centerUuid = [];
    if (phcValue) {
      setPageLoader(true);
      for (var i = 0; i < subCentersList.length; i++) {
        centerUuid.push(subCentersList[i].target.properties.uuid);
      }
    } else if (!phcValue && selected) {
      setPageLoader(true);
      // let centerUuid = []
      for (var i = 0; i < selected.length; i++) {
        centerUuid.push(selected[i].uuid);
      }
    }

    if (centerUuid) {
      let sendData = {
        relationshipType: "SERVICEDAREA",
        sourceType: "SubCenter",
        targetType: "Village",
        stepCount: 1,
        size: 100,
        srcInClause: {
          uuid: centerUuid,
        },
      };
      let postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(sendData),
      };
      dispatch(loadVillages(postResponse, setPageLoader));
    }
  }, [selected, phcValue, subCentersList]);

  let phcuuid = sessionStorage.getItem("uuidofphc");
  let dispatch = useDispatch();

  const [subTopicsValue, setSubTopicsValue] = useState(false);

  const { subCentersList } = useSelector((state) => state.data);
  const { villageList } = useSelector((state) => state.data);
  const { householdList } = useSelector((state) => state.data);
  const { IndividualList } = useSelector((state) => state.data);
  const { placeList } = useSelector((state) => state.data);
  const { allPhcVillage } = useSelector((state) => state.data);
  const { topicsDropdown } = useSelector((state) => state.formData);
  const { socioDropdown } = useSelector((state) => state.formData);
  const { houseHoldDropdown } = useSelector((state) => state.formData);
  const { healthIdDropdown } = useSelector((state) => state.formData);
  const { rmnchaDropdown } = useSelector((state) => state.formData);
  const { healthandwellnessDropdown } = useSelector((state) => state.formData);
  const { communicableDropdown } = useSelector((state) => state.formData);
  const { emrDropdown } = useSelector((state) => state.formData);
  const { villageAsstes } = useSelector((state) => state.formData);
  const { diabetiesdata } = useSelector((state) => state.data);
  const { examinationdata } = useSelector((state) => state.data);
  const { testdata } = useSelector((state) => state.data);
  const { historyList } = useSelector((state) => state.data);
  const { examinationList } = useSelector((state) => state.data);
  const { emrdataList } = useSelector((state) => state.data);
  const [villageUuid, setVillageUuid] = useState([]);

  useEffect(() => {
    let postData = {
      relationshipType: "CONTAINEDINPLACE",
      sourceType: "Phc",
      sourceProperties: {
        uuid: phcuuid,
      },
      targetProperties: {},
      targetType: "Village",
      stepCount: 2,
      size: 100,
    };
    var responseRequest = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(postData),
    };
    dispatch(loadPhcVillage(responseRequest));
    dispatch(loadSubCenters(phcuuid));
    dispatch(loadTopicsDropdown());
    dispatch(loadSocioDropdown());
    dispatch(loadHealthId());
    dispatch(loadRMNCHA());
    dispatch(loadHealthAndWellness());
    dispatch(loadCommunicable());
    dispatch(loadEMR());
    dispatch(loadVillageAsstes());

    if (villageUuid.length != 0) {
      var placeData = {
        relationshipType: "CONTAINEDINPLACE",
        sourceType: "Village",
        srcInClause: {
          uuid: villageUuid,
        },
        targetType: "Place",
        stepCount: 1,
        size: 1000,
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(placeData),
      };
      dispatch(loadPlaces(requestOptions));
    }
  }, [villageUuid, selectAssets]);

  const [zoom, setZoom] = useState(12.5);

  const [centCoord, setCentCoord] = useState([]);
  const [centCoord1, setCentCoord1] = useState([]);
  const [subcenterBoundary, setSubCenterBoundary] = useState([]);
  const [subcenterBoundary1, setSubCenterBoundary1] = useState([]);
  const [phcVillageBoundary, setPhcVillageBoundary] = useState([]);
  const [phcVillageBoundary1, setPhcVillageBoundary1] = useState([]);

  // states for multiselect options dropdown
  const [selectingOptions, setSelectingOptions] = useState([]);
  const [villageOptions, setVillageOptions] = useState([]);
  const [topicsOptions, setTopicsOptions] = useState([]);
  // states for multiselect options dropdown

  const [selectedVillage, setSelectedVillage] = useState([]);
  const [selectVillageBoundary, setSelectVillageBoundary] = useState([]);
  const [selectVilBound, setSelectVilBound] = useState([]);
  const [vilBounList, setVilBounList] = useState([]);
  const [vilBoundCoord, setVilBoundCoord] = useState([]);

  useEffect(() => {
    if (selected1.length != 0) {
      let selectedVillages = [];
      let villageUuids = [];
      for (var i = 0; i < allPhcVillage.length; i++) {
        for (var j = 0; j < selected1.length; j++) {
          // if (allPhcVillage[i].target.properties.uuid.includes(selected1[j].uuid)) {
          if (allPhcVillage[i].target.properties.uuid == selected1[j].uuid) {
            selectedVillages.push(allPhcVillage[i]);
            villageUuids.push(selected1[j].uuid);
          }
        }
      }

      const filterData = [
        ...new Map(
          selectedVillages.map((item) => [
            JSON.stringify(item.target.properties.uuid),
            item,
          ])
        ).values(),
      ];
      const filterUuid = [
        ...new Map(
          villageUuids.map((item) => [JSON.stringify(item), item])
        ).values(),
      ];
      setSelectedVillage(filterData);
      setVillageUuid(filterUuid);
    }
  }, [selected1]);

  useEffect(() => {
    let subcenterArray = [];
    let subcenterArray1 = [];
    let phcVillageArray = [];
    let bySubCenter;
    let centerBoundary = [];
    if (villageList) {
      bySubCenter = villageList.reduce((obj, item) => {
        if (obj[item.source.properties.name]) {
          obj[item.source.properties.name].push(item);

          return obj;
        }

        obj[item.source.properties.name] = [{ ...item }];
        return obj;
      }, []);
      Object.keys(bySubCenter).forEach((el) =>
        centerBoundary.push(bySubCenter[el])
      );
    }

    let centerOrd = [];
    let centerOrd1 = [];
    if (centerBoundary) {
      for (var i = 0; i < centerBoundary.length; i++) {
        centerOrd[i] = [];
        centerOrd1[i] = [];
        for (var j = 0; j < centerBoundary[i].length; j++) {
          if (centerBoundary[i][j].target.properties.boundary) {
            centerOrd[i].push(centerBoundary[i][j].target.properties.boundary);
            centerOrd1[i].push({
              name: centerBoundary[i][j].target.properties.name,
              boundary: centerBoundary[i][j].target.properties.boundary,
            });
          }
        }
      }
    }

    if (villageList) {
      for (var i = 0; i < villageList.length; i++) {
        if (villageList[i].target.properties.boundary) {
          subcenterArray.push(villageList[i].target.properties.boundary);
          subcenterArray1.push({
            name: villageList[i].target.properties.name,
            boundary: villageList[i].target.properties.boundary,
          });
        }
      }
    }
    let villageCoord = [];
    if (selectedVillage) {
      for (var i = 0; i < selectedVillage.length; i++) {
        if (selectedVillage[i].target.properties.boundary) {
          villageCoord.push({
            name: selectedVillage[i].target.properties.name,
            boundary: selectedVillage[i].target.properties.boundary,
          });
        }
      }
    }
    setSelectVillageBoundary(villageCoord);
    let phcVillageArray1 = [];
    if (allPhcVillage) {
      for (var i = 0; i < allPhcVillage.length; i++) {
        if (allPhcVillage[i].target.properties.boundary) {
          phcVillageArray.push(allPhcVillage[i].target.properties.boundary);
          phcVillageArray1.push({
            name: allPhcVillage[i].target.properties.name,
            boundary: allPhcVillage[i].target.properties.boundary,
            subCenter: allPhcVillage[i].source.properties.name,
          });
        }
      }
    }

    setCentCoord(centerOrd);
    setCentCoord1(centerOrd1);

    setPhcVillageBoundary(phcVillageArray);
    setPhcVillageBoundary1(phcVillageArray1);
    setSubCenterBoundary(subcenterArray);
    setSubCenterBoundary1(subcenterArray1);

    if (subCentersList) {
      let dropOption = [];
      for (var i = 0; i < subCentersList.length; i++) {
        dropOption.push({
          label: subCentersList[i].target.properties.name,
          value: subCentersList[i].target.properties.name,
          uuid: subCentersList[i].target.properties.uuid,
        });
      }
      setSelectingOptions(dropOption);
    }

    if (allPhcVillage) {
      const filterData = [
        ...new Map(
          allPhcVillage.map((item) => [
            JSON.stringify(item.target.properties.uuid),
            item,
          ])
        ).values(),
      ];
      let villageOption = [];
      for (var i = 0; i < filterData.length; i++) {
        villageOption.push({
          label: filterData[i].target.properties.name,
          value: filterData[i].target.properties.name,
          uuid: filterData[i].target.properties.uuid,
        });
      }
      setVillageOptions(villageOption);
    }

    if (topicsDropdown) {
      let topicOption = [];
      for (var i = 0; i < topicsDropdown.length; i++) {
        for (var j = 0; j < topicsDropdown[i].elements.length; j++) {
          topicOption.push({
            label: topicsDropdown[i].elements[j].title,
            value: topicsDropdown[i].elements[j].title,
          });
        }
      }
      setTopicsOptions(topicOption);
    }
  }, [villageList, subCentersList, allPhcVillage, selectedVillage]);

  const [phcCoordinates, setPhcCoordinates] = useState([]);
  const [phcCoordinates1, setPhcCoordinates1] = useState([]);
  const [villCords, setVillCords] = useState([]);
  const [villCords1, setVillCords1] = useState([]);
  const [subCoordinates, setSubCoordinates] = useState([]);
  const [subCoordinates1, setSubCoordinates1] = useState([]);

  useEffect(() => {
    let subcenterCoordinate = [];
    let subcenterCoordinate1 = [];
    let phcVillageCoordinate = [];
    let phcVillageCoordinate1 = [];

    for (var i = 0; i < phcVillageBoundary.length; i++) {
      phcVillageCoordinate.push(
        phcVillageBoundary[i].split("[").join("").split("],")
      );
    }
    for (var i = 0; i < phcVillageBoundary1.length; i++) {
      phcVillageCoordinate1.push({
        name: phcVillageBoundary1[i].name,
        boundary: phcVillageBoundary1[i].boundary
          .split("[")
          .join("")
          .split("],"),
      });
    }

    for (var j = 0; j < subcenterBoundary.length; j++) {
      subcenterCoordinate.push(
        subcenterBoundary[j].split("[").join("").split("],")
      );
    }
    for (var j = 0; j < subcenterBoundary1.length; j++) {
      subcenterCoordinate1.push({
        bound: subcenterBoundary1[j].boundary.split("[").join("").split("],"),
        name: subcenterBoundary1[j].name,
      });
    }

    let selectBoundArray = [];
    if (selectVillageBoundary) {
      for (var j = 0; j < selectVillageBoundary.length; j++) {
        selectBoundArray.push({
          bound: selectVillageBoundary[j].boundary
            .split("[")
            .join("")
            .split("],"),
          name: selectVillageBoundary[j].name,
        });
      }
    }
    setSelectVilBound(selectBoundArray);
    let vilageCords = [];
    if (centCoord.length != 0) {
      for (var i = 0; i < centCoord.length; i++) {
        vilageCords[i] = [];
        for (var j = 0; j < centCoord[i].length; j++) {
          vilageCords[i].push(centCoord[i][j].split("[").join("").split("],"));
        }
      }
    }
    let vilageCords1 = [];
    if (centCoord1.length != 0) {
      for (var i = 0; i < centCoord1.length; i++) {
        vilageCords1[i] = [];
        for (var j = 0; j < centCoord1[i].length; j++) {
          vilageCords1[i].push({
            bound: centCoord1[i][j].boundary.split("[").join("").split("],"),
            name: centCoord1[i][j].name,
          });
        }
      }
    }

    setVillCords(vilageCords);
    setVillCords1(vilageCords1);
    setPhcCoordinates(phcVillageCoordinate);
    setPhcCoordinates1(phcVillageCoordinate1);
    setSubCoordinates(subcenterCoordinate);
    setSubCoordinates1(subcenterCoordinate1);
  }, [
    subcenterBoundary,
    subcenterBoundary1,
    phcVillageBoundary,
    phcVillageBoundary1,
    centCoord,
    centCoord1,
    selectVillageBoundary,
  ]);

  const [phcCoordList, setPhcCoordList] = useState([]);
  const [phcCoordList1, setPhcCoordList1] = useState([]);
  const [coordList1, setCoordList1] = useState([]);
  const [coordList11, setCoordList11] = useState([]);
  const [villCoordList, setVillCoordList] = useState([]);
  const [villCoordList1, setVillCoordList1] = useState([]);

  useEffect(() => {
    if (phcCoordinates.length > 1) {
      let arrayPhcList = [];
      for (var i = 0; i < phcCoordinates.length; i++) {
        arrayPhcList[i] = [];
        for (var j = 0; j < phcCoordinates[i].length; j++) {
          if (phcCoordinates[i][j].includes("]")) {
            arrayPhcList[i].push(phcCoordinates[i][j].slice(0, -1).split(","));
          } else {
            arrayPhcList[i].push(phcCoordinates[i][j].split(","));
          }
        }
      }
      setPhcCoordList(arrayPhcList);
    }
    if (phcCoordinates1.length > 1) {
      let arrayPhcList2 = [];
      let arrayPhcList1 = [];
      for (var i = 0; i < phcCoordinates1.length; i++) {
        arrayPhcList2[i] = [];
        for (var j = 0; j < phcCoordinates1[i].boundary.length; j++) {
          if (phcCoordinates1[i].boundary[j].includes("]")) {
            arrayPhcList2[i].push(
              phcCoordinates1[i].boundary[j].slice(0, -1).split(",")
            );
          } else {
            arrayPhcList2[i].push(phcCoordinates1[i].boundary[j].split(","));
          }
        }
        arrayPhcList1.push({
          name: phcCoordinates1[i].name,
          boundary: arrayPhcList2[i],
        });
      }
      setPhcCoordList1(arrayPhcList1);
    }

    if (subCoordinates.length > 1) {
      let arrayList = [];
      for (var i = 0; i < subCoordinates.length; i++) {
        arrayList[i] = [];
        for (var j = 0; j < subCoordinates[i].length; j++) {
          if (subCoordinates[i][j].includes("]")) {
            arrayList[i].push(subCoordinates[i][j].slice(0, -1).split(","));
          } else {
            arrayList[i].push(subCoordinates[i][j].split(","));
          }
        }
      }
      setCoordList1(arrayList);
    }

    if (villCords.length > 0) {
      let arrayListVill = [];
      for (var i = 0; i < villCords.length; i++) {
        arrayListVill[i] = [];
        for (var j = 0; j < villCords[i].length; j++) {
          arrayListVill[i][j] = [];
          for (var k = 0; k < villCords[i][j].length; k++) {
            if (villCords[i][j].includes("]")) {
              arrayListVill[i][j].push(
                villCords[i][j][k].slice(0, -1).split(",")
              );
            } else {
              arrayListVill[i][j].push(villCords[i][j][k].split(","));
            }
          }
        }
      }

      setVillCoordList(arrayListVill);
    }
    if (villCords1.length > 0) {
      let arrayListVill1 = [];
      for (var i = 0; i < villCords1.length; i++) {
        arrayListVill1[i] = [];
        for (var j = 0; j < villCords1[i].length; j++) {
          arrayListVill1[i][j] = [];
          for (var k = 0; k < villCords1[i][j].bound.length; k++) {
            if (villCords1[i][j].bound[k].includes("]")) {
              arrayListVill1[i][j].push({
                bound: villCords1[i][j].bound[k].slice(0, -1).split(","),
                name: villCords1[i][j].name,
              });
            } else {
              arrayListVill1[i][j].push({
                bound: villCords1[i][j].bound[k].split(","),
                name: villCords1[i][j].name,
              });
            }
          }
        }
      }

      setVillCoordList1(arrayListVill1);
    }

    if (subCoordinates1.length > 1) {
      let arrayList1 = [];
      for (var i = 0; i < subCoordinates1.length; i++) {
        arrayList1[i] = [];
        for (var j = 0; j < subCoordinates1[i].bound.length; j++) {
          if (subCoordinates1[i].bound[j].includes("]")) {
            arrayList1[i].push({
              bound: subCoordinates1[i].bound[j].slice(0, -1).split(","),
              name: subCoordinates1[i].name,
            });
          } else {
            arrayList1[i].push({
              bound: subCoordinates1[i].bound[j].split(","),
              name: subCoordinates1[i].name,
            });
          }
        }
      }
      setCoordList11(arrayList1);
    }
    if (selectVilBound) {
      let vilBoundArrayList = [];
      let filterArray = [];
      for (var i = 0; i < selectVilBound.length; i++) {
        vilBoundArrayList[i] = [];
        for (var j = 0; j < selectVilBound[i].bound.length; j++) {
          if (selectVilBound[i].bound[j].includes("]")) {
            vilBoundArrayList[i].push(
              selectVilBound[i].bound[j].slice(0, -1).split(",")
            );
          } else {
            vilBoundArrayList[i].push(selectVilBound[i].bound[j].split(","));
          }
        }
        filterArray.push({
          name: selectVilBound[i].name,
          bound: vilBoundArrayList[i],
        });
      }

      setVilBounList(filterArray);
    }
  }, [
    subCoordinates,
    subCoordinates1,
    phcCoordinates,
    phcCoordinates1,
    villCords,
    villCords1,
    selectVilBound,
  ]);

  const [phcVlgCoord, setPhcVlgCoord] = useState([]);
  const [phcVlgCoord1, setPhcVlgCoord1] = useState([]);
  const [vlgLatLngCoordNew1, setVlgLatLngCoordNew1] = useState([]);

  useEffect(() => {
    let phccoord = [];
    let coordArr = [];
    if (phcCoordList.length != 0) {
      for (var i = 0; i < phcCoordList.length; i++) {
        phccoord[i] = [];
        phcCoordList[i].map((coordList) => {
          phccoord[i].push({
            lat: parseFloat(coordList[1]),
            lng: parseFloat(coordList[0]),
          });
        });
      }
    }
    let phccoord1 = [];
    let phcCoord2 = [];
    if (phcCoordList1.length != 0) {
      for (var i = 0; i < phcCoordList1.length; i++) {
        phccoord1[i] = [];
        phcCoordList1[i].boundary.map((coordList) => {
          phccoord1[i].push({
            lat: parseFloat(coordList[1]),
            lng: parseFloat(coordList[0]),
          });
        });
        phcCoord2.push({ name: phcCoordList1[i].name, boundary: phccoord1[i] });
      }
    }
    if (coordList1.length != 0) {
      for (var i = 0; i < coordList1.length; i++) {
        coordArr[i] = [];
        coordList1[i].map((coordList) => {
          coordArr[i].push({
            lat: parseFloat(coordList[1]),
            lng: parseFloat(coordList[0]),
          });
        });
      }
    }

    let villcoordArr1 = [];
    let vlgLatLng1 = [];
    if (villCoordList1.length != 0) {
      for (var i = 0; i < villCoordList1.length; i++) {
        villcoordArr1[i] = [];
        vlgLatLng1[i] = [];
        for (var j = 0; j < villCoordList1[i].length; j++) {
          villcoordArr1[i][j] = [];
          villCoordList1[i][j].map((coordList) => {
            villcoordArr1[i][j].push({
              lat: parseFloat(coordList.bound[1]),
              lng: parseFloat(coordList.bound[0]),
            });
          });
          vlgLatLng1[i].push({
            lat: parseFloat(villCoordList1[i][j][20].bound[1]),
            lng: parseFloat(villCoordList1[i][j][20].bound[0]),
            name: villCoordList1[i][j][20].name,
          });
        }
      }
    }

    let vilCoordList = [];
    let vilLatLngList = [];
    if (vilBounList.length != 0) {
      for (var i = 0; i < vilBounList.length; i++) {
        vilCoordList[i] = [];

        vilBounList[i].bound.map((coordList1) => {
          vilCoordList[i].push({
            lat: parseFloat(coordList1[1]),
            lng: parseFloat(coordList1[0]),
          });
        });
        vilLatLngList.push({
          name: vilBounList[i].name,
          bound: vilCoordList[i],
        });
      }
    }

    setVilBoundCoord(vilLatLngList);
    setVlgLatLngCoordNew1(villcoordArr1);
    setPhcVlgCoord(phccoord);
    setPhcVlgCoord1(phcCoord2);
  }, [
    coordList1,
    coordList11,
    vilBounList,
    phcCoordList,
    phcCoordList1,
    villCoordList,
    villCoordList1,
  ]);

  const filterOption = ({ label, value }, string) => {
    if (string === "") return true;
    const parsedString = string.split(/[, ]+/);
    for (const string of parsedString) {
      // Need to check of string is not empty after the split
      if (string !== "" && (label.includes(string) || value.includes(string)))
        return true;
    }

    return false;
  };

  const [centerValue, setCenterValue] = useState({ lat: "", lng: "" });

  useEffect(() => {
    setCenterValue({ lat: 13.159325195852871, lng: 77.128036475163938 });
  }, [selected, phcVlgCoord, selected1, vlgLatLngCoordNew1, vilBoundCoord]);

  const onClickChange = () => {
    props.setGooglePage(false);
    props.setSuggestions("");
    props.setValue("");
  };

  // Select Sub-Topics

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";
  const [arrayTopicsValue, setArrayTopicsValue] = useState("");
  useEffect(() => {
    if (arrayTopicsValue != "") {
      dispatch(loadHouseHoldDropdown(arrayTopicsValue));
    }
  }, [arrayTopicsValue]);

  let subTopicImage = [];
  if (arrayTopicsValue == "Household") {
    subTopicImage = [
      "../../img/gis-map/Vector.png",
      "../../img/gis-map/Vector (1).png",
      "../../img/gis-map/Vector (2).png",
      "../../img/gis-map/Vector (3).png",
      "../../img/gis-map/Vector (4).png",
      "../../img/gis-map/Vector (5).png",
      "../../img/gis-map/Vector (6).png",
      "../../img/gis-map/Popup header.png",
    ];
  } else if (arrayTopicsValue == "Individual") {
    subTopicImage = [
      "../../img/gis-map/Popup header (1).png",
      "../../img/gis-map/Popup header (2).png",
      "../../img/gis-map/Popup header (3).png",
      "../../img/gis-map/Popup header (4).png",
      "../../img/gis-map/Popup header (5).png",
      "../../img/gis-map/Vector (7).png",
      "../../img/gis-map/Vector (8).png",
      "../../img/gis-map/Vector (9).png",
    ];
  }

  let gisTopicsImage = [
    "../../img/gis-map-icon/gis-map-001.png",
    "../../img/gis-map-icon/gis-map-002.png",
    "../../img/gis-map-icon/gis-map-003.png",
    "../../img/gis-map-icon/gis-map-004.png",
    "../../img/gis-map-icon/gis-map-005.png",
    "../../img/gis-map-icon/gis-map-006.png",
    "../../img/gis-map-icon/gis-map-007.png",
    "../../img/gis-map-icon/gis-map-008.png",
    "../../img/gis-map-icon/gis-map-009.png",
    "../../img/gis-map-icon/gis-map-010.png",
    "../../img/gis-map-icon/gis-map-011.png",
    "../../img/gis-map-icon/gis-map-012.png",
    "../../img/gis-map-icon/gis-map-013.png",
    "../../img/gis-map-icon/gis-map-014.png",
  ];

  const setArrayTopics = (e, name) => {
    if (e.target.checked) {
      setArrayTopicsValue(name);
    } else {
      setArrayTopicsValue("");
    }
  };

  const [selectAssets, setSelectAssets] = useState("");
  const [subAsset, setSubAsset] = useState("");
  const setSelectSubAssets = (assetName) => {
    setSubAsset(assetName);
    setSelectAssets("");
  };
  // const [subAsset, setSubAsset] = useState([])
  // console.log(subAsset,"test")
  //  const setSelectSubAssets = (assetName) => {
  //     alert(assetName)
  //      setSubAsset([...subAsset, assetName]);
  //      setSelectAssets("")
  //  }

  const [selectSubTopic, setSelectSubTopic] = useState([]);

  const [value, setValue] = useState("");
  const [suggestions, setSuggestions] = useState([]);

  const [googlePage, setGooglePage] = useState(false);
  const gisState = () => {
    setGooglePage(true);
  };

  const [selectedPlaces, setSelectedPlaces] = useState([]);
  useEffect(() => {
    if (subTopicsValue == true) {
      let selectedList = [...placeList];
      setSelectedPlaces(selectedList);
    } else if ((selectAssets || subAsset) && placeList) {
      let selectedList = [];
      if (selectAssets) {
        placeList.map((item) => {
          if (item.target.properties.assetType === selectAssets) {
            selectedList.push(item);
          }
        });
      }
      if (subAsset) {
        placeList.map((item) => {
          if (item.target.properties.assetSubType === subAsset) {
            selectedList.push(item);
          }
        });
      }
      setSelectedPlaces(selectedList);
    }
  }, [selectAssets, placeList, subAsset, subTopicsValue]);

  const companies = [
    { id: 1, name: "Suggnahalli" },
    { id: 2, name: "Chikkamaskal" },
    { id: 3, name: "Hulikal" },
    { id: 4, name: "Chikahhali" },
  ];

  const lowerCasedCompanies = companies.map((company) => {
    return {
      id: company.id,
      name: company.name.toLowerCase(),
    };
  });

  function getSuggestions(value) {
    return lowerCasedCompanies.filter((company) =>
      company.name.includes(value.trim().toLowerCase())
    );
  }

  function clearMap() {
    setValue("");
  }
  // Select Sub-Topics
  window.initMap = function initMap() {};

  const defaultMapOptions = {
    styles: mapStyles,
  };

  const handleZoomChanged = () => {
    const newZoom = refMap.current.getZoom(); //get map Zoom
    setZoom(newZoom);
  };

  const handleNcdSubtopic = (e, choice) => {
    if (historySymptoms.length == 0) {
      setHistorySymptoms([...historySymptoms, choice]);
    } else {
      historySymptoms.filter((data) => {
        if (data.includes(choice)) {
          setHistorySymptoms(
            historySymptoms.filter((value) => value != choice)
          );
        } else {
          setHistorySymptoms([...historySymptoms, choice]);
        }
      });
    }
  };

  const handleNcdExaminatioSubtopic = (e, item) => {
    if (selectExaminValue.length == 0) {
      setSelectExaminValue([...selectExaminValue, item]);
    } else {
      selectExaminValue.filter((data) => {
        if (data.includes(item)) {
          setSelectExaminValue(
            selectExaminValue.filter((value) => value != item)
          );
        } else {
          setSelectExaminValue([...selectExaminValue, item]);
        }
      });
    }
  };

  const [selectEMRSubtopic, setSelectEMRSubtopic] = useState([]);
  const handleEMRSubtopic = (e, item, elements) => {
    if (selectEMRSubtopic.length == 0) {
      setSelectEMRSubtopic([...selectEMRSubtopic, elements]);
      setSelectEMR([...selectEMR, { name: item, value: elements }]);
    } else {
      selectEMRSubtopic.filter((data) => {
        if (data.includes(elements)) {
          setSelectEMR(selectEMR.filter((value) => value.value != elements));
          setSelectEMRSubtopic(
            selectEMRSubtopic.filter((value) => value != elements)
          );
        } else {
          setSelectEMRSubtopic([...selectEMRSubtopic, elements]);
          setSelectEMR([...selectEMR, { name: item, value: elements }]);
        }
      });
    }
  };
  const dropDownService = (choice) => {
    if (choice === "History & Symptoms") {
      dispatch(loadDiabetis());
    } else if (choice === "Examination") {
      dispatch(loadExamination());
    } else if (choice === "Test") {
      dispatch(loadTest());
    }
  };

  const handleSubtopic = (e, item, choice) => {
    if (selectSubTopic.length == 0) {
      setSelectSubTopic([...selectSubTopic, choice]);
      setFilters([
        ...filters,
        {
          key: item.propertyName,
          value: choice,
        },
      ]);
    } else {
      selectSubTopic.filter((data) => {
        if (data.includes(choice)) {
          setSelectSubTopic(selectSubTopic.filter((value) => value != choice));
          setFilters(filters.filter((e) => e.value != choice));
        } else {
          setSelectSubTopic([...selectSubTopic, choice]);
          setFilters([
            ...filters,
            {
              key: item.propertyName,
              value: choice,
            },
          ]);
        }
      });
    }
  };

  console.log(householdList, "tests");
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
      ref={refMap}
      defaultZoom={zoom}
      defaultCenter={{ lat: 13.159325195852871, lng: 77.128036475163938 }}
      center={centerValue}
      defaultOptions={defaultMapOptions}
      onZoomChanged={handleZoomChanged}
    >
      {arrayTopicsValue &&
        householdList &&
        householdList.map((center, i) => (
          <MarkerDiv
            key={i}
            name={center.context?.displayName}
            color="red"
            lat={
              center.context?.latitude && parseFloat(center.context?.latitude)
            }
            lng={
              center.context?.longitude && parseFloat(center.context?.longitude)
            }
            houseName={center.context?.displayName}
            houseID={center.context?.displayId}
            // totalFamilyMembers={center.context?.totalMembers}
            type="survey"
          />
        ))}
      {historyList &&
        historyList.map((center, i) => (
          <MarkerDiv
            key={i}
            name={center.context?.surveyType}
            color="red"
            lat={
              center.context?.latitude && parseFloat(center.context?.latitude)
            }
            lng={
              center.context?.longitude && parseFloat(center.context?.longitude)
            }
            houseName={center.context?.displayName}
            houseID={center.context?.displayId}
            totalFamilyMembers={center.context?.totalMembers}
            type=""
          />
        ))}
      {examinationList &&
        examinationList.map((center, i) => (
          <MarkerDiv
            key={i}
            name={center.context?.surveyType}
            color="red"
            lat={
              center.context?.latitude && parseFloat(center.context?.latitude)
            }
            lng={
              center.context?.longitude && parseFloat(center.context?.longitude)
            }
            houseName={center.context?.displayName}
            houseID={center.context?.displayId}
            totalFamilyMembers={center.context?.totalMembers}
            type=""
          />
        ))}

      {emrdataList &&
        emrdataList.map((center, i) => (
          <MarkerDiv
            key={i}
            name={center.context?.displayName}
            color="red"
            lat={
              center.context?.latitude && parseFloat(center.context?.latitude)
            }
            lng={
              center.context?.longitude && parseFloat(center.context?.longitude)
            }
            houseName={center.context?.displayName}
            houseID={center.context?.displayId}
            // totalFamilyMembers={center.context?.totalMembers}
            type=""
          />
        ))}

      {/* {arrayTopicsValue === "Individual" &&
        IndividualList &&
        zoom >= "12" &&
        IndividualList.map((center, i) => (
          <MarkerDiv
            key={i}
            name={center.context?.displayName}
            color="red"
            lat={
              center.context?.latitude && parseFloat(center.context?.latitude)
            }
            lng={
              center.context?.longitude && parseFloat(center.context?.longitude)
            }
            houseName={center.context?.displayName}
            houseID={center.context?.displayId}
            totalFamilyMembers={center.context?.totalMembers}
            type="Individual"
          />
        ))} */}

      {selectedPlaces.length != 0 &&
        selectedPlaces.map((places, i) => (
          <MarkerDiv
            key={i}
            name={places.target.properties.name}
            lat={
              places.target.properties.latitude &&
              parseFloat(places.target.properties.latitude)
            }
            lng={
              places.target.properties.longitude &&
              parseFloat(places.target.properties.longitude)
            }
            Name={places.target.properties.name}
            subType={places.target.properties.assetSubType}
            assetType={places.target.properties.assetType}
            type="place"
          />
        ))}

      {!phcValue &&
        selectedVillage &&
        selected.length == 0 &&
        selectedVillage.map((vilName, i) => (
          <MarkerDiv
            key={i}
            name={vilName.target.properties.name}
            index={i}
            lat={
              vilName.target.properties.latitude &&
              parseFloat(vilName.target.properties.latitude)
            }
            lng={
              vilName.target.properties.longitude &&
              parseFloat(vilName.target.properties.longitude)
            }
            type="village-name"
            zoom={zoom}
          />
        ))}
      {!phcValue &&
        villageList &&
        vlgLatLngCoordNew1 &&
        villageList.map((vilName, i) => (
          <MarkerDiv
            key={i}
            name={vilName.target.properties.name}
            index={i}
            lat={
              vilName.target.properties.latitude &&
              parseFloat(vilName.target.properties.latitude)
            }
            lng={
              vilName.target.properties.longitude &&
              parseFloat(vilName.target.properties.longitude)
            }
            type="village-name"
            zoom={zoom}
          />
        ))}
      {phcValue &&
        villageList.map((vilName, i) => (
          <MarkerDiv
            key={i}
            name={vilName.target.properties.name}
            index={i}
            lat={
              vilName.target.properties.latitude &&
              parseFloat(vilName.target.properties.latitude)
            }
            lng={
              vilName.target.properties.longitude &&
              parseFloat(vilName.target.properties.longitude)
            }
            type="village-name"
            zoom={zoom}
          />
        ))}

      {/* PHC Boubndary */}
      {phcValue &&
        vlgLatLngCoordNew1 &&
        vlgLatLngCoordNew1.map((coord, i) => (
          <React.Fragment key={i}>
            {coord.map((cordList, index) => (
              <Polygon
                key={index}
                path={cordList}
                options={{
                  fillColor: bordColor[i],
                  fillOpacity: 0.4,
                  strokeColor: "#3D78EA",
                  // strokeColor: "#0002A1",
                  strokeOpacity: 1,
                  strokeWeight: 2,
                }}
                onClick={() => {}}
              />
            ))}
          </React.Fragment>
        ))}
      {/* PHC Boubndary */}

      {/* sub-center boundaries */}
      {!phcValue &&
        selected.length != 0 &&
        vlgLatLngCoordNew1 &&
        vlgLatLngCoordNew1.map((coord, i) => (
          <React.Fragment key={i}>
            {coord.map((cordList, index) => (
              <Polygon
                key={index}
                path={cordList}
                options={{
                  fillColor: bordColor[i],
                  fillOpacity: 0.4,
                  strokeColor: "#3D78EA",
                  // strokeColor: "#0002A1",
                  strokeOpacity: 1,
                  strokeWeight: 2,
                }}
                onClick={() => {}}
              />
            ))}
          </React.Fragment>
        ))}
      {/* sub-center boundaries */}

      {/* Village Boundary */}
      {selected.length == 0 &&
        !phcValue &&
        vilBoundCoord.map((coord, index) => (
          <Polygon
            key={index}
            path={coord.bound}
            options={{
              fillColor: "#fff",
              fillOpacity: 0.4,
              strokeColor: "#000000",
              strokeOpacity: 1,
              strokeWeight: 2,
            }}
            onClick={() => {}}
          />
        ))}
      {/* Village Boundary */}
    </GoogleMap>
  ));

  return (
    <React.Fragment>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <div className="regHeader">
        <h1 className="register-Header">Geographic Information System</h1>
      </div>
      <hr style={{ margin: "0px" }} />
      <Breadcrumb>
        <Breadcrumb.Item
          className="pur-order-breadcrumb"
          onClick={onClickChange}
        >
          Dashboard
        </Breadcrumb.Item>
        <Breadcrumb.Item active>GIS Information System</Breadcrumb.Item>
      </Breadcrumb>
      <div className="pro-tab gis-mapping-div">
        <div className="searchFilter-div">
          <Row className="search-row">
            <Col md={3} className="search-select">
              <div className="pro-select">
                <Form.Group className="mb-3_drugname">
                  <Form.Label className="pro-label">Select Village</Form.Label>
                  {/* <VillageDrop villageList={villageList} setVillageBoundary={setVillageBoundary}
                                        setVillageUuid={setVillageUuid} type="village" /> */}
                  <MultiSelect
                    options={villageOptions}
                    value={selected1}
                    onChange={selectVillage}
                    labelledBy="Select"
                    // disabled={selected.length == 0}
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3} className="search-select">
              <div className="pro-select">
                <Form.Group className="mb-3_drugname">
                  <Form.Label className="pro-label">
                    Select Subcentre
                  </Form.Label>
                  <MultiSelect
                    options={selectingOptions}
                    value={selected}
                    onChange={selectSubCenter}
                    labelledBy="Select"
                  />
                </Form.Group>
              </div>
            </Col>
            <Col md={3} className="search-select">
              <div className="pro-select">
                <Form.Group className="mb-3_drugname">
                  <Form.Label className="pro-label">Select PHC</Form.Label>
                  <Form.Select
                    className="pro-input"
                    aria-label="Default select example"
                    placeholder="Select A Value..."
                    value={phcValue || ""}
                    name="phcvalue"
                    onChange={(e) => selectPhcValue(e.target.value)}
                  >
                    <option value="" disabled hidden>
                      Select Phc{" "}
                    </option>
                    <option value="Suggnahalli">Suggnahalli</option>
                  </Form.Select>
                </Form.Group>
              </div>
            </Col>

            <Col md={3} className="search-select">
              <div className="pro-select select-topics">
                <Form.Group className="mb-3_drugname">
                  <Form.Label className="pro-label">Select Topics</Form.Label>
                  &nbsp;
                  <Select
                    filterOption={filterOption}
                    options={topicsOptions}
                    onChange={setSelected2}
                    labelledBy="Select"
                    isDisabled={selectedVillage.length == 0 && true}
                  />
                </Form.Group>
              </div>
            </Col>
          </Row>
        </div>
        <Row>
          <Col md={3}>
            <div className="pro-map">
              {!selectTopic && selected1.length == 0 && (
                <p className="pro-text-select">Select Topics for GIS Mapping</p>
              )}
              {selectTopic && (
                <div>
                  <p className="pro-text-select">{selectTopic}</p>
                  <p className="pro-dummy-select">
                    Lorem Ipsum is simply dummy text of the...
                  </p>
                </div>
              )}
              <div className="pro-select">
                <Form.Group className="pro-suggestion">
                  <Autosuggest
                    suggestions={suggestions}
                    onSuggestionsClearRequested={() => setSuggestions([])}
                    onSuggestionsFetchRequested={({ value }) => {
                      setValue(value);
                      setSuggestions(getSuggestions(value));
                    }}
                    getSuggestionValue={(suggestion) => suggestion.name}
                    renderSuggestion={(suggestion) => (
                      <div>
                        <p onClick={gisState}>{suggestion.name}</p>
                      </div>
                    )}
                    type="search"
                    inputProps={{
                      placeholder: "Filter Sub-Topics By Keyword",
                      value: value,
                      onChange: (_, { newValue, method }) => {
                        setValue(newValue);
                      },
                    }}
                    highlightFirstSuggestion={true}
                  />
                  <Button className="searchClr">
                    <i className="bi bi-search"></i>
                  </Button>
                </Form.Group>
                {value && (
                  <Button type="clear" onClick={clearMap} className="topClr">
                    <i className="bi bi-x"></i>
                  </Button>
                )}
                {!selectTopic && selected1.length == 0 && (
                  // {(selected1.length == 0) &&
                  <div className="pro-gis-map">
                    <img src="../img/super/gis-map.png" />
                  </div>
                )}
                {/* {(selected1.length != 0 && !selectTopic) && */}
                {!selectTopic && selected1.length != 0 && (
                  <div className="pro-gis-map">
                    <p className="pro-head-select">
                      Select the Assets to View on Map
                    </p>
                    <input
                      type="checkbox"
                      id="subTopics"
                      name="subTopics"
                      value={subTopicsValue}
                      checked={subTopicsValue}
                      onChange={() => setSubTopicsValue(!subTopicsValue)}
                    />
                    &nbsp; Select All Assets
                    <Row className="assessment-buttons">
                      {villageAsstes.map((subTop, i) => (
                        <Accordion className="sub_topics" key={i}>
                          <Accordion.Item eventKey="0">
                            <Accordion.Header className="vital-acc-head dur-head">
                              <div
                                onClick={(e) =>
                                  setSelectAssets(subTop.groupName)
                                }
                                className={
                                  selectAssets &&
                                  selectAssets === subTop.groupName
                                    ? "selectedTopic"
                                    : "subTopic"
                                }
                              >
                                <p>
                                  {subTop.elements.map((item, index) => (
                                    <span key={index}>
                                      {!item.title == "" && (
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                      )}
                                    </span>
                                  ))}
                                </p>
                                <h4
                                  className="history-date dur-date"
                                  style={{
                                    display: "inline-flex",
                                    alignItems: "center",
                                  }}
                                >
                                  <img
                                    src={gisTopicsImage[i]}
                                    className="sub-topic-image"
                                    alt="Popup header"
                                  />
                                  {subTop.groupName}
                                </h4>
                              </div>
                            </Accordion.Header>
                            {subTop.elements.map((item, index) => (
                              <React.Fragment key={index}>
                                {item.title == "" ? (
                                  ""
                                ) : (
                                  <Accordion.Body className="sub_topic_acc_body">
                                    <p
                                      className={
                                        subAsset && subAsset == item.title
                                          ? "selectedTopic"
                                          : "subTopic"
                                      }
                                      key={index}
                                      onClick={(e) =>
                                        setSelectSubAssets(item.title)
                                      }
                                      style={{ cursor: "pointer" }}
                                    >
                                      {item.title}
                                    </p>
                                  </Accordion.Body>
                                )}
                              </React.Fragment>
                            ))}
                          </Accordion.Item>
                        </Accordion>
                      ))}
                    </Row>
                    {/* <div className="pro-gis-map d-grid gap-2">
                      <Button
                        onClick={(e) => {
                          setFilters([]);
                          setSelectSubTopic([]);
                        }}
                        className="regBtnN"
                      >
                        Clear filters
                      </Button>
                    </div> */}
                  </div>
                )}
                {selectTopic && (
                  <div className="pro-gis-map">
                    <Row className="assessment-buttons">
                      {selectTopic == "Socio-Economic Survey" && (
                        <>
                          {socioDropdown.map((Topics, i) => (
                            <Col lg={6} key={i}>
                              <React.Fragment>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="Topics"
                                  id={Topics._id}
                                  value={Topics.groupName}
                                  checked={Topics.groupName == arrayTopicsValue}
                                  onChange={(e) =>
                                    setArrayTopics(e, Topics.groupName)
                                  }
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={Topics._id}
                                >
                                  <span className={isChecked(arrayTopicsValue)}>
                                    {Topics.groupName}
                                  </span>
                                </label>
                              </React.Fragment>
                            </Col>
                          ))}
                        </>
                      )}
                      {!selectTopic && (
                        <div>
                          <img src="../img/super/gis-level.png" />
                        </div>
                      )}
                      {/* NCD Dropdown */}
                      {selectTopic == "Non-Communicable Diseases" && (
                        <>
                          {ncdarray &&
                            ncdarray.map((subTop, i) => (
                              <div key={i}>
                                <Accordion className="sub_topics">
                                  <Accordion.Item eventKey="0">
                                    <Accordion.Header className="vital-acc-head dur-head">
                                      <i className="bi bi-caret-down-fill down-mark duration"></i>
                                      <h4
                                        className="history-date dur-date"
                                        style={{
                                          display: "inline-flex",
                                          alignItems: "center",
                                        }}
                                      >
                                        {subTop}
                                      </h4>
                                    </Accordion.Header>
                                    {subTop == "Diabetes" && (
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {diabetiessubtopic.map(
                                          (choice, index) => (
                                            <div key={index}>
                                              <Accordion className="sub_topics">
                                                <Accordion.Item eventKey="0">
                                                  <Accordion.Header
                                                    className="vital-acc-head dur-head"
                                                    onClick={(e) =>
                                                      dropDownService(choice)
                                                    }
                                                  >
                                                    {choice}
                                                  </Accordion.Header>
                                                  {choice ==
                                                    "History & Symptoms" && (
                                                    <Accordion.Body className="sub_topic_acc_body">
                                                      {diabetiesdata[0]?.quesOptions.map(
                                                        (subchoice, index) => (
                                                          <div key={index}>
                                                            {subchoice.gisDisplay ==
                                                              true && (
                                                              <p
                                                                className={
                                                                  historySymptoms &&
                                                                  historySymptoms.includes(
                                                                    subchoice.displayName
                                                                  )
                                                                    ? "selectedTopic"
                                                                    : "subTopic"
                                                                }
                                                                onClick={(e) =>
                                                                  handleNcdSubtopic(
                                                                    e,
                                                                    subchoice.displayName
                                                                  )
                                                                }
                                                              >
                                                                {
                                                                  subchoice.displayName
                                                                }
                                                              </p>
                                                            )}
                                                          </div>
                                                        )
                                                      )}
                                                    </Accordion.Body>
                                                  )}
                                                  {choice == "Examination" && (
                                                    <Accordion.Body className="sub_topic_acc_body">
                                                      {examinationdata[0]?.quesOptions.map(
                                                        (choice, index) => (
                                                          <div key={index}>
                                                            {choice.gisDisplay ==
                                                              true && (
                                                              <React.Fragment>
                                                                {choice.choices.map(
                                                                  (
                                                                    subItem,
                                                                    index
                                                                  ) => (
                                                                    <p
                                                                      key={
                                                                        index
                                                                      }
                                                                      className={
                                                                        selectExaminValue &&
                                                                        selectExaminValue.includes(
                                                                          subItem
                                                                        )
                                                                          ? "selectedTopic"
                                                                          : "subTopic"
                                                                      }
                                                                      onClick={(
                                                                        e
                                                                      ) =>
                                                                        handleNcdExaminatioSubtopic(
                                                                          e,
                                                                          subItem
                                                                        )
                                                                      }
                                                                    >
                                                                      {subItem}
                                                                    </p>
                                                                  )
                                                                )}
                                                              </React.Fragment>
                                                            )}
                                                          </div>
                                                        )
                                                      )}
                                                    </Accordion.Body>
                                                  )}
                                                  {choice == "Test" && (
                                                    <Accordion.Body className="sub_topic_acc_body">
                                                      {testdata[0]?.quesOptions.map(
                                                        (choice, index) => (
                                                          <div key={index}>
                                                            {choice.gisDisplay ==
                                                              true && (
                                                              <React.Fragment>
                                                                {choice.choices.map(
                                                                  (
                                                                    subItem,
                                                                    index
                                                                  ) => (
                                                                    <p
                                                                      key={
                                                                        index
                                                                      }
                                                                      className={
                                                                        selectExaminValue &&
                                                                        selectExaminValue.includes(
                                                                          subItem
                                                                        )
                                                                          ? "selectedTopic"
                                                                          : "subTopic"
                                                                      }
                                                                      onClick={(
                                                                        e
                                                                      ) =>
                                                                        handleNcdExaminatioSubtopic(
                                                                          e,
                                                                          subItem
                                                                        )
                                                                      }
                                                                    >
                                                                      {subItem}
                                                                    </p>
                                                                  )
                                                                )}
                                                              </React.Fragment>
                                                            )}
                                                          </div>
                                                        )
                                                      )}
                                                    </Accordion.Body>
                                                  )}
                                                </Accordion.Item>
                                              </Accordion>
                                            </div>
                                          )
                                        )}
                                      </Accordion.Body>
                                    )}
                                  </Accordion.Item>
                                </Accordion>
                              </div>
                            ))}
                        </>
                      )}
                      {/* NCD-Dropdown-End */}
                      {/* HealthID-Start */}
                      {selectTopic == "Health ID" && (
                        <>
                          {healthIdDropdown &&
                            healthIdDropdown.map((subTop, i) => (
                              <div key={i}>
                                {subTop.groupName && (
                                  <Accordion className="sub_topics">
                                    <Accordion.Item eventKey="0">
                                      <Accordion.Header className="vital-acc-head dur-head">
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                        <h4
                                          className="history-date dur-date"
                                          style={{
                                            display: "inline-flex",
                                            alignItems: "center",
                                          }}
                                        >
                                          {subTop.groupName}
                                        </h4>
                                      </Accordion.Header>
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {subTop.elements.map(
                                          (elements, cindex) => (
                                            <p
                                              className={
                                                selectSubTopic &&
                                                selectSubTopic.includes()
                                                  ? "selectedTopic"
                                                  : "subTopic"
                                              }
                                              key={cindex}
                                              onClick={(e) =>
                                                handleSubtopic(
                                                  e,
                                                  item,
                                                  elements.title
                                                )
                                              }
                                            >
                                              {elements.title}
                                            </p>
                                          )
                                        )}
                                      </Accordion.Body>
                                    </Accordion.Item>
                                  </Accordion>
                                )}
                              </div>
                            ))}
                        </>
                      )}
                      {/* HealthID-End */}
                      {/* RMNCHA-Start */}
                      {selectTopic == "RMNCH+A" && (
                        <>
                          {rmnchaDropdown &&
                            rmnchaDropdown.map((subTop, i) => (
                              <div key={i}>
                                {subTop.groupName && (
                                  <Accordion className="sub_topics">
                                    <Accordion.Item eventKey="0">
                                      <Accordion.Header className="vital-acc-head dur-head">
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                        <h4
                                          className="history-date dur-date"
                                          style={{
                                            display: "inline-flex",
                                            alignItems: "center",
                                          }}
                                        >
                                          {subTop.groupName}
                                        </h4>
                                      </Accordion.Header>
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {subTop.elements.map(
                                          (elements, cindex) => (
                                            <p
                                              className={
                                                selectSubTopic &&
                                                selectSubTopic.includes()
                                                  ? "selectedTopic"
                                                  : "subTopic"
                                              }
                                              key={cindex}
                                              onClick={(e) =>
                                                handlermncaSubtopic(
                                                  e,
                                                  item,
                                                  elements.title
                                                )
                                              }
                                            >
                                              {elements.title}
                                            </p>
                                          )
                                        )}
                                      </Accordion.Body>
                                    </Accordion.Item>
                                  </Accordion>
                                )}
                              </div>
                            ))}
                        </>
                      )}
                      {/* RMNCH+A END */}
                      {/* healthandwellness-start */}
                      {selectTopic == "Health & Wellness Surveillance" && (
                        <>
                          {healthandwellnessDropdown &&
                            healthandwellnessDropdown.map((subTop, i) => (
                              <div key={i}>
                                {subTop.groupName && (
                                  <Accordion className="sub_topics">
                                    <Accordion.Item eventKey="0">
                                      <Accordion.Header className="vital-acc-head dur-head">
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                        <h4
                                          className="history-date dur-date"
                                          style={{
                                            display: "inline-flex",
                                            alignItems: "center",
                                          }}
                                        >
                                          {subTop.groupName}
                                        </h4>
                                      </Accordion.Header>
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {subTop.elements.map(
                                          (elements, cindex) => (
                                            <p
                                              className={
                                                selectSubTopic &&
                                                selectSubTopic.includes()
                                                  ? "selectedTopic"
                                                  : "subTopic"
                                              }
                                              key={cindex}
                                              onClick={(e) =>
                                                handlehealthandwellnessSubtopic(
                                                  e,
                                                  item,
                                                  elements.title
                                                )
                                              }
                                            >
                                              {elements.title}
                                            </p>
                                          )
                                        )}
                                      </Accordion.Body>
                                    </Accordion.Item>
                                  </Accordion>
                                )}
                              </div>
                            ))}
                        </>
                      )}
                      {/* healthandwellness-End */}
                      {/* communicable-start */}
                      {selectTopic == "Communicable Diseases" && (
                        <>
                          {communicableDropdown &&
                            communicableDropdown.map((subTop, i) => (
                              <div key={i}>
                                {subTop.groupName && (
                                  <Accordion className="sub_topics">
                                    <Accordion.Item eventKey="0">
                                      <Accordion.Header className="vital-acc-head dur-head">
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                        <h4
                                          className="history-date dur-date"
                                          style={{
                                            display: "inline-flex",
                                            alignItems: "center",
                                          }}
                                        >
                                          {subTop.groupName}
                                        </h4>
                                      </Accordion.Header>
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {subTop.elements.map(
                                          (elements, cindex) => (
                                            <p
                                              className={
                                                selectSubTopic &&
                                                selectSubTopic.includes()
                                                  ? "selectedTopic"
                                                  : "subTopic"
                                              }
                                              key={cindex}
                                              onClick={(e) =>
                                                handlecommunicableSubtopic(
                                                  e,
                                                  item,
                                                  elements.title
                                                )
                                              }
                                            >
                                              {elements.title}
                                            </p>
                                          )
                                        )}
                                      </Accordion.Body>
                                    </Accordion.Item>
                                  </Accordion>
                                )}
                              </div>
                            ))}
                        </>
                      )}
                      {/* communicable-End */}
                      {/* EMR-start */}
                      {selectTopic == "EMR" && (
                        <>
                          {emrDropdown &&
                            emrDropdown.map((subTop, i) => (
                              <div key={i}>
                                {subTop.groupName && (
                                  <Accordion className="sub_topics">
                                    <Accordion.Item eventKey="0">
                                      <Accordion.Header className="vital-acc-head dur-head">
                                        <i className="bi bi-caret-down-fill down-mark duration"></i>
                                        <h4
                                          className="history-date dur-date"
                                          style={{
                                            display: "inline-flex",
                                            alignItems: "center",
                                          }}
                                        >
                                          {subTop.groupName}
                                        </h4>
                                      </Accordion.Header>
                                      <Accordion.Body className="sub_topic_acc_body">
                                        {subTop.elements.map(
                                          (elements, cindex) => (
                                            <p
                                              className={
                                                selectEMRSubtopic &&
                                                selectEMRSubtopic.includes(
                                                  elements.title
                                                )
                                                  ? "selectedTopic"
                                                  : "subTopic"
                                              }
                                              key={cindex}
                                              onClick={(e) =>
                                                handleEMRSubtopic(
                                                  e,
                                                  subTop.groupName,
                                                  elements.title
                                                )
                                              }
                                            >
                                              {elements.title}
                                            </p>
                                          )
                                        )}
                                      </Accordion.Body>
                                    </Accordion.Item>
                                  </Accordion>
                                )}
                              </div>
                            ))}
                        </>
                      )}
                      {/* EMR-End */}
                      {arrayTopicsValue && (
                        <div>
                          <div>
                            <p className="pro-head-select">
                              Select the Sub-Topics
                            </p>
                            {/* Socio-Economic-Dropdowns*/}
                            {selectTopic == "Socio-Economic Survey" && (
                              <>
                                {houseHoldDropdown &&
                                  houseHoldDropdown.map((subTop, i) => (
                                    <div key={i}>
                                      {subTop?.quesOptions.map(
                                        (item, index) => (
                                          <div key={index}>
                                            {item.displayName && (
                                              <Accordion className="sub_topics">
                                                <Accordion.Item eventKey="0">
                                                  <Accordion.Header className="vital-acc-head dur-head">
                                                    <i className="bi bi-caret-down-fill down-mark duration"></i>
                                                    <h4
                                                      className="history-date dur-date"
                                                      style={{
                                                        display: "inline-flex",
                                                        alignItems: "center",
                                                      }}
                                                    >
                                                      {/* <img
                                                  src={
                                                    item.displayName ==
                                                    "Types of Family"
                                                      ? "../../img/gis-map/Vector (6).png"
                                                      : item.displayName ==
                                                        "HH Head Religion"
                                                      ? "../../img/gis-map/Vector (2).png"
                                                      : item.displayName ==
                                                        "Physical Location of HH"
                                                      ? "../../img/gis-map/Vector (5).png"
                                                      : item.displayName ==
                                                        "House Ownership"
                                                      ? "../../img/gis-map/Vector (3).png"
                                                      : item.displayName ==
                                                        "Type of House"
                                                      ? "../../img/gis-map/Popup header.png"
                                                      : item.displayName ==
                                                        "Availability of electricity"
                                                      ? "../../img/gis-map/Vector.png"
                                                      : item.displayName ==
                                                        "Kitchen Facilities"
                                                      ? "../../img/gis-map/Vector (4).png"
                                                      : item.displayName ==
                                                          "Drainage system" &&
                                                        "../../img/gis-map/Vector (1).png"
                                                  } */}
                                                      {/* className="sub-topic-image"
                                                  // <img src={subTopicImage[index]} className="sub-topic-image"

                                                  alt="Popup header"
                                                />{" "} */}
                                                      {item.displayName}
                                                    </h4>
                                                  </Accordion.Header>
                                                  <Accordion.Body className="sub_topic_acc_body">
                                                    {item.choices.map(
                                                      (choice, cindex) => (
                                                        <p
                                                          className={
                                                            selectSubTopic &&
                                                            selectSubTopic.includes(
                                                              choice
                                                            )
                                                              ? "selectedTopic"
                                                              : "subTopic"
                                                          }
                                                          key={cindex}
                                                          onClick={(e) =>
                                                            handleSubtopic(
                                                              e,
                                                              item,
                                                              choice
                                                            )
                                                          }
                                                        >
                                                          {choice}
                                                        </p>
                                                      )
                                                    )}
                                                  </Accordion.Body>
                                                </Accordion.Item>
                                              </Accordion>
                                            )}
                                          </div>
                                        )
                                      )}
                                    </div>
                                  ))}
                              </>
                            )}
                            {/* Socio-Economic-Dropdown-End */}
                          </div>
                          {/* <div className="pro-gis-map d-grid gap-2">
                            <Button
                              onClick={(e) => {
                                setSelectSubTopic([]);
                                setFilters([]);
                              }}
                              className="regBtnN"
                            >
                              Clear filters
                            </Button>
                          </div> */}
                        </div>
                      )}
                    </Row>
                    <div className="pro-gis-map d-grid gap-2">
                      <Button
                        onClick={(e) => clearFilter()}
                        className="regBtnN"
                      >
                        Clear filters
                      </Button>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </Col>
          <Col md={9}>
            <div
              style={{ height: "80vh", width: "100%", marginBottom: "40px" }}
              id="map"
            >
              <MyMapComponent />
            </div>
          </Col>
        </Row>
      </div>
    </React.Fragment>
  );
}
