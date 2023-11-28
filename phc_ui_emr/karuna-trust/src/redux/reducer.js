import * as types from "./actionType";

const initialState = {
  usersList: [],
  vitalsList: [],
  doctorsList: [],
  labTechList: [],
  docUsersList: [],
  // Form Utility
  phyExamList: {},
  // Form Utility
  PharmaProdList: [],
  pharmacistList: [],
  nurseList: [],
  tokenList: [],
  PharmaSearchList: [],
  PharmaProdLength: {},
  PharmaInwardLength: {},
  SinglePharmacy: {},
  singlePatDet: {},
  singlePatientDetails: {},

  // state for lab orders
  labOrderList: [],
  labOrderListByUhid: [],
  allLabOrderList: [],
  labOrderListByDate: [],
  // state for lab orders

  // state for orders
  PurchaseOrderList: [],
  OrderListByStatus: [],
  OrdersBySearch: [],
  SingleOrder: {},
  // state for pharma dashboard
  regPharmacy: [],
  AllPharmaList: [],
  dispenceForId: [],
  patDetails: {},
  orderDetails: {},
  dispOrderId: "",

  // State for Pharma Inwards
  InwardsList: [],
  inventory: [],
  expireData: [],
  inwardsById: {},

  //NCD
  diabetiesdata: [],
  examinationdata: [],
  testdata: [],
  history: [],
  examination: [],
  //EMR
  emrdata: [],
  // state for gis mapping
  allPhcVillage: [],
  subCentersList: [],
  subCentersList1: [],
  gramPanchayatList: [],
  villageList: [],
  householdList: [],
  IndividualList: [],
  placeList: [],
  // state for gis mapping

  //esanjeevani
  esanjeevani: [],

  // state for ViewModalPopUp
  prevDataArray: [],
  // state for ViewModalPopUp

  loading: true,
};

