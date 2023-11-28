import * as types from "./actionType";
import axios from "axios";
import moment from "moment";
import * as Tostify from "../components/ConstUrl/Tostify";
import * as constant from "../components/ConstUrl/constant";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";

let typeofuser = sessionStorage.getItem("typeofuser");
let phcuuid = sessionStorage.getItem("uuidofphc");
let date = moment(new Date()).format("YYYY-MM-DD");
let tomodate = moment()
  .add(1, "days")
  .startOf("day")
  .format("YYYY-MM-DD")
  .toString();

const getUsers = (usersList) => ({
  type: types.GET_USERS_DATA,
  payload: usersList,
});
const getVitalList = (vitalsList) => ({
  type: types.Get_Vitals_Data,
  payload: vitalsList,
});

const getMataList = (metaList) => ({
  type: types.GET_Meta_DATA,
  payload: metaList,
});

const getDoctor = (doctorsList) => ({
  type: types.GET_DOCTOR_LIST,
  payload: doctorsList,
});

const getLabTech = (labTechList) => ({
  type: types.Get_LabTech_List,
  payload: labTechList,
});
const getPharmacist = (pharmacistList) => ({
  type: types.Get_Pharmacist_List,
  payload: pharmacistList,
});
const getNurse = (nurseList) => ({
  type: types.Get_Nurse_List,
  payload: nurseList,
});

const createVisit = () => ({
  type: types.Post_Create_Visit,
});
const createRegistration = () => ({
  type: types.Post_Registration,
});
const UpdateRegistration = () => ({
  type: types.Patch_Registration,
});
const getTokenumber = (tokenList) => ({
  type: types.Get_Token_Visit,
  payload: tokenList,
});

// get call for form utility service
const getPhyExamList = (phyExamList) => ({
  type: types.Get_Phy_Exam_Items,
  payload: phyExamList,
});
const postPhyExamList = () => ({
  type: types.Post_Phy_Exam,
});
const updatePhyExamList = () => ({
  type: types.Update_Phy_Exam,
});
// get call for form utility service

const getPharmaList = (PharmaProdList) => ({
  type: types.Get_Pharmacy_Product,
  payload: PharmaProdList,
});
const getSinglePat = (singlePatDet) => ({
  type: types.Get_Single_PatDetails,
  payload: singlePatDet,
});
const getSinglePatient = (singlePatientDetails) => ({
  type: types.Get_Single_resDet,
  payload: singlePatientDetails,
});
const updatePatient = () => ({
  type: types.Update_PatDetails,
});
// set value for lab orders
const getLabOrders = (labOrderList) => ({
  type: types.Get_Lab_Orders,
  payload: labOrderList,
});
const getLabOrdersByUhid = (labOrderListByUhid) => ({
  type: types.Get_LabTest_By_Uhid,
  payload: labOrderListByUhid,
});
const getAllLabOrders = (allLabOrderList) => ({
  type: types.Get_All_Lab_Orders,
  payload: allLabOrderList,
});
const getLabOrdersByDate = (labOrderListByDate) => ({
  type: types.Get_Lab_OrdersByDate,
  payload: labOrderListByDate,
});
// set value for lab orders

const getPharmaByname = (PharmaSearchList) => ({
  type: types.Get_Pharmacy_Search,
  payload: PharmaSearchList,
});
const getPharmaListLength = (PharmaProdLength) => ({
  type: types.GET_Meta_DATA,
  payload: PharmaProdLength,
});

const addPharma = () => ({
  type: types.Post_Pharmacy_Product,
});
const updatePharma = () => ({
  type: types.Patch_Pharmacy_Product,
});

const getSingleProduct = (SinglePharmacy) => ({
  type: types.Get_Single_Product,
  payload: SinglePharmacy,
});

//Diabeties
const getDiabetisData = (diabetiesdata) => ({
  type: types.Get_Diabeties_Data,
  payload: diabetiesdata,
});

const getExaminationData = (examinationdata) => ({
  type: types.Get_Examination_Data,
  payload: examinationdata,
});

const getTestData = (testdata) => ({
  type: types.Get_Test_Data,
  payload: testdata,
});

export const getHistory = (historyList) => ({
  type: types.Get_History,
  payload: historyList,
});
export const getExamination = (examinationList) => ({
  type: types.Get_Examination,
  payload: examinationList,
});
//action of EMR
const getEMRData = (emrdataList) => ({
  type: types.Get_EMR_Data,
  payload: emrdataList,
});
// Action for Orders
const addPurchaseOrder = () => ({
  type: types.Post_Purchase_Order,
});
const updatePurchaseOrder = () => ({
  type: types.Update_Purchase_Order,
});
const getOdersList = (PurchaseOrderList) => ({
  type: types.Get_Order_List,
  payload: PurchaseOrderList,
});
const getOdersListByStatus = (OrderListByStatus) => ({
  type: types.Get_Order_List_By_Status,
  payload: OrderListByStatus,
});
const getOdersBySearch = (OrdersBySearch) => ({
  type: types.Order_By_Search,
  payload: OrdersBySearch,
});
const getSingleOrder = (SingleOrder) => ({
  type: types.Get_Single_Order,
  payload: SingleOrder,
});
// Action for Orders

