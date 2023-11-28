import React, { useState, useRef, useEffect } from "react";
import { Col, Row, Form, Button } from "react-bootstrap";
import moment from "moment";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import { useDispatch, useSelector } from "react-redux";
import { loadPhyExamItems } from "../../../../redux/formUtilityAction";
import SaveButton from "../../../EMR_Buttons/SaveButton";

function PhysicalExamination(props) {
  let dispatch = useDispatch();
  const { phyExamItems } = useSelector((state) => state.formData);

  const UHID = props.vitalsPatientData?.Patient?.UHId;
  const EncID = props.vitalsPatientData?.encounterId;
  const PatName = props.vitalsPatientData?.Patient?.name;
  const PatHeaId = props.vitalsPatientData?.Patient?.healthId;
  const PatGen = props.vitalsPatientData?.Patient?.gender;
  const PatDob = props.vitalsPatientData?.Patient?.dob;
  const PatMob = props.vitalsPatientData?.Patient?.phone;
  const patId = props.vitalsPatientData?.Patient?.patientId;
  const docName = props.vitalsPatientData?.Provider?.name;

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  const [checkValue1, setCheckbox] = useState(false);
  const [pupilDesc, setPupilDesc] = useState("");
  const [pupilReaction, setPupilReaction] = useState("");
  const [pupilSize, setPupilSize] = useState("");
  const [pupilComments, setPupilComments] = useState("");
  const [pupilRightDesc, setPupilRightDesc] = useState("");
  const [pupilRightReaction, setPupilRightReaction] = useState("");
  const [pupilRightSize, setPupilRightSize] = useState("");
  const [pupilRightComments, setPupilRightComments] = useState("");

  const [bowelMove, setBowelMove] = useState("");
  const [luqComments, setLuqComments] = useState("");
  const [llqComments, setLlqComments] = useState("");
  const [ruqComments, setRuqComments] = useState("");
  const [rlqComments, setRlqComments] = useState("");

  const [checked, setChecked] = useState([]);

  // set state value for title

  const [phyExamId, setPhyExamId] = useState("");
  // set state value for title

  const [status, setStatus] = useState("");

  // Generate string of checked items
  const checkedItems = checked.length
    ? checked.reduce((total, item) => {
        return total + ", " + item;
      })
    : "";

  useEffect(() => {
    dispatch(loadPhyExamItems());
  }, []);

  // set valuse for check boxes
  const [cadiacVascularData, setCadiacVascularData] = useState([]);

  const setCardiacValue = (e) => {
    if (e.target.checked) {
      setCadiacVascularData([...cadiacVascularData, e.target.value]);
    } else {
      setCadiacVascularData(
        cadiacVascularData.filter((id) => id !== e.target.value)
      );
    }
  };

  const [heartRhythmValue, setHeartRhythmValue] = useState("");
  const setHeartRhythm = (e, name) => {
    if (e.target.checked) {
      setHeartRhythmValue(name);
    } else {
      setHeartRhythmValue("");
    }
  };

  const [nailColourValue, setNailColourValue] = useState("");
  // const [isCheckChecked, setIsCheckChecked] = useState(false)
  const setNailColour = (e, name) => {
    if (e.target.checked) {
      setNailColourValue(name);
    } else {
      setNailColourValue("");
    }
  };

  const [aspirationRiskValue, setAspirationRiskValue] = useState([]);
  const setAspirationRisk = (e) => {
    if (e.target.checked) {
      setAspirationRiskValue([...aspirationRiskValue, e.target.value]);
    } else {
      setAspirationRiskValue(
        aspirationRiskValue.filter((id) => id !== e.target.value)
      );
    }
  };

  const [facialSummeryValue, setFacialSummeryValue] = useState([]);
  const setFacialSummery = (e) => {
    if (e.target.checked) {
      setFacialSummeryValue([...facialSummeryValue, e.target.value]);
    } else {
      setFacialSummeryValue(
        facialSummeryValue.filter((id) => id !== e.target.value)
      );
    }
  };

  const [characteristicValue, setCharacteristicValue] = useState("");
  const setCharacteristic = (e, name) => {
    if (e.target.checked) {
      setCharacteristicValue(name);
    } else {
      setCharacteristicValue("");
    }
  };

  const [gaitValue, setGaitValue] = useState([]);
  const setGait = (e) => {
    if (e.target.checked) {
      setGaitValue([...gaitValue, e.target.value]);
    } else {
      setGaitValue(gaitValue.filter((id) => id !== e.target.value));
    }
  };

  const [concentrationValue, setConcentrationValue] = useState("");
  const setConcentration = (e, name) => {
    if (e.target.checked) {
      setConcentrationValue(name);
    } else {
      setConcentrationValue("");
    }
  };

  //  Genitors urinary assessment
  const [urineColorValue, setUrineColorValue] = useState("");
  const setUrineColor = (e, name) => {
    if (e.target.checked) {
      setUrineColorValue(name);
    } else {
      setUrineColorValue("");
    }
  };

  const [urineDescriptionValue, setUrineDescriptionValue] = useState("");
  const setUrineDescription = (e, name) => {
    if (e.target.checked) {
      setUrineDescriptionValue(name);
    } else {
      setUrineDescriptionValue("");
    }
  };

  const [symptomsValue, setSymptomsValue] = useState("");
  const setUrineSymptoms = (e, name) => {
    if (e.target.checked) {
      setSymptomsValue(name);
    } else {
      setSymptomsValue("");
    }
  };

  //  skin integration
  const [integrationValue, setIntegrationValue] = useState("");
  const setSkinIntegration = (e, name) => {
    if (e.target.checked) {
      setIntegrationValue(name);
    } else {
      setIntegrationValue("");
    }
  };

  const [skinColorValue, setSkinColorValue] = useState("");
  const setSkinColor = (e, name) => {
    if (e.target.checked) {
      setSkinColorValue(name);
    } else {
      setSkinColorValue("");
    }
  };

  const [mucusDesValue, setMucusDesValue] = useState("");
  const setMucusDescription = (e, name) => {
    if (e.target.checked) {
      setMucusDesValue(name);
    } else {
      setMucusDesValue("");
    }
  };

  // Respiration
  const [respirationValue, setRespirationValue] = useState([]);
  const setRespiration = (e) => {
    if (e.target.checked) {
      setRespirationValue([...respirationValue, e.target.value]);
    } else {
      setRespirationValue(
        respirationValue.filter((id) => id !== e.target.value)
      );
    }
  };

  const [patternValue, setPatternValue] = useState("");
  const setResPattern = (e, name) => {
    if (e.target.checked) {
      setPatternValue(name);
    } else {
      setPatternValue("");
    }
  };

  const [coughValue, setCoughValue] = useState("");
  const setCough = (e, name) => {
    if (e.target.checked) {
      setCoughValue(name);
    } else {
      setCoughValue("");
    }
  };

  const [speColorValue, setSpeColorValue] = useState("");

  const setSpeColor = (e, name) => {
    if (e.target.checked) {
      setSpeColorValue(name);
    } else {
      setSpeColorValue("");
    }
  };

  const [amountValue, setAmountValue] = useState("");
  const setSpeAmount = (e, name) => {
    if (e.target.checked) {
      setAmountValue(name);
    } else {
      setAmountValue("");
    }
  };

  // Gastrointestinal assessment form
  const [gastSymptomsValue, setGastSymptomsValue] = useState([]);
  const setGastroSymptoms = (e) => {
    if (e.target.checked) {
      setGastSymptomsValue([...gastSymptomsValue, e.target.value]);
    } else {
      setGastSymptomsValue(
        gastSymptomsValue.filter((id) => id !== e.target.value)
      );
    }
  };

  const [abdomenValue, setAbdomenValue] = useState("");
  const setAbdomenAppearance = (e, name) => {
    if (e.target.checked) {
      setAbdomenValue(name);
    } else {
      setAbdomenValue("");
    }
  };

  const [palpationValue, setPalpationValue] = useState("");
  const setAbdomenPalpation = (e, name) => {
    if (e.target.checked) {
      setPalpationValue(name);
    } else {
      setPalpationValue("");
    }
  };

  const [stoolColorValue, setStoolColorValue] = useState("");
  const setStoolColor = (e, name) => {
    if (e.target.checked) {
      setStoolColorValue(name);
    } else {
      setStoolColorValue("");
    }
  };

  const [luqValue, setLuqValue] = useState("");
  const setLuq = (e, name) => {
    if (e.target.checked) {
      setLuqValue(name);
    } else {
      setLuqValue("");
    }
  };

  const [ruqValue, setRuqValue] = useState("");
  const setRuq = (e, name) => {
    if (e.target.checked) {
      setRuqValue(name);
    } else {
      setRuqValue("");
    }
  };

  const [llqValue, setLlqValue] = useState("");
  const setLlq = (e, name) => {
    if (e.target.checked) {
      setLlqValue(name);
    } else {
      setLlqValue("");
    }
  };

  const [rlqValue, setRlqValue] = useState("");
  const setRlq = (e, name) => {
    if (e.target.checked) {
      setRlqValue(name);
    } else {
      setRlqValue("");
    }
  };

  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const PresentTime = new Date();
  let parsedDate = moment(PresentTime, "DD.MM.YYYY H:mm:ss");
  let EffectiveDate = parsedDate.toISOString();
  let cardioAssess;
  if (cadiacVascularData || heartRhythmValue || nailColourValue) {
    cardioAssess = "Cardio Vascular Assessment";
  } else {
    cardioAssess = "";
  }

  let neuroAssess;
  if (
    aspirationRiskValue ||
    facialSummeryValue ||
    checkValue1 == true ||
    pupilDesc ||
    pupilReaction ||
    pupilSize ||
    pupilComments ||
    pupilRightDesc ||
    pupilRightReaction ||
    pupilRightSize ||
    pupilRightComments ||
    characteristicValue ||
    gaitValue ||
    concentrationValue
  ) {
    neuroAssess = "Neurological assessment";
  } else {
    neuroAssess = "";
  }

  let genAssess;
  if (urineColorValue || urineDescriptionValue || symptomsValue) {
    genAssess = "Genitors urinary assessment";
  } else {
    genAssess = "";
  }

  let skinAssess;
  if (integrationValue || skinColorValue || mucusDesValue) {
    skinAssess = "Skin assessment";
  } else {
    skinAssess = "";
  }

  let respAssess;
  if (
    respirationValue ||
    patternValue ||
    coughValue ||
    speColorValue ||
    amountValue
  ) {
    respAssess = "Respiratory assessment";
  } else {
    respAssess = "";
  }

  let gastroAssess;
  if (
    gastSymptomsValue ||
    abdomenValue ||
    palpationValue ||
    stoolColorValue ||
    bowelMove ||
    rlqValue ||
    luqValue ||
    llqValue ||
    ruqValue
  ) {
    gastroAssess = "Gastrointestinal assessment";
  } else {
    gastroAssess = "";
  }

  let assessmentValue = [
    {
      assessmentTitle: cardioAssess,
      observations: [
        {
          observationName: "Cardiac Rhythm",
          observationValues: cadiacVascularData,
        },
        {
          observationName: "Heart rhythm",
          observationValues: heartRhythmValue,
        },
        {
          observationName: "Nail bed colour",
          observationValues: nailColourValue,
        },
      ],
    },
    {
      assessmentTitle: neuroAssess,
      observations: [
        {
          observationName: "Aspiration risk",
          observationValues: aspirationRiskValue,
        },
        {
          observationName: "Facial symmetry",
          observationValues: facialSummeryValue,
        },
        {
          observationName: "Perrla",
          observationValues: checkValue1,
        },
        {
          observationName: "Pupil left",
          observationValues: [
            pupilDesc || "",
            pupilReaction || "",
            pupilSize || "",
            pupilComments || "",
          ],
        },
        {
          observationName: "Pupil right",
          observationValues: [
            pupilRightDesc || "",
            pupilRightReaction || "",
            pupilRightSize || "",
            pupilRightComments || "",
          ],
        },
        {
          observationName: "Characteristic speech",
          observationValues: characteristicValue,
        },
        {
          observationName: "Gait",
          observationValues: gaitValue,
        },
        {
          observationName: "Loss of concentration",
          observationValues: concentrationValue,
        },
      ],
    },
    {
      assessmentTitle: genAssess,
      observations: [
        {
          observationName: "Urine colour",
          observationValues: urineColorValue,
        },
        {
          observationName: "Urine description",
          observationValues: urineDescriptionValue,
        },
        {
          observationName: "Urinary symptoms",
          observationValues: symptomsValue,
        },
      ],
    },
    {
      assessmentTitle: skinAssess,
      observations: [
        {
          observationName: "Skin integration",
          observationValues: integrationValue,
        },
        {
          observationName: "Mucus membrane color",
          observationValues: skinColorValue,
        },
        {
          observationName: "Mucus membrane description",
          observationValues: mucusDesValue,
        },
      ],
    },
    {
      assessmentTitle: respAssess,
      observations: [
        {
          observationName: "Respiration",
          observationValues: respirationValue,
        },
        {
          observationName: "Respiratory pattern",
          observationValues: patternValue,
        },
        {
          observationName: "Cough",
          observationValues: coughValue,
        },
        {
          observationName: "Sputum colour",
          observationValues: speColorValue,
        },
        {
          observationName: "Sputum amount",
          observationValues: amountValue,
        },
      ],
    },
    {
      assessmentTitle: gastroAssess,
      observations: [
        {
          observationName: "Symptoms",
          observationValues: gastSymptomsValue,
        },
        {
          observationName: "Abdomen appearance",
          observationValues: abdomenValue,
        },
        {
          observationName: "Abdomen palpation",
          observationValues: palpationValue,
        },
        {
          observationName: "Stool color",
          observationValues: stoolColorValue,
        },
        {
          observationName: "Last bowel movement",
          observationValues: bowelMove,
        },
        {
          observationName: "RLQ",
          observationValues: [rlqValue, rlqComments],
        },
        {
          observationName: "LUQ",
          observationValues: [luqValue, luqComments],
        },
        {
          observationName: "LLQ",
          observationValues: [llqValue, llqComments],
        },
        {
          observationName: "RUQ",
          observationValues: [ruqValue, ruqComments],
        },
      ],
    },
  ];

  //submit observatio data
  const postobservation = () => {
    const observation_data = {
      UHId: UHID,
      encounterId: EncID,
      effectiveDate: EffectiveDate,
      patientId: patId,
      doctor: docName,
      type: "Physical Examination",
      assessments: assessmentValue,
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(observation_data),
    };

    fetch(`${constant.ApiUrl}/Observations`, requestOptions)
      .then((res) => res.json())

      .then((res) => {
        setStatus(true);
        if (res.status == 401) {
          alert("your data is not submited");
        } else {
          alert("your data is submited successfully");
        }
      });
  };

  // Update Observation value
  const updateobservation = (e) => {
    const observation_update = {
      doctor: docName,
      assessments: assessmentValue,
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(observation_update),
    };

    fetch(`${constant.ApiUrl}/Observations/${phyExamId}`, requestOptions)
      .then((res) => res.json())

      .then((res) => {
        setStatus(true);
        if (res.status == 401) {
          alert("your data is not submited");
        } else {
          alert("your data is Updated successfully");
        }
      });
  };
  // Update Observation value

  // back to home
  const [visible, setVisible] = useState(false);

  const toggleVisible = () => {
    const scrolled = document.documentElement.scrollTop;
    if (scrolled > 300) {
      setVisible(true);
    } else if (scrolled <= 300) {
      setVisible(false);
    }
  };

  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
      /* you can also use 'auto' behaviour
               in place of 'smooth' */
    });
  };

  window.addEventListener("scroll", toggleVisible);
  // back to home

  // view previous history modal
  const [physicalExaminationShow, setPhysicalExaminationShow] = useState(false);
  const physicalClose = () => setPhysicalExaminationShow(false);
  const physicalShow = () => setPhysicalExaminationShow(true);
  // view previous history modal

  // scroll navaigation
  const skin_form = useRef(null);
  const genitory_form = useRef(null);
  const gastro_form = useRef(null);
  const neuro_form = useRef(null);
  const respiratory_form = useRef(null);
  const cardio_form = useRef(null);
  const scrollToSection = (elementRef) => {
    window.scrollTo({
      top: elementRef.current.offsetTop,
      behavior: "smooth",
    });
  };
  // scroll navaigation

  // fetching data by encounter id
  useEffect(() => {
    if (EncID != undefined) {
      fetch(
        `${constant.ApiUrl}/Observations/filter?page=&size=&type=Physical Examination&encounterId=${EncID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res.data.length != 0) {
            setPhyExamId(res["data"][0]["_id"]);
            let phyArray = [];
            let tempObser = [];
            let tempObserHeart = [];
            let tempObserNail = [];
            let tempObserAspiration = [];
            let tempObserFacial = [];
            let tempObserCharacteristic = [];
            let tempObserGait = [];
            let tempObseConc = [];
            let tempObserPerral = [];
            let tempObserUColor = [];
            let tempObserUDesc = [];
            let tempObserUSym = [];
            let tempObserSInt = [];
            let tempObserMMColor = [];
            let tempObserMMDesc = [];
            let tempObserResp = [];
            let tempObserRespPattern = [];
            let tempObserCough = [];
            let tempObserSpuColor = [];
            let tempObserSpuAmount = [];
            let tempObserGasSym = [];
            let tempObserAbApper = [];
            let tempObserAbPalp = [];
            let tempObserStCoolr = [];
            let tempObserRlq = [];
            let tempObserLuq = [];
            let tempObserLlq = [];
            let tempObserRuq = [];

            for (let i = 0; i < res["data"][0]["assessments"].length; i++) {
              phyArray.push(res["data"][0]["assessments"][i]);

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Cardio Vascular Assessment"
              ) {
                for (
                  let j = 0;
                  j < res["data"][0]["assessments"][i]["observations"].length;
                  j++
                ) {
                  // phyobserveArray.push(phyArray[0]['observations'][j]);
                  // phyNuroArray.push(phyArray[1]['observations'][j]);
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Cardiac Rhythm"
                  ) {
                    setCadiacVascularData(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObser.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Heart rhythm"
                  ) {
                    setHeartRhythmValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserHeart.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Nail bed colour"
                  ) {
                    setNailColourValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserNail.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                }
              }

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Neurological assessment"
              ) {
                for (
                  let j = 0;
                  j < res["data"][0]["assessments"][i]["observations"].length;
                  j++
                ) {
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Aspiration risk"
                  ) {
                    setAspirationRiskValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserAspiration.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Facial symmetry"
                  ) {
                    setFacialSummeryValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserFacial.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName == "Perrla"
                  ) {
                    // setCadiacVascularData(phyArray[0]['observations'][j].observationValues)
                    tempObserPerral.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Pupil left"
                  ) {
                    // setCadiacVascularData(phyArray[0]['observations'][j].observationValues)
                    setPupilDesc(
                      phyArray[i]["observations"][j].observationValues[0]
                    );
                    setPupilReaction(
                      phyArray[i]["observations"][j].observationValues[1]
                    );
                    setPupilSize(
                      phyArray[i]["observations"][j].observationValues[2]
                    );
                    setPupilComments(
                      phyArray[i]["observations"][j].observationValues[3]
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Pupil right"
                  ) {
                    // setCadiacVascularData(phyArray[0]['observations'][j].observationValues)
                    setPupilRightDesc(
                      phyArray[i]["observations"][j].observationValues[0]
                    );
                    setPupilRightReaction(
                      phyArray[i]["observations"][j].observationValues[1]
                    );
                    setPupilRightSize(
                      phyArray[i]["observations"][j].observationValues[2]
                    );
                    setPupilRightComments(
                      phyArray[i]["observations"][j].observationValues[3]
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Characteristic speech"
                  ) {
                    setCharacteristicValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserCharacteristic.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName == "Gait"
                  ) {
                    setGaitValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObserGait.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][j].observationName ==
                    "Loss of concentration"
                  ) {
                    setConcentrationValue(
                      phyArray[i]["observations"][j].observationValues
                    );
                    tempObseConc.push(
                      phyArray[i]["observations"][j].observationValues
                    );
                  }
                }
              }

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Genitors urinary assessment"
              ) {
                for (
                  let k = 0;
                  k < res["data"][0]["assessments"][i]["observations"].length;
                  k++
                ) {
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Urine colour"
                  ) {
                    setUrineColorValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserUColor.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Urine description"
                  ) {
                    setUrineDescriptionValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserUDesc.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Urinary symptoms"
                  ) {
                    setSymptomsValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserUSym.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                }
              }

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Skin assessment"
              ) {
                for (
                  let k = 0;
                  k < res["data"][0]["assessments"][i]["observations"].length;
                  k++
                ) {
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Skin integration"
                  ) {
                    setIntegrationValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserSInt.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Mucus membrane color"
                  ) {
                    setSkinColorValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserMMColor.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Mucus membrane description"
                  ) {
                    setMucusDesValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserMMDesc.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                }
              }

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Respiratory assessment"
              ) {
                for (
                  let k = 0;
                  k < res["data"][0]["assessments"][i]["observations"].length;
                  k++
                ) {
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Respiration"
                  ) {
                    setRespirationValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserResp.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Respiratory pattern"
                  ) {
                    setPatternValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserRespPattern.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName == "Cough"
                  ) {
                    setCoughValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserCough.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Sputum colour"
                  ) {
                    setSpeColorValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserSpuColor.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Sputum amount"
                  ) {
                    setAmountValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserSpuAmount.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                }
              }

              if (
                res["data"][0]["assessments"][i]["assessmentTitle"] ==
                "Gastrointestinal assessment"
              ) {
                for (
                  let k = 0;
                  k < res["data"][0]["assessments"][i]["observations"].length;
                  k++
                ) {
                  if (
                    phyArray[i]["observations"][k].observationName == "Symptoms"
                  ) {
                    setGastSymptomsValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserGasSym.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Abdomen appearance"
                  ) {
                    setAbdomenValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserAbApper.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Abdomen palpation"
                  ) {
                    setPalpationValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserAbPalp.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Stool color"
                  ) {
                    setStoolColorValue(
                      phyArray[i]["observations"][k].observationValues
                    );
                    tempObserStCoolr.push(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (
                    phyArray[i]["observations"][k].observationName ==
                    "Last bowel movement"
                  ) {
                    // setCadiacVascularData(phyArray[0]['observations'][j].observationValues)
                    setBowelMove(
                      phyArray[i]["observations"][k].observationValues
                    );
                  }
                  if (phyArray[i]["observations"][k].observationName == "RLQ") {
                    setRlqValue(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    tempObserRlq.push(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    setRlqComments(
                      phyArray[i]["observations"][k].observationValues[1]
                    );
                  }
                  if (phyArray[i]["observations"][k].observationName == "LUQ") {
                    setLuqValue(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    tempObserLuq.push(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    setLuqComments(
                      phyArray[i]["observations"][k].observationValues[1]
                    );
                  }
                  if (phyArray[i]["observations"][k].observationName == "LLQ") {
                    setLlqValue(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    tempObserLlq.push(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    setLlqComments(
                      phyArray[i]["observations"][k].observationValues[1]
                    );
                  }
                  if (phyArray[i]["observations"][k].observationName == "RUQ") {
                    setRuqValue(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    tempObserRuq.push(
                      phyArray[i]["observations"][k].observationValues[0]
                    );
                    setRuqComments(
                      phyArray[i]["observations"][k].observationValues[1]
                    );
                  }
                }
              }
            }

            let allPhy = [];
            let phyElement = [];
            for (var i = 0; i < phyExamItems.length; i++) {
              allPhy.push(phyExamItems[i]);
              for (var j = 0; j < allPhy[i].elements.length; j++) {
                phyElement.push(allPhy[i].elements[j]);
              }
            }

            var ele = document.getElementsByName("cariac");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObser.length; j++) {
                if (tempObser[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("heart");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserHeart.length; j++) {
                if (tempObserHeart[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("nail");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserNail.length; j++) {
                if (tempObserNail[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("asp-risk");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserAspiration.length; j++) {
                if (tempObserAspiration[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("facial");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserFacial.length; j++) {
                if (tempObserFacial[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            for (var i = 0; i < tempObserPerral.length; i++) {
              if (tempObserPerral[i] == "true") {
                setCheckbox(true);
              } else {
                setCheckbox(false);
              }
            }
            var ele = document.getElementsByName("speech");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserCharacteristic.length; j++) {
                if (tempObserCharacteristic[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("gait");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserGait.length; j++) {
                if (tempObserGait[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("conce");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObseConc.length; j++) {
                if (tempObseConc[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("urine-color");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserUColor.length; j++) {
                if (tempObserUColor[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("urine-description");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserUDesc.length; j++) {
                if (tempObserUDesc[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("symptoms");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserUSym.length; j++) {
                if (tempObserUSym[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("integration");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserSInt.length; j++) {
                if (tempObserSInt[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("skin-color");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserMMColor.length; j++) {
                if (tempObserMMColor[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("skin-desc");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserMMDesc.length; j++) {
                if (tempObserMMDesc[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("respiration");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserResp.length; j++) {
                if (tempObserResp[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("respattern");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserRespPattern.length; j++) {
                if (tempObserRespPattern[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("cough");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserCough.length; j++) {
                if (tempObserCough[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("spec-color");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserSpuColor.length; j++) {
                if (tempObserSpuColor[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("sput-amnt");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserSpuAmount.length; j++) {
                if (tempObserSpuAmount[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("gassymptoms");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserGasSym.length; j++) {
                if (tempObserGasSym[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("abd-apear");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserAbApper.length; j++) {
                if (tempObserAbApper[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("abd-pupl");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserAbPalp.length; j++) {
                if (tempObserAbPalp[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("stool-color");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserStCoolr.length; j++) {
                if (tempObserStCoolr[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("rlq");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserRlq.length; j++) {
                if (
                  ele[i].type == "radio" &&
                  tempObserRlq[j].includes(phyElement[i].title)
                ) {
                  ele[i].checked = true;
                }
              }
            }

            var ele = document.getElementsByName("luq");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserLuq.length; j++) {
                if (tempObserLuq[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("llq");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserLlq.length; j++) {
                if (tempObserLlq[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
            var ele = document.getElementsByName("ruq");
            for (var i = 0; i < ele.length; i++) {
              for (var j = 0; j < tempObserRuq.length; j++) {
                if (tempObserRuq[j].includes(phyElement[i].title)) {
                  ele[i].checked = true;
                }
              }
            }
          }
        });
    }
  }, [EncID, status]);
  // fetching data by encounter id
  return (
    <React.Fragment>
      <ViewModalPopups
        chiefClose={physicalClose}
        cheifShow={physicalExaminationShow}
        PatName={PatName}
        physicalUHID={UHID}
        PatHeaId={PatHeaId}
        PatGen={PatGen}
        PatDob={PatDob}
        PatMob={PatMob}
        physicalEncId={EncID}
        dateto={dateto}
        datefrom={datefrom}
      />
      <div>
        <Row>
          <Col md={8}>
            <h4 className="psy-head">Physical Examination</h4>
          </Col>
          <Col md={4} className="float-end">
            <div className="history-btn-div">
              <div>
                {phyExamId ? (
                  <SaveButton
                    class_name="regBtnN"
                    butttonClick={(e) => updateobservation(phyExamId)}
                    button_name="Update"
                  />
                ) : (
                  <SaveButton
                    class_name="regBtnN"
                    butttonClick={postobservation}
                    button_name="Save"
                  />
                )}
              </div>
              <Button
                varient="light"
                className="view-prev-details"
                onClick={physicalShow}
              >
                <i className="fa fa-undo prev-icon"></i>
                Previous Physical Examination
              </Button>
            </div>
          </Col>
        </Row>
        <div className="my-navigate">
          <span className="go-assessment">Go To Assessment:</span>
          <ul className="assessment">
            <li onClick={() => scrollToSection(skin_form)} className="mytab">
              Skin
            </li>
            <li
              onClick={() => scrollToSection(genitory_form)}
              className="mytab"
            >
              Genitors Urinary
            </li>
            <li onClick={() => scrollToSection(gastro_form)} className="mytab">
              Gastrointestinal
            </li>
            <li onClick={() => scrollToSection(neuro_form)} className="mytab">
              Neurological
            </li>
            <li
              onClick={() => scrollToSection(respiratory_form)}
              className="mytab"
            >
              Respiratory
            </li>
            <li onClick={() => scrollToSection(cardio_form)} className="mytab">
              Cardio Vascular
            </li>
          </ul>
        </div>
        {/* <Row> */}
        <div className="form-col">
          {/* Cardio vascular assessment form */}
          <div
            id="cardio-form"
            ref={cardio_form}
            className="assessment-col-form"
          >
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/heart.svg" alt="heart" />
                <h6>&nbsp;Cardio Vascular Assessment </h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Cardiac rhythm</h6>
                  <div className="assessment-buttons">
                    {phyExamItems.map((phyItem, index) => (
                      <React.Fragment key={index}>
                        {phyItem.groupName == "CARDIAC RHTYHEM" && (
                          <div>
                            {phyItem.elements.map((cardiac, i) => (
                              <React.Fragment key={i}>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="cariac"
                                  id={cardiac._id}
                                  value={cardiac.title}
                                  onChange={setCardiacValue}
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={cardiac._id}
                                >
                                  <span className={isChecked(cardiac)}>
                                    {cardiac.title}
                                  </span>
                                </label>
                              </React.Fragment>
                            ))}
                          </div>
                        )}
                      </React.Fragment>
                    ))}
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Heart Rhythm</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Heart Rhythm" && (
                            <div>
                              {phyItem.elements.map((heart, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="heart"
                                    id={heart._id}
                                    checked={heart.title == heartRhythmValue}
                                    value={heart.title}
                                    onChange={(e) =>
                                      setHeartRhythm(e, heart.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={heart._id}
                                  >
                                    <span
                                      className={isChecked(heartRhythmValue)}
                                    >
                                      {heart.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Nail Bed Colour</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Nail Bed Colour" && (
                            <div>
                              {phyItem.elements.map((Nail, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="nail"
                                    id={Nail._id}
                                    checked={Nail.title == nailColourValue}
                                    value={Nail.title}
                                    onChange={(e) =>
                                      setNailColour(e, Nail.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={Nail._id}
                                  >
                                    <span
                                      className={isChecked(nailColourValue)}
                                    >
                                      {Nail.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Cardio vascular assessment form */}

        {/* Neurological assessment form */}
        <div className="form-col">
          <div id="neuro-form" ref={neuro_form} className="assessment-col-form">
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/nurve.svg" alt="nurve" />
                <h6>&nbsp;Neurological Assessment</h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Aspiration Risk</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Aspiration risk" && (
                            <div>
                              {phyItem.elements.map((risk, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="asp-risk"
                                    id={risk._id}
                                    value={risk.title}
                                    onChange={setAspirationRisk}
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={risk._id}
                                  >
                                    <span className={isChecked(risk)}>
                                      {risk.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Facial Symmetry</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Facial Symmetry" && (
                            <div>
                              {phyItem.elements.map((faceSummery, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="facial"
                                    id={faceSummery._id}
                                    value={faceSummery.title}
                                    onChange={setFacialSummery}
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={faceSummery._id}
                                  >
                                    <span className={isChecked(faceSummery)}>
                                      {faceSummery.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Perrla</h6>
                  <div className="assessment-buttons">
                    <Form.Group
                      className="mb-3_fname"
                      controlId="exampleForm.FName"
                    >
                      <div
                        className={`checkbox-slider ${
                          checkValue1 && "checkbox-slider--on"
                        }`}
                        onClick={() => setCheckbox(!checkValue1)}
                      >
                        <div className="checkbox-slider__ball"></div>
                      </div>
                    </Form.Group>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <Row>
                    <Col>
                      <h6>Pupil Left</h6>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="text"
                            autoComplete="off"
                            placeholder="Enter description"
                            value={pupilDesc}
                            onChange={(event) => {
                              setPupilDesc(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="text"
                            autoComplete="off"
                            placeholder="Enter reaction"
                            value={pupilReaction}
                            onChange={(event) => {
                              setPupilReaction(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="number"
                            autoComplete="off"
                            placeholder="Size (in mm)"
                            value={pupilSize}
                            onChange={(event) => {
                              setPupilSize(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            as="textarea"
                            type="number"
                            autoComplete="off"
                            placeholder="Comments (in any)"
                            value={pupilComments}
                            onChange={(event) => {
                              setPupilComments(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                    <Col>
                      <h6>Pupil Right</h6>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="text"
                            autoComplete="off"
                            placeholder="Enter description"
                            value={pupilRightDesc}
                            onChange={(event) => {
                              setPupilRightDesc(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="text"
                            autoComplete="off"
                            placeholder="Enter reaction"
                            value={pupilRightReaction}
                            onChange={(event) => {
                              setPupilRightReaction(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            type="number"
                            autoComplete="off"
                            placeholder="Size (in mm)"
                            value={pupilRightSize}
                            onChange={(event) => {
                              setPupilRightSize(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                      <div className="assessment-buttons">
                        <Form.Group
                          className="mb-3_mname"
                          controlId="exampleForm.MName"
                        >
                          <Form.Control
                            as="textarea"
                            type="number"
                            autoComplete="off"
                            placeholder="Comments (in any)"
                            value={pupilRightComments}
                            onChange={(event) => {
                              setPupilRightComments(event.target.value);
                            }}
                          />
                        </Form.Group>
                      </div>
                    </Col>
                  </Row>
                </div>
                <div className="assessment-button-section">
                  <h6>Characteristic Speech</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Characteristic speech" && (
                            <div>
                              {phyItem.elements.map((Characteristic, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="speech"
                                    id={Characteristic._id}
                                    checked={
                                      Characteristic.title ==
                                      characteristicValue
                                    }
                                    value={Characteristic.title}
                                    onChange={(e) =>
                                      setCharacteristic(e, Characteristic.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={Characteristic._id}
                                  >
                                    <span
                                      className={isChecked(characteristicValue)}
                                    >
                                      {Characteristic.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Gait</h6>
                  <div className="assessment-buttons">
                    {phyExamItems.map((phyItem, index) => (
                      <React.Fragment key={index}>
                        {phyItem.groupName == "Gait" && (
                          <div>
                            {phyItem.elements.map((gait, i) => (
                              <React.Fragment key={i}>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="gait"
                                  id={gait._id}
                                  value={gait.title}
                                  onChange={setGait}
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={gait._id}
                                >
                                  <span className={isChecked(gait)}>
                                    {gait.title}
                                  </span>
                                </label>
                              </React.Fragment>
                            ))}
                          </div>
                        )}
                      </React.Fragment>
                    ))}
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Loss Of Concentration</h6>
                  <div className="assessment-buttons">
                    {phyExamItems.map((phyItem, index) => (
                      <React.Fragment key={index}>
                        {phyItem.groupName == "Loss of Concentration" && (
                          <div>
                            {phyItem.elements.map((conc, i) => (
                              <React.Fragment key={i}>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="conce"
                                  checked={conc.title == concentrationValue}
                                  id={conc._id}
                                  value={conc.title}
                                  onChange={(e) =>
                                    setConcentration(e, conc.title)
                                  }
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={conc._id}
                                >
                                  <span
                                    className={isChecked(concentrationValue)}
                                  >
                                    {conc.title}
                                  </span>
                                </label>
                              </React.Fragment>
                            ))}
                          </div>
                        )}
                      </React.Fragment>
                    ))}
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Neurological assessment form */}

        {/* Genitors urinary assessment form */}
        <div className="form-col">
          <div
            id="genitory-form"
            ref={genitory_form}
            className="assessment-col-form"
          >
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/uritory.svg" alt="genitors" />
                <h6>&nbsp;Genitors Urinary Assessment</h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Urine Color</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Urine Colour" && (
                            <div>
                              {phyItem.elements.map((ucolor, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="urine-color"
                                    checked={ucolor.title == urineColorValue}
                                    id={ucolor._id}
                                    value={ucolor.title}
                                    onChange={(e) =>
                                      setUrineColor(e, ucolor.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={ucolor._id}
                                  >
                                    <span
                                      className={isChecked(urineColorValue)}
                                    >
                                      {ucolor.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Urine Description</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Urine description" && (
                            <div>
                              {phyItem.elements.map((udesc, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="urine-description"
                                    checked={
                                      udesc.title == urineDescriptionValue
                                    }
                                    id={udesc._id}
                                    value={udesc.title}
                                    onChange={(e) =>
                                      setUrineDescription(e, udesc.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={udesc._id}
                                  >
                                    <span
                                      className={isChecked(
                                        urineDescriptionValue
                                      )}
                                    >
                                      {udesc.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Urinary Symptoms</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Urinary Symptoms" && (
                            <div>
                              {phyItem.elements.map((usystems, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="symptoms"
                                    checked={usystems.title == symptomsValue}
                                    id={usystems._id}
                                    value={usystems.title}
                                    onChange={(e) =>
                                      setUrineSymptoms(e, usystems.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={usystems._id}
                                  >
                                    <span className={isChecked(symptomsValue)}>
                                      {usystems.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Genitors urinary assessment form */}

        {/* Skin assessment form */}
        <div className="form-col">
          <div id="skin-form" ref={skin_form} className="assessment-col-form">
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/skin.svg" alt="skin" />
                <h6>&nbsp; Skin Assessment</h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Skin Integration</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Skin Integrity" && (
                            <div>
                              {phyItem.elements.map((integration, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="integration"
                                    checked={
                                      integration.title == integrationValue
                                    }
                                    id={integration._id}
                                    value={integration.title}
                                    onChange={(e) =>
                                      setSkinIntegration(e, integration.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={integration._id}
                                  >
                                    <span
                                      className={isChecked(integrationValue)}
                                    >
                                      {integration.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Mucus Membrane Color</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Mucus Membrane Colour" && (
                            <div>
                              {phyItem.elements.map((skinmembrane, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="skin-color"
                                    checked={
                                      skinmembrane.title == skinColorValue
                                    }
                                    id={skinmembrane._id}
                                    value={skinmembrane.title}
                                    onChange={(e) =>
                                      setSkinColor(e, skinmembrane.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={skinmembrane._id}
                                  >
                                    <span className={isChecked(skinColorValue)}>
                                      {skinmembrane.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Mucus Membrane Description</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName ==
                            "Mucus Membrane Description" && (
                            <div>
                              {phyItem.elements.map((mucusmembrane, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="skin-desc"
                                    checked={
                                      mucusmembrane.title == mucusDesValue
                                    }
                                    id={mucusmembrane._id}
                                    value={mucusmembrane.title}
                                    onChange={(e) =>
                                      setMucusDescription(
                                        e,
                                        mucusmembrane.title
                                      )
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={mucusmembrane._id}
                                  >
                                    <span className={isChecked(mucusDesValue)}>
                                      {mucusmembrane.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Skin assessment form */}
        {/* </Col> */}

        {/* <Col md={12} className="form-col"> */}
        {/* Respiratory assessment form */}
        <div className="form-col">
          <div
            id="respiratory-form"
            ref={respiratory_form}
            className="assessment-col-form"
          >
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/respiratory.svg" alt="respiratory" />
                <h6>&nbsp; Respiratory Assessment</h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Respiration</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Respiration" && (
                            <div>
                              {phyItem.elements.map((respiration, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="respiration"
                                    id={respiration._id}
                                    value={respiration.title}
                                    onChange={setRespiration}
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={respiration._id}
                                  >
                                    <span className={isChecked(respiration)}>
                                      {respiration.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Respiratory Pattern</h6>
                  <div className="assessment-buttons">
                    {phyExamItems.map((phyItem, index) => (
                      <React.Fragment key={index}>
                        {phyItem.groupName == "Respiratory Pattern" && (
                          <div>
                            {phyItem.elements.map((respattern, i) => (
                              <React.Fragment key={i}>
                                <input
                                  className="checkbox-tools"
                                  type="checkbox"
                                  name="respattern"
                                  checked={respattern.title == patternValue}
                                  id={respattern._id}
                                  value={respattern.title}
                                  onChange={(e) =>
                                    setResPattern(e, respattern.title)
                                  }
                                />
                                <label
                                  className="for-checkbox-tools"
                                  htmlFor={respattern._id}
                                >
                                  <span className={isChecked(patternValue)}>
                                    {respattern.title}
                                  </span>
                                </label>
                              </React.Fragment>
                            ))}
                          </div>
                        )}
                      </React.Fragment>
                    ))}
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Cough</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == " Cough " && (
                            <div>
                              {phyItem.elements.map((cough, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="cough"
                                    checked={cough.title == coughValue}
                                    id={cough._id}
                                    value={cough.title}
                                    onChange={(e) => setCough(e, cough.title)}
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={cough._id}
                                  >
                                    <span className={isChecked(coughValue)}>
                                      {cough.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Sputum Colour</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Sputum Colour" && (
                            <div>
                              {phyItem.elements.map((spectcolor, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="spec-color"
                                    checked={spectcolor.title == speColorValue}
                                    id={spectcolor._id}
                                    value={spectcolor.title}
                                    onChange={(e) =>
                                      setSpeColor(e, spectcolor.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={spectcolor._id}
                                  >
                                    <span className={isChecked(speColorValue)}>
                                      {spectcolor.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Sputum Amount</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Sputum Amount" && (
                            <div>
                              {phyItem.elements.map((spectamount, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="sput-amnt"
                                    checked={spectamount.title == amountValue}
                                    id={spectamount._id}
                                    value={spectamount.title}
                                    onChange={(e) =>
                                      setSpeAmount(e, spectamount.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={spectamount._id}
                                  >
                                    <span className={isChecked(amountValue)}>
                                      {spectamount.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Respiratory assessment form */}

        {/* Gastrointestinal assessment form */}
        <div className="form-col">
          <div
            id="gastro-form"
            ref={gastro_form}
            className="assessment-col-form"
          >
            <form className="assessment-form">
              <div className="assessment-header">
                <img src="../img/PA-SVG/gastro.svg" alt="Gastro" />
                <h6>&nbsp; Gastrointestinal Assessment</h6>
              </div>
              <div className="col-container assessment-body">
                <div className="assessment-button-section">
                  <h6>Symptoms</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Symptoms" && (
                            <div>
                              {phyItem.elements.map((gastrosymtoms, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="gassymptoms"
                                    id={gastrosymtoms._id}
                                    value={gastrosymtoms.title}
                                    onChange={setGastroSymptoms}
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={gastrosymtoms._id}
                                  >
                                    <span className={isChecked(gastrosymtoms)}>
                                      {gastrosymtoms.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Abdomen Appearance</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Abdomen Appearance" && (
                            <div>
                              {phyItem.elements.map((adbAppearance, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="abd-apear"
                                    checked={
                                      adbAppearance.title == abdomenValue
                                    }
                                    id={adbAppearance._id}
                                    value={adbAppearance.title}
                                    onChange={(e) =>
                                      setAbdomenAppearance(
                                        e,
                                        adbAppearance.title
                                      )
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={adbAppearance._id}
                                  >
                                    <span className={isChecked(abdomenValue)}>
                                      {adbAppearance.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Abdomen Palpation</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Abdomen Palpation" && (
                            <div>
                              {phyItem.elements.map((palpation, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="abd-pupl"
                                    checked={palpation.title == palpationValue}
                                    id={palpation._id}
                                    value={palpation.title}
                                    onChange={(e) =>
                                      setAbdomenPalpation(e, palpation.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={palpation._id}
                                  >
                                    <span className={isChecked(palpationValue)}>
                                      {palpation.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Stool Color</h6>
                  <div className="assessment-buttons">
                    <div>
                      {phyExamItems.map((phyItem, index) => (
                        <React.Fragment key={index}>
                          {phyItem.groupName == "Stool Colour" && (
                            <div>
                              {phyItem.elements.map((stoolcolor, i) => (
                                <React.Fragment key={i}>
                                  <input
                                    className="checkbox-tools"
                                    type="checkbox"
                                    name="stool-color"
                                    checked={
                                      stoolcolor.title == stoolColorValue
                                    }
                                    id={stoolcolor._id}
                                    value={stoolcolor.title}
                                    onChange={(e) =>
                                      setStoolColor(e, stoolcolor.title)
                                    }
                                  />
                                  <label
                                    className="for-checkbox-tools"
                                    htmlFor={stoolcolor._id}
                                  >
                                    <span
                                      className={isChecked(stoolColorValue)}
                                    >
                                      {stoolcolor.title}
                                    </span>
                                  </label>
                                </React.Fragment>
                              ))}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Last Bowel Movement</h6>
                  <div className="assessment-buttons">
                    <Form.Group
                      className="mb-3_mname"
                      controlId="exampleForm.MName"
                    >
                      <Form.Control
                        type="text"
                        autoComplete="off"
                        placeholder="Enter details here"
                        value={bowelMove}
                        onChange={(event) => {
                          setBowelMove(event.target.value);
                        }}
                      />
                    </Form.Group>
                  </div>
                </div>
                <div className="assessment-button-section">
                  <h6>Bowel Sounds</h6>
                  <div>
                    <h6>LUQ</h6>
                    <div className="assessment-buttons">
                      <div>
                        {phyExamItems.map((phyItem, index) => (
                          <React.Fragment key={index}>
                            {phyItem.groupName == "LUQ" && (
                              <div>
                                {phyItem.elements.map((luq, i) => (
                                  <React.Fragment key={i}>
                                    <input
                                      className="checkbox-tools"
                                      type="checkbox"
                                      name="luq"
                                      checked={luq.title == luqValue}
                                      id={luq._id}
                                      value={luq.title}
                                      onChange={(e) => setLuq(e, luq.title)}
                                    />
                                    <label
                                      className="for-checkbox-tools"
                                      htmlFor={luq._id}
                                    >
                                      <span className={isChecked(luqValue)}>
                                        {luq.title}
                                      </span>
                                    </label>
                                  </React.Fragment>
                                ))}
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                      </div>
                    </div>
                    <div className="assessment-buttons">
                      <Form.Group
                        className="mb-3_mname"
                        controlId="exampleForm.MName"
                      >
                        <Form.Control
                          type="text"
                          autoComplete="off"
                          placeholder="Comments (if any)"
                          value={luqComments}
                          onChange={(event) => {
                            setLuqComments(event.target.value);
                          }}
                        />
                      </Form.Group>
                    </div>
                  </div>
                  <div>
                    <h6>RUQ</h6>
                    <div className="assessment-buttons">
                      <div>
                        {phyExamItems.map((phyItem, index) => (
                          <React.Fragment key={index}>
                            {phyItem.groupName == "RUQ" && (
                              <div>
                                {phyItem.elements.map((ruq, i) => (
                                  <React.Fragment key={i}>
                                    <input
                                      className="checkbox-tools"
                                      type="checkbox"
                                      name="ruq"
                                      checked={ruq.title == ruqValue}
                                      id={ruq._id}
                                      value={ruq.title}
                                      onChange={(e) => setRuq(e, ruq.title)}
                                    />
                                    <label
                                      className="for-checkbox-tools"
                                      htmlFor={ruq._id}
                                    >
                                      <span className={isChecked(ruqValue)}>
                                        {ruq.title}
                                      </span>
                                    </label>
                                  </React.Fragment>
                                ))}
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                      </div>
                    </div>
                    <div className="assessment-buttons">
                      <Form.Group
                        className="mb-3_mname"
                        controlId="exampleForm.MName"
                      >
                        <Form.Control
                          type="text"
                          autoComplete="off"
                          placeholder="Comments (if any)"
                          value={ruqComments}
                          onChange={(event) => {
                            setRuqComments(event.target.value);
                          }}
                        />
                      </Form.Group>
                    </div>
                  </div>
                  <div>
                    <h6>LLQ</h6>
                    <div className="assessment-buttons">
                      <div>
                        {phyExamItems.map((phyItem, index) => (
                          <React.Fragment key={index}>
                            {phyItem.groupName == "LLQ" && (
                              <div>
                                {phyItem.elements.map((llq, i) => (
                                  <React.Fragment key={i}>
                                    <input
                                      className="checkbox-tools"
                                      type="checkbox"
                                      name="llq"
                                      checked={llq.title == llqValue}
                                      id={llq._id}
                                      value={llq.title}
                                      onChange={(e) => setLlq(e, llq.title)}
                                    />
                                    <label
                                      className="for-checkbox-tools"
                                      htmlFor={llq._id}
                                    >
                                      <span className={isChecked(llqValue)}>
                                        {llq.title}
                                      </span>
                                    </label>
                                  </React.Fragment>
                                ))}
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                      </div>
                    </div>
                    <div className="assessment-buttons">
                      <Form.Group
                        className="mb-3_mname"
                        controlId="exampleForm.MName"
                      >
                        <Form.Control
                          type="text"
                          autoComplete="off"
                          placeholder="Comments (if any)"
                          value={llqComments}
                          onChange={(event) => {
                            setLlqComments(event.target.value);
                          }}
                        />
                      </Form.Group>
                    </div>
                  </div>
                  <div>
                    <h6>RLQ</h6>
                    <div className="assessment-buttons">
                      <div>
                        {phyExamItems.map((phyItem, index) => (
                          <React.Fragment key={index}>
                            {phyItem.groupName == "RLQ" && (
                              <div>
                                {phyItem.elements.map((rlq, i) => (
                                  <React.Fragment key={i}>
                                    <input
                                      className="checkbox-tools"
                                      type="checkbox"
                                      name="rlq"
                                      checked={rlq.title == rlqValue}
                                      id={rlq._id}
                                      value={rlq.title}
                                      onChange={(e) => setRlq(e, rlq.title)}
                                    />
                                    <label
                                      className="for-checkbox-tools"
                                      htmlFor={rlq._id}
                                    >
                                      <span className={isChecked(rlqValue)}>
                                        {rlq.title}
                                      </span>
                                    </label>
                                  </React.Fragment>
                                ))}
                              </div>
                            )}
                          </React.Fragment>
                        ))}
                      </div>
                    </div>
                    <div className="assessment-buttons">
                      <Form.Group
                        className="mb-3_mname"
                        controlId="exampleForm.MName"
                      >
                        <Form.Control
                          type="text"
                          autoComplete="off"
                          placeholder="Comments (if any)"
                          value={rlqComments}
                          onChange={(event) => {
                            setRlqComments(event.target.value);
                          }}
                        />
                      </Form.Group>
                    </div>
                  </div>
                </div>
              </div>
            </form>
            {phyExamId ? (
              <SaveButton
                class_name="regBtnN"
                butttonClick={(e) => updateobservation(phyExamId)}
                button_name="Update"
              />
            ) : (
              <SaveButton
                class_name="regBtnN"
                butttonClick={postobservation}
                button_name="Save"
              />
            )}
          </div>
        </div>
        {/* Gastrointestinal assessment form */}

        {/* </Col> */}
        <div className="back-btn">
          <div className="scroll-to-top">
            {visible && (
              <div onClick={scrollToTop}>
                <i className="bi bi-arrow-up-short up-arrow"></i>
                <p>Back To Top</p>
              </div>
            )}
          </div>
        </div>
        {/* </Row> */}
      </div>
    </React.Fragment>
  );
}

export default PhysicalExamination;
