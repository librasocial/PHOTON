import * as types from "./encounterActionTypes";

const initialState = {
  encounterData: [],
  pageCountSearch: "",
  loading: true,
};

const encounterReducers = (state = initialState, action) => {
  switch (action.type) {
    case types.Search_Patient_Encounter:
      return {
        ...state,
        encounterData: action.payload,
        loading: false,
      };
    case types.Search_Patient_PageCount:
      return {
        ...state,
        pageCountSearch: action.payload,
        loading: false,
      };
    default:
      return state;
  }
};

export default encounterReducers;
