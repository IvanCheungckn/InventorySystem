import React from 'react';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import Table from './components/Table';
import { Container, Jumbotron } from 'react-bootstrap';
import ImportDataButton from './components/ImportDataButton';
import './App.scss';
function App() {
  return (
    <div className="App">
      <Container>
      <Jumbotron fluid>
        <Container>
          <h1>Warehouse Inventory System</h1>
        </Container>
      </Jumbotron>
        <div className="app-button-row">
          <ImportDataButton />
        </div>
        <Table />
      </Container>
    </div>
  );
}

export default App;
