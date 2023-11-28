import React, { useState, useEffect } from "react";
import { Col, Row, Form, Button, Accordion } from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import ViewModalPopups from "../../ModalPopups/ViewModalPopups";
import GeneralExamination from "./GeneralExamination";
import Respiratory from "./Respiratory";
import Auscultation from "./Auscultation";
import CardioVascular from "./CardioVascular";
import Gastrointestinal from "./Gastrointestinal";
import Musculoskeletal from "./Musculoskeletal";
import moment from "moment";
import { loadPhyExamItems } from "../../../../redux/formUtilityAction";
import * as constant from "../../../ConstUrl/constant";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import { useDispatch, useSelector } from "react-redux";
import LimbsTable from "./LimbsTable";
import {
  loadPostPhyExamList,
  loadUpdatePhyExamList,
} from "../../../../redux/actions";

export default function PhysicalGeneralExam(props) {
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

  const [checked, setChecked] = useState([]);
  var isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  useEffect(() => {
    dispatch(loadPhyExamItems());
  }, []);

  // view previous history modal
  const [physicalExaminationShow, setPhysicalExaminationShow] = useState(false);
  const physicalClose = () => setPhysicalExaminationShow(false);
  const physicalShow = () => setPhysicalExaminationShow(true);
  // view previous history modal

  const dateto = moment(Date.now()).format("YYYY-MM-DD");
  const datefrom = moment(Date.now() - 7 * 24 * 3600 * 1000).format(
    "YYYY-MM-DD"
  );

  let assignSystemicArray = [
    { name: "Respiratory" },
    { name: "Auscultation" },
    { name: "Cardiovascular System" },
    { name: "Gastrointestinal System" },
    { name: "Musculoskeletal Examination" },
  ];

  let Name_of_Upper_Joint = [
    { name: "Shoulder" },
    { name: "Elbow" },
    { name: "Wrist" },
    { name: "Hand" },
  ];

  let Name_of_Lower_Joint = [
    { name: "Hip" },
    { name: "Knee" },
    { name: "Ankle" },
    { name: "Foot" },
  ];
  // General Examination

  const [slr, setSLR] = useState("");

  // General Examination

  // Physical Examination

  const [phyAssignSystemicArray, setPhyAssignSystemicArray] = useState([]);

  const setPhyAssignSystemic = (e) => {
    if (e.target.checked) {
      setPhyAssignSystemicArray([
        ...phyAssignSystemicArray,
        { value: e.target.value, status: true },
      ]);
    } else {
      setPhyAssignSystemicArray(
        phyAssignSystemicArray.filter((id) => id.value !== e.target.value)
      );
    }
  };

  // Physical Examination

  // setting value for General examination and pickle
  const [consciousVal, setConsciousVal] = useState("");
  const [corporateVal, setCorporateVal] = useState("");
  const [comfortableVal, setComfortableVal] = useState("");
  const [toxicVal, setToxicVal] = useState("");
  const [dyspneicVal, setDyspneicVal] = useState("");
  const [buildVal, setBuildVal] = useState("");
  const [nourishmentVal, setNourishmentVal] = useState("");
  const [pallorVal, setPallorVal] = useState("");
  const [icterusVal, setIcterusVal] = useState("");
  const [cyanosisVal, setCyanosisVal] = useState("");
  const [clubbingVal, setClubbingVal] = useState("");
  const [koilonychiaVal, setKoilonychiaVal] = useState("");
  const [lymphadenopathyVal, setLymphadenopathyVal] = useState("");
  const [pedalEdemaVal, setPedalEdemaVal] = useState("");
  const setGenPallor = (e, name, assessName) => {
    if (e.target.checked) {
      if (assessName == "Conscious") {
        setConsciousVal(name);
      }
      if (assessName == "Cooperative") {
        setCorporateVal(name);
      }
      if (assessName == "Comfortable") {
        setComfortableVal(name);
      }
      if (assessName == "Toxic") {
        setToxicVal(name);
      }
      if (assessName == "Dyspneic") {
        setDyspneicVal(name);
      }
      if (assessName == "Build") {
        setBuildVal(name);
      }
      if (assessName == "Nourishment") {
        setNourishmentVal(name);
      }
      if (assessName == "Pallor") {
        setPallorVal(name);
      }
      if (assessName == "Icterus") {
        setIcterusVal(name);
      }
      if (assessName == "Cyanosis") {
        setCyanosisVal(name);
      }
      if (assessName == "Clubbing") {
        setClubbingVal(name);
      }
      if (assessName == "Koilonychia") {
        setKoilonychiaVal(name);
      }
      if (assessName == "Lymphadenopathy") {
        setLymphadenopathyVal(name);
      }
      if (assessName == "Pedal Edema") {
        setPedalEdemaVal(name);
      }
    } else {
      if (assessName == "Conscious") {
        setConsciousVal("");
      }
      if (assessName == "Cooperative") {
        setCorporateVal("");
      }
      if (assessName == "Comfortable") {
        setComfortableVal("");
      }
      if (assessName == "Toxic") {
        setToxicVal("");
      }
      if (assessName == "Dyspneic") {
        setDyspneicVal("");
      }
      if (assessName == "Build") {
        setBuildVal("");
      }
      if (assessName == "Nourishment") {
        setNourishmentVal("");
      }
      if (assessName == "Pallor") {
        setPallorVal("");
      }
      if (assessName == "Icterus") {
        setIcterusVal("");
      }
      if (assessName == "Cyanosis") {
        setCyanosisVal("");
      }
      if (assessName == "Clubbing") {
        setClubbingVal("");
      }
      if (assessName == "Koilonychia") {
        setKoilonychiaVal("");
      }
      if (assessName == "Lymphadenopathy") {
        setLymphadenopathyVal("");
      }
      if (assessName == "Pedal Edema") {
        setPedalEdemaVal("");
      }
    }
  };

  // post data for general examination assessment
  let general_examination = {
    assessmentTitle: "General Examination",
    observations: [
      {
        observationName: "Conscious",
        observationValues: consciousVal,
      },
      {
        observationName: "Cooperative",
        observationValues: corporateVal,
      },
      {
        observationName: "Comfortable",
        observationValues: comfortableVal,
      },
      {
        observationName: "Toxic",
        observationValues: toxicVal,
      },
      {
        observationName: "Dyspneic",
        observationValues: dyspneicVal,
      },
      {
        observationName: "Build",
        observationValues: buildVal,
      },
      {
        observationName: "Nourishment",
        observationValues: nourishmentVal,
      },
    ],
  };
  // post data for general examination assessment

  // post data for pickle assessment
  let pickle = {
    assessmentTitle: "Pickle",
    observations: [
      {
        observationName: "Pallor",
        observationValues: pallorVal,
      },
      {
        observationName: "Icterus",
        observationValues: icterusVal,
      },
      {
        observationName: "Cyanosis",
        observationValues: cyanosisVal,
      },
      {
        observationName: "Clubbing",
        observationValues: clubbingVal,
      },
      {
        observationName: "Koilonychia",
        observationValues: koilonychiaVal,
      },
      {
        observationName: "Lymphadenopathy",
        observationValues: lymphadenopathyVal,
      },
      {
        observationName: "Pedal Edema",
        observationValues: pedalEdemaVal,
      },
    ],
  };

  // post data for pickle assessment
  // setting value for General examination and pickle

  // setting value for Respiratory
  const [respValues, setRespValues] = useState({
    respiratoryRate: "",
    obsRespiratoryRate: "",
    Tachypnea: "",
    AccessoryMuscles: "",
    InterRetractions: "",
  });

  const handleChangeResp = (e) => {
    let { name, value } = e.target;
    setRespValues({ ...respValues, [name]: value });
  };

  let respiratory_data = {
    assessmentTitle: "Respiratory",
    observations: [
      {
        observationName: "Respiratory Rate",
        observationValues: respValues.respiratoryRate,
      },
      {
        observationName: "Observed Respiratory Rate",
        observationValues: respValues.obsRespiratoryRate,
      },
      {
        observationName: "Tachypnea",
        observationValues: respValues.Tachypnea,
      },
      {
        observationName: "Accessory Muscles (Sternocleidomastoid)",
        observationValues: respValues.AccessoryMuscles,
      },
      {
        observationName: "Intercostal Retractions",
        observationValues: respValues.InterRetractions,
      },
    ],
  };

  // setting value for Respiratory

  // setting value for Asultation
  const [asculValues, setAsculValues] = useState({
    lungClr: "",
    rhonchi: "",
    wheezes: "",
    crepita: "",
    airEntry: "",
    breathMov: "",
    breathSou: "",
  });

  const handleChangeAsc = (e) => {
    let { name, value } = e.target;
    setAsculValues({ ...asculValues, [name]: value });
  };

  let auscultation_data = {
    assessmentTitle: "Auscultation",
    observations: [
      {
        observationName: "Lungs Clear on Auscultation",
        observationValues: asculValues.lungClr,
      },
      {
        observationName: "Rhonchi",
        observationValues: asculValues.rhonchi,
      },
      {
        observationName: "Wheezes",
        observationValues: asculValues.wheezes,
      },
      {
        observationName: "Crepitations",
        observationValues: asculValues.crepita,
      },
      {
        observationName: "Airway Entry",
        observationValues: asculValues.airEntry,
      },
      {
        observationName: "Breath Movements Rt/Lt",
        observationValues: asculValues.breathMov,
      },
      {
        observationName: "Breath Sounds Rt/Lt",
        observationValues: asculValues.breathSou,
      },
    ],
  };
  // setting value for Asultation

  // setting value for cardiacValues
  const [cardiacValues, setCardiacValues] = useState({
    pulseRate: "",
    rrrRate: "",
    obsRRR: "",
    tachyValue: "",
    bradycardia: "",
    jugularVenous: "",
    S1S2heard: "",
    noAdded: "",
  });

  const handleChangeCardio = (e) => {
    let { name, value } = e.target;
    setCardiacValues({ ...cardiacValues, [name]: value });
  };

  let cardiao_data = {
    assessmentTitle: "Cardiovascular System",
    observations: [
      {
        observationName: "Pulse Rate",
        observationValues: cardiacValues.pulseRate,
      },
      {
        observationName: "Regular Rate & Rhythm (RRR)",
        observationValues: cardiacValues.rrrRate,
      },
      {
        observationName: "Observed RRR",
        observationValues: cardiacValues.obsRRR,
      },
      {
        observationName: "Tachycardia",
        observationValues: cardiacValues.tachyValue,
      },
      {
        observationName: "Bradycardia",
        observationValues: cardiacValues.bradycardia,
      },
      {
        observationName: "Jugular Venous Pulse",
        observationValues: cardiacValues.jugularVenous,
      },
      {
        observationName: "S1 S2 Heard",
        observationValues: cardiacValues.S1S2heard,
      },
      {
        observationName: "No Added Sounds and Murmurs",
        observationValues: cardiacValues.noAdded,
      },
    ],
  };
  // setting value for cardiacValues

  // setting value for Gastrointestinal
  const [gastroValues, setGastroValues] = useState({
    inspection: "",
    palpation: "",
    tender: "",
    tenderness: "",
    hepatomegaly: "",
    splenomegaly: "",
    hernia: "",
    bowelSounds: "",
    murphySign: "",
    mcBurneyPoint: "",
  });

  const handleChangeGastro = (e) => {
    let { name, value } = e.target;
    setGastroValues({ ...gastroValues, [name]: value });
  };

  let gastro_data = {
    assessmentTitle: "Gastrointestinal System",
    observations: [
      {
        observationName: "Inspection",
        observationValues: gastroValues.inspection,
      },
      {
        observationName: "Palpation",
        observationValues: gastroValues.palpation,
      },
      {
        observationName: "Tender",
        observationValues: gastroValues.tender,
      },
      {
        observationName: "Tenderness in",
        observationValues: gastroValues.tenderness,
      },
      {
        observationName: "Hepatomegaly",
        observationValues: gastroValues.hepatomegaly,
      },
      {
        observationName: "Splenomegaly",
        observationValues: gastroValues.splenomegaly,
      },
      {
        observationName: "Hernia",
        observationValues: gastroValues.hernia,
      },
      {
        observationName: "Bowel Sounds",
        observationValues: gastroValues.bowelSounds,
      },
      {
        observationName: "Murphy's Sign",
        observationValues: gastroValues.murphySign,
      },
      {
        observationName: "McBurney's Point",
        observationValues: gastroValues.mcBurneyPoint,
      },
    ],
  };
  // setting value for Gastrointestinal

  // setting value for Musculoskeletal
  const [ambulatoryValues, setAmbulatoryValues] = useState({
    freely: "",
    assistant: "",
    wheelchair: "",
    walkingStick: "",
    gait: "",
    extraOcular: "",
  });
  const [neckValues, setNeckValues] = useState({
    flexion: "",
    bending: "",
    extension: "",
    spurling: "",
    rangeOfMove: "",
  });

  const handleChangeAmbu = (e) => {
    let { name, value } = e.target;
    setAmbulatoryValues({ ...ambulatoryValues, [name]: value });
  };
  const handleChangeNeck = (e) => {
    let { name, value } = e.target;
    setNeckValues({ ...neckValues, [name]: value });
  };

  let ambulatory_data = {
    assessmentTitle: "Ambulatory",
    observations: [
      {
        observationName: "Freely Ambulatory",
        observationValues: ambulatoryValues.freely,
      },
      {
        observationName: "With Assistant (Support)",
        observationValues: ambulatoryValues.assistant,
      },
      {
        observationName: "With Wheelchair",
        observationValues: ambulatoryValues.wheelchair,
      },
      {
        observationName: "With Walking Stick",
        observationValues: ambulatoryValues.walkingStick,
      },
      {
        observationName: "Gait",
        observationValues: ambulatoryValues.gait,
      },
      {
        observationName: "Extra-ocular Movements",
        observationValues: ambulatoryValues.extraOcular,
      },
    ],
  };

  let neck_data = {
    assessmentTitle: "NECK",
    observations: [
      {
        observationName: "Flexion",
        observationValues: neckValues.flexion,
      },
      {
        observationName: "Lateral Bending",
        observationValues: neckValues.bending,
      },
      {
        observationName: "Extension",
        observationValues: neckValues.extension,
      },
      {
        observationName: "Spurling's Sign",
        observationValues: neckValues.spurling,
      },
      {
        observationName: "Range of Movement",
        observationValues: neckValues.rangeOfMove,
      },
    ],
  };
  // setting value for Musculoskeletal

  //for upper rigt limb
  // setting value for RUS
  const [upperRightSholdLimbs, setUpperRightSholdLimbs] = useState({
    ursMotionRange: "",
    ursStrength: "",
    ursWasting: "",
    ursSensation: "",
    ursReflexes: "",
  });

  const handleChangeURS = (e) => {
    let { name, value } = e.target;
    setUpperRightSholdLimbs({ ...upperRightSholdLimbs, [name]: value });
  };
  // set post value for Right Upper Shoulder
  let RUS = {
    assessmentTitle: "Right Upper Limb - Shoulder",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperRightSholdLimbs.ursMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperRightSholdLimbs.ursStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperRightSholdLimbs.ursWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperRightSholdLimbs.ursSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperRightSholdLimbs.ursReflexes,
      },
    ],
  };
  // set post value for Right Upper Shoulder
  // setting value for RUS

  // setting value for RUE
  const [upperRightElbLimbs, setUpperRightElbLimbs] = useState({
    ureMotionRange: "",
    ureStrength: "",
    ureWasting: "",
    ureSensation: "",
    ureReflexes: "",
  });

  const handleChangeURE = (e) => {
    let { name, value } = e.target;
    setUpperRightElbLimbs({ ...upperRightElbLimbs, [name]: value });
  };

  // set post value for Right Upper Elbow
  let RUE = {
    assessmentTitle: "Right Upper Limb - Elbow",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperRightElbLimbs.ureMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperRightElbLimbs.ureStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperRightElbLimbs.ureWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperRightElbLimbs.ureSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperRightElbLimbs.ureReflexes,
      },
    ],
  };
  // set post value for Right Upper Elbow
  // setting value for RUE

  // setting value for RUW
  const [upperRightWriLimbs, setUpperRightWriLimbs] = useState({
    urwMotionRange: "",
    urwStrength: "",
    urwWasting: "",
    urwSensation: "",
    urwReflexes: "",
  });

  const handleChangeURW = (e) => {
    let { name, value } = e.target;
    setUpperRightWriLimbs({ ...upperRightWriLimbs, [name]: value });
  };

  // set post value for Right Upper Wrist
  let RUW = {
    assessmentTitle: "Right Upper Limb - Wrist",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperRightWriLimbs.urwMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperRightWriLimbs.urwStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperRightWriLimbs.urwWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperRightWriLimbs.urwSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperRightWriLimbs.urwReflexes,
      },
    ],
  };
  // set post value for Right Upper Wrist
  // setting value for RUW

  // setting value for RUH
  const [upperRightHandLimbs, setUpperRightHandLimbs] = useState({
    urhMotionRange: "",
    urhStrength: "",
    urhWasting: "",
    urhSensation: "",
    urhReflexes: "",
  });

  const handleChangeURH = (e) => {
    let { name, value } = e.target;
    setUpperRightHandLimbs({ ...upperRightHandLimbs, [name]: value });
  };
  // set post value for Right Upper Hand
  let RUH = {
    assessmentTitle: "Right Upper Limb - Hand",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperRightHandLimbs.urhMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperRightHandLimbs.urhStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperRightHandLimbs.urhWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperRightHandLimbs.urhSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperRightHandLimbs.urhReflexes,
      },
    ],
  };
  // set post value for Right Upper Hand
  // setting value for RUH
  //for upper rigt limb

  // for upper left limb
  // setting value for LUS
  const [upperLeftSholdLimbs, setUpperLeftSholdLimbs] = useState({
    ulsMotionRange: "",
    ulsStrength: "",
    ulsWasting: "",
    ulsSensation: "",
    ulsReflexes: "",
  });

  const handleChangeULS = (e) => {
    let { name, value } = e.target;
    setUpperLeftSholdLimbs({ ...upperLeftSholdLimbs, [name]: value });
  };

  // set post value for Left Upper Shoulder
  let LUS = {
    assessmentTitle: "Left Upper Limb - Shoulder",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperLeftSholdLimbs.ulsMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperLeftSholdLimbs.ulsStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperLeftSholdLimbs.ulsWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperLeftSholdLimbs.ulsSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperLeftSholdLimbs.ulsReflexes,
      },
    ],
  };
  // set post value for Left Upper Shoulder
  // setting value for LUS

  // setting value for LUE
  const [upperLeftElbLimbs, setUpperLeftElbLimbs] = useState({
    uleMotionRange: "",
    uleStrength: "",
    uleWasting: "",
    uleSensation: "",
    uleReflexes: "",
  });

  const handleChangeULE = (e) => {
    let { name, value } = e.target;
    setUpperLeftElbLimbs({ ...upperLeftElbLimbs, [name]: value });
  };

  // set post value for Left Upper Elbow
  let LUE = {
    assessmentTitle: "Left Upper Limb - Elbow",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperLeftElbLimbs.uleMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperLeftElbLimbs.uleStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperLeftElbLimbs.uleWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperLeftElbLimbs.uleSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperLeftElbLimbs.uleReflexes,
      },
    ],
  };
  // set post value for Left Upper Elbow
  // setting value for LUE

  // setting value for LUW
  const [upperLeftWriLimbs, setUpperLeftWriLimbs] = useState({
    ulwMotionRange: "",
    ulwStrength: "",
    ulwWasting: "",
    ulwSensation: "",
    ulwReflexes: "",
  });

  const handleChangeULW = (e) => {
    let { name, value } = e.target;
    setUpperLeftWriLimbs({ ...upperLeftWriLimbs, [name]: value });
  };

  // set post value for Left Upper Wrist
  let LUW = {
    assessmentTitle: "Left Upper Limb - Wrist",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperLeftWriLimbs.ulwMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperLeftWriLimbs.ulwStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperLeftWriLimbs.ulwWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperLeftWriLimbs.ulwSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperLeftWriLimbs.ulwReflexes,
      },
    ],
  };
  // set post value for Left Upper Wrist
  // setting value for LUW

  // setting value for LUH
  const [upperLeftHandLimbs, setUpperLeftHandLimbs] = useState({
    ulhMotionRange: "",
    ulhStrength: "",
    ulhWasting: "",
    ulhSensation: "",
    ulhReflexes: "",
  });

  const handleChangeULH = (e) => {
    let { name, value } = e.target;
    setUpperLeftHandLimbs({ ...upperLeftHandLimbs, [name]: value });
  };

  // set post value for Left Upper Hand
  let LUH = {
    assessmentTitle: "Left Upper Limb - Hand",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: upperLeftHandLimbs.ulhMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: upperLeftHandLimbs.ulhStrength,
      },
      {
        observationName: "Wasting",
        observationValues: upperLeftHandLimbs.ulhWasting,
      },
      {
        observationName: "Sensation",
        observationValues: upperLeftHandLimbs.ulhSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: upperLeftHandLimbs.ulhReflexes,
      },
    ],
  };

  // set post value for Right Upper Hand
  // setting value for LUH
  // for upper left limb

  //for lower right limb
  // setting value for RLH
  const [lowerRightHipLimbs, setLowerRightHipLimbs] = useState({
    lrhMotionRange: "",
    lrhStrength: "",
    lrhWasting: "",
    lrhSensation: "",
    lrhReflexes: "",
  });

  const handleChangeLRH = (e) => {
    let { name, value } = e.target;
    setLowerRightHipLimbs({ ...lowerRightHipLimbs, [name]: value });
  };

  // set post value for Right Lower Hip
  let RLH = {
    assessmentTitle: "Right Lower Limb - Hip",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerRightHipLimbs.lrhMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerRightHipLimbs.lrhStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerRightHipLimbs.lrhWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerRightHipLimbs.lrhSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerRightHipLimbs.lrhReflexes,
      },
    ],
  };
  // set post value for Right Lower Hip
  // setting value for RLH

  // setting value for RLK
  const [lowerRightKneeLimbs, setLowerRightKneeLimbs] = useState({
    lrkMotionRange: "",
    lrkStrength: "",
    lrkWasting: "",
    lrkSensation: "",
    lrkReflexes: "",
  });

  const handleChangeLRK = (e) => {
    let { name, value } = e.target;
    setLowerRightKneeLimbs({ ...lowerRightKneeLimbs, [name]: value });
  };

  // set post value for Right Lower Knee
  let RLK = {
    assessmentTitle: "Right Lower Limb - Knee",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerRightKneeLimbs.lrkMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerRightKneeLimbs.lrkStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerRightKneeLimbs.lrkWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerRightKneeLimbs.lrkSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerRightKneeLimbs.lrkReflexes,
      },
    ],
  };
  // set post value for Right Lower Knee
  // setting value for RLK

  // setting value for RLA
  const [lowerRightAnkleLimbs, setLowerRightAnkleLimbs] = useState({
    lraMotionRange: "",
    lraStrength: "",
    lraWasting: "",
    lraSensation: "",
    lraReflexes: "",
  });

  const handleChangeLRA = (e) => {
    let { name, value } = e.target;
    setLowerRightAnkleLimbs({ ...lowerRightAnkleLimbs, [name]: value });
  };

  // set post value for Right Lower Ankle
  let RLA = {
    assessmentTitle: "Right Lower Limb - Ankle",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerRightAnkleLimbs.lraMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerRightAnkleLimbs.lraStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerRightAnkleLimbs.lraWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerRightAnkleLimbs.lraSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerRightAnkleLimbs.lraReflexes,
      },
    ],
  };
  // set post value for Right Lower Ankle
  // setting value for RLA

  // setting value for RLF
  const [lowerRightFootLimbs, setLowerRightFootLimbs] = useState({
    lrfMotionRange: "",
    lrfStrength: "",
    lrfWasting: "",
    lrfSensation: "",
    lrfReflexes: "",
  });

  const handleChangeLRF = (e) => {
    let { name, value } = e.target;
    setLowerRightFootLimbs({ ...lowerRightFootLimbs, [name]: value });
  };

  // set post value for Right Lower Foot
  let RLF = {
    assessmentTitle: "Right Lower Limb - Foot",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerRightFootLimbs.lrfMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerRightFootLimbs.lrfStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerRightFootLimbs.lrfWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerRightFootLimbs.lrfSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerRightFootLimbs.lrfReflexes,
      },
    ],
  };
  // set post value for Right Lower Foot
  // setting value for RLF
  //for lower right limb

  //for lower right limb
  // setting value for LLH
  const [lowerLeftHipLimbs, setLowerLeftHipLimbs] = useState({
    llhMotionRange: "",
    llhStrength: "",
    llhWasting: "",
    llhSensation: "",
    llhReflexes: "",
  });

  const handleChangeLLH = (e) => {
    let { name, value } = e.target;
    setLowerLeftHipLimbs({ ...lowerLeftHipLimbs, [name]: value });
  };

  // set post value for Left Lower Hip
  let LLH = {
    assessmentTitle: "Left Lower Limb - Hip",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerLeftHipLimbs.llhMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerLeftHipLimbs.llhStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerLeftHipLimbs.llhWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerLeftHipLimbs.llhSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerLeftHipLimbs.llhReflexes,
      },
    ],
  };
  // set post value for Left Lower Hip
  // setting value for LLH

  // setting value for LLK
  const [lowerLeftKneeLimbs, setLowerLeftKneeLimbs] = useState({
    llkMotionRange: "",
    llkStrength: "",
    llkWasting: "",
    llkSensation: "",
    llkReflexes: "",
  });

  const handleChangeLLK = (e) => {
    let { name, value } = e.target;
    setLowerLeftKneeLimbs({ ...lowerLeftKneeLimbs, [name]: value });
  };

  // set post value for Left Lower Knee
  let LLK = {
    assessmentTitle: "Left Lower Limb - Knee",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerLeftKneeLimbs.llkMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerLeftKneeLimbs.llkStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerLeftKneeLimbs.llkWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerLeftKneeLimbs.llkSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerLeftKneeLimbs.llkReflexes,
      },
    ],
  };
  // set post value for Left Lower Knee
  // setting value for LLK

  // setting value for LLA
  const [lowerLeftAnkleLimbs, setLowerLeftAnkleLimbs] = useState({
    llaMotionRange: "",
    llaStrength: "",
    llaWasting: "",
    llaSensation: "",
    llaReflexes: "",
  });

  const handleChangeLLA = (e) => {
    let { name, value } = e.target;
    setLowerLeftAnkleLimbs({ ...lowerLeftAnkleLimbs, [name]: value });
  };

  // set post value for Left Lower Ankle
  let LLA = {
    assessmentTitle: "Left Lower Limb - Ankle",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerLeftAnkleLimbs.llaMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerLeftAnkleLimbs.llaStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerLeftAnkleLimbs.llaWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerLeftAnkleLimbs.llaSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerLeftAnkleLimbs.llaReflexes,
      },
    ],
  };
  // set post value for Left Lower Ankle
  // setting value for LLA

  // setting value for LLF
  const [lowerLeftFootLimbs, setLowerLeftFootLimbs] = useState({
    llfMotionRange: "",
    llfStrength: "",
    llfWasting: "",
    llfSensation: "",
    llfReflexes: "",
  });

  const handleChangeLLF = (e) => {
    let { name, value } = e.target;
    setLowerLeftFootLimbs({ ...lowerLeftFootLimbs, [name]: value });
  };

  // set post value for Left Lower Foot
  let LLF = {
    assessmentTitle: "Left Lower Limb - Foot",
    observations: [
      {
        observationName: "Range of Motion",
        observationValues: lowerLeftFootLimbs.llfMotionRange,
      },
      {
        observationName: "Strength",
        observationValues: lowerLeftFootLimbs.llfStrength,
      },
      {
        observationName: "Wasting",
        observationValues: lowerLeftFootLimbs.llfWasting,
      },
      {
        observationName: "Sensation",
        observationValues: lowerLeftFootLimbs.llfSensation,
      },
      {
        observationName: "Reflexes",
        observationValues: lowerLeftFootLimbs.llfReflexes,
      },
    ],
  };
  // set post value for Left Lower Foot
  // setting value for LLF
  //for lower right limb

  let Musculoskeletal_data = {
    assessmentTitle: "Musculoskeletal Examination",
    observations: [
      {
        observationName: "SLR (Straight Leg Raise test)",
        observationValues: slr,
      },
    ],
  };

  let assessmentValue = [];
  if (
    consciousVal ||
    corporateVal ||
    comfortableVal ||
    toxicVal ||
    dyspneicVal ||
    buildVal ||
    nourishmentVal
  ) {
    assessmentValue.push(general_examination);
  }
  if (
    pallorVal ||
    icterusVal ||
    cyanosisVal ||
    clubbingVal ||
    koilonychiaVal ||
    lymphadenopathyVal ||
    pedalEdemaVal
  ) {
    assessmentValue.push(pickle);
  }
  if (
    respValues.respiratoryRate ||
    respValues.obsRespiratoryRate ||
    respValues.Tachypnea ||
    respValues.AccessoryMuscles ||
    respValues.InterRetractions
  ) {
    assessmentValue.push(respiratory_data);
  }
  if (
    asculValues.lungClr ||
    asculValues.rhonchi ||
    asculValues.wheezes ||
    asculValues.crepita ||
    asculValues.airEntry ||
    asculValues.breathMov ||
    asculValues.breathSou
  ) {
    assessmentValue.push(auscultation_data);
  }
  if (
    cardiacValues.pulseRate ||
    cardiacValues.rrrRate ||
    cardiacValues.obsRRR ||
    cardiacValues.tachyValue ||
    cardiacValues.bradycardia ||
    cardiacValues.jugularVenous ||
    cardiacValues.S1S2heard ||
    cardiacValues.noAdded
  ) {
    assessmentValue.push(cardiao_data);
  }
  if (
    gastroValues.inspection ||
    gastroValues.palpation ||
    gastroValues.tender ||
    gastroValues.tenderness ||
    gastroValues.hepatomegaly ||
    gastroValues.splenomegaly ||
    gastroValues.hernia ||
    gastroValues.bowelSounds ||
    gastroValues.murphySign ||
    gastroValues.mcBurneyPoint
  ) {
    assessmentValue.push(gastro_data);
  }
  if (
    ambulatoryValues.freely ||
    ambulatoryValues.assistant ||
    ambulatoryValues.wheelchair ||
    ambulatoryValues.walkingStick ||
    ambulatoryValues.gait ||
    ambulatoryValues.extraOcular
  ) {
    assessmentValue.push(ambulatory_data);
  }
  if (
    neckValues.flexion ||
    neckValues.bending ||
    neckValues.extension ||
    neckValues.spurling ||
    neckValues.rangeOfMove
  ) {
    assessmentValue.push(neck_data);
  }
  if (
    upperRightSholdLimbs.ursMotionRange ||
    upperRightSholdLimbs.ursStrength ||
    upperRightSholdLimbs.ursWasting ||
    upperRightSholdLimbs.ursSensation ||
    upperRightSholdLimbs.ursReflexes
  ) {
    assessmentValue.push(RUS);
  }
  if (
    upperRightElbLimbs.ureMotionRange ||
    upperRightElbLimbs.ureStrength ||
    upperRightElbLimbs.ureWasting ||
    upperRightElbLimbs.ureSensation ||
    upperRightElbLimbs.ureReflexes
  ) {
    assessmentValue.push(RUE);
  }
  if (
    upperRightWriLimbs.urwMotionRange ||
    upperRightWriLimbs.urwStrength ||
    upperRightWriLimbs.urwWasting ||
    upperRightWriLimbs.urwSensation ||
    upperRightWriLimbs.urwReflexes
  ) {
    assessmentValue.push(RUW);
  }
  if (
    upperRightHandLimbs.urhMotionRange ||
    upperRightHandLimbs.urhStrength ||
    upperRightHandLimbs.urhWasting ||
    upperRightHandLimbs.urhSensation ||
    upperRightHandLimbs.urhReflexes
  ) {
    assessmentValue.push(RUH);
  }
  if (
    upperLeftSholdLimbs.ulsMotionRange ||
    upperLeftSholdLimbs.ulsStrength ||
    upperLeftSholdLimbs.ulsWasting ||
    upperLeftSholdLimbs.ulsSensation ||
    upperLeftSholdLimbs.ulsReflexes
  ) {
    assessmentValue.push(LUS);
  }
  if (
    upperLeftElbLimbs.uleMotionRange ||
    upperLeftElbLimbs.uleStrength ||
    upperLeftElbLimbs.uleWasting ||
    upperLeftElbLimbs.uleSensation ||
    upperLeftElbLimbs.uleReflexes
  ) {
    assessmentValue.push(LUE);
  }
  if (
    upperLeftWriLimbs.ulwMotionRange ||
    upperLeftWriLimbs.ulwStrength ||
    upperLeftWriLimbs.ulwWasting ||
    upperLeftWriLimbs.ulwSensation ||
    upperLeftWriLimbs.ulwReflexes
  ) {
    assessmentValue.push(LUW);
  }
  if (
    upperLeftHandLimbs.ulhMotionRange ||
    upperLeftHandLimbs.ulhStrength ||
    upperLeftHandLimbs.ulhWasting ||
    upperLeftHandLimbs.ulhSensation ||
    upperLeftHandLimbs.ulhReflexes
  ) {
    assessmentValue.push(LUH);
  }
  if (
    lowerRightHipLimbs.lrhMotionRange ||
    lowerRightHipLimbs.lrhStrength ||
    lowerRightHipLimbs.lrhWasting ||
    lowerRightHipLimbs.lrhSensation ||
    lowerRightHipLimbs.lrhReflexes
  ) {
    assessmentValue.push(RLH);
  }
  if (
    lowerRightKneeLimbs.lrkMotionRange ||
    lowerRightKneeLimbs.lrkStrength ||
    lowerRightKneeLimbs.lrkWasting ||
    lowerRightKneeLimbs.lrkSensation ||
    lowerRightKneeLimbs.lrkReflexes
  ) {
    assessmentValue.push(RLK);
  }
  if (
    lowerRightAnkleLimbs.lraMotionRange ||
    lowerRightAnkleLimbs.lraStrength ||
    lowerRightAnkleLimbs.lraWasting ||
    lowerRightAnkleLimbs.lraSensation ||
    lowerRightAnkleLimbs.lraReflexes
  ) {
    assessmentValue.push(RLA);
  }
  if (
    lowerRightFootLimbs.lrfMotionRange ||
    lowerRightFootLimbs.lrfStrength ||
    lowerRightFootLimbs.lrfWasting ||
    lowerRightFootLimbs.lrfSensation ||
    lowerRightFootLimbs.lrfReflexes
  ) {
    assessmentValue.push(RLF);
  }
  if (
    lowerLeftHipLimbs.llhMotionRange ||
    lowerLeftHipLimbs.llhStrength ||
    lowerLeftHipLimbs.llhWasting ||
    lowerLeftHipLimbs.llhSensation ||
    lowerLeftHipLimbs.llhReflexes
  ) {
    assessmentValue.push(LLH);
  }
  if (
    lowerLeftKneeLimbs.llkMotionRange ||
    lowerLeftKneeLimbs.llkStrength ||
    lowerLeftKneeLimbs.llkWasting ||
    lowerLeftKneeLimbs.llkSensation ||
    lowerLeftKneeLimbs.llkReflexes
  ) {
    assessmentValue.push(LLK);
  }
  if (
    lowerLeftAnkleLimbs.llaMotionRange ||
    lowerLeftAnkleLimbs.llaStrength ||
    lowerLeftAnkleLimbs.llaWasting ||
    lowerLeftAnkleLimbs.llaSensation ||
    lowerLeftAnkleLimbs.llaReflexes
  ) {
    assessmentValue.push(LLA);
  }
  if (
    lowerLeftFootLimbs.llfMotionRange ||
    lowerLeftFootLimbs.llfStrength ||
    lowerLeftFootLimbs.llfWasting ||
    lowerLeftFootLimbs.llfSensation ||
    lowerLeftFootLimbs.llfReflexes
  ) {
    assessmentValue.push(LLF);
  }
  if (slr) {
    assessmentValue.push(Musculoskeletal_data);
  }

  const PresentTime = new Date();
  let parsedDate = moment(PresentTime, "DD.MM.YYYY H:mm:ss");
  let EffectiveDate = parsedDate.toISOString();
  function savePhysicalData() {
    if (assessmentValue.length != 0) {
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

      dispatch(loadPostPhyExamList(requestOptions, getPhydataByEnId, EncID));
    } else {
      Tostify.notifySuccess("You haven't selected anything...!");
    }
  }

  function removePhysicalData() {
    setConsciousVal("");
    setCorporateVal("");
    setComfortableVal("");
    setToxicVal("");
    setDyspneicVal("");
    setBuildVal("");
    setNourishmentVal("");
    setPallorVal("");
    setIcterusVal("");
    setCyanosisVal("");
    setClubbingVal("");
    setKoilonychiaVal("");
    setLymphadenopathyVal("");
    setPedalEdemaVal("");
    setRespValues({
      respiratoryRate: "",
      obsRespiratoryRate: "",
      Tachypnea: "",
      AccessoryMuscles: "",
      InterRetractions: "",
    });
    setAsculValues({
      lungClr: "",
      rhonchi: "",
      wheezes: "",
      crepita: "",
      airEntry: "",
      breathMov: "",
      breathSou: "",
    });
    setCardiacValues({
      pulseRate: "",
      rrrRate: "",
      obsRRR: "",
      tachyValue: "",
      bradycardia: "",
      jugularVenous: "",
      S1S2heard: "",
      noAdded: "",
    });
    setGastroValues({
      inspection: "",
      palpation: "",
      tender: "",
      tenderness: "",
      hepatomegaly: "",
      splenomegaly: "",
      hernia: "",
      bowelSounds: "",
      murphySign: "",
      mcBurneyPoint: "",
    });
    setAmbulatoryValues({
      freely: "",
      assistant: "",
      wheelchair: "",
      walkingStick: "",
      gait: "",
      extraOcular: "",
    });
    setNeckValues({
      flexion: "",
      bending: "",
      extension: "",
      spurling: "",
      rangeOfMove: "",
    });
    setUpperRightSholdLimbs({
      ursMotionRange: "",
      ursStrength: "",
      ursWasting: "",
      ursSensation: "",
      ursReflexes: "",
    });
    setUpperRightElbLimbs({
      ureMotionRange: "",
      ureStrength: "",
      ureWasting: "",
      ureSensation: "",
      ureReflexes: "",
    });
    setUpperRightWriLimbs({
      urwMotionRange: "",
      urwStrength: "",
      urwWasting: "",
      urwSensation: "",
      urwReflexes: "",
    });
    setUpperRightHandLimbs({
      urhMotionRange: "",
      urhStrength: "",
      urhWasting: "",
      urhSensation: "",
      urhReflexes: "",
    });
    setUpperLeftSholdLimbs({
      ulsMotionRange: "",
      ulsStrength: "",
      ulsWasting: "",
      ulsSensation: "",
      ulsReflexes: "",
    });
    setUpperLeftElbLimbs({
      uleMotionRange: "",
      uleStrength: "",
      uleWasting: "",
      uleSensation: "",
      uleReflexes: "",
    });
    setUpperLeftWriLimbs({
      ulwMotionRange: "",
      ulwStrength: "",
      ulwWasting: "",
      ulwSensation: "",
      ulwReflexes: "",
    });
    setUpperLeftHandLimbs({
      ulhMotionRange: "",
      ulhStrength: "",
      ulhWasting: "",
      ulhSensation: "",
      ulhReflexes: "",
    });
    setLowerRightHipLimbs({
      lrhMotionRange: "",
      lrhStrength: "",
      lrhWasting: "",
      lrhSensation: "",
      lrhReflexes: "",
    });
    setLowerRightKneeLimbs({
      lrkMotionRange: "",
      lrkStrength: "",
      lrkWasting: "",
      lrkSensation: "",
      lrkReflexes: "",
    });
    setLowerRightAnkleLimbs({
      lraMotionRange: "",
      lraStrength: "",
      lraWasting: "",
      lraSensation: "",
      lraReflexes: "",
    });
    setLowerRightFootLimbs({
      lrfMotionRange: "",
      lrfStrength: "",
      lrfWasting: "",
      lrfSensation: "",
      lrfReflexes: "",
    });
    setLowerLeftHipLimbs({
      llhMotionRange: "",
      llhStrength: "",
      llhWasting: "",
      llhSensation: "",
      llhReflexes: "",
    });
    setLowerLeftKneeLimbs({
      llkMotionRange: "",
      llkStrength: "",
      llkWasting: "",
      llkSensation: "",
      llkReflexes: "",
    });
    setLowerLeftAnkleLimbs({
      llaMotionRange: "",
      llaStrength: "",
      llaWasting: "",
      llaSensation: "",
      llaReflexes: "",
    });
    setLowerLeftFootLimbs({
      llfMotionRange: "",
      llfStrength: "",
      llfWasting: "",
      llfSensation: "",
      llfReflexes: "",
    });
    // Tostify.notifySuccess("Data Removed Successfully...!");
  }

  const [phyExamId, setPhyExamId] = useState("");
  useEffect(() => {
    if (EncID != undefined) {
      getPhydataByEnId(EncID);
    }
  }, [EncID]);

  function getPhydataByEnId() {
    fetch(
      `${constant.ApiUrl}/Observations/filter?page=&size=&type=Physical Examination&encounterId=${EncID}`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        if (res.data.length != 0) {
          setPhyExamId(res["data"][0]["_id"]);
          let phyArray = [];

          for (let i = 0; i < res["data"][0]["assessments"].length; i++) {
            phyArray.push(res["data"][0]["assessments"][i]);
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "General Examination"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName == "Conscious"
                ) {
                  setConsciousVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Cooperative"
                ) {
                  setCorporateVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Comfortable"
                ) {
                  setComfortableVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (phyArray[i]["observations"][j].observationName == "Toxic") {
                  setToxicVal(phyArray[i]["observations"][j].observationValues);
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Dyspneic"
                ) {
                  setDyspneicVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (phyArray[i]["observations"][j].observationName == "Build") {
                  setBuildVal(phyArray[i]["observations"][j].observationValues);
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Nourishment"
                ) {
                  setNourishmentVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] == "Pickle"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName == "Pallor"
                ) {
                  setPallorVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Icterus"
                ) {
                  setIcterusVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Cyanosis"
                ) {
                  setCyanosisVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Clubbing"
                ) {
                  setClubbingVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Koilonychia"
                ) {
                  setKoilonychiaVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Lymphadenopathy"
                ) {
                  setLymphadenopathyVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Pedal Edema"
                ) {
                  setPedalEdemaVal(
                    phyArray[i]["observations"][j].observationValues
                  );
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Respiratory"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Respiratory Rate"
                ) {
                  respValues.respiratoryRate =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Observed Respiratory Rate"
                ) {
                  respValues.obsRespiratoryRate =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Tachypnea"
                ) {
                  respValues.Tachypnea =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Accessory Muscles (Sternocleidomastoid)"
                ) {
                  respValues.AccessoryMuscles =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Intercostal Retractions"
                ) {
                  respValues.InterRetractions =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Auscultation"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Lungs Clear on Auscultation"
                ) {
                  asculValues.lungClr =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Rhonchi"
                ) {
                  asculValues.rhonchi =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wheezes"
                ) {
                  asculValues.wheezes =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Crepitations"
                ) {
                  asculValues.crepita =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Airway Entry"
                ) {
                  asculValues.airEntry =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Breath Movements Rt/Lt"
                ) {
                  asculValues.breathMov =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Breath Sounds Rt/Lt"
                ) {
                  asculValues.breathSou =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Cardiovascular System"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName == "Pulse Rate"
                ) {
                  cardiacValues.pulseRate =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Regular Rate & Rhythm (RRR)"
                ) {
                  cardiacValues.rrrRate =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Observed RRR"
                ) {
                  cardiacValues.obsRRR =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Tachycardia"
                ) {
                  cardiacValues.tachyValue =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Bradycardia"
                ) {
                  cardiacValues.bradycardia =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Jugular Venous Pulse"
                ) {
                  cardiacValues.jugularVenous =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "S1 S2 Heard"
                ) {
                  cardiacValues.S1S2heard =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "No Added Sounds and Murmurs"
                ) {
                  cardiacValues.noAdded =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Gastrointestinal System"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName == "Inspection"
                ) {
                  gastroValues.inspection =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Palpation"
                ) {
                  gastroValues.palpation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Tender"
                ) {
                  gastroValues.tender =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Tenderness in"
                ) {
                  gastroValues.tenderness =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Hepatomegaly"
                ) {
                  gastroValues.hepatomegaly =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Splenomegaly"
                ) {
                  gastroValues.splenomegaly =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Hernia"
                ) {
                  gastroValues.hernia =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Bowel Sounds"
                ) {
                  gastroValues.bowelSounds =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Murphy's Sign"
                ) {
                  gastroValues.murphySign =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "McBurney's Point"
                ) {
                  gastroValues.mcBurneyPoint =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }

            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Ambulatory"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Freely Ambulatory"
                ) {
                  ambulatoryValues.freely =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "With Assistant (Support)"
                ) {
                  ambulatoryValues.assistant =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "With Wheelchair"
                ) {
                  ambulatoryValues.wheelchair =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "With Walking Stick"
                ) {
                  ambulatoryValues.walkingStick =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (phyArray[i]["observations"][j].observationName == "Gait") {
                  ambulatoryValues.gait =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Extra-ocular Movements"
                ) {
                  ambulatoryValues.extraOcular =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (res["data"][0]["assessments"][i]["assessmentTitle"] == "NECK") {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName == "Flexion"
                ) {
                  neckValues.flexion =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Lateral Bending"
                ) {
                  neckValues.bending =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Extension"
                ) {
                  neckValues.extension =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Spurling's Sign"
                ) {
                  neckValues.spurling =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Movement"
                ) {
                  neckValues.rangeOfMove =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Upper Limb - Shoulder"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperRightSholdLimbs.ursMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperRightSholdLimbs.ursStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperRightSholdLimbs.ursWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperRightSholdLimbs.ursSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperRightSholdLimbs.ursReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Upper Limb - Elbow"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperRightElbLimbs.ureMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperRightElbLimbs.ureStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperRightElbLimbs.ureWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperRightElbLimbs.ureSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperRightElbLimbs.ureReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Upper Limb - Wrist"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperRightWriLimbs.urwMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperRightWriLimbs.urwStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperRightWriLimbs.urwWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperRightWriLimbs.urwSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperRightWriLimbs.urwReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Upper Limb - Hand"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperRightHandLimbs.urhMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperRightHandLimbs.urhStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperRightHandLimbs.urhWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperRightHandLimbs.urhSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperRightHandLimbs.urhReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Upper Limb - Shoulder"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperLeftSholdLimbs.ulsMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperLeftSholdLimbs.ulsStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperLeftSholdLimbs.ulsWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperLeftSholdLimbs.ulsSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperLeftSholdLimbs.ulsReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Upper Limb - Elbow"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperLeftElbLimbs.uleMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperLeftElbLimbs.uleStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperLeftElbLimbs.uleWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperLeftElbLimbs.uleSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperLeftElbLimbs.uleReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Upper Limb - Wrist"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperLeftWriLimbs.ulwMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperLeftWriLimbs.ulwStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperLeftWriLimbs.ulwWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperLeftWriLimbs.ulwSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperLeftWriLimbs.ulwReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Upper Limb - Hand"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  upperLeftHandLimbs.ulhMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  upperLeftHandLimbs.ulhStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  upperLeftHandLimbs.ulhWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  upperLeftHandLimbs.ulhSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  upperLeftHandLimbs.ulhReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Lower Limb - Hip"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerRightHipLimbs.lrhMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerRightHipLimbs.lrhStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerRightHipLimbs.lrhWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerRightHipLimbs.lrhSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerRightHipLimbs.lrhReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Lower Limb - Knee"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerRightKneeLimbs.lrkMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerRightKneeLimbs.lrkStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerRightKneeLimbs.lrkWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerRightKneeLimbs.lrkSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerRightKneeLimbs.lrkReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Lower Limb - Ankle"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerRightAnkleLimbs.lraMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerRightAnkleLimbs.lraStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerRightAnkleLimbs.lraWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerRightAnkleLimbs.lraSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerRightAnkleLimbs.lraReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Right Lower Limb - Foot"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerRightFootLimbs.lrfMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerRightFootLimbs.lrfStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerRightFootLimbs.lrfWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerRightFootLimbs.lrfSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerRightFootLimbs.lrfReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Lower Limb - Hip"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerLeftHipLimbs.llhMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerLeftHipLimbs.llhStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerLeftHipLimbs.llhWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerLeftHipLimbs.llhSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerLeftHipLimbs.llhReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Lower Limb - Knee"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerLeftKneeLimbs.llkMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerLeftKneeLimbs.llkStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerLeftKneeLimbs.llkWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerLeftKneeLimbs.llkSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerLeftKneeLimbs.llkReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Lower Limb - Ankle"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerLeftAnkleLimbs.llaMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerLeftAnkleLimbs.llaStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerLeftAnkleLimbs.llaWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerLeftAnkleLimbs.llaSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerLeftAnkleLimbs.llaReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Left Lower Limb - Foot"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "Range of Motion"
                ) {
                  lowerLeftFootLimbs.llfMotionRange =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Strength"
                ) {
                  lowerLeftFootLimbs.llfStrength =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Wasting"
                ) {
                  lowerLeftFootLimbs.llfWasting =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Sensation"
                ) {
                  lowerLeftFootLimbs.llfSensation =
                    phyArray[i]["observations"][j].observationValues;
                }
                if (
                  phyArray[i]["observations"][j].observationName == "Reflexes"
                ) {
                  lowerLeftFootLimbs.llfReflexes =
                    phyArray[i]["observations"][j].observationValues;
                }
              }
            }
            if (
              res["data"][0]["assessments"][i]["assessmentTitle"] ==
              "Musculoskeletal Examination"
            ) {
              for (
                let j = 0;
                j < res["data"][0]["assessments"][i]["observations"].length;
                j++
              ) {
                if (
                  phyArray[i]["observations"][j].observationName ==
                  "SLR (Straight Leg Raise test)"
                ) {
                  setSLR(phyArray[i]["observations"][j].observationValues);
                }
              }
            }

            var ele = document.getElementsByName("Process");
            let result2 = phyArray.filter((assement) =>
              assignSystemicArray.find(
                (title) => title.name == assement.assessmentTitle
              )
            );
            // assignSystemicArray.every(title => title.name.includes(assement.assessmentTitle)))
            // let ret = secondary.filter((x) => firstArray.find((y) => x.chapId === y.value));
            let selectedType = [];
            for (var k = 0; k < ele.length; k++) {
              for (var l = 0; l < result2.length; l++) {
                if (ele[k].value == result2[l]["assessmentTitle"]) {
                  ele[k].checked = true;
                  selectedType.push({
                    value: result2[l]["assessmentTitle"],
                    status: true,
                  });
                }
              }
            }
            setPhyAssignSystemicArray(selectedType);
          }
        }
      });
  }
  //     }
  // }, [EncID])

  // Update Observation value
  const updateobservation = (e) => {
    if (assessmentValue.length != 0) {
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

      dispatch(
        loadUpdatePhyExamList(
          requestOptions,
          phyExamId,
          getPhydataByEnId,
          EncID
        )
      );
    } else {
      Tostify.notifySuccess("You haven't selected anything...!");
    }
  };
  // Update Observation value

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
        modType="physical"
      />
      <ToastContainer />
      <Row className="assessment-head-section">
        <Col lg={6}>
          <h5 className="physical-head">Physical Examination</h5>
        </Col>
        <Col lg={6}>
          <Button
            varient="light"
            className="float-right view-prev-details"
            onClick={physicalShow}
          >
            <i className="fa fa-undo prev-icon"></i>
            Previous Physical Examination
          </Button>
        </Col>
      </Row>
      <div className="physical-accordion">
        <Accordion id="reg-accordion">
          <Accordion.Item eventKey="0" id="custom-acco-item">
            <Accordion.Header className="general-roles-head">
              <span className="physical-staff">General Examination </span>
            </Accordion.Header>
            <Accordion.Body className="physical-roles-body">
              <GeneralExamination
                phyExamItems={phyExamItems}
                setGenPallor={setGenPallor}
                consciousVal={consciousVal}
                corporateVal={corporateVal}
                comfortableVal={comfortableVal}
                toxicVal={toxicVal}
                dyspneicVal={dyspneicVal}
                buildVal={buildVal}
                nourishmentVal={nourishmentVal}
                pallorVal={pallorVal}
                icterusVal={icterusVal}
                cyanosisVal={cyanosisVal}
                clubbingVal={clubbingVal}
                koilonychiaVal={koilonychiaVal}
                lymphadenopathyVal={lymphadenopathyVal}
                pedalEdemaVal={pedalEdemaVal}
              />
            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
        <hr />
        <Accordion id="reg-accordion">
          <Accordion.Item eventKey="0" id="custom-acco-item">
            <Accordion.Header className="physical-roles-head">
              <span className="physical-staff">
                Physical Examination/Systemic Examination{" "}
              </span>
            </Accordion.Header>
            <Accordion.Body className="physical-roles-body">
              <div className="assign-complaint">
                <div className="physical-direct">
                  <p className="total-physical-process">
                    <i className="bi bi-info-square-fill add-phc-icon"></i>
                    <b>Select</b> the type of physical examination to enter
                    details
                  </p>
                  <div>
                    <img
                      src="../img/super/arrowdown.png"
                      className="assign-image"
                    />
                  </div>
                </div>
              </div>
              <div className="responsibility-buttons">
                {assignSystemicArray.map((Process, i) => (
                  <React.Fragment key={i}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Process"
                      id={"psexa" + i}
                      value={Process.name}
                      onChange={setPhyAssignSystemic}
                    />
                    <label className="for-checkbox-tools" htmlFor={"psexa" + i}>
                      <span className={isChecked(Process)}>{Process.name}</span>
                    </label>
                  </React.Fragment>
                ))}
              </div>

              {phyAssignSystemicArray.length != 0 &&
                phyAssignSystemicArray.map((item, i) => (
                  <React.Fragment key={i}>
                    {item.status && item.value === "Respiratory" && (
                      <div className="assessment-component-section">
                        <Respiratory
                          phyExamItems={phyExamItems}
                          respValues={respValues}
                          handleChange={handleChangeResp}
                        />
                      </div>
                    )}
                    {item.status && item.value === "Auscultation" && (
                      <div className="assessment-component-section">
                        <Auscultation
                          phyExamItems={phyExamItems}
                          asculValues={asculValues}
                          handleChange={handleChangeAsc}
                        />
                      </div>
                    )}
                    {item.status && item.value === "Cardiovascular System" && (
                      <div className="assessment-component-section">
                        <CardioVascular
                          phyExamItems={phyExamItems}
                          cardiacValues={cardiacValues}
                          handleChange={handleChangeCardio}
                        />
                      </div>
                    )}
                    {item.status &&
                      item.value === "Gastrointestinal System" && (
                        <div className="assessment-component-section">
                          <Gastrointestinal
                            phyExamItems={phyExamItems}
                            gastroValues={gastroValues}
                            handleChange={handleChangeGastro}
                          />
                        </div>
                      )}
                    {item.status &&
                      item.value === "Musculoskeletal Examination" && (
                        <div>
                          <div className="assessment-component-section">
                            <Musculoskeletal
                              phyExamItems={phyExamItems}
                              ambulatoryValues={ambulatoryValues}
                              handleChange={handleChangeAmbu}
                              neckValues={neckValues}
                              handleChangeNeck={handleChangeNeck}
                            />
                          </div>
                          <div className="assessment-table-section">
                            <LimbsTable
                              phyExamItems={phyExamItems}
                              Name_of_Joint={Name_of_Upper_Joint}
                              upperRightSholdLimbs={upperRightSholdLimbs}
                              handleChangeURS={handleChangeURS}
                              upperRightElbLimbs={upperRightElbLimbs}
                              handleChangeURE={handleChangeURE}
                              upperRightWriLimbs={upperRightWriLimbs}
                              handleChangeURW={handleChangeURW}
                              upperRightHandLimbs={upperRightHandLimbs}
                              handleChangeURH={handleChangeURH}
                              table_type="Right Upper"
                              header="Right Upper Limb"
                            />
                          </div>
                          <div className="assessment-table-section">
                            <LimbsTable
                              phyExamItems={phyExamItems}
                              Name_of_Joint={Name_of_Upper_Joint}
                              upperLeftSholdLimbs={upperLeftSholdLimbs}
                              handleChangeULS={handleChangeULS}
                              upperLeftElbLimbs={upperLeftElbLimbs}
                              handleChangeULE={handleChangeULE}
                              upperLeftWriLimbs={upperLeftWriLimbs}
                              handleChangeULW={handleChangeULW}
                              upperLeftHandLimbs={upperLeftHandLimbs}
                              handleChangeULH={handleChangeULH}
                              table_type="Left Upper"
                              header="Left Upper Limb"
                            />
                          </div>
                          <div className="assessment-table-section">
                            <LimbsTable
                              phyExamItems={phyExamItems}
                              Name_of_Joint={Name_of_Lower_Joint}
                              lowerRightHipLimbs={lowerRightHipLimbs}
                              handleChangeLRH={handleChangeLRH}
                              lowerRightKneeLimbs={lowerRightKneeLimbs}
                              handleChangeLRK={handleChangeLRK}
                              lowerRightAnkleLimbs={lowerRightAnkleLimbs}
                              handleChangeLRA={handleChangeLRA}
                              lowerRightFootLimbs={lowerRightFootLimbs}
                              handleChangeLRF={handleChangeLRF}
                              table_type="Right Lower"
                              header="Right Lower Limb"
                            />
                          </div>
                          <div className="assessment-table-section">
                            <LimbsTable
                              phyExamItems={phyExamItems}
                              Name_of_Joint={Name_of_Lower_Joint}
                              lowerLeftHipLimbs={lowerLeftHipLimbs}
                              handleChangeLLH={handleChangeLLH}
                              lowerLeftKneeLimbs={lowerLeftKneeLimbs}
                              handleChangeLLK={handleChangeLLK}
                              lowerLeftAnkleLimbs={lowerLeftAnkleLimbs}
                              handleChangeLLA={handleChangeLLA}
                              lowerLeftFootLimbs={lowerLeftFootLimbs}
                              handleChangeLLF={handleChangeLLF}
                              table_type="Left Lower"
                              header="Left Lower Limb"
                            />
                          </div>
                          <div className="assessment-component-section">
                            <Row className="assessment-button-pro-section">
                              <Col lg={3}>
                                <div className="assessments-button phy-general">
                                  <Form.Group
                                    className="mb-3_fname"
                                    controlId="exampleForm.FName"
                                  >
                                    <Form.Label className="require">
                                      SLR (Straight Leg Raise test){" "}
                                    </Form.Label>
                                  </Form.Group>
                                </div>
                              </Col>
                              <Col lg={3}>
                                <div className="assessments-button phy-general">
                                  <Form.Group
                                    className="mb-3_fname"
                                    controlId="exampleForm.FName"
                                  >
                                    <Form.Select
                                      aria-label="Default select example"
                                      value={slr || ""}
                                      name="slr"
                                      onChange={(e) => setSLR(e.target.value)}
                                    >
                                      <option value="" hidden>
                                        Select{" "}
                                      </option>
                                      {phyExamItems.map((formItem, i) => (
                                        <React.Fragment key={i}>
                                          {formItem.groupName ==
                                            "SLR (Straight Leg Raise test)" && (
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
                          </div>
                        </div>
                      )}
                  </React.Fragment>
                ))}
            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
      </div>
      <div className="assessment-form-button-section">
        <div className="save-btn-section">
          <SaveButton
            butttonClick={removePhysicalData}
            class_name="regBtnPC"
            button_name="Cancel"
          />
        </div>
        <div className="save-btn-section">
          {phyExamId ? (
            <SaveButton
              class_name="regBtnN"
              butttonClick={(e) => updateobservation(phyExamId)}
              button_name="Update"
            />
          ) : (
            <SaveButton
              butttonClick={savePhysicalData}
              class_name="regBtnN"
              button_name="Save"
            />
          )}
        </div>
      </div>
    </React.Fragment>
  );
}
