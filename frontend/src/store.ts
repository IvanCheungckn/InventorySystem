import { createStore, combineReducers, compose, applyMiddleware } from 'redux'
import thunk, { ThunkDispatch as OldThunkDispatch } from 'redux-thunk';
import { ProductState, productsReducer } from './redux/product/reducer';
import { ProductActions } from './redux/product/actions';
import { locationReducer, LocationState } from './redux/location/reducer';
import { LocationsAction } from './redux/location/actions';



declare global {
  /* tslint:disable:interface-name */
  interface Window {
    __REDUX_DEVTOOLS_EXTENSION_COMPOSE__: any
  }
}
// export type RootAction = QuestionsActions|RoomsActions|AuthActions;

export type RootAction = ProductActions | LocationsAction; // this

export type ThunkDispatch = OldThunkDispatch<RootState, null, RootAction>

export interface RootState {
    product: ProductState,
    location: LocationState
}

const reducer = combineReducers<RootState>({
    product: productsReducer,
    location: locationReducer
})

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

export const store = createStore(reducer,
  composeEnhancers(
    applyMiddleware(thunk)
  ));
