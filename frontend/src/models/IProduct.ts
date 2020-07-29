import { ILocation } from "./ILocation";
import { IStock } from "./IStock";

export interface IProduct {
    id: number,
    name: string,
    shortName: string,
    code: string,
    weight: number,
    createdAt: Date,
    updatedAt: Date,
    stockList: IStock[]
}

// export interface IProductTable {
//     productCode: string,
//     productName: string,
//     productWeight: number,
//     totalInventory: number,


// }

