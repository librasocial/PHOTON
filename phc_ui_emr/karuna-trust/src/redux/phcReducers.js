import * as types from "./phcActionType";

const initialState = {
  phcDetails: {},
  stateList: [],
  districtList: [],
  districtListForPresent: [],
  districtListForPermanent: [],
  talukList: [],
  talukListForPresent: [],
  talukListForPermanent: [],
  villageList: [],
  villageListForPresent: [],
  villageListForPermanent: [],
  gramPanchayatList: [],
  villPanchayatList: [],
  sunCenterDetails: {},
  staffDetails: {},
  staffData: [],

  // fetch gram list
  gramPanchayatListPhc: [],
  subcentreVillages: [],

  facPhcData: [],
  facSCData: [],

  allActivityList: [],
  activityList: [],
  allProcessList: [],

  assignedOwner: [],

  loading: true,
};

const phcReducers = (state = initialState, action) => {
  switch (action.type) {
    case types.Create_Phc:
    case types.Update_Phc:
    case types.Get_Phc_Details:
      return {
        ...state,
        phcDetails: action.payload,
        loading: false,
      };
    case types.Create_PhcRel:
    case types.Create_Sub_Center:
    case types.Create_SubCenterRel:
    case types.Update_Sub_Center:
    case types.Get_Sub_Center_Details:
      return {
        ...state,
        sunCenterDetails: action.payload,
        loading: false,
      };
    case types.Get_Subcentre_Villages:
      return {
        ...state,
        subcentreVillages: action.payload,
        loading: false,
      };

    case types.Create_Facility_PHC:
    case types.Update_Facility_PHC:
    case types.Create_Facility_SC:
    case types.Create_Facility_Rel:
    case types.Create_Facility_RelSC:

    case types.Get_Fac_PHC:
      return {
        ...state,
        facPhcData: action.payload,
        loading: false,
      };
    case types.Get_Fac_SC:
      return {
        ...state,
        facSCData: action.payload,
        loading: false,
      };

    case types.Create_Staff_Member:
    case types.Create_Cog_User:
    case types.Update_Staff_Member:
    case types.Get_Staff_Member_Details:
      return {
        ...state,
        staffDetails: action.payload,
        loading: false,
      };
    case types.Get_All_Staff_Member:
      return {
        ...state,
        staffData: action.payload,
        loading: false,
      };
    case types.Get_All_State:
      return {
        ...state,
        stateList: action.payload,
        loading: false,
      };
    case types.Get_All_District:
      return {
        ...state,
        districtList: action.payload,
        loading: false,
      };
    case types.Get_All_DistrictForPresent:
      return {
        ...state,
        districtListForPresent: action.payload,
        loading: false,
      };
    case types.Get_All_DistrictForPermanent:
      return {
        ...state,
        districtListForPermanent: action.payload,
        loading: false,
      };
    case types.Get_All_Taluk:
      return {
        ...state,
        talukList: action.payload,
        loading: false,
      };
    case types.Get_All_TalukForPresent:
      return {
        ...state,
        talukListForPresent: action.payload,
        loading: false,
      };
    case types.Get_All_TalukForPermanent:
      return {
        ...state,
        talukListForPermanent: action.payload,
        loading: false,
      };
    case types.Get_All_GramPanch:
      return {
        ...state,
        gramPanchayatList: action.payload,
        loading: false,
      };
    case types.Get_All_Village:
      return {
        ...state,
        villageList: action.payload,
        loading: false,
      };
    case types.Get_All_VillageForPresent:
      return {
        ...state,
        villageListForPresent: action.payload,
        loading: false,
      };
    case types.Get_All_VillageForPermanent:
      return {
        ...state,
        villageListForPermanent: action.payload,
        loading: false,
      };
    case types.Get_All_Vill_GramPanch:
      return {
        ...state,
        villPanchayatList: action.payload,
        loading: false,
      };

    case types.Get_Phc_GramPanchayat:
      return {
        ...state,
        gramPanchayatListPhc: action.payload,
        loading: false,
      };

    // Reducer for owner and activities
    case types.Create_Owner:
    case types.Get_All_Activity:
      return {
        ...state,
        allActivityList: action.payload,
        loading: false,
      };
    case types.Get_Assign_Activity:
      return {
        ...state,
        activityList: action.payload,
        loading: false,
      };
    case types.Get_All_Process:
      return {
        ...state,
        allProcessList: action.payload,
        loading: false,
      };
    // Reducer for owner and activities
    // Reducer for activity
    case types.Create_Activity_Phc:

    case types.Get_Assign_Owner:
      return {
        ...state,
        assignedOwner: action.payload,
        loading: false,
      };
    // Reducer for activity
    default:
      return state;
  }
};
export default phcReducers;