// Actions for pharmacy dashboard
const getRegPharmacy = (regPharmacy) => ({
  type: types.Get_Reg_Pharma,
  payload: regPharmacy,
});
const getAllPharmaList = (AllPharmaList) => ({
  type: types.Get_All_Pharma,
  payload: AllPharmaList,
});
const getOrderId = (dispOrderId) => ({
  type: types.Get_Order_Id,
  payload: dispOrderId,
});
const getPatDetails = (patDetails) => ({
  type: types.Get_Pat_Details,
  payload: patDetails,
});
const getOrderDetails = (orderDetails) => ({
  type: types.Get_Order_Details,
  payload: orderDetails,
});
const addDespence = () => ({
  type: types.Create_Despence_Order,
});
const postAddDespence = () => ({
  type: types.Post_Despence_Order,
});
// Actions for pharmacy dashboard

// Actions for pharmacy inwards
const getInwards = (InwardsList) => ({
  type: types.Get_All_Inwards,
  payload: InwardsList,
});
const getInwardsListLength = (PharmaInwardLength) => ({
  type: types.GET_Meta_Inwards,
  payload: PharmaInwardLength,
});
const getInwardsByID = (inwardsById) => ({
  type: types.Get_Inwards_By_Id,
  payload: inwardsById,
});
const getInventory = (inventory) => ({
  type: types.Get_Invenory_By_Id,
  payload: inventory,
});
const getExpireData = (expireData) => ({
  type: types.Get_Invenory_ExpireDate,
  payload: expireData,
});
const getDispenceForId = (dispenceForId) => ({
  type: types.Get_Dispence_ForId,
  payload: dispenceForId,
});
// Actions for pharmacy inwards
const addInwards = () => ({
  type: types.Post_Inwards,
});
const updateInwards = () => ({
  type: types.Update_Inwards,
});
// Actions for pharmacy inwards

// Action for Gis Mapping
const getPhcVillage = (allPhcVillage) => ({
  type: types.Get_Phc_Boundary,
  payload: allPhcVillage,
});
const getSubCenter = (subCentersList) => ({
  type: types.Get_SubCenters,
  payload: subCentersList,
});
const getSubCenter1 = (subCentersList1) => ({
  type: types.Get_SubCenters1,
  payload: subCentersList1,
});
const getVillages = (villageList) => ({
  type: types.Get_Villages,
  payload: villageList,
});
const getGramPanchayat = (gramPanchayatList) => ({
  type: types.Get_All_grampanchayat,
  payload: gramPanchayatList,
});
export const getHouseHolds = (householdList) => ({
  type: types.Get_HouseHold,
  payload: householdList,
});
export const getIndividual = (IndividualList) => ({
  type: types.Get_Individual,
  payload: IndividualList,
});
const getPlaces = (placeList) => ({
  type: types.Get_Places,
  payload: placeList,
});
// Action for Gis Mapping

// Action for ViewModalPopUp
const getPrevDataMO = (prevDataArray) => ({
  type: types.Get_Prev_Data,
  payload: prevDataArray,
});
// Action for ViewModalPopUp

//esanjeevani
const getEsanjeevani = (esanjeevani) => ({
  type: types.Get_Esanjeevani_Data,
  payload: esanjeevani,
});

// Get Physical Examination Form Utility
export const loadPhyExamList = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/formutility/EMR_PhysicalExamination`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getPhyExamList(resp));
      })
      .catch((error) => console.log(error));
  };
};
// Get Physical Examination Form Utility
// Post Physical Examination Form Utility
export const loadPostPhyExamList = (
  requestOptions,
  getPhydataByEnId,
  EncID
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/Observations`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 401) {
          Tostify.notifyFail("Data Not Submitted...!");
        } else {
          dispatch(postPhyExamList(resp));
          getPhydataByEnId(EncID);
          Tostify.notifySuccess("Data Submitted Successfully...!");
        }
      })
      .catch((error) => console.log(error));
  };
};
// Post Physical Examination Form Utility
// Update Physical Examination Form Utility
export const loadUpdatePhyExamList = (
  requestOptions,
  phyExamId,
  getPhydataByEnId,
  EncID
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/Observations/${phyExamId}`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 401) {
          Tostify.notifyFail("Data Not Submitted...!");
        } else {
          dispatch(updatePhyExamList(resp));
          getPhydataByEnId(EncID);
          Tostify.notifySuccess("Data Submitted Successfully...!");
        }
      })
      .catch((error) => console.log(error));
  };
};
// Update Physical Examination Form Utility

// Get Pharmacy List
export const loadPharmaList = (page, pagePerRow) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/product-svc/products/filter?page=${page}&size=${pagePerRow}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getPharmaList(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};

export const loadPharmaByName = (page, pagePerRow, searchKeyWord) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/product-svc/products/filter?page=${page}&size=${pagePerRow}&searchStr=${searchKeyWord}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getPharmaByname(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};

export const loadPharmaPatientBySearch = (
  srName,
  stdate,
  endate,
  page,
  rowsPerPage,
  searchvalue,
  setPageLoader
) => {
  return function (dispatch) {
    let url;
    if (!endate && srName && !stdate) {
      if (searchvalue == "NAME") {
        url = `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?patientName=${srName}&page=${page}&size=${rowsPerPage}`;
      } else if (searchvalue == "UHID") {
        url = `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?UHID=${srName}&page=${page}&size=${rowsPerPage}`;
      }
    } else if (!srName && stdate && endate) {
      url = `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
    } else if (srName && stdate && endate) {
      if (searchvalue == "NAME") {
        url = `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?patientName=${srName}&stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
      } else if (searchvalue == "UHID") {
        url = `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?UHID=${srName}&stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
      }
    }

    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllPharmaList(resp.data));
        setPageLoader(false);
      })
      .catch((error) => console.log(error));
  };
};
// Get Pharmacy List

// Add Pharma to the List

