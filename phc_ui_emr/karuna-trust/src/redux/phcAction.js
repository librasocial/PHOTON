import * as types from "./phcActionType";
import axios from "axios";
import moment from "moment";
import * as Tostify from "../components/ConstUrl/Tostify";
import * as constant from "../components/ConstUrl/constant";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";
import { loadAccessToken } from "./AdminAction";

// calling Action
const createPHC = () => ({
  type: types.Create_Phc,
});
const updatePHC = () => ({
  type: types.Update_Phc,
});
const getPHC = (phcDetails) => ({
  type: types.Get_Phc_Details,
  payload: phcDetails,
});
const getSubCenter = (sunCenterDetails) => ({
  type: types.Get_Sub_Center_Details,
  payload: sunCenterDetails,
});
const getSubcentreVillages = (subcentreVillages) => ({
  type: types.Get_Subcentre_Villages,
  payload: subcentreVillages,
});
const createPHCRel = () => ({
  type: types.Create_PhcRel,
});
const createSubCenterRel = () => ({
  type: types.Create_SubCenterRel,
});
const createSubCenter = () => ({
  type: types.Create_Sub_Center,
});
const updateSubCenter = () => ({
  type: types.Update_Sub_Center,
});
const getAllState = (stateList) => ({
  type: types.Get_All_State,
  payload: stateList,
});
const getAllDistrict = (districtList) => ({
  type: types.Get_All_District,
  payload: districtList,
});
const getAllDistrictForPresent = (districtListForPresent) => ({
  type: types.Get_All_DistrictForPresent,
  payload: districtListForPresent,
});
const getAllDistrictForPermanent = (districtListForPermanent) => ({
  type: types.Get_All_DistrictForPermanent,
  payload: districtListForPermanent,
});
const getAllTaluk = (talukList) => ({
  type: types.Get_All_Taluk,
  payload: talukList,
});
const getAllTalukForPresent = (talukListForPresent) => ({
  type: types.Get_All_TalukForPresent,
  payload: talukListForPresent,
});
const getAllTalukForPermanent = (talukListForPermanent) => ({
  type: types.Get_All_TalukForPermanent,
  payload: talukListForPermanent,
});
const getAllVillage = (villageList) => ({
  type: types.Get_All_Village,
  payload: villageList,
});
const getAllVillageForPresent = (villageListForPresent) => ({
  type: types.Get_All_VillageForPresent,
  payload: villageListForPresent,
});
const getAllVillageForPermanent = (villageListForPermanent) => ({
  type: types.Get_All_VillageForPermanent,
  payload: villageListForPermanent,
});
const getAllGrams = (gramPanchayatList) => ({
  type: types.Get_All_GramPanch,
  payload: gramPanchayatList,
});
const getAllVillGrams = (villPanchayatList) => ({
  type: types.Get_All_Vill_GramPanch,
  payload: villPanchayatList,
});

const createFacilityPHC = () => ({
  type: types.Create_Facility_PHC,
});
const updateFacilityPHC = () => ({
  type: types.Update_Facility_PHC,
});
const createFacilitySC = () => ({
  type: types.Create_Facility_SC,
});
const createFacHeadRel = () => ({
  type: types.Create_Facility_Rel,
});
const createFacHeadRelSC = () => ({
  type: types.Create_Facility_RelSC,
});
const getFacPhc = (facPhcData) => ({
  type: types.Get_Fac_PHC,
  payload: facPhcData,
});
const getFacSC = (facSCData) => ({
  type: types.Get_Fac_SC,
  payload: facSCData,
});

const createStaffMember = () => ({
  type: types.Create_Staff_Member,
});
const createCogAcess = () => ({
  type: types.Create_Cog_User,
});
const updateStaffMember = () => ({
  type: types.Update_Staff_Member,
});
const getStaffMember = (staffDetails) => ({
  type: types.Get_Staff_Member_Details,
  payload: staffDetails,
});
const getAllStaffData = (staffData) => ({
  type: types.Get_All_Staff_Member,
  payload: staffData,
});

const getGramPanchayatPhc = (gramPanchayatListPhc) => ({
  type: types.Get_Phc_GramPanchayat,
  payload: gramPanchayatListPhc,
});

