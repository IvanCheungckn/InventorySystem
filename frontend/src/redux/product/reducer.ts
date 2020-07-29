import { IProduct } from "../../models/IProduct";
import { ProductActions } from "./actions";

export interface ProductState {
    products: {
        [code: string]: IProduct
    }
}
// immutability

const initialState: ProductState = {
    products: {},
}
export const productsReducer = /* reducer */ (oldState = initialState, action: ProductActions) => {
    switch (action.type) {
        case '@@PRODUCT/LOAD_PRODUCTS':
            {
                const newProducts = { ...oldState.products };

                for (let product of action.products) {
                    newProducts[product.code] = product;
                }

                return {
                    ...oldState,
                    products: newProducts
                };
            }
        case '@@PRODUCT/LOAD_CREATED_PRODUCTS':
            {
                const newProducts = { ...oldState.products };
                
                newProducts[action.product.code] = action.product;
            
                return {
                    ...oldState,
                    products: newProducts
                };
            }
        case '@@PRODUCT/DELETE_PRODUCT':
            {
                const newProducts = { ...oldState.products };
                
                delete newProducts[action.productCode]

                return {
                    ...oldState,
                    products: newProducts
                };
            }
        default:
            return oldState;
    }
}
