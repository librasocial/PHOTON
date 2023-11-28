import * as types from "./encounterActionTypes";
import * as constant from "../components/ConstUrl/constant";
import * as serviceHeaders from "../components/ConstUrl/serviceHeaders";

const getSerchedPatient = (encounterData) => ({
  type: types.Search_Patient_Encounter,
  payload: encounterData,
});
const getSetPageCount = (pageCountSearch) => ({
  type: types.Search_Patient_PageCount,
  payload: pageCountSearch,
});

export const loadSerchedPatient = (
  searchtypevaltype,
  searchval,
  pagenumber
) => {
  return function (dispatch) {
    fetch(
      `${constant.ApiUrl}/patients/search?fieldName=${searchtypevaltype}&value=` +
        searchval +
        `&page=` +
        pagenumber +
        `&size=1000`,
      serviceHeaders.getRequestOptions
    )
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp) {
          dispatch(getSerchedPatient(resp["data"]));
          if (resp.meta) {
            dispatch(getSetPageCount(Math.ceil(resp.meta?.totalElements / 25)));
          }
        }
      })
      .catch((error) => console.log(error));
  };
};