// Calling action for owner and activities
const createProcessOwner = () => ({
  type: types.Create_Owner,
});

const getAllActivityList = (allActivityList) => ({
  type: types.Get_All_Activity,
  payload: allActivityList,
});
const getAssignActivityList = (activityList) => ({
  type: types.Get_Assign_Activity,
  payload: activityList,
});
const getAllProcessList = (allProcessList) => ({
  type: types.Get_All_Process,
  payload: allProcessList,
});

// Calling action for owner and activities

// calling action for acticity
const createActivityPhc = () => ({
  type: types.Create_Activity_Phc,
});
// calling action for acticity

// Action method for assigning owner for process
const getProcessOwner = (assignedOwner) => ({
  type: types.Get_Assign_Owner,
  payload: assignedOwner,
});
// Action method for assigning owner for process
// calling Action

// call services
export const loadCreatePHC = (postResponse, gramPanchayat) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/organization-svc/organizations`, postResponse)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          dispatch(createPHC(resp));
          let phcUuid = resp.uuid;
          if (gramPanchayat.length != 0) {
            for (var i = 0; i < gramPanchayat.length; i++) {
              let relationData = {
                type: "SERVICEDBY",
                source: {
                  type: "GramPanchayat",
                  properties: {
                    uuid: gramPanchayat[i].uuid,
                  },
                },
                target: {
                  type: "Phc",
                  properties: {
                    uuid: phcUuid,
                  },
                },
              };

              var postData = {
                headers: serviceHeaders.myHeaders1,
                method: "POST",
                mode: "cors",
                body: JSON.stringify(relationData),
              };
              fetch(
                `${constant.ApiUrl}/organization-svc/organizations/relationships`,
                postData
              )
                .then((relresp) => relresp.json())
                .then((relresp) => {
                  dispatch(createPHCRel(relresp));
                  Tostify.notifySuccess("Phc created successfully...!");
                });
            }
          }
          dispatch(loadGetPhcDetails(resp.uuid));
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadGetPhcDetails = (phcUuid, setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/${phcUuid}`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (!resp) {
          alert("Somtething went wrong...!");
        } else {
          dispatch(getPHC(resp));
        }
        if (setPageLoader) {
          setPageLoader(false);
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadAllGramPanchayatPhc = (phcUuid) => {
  return function (dispatch) {
    if (phcUuid) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Phc&srcNodeId=${phcUuid}&rel=SERVICEDBY&targetType=GramPanchayat`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getGramPanchayatPhc(resp.content));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadUpdatePHC = (
  phcId,
  postResponse,
  setPageLoader,
  closeModal
) => {
  return function (dispatch) {
    if (phcId) {
      setPageLoader(true);
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/${phcId}`,
        postResponse
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.status == 500) {
            alert("Somtething went wrong...!");
          } else {
            dispatch(updatePHC(resp));
            if (closeModal) {
              closeModal();
            }
            // if (gramPanchayat) {
            //     for (var i = 0; i < gramPanchayat.length; i++) {
            //         let relationData = {
            //             "type": "SERVICEDBY",
            //             "source": {
            //                 "type": "GramPanchayat",
            //                 "properties": {
            //                     "uuid": gramPanchayat[i].uuid
            //                 }
            //             },
            //             "target": {
            //                 "type": "Phc",
            //                 "properties": {
            //                     "uuid": phcId
            //                 }
            //             }
            //         }

            //         var postData = {
            //             headers: serviceHeaders.myHeaders1,
            //             "method": "POST",
            //             "mode": "cors",
            //             body: JSON.stringify(relationData)
            //         }
            //         fetch(`${constant.ApiUrl}/organization-svc/organizations/relationships`, postData)
            //             .then((relresp) => relresp.json())
            //             .then((relresp) => {
            //                 dispatch(createPHCRel(relresp))
            //                 Tostify.notifySuccess("Phc created successfully...!")
            //             })
            //     }
            // }
          }
          dispatch(loadGetPhcDetails(phcId, setPageLoader));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadUploadImage = (facId, imageData) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/getprefetchedurl?bucketKey=${facId}.jpg&assetType=Phc`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        console.log(resp.preSignedUrl, "resp");
        let imageurl = resp.preSignedUrl;

        fetch(imageurl, imageData)
          // .then((data) => data.status
          .then((res1) => res1)
          .then((res1) => {
            console.log(res1, "preSignedUrl");
            // const uploadstatus = res1;
            // if (res1.status === 200) {
            //     setIsuploaded(true);
            // } else {
            //     setIsuploaded(false);
            // }
          });
      });
  };
};

export const loadCreateSubCenter = (
  requestOptions,
  listVillage,
  handleSubCenterClose
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/organization-svc/organizations`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Something went wrong...!");
        } else {
          dispatch(createSubCenter(resp));
          let phcUuid = resp.uuid;
          if (listVillage.length != 0) {
            for (var i = 0; i < listVillage.length; i++) {
              let relationData = {
                type: "SERVICEDAREA",
                source: {
                  type: "Village",
                  properties: {
                    uuid: listVillage[i].uuid,
                  },
                },
                target: {
                  type: "SubCenter",
                  properties: {
                    uuid: phcUuid,
                  },
                },
              };

              var postData = {
                headers: serviceHeaders.myHeaders1,
                method: "POST",
                mode: "cors",
                body: JSON.stringify(relationData),
              };
              fetch(
                `${constant.ApiUrl}/organization-svc/organizations/relationships`,
                postData
              )
                .then((relresp) => relresp.json())
                .then((relresp) => {
                  dispatch(createSubCenterRel(relresp));
                  handleSubCenterClose(false);
                  Tostify.notifySuccess("Subcenter created successfully...!");
                });
            }
          }
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadAllState = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Country&srcNodeId=df2f5468-55ca-4fcd-a2d3-f47639ea6373&rel=CONTAINEDINPLACE&targetType=State`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllState(resp.content));
      })
      .catch((error) => console.log(error));
  };
};

export const loadUpdateSubCenter = (
  subUuid,
  requestOptions,
  listVillage,
  closeModal
) => {
  return function (dispatch) {
    if (subUuid) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/${subUuid}`,
        requestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp.status == 500) {
            alert("Something went wrong...!");
          } else {
            dispatch(updateSubCenter(resp));
            closeModal(false);
            if (listVillage.length != 0) {
              for (var i = 0; i < listVillage.length; i++) {
                let relationData = {
                  type: "SERVICEDAREA",
                  source: {
                    type: "Village",
                    properties: {
                      uuid: listVillage[i].uuid,
                    },
                  },
                  target: {
                    type: "SubCenter",
                    properties: {
                      uuid: subUuid,
                    },
                  },
                };

                var postData = {
                  headers: serviceHeaders.myHeaders1,
                  method: "POST",
                  mode: "cors",
                  body: JSON.stringify(relationData),
                };
                fetch(
                  `${constant.ApiUrl}/organization-svc/organizations/relationships`,
                  postData
                )
                  .then((relresp) => relresp.json())
                  .then((relresp) => {
                    dispatch(createSubCenterRel(relresp));
                    Tostify.notifySuccess("Subcenter created successfully...!");
                  });
              }
            }
          }
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadSubCenterDetails = (subUuid, setRegisterModalShow) => {
  return function (dispatch) {
    if (subUuid != null) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/${subUuid}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getSubCenter(resp));
          if (setRegisterModalShow) {
            dispatch(loadAccessToken(setRegisterModalShow));
          }
        })
        .catch((error) => console.log(error));
    } else {
      dispatch(getSubCenter({}));
    }
  };
};