export const loadAddPharma = (
  pharcamcy,
  ModalClose,
  Loader,
  page,
  rowsPerPage
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/product-svc/products`, pharcamcy)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.error) {
          alert("please try again");
        } else {
          dispatch(addPharma());
          ModalClose(false);
          Loader(true);
          dispatch(loadPharmaList(page, rowsPerPage));
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadUpdatePharma = (
  productId,
  updatePharcamcy,
  ModalClose,
  setNewLoading,
  setBtnStatus,
  page,
  rowsPerPage
) => {
  return function (dispatch) {
    if (productId) {
      fetch(
        `${constant.ApiUrl}/product-svc/products/${productId}`,
        updatePharcamcy
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.error) {
            alert("please try again");
          } else {
            dispatch(updatePharma());
            ModalClose(false);
            setNewLoading(true);
            setBtnStatus(false);
            dispatch(loadPharmaList(page, rowsPerPage));
          }
        })
        .catch((error) => console.log(error));
    }
  };
};
// Add Pharma to the List

// Get Single Pharma Data
export const loadSingleProduct = (productId) => {
  // if(productId){
  return function (dispatch) {
    if (productId) {
      fetch(
        `${constant.ApiUrl}/product-svc/products/${productId}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getSingleProduct(resp));
          loadUsers();
        })
        .catch((error) => console.log(error));
    }
  };
  // }
};
// Get Single Pharma Data

// Function for Oeders
// Get Orders List
export const loadOrdersList = (page, rowsPerPage) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?page=${page}&size=${rowsPerPage}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOdersList(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};
export const loadOrdersListByStatus = (page, rowsPerPage) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?page=${page}&size=${rowsPerPage}&statuses=CREATED,AUTHORISED`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOdersListByStatus(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};
// get orders by search
export const loadOrdersBySearch = (
  srName,
  stdate,
  endate,
  page,
  rowsPerPage
) => {
  let url;
  if (srName && !stdate && !endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${srName}&page=${page}&size=${rowsPerPage}`;
  } else if (!srName && stdate && endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
  } else if (srName && stdate && endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${srName}&stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
  }
  return function (dispatch) {
    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOdersList(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};
export const loadOrdersBySearchMo = (
  srName,
  stdate,
  endate,
  page,
  rowsPerPage
) => {
  let url;
  if (srName && !stdate && !endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${srName}&page=${page}&size=${rowsPerPage}&statuses=CREATED,ATHORISED`;
  } else if (!srName && stdate && endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}&statuses=CREATED,ATHORISED`;
  } else if (srName && stdate && endate) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${srName}&stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}&statuses=CREATED,ATHORISED`;
  }
  return function (dispatch) {
    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOdersListByStatus(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};

export const loadOrdersBySTS = (
  suplierName,
  statusValue,
  selectedType,
  page,
  rowsPerPage
) => {
  let url;
  if (
    suplierName.length != 0 &&
    selectedType.length == 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${suplierName}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length != 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?poType=${selectedType}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length == 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?statuses=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length != 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${suplierName}&poType=${selectedType}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length == 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${suplierName}&statuses=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length != 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${suplierName}&poType=${selectedType}&statuses=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length != 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?poType=${selectedType}&statuses=${statusValue}&page=${page}&size=${rowsPerPage}`;
  }

  return function (dispatch) {
    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOdersList(resp.data));
        dispatch(getPharmaListLength(Math.ceil(resp["meta"]["totalElements"])));
      })
      .catch((error) => console.log(error));
  };
};

export const loadByNameType = (
  suplierName,
  selectedType,
  page,
  rowsPerPage
) => {
  return function (dispatch) {
    if (suplierName && selectedType) {
      fetch(
        `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?supplierName=${suplierName}&poType=${selectedType}&page=${page}&size=${rowsPerPage}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getOdersList(resp.data));
          dispatch(
            getPharmaListLength(Math.ceil(resp["meta"]["totalElements"]))
          );
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadByNTDate = (
  stDate,
  toDate,
  suplierName,
  selectedType,
  page,
  rowsPerPage
) => {
  return function (dispatch) {
    if (stDate && toDate && suplierName && selectedType) {
      fetch(
        `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/filter?stDate=${stDate}&edDate=${toDate}&supplierName=${suplierName}&poType=${selectedType}&page=${page}&size=${rowsPerPage}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getOdersBySearch(resp.data));
          dispatch(
            getPharmaListLength(Math.ceil(resp["meta"]["totalElements"]))
          );
        })
        .catch((error) => console.log(error));
    }
  };
};
// Get Single Pharma Data
export const loadSingleOrder = (orderId) => {
  return function (dispatch) {
    if (orderId != null) {
      fetch(
        `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/${orderId}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getSingleOrder(resp));
        })
        .catch((error) => console.log(error));
    } else {
      dispatch(getSingleOrder({}));
    }
  };
};
// Get Single Pharma Data

// AddPurchaseOrder
export const loadAddPurchaseOrder = (postResponse, loader) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/purchase-orders-svc/purchaseorders`, postResponse)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.error) {
          alert("please try again");
        } else {
          dispatch(addPurchaseOrder());
          loader(true);
        }
      })
      .catch((error) => console.log(error));
  };
};
// AddPurchaseOrder
// UpdatePurchaseOrder
export const loadUpdatePurchaseOrder = (orderId, updateResponse, loader) => {
  return function (dispatch) {
    if (orderId) {
      fetch(
        `${constant.ApiUrl}/purchase-orders-svc/purchaseorders/${orderId}`,
        updateResponse
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.error) {
            alert("please try again");
          } else {
            dispatch(updatePurchaseOrder());
            loader(true);
          }
        })
        .catch((error) => console.log(error));
    }
  };
};
// UpdatePurchaseOrder
// Function for Oeders

