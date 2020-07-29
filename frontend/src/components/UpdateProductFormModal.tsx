import React from 'react'
import Modal from 'antd/lib/modal/Modal';
import {Row, Col } from 'react-bootstrap';
import { useFormState } from 'react-use-form-state';
import './Modal.scss'
import { useDispatch, useSelector } from 'react-redux';
import { updateProductStock } from '../redux/product/thunk';
import { RootState } from '../store';
import { IUpdateStockForm } from '../models/IStock';
export const UpdateProductFormModal:React.FC<{showModal: boolean, setCloseModal: ()=>void, productCode: string|null}> = (props) => {
      const dispatch = useDispatch();

      const products = useSelector((state:RootState)=> state.product.products)
      const locations = useSelector((state:RootState)=> state.location.locations)
      const [formState, { label, number }] = useFormState();
      if (!props.productCode){
        return <div></div>
      }
      const product = products[props.productCode!]
    
      const handleCancel = (event: any) => {
        props.setCloseModal();
      };
      const handleSubmit = (event:any) =>{
        event.preventDefault();
        const updateForm:IUpdateStockForm[] = []
        for (const location of Object.values(locations)){
          const locationName = location.name
          const qty = formState.values[location.name]
          updateForm.push({locationName, qty})
        }
        dispatch(updateProductStock(updateForm, props.productCode!))
        handleCancel(event);
        formState.clear();
      }
      
    return (
        <div>
            <Modal
            title="Transfer Product"
            visible={props.showModal}
            cancelButtonProps={{ style: { display: 'none' } }}
            okButtonProps={{ style: { display: 'none' } }}
            // onOk={handleOk}
            onCancel={handleCancel}
            >
                <Row><h4>{product.name} ({product.code})</h4></Row>
                <Row>
                  <Col><h6>Warehouse Location</h6></Col>
                  <Col><h6>Current Inventory</h6></Col>
                  <Col><h6>Proposed Inventory</h6></Col>
                </Row>
                
                
                <form onSubmit={handleSubmit}>
                    {Object.keys(locations).map(location=>{
                      return (
                        <Row className="modal-row" key={location}>
                          <Col><label {...label(locations[location].name)}>{locations[location].name}</label></Col>
                          <Col>{product.stockList.find(stock=>stock.location.name===locations[location].name)?product.stockList.find(stock=>stock.location.name===locations[location].name)?.quantity:0}</Col>
                          <Col><input {...number(locations[location].name)} required/></Col>
                        </Row>
                      )
                    })}
                    <Row className="d-flex justify-content-around"><button>Submit</button></Row>
                    
                </form>
                
            </Modal>
        </div>
    )
}