export const loadSubcentreVillages = (idForUpdate) => {
  return function (dispatch) {
    if (idForUpdate) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${idForUpdate}&rel=SERVICEDAREA&targetType=Village`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getSubcentreVillages(resp.content));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadCreateFacilityPHC = (
  requestOptions,
  sourceData,
  facHeadUuid,
  setPageLoader,
  status
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/organization-svc/organizations`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          let uuidForFac = resp["uuid"];
          var postFacHead = {
            type: "HEADOF",
            source: sourceData,
            target: {
              type: "Facility",
              properties: {
                uuid: uuidForFac,
              },
            },
          };

          var postResponseHead = {
            headers: serviceHeaders.myHeaders1,
            method: "POST",
            mode: "cors",
            body: JSON.stringify(postFacHead),
          };
          if (!status) {
            dispatch(createFacilityPHC(resp));
            fetch(
              `${constant.ApiUrl}/member-svc/members/${facHeadUuid}/relationships`,
              postResponseHead
            )
              .then((respfac) => respfac.json())
              .then((respfac) => {
                dispatch(createFacHeadRel(respfac));
              });
          } else {
            dispatch(createFacilityPHC(resp));
            fetch(
              `${constant.ApiUrl}/member-svc/members/${facHeadUuid}/relationships`,
              postResponseHead
            )
              .then((respfac) => respfac.json())
              .then((respfac) => {
                dispatch(createFacHeadRel(respfac));
                Tostify.notifySuccess("Staff Member Added Successfully...!");
              });
          }
          dispatch(loadAllFacPhc(facHeadUuid, setPageLoader));
        }
      });
  };
};
export const loadCreateFacilitySC = (
  requestOptions,
  sourceData,
  facHeadUuid,
  suCenterId,
  setPageLoader,
  status
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/organization-svc/organizations`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          let uuidForFac = resp["uuid"];
          var postFacHead = {
            type: "HEADOF",
            source: sourceData,
            target: {
              type: "Facility",
              properties: {
                uuid: uuidForFac,
              },
            },
          };

          var postResponseHead = {
            headers: serviceHeaders.myHeaders1,
            method: "POST",
            mode: "cors",
            body: JSON.stringify(postFacHead),
          };
          if (!status) {
            dispatch(createFacilitySC(resp));
            fetch(
              `${constant.ApiUrl}/member-svc/members/${facHeadUuid}/relationships`,
              postResponseHead
            )
              .then((respfac) => respfac.json())
              .then((respfac) => {
                dispatch(createFacHeadRelSC(respfac));
              });
          } else {
            dispatch(createFacilitySC(resp));
            fetch(
              `${constant.ApiUrl}/member-svc/members/${facHeadUuid}/relationships`,
              postResponseHead
            )
              .then((respfac) => respfac.json())
              .then((respfac) => {
                dispatch(createFacHeadRelSC(respfac));
                Tostify.notifySuccess("Staff Member Added Successfully...!");
              });
          }
          dispatch(loadAllFacSC(suCenterId, setPageLoader));
        }
      });
  };
};

export const loadCreateStaffMember = (
  requestOptions,
  loginAccess,
  modalClose
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/member-svc/members`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          modalClose(true);
          dispatch(createStaffMember(resp));
          if (loginAccess.USERNAME && loginAccess.PASSWORD) {
            let staffUUid = resp.uuid;

            var loginAccessData = {
              Username: loginAccess.USERNAME,
              TemporaryPassword: loginAccess.PASSWORD,
              UserAttributes: {
                "custom:membershipId": staffUUid,
              },
            };

            var postResponse = {
              headers: serviceHeaders.myHeaders1,
              method: "POST",
              mode: "cors",
              body: JSON.stringify(loginAccessData),
            };
            fetch(
              `https://dev-api-phcdt.sampoornaswaraj.org/identities`,
              postResponse
            )
              .then((respCog) => respCog.json())
              .then((respCog) => {
                if (respCog == false) {
                  Tostify.notifyFail("user alredy created...!");
                } else {
                  dispatch(createCogAcess(respCog));
                  Tostify.notifySuccess("User Created Successfully...!");
                }
              });
          } else {
            Tostify.notifySuccess("Staff Member Added Successfully...!");
          }
        }
      });
  };
};