// Service call for pharmacy dashboard
export const loadRegPharma = (pharmaId, isoDateforgenerated, setPageLoader) => {
  return function (dispatch) {
    if (pharmaId) {
      fetch(
        `${constant.ApiUrl}/reservations/filter?providerId=` +
          pharmaId +
          "&page=" +
          1 +
          "&date=" +
          isoDateforgenerated,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getRegPharmacy(resp.data));
          dispatch(
            getPharmaListLength(Math.ceil(resp["meta"]["totalElements"]))
          );

          // allPharmaList
          fetch(
            `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/filter?stDate=${date}&edDate=${tomodate}&page=0&size=1000000`,
            serviceHeaders.getRequestOptions
          )
            .then((resp) => resp.json())
            .then((resp) => {
              dispatch(getAllPharmaList(resp.data));
              setPageLoader(false);
              // dispatch(getPharmaListLength(Math.ceil(resp['meta']['totalElements'])));
            })
            .catch((error) => console.log(error));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadPatDetails = (labResId) => {
  return function (dispatch) {
    if (labResId) {
      fetch(
        `${constant.ApiUrl}/reservations/` + labResId,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getPatDetails(resp));
        })
        .catch((error) => console.log(error));
    }
  };
};
export const loadOrderDetails = (orderId) => {
  return function (dispatch) {
    if (orderId) {
      fetch(
        `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/` + orderId,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getOrderDetails(resp));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadAddDespence = (postDespence) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders`, postDespence)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getOrderId(resp.id));
        dispatch(addDespence());
      })
      .catch((error) => console.log(error));
  };
};

export const loadPostDespence = (orderId, requestOptions, setLoading1) => {
  return function (dispatch) {
    if (orderId) {
      fetch(
        `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/${orderId}/dispenses`,
        requestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(postAddDespence());
          setLoading1(true);
        })
        .catch((error) => console.log(error));
    }
  };
};

// Service call for pharmacy dashboard
export const loadInwards = (page, rowsPerPage) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/inward-svc/inwards/filter?page=${page}&size=${rowsPerPage}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getInwards(resp.data));
        dispatch(
          getInwardsListLength(Math.ceil(resp["meta"]["totalElements"]))
        );
      })
      .catch((error) => console.log(error));
  };
};
export const loadInventory = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/product-svc/products/inventories/filter?page=0&size=100000000`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getInventory(resp.data));
        // dispatch(getInwardsListLength(Math.ceil(resp['meta']['totalElements'])));
      })
      .catch((error) => console.log(error));
  };
};
export const loadExpireDate = (batchNumber) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/product-svc/products/inventories/filter?batchNumber=${batchNumber}&page=0&size=100000000`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getExpireData(resp.data));
        // dispatch(getInwardsListLength(Math.ceil(resp['meta']['totalElements'])));
      })
      .catch((error) => console.log(error));
  };
};
export const loadDispenceForId = (pharmaOrdId) => {
  return function (dispatch) {
    if (pharmaOrdId) {
      fetch(
        `${constant.ApiUrl}/pharmacy-orders-svc/pharmacyorders/dispenses/filter?orderId=${pharmaOrdId}&page=0&size=100000000`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getDispenceForId(resp.data));
        })
        .catch((error) => console.log(error));
    }
  };
};

// Service call for pharmacy dashboard

// service call for inwards
export const loadInwardsById = (inwardsId) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/inward-svc/inwards/${inwardsId}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getInwardsByID(resp));
      })
      .catch((error) => console.log(error));
  };
};
export const loadAddInwards = (
  inwardsResponse,
  setInwardsLoading1,
  setInwardsLoading2,
  setInwardsLoading3
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/inward-svc/inwards`, inwardsResponse)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(addInwards());
        setInwardsLoading1(true);
        setTimeout(() => {
          setInwardsLoading2(true);
          setTimeout(() => {
            setInwardsLoading3(true);
            setTimeout(() => {
              setInwardsLoading1(false);
              setInwardsLoading2(false);
            }, 2500);
            setInwardsLoading2(false);
          }, 2500);
          setInwardsLoading1(false);
        }, 2500);
      })
      .catch((error) => console.log(error));
  };
};
export const loadUpdateInwards = (
  inwardsResponse,
  inwardId,
  setInwardsLoading1,
  setInwardsLoading2,
  setInwardsLoading3
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/inward-svc/inwards/${inwardId}`, inwardsResponse)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(updateInwards());
        setInwardsLoading1(true);
        setTimeout(() => {
          setInwardsLoading2(true);
          setTimeout(() => {
            setInwardsLoading3(true);
            setTimeout(() => {
              setInwardsLoading1(false);
              setInwardsLoading2(false);
            }, 2500);
            setInwardsLoading2(false);
          }, 2500);
          setInwardsLoading1(false);
        }, 2500);
      })
      .catch((error) => console.log(error));
  };
};

export const loadInwardsBySearch = (
  srName,
  stdate,
  endate,
  page,
  rowsPerPage
) => {
  let url;
  if (srName && !stdate && !endate) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?supplier=${srName}&page=${page}&size=${rowsPerPage}`;
  } else if (!srName && stdate && endate) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
  } else if (srName && stdate && endate) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?stDate=${stdate}&edDate=${endate}&page=${page}&size=${rowsPerPage}`;
  }
  return function (dispatch) {
    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getInwards(resp.data));
        dispatch(
          getInwardsListLength(Math.ceil(resp["meta"]["totalElements"]))
        );
      })
      .catch((error) => console.log(error));
  };
};

export const loadInwardsBySTS = (
  suplierName,
  statusValue,
  selectedType,
  page,
  rowsPerPage
) => {
  let url;
  if (
    suplierName.length != 0 &&
    selectedType.length == 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?supplier=${suplierName}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length != 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?inwardType=${selectedType}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length == 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?status=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length != 0 &&
    statusValue.length == 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?supplier=${suplierName}&inwardType=${selectedType}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length == 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?supplier=${suplierName}&status=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length != 0 &&
    selectedType.length != 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?supplier=${suplierName}&inwardType=${selectedType}&status=${statusValue}&page=${page}&size=${rowsPerPage}`;
  } else if (
    suplierName.length == 0 &&
    selectedType.length != 0 &&
    statusValue.length != 0
  ) {
    url = `${constant.ApiUrl}/inward-svc/inwards/filter?inwardType=${selectedType}&status=${statusValue}&page=${page}&size=${rowsPerPage}`;
  }
  return function (dispatch) {
    fetch(`${url}`, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getInwards(resp.data));
        dispatch(
          getInwardsListLength(Math.ceil(resp["meta"]["totalElements"]))
        );
      })
      .catch((error) => console.log(error));
  };
};
// Service call for pharmacy dashboard

