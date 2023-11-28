import * as types from "./formUtilityActionType";
import axios from "axios";
import moment from "moment";
import { useHistory } from "react-router-dom";
import * as constant from "../components/ConstUrl/constant";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";

const getRegFormItems = (regFormItems) => ({
  type: types.Get_regForm_Data,
  payload: regFormItems,
});
const getProductMasterItems = (productMasterItems) => ({
  type: types.Get_ProductMaster_Data,
  payload: productMasterItems,
});
const getcreatevisit = (createvisitdata) => ({
  type: types.Creat_visit_Dropdown,
  payload: createvisitdata,
});
const getMedHistoryItems = (medHistoryItems) => ({
  type: types.Get_Medical_History,
  payload: medHistoryItems,
});
const getPhyExamItems = (phyExamItems) => ({
  type: types.Get_Physical_Examination,
  payload: phyExamItems,
});
const getLabDropdown = (labDropdown) => ({
  type: types.Lab_Service_Dropdown,
  payload: labDropdown,
});
const getRefernceDropdown = (refrenceDropdown) => ({
  type: types.Refernce_Dropdown,
  payload: refrenceDropdown,
});
const getAllergyDropdown = (allergyDropdown) => ({
  type: types.Allergy_Dropdown,
  payload: allergyDropdown,
});
const getPresciptionDropdown = (prescDropdown) => ({
  type: types.Presciption_Dropdown,
  payload: prescDropdown,
});
const getTopicsDropdown = (topicsDropdown) => ({
  type: types.Topics_Dropdown,
  payload: topicsDropdown,
});
const getSocioDropdown = (socioDropdown) => ({
  type: types.Socio_Dropdown,
  payload: socioDropdown,
});
const getHouseHoldDropdown = (houseHoldDropdown) => ({
  type: types.HouseHold_Dropdown,
  payload: houseHoldDropdown,
});
const getHealthIdDropdown = (healthIdDropdown) => ({
  type: types.HealthId_Dropdown,
  payload: healthIdDropdown,
});

const getRMNCHA = (rmnchaDropdown) => ({
  type: types.RMNCHA_Dropdown,
  payload: rmnchaDropdown,
});
const getHealthAndWellness = (healthandwellnessDropdown) => ({
  type: types.HealthAndWellness_Dropdown,
  payload: healthandwellnessDropdown,
});
const getCommunicable = (communicableDropdown) => ({
  type: types.Communicable_Dropdown,
  payload: communicableDropdown,
});
const getEMR = (emrDropdown) => ({
  type: types.EMR_Dropdown,
  payload: emrDropdown,
});
const getDiagnosisDropdown = (diagnosysDropdown) => ({
  type: types.Diagnosis_Dropdown,
  payload: diagnosysDropdown,
});
const getSurgicalDropdown = (surgicalDropdown) => ({
  type: types.Surgical_Dropdown,
  payload: surgicalDropdown,
});

const getVillageAsstes = (villageAsstes) => ({
  type: types.VillageAsstes_Dropdown,
  payload: villageAsstes,
});

// For Admin module
const getFacilityDropdown = (facilityDropDown) => ({
  type: types.Get_Facility_DropDowns,
  payload: facilityDropDown,
});
// For Admin module

export const loadRegFormItems = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_RegisterPatient`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getRegFormItems(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadProductMaster = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_ProductSnomedCT`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getProductMasterItems(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};

export const loadcreatevisit = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_CreateVisit`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getcreatevisit(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadMedHistoryItems = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_MedicalHistory`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getMedHistoryItems(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadPhyExamItems = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_PhysicalExamination`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getPhyExamItems(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadLabDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_AddLabServices`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getLabDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadRefernceDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_AddReferenceRange`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getRefernceDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadAllergyDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_Allergy`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getAllergyDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadPresciptionDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_Prescription`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getPresciptionDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};

export const loadTopicsDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GIS_Mapping`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getTopicsDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadSocioDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_SocioEconomicSurvey`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getSocioDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadHouseHoldDropdown = (arrayTopicsValue) => {
  return function (dispatch) {
    let url;
    if (arrayTopicsValue == "Household") {
      url = `${constant.ApiUrl}/surveys/filter?surveyType=HouseHold&limit=1&page=1`;
    } else if (arrayTopicsValue == "Individual") {
      url = `${constant.ApiUrl}/surveys/filter?surveyType=Citizen&limit=2&page=1`;
    }
    fetch(url, serviceHeaders.getRequestOptions)
      .then((res) => res.json())
      .then((res) => {
        dispatch(getHouseHoldDropdown(res.data));
      })
      .catch((error) => console.log(error));
  };
};

export const loadHealthId = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_HealthId`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getHealthIdDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadRMNCHA = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_RMNCH+A`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getRMNCHA(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadHealthAndWellness = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_HealthAndWellnessSurveillance`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getHealthAndWellness(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadCommunicable = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_CommunicableDiseases`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getCommunicable(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};
export const loadEMR = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_EMR`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getEMR(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};

export const loadDiagnosisDropdown = (setPageLoader) => {
  return function (dispatch) {
    if (setPageLoader) {
      fetch(
        `${constant.ApiUrl}/formutility/EMR_ProvisionalAndFinalDiagnosis`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          dispatch(getDiagnosisDropdown(res.formItems));
          setPageLoader(false);
        })
        .catch((error) => console.log(error));
    } else {
      dispatch(getDiagnosisDropdown([]));
    }
  };
};
export const loadSurgicalDropdown = (setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_SurgicalHistory`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getSurgicalDropdown(res.formItems));
        setPageLoader(false);
      })
      .catch((error) => console.log(error));
  };
};

export const loadFacilityDropdown = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/Admin_Module`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getFacilityDropdown(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};

export const loadVillageAsstes = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/GISMapping_VillageAsstes`,
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getVillageAsstes(res.formItems));
      })
      .catch((error) => console.log(error));
  };
};

// export const