export const loadAllFacPhc = (phcuuid, setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Phc&srcNodeId=${phcuuid}&rel=HOSTEDIN&targetType=Facility&size=100`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp) {
          dispatch(getFacPhc(resp.content));
          setPageLoader(false);
        }
      });
  };
};
export const loadAllFacSC = (subUuid, setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=SubCenter&srcNodeId=${subUuid}&rel=HOSTEDIN&targetType=Facility&size=100`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp) {
          dispatch(getFacSC(resp.content));
          setPageLoader(false);
        }
      });
  };
};

export const loadUpdateFac = (
  facId,
  postResponse,
  setPageLoader,
  uuid,
  type,
  setEditFacId
) => {
  return function (dispatch) {
    if (facId) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/${facId}`,
        postResponse
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp) {
            dispatch(updateFacilityPHC(resp));
            if (setEditFacId) {
              setEditFacId("");
            }
            if (type == "Phc") {
              dispatch(loadAllFacPhc(uuid, setPageLoader));
            } else if (type == "Sub-Center") {
              dispatch(loadAllFacSC(uuid, setPageLoader));
            }

            // setPageLoader(false)
          }
        });
    } else {
      alert("facId is missing to update");
    }
  };
};

export const loadAllStaffData = (phcuuid, setPageLoader) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/member-svc/members/relationships/filter?srcType=Phc&srcNodeId=${phcuuid}&rel=EMPLOYEEOF&targetType=Employee&size=25`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp) {
          dispatch(getAllStaffData(resp.content));
          if (setPageLoader) {
            setPageLoader(false);
          }
        }
      });
  };
};