const isoDateforgenerated = moment(new Date().toISOString()).format(
  "YYYY-MM-DD"
);

export let loadUsers;

if (typeofuser === "Medical Officer") {
  loadUsers = (doctorid, setPageLoader) => {
    if (doctorid) {
      return function (dispatch) {
        fetch(
          `${constant.ApiUrl}/reservations/filter?providerId=${doctorid}&page=&date=${isoDateforgenerated}`,
          serviceHeaders.getRequestOptions
        )
          .then((res) => res.json())
          .then((resp) => {
            dispatch(getUsers(resp.data));
            setPageLoader(false);
          })
          .catch((error) => console.log(error));
      };
    }
  };
} else if (typeofuser === "Nurse") {
  loadUsers = (setPageLoader) => {
    return function (dispatch) {
      fetch(
        `${constant.ApiUrl}/reservations/filter?page=&date=${isoDateforgenerated}&status=[Confirmed,OnHold]`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((resp) => {
          dispatch(getUsers(resp.data));
          dispatch(getMataList(resp.meta.totalElements));
          setPageLoader(false);
        })
        .catch((error) => console.log(error));
    };
  };
} else if (typeofuser === "Lab Technician") {
  loadUsers = (doctorid, setPageLoader) => {
    if (doctorid) {
      return function (dispatch) {
        fetch(
          `${constant.ApiUrl}/reservations/filter?providerId=${doctorid}&page=&date=${isoDateforgenerated}`,
          serviceHeaders.getRequestOptions
        )
          .then((res) => res.json())
          .then((resp) => {
            dispatch(getUsers(resp.data));
            dispatch(getMataList(resp.meta.totalElements));
            fetch(
              `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${date}&endDate=${date}&page=0&size=200`, //size=1000000
              serviceHeaders.getRequestOptions
            )
              .then((resp) => resp.json())
              .then((resp) => {
                dispatch(getLabOrders(resp.content));
                setPageLoader(false);
              })
              .catch((error) => console.log(error));
          })
          .catch((error) => console.log(error));
      };
    }
  };
} else {
  loadUsers = (setPageLoader) => {
    return function (dispatch) {
      fetch(
        `${constant.ApiUrl}/reservations/filter?page=&date=${isoDateforgenerated}&status=[CheckedIn,Confirmed,OnHold,startedConsultation,Cancelled]`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((resp) => {
          dispatch(getUsers(resp.data));
          dispatch(getMataList(resp.meta.totalElements));
          setPageLoader(false);
        })
        .catch((error) => console.log(error));
    };
  };
}

export const loadVitalsList = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/vitalsigns/filter?encounterDate=${isoDateforgenerated}&page=1`,
      serviceHeaders.getRequestOptions
    )
      .then((vitals) => vitals.json())
      .then((vitals) => {
        dispatch(getVitalList(vitals.data));
      })
      .catch((error) => console.log(error));
  };
};

export const loadDoctor = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=MedicalOfficer&srcNodeId=` +
        phcuuid +
        "&srcType=Phc",
      serviceHeaders.getRequestOptions
    )
      .then((res) => res.json())
      .then((res) => {
        dispatch(getDoctor(res));
      })
      .catch((error) => console.log(error));
  };
};
export const loadLabTech = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=JuniorLabTechnician&srcNodeId=` +
        phcuuid +
        "&srcType=Phc",
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getLabTech(resp));
      })
      .catch((error) => console.log(error));
  };
};
export const loadPharma = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=JuniorPharmacist&srcNodeId=` +
        phcuuid +
        "&srcType=Phc",
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getPharmacist(resp));
      })
      .catch((error) => console.log(error));
  };
};
export const loadNurse = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=AshaWorker&srcNodeId=` +
        phcuuid +
        "&srcType=SubCenter",
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getNurse(resp));
      })
      .catch((error) => console.log(error));
  };
};

export const loadDoctorPatdata = (doctors) => {
  return function (dispatch) {
    if (typeofuser == "Nurse") {
      if (doctors == "all") {
        loadUsers();
      } else {
        fetch(
          `${constant.ApiUrl}/reservations/filter?providerId=${doctors}&page=&date=${isoDateforgenerated}&status=[Confirmed,OnHold]`,
          serviceHeaders.getRequestOptions
        )
          .then((resp) => resp.json())
          .then((resp) => {
            dispatch(getUsers(resp.data));
          })
          .catch((error) => console.log(error));
      }
    } else {
      if (doctors == "all") {
        loadUsers();
      } else {
        fetch(
          `${constant.ApiUrl}/reservations/filter?providerId=${doctors}&page=&date=${isoDateforgenerated}`,
          serviceHeaders.getRequestOptions
        )
          .then((resp) => resp.json())
          .then((resp) => {
            dispatch(getUsers(resp.data));
          })
          .catch((error) => console.log(error));
      }
    }
  };
};

export const loadSinglePatDet = (pateintid) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/reservations/${pateintid}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getSinglePat(resp));
      })
      .catch((error) => console.log(error));
  };
};
export const loadSinglePatient = (pateintid) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/patients/${pateintid}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getSinglePatient(resp));
      })
      .catch((error) => console.log(error));
  };
};

export const updatePatDet = (pateintid, requestOptions1, setShowModal) => {
  return function (dispatch) {
    if (pateintid) {
      fetch(`${constant.ApiUrl}/reservations/${pateintid}`, requestOptions1)
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(updatePatient(resp));
          loadUsers();
          if (setShowModal) {
            setShowModal(false);
          }
        })
        .catch((error) => console.log(error));
    }
  };
};

// create Visit
export const loadCreateVisit = (postVisit) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/reservations`, postVisit)
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(createVisit(resp));
        loadUsers();
        sessionStorage.setItem("VisitPatientName", resp.Patient.name);
        sessionStorage.setItem("VisitResFor", resp.reservationFor);
        sessionStorage.setItem("VisitProvider", resp.Provider.name);
      })
      .catch((error) => console.log(error));
  };
};

