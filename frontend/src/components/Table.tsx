import React, { useEffect, useState } from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store';
import { fetchAllProducts } from '../redux/product/thunk';
import ToolkitProvider, { Search } from 'react-bootstrap-table2-toolkit';
import { Button } from 'react-bootstrap';
import { UpdateProductFormModal } from './UpdateProductFormModal';
import { fetchAllLocation } from '../redux/location/thunks';
import { DeleteProductModal } from './DeleteProductModal';

export default function Table() {
    const dispatch = useDispatch();
    const productsData = useSelector((state: RootState) => state.product.products)
    const [showTransferForm, setShowTransferForm] = useState(false);
    const [showDeleteForm, setShowDeleteForm] = useState(false);
    const [transferFormData, setTransferFormData] = useState(null);
    const [DeleteFormData, setDeleteFormData] = useState({productCode:null, productName:null} as any);
    const products = []
    useEffect(() => {
        dispatch(fetchAllProducts());
        dispatch(fetchAllLocation())
    }, [dispatch])
    for (const productCode in productsData) {
        const totalInventory = productsData[productCode].stockList?.reduce((acc, current) => {
            return acc += current.quantity
        }, 0)
        products.push({
            id: productsData[productCode].id,
            productCode: productCode,
            productName: productsData[productCode].name,
            productWeight: productsData[productCode].weight,
            totalInventory: totalInventory? totalInventory:0
        })
    }

    const deleteFormatter = (cell: any, row: any, rowIndex: number, formatExtraData: any) => {
        return <Button
            variant="info"
            onClick={() => {
                const data:{productCode:string, productName:string} = {} as any
                data["productCode"] = row.productCode
                data["productName"] = row.productName
                setDeleteFormData(data);
                setShowDeleteForm(true);
            }}
        >
            Delete
      </Button>
    }
    const updateQtyFormatter = (cell: any, row: any, rowIndex: number, formatExtraData: any) => {
        return <Button
            variant="outline-warning"
            onClick={() => {
                setTransferFormData(row.productCode);
                setShowTransferForm(true);
            }}
        >
            Transfer Quantity/ Update Quantity
      </Button>
    }
    const { SearchBar } = Search;
    const columns = [{
        dataField: 'productCode',
        text: 'Product Code',
        sort: true
    }, {
        dataField: 'productName',
        text: 'Product Name',
        sort: true
    }, {
        dataField: 'productWeight',
        text: 'Product Weight',
        sort: true
    }, {
        dataField: 'totalInventory',
        text: 'Total Inventory',
        sort: true
    },  {
        dataField: "transferQtyOrUpdate",
        text: "Transfer Quantity / Update Quantity",
        formatter: updateQtyFormatter,
        sort: false
    }, {
        dataField: "delete",
        text: "Delete",
        formatter: deleteFormatter,
        sort: false
    }];

    return (
        <>
        <ToolkitProvider
            bootstrap4
            search
            keyField='id'
            data={products}
            columns={columns}
            
            >
        {(props) => (
                <div>
                    <h3>Input to search:</h3>
                    <SearchBar {...props.searchProps} />
                    <hr />
                    <BootstrapTable
                        {...props.baseProps}
                        hover
                        pagination={paginationFactory({})} 
                    />
                </div>
            )}
        </ToolkitProvider >
        <UpdateProductFormModal showModal={showTransferForm} setCloseModal={()=>setShowTransferForm(false)} productCode={transferFormData}/>
        <DeleteProductModal showModal={showDeleteForm} setCloseModal={()=>setShowDeleteForm(false)} data={DeleteFormData}/>
        </>
    )
}