export const loadStaffData = (uuid, setStaffShow) => {
  return function (dispatch) {
    if (uuid != null) {
      fetch(
        `${constant.ApiUrl}/member-svc/members/${uuid}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (resp) {
            dispatch(getStaffMember(resp.content));
            if (setStaffShow) {
              setStaffShow(true);
            }
          }
        });
    } else {
      dispatch(getStaffMember({}));
    }
  };
};

export const loadUpdateStaffMember = (
  uuid,
  requestOptions,
  pageLoader,
  modalClose,
  loginAccess
) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/member-svc/members/${uuid}`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          if (modalClose) {
            modalClose(true);
          }
          dispatch(updateStaffMember(resp));
          if (loginAccess.USERNAME && loginAccess.PASSWORD) {
            let staffUUid = uuid;

            var loginAccessData = {
              Username: loginAccess.USERNAME,
              TemporaryPassword: loginAccess.PASSWORD,
              UserAttributes: {
                "custom:membershipId": staffUUid,
              },
            };
            var postResponse = {
              headers: serviceHeaders.myHeaders1,
              method: "POST",
              mode: "cors",
              body: JSON.stringify(loginAccessData),
            };
            fetch(
              `https://dev-api-phcdt.sampoornaswaraj.org/identities`,
              postResponse
            )
              .then((respCog) => respCog.json())
              .then((respCog) => {
                if (respCog == false) {
                  Tostify.notifyFail("User already created...!");
                } else {
                  dispatch(createCogAcess(respCog));
                  pageLoader(false);
                  Tostify.notifySuccess("User Created Successfully...!");
                }
              });
          } else {
            pageLoader(false);
            Tostify.notifySuccess("Staff Member Updated Successfully...!");
          }
        }
      });
  };
};

export const loadAllDistrict = (stateuuid, addType) => {
  return function (dispatch) {
    if (stateuuid) {
      fetch(
        `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=State&srcNodeId=${stateuuid}&rel=CONTAINEDINPLACE&targetType=District&page=0&size=100`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          if (!addType) {
            dispatch(getAllDistrict(resp.content));
          } else {
            if (addType == "Present") {
              dispatch(getAllDistrictForPresent(resp.content));
            } else if (addType == "Permanent") {
              dispatch(getAllDistrictForPermanent(resp.content));
            }
          }
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadAllTaluk = (distuuid, addType) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=District&srcNodeId=${distuuid}&rel=CONTAINEDINPLACE&targetType=Taluk`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (!addType) {
          dispatch(getAllTaluk(resp.content));
        } else {
          if (addType == "Present") {
            dispatch(getAllTalukForPresent(resp.content));
          } else if (addType == "Permanent") {
            dispatch(getAllTalukForPermanent(resp.content));
          }
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadAllVillage = (talukuuid, addType) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Taluk&srcNodeId=${talukuuid}&rel=CONTAINEDINPLACE&targetType=Village&size=1000`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (!addType) {
          dispatch(getAllVillage(resp.content));
        } else {
          if (addType == "Present") {
            dispatch(getAllVillageForPresent(resp.content));
          } else if (addType == "Permanent") {
            dispatch(getAllVillageForPermanent(resp.content));
          }
        }
      })
      .catch((error) => console.log(error));
  };
};

