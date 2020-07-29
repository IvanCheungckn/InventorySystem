import { ThunkDispatch, RootState } from "../../store";
import { loadLocationsAction } from "./actions";


// Thunk Action
export function fetchAllLocation() {
    return async (dispatch: ThunkDispatch, getState:()=>RootState) => {
        try {
            const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/location`)
            const result = await res.json();
            if (result.success) {
                dispatch(loadLocationsAction(result.message));
            } else {
                window.alert(result.message);
            }
        } catch (e) {
            window.alert(e.message);
        }
    }
}