import React from 'react'
import Modal from 'antd/lib/modal/Modal';
import './Modal.scss'
import { useDispatch } from 'react-redux';
import { deleteProduct } from '../redux/product/thunk';
export const DeleteProductModal:React.FC<{showModal: boolean, setCloseModal: ()=>void, data: {productCode:string, productName:string}}> = (props) => {
      const dispatch = useDispatch();

      const handleOk = (event: any) => {
        dispatch(deleteProduct(props.data.productCode))
        props.setCloseModal();
      };
    
      const handleCancel = (event: any) => {
        props.setCloseModal();

      };

    return (
        <div>
            <Modal
            title="Delete Product"
            visible={props.showModal}
            okText="Delete"
            onOk={handleOk}
            onCancel={handleCancel}
            >
                <p>Are you sure to delete product:</p>
                <p>{props.data.productName}  {props.data.productCode}</p>
            </Modal>
        </div>
    )
}