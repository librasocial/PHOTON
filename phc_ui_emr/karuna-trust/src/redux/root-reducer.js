import { combineReducers } from "redux";
import formUtiltyReducers from "./formUtilityReducer";
import encounterReducers from "./encounterReducer";
import usersReducers from "./reducer";
import phcReducers from "./phcReducers";
import abhaReducers from "./AdminReducer";
import WSReducer from "./WSReducer";

const rootReducer = combineReducers({
  data: usersReducers,
  formData: formUtiltyReducers,
  visitData: encounterReducers,
  phcData: phcReducers,
  abhaData: abhaReducers,
  wsAbhaData: WSReducer,
});

export default rootReducer;
