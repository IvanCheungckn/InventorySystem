import { ILocation } from "./ILocation";

export interface IStock {
    id: number,
    location: ILocation,
    quantity: number,
    createdAt: Date,
    updatedAt: Date
}

export interface IUpdateStockForm{
    locationName: string;
     qty: number;
}