export const loadRegistration = (
  healthId,
  requestOptions,
  setLoading1,
  setLoading2,
  setNewLoading,
  setPatinentId
) => {
  return function (dispatch) {
    if (healthId) {
      fetch(
        `${constant.ApiUrl}/patients/search?fieldName=HealthID&value=` +
          healthId +
          `&page=1`,
        serviceHeaders.getRequestOptions
      )
        // fetch(`${constant.ApiUrl}/reservations`, postVisit)
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.data.length != 0) {
            Tostify.notifyWarning(
              "Patient is already registred..!, Go to create visit and Search the patient..."
            );
          } else {
            fetch(`${constant.ApiUrl}/patients`, requestOptions)
              .then((res) => res.json())
              .then((res) => {
                if (res.errors) {
                  alert(
                    "Something went wrong, data is not submitted, Please try again"
                  );
                } else {
                  // setPatinentId(res._id)
                  setPatinentId(res._id);
                  dispatch(createRegistration(res));
                  setLoading1(true);
                  setTimeout(() => {
                    setLoading2(true);
                    setTimeout(() => {
                      setNewLoading(true);
                      setTimeout(() => {
                        setLoading1(false);
                        setLoading2(false);
                      }, 2500);
                      setLoading2(false);
                    }, 2500);
                    setLoading1(false);
                  }, 2500);
                }
              });
          }
        })
        .catch((error) => console.log(error));
    } else {
      fetch(`${constant.ApiUrl}/patients`, requestOptions)
        .then((res) => res.json())
        .then((res) => {
          if (res.errors) {
            alert(
              "Something went wrong, data is not submitted, Please try again"
            );
          } else {
            // setPatinentId(res._id)
            setPatinentId(res._id);
            dispatch(createRegistration(res));
            setLoading1(true);
            setTimeout(() => {
              setLoading2(true);
              setTimeout(() => {
                setNewLoading(true);
                setTimeout(() => {
                  setLoading1(false);
                  setLoading2(false);
                }, 2500);
                setLoading2(false);
              }, 2500);
              setLoading1(false);
            }, 2500);
          }
        })
        .catch((error) => console.log(error));
    }
  };
};
export const loadUpdateRegistration = (
  patId,
  requestOptions,
  setLoading1,
  setLoading2,
  setNewLoading
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/patients/` + patId, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.errors) {
          alert("Something went wrong, data is not Updated, Please try again");
        } else {
          dispatch(UpdateRegistration(res));
          setTimeout(() => {
            setLoading2(true);
            setTimeout(() => {
              setNewLoading(true);
              setTimeout(() => {
                setLoading1(false);
                setLoading2(false);
              }, 2500);
              setLoading2(false);
            }, 2500);
            setLoading1(false);
          }, 2500);
        }
      });
  };
};

export const loadTokenNumber = (assignType, setAssignload) => {
  return function (dispatch) {
    if (assignType) {
      fetch(
        `${constant.ApiUrl}/reservations/filter?providerId=${assignType}&date=${isoDateforgenerated}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getTokenumber(resp.data));
          setAssignload(false);
        })
        .catch((error) => console.log(error));
    }
  };
};
// create Visit

