import React from 'react'
import Modal from 'antd/lib/modal/Modal';
import { useFormState } from 'react-use-form-state';
import './Modal.scss'
import { useDispatch } from 'react-redux';
import { createProduct } from '../redux/product/thunk';
export const CreateProductModal:React.FC<{showModal: boolean, setShowModal: ()=>void, setCloseModal: ()=>void}> = (props) => {
      const dispatch = useDispatch();
    
      const handleCancel = (event: any) => {
        props.setCloseModal();
      };
      const handleSubmit = (event:any) =>{
        event.preventDefault();
        dispatch(createProduct(formState.values.productName, formState.values.productWeight))
        handleCancel(event);
        formState.clear();
      }
      const [formState, { label, text, number }] = useFormState();

    return (
        <div>
            <Modal
            title="Create Product"
            visible={props.showModal}
            cancelButtonProps={{ style: { display: 'none' } }}
            okButtonProps={{ style: { display: 'none' } }}
            // onOk={handleOk}
            onCancel={handleCancel}
            >
                <form onSubmit={handleSubmit}>
                    <div className="modal-row">
                        <label {...label('productName')}>Product Name</label>
                        <input {...text('productName')} required/>
                    </div>
                    <div className="modal-row">
                        <label {...label('productWeight')}>Product Weight</label>
                        <input {...number('productWeight')} required />
                    </div>
                    <button>Submit</button>
                </form>
                
            </Modal>
        </div>
    )
}