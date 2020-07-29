import { ILocation } from "../../models/ILocation";

// action creator
export function loadLocationsAction(locations: ILocation[]) {
    return {
        type: '@@LOCATION/LOAD_LOCATIONS' as '@@LOCATION/LOAD_LOCATIONS',
        locations
    }
}

export type LocationsAction = ReturnType<typeof loadLocationsAction>