// Lab orders
// export const loadLabOrders = () => {
//     return function (dispatch) {
//         fetch(`${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${date}&endDate=${date}&page=0&size=100000000`, serviceHeaders.getRequestOptions)
//             .then((resp) => resp.json())
//             .then((resp) => {
//                 dispatch(getLabOrders(resp.content));
//             })
//             .catch((error) => console.log(error));
//     }
// }
export const loadLabOrdersByUhid = (uhid) => {
  return function (dispatch) {
    if (uhid) {
      fetch(
        `${constant.ApiUrl}/lab-orders-svc/laborders/filter?size=100&page=0&uhId=${uhid}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getLabOrdersByUhid(resp.content));
        })
        .catch((error) => console.log(error));
    }
  };
};
export const loadAllLabOrders = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/filter?page=0&size=500&statuses=RESULTS_ENTERED`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllLabOrders(resp.content));
      })
      .catch((error) => console.log(error));
  };
};
export const loadLabOrdersByDate = (fromDate, endDate) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/lab-orders-svc/laborders/filter?startDate=${fromDate}&endDate=${endDate}&page=0&size=200`, //size=1000000
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getLabOrdersByDate(resp.content));
      })
      .catch((error) => console.log(error));
  };
};
// Lab orders

// Service call for GIS Mapping
export const loadPhcVillage = (responseRequest) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter`,
      responseRequest
    )
      .then((phcResp) => phcResp.json())
      .then((phcResp) => {
        dispatch(getPhcVillage(phcResp.content));
      })
      .catch((error) => console.log(error));
  };
};
export const loadSubCenters = (phcuuid, setPageLoader) => {
  return function (dispatch) {
    if (phcuuid != null) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Phc&srcNodeId=${phcuuid}&rel=SUBORGOF&targetType=SubCenter&page=&size=100`,
        serviceHeaders.getRequestOptions
      )
        .then((subResp) => subResp.json())
        .then((subResp) => {
          dispatch(getSubCenter(subResp.content));
          if (setPageLoader) {
            setPageLoader(false);
          }
        })
        .catch((error) => console.log(error));
    }
  };
};
export const loadSubCenters1 = (multiUuid) => {
  return function (dispatch) {
    let villageList = [];
    for (var i = 0; i < multiUuid.length; i++) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${multiUuid[i]}&rel=SERVICEDAREA&targetType=Village`,
        serviceHeaders.getRequestOptions
      )
        .then((subResp) => subResp.json())
        .then((subResp) => {
          for (var j = 0; j < subResp.content.length; j++) {
            villageList[j] = [];
            villageList[j].push(subResp.content[j]);
          }
        })
        .catch((error) => console.log(error));
    }
    dispatch(getSubCenter1(villageList));
  };
};
// export const loadVillages = (subcenterUuid) => {
//     return function (dispatch) {
//         fetch(`${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${subcenterUuid}&rel=SERVICEDAREA&targetType=Village`,
//             serviceHeaders.getRequestOptions)
//             .then((subResp) => subResp.json())
//             .then((subResp) => {
//                 // console.log(subResp.content, "subResp.content")
//                 dispatch(getVillages(subResp.content))
//             })
//             .catch((error) => console.log(error));
//     }
// }
// export const loadGramPanchayat = (postResponse) => {
//     return function (dispatch) {
//         fetch(`${constant.ApiUrl}/organization-svc/organizations/relationships/filter`,
//             postResponse)
//             .then((subResp) => subResp.json())
//             .then((subResp) => {
//                 dispatch(getGramPanchayat(subResp.content))
//             })
//             .catch((error) => console.log(error));
//     }
// }
export const loadVillages = (postResponse, setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter`,
      postResponse
    )
      .then((subResp) => subResp.json())
      .then((subResp) => {
        setPageLoader(false);
        dispatch(getVillages(subResp.content));
      })
      .catch((error) => console.log(error));
  };
};

// Diabetis subdropdown
export const loadDiabetis = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/surveys/filter?surveyType=DiabetesHistorySymptoms&surveyName=Diabetes`,
      serviceHeaders.getRequestOptions
    )
      .then((response) => response.json())
      .then((response) => {
        dispatch(getDiabetisData(response.data));
      })
      .catch((error) => {});
  };
};
export const loadExamination = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/surveys/filter?surveyType=DiabetesExamination&surveyName=Diabetes`,
      serviceHeaders.getRequestOptions
    )
      .then((response) => response.json())
      .then((response) => {
        dispatch(getExaminationData(response.data));
      })
      .catch((error) => {});
  };
};
export const loadTest = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/surveys/filter?surveyType=DiabetesTest&surveyName=Diabetes`,
      serviceHeaders.getRequestOptions
    )
      .then((response) => response.json())
      .then((response) => {
        dispatch(getTestData(response.data));
      })
      .catch((error) => {});
  };
};
// NCD ONclick service
export const loadHistory = (selectedVillage, historySymptoms) => {
  return function (dispatch) {
    var villageString = "";
    var params = "";

    selectedVillage.map((e) => {
      villageString = villageString + "&villageId=" + e.target.properties.uuid;
    });
    historySymptoms.map((e) => {
      params = "&" + e.replace(/ /g, "") + "=Yes";
    });
    fetch(
      `${constant.ApiUrl}/surveycube-svc/surveycube/filter?surveyType=DIABETESHISTORYSYMPTOMS&topics=NCD${villageString}${params}`,
      serviceHeaders.getRequestOptions
    )
      .then((subResp) => subResp.json())
      .then((subResp) => {
        dispatch(getHistory(subResp.data));
      })
      .catch((error) => console.log(error));
  };
};
export const loadExaminationDiabeties = (
  selectedVillage,
  selectExaminValue
) => {
  return function (dispatch) {
    var villageString = "";
    var params = "";

    selectedVillage.map((e) => {
      villageString = villageString + "&villageId=" + e.target.properties.uuid;
    });
    selectExaminValue.map((e) => {
      params = "&" + e + "=Yes";
    });

    fetch(
      `${constant.ApiUrl}/surveycube-svc/surveycube/filter?surveyType=DIABETESEXAMINATION&topics=NCD${villageString}${params}`,
      serviceHeaders.getRequestOptions
    )
      .then((subResp) => subResp.json())
      .then((subResp) => {
        dispatch(getExamination(subResp.data));
      })
      .catch((error) => console.log(error));
  };
};

