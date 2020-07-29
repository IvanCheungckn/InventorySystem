import { ILocation } from "../../models/ILocation";
import { LocationsAction } from "./actions";


export interface LocationState {
    locations: {
        [id: string]: ILocation
    }
}
// immutability

const initialState: LocationState = {
    locations: {},
}
export const locationReducer = /* reducer */ (oldState = initialState, action: LocationsAction) => {
    switch (action.type) {
        case '@@LOCATION/LOAD_LOCATIONS':
            {
                const newLocations = { ...oldState.locations };

                for (let location of action.locations) {
                    newLocations[location.id] = location;
                }

                return {
                    ...oldState,
                    locations: newLocations
                };
            }
        default:
            return oldState;
    }
}
