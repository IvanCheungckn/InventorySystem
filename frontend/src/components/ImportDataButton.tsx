import React, { useState } from 'react'
import { Menu, Dropdown } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import { Upload, message, Button } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import './ImportDataButton.scss'
import { CreateProductModal } from './CreateProductModal';
import { useDispatch } from 'react-redux';
import { fetchAllProducts } from '../redux/product/thunk';

export default function ImportDataButton() {
    const [showModal, setShowModal] = useState(false);
    const dispatch = useDispatch();

    const newProductProps = {
        name: 'file',
        action: `${process.env.REACT_APP_BACKEND_URL}/api/stock`,
        onChange(info:any) {
          if (info.file.response?.success === true){
            dispatch(fetchAllProducts())
          }
          if (info.file.response?.success === false){
            window.alert(info.file.response.message)
          }
          if (info.file.status === 'done') {
            message.success(`${info.file.name} file uploaded successfully`);
          } else if (info.file.status === 'error') {
            message.error(`${info.file.name} file upload failed.`);
          }
        },
      };
    const menu = (
        <Menu>
          <Menu.Item>
            <Button onClick={()=>setShowModal(true)}>
                Create Product
            </Button>
          </Menu.Item>
          <Menu.Item>
            <Upload {...newProductProps}>
                <Button>
                <UploadOutlined /> Store Inventory Quantity of Products / Update Products by CSV
                </Button>
            </Upload>
          </Menu.Item>
        </Menu>
      );
      
    return (
        <>
            <Dropdown overlay={menu}>
                <button className="ant-dropdown-link dropdown-btn"  onClick={e => e.preventDefault()}>
                New <DownOutlined />
                </button>
            </Dropdown>
            <CreateProductModal showModal={showModal} setShowModal={()=>setShowModal(true)} setCloseModal={()=>setShowModal(false)}/>
        </>

    )
}