export const loadHouseHold = (arrayTopicsValue, selectedVillage, filters) => {
  return function (dispatch) {
    var villageString = "";
    var params = "";
    selectedVillage.map((e) => {
      villageString = villageString + "&villageId=" + e.target.properties.uuid;
    });
    filters.map((e) => {
      params = params + "&" + e.key + "=" + e.value;
    });

    let url;
    if (arrayTopicsValue == "Household") {
      url = `${constant.ApiUrl}/surveycube-svc/surveycube/filter?surveyType=HOUSEHOLD&topics=SES${villageString}${params}`;
    } else if (arrayTopicsValue == "Individual") {
      url = `${constant.ApiUrl}/surveycube-svc/surveycube/filter?surveyType=INDIVIDUAL&topics=SES${villageString}${params}`;
    }
    fetch(url, serviceHeaders.getRequestOptions)
      .then((subResp) => subResp.json())
      .then((subResp) => {
        dispatch(getHouseHolds(subResp.data));
      })
      .catch((error) => console.log(error));
  };
};

export const loadEMRData = (selectedVillage, selectEMR) => {
  return function (dispatch) {
    var villageString = "";
    var params = "";

    selectedVillage.map((e) => {
      villageString = villageString + "&villageId=" + e.target.properties.uuid;
    });
    selectEMR.map((e) => {
      params = params + e.name + "=" + e.value;
    });

    fetch(
      `${
        constant.ApiUrl
      }/surveycube-svc/surveycube/filter?surveyType=EMRPATEINT&topics=EMRPATEINT${villageString}${params.toString()}`,
      serviceHeaders.getRequestOptions
    )
      .then((subResp) => subResp.json())
      .then((subResp) => {
        dispatch(getEMRData(subResp.data));
      })
      .catch((error) => console.log(error));
  };
};
// export const loadIndividual = (requestOptions, selectedVillage, filters) => {
//   return function (dispatch) {
//     var villageString = "";
//     var params = "";

//     selectedVillage.map((e) => {
//       villageString = villageString + "&villageId=" + e.target.properties.uuid;
//     });
//     filters.map((e) => {
//       params = params + "&" + e.key + "=" + e.value;
//     });

//     fetch(
//       `${constant.ApiUrl}/surveycube-svc/surveycube/filter?surveyType=INDIVIDUAL&topics=SES${villageString}${params}`,
//       requestOptions
//     )
//       .then((subResp) => subResp.json())
//       .then((subResp) => {
//         dispatch(getIndividual(subResp.data));
//       })
//       .catch((error) => console.log(error));
//   };
// };

export const loadPlaces = (requestOptions) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter`,
      requestOptions
    )
      .then((subResp) => subResp.json())
      .then((subResp) => {
        dispatch(getPlaces(subResp.content));
      })
      .catch((error) => console.log(error));
  };
};
// Service call for GIS Mapping

// Service call for ViewModalPopUp
export const loadPrevDataMO = (prevUhId, modalType) => {
  return function (dispatch) {
    let url;
    if (modalType == "Chief") {
      url = `${constant.ApiUrl}/MedicalConditions/filter?page=1&size=100000&UHId=${prevUhId}`;
    } else if (modalType == "Illness") {
      url = `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=100000&type=Illness&UHId=${prevUhId}`;
    } else if (modalType == "vitals") {
      url = `${constant.ApiUrl}/vitalsigns/filter?UHId=${prevUhId}&page=1`;
    } else if (modalType == "physical") {
      url = `${constant.ApiUrl}/Observations/filter?page=1&size=100000&type=Physical Examination&UHId=${prevUhId}`;
    } else if (modalType == "provisional") {
      url = `${constant.ApiUrl}/Observations/filter?page=1&size=100000&type=Clinical Diagnosis&UHId=${prevUhId}`;
    } else if (modalType == "medical") {
      url = `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=100000&type=Medical&UHId=${prevUhId}`;
    } else if (modalType == "surgical") {
      url = `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=100000&type=Surgical&UHId=${prevUhId}`;
    } else if (modalType == "family") {
      url = `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=100000&type=Family&UHId=${prevUhId}`;
    } else if (modalType == "social") {
      url = `${constant.ApiUrl}/familymemberhistory/filter?page=1&size=100000&type=Social&UHId=${prevUhId}`;
    } else if (modalType == "prescription") {
      url = `${constant.ApiUrl}/prescriptions/filter?page=1&size=100000&UHId=${prevUhId}`;
    } else if (modalType == "investigation") {
      url = `${constant.ApiUrl}/lab-orders-svc/laborders/filter?page=0&size=100000&uhId=${prevUhId}`;
    } else if (modalType == "allergy") {
      url = `${constant.ApiUrl}/allergies/filter?page=1&size=100000&UHId=${prevUhId}`;
    }
    fetch(url, serviceHeaders.getRequestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (modalType == "investigation") {
          console.log(resp.content, "subResp.content");
          dispatch(getPrevDataMO(resp.content));
        } else {
          dispatch(getPrevDataMO(resp.data));
        }
      })
      .catch((error) => console.log(error));
  };
};
//eSanjeevani

// export const eSanjeevaniApi = () => {
//   fetch(
//     "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/esanjeevani/api/v1/Patient",
//     requestOptions
//   )
//     .then((response) => response.json())
//     .then((result) => {
//       console.log(result.message);
//       dispatch(getEsanjeevani(result));
//       //  alert(result.message);

//       //  ssoESanjeevani();
//     })
//     .catch((error) => console.log("error", error));
// };
