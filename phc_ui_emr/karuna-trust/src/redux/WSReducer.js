import * as types from "./WSActionType";

const initialState = {
    fetchModes: [],
    loading: true,
}

const WSReducer = (state = initialState, action) => {
    switch (action.type) {
        case types.Get_Fetch_MOdes:
            return {
                ...state,
                fetchModes: action.payload,
                loading: false,
            };

        default:
            return state;
    }
}
export default WSReducer;