export const loadAllGrams = (talukuuid) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=Taluk&srcNodeId=${talukuuid}&rel=ADMINISTEREDBY&targetType=GramPanchayat`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllGrams(resp.content));
      })
      .catch((error) => console.log(error));
  };
};

export const loadAllVillGrams = (gramuuid) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/organization-svc/organizations/relationships/filter?srcType=GramPanchayat&srcNodeId=${gramuuid}&rel=GOVERNEDBY&targetType=Village`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllVillGrams(resp.content));
      })
      .catch((error) => console.log(error));
  };
};

// calls service for owner and activity
export const loadCreateOwner = (requestOptions, closingModal) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/permissions-svc/permissions`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          dispatch(createProcessOwner(resp));
          closingModal();
        }
      });
  };
};

export const loadAllActivityList = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/permissions-svc/permissions/relationships/filter?rel=ISPARTOF&srcType=Process&targetType=Activity&size=1000`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllActivityList(resp.content));
      })
      .catch((error) => console.log(error));
  };
};

export const loadAsssignActivityList = (uuid) => {
  return function (dispatch) {
    if (uuid == null) {
      dispatch(getAssignActivityList([]));
    } else {
      fetch(
        `${constant.ApiUrl}/permissions-svc/permissions/relationships/filter?srcType=Employee&rel=HASACCESS&&targetType=Activity&srcNodeId=${uuid}`,
        serviceHeaders.getRequestOptions
      )
        .then((resp) => resp.json())
        .then((resp) => {
          dispatch(getAssignActivityList(resp.content));
        })
        .catch((error) => console.log(error));
    }
  };
};

export const loadAllProcessList = () => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/permissions-svc/permissions/relationships/filter?rel=CAPABILITYOF&srcType=Application&targetType=Process&srcNodeId=90165674-c46b-4c2e-be40-25631b80b8ff`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        dispatch(getAllProcessList(resp.content));
      })
      .catch((error) => console.log(error));
  };
};
// calls service for owner and activity

// create activity for phc
export const loadAssignActPhc = (postResponse, closingModal) => {
  return function (dispatch) {
    fetch(`${constant.ApiUrl}/permissions-svc/permissions`, postResponse)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.status == 500) {
          alert("Somtething went wrong...!");
        } else {
          dispatch(createActivityPhc(resp));
          closingModal();
        }
      });
  };
};
// create activity for phc

// Fetch process owner assigned to process
// export const loadProcessOwner = (processListId) => {
//     return function (dispatch) {
//         if(processListId){
//             let respList = []
//             for(var i = 0; i < processListId.length; i++){
//                 fetch(`${constant.ApiUrl}/permissions-svc/permissions/relationships/filter?rel=OWNEROF&srcType=Process&targetType=Employee&srcNodeId=${processListId[i]}`, serviceHeaders.getRequestOptions)
//                     .then((resp) => resp.json())
//                     .then((resp) => {
//                         if (resp.content[0] != undefined) {
//                             respList.push(resp.content[0])
//                         }
//                     })
//             }

//             dispatch(getProcessOwner1(respList))
//         }
//     }
// }

export const getParticularOwner = (
  processListId,
  center_type,
  setPageLoader
) => {
  return function (dispatch) {
    if (processListId == null) {
      dispatch(getProcessOwner([]));
    } else {
      let orgType;
      if (center_type == "Phc") {
        orgType = "Phc";
      } else if (center_type == "Sub-Center") {
        orgType = "SubCenter";
      }
      let relationData = {
        relationshipType: "OWNEROF",
        sourceType: "Process",
        sourceProperties: {
          uuid: processListId,
        },
        relationshipProperties: {
          orgType: orgType,
        },
        targetType: "Employee",
        stepCount: 1,
      };

      var postData = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        mode: "cors",
        body: JSON.stringify(relationData),
      };
      fetch(
        `${constant.ApiUrl}/permissions-svc/permissions/relationships/filter`,
        postData
      )
        .then((relresp) => relresp.json())
        .then((relresp) => {
          setPageLoader(false);
          dispatch(getProcessOwner(relresp.content));
        });
    }
  };
};
// Fetch process owner assigned to process

// call services
