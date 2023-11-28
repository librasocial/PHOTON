import * as types from "./formUtilityActionType";

const initialState = {
  regFormItems: [],
  medHistoryItems: [],
  phyExamItems: [],
  labDropdown: [],
  refrenceDropdown: [],
  allergyDropdown: [],
  prescDropdown: [],
  topicsDropdown: [],
  socioDropdown: [],
  houseHoldDropdown: [],
  healthIdDropdown: [],
  rmnchaDropdown: [],
  communicableDropdown: [],
  emrDropdown: [],
  healthandwellness: [],
  diagnosysDropdown: [],
  surgicalDropdown: [],
  villageAsstes: [],
  createvisitdata: [],

  // for admin module
  facilityDropDown: [],

  productMasterItems: [],
  // for admin module
  loading: true,
};

const formUtiltyReducers = (state = initialState, action) => {
  switch (action.type) {
    case types.Get_regForm_Data:
      return {
        ...state,
        regFormItems: action.payload,
        loading: false,
      };

    case types.Get_ProductMaster_Data:
      return {
        ...state,
        productMasterItems: action.payload,
        loading: false,
      };
    case types.Creat_visit_Dropdown:
      return {
        ...state,
        createvisitdata: action.payload,
        loading: false,
      };
    case types.Get_Medical_History:
      return {
        ...state,
        medHistoryItems: action.payload,
        loading: false,
      };
    case types.Get_Physical_Examination:
      return {
        ...state,
        phyExamItems: action.payload,
        loading: false,
      };
    case types.Lab_Service_Dropdown:
      return {
        ...state,
        labDropdown: action.payload,
        loading: false,
      };
    case types.Refernce_Dropdown:
      return {
        ...state,
        refrenceDropdown: action.payload,
        loading: false,
      };
    case types.Allergy_Dropdown:
      return {
        ...state,
        allergyDropdown: action.payload,
        loading: false,
      };
    case types.Presciption_Dropdown:
      return {
        ...state,
        prescDropdown: action.payload,
        loading: false,
      };
    case types.Topics_Dropdown:
      return {
        ...state,
        topicsDropdown: action.payload,
        loading: false,
      };
    case types.Socio_Dropdown:
      return {
        ...state,
        socioDropdown: action.payload,
        loading: false,
      };
    case types.HouseHold_Dropdown:
      return {
        ...state,
        houseHoldDropdown: action.payload,
        loading: false,
      };
    case types.HealthId_Dropdown:
      return {
        ...state,
        healthIdDropdown: action.payload,
        loading: false,
      };

    case types.RMNCHA_Dropdown:
      return {
        ...state,
        rmnchaDropdown: action.payload,
        loading: false,
      };
    case types.HealthAndWellness_Dropdown:
      return {
        ...state,
        healthandwellnessDropdown: action.payload,
        loading: false,
      };
    case types.Communicable_Dropdown:
      return {
        ...state,
        communicableDropdown: action.payload,
        loading: false,
      };
    case types.EMR_Dropdown:
      return {
        ...state,
        emrDropdown: action.payload,
        loading: false,
      };
    case types.Diagnosis_Dropdown:
      return {
        ...state,
        diagnosysDropdown: action.payload,
        loading: false,
      };
    case types.Surgical_Dropdown:
      return {
        ...state,
        surgicalDropdown: action.payload,
        loading: false,
      };
    case types.VillageAsstes_Dropdown:
      return {
        ...state,
        villageAsstes: action.payload,
        loading: false,
      };

    // For Admin module
    case types.Get_Facility_DropDowns:
      return {
        ...state,
        facilityDropDown: action.payload,
        loading: false,
      };
    // For Admin module
    default:
      return state;
  }
};
export default formUtiltyReducers;
