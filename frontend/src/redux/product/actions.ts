import { IProduct } from "../../models/IProduct"
import { IStock } from "../../models/IStock"

// action creator
export function loadProductsAction(products: IProduct[]) {
    return {
        type: '@@PRODUCT/LOAD_PRODUCTS' as '@@PRODUCT/LOAD_PRODUCTS',
        products
    }
}

export function loadCreatedProductAction(product: IProduct) {
    return {
        type: '@@PRODUCT/LOAD_CREATED_PRODUCTS' as '@@PRODUCT/LOAD_CREATED_PRODUCTS',
        product
    }
}
export function updateProductStockAction(stocks: IStock[], productCode:string) {
    return {
        type: '@@PRODUCT/UPDATE_PRODUCTS_STOCK' as '@@PRODUCT/UPDATE_PRODUCTS_STOCK',
        stocks,
        productCode
    }
}
export function deleteProductAction(productCode:string) {
    return {
        type: '@@PRODUCT/DELETE_PRODUCT' as '@@PRODUCT/DELETE_PRODUCT',
        productCode
    }
}

export type ProductActions = ReturnType<typeof loadProductsAction> 
                            | ReturnType<typeof loadCreatedProductAction> 
                            | ReturnType<typeof updateProductStockAction>
                            | ReturnType<typeof deleteProductAction>