import { ThunkDispatch, RootState } from "../../store";
import { loadProductsAction, loadCreatedProductAction, deleteProductAction } from "./actions";
import { IUpdateStockForm } from "../../models/IStock";


// Thunk Action
export function fetchAllProducts() {
    return async (dispatch: ThunkDispatch, getState:()=>RootState) => {
        try {
            const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/product`)
            const result = await res.json();
            if (result.success) {
                dispatch(loadProductsAction(result.message));
            } else {
                window.alert(result.message);
            }
        } catch (e) {
            window.alert(e.message);
        }
    }
}

export function createProduct(productName: string, productWeight: number) {
    return async (dispatch: ThunkDispatch, getState:()=>RootState) => {
        try {
            const data = new FormData();
            data.append("productName", productName)
            data.append("productWeight", productWeight+'')
            const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/product`, {
                method: 'POST',
                body: data
                })        
            const result = await res.json();
            if (result.success) {
                dispatch(loadCreatedProductAction(result.message));
            } else {
                window.alert(result.message);
            }
        } catch (e) {
            window.alert(e.message);
        }
    }
}

export function updateProductStock(updateForm: IUpdateStockForm[], productCode: string) {
    return async (dispatch: ThunkDispatch, getState:()=>RootState) => {
        try {
            // const data = new FormData();
            // for (const key of Object.keys(updateForm)){
            //     data.append("productName", productName)
            //     data.append("productWeight", productWeight+'')
            // }
            const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/stock/${productCode}`, {
                headers:{
                    "Content-Type": "application/json"
                },
                method: 'PUT',
                body: JSON.stringify(updateForm)
                })        
            const result = await res.json();
            if (result.success) {
                // dispatch(updateProductStockAction(result.message, productCode));
                dispatch(fetchAllProducts());
            } else {
                window.alert(result.message);
            }
        } catch (e) {
            window.alert(e.message);
        }
    }
}

export function deleteProduct(productCode: string) {
    return async (dispatch: ThunkDispatch, getState:()=>RootState) => {
        try {
            const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/api/product/${productCode}`, {
                method: 'DELETE'
                })        
            const result = await res.json();
            if (result.success) {
                dispatch(deleteProductAction(productCode));
            } else {
                window.alert(result.message);
            }
        } catch (e) {
            window.alert(e.message);
        }
    }
}