const usersReducers = (state = initialState, action) => {
  switch (action.type) {
    case types.GET_USERS_DATA:
      return {
        ...state,
        usersList: action.payload,
        loading: false,
      };
    case types.Get_Vitals_Data:
      return {
        ...state,
        vitalsList: action.payload,
        loading: false,
      };
    //  form utility services
    case types.Get_Phy_Exam_Items:
      return {
        ...state,
        phyExamList: action.payload,
        loading: false,
      };
    case types.Post_Phy_Exam:
    case types.Update_Phy_Exam:
    //  form utility services
    case types.GET_Meta_DATA:
      return {
        ...state,
        PharmaProdLength: action.payload,
        loading: false,
      };
    case types.GET_Meta_Inwards:
      return {
        ...state,
        PharmaInwardLength: action.payload,
        loading: false,
      };
    case types.GET_DOCID_USERS:
      return {
        ...state,
        usersList: action.payload,
        loading: false,
      };
    case types.GET_USERS_NURSE:
      return {
        ...state,
        usersList: action.payload,
        loading: false,
      };
    case types.GET_DOCTOR_LIST:
      return {
        ...state,
        doctorsList: action.payload,
        loading: false,
      };
    case types.Get_LabTech_List:
      return {
        ...state,
        labTechList: action.payload,
        loading: false,
      };
    case types.Get_Pharmacist_List:
      return {
        ...state,
        pharmacistList: action.payload,
        loading: false,
      };
    case types.Get_Nurse_List:
      return {
        ...state,
        nurseList: action.payload,
        loading: false,
      };

    // NCD
    case types.Get_Diabeties_Data:
      return {
        ...state,
        diabetiesdata: action.payload,
        loading: false,
      };

    case types.Get_Examination_Data:
      return {
        ...state,
        examinationdata: action.payload,
        loading: false,
      };
    case types.Get_Test_Data:
      return {
        ...state,
        testdata: action.payload,
        loading: false,
      };
    case types.Get_History:
      return {
        ...state,
        historyList: action.payload,
        loading: false,
      };
    case types.Get_Examination:
      return {
        ...state,
        examinationList: action.payload,
        loading: false,
      };
    //EMR
    case types.Get_EMR_Data:
      return {
        ...state,
        emrdataList: action.payload,
        loading: false,
      };
    case types.Post_Registration:
    case types.Patch_Registration:
    case types.Post_Create_Visit:
    case types.Get_Token_Visit:
      return {
        ...state,
        tokenList: action.payload,
        loading: false,
      };
    case types.Get_Patient_Data_Doctor:
      return {
        ...state,
        docUsersList: action.payload,
        loading: false,
      };
    case types.Update_PatDetails:
    case types.Get_Single_PatDetails:
      return {
        ...state,
        singlePatDet: action.payload,
        loading: false,
      };
    case types.Get_Single_resDet:
      return {
        ...state,
        singlePatientDetails: action.payload,
        loading: false,
      };
    // end lab orders
    case types.Get_Lab_Orders:
      return {
        ...state,
        labOrderList: action.payload,
        loading: false,
      };
    case types.Get_LabTest_By_Uhid:
      return {
        ...state,
        labOrderListByUhid: action.payload,
        loading: false,
      };
    case types.Get_All_Lab_Orders:
      return {
        ...state,
        allLabOrderList: action.payload,
        loading: false,
      };
    case types.Get_Lab_OrdersByDate:
      return {
        ...state,
        labOrderListByDate: action.payload,
        loading: false,
      };
    // end lab orders
    case types.Get_Pharmacy_Product:
      return {
        ...state,
        PharmaProdList: action.payload,
        loading: false,
      };
    case types.Get_Pharmacy_Search:
      return {
        ...state,
        PharmaSearchList: action.payload,
        loading: false,
      };
    case types.Post_Pharmacy_Product:
    case types.Patch_Pharmacy_Product:
    case types.Get_Single_Product:
      return {
        ...state,
        SinglePharmacy: action.payload,
        loading: false,
      };
    // Actions For Orders
    case types.Post_Purchase_Order:
    case types.Update_Purchase_Order:
    case types.Get_Order_List:
      return {
        ...state,
        PurchaseOrderList: action.payload,
        loading: false,
      };
    case types.Get_Order_List_By_Status:
      return {
        ...state,
        OrderListByStatus: action.payload,
        loading: false,
      };
    case types.Order_By_Search:
      return {
        ...state,
        OrdersBySearch: action.payload,
        loading: false,
      };
    case types.Get_Single_Order:
      return {
        ...state,
        SingleOrder: action.payload,
        loading: false,
      };
    // reducer call for pharmacy dashboard
    case types.Get_Reg_Pharma:
      return {
        ...state,
        regPharmacy: action.payload,
        loading: false,
      };
    case types.Get_Order_Id:
      return {
        ...state,
        dispOrderId: action.payload,
        loading: false,
      };
    case types.Get_All_Pharma:
      return {
        ...state,
        AllPharmaList: action.payload,
        loading: false,
      };
    case types.Get_Pat_Details:
      return {
        ...state,
        patDetails: action.payload,
        loading: false,
      };
    case types.Get_Order_Details:
      return {
        ...state,
        orderDetails: action.payload,
        loading: false,
      };
    case types.Create_Despence_Order:
    case types.Post_Despence_Order:
    // reducer call for pharmacy dashboard
    // reducer call for pharmacy inwards
    case types.Get_All_Inwards:
      return {
        ...state,
        InwardsList: action.payload,
        loading: false,
      };
    case types.Get_Invenory_By_Id:
      return {
        ...state,
        inventory: action.payload,
        loading: false,
      };
    case types.Get_Invenory_ExpireDate:
      return {
        ...state,
        expireData: action.payload,
        loading: false,
      };
    case types.Get_Dispence_ForId:
      return {
        ...state,
        dispenceForId: action.payload,
        loading: false,
      };
    case types.Get_Inwards_By_Id:
      return {
        ...state,
        inwardsById: action.payload,
        loading: false,
      };
    case types.Post_Inwards:
    case types.Update_Inwards:
    // reducer call for pharmacy inwards

    //  Case for Gis Mapping
    case types.Get_Phc_Boundary:
      return {
        ...state,
        allPhcVillage: action.payload,
        loading: false,
      };
    case types.Get_SubCenters:
      return {
        ...state,
        subCentersList: action.payload,
        loading: false,
      };
    case types.Get_SubCenters1:
      return {
        ...state,
        subCentersList1: action.payload,
        loading: false,
      };
    case types.Get_All_grampanchayat:
      return {
        ...state,
        gramPanchayatList: action.payload,
        loading: false,
      };
    case types.Get_Villages:
      return {
        ...state,
        villageList: action.payload,
        loading: false,
      };
    case types.Get_HouseHold:
      return {
        ...state,
        householdList: action.payload,
        loading: false,
      };
    case types.Get_Individual:
      return {
        ...state,
        IndividualList: action.payload,
        loading: false,
      };
    case types.Get_Places:
      return {
        ...state,
        placeList: action.payload,
        loading: false,
      };
    //  Case for Gis Mapping

    //esanjeevani

    case types.Get_Esanjeevani_Data:
      return {
        ...state,
        esanjeevani: action.payload,
        loading: false,
      };

    //  Case for ViewModalPopUp
    case types.Get_Prev_Data:
      return {
        ...state,
        prevDataArray: action.payload,
        loading: false,
      };
    //  Case for ViewModalPopUp
    default:
      return state;
  }
};
export default usersReducers;
